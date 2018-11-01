package sdatabase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.hqyxjy.ldf.supercalendar.SyllabusActivity;
import com.ldf.calendar.model.CalendarDate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class SDatabase {
    Context context;
    SQLiteDatabase mydatabase;
    MyDatabaseHelper myOpenHelper;
    SyllabusActivity syllabusActivity;

    public SDatabase(Context context){
        this.context = context;
        myOpenHelper = new MyDatabaseHelper(context);

    }

    public ArrayList<Schedule> getarray(){
        ArrayList<Schedule> l1=new ArrayList<Schedule>();
        ArrayList<Schedule> l2=new ArrayList<Schedule>();
        mydatabase = myOpenHelper.getWritableDatabase();
        syllabusActivity = new SyllabusActivity();
        Log.i("lch2",syllabusActivity.getSday());

        Cursor cursor = mydatabase.rawQuery("select sid, stitle, stimes from s", null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            String t = cursor.getString(cursor.getColumnIndex("stimes"));
            if(t.equals(syllabusActivity.getSday())) {
                int id = cursor.getInt(cursor.getColumnIndex("sid"));
                String title = cursor.getString(cursor.getColumnIndex("stitle"));
                Schedule data = new Schedule(id, title);
                l1.add(data);
            }
            cursor.moveToNext();
        }
        mydatabase.close();
        for (int i = l1.size(); i >0; i--) {
            l2.add(l1.get(i-1));
        }
        return l2;
    }

 /*   //测试所用
    public ArrayList<Schedule> getabcarray(){
        ArrayList<Schedule> l1=new ArrayList<Schedule>();
        ArrayList<Schedule> l2=new ArrayList<Schedule>();
        mydatabase = myOpenHelper.getWritableDatabase();
        Cursor cursor = mydatabase.rawQuery("select sid, stitle from s", null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            String title1=cursor.getString(cursor.getColumnIndex("stitle"));
            Log.i("title1",title1);
            if(title1.equals("abc")) {
                int id = cursor.getInt(cursor.getColumnIndex("sid"));
                String title = cursor.getString(cursor.getColumnIndex("stitle"));
                Schedule data = new Schedule(id, title);
                l1.add(data);
            }
            cursor.moveToNext();
        }
        mydatabase.close();
        for (int i = l1.size(); i >0; i--) {
            l2.add(l1.get(i-1));
        }
        return l2;
    }*/
 //获取所选日期计划
 public ArrayList<Schedule> getSelecteDayArray(){
     ArrayList<Schedule> l1=new ArrayList<Schedule>();
     ArrayList<Schedule> l2=new ArrayList<Schedule>();
     mydatabase = myOpenHelper.getWritableDatabase();
     SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");//编辑便签的时间，格式化
     Date date = new Date(System.currentTimeMillis());
     String time = simpleDateFormat.format(date);
     Cursor cursor = mydatabase.rawQuery("select sid, stitle, stimes from s", null);
     cursor.moveToFirst();
     while(!cursor.isAfterLast()){
         String stime = cursor.getString(cursor.getColumnIndex("stimes"));
         Log.i("time",time);
         Log.i("stime",stime);
         if(stime.equals(time)) {
             int id = cursor.getInt(cursor.getColumnIndex("sid"));
             String title = cursor.getString(cursor.getColumnIndex("stitle"));
             Schedule data = new Schedule(id, title);
             l1.add(data);
         }
         cursor.moveToNext();
     }
     mydatabase.close();
     for (int i = l1.size(); i >0; i--) {
         l2.add(l1.get(i-1));
     }
     return l2;
 }
    //添加数据
    public void toInsert(Schedule schedule){
        mydatabase = myOpenHelper.getWritableDatabase();
        mydatabase.execSQL("insert into s(stitle,scontent,stimes)values('"
                + schedule.getTitle()+"','"
                +schedule.getContent()+"','"
                +schedule.getTimes() +"')");
        mydatabase.close();
    }
    //修改表中数据
    public void toUpdate(Schedule data){
        mydatabase = myOpenHelper.getWritableDatabase();
        mydatabase.execSQL(
                "update s set stitle='"+ data.getTitle()+
                        "',stimes='"+data.getTimes()+
                        "',scontent='"+data.getContent() +
                        "' where sid='"+ data.getIds()+"'");
        mydatabase.close();
    }
    //删除数据
    public void toDelete(int sid){
        mydatabase = myOpenHelper.getWritableDatabase();
        mydatabase.execSQL("delete from s where sid="+sid+"");
        mydatabase.close();
    }
    //获取要修改数据（就是当选择listview子项想要修改数据时，获取数据显示在新建页面）
    public Schedule getTiandCon(int id){
        mydatabase = myOpenHelper.getWritableDatabase();
        Cursor cursor=mydatabase.rawQuery("select stitle,scontent from s where sid='"+id+"'" , null);
        cursor.moveToFirst();
        String title=cursor.getString(cursor.getColumnIndex("stitle"));
        String content=cursor.getString(cursor.getColumnIndex("scontent"));

        Schedule data=new Schedule(title,content);
        mydatabase.close();
        return data;
    }


}
