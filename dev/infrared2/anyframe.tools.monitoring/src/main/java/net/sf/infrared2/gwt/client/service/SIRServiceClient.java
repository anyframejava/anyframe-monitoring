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
package net.sf.infrared2.gwt.client.service;

import net.sf.infrared2.gwt.client.i18n.ApplicationMessages;
import net.sf.infrared2.gwt.client.report.ReportConfigTO;
import net.sf.infrared2.gwt.client.to.ApplicationConfigTO;
import net.sf.infrared2.gwt.client.to.NavigatorEntryTO;
import net.sf.infrared2.gwt.client.to.application.ApplicationViewTO;
import net.sf.infrared2.gwt.client.to.other.DetailOtherViewTO;
import net.sf.infrared2.gwt.client.to.other.OtherViewTO;
import net.sf.infrared2.gwt.client.to.other.TraceTreeNodeTO;
import net.sf.infrared2.gwt.client.to.sql.SqlViewTO;
import net.sf.infrared2.gwt.client.users.session.UserSessionBean;
import net.sf.infrared2.gwt.client.view.facade.ApplicationViewFacade;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * <b>SIRServiceClient</b><p>
 * SIRService Client. Simplify works with RPC SIRService and represent its
 * wrapper.
 *
 * @author Birukov Sergey
 * @author Sergey Evluhin
 */
public class SIRServiceClient {

    /** Field isCacheEnabled  */
    private static boolean isCacheEnabled;

    /** Field service  */
    private ISIRServiceAsync service;

    /**
     * Default constructor.
     */
    public SIRServiceClient() {
        super();
        service = (ISIRServiceAsync) GWT.create(ISIRService.class);
        ServiceDefTarget endpoint = (ServiceDefTarget) service;
        String moduleRelativeURL = GWT.getModuleBaseURL() + "ms";
        endpoint.setServiceEntryPoint(moduleRelativeURL);
    }

    /**
     * Returns entries (applications, layers, etc) for navigator panel.
     *
     * @param config - transfer object, that contains selected applications and
     *            settings for its presentation.
     * @param callback - is the object that receive results of any call to
     *            server.
     */
    public void getNavigator(ApplicationConfigTO config, AsyncCallback callback) {
        service.getNavigator(config, callback);
    }

    /**
     * Returns the data for an Application views.
     *
     * @param config - transfer object, that contains selected applications and
     *            settings for its presentation.
     * @param navigatorEntryTO - transfer object associated with link in
     *            navigator panel.
     * @param otherOperationsName - graph title for small united operations.
     * @param callback - is the object that receive results of any call to
     *            server.
     */
    public void getApplicationData(ApplicationConfigTO config, NavigatorEntryTO navigatorEntryTO, String otherOperationsName, AsyncCallback callback) {
        ApplicationViewFacade.maskAll(ApplicationMessages.MESSAGES.loadingMask());
        if (isCacheEnabled && !UserSessionBean.isSessionExpired()) {
            ApplicationViewTO to = (ApplicationViewTO) UserSessionBean.getCache().get(navigatorEntryTO);
            if (to != null) {
                callback.onSuccess(to);
                return;
            }
        }
        service.getApplicationData(config, navigatorEntryTO, otherOperationsName, callback);
    }

    /**
     * Returns the data for an Application views.
     *
     * @param config - transfer object, that contains selected applications and
     *            settings for its presentation.
     * @param navigatorEntryTO - transfer object associated with link in
     *            navigator panel.
     * @param otherOperationsName - graph title for small united operations.
     * @param callback - is the object that receive results of any call to
     *            server.
     */
    public void getOtherViewData(ApplicationConfigTO config, NavigatorEntryTO navigatorEntryTO, String otherOperationsName, AsyncCallback callback) {
        ApplicationViewFacade.maskAll(ApplicationMessages.MESSAGES.loadingMask());
        if (isCacheEnabled && !UserSessionBean.isSessionExpired()) {
            Object result = UserSessionBean.getCache().get(navigatorEntryTO);
            OtherViewTO to = null;
            if (result != null)
                to = (OtherViewTO) result;
            if (to != null) {
                callback.onSuccess(to);
                return;
            }
        }
        service.getOtherViewData(config, navigatorEntryTO, otherOperationsName, callback);
    }

