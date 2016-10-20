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
 * BeanComparator.java Date created: 22.01.2008
 * Last modified by: $Author: soyon.lim $
 * $Revision: 14286 $ $Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.server.util;


import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import net.sf.infrared2.gwt.client.Constants;
import net.sf.infrared2.gwt.client.report.ReportConfigTO;
import net.sf.infrared2.gwt.client.to.NavigatorEntryTO;
import net.sf.infrared2.gwt.client.to.NavigatorTO;
import net.sf.infrared2.gwt.client.to.application.ApplicationViewTO;
import net.sf.infrared2.gwt.client.to.other.OtherViewTO;
import net.sf.infrared2.gwt.client.to.sql.SqlViewTO;
import net.sf.infrared2.gwt.client.to.status.ResultStatus;
import net.sf.infrared2.server.Constant;
import net.sf.infrared2.server.report.model.ReportTO;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * <b>CacheUtil</b><p>
 * Class CacheUtil contains cache utility methods
 *
 * @author gzgonikov
 */
public class CacheUtil {

    /** Field isServerCasheEnabled  */
    private static boolean isServerCasheEnabled;

    /**
     * Method isServerCasheEnabled returns the serverCasheEnabled of this CacheUtil object.
     *
     * @return the serverCasheEnabled (type boolean) of this CacheUtil object.
     */
    public static boolean isServerCasheEnabled() {
        return isServerCasheEnabled;
    }

    /**
     * Method setServerCasheEnabled sets the serverCasheEnabled of this CacheUtil object.
     *
     * @param serverCasheEnabled the serverCasheEnabled of this CacheUtil object.
     *
     */
    public static void setServerCasheEnabled(boolean serverCasheEnabled) {
        isServerCasheEnabled = serverCasheEnabled;
    }

    /**
     * Method initCache initialize cache.
     * @param config of type ServletConfig
     */
    public static boolean isClientCacheEnabled(ServletConfig config) {
        ServletContext servletContext = config.getServletContext();
        String clientCacheEnable = servletContext.getInitParameter(Constant.CLIENT_CACHE_ENABLED);
        return (clientCacheEnable == null) || "true".equals(clientCacheEnable);
    }

    /**
     * Method getDataCache returns cache
     *
     * @param session of type HttpSession
     * @return Map<NavigatorEntryTO, Serializable>
     */
    public static Map<NavigatorEntryTO, Serializable> getDataCache(HttpSession session) {
        if (session==null) return null;
        Map<NavigatorEntryTO, Serializable> cache = null;
        if (session.getAttribute(Constant.DATA_CACHE)!=null && session.getAttribute(Constant.DATA_CACHE) instanceof Map)
            cache = (Map<NavigatorEntryTO, Serializable>) session.getAttribute(Constant.DATA_CACHE);
        if (cache==null) {
            cache = new HashMap<NavigatorEntryTO, Serializable>();
            session.setAttribute(Constant.DATA_CACHE, cache);
        }
        return cache;
    }

    /**
     * Method getFromDataCache returs oblect from data cache based on key.
     *
     * @param navigatorEntryTO of type NavigatorEntryTO
     * @param session of type HttpSession
     * @return Serializable
     */
    public static Serializable getFromDataCache(NavigatorEntryTO navigatorEntryTO, HttpSession session) {
        if (!isServerCasheEnabled) return null;
        Serializable result=null;
        Map<NavigatorEntryTO, Serializable> cache = CacheUtil.getDataCache(session);
        if (cache!=null){
            result =  cache.get(navigatorEntryTO);
        }
        return result;
    }

    /**
     * Method putToDataCache put object to data cache
     *
     * @param navigatorEntryTO of type NavigatorEntryTO
     * @param obj of type Serializable
     * @param session of type HttpSession
     */
    public static void putToDataCache(NavigatorEntryTO navigatorEntryTO, Serializable obj, HttpSession session) {
        if (isServerCasheEnabled){
            Map<NavigatorEntryTO, Serializable> cache = CacheUtil.getDataCache(session);
            cache.put(navigatorEntryTO, obj);
        }
    }

