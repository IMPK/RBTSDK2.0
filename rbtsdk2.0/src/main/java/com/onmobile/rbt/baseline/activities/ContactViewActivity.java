package com.onmobile.rbt.baseline.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import com.onmobile.rbt.baseline.AppManager;
import com.onmobile.rbt.baseline.http.api_action.dtos.familyandfriends.ChildOperationResponseDTO;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.activities.base.BaseActivity;
import com.onmobile.rbt.baseline.adapter.ContactsRecyclerAdapter;
import com.onmobile.rbt.baseline.basecallback.AppBaselineCallback;
import com.onmobile.rbt.baseline.dialog.AppDialog;
import com.onmobile.rbt.baseline.model.ContactModelDTO;
import com.onmobile.rbt.baseline.util.AppConstant;
import com.onmobile.rbt.baseline.util.ContactsProvider;
import com.onmobile.rbt.baseline.util.PermissionUtil;
import com.onmobile.rbt.baseline.util.Util;
import com.onmobile.rbt.baseline.widget.RecyclerViewFastScroller;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ContactViewActivity extends BaseActivity implements View.OnClickListener {

    private ArrayList<ContactModelDTO> mContactModelArrayList = new ArrayList<>();
    private ContactsRecyclerAdapter mRecyclerAdapter;
    private RecyclerView mRecyclerView;
    private EditText mEditText;
    private ArrayList<ContactModelDTO> searchUsers = new ArrayList<>();

    private ContactData mContactData;
    private boolean isAddChild = false;
    private ArrayList<ContactsRecyclerAdapter.AdapterData> mAlreadySetData;

    private ArrayList<ContactModelDTO> mContactList = new ArrayList<>();
    private ProgressDialog mProgressDialog;

    @NonNull
    @Override
    protected String initTag() {
        return ContactViewActivity.class.getSimpleName();
    }

    @Override
    protected int initLayout() {
        return R.layout.callers_contact_activity;
    }

    @Override
    protected void unbindExtras(Intent intent) {
        if (intent != null && intent.hasExtra(AppConstant.KEY_DATA_CONTACT)) {
            mContactData = (ContactData) intent.getSerializableExtra(AppConstant.KEY_DATA_CONTACT);
        }

        if (intent != null && intent.hasExtra(AppConstant.KEY_IS_ADD_CHILD)) {
            isAddChild = intent.getBooleanExtra(AppConstant.KEY_IS_ADD_CHILD, false);
        }
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void setupToolbar() {

    }

    @Override
    protected void bindViews() {
        mRecyclerView = findViewById(R.id.recyclerView);
        Button cancel = findViewById(R.id.cancel_contact);
        cancel.setOnClickListener(this);
        Button doneContact = findViewById(R.id.add_contact);
        doneContact.setOnClickListener(this);
        mEditText = findViewById(R.id.edt_tool_search);
    }

    @Override
    protected void onPreOnCreate(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void onPostOnCreate(@Nullable Bundle savedInstanceState) {

        if (mContactData != null && mContactData.getAlreadySetContacts() != null && mContactData.getAlreadySetContacts().size() > 0) {
            mAlreadySetData = new ArrayList<>();
            for (Map.Entry<String, Boolean> entry : mContactData.getAlreadySetContacts().entrySet())
                mAlreadySetData.add(new ContactsRecyclerAdapter.AdapterData(entry.getKey(), entry.getValue()));
        }

        final RecyclerViewFastScroller fastScroller = findViewById(R.id.fastscroller);
        mRecyclerAdapter = new ContactsRecyclerAdapter(ContactViewActivity.this, mContactList, mAlreadySetData);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemViewCacheSize(1000);
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(ContactViewActivity.this, LinearLayoutManager.VERTICAL, false) {
            @Override
            public void onLayoutChildren(final RecyclerView.Recycler recycler, final RecyclerView.State state) {
                super.onLayoutChildren(recycler, state);
                //TODO if the items are filtered, considered hiding the fast scroller here
                final int firstVisibleItemPosition = findFirstVisibleItemPosition();
                if (firstVisibleItemPosition != 0) {
                    // this avoids trying to handle un-needed calls
                    if (firstVisibleItemPosition == -2)
                        //not initialized, or no items shown, so hide fast-scroller
                        fastScroller.setVisibility(View.GONE);
                    return;
                }
                final int lastVisibleItemPosition = findLastVisibleItemPosition();
                int itemsShown = lastVisibleItemPosition - firstVisibleItemPosition + 1;
                //if all items are shown, hide the fast-scroller
                fastScroller.setVisibility(mRecyclerAdapter.getItemCount() > itemsShown ? View.VISIBLE : View.GONE);
            }
        });

        fastScroller.setRecyclerView(mRecyclerView);
        fastScroller.setViewsToUse(R.layout.recycler_view_fast_scroller, R.id.fastscroller_bubble, R.id.fastscroller_handle);

        permissionListener = new IHandlePermissionCallback() {
            @Override
            public void onPermissionGranted() {
                new FetchPhoneContacts(ContactViewActivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }

            @Override
            public void onPermissionDenied() {
                showShortToast(getString(R.string.contact_permission_denied_msg));
            }
        };

        handlePermission(PermissionUtil.RequestCode.CONTACTS, permissionListener, PermissionUtil.Permission.CONTACTS);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //Util.hideKeyboard(ContactViewActivity.this, getWindow().getDecorView().getRootView());
            }
        });
        Util.hideKeyboard(ContactViewActivity.this, mEditText);
        filter();
        clickSeacrh();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.cancel_contact) {
            Util.hideKeyboard(ContactViewActivity.this, getWindow().getDecorView().getRootView());
            finish();
        } else if (view.getId() == R.id.add_contact) {
            Util.hideKeyboard(ContactViewActivity.this, getWindow().getDecorView().getRootView());
            if (mRecyclerAdapter.getSelectedContact() == null) {
                return;
            }
            if (mRecyclerAdapter != null) {
                if (mContactData == null)
                    mContactData = new ContactData();
                if (mRecyclerAdapter.getSelectedContact() != null) {
                    mContactData.setSelectedContact(mRecyclerAdapter.getSelectedContactItem());
                }
                if (mRecyclerAdapter.getAlreadySetData() != null) {
                    Map<String, Boolean> checkedMap = new HashMap<>();
                    for (ContactsRecyclerAdapter.AdapterData data : mRecyclerAdapter.getAlreadySetData())
                        checkedMap.put(data.getContact(), data.isSelected());
                    mContactData.setAlreadySetContacts(checkedMap);
                }
                if (!isAddChild) {
                    Intent output = new Intent();
                    output.putExtra(AppConstant.KEY_DATA_CONTACT, mContactData);
                    setResult(RESULT_OK, output);
                    finish();
                } else {
                    addChild(mContactData);
                }
            } else {
                setResult(RESULT_CANCELED);
                finish();
            }
        }
    }

    class FetchPhoneContacts extends AsyncTask<Void, Void, ArrayList<ContactModelDTO>> {

        private Context context;
        private WeakReference<Activity> reference;

        private ArrayList<ContactModelDTO> originalContactList;

        FetchPhoneContacts(Context context) {
            this.context = context;
            reference = new WeakReference<>((Activity) context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true, getString(R.string.loading_contacts));
        }

        @Override
        protected ArrayList<ContactModelDTO> doInBackground(Void... voids) {
            ArrayList<ContactModelDTO> contactList = ContactsProvider.getContactList(context, false);
            if (contactList != null) {
                originalContactList = new ArrayList<>(contactList);
                if (contactList.size() > 0 && mContactData != null) {
                    if (mContactData.getAlreadySetContacts() != null && mContactData.getAlreadySetContacts().size() > 0) {
                        int filterCount = 0;
                        ArrayList<ContactModelDTO> newContactList = new ArrayList<>(mContactData.getAlreadySetContacts().size());
                        for (ContactModelDTO contactModelDTO : contactList) {
                            if (filterCount == mContactData.getAlreadySetContacts().size())
                                break;
                            if (contactModelDTO != null && !TextUtils.isEmpty(contactModelDTO.getMobileNumber())) {
                                for (String alreadySetNumber : mContactData.getAlreadySetContacts().keySet()) {
                                    if (Util.isMobileNumberEqual(contactModelDTO.getMobileNumber(), alreadySetNumber)) {
                                        newContactList.add(contactModelDTO);
                                        filterCount++;
                                        break;
                                    }
                                }
                            }
                        }
                        contactList.removeAll(newContactList);
                    }
                    if (mContactData.getSelectedContact() != null && !TextUtils.isEmpty(mContactData.getSelectedContact().getMobileNumber())) {
                        int counter = 0;
                        String phoneNumberToCompare = Util.filterNumber(mContactData.getSelectedContact().getMobileNumber());
                        for (ContactModelDTO contactModelDTO : contactList) {
                            if (contactModelDTO != null) {
                                if (contactModelDTO.getMobileNumber().contains(phoneNumberToCompare)) {
                                    mRecyclerAdapter.selectedIndex = counter+1;
                                    break;
                                }
                            }
                            counter++;
                        }
                    }
                }
            }
            return contactList;
        }

        @Override
        protected void onPostExecute(ArrayList<ContactModelDTO> contactList) {
            super.onPostExecute(contactList);
            Activity activity = reference.get();
            if (activity == null)
                return;
            showProgress(false, null);
            if (contactList == null) {
                mRecyclerView.post(() -> mRecyclerAdapter.notifyDataSetChanged());
                return;
            }
            searchUsers.clear();
            searchUsers.addAll(contactList);
            mContactList.clear();
            mContactList.addAll(contactList);
            mRecyclerAdapter.setContactsArrayListOriginal(originalContactList);
            mRecyclerView.post(() -> mRecyclerAdapter.notifyDataSetChanged());
            if (mRecyclerAdapter.selectedIndex > -1) {
                mRecyclerView.postDelayed(() -> mRecyclerView.scrollToPosition(mRecyclerAdapter.selectedIndex), 200);
            }
        }
    }

    private void clickSeacrh() {
        mEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == KeyEvent.KEYCODE_BACK)
                Util.hideKeyboard(ContactViewActivity.this, getWindow().getDecorView().getRootView());
            return false;
        });
    }

    public void filter() {
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String text = mEditText.getText().toString().toLowerCase(Locale.getDefault());
                if (mContactModelArrayList != null) {
                    search(text);
                }

            }

        });
    }

    private void search(String text) {
        mContactModelArrayList.clear();
        if (text.length() > 0) {

            mRecyclerView.setAdapter(null);
            for (ContactModelDTO contactUser : searchUsers) {
                if (contactUser.getName().toLowerCase(Locale.getDefault())
                        .contains(text)) {
                    mContactModelArrayList.add(contactUser);
                }
            }


        } else {
            mContactModelArrayList.addAll(searchUsers);

        }

        mRecyclerAdapter = new ContactsRecyclerAdapter(ContactViewActivity.this, mContactModelArrayList, mAlreadySetData);
        mRecyclerView.setAdapter(mRecyclerAdapter);

    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }

    public static class ContactData implements Serializable {
        private ContactModelDTO selectedContact;
        private Map<String, Boolean> alreadySetContacts;

        public ContactModelDTO getSelectedContact() {
            return selectedContact;
        }

        public void setSelectedContact(ContactModelDTO selectedContact) {
            this.selectedContact = selectedContact;
        }

        public Map<String, Boolean> getAlreadySetContacts() {
            return alreadySetContacts;
        }

        public void setAlreadySetContacts(Map<String, Boolean> alreadySetContacts) {
            this.alreadySetContacts = alreadySetContacts;
        }
    }

    private void addChild(ContactData contactData) {
        String childMsisdn = contactData.getSelectedContact().getMobileNumber();
        showProgress(true, null);
        AppManager.getInstance().getRbtConnector().addChildRequest(childMsisdn, new AppBaselineCallback<ChildOperationResponseDTO>() {
            @Override
            public void success(ChildOperationResponseDTO result) {
                showProgress(false, null);
                Intent output = new Intent();
                output.putExtra(AppConstant.KEY_DATA_CONTACT, mContactData);
                setResult(RESULT_OK, output);
                finish();
            }

            @Override
            public void failure(String message) {
                showProgress(false, null);
                showShortSnackBar(message);
            }
        });
    }

    public void showProgress(boolean showProgress, String message) {
        if (mProgressDialog == null) {
            mProgressDialog = AppDialog.getProgressDialog(this);
            mProgressDialog.setCancelable(false);
        }
        if (showProgress) {
            if (TextUtils.isEmpty(message))
                message = getString(R.string.loading_with_dots);
            mProgressDialog.setMessage(message);
            mProgressDialog.show();
        } else {
            mProgressDialog.dismiss();
        }
    }

}
