package title;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hqyxjy.ldf.supercalendar.R;


public class titleBra extends RelativeLayout {

    private ImageButton t_add;
    public titleBra(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.task_title, this);
        t_add = (ImageButton)findViewById(R.id.t_add);
    }
}
