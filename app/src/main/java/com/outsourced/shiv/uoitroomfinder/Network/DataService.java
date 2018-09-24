package com.outsourced.shiv.uoitroomfinder.Network;

import com.outsourced.shiv.uoitroomfinder.Models.Class.ClassResult;
import com.outsourced.shiv.uoitroomfinder.Models.Room.RoomResult;
import com.outsourced.shiv.uoitroomfinder.Models.FutureClass.FutureClassResult;
import com.outsourced.shiv.uoitroomfinder.Models.RoomSchedule.RoomScheduleResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DataService {

    @GET("class/all")
    Call<ClassResult> getAllClasses();

    @GET("class/all")
    Call<ClassResult> getClassesByParam(@Query("date") String date,
                                        @Query("start_time") String start_time, @Query("end_time") String end_time);

    @GET("room/all")
    Call<RoomResult> getAllRooms();

    @GET("room/schedule")
    Call<RoomScheduleResult> getRoomSchedule(@Query("room") String room);

    @GET("class/future")
    Call<FutureClassResult> getFutureClassesByParam(@Query("room") String room, @Query("date") String date,
                                                                @Query("start_time") String start_time);
}
