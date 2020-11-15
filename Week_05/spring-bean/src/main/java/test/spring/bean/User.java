package test.spring.bean;

public class User {

    private String id;

    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 静态方法获取
     * @return
     */
    public static User createUser() {
        User user = new User();
        user.setName("test01");
        user.setId("123");
        return user;
    }
}
