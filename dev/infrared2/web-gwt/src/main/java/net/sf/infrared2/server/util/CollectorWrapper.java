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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import net.sf.infrared.base.model.AggregateExecutionTime;
import net.sf.infrared.base.util.TreeNode;
import net.sf.infrared2.gwt.client.Constants;
import net.sf.infrared2.gwt.client.report.ReportConfigTO;
import net.sf.infrared2.gwt.client.to.ApplicationConfigTO;
import net.sf.infrared2.gwt.client.to.ApplicationEntryTO;
import net.sf.infrared2.gwt.client.to.NavigatorEntryTO;
import net.sf.infrared2.gwt.client.to.NavigatorTO;
import net.sf.infrared2.gwt.client.to.application.ApplicationViewTO;
import net.sf.infrared2.gwt.client.to.other.DetailOtherViewTO;
import net.sf.infrared2.gwt.client.to.other.OtherViewTO;
import net.sf.infrared2.gwt.client.to.other.SummaryRowTO;
import net.sf.infrared2.gwt.client.to.other.TraceTreeNodeTO;
import net.sf.infrared2.gwt.client.to.sql.SqlViewTO;
import net.sf.infrared2.server.Constant;
import net.sf.infrared2.server.adapter.Adapter;
import net.sf.infrared2.server.report.engine.ReportEngine;
import net.sf.infrared2.server.report.model.ReportSourceContentTO;
import net.sf.infrared2.server.report.model.ReportTO;
import net.sf.infrared2.server.util.sql.SQLToHtml;

/**
 * <b>CollectorWrapper</b><p>
 * This class provide a wripper for collector to bridge collector with web-application
 *
 * @author gzgonikov
 */
public class CollectorWrapper {


    /**
     * Method getOtherViewData returns TO for other layer.
     *
     * @param navigatorEntryTO of type NavigatorEntryTO
     * @param perfomanceDataMap of type Map<String, PerformanceDataSnapshot>
     * @param sessionId of type String
     * @param contextPath of type String
     * @param otherOperationsName of type String
     * @return OtherViewTO
     */
    public static OtherViewTO getOtherViewData(NavigatorEntryTO navigatorEntryTO, Map<String, PerformanceDataSnapshot> perfomanceDataMap,
                                               String sessionId, String contextPath, String otherOperationsName) {

        PerformanceDataSnapshot perfomanceData = getDataSnapshotFromMap(navigatorEntryTO, perfomanceDataMap);

        if (Constants.MODULE_LAST_INV.equals(navigatorEntryTO.getModuleType())){
            return getOtherViewDataLastInvocations(navigatorEntryTO, perfomanceData);
        }

        OtherViewTO otherViewTO = new OtherViewTO();
        otherViewTO.setInclusiveTableRows(Adapter.getSummaryRowTOArray(
                Adapter.getAggregateExecutionTimeArray(navigatorEntryTO.getTitle(),
                        perfomanceData, navigatorEntryTO.getModuleType()),
                true)
        );

        otherViewTO.setExclusiveTableRows(Adapter.getSummaryRowTOArray(
                Adapter.getAggregateExecutionTimeArray(navigatorEntryTO.getTitle(),
                        perfomanceData, navigatorEntryTO.getModuleType()),
                false)
        );

        TreeNode mergedHead;
        TraceTreeNodeTO traceTreeNodeTO = new TraceTreeNodeTO();
        traceTreeNodeTO.setChildren(new TraceTreeNodeTO[otherViewTO.getInclusiveTableRows().length]);
        for (int i = 0; i < otherViewTO.getInclusiveTableRows().length; i++) {
            int lastIndexOfPoint = navigatorEntryTO.getTitle().lastIndexOf(".");
            String actualLayer = (lastIndexOfPoint == -1) ? navigatorEntryTO.getTitle() : navigatorEntryTO.getTitle().substring(lastIndexOfPoint + 1);
            SummaryRowTO summaryRow = otherViewTO.getInclusiveTableRows()[i];
            mergedHead = TreeUtil.getMergedExecutionContextTreeNode
                    (
                            perfomanceData.getStats().getTree(),
                            summaryRow.getOperationName(),
                            actualLayer,
                            summaryRow.getContextClass(),
                            navigatorEntryTO.getModuleType().equals(Constant.MODULE_HIERARCHICAL) ?
                                    navigatorEntryTO.getTitle() : null
                    );
            traceTreeNodeTO.getChildren()[i] = Adapter.getTraceTreeNodeTO(mergedHead, summaryRow);
        }
        otherViewTO.setTrace(traceTreeNodeTO);

        GraphicUtil.getOtherViewDataImages(otherViewTO, sessionId, contextPath, otherOperationsName);

        return otherViewTO;
    }

