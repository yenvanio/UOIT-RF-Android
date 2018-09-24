package com.outsourced.shiv.uoitroomfinder.Models;

import com.google.gson.annotations.Expose;

import java.util.List;

public class FutureClass {

    @Expose
    private String room;
    @Expose
    private String start_time;
    @Expose
    private String end_time;
    @Expose
    private String title;
    @Expose
    private String code;

    public FutureClass(String room, String start_time, String end_time, String title, String code) {
        this.room = room;
        this.start_time = start_time;
        this.end_time = end_time;
        this.title = title;
        this.code = code;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public class FutureClassResult {

        @Expose
        private List<Class> classes;

        public FutureClassResult(List<Class> classes) {
            this.classes = classes;
        }

        public List<Class> getClasses() {
            return classes;
        }

        public void setClasses(List<Class> classes) {
            this.classes = classes;
        }

    }
}
