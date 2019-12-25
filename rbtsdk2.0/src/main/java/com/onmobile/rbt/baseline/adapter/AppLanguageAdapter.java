package com.onmobile.rbt.baseline.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.onmobile.rbt.baseline.AppManager;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.application.PersistentSharedPrefProvider;
import com.onmobile.rbt.baseline.listener.AppLanguageSelectionListener;

import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Appolla Sreedhar @26/06/2019
 */

public class AppLanguageAdapter extends RecyclerView.Adapter<AppLanguageAdapter.AppLanguageViewHolder>{
    private Context mContext;
    private List<String> mAppLocaleList;
    private AppLanguageSelectionListener mListener;
    private String mAppLocaleCode;
    private String mSelectedLocaleCode;

    public AppLanguageAdapter(Context context, List<String> appLocaleList,
                              AppLanguageSelectionListener listener) {
        mContext = context;
        mAppLocaleList = appLocaleList;
        mListener = listener;
        mAppLocaleCode = PersistentSharedPrefProvider.getInstance(context).getAppLocal();
        if (mAppLocaleCode==null){
            mAppLocaleCode= AppManager.getInstance().getAppLocaleManager(mContext).setAppLocalForDeviceLanguage(AppManager.getContext(),true,false).getLanguage();
        }
        mSelectedLocaleCode = mAppLocaleCode;
    }

    @NonNull
    @Override
    public AppLanguageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.app_language_cell,parent,false);
        return new AppLanguageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppLanguageViewHolder holder, int position) {
        RadioButton checkBox=holder.radioButton;
        TextView textView=holder.localeName;
        RelativeLayout itemClick=holder.itemClick;

        if(mSelectedLocaleCode != null && mSelectedLocaleCode.equalsIgnoreCase(mAppLocaleList.get(position))){
            checkBox.setChecked(true);
        }
        else{
            checkBox.setChecked(false);
        }

        String localeCode= mAppLocaleList.get(position);

        String languageName = (new Locale(localeCode)).getDisplayLanguage();
        switch (localeCode){
            case "en":
                languageName = mContext.getString(R.string.language_en);
                break;
            case "ar":
                languageName = mContext.getString(R.string.language_ar);

        }
        textView.setText(languageName);

        itemClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSelectedLocaleCode = mAppLocaleList.get(position);
                mListener.selectedLanguage(mAppLocaleList.get(position));
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mAppLocaleList.size();
    }

    public class AppLanguageViewHolder extends RecyclerView.ViewHolder {
        private RadioButton radioButton;
        private TextView localeName;
        private RelativeLayout itemClick;
        public AppLanguageViewHolder(View itemView) {
            super(itemView);
            radioButton=itemView.findViewById(R.id.check_box);
            localeName=itemView.findViewById(R.id.locale_code);
            itemClick=itemView.findViewById(R.id.item_click);
        }
    }
}
