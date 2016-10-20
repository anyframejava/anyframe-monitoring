/*
 * Copyright 2002-2008 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/
package net.sf.infrared2.gwt.client.view.facade;

import net.sf.infrared2.gwt.client.AbstractResizeListener;
import net.sf.infrared2.gwt.client.Constants;
import net.sf.infrared2.gwt.client.Engine;
import net.sf.infrared2.gwt.client.WindowResizeManagerFactory;
import net.sf.infrared2.gwt.client.callback.CacheAsyncCallback;
import net.sf.infrared2.gwt.client.i18n.ApplicationMessages;
import net.sf.infrared2.gwt.client.stack.StackPanelHolder;
import net.sf.infrared2.gwt.client.to.NavigatorEntryTO;
import net.sf.infrared2.gwt.client.to.application.ApplicationViewTO;
import net.sf.infrared2.gwt.client.to.other.OtherViewTO;
import net.sf.infrared2.gwt.client.to.sql.SqlViewTO;
import net.sf.infrared2.gwt.client.users.session.UserSessionBean;
import net.sf.infrared2.gwt.client.view.facade.layer.DefaultViewImpl;
import net.sf.infrared2.gwt.client.view.facade.layer.SQLLayerViewImpl;
import net.sf.infrared2.gwt.client.view.facade.layer.application.AbstractApplicationLayer;
import net.sf.infrared2.gwt.client.view.facade.layer.lastinv.LastInvocationViewImpl;
import net.sf.infrared2.gwt.client.view.facade.layer.other.OtherLayerViewImpl;
import net.sf.infrared2.gwt.client.view.header.HeaderPanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.WindowResizeListener;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.Ext;
import com.gwtext.client.core.Margins;
import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.widgets.HTMLPanel;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.event.PanelListenerAdapter;
import com.gwtext.client.widgets.layout.BorderLayout;
import com.gwtext.client.widgets.layout.BorderLayoutData;
import com.gwtext.client.widgets.layout.CardLayout;

/**
 * <b>ApplicationViewFacade</b>
 * <p>
 * Main point for managing application layout and views. Enable/disable
 * interaction with user for center frame and etc.
 */
public class ApplicationViewFacade {
    /** Single instance of ApplicationViewFacade. */
    private static final ApplicationViewFacade facade = new ApplicationViewFacade();
    /** Text for load indicator */
    private static final String LOAD_TEXT = ApplicationMessages.MESSAGES.loadingMask();
    /** Main border layout panel */
    private final Panel layout;
    /** Center panel of content */
    private Panel centerPanel;

    /**
     * Default Constructor. Creates empty main border layout of the application.
     */
    private ApplicationViewFacade() {
        layout = createBorderLayout();

    }

    /**
     * Clean center panel content and loads default view.
     */
    public static void cleanAll() {
        createDefaultView();
    }

    /**
     * Returns instance of the center panel.
     * 
     * @return instance of the center panel.
     */
    public static Panel getCenterPanel() {
        return facade.centerPanel;
    }

    /**
     * Creates default view.
     */
    public static void createDefaultView() {
        DefaultViewImpl.createView();
    }

    /**
     * Returns the main panel of the whole application.
     * 
     * @return the main panel of the whole application.
     */
    public static Panel getApplicationLayout() {
        return facade.layout;
    }

    /**
     * Mask all size of client's window (disable all user interaction with
     * interface).
     * @param text, text to be shown
     */
    public static void maskAll(String text) {
        if (text==null || "".equals(text)) facade.layout.getEl().mask();
        else facade.layout.getEl().mask(text);
        // Hack for IE. IE doesn't resize mask element.
        if (Ext.isIE()) {
            Window.addWindowResizeListener(maskResizeListener);
        }
    }

    /**
     * Discards the masking of size client's window .
     */
    public static void unmaskAll() {        
        facade.layout.getEl().unmask();
        unMaskCenter();
        // Hack for IE. IE doesn't resize mask element.
        if (Ext.isIE()) {
            Window.removeWindowResizeListener(maskResizeListener);
        }
    }

    /**
     * Mask center panel (disable all user interaction with center panel
     * elements).
     */
    public static void maskCenter() {
        facade.centerPanel.getEl().mask(LOAD_TEXT);
        // Hack for IE. IE doesn't resize mask element.
        if (Ext.isIE()) {
            Window.addWindowResizeListener(maskResizeListener);
        }
    }

