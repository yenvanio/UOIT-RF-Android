package com.outsourced.shiv.uoitroomfinder.Models;

import android.location.Location;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class Room {

    @Expose
    private String room;
    @Expose
    private String building;
    @Expose
    private String location;

    public Room(String room, String building, String location) {
        this.room = room;
        this.building = building;
        this.location = location;
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

    public class RoomResult {

        @Expose
        private List<Room> rooms;

        public RoomResult(List<Room> rooms) {
            this.rooms = rooms;
        }

        public List<Room> getRooms() {
            return rooms;
        }

        public void setRooms(List<Room> rooms) {
            this.rooms = rooms;
        }

    }
}
