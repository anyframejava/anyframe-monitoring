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
 * HtmlReportGenerationBuilder.java		Date created: 19.04.2008
 * Last modified by: $Author: soyon.lim $
 * $Revision: 14286 $	$Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.server.report.generator.builder.impl;

import com.lowagie.text.*;
import com.lowagie.text.html.HtmlWriter;

import java.awt.*;
import java.io.IOException;

import net.sf.infrared2.gwt.client.report.ReportConfigTO;
import net.sf.infrared2.gwt.client.to.ApplicationEntryTO;
import net.sf.infrared2.gwt.client.to.InstanceEntryTO;
import net.sf.infrared2.gwt.client.to.NavigatorEntryTO;
import net.sf.infrared2.gwt.client.to.application.ApplicationViewTO;
import net.sf.infrared2.gwt.client.to.other.OtherViewTO;
import net.sf.infrared2.gwt.client.to.other.SummaryRowTO;
import net.sf.infrared2.gwt.client.to.sql.SqlViewTO;
import net.sf.infrared2.server.adapter.Adapter;
import net.sf.infrared2.server.report.ReportConstants;
import net.sf.infrared2.server.report.generator.adapter.ReportAdapter;
import net.sf.infrared2.server.util.SqlStatistics;
import net.sf.infrared2.server.util.ViewUtil;

/**
 * <b>HtmlReportGenerationBuilder</b>
 * <p>
 * Builder class for HTML reports based on IText library.
 * 
 * @author Sergey Evluhin<br>
 * Copyright Exadel Inc, 2008
 */
public class HtmlReportGenerationBuilder extends AbstractITextReportBuilder {

    private HtmlWriter writer;

    /**
     * Creates builder.
     * 
     * @param doc - report document. It must be opened (doc.open() method
     *            called).
     * @param reportConfigTO a <code>ReportConfigTO</code>-transfer object hold all settings for report
     * @param writer
     */
    public HtmlReportGenerationBuilder(Document doc, ReportConfigTO reportConfigTO, HtmlWriter writer) {
        super(reportConfigTO);
        document = doc;
        this.writer = writer;
    }

    /**
     * Creates header of for application, instance and module name
     * 
     * @param to - transfer object, contains module type, application title and
     *            instance title.
     * @throws DocumentException
     * @see net.sf.infrared2.server.report.generator.builder.AbstractGeneratorBuilder#createApplicationInstanceHeader(NavigatorEntryTO)
     */
    @Override
    protected void createApplicationInstanceHeader(NavigatorEntryTO to) throws DocumentException {
        String applicationTitle = null;
        if (getReportConfigTO().getMergedApplicationTitle() != null) {
            applicationTitle = ReportConstants.MERGED_FOR
                            + getReportConfigTO().getMergedApplicationTitle();
        } else {
            final ApplicationEntryTO applications = to.getApplications();
            if (applications != null) {
                applicationTitle = applications.getApplicationTitle();

                if (applications.getInstanceList() != null
                                && applications.getInstanceList().length > 0) {

                    final InstanceEntryTO instance = applications.getInstanceList()[0];
                    if (instance != null) {
                        applicationTitle += ", " + instance.getName();
                    }
                }
            }
        }
        Paragraph sTitle = new Paragraph(applicationTitle, SECTION_FONT);
        document.add(sTitle);
    }

