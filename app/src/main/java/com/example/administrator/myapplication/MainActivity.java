package com.example.administrator.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.os.StrictMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    SimpleAdapter adapter;
    List<Map<String,Object>> mapList;
    Button btn_add;
    int choose_item_position;

    public final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork()
                .penaltyLog().build());
        LayoutInflater inflater = getLayoutInflater();
        final View v = inflater.inflate(R.layout.login_dialog, null);
        //LET render login page first
        new AlertDialog.Builder(this).setTitle("用户登录").setView(v).setPositiveButton(
                "登录", new DialogInterface.OnClickListener() {
                    //Because Cannot Acquire View handler directly
                    //So ReInit a view and set it points to target view.
                    //Duplicate here!

                    EditText Username = (EditText)v.findViewById(R.id.Username);
                    EditText Password = (EditText)v.findViewById(R.id.Password);


                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //deal with the login event



                                Connector conn = new Connector(Username.getText().toString(), Password.getText().toString());
                            if(conn.Check_login()){
                                    Toast.makeText(getApplicationContext(),"Login Succeed.",Toast.LENGTH_SHORT).show();
                                }else {
                            //Bounce a dialog and exit app.
                                new AlertDialog.Builder(MainActivity.this).setTitle("系统提示").setMessage("登录失败").setPositiveButton("确定",new DialogInterface.OnClickListener(){

                                    @Override
                                    public void onClick(DialogInterface dialog, int which){
                                        //Exit app.
                                        return;
                                    }
                                });
                        }
                    }
                }

        ).setNegativeButton("注册", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //deal with the register event
            }
        }).setCancelable(false).show();


        //INIT init thread
        InitThread Initth = new InitThread();
        Initth.start();


        listView = (ListView) findViewById(R.id.item_list);
        mapList = new ArrayList<Map<String, Object>>();
        btn_add = (Button) findViewById(R.id.add_but);


        adapter = new SimpleAdapter(this,mapList,R.layout.item_layout,new String[]{"title","date"},
                new int[]{R.id.title,R.id.date});

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),DataActivity.class);
                TextView title = (TextView) view.findViewById(R.id.title);
                intent.putExtra("position",position);
                intent.putExtra("title",title.getText().toString());
                intent.putExtra("resource_id",id);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                Log.d(TAG, "onItemClick:     "+id);
                startActivityForResult(intent,0);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                choose_item_position = position;
                delete_item();
                return true;
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),DataActivity.class);
                intent.putExtra("position",-1);
                startActivityForResult(intent,0);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.change_passwd:
                new AlertDialog.Builder(this).setTitle("密码修改").setView(R.layout.change_password_dialog).setPositiveButton(
                        "确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //deal with the login event
                            }
                        }
                ).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //deal with the register event
                    }
                }).show();

            case R.id.log_out:
                //to logout
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 0:
                if(resultCode==RESULT_OK){
                    Bundle bundle = data.getExtras();
                    updateView(bundle.getInt("position"),bundle.getString("title"),bundle.getString("date"));
                }
        }
    }


    private void updateView(int position,String new_title,String new_date){
        Map<String,Object> map;
        if(position!=-1)
            map = mapList.remove(position);
        else
            map = new HashMap<String, Object>();
        map.put("title",new_title);
        map.put("date",new_date);
        mapList.add(0,map);
        adapter.notifyDataSetChanged();
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

}
