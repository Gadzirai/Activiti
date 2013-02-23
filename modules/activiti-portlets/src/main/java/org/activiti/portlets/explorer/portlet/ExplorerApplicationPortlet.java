package org.activiti.portlets.explorer.portlet;

import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.ApplicationPortlet2;
import com.vaadin.terminal.gwt.server.PortletApplicationContext2;
import org.activiti.explorer.ExplorerApp;
import org.activiti.portlets.explorer.application.ActivitiExplorerPortletApplication;
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

/**
 * Created with IntelliJ IDEA.
 *
 * @author tomek@lipski.net.pl
 *         Date: 2/23/13 4:06 PM
 */
public class ExplorerApplicationPortlet extends ApplicationPortlet2 {


    private static final long serialVersionUID = 1L;

    protected ApplicationContext applicationContext;

    @Override
    public void init(PortletConfig portletConfig) throws PortletException {
        super.init(portletConfig);

        applicationContext = PortletApplicationContextUtils.getWebApplicationContext(portletConfig.getPortletContext());
    }

    @Override
    protected Class<? extends Application> getApplicationClass() {
        return ActivitiExplorerPortletApplication.class;
    }


    @Override
    protected Application getNewApplication(PortletRequest request) {
        if (applicationContext == null) {
            PortletContext portletContext = getPortletContext();
            applicationContext = PortletApplicationContextUtils.getWebApplicationContext(getPortletContext());
        }
        return  applicationContext.getBean(ExplorerApp.class);
    }

    @Override
    protected void handleRequest(PortletRequest request, PortletResponse response) throws PortletException, IOException {
        LocaleContext previousLocaleContext = LocaleContextHolder.getLocaleContext();
        LocaleContextHolder.setLocaleContext( new SimpleLocaleContext(request.getLocale()), false);

        // Expose current RequestAttributes to current thread.
        RequestAttributes previousRequestAttributes = RequestContextHolder.getRequestAttributes();
        PortletRequestAttributes requestAttributes = null;
        if (previousRequestAttributes == null || previousRequestAttributes.getClass().equals(PortletRequestAttributes.class)) {
            requestAttributes = new PortletRequestAttributes(request);
            RequestContextHolder.setRequestAttributes(requestAttributes, false);
        }

        try {
            PortletApplicationContext2 context = getApplicationContext(request.getPortletSession());
            Application application = context.getApplicationForWindowId(request.getWindowID());
            if (application instanceof ActivitiExplorerPortletApplication) {
                ((ActivitiExplorerPortletApplication)application).setInCtx();
            }
            super.handleRequest(request, response);
        } finally {
            LocaleContextHolder.setLocaleContext(previousLocaleContext, false);
            if (requestAttributes != null) {
                RequestContextHolder.setRequestAttributes(previousRequestAttributes, false);
                requestAttributes.requestCompleted();
            }
        }
    }
//    @Override
//    protected void writeAjaxPageHtmlVaadinScripts(Window window, String themeName, Application application, BufferedWriter page, String appUrl, String themeUri,
//                                                  String appId, HttpServletRequest request) throws ServletException, IOException {
//        super.writeAjaxPageHtmlVaadinScripts(window, themeName, application, page, appUrl, themeUri, appId, request);
//
//        // Add static JS files
//        String scrollJs = themeUri + "/js/vscrollarea.js";
//        page.write("<script type=\"text/javascript\" src=\"" + scrollJs + "\" />");
//
//        String browserDependentCss = "<script type=\"text/javascript\">//<![CDATA[" +
//                "var mobi = ['opera', 'iemobile', 'webos', 'android', 'blackberry', 'ipad', 'safari'];" +
//                "var midp = ['blackberry', 'symbian'];" +
//                "var ua = navigator.userAgent.toLowerCase();" +
//                "if ((ua.indexOf('midp') != -1) || (ua.indexOf('mobi') != -1) || ((ua.indexOf('ppc') != -1) && (ua.indexOf('mac') == -1)) || (ua.indexOf('webos') != -1)) {" +
//                "  document.write('<link rel=\"stylesheet\" href=\"" + themeUri + "/allmobile.css\" type=\"text/css\" media=\"all\"/>');" +
//                "  if (ua.indexOf('midp') != -1) {" +
//                "    for (var i = 0; i < midp.length; i++) {" +
//                "      if (ua.indexOf(midp[i]) != -1) {" +
//                "        document.write('<link rel=\"stylesheet\" href=\"" + themeUri + "' + midp[i] + '.css\" type=\"text/css\"/>');" +
//                "      }" +
//                "    }" +
//                "  }" +
//                "   else {" +
//                "     if ((ua.indexOf('mobi') != -1) || (ua.indexOf('ppc') != -1) || (ua.indexOf('webos') != -1)) {" +
//                "       for (var i = 0; i < mobi.length; i++) {" +
//                "         if (ua.indexOf(mobi[i]) != -1) {" +
//                "           if ((mobi[i].indexOf('blackberry') != -1) && (ua.indexOf('6.0') != -1)) {" +
//                "             document.write('<link rel=\"stylesheet\" href=\"" + themeUri + "' + mobi[i] + '6.0.css\" type=\"text/css\"/>');" +
//                "           }" +
//                "           else {" +
//                "             document.write('<link rel=\"stylesheet\" href=\"" + themeUri + "' + mobi[i] + '.css\" type=\"text/css\"/>');" +
//                "           }" +
//                "          break;" +
//                "         }" +
//                "       }" +
//                "     }" +
//                "   }" +
//                " }" +
//                "if ((navigator.userAgent.indexOf('iPhone') != -1) || (navigator.userAgent.indexOf('iPad') != -1)) {" +
//                " document.write('<meta name=\"viewport\" content=\"width=device-width\" />');" +
//                "}" +
//                "  //]]>" +
//                "</script>" +
//                "<!--[if lt IE 7]><link rel=\"stylesheet\" type=\"text/css\" href=\"" + themeUri + "/lt7.css\" /><![endif]-->";
//
//        page.write(browserDependentCss);
//    }

}
