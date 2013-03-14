package org.activiti.portlets.explorer.interop.liferay;

import org.activiti.engine.identity.User;

/**
 * Created with IntelliJ IDEA.
 *
 * @author tomek@lipski.net.pl
 *         Date: 3/10/13 7:11 PM
 */
public class LiferayUserImpl implements User {

    private com.liferay.portal.model.User portalUser;

    public LiferayUserImpl(com.liferay.portal.model.User portalUser) {
        this.portalUser = portalUser;
    }

    @Override
    public String getId() {
        return String.valueOf(portalUser.getUserId());
    }

    @Override
    public void setId(String id) {
       //nothing
    }

    @Override
    public String getFirstName() {
        return portalUser.getFirstName();
    }

    @Override
    public void setFirstName(String firstName) {
        //nothing
    }

    @Override
    public void setLastName(String lastName) {
        //nothing
    }

    @Override
    public String getLastName() {
        return portalUser.getLastName();
    }

    @Override
    public void setEmail(String email) {
        //nothing
    }

    @Override
    public String getEmail() {
        return portalUser.getEmailAddress();
    }

    @Override
    public String getPassword() {
        return null;//not needed
    }

    @Override
    public void setPassword(String string) {
        //nothing
    }
}