    /**
     * Creates report for application layer
     * 
     * @param to - transfer object for application layer view.
     * @throws DocumentException
     * @throws IOException- throws when images URLs are not accessible or image
     *             format not recognized.
     * @see net.sf.infrared2.server.report.generator.builder.AbstractGeneratorBuilder#createApplicationLayer(ApplicationViewTO)
     */
    @Override
    protected void createApplicationLayer(ApplicationViewTO to) throws DocumentException,
                    IOException {
        Paragraph paragraph1 = new Paragraph(ReportConstants.APPLICATION_LEVEL, PARAGRAPH_FONT);
        document.add(paragraph1);

        // view statistic is checked
        if (getReportConfigTO().isStatistics()) {

            Paragraph statistic = new Paragraph(ReportConstants.STATISTIC, TEXT_FONT);
            document.add(statistic);

            // statistic table
            Table t = new Table(3, 2);
            t.setCellsFitPage(true);
            t.setPadding(5);

            Cell cell;
            Chunk chunk;

            chunk = new Chunk(ReportConstants.LAYER, TABLE_HEADER_FONT);

            cell = getCell(chunk);
            t.addCell(cell);

            chunk = new Chunk(ReportConstants.TOTAL_TIME, TABLE_HEADER_FONT);
            cell = getCell(chunk);
            t.addCell(cell);

            chunk = new Chunk(ReportConstants.COUNT, TABLE_HEADER_FONT);
            cell = getCell(chunk);
            t.addCell(cell);

            // statistic table body
            final Object[][] rows = ApplicationViewTO.convertApplicationRowTOWithoutColors(to
                            .getRows());

            for (int i = 0; i < rows.length; i++) {
                Object[] objects = rows[i];
                for (int j = 0; j < objects.length; j++) {
                    Object object = objects[j];
                    String value = normalizeValue(object);
                    chunk = new Chunk(value, TEXT_FONT);
                    cell = getCell(chunk);
                    t.addCell(cell);
                }
            }
            document.add(t);

        }
        // view diagrams is selected
        if (getReportConfigTO().isDiagrams()) {

            Paragraph diagrams = new Paragraph(ReportConstants.DIAGRAMS, TEXT_FONT);
            document.add(diagrams);

            Paragraph layersByTotalTime = new Paragraph(ReportConstants.LAYERS_BY_TOTAL_TIME,
                            TEXT_FONT);
            document.add(layersByTotalTime);

//            Image im1 = null;
            final String url1 = (String) to.getImgByTimeUrl().keySet().iterator().next();
            writer.add((String) to.getImgByTimeUrl().get(url1));

//            im1 = getImage(url1);

//            document.add(im1);

            Paragraph layersByCount = new Paragraph(ReportConstants.LAYERS_BY_COUNT, TEXT_FONT);
            document.add(layersByCount);

            final String url2 = (String) to.getImgByCountUrl().keySet().iterator().next();
            writer.add((String) to.getImgByCountUrl().get(url2));
//            Image im2 = getImage(url2);

//            im1 = getImage(url2);

//            document.add(im2);

        }

        newPage();
    }

    /**
     * Creates title of the module
     * 
     * @param title - title of the module
     * @throws DocumentException
     * @see net.sf.infrared2.server.report.generator.builder.AbstractGeneratorBuilder#createModuleTitle(String)
     */
    @Override
    protected void createModuleTitle(String title) throws DocumentException {
        // Absolute layer
        Paragraph cTitle = new Paragraph(title, CHAPTER_FONT);
        Chapter chapter = new Chapter(cTitle, 1);
        document.add(chapter);
    }

