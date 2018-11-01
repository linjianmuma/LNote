package title;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.hqyxjy.ldf.supercalendar.R;

public class titleBra2 extends RelativeLayout {

    public titleBra2(Context context, @Nullable AttributeSet attrs){
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.task2_title, this);
    }
}
