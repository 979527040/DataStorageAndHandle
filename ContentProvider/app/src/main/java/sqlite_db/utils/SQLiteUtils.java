package sqlite_db.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by 97952 on 2017/11/15.
 */

public class SQLiteUtils {
    public MyDatabaseHelper getDbHelper() {
        return dbHelper;
    }

    private MyDatabaseHelper dbHelper;

    public SQLiteUtils(Context context) {
        //todo 对数据库进行升级，将版本号升级为2
        dbHelper = new MyDatabaseHelper(context, "BookStore.db", null, 2);
    }

    //TODO 向数据库中添加数据
    public  void add(){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        //开始组装第一条数据
        values.put("name","ZhuZunLong");
        values.put("author","Luxun");
        values.put("pages",12);
        values.put("price",23.2);
        db.insert("Book",null,values);
        values.clear();
        //开始组装第二条数据
        values.put("name","Lenovo");
        values.put("author","Bingxin");
        values.put("pages",122);
        values.put("price",22.2);
        db.insert("Book",null,values);
        //TODO 或者使用SQL方式来向数据库中添加数据
//        db.execSQL("insert into Book(name,author,pages,price) values(?,?,?,?)",new String[]{"The Da","Dan Brown","453","3.00"});
    }
    //TODO 更改
    public  void update(){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("price",10);
        db.update("Book",values,"name=?",new String[] {"ZhuZunLong"});
        //TODO 或者使用SQL方式来向数据库中更改数据
//        db.execSQL("update Book set price=? where name=?",new String[]{"1.33","The Da"});
    }
    //TODO 删除
    public  void delete(){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        //删除Book表中pages大于100的那条数据
        db.delete("Book","pages>?",new String[] {"100"});
        //TODO 或者使用SQL方式来向数据库删除数据
//        db.execSQL("delete from Book where pages>?",new String[]{"400"});
    }
    //todo 查询
    public  void search(){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        //查询Book表中的所有数据
        Cursor cursor=db.query("Book",null,null,null,null,null,null);
        if(cursor.moveToFirst()){//将指针移动到第一行
            do{
                //遍历Cursor对象，取出数据并打印
                String name=cursor.getString(cursor.getColumnIndex("name"));
                String author=cursor.getString(cursor.getColumnIndex("author"));
                int pages=cursor.getInt(cursor.getColumnIndex("pages"));
                double price=cursor.getDouble(cursor.getColumnIndex("price"));
                Log.e("Search","book-name-is:"+name);
                Log.e("Search","book-author-is:"+author);
                Log.e("Search","book-page-is:"+pages);
                Log.e("Search","book-price-is:"+price);
            }while(cursor.moveToNext());
        }
        cursor.close();
        //TODO 或者使用SQL方式来实现查询数据
//        db.rawQuery("select * from Book",null);
    }
}
