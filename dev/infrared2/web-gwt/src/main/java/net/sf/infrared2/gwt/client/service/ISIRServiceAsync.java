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

import net.sf.infrared2.gwt.client.report.ReportConfigTO;
import net.sf.infrared2.gwt.client.to.ApplicationConfigTO;
import net.sf.infrared2.gwt.client.to.NavigatorEntryTO;
import net.sf.infrared2.gwt.client.to.other.TraceTreeNodeTO;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * <b>ISIRService</b><p>
 * Main service interface of application to make asynchronous calls.
 * 
 * @author Sergey Evluhin
 */
public interface ISIRServiceAsync {

    /**
     * Method init initialize state of client side.
     * @return ResultStatusTO
     */
    public void initClientState(AsyncCallback callback);

    /**
     * Clean the session data.
     * @return ResultStatusTO
     */
    public void refreshData(AsyncCallback callback);

    /**
     * Returns entries (applications, layers, etc) for navigator panel.
     * 
     * @param config - transfer object, that contains selected applications and
     *            settings for its presentation.
     * @return data for navigator panel.
     */
    public void getNavigator(ApplicationConfigTO config, AsyncCallback callback);

    /**
     * Returns the data for an Application views.
     * 
     * @param config - transfer object, that contains selected applications and
     *            settings for its presentation.
     * @param navigatorEntryTO - transfer object associated with link in
     *            navigator panel.
     * @param otherOperationsName - graph title for small united operations.
     * @return transfer object for Application view.
     */
    public void getApplicationData(ApplicationConfigTO config,
                    NavigatorEntryTO navigatorEntryTO, String otherOperationsName, AsyncCallback callback);

    /**
     * Returns the data for an Other views.
     * 
     * @param config - transfer object, that contains selected applications and
     *            settings for its presentation.
     * @param navigatorEntryTO - transfer object associated with link in
     *            navigator panel.
     * @param otherOperationsName - graph title for small united operations.
     * @return transfer object for view different to Application and SQL.
     */
    public void getOtherViewData(ApplicationConfigTO config,
                    NavigatorEntryTO navigatorEntryTO, String otherOperationsName, AsyncCallback callback);

    /**
     * Returns the data associated with node in trace tree.
     * 
     * @param config - transfer object, that contains selected applications and
     *            settings for its presentation.
     * @param navigatorEntryTO - transfer object associated with link in
     *            navigator panel.
     * @param node - selected node of the trace tree.
     * @return transfer object that is the detailed statistic for the concrete
     *         trace element.
     */
    public void getDetailsOtherViewData(ApplicationConfigTO config,
                    NavigatorEntryTO navigatorEntryTO, TraceTreeNodeTO node, AsyncCallback callback);

    /**
     * Get information about available applications data.
     * 
     * @return application configuration transfer object.
     */
    public void getApplicationConfigTO(AsyncCallback callback);

    /**
     * Returns the data associated with SQL view.
     * 
     * @param config - transfer object, that contains selected applications and
     *            settings for its presentation.
     * @param navigatorEntryTO - transfer object associated with link in
     *            navigator panel.
     * @return transfer object for SQL view.
     */
    public void getSqlViewData(ApplicationConfigTO config, NavigatorEntryTO navigatorEntryTO, AsyncCallback callback);

    /**
     * Generate report on the server side of application and returns transfer
     * object that contains information for getting this report from server.
     * 
     * @param applConfig - transfer object, that contains selected applications
     *            and settings for its presentation.
     * @param reportConfig - configuration object that contains selected options
     *            for the generating report.
     * @param otherOperationsName - graph title for the small united operations.
     * @return transfer object that contains information for getting this report
     *         from server
     */
    public void generateReport(ApplicationConfigTO applConfig,
                    ReportConfigTO reportConfig, String otherOperationsName, AsyncCallback callback);

    /**
     * Returns the version of this application.
     */
    public void getAppVersion(AsyncCallback callback);
    
    /**
     * Gets the refresh interval millis.
     * 
     * @return the refresh interval millis
     */
    public void getRefreshIntervalMillis(AsyncCallback callback);
    
    /**
     * Reset.
     * 
     * @param callback the callback
     */
    public void reset(AsyncCallback callback);
    
    /**
     * Sets the reset cache interval.
     * 
     * @param resetIntervalMin the reset interval min
     * @param callback the callback
     */
    public void setResetCacheIntervalMin(long resetIntervalMin, AsyncCallback callback);
    
    /**
     * Gets the reset cache interval min.
     * 
     * @return the reset cache interval min
     */
    public void getResetCacheIntervalMin(AsyncCallback callback);

}
