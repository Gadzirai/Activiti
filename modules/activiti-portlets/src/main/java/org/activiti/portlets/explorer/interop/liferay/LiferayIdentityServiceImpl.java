package org.activiti.portlets.explorer.interop.liferay;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.*;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author tomek@lipski.net.pl
 *         Date: 3/10/13 4:44 PM
 */
public class LiferayIdentityServiceImpl implements IdentityService {

    @Override
    public UserQuery createUserQuery() {
        return new LiferayUserQueryImpl();
    }

    @Override
    public GroupQuery createGroupQuery() {
        return new LiferayGroupQueryImpl();
    }

    @Override
    public Picture getUserPicture(String userId) {//TODO
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getUserInfo(String userId, String key) { //TODO
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<String> getUserInfoKeys(String userId) { //TODO
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

//    @Override
//    public List<String> getUserAccountNames(String userId) { //TODO
//        return null;  //To change body of implemented methods use File | Settings | File Templates.
//    }

//    @Override
//    public Account getUserAccount(String userId, String userPassword, String accountName) { //TODO
//        return null;  //To change body of implemented methods use File | Settings | File Templates.
//    }


    @Override
    public User newUser(String userId) {
        throw new IllegalArgumentException("Please use Liferay's user management API to do that.");
    }

    @Override
    public void saveUser(User user) {
        throw new IllegalArgumentException("Please use Liferay's user management API to do that.");
    }


    @Override
    public void deleteUser(String userId) {
        throw new IllegalArgumentException("Please use Liferay's user management API to do that.");
    }

    @Override
    public Group newGroup(String groupId) {
        throw new IllegalArgumentException("Please use Liferay's user management API to do that.");
    }

    @Override
    public void saveGroup(Group group) {
        throw new IllegalArgumentException("Please use Liferay's user management API to do that.");
    }

    @Override
    public void deleteGroup(String groupId) {
        throw new IllegalArgumentException("Please use Liferay's user management API to do that.");
    }

    @Override
    public void createMembership(String userId, String groupId) {
        throw new IllegalArgumentException("Please use Liferay's user management API to do that.");
    }

    @Override
    public void deleteMembership(String userId, String groupId) {
        throw new IllegalArgumentException("Please use Liferay's user management API to do that.");
    }

    @Override
    public boolean checkPassword(String userId, String password) {
        throw new IllegalArgumentException("Please use Liferay's user management API to do that.");
    }

    @Override
    public void setAuthenticatedUserId(String authenticatedUserId) {
        //ignore
    }

    @Override
    public void setUserPicture(String userId, Picture picture) {
        throw new IllegalArgumentException("Please use Liferay's user management API to do that.");
    }

    @Override
    public void setUserInfo(String userId, String key, String value) {
        throw new IllegalArgumentException("Please use Liferay's user management API to do that.");
    }



    @Override
    public void deleteUserInfo(String userId, String key) {
        throw new IllegalArgumentException("Please use Liferay's user management API to do that.");
    }

//    @Override
//    public void setUserAccount(String userId, String userPassword, String accountName, String accountUsername, String accountPassword, Map<String, String> accountDetails) {
//        throw new IllegalArgumentException("Please use Liferay's user management API to do that.");
//    }
//
//
//    @Override
//    public void deleteUserAccount(String userId, String accountName) {
//        throw new IllegalArgumentException("Please use Liferay's user management API to do that.");
//    }
}
