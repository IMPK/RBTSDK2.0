package com.onmobile.rbt.baseline.application;

/**
 * Created by Nikita Gurwani .
 */
public interface ICheckOldAppDbValues {

    void ifDatabaseExist(boolean exist);

    void ifTableExist(boolean exist);

    void getMsisdnIfExist(String msisdn);
}
