package com.onmobile.rbt.baseline;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.onmobile.rbt.baseline.activities.SplashActivity;
import com.onmobile.rbt.baseline.application.SharedPrefProvider;


import java.util.ArrayList;

public class RbtSdkClient/* implements IExposedContent */{

    private static Context mContext;
    private static String mMsisdn;
    private static MsisdnType mMsisdnType;
    private static String mOperator;
    //private SharedPreferences.Editor RbtSdkClienteditor;
    private static SharedPrefProvider mSharedPrefProvider;

    private ArrayList<String> songPreviewedList;

    //private IRbtSdkManager mRbtSdkManager;
    //private final ArrayList<ContentDTO> exposedContentDTOArrayList = new ArrayList<>();
    private static IRBTSDKEventlistener mIRBTSDKEventlistener;


    public RbtSdkClient(Context context, String msisdn, MsisdnType msisdnType, String mOperator, IRBTSDKEventlistener aIRBTSDKEventlistener) {

        this(context, msisdn, msisdnType, mOperator);
        this.mIRBTSDKEventlistener = aIRBTSDKEventlistener;

    }

    public RbtSdkClient(Context context, String msisdn, MsisdnType msisdnType, String mOperator) {
        mContext = context;
        mMsisdn = msisdn;
        mMsisdnType = msisdnType;
        this.mOperator = mOperator;
        //mRbtSdkManager = RbtSdkManagerProvider.getRbtSdkManager(mContext);
        mSharedPrefProvider = SharedPrefProvider.getInstance(mContext);
        mSharedPrefProvider.setSDKOperator(decodeOperatorValue());
        persistOperatorCode(decodeOperatorValue());
        songPreviewedList = new ArrayList<>();
    }

