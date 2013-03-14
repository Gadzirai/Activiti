package org.activiti.portlets.explorer.interop.liferay;

import org.activiti.engine.identity.Group;

/**
 * Created with IntelliJ IDEA.
 *
 * @author tomek@lipski.net.pl
 *         Date: 3/11/13 12:21 AM
 */
public class LiferayGroupImpl implements Group {
    private com.liferay.portal.model.Group portalGroup;

    public LiferayGroupImpl(com.liferay.portal.model.Group portalGroup) {
        this.portalGroup = portalGroup;
    }

    @Override
    public String getId() {
        return String.valueOf(portalGroup.getGroupId());
    }

    @Override
    public void setId(String id) {
        //nothing
    }

    @Override
    public String getName() {
        return portalGroup.getName();
    }

    @Override
    public void setName(String name) {
        //nothing
    }

    @Override
    public String getType() {
        return "assignment";
    }

    @Override
    public void setType(String string) {
        //nothing
    }
}
