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
 * PdfReportGenerationBuilder.java		Date created: 19.04.2008
 * Last modified by: $Author: soyon.lim $
 * $Revision: 14286 $	$Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.server.report.generator.builder.impl;

import java.awt.Color;
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

import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.Chapter;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

/**
 * <b>PdfReportGenerationBuilder</b>
 * <p>
 * Builder class for PDF reports based on IText library.
 * 
 * @author Sergey Evluhin<br>
 * Copyright Exadel Inc, 2008
 */
public class PdfReportGenerationBuilder extends AbstractITextReportBuilder {

    /**
     * Creates builder.
     * 
     * @param doc - report document. It must be opened (doc.open() method
     *            called).
     * @param reportConfigTO a <code>ReportConfigTO</code>-transfer object hold all settings for report
     */
    public PdfReportGenerationBuilder(Document doc, ReportConfigTO reportConfigTO) {
        super(reportConfigTO);
        this.document = doc;
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
            document.add(new Phrase(""));
            // statistic table
            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(30);
            table.getDefaultCell().setPadding(3);
            table.setHorizontalAlignment(PdfPTable.ALIGN_LEFT);
            PdfPCell cell;
            /** Create table header */
            cell = getPDFHeaderCell(ReportConstants.LAYER);
            table.addCell(cell);
            cell = getPDFHeaderCell(ReportConstants.TOTAL_TIME);
            table.addCell(cell);
            cell = getPDFHeaderCell(ReportConstants.COUNT);            
            table.addCell(cell);
            // statistic table body
            final Object[][] rows = ApplicationViewTO.convertApplicationRowTOWithoutColors(to
                            .getRows());
            for (int i = 0; i < rows.length; i++) {
                Object[] objects = rows[i];
                for (int j = 0; j < objects.length; j++) {
                    Object object = objects[j];
                    String value = normalizeValue(object);
                    cell = new PdfPCell(new Paragraph(value, TEXT_FONT));
                    cell.setHorizontalAlignment(Cell.ALIGN_LEFT);
                    table.addCell(cell);
                }
            }
            document.add(table);
            document.add(new Phrase(""));
        }
        // view diagrams is selected
        if (getReportConfigTO().isDiagrams()) {

            Paragraph diagrams = new Paragraph(ReportConstants.DIAGRAMS, TEXT_FONT);
            document.add(diagrams);

            Paragraph layersByTotalTime = new Paragraph(ReportConstants.LAYERS_BY_TOTAL_TIME,
                            TEXT_FONT);
            document.add(layersByTotalTime);

            Image im1 = null;
            final String url1 = (String) to.getImgByTimeUrl().keySet().iterator().next();
            im1 = getImage(url1);

            document.add(im1);

            Paragraph layersByCount = new Paragraph(ReportConstants.LAYERS_BY_COUNT, TEXT_FONT);
            document.add(layersByCount);

            final String url2 = (String) to.getImgByCountUrl().keySet().iterator().next();

            Image im2 = getImage(url2);

            im1 = getImage(url2);

            document.add(im2);

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
            document.add(new Phrase(""));            
            document.add(topTotalTime);
            document.add(new Phrase(""));
            // statistic table
            PdfPTable tableByTime = createOtherTableHeader(ReportConstants.TIME_COLOR);

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

            PdfPCell cell;
            for (int i = 0; i < rowsByTime.length; i++) {
                Object[] objects = rowsByTime[i];
                for (int j = 0; j < objects.length; j++) {
                    Object object = objects[j];
                    if (j == 0) {
                        cell = new PdfPCell();
                        cell.setBackgroundColor(new Color(Integer.valueOf(object.toString(), 16)));
                        tableByTime.addCell(cell);
                        continue;
                    }
                    String value = normalizeValue(object);
                    cell = new PdfPCell(new Paragraph(value, TEXT_FONT));
                    if(j==1){
                        cell.setHorizontalAlignment(Cell.ALIGN_LEFT);
                    }
                    else {
                        cell.setHorizontalAlignment(Cell.ALIGN_CENTER);
                    }
                    tableByTime.addCell(cell);
                }
            }
            // tableByTime.setWidth(100);
            document.add(tableByTime);

            Paragraph topByCount = new Paragraph(ReportConstants.TOP + " "
                            + getReportConfigTO().getQueries() + " "
                            + ReportConstants.OPERATIONS_BY_COUNT, TEXT_FONT);
            
            document.add(new Phrase(""));            
            document.add(topByCount);
            document.add(new Phrase(""));
            // statistic table
            PdfPTable tableByCount = createOtherTableHeader(ReportConstants.COUNT_COLOR);

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
                        cell = new PdfPCell();
                        cell.setBackgroundColor(new Color(Integer.valueOf(object.toString(), 16)));
                        tableByCount.addCell(cell);
                        continue;
                    }
                    String value = normalizeValue(object);
                    cell = new PdfPCell(new Paragraph(value, TEXT_FONT));
                    if(j==1){
                        cell.setHorizontalAlignment(Cell.ALIGN_LEFT);
                    }
                    else {
                        cell.setHorizontalAlignment(Cell.ALIGN_CENTER);
                    }
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

