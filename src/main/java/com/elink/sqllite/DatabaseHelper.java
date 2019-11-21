package com.elink.sqllite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @Description：
 * @Author： Evloution_
 * @Date： 2019/9/26
 * @Email： 15227318030@163.com
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    //带全部参数的构造函数，此构造函数必不可少
    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE conference"
                + "(_id INTEGER PRIMARY KEY,"
                + " BookName VARCHAR(30)  NOT NULL,"
                + " Author VARCHAR(20))";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
