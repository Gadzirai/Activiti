package org.activiti.portlets.explorer.portlet;

import com.vaadin.terminal.gwt.server.PortletRequestListener;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.i18n.SimpleLocaleContext;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.portlet.context.PortletRequestAttributes;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.servlet.ServletRequestEvent;
import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 *
 * @author tomek@lipski.net.pl
 *         Date: 2/23/13 5:37 PM
 */
public class SpringPortletRequestContextListener implements PortletRequestListener {
    private static final String REQUEST_ATTRIBUTES_ATTRIBUTE =
            RequestContextListener.class.getName() + ".REQUEST_ATTRIBUTES";


    public void onRequestStart(PortletRequest request, PortletResponse response) {
        // Expose current LocaleResolver and request as LocaleContext.
        LocaleContext previousLocaleContext = LocaleContextHolder.getLocaleContext();
        LocaleContextHolder.setLocaleContext( new SimpleLocaleContext(request.getLocale()), false);

        // Expose current RequestAttributes to current thread.
        RequestAttributes previousRequestAttributes = RequestContextHolder.getRequestAttributes();
        PortletRequestAttributes requestAttributes = null;
        if (previousRequestAttributes == null || previousRequestAttributes.getClass().equals(PortletRequestAttributes.class)) {
            requestAttributes = new PortletRequestAttributes(request);
            RequestContextHolder.setRequestAttributes(requestAttributes, false);
        }
    }

    public void onRequestEnd(PortletRequest request, PortletResponse response) {
        //TODO
//        LocaleContextHolder.setLocaleContext(previousLocaleContext, this.threadContextInheritable);
//        if (requestAttributes != null) {
//            RequestContextHolder.setRequestAttributes(previousRequestAttributes, this.threadContextInheritable);
//            requestAttributes.requestCompleted();
//        }

    }

}
