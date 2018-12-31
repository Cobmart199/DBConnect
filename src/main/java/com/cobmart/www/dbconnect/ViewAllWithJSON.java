package com.cobmart.www.dbconnect;

import android.arch.lifecycle.LiveData;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewAllWithJSON extends BaseActivity {

    private ListView lstViewEvents;
    ArrayAdapter adapter;
    Date stareDate;
    Gson gson;

    LiveData<List<Event>> items;
    //LiveData<List<Event>> events;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_with_json);

        lstViewEvents = findViewById(R.id.lstViewEvents);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
        gson = gsonBuilder.create();

       // internetOnClick( null );
        LoginClick( null);


        lstViewEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                toastIt("You clicked on " + position + " Name: " + events[position] );

                intent = new Intent(getApplicationContext(),EditActivity.class);
                //intent.putExtra( "recordID", events[position].getId() );
                //intent.putExtra( "recordID", events[position].getEventID());
                intent.putExtra("recordID", events[position].getId());
                startActivity(intent);
            }
        });


    }


    public void internetOnClick(View v){
        username = "";
        password = "";
        //String url = "https://api2018.azurewebsites.net/events";//"http://api.lockersoft.com/api_test.php";
        String url= "http://localhost:3000/events/";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                //call back
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //do something with the returned data
                        Log.d(" Internet", response);
                        //events = gson.fromJson( response, Event[].class);
                        Event event = gson.fromJson( response, Event.class);
                        //Take data to display on the View
                        adapter = new ArrayAdapter<Event>( getApplicationContext(), R.layout.activity_listviewall, events);
                        lstViewEvents.setAdapter( adapter );
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Do something with the error
                        Log.d("Internet", error.toString());
                        toastIt("Internet Failure " + error.toString()          );
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                String credentials = username + ":" + password;
                String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.DEFAULT);
                params.put("Authorization", auth);
                return params;

            }

        };

        requestQueue.add(stringRequest);
    }





    public void LoginClick(View v){

        String url= "http://localhost:3000/events/";
        //JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Do something with the returned data
                        Log.d("Internet", response.toString());
                        Event event  = gson.fromJson(response.toString(), Event.class);

                        //Take data to display on the View
                        adapter = new ArrayAdapter<Event>( getApplicationContext(), R.layout.activity_listviewall, events);
                        lstViewEvents.setAdapter( adapter );

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Do something with the error
                        Log.d("Internet", error.toString());
                        toastIt("Internet Failure" + error.toString());
                    }
                }
        )/* {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                String credentials = userName + ":" + password;
                String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.DEFAULT);
                params.put("Authorization", auth);
                return params;

            }

        }*/;

        requestQueue.add(getRequest);

    }


}
