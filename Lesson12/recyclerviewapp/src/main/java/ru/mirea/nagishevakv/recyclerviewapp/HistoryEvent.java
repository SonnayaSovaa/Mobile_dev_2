package ru.mirea.nagishevakv.recyclerviewapp;

public class HistoryEvent {
    private String historyEventName;
    private String imgName;
    private int eventDate;
    public HistoryEvent(String historyEventName, String imgName, int eventDate) {
        this.historyEventName= historyEventName;
        this.imgName= imgName;
        this.eventDate= eventDate;
    }
    public int getEventDate() {
        return eventDate;
    }
    public String getHistoryEventName() {
        return historyEventName;
    }
    public String getImgName() {
        return imgName;
    }
    @Override
    public String toString() {
        return this.historyEventName+" (eventDate: "+ this.eventDate+")";
    }
}