package com.onmobile.baseline.http.api_action.storeapis.purchase_combo;


import com.google.gson.annotations.SerializedName;
import com.onmobile.baseline.http.retrofit_io.APIRequestParameters;

import java.io.Serializable;


public class ScheduleDTO implements Serializable{

    public ScheduleDTO(APIRequestParameters.ScheduleType type, String description) {
        super();
        this.type = type;
        this.description = description;
    }

    @SerializedName("type")
    private APIRequestParameters.ScheduleType type;

    @SerializedName("id")
    private String id;

    @SerializedName("description")
    private String description;

    @SerializedName("date_range")
    private DateRange dateRange;

    @SerializedName("time_range")
    private TimeRange timeRange;

    @SerializedName("play_duration")
    private String playDuration;

    public APIRequestParameters.ScheduleType getType() {
        return type;
    }

    public void setType(APIRequestParameters.ScheduleType type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DateRange getDateRange() {
        return dateRange;
    }

    public void setDateRange(DateRange dateRange) {
        this.dateRange = dateRange;
    }

    public TimeRange getTimeRange() {
        return timeRange;
    }

    public void setTimeRange(TimeRange timeRange) {
        this.timeRange = timeRange;
    }

    public String getPlayDuration() {
        return playDuration;
    }

    public void setPlayDuration(String playDuration) {
        this.playDuration = playDuration;
    }

    public class DateRange implements Serializable{
        @SerializedName("start_date")
        private String startDate;

        @SerializedName("end_date")
        private String endDate;

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }
    }

    public class TimeRange implements Serializable{
        @SerializedName("from_time")
        private String fromTime;

        @SerializedName("to_time")
        private String toTime;

        public String getFromTime() {
            return fromTime;
        }

        public void setFromTime(String fromTime) {
            this.fromTime = fromTime;
        }

        public String getToTime() {
            return toTime;
        }

        public void setToTime(String toTime) {
            this.toTime = toTime;
        }
    }

}
