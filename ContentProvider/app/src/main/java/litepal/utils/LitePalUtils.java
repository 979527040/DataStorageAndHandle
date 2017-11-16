package litepal.utils;

import android.database.Cursor;
import android.provider.ContactsContract;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.List;

/**
 * Created by 97952 on 2017/11/15.
 */

public class LitePalUtils {
    //TODO 创建数据库
    public static void create(){
        Connector.getDatabase();
    }
    //TODO 添加数据
    public static void add(){
        Book book=new Book();
        book.setName("The Da Vinci Code");
        book.setAuthor("ZhuZunLong");
        book.setPages(456);
        book.setPrice(13.4);
        book.setPress("Unknow");
        book.save();
    }
    //TODO 更新数据
    public static void update(){
        Book book=new Book();
        book.setPrice(12.2);
        book.setPress("Anchor");
        //指定将名字为The Da Vinci Code，作者为ZhuZunLong的书的价格修改为12.2，press修改为Achor,
        //如果不写条件语句，默认为全部修改
        book.updateAll("name=?and author=?","The Da Vinci Code","ZhuZunLong");
    }
    //TODO 更新数据,将所有书的页数都更新为0，当要把一个字段更新成默认值的时候不能通过set的方式，需要用setToDefault()方法
    public static void update2(){
        Book book=new Book();
        book.setToDefault("pages");
        book.updateAll();
    }
    //TODO 删除数据
    public static void delete(){
        //删除价格小于15的书
        DataSupport.deleteAll(Book.class,"price<?","15");
    }
    //TODO 查询数据
    public static void search(){
        //查询所有数据
        List<Book> books=DataSupport.findAll(Book.class);
        //查询第一条数据
        Book firstBook=DataSupport.findFirst(Book.class);
        //查询最后一条数据
        Book lastBook=DataSupport.findLast(Book.class);
        //查询哪几列数据,如下，查询名字和作者这两列数据
        List<Book> books_data=DataSupport.select("name","author").find(Book.class);
        //指定查询的约束条件,如下，只查询页数大于400的数据
        List<Book> books_where=DataSupport.where("pages>?","400").find(Book.class);
        //查询顺序,desc表示降序，asc表示升序
        List<Book> books_order= DataSupport.order("price desc").find(Book.class);
        //limit()方法用于指定查询结果的数量,如下，只查询前三条数据
        List<Book> books_limit=DataSupport.limit(3).find(Book.class);
        //offset()方法用于指定查询结果的偏移量，比如查询表中的第2条，第3条和第4条数据可以如下
        List<Book> books_offset=DataSupport.limit(3).offset(1).find(Book.class);
        //TODO litepal还支持原生的SQL查询语句，如果以上满足不了用户需求
        Cursor c=DataSupport.findBySQL("select*from Book where pages>?and price>?","400","20");
    }
}
