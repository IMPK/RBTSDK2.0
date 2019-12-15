package com.onmobile.rbt.baseline.http.api_action.dtos.appconfigdtos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by hitesh.p on 9/18/2017.
 */

public class ShareContentDTO implements Serializable{
    @SerializedName("additional_parameters")
    String additionalParameters;
    @SerializedName("mweb_url")
    String mWebURL;
    @SerializedName("ios_bundleid")
    String iosBundleID;
    @SerializedName("ios_supported_versioncode")
    String iosSupportedVersionCode;
    @SerializedName("android_packgageid")
    String androidPackageID;
    @SerializedName("android_supported_version")
    String androidSpportedVersion;
    @SerializedName("app_url")
    String appURL;

    public String getAdditionalParameters() {
        return additionalParameters;
    }

    public void setAdditionalParameters(String additionalParameters) {
        this.additionalParameters = additionalParameters;
    }

    public String getmWebURL() {
        return mWebURL;
    }

    public void setmWebURL(String mWebURL) {
        this.mWebURL = mWebURL;
    }

    public String getIosBundleID() {
        return iosBundleID;
    }

    public void setIosBundleID(String iosBundleID) {
        this.iosBundleID = iosBundleID;
    }

    public String getIosSupportedVersionCode() {
        return iosSupportedVersionCode;
    }

    public void setIosSupportedVersionCode(String iosSupportedVersionCode) {
        this.iosSupportedVersionCode = iosSupportedVersionCode;
    }

    public String getAndroidPackageID() {
        return androidPackageID;
    }

    public void setAndroidPackageID(String androidPackageID) {
        this.androidPackageID = androidPackageID;
    }

    public String getAndroidSpportedVersion() {
        return androidSpportedVersion;
    }

    public void setAndroidSpportedVersion(String androidSpportedVersion) {
        this.androidSpportedVersion = androidSpportedVersion;
    }

    public String getAppURL() {
        return appURL;
    }

    public void setAppURL(String appURL) {
        this.appURL = appURL;
    }
}
