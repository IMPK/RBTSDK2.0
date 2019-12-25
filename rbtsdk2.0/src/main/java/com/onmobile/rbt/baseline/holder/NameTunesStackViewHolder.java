package com.onmobile.rbt.baseline.holder;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.onmobile.rbt.baseline.AppManager;
import com.onmobile.rbt.baseline.http.api_action.dtos.ChartItemDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.rbt.baseline.musicplayback.BaselineMusicPlayer;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.activities.HomeActivity;
import com.onmobile.rbt.baseline.activities.NameTuneSeeAllActivity;
import com.onmobile.rbt.baseline.activities.SearchActivity;
import com.onmobile.rbt.baseline.activities.base.BaseActivity;
import com.onmobile.rbt.baseline.adapter.NameTunesAdapter;
import com.onmobile.rbt.baseline.analytics.AnalyticsConstants;
import com.onmobile.rbt.baseline.application.SharedPrefProvider;
import com.onmobile.rbt.baseline.basecallback.AppBaselineCallback;
import com.onmobile.rbt.baseline.dialog.custom.AppRatingPopup;
import com.onmobile.rbt.baseline.listener.OnBottomSheetChangeListener;
import com.onmobile.rbt.baseline.listener.OnItemClickListener;
import com.onmobile.rbt.baseline.util.AppConstant;
import com.onmobile.rbt.baseline.util.ArtistPickerDialog;
import com.onmobile.rbt.baseline.util.FontUtils;
import com.onmobile.rbt.baseline.util.NameTuneLanguagePickerDialog;
import com.onmobile.rbt.baseline.util.Util;
import com.onmobile.rbt.baseline.util.WidgetUtils;

