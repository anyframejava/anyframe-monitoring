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
 * PropertyUtil.java Date created: 22.01.2008
 * Last modified by: $Author: soyon.lim $
 * $Revision: 14286 $ $Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.server.util;


import net.sf.infrared2.gwt.client.to.*;
import net.sf.infrared2.gwt.client.to.other.*;
import net.sf.infrared2.server.util.CollectorWrapperTest;
import net.sf.infrared2.server.util.SerializeHelper;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

import net.sf.infrared2.gwt.client.Constants;
import net.sf.infrared2.gwt.client.report.ReportConfigTO;
import net.sf.infrared2.gwt.client.to.application.ApplicationRowTO;
import net.sf.infrared2.gwt.client.to.application.ApplicationViewTO;
import net.sf.infrared2.gwt.client.to.sql.SqlRowTO;
import net.sf.infrared2.gwt.client.to.sql.SqlViewTO;
import net.sf.infrared2.server.Constant;
import net.sf.infrared2.server.chart.SmartImageMap;

import java.util.Enumeration;
import java.util.Map;

/**
 * CollectorWrapper Tester.
 *
 * @author Gleb Zonikov
 * @version 1.0
 * @since <pre>02/13/2008</pre>
 */
public class CollectorWrapperTest extends TestCase {

    /** Field reportConfig  */
    private static final ReportConfigTO reportConfig = new ReportConfigTO();
    /** Field sessionId  */
    private final static String sessionId = "skdjfhksdjfsdhfk934573642";
    /** Field contextPath  */
    private final static String contextPath = "";
    /** Field otherOperNames  */
    private final static String otherOperNames = "otherOperNames";
    /** Field protHostPortContext  */
    private final static String protHostPortContext = "protHostPortContext";
    /** Field perfomanceData  */
    private static Map<String, PerformanceDataSnapshot> perfomanceData, perfomanceDataDivided;
    /** Field config  */
    private static ApplicationConfigTO config, configDivided;

    /** Field session - fake session */
    private static final  HttpSession session = new HttpSession(){
        public long getCreationTime() {
            return 0;
        }

        public String getId() {
            return sessionId;
        }

        public long getLastAccessedTime() {
            return 0;
        }

        public ServletContext getServletContext() {
            return null;
        }

        public void setMaxInactiveInterval(int i) {
        }

        public int getMaxInactiveInterval() {
            return 0;
        }

        public HttpSessionContext getSessionContext() {
            return null;
        }

        public Object getAttribute(String s) {
            return null;
        }

        public Object getValue(String s) {
            return null;
        }

        public Enumeration getAttributeNames() {
            return null;
        }

        public String[] getValueNames() {
            return new String[0];
        }

        public void setAttribute(String s, Object o) {
        }

        public void putValue(String s, Object o) {
        }

        public void removeAttribute(String s) {
        }

        public void removeValue(String s) {
        }

        public void invalidate() {
        }

        public boolean isNew() {
            return false;
        }
    };

    private static final String EXADEL_APPLICATION = "Exadel test application";

    private static final String EXADEL_INSTANCE = "exadel-23";

    private static final String EXADEL_TITLE = EXADEL_APPLICATION+", "+EXADEL_INSTANCE;

    static {
        perfomanceData = (Map<String, PerformanceDataSnapshot>) SerializeHelper.deserializeFromFile(SerializeHelper.LIVE_DATA_FILE_NAME);
        perfomanceDataDivided = (Map<String, PerformanceDataSnapshot>) SerializeHelper.deserializeFromFile(SerializeHelper.LIVE_DATA_FILE_NAME_DIVIDED);
        config = (ApplicationConfigTO) SerializeHelper.deserializeFromFile(SerializeHelper.CONFIG_FILE);
        setApplicationConfigTO();
        assertNotNull("PerformanceDataSnapshot is null after deserialization", perfomanceData);
        assertNotNull("ApplicationConfigTO is null after deserialization", config);
    }

    /**
     * Constructor CollectorWrapperTest creates a new CollectorWrapperTest instance.
     *
     * @param name of type String
     */
    public CollectorWrapperTest(String name) {
        super(name);
        initGraphic();
    }

    /**
     * Method setUp, set up test environment.
     * @throws Exception when
     */
    public void setUp() throws Exception {
//        super.setUp();
    }

