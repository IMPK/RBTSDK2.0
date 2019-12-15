package com.onmobile.rbt.baseline.http.api_action.dtos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Nikita Gurwani .
 */
public class ListOfSongsResponseDTO implements Serializable {

    List<RingBackToneDTO> ringBackToneDTOS;

    List<ChartItemDTO> chartItemDTO ;

    List<RingBackToneDTO> downloadedRingBackToneDTOS;

    List<ChartItemDTO> downloadedChartItemDTO ;

    @SerializedName("total_item_count")
    private int totalItemCount;

    @SerializedName("offset")
    private int offset;

    @SerializedName("size")
    private int size;
    public List<RingBackToneDTO> getRingBackToneDTOS() {
        return ringBackToneDTOS;
    }

    public void setRingBackToneDTOS(List<RingBackToneDTO> ringBackToneDTOS) {
        this.ringBackToneDTOS = ringBackToneDTOS;
    }

    public List<ChartItemDTO> getChartItemDTO() {
        return chartItemDTO;
    }

    public void setChartItemDTO(List<ChartItemDTO> chartItemDTO) {
        this.chartItemDTO = chartItemDTO;
    }

    public int getTotalItemCount() {
        return totalItemCount;
    }

    public void setTotalItemCount(int totalItemCount) {
        this.totalItemCount = totalItemCount;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<RingBackToneDTO> getDownloadedRingBackToneDTOS() {
        return downloadedRingBackToneDTOS;
    }

    public void setDownloadedRingBackToneDTOS(List<RingBackToneDTO> downloadedRingBackToneDTOS) {
        this.downloadedRingBackToneDTOS = downloadedRingBackToneDTOS;
    }

    public List<ChartItemDTO> getDownloadedChartItemDTO() {
        return downloadedChartItemDTO;
    }

    public void setDownloadedChartItemDTO(List<ChartItemDTO> downloadedChartItemDTO) {
        this.downloadedChartItemDTO = downloadedChartItemDTO;
    }
}
