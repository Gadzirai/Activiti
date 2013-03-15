package org.activiti.portlets.explorer.interop.liferay;

import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.Property;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.service.GroupLocalServiceUtil;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.GroupQuery;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 *
 * @author tomek@lipski.net.pl
 *         Date: 3/10/13 4:47 PM
 */
public class LiferayGroupQueryImpl implements GroupQuery {

    private static Logger LOGGER = Logger.getLogger(LiferayUserQueryImpl.class.getName());

    private String groupId, groupName, groupNameLike, groupType, groupMember, potentialStarter, orderBy;
    private boolean asc = true;
    @Override
    public GroupQuery groupId(String groupId) {
        this.groupId = groupId;
        return this;
    }

    @Override
    public GroupQuery groupName(String groupName) {
        this.groupName = groupName;
        return this;
    }

    @Override
    public GroupQuery groupNameLike(String groupNameLike) {
        this.groupNameLike = groupNameLike;
        return this;
    }

    @Override
    public GroupQuery groupType(String groupType) {
        this.groupType = groupType;
        return this;
    }

    @Override
    public GroupQuery groupMember(String groupMemberUserId) {
        this.groupMember = groupMemberUserId;
        return this;
    }

    @Override
    public GroupQuery potentialStarter(String procDefId) {
        this.potentialStarter = procDefId;
        return this;
    }

    @Override
    public GroupQuery orderByGroupId() {
        orderBy = "groupId";
        return this;
    }

    @Override
    public GroupQuery orderByGroupName() {
        orderBy = "name";
        return this;
    }

    @Override

    public GroupQuery orderByGroupType() {
        orderBy = "groupType";
        return this;
    }

    @Override
    public GroupQuery asc() {
        asc = true;
        return this;
    }

    @Override
    public GroupQuery desc() {
        asc = false;
        return this;
    }

    @Override
    public long count() {
        try {
            return GroupLocalServiceUtil.dynamicQueryCount(buildDynamicQuery());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, toString(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Group singleResult() {
        List<Group> groups = listPage(0, 1);
        if (groups.isEmpty()) {
            return null;
        } else {
            return groups.get(0);
        }
    }

    @Override
    public List<Group> list() {
        return listPage(0, 10000);
    }

    @Override
    public List<Group> listPage(int firstResult, int maxResults) {
        try {
            List<com.liferay.portal.model.Group> list =
                    GroupLocalServiceUtil.dynamicQuery(buildDynamicQuery(), firstResult, maxResults);
            LOGGER.fine(this + " found " + list.size() + " groups");
            List<Group> results = new ArrayList<Group>(list.size());
            for (com.liferay.portal.model.Group portalGroup : list) {
                LOGGER.finer(this + " found group: " + portalGroup);
                results.add(new LiferayGroupImpl(portalGroup));
            }
            return results;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, toString(), e);
            throw new RuntimeException(e);
        }

    }

    private DynamicQuery buildDynamicQuery() throws SystemException, PortalException {

        LOGGER.fine("Searching for groups: " + this);
        DynamicQuery dq = GroupLocalServiceUtil.dynamicQuery();
        if (groupId != null) {
            dq.add(PropertyFactoryUtil.forName("name").eq(groupId));
        }


        if (groupName != null) {
            dq.add(PropertyFactoryUtil.forName("description").like(
                    "%" + groupName.replace("\\", "\\\\").replace("%", "\\%") + "%"));
        }
        if (groupNameLike != null) {
            dq.add(PropertyFactoryUtil.forName("description").like(groupNameLike));
        }
        if (groupType != null) { //TODO
//            dq.add(PropertyFactoryUtil.forName("lastName").like(
//                    "%" + userLastName.replace("\\", "\\\\").replace("%", "\\%") + "%"));
        }
        if (groupMember != null) {
            Collection groupIds = new HashSet<Long>();
            for (com.liferay.portal.model.Group grp : GroupLocalServiceUtil
                    .getUserGroups(Long.parseLong(groupMember))) {
                groupIds.add(grp.getGroupId());
            }

            if (!groupIds.isEmpty())
                dq.add(PropertyFactoryUtil.forName("groupId").in(groupIds));

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
        return "LiferayGroupQueryImpl{" +
                "groupId='" + groupId + '\'' +
                ", groupName='" + groupName + '\'' +
                ", groupNameLike='" + groupNameLike + '\'' +
                ", groupType='" + groupType + '\'' +
                ", groupMember='" + groupMember + '\'' +
                ", potentialStarter='" + potentialStarter + '\'' +
                ", orderBy='" + orderBy + '\'' +
                ", asc=" + asc +
                '}';
    }
}
