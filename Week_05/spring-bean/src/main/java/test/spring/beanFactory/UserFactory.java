package test.spring.beanFactory;

import test.spring.bean.User;

public interface UserFactory {

    default User createUser() {
        return User.createUser();
    }

    void doDestroy();
}
