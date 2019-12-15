package com.onmobile.rbt.baseline.http.api_action.storeapis.batchrequest;

import android.net.Uri;

import com.onmobile.rbt.baseline.http.api_action.catalogapis.BaseAPICatalogRequestAction;
import com.onmobile.rbt.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.rbt.baseline.http.api_action.storeapis.BaseAPIStoreRequestAction;
import com.onmobile.rbt.baseline.http.cache.UserSettingsCacheManager;
import com.onmobile.rbt.baseline.http.retrofit_io.APIRequestParameters;

/**
 * Created by nikita
 */
public class BatchRequestApiGenerator {

    private static APIRequestParameters.EMode getType(RingBackToneDTO ringBackToneDTO) {
        if (ringBackToneDTO.getType().equalsIgnoreCase("rbtstation")) {
            return APIRequestParameters.EMode.RINGBACK_STATION;
        } else {
            return APIRequestParameters.EMode.RINGBACK;
        }
    }

    public static BatchRequestItemDTO generateSongDetailRequestApi(RingBackToneDTO song, int index) {

        String uri = Uri.parse(new BaseAPICatalogRequestAction().getBaseCatalogRequestURL())
                .buildUpon()
                .appendPath(APIRequestParameters.APIParameter.CONTENT)
                .appendPath(getType(song).value())
                .appendPath(song.getId())
                .appendQueryParameter("mode", APIRequestParameters.EMode.ALBUM.toString())
                .appendQueryParameter("mode", APIRequestParameters.EMode.TRACK.toString())
                .appendQueryParameter("mode", APIRequestParameters.EMode.BUNDLE.toString())
                .appendQueryParameter("mode", APIRequestParameters.EMode.PLAYLIST.toString())
                .build().toString();

        BatchRequestItemDTO item = new BatchRequestItemDTO();

        item.id = String.valueOf(index);
        item.url = uri;
        item.method = "GET";
        return item;
    }


    public static BatchRequestItemDTO generateChartDetailRequestApi(String songId, int index, String max, String offset, String imageWidth) {

        String uri = Uri.parse(new BaseAPICatalogRequestAction().getBaseCatalogRequestURL())
                .buildUpon()
                .appendPath(APIRequestParameters.EMode.CHART.toString().toLowerCase())
                .appendPath(songId)
                .appendQueryParameter(APIRequestParameters.APIParameter.MODE, APIRequestParameters.EMode.CHART.toString().toLowerCase())
                .appendQueryParameter(APIRequestParameters.APIParameter.MAX, max)
                .appendQueryParameter(APIRequestParameters.APIParameter.OFFSET, offset)
                .appendQueryParameter(APIRequestParameters.APIParameter.IMAGE_WIDTH, imageWidth)
                .build().toString();

        BatchRequestItemDTO item = new BatchRequestItemDTO();

        item.id = String.valueOf(index);
        item.url = uri;
        item.method = "GET";
        return item;
    }

    public static BatchRequestItemDTO generateDeletePlayRuleRequest(String playruleID, int index) {

        String uri = Uri.parse(new BaseAPIStoreRequestAction().getBaseStoreURLRequest())
                .buildUpon()
                /*.appendPath(APIRequestParameters.APIURLEndPoints.STORE)
                .appendPath(APIRequestParameters.APIURLEndPoints.VERSION)*/
                .appendPath(APIRequestParameters.EMode.RINGBACK.value())
                .appendPath("subs")
                .appendPath("playrules")
                .appendPath(playruleID)
                .appendQueryParameter(APIRequestParameters.APIParameter.STORE_ID, String.valueOf(APIRequestParameters.APIURLEndPoints.STORE_ID))
                .appendQueryParameter(APIRequestParameters.APIParameter.CRED_TOKEN, UserSettingsCacheManager.getAuthenticationToken())
                .build().toString();

        BatchRequestItemDTO item = new BatchRequestItemDTO();

        item.id = String.valueOf(index);
        item.url = uri;
        item.method = "DELETE";
        return item;
    }

