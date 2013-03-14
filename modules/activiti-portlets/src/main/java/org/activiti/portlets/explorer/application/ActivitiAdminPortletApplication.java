package org.activiti.portlets.explorer.application;

/**
 * Portlet application for Activiti Explorer.
 *
 * Works only with Liferay 6.0+.
 *
 * @author tomek@lipski.net.pl
 * Date: 2/22/13 7:55 PM
 */
public class ActivitiAdminPortletApplication extends ActivitiGenericPortletApplication {

    protected void initView() {
        viewManager.showAdministrationPage();
    }
}