    /**
     * Create report for layers, different to application and SQL.
     * 
     * @param to - transfer object for layer view.
     * @param inclusive - indicates when must be used inclusive or exclusive
     *            data.
     * @throws DocumentException
     * @throws IOException - throws when images URLs are not accessible.
     * @see net.sf.infrared2.server.report.generator.builder.AbstractGeneratorBuilder#createOtherLayer(OtherViewTO,
     *      boolean)
     */
    @Override
    protected void createOtherLayer(OtherViewTO to, boolean inclusive) throws DocumentException,
                    IOException {
        String title = to.getNavigatorEntryTO().getTitle();
        if (inclusive) {
            title += " (" + ReportConstants.INCLUSIVE + ")";
        } else {
            title += " (" + ReportConstants.EXCLUSIVE + ")";
        }
        Paragraph paragraph1 = new Paragraph(title, PARAGRAPH_FONT);
        document.add(paragraph1);

        // view statistic is checked
        if (getReportConfigTO().isStatistics()) {
            Paragraph statistic = new Paragraph(ReportConstants.STATISTIC, TEXT_FONT);
            document.add(statistic);

            Paragraph topTotalTime = new Paragraph(ReportConstants.TOP + " "
                            + getReportConfigTO().getQueries() + " "
                            + ReportConstants.OPERATIONS_BY_TOTAL_TIME, TEXT_FONT);
            document.add(topTotalTime);

            // statistic table
            Table tableByTime = createOtherTableHeader(ReportConstants.TIME_COLOR);

            final SummaryRowTO[] tableRows;
            if (inclusive)
                tableRows = to.getInclusiveTableRows();
            else
                tableRows = to.getExclusiveTableRows();

            // statistic table body
            Object[][] rowsByTime = ReportAdapter.adaptSummaryRowTOToArray(ViewUtil
                            .getTopNOperationsByParam(tableRows, Integer
                                            .parseInt(getReportConfigTO().getQueries()),
                                            "getTotalTime"), true, true);

            Chunk chunk;
            Cell cell;
            for (int i = 0; i < rowsByTime.length; i++) {
                Object[] objects = rowsByTime[i];
                for (int j = 0; j < objects.length; j++) {
                    Object object = objects[j];
                    if (j == 0) {
                        chunk = new Chunk("");
                        cell = getCell(chunk);
                        cell.setBackgroundColor(new Color(Integer.valueOf(object.toString(), 16)));
                        tableByTime.addCell(cell);
                        continue;
                    }
                    String value = normalizeValue(object);
                    chunk = new Chunk(value, TEXT_FONT);
                    cell = getCell(chunk);

                    tableByTime.addCell(cell);
                }
            }
            // tableByTime.setWidth(100);
            document.add(tableByTime);

            Paragraph topByCount = new Paragraph(ReportConstants.TOP + " "
                            + getReportConfigTO().getQueries() + " "
                            + ReportConstants.OPERATIONS_BY_COUNT, TEXT_FONT);
            document.add(topByCount);

            // statistic table
            Table tableByCount = createOtherTableHeader(ReportConstants.COUNT_COLOR);

            // statistic table body
            Object[][] rowsByCount = ReportAdapter.adaptSummaryRowTOToArray(
                            ViewUtil
                                            .getTopNOperationsByParam(tableRows, Integer
                                                            .parseInt(getReportConfigTO()
                                                                            .getQueries()),
                                                            "getCount"), true, false);

            for (int i = 0; i < rowsByCount.length; i++) {
                Object[] objects = rowsByCount[i];
                for (int j = 0; j < objects.length; j++) {
                    Object object = objects[j];
                    if (j == 0) {
                        chunk = new Chunk("");
                        cell = getCell(chunk);
                        cell.setBackgroundColor(new Color(Integer.valueOf(object.toString(), 16)));
                        tableByCount.addCell(cell);
                        continue;
                    }
                    String value = normalizeValue(object);
                    chunk = new Chunk(value, TEXT_FONT);
                    cell = getCell(chunk);
                    tableByCount.addCell(cell);
                }
            }
            // tableByCount.setWidth(100);
            document.add(tableByCount);

        }

        // view diagrams is checked
        if (getReportConfigTO().isDiagrams()) {
            if (getReportConfigTO().isStatistics()) {
                newPage();
            }
            Paragraph diagrams = new Paragraph(ReportConstants.DIAGRAMS, TEXT_FONT);
            document.add(diagrams);

            Paragraph opByTotalTime = new Paragraph(ReportConstants.OPERATIONS_BY_TOTAL_TIME,
                            TEXT_FONT);
            document.add(opByTotalTime);

//            Image im1;
            if (inclusive) {
                final String url1 = (String) to.getImgInclusiveOperationByTimeUrl().keySet()
                                .iterator().next();
                writer.add((String) to.getImgInclusiveOperationByTimeUrl().get(url1));
//                im1 = getImage(url1);

            } else {
                final String url2 = (String) to.getImgExclusiveOperationByTimeUrl().keySet()
                                .iterator().next();
                writer.add((String) to.getImgExclusiveOperationByTimeUrl().get(url2));
//                im1 = getImage(url2);
            }

//            document.add(im1);

            Paragraph opByCount = new Paragraph(ReportConstants.OPERATIONS_BY_COUNT, TEXT_FONT);
            document.add(opByCount);

//            Image im2;
            if (inclusive) {
                final String url3 = (String) to.getImgInclusiveOperationByCountUrl().keySet()
                                .iterator().next();
                writer.add((String) to.getImgInclusiveOperationByCountUrl().get(url3));

//                im2 = getImage(url3);
            } else {
                final String url4 = (String) to.getImgExclusiveOperationByCountUrl().keySet()
                                .iterator().next();
                writer.add((String) to.getImgExclusiveOperationByCountUrl().get(url4));
//                im2 = getImage(url4);
            }
//            document.add(im2);

        }
        newPage();

    }

