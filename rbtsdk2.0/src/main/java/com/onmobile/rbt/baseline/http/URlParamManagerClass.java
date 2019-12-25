package com.onmobile.rbt.baseline.http;

import com.onmobile.rbt.baseline.Constant;

/**
 * Created by prateek.khurana on 19-Dec 2019
 */
public class URlParamManagerClass {

    public static void setUrls(String operator) {
        if (operator.equalsIgnoreCase(Constant.operator.IDEA_OPERATOR)) {

        } else if (operator.equalsIgnoreCase(Constant.operator.VODAFONE_PLAY_OPERATOR)) {

        } else if (operator.equalsIgnoreCase(Constant.operator.VODAFONE_IND_OPERATOR)) {
            Configuration.setApi_end_point_catalog("https://vsfind.aps.contentstore.onmobile.com");
            Configuration.setApi_end_point_store("https://vsfind.aps.contentstore.onmobile.com");
            Configuration.setAuthentication_api("http://vodind.aps.contentstore.onmobile.com");
            Configuration.setPush_notification_host("http://vsfind.aps.contentstore.onmobile.com");
            Configuration.setCertificate_pinning_key("sha256/to6qIG5XaVGhZPhkMtnOUivcGriJ3fNp2ufVMu1svnI=");
            Configuration.setFeedback_host("https://vsfind.aps.contentstore.onmobile.com");
            Configuration.setNetwork_utility_host("http://vodind.aps.contentstore.onmobile.com");
            Configuration.setServerNameCatalog("catalog");
            Configuration.setServerNameStore("store");
            Configuration.setServerNameNotification("notification");
            Configuration.setCutrbt_host_link("https://stream.onmo.com");
            Configuration.setResponse_type("json");
            Configuration.setVersion("v3");
            Configuration.setFirebase_dynamic_link_domain("https://k8s47.app.goo.gl");
            Configuration.setStore_id("289");
        } else if (operator.equalsIgnoreCase(Constant.operator.GRAMEEN_PHONE_OPERATOR)) {

        } else if (operator.equalsIgnoreCase(Constant.operator.VODA_IDEA_OPERATOR)) {

        }

    }
}
