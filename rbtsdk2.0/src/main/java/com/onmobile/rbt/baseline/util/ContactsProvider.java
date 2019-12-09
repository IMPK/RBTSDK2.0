package com.onmobile.rbt.baseline.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;
import android.util.Log;

import com.onmobile.rbt.baseline.model.ContactModelDTO;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class ContactsProvider {

    private static ArrayList<ContactModelDTO> mCacheContactList;

    public static ArrayList<ContactModelDTO> getCacheContactList() {
        return mCacheContactList;
    }

    public static void setCacheContactList(ArrayList<ContactModelDTO> mCacheContactList) {
        ContactsProvider.mCacheContactList = mCacheContactList;
    }

    public static ArrayList<ContactModelDTO> getContactList(Context mContext) {
        return getContactList(mContext, true);
    }

    public static ArrayList<ContactModelDTO> getContactList(Context mContext, boolean isCache) {
        if (isCache && getCacheContactList() != null && getCacheContactList().size() > 0) {
            return getCacheContactList();
        }

        if (!PermissionUtil.hasPermission(mContext, PermissionUtil.Permission.CONTACTS))
            return null;

        ArrayList<ContactModelDTO> arrContacts = new ArrayList<>();
        ContactModelDTO contactModelDTO = null;

        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        Cursor cursor = mContext.getContentResolver().query(uri,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER,
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                        ContactsContract.Contacts.HAS_PHONE_NUMBER,
                        ContactsContract.CommonDataKinds.Phone.PHOTO_URI}, null, null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");

        if (cursor != null) {
            try {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    String contactNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                    String val = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                    int num = Integer.parseInt(val);
                    if (contactNumber != null) {
                        if (num > 0 && contactNumber.length() >= 2 && FormateContactNumber(validNumCheck(contactNumber))) {
                            contactModelDTO = new ContactModelDTO();
                            String photo = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI));
                            if (photo != null) {
                                Uri img = Uri.parse(photo);
                                Logger.d("id", "contact_id " + id + " " + name + " " + img + " " + contactNumber);
                                contactModelDTO.setPhotoURI(img + "");
                            }
                            contactModelDTO.setName(name);
                            contactModelDTO.setMobileNumber(validNumCheck(contactNumber));
                            if (!arrContacts.contains(contactModelDTO)) {
                                String mobileNumber = contactModelDTO.getMobileNumber();
                                if (mobileNumber.length() >= 7 && mobileNumber.length() <= 14) {
                                    arrContacts.add(contactModelDTO);
                                }
                            }
                        }
                    }
                    cursor.moveToNext();
                }
            } catch (Exception ignored) {
            } finally {
                cursor.close();
            }
        }
        ArrayList<ContactModelDTO> finalList = duplicates(arrContacts);
        setCacheContactList(finalList);
        return finalList;
    }

    private static ArrayList<ContactModelDTO> duplicates(ArrayList<ContactModelDTO> list) {
        Set<ContactModelDTO> hs = new LinkedHashSet<>();
        hs.addAll(list);
        list.clear();
        list.addAll(hs);
        return list;
    }

    private static String validNumCheck(String num) {
        String ContactNumber = num.replaceAll(" ", "");
        String number = ContactNumber.replaceAll("[^0-9]", "");
        return number;
    }

    private static Boolean FormateContactNumber(String num) {
        String formatedContactNumber = num.replaceAll(" ", "");
        return PhoneNumberUtils.isGlobalPhoneNumber(formatedContactNumber);
    }

}