  /*  public static BatchRequestItemDTO generateSongDetailRequestApi(ComboApiAssetDto asset, int index) {
        String uri = Uri.parse(getHostUrlForBatchBody())
                .buildUpon()
                .appendPath(Configuration.SERVER_NAME_CATALOG)
                .appendPath(Configuration.getVersion())
                .appendPath(Configuration.getResponseType())
                .appendPath(Configuration.getStorefrontID() + "")
                .appendPath(Constants.CONTENT)
                .appendPath(Utils.getAssetTypeForContent(asset.getTypeEnum()).toString())
                .appendPath(asset.getId() + "")
                .appendQueryParameter("mode", ContentItemType.ALBUM.toString())
                .appendQueryParameter("mode", ContentItemType.TRACK.toString())
                .appendQueryParameter("mode", ContentItemType.BUNDLE.toString())
                .appendQueryParameter("mode", ContentItemType.PLAYLIST.toString())
                .build().toString();

        BatchRequestItemDTO item = new BatchRequestItemDTO();

        item.id = index;
        item.url = uri;
        item.method = "GET";
        return item;
    }

    public static BatchRequestItemDTO generateSongDetailRequestApi(CartAssetDTO cartAssetDTO, int index) {
        String uri = Uri.parse(getHostUrlForBatchBody())
                .buildUpon()
                .appendPath(Configuration.SERVER_NAME_CATALOG)
                .appendPath(Configuration.getVersion())
                .appendPath(Configuration.getResponseType())
                .appendPath(Configuration.getStorefrontID() + "")
                .appendPath(Constants.CONTENT)
                .appendPath(Utils.getAssetTypeForContent(ContentItemType.RINGBACK_TONE.toString()).toString())
                .appendPath(cartAssetDTO.getAsset_id() + "")
//                .appendQueryParameter("mode", ContentItemType.ALBUM.toString())
//                .appendQueryParameter("mode", ContentItemType.TRACK.toString())
//                .appendQueryParameter("mode", ContentItemType.BUNDLE.toString())
//                .appendQueryParameter("mode", ContentItemType.PLAYLIST.toString())
                .build().toString();

        BatchRequestItemDTO item = new BatchRequestItemDTO();

        item.id = index;
        item.url = uri;
        item.method = "GET";
        return item;
    }

    public static BatchRequestItemDTO generateSongDetailRequestApi(SongListUserHistoryDto song, int index) {

        String uri = Uri.parse(getHostUrlForBatchBody())
                .buildUpon()
                .appendPath(Configuration.SERVER_NAME_CATALOG)
                .appendPath(Configuration.getVersion())
                .appendPath(Configuration.getResponseType())
                .appendPath(Configuration.getStorefrontID() + "")
                .appendPath(Constants.CONTENT)
                .appendPath(Utils.getAssetTypeForContent(song.getType()).toString())
                .appendPath(song.getId() + "")
                .appendQueryParameter("mode", ContentItemType.ALBUM.toString())
                .appendQueryParameter("mode", ContentItemType.TRACK.toString())
                .appendQueryParameter("mode", ContentItemType.BUNDLE.toString())
                .appendQueryParameter("mode", ContentItemType.PLAYLIST.toString())
                .build().toString();

        BatchRequestItemDTO item = new BatchRequestItemDTO();

        item.id = index;
        item.url = uri;
        item.method = "GET";
        return item;
    }

    public static BatchRequestListDTO generateChartBatchApiBody(List<String> chartList) {
        BatchRequestListDTO batchRequestListDTO = new BatchRequestListDTO();
        batchRequestListDTO.batchItems = new ArrayList<>();     //redundant line of code as batchItems already been initialized while creating object in above line
        for (int i = 0; i < chartList.size(); i++) {
            batchRequestListDTO.batchItems.add(generateChartApi(chartList.get(i), i + 1));
        }
        return batchRequestListDTO;
    }

    public static BatchRequestItemDTO generateChartApi(String chartName, int index) {


        String uri = Uri.parse(getHostUrlForBatchBody())
                .buildUpon()
                .appendPath(Configuration.SERVER_NAME_CATALOG)
                .appendPath(Configuration.getVersion())
                .appendPath(Configuration.getResponseType())
                .appendPath(Configuration.getStorefrontID() + "")
                .appendPath("chart")
                .appendPath(chartName)
                .appendQueryParameter("offset", "0")
                .appendQueryParameter("max", "10")
                .build().toString();

        BatchRequestItemDTO item = new BatchRequestItemDTO();

        item.id = index;
        item.url = uri;
        item.method = "GET";
        return item;
    }


    //If "BatchAPIBodyURLEndPointRequired" flag of AppfeatureConfig would be true then it will take whatever will be configured in application.properties
    //If False then we dont need to pass host while constructing batch body url so it will return empty string.
    public static String getHostUrlForBatchBody() {
        if (isBatchAPIBodyEndPointRequired()) {
            //take it from application.properties
            if (Configuration.getBatch_body_end_point() != null && !Configuration.getBatch_body_end_point().isEmpty()) {
                return Configuration.getBatch_body_end_point();
            } else {
                return Configuration.getEndPointStore();
            }
        } else {
            return "";
        }
    }


    public static boolean isBatchAPIBodyEndPointRequired() {
        boolean isSupported = false;
        try {
            isSupported = ((BaselineApp) Utils.mContext).getApplicationConfiguration()
                    .get(AppConfigConstants.Modules.MainApp.toString())
                    .get(AppConfigConstants.MainApp.BatchAPIBodyURLEndPointRequired.toString())
                    .equalsIgnoreCase(Constants.APP_TRUE);
        } catch (Exception e) {
            Log.e("BaseBatchRequest : ", "isBatchAPIBodyEndPointRequired: " + "could not be read from the AppConfig xml.");
        }
        return isSupported;
    }


    public static BatchRequestItemDTO generateChartDetailRequestApi(String chartId, int index) {
        String uri = Uri.parse(getHostUrlForBatchBody())
                .buildUpon()
                .appendPath(Configuration.SERVER_NAME_CATALOG)
                .appendPath(Configuration.getVersion())
                .appendPath(Configuration.getResponseType())
                .appendPath(Configuration.getStorefrontID() + "")
                .appendPath(Constants.CHART)
                .appendPath(chartId)
                .build().toString();

        BatchRequestItemDTO item = new BatchRequestItemDTO();

        item.id = index;
        item.url = uri;
        item.method = "GET";
        return item;
    }*/

}