    /**
     * Method tearDown tear down test environment.
     * @throws Exception when
     */
    public void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Method testGetOtherViewData ...
     * @throws Exception when
     */
    public void testGetOtherViewData() throws Exception {
        NavigatorEntryTO navEntry = getNavigatorEntryTO(perfomanceData, config, Constant.MODULE_ABSOLUTE, "HTTP", false);
        assertNotNull(navEntry);
        OtherViewTO otherTO = CollectorWrapper.getOtherViewData(navEntry, perfomanceData, sessionId,
                contextPath, otherOperNames);
        assertNotNull(otherTO);
        SummaryRowTO[] exclusiveRows = otherTO.getExclusiveTableRows();
        assertNotNull(exclusiveRows);
        SummaryRowTO[] inclusiveRows = otherTO.getInclusiveTableRows();
        assertNotNull(inclusiveRows);
        TraceTreeNodeTO node = otherTO.getTrace();
        assertNotNull(node);

        navEntry = getNavigatorEntryTO(perfomanceData, config, Constants.MODULE_LAST_INV, "Sequence1", false);
        assertNotNull(navEntry);
        CollectorWrapper.getOtherViewData(navEntry, perfomanceData, sessionId,
                contextPath, otherOperNames);

        navEntry = getNavigatorEntryTO(perfomanceData, config, Constant.MODULE_HIERARCHICAL, "LayerTwo", false);
        assertNotNull(navEntry);
        CollectorWrapper.getOtherViewData(navEntry, perfomanceData, sessionId,
                contextPath, otherOperNames);
        
        navEntry = getNavigatorEntryTO(perfomanceDataDivided, configDivided, Constant.MODULE_ABSOLUTE, "LayerOne", true);
        assertNotNull(navEntry);
        CollectorWrapper.getOtherViewData(navEntry, perfomanceDataDivided, sessionId,
                contextPath, otherOperNames);

        navEntry = getNavigatorEntryTO(perfomanceDataDivided, configDivided, Constants.MODULE_LAST_INV, "Sequence1", true);
        assertNotNull(navEntry);
        CollectorWrapper.getOtherViewData(navEntry, perfomanceDataDivided, sessionId,
                contextPath, otherOperNames);

        navEntry = getNavigatorEntryTO(perfomanceDataDivided, configDivided, Constant.MODULE_HIERARCHICAL, "LayerTwo", true);
        assertNotNull(navEntry);
        CollectorWrapper.getOtherViewData(navEntry, perfomanceDataDivided, sessionId,
                contextPath, otherOperNames);
    }

    /**
     * Method testGetSqlViewData ...
     * @throws Exception when
     */
    public void testGetSqlViewData() throws Exception {
        SqlViewTO sqlViewTO = CollectorWrapper.getSqlViewData(
                getNavigatorEntryTO(perfomanceData, config, Constant.MODULE_ABSOLUTE, Constants.TITLE_SQL, false),
                perfomanceData, sessionId, contextPath);
        assertNotNull(sqlViewTO);
        GeneralInformationRowTO[] general = sqlViewTO.getAllSqlQueries();
        assertNotNull(general);
        Map formattedSql = sqlViewTO.getFormattedSql();
        assertNotNull(formattedSql);
        SqlRowTO[] sqlByCount = sqlViewTO.getSqlRowsByExecutionCount();
        assertNotNull(sqlByCount);
        SqlRowTO[] sqlByTime = sqlViewTO.getSqlRowsByExecutionTime();
        assertNotNull(sqlByTime);

        CollectorWrapper.getSqlViewData(
                getNavigatorEntryTO(perfomanceData, config, Constant.MODULE_HIERARCHICAL, Constants.TITLE_SQL, false),
                perfomanceData, sessionId, contextPath);

        CollectorWrapper.getSqlViewData(
                getNavigatorEntryTO(perfomanceDataDivided, configDivided, Constant.MODULE_HIERARCHICAL, Constants.TITLE_SQL, true),
                perfomanceDataDivided, sessionId, contextPath);

        CollectorWrapper.getSqlViewData(
                getNavigatorEntryTO(perfomanceDataDivided, configDivided, Constant.MODULE_ABSOLUTE, Constants.TITLE_SQL, true),
                perfomanceDataDivided, sessionId, contextPath);

    }

