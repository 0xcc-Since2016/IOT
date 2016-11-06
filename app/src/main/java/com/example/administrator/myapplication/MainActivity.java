package com.example.administrator.myapplication;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuView;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    SimpleAdapter adapter;
    List<Map<String,Object>> mapList;
    Button btn_add;
    int choose_item_position;
    AlertDialog dialog_login;
    AlertDialog dialog_register;
    ProgressDialog dialog_progress;
    String username = "";

    public final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                showProgressDialog("正在登录...");

                //Init Request thread
                username = Username.getText().toString();
                HttpLoginThread login = new HttpLoginThread(Username.getText().toString(),
                                                            Password.getText().toString(),
                                                            Settings.login_url,
                                                            "POST");
                login.start();
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
        //FileUpload thread

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
                //GetFrom DataActivity
                intent.putExtra("content", mapList.get(position).get("content").toString());
                intent.putExtra("resource_id",id);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                Log.d(TAG, "onItemClick: " + id);
                Log.d(TAG, "POSITION: " + position);
                startActivityForResult(intent,0);
            }
        });

        //Delete Item in list.
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

                //Adding Data, drag to dataActivity.
                //Establish new Activity:
                //new Intent from Original to Child.
                Intent intent = new Intent(getApplicationContext(),DataActivity.class);
                intent.putExtra("position",-1);
                startActivityForResult(intent,0);
            }
        });

        this.initListView();
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
            public void onShow(final DialogInterface dialog) {

                dialog_register.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String repasswd = Password2.getText().toString();
                        String passwd   = Password.getText().toString();
                        if (!repasswd.equals(passwd)){
                            //Two Passwords are not in consistence state!
                            Toast.makeText(getApplicationContext(), "两次输入密码不一致",Toast.LENGTH_SHORT).show();
                            Password.setText("");
                            Password2.setText("");

                        }else {
                            dialog_register.hide();
                            showProgressDialog("正在注册...");
                            HttpLoginThread register = new HttpLoginThread(Username.getText().toString(),
                                    Password.getText().toString(),
                                    Settings.register_url,
                                    "POST");
                            register.start();
                        }
                    }
                });

                dialog_register.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO: 2016/10/31  
                        //dialog_register.dismiss();
                        //dialog_login.show();
                        dialog_register.hide();
                        dialog_login.show();
                    }
                });
            }
        });
        dialog_register.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.main,menu);
