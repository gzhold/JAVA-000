package test.aop.bytebuddy.log;


public class MyService {


    @MyLog(value = "ERROR")
    public void queryData(int id) {
        System.out.println("Query data, id is " + id);
    }

    @MyLog
    public void getFromHttpHeader(String param) {
        System.out.println("Get from http header: " + param);
    }

    public void noLog() {
        System.out.println("no have log!");
    }

}