import java.util.LinkedHashMap;
import java.util.List;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NameTunesStackViewHolder extends StackViewHolder<List<RingBackToneDTO>> implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    private AppCompatEditText mNameTuneEditText;
    private RelativeLayout mCreateNameTuneLayout;
    private AppCompatTextView mCreateNameTune;
    private ContentLoadingProgressBar mProgressbar;
    private TextView mErrorTextView;
    private RelativeLayout mArtistPickerLayout, mLanguagePickerLayout;
    private ArtistPickerDialog mArtistPickerDialog;
    private NameTuneLanguagePickerDialog mLanguagePickerDialog;
    private TextView mVoiceText, mLanguageText, mLanguageCode;
    //private LinearLayout mNameTuneFilterLayout;
    private AppCompatImageButton mMic;

    private List<String> mVoiceList, mLanguageList, mLanguageCodeList;
    private LinkedHashMap<String, String> languagesMap;
    private int mVoiceSelectedIndex, mLanguageSelectedIndex;
    private String mVoiceSelected, mLanguageSelected, mLanguageSelectedCode;

    private FragmentManager mFragmentManager;
    private Context mContext;

    private String searchQuery;

    public NameTunesStackViewHolder(Context context, View root, View loading, View content, FragmentManager fragmentManager) {
        super(context, root, loading, content);
        this.mFragmentManager = fragmentManager;
        mContext = context;

//        mVoiceList = new ArrayList<>();
//        mVoiceList.add("Male");
//        mVoiceList.add("Female");
//        mVoiceSelectedIndex = 0;
//        mVoiceSelected = mVoiceList.get(mVoiceSelectedIndex);
//        mVoiceText.setText(mVoiceSelected);
//
//        languagesMap = AppManager.getInstance().getRbtConnector().getLanguageToDisplay();
//        if (languagesMap != null) {
//            mLanguageList = new ArrayList<>(languagesMap.values());
//            mLanguageCodeList = new ArrayList<>(languagesMap.keySet());
//
//            mLanguageSelectedIndex = 0;
//            mLanguageSelected = mLanguageList.get(mLanguageSelectedIndex);
//            mLanguageSelectedCode = mLanguageCodeList.get(mLanguageSelectedIndex);
//            mLanguageText.setText(mLanguageSelected);
//        }
    }

    @Override
    protected void initViews() {
        if (contentLayout != null) {
            mArtistPickerLayout = contentLayout.findViewById(R.id.artist_picker_layout);
            mLanguagePickerLayout = contentLayout.findViewById(R.id.language_picker_layout);
            mArtistPickerLayout.setOnClickListener(this);
            mLanguagePickerLayout.setOnClickListener(this);

            mProgressbar = contentLayout.findViewById(R.id.pb_loading);
            mErrorTextView = contentLayout.findViewById(R.id.tv_error);
            mVoiceText = contentLayout.findViewById(R.id.voice_text);
            mLanguageText = contentLayout.findViewById(R.id.language_text);
            mCreateNameTune = contentLayout.findViewById(R.id.create_name_tune_btn);
            mCreateNameTune.setOnClickListener(this);
            //mNameTuneFilterLayout = contentLayout.findViewById(R.id.name_tune_filter_layout);
            mMic = contentLayout.findViewById(R.id.mic_btn);
            hideNextButton();

            mMic.setOnClickListener(this);

            setTvBtnNextColor(R.color.create_name_tune_text);
            mRecyclerView = contentLayout.findViewById(R.id.name_tune_recycler_view);
//            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//                @Override
//                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                    super.onScrolled(recyclerView, dx, dy);
//                    if (dy > 0) {
//                        hideKeyboard(mRecyclerView);
//                    }
//                }
//            });

            mNameTuneEditText = contentLayout.findViewById(R.id.name_tune_edit_text);
            FontUtils.setMediumFont(getContext(), mNameTuneEditText);

            mCreateNameTuneLayout = contentLayout.findViewById(R.id.create_name_layout);

        }
    }

    @Override
    protected void bindViews() {
        mNameTuneEditText.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_ACTION_SEARCH) {
                searchQuery = mNameTuneEditText.getText().toString();
                loadNameTunes(searchQuery);
            }
            return false;
        });

    }

    private void loadNameTunes(String searchText) {
        mNameTuneEditText.addTextChangedListener(textWatcher);
        tvBtnNext.setOnClickListener(null);
        if (!TextUtils.isEmpty(searchText) && searchText.trim().length() > AppConstant.QUERY_NAME_TUNES_SEARCH_MIN_CHAR) {
            try {
                player().stopMusic();
            } catch (Exception ignored) {
            }

            Util.hideKeyboard(getContext(), mRecyclerView);
            List<String> languageList = SharedPrefProvider.getInstance(AppManager.getContext()).getUserLanguageCode();

            if (languageList == null || languageList.size() < 1)
                return;

            AppManager.getInstance().getRbtConnector().getNametunes(0, searchText, /*mLanguageSelectedCode*/languageList.get(0), new AppBaselineCallback<ChartItemDTO>() {
                @Override
                public void success(ChartItemDTO result) {
                    contentLayout.setBackgroundResource(R.drawable.name_tune_white);
                    if (result.getItemCount() > 0) {
                        showNextButton();
                        tvBtnNext.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(mContext, NameTuneSeeAllActivity.class);
                                intent.putExtra(AppConstant.KEY_DATA_ITEM, result);
                                intent.putExtra(AppConstant.KEY_DATA_SEARCH_QUERY, searchText);
                                mContext.startActivity(intent);
                            }
                        });
                    } else {
                        NameTunesAdapter nameTunesAdapter = new NameTunesAdapter(getContext(), result.getRingBackToneDTOS(), R.layout.layout_name_tune_item, mItemClickListener);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                        mRecyclerView.setLayoutManager(linearLayoutManager);
                        mRecyclerView.setHasFixedSize(true);
                        //mRecyclerView.setOnTouchListener(listener);
                        mRecyclerView.setNestedScrollingEnabled(false);
                        mRecyclerView.setAdapter(nameTunesAdapter);
                        mProgressbar.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.GONE);
                        String nameTuneNotFoundMessage = String.format(mContext.getString(R.string.name_tune_not_found_message), searchText);
                        mErrorTextView.setText(nameTuneNotFoundMessage);
                        mErrorTextView.setVisibility(View.VISIBLE);
                        mCreateNameTuneLayout.setVisibility(View.VISIBLE);
                        return;
                    }
                    NameTunesAdapter nameTunesAdapter = new NameTunesAdapter(getContext(), result.getRingBackToneDTOS(), R.layout.layout_name_tune_item, mItemClickListener);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                    mRecyclerView.setLayoutManager(linearLayoutManager);
                    mRecyclerView.setHasFixedSize(true);
                    //mRecyclerView.setOnTouchListener(listener);
                    mRecyclerView.setNestedScrollingEnabled(false);
                    mRecyclerView.setAdapter(nameTunesAdapter);
                    mErrorTextView.setVisibility(View.GONE);
                    mProgressbar.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mCreateNameTuneLayout.setVisibility(View.VISIBLE);
                }

                @Override
                public void failure(String errMsg) {
                    mProgressbar.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.GONE);
                    mErrorTextView.setText(errMsg);
                    mErrorTextView.setVisibility(View.VISIBLE);
                }
            });

            //mNameTuneFilterLayout.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.GONE);
            mErrorTextView.setVisibility(View.GONE);
            mProgressbar.setVisibility(View.VISIBLE);
            contentLayout.setBackgroundResource(R.drawable.name_tune_white);
        }
    }


    @Override
    public void bindHolder(List<RingBackToneDTO> data) {
        showContent();
    }

    @Override
    public void unbind() {

    }

    View.OnTouchListener listener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_MOVE
                    ) {
                v.getParent().requestDisallowInterceptTouchEvent(true);

            } else {
                v.getParent().requestDisallowInterceptTouchEvent(false);

            }
            return false;
        }

    };

    BaselineMusicPlayer mPlayer;

    private BaselineMusicPlayer player() {
        if (mPlayer == null) {
            mPlayer = BaselineMusicPlayer.getInstance();
        }
        return mPlayer;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.artist_picker_layout) {
            mArtistPickerDialog = new ArtistPickerDialog(getContext(), mVoiceList, mVoiceSelectedIndex, new ArtistPickerDialog.IArtistPickerListener() {
                @Override
                public void onCancel() {
                    mArtistPickerDialog.dismiss();
                }

                @Override
                public void onDone(int index) {
                    mArtistPickerDialog.dismiss();
                    if (mVoiceSelectedIndex != index) {
                        mVoiceSelectedIndex = index;
                        mVoiceSelected = mVoiceList.get(mVoiceSelectedIndex);
                        mVoiceText.setText(mVoiceSelected);
                    }
                }
            });
            mArtistPickerDialog.show();
        }else if(view.getId() == R.id.language_picker_layout) {
            mLanguagePickerDialog = new NameTuneLanguagePickerDialog(getContext(), mLanguageList, mLanguageSelectedIndex, new NameTuneLanguagePickerDialog.ILanguagePickerListener() {
                @Override
                public void onCancel() {
                    mLanguagePickerDialog.dismiss();
                }

                @Override
                public void onDone(int selectedLanagugeIndex) {
                    mLanguagePickerDialog.dismiss();
                    if (mLanguageSelectedIndex != selectedLanagugeIndex) {
                        mLanguageSelectedIndex = selectedLanagugeIndex;
                        mLanguageSelected = mLanguageList.get(mLanguageSelectedIndex);
                        mLanguageSelectedCode = mLanguageCodeList.get(mLanguageSelectedIndex);
                        mLanguageText.setText(mLanguageSelected);
                    }

                }
            });
            mLanguagePickerDialog.show();
        }else if(view.getId() == R.id.create_name_tune_btn) {
            if (player().isMediaPlaying()) {
                player().stopMusic();
            }
            WidgetUtils.getCreateNameTuneBottomSheet(searchQuery, mLanguageSelected).setCallback(new OnBottomSheetChangeListener() {
                @Override
                public void onShow(DialogInterface dialogInterface) {

                }

                @Override
                public void onDismiss(DialogInterface dialogInterface, boolean success, String message, Object result) {
                }

                @Override
                public void onCancel(DialogInterface dialogInterface, boolean success, String message) {
                }
            }).showSheet(mFragmentManager);
        }else if(view.getId() == R.id.mic_btn) {
            if (player().isMediaPlaying()) {
                player().stopMusic();
            }
            ((BaseActivity) getContext()).openSpeechInput(SearchActivity.REQ_CODE_SPEECH_INPUT, text -> {
                if (mNameTuneEditText != null) {
                    mNameTuneEditText.setText(text);
                    searchQuery = mNameTuneEditText.getText().toString();
                    loadNameTunes(searchQuery);
                }
            });
        }
    }

    private OnItemClickListener<RingBackToneDTO> mItemClickListener = (OnItemClickListener<RingBackToneDTO>) (view, ringBackToneDTO, position, sharedElements) -> {
        if(view.getId() == R.id.name_tune_parent_layout || view.getId() == R.id.tv_set_name_tune)  {
            openSheet(ringBackToneDTO);
        }
    };

    public void openSheet(RingBackToneDTO ringBackToneDTO) {
        if (ringBackToneDTO == null)
            return;
        WidgetUtils.getSetNameTuneBottomSheet(AnalyticsConstants.EVENT_PV_SOURCE_SET_FROM_NAME_TUNE_CARD, ringBackToneDTO).setCallback(new OnBottomSheetChangeListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {

            }

            @Override
            public void onDismiss(DialogInterface dialogInterface, boolean success, String message, Object result) {
                handleBottomSheetResult(success, message);
            }

            @Override
            public void onCancel(DialogInterface dialogInterface, boolean success, String message) {
                handleBottomSheetResult(success, message);
            }
        }).showSheet(mFragmentManager);
    }

    /**
     * Handle callback response of bottom sheet
     *
     * @param success A boolean value that represent success/failure
     * @param message Success/Failure message
     */
    private void handleBottomSheetResult(boolean success, String message) {
        if (success) {
            AppRatingPopup appRatingPopup = new AppRatingPopup(getContext(), () -> {
                if (getContext() instanceof HomeActivity) {
                    return;
                }
                if (success && getContext() != null) {
                    ((BaseActivity) getContext()).redirect(HomeActivity.class, null, true, true);
                } else if (SharedPrefProvider.getInstance(getContext()).isLoggedIn()) {
                    ((BaseActivity) getContext()).redirect(HomeActivity.class, null, true, true);
                }
            });
            appRatingPopup.show();
        } else {
            if (getContext() instanceof HomeActivity) {
                return;
            }
            if (success && getContext() != null) {
                ((BaseActivity) getContext()).redirect(HomeActivity.class, null, true, true);
            } else if (SharedPrefProvider.getInstance(getContext()).isLoggedIn()) {
                ((BaseActivity) getContext()).redirect(HomeActivity.class, null, true, true);
            }
        }
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            mNameTuneEditText.removeTextChangedListener(textWatcher);
            if (player().isMediaPlaying()) {
                player().stopMusic();
            } else {
                try {
                    player().stopMusic();
                } catch (Exception e) {

                }
            }
            mRecyclerView.setAdapter(null);
            mRecyclerView.setVisibility(View.GONE);
            mCreateNameTuneLayout.setVisibility(View.GONE);
            //mNameTuneFilterLayout.setVisibility(View.VISIBLE);
            mErrorTextView.setVisibility(View.GONE);
            mProgressbar.setVisibility(View.GONE);
            hideNextButton();
            contentLayout.setBackgroundResource(R.drawable.name_tunes_card_img);
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    mNameTuneEditText.requestFocus();
                }
            });
        }


        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

}
