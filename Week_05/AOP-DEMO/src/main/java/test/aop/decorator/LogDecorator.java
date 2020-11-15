package test.aop.decorator;

public class LogDecorator implements UserService {

    private UserService delegate;

    public LogDecorator(UserService delegate) {
        this.delegate = delegate;
    }

    public String query(String id) {
        System.out.println("Log-query user, id is " + id);
        return delegate.query(id);
    }
}
