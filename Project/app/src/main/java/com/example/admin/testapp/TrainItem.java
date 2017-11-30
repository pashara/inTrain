package com.example.admin.testapp;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.simple.JSONObject;

/**
 * Created by admin on 19.09.2017.
 */

class TrainItem implements Parcelable {
    private boolean isInit = false;
    private String code;
    private String title;
    private String from;
    private String to;
    private TrainType type;
    private String startTime;
    private String endTime;
    private String timeInRoad;
    private String information;
    //private Stations stations;

    private void init(){
        code = new String();
        from = new String();
        to = new String();
        type = TrainType.NULL;
        startTime = new String();
        endTime = new String();
        timeInRoad = new String();
        information = new String();
    }
    public TrainItem(){
        init();
    }


    public TrainItem(JSONObject slide){
        TrainType currentTrainType;
        switch (slide.get("type").toString()) {
            case "international":
                currentTrainType = TrainType.International;
                break;
            case "interregional_economy":
                currentTrainType = TrainType.InterregionalEconomy;
                break;
            case "regional_business":
                currentTrainType = TrainType.RegionalBusiness;
                break;
            case "regional_economy":
                currentTrainType = TrainType.RegionalEconomy;
                break;
            default:
                currentTrainType = TrainType.NULL;
        }

        JSONObject race_title = (JSONObject)slide.get("race_title");
        this.setFrom(race_title.get("from").toString());
        this.setTo(race_title.get("to").toString());
        this.setTitle(race_title.get("full").toString());

        this.setType(currentTrainType);


        JSONObject time = (JSONObject)slide.get("time");
        this.setStartTime(time.get("start").toString());
        this.setEndTime(time.get("end").toString());
        this.setTimeInRoad(((JSONObject)slide.get("time_in_road")).get("text").toString());
        this.setInformation("00:00");
        isInit = true;
    }


    public String getStartTime() {
        return startTime;
    }

    public String getInformation() {
        return information;
    }

    public String getEndTime() {
        return endTime;
    }

    public TrainType getType() { return type; }

    public String getTimeInRoad() { return timeInRoad; }

    public String getTo() { return to; }

    public String getFrom() { return from; }

    public String getCode(){ return code; }

    public String getTitle(){ return title; }

    public boolean isInit(){ return isInit;}


    public void setFrom(String from) {
        this.from = from;
        isInit = true;
    }

    public void setTo(String to) { this.to = to; }

    public void setType(TrainType type) {
        this.type = type;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public void setCode(String code){ this.code = code; }

    public void setTitle(String title){ this.title = title; }

    public void setTimeInRoad(String timeInRoad){ this.timeInRoad = timeInRoad; }
    public String toString() {
        return startTime + " - " + endTime + "|" + type.getTitle() + "|" + this.getTitle() + " ";
    }







    public void writeToParcel(Parcel out, int flags) {
        //private TrainType type;

        out.writeString(String.valueOf(isInit));
        out.writeString(code);
        out.writeString(title);
        out.writeString(from);
        out.writeString(to);
        out.writeString(startTime);
        out.writeString(endTime);
        out.writeString(timeInRoad);
        out.writeString(information);
    }

    private TrainItem(Parcel in) {
        isInit = Boolean.valueOf(in.readString());
        code = in.readString();
        title = in.readString();
        from = in.readString();
        to = in.readString();
        startTime = in.readString();
        endTime = in.readString();
        timeInRoad = in.readString();
        information = in.readString();
        type = TrainType.International;
    }

    public static final Parcelable.Creator<TrainItem> CREATOR = new Parcelable.Creator<TrainItem>() {
        public TrainItem createFromParcel(Parcel in) {
            return new TrainItem(in);
        }

        public TrainItem[] newArray(int size) {
            return new TrainItem[size];
        }
    };
    public int describeContents() {
        return 0;
    }
}