    /**
     * Method testGetDetailsOtherViewData ...
     * @throws Exception when
     */
    public void testGetDetailsOtherViewData() throws Exception {
        OtherViewTO otherTO = CollectorWrapper.getOtherViewData(
                this.getNavigatorEntryTO(perfomanceData, config, Constant.MODULE_ABSOLUTE, "HTTP", false),
                perfomanceData, sessionId, contextPath, otherOperNames);
        DetailOtherViewTO detailTO = CollectorWrapper.getDetailsOtherViewData(
                this.getNavigatorEntryTO(perfomanceData, config, Constant.MODULE_ABSOLUTE, "HTTP", false),
                otherTO.getTrace().getChildren()[0], perfomanceData);
        assertNotNull(detailTO);
        JdbcRowTO[] jdbcTOs = detailTO.getJdbcRows();
        assertNotNull(jdbcTOs);
        SqlRowTO[] sqbByCount = detailTO.getSqlRowsByExecutionCount();
        assertNotNull(sqbByCount);
        SqlRowTO[] sqbByTime = detailTO.getSqlRowsByExecutionTime();
        assertNotNull(sqbByTime);

        otherTO = CollectorWrapper.getOtherViewData(
                this.getNavigatorEntryTO(perfomanceData, config, Constant.MODULE_HIERARCHICAL, "HTTP", false),
                perfomanceData, sessionId, contextPath, otherOperNames);
        CollectorWrapper.getDetailsOtherViewData(
                this.getNavigatorEntryTO(perfomanceData, config, Constant.MODULE_HIERARCHICAL, "HTTP", false),
                otherTO.getTrace().getChildren()[0], perfomanceData);

        otherTO = CollectorWrapper.getOtherViewData(
                this.getNavigatorEntryTO(perfomanceData, config, Constants.MODULE_LAST_INV, "Sequence1", false),
                perfomanceData, sessionId, contextPath, otherOperNames);
        CollectorWrapper.getDetailsOtherViewData(
                this.getNavigatorEntryTO(perfomanceData, config, Constants.MODULE_LAST_INV, "Sequence1", false),
                otherTO.getTrace().getChildren()[0], perfomanceData);

        otherTO = CollectorWrapper.getOtherViewData(
                this.getNavigatorEntryTO(perfomanceDataDivided, configDivided, Constant.MODULE_ABSOLUTE, "LayerOne", true),
                perfomanceDataDivided, sessionId, contextPath, otherOperNames);
        CollectorWrapper.getDetailsOtherViewData(
                this.getNavigatorEntryTO(perfomanceDataDivided, configDivided, Constant.MODULE_ABSOLUTE, "LayerOne", true),
                otherTO.getTrace().getChildren()[0], perfomanceDataDivided);

        otherTO = CollectorWrapper.getOtherViewData(
                this.getNavigatorEntryTO(perfomanceDataDivided, configDivided, Constant.MODULE_HIERARCHICAL, "LayerTwo", true),
                perfomanceDataDivided, sessionId, contextPath, otherOperNames);
        CollectorWrapper.getDetailsOtherViewData(
                this.getNavigatorEntryTO(perfomanceDataDivided, configDivided, Constant.MODULE_HIERARCHICAL, "LayerTwo", true),
                otherTO.getTrace().getChildren()[0], perfomanceDataDivided);

        otherTO = CollectorWrapper.getOtherViewData(
                this.getNavigatorEntryTO(perfomanceDataDivided, configDivided, Constants.MODULE_LAST_INV, "Sequence1", true),
                perfomanceDataDivided, sessionId, contextPath, otherOperNames);
        CollectorWrapper.getDetailsOtherViewData(
                this.getNavigatorEntryTO(perfomanceDataDivided, configDivided, Constants.MODULE_LAST_INV, "Sequence1", true),
                otherTO.getTrace().getChildren()[0], perfomanceDataDivided);

    }

