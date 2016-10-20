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
 * SIRServiceStubImpl.java Date created: 22.01.2008
 * Last modified by: $Author: soyon.lim $
 * $Revision: 14286 $ $Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */
package net.sf.infrared2.gwt.server.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import net.sf.infrared2.gwt.client.Constants;
import net.sf.infrared2.gwt.client.report.ReportConfigTO;
import net.sf.infrared2.gwt.client.to.ApplicationConfigTO;
import net.sf.infrared2.gwt.client.to.ApplicationEntryTO;
import net.sf.infrared2.gwt.client.to.ApplicationVersionTO;
import net.sf.infrared2.gwt.client.to.InitialStateTO;
import net.sf.infrared2.gwt.client.to.InstanceEntryTO;
import net.sf.infrared2.gwt.client.to.NavigatorEntryTO;
import net.sf.infrared2.gwt.client.to.NavigatorTO;
import net.sf.infrared2.gwt.client.to.application.ApplicationRowTO;
import net.sf.infrared2.gwt.client.to.application.ApplicationViewTO;
import net.sf.infrared2.gwt.client.to.other.DetailOtherViewTO;
import net.sf.infrared2.gwt.client.to.other.GeneralInformationRowTO;
import net.sf.infrared2.gwt.client.to.other.JdbcRowTO;
import net.sf.infrared2.gwt.client.to.other.OtherViewTO;
import net.sf.infrared2.gwt.client.to.other.SummaryRowTO;
import net.sf.infrared2.gwt.client.to.other.TraceTreeNodeTO;
import net.sf.infrared2.gwt.client.to.report.ReportResultTO;
import net.sf.infrared2.gwt.client.to.sql.SqlRowTO;
import net.sf.infrared2.gwt.client.to.sql.SqlViewTO;
import net.sf.infrared2.gwt.client.to.status.ResultStatus;
import net.sf.infrared2.gwt.client.to.status.ResultStatusTO;
import net.sf.infrared2.gwt.client.tree.FakeTreeImpl;
import net.sf.infrared2.server.Constant;
import net.sf.infrared2.server.chart.SmartImageMap;
import net.sf.infrared2.server.util.GraphicUtil;
import net.sf.infrared2.server.util.sql.SQLToHtml;

import org.apache.log4j.Logger;


/**
 * <b>SIRServiceStubImpl</b>
 * <p>
 * Async service STUB(!) implementation. Provide all necessary data from
 * infrared to Web-GUI module.<br>
 * To be used for infrared-independent client presentation.<br>
 * To switch modes change value of isRealMode context parameter of web.xml
 * 
 * @author Zgonikov Gleb
 */

public class SIRServiceStubImpl extends SIRServiceImpl {

    /** use serialVersionUID from JDK 1.0.2 for interoperability */
    private static final long serialVersionUID = 1695723380824504879L;

    /** Field isRealMode */
    private static boolean isRealMode;

    /** Field logger */
    private static final transient Logger logger = Logger.getLogger(SIRServiceStubImpl.class);

    /**
     * smartImageMap keeps map of images. Located in Servlet Context.
     */
    protected SmartImageMap smartImageMap;