    /**
     * Method clearDataCache clear data cache.
     *
     * @param session of type HttpSession
     */
    public static void clearDataCache(HttpSession session) {
        session.removeAttribute(Constant.DATA_CACHE);
    }

    /**
     * Method getReportCache returs report cache.
     *
     * @param session of type HttpSession
     * @return Map<ReportConfigTO, ReportTO>
     */
    public static Map<ReportConfigTO, ReportTO> getReportCache(HttpSession session) {
        Map<ReportConfigTO, ReportTO> cache=null;
        if (session.getAttribute(Constant.REPORT_CACHE)!=null){
            cache = (Map<ReportConfigTO, ReportTO>) session.getAttribute(Constant.REPORT_CACHE);
        }
        if (cache==null) {
            cache = new HashMap<ReportConfigTO, ReportTO>();
            session.setAttribute(Constant.REPORT_CACHE, cache);
        }
        return cache;
    }

    /**
     * Method getFromReportCache returns report from cache.
     *
     * @param reportConfigTO of type ReportConfigTO
     * @param session of type HttpSession
     * @return ReportTO
     */
    public static ReportTO getFromReportCache(ReportConfigTO reportConfigTO, HttpSession session) {
        ReportTO result=null;
        Map<ReportConfigTO, ReportTO> cache = CacheUtil.getReportCache(session);
        if (cache!=null){
            result = cache.get(reportConfigTO);
        }
        return result;
    }

    /**
     * Method putToReportCache put report to cache
     *
     * @param reportConfigTO of type ReportConfigTO
     * @param report of type ReportTO
     * @param session of type HttpSession
     */
    public static void putToReportCache(ReportConfigTO reportConfigTO, ReportTO report, HttpSession session) {
        Map<ReportConfigTO, ReportTO> cache = CacheUtil.getReportCache(session);
        cache.put(reportConfigTO, report);
    }

    /**
     * Method clearReportCache clear report cache
     *
     * @param session of type HttpSession
     */
    public static void clearReportCache(HttpSession session) {
        session.removeAttribute(Constant.REPORT_CACHE);
    }

    /**
     * Method clearCaches clear all cache.
     *
     * @param session of type HttpSession
     */
    public static void clearCaches(HttpSession session) {
        clearReportCache(session);
        clearDataCache(session);
    }

    /**
     * Method generateReportKey generates report key.
     *
     * @param contextPath of type String
     * @param reportServletPattern of type String
     * @return String
     */
    public static String generateReportKey(String contextPath, String reportServletPattern) {
        final long currentTimeMillis = System.currentTimeMillis();
        long random = currentTimeMillis+new java.util.Random().nextLong();
        if (random<0) random = -1*random;
        return contextPath + reportServletPattern + Long.toString(random);
    }

    /**
     * Method getFullDataMap fill cache.
     *
     * @param session of type HttpSession
     * @param navigator of type NavigatorTO
     * @param perfomanceData of type Map<String, PerformanceDataSnapshot>
     * @param contextPath of type String
     * @param otherOperationsName of type String
     * @return Map<NavigatorEntryTO, Serializable>
     */
    public static Map<NavigatorEntryTO, Serializable> getFullDataMap(HttpSession session, NavigatorTO navigator,
                                                                     Map<String, PerformanceDataSnapshot> perfomanceData,
                                                                     String contextPath, String otherOperationsName) {
        Map<NavigatorEntryTO, Serializable> cache = CacheUtil.getDataCache(session);
        fillDataCache(cache, navigator, perfomanceData, contextPath, otherOperationsName, session.getId());
        return cloneCache((HashMap<NavigatorEntryTO, Serializable>) cache);
    }

    /**
     * Method cloneCache clone cache.
     *
     * @param cache of type HashMap<NavigatorEntryTO, Serializable>
     * @return Map<NavigatorEntryTO, Serializable>
     */
    private static Map<NavigatorEntryTO, Serializable> cloneCache(HashMap<NavigatorEntryTO, Serializable> cache) {
        return (Map<NavigatorEntryTO, Serializable>) cache.clone();
    }

