package com.ads.sapp.call.api.event;

import com.ads.sapp.call.api.IsError;

import java.util.ArrayList;

public class EventData {
    private ArrayList<EventModel> eventModels;
    private IsError isError;

    public EventData() {
    }

    public EventData(ArrayList<EventModel> eventModels, IsError isError) {
        this.eventModels = eventModels;
        this.isError = isError;
    }

    public ArrayList<EventModel> getEventModel() {
        return eventModels;
    }

    public void setEventModel(ArrayList<EventModel> eventModels) {
        this.eventModels = eventModels;
    }

    public IsError getIsError() {
        return isError;
    }

    public void setIsError(IsError isError) {
        this.isError = isError;
    }
}
