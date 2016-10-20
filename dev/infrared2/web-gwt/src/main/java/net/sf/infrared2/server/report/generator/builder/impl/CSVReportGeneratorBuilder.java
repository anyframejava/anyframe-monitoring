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
 * ReportAdapter.java		Date created: 17.02.2008
 * Last modified by: $Author: soyon.lim $
 * $Revision: 14286 $	$Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.server.report.generator.builder.impl;

import net.sf.infrared2.gwt.client.report.ReportConfigTO;
import net.sf.infrared2.gwt.client.to.ApplicationEntryTO;
import net.sf.infrared2.gwt.client.to.InstanceEntryTO;
import net.sf.infrared2.gwt.client.to.NavigatorEntryTO;
import net.sf.infrared2.gwt.client.to.application.ApplicationViewTO;
import net.sf.infrared2.gwt.client.to.other.OtherViewTO;
import net.sf.infrared2.gwt.client.to.sql.SqlViewTO;
import net.sf.infrared2.server.adapter.Adapter;
import net.sf.infrared2.server.report.ReportConstants;
import net.sf.infrared2.server.report.generator.adapter.ReportAdapter;
import net.sf.infrared2.server.report.generator.builder.AbstractGeneratorBuilder;
import net.sf.infrared2.server.report.generator.impl.csv.StringArrayToCSVWriter;
import net.sf.infrared2.server.util.SqlStatistics;
import net.sf.infrared2.server.util.ViewUtil;

/**
 * <b>CSVReportGeneratorBuilder</b><p>
 * Class CSVReportGeneratorBuilder build csv report.
 *
 * @author gzgonikov
 */
public class CSVReportGeneratorBuilder extends AbstractGeneratorBuilder {

    /** Field csvWriter  */
    private StringArrayToCSVWriter csvWriter;

    /**
     * Constructor CSVReportGeneratorBuilder creates a new CSVReportGeneratorBuilder instance.
     *
     * @param reportConfigTO of type ReportConfigTO
     */
    protected CSVReportGeneratorBuilder(ReportConfigTO reportConfigTO) {
        super(reportConfigTO);
    }

    /**
     * Constructor CSVReportGeneratorBuilder creates a new CSVReportGeneratorBuilder instance.
     *
     * @param reportConfigTO of type ReportConfigTO
     * @param csvWriter of type StringArrayToCSVWriter
     */
    public CSVReportGeneratorBuilder(ReportConfigTO reportConfigTO, StringArrayToCSVWriter csvWriter){
        super(reportConfigTO);
        this.csvWriter = csvWriter;
    }
    /**
     * Method createOtherLayer generate content for other layer.
     *
     * @param otherViewTO of type OtherViewTO
     * @param isInclusive of type boolean
     * @throws Exception when some problems occured
     *
     * @see net.sf.infrared2.server.report.generator.builder.AbstractGeneratorBuilder#createOtherLayer(OtherViewTO, boolean)
     */
    @Override
    protected void createOtherLayer(OtherViewTO otherViewTO, boolean isInclusive) throws Exception{
        csvWriter.writeln(otherViewTO.getNavigatorEntryTO().getTitle() +
                ((isInclusive)? ReportConstants.LAYER_LEVEL_INCLUSIVE:ReportConstants.LAYER_LEVEL_EXCLUSIVE));
        csvWriter.writeln(ReportConstants.STATISTIC);

        createOtherLayerTableHeader(ReportConstants.OPERATIONS_BY_TOTAL_TIME);
        csvWriter.writeln(ReportAdapter.adaptSummaryRowTOToArray(
                ViewUtil.getTopNOperationsByParam((isInclusive)?otherViewTO.getInclusiveTableRows():
                        otherViewTO.getExclusiveTableRows(),
                        Integer.parseInt(getReportConfigTO().getQueries()),"getTotalTime"), false, false));
        csvWriter.writeln();

        createOtherLayerTableHeader(ReportConstants.OPERATIONS_BY_COUNT);
        csvWriter.writeln(ReportAdapter.adaptSummaryRowTOToArray(
                ViewUtil.getTopNOperationsByParam((isInclusive)?otherViewTO.getInclusiveTableRows():
                        otherViewTO.getExclusiveTableRows(),
                        Integer.parseInt(getReportConfigTO().getQueries()),"getCount"), false, false));
        csvWriter.writeln();
    }

    /**
     * Method createOtherLayerTableHeader create header for tables in other layer with title.
     *
     * @param labelOperations of type String
     * @throws Exception when some problems occured
     */
    private void createOtherLayerTableHeader(String labelOperations) throws Exception {
        csvWriter.writeln(ReportConstants.TOP+" "+getReportConfigTO().getQueries()+" "+labelOperations);
        createOtherLayerTableHeader();
    }

    /**
     * Method createOtherLayerTableHeader create header for tables in other layer.
     * @throws Exception when some problems occured
     */
    private void createOtherLayerTableHeader() throws Exception {
        csvWriter.writeln(new String[]{
                ReportConstants.OPERATION_NAME,
                ReportConstants.TOTAL_TIME,
                ReportConstants.COUNT,
                ReportConstants.AVERAGE_TIME,
                ReportConstants.ADJUSTED_AVERAGE_TIME,
                ReportConstants.MINIMUM_EXECUTION_TIME,
                ReportConstants.MAXIMUM_EXECUTION_TIME,
                ReportConstants.FIRST_EXECUTION_TIME,
                ReportConstants.LAST_EXECUTION_TIME
        });
    }

