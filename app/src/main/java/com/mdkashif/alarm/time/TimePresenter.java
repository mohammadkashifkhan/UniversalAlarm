package com.mdkashif.alarm.time;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Kashif on 24-Apr-18.
 */
public class TimePresenter {
    List<String> hours,minutes,type;
    List<String> days;
    String[] arrDays;
    TimePresenterCallback timePresenterCallback;

    public TimePresenter(TimePresenterCallback timePresenterCallback) {
        this.timePresenterCallback=timePresenterCallback;
        hours= new ArrayList<>();
        minutes= new ArrayList<>();
        type= new ArrayList<>();
        days= new ArrayList<>();
        arrDays = new String[]{"M", "T", "W", "T", "F", "S", "S"};
    }

    public void setDataForTimeAndDays(){
        for (int i = 1; i <= 12; i++) {
            hours.add(i+"");
        }
        for (int i = 1; i <= 60; i++) {
            minutes.add(i+"");
        }
        days= Arrays.asList(arrDays);
        type.add("AM");
        type.add("PM");
        timePresenterCallback.onDaysOfWeekSuccess(hours,minutes,type,days);
    }

    public interface TimePresenterCallback {
        void onDaysOfWeekSuccess(List<String> hours,List<String> minutes,List<String> type,List<String> days);
    }
}