    /**
     * Discards the masking of the center panel.
     */
    public static void unMaskCenter() {
        facade.centerPanel.getEl().unmask();
        // Hack for IE. IE doesn't resize mask element.
        if (Ext.isIE()) {
            Window.removeWindowResizeListener(maskResizeListener);
        }
    }

    /**
     * @deprecated
     * Check if center currently masked now.
     * 
     * @return true if masked.
     */
    public static boolean isMaskedCenter() {
        return facade.centerPanel.getEl().isMasked();
    }

    /**
     * @deprecated 
     * Removes all components from center panel.
     */
    public static void cleanCenterPanel() {
        facade.centerPanel.removeAll(true);
    }

    /**
     * Adds new panel to the center panel.
     * 
     * @param newPanel - panel, have to be added.
     */
    public static void addCenterPanel(Panel newPanel) {
        facade.showScreen(newPanel);
        facade.centerPanel.doLayout();
        unmaskAll();
    }

    /**
     * Process the action of selection some entry from navigation panel.
     */
    public static void processStack(final NavigatorEntryTO navigatorEntryTO) {
//        maskCenter();
        if (navigatorEntryTO.isRoot()) {
            Engine.getClient().getApplicationData(UserSessionBean.getApplConfigBeanInstance(),
                            navigatorEntryTO,
                            ApplicationMessages.MESSAGES.otherOperationsGraphic(), getAppDataCallback());
        } else if (navigatorEntryTO.isSQL()) {
            Engine.getClient().getSqlViewData(UserSessionBean.getApplConfigBeanInstance(),
                            navigatorEntryTO, getSqlDataCallback());

        } else if (Constants.MODULE_LAST_INV.equals(navigatorEntryTO.getModuleType())) {
            Engine.getClient().getOtherViewData(UserSessionBean.getApplConfigBeanInstance(),
                            navigatorEntryTO,
                            ApplicationMessages.MESSAGES.otherOperationsGraphic(),
                            getLastInvDataCallback());
        } else {
            Engine.getClient().getOtherViewData(UserSessionBean.getApplConfigBeanInstance(),
                            navigatorEntryTO,
                            ApplicationMessages.MESSAGES.otherOperationsGraphic(),
                            getOtherDataCallback());
        }
    }

    /**
     * Method showScreen ...
     *
     * @param panel of type Panel
     */
    public void showScreen(Panel panel) {
        String panelID = panel.getId();
        CardLayout cardLayout = (CardLayout) centerPanel.getLayout();
        if (centerPanel.getComponent(panelID) == null){
            centerPanel.add(panel);
        }
        cardLayout.setActiveItem(panelID);
    }

    /**
     * Builds empty main application layout based on BorderLayout
     * 
     * @return application Layout.
     */
    private Panel createBorderLayout() {

        Panel borderPanel = new Panel();
        borderPanel.setLayout(new BorderLayout());

        Panel northPanel = HeaderPanel.getInstance();
        northPanel.setAutoHeight(true);
        // northPanel.setAutoEl(autoEl)
        // northPanel.setBufferResize(bufferResize)
        // northPanel.setFloating(floating)

        borderPanel.add(northPanel, new BorderLayoutData(RegionPosition.NORTH));

        HTMLPanel southPanel = new HTMLPanel();
        southPanel.setId("footer");
        southPanel.setBaseCls("footer");
        southPanel.setHtml(ApplicationMessages.MESSAGES.footerContentLabel());
        BorderLayoutData southData = new BorderLayoutData(RegionPosition.SOUTH);
        southData.setMargins(new Margins(0, 0, 0, 0));

        borderPanel.add(southPanel, southData);

        final BorderLayoutData westData = new BorderLayoutData(RegionPosition.WEST);
        westData.setSplit(true);
        westData.setMinSize(200);
        westData.setMinWidth(200);
        westData.setMaxSize(350);
        Panel westPanel = StackPanelHolder.getInstance();
        westPanel.setWidth(200);

        borderPanel.add(westPanel, westData);

        final BorderLayoutData centerData = new BorderLayoutData(RegionPosition.CENTER);
        centerData.setMinSize(250);
        centerData.setMinWidth(250);
        centerData.setMinHeight(250);
        centerPanel = new Panel();
        centerPanel.setBorder(false);
        centerPanel.setBodyBorder(false);
        centerPanel.setAutoDestroy(false);
        centerPanel.addListener(new PanelListenerAdapter() {
            // it fires when splitter is moved (splitter between center panel
            // and navigator)
            public void onBodyResize(Panel panel, String width, String height) {
                WindowResizeManagerFactory.getResizeManager(
                                WindowResizeManagerFactory.GRIDS_MANAGER).fireResize();

            }
        });

        // final FitLayout fitLayout = new FitLayout();

        centerPanel.setLayout(new CardLayout());
        borderPanel.add(centerPanel, centerData);

        return borderPanel;
    }