    /**
     * Method getDataSnapshotFromMap returns data snapshot.
     *
     * @param navigatorEntryTO of type NavigatorEntryTO
     * @param perfomanceDataMap of type Map<String, PerformanceDataSnapshot>
     * @return PerformanceDataSnapshot
     */
    private static PerformanceDataSnapshot getDataSnapshotFromMap(NavigatorEntryTO navigatorEntryTO,
                                                                  Map<String, PerformanceDataSnapshot> perfomanceDataMap) {
        PerformanceDataSnapshot perfomanceData = null;
        if (navigatorEntryTO.getApplications()!=null){
            perfomanceData =  perfomanceDataMap.get(
                    Adapter.getDataSnapshotApplicationKey(
                            new ApplicationEntryTO[]{navigatorEntryTO.getApplications()}));
        }
        else {
            Set keys = perfomanceDataMap.keySet();
            perfomanceData = perfomanceDataMap.get(keys.iterator().next());
        }
        return perfomanceData;
    }

    /**
     * Method getOtherViewDataLastInvocations returns TO for last invocation layer.
     *
     * @param navigatorEntryTO of type NavigatorEntryTO
     * @param perfomanceData of type PerformanceDataSnapshot
     * @return OtherViewTO
     */
    private static OtherViewTO getOtherViewDataLastInvocations(NavigatorEntryTO navigatorEntryTO,
                                                               PerformanceDataSnapshot perfomanceData) {
        OtherViewTO otherViewTO = new OtherViewTO();
        TraceTreeNodeTO traceTreeNodeTO = new TraceTreeNodeTO();
        traceTreeNodeTO.setChildren(new TraceTreeNodeTO[]{new TraceTreeNodeTO()});
        otherViewTO.setTrace(traceTreeNodeTO);
        otherViewTO.getTrace().getChildren()[0]=Adapter.getTraceForLastInvocations(navigatorEntryTO, perfomanceData);        
        return otherViewTO;
    }

    /**
     * Method getSqlViewData returns TO for SQL layer.
     *
     * @param navigatorEntryTO of type NavigatorEntryTO
     * @param perfomanceDataMap of type Map<String, PerformanceDataSnapshot>
     * @param sessionId of type String
     * @param contextPath of type String
     * @return SqlViewTO
     */
    public static SqlViewTO getSqlViewData(NavigatorEntryTO navigatorEntryTO, Map<String,
            PerformanceDataSnapshot> perfomanceDataMap, String sessionId, String contextPath) {

        PerformanceDataSnapshot perfomanceData = getDataSnapshotFromMap(navigatorEntryTO, perfomanceDataMap);

        SqlStatistics[] sqlStats = perfomanceData.getSqlStatisticsForLayer(navigatorEntryTO.getTitle(),
                navigatorEntryTO.getModuleType());

        SqlStatistics[] topNQueriesByExecution = ViewUtil.getTopNQueriesByExecutionTime(sqlStats,
                WebConfig.getNumOfSqlQueries());

        SqlStatistics[] topNQueriesByCount = ViewUtil.getTopNQueriesByCount(sqlStats, WebConfig.getNumOfSqlQueries());

        SqlViewTO sqlViewTO = Adapter.getSqlViewTO(perfomanceData.getSqlDetailsForLayer(
                navigatorEntryTO.getTitle(), navigatorEntryTO.getModuleType()),
                topNQueriesByExecution, topNQueriesByCount, sqlStats);

        Map formattedSqlMap = new HashMap();
        for (int i = 0; i < sqlViewTO.getAllSqlQueries().length; i++) {
            formattedSqlMap.put(sqlViewTO.getAllSqlQueries()[i].getId(),
                    SQLToHtml.convertToHtml(sqlViewTO.getAllSqlQueries()[i].getName()));
        }
        sqlViewTO.setFormattedSql(formattedSqlMap);

        GraphicUtil.getSqlViewDataImages(sqlViewTO, sessionId, contextPath);

        return sqlViewTO;
    }

