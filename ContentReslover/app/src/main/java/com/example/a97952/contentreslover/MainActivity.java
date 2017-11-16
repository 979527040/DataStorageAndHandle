package com.example.a97952.contentreslover;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private String newId;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verifyStoragePermissions(this);
        //增加
        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
            }
        });
        //查询
        findViewById(R.id.query).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });
        //更新
        findViewById(R.id.update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });
        //删除
        findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });
    }

    private void add() {
        Uri uri = Uri.parse("content://com.databasetext.provider/Book");
        ContentValues values = new ContentValues();
        values.put("name", "Kings");
        values.put("author", "GeorgeMartin");
        values.put("pages", 103);
        values.put("price", 22.3);
        Uri newUri = getContentResolver().insert(uri, values);
        newId = newUri.getPathSegments().get(1);
    }

    private void search() {
        Uri uri = Uri.parse("content://com.databasetext.provider/Book/1");
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String author = cursor.getString(cursor.getColumnIndex("author"));
                int pages = cursor.getInt(cursor.getColumnIndex("pages"));
                double price = cursor.getDouble(cursor.getColumnIndex("price"));
                Log.e("MainActivity", "book name is:" + name);
                Log.e("MainActivity", "book author is:" + author);
                Log.e("MainActivity", "book pages is:" + pages);
                Log.e("MainActivity", "book price is:" + price);
            }
        }
        cursor.close();
    }

    private void update() {
        Uri uri = Uri.parse("content://com.databasetext.provider/Book/" + newId);
        ContentValues values = new ContentValues();
        values.put("name", "A Storm of Swords");
        values.put("pages", 1234);
        values.put("price", 23.3);
        getContentResolver().update(uri, values, null, null);
    }

    private void delete() {
        Uri uri = Uri.parse("content://com.databasetext.provider/Book/" + newId);
        getContentResolver().delete(uri, null, null);
    }

    public static void verifyStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED&&grantResults[1]==PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "权限申请成功", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
