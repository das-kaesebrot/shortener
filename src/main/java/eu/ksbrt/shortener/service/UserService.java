package eu.ksbrt.shortener.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import eu.ksbrt.shortener.dao.UserDao;
import eu.ksbrt.shortener.model.User;

@Service
public class UserService {

    private final UserDao userDao;

    @Autowired
    public UserService(@Qualifier("fakeDao") UserDao userDao) {
        this.userDao = userDao;
    }
    
    public int addUser(User user) {
        return userDao.addUser(user);
    }

}
