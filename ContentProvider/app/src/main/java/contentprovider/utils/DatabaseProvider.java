package contentprovider.utils;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import sqlite_db.utils.MyDatabaseHelper;

//todo 自定义ContentProvider，向其他程序提供数据，利用之前在MainActivity中创建的BookStore.db数据库文件，让其他程序也可以访问此数据库
//content://com.example.app.provider/table1 ->标准的uri链接
//content://com.example.app.provider/table1/1  ->表示期望访问的是table1这张表中id为1的数据
public class DatabaseProvider extends ContentProvider {
    public static final int BOOK_DIR = 0;
    public static final int BOOK_ITEM = 1;
    public static final int CATEGORY_DIR = 2;
    public static final int CATEGORY_ITEM = 3;
    //是在AndroidMainfest.xml中定义的
    public static final String AUTHORITY = "com.databasetext.provider";
    private static UriMatcher uriMatcher;
    private MyDatabaseHelper dbHelper;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "Book", BOOK_DIR);
        uriMatcher.addURI(AUTHORITY, "Book/#", BOOK_ITEM);
        uriMatcher.addURI(AUTHORITY, "Category", CATEGORY_DIR);
        uriMatcher.addURI(AUTHORITY, "Category/#", CATEGORY_ITEM);
    }

    /**
     * @param uri           使用uri来确定删除哪一张表中的数据，
     * @param selection     用于约束删除哪些行
     * @param selectionArgs 用于约束删除哪些行
     * @return 返回被删除的行数
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        //删除
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int deleteRows = 0;
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
                deleteRows = db.delete("Book", selection, selectionArgs);
                break;
            case BOOK_ITEM:
                String bookId = uri.getPathSegments().get(1);
                deleteRows = db.delete("Book", "id=?", new String[]{bookId});
                break;
            case CATEGORY_DIR:
                deleteRows = db.delete("Category", selection, selectionArgs);
                break;
            case CATEGORY_ITEM:
                String categoryId = uri.getPathSegments().get(1);
                deleteRows = db.delete("Category", "id=?", new String[]{categoryId});
                break;
        }
        return deleteRows;
    }

    /**
     * @param uri 根据传入的内容uri来返回相应的MIME类型
     * @return
     */
    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
                return "vnd.android.cursor.dir/vnd.com.databasetext.provider.Book";
            case BOOK_ITEM:
                return "vnd.android.cursor.item/vnd.com.databasetext.provider.Book";
            case CATEGORY_DIR:
                return "vnd.android.cursor.dir/vnd.com.databasetext.provider.Category";
            case CATEGORY_ITEM:
                return "vnd.android.cursor.item/vnd.com.databasetext.provider.Category";
        }
        return null;
    }

    /**
     * @param uri    确定要添加到的表
     * @param values 待添加的数据保存在values中
     * @return
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        //添加数据
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Uri uriReturn = null;
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
                break;
            case BOOK_ITEM:
                long newBookId = db.insert("Book", null, values);
                uriReturn = Uri.parse("content://" + AUTHORITY + "/Book/" + newBookId);
                break;
            case CATEGORY_DIR:
                break;
            case CATEGORY_ITEM:
                long newCategoryId = db.insert("Category", null, values);
                uriReturn = Uri.parse("content://" + AUTHORITY + "/Category/" + newCategoryId);
                break;
        }
        return uriReturn;
    }

    // TODO: 初始化内容提供器的时候调用，通常用来完成对数据库的创建和升级，返回true表示初始化成功，false表示失败
    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        dbHelper = new MyDatabaseHelper(getContext(), "BookStore.db", null, 2);
        return true;
    }

    /**
     * @param uri           用于确定查询那张表
     * @param projection,   用于确定查询哪些列
     * @param selection     用于与约束查询那些行
     * @param selectionArgs 用于与约束查询那些行
     * @param sortOrder，    对结果进行排序
     * @return
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        //查询数据
        //TODO 通过getPathSegments方法他会将内容uri权限之后的部分以"/"进行分割，并把分割后的结果放入到字符串列表中，这个列表中的第0个位置存放的就是路径，第一个位置就是id
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
                cursor = db.query("Book", projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case BOOK_ITEM:
                String bookId = uri.getPathSegments().get(1);
                cursor = db.query("Book", projection, "id=?", new String[]{bookId}, null, null, sortOrder);
                break;
            case CATEGORY_DIR:
                cursor = db.query("Category", projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case CATEGORY_ITEM:
                String categoryId = uri.getPathSegments().get(1);
                cursor = db.query("Category", projection, "id=?", new String[]{categoryId}, null, null, sortOrder);
                break;
        }
        return cursor;
    }

    /**
     * @param uri           用来确定更新那张表
     * @param values        新数据保存在values中
     * @param selection     用于约束更新哪些行
     * @param selectionArgs 用于约束更新哪些行
     * @return 受影响的行数将作为返回值
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        //更新数据
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int updateRows = 0;
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
                updateRows = db.update("Book", values, selection, selectionArgs);
                break;
            case BOOK_ITEM:
                String bookId = uri.getPathSegments().get(1);
                updateRows = db.update("Book", values, "id=?", new String[]{bookId});
                break;
            case CATEGORY_DIR:
                updateRows = db.update("Category", values, selection, selectionArgs);
                break;
            case CATEGORY_ITEM:
                String categoryId = uri.getPathSegments().get(1);
                updateRows = db.update("Category", values, "id=?", new String[]{categoryId});
                break;
        }
        return updateRows;
    }
}
