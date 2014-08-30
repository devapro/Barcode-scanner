package arseniy.bardcode.core;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import arseniy.bardcode.AppConst;

/**
 * Created by arseniy on 27/08/14.
 */
public class ScanCodeModel {

    public String[] codes;
    public String[] names;
    private Activity activ;

    public ScanCodeModel(Activity activ){
        this.activ = activ;
        this.updateModel();
    }

    public void updateModel(){
        DbHelper dbHelper = new DbHelper(activ);
        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // делаем запрос всех данных из таблицы scanCode, получаем Cursor
        Cursor c = db.query(AppConst.TableScanCodeName, null, null, null, null, null, null);
        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        codes = new String[c.getCount()];
        names = new String[c.getCount()];
        if (c.moveToFirst()) {
            // определяем номера столбцов по имени в выборке
            int idColIndex = c.getColumnIndex("id");
            int nameColIndex = c.getColumnIndex("name");
            int codelColIndex = c.getColumnIndex("cod");
            int i = 0;
            do {
                // получаем значения по номерам столбцов и пишем все в лог
                Log.d("Db", " code = " + c.getString(codelColIndex)
                );
                codes[i] = c.getString(codelColIndex);
                names[i] = c.getString(nameColIndex);
                i++;
                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (c.moveToNext());
        } else
            Log.d("Db", "0 rows");
        c.close();
        dbHelper.close();
    }

    public boolean isRecord(){
        return codes.length >0;
    }

    public static boolean deleteItemByCode(String cod, Activity activ){
        DbHelper dbHelper = new DbHelper(activ);
        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int delCount = db.delete(AppConst.TableScanCodeName, "cod = " + cod, null);
        dbHelper.close();
        return delCount > 0 ? true : false;
    }

    public static boolean addCode(String cod, String format, Activity activ){
        DbHelper dbHelper = new DbHelper(activ);
        // создаем объект для данных
        ContentValues cv = new ContentValues();
        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //заносим результата сканирования в бд
        // подготовим данные для вставки в виде пар: наименование столбца - значение
        cv.put("cod", cod);
        cv.put("format", format);
        cv.put("name", "");
        db.insert(AppConst.TableScanCodeName, null, cv);
        dbHelper.close();
        return true;
    }
}
