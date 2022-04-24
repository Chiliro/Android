package com.example.booksys;

import org.json.JSONException;
import org.junit.Test;

import com.example.booksys.Utils.GsonUtils;
import com.example.booksys.pojo.ReaderType;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws JSONException {
        //        List<Book> bookList = new ArrayList<>();
//
//        for (int i = 0; i < 10; i++) {
//            Book book = new Book();
//            book.setBkId(i);
//            book.setBkName("xy" + i);
//            book.setBkPress("清华出版社" + i);
//            book.setBkAuthor("於阳明" + i);
//            book.setBkPrice(5 + i);
//            book.setBkNum(i);
//            book.setBkState((byte) 1);
//            bookList.add(book);
//        }
//
//        System.out.println(bookList);

        String state = "{\n" +
                "\t\"msg\": \"LOGIN_USER_SUCCESS\",\n" +
                "\t\"readerType\": {\n" +
                "\t\t\"rdType\": 1,\n" +
                "\t\t\"rdTypeName\": \"校长\",\n" +
                "\t\t\"canLendQty\": 30,\n" +
                "\t\t\"canLendDay\": 100\n" +
                "\t},\n" +
                "\t\"data\": {\n" +
                "\t\t\"identification\": \"1\",\n" +
                "\t\t\"password\": \"\",\n" +
                "\t\t\"root\": 0,\n" +
                "\t\t\"QQ\": null,\n" +
                "\t\t\"rdId\": 1\n" +
                "\t},\n" +
                "\t\"reader\": {\n" +
                "\t\t\"rdId\": 1,\n" +
                "\t\t\"rdType\": 1,\n" +
                "\t\t\"rdName\": \"张三\",\n" +
                "\t\t\"rdDept\": \"长江大学党委组织部\",\n" +
                "\t\t\"rdBorrowQty\": 1,\n" +
                "\t\t\"rdState\": 0\n" +
                "\t},\n" +
                "\t\"status\": 1\n" +
                "}";
//        Map<String, Object> map = GsonUtils.strToJavaBeanMaps(state);
//        System.out.println(map.get("msg"));
//
//        System.out.println(map.get("status"));
//
//
//        System.out.println(map.get("readerType"));
//        ReaderType readerType = GsonUtils.objectToJavaBean(Objects.requireNonNull(map.get("readerType")), ReaderType.class);
//        System.out.println(readerType);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        System.out.println(df.format(new Date()));// new Date()为获取当前系统时间

//        if("".equals("")){
//            System.out.println("1".equals(null));
//
//        }


    }
}