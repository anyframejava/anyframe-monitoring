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
 * SIRServiceImpl.java Date created: 22.01.2008
 * Last modified by: $Author: soyon.lim $
 * $Revision: 14286 $ $Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.gwt.server.service;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import net.sf.infrared2.gwt.client.Constants;
import net.sf.infrared2.gwt.client.report.ReportConfigTO;
import net.sf.infrared2.gwt.client.service.ISIRService;
import net.sf.infrared2.gwt.client.to.ApplicationConfigTO;
import net.sf.infrared2.gwt.client.to.ApplicationVersionTO;
import net.sf.infrared2.gwt.client.to.InitialStateTO;
import net.sf.infrared2.gwt.client.to.NavigatorEntryTO;
import net.sf.infrared2.gwt.client.to.NavigatorTO;
import net.sf.infrared2.gwt.client.to.application.ApplicationViewTO;
import net.sf.infrared2.gwt.client.to.other.DetailOtherViewTO;
import net.sf.infrared2.gwt.client.to.other.OtherViewTO;
import net.sf.infrared2.gwt.client.to.other.TraceTreeNodeTO;
import net.sf.infrared2.gwt.client.to.report.ReportResultTO;
import net.sf.infrared2.gwt.client.to.sql.SqlViewTO;
import net.sf.infrared2.gwt.client.to.status.ResultStatus;
import net.sf.infrared2.gwt.client.to.status.ResultStatusTO;
import net.sf.infrared2.gwt.server.util.RPCServletUtils;
import net.sf.infrared2.gwt.server.util.VersionProvider;
import net.sf.infrared2.server.Constant;
import net.sf.infrared2.server.report.model.ReportTO;
import net.sf.infrared2.server.util.CacheUtil;
import net.sf.infrared2.server.util.CollectorWrapper;
import net.sf.infrared2.server.util.DataFetchUtil;
import net.sf.infrared2.server.util.ResetStatisticCacheJob;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * <b>SIRServiceImpl</b><p>
 * Async service implementation. <br>Provide all necessary data from infrared to Web-GUI module
 * @author Zgonikov Gleb
 */

public class SIRServiceImpl extends RemoteServiceServlet implements ISIRService {

    /** Field logger  */
    private static transient Log LOGGER = LogFactory.getLog(SIRServiceImpl.class);

    /** use serialVersionUID from JDK 1.0.2 for interoperability */
    private static final long serialVersionUID = -7616513167939832389L;

    /** The Constant MS_SEC. */
    private static final int MS_SEC = 1000;

    /** Present application version */
    private static String appVersion;
    /** True if session expired */
    private static boolean isSessionExpired;

    /**
     * Method isSessionExpired returns the sessionExpired of this SIRServiceImpl object.
     *
     * @return the sessionExpired (type boolean) of this SIRServiceImpl object.
     */
    public static boolean isSessionExpired() {
        return isSessionExpired;
    }

    /**
     * Method setSessionExpired sets the sessionExpired of this SIRServiceImpl object.
     *
     * @param sessionExpired the sessionExpired of this SIRServiceImpl object.
     *
     */
    public static void setSessionExpired(boolean sessionExpired) {
        isSessionExpired = sessionExpired;
    }

    /**
     * Standard servlet initiation expanded with initiation of image map.
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.appVersion = VersionProvider.getVersion(config);
    }

    /**
     * log exception message and send response with error message
     * @param e a thrown <code>Throwable</code
     */
    @Override
    protected void doUnexpectedFailure(Throwable e) {
        ServletContext servletContext = getServletContext();
        RPCServletUtils.writeResponseForFailure(servletContext, getThreadLocalResponse(), e);
    }

    /**
     * Method init initialize state of client side.
     * @return ResultStatusTO
     */
    public InitialStateTO initClientState() {
        LOGGER.debug("init Client State");
        refreshData();
        InitialStateTO toReturn = new InitialStateTO();
        toReturn.setCacheClientEnabled(CacheUtil.isClientCacheEnabled(getServletConfig()));
        toReturn.setSessionDuration(getThreadLocalRequest().getSession().getMaxInactiveInterval() - 180);
        toReturn.setResultStatus(new ResultStatus(Constants.SUCCESS_CODE));
        return toReturn;
    }

