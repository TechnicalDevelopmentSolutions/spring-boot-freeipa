package com.techdevsolutions.service.user;

import com.techdevsolutions.beans.Filter;
import com.techdevsolutions.beans.Search;
import com.techdevsolutions.beans.auditable.User;
import com.techdevsolutions.beans.ValidationResponse;
import com.techdevsolutions.dao.test.user.UserTestDao;
import com.techdevsolutions.service.CrudServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

@Service
public class UserService implements CrudServiceInterface<User> {
    private Logger logger = Logger.getLogger(UserService.class.getName());
    UserTestDao userDao;
//    UserMySqlDao userDao;

    @Autowired
    public UserService(UserTestDao userDao) {
        this.userDao = userDao;
    }

//    @Autowired
//    public UserService(UserMySqlDao userDao) {
//        this.userDao = userDao;
//    }

    public List<User> search(Search search) throws Exception {
        logger.info("UserService - search: " + search.toString());
        List<User> users = this.userDao.search(search);

        for (User user : users) {
            ValidationResponse vr = User.Validate(user);
            if (!vr.getValid()) { throw new Exception("Invalid item: " + vr.getMessage()); }
        }

        return users;
    }

    public List<User> get(Filter filter) throws Exception {
        logger.info("UserService - get: " + filter.toString());
        List<User> items = this.userDao.get(filter);

        for (User item : items) {
            ValidationResponse vr = User.Validate(item);
            if (!vr.getValid()) { throw new Exception("Invalid item: " + vr.getMessage()); }
        }

        return items;
    }

    public User get(Integer id) throws Exception {
        logger.info("UserService - get - ID: " + id);

        return this.userDao.get(id);
    }

    public User create(User item) throws Exception {
        logger.info("UserService - create - name: " + item.getName());

        ValidationResponse vr = User.Validate(item, true);
        if (!vr.getValid()) { throw new Exception("Invalid item: " + vr.getMessage()); }

        return this.userDao.create(item);
    }

    public void delete(Integer id) throws Exception {
        logger.info("UserService - delete - ID: " + id);
        User i = this.get(id);
        if (i == null) { throw new NoSuchElementException("UserService - delete - Item was not found using ID: "+ id); }
        this.userDao.delete(id);
    }

    public void remove(Integer id) throws Exception {
        logger.info("UserService - remove - ID: " + id);
        User i = this.get(id);
        if (i == null) { throw new NoSuchElementException("UserService - remove - Item was not found using ID: "+ id); }
        this.userDao.remove(id);
    }

    public User update(User item) throws Exception {
        logger.info("UserService - update - ID: " + item.getId());

        ValidationResponse vr = User.Validate(item);
        if (!vr.getValid()) { throw new Exception("Invalid item: " + vr.getMessage()); }

        User i = this.get(item.getId());
        if (i == null) { throw new NoSuchElementException("UserService - update - Item was not found using ID: " + item.getId()); }

        i.setName(item.getName());
        i.setUpdatedBy(item.getUpdatedBy());
        i.setUpdatedDate(item.getUpdatedDate());
        return this.userDao.update(i);
    }
}