            Image im1;
            if (inclusive) {
                final String url1 = (String) to.getImgInclusiveOperationByTimeUrl().keySet()
                                .iterator().next();

                im1 = getImage(url1);

            } else {
                final String url2 = (String) to.getImgExclusiveOperationByTimeUrl().keySet()
                                .iterator().next();

                im1 = getImage(url2);
            }

            document.add(im1);

            Paragraph opByCount = new Paragraph(ReportConstants.OPERATIONS_BY_COUNT, TEXT_FONT);
            document.add(opByCount);

            Image im2;
            if (inclusive) {
                final String url3 = (String) to.getImgInclusiveOperationByCountUrl().keySet()
                                .iterator().next();

                im2 = getImage(url3);
            } else {
                final String url4 = (String) to.getImgExclusiveOperationByCountUrl().keySet()
                                .iterator().next();
                im2 = getImage(url4);
            }
            document.add(im2);

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
            
            document.add(new Phrase(""));
            
            // statistic table
            PdfPTable tableByTime = createSqlTableHeader();
            tableByTime.setWidthPercentage(100);
           SqlStatistics[] toNQueriesByTime = ViewUtil.getTopNQueriesByExecutionTime(Adapter
                            .getSqlStatisticsArray(to.getAllSqlStatistics()), Integer
                            .parseInt(getReportConfigTO().getQueries()));
            Object[][] rowsByTime = ReportAdapter.adaptSqlViewTOArray(toNQueriesByTime);
            
            // statistic table body
            PdfPCell cell;
            float[] widths = {25f, 3f, 4f, 4f, 4f, 6f, 6f, 6f, 6f, 6f, 6f, 6f, 6f, 6f, 6f};
            //body table 
            PdfPTable tableByTimeBody = new PdfPTable(widths);
            tableByTimeBody.setWidthPercentage(100);
            tableByTimeBody.getDefaultCell().setPadding(3); 
            
            for (int i = 0; i < rowsByTime.length; i++) {
                
                Object[] objects = rowsByTime[i];
                for (int j = 0; j < objects.length; j++) {
                    Object object = objects[j];
                    String value = normalizeValue(object);
                    cell = new PdfPCell(new Paragraph(value, TEXT_FONT));
                    if (j == 0) {
                        cell.setHorizontalAlignment(Cell.ALIGN_LEFT);
                    }
                    else {
                        cell.setHorizontalAlignment(Cell.ALIGN_CENTER);
                    }
                    tableByTimeBody.addCell(cell);
                }
            }
            // tableByTime.setWidth(100);
            document.add(tableByTime);
            document.add(tableByTimeBody);