    /**
     * Add header "Layer Level - SQL layers"
     * 
     * @param to - transfer object for SQL layer view.
     * @throws DocumentException
     * @throws IOException - throws when images URLs are not accessible.
     * @see net.sf.infrared2.server.report.generator.builder.AbstractGeneratorBuilder#createSqlLayer(SqlViewTO)
     */
    @Override
    protected void createSqlLayer(SqlViewTO to) throws DocumentException, IOException {
        Paragraph paragraph1 = new Paragraph(to.getNavigatorEntryTO().getTitle(), PARAGRAPH_FONT);
        document.add(paragraph1);
        // view statistic is selected
        if (getReportConfigTO().isStatistics()) {
            Paragraph statistic = new Paragraph(ReportConstants.STATISTIC, TEXT_FONT);
            document.add(statistic);

            Paragraph topByTime = new Paragraph(ReportConstants.TOP + " "
                            + getReportConfigTO().getQueries() + " "
                            + ReportConstants.QUERIES_AVERAGE_EXECUTE_TIME, TEXT_FONT);
            document.add(topByTime);

            // statistic table
            Table tableByTime = createSqlTableHeader();
            SqlStatistics[] toNQueriesByTime = ViewUtil.getTopNQueriesByExecutionTime(Adapter
                            .getSqlStatisticsArray(to.getAllSqlStatistics()), Integer
                            .parseInt(getReportConfigTO().getQueries()));
            Object[][] rowsByTime = ReportAdapter.adaptSqlViewTOArray(toNQueriesByTime);
            // statistic table body
            Chunk chunk;
            Cell cell;
            for (int i = 0; i < rowsByTime.length; i++) {
                Object[] objects = rowsByTime[i];
                for (int j = 0; j < objects.length; j++) {
                    Object object = objects[j];
                    String value = normalizeValue(object);
                    chunk = new Chunk(value, TEXT_FONT);
                    cell = getCell(chunk);
                    if (j == 0) {
                        cell.setHorizontalAlignment(Cell.ALIGN_LEFT);
                    }
                    tableByTime.addCell(cell);
                }
            }
            // tableByTime.setWidth(100);

            document.add(tableByTime);

            Paragraph topByCount = new Paragraph(ReportConstants.TOP + " "
                            + getReportConfigTO().getQueries() + " "
                            + ReportConstants.QUERIES_EXECUTION_COUNT, TEXT_FONT);
            document.add(topByCount);

            // statistic table
            Table tableByCount = createSqlTableHeader();

            SqlStatistics[] toNQueriesByCount = ViewUtil.getTopNQueriesByCount(Adapter
                            .getSqlStatisticsArray(to.getAllSqlStatistics()), Integer
                            .parseInt(getReportConfigTO().getQueries()));
            Object[][] rowsByCount = ReportAdapter.adaptSqlViewTOArray(toNQueriesByCount);

            // statistic table body
            // final Object[][] rowsByCount =
            // SqlViewTO.convertSqlRows(to.getSqlRowsByExecutionCount());
            for (int i = 0; i < rowsByCount.length; i++) {
                Object[] objects = rowsByCount[i];
                for (int j = 0; j < objects.length; j++) {
                    Object object = objects[j];
                    String value = normalizeValue(object);
                    chunk = new Chunk(value, TEXT_FONT);
                    cell = getCell(chunk);
                    if (j == 0) {
                        cell.setHorizontalAlignment(Cell.ALIGN_LEFT);
                    }
                    tableByCount.addCell(cell);
                }
            }
            // tableByCount.setWidth(100);
            document.add(tableByCount);
        }

        // view diagrams is selected
        if (getReportConfigTO().isDiagrams()) {
            if (getReportConfigTO().isStatistics()) {
                newPage();
            }

            Paragraph diagrams = new Paragraph(ReportConstants.DIAGRAMS, TEXT_FONT);
            document.add(diagrams);

            Paragraph graphicByExTimeNote = new Paragraph(ReportConstants.GRAPHIC_FOR_5_QUERIES,
                            TEXT_FONT);
            document.add(graphicByExTimeNote);

            Paragraph queriesByExTime = new Paragraph(
                            ReportConstants.QUERIES_BY_AVERAGE_EXECUTION_TIME, TEXT_FONT);
            document.add(queriesByExTime);

            final String url1 = (String) to.getImgUrlByTime().keySet().iterator().next();
            writer.add((String) to.getImgUrlByTime().get(url1));
//            Image im1 = null;
//            im1 = getImage(url1);

//            document.add(im1);

            Paragraph quriesByExCount = new Paragraph(ReportConstants.QUERIES_BY_EXECUTION_COUNT,
                            TEXT_FONT);
            document.add(quriesByExCount);

            Paragraph graphicByExCountNote = new Paragraph(ReportConstants.GRAPHIC_FOR_5_QUERIES,
                            TEXT_FONT);
            document.add(graphicByExCountNote);

            final String url2 = (String) to.getImgUrlByCount().keySet().iterator().next();
            writer.add((String) to.getImgUrlByCount().get(url2));
//            Image im2 = null;
//            im2 = getImage(url2);
//            document.add(im2);

        }
        newPage();
    }

