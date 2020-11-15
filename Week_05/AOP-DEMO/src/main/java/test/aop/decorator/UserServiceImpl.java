package test.aop.decorator;

public class UserServiceImpl implements UserService {

    public String query(String id) {
        return "test" + id;
    }
}