    /**
     * Method createSqlLayer generate data for sql layer.
     *
     * @param sqlViewTO of type SqlViewTO
     * @throws Exception when some problems occured.
     */
    @Override
    protected void createSqlLayer(SqlViewTO sqlViewTO) throws Exception{

        csvWriter.writeln(ReportConstants.SQL);    
        csvWriter.writeln(ReportConstants.STATISTIC);
        createSqlTableHeader(ReportConstants.QUERIES_AVERAGE_EXECUTE_TIME);
        SqlStatistics[] toNQueriesByTime = ViewUtil.getTopNQueriesByExecutionTime(Adapter.getSqlStatisticsArray(sqlViewTO.getAllSqlStatistics()),
                Integer.parseInt(getReportConfigTO().getQueries()));
        csvWriter.writeln(ReportAdapter.adaptSqlViewTOArray(toNQueriesByTime));
        csvWriter.writeln();

        createSqlTableHeader(ReportConstants.QUERIES_EXECUTION_COUNT);
        SqlStatistics[] toNQueriesByCount = ViewUtil.getTopNQueriesByCount(Adapter.getSqlStatisticsArray(sqlViewTO.getAllSqlStatistics()),
                Integer.parseInt(getReportConfigTO().getQueries()));
        csvWriter.writeln(ReportAdapter.adaptSqlViewTOArray(toNQueriesByCount));
        csvWriter.writeln();
    }

    /**
     * Method createSqlTableHeader creates header for sql tables with title.
     *
     * @param labelType of type String
     * @throws Exception when
     */
    private void createSqlTableHeader(String labelType) throws Exception {
        csvWriter.writeln(ReportConstants.TOP+" "+getReportConfigTO().getQueries()+" "+labelType);
        createSqlTableHeader();
    }

    /**
     * Method createSqlTableHeader creates header for sql tables.
     * @throws Exception when
     */
    private void createSqlTableHeader() throws Exception {
        csvWriter.writeln(new String[]{
                ReportConstants.SQL_QUERY,
                ReportConstants.ID,
                ReportConstants.AVERAGE_TIME_TOTAL,
                ReportConstants.AVERAGE_TIME_EXECUTE,
                ReportConstants.AVERAGE_TIME_PREPARE,
                ReportConstants.COUNT_EXECUTE,
                ReportConstants.COUNT_PREPARE,
                ReportConstants.MAX_TIME_EXECUTE,
                ReportConstants.MAX_TIME_PREPARE,
                ReportConstants.MIX_TIME_EXECUTE,
                ReportConstants.MIX_TIME_PREPARE,
                ReportConstants.FIRST_EXEC_TIME_EXECUTE,
                ReportConstants.FIRST_EXEC_TIME_PREPARE,
                ReportConstants.LAST_EXEC_TIME_EXECUTE,
                ReportConstants.LAST_EXEC_TIME_PREPARE
        });
    }

    /**
     * Method createApplicationLayer generate data for application layer.
     *
     * @param applicationViewTO of type ApplicationViewTO
     * @throws Exception when some problem occured.
     */
    @Override
    protected void createApplicationLayer(ApplicationViewTO applicationViewTO) throws Exception{

        csvWriter.writeln(ReportConstants.APPLICATION_LEVEL);
        csvWriter.writeln(ReportConstants.STATISTIC);
        createApplicationLayerTableHeader();
        csvWriter.writeln(ReportAdapter.adaptApplicationViewTOArray(applicationViewTO, false));
        csvWriter.writeln();
    }

    /**
     * Method createApplicationLayerTableHeader creates header for table in application layer with title.
     * @throws Exception when
     */
    private void createApplicationLayerTableHeader() throws Exception {
        csvWriter.writeln(new String[]{ReportConstants.LAYER, ReportConstants.TOTAL_TIME, ReportConstants.COUNT});    
    }

    /**
     * Method createApplicationInstanceHeader creates application(s) title
     *
     * @param navigatorEntryTO of type NavigatorEntryTO
     * @throws Exception when some problem occured.
     */
    @Override
    protected void createApplicationInstanceHeader(NavigatorEntryTO navigatorEntryTO) throws Exception{

        if (getReportConfigTO().getMergedApplicationTitle()!=null) {
            csvWriter.writeln(ReportConstants.MERGED_FOR+getReportConfigTO().getMergedApplicationTitle());
            return;
        }
        ApplicationEntryTO applications = navigatorEntryTO.getApplications();
        if (applications != null) {
            String applicationTitle = applications.getApplicationTitle();

            if (applications.getInstanceList() != null && applications.getInstanceList().length > 0) {

                final InstanceEntryTO instance = applications.getInstanceList()[0];
                if (instance != null) {
                    applicationTitle += ", " + instance.getName();
                }
            }
        csvWriter.writeln(applicationTitle);
        }
    }

    /**
     * Method createModuleTitle crates title for module.
     *
     * @param qualifiedModuleName of type String
     * @throws Exception when some problem occured.
     */
    @Override
    protected void createModuleTitle(String qualifiedModuleName) throws Exception{
        csvWriter.writeln(qualifiedModuleName);
    }
}
