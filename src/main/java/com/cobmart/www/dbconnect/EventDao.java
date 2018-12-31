package com.cobmart.www.dbconnect;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface EventDao {
    @Query(" Select * From events")
    //List<Event> getAll();
    LiveData<List<Event>> getAll();

    @Query("Select name From events")
    LiveData<List<String>> getAllNames();

    @Query( "Select * FROM events WHERE name=:event_name LIMIT 1")
    Event findByName(String event_name);

    @Query("Select * from events where eventID = :eventID LIMIT 1")
    LiveData<Event>findByRecordNum(long eventID);
    //Event findByRecordNum(int eventID);


    @Insert
    void  addEvent(Event event);

    @Update
    void updateEvent(Event event);

    @Delete
    void deleteEvent(Event event);
}