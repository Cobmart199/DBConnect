
package com.cobmart.www.dbconnect;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class MainActivity extends BaseActivity {


    private EditText edtEventName;
    private EditText edtEventDesc;
    private EditText edtStartDate;
    private EditText edtEventPrice;
    private EditText edtEventRating;

   // private ListView listViewEvents;

    List<String> tempItems = new ArrayList<String>();
    ArrayAdapter adapter;
    Date startDate;

    Gson gson;

    //LiveData<List<String>>items;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtEventDesc = findViewById(R.id.edtEventDesc);
        edtEventName = findViewById(R.id.edtEventName);
        edtStartDate = findViewById(R.id.edtStartDate);
        edtEventPrice = findViewById(R.id.edtEventPrice);
        edtEventRating = findViewById(R.id.edtEventRating);
       // listViewEvents = findViewById(R.id.lstViewEvents);

        tempItems.add("Computers");
        tempItems.add("Keyboard");
        tempItems.add("Mouse");




        //Create an adapter for the listView events
       // adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, tempItems);
       // listViewEvents.setAdapter( adapter );
       // listViewEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      //  });


        //items = eventDatabase.eventDao().getAllNames();
       // items.observe(this, new Observer<List<String>>() {
          //  @Override
          //  public void onChanged(@Nullable List<String> eventNames) {
             //   adapter =
                      //  new ArrayAdapter<String>(
                               // getApplicationContext(),
                               // R.layout.activity_listview, eventNames);

                //listViewEvents.setAdapter( adapter );
                //notify the OS that the data changed to redraw everything
               // adapter.notifyDataSetChanged();
               // listViewEvents.invalidateViews();
               // listViewEvents.refreshDrawableState();
            //}
        //});


//        eventDatabase = Room.databaseBuilder(getApplicationContext(),
//                AppDatabase.class, "events.db")
//                .fallbackToDestructiveMigration()
//                .build();

    }


    public  void btnRedirect(View v){
        intent = new Intent(this, ViewAllWithJSON.class);
        toastIt("Redirecting to All Records With JSON");
        startActivity(intent);
    }

    public void internetOnClick(View v){

        String url = "http://api.lockersoft.com/api_test.php";
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, url,
                //call back
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //do something with the returned data
                        Log.d(" Internet", response);
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
        );

        requestQueue.add(stringRequest);
    }



    public void internetOnClickSingle(View v){

        String url = "https://api2018.azurewebsites.net/events/1";
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, url,
                //call back
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //do something with the returned data
                        Log.d(" Internet", response);
                        Event event = gson.fromJson(response, Event.class);
                        //Take data to display on the view
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
                Map<String, String> headers = new HashMap<String, String >();
                headers.put("Content-Type", "application/json");
                String credentials = username + ":" + password;
                Log.d("Auth", "Login Info:" + credentials);
                String auth = "Basic" + Base64.encodeToString( credentials.getBytes(), Base64.NO_WRAP);
                headers.put(" Authorization", auth);
                return headers;
            }
        };

        requestQueue.add(stringRequest);
    }



    public void saveEventOnClick( View v ){

        final String eventDescription  = edtEventDesc.getText().toString();
        final String eventName = edtEventName.getText().toString();
        final String eventPrice = edtEventPrice.getText().toString();
        final String eventRating = edtEventRating.getText().toString();


        String startDateStr = edtStartDate.getText().toString();


        SimpleDateFormat sdf = new SimpleDateFormat( "dd/mm/yyyy", Locale.ENGLISH);

        try{
             startDate = sdf.parse( startDateStr);
        } catch (ParseException e) {
            Log.d("EVENT", "Bad Date");
            startDate = new Date();
        }


        new Thread  (new Runnable() {
            @Override
            public void run() {
                Event event = new Event();
                event.setName(eventName);
                event.setDescription(eventDescription);

                //event.setPrice(Double.parseDouble(eventPrice));
                //event.setRating(Integer.parseInt(eventRating));

               // event.setStartDate(startDate);
               // event.setEndDate(new Date());
               // eventDatabase.eventDao().addEvent(event);
            }
        }).start();

        Intent intent = new Intent(this, ViewAll.class);
        startActivity(intent);
    }
}
