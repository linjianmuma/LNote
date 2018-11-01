package com.hqyxjy.ldf.supercalendar;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import sdatabase.MyDatabaseHelper;
import sdatabase.SDatabase;
import sdatabase.Schedule;

public class NewSchedule extends AppCompatActivity {
    private SDatabase sDatabase = new SDatabase(this);
    private MyDatabaseHelper myDatabaseHelper;
    private SQLiteDatabase mydatabase;
    private EditText et_title;
    private EditText et_content;
    private Schedule schedule;
    int sid;
    String sday;
    ImageButton ib_save;
    ImageButton ib_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_schedule);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        myDatabaseHelper = new MyDatabaseHelper(this);
        et_title=(EditText)findViewById(R.id.et_stitle);
        et_content=(EditText)findViewById(R.id.et_scontent);
        Intent intent = this.getIntent();
        sid = intent.getIntExtra("sid", 0);
        sday = intent.getStringExtra("sday");
        if (sid !=0){
            schedule = sDatabase.getTiandCon(sid);
            et_title.setText(schedule.getTitle());
            et_content.setText(schedule.getContent());
        }
        //保存并退出
        ib_save = (ImageButton)findViewById(R.id.ib_save);
        ib_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = et_title.getText().toString();
                if(title.equals(""))
                {
                    Toast.makeText(NewSchedule.this,"请输入任务名",Toast.LENGTH_LONG).show();
                }else{
                    isSave();
                }
            }
        });
        //退出
        ib_back = (ImageButton)findViewById(R.id.ib_back);
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    public void onBackPressed() {     //重写返回建方法，如果是属于新建则插入数据表并返回主页面，如果是修改，修改表中数据并返回主页面
        String title = et_title.getText().toString();
        String content = et_content.getText().toString();
        if(title.equals(""))
        {
            title = "无标题";
        }
        if(sid!=0){
            schedule=new Schedule(title, sid, content, sday);
            sDatabase.toUpdate(schedule);
            Intent intent=new Intent(NewSchedule.this,SyllabusActivity.class);
            startActivity(intent);
            NewSchedule.this.finish();
        }
        //新建日记
        else{
            schedule=new Schedule(title,content,sday);
            sDatabase.toInsert(schedule);
            Intent intent=new Intent(NewSchedule.this,SyllabusActivity.class);
            startActivity(intent);
            NewSchedule.this.finish();
        }

    }
    private void isSave(){   //写一个方法进行调用，如果是属于新建则插入数据表并返回主页面，如果是修改，修改表中数据并返回主页面
        String title = et_title.getText().toString();
        String content = et_content.getText().toString();
        if(title.equals(""))
        {
            title = "无标题";
        }
        if(sid!=0){
            schedule=new Schedule(title,sid, content, sday);
            sDatabase.toUpdate(schedule);
            Intent intent=new Intent(NewSchedule.this,SyllabusActivity.class);
            startActivity(intent);
            NewSchedule.this.finish();
        }
        //新建日记
        else{
            schedule=new Schedule(title,content,sday);
            sDatabase.toInsert(schedule);
            Intent intent=new Intent(NewSchedule.this,SyllabusActivity.class);
            startActivity(intent);
            NewSchedule.this.finish();
        }
    }
}
