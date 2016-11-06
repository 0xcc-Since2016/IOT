package com.example.administrator.myapplication;

import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class SoundActivity extends AppCompatActivity {
    Button record;
    GridView gridView;

    List<Map<String,Object>> mapList;
    SimpleAdapter adapter;

    int choose_item_position;
    private File file;
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;

    public final String TAG = SoundActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound);

        record = (Button) findViewById(R.id.btn_record);

        mapList = new ArrayList<Map<String, Object>>();
        adapter = new SimpleAdapter(this,mapList,R.layout.sound_item,new String[]{"identifier"},new int[]{R.id.sound_identifier});
        gridView = (GridView) findViewById(R.id.gridview4sound);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mediaPlayer = new MediaPlayer();
                try {
                    Log.d("[*]VISITPOSITION:", position+"");
                    Map<String,Object> temp;
                    temp = mapList.get(position);
                    File visitfile = new File(temp.get("identifier").toString());
                    Log.d("[*]GOINGTOPLAY", visitfile.getAbsolutePath());
                    mediaPlayer.setDataSource(visitfile.getAbsolutePath());
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
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

        record.setOnTouchListener(new View.OnTouchListener() {
            @Override
            //Start Record here when Touch DOWN Recording Start.
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    try {

                        String filename = String.valueOf(System.currentTimeMillis()/1000);
                        file = new File(Settings.folder + Settings.soundrec_folder + "/" + filename);
                        file.createNewFile();
                        Log.d("[*]SHOWFILEPATH", file.getAbsolutePath());
                        mediaRecorder = new MediaRecorder();
                        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
                        mediaRecorder.setOutputFile(file.getAbsolutePath());
                        mediaRecorder.prepare();
                        mediaRecorder.start();
                        Toast.makeText(getApplicationContext(),"start record!",Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                //Finish Record when Touch UP , Recording finish.
                //Need to add a constraint to ensure ACTION_UP is ahead of ACTION_Down.

                else if(event.getAction() == MotionEvent.ACTION_UP) {
                    try {
                        if (mediaRecorder != null) {
                            mediaRecorder.stop();
                            mediaRecorder.release();
                            mediaRecorder = null;
                            Toast.makeText(getApplicationContext(), "stop record!", Toast.LENGTH_SHORT).show();
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("identifier", file.getAbsolutePath());
                            mapList.add(map);
                            adapter.notifyDataSetChanged();
                        }
                    } catch(Exception e){
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "stop record!", Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            }
        });

        InitGridView();
    }

    private void delete_item(){
        new AlertDialog.Builder(this).setTitle("Are you 确定").setMessage("是否删除这个项目？").setPositiveButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Delete directly is supported.
                Map<String,Object> map;
                map = mapList.remove(choose_item_position);
                String filename = map.get("identifier").toString();
                File filetodel  = new File(filename);
                filetodel.delete();
                adapter.notifyDataSetChanged();

            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setCancelable(false).show();

    }

    public void InitGridView(){

        File initFile       = new File(Settings.folder + Settings.soundrec_folder);
        String[] filelist   = initFile.list();
        for (int i = 0 ; i < filelist.length; i++){

            String temp = Settings.folder + Settings.soundrec_folder + "/" + filelist[i];
            Map<String, Object> map = new HashMap<String,Object>();
            map.put("identifier", temp);
            mapList.add(map);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onPause(){

        super.onPause();
        //Store mapList's Items into Files.

    }


}
