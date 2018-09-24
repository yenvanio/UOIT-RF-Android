package com.outsourced.shiv.uoitroomfinder.Models;

import com.google.gson.annotations.Expose;

import java.util.Date;
import java.util.List;

public class RoomSchedule {

    @Expose
    private String day;
    @Expose
    private Date start_date;
    @Expose
    private String start_time;
    @Expose
    private Date end_date;
    @Expose
    private String end_time;

    public RoomSchedule(String day, Date start_date, String start_time, Date end_date, String end_time) {
        this.day = day;
        this.start_date = start_date;
        this.start_time = start_time;
        this.end_date = end_date;
        this.end_time = end_time;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public class RoomScheduleResult {

        @Expose
        private List<RoomSchedule> classes;

        public RoomScheduleResult(List<RoomSchedule> classes) {
            this.classes = classes;
        }

        public List<RoomSchedule> getRoomSchedule() {
            return classes;
        }

        public void setRoomSchedule(List<RoomSchedule> classes) {
            this.classes = classes;
        }

    }
}