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
package net.sf.infrared2.gwt.client;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Timer;
import com.gwtext.client.widgets.HTMLPanel;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.QuickTips;
import com.gwtext.client.widgets.Viewport;

import java.util.Date;

import net.sf.infrared2.gwt.client.callback.InfraredCallback;
import net.sf.infrared2.gwt.client.i18n.ApplicationMessages;
import net.sf.infrared2.gwt.client.service.SIRServiceClient;
import net.sf.infrared2.gwt.client.stack.StackPanelHolder;
import net.sf.infrared2.gwt.client.to.ApplicationConfigTO;
import net.sf.infrared2.gwt.client.to.ApplicationEntryTO;
import net.sf.infrared2.gwt.client.to.ApplicationVersionTO;
import net.sf.infrared2.gwt.client.to.InitialStateTO;
import net.sf.infrared2.gwt.client.users.session.UserSessionBean;
import net.sf.infrared2.gwt.client.utils.CookieUtils;
import net.sf.infrared2.gwt.client.utils.SIRConfigurationManager;
import net.sf.infrared2.gwt.client.utils.UIUtils;
import net.sf.infrared2.gwt.client.view.facade.ApplicationViewFacade;
import net.sf.infrared2.gwt.client.view.facade.HeaderActionLinksStack;
import net.sf.infrared2.gwt.client.view.header.HeaderPanel;
import net.sf.infrared2.gwt.client.view.header.HeaderToolBarBuilder;
 
/**
 * <b>Engine</b><p>
 * Engine class Constructs/initialize UI and data model, data providers and etc,
 * configure it all to works together and starts the application.
 * 
 * @author Birukov Sergey
 */
public class Engine {
    
    /** The Constant TIME_FOTMAT. */
    private static final  DateTimeFormat  TIME_FOTMAT = DateTimeFormat.getFormat("HH:mm");
    
    /** SIRService Client. Simplify works with RPC SIRService and represent its wrapper.*/
    private static SIRServiceClient client;
    /** Realize load configuration from cookie and merge data between server . */
    private static SIRConfigurationManager configManager;
    /** Singleton class that is responsible for reading and writing cookies
     has other useful helper util.*/
    private static CookieUtils cookieUtils;
    /** The place holder for footer information perform. */
    private static HTMLPanel footer;

    private static Viewport viewport;

    /** Field timer - each 10 second ask gc to cleanup resources */
    private static Timer timer = new Timer(){
        public void run() {
            System.gc();
        }
    };

    /**
     * Starting the client side of the application.
     */
    public static void start() {
        if (viewport!=null) viewport=null;
        timer.cancel();
        timer.scheduleRepeating(10*1000);
        UserSessionBean.clearCache();
        UserSessionBean.setSessionExpired(false);
        QuickTips.init();
        client = new SIRServiceClient();
        client.initClientState(new InfraredCallback(){
            public void doSuccess(Object o) {
                SIRServiceClient.setCacheEnabled(((InitialStateTO) o).isCacheClientEnabled());
                UserSessionBean.setSessionDuration(((InitialStateTO) o).getSessionDuration());
                loadConfiguration();
            }            
        });
        Panel layout = ApplicationViewFacade.getApplicationLayout();
        loadVersion(layout);
        viewport = new Viewport(layout);
    }

    /**
     * Method restart, reload configurations
     */
    public static void restart(){
        UserSessionBean.clearCache();
        loadConfiguration();
    }

    /**
     * Load version of the application.
     */
    private static void loadVersion(Panel layout) {
        footer = (HTMLPanel) layout.getComponent("footer");
        Engine.getClient().getAppVersion(new InfraredCallback() {
            public void doSuccess(Object result) {
              String version = ((ApplicationVersionTO) result).getVersion();
              version = "".equals(version) ? "" : "<br/> " + version;
              String footerHTML = ApplicationMessages.MESSAGES.footerContentLabel() + version;
              footer.setHtml(footerHTML);              
            }
          });
    }

    /**
     * It's added for singleton capability
     * only!
     * 
     * @return instance of SIRServiceClient. 
     */
    public static SIRServiceClient getClient() {
        return client;
    }

    /**
     * Load initial configuration.
     * 
     */
    private static void loadConfiguration() {
        Engine.getClient().getApplicationConfigTO(new InfraredCallback() {
            public void doSuccess(Object result) {
                /* Need load, merge and save in session bean */
                    ApplicationConfigTO applConfigTo = (ApplicationConfigTO) result;
                    if (applConfigTo.getApplicationList()!=null && applConfigTo.getApplicationList().length>0){
                        configManager = SIRConfigurationManager.getInstance();
                        cookieUtils = CookieUtils.getInstance();
                        Date currentDate = new Date();
                        String currentTime = TIME_FOTMAT.format(currentDate);
                        if (!cookieUtils.isCookieExists()) {
                            /* First login in application */
                            applConfigTo.setArchiveDate(false);
                            applConfigTo.setLiveDate(true);
                            applConfigTo.setDividedByApplications(true);
                            applConfigTo.setGraphLabelEnabled(false);
                            applConfigTo.setStartDate(UIUtils.getStringDateFormatter(currentDate));
                            applConfigTo.setStartTime(currentTime);
                            applConfigTo.setEndDate(UIUtils.getStringDateFormatter(currentDate));
                            applConfigTo.setEndTime(currentTime);
                            applConfigTo.setApplicationList(new ApplicationEntryTO[0]);
                            UserSessionBean.setConfigBean(applConfigTo);
                            UserSessionBean.setCookiesAvailable(false);
                            ApplicationViewFacade.cleanAll();
                            StackPanelHolder.getInstance().setDefault();
                        } else {
                            /* merge and update from cookies */
                            UserSessionBean.setCookiesAvailable(true);
                            ApplicationConfigTO loadFromCookie = configManager.loadFromCookie();
                            ApplicationEntryTO[] mergedConfig = configManager.clientVsServerConfigComparator(
                                            loadFromCookie.getApplicationList(), applConfigTo
                                                            .getApplicationList());
                            ApplicationConfigTO mergedConfigTO = new ApplicationConfigTO();
                            mergedConfigTO.setApplicationList(mergedConfig);
                            mergedConfigTO.setLiveDate(loadFromCookie.isLiveDate());
                            mergedConfigTO.setArchiveDate(loadFromCookie.isArchiveDate());
                            mergedConfigTO.setStartDate(loadFromCookie.getStartDate());
                            mergedConfigTO.setStartTime(loadFromCookie.getStartTime());
                            mergedConfigTO.setEndDate(loadFromCookie.getEndDate());
                            mergedConfigTO.setEndTime(loadFromCookie.getEndTime());
                            mergedConfigTO.setDividedByApplications(loadFromCookie
                                            .isDividedByApplications());
                            mergedConfigTO.setGraphLabelEnabled(loadFromCookie.isGraphLabelEnabled());
                            UserSessionBean.setConfigBean(mergedConfigTO);

                            HeaderToolBarBuilder toolbarBuilder = HeaderToolBarBuilder.getInstance(UserSessionBean.getApplConfigBeanInstance());
                            HeaderPanel.getInstance().showSelectedApplInfo(toolbarBuilder.getSelectedApplLabelText());
                            HeaderPanel.getInstance().doLayout();
                            StackPanelHolder.getInstance().fillStack(UserSessionBean.getApplConfigBeanInstance());
                        }
                }else {
                    ApplicationViewFacade.cleanAll();
                    StackPanelHolder.getInstance().setDefault();
                }
                HeaderActionLinksStack.init();
            }
        });
    }
}