    /**
     * clean session data
     */
    public ResultStatusTO refreshData() {
        LOGGER.debug("refresh Data");
        CollectorWrapper.refreshData(getThreadLocalRequest().getSession());
        return new ResultStatusTO(new ResultStatus(Constants.SUCCESS_CODE));
    }

    /**
     * Get actual applications and instances list from collector
     * @return actual applications with instances
     */
    public ApplicationConfigTO getApplicationConfigTO() {
        getThreadLocalRequest().getSession().removeAttribute(Constant.DATA_SNAPSHOT);
        ApplicationConfigTO config = CollectorWrapper.getApplicationConfigTO(getThreadLocalRequest().getSession());
        if (config == null)
            config = new ApplicationConfigTO();
        config.setResetIntervalMin(getResetCacheIntervalMin());
        config.setResultStatus(new ResultStatus(Constants.SUCCESS_CODE));
        LOGGER.debug("get ApplicationConfigTO : " + config.toString());
        return config;
    }

    /**
     * @param config an application config from client
     * @return navigator links
     */
    public NavigatorTO getNavigator(ApplicationConfigTO config) {
        LOGGER.debug("get Navigator");
        //TODO for testing only; remove after first use
        //        try {
        //            SerializeHelper.serializeToFile(config, SerializeHelper.CONFIG_FILE);
        //        } catch (Exception e) {
        //            e.printStackTrace();
        //        }
        refreshData();
        NavigatorTO toReturn = CollectorWrapper.getNavigator(DataFetchUtil.getPerfomanceData(config, getThreadLocalRequest().getSession(), true),
                config, true);
        toReturn.setResultStatus(new ResultStatus(Constants.SUCCESS_CODE));
        return toReturn;
    }

    /**
     * @param config an application config from client
     * @param navigatorEntryTO a <code>NavigatorEntryTO</code
     * @param otherOperationsName a <code>String</code,
     * name for operations that will be put into other group
     * @return data for application(s) layers
     */
    public ApplicationViewTO getApplicationData(ApplicationConfigTO config, NavigatorEntryTO navigatorEntryTO, String otherOperationsName) {
        LOGGER.debug("get Application Data");
        //getFailure();
        ApplicationViewTO toReturn = null;
        if (isSessionExpired) {
            toReturn = new ApplicationViewTO();
            toReturn.setResultStatus(new ResultStatus(Constants.SESSION_EXPIRED_CODE));
            isSessionExpired = false;
            return toReturn;
        }
        toReturn = (ApplicationViewTO) CacheUtil.getFromDataCache(navigatorEntryTO, getThreadLocalRequest().getSession());
        if (toReturn != null) {
            return toReturn;
        }

        toReturn = CollectorWrapper.getApplicationData(DataFetchUtil.getPerfomanceData(config, getThreadLocalRequest().getSession(), true),
                navigatorEntryTO, getThreadLocalRequest().getRequestedSessionId(), getProtHostPortContext(), otherOperationsName);
        toReturn.setNavigatorEntryTO(navigatorEntryTO);
        CacheUtil.putToDataCache(navigatorEntryTO, toReturn, getThreadLocalRequest().getSession());
        toReturn.setResultStatus(new ResultStatus(Constants.SUCCESS_CODE));
        return toReturn;
    }

    /**
    * @return String that is requested URL without pathInfo
    */
    protected String getProtHostPortContext() {
        StringBuffer protHostPortContext = getThreadLocalRequest().getRequestURL();
        int contextIndex = protHostPortContext.indexOf(getThreadLocalRequest().getContextPath());
        return protHostPortContext.substring(0, contextIndex + getThreadLocalRequest().getContextPath().length());
    }

