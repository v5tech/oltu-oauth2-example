package cn.zetark.oauth2.dao;

import cn.zetark.oauth2.entity.User;

import java.util.List;

public interface UserDao {

    public User createUser(User user);

    public User updateUser(User user);

    public void deleteUser(Long userId);

    User findOne(Long userId);

    List<User> findAll();

    User findByUsername(String username);

}
