package sdatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    public static final String S_BOOK = "create table s("
            + "sid integer PRIMARY KEY autoincrement,"
            +"stitle text,"
            +"scontent text,"
            +"stimes text,"
            +"stype text)";
    public MyDatabaseHelper(@Nullable Context context) {
        super(context,"sdata" , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(S_BOOK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
