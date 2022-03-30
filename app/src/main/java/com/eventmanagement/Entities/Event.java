package com.eventmanagement.Entities;

import android.os.Parcel;
import android.os.Parcelable;

public class Event implements Parcelable {
    String key;
    String upperKey;
    String eventName;
    String description;

    protected Event(Parcel in) {
        key = in.readString();
        upperKey = in.readString();
        eventName = in.readString();
        description = in.readString();
        date = in.readString();
        time = in.readString();
        location = in.readString();
        orgId = in.readString();
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUpperKey() {
        return upperKey;
    }

    public void setUpperKey(String upperKey) {
        this.upperKey = upperKey;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    String date;
    String time;
    String location;
    String orgId;

    public Event() {
    }

    public Event(String eventName, String description, String date, String time, String location, String orgId) {
        this.eventName = eventName;
        this.description = description;
        this.date = date;
        this.time = time;
        this.location = location;
        this.orgId = orgId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(upperKey);
        dest.writeString(eventName);
        dest.writeString(description);
        dest.writeString(date);
        dest.writeString(time);
        dest.writeString(location);
        dest.writeString(orgId);
    }
}
