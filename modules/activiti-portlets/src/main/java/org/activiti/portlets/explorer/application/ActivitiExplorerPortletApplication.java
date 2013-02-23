package org.activiti.portlets.explorer.application;

import com.liferay.portal.model.User;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.util.PortalUtil;
import com.vaadin.service.ApplicationContext;
import com.vaadin.terminal.gwt.server.PortletApplicationContext2;
import com.vaadin.ui.Label;
import com.vaadin.ui.Window;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.identity.Group;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.explorer.Constants;
import org.activiti.explorer.ExplorerApp;
import org.activiti.explorer.identity.LoggedInUserImpl;
import org.activiti.explorer.ui.task.InboxPage;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Portlet application for Activiti Explorer.
 *
 * Works only with Liferay 6.0+.
 *
 * @author tomek@lipski.net.pl
 * Date: 2/22/13 7:55 PM
 */
public class ActivitiExplorerPortletApplication extends
        ExplorerApp implements PortletApplicationContext2.PortletListener {
    private boolean portlet = false;
    @Override
    public void init() {

        ApplicationContext context = getContext();
        if (context instanceof PortletApplicationContext2) {
            PortletApplicationContext2 portletContext = (PortletApplicationContext2) context;
            portletContext.addPortletListener(this, this);
            portlet = true;
            setMainWindow(new Window());

        } else{
            super.init();
        }

    }


    @Override
    public void handleRenderRequest(javax.portlet.RenderRequest renderRequest, javax.portlet.RenderResponse renderResponse, Window window) {
        // Set current application object as thread-local to make it easy accessible
        current.set(this);

        try {
//            setMainWindow(mainWindow);
//            mainWindow.setSizeFull();
//            if (getUser() == null )       //TODO optimize!
            User portalUser = PortalUtil.getUser(renderRequest);
            if (portalUser != null) {
                String userId = String.valueOf(portalUser.getUserId());

                IdentityService identityService = ProcessEngines.getDefaultProcessEngine().getIdentityService();
                org.activiti.engine.identity.User activitiUser =
                        identityService.createUserQuery().userId(userId).singleResult();
                if (activitiUser == null) {
                    activitiUser = identityService.newUser(userId);
                    activitiUser.setEmail(portalUser.getEmailAddress());
                    activitiUser.setFirstName(portalUser.getFirstName());
                    activitiUser.setLastName(portalUser.getLastName());
                    activitiUser.setPassword(String.valueOf(Math.random()));
                } else {
                    activitiUser.setEmail(portalUser.getEmailAddress());
                    activitiUser.setFirstName(portalUser.getFirstName());
                    activitiUser.setLastName(portalUser.getLastName());
                    activitiUser.setPassword(String.valueOf(Math.random()));
                }
                identityService.saveUser(activitiUser);

                List<com.liferay.portal.model.Group> userGroups = GroupLocalServiceUtil.getUserGroups(portalUser.getUserId());

                List<Group> activitiGroups = identityService.createGroupQuery().groupMember(userId).list();
                for (Group activitGroup : activitiGroups) {
                    identityService.deleteMembership(userId, activitGroup.getId());
                }
                for (com.liferay.portal.model.Group portalGroup : userGroups) {
                    String groupId = String.valueOf(portalGroup.getGroupId());
                    if (identityService.createGroupQuery()
                            .groupId(groupId).singleResult() == null) {
                        Group activitiGroup = identityService.newGroup(groupId);
                        activitiGroup.setName(portalGroup.getName());
                        activitiGroup.setType("assignment");  //http://developer4life.blogspot.com/2012/02/activiti-authentication-and-identity.html
                        identityService.saveGroup(activitiGroup);
                    }
                    identityService.createMembership(userId, groupId);
                }
                if (PortalUtil.isOmniadmin(portalUser.getUserId())) {

                }

                LoggedInUserImpl loggedInUser = new LoggedInUserImpl(activitiUser, "********");
                List<Group> groups = identityService.createGroupQuery().groupMember(userId).list();
                for (Group group : groups) {
                    if (Constants.SECURITY_ROLE.equals(group.getType())) {
                        loggedInUser.addSecurityRoleGroup(group);
                        if (Constants.SECURITY_ROLE_USER.equals(group.getId())) {
                            loggedInUser.setUser(true);
                        }
                        if (Constants.SECURITY_ROLE_ADMIN.equals(group.getId())) {
                            loggedInUser.setAdmin(true);
                        }
                    } else {
                        loggedInUser.addGroup(group);
                    }
                }
                setUser(loggedInUser);
                Authentication.setAuthenticatedUserId(userId);
                identityService.setAuthenticatedUserId(userId);
//                viewManager.showDefaultPage();
                mainWindow.removeAllComponents();
                InboxPage c = new InboxPage();
                c.setSizeFull();
                mainWindow.addComponent(c);
            } else {
                mainWindow.removeAllComponents();
                mainWindow.addComponent(new Label("Please sign in")); //TODO i18n
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void terminalError(com.vaadin.terminal.Terminal.ErrorEvent event) {
        Logger.getLogger(ActivitiExplorerPortletApplication.class.getName())
                .log(Level.SEVERE, "Vaadin terminal error", event.getThrowable());

        super.terminalError(event);
    }

    @Override
    public void handleActionRequest(javax.portlet.ActionRequest actionRequest, javax.portlet.ActionResponse actionResponse, Window window) {
        //nothing to do here
    }

    @Override
    public void handleEventRequest(javax.portlet.EventRequest eventRequest, javax.portlet.EventResponse eventResponse, Window window) {
        //nothing to do here
    }

    @Override
    public void handleResourceRequest(javax.portlet.ResourceRequest resourceRequest, javax.portlet.ResourceResponse resourceResponse, Window window) {
        //nothing to do here
    }
}