    /**
     * Creates table headers without data for Other layers.
     * 
     * @param colorTitle - title for header.
     * @return Table object
     * @throws BadElementException when some problem appear.
     */
    private Table createOtherTableHeader(String colorTitle) throws BadElementException {
        Table t = new Table(10);
        t.setCellsFitPage(true);
        t.setPadding(2);
        t.setWidth(100);

        Chunk colorChunk = new Chunk(colorTitle, TABLE_HEADER_FONT);
        Cell colorCell = getHeaderCell(colorChunk);
        // opNameCell.setWidth(80);
        t.addCell(colorCell);

        Chunk opNameChunk = new Chunk(ReportConstants.OPERATION_NAME, TABLE_HEADER_FONT);
        Cell opNameCell = getHeaderCell(opNameChunk);
        // opNameCell.setWidth(80);
        t.addCell(opNameCell);

        Chunk totalTimeChunk = new Chunk(ReportConstants.TOTAL_TIME, TABLE_HEADER_FONT);
        Cell totalTimeCell = getHeaderCell(totalTimeChunk);
        t.addCell(totalTimeCell);

        Chunk countChunk = new Chunk(ReportConstants.COUNT, TABLE_HEADER_FONT);
        Cell countCell = getHeaderCell(countChunk);
        t.addCell(countCell);

        Chunk avgTimeChunk = new Chunk(ReportConstants.AVERAGE_TIME, TABLE_HEADER_FONT);
        Cell avgTimeCell = getHeaderCell(avgTimeChunk);
        t.addCell(avgTimeCell);

        Chunk adjAvgTimeChunk = new Chunk(ReportConstants.ADJUSTED_AVERAGE_TIME, TABLE_HEADER_FONT);
        Cell adjAvgTimeCell = getHeaderCell(adjAvgTimeChunk);
        t.addCell(adjAvgTimeCell);

        Chunk minExTimeChunk = new Chunk(ReportConstants.MINIMUM_EXECUTION_TIME, TABLE_HEADER_FONT);
        Cell minExTimeCell = getHeaderCell(minExTimeChunk);
        t.addCell(minExTimeCell);

        Chunk maxExTimeChunk = new Chunk(ReportConstants.MAXIMUM_EXECUTION_TIME, TABLE_HEADER_FONT);
        Cell maxExTimeCell = getHeaderCell(maxExTimeChunk);
        t.addCell(maxExTimeCell);

        Chunk firstExTimeChunk = new Chunk(ReportConstants.FIRST_EXECUTION_TIME, TABLE_HEADER_FONT);
        Cell firstExTimeCell = getHeaderCell(firstExTimeChunk);
        t.addCell(firstExTimeCell);

        Chunk lastExTimeChunk = new Chunk(ReportConstants.LAST_EXECUTION_TIME, TABLE_HEADER_FONT);
        Cell lastExTimeCell = getHeaderCell(lastExTimeChunk);
        t.addCell(lastExTimeCell);

        return t;
    }

