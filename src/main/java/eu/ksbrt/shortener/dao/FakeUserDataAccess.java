package eu.ksbrt.shortener.dao;

import eu.ksbrt.shortener.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

@Repository("fakeDao")
public class FakeUserDataAccess implements UserDao {

    private static List<User> DB = new ArrayList<>();
    
    @Override
    public int insertUser(UUID id, User user) {
        DB.add(new User(id, user.getUsername()));
        return 1;
    }
}
