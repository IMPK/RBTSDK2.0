package com.onmobile.rbt.baseline.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.onmobile.rbt.baseline.http.api_action.dtos.familyandfriends.ChildOperationResponseDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.familyandfriends.GetParentInfoResponseDTO;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.adapter.GiftRbtChildAdapter;
import com.onmobile.rbt.baseline.application.BaselineApplication;
import com.onmobile.rbt.baseline.basecallback.AppBaselineCallback;
import com.onmobile.rbt.baseline.dialog.AppDialog;
import com.onmobile.rbt.baseline.fragment.base.BaseFragment;
import com.onmobile.rbt.baseline.model.ContactModelDTO;
import com.onmobile.rbt.baseline.util.ContactsProvider;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FragmentGiftRbtChild extends BaseFragment implements View.OnClickListener{

    private RecyclerView mGiftRbtChildRecyclerView;
    private TextView mAddFriends;
    private TextView mChildCountInfoText;
    private ProgressDialog mProgressDialog;

    public static FragmentGiftRbtChild newInstance() {
        FragmentGiftRbtChild fragment = new FragmentGiftRbtChild();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    protected String initTag() {
        return FragmentGiftRbtChild.class.getSimpleName();
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_gift_rbt_child;
    }

    @Override
    protected void unbindExtras(Bundle bundle) {
    }

    @Override
    protected void initComponents() {
    }

    @Override
    protected void initViews(View view) {
        mGiftRbtChildRecyclerView = view.findViewById(R.id.gift_child_recycler_view);
        mAddFriends = view.findViewById(R.id.tv_add_friends);
        if(BaselineApplication.getApplication().getRbtConnector().isFriendsAndFamilyActiveUser()) {
            mAddFriends.setEnabled(true);
            mAddFriends.setOnClickListener(this);
        }
        else{
            mAddFriends.setEnabled(false);
            mAddFriends.setOnClickListener(null);
        }


        mChildCountInfoText = view.findViewById(R.id.child_count_info_text);
    }

    @Override
    protected void bindViews(View view) {
        List<GiftChildInfo> childList = new ArrayList<>();

        GetParentInfoResponseDTO parentInfo = BaselineApplication.getApplication().getRbtConnector().getCacheParentInfo();
        if(parentInfo.getCountLeft() > 0 && parentInfo.getCountLeft() <= parentInfo.getTotalCount()){
            String childCountInfoString = String.format(getString(R.string.child_added_count_info),
                    (parentInfo.getTotalCount()-parentInfo.getCountLeft())+"",
                    parentInfo.getTotalCount()+"");
            mChildCountInfoText.setText(childCountInfoString);
        }
        else{
            mAddFriends.setEnabled(false);
            mChildCountInfoText.setText(R.string.max_child_added);
        }

        List<ContactModelDTO> contactUsers = ContactsProvider.getCacheContactList();
        for(int i=0;i<parentInfo.getChilds().size();i++){
            GetParentInfoResponseDTO.ChildInfo childInfo = parentInfo.getChilds().get(i);
            ContactModelDTO contactModelDTO = new ContactModelDTO();
            contactModelDTO.setName(childInfo.getMsisdn());
            contactModelDTO.setMobileNumber(childInfo.getMsisdn());
            if(contactUsers != null) {
                for (ContactModelDTO user : contactUsers) {
                    if (user.getMobileNumber().contains(childInfo.getMsisdn())) {
                        contactModelDTO.setName(user.getName());
                        contactModelDTO.setPhotoURI(user.getPhotoURI());
                        break;
                    }
                }
            }


            childList.add(new GiftChildInfo(childInfo, contactModelDTO));

        }

        GiftRbtChildAdapter giftRbtChildAdapter = new GiftRbtChildAdapter(getRootActivity(), childList, new GiftRbtChildAdapter.IDeleteChildListener() {
            @Override
            public void deleteChild(int position) {
                GiftChildInfo giftChildInfo = childList.get(position);
                deleteChildConfirmDialog(giftChildInfo);
            }
        });
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        mGiftRbtChildRecyclerView.setLayoutManager(llm);
        mGiftRbtChildRecyclerView.setAdapter(giftRbtChildAdapter);

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.tv_add_friends) {
            //((GiftRbtActivity)getActivity()).addFriends();
        }
    }

    public class GiftChildInfo{
        GetParentInfoResponseDTO.ChildInfo childInfo;
        ContactModelDTO contactModelDTO;

        public GiftChildInfo(GetParentInfoResponseDTO.ChildInfo childInfo, ContactModelDTO contactModelDTO) {
            this.childInfo = childInfo;
            this.contactModelDTO = contactModelDTO;
        }

        public GetParentInfoResponseDTO.ChildInfo getChildInfo() {
            return childInfo;
        }

        public void setChildInfo(GetParentInfoResponseDTO.ChildInfo childInfo) {
            this.childInfo = childInfo;
        }

        public ContactModelDTO getContactModelDTO() {
            return contactModelDTO;
        }

        public void setContactModelDTO(ContactModelDTO contactModelDTO) {
            this.contactModelDTO = contactModelDTO;
        }
    }

    public void showProgress(boolean showProgress) {
        if (mProgressDialog == null) {
            mProgressDialog = AppDialog.getProgressDialog(getRootActivity());
            mProgressDialog.setCancelable(false);
        }
        if (showProgress) {
            mProgressDialog.show();
        } else {
            mProgressDialog.dismiss();
        }
    }


    public void deleteChildConfirmDialog(GiftChildInfo giftChildInfo){
        AlertDialog.Builder builder = new AlertDialog.Builder(getRootActivity());
        String dialogMessage = String.format(getString(R.string.delete_child_message), giftChildInfo.contactModelDTO.getName());
        builder.setMessage(dialogMessage)
                .setPositiveButton(R.string.delete_child_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        //deleteChild(giftChildInfo);
                    }
                })
                .setNegativeButton(R.string.delete_child_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

}
