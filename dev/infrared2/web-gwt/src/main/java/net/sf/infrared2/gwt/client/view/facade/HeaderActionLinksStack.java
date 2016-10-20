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
/* 
 * HeaderActionLinkStack.java Date created: 21.01.2008
 * Last modified by: $Author: soyon.lim $ 
 * $Revision: 14286 $ $Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.gwt.client.view.facade;

import net.sf.infrared2.gwt.client.Engine;
import net.sf.infrared2.gwt.client.i18n.ApplicationMessages;
import net.sf.infrared2.gwt.client.users.session.UserSessionBean;
import net.sf.infrared2.gwt.client.utils.BrowserUtils;
import net.sf.infrared2.gwt.client.view.header.HeaderLinkStackHolder;
import net.sf.infrared2.gwt.client.view.reportwizard.SIReportWizard;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

/**
 * <b>HeaderActionLinksStack</b><p>
 * Fill header panel links according external rules:<br>
 * You can customize count of links and its actions.
 *
 * @author Andrey Zavgorodniy
 */
public class HeaderActionLinksStack {

    /** Report hyper link presentation */
    private static final HTML reportLink = new HTML(ApplicationMessages.MESSAGES.reportLink());
    /** Refresh hyper link presentation */
    private static final HTML refreshLink = new HTML(ApplicationMessages.MESSAGES.refreshLink());
    /** The Constant resetLink. */
    private static final HTML resetLink = new HTML(ApplicationMessages.MESSAGES.resetLink());

    /** Report link click listener handler */
    private static final ReportHeaderLinkClickListener reportClickListener = new ReportHeaderLinkClickListener();
    /** Refresh link click listener handler */
    private static final RefreshHeaderLinkClickListener refreshClickListener = new RefreshHeaderLinkClickListener();
    
    private static final ResetHeaderLinkClickListener resetClickListener = new ResetHeaderLinkClickListener();

    /**
     * Get info from server and put to HeaderLinkStackHolder
     */
    public static void init() {
        if (UserSessionBean.isAnyApplSelected()){
            enableReportLink();
            enableRefreshLink();
            enableResetLink();
        }
        else {
            disableReportLink();
            disableRefreshLink();
            disableResetLink();
        }
        HTML[] actionLinks = new HTML[]{reportLink, refreshLink, resetLink};
        HeaderLinkStackHolder.getInstance().addLinks(actionLinks);
    }

    /**
     * Enable refresh link uses CSS style class.
     */
    public static void enableRefreshLink(){
        refreshLink.addClickListener(refreshClickListener);
        refreshLink.setStylePrimaryName("ia-header-menu-link");
    }

    /**
     * Disable refresh link uses CSS style class.
     */
    public static void disableRefreshLink(){
        refreshLink.removeClickListener(refreshClickListener);
        refreshLink.setStylePrimaryName("ia-header-menu-link-disabled");    
    }

    
    /**
     * Disable report link uses CSS style class.
     */
    public static void disableReportLink(){
        reportLink.removeClickListener(reportClickListener);
        reportLink.setStylePrimaryName("ia-header-menu-link-disabled");
    }

    /**
     * Enable report link uses CSS style class.
     */
    public static void enableReportLink(){
        reportLink.addClickListener(reportClickListener);
        reportLink.setStylePrimaryName("ia-header-menu-link");
    }
    
    /**
     * Disable reset link.
     */
    public static void disableResetLink(){
        resetLink.removeClickListener(resetClickListener);
        resetLink.setStylePrimaryName("ia-header-menu-link-disabled");
    }

    /**
     * Enable reset link.
     */
    public static void enableResetLink(){
        resetLink.addClickListener(resetClickListener);
        resetLink.setStylePrimaryName("ia-header-menu-link");
    }

    /**
     * Report header link click listener class.
     * @author Andrey Zavgorodniy
     */
    private static class ReportHeaderLinkClickListener implements ClickListener {
        public void onClick(Widget arg0) {
            SIReportWizard dlg = SIReportWizard.getInstance();
            dlg.setStylePrimaryName("popup");
            dlg.setPixelSize(500, 250);
            dlg.center();
            dlg.showWizard();
        }
    }

    /**
     * Refresh header link click listener class.
     * @author Andrey Zavgorodniy
     */
    private static class RefreshHeaderLinkClickListener implements ClickListener {

        public void onClick(Widget sender) {
            this.refreshAction();
        }

        /**
         * Asynchronous refresh application state action.
         */
        private void refreshAction() {
            BrowserUtils.reloadPage();
            /*UserSessionBean.clearCache();
            Engine.getClient().getApplicationConfigTO(new InfraredCallback() {
                public void doSuccess(Object result) {
                    ApplicationConfigTO applConfigTo = (ApplicationConfigTO) result;
                    ApplicationConfigTO sessionConfig = UserSessionBean.getApplConfigBeanInstance();
                    if (sessionConfig != null && applConfigTo != null) {
                        /*//* Get configuration info from session bean *//*
                        ApplicationEntryTO[] sessionStoredAppl = UserSessionBean
                                        .getApplConfigBeanInstance().getApplicationList();
                        ApplicationEntryTO[] serverStoredAppl = applConfigTo.getApplicationList();

                        /*//* Merge configuration process *//*
                        ApplicationEntryTO[] configAfterMerging = SIRConfigurationManager
                                        .getInstance().clientVsServerConfigComparator(sessionStoredAppl, serverStoredAppl);
                        sessionConfig.setApplicationList(configAfterMerging);

                        if (configAfterMerging != null && configAfterMerging.length > 0) {
                            /*//* Recreate header tool bar panel *//*
                            HeaderToolBarBuilder toolbarBuilder = HeaderToolBarBuilder.getInstance(sessionConfig);
                            HeaderPanel.getInstance().showSelectedApplInfo(toolbarBuilder.getSelectedApplLabelText());
                            StackPanelHolder.getInstance().fillStack(sessionConfig);
                        } else {
                            /*//* Initial state *//*
                            HeaderPanel.getInstance().hideSelectedApplInfo();
                            ApplicationViewFacade.cleanAll();
                        }
                    }
                }
            });
          Engine.getClient().refreshData(new InfraredCallback(){
            public void doSuccess(Object arg0) {
            }
          });*/
        }
    }
    
    private static class ResetHeaderLinkClickListener implements ClickListener{

        /**
         * on click.
         * 
         * @param sender the sender
         */
        public void onClick(Widget sender) {
            Engine.getClient().reset(new AsyncCallback(){

                public void onFailure(Throwable caught) {
                    
                }

                public void onSuccess(Object result) {
                    refreshClickListener.onClick(null);
                }
                
            });
        }
        
    }

}
