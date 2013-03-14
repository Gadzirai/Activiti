package org.activiti.portlets.explorer.interop.liferay;

import org.activiti.spring.SpringProcessEngineConfiguration;

/**
 * Created with IntelliJ IDEA.
 *
 * @author tomek@lipski.net.pl
 *         Date: 3/10/13 4:43 PM
 */
public class SpringPortletProcessEngineConfigurationImpl extends SpringProcessEngineConfiguration {

    public SpringPortletProcessEngineConfigurationImpl() {
        identityService = new LiferayIdentityServiceImpl();
    }
}
