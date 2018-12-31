package com.cobmart.www.dbconnect;

import android.app.AlertDialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EditActivity extends BaseActivity {

    Event eventDelete;
    Date startDate;
    EditText edtName, edtDescription, edtPrice, edtRating;
    LiveData<Event> event;
    ImageView imageView;
    Long position;
    private static final int REQUEST_CAPTURE_IMAGE = 42;
    String imageFilePath;
    Observer<Event> eventObserver;
    Intent intent;
    int recordID;
//    Long position;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        edtName = findViewById(R.id.edtName);
        edtDescription = findViewById(R.id.edtDescription);
        edtPrice = findViewById(R.id.edtEventPrice);
        edtRating = findViewById(R.id.edtEventRating);
        imageView = findViewById(R.id.imageView);
        imageView.setClickable(true);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toastIt("Openning the camera");
            }
        });

        imageView.setImageResource(R.drawable.omm);
        imageView.setClickable(true);
        imageView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v){
                toastIt("Opening the Camera");
                openCameraIntent();
            }
        });

        //LiveData

        //Look at the bundle and get Extra
        if(getIntent().getExtras() != null) {
           recordID = getIntent().getExtras().getInt("recordID");
        }


        edtName.setText( events[recordID].getName());
        edtDescription.setText( events[recordID].getDescription());


        // position = intent.getLongExtra("Result", 0);