    /**
     * @param config an application config from client
     * @param navigatorEntryTO a <code>NavigatorEntryTO</code
     * @param otherOperationsName a <code>String</code,
     * name for operations that will be put into other group
     * @return data for other layers
     */
    public OtherViewTO getOtherViewData(ApplicationConfigTO config, NavigatorEntryTO navigatorEntryTO, String otherOperationsName) {
        LOGGER.debug("get OtherView Data");
        OtherViewTO toReturn = null;
        if (isSessionExpired) {
            toReturn = new OtherViewTO();
            toReturn.setResultStatus(new ResultStatus(Constants.SESSION_EXPIRED_CODE));
            isSessionExpired = false;
            return toReturn;
        }
        toReturn = (OtherViewTO) CacheUtil.getFromDataCache(navigatorEntryTO, getThreadLocalRequest().getSession());
        if (toReturn != null) {
            return toReturn;
        }
        toReturn = CollectorWrapper.getOtherViewData(navigatorEntryTO, DataFetchUtil.getPerfomanceData(config, getThreadLocalRequest().getSession(),
                true), getThreadLocalRequest().getRequestedSessionId(), getProtHostPortContext(), otherOperationsName);
        toReturn.setNavigatorEntryTO(navigatorEntryTO);
        CacheUtil.putToDataCache(navigatorEntryTO, toReturn, getThreadLocalRequest().getSession());
        toReturn.setResultStatus(new ResultStatus(Constants.SUCCESS_CODE));
        return toReturn;
    }

    /**
     * @param config an application config from client
     * @param navigatorEntryTO a <code>NavigatorEntryTO</code
     * @return data for sql layers
     */
    public SqlViewTO getSqlViewData(ApplicationConfigTO config, NavigatorEntryTO navigatorEntryTO) {
        LOGGER.debug("get Sql View Data");
        SqlViewTO toReturn = null;
        if (isSessionExpired) {
            toReturn = new SqlViewTO();
            toReturn.setResultStatus(new ResultStatus(Constants.SESSION_EXPIRED_CODE));
            isSessionExpired = false;
            return toReturn;
        }
        toReturn = (SqlViewTO) CacheUtil.getFromDataCache(navigatorEntryTO, getThreadLocalRequest().getSession());
        if (toReturn != null) {
            return toReturn;
        }
        toReturn = CollectorWrapper.getSqlViewData(navigatorEntryTO, DataFetchUtil.getPerfomanceData(config, getThreadLocalRequest().getSession(),
                true), getThreadLocalRequest().getRequestedSessionId(), getProtHostPortContext());
        toReturn.setNavigatorEntryTO(navigatorEntryTO);
        CacheUtil.putToDataCache(navigatorEntryTO, toReturn, getThreadLocalRequest().getSession());
        toReturn.setResultStatus(new ResultStatus(Constants.SUCCESS_CODE));
        return toReturn;
    }

    /**
     * @param config an application config from client
     * @param navigatorEntryTO a <code>NavigatorEntryTO</code
     * @return data for other layers details
     */
    public DetailOtherViewTO getDetailsOtherViewData(ApplicationConfigTO config, NavigatorEntryTO navigatorEntryTO, TraceTreeNodeTO node) {
        LOGGER.debug("get Details Other View Data");
        DetailOtherViewTO toReturn = null;
        if (isSessionExpired) {
            toReturn = new DetailOtherViewTO();
            toReturn.setResultStatus(new ResultStatus(Constants.SESSION_EXPIRED_CODE));
            isSessionExpired = false;
            return toReturn;
        }
        navigatorEntryTO.setOperationNode(node);
        toReturn = (DetailOtherViewTO) CacheUtil.getFromDataCache(navigatorEntryTO, getThreadLocalRequest().getSession());
        if (toReturn != null) {
            return toReturn;
        }
        toReturn = CollectorWrapper.getDetailsOtherViewData(navigatorEntryTO, node, DataFetchUtil.getPerfomanceData(config, getThreadLocalRequest()
                .getSession(), true));
        if (toReturn != null) {
            toReturn.setNavigatorEntryTO(navigatorEntryTO);
            CacheUtil.putToDataCache(navigatorEntryTO, toReturn, getThreadLocalRequest().getSession());
        } else {
            toReturn = new DetailOtherViewTO();
        }
        toReturn.setResultStatus(new ResultStatus(Constants.SUCCESS_CODE));
        return toReturn;
    }

