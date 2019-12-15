package com.onmobile.rbt.baseline.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.onmobile.baseline.http.api_action.dtos.ChartItemDTO;
import com.onmobile.baseline.http.api_action.dtos.ListOfSongsResponseDTO;
import com.onmobile.baseline.http.api_action.dtos.PlayRuleDTO;
import com.onmobile.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.baseline.http.retrofit_io.APIRequestParameters;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.activities.HomeActivity;
import com.onmobile.rbt.baseline.adapter.ActivityRbtAdapter;
import com.onmobile.rbt.baseline.analytics.AnalyticsConstants;
import com.onmobile.rbt.baseline.application.ApiConfig;
import com.onmobile.rbt.baseline.application.BaselineApplication;
import com.onmobile.rbt.baseline.basecallback.AppBaselineCallback;
import com.onmobile.rbt.baseline.bottomsheet.action.BSLAModel;
import com.onmobile.rbt.baseline.bottomsheet.action.BottomSheetListActionFragment;
import com.onmobile.rbt.baseline.configuration.AppConfigurationValues;
import com.onmobile.rbt.baseline.dialog.AppDialog;
import com.onmobile.rbt.baseline.dialog.custom.AppRatingPopup;
import com.onmobile.rbt.baseline.dialog.listeners.DialogListener;
import com.onmobile.rbt.baseline.event.RBTStatus;
import com.onmobile.rbt.baseline.fragment.base.BaseFragment;
import com.onmobile.rbt.baseline.listener.OnActivityRbtItemClickListener;
import com.onmobile.rbt.baseline.listener.OnBottomSheetChangeListener;
import com.onmobile.rbt.baseline.model.ActivityRbtType;
import com.onmobile.rbt.baseline.model.ContactModelDTO;
import com.onmobile.rbt.baseline.model.UserActivityItemDTO;
import com.onmobile.rbt.baseline.model.UserActivityRbtDTO;
import com.onmobile.rbt.baseline.util.ContactsProvider;
import com.onmobile.rbt.baseline.util.FunkyAnnotation;
import com.onmobile.rbt.baseline.util.PermissionUtil;
import com.onmobile.rbt.baseline.util.Util;
import com.onmobile.rbt.baseline.util.WidgetUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.util.Pair;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class FragmentActRbt extends BaseFragment {

    private RecyclerView mRvContent;
    private ViewGroup mLoadingErrorContainer;
    private ContentLoadingProgressBar mPbLoading;
    private AppCompatTextView mTvLoadingError;
    private AppCompatButton mBtnRetry;

    private ArrayList<UserActivityRbtDTO> mActivityRbtDTOS;
    private ActivityRbtAdapter mActivityRbtAdapter;

    private SimpleDateFormat mSimpleDateFormat;

    private int mHistoryItemOffset;
    private int mHistoryMaxOffset;
    private int mHistoryListCount;
    private ArrayList<ContactModelDTO> mContactList;

    private ProgressDialog mProgressDialog;
    private LinearLayout mEmptyActivityLayout;
    private TextView mGoTOStoreBtn;

    private boolean mIsVisibleToUser;
    private OnActivityRbtItemClickListener mItemClickListener = new OnActivityRbtItemClickListener() {
        @SafeVarargs
        @Override
        public final void onItemClick(View view, UserActivityRbtDTO item, int position, Pair<View, String>... sharedElements) {
            int id = view.getId();
            if (id == R.id.parent_layout) {
                if (/*!item.isHistory() &&*/ (item.getType() == ActivityRbtType.TYPE_PROFILE_TUNES || item.getType() == ActivityRbtType.TYPE_AZAN)) {
                    return;
                }
                showActions(position, item);
            } else if (id == R.id.switch_button) {
                SwitchCompat switchCompat = (SwitchCompat) view;
                boolean delete = !switchCompat.isChecked();
                if (delete && (!item.isHistory() &&
                        (AppConfigurationValues.isSelectionModel() ||
                                (!AppConfigurationValues.isSelectionModel() && !item.getUserActivityItemDTO().isDownloadedOnly())))) {

                    boolean isCheckRequired = true;
                    Object data = item.getUserActivityItemDTO().getData();
                    if (data instanceof RingBackToneDTO) {
                        RingBackToneDTO ringBackToneDTO = (RingBackToneDTO) data;
                        if (ringBackToneDTO.getSubType() == APIRequestParameters.EModeSubType.RINGBACK_AZAN
                                || ringBackToneDTO.getSubType() == APIRequestParameters.EModeSubType.RINGBACK_PROFILE) {
                            isCheckRequired = false;
                        }
                    }

                    if (isCheckRequired && !AppConfigurationValues.isDeleteLastSelection() &&
                            BaselineApplication.getApplication().getRbtConnector().isLastSelection()) {
                        reapplyFieldData(position, item);
                        getRootActivity().showLongSnackBar(getString(R.string.delete_last_selection_not_allowed_msg));
                    } else {
                        deactivatePlan(position, item);
                    }
                } else if (!delete && (item.isHistory() || (!AppConfigurationValues.isSelectionModel() && item.getUserActivityItemDTO().isDownloadedOnly()))) {
                    activatePlan(position, item);
                }
            }
        }
    };
    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            fetchAllContents();
        }
    };