    private static void initializeSDK() {
        if (isValidated()) {
            String decodedOperator = decodeOperatorValue();
            String dynamicPackageName = mContext.getPackageName();
            Log.e("RbtSdkClient", ": " + dynamicPackageName);
            Log.e("decodedOperator", ": " + decodedOperator);
            persistOperatorCode(decodedOperator);


            if (decodedOperator.equalsIgnoreCase(Constant.operatorName(decodedOperator))) {
                if (Constant.isPackageVerificationRequired) {
                    if (dynamicPackageName.equalsIgnoreCase(packageNameProvided(decodedOperator))) {

                        AppManager.getInstance().setSdkMsisdn(mMsisdn);
                        AppManager.getInstance().setSdkMsidnType(mMsisdnType);
                        AppManager.getInstance().setSdkOperator(decodedOperator);
                        AppManager.getInstance().setmIRbtsdkEventlistener(mIRBTSDKEventlistener);
                        AppManager.getInstance().init(mContext);
                        //AppManager.getInstance(mMsisdn,mMsisdnType,decodedOperator).init(mContext);

//                        android.content.Intent intent = new android.content.Intent(mContext, SplashActivity.class);
//                        intent.putExtra(Constant.Intent.EXTRA_MSISDN_SDK, mMsisdn);
//                        intent.putExtra(Constant.Intent.EXTRA_MSISDN_TYPE_SDK, mMsisdnType);
//                        intent.putExtra(Constant.Intent.EXTRA_OPERATOR_SDK, decodedOperator);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        mContext.startActivity(intent);
                    } else {
                        Toast.makeText(mContext, mContext.getString(R.string.not_authorised_access), Toast.LENGTH_LONG).show();
                    }
                } else {
                    AppManager.getInstance().setSdkMsisdn(mMsisdn);
                    AppManager.getInstance().setSdkMsidnType(mMsisdnType);
                    AppManager.getInstance().setSdkOperator(decodedOperator);
                    AppManager.getInstance().setmIRbtsdkEventlistener(mIRBTSDKEventlistener);
                    AppManager.getInstance().init(mContext);
                    //AppManager.getInstance(mMsisdn,mMsisdnType,decodedOperator).init(mContext);

//                    android.content.Intent intent = new android.content.Intent(mContext, SplashActivity.class);
//                    intent.putExtra(Constant.Intent.EXTRA_MSISDN_SDK, mMsisdn);
//                    intent.putExtra(Constant.Intent.EXTRA_MSISDN_TYPE_SDK, mMsisdnType);
//                    intent.putExtra(Constant.Intent.EXTRA_OPERATOR_SDK, decodedOperator);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    mContext.startActivity(intent);
                }

                // listener rigister for events
                EventReceiver.registerEventListener(mContext, mIRBTSDKEventlistener);
            } else {
                Toast.makeText(mContext, mContext.getString(R.string.select_valid_operator), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void startSDK() {
        //RetrofitProvider.reset();
        AppManager.getInstance().redirectToSDK();

    }

    private static String decodeOperatorValue() {
        String decodedOperator = "";
        try {
            byte[] data = Base64.decode(mOperator, Base64.DEFAULT);
            decodedOperator = new String(data, "UTF-8");
            Log.e("RbtSdkClient", "Decoded Ope : " + decodedOperator);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decodedOperator;
    }


    public static class Builder {
        private Context context;
        private String msisdn;
        private MsisdnType msisdnType;
        private String mOperator;
        SharedPrefProvider sharedPrefProvider;
        SharedPreferences pref;
        private IRBTSDKEventlistener mRBTSDKEventlistener;
        static SharedPreferences.Editor editor;

        public Builder init(Context context) {
            this.context = context;
            sharedPrefProvider = SharedPrefProvider.getInstance(context);
            //sharedPrefProvider.clear();
            pref = this.context.getSharedPreferences("MyPref", 0);
            editor = pref.edit();
            return this;
        }

        public Builder setEventListener(final IRBTSDKEventlistener aRBTSDKEventlistener) {
            if (aRBTSDKEventlistener != null) {
                mRBTSDKEventlistener = aRBTSDKEventlistener;
            }
            return this;
        }

        public Builder setMsisdn(String msisdn) {
            this.msisdn = msisdn;
            sharedPrefProvider.setMsisdn(msisdn);
            //MultiProcessPreferenceProvider.setPreferenceValue(Constant.Preferences.USER_MSISDN_NUMBER, msisdn);
            return this;
        }

        public Builder setOperator(String operator) {
            this.mOperator = operator;
            sharedPrefProvider.setSDKOperatorEncoded(operator);
            //MultiProcessPreferenceProvider.setPreferenceValue(Constant.Preferences.ENCODED_OPERATOR_STRING, mOperator);
            return this;
        }

        public Builder setMsisdnType(MsisdnType msisdnType) {
            if (msisdnType != null) {
                this.msisdnType = MsisdnType.valueOf(msisdnType.name());
                sharedPrefProvider.setSDKMsisdnType(msisdnType.ordinal() + "");
            }
            return this;
        }

        public RbtSdkClient build() throws RbtSdkInitialisationException {

            if (context == null) {
                throw new RbtSdkInitialisationException(context, context.getString(R.string.context_cannot_null), ExceptionConstants.CONTEXT_NULL);
            }

            if (msisdn == null || msisdn.isEmpty()) {
                throw new RbtSdkInitialisationException(context, context.getString(R.string.msisdn_invalid), ExceptionConstants.MSISDN_INVALID);
            }

            if (msisdnType == null) {
                throw new RbtSdkInitialisationException(context, context.getString(R.string.msisdn_type), ExceptionConstants.MSISDN_INVALID);
            }

            if (mOperator == null) {
                throw new RbtSdkInitialisationException(context, context.getString(R.string.operator_type), ExceptionConstants.OPERATOR_INVALID);
            }
            if (mRBTSDKEventlistener != null){
                RbtSdkClient rbtSdkClient = new RbtSdkClient(context, msisdn, msisdnType, mOperator, mRBTSDKEventlistener);
                initializeSDK();
                return rbtSdkClient;
            } else
                return new RbtSdkClient(context, msisdn, msisdnType, mOperator);

        }

        public static SharedPreferences.Editor getSharedPreferencesInstance() {
            return editor;
        }
    }

    private static boolean isValidated() {
        if (mMsisdn.equalsIgnoreCase("")) {
            Toast.makeText(mContext, mContext.getString(R.string.enter_msisdn), Toast.LENGTH_LONG).show();
            return false;
        }
        if (mMsisdn.length() < 10) {
            Toast.makeText(mContext, mContext.getString(R.string.enter_valid_msisdn), Toast.LENGTH_LONG).show();
            return false;

        }

        if (mOperator.equalsIgnoreCase("")) {
            Toast.makeText(mContext, mContext.getString(R.string.select_opeartor), Toast.LENGTH_LONG).show();
            return false;
        }
        if (mMsisdnType.equals(MsisdnType.DEFAULT)) {
            Toast.makeText(mContext, mContext.getString(R.string.select_msisdn_type), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;

    }

    private static String packageNameProvided(String operator) {
        String packageNameFromOperator = "";

        if (operator.equalsIgnoreCase(Constant.operator.IDEA_OPERATOR)) {
            packageNameFromOperator = Constant.packageNameProvided.IDEA_PACKAGENAME;
        } else if (operator.equalsIgnoreCase(Constant.operator.VODAFONE_PLAY_OPERATOR)) {
            packageNameFromOperator = Constant.packageNameProvided.VODAFONE_PLAY_PACKAGENAME;
        } else if (operator.equalsIgnoreCase(Constant.operator.VODAFONE_IND_OPERATOR)) {
            packageNameFromOperator = Constant.packageNameProvided.VODAFONE_IND_PACKAGENAME;
        } else if (operator.equalsIgnoreCase(Constant.operator.GRAMEEN_PHONE_OPERATOR)) {
            packageNameFromOperator = Constant.packageNameProvided.GRAMEEN_PHONE_PACKAGENAME;
        } else if (operator.equalsIgnoreCase(Constant.operator.VODA_IDEA_OPERATOR)) {
            packageNameFromOperator = Constant.operator.VODA_IDEA_OPERATOR;
        }

        return packageNameFromOperator;
    }

    private static void persistOperatorCode(String operatorName) {
        if (operatorName.equalsIgnoreCase(Constant.operator.IDEA_OPERATOR)) {
            mSharedPrefProvider.setSelectedSDKOperator("it");
        }
        if (operatorName.equalsIgnoreCase(Constant.operator.VODAFONE_IND_OPERATOR) || operatorName.equalsIgnoreCase(Constant.operator.VODAFONE_PLAY_OPERATOR)) {
            mSharedPrefProvider.setSelectedSDKOperator("en");
        }
        if (operatorName.equalsIgnoreCase(Constant.operator.GRAMEEN_PHONE_OPERATOR)) {
            mSharedPrefProvider.setSelectedSDKOperator("ga");
        }
        if (operatorName.equalsIgnoreCase(Constant.operator.VODA_IDEA_OPERATOR)) {
            mSharedPrefProvider.setSelectedSDKOperator("vi");
        }
    }

//    @Override
//    public void getContentList(SDKConstants.ETYPE musicType, final SDKConstants.ECATEGORY category, SDKConstants.ELANGUAGE hindi, final int mOffset, final IContentResponseHandler<ArrayList<ContentDTO>> responseHandler) {
//        intializeLibrary(new ILibraryResponseHandler() {
//            @Override
//            public void onResponseSuccess() {
//                if (mRbtSdkManager != null) {
//                    AppConfigDTO appConfigDTO = MultiProcessPreferenceProvider.getAppConfigPreference(Constant.Preferences.APP_CONFIG);
//                    SupportedCategoriesDTO supportedCategoriesDTO = appConfigDTO.getSupportedCategoriesDTO();
//                    int categoryId = -1;
//                    if (supportedCategoriesDTO != null) {
//                        if (supportedCategoriesDTO.getMost_Downloaded() != null && category.toString().equalsIgnoreCase("Most_Downloaded")) {
//                            categoryId = Integer.parseInt(supportedCategoriesDTO.getMost_Downloaded());
//                        } else if (supportedCategoriesDTO.getIndipop() != null && category.toString().equalsIgnoreCase("Indipop")) {
//                            categoryId = Integer.parseInt(supportedCategoriesDTO.getIndipop());
//                        } else if (supportedCategoriesDTO.getTop_10() != null && category.toString().equalsIgnoreCase("Top_10")) {
//                            categoryId = Integer.parseInt(supportedCategoriesDTO.getTop_10());
//                        } else if (supportedCategoriesDTO.getTrending() != null && category.toString().equalsIgnoreCase("Trending")) {
//                            categoryId = Integer.parseInt(supportedCategoriesDTO.getTrending());
//                        }
//                    }
//                    if (categoryId == -1) {
//                        Toast.makeText(mContext, "Invalid Category It seems", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                    //final int finalCategoryId = categoryId;
//                    mRbtSdkManager.getChartContent(categoryId, mOffset, 414, new IUIResponseHandler<ChartDTO>() {
//                        @Override
//                        public void handleResponse(ChartDTO result) {
//                            {
//                                List<Item> itemList = result.getItems();
//                                for (Item item : itemList) {
//                                    ContentDTO exposedContentDTO = new ContentDTO();
//                                    exposedContentDTO.setAlbum(item.getAlbumName());
//                                    exposedContentDTO.setArtist(item.getPrimaryArtistName());
//                                    exposedContentDTO.setContentLanguage(item.getLanguage());
//                                    exposedContentDTO.setTitle(item.getTrackName());
//                                    exposedContentDTO.setImageURL(item.getPrimaryImage());
//                                    exposedContentDTO.setPreviewStreamURL(item.getPreviewStreamUrl());
//                                    exposedContentDTO.setContentType(item.getType());
//                                    exposedContentDTO.setContentID(item.getId());
//                                    exposedContentDTO.setDisplayDownloadCount(item.getDisplayDownloadCount());
//                                    exposedContentDTO.setName(item.getName());
//                                    exposedContentDTO.setSubType(item.getSubtype());
//
//                                    exposedContentDTOArrayList.add(exposedContentDTO);
//                                }
//                                responseHandler.onResponseSuccess(exposedContentDTOArrayList);
//                            }
//                        }
//
//                        @Override
//                        public void handleError(String msg) {
//                            responseHandler.onResponseError("", SDKConstants.ErrorCode.NETWORK_ERROR);
//                        }
//                    }).executeAsync();
//                }
//            }
//
//            @Override
//            public void onResponseError(String errorMessage, int errorCode) {
//                Toast.makeText(mContext, errorMessage, Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    @Override
//    public void moveToPreBuyScreen(final ContentDTO exposedContentDTO) {
//        String msisdnNumber=MultiProcessPreferenceProvider.getPreferenceString(Constant.Preferences.USER_MSISDN_NUMBER);
//        new LibraryInitializationHandler(msisdnNumber, new IInitializer() {
//            @Override
//            public void onInitializationSuccess() {
//                Item ringbackDTO = new Item();
//                ringbackDTO.setAlbumName(exposedContentDTO.getAlbum());
//                ringbackDTO.setPrimaryArtistName(exposedContentDTO.getArtist());
//                ringbackDTO.setId(exposedContentDTO.getContentID());
//                ringbackDTO.setLanguage(exposedContentDTO.getContentLanguage());
//                ringbackDTO.setType(exposedContentDTO.getContentType());
//                ringbackDTO.setPrimaryImage(exposedContentDTO.getImageURL());
//                ringbackDTO.setPreviewStreamUrl(exposedContentDTO.getPreviewStreamURL());
//                ringbackDTO.setTrackName(exposedContentDTO.getTitle());
//                ringbackDTO.setDisplayDownloadCount(exposedContentDTO.getDisplayDownloadCount());
//                ringbackDTO.setName(exposedContentDTO.getName());
//                ringbackDTO.setSubtype(exposedContentDTO.getSubType());
//
//                final Intent intent = new Intent(mContext, PreBuyActivity.class);
//                intent.putExtra(ITEM_EXTRA_IN_ADAPTER, ringbackDTO);
//                if (ringbackDTO.getSubtype().getType().equalsIgnoreCase("ringback_musictune")) {
//                    intent.putExtra(EXTRA_IS_PROFILE_TUNE, false);
//                }
//                intent.putExtra(EXTRA_IS_NAVIGATED_FROM_PARENT_APP, "true");
//                mContext.startActivity(intent);
//            }
//
//            @Override
//            public void onInitializationFailed(String errorObject) {
//
//            }
//        },mRbtSdkManager);
//    }
//
//    @Override
//    public void viewAllContent(SDKConstants.ETYPE musicType, final SDKConstants.ECATEGORY category, SDKConstants.ELANGUAGE elanguage) {
//        intializeLibrary(new ILibraryResponseHandler() {
//            @Override
//            public void onResponseSuccess() {
//
//                AppConfigDTO appConfigDTO = MultiProcessPreferenceProvider.getAppConfigPreference(Constant.Preferences.APP_CONFIG);
//                SupportedCategoriesDTO supportedCategoriesDTO = appConfigDTO.getSupportedCategoriesDTO();
//                int categoryId = -1;
//                if (supportedCategoriesDTO != null) {
//                    if (supportedCategoriesDTO.getMost_Downloaded() != null && category.toString().equalsIgnoreCase("Most_Downloaded")) {
//                        categoryId = Integer.parseInt(supportedCategoriesDTO.getMost_Downloaded());
//                    } else if (supportedCategoriesDTO.getIndipop() != null && category.toString().equalsIgnoreCase("Indipop")) {
//                        categoryId = Integer.parseInt(supportedCategoriesDTO.getIndipop());
//                    } else if (supportedCategoriesDTO.getTop_10() != null && category.toString().equalsIgnoreCase("Top_10")) {
//                        categoryId = Integer.parseInt(supportedCategoriesDTO.getTop_10());
//                    } else if (supportedCategoriesDTO.getTrending() != null && category.toString().equalsIgnoreCase("Trending")) {
//                        categoryId = Integer.parseInt(supportedCategoriesDTO.getTrending());
//                    }
//                }
//                if (categoryId == -1) {
//                    Toast.makeText(mContext, "Invalid Category It seems", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                String ringbackDTOId= String.valueOf(categoryId);
//                String ringbackDTOName = category.toString();
//                String ringbackDTOType = "chart";
//
//                ringbackDTOName = ringbackDTOName.replace("_", " ");
//
//                Item ringbackDTO = new Item();
//                ringbackDTO.setId(ringbackDTOId);
//                ringbackDTO.setName(ringbackDTOName);
//                ringbackDTO.setType(ringbackDTOType);
//
//                Intent intent = new Intent(mContext, ChartActivity.class);
//                intent.putExtra("item", ringbackDTO);
//                intent.putExtra(EXTRA_IS_NAVIGATED_FROM_PARENT_APP, "true");
//                intent.putExtra(EXTRA_IS_NAVIGATED_FROM_VIEW_ALL_CONTENT, "true");
//                mContext.startActivity(intent);
//            }
//
//            @Override
//            public void onResponseError(String errorMessage, int errorCode) {
//                Toast.makeText(mContext, errorMessage, Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    @Override
//    public void previewedSong(ContentDTO exposedContentDTO) {
//        //songPreviewedList needs to be intialized somewhere.
//        songPreviewedList.add(exposedContentDTO.getContentID());
//        MultiProcessPreferenceProvider.writeList("previewedContent", songPreviewedList);
//    }
//
//    @Override
//    public void previewedSong(String contentId) {
//        songPreviewedList.add(contentId);
//        MultiProcessPreferenceProvider.writeList("previewedContent", songPreviewedList);
//    }
//
//
//    private void intializeLibrary(final ILibraryResponseHandler responseHandler) {
//        String msisdnNumber=MultiProcessPreferenceProvider.getPreferenceString(Constant.Preferences.USER_MSISDN_NUMBER);
//        new LibraryInitializationHandler(msisdnNumber, new IInitializer() {
//            @Override
//            public void onInitializationSuccess() {
//                responseHandler.onResponseSuccess();
//            }
//
//            @Override
//            public void onInitializationFailed(String errorObject) {
//                responseHandler.onResponseError("SDK not intialized", 901);
//            }
//        }, mRbtSdkManager);
//    }

}