            Paragraph topByCount = new Paragraph(ReportConstants.TOP + " "
                            + getReportConfigTO().getQueries() + " "
                            + ReportConstants.QUERIES_EXECUTION_COUNT, TEXT_FONT);
            document.add(topByCount);
            document.add(new Phrase(""));

            // statistic table
            PdfPTable tableByCount = createSqlTableHeader();
            tableByCount.setWidthPercentage(100);
            SqlStatistics[] toNQueriesByCount = ViewUtil.getTopNQueriesByCount(Adapter
                            .getSqlStatisticsArray(to.getAllSqlStatistics()), Integer
                            .parseInt(getReportConfigTO().getQueries()));
            Object[][] rowsByCount = ReportAdapter.adaptSqlViewTOArray(toNQueriesByCount);
            //body table 
            PdfPTable tableByCountBody = new PdfPTable(widths);
            tableByCountBody.setWidthPercentage(100);
            tableByCountBody.getDefaultCell().setPadding(3); 

            // statistic table body
            for (int i = 0; i < rowsByCount.length; i++) {
                Object[] objects = rowsByCount[i];
                for (int j = 0; j < objects.length; j++) {
                    Object object = objects[j];
                    String value = normalizeValue(object);
                    cell = new PdfPCell(new Paragraph(value, TEXT_FONT));
                    if (j == 0) {
                        cell.setHorizontalAlignment(Cell.ALIGN_LEFT);
                    }
                    else {
                        cell.setHorizontalAlignment(Cell.ALIGN_CENTER);
                    }                    
                    tableByCountBody.addCell(cell);
                }
            }
            document.add(tableByCount);
            document.add(tableByCountBody);
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

            Image im1 = null;
            im1 = getImage(url1);

            document.add(im1);

            Paragraph quriesByExCount = new Paragraph(ReportConstants.QUERIES_BY_EXECUTION_COUNT,
                            TEXT_FONT);
            document.add(quriesByExCount);

            Paragraph graphicByExCountNote = new Paragraph(ReportConstants.GRAPHIC_FOR_5_QUERIES,
                            TEXT_FONT);
            document.add(graphicByExCountNote);

