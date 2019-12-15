package com.onmobile.rbt.baseline.http.api_action.storeapis;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.onmobile.rbt.baseline.http.HttpModuleAPIAccessor;
import com.onmobile.rbt.baseline.http.api_action.dtos.ChartItemDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.ListOfSongsResponseDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.SongListUserHistoryDto;
import com.onmobile.rbt.baseline.http.api_action.dtos.UserHistoryResultsDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.appconfigdtos.AppConfigDataManipulator;
import com.onmobile.rbt.baseline.http.api_action.dtos.appconfigdtos.UserRbtHistoryDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.udp.UdpDetailDTO;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ErrorCode;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ErrorResponse;
import com.onmobile.rbt.baseline.http.api_action.storeapis.batchrequest.GetContentBatchRequest;
import com.onmobile.rbt.baseline.http.basecallback.BaselineCallback;
import com.onmobile.rbt.baseline.http.cache.UserSettingsCacheManager;
import com.onmobile.rbt.baseline.http.retrofit_io.APIRequestParameters;
import com.onmobile.rbt.baseline.http.utils.Logger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetUserHistoryRequest extends BaseAPIStoreRequestAction {

    private static final Logger sLogger = Logger.getLogger(GetUserHistoryRequest.class);
    private BaselineCallback<UserHistoryResultsDTO> baselineCallback;
    private BaselineCallback<ListOfSongsResponseDTO> mRingbackTonesCallback;
    private UserHistoryQueryParameters userHistoryQueryParameters;
    private int max, offset;
    private int retryCount = 0;

    /*public GetPlayRuleRequest(BaselineCallback<UserHistoryResultsDTO> baselineCallback) {
        this.baselineCallback = baselineCallback;
        initCall();
    }*/
    private Call<UserHistoryResultsDTO> call;


    public GetUserHistoryRequest(BaselineCallback<ListOfSongsResponseDTO> baselineCallback) {
        this.mRingbackTonesCallback = baselineCallback;
        // initCall();
    }

    @Override
    public void cancel() {
        if (call != null) {
            try {
                call.cancel();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void execute() {
        ++retryCount;
        call.enqueue(new Callback<UserHistoryResultsDTO>() {
            @Override
            public void onResponse(Call<UserHistoryResultsDTO> call, final Response<UserHistoryResultsDTO> response) {
                sLogger.e("response");
                if (response.isSuccessful()) {
                    List<RingBackToneDTO> songList = new ArrayList<>();
                    final List<RingBackToneDTO> shuffleListIds = new ArrayList<>();
                    final UserHistoryResultsDTO userHistoryResultsDTO = response.body();
                    List<SongListUserHistoryDto> list = response.body().getSongList();
                    for (SongListUserHistoryDto dto : list) {
                        RingBackToneDTO ringBackToneDTO = new RingBackToneDTO();
                        ringBackToneDTO.setId(dto.getId());
                        ringBackToneDTO.setSubType(dto.getSubType());
                        if (dto.getType().equalsIgnoreCase(APIRequestParameters.EMode.RBTSTATION.value())) {
                            ringBackToneDTO.setType(APIRequestParameters.EMode.RBTSTATION.value());
                            songList.add(ringBackToneDTO);
                        } else if (dto.getType().equalsIgnoreCase(APIRequestParameters.EMode.SHUFFLE_LIST.value())) {
                            ringBackToneDTO.setType(APIRequestParameters.EMode.SHUFFLE_LIST.value());
                            shuffleListIds.add(ringBackToneDTO);
                        } else {
                            ringBackToneDTO.setType(APIRequestParameters.EMode.RINGBACK.value());
                            songList.add(ringBackToneDTO);
                        }
                    }

                    if (songList.size() > 0)
                        new GetContentBatchRequest(songList, new BaselineCallback<ListOfSongsResponseDTO>() {
                            @Override
                            public void success(ListOfSongsResponseDTO result) {
                                if (result != null) {
                                    if (shuffleListIds.size() > 0) {
                                        invokeUDPDetails(userHistoryResultsDTO, shuffleListIds, result);
                                        return;
                                    }
                                    explicitResponse(userHistoryResultsDTO, result);
                                }
                            }

                            @Override
                            public void failure(ErrorResponse errMsg) {
                                mRingbackTonesCallback.failure(errMsg);
                            }
                        }).execute();
                    else if (shuffleListIds.size() > 0) {
                        invokeUDPDetails(userHistoryResultsDTO, shuffleListIds, null);
                    }
                    else{
                        mRingbackTonesCallback.success(null);
                    }
                } else {
                    try {
                        handleError(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                        handleError(null);
                    }
                }
            }

            @Override
            public void onFailure(Call<UserHistoryResultsDTO> call, Throwable t) {
                sLogger.e(t.getMessage());
                mRingbackTonesCallback.failure(handleRetrofitError(t));
            }
        });
    }

    @Override
    public void initCall() {
        call = getApi().getUserHistory(getBaseStoreURLRequest(), getUserHistoryOptions());
    }


    @Override
    protected Map<String, String> getUserHistoryOptions() {
        Map<String, String> options = new HashMap<>();
        UserRbtHistoryDTO userRbtHistoryDTO = AppConfigDataManipulator.getUserHistoryParams();
        options.put(APIRequestParameters.APIParameter.STORE_ID, getStoreId());
        options.put(APIRequestParameters.APIParameter.CRED_TOKEN, UserSettingsCacheManager.getAuthenticationToken());
        options.put(APIRequestParameters.APIParameter.OFFSET, String.valueOf(offset));
        options.put(APIRequestParameters.APIParameter.SIZE, String.valueOf(max != 0 ? max : 10));
        options.put(APIRequestParameters.APIParameter.STATUS, userRbtHistoryDTO.getGetParameters().getUser_status());
        options.put(APIRequestParameters.APIParameter.TYPE, userRbtHistoryDTO.getGetParameters().getContent_type());
        options.put(APIRequestParameters.APIParameter.SUBTYPE, userRbtHistoryDTO.getGetParameters().getContent_subtype());
        return options;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    @Override
    public void handleAuthError(BaselineCallback<String> baselineCallback) {
        super.handleAuthError(baselineCallback);
        if (retryCount < 3) {
            HttpModuleAPIAccessor.getAuthToken(baselineCallback);
        }
    }

    @Override
    public void handleError(String errorBody) {
        try {
            Gson gson = new Gson();
            Type errorType = new TypeToken<ErrorResponse>() {
            }.getType();
            final ErrorResponse errorResponse = gson.fromJson(errorBody, errorType);
            if (errorResponse.getCode() == ErrorCode.authentication_token_expired) {
                handleAuthError(new BaselineCallback<String>() {
                    @Override
                    public void success(String result) {
                        initCall();
                        execute();
                    }

                    @Override
                    public void failure(ErrorResponse errMsg) {
                        if (baselineCallback != null) {
                            baselineCallback.failure(errorResponse);
                        }
                    }
                });
            } else {
                mRingbackTonesCallback.failure(errorResponse);
            }

        } catch (Exception e) {
            mRingbackTonesCallback.failure(handleGeneralError(e));
        }
    }

    private void explicitResponse(UserHistoryResultsDTO userHistoryResultsDTO, ListOfSongsResponseDTO result) {
        if (mRingbackTonesCallback != null) {
            if (userHistoryResultsDTO != null) {
                result.setOffset(Integer.parseInt(userHistoryResultsDTO.getOffset()));
                result.setTotalItemCount(Integer.parseInt(userHistoryResultsDTO.getTotalItemCount()));
                result.setSize(Integer.parseInt(userHistoryResultsDTO.getItemCount()));
                mRingbackTonesCallback.success(result);
                return;
            }
            mRingbackTonesCallback.failure(null);
        }
    }

    private void invokeUDPDetails(final UserHistoryResultsDTO userHistoryResultsDTO, final List<RingBackToneDTO> shuffleListIds, final ListOfSongsResponseDTO lastResult) {
        final int[] callSuccessCounter = {0};
        final ListOfSongsResponseDTO finalResponse = lastResult != null ? lastResult : new ListOfSongsResponseDTO();
        if (finalResponse.getChartItemDTO() == null)
            finalResponse.setChartItemDTO(new ArrayList<ChartItemDTO>());
        for (final RingBackToneDTO shuffleListId : shuffleListIds) {
            new GetDetailUserShufflePlaylistRequest(shuffleListId.getId(), new BaselineCallback<UdpDetailDTO>() {
                @Override
                public void success(UdpDetailDTO result) {
                    callSuccessCounter[0]++;
                    ChartItemDTO chartItemDTO = new ChartItemDTO();
                    chartItemDTO.setId(result.getId());
                    chartItemDTO.setType(result.getType());
                    chartItemDTO.setCaption(result.getName());
                    chartItemDTO.setPrimaryImage(result.getExtraInfo());
                    finalResponse.getChartItemDTO().add(chartItemDTO);
                    if (callSuccessCounter[0] == shuffleListIds.size())
                        explicitResponse(userHistoryResultsDTO, finalResponse);
                }

                @Override
                public void failure(ErrorResponse errorResponse) {
                    explicitResponse(userHistoryResultsDTO, finalResponse);
                }
            }).execute();
        }
    }

}