    /**
     * Returns the data associated with node in trace tree.
     *
     * @param appTo - transfer object, that contains selected applications and
     *            settings for its presentation.
     * @param navTo - transfer object associated with link in
     *            navigator panel.
     * @param node - selected node of the trace tree.
     * @param callback - is the object that receive results of any call to
     *            server.
     */
    public void getDetailsOtherViewData(ApplicationConfigTO appTo, NavigatorEntryTO navTo, TraceTreeNodeTO node, AsyncCallback callback) {
        if (isCacheEnabled && !UserSessionBean.isSessionExpired()) {
            NavigatorEntryTO cloneNavigatorTO = navTo.clone();
            cloneNavigatorTO.setOperationNode(node);
            Object result = UserSessionBean.getCache().get(cloneNavigatorTO);
            DetailOtherViewTO to = null;
            if (result != null)
                to = (DetailOtherViewTO) result;
            if (to != null) {
                callback.onSuccess(to);
                return;
            }
        }
        service.getDetailsOtherViewData(appTo, navTo, node, callback);
    }

    /**
     * Returns the data associated with SQL view.
     *
     * @param config - transfer object, that contains selected applications and
     *            settings for its presentation.
     * @param navigatorEntryTO - transfer object associated with link in
     *            navigator panel.
     * @param callback - is the object that receive results of any call to
     *            server.
     */
    public void getSqlViewData(ApplicationConfigTO config, NavigatorEntryTO navigatorEntryTO, AsyncCallback callback) {
        ApplicationViewFacade.maskAll(ApplicationMessages.MESSAGES.loadingMask());
        if (isCacheEnabled && !UserSessionBean.isSessionExpired()) {
            SqlViewTO to = (SqlViewTO) UserSessionBean.getCache().get(navigatorEntryTO);
            if (to != null) {
                callback.onSuccess(to);
                return;
            }
        }
        service.getSqlViewData(config, navigatorEntryTO, callback);
    }

    /**
     * Get information about available applications data.
     *
     * @param callback - is the object that receive results of any call to
     *            server.
     */
    public void getApplicationConfigTO(AsyncCallback callback) {
        service.getApplicationConfigTO(callback);
    }

    /**
     * Method init initialize state of client side.
     *
     */
    public void initClientState(AsyncCallback async) {
        service.initClientState(async);
    }

    /**
     * Clean the session data.
     *
     * @param callback - is the object that receive results of any call to
     *            server.
     */
    public void refreshData(AsyncCallback callback) {
        UserSessionBean.getCache().clear();
        service.refreshData(callback);
    }

    /**
     * Generate report on the server side of application and returns transfer
     * object that contains information for getting this report from server.
     *
     * @param applConfig - transfer object, that contains selected applications
     *            and settings for its presentation.
     * @param reportConfig - configuration object that contains selected options
     *            for the generating report.
     * @param otherOperationsName - graph title for the small united operations. *
     * @param callback - is the object that receive results of any call to
     *            server.
     */
    public void generateReport(ApplicationConfigTO applConfig, ReportConfigTO reportConfig, String otherOperationsName, AsyncCallback callback) {
        service.generateReport(applConfig, reportConfig, otherOperationsName, callback);
    }

    /**
     * Gets the refresh interval millis.
     * 
     * @return the refresh interval millis
     */
    public void getRefreshIntervalMillis(AsyncCallback callback) {
        service.getRefreshIntervalMillis(callback);
    }

    /**
     * Returns the version of this application. *
     *
     * @param callback - is the object that receive results of any call to
     *            server.
     */
    public void getAppVersion(AsyncCallback callback) {
        service.getAppVersion(callback);
    }

    /**
     * Reset.
     * 
     * @param callback the callback
     */
    public void reset(AsyncCallback callback) {
        service.reset(callback);
    }
    
    /**
     * Sets the reset cache interval.
     * 
     * @param resetIntervalMin the reset interval min
     * @param callback the callback
     */
    public void setResetCacheIntervalMin(long resetIntervalMin, AsyncCallback callback){
        service.setResetCacheIntervalMin(resetIntervalMin, callback);
    }
    
    /**
     * Gets the reset cache interval min.
     * 
     * @return the reset cache interval min
     */
    public void getResetCacheIntervalMin(AsyncCallback callback){
        ApplicationViewFacade.maskAll(ApplicationMessages.MESSAGES.loadingMask());
        service.getResetCacheIntervalMin(callback);
    }

    /**
     * Sets the cache enabled.
     * 
     * @param cacheEnabled the new cache enabled
     */
    public static void setCacheEnabled(boolean cacheEnabled) {
        isCacheEnabled = cacheEnabled;
    }
}