            final String url2 = (String) to.getImgUrlByCount().keySet().iterator().next();
            Image im2 = null;
            im2 = getImage(url2);
            document.add(im2);

        }
        newPage();
    }

    /**
     * Creates table headers without data for Other layers.
     * 
     * @param colorTitle - title for header.
     * @return PdfPTable object
     * @throws BadElementException when some problem appear.
     */
    private PdfPTable createOtherTableHeader(String colorTitle) throws BadElementException {
        float[] widths = {8f, 28f, 8f, 8f, 8f, 8f, 8f, 8f, 8f, 8f};
        PdfPTable t = new PdfPTable(widths);
        t.getDefaultCell().setPadding(2);
        t.setWidthPercentage(100);

        PdfPCell colorCell = getPDFHeaderCell(colorTitle);
        t.addCell(colorCell);

        PdfPCell opNameCell = getPDFHeaderCell(ReportConstants.OPERATION_NAME);
        t.addCell(opNameCell);

        PdfPCell totalTimeCell = getPDFHeaderCell(ReportConstants.TOTAL_TIME);
        t.addCell(totalTimeCell);

        PdfPCell countCell = getPDFHeaderCell(ReportConstants.COUNT);
        t.addCell(countCell);

        PdfPCell avgTimeCell = getPDFHeaderCell(ReportConstants.AVERAGE_TIME);
        t.addCell(avgTimeCell);

        PdfPCell adjAvgTimeCell = getPDFHeaderCell(ReportConstants.ADJUSTED_AVERAGE_TIME);
        t.addCell(adjAvgTimeCell);

        PdfPCell minExTimeCell = getPDFHeaderCell(ReportConstants.MINIMUM_EXECUTION_TIME);
        t.addCell(minExTimeCell);

        PdfPCell maxExTimeCell = getPDFHeaderCell(ReportConstants.MAXIMUM_EXECUTION_TIME);
        t.addCell(maxExTimeCell);

        PdfPCell firstExTimeCell = getPDFHeaderCell(ReportConstants.FIRST_EXECUTION_TIME);
        t.addCell(firstExTimeCell);

        PdfPCell lastExTimeCell = getPDFHeaderCell(ReportConstants.LAST_EXECUTION_TIME);
        t.addCell(lastExTimeCell);

        return t;
    }

    /**
     * Creates table headers without data for SQL layers.
     * 
     * @return Table object
     * @throws BadElementException when some problem appear.
     */
    private PdfPTable createSqlTableHeader() throws BadElementException {
        float[] widths = {25f, 3f, 12f, 12f, 12f, 12f, 12f, 12f};
        //outer table 
        PdfPTable outer = new PdfPTable(widths);
        outer.getDefaultCell().setPadding(0); 
        //column1
        PdfPCell queryCell = getPDFHeaderCell(ReportConstants.SQL_QUERY);
        outer.addCell(queryCell);
        //column2
        PdfPCell idCell = getPDFHeaderCell(ReportConstants.ID);
        outer.addCell(idCell);
        //column3
        PdfPTable averageTimeTable = new PdfPTable(3);
        averageTimeTable.getDefaultCell().setVerticalAlignment(Cell.ALIGN_MIDDLE);
        PdfPCell avgTimeCell = getPDFHeaderCell(ReportConstants.AVERAGE_TIME);
        avgTimeCell.setColspan(3);
        //row1
        averageTimeTable.addCell(avgTimeCell);
        //row2
        PdfPCell totalCell = getPDFHeaderCell(ReportConstants.TOTAL);
        averageTimeTable.addCell(totalCell);
        PdfPCell executeCell = getPDFHeaderCell(ReportConstants.EXECUTE);
        averageTimeTable.addCell(executeCell);
        PdfPCell prepareCell = getPDFHeaderCell(ReportConstants.PREPARE);
        averageTimeTable.addCell(prepareCell);
        outer.addCell(averageTimeTable);
        //column4
        outer.addCell(getInnerTable(ReportConstants.COUNT, 2));
        //column5
        outer.addCell(getInnerTable(ReportConstants.MAXIMUM_TIME, 2));
        //column6
        outer.addCell(getInnerTable(ReportConstants.MINIMUM_TIME, 2));
        //column7
        outer.addCell(getInnerTable(ReportConstants.FIRST_EXECUTION_TIME, 2));
        //column8
        outer.addCell(getInnerTable(ReportConstants.LAST_EXECUTION_TIME, 2));
        return outer;
    }
    
    /**
     * Create inner table with given column span for emulate row span view.
     * @param title - the title for first row
     * @param colspan - the count of column span
     * @return - the inner PdfPTable table with given column span for emulate row span view.
     */
    private PdfPTable getInnerTable(String title, int colspan) {
        PdfPTable inner = new PdfPTable(colspan);
        inner.getDefaultCell().setVerticalAlignment(Cell.ALIGN_MIDDLE);
        inner.setWidthPercentage(100);
        inner.getDefaultCell().setPadding(0); 
        inner.getDefaultCell().setUseAscender(true); 
        inner.getDefaultCell().setUseDescender(true);
        //first row
        PdfPCell cell = getPDFHeaderCell(title);
        cell.setColspan(colspan);
        inner.addCell(cell);
        //second row
        PdfPCell executeCell = getPDFHeaderCell(ReportConstants.EXECUTE);
        inner.addCell(executeCell);
        PdfPCell prepareCell = getPDFHeaderCell(ReportConstants.PREPARE);
        inner.addCell(prepareCell);
        return inner;
    }
    
}
