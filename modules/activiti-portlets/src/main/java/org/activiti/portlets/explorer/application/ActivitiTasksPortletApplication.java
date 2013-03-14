package org.activiti.portlets.explorer.application;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.User;
import com.liferay.portal.util.PortalUtil;
import com.vaadin.service.ApplicationContext;
import com.vaadin.terminal.gwt.server.PortletApplicationContext2;
import com.vaadin.ui.Label;
import com.vaadin.ui.Window;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.explorer.ExplorerApp;
import org.activiti.explorer.identity.LoggedInUserImpl;
import org.activiti.explorer.navigation.UriFragment;

import javax.portlet.PortletRequest;
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
public class ActivitiTasksPortletApplication extends ActivitiGenericPortletApplication {

    protected void initView() {
        viewManager.showTasksPage();
    }
}
