package org.activiti.portlets.explorer.application;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.User;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.util.PortalUtil;
import com.vaadin.service.ApplicationContext;
import com.vaadin.terminal.Terminal;
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
import org.activiti.explorer.navigation.UriFragment;

import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
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
            setMainWindow(mainWindow);
            setErrorHandler(this);
        } else{
            super.init();
        }

    }

    public void setInCtx() {
        current.set(this);
    }

    @Override
    public void handleRenderRequest(javax.portlet.RenderRequest renderRequest, javax.portlet.RenderResponse renderResponse, Window window) {
        // Set current application object as thread-local to make it easy accessible
        current.set(this);

        try {
//            setMainWindow(mainWindow);
            mainWindow.setHeight(null);
//            mainWindow.setSizeFull();
//            if (getUser() == null )       //TODO optimize!
            User portalUser = bridgePortalUser(renderRequest);
            if (portalUser != null) {
                viewManager.showTasksPage();
            } else {
                getMainWindow().removeAllComponents();
                getMainWindow().addComponent(new Label("Please sign in")); //TODO i18n
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public User bridgePortalUser(PortletRequest request) throws PortalException, SystemException {
        User portalUser = PortalUtil.getUser(request);
        if (portalUser != null) {
            String userId = String.valueOf(portalUser.getUserId());

            IdentityService identityService = ProcessEngines.getDefaultProcessEngine().getIdentityService();
            org.activiti.engine.identity.User activitiUser =
                    identityService.createUserQuery().userId(userId).singleResult();

//            List<com.liferay.portal.model.Group> userGroups = GroupLocalServiceUtil.getUserGroups(portalUser.getUserId());

            LoggedInUserImpl loggedInUser = new LoggedInUserImpl(activitiUser, "********");

            if (PortalUtil.isOmniadmin(portalUser.getUserId())) {
                loggedInUser.setUser(true);
                loggedInUser.setAdmin(true);
            } else {
                loggedInUser.setUser(true);
            }

            setUser(loggedInUser);
            Authentication.setAuthenticatedUserId(userId);
            identityService.setAuthenticatedUserId(userId);

        }
        return portalUser;
    }


    @Override
    public void setCurrentUriFragment(UriFragment fragment) {
        //nothing, we use portal for now
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