//    private View.OnClickListener mClickListener = v -> fetchAllContents();

    @NonNull
    @Override
    protected String initTag() {
        return FragmentActRbt.class.getSimpleName();
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_home_act_rbt;
    }

    @Override
    protected void unbindExtras(Bundle bundle) {

    }

    @Override
    protected void initComponents() {
        mActivityRbtDTOS = new ArrayList<>();
        mActivityRbtAdapter = new ActivityRbtAdapter(mActivityRbtDTOS, mItemClickListener);
        mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initViews(View view) {
        mRvContent = view.findViewById(R.id.activity_rbt_recycler_view);
        mLoadingErrorContainer = view.findViewById(R.id.container_loading);
        mPbLoading = view.findViewById(R.id.progress_bar_loading);
        mTvLoadingError = view.findViewById(R.id.tv_loading);
        mBtnRetry = view.findViewById(R.id.btn_retry_loading);
        mEmptyActivityLayout = view.findViewById(R.id.empty_activity_parent_layout);
        mGoTOStoreBtn = view.findViewById(R.id.empty_activity_go_to_store);
        mGoTOStoreBtn.setOnClickListener(view1 -> ((HomeActivity) getRootActivity()).changeFragment(FragmentActRbt.this, FragmentHomeStore.class, null));
        mBtnRetry.setOnClickListener(mClickListener);
    }

    @Override
    protected void bindViews(View view) {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupRecyclerView();
        fetchAllContents();
    }

    private void setupRecyclerView() {
        mRvContent.setHasFixedSize(false);
        mRvContent.setItemViewCacheSize(100);
        mRvContent.setItemAnimator(null);
        mRvContent.setLayoutManager(new LinearLayoutManager(getRootActivity()));
        mRvContent.setAdapter(mActivityRbtAdapter);

    }

    private void fetchAllContents() {
        showLoading();
        int oldSize = mActivityRbtDTOS.size();
        mActivityRbtDTOS.clear();
        mHistoryItemOffset = 0;
        mHistoryMaxOffset = -1;
        mHistoryListCount = 0;
        if (oldSize > 0)
            mRvContent.post(() -> mActivityRbtAdapter.notifyDataSetChanged());
        BaselineApplication.getApplication().getRbtConnector().getPlayRules(new AppBaselineCallback<ListOfSongsResponseDTO>() {
            @Override
            public void success(ListOfSongsResponseDTO result) {
                if (!isAdded()) return;
                mActivityRbtAdapter.setLoaded();
                boolean isContactPermissionGranted = PermissionUtil.hasPermission(getFragmentContext(), PermissionUtil.Permission.CONTACTS);
                if ((mContactList != null && isContactPermissionGranted) || !isContactPermissionGranted) {
                    buildFields(result, false);
                    mRvContent.scrollToPosition(0);
                    //mActivityRbtAdapter.setOnLoadMoreListener(mRvContent, FragmentActRbt.this::loadMoreHistory);
                    // if (mActivityRbtDTOS.size() <= 5)
                    mActivityRbtAdapter.setLoaded();
                    loadMoreHistory();
                } else {
                    mActivityRbtAdapter.setLoaded();
                    new FetchPhoneContacts(getRootActivity(), result, true).execute();
                }
            }

            @Override
            public void failure(String errMsg) {
                if (!isAdded()) return;
                //showError(errMsg);
                mActivityRbtAdapter.setLoaded();
                loadMoreHistory();
            }
        });
    }

    private void fetchHistoryContent() {
        BaselineApplication.getApplication().getRbtConnector().getUserHistory(mHistoryItemOffset, new AppBaselineCallback<ListOfSongsResponseDTO>() {
            @Override
            public void success(ListOfSongsResponseDTO result) {
                if (result == null) {
                    removeProgressLoading(true);
                    if (mActivityRbtDTOS.size() == 0) {
                        showEmptyActivity();
                    }

                    return;
                }
                buildFields(result, true);
            }

            @Override
            public void failure(String errMsg) {
                if (!isAdded()) return;
                removeProgressLoading(true);
                if (mActivityRbtDTOS.size() > 0) getRootActivity().showShortToast(errMsg);
                else showError(errMsg);
            }
        });
    }

    private void loadMoreHistory() {
        if (mHistoryMaxOffset == -1 || (mHistoryListCount < mHistoryMaxOffset && !mActivityRbtAdapter.isLoading())) {
            //if (mHistoryItemOffset > 0) {
            addProgressLoading();
            //}
            fetchHistoryContent();
        }
    }

    private void addProgressLoading() {
        int position = mActivityRbtDTOS.size();
        mActivityRbtDTOS.add(new UserActivityRbtDTO());
        mRvContent.post(() -> mActivityRbtAdapter.notifyItemInserted(position));
    }

    private void removeProgressLoading(boolean loaded) {
        int position = mActivityRbtDTOS.size() - 1;
        if (position >= 0) {
            mActivityRbtDTOS.remove(position);
            mRvContent.post(() -> mActivityRbtAdapter.notifyItemRemoved(position));
            if (loaded) mActivityRbtAdapter.setLoaded();
        }
    }

    private void buildFields(ListOfSongsResponseDTO data, boolean isHistory) {
        if (!isAdded()) return;
        if (data != null && (((data.getRingBackToneDTOS() != null && data.getRingBackToneDTOS().size() > 0) || (data.getChartItemDTO() != null && data.getChartItemDTO().size() > 0))
                || ((data.getDownloadedRingBackToneDTOS() != null && data.getDownloadedRingBackToneDTOS().size() > 0) || (data.getDownloadedChartItemDTO() != null && data.getDownloadedChartItemDTO().size() > 0)))) {

            List<ChartItemDTO> shuffleResults = data.getChartItemDTO();
            List<RingBackToneDTO> musicResults = data.getRingBackToneDTOS();
            List<ChartItemDTO> downloadedShuffleResults = data.getDownloadedChartItemDTO();
            List<RingBackToneDTO> downloadedMusicResults = data.getDownloadedRingBackToneDTOS();
            int totalItems = 0;
            if (shuffleResults != null) totalItems += shuffleResults.size();
            if (musicResults != null) totalItems += musicResults.size();
            if (downloadedShuffleResults != null) totalItems += downloadedShuffleResults.size();
            if (downloadedMusicResults != null) totalItems += downloadedMusicResults.size();
            if (totalItems == 0) {
                if (isHistory && mActivityRbtDTOS.size() == 0) {
                    removeProgressLoading(true);
                    showError(getString(R.string.no_active_or_history_found_message));
                }
                return;
            }
            if (isHistory) {
                removeProgressLoading(false);
                if (mHistoryItemOffset == 0) {
                    UserActivityItemDTO userActivityItemDTO1 = new UserActivityItemDTO();
                    userActivityItemDTO1.setTitle(getString(R.string.history));
                    UserActivityRbtDTO tempActivityRbtDT1 = new UserActivityRbtDTO(ActivityRbtType.TYPE_TITLE_SMALL, userActivityItemDTO1, true);
                    mActivityRbtDTOS.add(tempActivityRbtDT1);
                    mActivityRbtAdapter.setOnLoadMoreListener(mRvContent, FragmentActRbt.this::loadMoreHistory);
                }
                mHistoryItemOffset += ApiConfig.MAX_ITEM_COUNT;
//                if (shuffleResults != null)
//                    mHistoryItemOffset += shuffleResults.size();
            } else {
                UserActivityItemDTO userActivityItemDTO1 = new UserActivityItemDTO();
                userActivityItemDTO1.setTitle(getString(R.string.active));
                UserActivityRbtDTO tempActivityRbtDT1 = new UserActivityRbtDTO(ActivityRbtType.TYPE_TITLE_SMALL, userActivityItemDTO1, false);
                mActivityRbtDTOS.add(tempActivityRbtDT1);

                UserActivityItemDTO userActivityItemDTO2 = new UserActivityItemDTO();
                UserActivityRbtDTO userActivityRbtDTO2 = new UserActivityRbtDTO(ActivityRbtType.TYPE_PERSONALIZED_SHUFFLE, userActivityItemDTO2, false);
                mActivityRbtDTOS.add(userActivityRbtDTO2);
            }

            if (shuffleResults != null) {
                for (ChartItemDTO item : shuffleResults) {
                    UserActivityItemDTO userActivityItemDTO = null;
                    if (APIRequestParameters.EMode.RBTSTATION.value().equalsIgnoreCase(item.getType()) ||
                            APIRequestParameters.EMode.RINGBACK_STATION.value().equalsIgnoreCase(item.getType()) ||
                            APIRequestParameters.EMode.SHUFFLE_LIST.value().equalsIgnoreCase(item.getType()))
                        userActivityItemDTO = getUserActivity(item, isHistory);
                    if (userActivityItemDTO != null) {
                        UserActivityRbtDTO userActivityRbtDTO = new UserActivityRbtDTO(ActivityRbtType.TYPE_MUSIC_SHUFFLES, userActivityItemDTO, isHistory);
                        mActivityRbtDTOS.add(userActivityRbtDTO);
                    }
                }
            }

            if (musicResults != null) {
                for (RingBackToneDTO item : musicResults) {
                    int type = 0;
                    UserActivityItemDTO userActivityItemDTO = null;
                    if (APIRequestParameters.EMode.SONG.value().equals(item.getType()) || APIRequestParameters.EMode.RINGBACK.value().equals(item.getType())) {
                        if (item.getSubType() == null) continue;
                        switch (item.getSubType()) {
                            case RINGBACK_MUSICTUNE:
                                type = ActivityRbtType.TYPE_RINGBACK;
                                break;
                            case RINGBACK_PROFILE:
                                type = ActivityRbtType.TYPE_PROFILE_TUNES;
                                break;
                            case RINGBACK_NAMETUNE:
                                type = ActivityRbtType.TYPE_NAME_TUNES;
                                break;
                            case RINGBACK_AZAN:
                                type = ActivityRbtType.TYPE_AZAN;
                                break;
                            default:
                                continue;
                        }
                        userActivityItemDTO = getUserActivity(item, isHistory);
                    }
                /*else if (APIRequestParameters.EMode.RBTSTATION.value().equals(item.getType()) || APIRequestParameters.EMode.RINGBACK_STATION.value().equals(item.getType())) {
                    type = ActivityRbtType.TYPE_MUSIC_SHUFFLES;
                    ChartItemDTO shuffle = data.getChartItemDTO();
                    if (shuffle == null)
                        continue;
                    userActivityItemDTO = getUserActivity(shuffle, item, isHistory);
                }
                else {
                    continue;
                }*/
                    if (userActivityItemDTO != null) {
                        UserActivityRbtDTO userActivityRbtDTO = new UserActivityRbtDTO(type, userActivityItemDTO, isHistory);
                        mActivityRbtDTOS.add(userActivityRbtDTO);
                    }
                }
            }

            if (downloadedShuffleResults != null) {
                for (ChartItemDTO item : downloadedShuffleResults) {
                    UserActivityItemDTO userActivityItemDTO = null;
                    if (APIRequestParameters.EMode.RBTSTATION.value().equalsIgnoreCase(item.getType()) ||
                            APIRequestParameters.EMode.RINGBACK_STATION.value().equalsIgnoreCase(item.getType()) ||
                            APIRequestParameters.EMode.SHUFFLE_LIST.value().equalsIgnoreCase(item.getType()))
                        userActivityItemDTO = getUserActivity(item, isHistory);
                    if (userActivityItemDTO != null) {
                        UserActivityRbtDTO userActivityRbtDTO = new UserActivityRbtDTO(ActivityRbtType.TYPE_MUSIC_SHUFFLES, userActivityItemDTO, isHistory);
                        mActivityRbtDTOS.add(userActivityRbtDTO);
                    }
                }
            }

            if (downloadedMusicResults != null) {
                for (RingBackToneDTO item : downloadedMusicResults) {
                    int type = 0;
                    UserActivityItemDTO userActivityItemDTO = null;
                    if (APIRequestParameters.EMode.SONG.value().equals(item.getType()) || APIRequestParameters.EMode.RINGBACK.value().equals(item.getType())) {
                        if (item.getSubType() == null) continue;
                        switch (item.getSubType()) {
                            case RINGBACK_MUSICTUNE:
                                type = ActivityRbtType.TYPE_RINGBACK;
                                break;
                            case RINGBACK_PROFILE:
                                type = ActivityRbtType.TYPE_PROFILE_TUNES;
                                break;
                            case RINGBACK_NAMETUNE:
                                type = ActivityRbtType.TYPE_NAME_TUNES;
                                break;
                            case RINGBACK_AZAN:
                                type = ActivityRbtType.TYPE_AZAN;
                                break;
                            default:
                                continue;
                        }
                        userActivityItemDTO = getUserActivity(item, isHistory);
                    }
                /*else if (APIRequestParameters.EMode.RBTSTATION.value().equals(item.getType()) || APIRequestParameters.EMode.RINGBACK_STATION.value().equals(item.getType())) {
                    type = ActivityRbtType.TYPE_MUSIC_SHUFFLES;
                    ChartItemDTO shuffle = data.getChartItemDTO();
                    if (shuffle == null)
                        continue;
                    userActivityItemDTO = getUserActivity(shuffle, item, isHistory);
                }
                else {
                    continue;
                }*/
                    if (userActivityItemDTO != null) {
                        UserActivityRbtDTO userActivityRbtDTO = new UserActivityRbtDTO(type, userActivityItemDTO, isHistory);
                        mActivityRbtDTOS.add(userActivityRbtDTO);
                    }
                }
            }

            mRvContent.post(() -> mActivityRbtAdapter.notifyDataSetChanged());

            if (isHistory) {
                mHistoryListCount += ApiConfig.MAX_ITEM_COUNT;
                //if (shuffleResults != null)
                //mHistoryItemOffset += shuffleResults.size();
                mHistoryMaxOffset = data.getTotalItemCount();
                mActivityRbtAdapter.setLoaded();
            }

            showContent();
        } else {
            if (isHistory) {
                removeProgressLoading(true);
                if (mActivityRbtDTOS.size() < 1)
                    showError(getString(R.string.no_active_or_history_found_message));
            }
        }
    }

    private UserActivityItemDTO getUserActivity(RingBackToneDTO item, boolean isHistory) {
        UserActivityItemDTO userActivityItemDTO;
        String setDate = null;
        String endDate = null;
        if (item.getPlayRuleDTO() != null && item.getPlayRuleDTO().getPlayRuleInfo() != null) {
            try {
                String tempDate = item.getPlayRuleDTO().getPlayRuleInfo().getSetTime();
                Date date = mSimpleDateFormat.parse(tempDate);
                setDate = mSimpleDateFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
                setDate = item.getPlayRuleDTO().getPlayRuleInfo().getSetTime();
            }
            if (isHistory && item.getPlayRuleDTO().getSchedule() != null && item.getPlayRuleDTO().getSchedule().getDateRange() != null)
                endDate = item.getPlayRuleDTO().getSchedule().getDateRange().getEndDate();
        }
        if (isHistory) {
            userActivityItemDTO = new UserActivityItemDTO(item.getPrimaryArtistName(), item.getTrackName(), null, setDate, item.getPrimaryImage(), endDate, item, false);
        } else {
            String callingParty = null;
            if (item.getPlayRuleDTO() != null && item.getPlayRuleDTO().getCallingparty() != null && item.getPlayRuleDTO().getCallingparty().getId() != null && !item.getPlayRuleDTO().getCallingparty().getId().equals("0")) {
                callingParty = item.getPlayRuleDTO().getCallingparty().getId();

                if (PermissionUtil.hasPermission(getFragmentContext(), PermissionUtil.Permission.CONTACTS)) {
                    String phoneNumberToCompare = Util.filterNumber(callingParty);
                    for (ContactModelDTO contactModelDTO : mContactList) {
                        if (contactModelDTO.getMobileNumber().contains(phoneNumberToCompare)) {
                            callingParty = contactModelDTO.getName();
                            break;
                        }

                    }
                }
            }
            userActivityItemDTO = new UserActivityItemDTO(item.getPrimaryArtistName(), item.getTrackName(), callingParty, setDate, item.getPrimaryImage(), item, (item.getPlayRuleDTO() == null && !AppConfigurationValues.isSelectionModel()) ? true : false);
        }
        return userActivityItemDTO;
    }

    private UserActivityItemDTO getUserActivity(ChartItemDTO item, boolean isHistory) {
        UserActivityItemDTO userActivityItemDTO;
        String setDate = null;
        String endDate = null;
        if (item.getPlayRuleDTO() != null && item.getPlayRuleDTO().getPlayRuleInfo() != null) {
            try {
                String tempDate = item.getPlayRuleDTO().getPlayRuleInfo().getSetTime();
                Date date = mSimpleDateFormat.parse(tempDate);
                setDate = mSimpleDateFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
                setDate = item.getPlayRuleDTO().getPlayRuleInfo().getSetTime();
            }
            if (isHistory && item.getPlayRuleDTO().getSchedule() != null && item.getPlayRuleDTO().getSchedule().getDateRange() != null)
                endDate = item.getPlayRuleDTO().getSchedule().getDateRange().getEndDate();
        }
        if (isHistory) {
            userActivityItemDTO = new UserActivityItemDTO(item.getCaption(), item.getCaption(), null, setDate, item.getPrimaryImage(), endDate, item, false);
        } else {
            String callingParty = null;
            if (item.getPlayRuleDTO() != null && item.getPlayRuleDTO().getCallingparty() != null && item.getPlayRuleDTO().getCallingparty().getId() != null && !item.getPlayRuleDTO().getCallingparty().getId().equals("0")) {
                callingParty = item.getPlayRuleDTO().getCallingparty().getId();
                if (PermissionUtil.hasPermission(getFragmentContext(), PermissionUtil.Permission.CONTACTS)) {
                    String phoneNumberToCompare = Util.filterNumber(callingParty);
                    for (ContactModelDTO contactModelDTO : mContactList) {
                        if (contactModelDTO.getMobileNumber().contains(phoneNumberToCompare)) {
                            callingParty = contactModelDTO.getName();
                            break;
                        }
                    }
                }
            }
            userActivityItemDTO = new UserActivityItemDTO(item.getCaption(), item.getCaption(), callingParty, setDate, item.getPrimaryImage(), endDate, item, (item.getPlayRuleDTO() == null && !AppConfigurationValues.isSelectionModel()) ? true : false);
        }
        return userActivityItemDTO;
    }

    private void showContent() {
        mEmptyActivityLayout.setVisibility(View.GONE);
        mRvContent.setVisibility(View.VISIBLE);
        mLoadingErrorContainer.setVisibility(View.GONE);
    }

    private void showLoading() {
        mEmptyActivityLayout.setVisibility(View.GONE);
        mRvContent.setVisibility(View.GONE);
        mLoadingErrorContainer.setVisibility(View.VISIBLE);
        mPbLoading.setVisibility(View.VISIBLE);
        mTvLoadingError.setVisibility(View.GONE);
        mBtnRetry.setVisibility(View.GONE);
    }

    private void showError(String errorMessage) {
        mEmptyActivityLayout.setVisibility(View.GONE);
        mRvContent.setVisibility(View.GONE);
        mLoadingErrorContainer.setVisibility(View.VISIBLE);
        mPbLoading.setVisibility(View.GONE);

        mTvLoadingError.setText(errorMessage);
        mTvLoadingError.setVisibility(View.VISIBLE);
        mBtnRetry.setVisibility(View.VISIBLE);
    }

    private void showEmptyActivity() {
        mRvContent.setVisibility(View.GONE);
        mLoadingErrorContainer.setVisibility(View.GONE);
        mEmptyActivityLayout.setVisibility(View.VISIBLE);
    }

    private void showActions(final int position, final UserActivityRbtDTO item) {
        if (item == null) return;
        int itemType = 0;
        switch (item.getType()) {
            case ActivityRbtType.TYPE_RINGBACK:
                itemType = FunkyAnnotation.TYPE_BS_REG_SET_TUNES;
                break;
            case ActivityRbtType.TYPE_PROFILE_TUNES:
                itemType = FunkyAnnotation.TYPE_BS_REG_PROFILE_TUNES;
                break;
            case ActivityRbtType.TYPE_NAME_TUNES:
                itemType = FunkyAnnotation.TYPE_BS_REG_NAME_TUNES;
                break;
            case ActivityRbtType.TYPE_MUSIC_SHUFFLES:
                itemType = FunkyAnnotation.TYPE_BS_REG_SHUFFLE_TUNES;
                break;
            case ActivityRbtType.TYPE_AZAN:
                itemType = FunkyAnnotation.TYPE_BS_REG_SET_AZAN;
                break;
        }
        final List<BSLAModel> options = new ArrayList<>();
        final BSLAModel delete = new BSLAModel(1, getString(R.string.delete));
        final BSLAModel set = new BSLAModel(2, getString(R.string.set_small));
        final BSLAModel share = new BSLAModel(3, getString(R.string.share));
        /*final BSLAModel videoPreview = new BSLAModel(4, getString(R.string.video_preview));
        final BSLAModel stories = new BSLAModel(5, getString(R.string.stories));
        final BSLAModel ringTones = new BSLAModel(6, getString(R.string.ring_tones));*/
        if (!item.isHistory() && !isPending(item.getUserActivityItemDTO().getData())
                && (AppConfigurationValues.isSelectionModel() || item.getUserActivityItemDTO().isDownloadedOnly()))
            options.add(delete);
        else if (item.isHistory())
            options.add(set);
        if (itemType == FunkyAnnotation.TYPE_BS_REG_SET_TUNES)
            options.add(share);
        else if (itemType == FunkyAnnotation.TYPE_BS_REG_SHUFFLE_TUNES) {
            if (item.getUserActivityItemDTO().getData() != null && item.getUserActivityItemDTO().getData() instanceof ChartItemDTO) {
                ChartItemDTO chartItemDTO = (ChartItemDTO) item.getUserActivityItemDTO().getData();
                if (chartItemDTO != null
                        && (APIRequestParameters.EMode.RINGBACK_STATION.value().equalsIgnoreCase(chartItemDTO.getType())
                        || APIRequestParameters.EMode.RBTSTATION.value().equalsIgnoreCase(chartItemDTO.getType())))
                    options.add(share);
            }
        }
        /*options.add(videoPreview);
        options.add(stories);
        options.add(ringTones);*/
        new BottomSheetListActionFragment(itemType, item, options, (id, label) -> {
            if (id == delete.getId()) {
                if (!AppConfigurationValues.isSelectionModel() && item.getUserActivityItemDTO().isDownloadedOnly()) {
                    deleteSong(position, item);
                } else {
                    deactivatePlan(position, item);
                }
            } else if (id == set.getId()) {
                activatePlan(position, item);
            } /*else if (id == share.getId()) {
                shareContent(item);
            }*/
        }).show(getChildFragmentManager(), getTag());
    }

    private void activatePlan(int position, UserActivityRbtDTO item) {
        switch (item.getType()) {
            case ActivityRbtType.TYPE_RINGBACK:
                openCallertuneBottomSheet(position, item);
                break;
            case ActivityRbtType.TYPE_AZAN:
                openAzaanBottomSheet(position, item);
                break;
            case ActivityRbtType.TYPE_PROFILE_TUNES:
                openProfileTuneBottomSheet(position, item);
                break;
            case ActivityRbtType.TYPE_NAME_TUNES:
                openNameTuneBottomSheet(position, item);
                break;
            case ActivityRbtType.TYPE_MUSIC_SHUFFLES:
                openShuffleBottomSheet(position, item);
                break;
        }
    }

    private void deactivatePlan(int position, UserActivityRbtDTO item) {
        Object data = item.getUserActivityItemDTO().getData();
        if (data != null) {
            Context context = getFragmentContext();
            if (AppConfigurationValues.isSelectionModel()) {
                AppDialog.getAlertDialogWithNoDismiss(context, context.getString(R.string.title_delete_active_plan), false, context.getString(R.string.description_delete_active_plan), false, context.getString(R.string.yes), context.getString(R.string.no), true, false, new DialogListener() {
                    @Override
                    public void PositiveButton(DialogInterface dialog, int id) {
                        doDelete(position, item);
                    }

                    @Override
                    public void NegativeButton(DialogInterface dialog, int id) {
                        reapplyFieldData(position, item);
                    }
                });
            } else {
                doDelete(position, item);
            }
        } else {
            getRootActivity().showLongSnackBar(getString(R.string.something_went_wrong));
        }


    }

    private void doDelete(int position, UserActivityRbtDTO item) {
        Context context = getFragmentContext();
        Object data = item.getUserActivityItemDTO().getData();
        if (mProgressDialog == null)
            mProgressDialog = AppDialog.getProgressDialog((Activity) context, context.getString(R.string.deleting_active_plan_message));
        mProgressDialog.show();
        PlayRuleDTO playRuleDTO = null;
        if (data instanceof RingBackToneDTO) {
            RingBackToneDTO ringBackToneDTO = (RingBackToneDTO) data;
            playRuleDTO = ringBackToneDTO.getPlayRuleDTO();
        } else if (data instanceof ChartItemDTO) {
            ChartItemDTO chartItemDTO = (ChartItemDTO) data;
            playRuleDTO = chartItemDTO.getPlayRuleDTO();
        }
        if (playRuleDTO != null)
            BaselineApplication.getApplication().getRbtConnector().deletePlayRule(playRuleDTO.getId(), new AppBaselineCallback<String>() {
                @Override
                public void success(String result) {
                    if (!isAdded()) return;
                    mProgressDialog.dismiss();
                    fetchAllContents();
                }

                @Override
                public void failure(String errMsg) {
                    if (!isAdded()) return;
                    mProgressDialog.dismiss();
                    reapplyFieldData(position, item);
                    getRootActivity().showLongSnackBar(errMsg);
                }
            });
        else {
            mProgressDialog.dismiss();
            getRootActivity().showLongSnackBar(getString(R.string.something_went_wrong));
        }
    }


    private void deleteSong(int position, UserActivityRbtDTO item) {
        Object data = item.getUserActivityItemDTO().getData();
        if (data != null) {
            Context context = getFragmentContext();
            AppDialog.getAlertDialogWithNoDismiss(context, context.getString(R.string.download_model_delete_song_dialog_title), false, context.getString(R.string.download_model_delete_song_dialog_msg), false, context.getString(R.string.yes), context.getString(R.string.no), true, false, new DialogListener() {
                @Override
                public void PositiveButton(DialogInterface dialog, int id) {
                    if (mProgressDialog == null) {
                        mProgressDialog = AppDialog.getProgressDialog(context, context.getString(R.string.download_model_delete_song_progress));
                        mProgressDialog.setCancelable(false);
                    }
                    mProgressDialog.show();
                    String tuneId = null;
                    String type = null;
                    if (data instanceof RingBackToneDTO) {
                        RingBackToneDTO ringBackToneDTO = (RingBackToneDTO) data;
                        tuneId = ringBackToneDTO.getId();
                        type = ringBackToneDTO.getType();
                    } else if (data instanceof ChartItemDTO) {
                        ChartItemDTO chartItemDTO = (ChartItemDTO) data;
                        tuneId = chartItemDTO.getId() + "";
                        type = chartItemDTO.getType();
                    }

                    if (tuneId != null && type != null) {
                        BaselineApplication.getApplication().getRbtConnector().deletePurchasedRBTRequest(tuneId, type, new AppBaselineCallback<String>() {
                            @Override
                            public void success(String result) {
                                mProgressDialog.dismiss();
                                fetchAllContents();
                            }

                            @Override
                            public void failure(String message) {
                                if (!isAdded()) return;
                                mProgressDialog.dismiss();
                                getRootActivity().showLongSnackBar(message);
                            }
                        });
                    } else {
                        mProgressDialog.dismiss();
                        getRootActivity().showLongSnackBar(getString(R.string.something_went_wrong));
                    }
                }

                @Override
                public void NegativeButton(DialogInterface dialog, int id) {
                    reapplyFieldData(position, item);
                }
            });
        } else {
            getRootActivity().showLongSnackBar(getString(R.string.something_went_wrong));
        }
    }

    private void reapplyFieldData(int position, UserActivityRbtDTO item) {
        mActivityRbtDTOS.set(position, item);
        mRvContent.post(() -> mActivityRbtAdapter.notifyItemChanged(position));
    }

    private void openCallertuneBottomSheet(int position, UserActivityRbtDTO item) {
        Object data = item.getUserActivityItemDTO().getData();
        if (data instanceof RingBackToneDTO) {
            RingBackToneDTO ringBackToneDTO = (RingBackToneDTO) data;
            WidgetUtils.getSetCallerTuneBottomSheet(AnalyticsConstants.EVENT_PV_SOURCE_SET_FROM_ACTIVITY, ringBackToneDTO).setCallback(new OnBottomSheetChangeListener() {
                @Override
                public void onShow(DialogInterface dialogInterface) {

                }

                @Override
                public void onDismiss(DialogInterface dialogInterface, boolean success, String message, Object result) {
                    handleBottomSheetResult(position, item, success, message);
                }

                @Override
                public void onCancel(DialogInterface dialogInterface, boolean success, String message) {
                    handleBottomSheetResult(position, item, success, message);
                }
            }).showSheet(getChildFragmentManager());
        }
    }

    private void openAzaanBottomSheet(int position, UserActivityRbtDTO item) {
        Object data = item.getUserActivityItemDTO().getData();
        if (data instanceof RingBackToneDTO) {
            RingBackToneDTO ringBackToneDTO = (RingBackToneDTO) data;
            WidgetUtils.getSetAzaanBottomSheet(AnalyticsConstants.EVENT_PV_SOURCE_SET_FROM_ACTIVITY, ringBackToneDTO).setCallback(new OnBottomSheetChangeListener() {
                @Override
                public void onShow(DialogInterface dialogInterface) {

                }

                @Override
                public void onDismiss(DialogInterface dialogInterface, boolean success, String message, Object result) {
                    handleBottomSheetResult(position, item, success, message);
                }

                @Override
                public void onCancel(DialogInterface dialogInterface, boolean success, String message) {
                    handleBottomSheetResult(position, item, success, message);
                }
            }).showSheet(getChildFragmentManager());
        }
    }

    private void openProfileTuneBottomSheet(int position, UserActivityRbtDTO item) {
        Object data = item.getUserActivityItemDTO().getData();
        if (data instanceof RingBackToneDTO) {
            RingBackToneDTO ringBackToneDTO = (RingBackToneDTO) data;
            RingBackToneDTO finalRingBackToneDTO = ringBackToneDTO;
            if (ringBackToneDTO.getItems() != null && ringBackToneDTO.getItems().size() > 0) {
                finalRingBackToneDTO = ringBackToneDTO.getItems().get(0);
            }
            WidgetUtils.getSetProfileTuneBottomSheet(AnalyticsConstants.EVENT_PV_SOURCE_SET_FROM_ACTIVITY, finalRingBackToneDTO).setCallback(new OnBottomSheetChangeListener() {
                @Override
                public void onShow(DialogInterface dialogInterface) {

                }

                @Override
                public void onDismiss(DialogInterface dialogInterface, boolean success, String message, Object result) {
                    handleBottomSheetResult(position, item, success, message);
                }

                @Override
                public void onCancel(DialogInterface dialogInterface, boolean success, String message) {
                    handleBottomSheetResult(position, item, success, message);
                }
            }).showSheet(getChildFragmentManager());

            /*getRootActivity().showShortSnackBar(getString(R.string.something_went_wrong));
            reapplyFieldData(position, item);*/
        }
    }

    private void openNameTuneBottomSheet(int position, UserActivityRbtDTO item) {
        Object data = item.getUserActivityItemDTO().getData();
        if (data instanceof RingBackToneDTO) {
            RingBackToneDTO ringBackToneDTO = (RingBackToneDTO) data;
            WidgetUtils.getSetNameTuneBottomSheet(AnalyticsConstants.EVENT_PV_SOURCE_SET_FROM_ACTIVITY, ringBackToneDTO).setCallback(new OnBottomSheetChangeListener() {
                @Override
                public void onShow(DialogInterface dialogInterface) {

                }

                @Override
                public void onDismiss(DialogInterface dialogInterface, boolean success, String message, Object result) {
                    handleBottomSheetResult(position, item, success, message);
                }

                @Override
                public void onCancel(DialogInterface dialogInterface, boolean success, String message) {
                    handleBottomSheetResult(position, item, success, message);
                }
            }).showSheet(getChildFragmentManager());
        }
    }

    private void openShuffleBottomSheet(int position, UserActivityRbtDTO item) {
        Object data = item.getUserActivityItemDTO().getData();
        if (data instanceof ChartItemDTO) {
            ChartItemDTO chartItemDTO = (ChartItemDTO) data;
            WidgetUtils.getSetShuffleBottomSheet(AnalyticsConstants.EVENT_PV_SOURCE_SET_FROM_ACTIVITY, chartItemDTO).setCallback(new OnBottomSheetChangeListener() {
                @Override
                public void onShow(DialogInterface dialogInterface) {

                }

                @Override
                public void onDismiss(DialogInterface dialogInterface, boolean success, String message, Object result) {
                    handleBottomSheetResult(position, item, success, message);
                }

                @Override
                public void onCancel(DialogInterface dialogInterface, boolean success, String message) {
                    handleBottomSheetResult(position, item, success, message);
                }
            }).showSheet(getChildFragmentManager());
        }
    }

    private void handleBottomSheetResult(int position, UserActivityRbtDTO item, boolean success, String message) {
        if (success) {
            AppRatingPopup appRatingPopup = new AppRatingPopup(getRootActivity(), this::fetchAllContents);
            appRatingPopup.show();
        } else {
            reapplyFieldData(position, item);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mIsVisibleToUser = isVisibleToUser;
        if (mIsVisibleToUser) {

            if (mActivityRbtDTOS != null && mActivityRbtDTOS.size() > 1 &&
                    mActivityRbtDTOS.get(1).getType() == ActivityRbtType.TYPE_PERSONALIZED_SHUFFLE) {
                if (mActivityRbtAdapter != null) {
                    try {
                        mRvContent.post(() -> mActivityRbtAdapter.notifyItemChanged(1));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RBTStatus event) {
        if (!mIsVisibleToUser) fetchAllContents();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    class FetchPhoneContacts extends AsyncTask<Void, Void, ArrayList<ContactModelDTO>> {

        private Context context;
        //        private ProgressDialog dialog;
        private WeakReference<Activity> reference;
        private ListOfSongsResponseDTO listOfSongsResponseDTO;
        private boolean isAddLoadMoreListener;

        FetchPhoneContacts(Context context, ListOfSongsResponseDTO listOfSongsResponseDTO, boolean isAddLoadMoreListener) {
            this.context = context;
            reference = new WeakReference<>((Activity) context);
            /*dialog = AppDialog.getProgressDialog(getRootActivity(), getString(R.string.loading_contacts));*/
            this.listOfSongsResponseDTO = listOfSongsResponseDTO;
            this.isAddLoadMoreListener = isAddLoadMoreListener;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //dialog.show();
        }

        @Override
        protected ArrayList<ContactModelDTO> doInBackground(Void... voids) {
            return ContactsProvider.getContactList(context);
        }

        @Override
        protected void onPostExecute(ArrayList<ContactModelDTO> contactList) {
            super.onPostExecute(contactList);

            mContactList = new ArrayList<>();

            Activity activity = reference.get();
            if (activity == null) return;
            //dialog.hide();
            if (contactList == null) return;

            mContactList.addAll(contactList);
            getRootActivity().runOnUiThread(() -> {
                if (mActivityRbtDTOS != null) {
                    mActivityRbtDTOS.clear();
                }
                buildFields(listOfSongsResponseDTO, false);
                if (isAddLoadMoreListener) {
                    //mActivityRbtAdapter.setOnLoadMoreListener(mRvContent, FragmentActRbt.this::loadMoreHistory);
                    //if (mActivityRbtDTOS.size() <= 5)
                    mHistoryItemOffset = 0;
                    mHistoryMaxOffset = -1;
                    mHistoryListCount = 0;
                    loadMoreHistory();
                }
            });
        }
    }

    private boolean isPending(Object obj) {
        if (obj != null) {
            String playRuleId = null;
            if (obj instanceof RingBackToneDTO) {
                playRuleId = ((RingBackToneDTO) obj).getId();
            } else if (obj instanceof ChartItemDTO) {
                playRuleId = String.valueOf(((ChartItemDTO) obj).getId());
            }
            if (!TextUtils.isEmpty(playRuleId)) {
                List<PlayRuleDTO> playRuleDTOList = BaselineApplication.getApplication().getRbtConnector().getPlayRuleById(playRuleId);
                if (playRuleDTOList != null && playRuleDTOList.size() > 0) {
                    for (PlayRuleDTO playRuleDTO :
                            playRuleDTOList) {
                        if (playRuleDTO != null && !TextUtils.isEmpty(playRuleDTO.getStatus())
                                && playRuleDTO.getStatus().
                                equalsIgnoreCase(APIRequestParameters.UserType.ACTIVE.getStatusSecondary())) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private Dialog mTranslucentDialog;

    private void showTranslucentDialog() {
        if (mTranslucentDialog == null)
            mTranslucentDialog = AppDialog.getTranslucentProgressDialog(getRootActivity());
        if (!mTranslucentDialog.isShowing())
            mTranslucentDialog.show();
    }

    private void hideTranslucentDialog() {
        if (mTranslucentDialog != null && mTranslucentDialog.isShowing())
            mTranslucentDialog.dismiss();
    }

}
