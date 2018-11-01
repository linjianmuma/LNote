package com.hqyxjy.ldf.supercalendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ldf.calendar.Utils;
import com.ldf.calendar.component.CalendarAttr;
import com.ldf.calendar.component.CalendarRenderer;
import com.ldf.calendar.view.MonthPager;
import com.ldf.calendar.interf.OnSelectDateListener;
import com.ldf.calendar.component.CalendarViewAdapter;
import com.ldf.calendar.model.CalendarDate;
import com.ldf.calendar.view.Calendar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import sdatabase.MyDatabaseHelper;
import sdatabase.SDatabase;
import sdatabase.Schedule;

/**
 * Created by ldf on 16/11/4.
 */

@SuppressLint("SetTextI18n")
public class SyllabusActivity extends AppCompatActivity {
    TextView tvYear;
    TextView tvMonth;
    TextView backToday;
    ImageButton today;
    CoordinatorLayout content;
    MonthPager monthPager;
    RecyclerView rvToDoList;
    TextView scrollSwitch;
    TextView themeSwitch;
    TextView nextMonthBtn;
    TextView lastMonthBtn;
    ImageButton add;

    static String sday; //获取所点击的日期
    ExampleAdapter exampleAdapter;
    ArrayList<Schedule> schedules;

    private ArrayList<Calendar> currentCalendars = new ArrayList<>();
    private SDatabase sDatabase;
    private MyDatabaseHelper myDatabaseHelper;
    private SQLiteDatabase mydatabase;
    private CalendarViewAdapter calendarAdapter;
    private OnSelectDateListener onSelectDateListener;
    private int mCurrentPage = MonthPager.CURRENT_DAY_INDEX;
    private Context context;
    private CalendarDate currentDate;
    private boolean initiated = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syllabus);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        context = this;
        content = (CoordinatorLayout) findViewById(R.id.content);
        monthPager = (MonthPager) findViewById(R.id.calendar_view);
        //此处强行setViewHeight，毕竟你知道你的日历牌的高度
       // monthPager.setViewHeight(Utils.dpi2px(context, 270));
        monthPager.setViewHeight(Utils.dpi2px(context, 262));
        tvYear = (TextView) findViewById(R.id.show_year_view);
        tvMonth = (TextView) findViewById(R.id.show_month_view);
        backToday = (TextView) findViewById(R.id.back_today_button);
        today = (ImageButton)findViewById(R.id.ib_today);
        scrollSwitch = (TextView) findViewById(R.id.scroll_switch);
        themeSwitch = (TextView) findViewById(R.id.theme_switch);
        nextMonthBtn = (TextView) findViewById(R.id.next_month);
        lastMonthBtn = (TextView) findViewById(R.id.last_month);
        //重启这个活动后重新刷新sday为今天
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");//编辑便签的时间，格式化
        Date date = new Date(System.currentTimeMillis());
        String time = simpleDateFormat.format(date);
        sday = time;
        //跳转至任务详细界面
        add = (ImageButton)findViewById(R.id.t_add);
        add.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SyllabusActivity.this, NewSchedule.class);
                intent.putExtra("sday", sday);
                startActivity(intent);
               SyllabusActivity.this.finish();
            }
        });

        initReceyleview();

        initCurrentDate();
        initCalendarView();
        initToolbarClickListener();

    }

    /**
     * onWindowFocusChanged回调时，将当前月的种子日期修改为今天
     *
     * @return void
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && !initiated) {
            refreshMonthPager();
            initiated = true;
        }
    }

    /*
    * 如果你想以周模式启动你的日历，请在onResume是调用
    * Utils.scrollTo(content, rvToDoList, monthPager.getCellHeight(), 200);
    * calendarAdapter.switchToWeek(monthPager.getRowIndex());
    * */
    @Override
    protected void onResume() {
        super.onResume();
        Utils.scrollTo(content, rvToDoList, monthPager.getCellHeight(), 200);
        calendarAdapter.switchToWeek(monthPager.getRowIndex());
    }

    /**
     * 初始化对应功能的listener
     *
     * @return void
     */
    private void initToolbarClickListener() {
        backToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickBackToDayBtn();
            }
        });
        today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickBackToDayBtn();
            }
        });
        scrollSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (calendarAdapter.getCalendarType() == CalendarAttr.CalendarType.WEEK) {
                    Utils.scrollTo(content, rvToDoList, monthPager.getViewHeight(), 200);
                    calendarAdapter.switchToMonth();
                } else {
                    Utils.scrollTo(content, rvToDoList, monthPager.getCellHeight(), 200);
                    calendarAdapter.switchToWeek(monthPager.getRowIndex());
                }
            }
        });
        themeSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshSelectBackground();
            }
        });
        nextMonthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                monthPager.setCurrentItem(monthPager.getCurrentPosition() + 1);
            }
        });
        lastMonthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                monthPager.setCurrentItem(monthPager.getCurrentPosition() - 1);
            }
        });
    }

    /**
     * 初始化currentDate
     *
     * @return void
     */
    private void initCurrentDate() {
        currentDate = new CalendarDate();
        tvYear.setText(currentDate.getYear() + "年");
        tvMonth.setText(currentDate.getMonth() + "");
    }

    /**
     * 初始化CustomDayView，并作为CalendarViewAdapter的参数传入
     */
    private void initCalendarView() {
        initListener();
        CustomDayView customDayView = new CustomDayView(context, R.layout.custom_day);
        calendarAdapter = new CalendarViewAdapter(
                context,
                onSelectDateListener,
                CalendarAttr.WeekArrayType.Monday,
                customDayView);
        calendarAdapter.setOnCalendarTypeChangedListener(new CalendarViewAdapter.OnCalendarTypeChanged() {
            @Override
            public void onCalendarTypeChanged(CalendarAttr.CalendarType type) {
                rvToDoList.scrollToPosition(0);
            }
        });
        initMarkData();
        initMonthPager();
    }

    /**
     * 初始化标记数据，HashMap的形式，可自定义
     * 如果存在异步的话，在使用setMarkData之后调用 calendarAdapter.notifyDataChanged();
     */
    private void initMarkData() {
        HashMap<String, String> markData = new HashMap<>();
        markData.put("2017-8-9", "1");
        markData.put("2017-7-9", "0");
        markData.put("2017-6-9", "1");
        markData.put("2017-6-10", "0");
        calendarAdapter.setMarkData(markData);
    }

    private void initListener() {
        onSelectDateListener = new OnSelectDateListener() {
            @Override
            public void onSelectDate(CalendarDate date) {
                refreshClickDate(date);
            }

            @Override
            public void onSelectOtherMonth(int offset) {
                //偏移量 -1表示刷新成上一个月数据 ， 1表示刷新成下一个月数据
                monthPager.selectOtherMonth(offset);
            }
        };
    }


    private void refreshClickDate(CalendarDate date) {
        currentDate = date;
        tvYear.setText(date.getYear() + "年");
        tvMonth.setText(date.getMonth() + "");
        int i1=date.getYear();
        int i2=date.getMonth();
        int i3=date.getDay();
        sday=i1+"."+i2+"."+i3;
        initReceyleview();
    }

    /**
     * 初始化monthPager，MonthPager继承自ViewPager
     *
     * @return void
     */
    private void initMonthPager() {
        monthPager.setAdapter(calendarAdapter);
        monthPager.setCurrentItem(MonthPager.CURRENT_DAY_INDEX);
        monthPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                position = (float) Math.sqrt(1 - Math.abs(position));
                page.setAlpha(position);
            }
        });
        monthPager.addOnPageChangeListener(new MonthPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mCurrentPage = position;
                currentCalendars = calendarAdapter.getPagers();
                if (currentCalendars.get(position % currentCalendars.size()) != null) {
                    CalendarDate date = currentCalendars.get(position % currentCalendars.size()).getSeedDate();
                    currentDate = date;
                    tvYear.setText(date.getYear() + "年");
                    tvMonth.setText(date.getMonth() + "");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    public void onClickBackToDayBtn() {
        refreshMonthPager();
    }

    private void refreshMonthPager() {
        CalendarDate today = new CalendarDate();
        calendarAdapter.notifyDataChanged(today);
        tvYear.setText(today.getYear() + "年");
        tvMonth.setText(today.getMonth() + "");
    }

    private void refreshSelectBackground() {
        ThemeDayView themeDayView = new ThemeDayView(context, R.layout.custom_day_focus);
        calendarAdapter.setCustomDayRenderer(themeDayView);
        calendarAdapter.notifyDataSetChanged();
        calendarAdapter.notifyDataChanged(new CalendarDate());
    }
    //lch获取所选日期
    public String getSday()
    {
        return sday;
    }
    public void initReceyleview(){
        schedules = new ArrayList<>();
        sDatabase = new SDatabase(this);
        schedules = sDatabase.getarray();
        exampleAdapter = new ExampleAdapter(this, schedules);
        rvToDoList = (RecyclerView) findViewById(R.id.list);
        rvToDoList.setHasFixedSize(true);
        rvToDoList.setLayoutManager(new LinearLayoutManager(this));
        rvToDoList.setAdapter(exampleAdapter);

        //item点击事件
        ArrayList<Schedule> finalSchedules = schedules;
        ArrayList<Schedule> finalSchedules1 = schedules;
        exampleAdapter.setOnItemClickListener(new ExampleAdapter.OnItemClickListener() {

            @Override
            //单击跳转编辑页面
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(SyllabusActivity.this, NewSchedule.class);
                intent.putExtra("sid", finalSchedules1.get(position).getIds());
                intent.putExtra("sday", sday);
                Log.i("lchsy","1");
                // intent.putExtra("time",finalSchedules1.get(position).getTimes());  //忘记这行是干嘛得了
                startActivity(intent);
                SyllabusActivity.this.finish();
            }
            @Override
            //长按删除该item
            public void onItemLongClick(View view, int position) {
                sDatabase.toDelete(finalSchedules.get(position).getIds());
                finalSchedules.remove(position);
                rvToDoList.setAdapter(exampleAdapter);
                Toast.makeText(SyllabusActivity.this,"删除成功", Toast.LENGTH_SHORT).show();
            }


        });
    }
}