    /**
     * chartDimensionsMap keeps sizes (width and height) all pie3D and Bar
     * Charts.
     */
    protected HashMap<String, Integer> chartDimensionsMap = new HashMap<String, Integer>();

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        initGraphic();
    }

    public InitialStateTO initClientState() {
        // refreshData();
        if (isRealMode)
            return super.initClientState();
        InitialStateTO toReturn = new InitialStateTO();
        toReturn.setCacheClientEnabled(false);
        toReturn.setSessionDuration(30 * 60 - 180);
        toReturn.setResultStatus(new ResultStatus(Constants.SUCCESS_CODE));
        return toReturn;
    }

    /**
     * Method initGraphic ...
     */
    private void initGraphic() {
        if (GraphicUtil.getSmartImageMap() == null) {
            GraphicUtil.setSmartImageMap(new SmartImageMap());
        }
        if (!isRealMode) {
            /* Set default value of pie3D Chart Dimension */
            GraphicUtil.getChartDimensionsMap().put(Constant.PIE3D_CHART_HEIGHT_LABEL, 250);
            GraphicUtil.getChartDimensionsMap().put(Constant.PIE3D_CHART_WIDTH_LABEL, 540);
            /* Set default value of Bar Chart Dimension */
            GraphicUtil.getChartDimensionsMap().put(Constant.BAR_CHART_HEIGHT_LABEL, 200);
            GraphicUtil.getChartDimensionsMap().put(Constant.BAR_CHART_WIDTH_LABEL, 500);
            GraphicUtil.getChartDimensionsMap().put(Constant.IMAGE_TIME_OUT, 20);
        }
    }

    /**
     * clean session data
     */
    @Override
    public ResultStatusTO refreshData() {

        if (isRealMode)
            return super.refreshData();

        return new ResultStatusTO(new ResultStatus(Constants.SUCCESS_CODE));
    }

    /**
     * @return navigator links
     * @see SIRServiceImpl#getNavigator(ApplicationConfigTO)
     */
    @Override
    public NavigatorTO getNavigator(ApplicationConfigTO config) {

        if (isRealMode)
            return super.getNavigator(config);

        GraphicUtil.setGraphLabelEnabled(config.isGraphLabelEnabled());

        ApplicationConfigTO configTO = getApplicationConfigTO();
        final ApplicationEntryTO applEntry = new ApplicationEntryTO();
        applEntry.setApplicationTitle(configTO.getApplicationList()[0].getApplicationTitle());
        applEntry.setInstanceList(new InstanceEntryTO[] { configTO.getApplicationList()[0]
                        .getInstanceList()[0] });

        final NavigatorEntryTO root = new NavigatorEntryTO();
        root.setModuleType(Constant.MODULE_ABSOLUTE);
        root.setTitle(Constant.TITLE_APPLICATION);
        root.setRoot(true);
        if (config.isDividedByApplications()) {
            root.setTitle(applEntry.getApplicationTitle() + ", "
                            + applEntry.getInstanceList()[0].getName());
            root.setApplications(applEntry);
        }

        NavigatorEntryTO[] absModule = new NavigatorEntryTO[] {
                        root,
                        new NavigatorEntryTO("HTTP", Constant.MODULE_ABSOLUTE, (config
                                        .isDividedByApplications()) ? applEntry : null),
                        new NavigatorEntryTO("JDBC", Constant.MODULE_ABSOLUTE, (config
                                        .isDividedByApplications()) ? applEntry : null),
                        new NavigatorEntryTO("SQL", Constant.MODULE_ABSOLUTE, (config
                                        .isDividedByApplications()) ? applEntry : null), };

        final NavigatorEntryTO navigatorEntryTO = new NavigatorEntryTO(Constant.TITLE_APPLICATION,
                        Constant.MODULE_HIERARCHICAL);
        navigatorEntryTO.setRoot(true);
        if (config.isDividedByApplications()) {
            navigatorEntryTO.setTitle(applEntry.getApplicationTitle() + ", "
                            + applEntry.getInstanceList()[0].getName());
            navigatorEntryTO.setApplications(applEntry);
        }
        NavigatorEntryTO[] hierarchModule = new NavigatorEntryTO[] {
                        navigatorEntryTO,
                        new NavigatorEntryTO("HTTP", Constant.MODULE_HIERARCHICAL, (config
                                        .isDividedByApplications()) ? applEntry : null),
                        new NavigatorEntryTO("HTTP.Jsp", Constant.MODULE_HIERARCHICAL, (config
                                        .isDividedByApplications()) ? applEntry : null),
                        new NavigatorEntryTO("SQL", Constant.MODULE_HIERARCHICAL, (config
                                        .isDividedByApplications()) ? applEntry : null),
                        new NavigatorEntryTO("LayerOne", Constant.MODULE_HIERARCHICAL, (config
                                        .isDividedByApplications()) ? applEntry : null),
                        new NavigatorEntryTO("HTTP.Jsp.LayerTwo", Constant.MODULE_HIERARCHICAL,
                                        (config.isDividedByApplications()) ? applEntry : null),
                        new NavigatorEntryTO("LayerOne.SQL", Constant.MODULE_HIERARCHICAL, (config
                                        .isDividedByApplications()) ? applEntry : null),
                        new NavigatorEntryTO("HTTP.LayerTwo", Constant.MODULE_HIERARCHICAL, (config
                                        .isDividedByApplications()) ? applEntry : null),
                        new NavigatorEntryTO("HTTP.LayerTwo.LayerOne",
                                        Constant.MODULE_HIERARCHICAL, (config
                                                        .isDividedByApplications()) ? applEntry
                                                        : null),
                        new NavigatorEntryTO("HTTP.LayerTwo.LayerOne.SQL",
                                        Constant.MODULE_HIERARCHICAL, (config
                                                        .isDividedByApplications()) ? applEntry
                                                        : null),
                        new NavigatorEntryTO("HTTP.LayerTwo.SQL", Constant.MODULE_HIERARCHICAL,
                                        (config.isDividedByApplications()) ? applEntry : null),
                        new NavigatorEntryTO("HTTP.Jsp.LayerTwo.JDBC",
                                        Constant.MODULE_HIERARCHICAL, (config
                                                        .isDividedByApplications()) ? applEntry
                                                        : null),
                        new NavigatorEntryTO("LayerOne.JDBC", Constant.MODULE_HIERARCHICAL, (config
                                        .isDividedByApplications()) ? applEntry : null),
                        new NavigatorEntryTO("JDBC", Constant.MODULE_HIERARCHICAL, (config
                                        .isDividedByApplications()) ? applEntry : null),
                        new NavigatorEntryTO("HTTP.Jsp.LayerOne", Constant.MODULE_HIERARCHICAL,
                                        (config.isDividedByApplications()) ? applEntry : null),
                        new NavigatorEntryTO("HTTP.LayerTwo.JDBC", Constant.MODULE_HIERARCHICAL,
                                        (config.isDividedByApplications()) ? applEntry : null),
                        new NavigatorEntryTO("HTTP.LayerTwo.LayerOne.JDBC",
                                        Constant.MODULE_HIERARCHICAL, (config
                                                        .isDividedByApplications()) ? applEntry
                                                        : null),
                        new NavigatorEntryTO("HTTP", Constant.MODULE_HIERARCHICAL, (config
                                        .isDividedByApplications()) ? applEntry : null),
                        new NavigatorEntryTO("HTTP.Jsp", Constant.MODULE_HIERARCHICAL, (config
                                        .isDividedByApplications()) ? applEntry : null),
                        new NavigatorEntryTO("SQL", Constant.MODULE_HIERARCHICAL, (config
                                        .isDividedByApplications()) ? applEntry : null),
                        new NavigatorEntryTO("LayerOne", Constant.MODULE_HIERARCHICAL, (config
                                        .isDividedByApplications()) ? applEntry : null),
                        new NavigatorEntryTO("HTTP.Jsp.LayerTwo", Constant.MODULE_HIERARCHICAL,
                                        (config.isDividedByApplications()) ? applEntry : null),
                        new NavigatorEntryTO("LayerOne.SQL", Constant.MODULE_HIERARCHICAL, (config
                                        .isDividedByApplications()) ? applEntry : null),
                        new NavigatorEntryTO("HTTP.LayerTwo", Constant.MODULE_HIERARCHICAL, (config
                                        .isDividedByApplications()) ? applEntry : null),
                        new NavigatorEntryTO("HTTP.LayerTwo.LayerOne",
                                        Constant.MODULE_HIERARCHICAL, (config
                                                        .isDividedByApplications()) ? applEntry
                                                        : null),
                        new NavigatorEntryTO("HTTP.LayerTwo.LayerOne.SQL",
                                        Constant.MODULE_HIERARCHICAL, (config
                                                        .isDividedByApplications()) ? applEntry
                                                        : null),
                        new NavigatorEntryTO("HTTP.LayerTwo.SQL", Constant.MODULE_HIERARCHICAL,
                                        (config.isDividedByApplications()) ? applEntry : null),
                        new NavigatorEntryTO("HTTP.Jsp.LayerTwo.JDBC",
                                        Constant.MODULE_HIERARCHICAL, (config
                                                        .isDividedByApplications()) ? applEntry
                                                        : null),
                        new NavigatorEntryTO("LayerOne.JDBC", Constant.MODULE_HIERARCHICAL, (config
                                        .isDividedByApplications()) ? applEntry : null),
                        new NavigatorEntryTO("JDBC", Constant.MODULE_HIERARCHICAL, (config
                                        .isDividedByApplications()) ? applEntry : null),
                        new NavigatorEntryTO("HTTP.Jsp.LayerOne", Constant.MODULE_HIERARCHICAL,
                                        (config.isDividedByApplications()) ? applEntry : null),
                        new NavigatorEntryTO("HTTP.LayerTwo.JDBC", Constant.MODULE_HIERARCHICAL,
                                        (config.isDividedByApplications()) ? applEntry : null),
                        new NavigatorEntryTO("HTTP.LayerTwo.LayerOne.JDBC",
                                        Constant.MODULE_HIERARCHICAL, (config
                                                        .isDividedByApplications()) ? applEntry
                                                        : null),
                        new NavigatorEntryTO("HTTP", Constant.MODULE_HIERARCHICAL, (config
                                        .isDividedByApplications()) ? applEntry : null),
                        new NavigatorEntryTO("HTTP.Jsp", Constant.MODULE_HIERARCHICAL, (config
                                        .isDividedByApplications()) ? applEntry : null),
                        new NavigatorEntryTO("SQL", Constant.MODULE_HIERARCHICAL, (config
                                        .isDividedByApplications()) ? applEntry : null),
                        new NavigatorEntryTO("LayerOne", Constant.MODULE_HIERARCHICAL),
                        new NavigatorEntryTO("HTTP.Jsp.LayerTwo", Constant.MODULE_HIERARCHICAL,
                                        (config.isDividedByApplications()) ? applEntry : null),
                        new NavigatorEntryTO("LayerOne.SQL", Constant.MODULE_HIERARCHICAL, (config
                                        .isDividedByApplications()) ? applEntry : null),
                        new NavigatorEntryTO("HTTP.LayerTwo", Constant.MODULE_HIERARCHICAL, (config
                                        .isDividedByApplications()) ? applEntry : null),
                        new NavigatorEntryTO("HTTP.LayerTwo.LayerOne",
                                        Constant.MODULE_HIERARCHICAL, (config
                                                        .isDividedByApplications()) ? applEntry
                                                        : null),
                        new NavigatorEntryTO("HTTP.LayerTwo.LayerOne.SQL",
                                        Constant.MODULE_HIERARCHICAL, (config
                                                        .isDividedByApplications()) ? applEntry
                                                        : null),
                        new NavigatorEntryTO("HTTP.LayerTwo.SQL", Constant.MODULE_HIERARCHICAL,
                                        (config.isDividedByApplications()) ? applEntry : null),
                        new NavigatorEntryTO("HTTP.Jsp.LayerTwo.JDBC",
                                        Constant.MODULE_HIERARCHICAL, (config
                                                        .isDividedByApplications()) ? applEntry
                                                        : null),
                        new NavigatorEntryTO("LayerOne.JDBC", Constant.MODULE_HIERARCHICAL, (config
                                        .isDividedByApplications()) ? applEntry : null),
                        new NavigatorEntryTO("JDBC", Constant.MODULE_HIERARCHICAL, (config
                                        .isDividedByApplications()) ? applEntry : null),
                        new NavigatorEntryTO("HTTP.Jsp.LayerOne", Constant.MODULE_HIERARCHICAL,
                                        (config.isDividedByApplications()) ? applEntry : null),
                        new NavigatorEntryTO("HTTP.LayerTwo.JDBC", Constant.MODULE_HIERARCHICAL,
                                        (config.isDividedByApplications()) ? applEntry : null),
                        new NavigatorEntryTO("HTTP.LayerTwo.LayerOne.JDBC",
                                        Constant.MODULE_HIERARCHICAL, (config
                                                        .isDividedByApplications()) ? applEntry
                                                        : null) };

        final NavigatorEntryTO mainLastInv = new NavigatorEntryTO(Constant.TITLE_APPLICATION,
                        Constants.MODULE_LAST_INV);
        mainLastInv.setRoot(true);
        if (config.isDividedByApplications()) {
            mainLastInv.setTitle(applEntry.getApplicationTitle() + ", "
                            + applEntry.getInstanceList()[0].getName());
            mainLastInv.setApplications(applEntry);
        }
        NavigatorEntryTO[] lastInv = new NavigatorEntryTO[] {
                        mainLastInv,
                        new NavigatorEntryTO("Sequence1", Constants.MODULE_LAST_INV, (config
                                        .isDividedByApplications()) ? applEntry : null),
                        new NavigatorEntryTO("Sequence2", Constants.MODULE_LAST_INV, (config
                                        .isDividedByApplications()) ? applEntry : null),
                        new NavigatorEntryTO("Sequence3", Constants.MODULE_LAST_INV, (config
                                        .isDividedByApplications()) ? applEntry : null),
                        new NavigatorEntryTO("Sequence4", Constants.MODULE_LAST_INV, (config
                                        .isDividedByApplications()) ? applEntry : null),
                        new NavigatorEntryTO("Sequence5", Constants.MODULE_LAST_INV, (config
                                        .isDividedByApplications()) ? applEntry : null),
                        new NavigatorEntryTO("Sequence6", Constants.MODULE_LAST_INV, (config
                                        .isDividedByApplications()) ? applEntry : null) };
        NavigatorTO toReturn = new NavigatorTO(absModule, lastInv, hierarchModule);
        toReturn.setResultStatus(new ResultStatus(Constants.SUCCESS_CODE));
        return toReturn;
    }

    /**
     * @return data for application(s) layers
     */
    @Override
    public ApplicationViewTO getApplicationData(ApplicationConfigTO config,
                    NavigatorEntryTO navigatorEntryTO, String otherOperationsName) {

        if (isRealMode)
            return super.getApplicationData(config, navigatorEntryTO, otherOperationsName);

        ApplicationViewTO toReturn = new ApplicationViewTO();
        ApplicationRowTO[] rows = null;

        if (Constants.MODULE_LAST_INV.equals(navigatorEntryTO.getModuleType())) {
            rows = new ApplicationRowTO[] { new ApplicationRowTO("Sequence1", 8204.00, 26),
                            new ApplicationRowTO("Sequence2", 4.00, 2),
                            new ApplicationRowTO("Sequence3", 6104.00, 45),
                            new ApplicationRowTO("Sequence4", 0.00, 1),
                            new ApplicationRowTO("Sequence5", 0.00, 1) };
        } else if (Constant.MODULE_ABSOLUTE.equals(navigatorEntryTO.getModuleType())) {
            rows = new ApplicationRowTO[] { new ApplicationRowTO("HTTP", 8204.00, 26),
                            new ApplicationRowTO("SQL", 4.00, 2),
                            new ApplicationRowTO("JSP", 6104.00, 45),
                            new ApplicationRowTO("JDBC", 0.00, 1) };
        }
        toReturn.setRows(rows);

        toReturn.setModuleType(navigatorEntryTO.getModuleType());

        GraphicUtil.getApplicationDataImages(toReturn,
                        getThreadLocalRequest().getSession().getId(), getProtHostPortContext(),
                        navigatorEntryTO, otherOperationsName);

        toReturn.setImgByCountUrl(getFakeImage("images/tmp/diagram.gif"));
        toReturn.setImgByTimeUrl(getFakeImage("images/tmp/diagram2.gif"));

        toReturn.setResultStatus(new ResultStatus(Constants.SUCCESS_CODE));
        toReturn.setNavigatorEntryTO(navigatorEntryTO);
        return toReturn;
    }

    /**
     * Method getFakeImage generates fake image map.
     * 
     * @param s of type String
     * @return Map<String, String>
     */
    private Map<String, String> getFakeImage(String s) {
        Map<String, String> imageMap = new HashMap<String, String>();
        imageMap.put("<IMG SRC='" + s + "'" + " />", "<IMG SRC='" + s + "'" + " />");
        return imageMap;
    }

    /**
     * @return data for other layers
     */
    @Override
    public OtherViewTO getOtherViewData(ApplicationConfigTO config,
                    NavigatorEntryTO navigatorEntryTO, String otherOperationsName) {

        if (isRealMode)
            return super.getOtherViewData(config, navigatorEntryTO, otherOperationsName);

        OtherViewTO toReturn = new OtherViewTO();

        // set trace tree
        toReturn.setTrace(FakeTreeImpl.getTraceTree());

        // setting summary table information
        SummaryRowTO[] summaryTableRowsIncl = new SummaryRowTO[] {
                        new SummaryRowTO("/exadel/", 2345.00, 21, 123.44112, 142.11232, 12, 432,
                                        23, 41),
                        new SummaryRowTO("/exadel/editCompany.jsf", 2345.01230, 241, 1323.41234,
                                        1142.12123, 121, 432, 213, 421),
                        new SummaryRowTO("/exadel/companyList.jsf", 345.01230, 231, 1223.41234,
                                        1442.11232, 122, 4321, 232, 412),
                        new SummaryRowTO("/exadel/result.jsf", 235.01230, 214, 123.44, 1742.11232,
                                        142, 4327, 233, 411),
                        new SummaryRowTO("/exadel/createCompany.jsf", 45.0120, 231, 1237.41234,
                                        1142.11232, 142, 4362, 223, 241) };
        toReturn.setInclusiveTableRows(summaryTableRowsIncl);
        SummaryRowTO[] summaryTableRowsExcl = new SummaryRowTO[] {
                        new SummaryRowTO("/exadel/", 345.00, 21, 123.44, 12.12, 1, 42, 2, 1),
                        new SummaryRowTO("/exadel/editCompany.jsf", 2345.00, 241, 132.44, 1142.12,
                                        121, 432, 213, 421),
                        new SummaryRowTO("/exadel/companyList.jsf", 34.00, 21, 1223.41234, 1442.12,
                                        122, 4321, 232, 412),
                        new SummaryRowTO("/exadel/result.jsf", 23.00, 24, 12.44, 142.11232, 12,
                                        427, 23, 41),
                        new SummaryRowTO("/exadel/createCompany.jsf", 5.00, 21, 1237.41234, 142.12,
                                        42, 4362, 23, 21) };

        toReturn.setExclusiveTableRows(summaryTableRowsExcl);

        GraphicUtil.getOtherViewDataImages(toReturn, getThreadLocalRequest().getSession().getId(),
                        getProtHostPortContext(), otherOperationsName);

        toReturn.setImgExclusiveOperationByCountUrl(getFakeImage("images/tmp/diagram.gif"));
        toReturn.setImgExclusiveOperationByTimeUrl(getFakeImage("images/tmp/diagram2.gif"));
        toReturn.setImgInclusiveOperationByCountUrl(getFakeImage("images/tmp/diagram3.gif"));
        toReturn.setImgInclusiveOperationByTimeUrl(getFakeImage("images/tmp/diagram4.gif"));

        toReturn.setResultStatus(new ResultStatus(Constants.SUCCESS_CODE));
        toReturn.setNavigatorEntryTO(navigatorEntryTO);
        return toReturn;
    }

    /**
     * Method getDetailsOtherViewData returns data for details.
     * 
     * @param appTo of type ApplicationConfigTO
     * @param navTo of type NavigatorEntryTO
     * @param node of type TraceTreeNodeTO
     * @return DetailOtherViewTO
     */
    @Override
    public DetailOtherViewTO getDetailsOtherViewData(ApplicationConfigTO appTo,
                    NavigatorEntryTO navTo, TraceTreeNodeTO node) {
        if (isRealMode)
            return super.getDetailsOtherViewData(appTo, navTo, node);

        DetailOtherViewTO toReturn = new DetailOtherViewTO();
        // setting general info table information
        // GeneralInformationRowTO[] generalInformationTableRows = new
        // GeneralInformationRowTO[]{
        // new GeneralInformationRowTO(12, 1233, 132, 33, 123, 12, 123, 45, 67,
        // 243, 123, 645)
        // };
        // to.setGeneralInformationTableRows(generalInformationTableRows);

        if (Constants.TITLE_SQL.equals(node.getLayerName())) {
            toReturn.setFormattedSql(SQLToHtml.convertToHtml(node.getText()));
        } else {
            // setting jdbc table information
            JdbcRowTO[] jdbcRows = new JdbcRowTO[] {
                            new JdbcRowTO("java.sql.Conection:submit", 132.44, 2),
                            new JdbcRowTO("java.sql.Statement:close", 12.00, 1),

            };
            toReturn.setJdbcRows(jdbcRows);

            // setting Top 5 SQL queries by Average Execution Time table
            // information
            SqlRowTO[] sqlRowsByExecutionTime = new SqlRowTO[] {
                            new SqlRowTO("Q1", "update COMPANY set name=?", 131.00, 42.00, 89.00,
                                            1, 1, 42, 89, 42, 89, 42, 89, 42, 89),
                            new SqlRowTO("Q2", "delete from COMPANY", 16.00, 16.00, 0.00, 1, 1, 16,
                                            0, 16, 0, 16, 0, 16, 0),
                            new SqlRowTO("Q3", "update COMPANY set name=?", 131.00, 42.00, 89.00,
                                            1, 1, 42, 89, 42, 89, 42, 89, 42, 89),
                            new SqlRowTO("Q4", "delete from COMPANY", 16.00, 16.00, 0.00, 1, 1, 16,
                                            0, 16, 0, 16, 0, 16, 0),
                            new SqlRowTO("Q5", "update COMPANY set name=?", 131.00, 42.00, 89.00,
                                            1, 1, 42, 89, 42, 89, 42, 89, 42, 89) };
            toReturn.setSqlRowsByExecutionTime(sqlRowsByExecutionTime);

            // setting Top 5 SQL Queries by Execution Counts table information
            SqlRowTO[] sqlRowsByExecutionCount = new SqlRowTO[] {
                            new SqlRowTO("Q1", "update COMPANY set name=?", 131.00, 42.00, 89.00,
                                            1, 1, 42, 89, 42, 89, 42, 89, 42, 89),
                            new SqlRowTO("Q2", "delete from COMPANY", 16.00, 16.00, 0.00, 1, 1, 16,
                                            0, 16, 0, 16, 0, 16, 0)

            };
            toReturn.setSqlRowsByExecutionCount(sqlRowsByExecutionCount);

        }
        toReturn.setResultStatus(new ResultStatus(Constants.SUCCESS_CODE));
        toReturn.setNavigatorEntryTO(navTo);
        return toReturn;
    }

    /**
     * @return data for SQL layers
     */
    @Override
    public SqlViewTO getSqlViewData(ApplicationConfigTO config, NavigatorEntryTO navigatorEntryTO) {

        if (isRealMode)
            return super.getSqlViewData(config, navigatorEntryTO);

        SqlViewTO toReturn = new SqlViewTO();

        // setting Top 5 SQL queries by Average Execution Time table information
        SqlRowTO[] sqlRowsByExecutionTime = new SqlRowTO[] {
                        new SqlRowTO("Q1", "update COMPANY set name=?", 131.00, 42.00, 89.00, 1, 1,
                                        42, 89, 42, 89, 42, 89, 42, 89),
                        new SqlRowTO("Q2", "delete from COMPANY", 16.00, 16.00, 0.00, 1, 1, 16, 0,
                                        16, 0, 16, 0, 16, 0),
                        new SqlRowTO("Q3", "update COMPANY set name=?", 131.00, 42.00, 89.00, 1, 1,
                                        42, 89, 42, 89, 42, 89, 42, 89),
                        new SqlRowTO("Q4", "delete from COMPANY", 16.00, 16.00, 0.00, 1, 1, 16, 0,
                                        16, 0, 16, 0, 16, 0),
                        new SqlRowTO("Q5", "update COMPANY set name=?", 131.00, 42.00, 89.00, 1, 1,
                                        42, 89, 42, 89, 42, 89, 42, 89) };
        toReturn.setSqlRowsByExecutionTime(sqlRowsByExecutionTime);

        // setting Top 5 SQL Queries by Execution Counts table information
        SqlRowTO[] sqlRowsByExecutionCount = new SqlRowTO[] {
                        new SqlRowTO("Q6", "update COMPANY set name=?", 131.00, 42.00, 89.00, 1, 1,
                                        42, 89, 42, 89, 42, 89, 42, 89),
                        new SqlRowTO("Q7", "delete from COMPANY", 16.00, 16.00, 0.00, 1, 1, 16, 0,
                                        16, 0, 16, 0, 16, 0)

        };
        toReturn.setSqlRowsByExecutionCount(sqlRowsByExecutionCount);

        GeneralInformationRowTO[] allSqlRows = new GeneralInformationRowTO[] {
                        new GeneralInformationRowTO("Q1", "update COMPANY set name=?", 12, 11233,
                                        1312, 343, 123, 12, 12, 1323L, 45L, 67, 243, 123, 645),
                        new GeneralInformationRowTO("Q2", "delete from COMPANY", 121, 12133, 1132,
                                        323, 1243, 12, 12, 123L, 45L, 67, 243, 123, 645),
                        new GeneralInformationRowTO("Q3", "update COMPANY set name=?", 1232, 12133,
                                        1332, 313, 123, 152, 12, 123L, 45L, 67, 243, 123, 645),
                        new GeneralInformationRowTO("Q4", "delete from COMPANY", 132, 12333, 1321,
                                        343, 123, 122, 12, 123L, 435L, 67, 243, 123, 645),
                        new GeneralInformationRowTO("Q5", "update COMPANY set name=?", 1422, 14233,
                                        1532, 33, 1523, 152, 12, 123L, 45L, 67, 243, 123, 645),
                        new GeneralInformationRowTO("Q6", "update COMPANY set name=?", 152, 12323,
                                        1382, 363, 123, 12, 12, 1723L, 45L, 67, 243, 123, 645),
                        new GeneralInformationRowTO("Q7", "delete from COMPANY", 162, 1293, 1832,
                                        33, 123, 128, 12, 1923L, 45L, 67, 243, 123, 645),

        };
        toReturn.setAllSqlQueries(allSqlRows);

        Map<String, String> map = new HashMap<String, String>();
        map.put("Q1", SQLToHtml.convertToHtml("update COMPANY set name=?"));
        map.put("Q2", SQLToHtml.convertToHtml("delete from COMPANY"));
        map.put("Q3", SQLToHtml.convertToHtml("update COMPANY set name=?"));
        map.put("Q4", SQLToHtml.convertToHtml("delete from COMPANY"));
        map.put("Q5", SQLToHtml.convertToHtml("update COMPANY set name=?"));
        map.put("Q6", SQLToHtml.convertToHtml("update COMPANY set name=?"));
        map.put("Q7", SQLToHtml.convertToHtml("delete from COMPANY"));
        toReturn.setFormattedSql(map);
        // SqlRowTO[] allSqlRows= new SqlRowTO[]{
        // new SqlRowTO("Q1","update COMPANY set
        // name=?",131.00,42.00,89.00,1,1,42,89,42,89,42,89,42,89),
        // new SqlRowTO("Q2","delete from
        // COMPANY",16.00,16.00,0.00,1,1,16,0,16,0,16,0,16,0),
        // new SqlRowTO("Q3","update COMPANY set
        // name=?",131.00,42.00,89.00,1,1,42,89,42,89,42,89,42,89),
        // new SqlRowTO("Q4","delete from
        // COMPANY",16.00,16.00,0.00,1,1,16,0,16,0,16,0,16,0),
        // new SqlRowTO("Q3","update COMPANY set
        // name=?",131.00,42.00,89.00,1,1,42,89,42,89,42,89,42,89),
        // new SqlRowTO("Q4","delete from
        // COMPANY",16.00,16.00,0.00,1,1,16,0,16,0,16,0,16,0),
        // new SqlRowTO("Q5","update COMPANY set
        // name=?",131.00,42.00,89.00,1,1,42,89,42,89,42,89,42,89)
        // };

        // graphics for "by time"

        GraphicUtil.getSqlViewDataImages(toReturn, getThreadLocalRequest().getSession().getId(),
                        getProtHostPortContext());

        toReturn.setImgUrlByCount(getFakeImage("images/tmp/diagram.gif"));
        toReturn.setImgUrlByTime(getFakeImage("images/tmp/diagram2.gif"));

        toReturn.setResultStatus(new ResultStatus(Constants.SUCCESS_CODE));
        toReturn.setNavigatorEntryTO(navigatorEntryTO);
        return toReturn;
    }

    /**
     * @return actual applications with instances
     * @see SIRServiceImpl#getApplicationConfigTO()
     */
    @Override
    public ApplicationConfigTO getApplicationConfigTO() {
        // getFailure();
        if (isRealMode)
            return super.getApplicationConfigTO();

        ApplicationConfigTO toReturn = new ApplicationConfigTO();
        // applicationConfigTO.setStartDate("12/11/2007");
        // applicationConfigTO.setEndDate("12/12/2007");
        toReturn.setLiveDate(false);
        toReturn
                        .setApplicationList(new ApplicationEntryTO[] {
                                        new ApplicationEntryTO(
                                                        "Application1",
                                                        new InstanceEntryTO[] {
                                                                        new InstanceEntryTO(
                                                                                        "instance1"),
                                                                        new InstanceEntryTO(
                                                                                        "instance2") }),
                                        new ApplicationEntryTO(
                                                        "Application2",
                                                        new InstanceEntryTO[] {
                                                                        new InstanceEntryTO(
                                                                                        "instance1"),
                                                                        new InstanceEntryTO(
                                                                                        "instance2") }),
                                        new ApplicationEntryTO(
                                                        "Application3",
                                                        new InstanceEntryTO[] { new InstanceEntryTO(
                                                                        "instance1") }) });
        toReturn.setResultStatus(new ResultStatus(Constants.SUCCESS_CODE));
        return toReturn;
    }

    /**
     * generates report on server
     * 
     * @param applConfig an application config from client
     * @param reportConfig a <code>ReportConfigTO</code
     * @param otherOperationsName a <code>String</code,
     * name for operations that will be put into other group
     * @return generated report
     *
     * @see net.sf.infrared2.gwt.client.service.ISIRService#generateReport(net.sf.infrared2.gwt.client.to.ApplicationConfigTO, net.sf.infrared2.gwt.client.report.ReportConfigTO, String)
     *
     * @see SIRServiceImpl#generateReport(ApplicationConfigTO, ReportConfigTO, String)
     */
    public ReportResultTO generateReport(ApplicationConfigTO applConfig,
                    ReportConfigTO reportConfig, String otherOperationsName) {
        if (isRealMode)
            return super.generateReport(applConfig, reportConfig, otherOperationsName);
        return new ReportResultTO(new ResultStatus(Constants.SUCCESS_CODE));
    }

    /**
     * @see SIRServiceImpl#getFailure() Having this method added to any of
     *      SIRService methods anyone could test onFailure() behave on
     *      client-side
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

        if (isRealMode)
            return super.getAppVersion();

        ApplicationVersionTO toReturn = new ApplicationVersionTO();
        toReturn.setVersion("Stub mode version");
        toReturn.setResultStatus(new ResultStatus(Constants.SUCCESS_CODE));
        return toReturn;
    }

    /**
     * Method setRealMode sets the realMode of this SIRServiceStubImpl object.
     * 
     * @param realMode the realMode of this SIRServiceStubImpl object.
     */
    public static void setRealMode(boolean realMode) {
        isRealMode = realMode;
    }
}
