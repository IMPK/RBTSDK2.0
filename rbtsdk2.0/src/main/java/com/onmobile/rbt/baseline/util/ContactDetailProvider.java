package com.onmobile.rbt.baseline.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;

import com.onmobile.rbt.baseline.model.ContactModelDTO;

import java.util.List;

public class ContactDetailProvider extends AsyncTask<String, Void, ContactModelDTO> {
    private Context mContext;
    private String mNumber;
    private IContactDetailProvider mListener;

    public ContactDetailProvider(Context context, String number, IContactDetailProvider listener) {
        mContext = context;
        mNumber = number;
        mListener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ContactModelDTO doInBackground(String... params) {
        String contactName = null;
        ContactModelDTO contactdetails = new ContactModelDTO();
        contactdetails.setMobileNumber(mNumber);
        try {
            if (PermissionUtil.hasPermission(mContext, PermissionUtil.Permission.CONTACTS) && mNumber != null) {

                String phoneNumberToCompare = Util.filterNumber(mNumber);

                Uri lookupUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumberToCompare));
                Cursor c = mContext.getContentResolver().query(lookupUri, new String[]{ContactsContract.Data.DISPLAY_NAME, ContactsContract.Data.PHOTO_URI},
                        ContactsContract.PhoneLookup.CONTENT_FILTER_URI + " LIKE ?",
                        new String[]{phoneNumberToCompare}, null);

                if (c != null) {
                    try {
                        if (c.moveToFirst()) {
                            String displayName = c.getString(0);
                            String photoUri = c.getString(1);
                            contactdetails.setName(displayName);
                            contactdetails.setPhotoURI(photoUri);
                        } else {
                            List<ContactModelDTO> contactUsers = ContactsProvider.getContactList(mContext);
                            if (contactUsers != null) {
                                for (ContactModelDTO user : contactUsers) {
                                    if (user.getMobileNumber().contains(phoneNumberToCompare)) {
                                        contactName = user.getName();
                                        contactdetails.setName(contactName);
                                        contactdetails.setPhotoURI(user.getPhotoURI());
                                        break;
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        c.close();

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contactdetails;
    }


    @Override
    protected void onPostExecute(ContactModelDTO contact) {
        super.onPostExecute(contact);
        if (mListener != null) {
            mListener.contactRecieved(contact);
        }
    }


}





