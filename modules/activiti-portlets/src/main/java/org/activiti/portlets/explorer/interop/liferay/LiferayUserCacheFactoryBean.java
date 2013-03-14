package org.activiti.portlets.explorer.interop.liferay;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.util.PortalUtil;
import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.User;
import org.activiti.explorer.cache.UserCache;
import org.activiti.explorer.cache.UserCacheFactoryBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author tomek@lipski.net.pl
 *         Date: 3/11/13 12:54 AM
 */
public class LiferayUserCacheFactoryBean extends UserCacheFactoryBean {

    private IdentityService identityService;

    public IdentityService getIdentityService() {
        return identityService;
    }

    public void setIdentityService(IdentityService identityService) {
        this.identityService = identityService;
    }

    public UserCache getObject() throws Exception {
        if(userCache == null) {
            initUserCache();
        }
        return userCache;
    }

    private void initUserCache() {
        userCache = new LiferayUserCacheImpl();
    }

    private class LiferayUserCacheImpl implements UserCache {
        @Override
        public List<User> findMatchingUsers(String prefix) {
            try {
                List<com.liferay.portal.model.User> search = UserLocalServiceUtil.search(PortalUtil.getDefaultCompanyId(),
                        prefix, 0, null, 0, 100, (OrderByComparator) null);
                List<User> result = new ArrayList<User>(search.size());
                for (com.liferay.portal.model.User portalUser : search) {
                    result.add(new LiferayUserImpl(portalUser));
                }
                return result;
            } catch (SystemException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public User findUser(String userId) {
            try {
                com.liferay.portal.model.User user = UserLocalServiceUtil.getUser(Long.parseLong(userId));
                if (user != null) {
                    return new LiferayUserImpl(user);
                } else {
                    return null;
                }
            } catch (PortalException e) {
                throw new RuntimeException(e);
            } catch (SystemException e) {
                throw new RuntimeException(e);
            }

        }

        @Override
        public void notifyUserDataChanged(String userId) {
            //nothing
        }
    }
}
