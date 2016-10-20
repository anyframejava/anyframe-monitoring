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
import net.sf.infrared2.gwt.client.to.*;
import net.sf.infrared2.gwt.client.to.application.ApplicationViewTO;
import net.sf.infrared2.gwt.client.to.other.DetailOtherViewTO;
import net.sf.infrared2.gwt.client.to.other.OtherViewTO;
import net.sf.infrared2.gwt.client.to.other.TraceTreeNodeTO;
import net.sf.infrared2.gwt.client.to.report.ReportResultTO;
import net.sf.infrared2.gwt.client.to.sql.SqlViewTO;
import net.sf.infrared2.gwt.client.to.status.ResultStatusTO;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * <b>ISIRService</b><p>
 * Main service interface of application to make asynchronous calls.
 * 
 * @author Sergey Evluhin
 */
public interface ISIRService extends RemoteService {

    /**
     * Method init initialize state of client side.
     * @return ResultStatusTO
     */
    public InitialStateTO initClientState();

    /**
     * Clean the session data.
     * @return ResultStatusTO
     */
    public ResultStatusTO refreshData();

    /**
     * Returns entries (applications, layers, etc) for navigator panel.
     * 
     * @param config - transfer object, that contains selected applications and
     *            settings for its presentation.
     * @return data for navigator panel.
     */
    public NavigatorTO getNavigator(ApplicationConfigTO config);

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
    public ApplicationViewTO getApplicationData(ApplicationConfigTO config,
                    NavigatorEntryTO navigatorEntryTO, String otherOperationsName);

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
    public OtherViewTO getOtherViewData(ApplicationConfigTO config,
                    NavigatorEntryTO navigatorEntryTO, String otherOperationsName);

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
    public DetailOtherViewTO getDetailsOtherViewData(ApplicationConfigTO config,
                    NavigatorEntryTO navigatorEntryTO, TraceTreeNodeTO node);

    /**
     * Get information about available applications data.
     * 
     * @return application configuration transfer object.
     */
    public ApplicationConfigTO getApplicationConfigTO();

    /**
     * Returns the data associated with SQL view.
     * 
     * @param config - transfer object, that contains selected applications and
     *            settings for its presentation.
     * @param navigatorEntryTO - transfer object associated with link in
     *            navigator panel.
     * @return transfer object for SQL view.
     */
    public SqlViewTO getSqlViewData(ApplicationConfigTO config, NavigatorEntryTO navigatorEntryTO);

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
    public ReportResultTO generateReport(ApplicationConfigTO applConfig,
                    ReportConfigTO reportConfig, String otherOperationsName);

    /**
     * Returns the version of this application.
     */
    public ApplicationVersionTO getAppVersion();
    
    /**
     * Gets the refresh interval millis.
     * 
     * @return the refresh interval millis
     */
    public InitialStateTO getRefreshIntervalMillis();
    
    /**
     * Reset.
     */
    public void reset();
    
    /**
     * Sets the reset cache interval.
     * 
     * @param resetIntervalMin the reset interval min
     */
    public void setResetCacheIntervalMin(long resetIntervalMin);
    
    /**
     * Gets the reset cache interval min.
     * 
     * @return the reset cache interval min
     */
    public Long getResetCacheIntervalMin();
}