//        MenuView.ItemView uploadItem = (MenuView.ItemView)menu.findItem(R.id.backup);
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

            case R.id.backup:

                Toast.makeText(getApplicationContext(), "Uploading Files...", Toast.LENGTH_SHORT).show();
                DataStore dtst = new DataStore();
                try {
                    InstantUpload instantup = new InstantUpload(username, Settings.label_file,Settings.folder + Settings.label_file);
                    instantup.start();
                }catch(Exception e){
                    Log.d("[*]FILEUPLOADERROR", e.toString());
                }

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
                    updateView(bundle.getInt("position"),bundle.getString("title"),bundle.getString("date"), bundle.getString("content"));
                }
        }
    }

    private void updateView(int position,String new_title,String new_date, String new_content){

        Map<String,Object> map;
        if(position!=-1)
            map = mapList.remove(position);
        else
            map = new HashMap<String, Object>();
        map.put("title",new_title);
        map.put("date",new_date);
        map.put("content", new_content);
        mapList.add(0,map);
        printOutMapList(mapList);
        DataStore StoreData = new DataStore();
        try {

            String fileContent = StoreData.getFromFile(Settings.folder + Settings.label_file);
            Log.d("[*]LABELFILECONTENT", fileContent);

        }catch (Exception e){
            Log.d("[*]READFILEERROR", e.toString());
        }

        adapter.notifyDataSetChanged();

    }

    public void initListView(){

        try {

            DataStore StoreData = new DataStore();
            String fileContent = StoreData.getFromFile(Settings.folder + Settings.label_file);
            Log.d("[*]FILECONTENT", fileContent);
            String[] res      = fileContent.split("\n");
            for(int i=0; i < res.length; i++){

                String temp       = res[i];
                String[] res_temp = temp.split("\\|\\|\\|");

                Log.d("[*]VALUES" , res_temp.toString());
                String title      = res_temp[0];
                String content    = res_temp[1];
                String date       = res_temp[2];
                Log.d("[*]VALUES" , title + "," + content + "," + date);
                Map<String,Object> map = new HashMap<String, Object>();
                map.put("title",   title);
                map.put("date",    date);
                map.put("content", content);
                mapList.add(0,map);
            }
            adapter.notifyDataSetChanged();
        }catch(Exception e){
            Log.d("[*]ERRORREADLABELFILE", e.toString());
        }
    }

    //Debug Method
    public void printOutMapList(List mapList){

        Iterator it = mapList.iterator();
        while(it.hasNext()){

            Map<String,Object> tempmap = (Map<String, Object>) it.next();
            Log.d("[*]MapList", tempmap.get("title").toString());
            Log.d("[*]MapList", tempmap.get("content").toString());
            Log.d("[*]MapList", tempmap.get("date").toString());
            Log.d("[*]NextOne", "\n");

        }

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

    private class HttpLoginThread extends Thread{

        String username     = "uname=";
        String password     = "upwd=";
        String values       = "";
        String target_url   = "";
        String method       = "";

        public HttpLoginThread(String username , String password, String target_url, String method){
            this.username += Encryption.encrypt(username) ;
            this.password += Encryption.encrypt(password) ;
            Log.d(TAG, "HttpLoginThread: "+this.username);
            Log.d(TAG, "HttpLoginThread: "+this.password);
            this.values   += this.username + "&" + this.password;
            this.target_url= target_url;
            this.method    = method;
        }
        @Override
        public void run(){

            Connector conn = new Connector(this.target_url);
            String res     = "";
            if(method.equals("GET")) {
                res = conn.launchGETReq();
            }else{
                res = conn.launchPOSTReq(values);
            }

            Looper mainLooper = Looper.getMainLooper();
            //Get A handler of mainThread
            Loginhandler mainhandler = new Loginhandler(mainLooper);
            //Remove all msgs in msg-queue
            mainhandler.removeMessages(0);
            Message message = mainhandler.obtainMessage(1,1,1,res);
            mainhandler.sendMessage(message);
        }
    }

    private class Loginhandler extends Handler{

        public Loginhandler(Looper looper)  {super(looper);}
        @Override
        public void handleMessage(Message msg){

            //Handle when get msg.
            String server_res = msg.obj.toString();
            //Determine if Login-process is succeed or not.
            Log.d("[*]Tag", server_res);
            if (server_res.equals("REGOK")){

                //Register oK.

                Log.d("Reg-Msg", "RegisterSuccess");
                Toast.makeText(getApplicationContext(),"Register Succeed.",Toast.LENGTH_SHORT).show();
                dialog_progress.hide();
                dialog_login.show();

            }
            else if(server_res.equals("REGError")){

                Log.d("Reg-Msg", "RegisterError");
                Toast.makeText(getApplicationContext(),"Register Failed.",Toast.LENGTH_SHORT).show();
                dialog_progress.hide();
                dialog_register.show();
            }
            else if(server_res.equals("OK")) {

                    Log.d("Login-Msg", "LoginSucceed");
                    Toast.makeText(getApplicationContext(), "Login Succeed.", Toast.LENGTH_SHORT).show();
                    dialog_progress.hide();
                    dialog_login.hide();
                    FileUpload flupload = new FileUpload(username);
                    flupload.start();

            }
            else if(server_res.equals("Error")){
                Log.d("Login-Msg", "LoginFailed");
                Toast.makeText(getApplicationContext(),"Login Failed.",Toast.LENGTH_SHORT).show();
                dialog_progress.hide();
                dialog_login.show();
                }
            }
        }

    @Override
    protected  void onStop(){

        //KeepData locally.
        super.onStop();
        String res = "";
        for(int i = 0 ; i < mapList.size(); i++){

            Map<String,Object>  map = mapList.get(i);
            String piece = map.get("title") + "|||" +
                           map.get("content") + "|||" +
                           map.get("date")      + "\n";

            res += piece;
        }

        DataStore dataStore = new DataStore();
        try {
            dataStore.flushToFile(res.getBytes("UTF-8"), Settings.folder + Settings.label_file);
        }catch(Exception e){
            Log.d("[*]SAVETOFILEERROR", e.toString());
        }
    }
}
