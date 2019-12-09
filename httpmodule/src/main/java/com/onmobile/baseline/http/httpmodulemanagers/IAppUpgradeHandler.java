package com.onmobile.baseline.http.httpmodulemanagers;

/**
 * Created by Nikita Gurwani .
 */
public interface IAppUpgradeHandler {

    boolean appOptional(boolean isRequired, String textMsg);

    boolean appMandatory(boolean isRequired , String textMsg);

    boolean noPopRequired();
}