    /**
     * Method getDetailsOtherViewData returns TO for details tabs.
     *
     * @param navTo of type NavigatorEntryTO
     * @param node of type TraceTreeNodeTO
     * @param perfomanceDataMap of type Map<String, PerformanceDataSnapshot>
     * @return DetailOtherViewTO
     */
    public static DetailOtherViewTO getDetailsOtherViewData(NavigatorEntryTO navTo,
                                                            TraceTreeNodeTO node,
                                                            Map<String, PerformanceDataSnapshot> perfomanceDataMap) {

        PerformanceDataSnapshot perfomanceData = getDataSnapshotFromMap(navTo, perfomanceDataMap);

        TreeNode mergedHead;
        mergedHead = Adapter.getTreeNode(perfomanceData, node, navTo);    
        AggregateExecutionTime[] mergedTreeJdbcSummaries = ViewUtil.getJDBCSummary(mergedHead);
        SqlStatistics[] mergedTreeSqlStatistics = ViewUtil.getSqlStatistics(mergedHead);
        SqlStatistics[] topNQueriesByExecution = ViewUtil.getTopNQueriesByExecutionTime(mergedTreeSqlStatistics,
                WebConfig.getNumOfSqlQueries());
        SqlStatistics[] topNQueriesByCount = ViewUtil.getTopNQueriesByCount(mergedTreeSqlStatistics,
                WebConfig.getNumOfSqlQueries());
        DetailOtherViewTO detailOtherViewTO = Adapter.getDetailOtherViewTO(mergedTreeJdbcSummaries,
                topNQueriesByExecution, topNQueriesByCount);
        if (node.getLayerName() != null && node.getLayerName().endsWith(Constants.TITLE_SQL)) {
            if (detailOtherViewTO == null) detailOtherViewTO = new DetailOtherViewTO();
            detailOtherViewTO.setFormattedSql(SQLToHtml.convertToHtml(node.getText()));
        }
        return detailOtherViewTO;
    }

    /**
     * Method getApplicationData returns TO for application layer.
     *
     * @param perfomanceDataMap of type Map<String, PerformanceDataSnapshot>
     * @param navigatorEntryTO of type NavigatorEntryTO
     * @param sessionId of type String
     * @param contextPath of type String
     * @param otherOperationsName of type String
     * @return ApplicationViewTO
     */
    public static ApplicationViewTO getApplicationData(Map<String, PerformanceDataSnapshot> perfomanceDataMap,
                                                       NavigatorEntryTO navigatorEntryTO,
                                                       String sessionId, String contextPath, String otherOperationsName) {
        PerformanceDataSnapshot perfomanceData = getDataSnapshotFromMap(navigatorEntryTO, perfomanceDataMap);
        ApplicationViewTO applViewTO = Adapter.getApplicationViewTO(perfomanceData, navigatorEntryTO);
        applViewTO.setModuleType(navigatorEntryTO.getModuleType());
        GraphicUtil.getApplicationDataImages(applViewTO, sessionId, contextPath, navigatorEntryTO, otherOperationsName);
        return applViewTO;
    }

    /**
     * Method getNavigator returns navigator links
     *
     * @param perfomanceData of type Map<String, PerformanceDataSnapshot>
     * @param config of type ApplicationConfigTO
     * @return NavigatorTO
     */
    public static NavigatorTO getNavigator(Map<String, PerformanceDataSnapshot> perfomanceData, ApplicationConfigTO config, boolean setLabels) {
        if (setLabels) GraphicUtil.setGraphLabelEnabled(config.isGraphLabelEnabled());
        return Adapter.getNavigatorTO(perfomanceData, Constant.TITLE_SEQUENCE, config);
    }

