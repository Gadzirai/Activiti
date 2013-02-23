package org.activiti.portlets.explorer.ui;

import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import org.activiti.explorer.navigation.UriFragment;
import org.activiti.explorer.ui.MainWindow;

/**
 * Created with IntelliJ IDEA.
 *
 * @author tomek@lipski.net.pl
 *         Date: 2/24/13 12:04 AM
 */
public class PortletMainWindow extends MainWindow {

    @Override
    public void attach() {
    }

    @Override
    public void showLoginPage() {
    }

    @Override
    public void showDefaultContent() {

    }

    @Override
    public void switchView(Component component) {
        CssLayout lay = new CssLayout();
        lay.addComponent(component);
        setContent(lay);
        //in a portlet, we cannot have height 100%, because 100% of 0 pixels available by default gives height=0
        component.setHeight(null);
    }

    @Override
    public void setMainNavigation(String navigation) {
//        super.setMainNavigation(navigation);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    protected void initHiddenComponents() {
//        super.initHiddenComponents();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public UriFragment getCurrentUriFragment() {
        return null;
    }

    @Override
    public void setCurrentUriFragment(UriFragment fragment) {
//        super.setCurrentUriFragment(fragment);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public boolean isShowingLoginPage() {
        return false;
    }
}
