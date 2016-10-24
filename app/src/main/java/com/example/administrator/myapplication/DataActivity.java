package com.example.administrator.myapplication;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;
import java.text.SimpleDateFormat;



public class DataActivity extends AppCompatActivity {

    Button btn_gps;
    Button btn_sound;
    Button btn_picture;
    boolean btn_gps_status;

    EditText content;
    EditText title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        btn_gps_status=true;

        btn_gps = (Button) findViewById(R.id.btn_gps);
        btn_sound = (Button) findViewById(R.id.btn_sound);
        btn_picture = (Button) findViewById(R.id.btn_picture);
        content = (EditText) findViewById(R.id.content);
        title = (EditText) findViewById((R.id.title));


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        title.setText(bundle.getString("title"));
        if(bundle.getString("date")==null){
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String d = format.format(new Date());
            getSupportActionBar().setTitle(d);
        }
        else {
            getSupportActionBar().setTitle("");
        }


        btn_gps.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                btn_gps_status=!btn_gps_status;
                if(btn_gps_status){
                    btn_gps.setBackground(getResources().getDrawable(R.drawable.ic_location_on_black_24dp));
                    Toast.makeText(getApplicationContext(),"turn loaction on",Toast.LENGTH_SHORT).show();
                }
                else {
                    btn_gps.setBackground(getResources().getDrawable(R.drawable.ic_location_off_black_24dp));
                    Toast.makeText(getApplicationContext(),"turn loaction off",Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        btn_gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btn_gps_status) {
                    Intent intent = new Intent(getApplicationContext(), GpsActivity.class);
                    startActivity(intent);
                }
            }
        });

        btn_sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SoundActivity.class);
                startActivity(intent);
            }
        });

        btn_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),PictureActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onBackPressed() {
        String text = title.getText().toString();
        if(!text.isEmpty()){
            Intent intent = getIntent();
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
            String d = format.format(new Date());
            intent.putExtra("date",format.format(new Date()));
            intent.putExtra("title",text);
            setResult(RESULT_OK,intent);
        }
        finish();
        super.onBackPressed();
    }
}
