package org.activiti.portlets.explorer.interop.liferay;

import com.liferay.portal.kernel.dao.orm.*;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.service.UserLocalServiceUtil;
import org.activiti.engine.identity.User;
import org.activiti.engine.identity.UserQuery;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 *
 * @author tomek@lipski.net.pl
 *         Date: 3/10/13 4:46 PM
 */
public class LiferayUserQueryImpl implements UserQuery {

    private static Logger LOGGER = Logger.getLogger(LiferayUserQueryImpl.class.getName());

    private String userId = null;
    private String userFirstName = null;
    private String userLastName = null;
    private String userFirstNameLike = null;
    private String userLastNameLike = null;
    private String userEmail = null;
    private String userEmailLike = null;
    private String memberOfGroup = null;
    private String potentialStarter = null;
    private String orderBy = null;
    private boolean asc = true;
    
    @Override
    public UserQuery userId(String id) {
        userId = id;
        return this;
    }

    @Override
    public UserQuery userFirstName(String firstName) {
        userFirstName = firstName;
        return this;
    }

    @Override
    public UserQuery userFirstNameLike(String firstNameLike) {
        userFirstNameLike = firstNameLike;
        return this;
    }

    @Override
    public UserQuery userLastName(String lastName) {
        userLastName = lastName;
        return this;
    }

    @Override
    public UserQuery userLastNameLike(String lastNameLike) {
        userLastNameLike = lastNameLike;
        return this;
    }

    @Override
    public UserQuery userEmail(String email) {
        userEmail = email;
        return this;
    }

    @Override
    public UserQuery userEmailLike(String emailLike) {
        userEmailLike = emailLike;
        return this;
    }

    @Override
    public UserQuery memberOfGroup(String groupId) {
        memberOfGroup = groupId;
        return this;
    }

    @Override
    public UserQuery potentialStarter(String procDefId) {
        potentialStarter = procDefId;
        return this;
    }

    @Override
    public UserQuery orderByUserId() {
        orderBy = "userId";
        return this;
    }

    @Override
    public UserQuery orderByUserFirstName() {
        orderBy = "firstName";
        return this;
    }

    @Override
    public UserQuery orderByUserLastName() {
        orderBy = "lastName";
        return this;
    }

    @Override
    public UserQuery orderByUserEmail() {
        orderBy = "emailAddress";
        return this;
    }

    @Override
    public UserQuery asc() {
        asc = true;
        return this;
    }

    @Override
    public UserQuery desc() {
        asc = false;
        return this;
    }

    @Override
    public long count() {
        try {
            return UserLocalServiceUtil.dynamicQueryCount(buildDynamicQuery());
        } catch (SystemException e) {
            LOGGER.log(Level.SEVERE, this.toString(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public User singleResult() {

        List<User> users = listPage(0, 1);
        if (users.isEmpty()) {
            return null;
        } else {
            return users.get(0);
        }
    }

    @Override
    public List<User> list() {
        //some sanity
        return listPage(0, 10000);
    }

    @Override
    public List<User> listPage(int firstResult, int maxResults) {
        try {
            DynamicQuery dq = buildDynamicQuery();
            List<com.liferay.portal.model.User> portalUsers = UserLocalServiceUtil.dynamicQuery(dq, firstResult, firstResult + maxResults);
            List<User> res = new ArrayList<User>(portalUsers.size());
            for (com.liferay.portal.model.User portalUser : portalUsers) {
                LOGGER.finer(this + "Found portal user:" + portalUser);

                res.add(new LiferayUserImpl(portalUser));
            }
            LOGGER.fine(this + " Found: " + res.size());

            return res;
        } catch (SystemException e) {
            LOGGER.log(Level.SEVERE, this.toString(), e);
            throw new RuntimeException(e);
        }
    }

    private DynamicQuery buildDynamicQuery() throws SystemException {
        LOGGER.fine("Searching for users: " + this);
        DynamicQuery dq = UserLocalServiceUtil.dynamicQuery();
        if (userId != null) {
            dq.add(PropertyFactoryUtil.forName("userId").eq(Long.parseLong(userId)));
        }

        if (userFirstName != null) {
            dq.add(PropertyFactoryUtil.forName("firstName").like(
                    "%" + userFirstName.replace("\\", "\\\\").replace("%", "\\%") + "%"));
        }
        if (userFirstNameLike != null) {
            dq.add(PropertyFactoryUtil.forName("firstName").like(userFirstNameLike));
        }
        if (userLastName != null) {
            dq.add(PropertyFactoryUtil.forName("lastName").like(
                    "%" + userLastName.replace("\\", "\\\\").replace("%", "\\%") + "%"));
        }
        if (userLastNameLike != null) {
            dq.add(PropertyFactoryUtil.forName("lastName").like(userLastNameLike));
        }
        if (userEmail != null) {
            dq.add(PropertyFactoryUtil.forName("emailAddress").like(
                    "%" + userEmail.replace("\\", "\\\\").replace("%", "\\%") + "%"));
        }

        if (userEmailLike != null) {
            dq.add(PropertyFactoryUtil.forName("emailAddress").like(userEmailLike));
        }
        if (memberOfGroup != null) {
            Collection userIds = new HashSet<Long>();
            for (com.liferay.portal.model.User user : UserLocalServiceUtil.getUserGroupUsers(Long.parseLong(memberOfGroup))) {
                userIds.add(user.getUserId());
            }

            if (!userIds.isEmpty())
                dq.add(PropertyFactoryUtil.forName("userId").in(userIds));
        }
        if (orderBy != null) {
            Property orderByProperty = PropertyFactoryUtil.forName(orderBy);
            if (asc) {
                dq.addOrder(orderByProperty.asc());
            } else {
                dq.addOrder(orderByProperty.desc());
            }
        }
        LOGGER.fine(this + " Query: " + dq);
        return dq;
    }

    @Override
    public String toString() {
        return "LiferayUserQueryImpl{" +
                "userId='" + userId + '\'' +
                ", userFirstName='" + userFirstName + '\'' +
                ", userLastName='" + userLastName + '\'' +
                ", userFirstNameLike='" + userFirstNameLike + '\'' +
                ", userLastNameLike='" + userLastNameLike + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userEmailLike='" + userEmailLike + '\'' +
                ", memberOfGroup='" + memberOfGroup + '\'' +
                ", potentialStarter='" + potentialStarter + '\'' +
                ", orderBy='" + orderBy + '\'' +
                ", asc=" + asc +
                '}';
    }
}
