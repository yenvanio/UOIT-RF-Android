package com.outsourced.shiv.uoitroomfinder.Models;

import android.location.Location;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Class implements Serializable {

    @Expose
    private String room;
    @Expose
    private String building;
    @Expose
    private String location;
    @Expose
    private String isLab;

    @Expose
    private String code;
    @Expose
    private String title;
    @Expose
    private String start_time;
    @Expose
    private String end_time;

    private String duration;
    private List<Location> locations = new ArrayList<>();


    public Class(String isLab, String room, String building, String location) {
        this.isLab = isLab;
        this.room = room;
        this.building = building;
        this.location = location;
    }

    public String getIsLab() {
        return isLab;
    }

    public void setIsLab(String isLab) {
        this.isLab = isLab;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCourse() {
        return title;
    }

    public void setCourse(String course) {
        this.title = course;
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

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Location> getLocations() {
        return locations;
    }

        public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public class ClassResult {

        @Expose
        private List<Class> classes;

        public ClassResult(List<Class> classes) {
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