    /**
     * Method fillDataCache fill cache.
     *
     * @param cache of type Map<NavigatorEntryTO, Serializable>
     * @param navigator of type NavigatorTO
     * @param perfomanceData of type Map<String, PerformanceDataSnapshot>
     * @param contextPath of type String
     * @param otherOperationsName of type String
     * @param sessionId of type String
     */
    private static void fillDataCache(Map<NavigatorEntryTO, Serializable> cache, NavigatorTO navigator,
                                      Map<String, PerformanceDataSnapshot> perfomanceData, String contextPath,
                                      String otherOperationsName, String sessionId) {
        fillAbstractModule(cache, navigator.getAbsolute(), perfomanceData, contextPath, otherOperationsName, sessionId);
        fillAbstractModule(cache, navigator.getHierarchModule(), perfomanceData, contextPath, otherOperationsName, sessionId);
        fillAbstractModule(cache, navigator.getLastInv(), perfomanceData, contextPath, otherOperationsName, sessionId);
    }

    /**
     * Method fillAbstractModule fill abstract module cache data.
     *
     * @param cache of type Map<NavigatorEntryTO, Serializable>
     * @param module of type NavigatorEntryTO[]
     * @param perfomanceData of type Map<String, PerformanceDataSnapshot>
     * @param contextPath of type String
     * @param otherOperationsName of type String
     * @param sessionId of type String
     */
    private static void fillAbstractModule(Map<NavigatorEntryTO, Serializable> cache, NavigatorEntryTO[] module,
                                           Map<String, PerformanceDataSnapshot> perfomanceData, String contextPath,
                                           String otherOperationsName, String sessionId) {
        if(module==null) return;
        for (NavigatorEntryTO navEntry : module){
            if (!cache.containsKey(navEntry)){
                cache.put(navEntry, getCorrespondingTO(navEntry, perfomanceData, contextPath,
                        otherOperationsName, sessionId));
            }
        }
    }

    /**
     * Method getCorrespondingTO returns corresponding TO from cache.
     *
     * @param navEntry of type NavigatorEntryTO
     * @param perfomanceData of type Map<String, PerformanceDataSnapshot>
     * @param contextPath of type String
     * @param otherOperationsName of type String
     * @param sessionId of type String
     * @return Serializable
     */
    private static Serializable getCorrespondingTO(NavigatorEntryTO navEntry, Map<String,
            PerformanceDataSnapshot> perfomanceData, String contextPath,
                                                     String otherOperationsName, String sessionId) {
        ResultStatus rs = new ResultStatus(Constants.SUCCESS_CODE);
        if (navEntry.isRoot()){
            ApplicationViewTO result = CollectorWrapper.getApplicationData(perfomanceData, navEntry,
                sessionId, contextPath, otherOperationsName);
            result.setNavigatorEntryTO(navEntry);
            result.setResultStatus(rs);
            return result;
        }
        else if (navEntry.getTitle().endsWith(Constants.TITLE_SQL)){
            SqlViewTO result = CollectorWrapper.getSqlViewData(navEntry, perfomanceData, sessionId, contextPath);
            result.setNavigatorEntryTO(navEntry);
            result.setResultStatus(rs);
            return result;
        }
        else {
            OtherViewTO result = CollectorWrapper.getOtherViewData(navEntry, perfomanceData,
                    sessionId, contextPath, otherOperationsName);
            result.setNavigatorEntryTO(navEntry);
            result.setResultStatus(rs);
            return result;
        }
    }

    /**
     * Method getReportConfigKey return key for report in cache.
     *
     * @param pathInfo of type String
     * @param session of type HttpSession
     * @return ReportConfigTO
     */
    public static ReportConfigTO getReportConfigKey(String pathInfo, HttpSession session) {
        Map<ReportConfigTO, ReportTO> reportCache = getReportCache(session);
        Set<ReportConfigTO> keys = reportCache.keySet();
        for (ReportConfigTO config : keys){
            if (config.getKeyInReportMap().equals(pathInfo)) return config;
        }
        return null;
    }
}
