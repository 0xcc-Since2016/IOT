package com.example.administrator.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GpsActivity extends AppCompatActivity {
    
    public final String TAG = GpsActivity.class.getSimpleName();

    TextView longitude;
    TextView latitude;
    Button btn_loacte;
    GridView gridView;
    List<Map<String,Object>> mapList;
    SimpleAdapter adapter;
    Location location;
    LocationManager locationManager;
    int choose_item_position;

    final String LOG_TAG = GpsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);


        locationManager =(LocationManager) getSystemService(Context.LOCATION_SERVICE);
        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);


        mapList = new ArrayList<Map<String, Object>>();
        adapter = new SimpleAdapter(this,mapList,R.layout.location_item,new String[]{"identifier"},new int[]{R.id.location_identifier});
        gridView = (GridView) findViewById(R.id.gridview4location);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String loaction_in_string = (String) mapList.get(position).get("identifier");
                Uri uri = Uri.parse(loaction_in_string);
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Log.d(LOG_TAG, "Couldn't call " + location + ", no receiving apps installed!");
                }

            }
        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                choose_item_position = position;
                delete_item();
                return true;
            }
        });

        btn_loacte = (Button) findViewById(R.id.locate_now);

        btn_loacte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (location!=null) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    StringBuilder sb = new StringBuilder();
                    sb.append("geo:");
                    sb.append(location.getLatitude());
                    sb.append(",");
                    sb.append(location.getLongitude());
                    map.put("identifier", sb.toString());
                    mapList.add(map);
                    adapter.notifyDataSetChanged();
                }
                else{
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("identifier", "test");
                    mapList.add(map);
                    adapter.notifyDataSetChanged();
                }
            }
        });


        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 2, new LocationListener() {
            @Override
            public void onLocationChanged(Location location_new) {
                location=location_new;
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        });
    }

    private void delete_item(){
        new AlertDialog.Builder(this).setTitle("Are you 确定").setMessage("是否删除这个项目？").setPositiveButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mapList.remove(choose_item_position);
                adapter.notifyDataSetChanged();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setCancelable(false).show();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setting:
                //pass
        }
        return super.onOptionsItemSelected(item);
    }


}

