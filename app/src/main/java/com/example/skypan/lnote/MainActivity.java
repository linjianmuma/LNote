package com.example.skypan.lnote;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.github.clans.fab.FloatingActionButton;
import com.hqyxjy.ldf.supercalendar.SyllabusActivity;

import java.util.ArrayList;
import database.MyAdapter;
import database.MyDatabase;
import model.Data;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    FloatingActionButton imageButton;
    FloatingActionButton calendar;
    LayoutInflater layoutInflater;
    ArrayList<Data> arrayList;
    MyDatabase myDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView)findViewById(R.id.lv);
        imageButton = (FloatingActionButton) findViewById(R.id.fab_add);
        calendar = (FloatingActionButton)findViewById(R.id.fab_calendar);
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SyllabusActivity.class);
                startActivity(intent);
            }
        });
        layoutInflater = getLayoutInflater();
        myDatabase = new MyDatabase(this);
        arrayList = myDatabase.getarray();
        MyAdapter adapter = new MyAdapter(layoutInflater,arrayList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {   //点击一下跳转到编辑页面（编辑页面与新建页面共用一个布局）
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),New_note.class);
                intent.putExtra("ids",arrayList.get(position).getIds());
                startActivity(intent);
                MainActivity.this.finish();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {   //长按删除
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(MainActivity.this) //弹出一个对话框
                        //.setTitle("确定要删除此便签？")
                        .setMessage("确定要删除此便签？")
                        .setNegativeButton("取消",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("确定",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                myDatabase.toDelete(arrayList.get(position).getIds());
                                arrayList.remove(position);
                                MyAdapter myAdapter = new MyAdapter(layoutInflater,arrayList);
                                listView.setAdapter(myAdapter);
                            }
                        })
                        .create()
                        .show();
                return true;
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {   //点击悬浮按钮时，跳转到新建页面
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),New_note.class);
                startActivity(intent);
                MainActivity.this.finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_lo,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_newnote:
                Intent intent = new Intent(getApplicationContext(),New_note.class);
                startActivity(intent);
                MainActivity.this.finish();
                break;
            case R.id.menu_exit:
                MainActivity.this.finish();
                break;
            default:
                break;
        }
        return  true;
    }
}


