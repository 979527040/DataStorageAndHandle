package sqlite_db.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by 97952 on 2017/11/14.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {
    public static final String CREATE_BOOK="create table Book ("
            +"id integer primary key autoincrement, "
            +"author text, "
            +"price double, "
            +"pages integer, "
            +"name text)";
    public static final String CREATE_CATEGORY="create table CateGory ("
            +"id integer primary key autoincrement, "
            +"cateName text, "
            +"catePrice text)";

    private Context mContext;

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_BOOK);
            db.execSQL(CREATE_CATEGORY);
        Log.e("DB","Create succeeded");
    }
    //TODO 如果我们需要新增加一张表，一种方法是卸载应用程序重新执行建表操作，这种方法太麻烦，可以直接在更新方法中操作
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("drop table if exists Book");
            db.execSQL("drop table if exists CateGory");
            onCreate(db);
    }
}
