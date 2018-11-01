package com.example.skypan.lnote;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import model.Data;

public class TitleBar extends LinearLayout {

    EditText ed_title;
    EditText ed_content;
    Data data;
    int ids;
    public TitleBar(final Context context, AttributeSet attrs) {
        super(context, attrs);

        //引入布局
        LayoutInflater.from(context).inflate(R.layout.title, this);
   /*  ImageButton back = (ImageButton) findViewById(R.id.back);
       TextView title = (TextView) findViewById(R.id.title);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity) getContext()).finish();
            }
        });*/
    }
}
