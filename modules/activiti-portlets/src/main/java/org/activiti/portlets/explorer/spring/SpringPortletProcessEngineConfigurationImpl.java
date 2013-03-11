package org.activiti.portlets.explorer.spring;

import org.activiti.engine.impl.IdentityServiceImpl;
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
