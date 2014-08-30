package arseniy.bardcode.core;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import arseniy.bardcode.AppConst;

/**
 * Created by arseniy on 19/08/14.
 */
public class DbHelper extends SQLiteOpenHelper {
    public DbHelper(Context context) {
        super(context, AppConst.DbName, null, AppConst.DbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("Db", "--- onCreate database ---");
        // создаем таблицу с полями
        db.execSQL("create table scanCode ("
                + "id integer primary key autoincrement,"
                + "cod text,"
                + "format text,"
                + "name text" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
