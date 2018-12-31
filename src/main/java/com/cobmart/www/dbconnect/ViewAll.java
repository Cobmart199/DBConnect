package com.cobmart.www.dbconnect;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ViewAll extends BaseActivity {
    private ListView listViewEvents;

    List<String> tempItems = new ArrayList<String>();
    ArrayAdapter adapter;
    Date startDate;
    Gson gson;


    LiveData<List<String>> items;
    LiveData<List<Event>> events;


    Intent intent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);

        listViewEvents = findViewById(R.id.lstViewEvents);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("yyyy-MM-dd'T'HH:mm:ssX" );
        gson = gsonBuilder.create();

        //internetOnClick( null );


        //Create an adapter for the listView events
        // adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, tempItems);


       // events = eventDatabase.eventDao().getAll();

        events.observe(this, new Observer<List<Event>>() {
            @Override
            public void onChanged(@Nullable final List<Event> events) {


                for(Event e :events){
                   tempItems.add(e.getName() + "\n" + e.getDescription());
                }


                listViewEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        toastIt("You clicked on " + position);
                        //send the data to ReadRecord.

                         intent = new Intent(ViewAll.this, EditActivity.class);
                        //How do I use intent.putExtra(String, String);
                        //intent.putExtra("Result", events.get(position).getEventID());
                        startActivity(intent);

                    }
                });


                adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.activity_listview, tempItems);

                listViewEvents.setAdapter( adapter );
                //notify the OS that the data changed to redraw everything
                adapter.notifyDataSetChanged();
                listViewEvents.invalidateViews();
                listViewEvents.refreshDrawableState();


            }
        });


/*
        //        items = eventDatabase.eventDao().getAllNames();
        //        items.observe(this, new Observer<List<String>>() {
        //            @Override
        //            public void onChanged(@Nullable List<String> eventNames) {
        //                adapter =
        //                        new ArrayAdapter<String>(
        //                                getApplicationContext(),
        //                                R.layout.activity_listviewall, eventNames);
        //
        //                listViewEvents.setAdapter( adapter );
        //                //notify the OS that the data changed to redraw everything
        //                adapter.notifyDataSetChanged();
        //                listViewEvents.invalidateViews();
        //                listViewEvents.refreshDrawableState();
        //            }
        //        });


        //        eventDatabase = Room.databaseBuilder(getApplicationContext(),
        //                AppDatabase.class, "events.db")
        //                .fallbackToDestructiveMigration()
        //                .build();

 */

    }



    public void btnAddNewRecordClick(View v){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
