package cn.zetark.oauth2.service.impl;

import cn.zetark.oauth2.dao.UserDao;
import cn.zetark.oauth2.entity.User;
import cn.zetark.oauth2.service.PasswordHelper;
import cn.zetark.oauth2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private PasswordHelper passwordHelper;

    /**
     * 创建用户
     *
     * @param user
     */
    @Override
    public User createUser(User user) {
        //加密密码
        passwordHelper.encryptPassword(user);
        return userDao.createUser(user);
    }

    @Override
    public User updateUser(User user) {
        return userDao.updateUser(user);
    }

    @Override
    public void deleteUser(Long userId) {
        userDao.deleteUser(userId);
    }

    /**
     * 修改密码
     *
     * @param userId
     * @param newPassword
     */
    @Override
    public void changePassword(Long userId, String newPassword) {
        User user = userDao.findOne(userId);
        user.setPassword(newPassword);
        passwordHelper.encryptPassword(user);
        userDao.updateUser(user);
    }

    @Override
    public User findOne(Long userId) {
        return userDao.findOne(userId);
    }

    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }

    /**
     * 根据用户名查找用户
     *
     * @param username
     * @return
     */
    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    /**
     * 验证登录
     *
     * @param username   用户名
     * @param password   密码
     * @param salt       盐
     * @param encryptpwd 加密后的密码
     * @return
     */
    @Override
    public boolean checkUser(String username, String password, String salt, String encryptpwd) {
        String pwd = passwordHelper.encryptPassword(username, password, salt);
        return pwd.equals(encryptpwd);
    }


}
