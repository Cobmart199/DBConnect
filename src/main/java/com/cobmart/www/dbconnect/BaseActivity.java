package com.cobmart.www.dbconnect;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;

/**
 * Created by Cyril O. Balonwu on 4/25/2018.
 */

public class BaseActivity extends AppCompatActivity {

    //public AppDatabase eventDatabase;
    RequestQueue requestQueue;
    static String username, password;

    Intent intent;
    static Event[] events;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
                        // @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        /*

        if(eventDatabase == null) {
            eventDatabase = Room.databaseBuilder(getApplicationContext(),
                    AppDatabase.class, "events.db")
                    .fallbackToDestructiveMigration()
                    .build();

        }
        */



        //Volley Library
        Cache cache = new DiskBasedCache( getCacheDir(),1024 * 1024);
        Network network = new BasicNetwork( new HurlStack());

        requestQueue = new RequestQueue( cache, network);
        requestQueue.start();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate the menu
        getMenuInflater().inflate(R.menu.menu, menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch ( item.getItemId()){
            case R.id.menuEdit :
                 intent = new Intent(this, EditActivity.class);
                startActivity(intent);
                toastIt("You Selected menuEdit");
                return true;

            case R.id.menuViewAll :
                 intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;


            case R.id.menuAllRecords :
                intent = new Intent(this,ViewAllWithJSON.class);
                startActivity(intent);
                toastIt("You selected WebService View Records");
                return true;

            default:
                return  super.onOptionsItemSelected(item);
        }

    }

    public void toastIt(String msg){
         Toast.makeText(getApplicationContext(),
                 msg, Toast.LENGTH_SHORT).show();
     }

}