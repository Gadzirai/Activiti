package org.activiti.portlets.explorer.portlet;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.User;
import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.ApplicationPortlet2;
import com.vaadin.terminal.gwt.server.PortletApplicationContext2;
import org.activiti.explorer.ExplorerApp;
import org.activiti.portlets.explorer.application.ActivitiGenericPortletApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.i18n.SimpleLocaleContext;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.portlet.context.PortletApplicationContextUtils;
import org.springframework.web.portlet.context.PortletRequestAttributes;

import javax.portlet.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 *
 * @author tomek@lipski.net.pl
 *         Date: 2/23/13 4:06 PM
 */
public class ActivitiApplicationPortlet extends ApplicationPortlet2 {

    private static final Logger LOGGER = Logger.getLogger(ActivitiApplicationPortlet.class.getName());

    private static final long serialVersionUID = 1L;

    protected ApplicationContext applicationContext;

    @Override
    public void init(PortletConfig portletConfig) throws PortletException {
        super.init(portletConfig);

        applicationContext = PortletApplicationContextUtils.getWebApplicationContext(portletConfig.getPortletContext());
    }

    @Override
    protected Application getNewApplication(PortletRequest request) {
        if (applicationContext == null) {
            PortletContext portletContext = getPortletContext();
            applicationContext = PortletApplicationContextUtils.getWebApplicationContext(getPortletContext());
        }
        return  applicationContext.getBean(getApplicationClass());
    }

    @Override
    protected void handleRequest(PortletRequest request, PortletResponse response) throws PortletException, IOException {
        LOGGER.finer("Handling portlet request");
        LocaleContext previousLocaleContext = LocaleContextHolder.getLocaleContext();
        LocaleContextHolder.setLocaleContext( new SimpleLocaleContext(request.getLocale()), false);

        // Expose current RequestAttributes to current thread.
        RequestAttributes previousRequestAttributes = RequestContextHolder.getRequestAttributes();
        PortletRequestAttributes requestAttributes = null;
        if (previousRequestAttributes == null || previousRequestAttributes.getClass().equals(PortletRequestAttributes.class)) {
            LOGGER.finer("Setting request attributes for spring context");
            requestAttributes = new PortletRequestAttributes(request);
            RequestContextHolder.setRequestAttributes(requestAttributes, false);
        }


        try {
            PortletApplicationContext2 context = getApplicationContext(request.getPortletSession());
            Application application = context.getApplicationForWindowId(request.getWindowID());
            if (application instanceof ActivitiGenericPortletApplication) {
                LOGGER.finer("Found ActivitiGenericPortletApplication to be processed, setting it in Spring Context");
                ActivitiGenericPortletApplication explorerPortletApplication = (ActivitiGenericPortletApplication) application;
                try {
                    User user = explorerPortletApplication.bridgePortalUser(request);
                    if (user != null) {
                        LOGGER.finer("Bridged portal user to Activiti: " + user.getLogin());
                    } else {
                        LOGGER.fine("No portal user to be bridged to Activiti!");
                    }

                } catch (PortalException e) {
                    LOGGER.log(Level.SEVERE, "Failed to bridge portal user to Activiti: ", e);
                    throw new PortletException(e);
                } catch (SystemException e) {
                    LOGGER.log(Level.SEVERE, "Failed to bridge portal user to Activiti: ", e);
                    throw new PortletException(e);
                }
                explorerPortletApplication.setInCtx();
            }
            super.handleRequest(request, response);
        } finally {
            LOGGER.finer("Done handling portlet request, unregistering contexts");
            LocaleContextHolder.setLocaleContext(previousLocaleContext, false);
            if (requestAttributes != null) {
                RequestContextHolder.setRequestAttributes(previousRequestAttributes, false);
                requestAttributes.requestCompleted();
            }
        }
    }
}