    /**
     * generates report on server
     * @param applConfig an application config from client
     * @param reportConfig a <code>ReportConfigTO</code
     * @param otherOperationsName a <code>String</code,
     * name for operations that will be put into other group
     * @return generated report
     *
     * @see net.sf.infrared2.gwt.client.service.ISIRService#generateReport(ApplicationConfigTO, ReportConfigTO, String)
     */
    public ReportResultTO generateReport(ApplicationConfigTO applConfig, ReportConfigTO reportConfig, String otherOperationsName) {
        LOGGER.debug("generate Report");
        if (isSessionExpired) {
            ReportResultTO toReturn = new ReportResultTO();
            toReturn.setResultStatus(new ResultStatus(Constants.SESSION_EXPIRED_CODE));
            isSessionExpired = false;
            return toReturn;
        }
        if (CacheUtil.getFromReportCache(reportConfig, getThreadLocalRequest().getSession()) != null) {
            for (ReportConfigTO cacheReportConfigTO : CacheUtil.getReportCache(getThreadLocalRequest().getSession()).keySet()) {
                if (cacheReportConfigTO.equals(reportConfig))
                    return new ReportResultTO(new ResultStatus(Constants.SUCCESS_CODE), cacheReportConfigTO.getKeyInReportMap());
            }
        }
        ReportTO reportTO = CollectorWrapper.generateReport(applConfig, reportConfig, otherOperationsName, getThreadLocalRequest().getSession(),
                getThreadLocalRequest().getContextPath(), getProtHostPortContext(), DataFetchUtil.getPerfomanceData(applConfig,
                        getThreadLocalRequest().getSession(), true));
        CacheUtil.clearReportCache(getThreadLocalRequest().getSession());
        CacheUtil.putToReportCache(reportConfig, reportTO, getThreadLocalRequest().getSession());
        return new ReportResultTO(new ResultStatus(Constants.SUCCESS_CODE), reportConfig.getKeyInReportMap());
    }

    /**
    * Having this method added to any of SIRService methods
    * anyone could test onFailure() behaviour on client-side
    */
    private void getFailure() {
        int[] array = new int[1];
        int x;
        for (int i = -1; i < 0; i++)
            x = array[i];
    }

    /**
     * @see net.sf.infrared2.gwt.client.service.ISIRService#getAppVersion()
     * @return the appVersion
     */
    public ApplicationVersionTO getAppVersion() {
        ApplicationVersionTO toReturn = new ApplicationVersionTO();
        toReturn.setVersion(appVersion);
        toReturn.setResultStatus(new ResultStatus(Constants.SUCCESS_CODE));
        return toReturn;
    }

    /**
     * Gets the refresh interval millis.
     * 
     * @return the refresh interval millis
     */
    public InitialStateTO getRefreshIntervalMillis() {
        InitialStateTO toReturn = new InitialStateTO();
        toReturn.setCacheClientEnabled(CacheUtil.isClientCacheEnabled(getServletConfig()));
        int sessionDuration = (getThreadLocalRequest().getSession().getMaxInactiveInterval() - 180) * MS_SEC;
        toReturn.setSessionDuration(sessionDuration);
        toReturn.setResultStatus(new ResultStatus(Constants.SUCCESS_CODE));
        return toReturn;
    }

    /**
     * @param version the appVersion to set
     */
    protected void setAppVersion(String version) {
        this.appVersion = version;
    }

    /**
     * Reset.
     */
    public void reset() {
        DataFetchUtil.getCollector().clearStats();
    }

    /**
     * Sets the reset cache interval.
     * 
     * @param resetIntervalMin the reset interval min
     */
    public void setResetCacheIntervalMin(long resetIntervalMin) {
        ResetStatisticCacheJob.setResetCacheInterval(resetIntervalMin * 60);
    }

    /**
     * Gets the reset cache interval min.
     * 
     * @return the reset cache interval min
     */
    public Long getResetCacheIntervalMin() {
        Long intervalSec = ResetStatisticCacheJob.getResetCacheInterval();
        return (intervalSec != null ? intervalSec / 60 : null);
    }

}