//        intent = getIntent();
//        position = intent.getLongExtra("Result", 0);
//
//        //event = eventDatabase.eventDao().findByRecordNum( 3 ); //Fix this not to be hard coded
//        event = eventDatabase.eventDao().findByRecordNum( position );
//
//
//        event.observe(this, new Observer<Event>() {
//            @Override
//
//            public void onChanged(@Nullable Event event) {
//                if(event != null){
//                    eventDelete = event;
//                    edtName.setText(event.getName());
//                    edtDescription.setText(event.getDescription());
//                    edtPrice.setText(Double.toString(event.getPrice()));
//                    edtRating.setText(Integer.toString(event.getRating()));
//                }
//            }
//        });

    }


    private File createImageFile() throws IOException{
        //Create the image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format( new Date());
        String imageFileName = " JPEG_" + timeStamp +  "_";
        File storeDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES );
        //File image = File.createTempFile("Photo", "UrPhoto");
        File image = File.createTempFile(
                imageFileName,
                "jpg",
                storeDir
        );

        return image;

    }




    private  void openCameraIntent(){
        Intent pictureIntent =  new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(pictureIntent.resolveActivity( getPackageManager()) != null ){

            File photoFile = null;
            //Create a file on the SD card to store the  image data

            try{
                photoFile = createImageFile();
            }catch (IOException ex){
                toastIt("File Error");
            }

            if(photoFile !=null ){
                Uri photoURI = FileProvider.getUriForFile( this,
                        "com.lockersoft.androidmobilespring2018.fileprovider", photoFile);
                pictureIntent.putExtra( MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult( pictureIntent, REQUEST_CAPTURE_IMAGE);
            }

        }

    }

    @Override
    protected void onActivityResult(int resultCode, int requestCode, Intent data ){
        super.onActivityResult(resultCode, requestCode, data);

        if(requestCode == REQUEST_CAPTURE_IMAGE && resultCode == RESULT_OK &&
                data != null && data != null){
                //data != null && data.getData() != null){
               //I have an image
              // Bitmap bitmap = (Bitmap)data.getExtras().get("data");
             //  imageView.setImageBitmap( bitmap);

            //store the image in the SD Card etc
        }

    }

    public void deleteOnClick(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder( this );
        builder.setMessage("Are you sure you want to delete this event")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        //Perform something when they click YES
                        //Delete Record
                        toastIt("Record Deleted");

                        //Remove event observer

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //eventDatabase.eventDao().deleteEvent(eventDelete);

                                intent = new Intent(EditActivity.this, ViewAll.class); //same as getApplicationContext()
                                EditActivity.this.startActivity(intent);
                            }
                        }).start();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Perform something when they say No - Cancel
                        dialogInterface.cancel();
                        toastIt(" You pressed NO");
                    }
                })
                .create()
                .show();
    }


    public void deleteWithJSON(View v){
        String url = "https://api2018.azurewebsites.net/events" + 12;
        //POST that JSON object to the server using VOLLEY
        StringRequest request = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("EVENT", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("EVENT", error.toString());
                    }
                }
        );

        requestQueue.add( request);

        }


    public void onSaveChanges(View v){
        //Event event = new Event();
        event.getValue().setName(edtName.getText().toString());
        event.getValue().setDescription(edtDescription.getText().toString());
       // event.getValue().setPrice(Double.parseDouble(edtPrice.getText().toString()));
        event.getValue().setRating(Integer.parseInt(edtRating.getText().toString()));
        //event.getValue().setStartDate(new Date());
       // event.getValue().setEndDate(new Date());
        new Thread(new Runnable() {
            @Override
            public void run() {
                //eventDatabase.eventDao().updateEvent(event.getValue());
            }
        }).start();

    }


    public void saveEventOnClick( View v ){

        final String eventDescription  = edtDescription.getText().toString();
        final String eventName = edtName.getText().toString();
        final String eventPrice = edtPrice.getText().toString();
        final String eventRating = edtRating.getText().toString();


        //String startDateStr = edtStartDate.getText().toString();


        SimpleDateFormat sdf = new SimpleDateFormat( "dd/mm/yyyy", Locale.ENGLISH);

//        try{
//           // startDate = sdf.parse( startDateStr);
//        } catch (ParseException e) {
//            Log.d("EVENT", "Bad Date");
//            //startDate = new Date();
//        }


        new Thread  (new Runnable() {
            @Override
            public void run() {
                Event event = new Event();
                event.setName(eventName);
                event.setDescription(eventDescription);

                //event.setPrice(Double.parseDouble(eventPrice));
                event.setRating(Integer.parseInt(eventRating));

                //event.setStartDate(startDate);
                //event.setStartDate(new Date());
                //event.setEndDate(new Date());
                //eventDatabase.eventDao().addEvent(event);
               // eventDatabase.eventDao().updateEvent(event);
            }
        }).start();

        Intent intent = new Intent(this, ViewAll.class);
        startActivity(intent);
    }


    public void saveEventWithJSON( View v){
        Date date = new Date();
        String url = "https://api2018.azurewebsites.net/events";
        final String eventDescription = edtDescription.getText().toString();
        final String  eventName = edtName.getText().toString();
        String StartDateStr = date.toString();
        Log.d("EVENT,","StartDate: "  +  StartDateStr );

        SimpleDateFormat sdf = new SimpleDateFormat( "dd/mm/yyyy", Locale.ENGLISH);

        try{
            startDate = sdf.parse( StartDateStr);
        }catch (ParseException e){
            Log.d("EVENT", "Bad Date");
            startDate = new Date();
        }

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("name", eventName);
        params.put("description", eventDescription);

        // POST that JSON object to the server using VOLLEY

        JsonObjectRequest request = new JsonObjectRequest(url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("EVENT", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("EVENT",error.toString());
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

        requestQueue.add(request);
    }


    public void LoginClick(View v){

        //String url = "http://www.battlegameserver.com/api/v1/login.json";
        String url = "https://api2018.azurewebsites.net/events";
        //JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Do something with the returned data
                        Log.d("Internet", response.toString());
                        //userPrefs = gson.fromJson(response.toString(), UserPreferences.class);

                        //Intent intent = new Intent( getApplicationContext(), GameLobbyActivity.class);
                       // startActivity(intent);

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
        ) {
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

        requestQueue.add(getRequest);

    }








}
