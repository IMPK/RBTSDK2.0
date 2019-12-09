package com.onmobile.baseline.http.api_action.dtos.userjourneynotifi;

import android.content.pm.PackageInfo;
import android.os.Build;
import android.text.TextUtils;

import com.onmobile.baseline.http.api_action.UserJourneyPlayerIdServerSyncRequest;
import com.onmobile.baseline.http.basecallback.BaselineCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hitesh.p on 12/21/2018.
 */

public class ServerSyncHelper {

    public static void syncBasicDataWithServer(PackageInfo pInfo, String oneSignalId, String firebaseId, String cleverTapId, List<String> language, String user_id, BaselineCallback<List<ServerSyncResponseDto>> listBaselineCallback) {

        if (TextUtils.isEmpty(oneSignalId) && TextUtils.isEmpty(firebaseId) && TextUtils.isEmpty(cleverTapId)) {
            listBaselineCallback.failure(null);
            return;
        }

        ServerSyncRequestDto serverSyncRequestDto = new ServerSyncRequestDto();
        ArrayList<ServerSyncRequestDto.ServerSyncSubscriptionDto> serverSyncSubscriptionList = new ArrayList<>();

        if (language != null && language.size() > 0)
            serverSyncRequestDto.setLanguage(language.get(0));
        else
            serverSyncRequestDto.setLanguage(null);

        if (user_id != null) {
            serverSyncRequestDto.setExternalUserId(user_id);
        }

        serverSyncRequestDto.setApplicationVersion("" + pInfo.versionCode);
        serverSyncRequestDto.setApplicationIdentifier(pInfo.packageName);
        serverSyncRequestDto.setApplicationVersionCode(pInfo.versionName);

        if (!TextUtils.isEmpty(oneSignalId)) {
            ServerSyncRequestDto.ServerSyncSubscriptionDto oneSignalServerSyncSubscriptionDto = serverSyncRequestDto.new ServerSyncSubscriptionDto();
            oneSignalServerSyncSubscriptionDto.setEnabled(true);
            oneSignalServerSyncSubscriptionDto.setNotificationMode("native");
            oneSignalServerSyncSubscriptionDto.setProviderName("one_signal");
            oneSignalServerSyncSubscriptionDto.setProviderId(oneSignalId);
            serverSyncSubscriptionList.add(oneSignalServerSyncSubscriptionDto);
        }

        if (!TextUtils.isEmpty(firebaseId)) {
            ServerSyncRequestDto.ServerSyncSubscriptionDto firebaseServerSyncSubscriptionDto = serverSyncRequestDto.new ServerSyncSubscriptionDto();
            firebaseServerSyncSubscriptionDto.setEnabled(true);
            firebaseServerSyncSubscriptionDto.setNotificationMode("native");
            firebaseServerSyncSubscriptionDto.setProviderName("firebase_id");
            firebaseServerSyncSubscriptionDto.setProviderId(firebaseId);
            serverSyncSubscriptionList.add(firebaseServerSyncSubscriptionDto);
        }

        if (!TextUtils.isEmpty(cleverTapId)) {
            ServerSyncRequestDto.ServerSyncSubscriptionDto cleverTapServerSyncSubscriptionDto = serverSyncRequestDto.new ServerSyncSubscriptionDto();
            cleverTapServerSyncSubscriptionDto.setEnabled(true);
            cleverTapServerSyncSubscriptionDto.setNotificationMode("native");
            cleverTapServerSyncSubscriptionDto.setProviderName("clevertap_id");
            cleverTapServerSyncSubscriptionDto.setProviderId(cleverTapId);
            serverSyncSubscriptionList.add(cleverTapServerSyncSubscriptionDto);
        }

        serverSyncRequestDto.setSubscriptions(serverSyncSubscriptionList);
        serverSyncRequestDto.setUserAttributes(getUserAttrsForServerSync());

        new UserJourneyPlayerIdServerSyncRequest(serverSyncRequestDto, listBaselineCallback).execute();
        //ServerSyncRequest.newRequest().body(serverSyncRequestDto).setCallBack(null).build(context).execute();
    }

    private static ArrayList<ServerSyncRequestDto.ServerSyncUserAttributesDto> getUserAttrsForServerSync() {
        ArrayList<ServerSyncRequestDto.ServerSyncUserAttributesDto> serverSyncUserAttributesDtos = new ArrayList<>();
        String[] names = {"os_version", "model", "manufacturer"};
        String[] values = {Build.VERSION.RELEASE, Build.MODEL, Build.MANUFACTURER};

        for (int i = 0; i < names.length; i++) {
            ServerSyncRequestDto.ServerSyncUserAttributesDto serverSyncUserAttributesDto = new ServerSyncRequestDto().new ServerSyncUserAttributesDto();
            serverSyncUserAttributesDto.setAttributeName(names[i]);
            serverSyncUserAttributesDto.setAttributeValue(values[i]);
            serverSyncUserAttributesDtos.add(serverSyncUserAttributesDto);
        }

        return serverSyncUserAttributesDtos;
    }
}