    /**
     * Creates table headers without data for SQL layers.
     * 
     * @return Table object
     * @throws BadElementException when some problem appear.
     */
    private Table createSqlTableHeader() throws BadElementException {
        Table t = new Table(15, 2);
        t.setCellsFitPage(true);
        t.setPadding(2);
        t.setWidth(100);

        Chunk queryChunk = new Chunk(ReportConstants.SQL_QUERY, TABLE_HEADER_FONT);
        Cell queryCell = getHeaderCell(queryChunk);
        // queryCell.setWidth(80);
        queryCell.setRowspan(2);
        t.addCell(queryCell);

        Chunk idChunk = new Chunk(ReportConstants.ID, TABLE_HEADER_FONT);
        Cell idCell = getHeaderCell(idChunk);
        idCell.setRowspan(2);
        t.addCell(idCell);

        Chunk avgTimeChunk = new Chunk(ReportConstants.AVERAGE_TIME, TABLE_HEADER_FONT);
        Cell avgTimeCell = getHeaderCell(avgTimeChunk);
        avgTimeCell.setColspan(3);
        t.addCell(avgTimeCell);

        Chunk countChunk = new Chunk(ReportConstants.COUNT, TABLE_HEADER_FONT);
        Cell countCell = getHeaderCell(countChunk);
        countCell.setColspan(2);
        t.addCell(countCell);

        Chunk maxTimeChunk = new Chunk(ReportConstants.MAXIMUM_TIME, TABLE_HEADER_FONT);
        Cell maxTimeCell = getHeaderCell(maxTimeChunk);
        maxTimeCell.setColspan(2);
        t.addCell(maxTimeCell);

        Chunk minTimeChunk = new Chunk(ReportConstants.MINIMUM_TIME, TABLE_HEADER_FONT);
        Cell minTimeCell = getHeaderCell(minTimeChunk);
        minTimeCell.setColspan(2);
        t.addCell(minTimeCell);

        Chunk firstExTimeChunk = new Chunk(ReportConstants.FIRST_EXECUTION_TIME, TABLE_HEADER_FONT);
        Cell firstExTimeCell = getHeaderCell(firstExTimeChunk);
        firstExTimeCell.setColspan(2);
        t.addCell(firstExTimeCell);

        Chunk lastExTimeChunk = new Chunk(ReportConstants.LAST_EXECUTION_TIME, TABLE_HEADER_FONT);
        Cell lastExTimeCell = getHeaderCell(lastExTimeChunk);
        lastExTimeCell.setColspan(2);
        t.addCell(lastExTimeCell);

        // Header level 2

        Chunk totalChunk = new Chunk(ReportConstants.TOTAL, TABLE_HEADER_FONT);
        Cell totalCell = getHeaderCell(totalChunk);
        t.addCell(totalCell);

        for (int i = 0; i < 6; i++) {
            Chunk executeChunk = new Chunk(ReportConstants.EXECUTE, TABLE_HEADER_FONT);
            Cell executeCell = getHeaderCell(executeChunk);
            t.addCell(executeCell);

            Chunk prepareChunk = new Chunk(ReportConstants.PREPARE, TABLE_HEADER_FONT);
            Cell prepareCell = getHeaderCell(prepareChunk);
            t.addCell(prepareCell);
        }

        return t;
    }
}
