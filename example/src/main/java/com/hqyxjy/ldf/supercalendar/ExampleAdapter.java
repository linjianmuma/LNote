package com.hqyxjy.ldf.supercalendar;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import sdatabase.MyDatabaseHelper;
import sdatabase.SDatabase;
import sdatabase.Schedule;

/**
 * Created by ldf on 17/6/14.
 */

public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.VH> {

    private List<Schedule> schedules;
    private Context mContext;
    private ExampleAdapter.OnItemClickListener onItemClickListener;

    public ExampleAdapter(Context context, List<Schedule> schedules) {
        mContext = context;
        this.schedules = schedules;
    }
    // ① 定义点击回调接口
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    // ② 定义一个设置点击监听器的方法
    public  void setOnItemClickListener(ExampleAdapter.OnItemClickListener listener) {
       this.onItemClickListener = listener;
    }


    public static class VH extends RecyclerView.ViewHolder {
        TextView stitle;
        CheckBox delete;
        public VH(View v) {
            super(v);
            stitle = (TextView)v.findViewById(R.id.text_view);
        }


    }
    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
       View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
       return new VH(v);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
       holder.stitle.setText(schedules.get(position).getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(onItemClickListener != null) {
                   int pos = holder.getLayoutPosition();
                   onItemClickListener.onItemClick(holder.itemView, pos);
               }
           }
       });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(onItemClickListener != null) {
                    int pos = holder.getLayoutPosition();
                    onItemClickListener.onItemLongClick(holder.itemView, pos);
                }
                //表示此事件已经消费，不会触发单击事件
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return schedules.size();
    }


}