    /**
     * This is special resize listener for IE, because IE does not correctly
     * resize masking element.
     */
    private static WindowResizeListener maskResizeListener = new AbstractResizeListener() {
        public void resize(int w, int h) {
            try {

                if (ApplicationViewFacade.getApplicationLayout().getEl() != null
                                && ApplicationViewFacade.getApplicationLayout().getEl().isMasked()) {
                    ApplicationViewFacade.unmaskAll();
                    ApplicationViewFacade.maskAll(LOAD_TEXT);
                } else if (ApplicationViewFacade.getCenterPanel().getEl() != null
                                && ApplicationViewFacade.getCenterPanel().getEl().isMasked()) {
                    ApplicationViewFacade.unMaskCenter();
                    ApplicationViewFacade.maskCenter();
                }

            } catch (Exception e) {
                GWT.log("Mask resize listener failed. ", e);
            }
        }
    };

    /**
     * Callback for Application view.
     */
//    private static AsyncCallback appDataCallback = new CacheAsyncCallback() {
//
//        public void atSuccess(Object result) {
//            AbstractApplicationLayer.buildView((ApplicationViewTO) result);
//        }
//
//        public void doFailure(Throwable caught) {
//
//        }
//
//    };
    
    /**
     * Callback for Application view.
     */
    private static AsyncCallback getAppDataCallback(){
    	return new CacheAsyncCallback() {

            public void atSuccess(Object result) {
                AbstractApplicationLayer.buildView((ApplicationViewTO) result);
            }

            public void doFailure(Throwable caught) {

            }

        };
    }
    
    /**
     * Callback for Other view.
     */
//    private static AsyncCallback otherDataCallback = new CacheAsyncCallback() {
//
//        public void atSuccess(Object result) {
//
//            OtherLayerViewImpl.buildView((OtherViewTO) result);
//
//        }
//
//        public void doFailure(Throwable caught) {
//
//        }
//    };
    
    /**
     * Callback for Other view.
     */
    private static AsyncCallback getOtherDataCallback(){
    	return new CacheAsyncCallback() {

            public void atSuccess(Object result) {

                OtherLayerViewImpl.buildView((OtherViewTO) result);

            }

            public void doFailure(Throwable caught) {

            }
        };
    }

    /**
     * Callback for Other view of last invocations module
     */
//    private static AsyncCallback lastInvDataCallback = new CacheAsyncCallback() {
//
//        public void atSuccess(Object result) {
//            LastInvocationViewImpl.buildView((OtherViewTO) result);
//
//        }
//
//        public void doFailure(Throwable caught) {
//
//        }
//    };
    
    /**
     * Callback for Other view of last invocations module
     */
    private static AsyncCallback getLastInvDataCallback(){
    	return new CacheAsyncCallback() {

            public void atSuccess(Object result) {
                LastInvocationViewImpl.buildView((OtherViewTO) result);

            }

            public void doFailure(Throwable caught) {

            }
        };
    }

    /**
     * Callback for SQL view.
     */
//    private static AsyncCallback sqlDataCallback = new CacheAsyncCallback() {
//
//        public void atSuccess(Object result) {
//            SQLLayerViewImpl.getInstance().createView((SqlViewTO) result);
//        }
//
//        public void doFailure(Throwable caught) {
//            ApplicationViewFacade.unMaskCenter();
//        }
//    };
    
    /**
     * Callback for SQL view.
     */
    private static AsyncCallback getSqlDataCallback(){
    	return new CacheAsyncCallback() {

            public void atSuccess(Object result) {
                SQLLayerViewImpl.getInstance().createView((SqlViewTO) result);
            }

            public void doFailure(Throwable caught) {
                ApplicationViewFacade.unMaskCenter();
            }
        };
    }

}