    /**
     * Method testGetApplicationData ...
     * @throws Exception when
     */
    public void testGetApplicationData() throws Exception {
        NavigatorEntryTO navTO = this.getNavigatorEntryTO(perfomanceData, config, Constant.MODULE_ABSOLUTE, Constant.TITLE_APPLICATION, false);
        ApplicationViewTO applicationViewTO = CollectorWrapper.getApplicationData(
                perfomanceData, navTO, sessionId, contextPath, "other operations"
        );
        assertNotNull(applicationViewTO);
        ApplicationRowTO[] applicationRowTOArray = applicationViewTO.getRows();
        assertNotNull(applicationRowTOArray);

        navTO = this.getNavigatorEntryTO(perfomanceData, config, Constant.MODULE_HIERARCHICAL, Constant.TITLE_APPLICATION, false);
        CollectorWrapper.getApplicationData(perfomanceData, navTO, sessionId, contextPath, "other operations");

        navTO = this.getNavigatorEntryTO(perfomanceData, config, Constants.MODULE_LAST_INV, Constant.TITLE_APPLICATION, false);
        CollectorWrapper.getApplicationData(perfomanceData, navTO, sessionId, contextPath, "other operations");

        navTO = this.getNavigatorEntryTO(perfomanceDataDivided, configDivided, Constant.MODULE_ABSOLUTE, EXADEL_TITLE, true);
        CollectorWrapper.getApplicationData(perfomanceDataDivided, navTO, sessionId, contextPath, "other operations");

        navTO = this.getNavigatorEntryTO(perfomanceDataDivided, configDivided, Constant.MODULE_HIERARCHICAL, EXADEL_TITLE, true);
        CollectorWrapper.getApplicationData(perfomanceDataDivided, navTO, sessionId, contextPath, "other operations");

        navTO = this.getNavigatorEntryTO(perfomanceDataDivided, configDivided, Constants.MODULE_LAST_INV, EXADEL_TITLE, true);
        CollectorWrapper.getApplicationData(perfomanceDataDivided, navTO, sessionId, contextPath, "other operations");

    }

    /**
     * Method testGetNavigator ...
     * @throws Exception when
     */
    public void testGetNavigator() throws Exception {
        NavigatorTO navigatorTO = CollectorWrapper.getNavigator(perfomanceData, config, true);
        assertNotNull(navigatorTO);
        NavigatorEntryTO[] absolute = navigatorTO.getAbsolute();
        NavigatorEntryTO[] hierarch = navigatorTO.getHierarchModule();
        NavigatorEntryTO[] last = navigatorTO.getLastInv();
        assertNotNull(absolute);
        assertNotNull(hierarch);
        assertNotNull(last);
    }

    /**
     * Method testGetApplicationConfigTO ...
     * @throws Exception when
     */
    public void testGetApplicationConfigTO() throws Exception {
        assertNotNull(config);
        ApplicationEntryTO[] applications = config.getApplicationList();
        assertNotNull(applications);
        for (ApplicationEntryTO entry : applications) {
            assertNotNull(entry.getApplicationTitle());
            assertNotNull(entry.getInstanceList());
        }
        String endDate = config.getEndDateTime();
        String startDate = config.getStartDateTime();
    }

    /**
     * Method testReport run report generation test.
     * @throws Exception when some problem occured.
     */
    public void testReport() throws Exception{
        reportConfig.setAbsoluteModuleMode(true);
        reportConfig.setAllDisplayed(true);
        reportConfig.setApplications(true);
        reportConfig.setCurrentlySelected(false);
        reportConfig.setCurrentlySelectedString("");
        reportConfig.setDiagrams(false);
        reportConfig.setExclusiveMode(false);
        reportConfig.setHierarchicalModuleMode(true);
        reportConfig.setInclusiveExclusiveMode(true);
        reportConfig.setInclusiveMode(false);
        reportConfig.setKeyInReportMap("");
        reportConfig.setLayer(true);
        reportConfig.setMergedApplicationTitle("");
        reportConfig.setQueries("10");
        reportConfig.setStatistics(true);

        reportConfig.setCsvFormat(true);
        reportConfig.setReportFormatName(Constants.CSV_FORMAT_NAME);

        CollectorWrapper.generateReport(config, reportConfig, otherOperNames, session,
                contextPath, protHostPortContext, perfomanceData);

        reportConfig.setCsvFormat(false);
        reportConfig.setExcelFormat(true);
        reportConfig.setReportFormatName(Constants.MS_EXCEL_FORMAT_NAME);

        CollectorWrapper.generateReport(config, reportConfig, otherOperNames, session,
                contextPath, protHostPortContext, perfomanceData);

        reportConfig.setExcelFormat(false);
        reportConfig.setPdfFormat(true);
        reportConfig.setReportFormatName(Constants.PDF_FORMAT_NAME);

        CollectorWrapper.generateReport(config, reportConfig, otherOperNames, session,
                contextPath, protHostPortContext, perfomanceData);

        reportConfig.setPdfFormat(false);
        reportConfig.setHtmlFormat(true);
        reportConfig.setReportFormatName(Constants.HTML_FORMAT_NAME);

        CollectorWrapper.generateReport(config, reportConfig, otherOperNames, session,
                contextPath, protHostPortContext, perfomanceData);
    }