    /**
     * Method getApplicationConfigTO forms application configuration.
     *
     * @param session of type HttpSession
     * @return ApplicationConfigTO
     */
    public static ApplicationConfigTO getApplicationConfigTO(HttpSession session) {
        Set<String> applNames = DataFetchUtil.getApplicationNames();
        if (applNames == null || applNames.size() == 0) return null;
        ApplicationEntryTO[] applicationEntryTOArray = new ApplicationEntryTO[applNames.size()];
        ApplicationEntryTO applicationEntryTO;
        int i = 0;
        for (String applName : applNames) {
            applicationEntryTO = Adapter.getApplicationEntryTO(applName);
            applicationEntryTO.setInstanceList(Adapter.getInstanceEntryTOArray(applName));
            applicationEntryTOArray[i++] = applicationEntryTO;
        }
        ApplicationConfigTO toReturn = Adapter.getApplicationConfigTO(applicationEntryTOArray);
//        CollectorWrapper.removeEmptyApplications(toReturn, session);
        return toReturn;
    }

    /**
     * Method refreshData clear session attributes and caches.
     *
     * @param session of type HttpSession
     */
    public static void refreshData(HttpSession session) {
        if (session==null) return;
        session.removeAttribute(Constant.DATA_SNAPSHOT);
        CacheUtil.clearCaches(session);
        if (GraphicUtil.getSmartImageMap()!=null){
            GraphicUtil.getSmartImageMap().clear(session.getId());
        }
    }

    /**
     * Method removeEmptyApplications remove from config applications
     * info about was not collected yet.
     *
     * @param config of type ApplicationConfigTO
     * @param session of type HttpSession
     */
    public static void removeEmptyApplications(ApplicationConfigTO config, HttpSession session) {
        if (config==null) return;
        ApplicationConfigTO newTo = new ApplicationConfigTO();
        List<ApplicationEntryTO> resultApplications = new ArrayList<ApplicationEntryTO>();
        for (ApplicationEntryTO to : config.getApplicationList()){
            newTo.setApplicationList(new ApplicationEntryTO[]{to});
            NavigatorTO navTo = CollectorWrapper.getNavigator(DataFetchUtil.getPerfomanceData(newTo, session, false), newTo, false);
            boolean emptyNavTO = navTo==null || (navTo.getAbsolute()==null || navTo.getAbsolute().length==0);
            if (!emptyNavTO){
                resultApplications.add(to);
            }
        }
        config.setApplicationList(resultApplications.toArray(new ApplicationEntryTO[resultApplications.size()]));
    }

    /**
     * Method generateReport generate report.
     *
     * @param applConfig of type ApplicationConfigTO
     * @param reportConfig of type ReportConfigTO
     * @param otherOperationsName of type String
     * @param session of type HttpSession
     * @param contextPath of type String
     * @param protHostPortContext of type String
     * @param perfomanceData
     * @return ReportTO
     */
    public static ReportTO generateReport(ApplicationConfigTO applConfig, ReportConfigTO reportConfig,
                                          String otherOperationsName, HttpSession session,
                                          String contextPath, String protHostPortContext, Map<String,
            PerformanceDataSnapshot> perfomanceData){
        NavigatorTO navigator = CollectorWrapper.getNavigator(perfomanceData, applConfig, false);
        ReportEngine reportEngine = ReportEngine.getInstance();
        Map<NavigatorEntryTO, Serializable> sourceData = CacheUtil.getFullDataMap(
                session, navigator,
                perfomanceData,
                protHostPortContext, otherOperationsName);
        reportEngine.generateReport(reportConfig, new ReportSourceContentTO(sourceData, navigator, applConfig, reportConfig));
        reportConfig.setKeyInReportMap(CacheUtil.generateReportKey(contextPath,
                Constant.REPORT_SERVLET_PATTERN));
        return  reportEngine.getReportTO();
    }

}
