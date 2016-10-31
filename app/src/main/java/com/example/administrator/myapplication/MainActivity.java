package com.example.administrator.myapplication;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.os.StrictMode;

import java.lang.reflect.Field;
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
    AlertDialog dialog_login;
    AlertDialog dialog_register;
    ProgressDialog dialog_progress;

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
        final EditText Username = (EditText)v.findViewById(R.id.Username);
        final EditText Password = (EditText)v.findViewById(R.id.Password);



        dialog_login = new AlertDialog.Builder(this).setTitle("用户登录").setView(v).setPositiveButton(
                "登录",null).setNegativeButton("注册",null).setCancelable(false).create();


        dialog_login.show();

        dialog_login.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_login.hide();
                showProgressDialog("正在登陆");
                Connector conn = new Connector(Username.getText().toString(), Password.getText().toString());
                if(conn.Check_login()){
                    Log.d("Login-Msg", "LoginSucceed");
                    Toast.makeText(getApplicationContext(),"Login Succeed.",Toast.LENGTH_SHORT).show();
                    dialog_login.dismiss();
                }
                else{
                    Log.d("Login-Msg", "LoginFailed");
                    Toast.makeText(getApplicationContext(),"Login Failed.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog_login.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_login.hide();
                showRegisterDialog();
            }
        });


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

    void showProgressDialog(String message){
        dialog_progress = new ProgressDialog(this);
        dialog_progress.setMessage(message);
        dialog_progress.setCancelable(false);
        dialog_progress.show();
    }

    void dismissProgressDialog(){
        dialog_progress.dismiss();
    }


    void showRegisterDialog(){

        LayoutInflater inflater = getLayoutInflater();
        final View v = inflater.inflate(R.layout.register_dialog, null);
        //LET render login page first
        final EditText Username = (EditText)v.findViewById(R.id.Username_4_register);
        final EditText Password = (EditText)v.findViewById(R.id.Password_4_register);
        final EditText Password2 = (EditText)v.findViewById(R.id.Password2_4_register);

        dialog_register = new AlertDialog.Builder(this).setTitle("用户注册").setView(v).setPositiveButton(
                "确认",null).setNegativeButton("取消",null).setCancelable(false).create();

        dialog_register.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {

                dialog_register.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO: 2016/10/31  
                        //dialog_register.dismiss();

                    }
                });

                dialog_register.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO: 2016/10/31  
                        //dialog_register.dismiss();
                        //dialog_login.show();
                    }
                });
            }
        });

        dialog_register.show();
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
