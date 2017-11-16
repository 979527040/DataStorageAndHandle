package file_save.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by 97952 on 2017/11/14.
 * 文件存储
 */
// Activity还提供了getCacheDir()和getFilesDir()方法：
// getCacheDir()方法用于获取/data/data/<package name>/cache目录
// getFilesDir()方法用于获取/data/data/<package name>/files目录

// 在SDCard中创建与删除文件权限
//<uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS"/>
//往SDCard写入数据权限
//<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
//TODO 从保存到文件中
public class FileSave {
    //TODO MODE_PRIVATE 每次打开文件都会覆盖原来的内容,是默认模式
    //TODO MODE_APPEND表示如果该文件已存在，就往文件里追加内容，不存在就创建新文件
    //TODO MODE_WORLD_READABLE和MODE_WORLD_WRITEABLE,这两种模式表示允许其他应用对我们的程序中的文件进行读写，不过这在4.2版本中被废弃了
    //TODO sd卡路径 2.2前 /sdcard 2.2后 /mnt/sdcard 最好获得路径方法：Environment.getExternalStorageDirectory()
    public void save(Context context) {
        String data = "Data to Save";
        FileOutputStream out = null;
        BufferedWriter writer = null;
        try {
            //文件名为data
            out = context.openFileOutput("data", Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //TODO 从文件中读取
    public String load(Context context) {
        FileInputStream in = null;
        BufferedReader reader = null;
        StringBuilder content = new StringBuilder();
        try {
            in = context.openFileInput("data");
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return content.toString();
    }
}