    /**
     * Method suite ...
     * @return Test
     */
    public static Test suite() {
        return new TestSuite(CollectorWrapperTest.class);
    }

    /**
     * Method setApplicationConfigTO init ApplicationConfigTO before testing
     *
     */
    private static void setApplicationConfigTO(){
        configDivided = new ApplicationConfigTO(true, true);
        configDivided.setApplicationList(new ApplicationEntryTO[]{
                new ApplicationEntryTO("Exadel test application", new InstanceEntryTO[]{new InstanceEntryTO("exadel-23")}),
                new ApplicationEntryTO("Exadel test application 2", new InstanceEntryTO[]{new InstanceEntryTO("exadel-23")})
        });
    }

    /**
     * Method getNavigatorEntryTO ...
     *
     * @param perfomanceData
     * @param config
     * @param moduleType of type String
     * @param layerType of type String @return NavigatorEntryTO
     */
    private NavigatorEntryTO getNavigatorEntryTO(Map<String, PerformanceDataSnapshot> perfomanceData,
                                                 ApplicationConfigTO config, String moduleType,
                                                 String layerType, boolean isDivided) {
        assertNotNull(moduleType);
        assertNotNull(layerType);
        NavigatorTO navigatorTO = CollectorWrapper.getNavigator(perfomanceData, config, true);
        assertNotNull(navigatorTO);
        NavigatorEntryTO navEntry = null;
        NavigatorEntryTO[] navEntryArray = null;
        if (Constant.MODULE_ABSOLUTE.equals(moduleType)) navEntryArray = navigatorTO.getAbsolute();
        else if (Constant.MODULE_HIERARCHICAL.equals(moduleType))navEntryArray = navigatorTO.getHierarchModule();
        else if (Constants.MODULE_LAST_INV.equals(moduleType)) navEntryArray = navigatorTO.getLastInv();
        assertNotNull(navEntryArray);
        for (NavigatorEntryTO entry : navEntryArray) {
            assertNotNull(entry);
            assertNotNull(entry.getTitle());
            if (entry.getTitle().endsWith(layerType)) {
                if (!isDivided){
                    navEntry = entry;
                    break;
                }
                else if (entry.getApplications()!=null){
                    if (EXADEL_APPLICATION.equals(entry.getApplications().getApplicationTitle()) &&
                            EXADEL_INSTANCE.equals(entry.getApplications().getInstanceList()[0].getName())){
                        navEntry = entry;
                        break;        
                    }
                }
            }
        }
        return navEntry;
    }

    /**
     * Method initGraphic ...
     */
    private void initGraphic() {
        if (GraphicUtil.getSmartImageMap() == null) {
            GraphicUtil.setSmartImageMap(new SmartImageMap());
        }
        /* Set default value of pie3D Chart Dimension */
        GraphicUtil.getChartDimensionsMap().put(Constant.PIE3D_CHART_HEIGHT_LABEL, 350);
        GraphicUtil.getChartDimensionsMap().put(Constant.PIE3D_CHART_WIDTH_LABEL, 640);
        /* Set default value of Bar Chart Dimension */
        GraphicUtil.getChartDimensionsMap().put(Constant.BAR_CHART_HEIGHT_LABEL, 300);
        GraphicUtil.getChartDimensionsMap().put(Constant.BAR_CHART_WIDTH_LABEL, 600);
        GraphicUtil.getChartDimensionsMap().put(Constant.IMAGE_TIME_OUT, 20);
    }

}