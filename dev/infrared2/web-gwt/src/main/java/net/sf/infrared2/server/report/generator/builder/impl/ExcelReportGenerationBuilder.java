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
 * ExcelReportGenerationBuilder.java		Date created: 18.03.2008
 * Last modified by: $Author: soyon.lim $
 * $Revision: 14286 $	$Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.server.report.generator.builder.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import net.sf.infrared2.gwt.client.report.ReportConfigTO;
import net.sf.infrared2.gwt.client.to.ApplicationEntryTO;
import net.sf.infrared2.gwt.client.to.InstanceEntryTO;
import net.sf.infrared2.gwt.client.to.NavigatorEntryTO;
import net.sf.infrared2.gwt.client.to.application.ApplicationViewTO;
import net.sf.infrared2.gwt.client.to.comparator.ByTimeComparator;
import net.sf.infrared2.gwt.client.to.other.OtherViewTO;
import net.sf.infrared2.gwt.client.to.other.SummaryRowTO;
import net.sf.infrared2.gwt.client.to.sql.SqlViewTO;
import net.sf.infrared2.server.Constant;
import net.sf.infrared2.server.adapter.Adapter;
import net.sf.infrared2.server.report.ReportConstants;
import net.sf.infrared2.server.report.generator.adapter.ReportAdapter;
import net.sf.infrared2.server.report.generator.builder.AbstractGeneratorBuilder;
import net.sf.infrared2.server.util.SqlStatistics;
import net.sf.infrared2.server.util.ViewUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;


/**
 * <b>ExcelReportGenerationBuilder<b/><p>
 * Builder for generate report in MS Excel format uses Apache POI HSS library.
 * 
 * @author Andrey Zavgorodniy 
 * Copyright Exadel Inc, 2008
 */
public class ExcelReportGenerationBuilder extends AbstractGeneratorBuilder{
	
	/**
     * The logger.
     */
    private static Log logger = LogFactory.getLog(ExcelReportGenerationBuilder.class);
    
    /** Style for application and instance labels */
    private HSSFCellStyle mainHeadingStyle = null;
    /** Style for layers labels */
    private HSSFCellStyle subHeadingStyle = null;
    /** Style for italic font */
    private HSSFCellStyle headingItalicStyle = null;
    /** Style for italic font */
    private HSSFCellStyle topQueriesStyle = null;
    /** Style for table header */
    private HSSFCellStyle columnHeadingStyle = null;
    /** Style for diagrams labels */
    private HSSFCellStyle diagramsLabelStyle = null;
    /** Data cell font style */
    private HSSFCellStyle dataCellStyle = null;

    /** Field workBook  */
    private HSSFWorkbook workBook = null;

    /** Field absoluteDrawingPatriarch  */
    HSSFPatriarch absoluteDrawingPatriarch = null;
    /** Field hierarchicalDrawingPatriarch  */
    HSSFPatriarch hierarchicalDrawingPatriarch = null;
    
    /** Current row index. */
    private int currentRow;
    /** Current sheet depends of mode type. */
    private HSSFSheet currentSheet;
    /** Current mode - absolute or hierarchical. */
    private String currentMode;

    /** Field colorMap  */
    private Map<String, Short> colorMap = new HashMap<String, Short>();

    /**
     * Constructor.
     * 
     * @param workBook - the main MS Excel work book element.
     * @param reportConfig - the report configuration transfer object.
     */
    public ExcelReportGenerationBuilder(HSSFWorkbook workBook, ReportConfigTO reportConfig) {
        super(reportConfig);
        this.workBook = workBook;
        this.init();
        installPalette(ReportAdapter.getHexColors(Constant.COLOR_SET));
    }

    /**
     * Initialize styles for excel document.
     */
    private void init() {
        this.setupStyles();
    }

    /**
     * Style setup.
     */
    private void setupStyles() {
        this.setUpMainHeadingStyle();
        this.setUpSubHeadingStyle();
        this.setUpColumnHeadingStyle();
        this.setUpHeadingItalicStyle();
        this.setUpTopQueriesStyle();
        this.setUpDiagramsLabelStyle();
        this.setDataCellStyle();
    }
    
    /**
     * Set Up column heading style.
     */
    private void setUpColumnHeadingStyle() {
        this.columnHeadingStyle = this.workBook.createCellStyle();
        HSSFFont columnHeadingFont = this.workBook.createFont();
        columnHeadingFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        columnHeadingFont.setFontName("Arial");
        columnHeadingFont.setFontHeightInPoints((short) 10);
        this.columnHeadingStyle.setFont(columnHeadingFont);
        this.columnHeadingStyle.setWrapText(true);
        this.columnHeadingStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        this.columnHeadingStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        this.columnHeadingStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        this.columnHeadingStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        this.columnHeadingStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        this.columnHeadingStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        this.columnHeadingStyle.setFillPattern(HSSFCellStyle.NO_FILL);
        this.columnHeadingStyle.setFillBackgroundColor(HSSFColor.GREY_25_PERCENT.index);
    }

    /**
     * Set up main heading style.
     */
    private void setUpMainHeadingStyle() {
        this.mainHeadingStyle = this.workBook.createCellStyle();
        HSSFFont mainHeadingFont = this.workBook.createFont();
        mainHeadingFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        mainHeadingFont.setFontHeightInPoints((short) 20);
        mainHeadingFont.setFontName("Arial");
        this.mainHeadingStyle.setFont(mainHeadingFont);
    }

    /**
     * Set up sub heading style.
     */
    private void setUpSubHeadingStyle() {
        this.subHeadingStyle = this.workBook.createCellStyle();
        HSSFFont subHeadingFont = this.workBook.createFont();
        subHeadingFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        subHeadingFont.setFontHeightInPoints((short) 16);
        subHeadingFont.setFontName("Arial");
        this.subHeadingStyle.setFont(subHeadingFont);
    }

    /**
     * Set up heading italic style.
     */
    private void setUpHeadingItalicStyle() {
        this.headingItalicStyle = this.workBook.createCellStyle();
        HSSFFont headingItalicFont = this.workBook.createFont();
        headingItalicFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        headingItalicFont.setItalic(true);
        headingItalicFont.setFontHeightInPoints((short) 14);
        headingItalicFont.setFontName("Arial");
        this.headingItalicStyle.setFont(headingItalicFont);
    }

    /**
     * Set up top queries style.
     */
    private void setUpTopQueriesStyle() {
        this.topQueriesStyle = this.workBook.createCellStyle();
        HSSFFont topQueriesFont = this.workBook.createFont();
        topQueriesFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        topQueriesFont.setFontHeightInPoints((short) 12);
        topQueriesFont.setFontName("Arial");
        this.topQueriesStyle.setFont(topQueriesFont);
    }

    /**
     * Set up diagrams label style.
     */
    private void setUpDiagramsLabelStyle() {
        this.diagramsLabelStyle = this.workBook.createCellStyle();
        HSSFFont diagramsLabelFont = this.workBook.createFont();
        diagramsLabelFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        diagramsLabelFont.setFontHeightInPoints((short) 10);
        diagramsLabelFont.setFontName("Arial");
        this.diagramsLabelStyle.setFont(diagramsLabelFont);

    }

    /**
     * Set up diagrams label style.
     */
    private void setDataCellStyle() {
        this.dataCellStyle = this.workBook.createCellStyle();
        HSSFFont dataCellFont = this.workBook.createFont();
        dataCellFont.setFontHeightInPoints((short) 10);
        dataCellFont.setFontName("Arial");
        this.dataCellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        this.dataCellStyle.setFont(dataCellFont);

    }

    /**
     * Insert blank row.
     * 
     * @param sheet - the current sheet.
     * @param currentRow - the current row
     * @param noOfRows - the no of rows
     * @return - the new row index
     */
    private int insertBlankRow(HSSFSheet sheet, int currentRow, int noOfRows) {
        while (noOfRows-- > 0) {
            sheet.createRow(currentRow++);
        }
        return currentRow;
    }

    /**
     * Insert statistic label row.
     * 
     * @param sheet - the current sheet.
     * @param currentRow - the current row
     * @return - the new row index
     */
    private int insertStatisticLabelRow(HSSFSheet sheet, int currentRow) {
        HSSFRow row = sheet.createRow(currentRow++);
        HSSFCell cell_Static = row.createCell((short) 0);
        cell_Static.setCellValue(ReportConstants.STATISTIC);
        cell_Static.setCellStyle(this.headingItalicStyle);
        return currentRow;
    }

    /**
     * Insert diagrams label row.
     * 
     * @param sheet - the current sheet.
     * @param currentRow - the current row
     * @return - the new row index
     */
    private int insertDiagramsLabelRow(HSSFSheet sheet, int currentRow) {
        HSSFRow row = sheet.createRow(currentRow++);
        HSSFCell cell_Static = row.createCell((short) 0);
        cell_Static.setCellValue(ReportConstants.DIAGRAMS);
        cell_Static.setCellStyle(this.headingItalicStyle);
        return currentRow;
    }

    /**
     * Add application level table header.
     * 
     * @param sheet - the current sheet.
     * @param currentRow - the start record number.
     * @return - the new row index number.
     */
    private int applicationLevelTableHeader(HSSFSheet sheet, int currentRow) {
        HSSFRow headerRow = sheet.createRow(currentRow++);
        HSSFCell cell ;
        HSSFCell cellTimeColor = headerRow.createCell((short) 0);
        cellTimeColor.setCellStyle(this.columnHeadingStyle);
        cellTimeColor.setCellValue(ReportConstants.TIME_COLOR);
        
        HSSFCell cellCountColor = headerRow.createCell((short) 1);
        cellCountColor.setCellStyle(this.columnHeadingStyle);
        cellCountColor.setCellValue(ReportConstants.COUNT_COLOR);
        
        HSSFCell cellLayer = headerRow.createCell((short) 2);
        cellLayer.setCellStyle(this.columnHeadingStyle);
        cellLayer.setCellValue(ReportConstants.LAYER);

        HSSFCell cell_TotalTime = headerRow.createCell((short) 3);
        cell_TotalTime.setCellStyle(this.columnHeadingStyle);
        cell_TotalTime.setCellValue(ReportConstants.TOTAL_TIME);

        HSSFCell cell_Count = headerRow.createCell((short) 4);
        cell_Count.setCellStyle(this.columnHeadingStyle);
        cell_Count.setCellValue(ReportConstants.COUNT);
        return currentRow;
    }

    /**
     * Add application level statistic.
     * 
     * @param applicationViewTO - the application view transfer object
     * @param sheet - the current sheet.
     * @param currentRow - the start record number.
     * @return - the new row index number.
     */
    private int addApplicationLevelBody(ApplicationViewTO applicationViewTO, HSSFSheet sheet,
                    int currentRow) {
        HSSFRow rowTitle = sheet.createRow(currentRow++);
        HSSFCell cellTitle = rowTitle.createCell((short) 0);
        cellTitle.setCellValue(ReportConstants.APPLICATION_LEVEL);
        cellTitle.setCellStyle(this.mainHeadingStyle);
        if (getReportConfigTO().isStatistics()) {
            currentRow = this.insertStatisticLabelRow(sheet, currentRow);
            currentRow = this.applicationLevelTableHeader(sheet, currentRow);
            HSSFRow row;
            HSSFCell cell;
            HSSFCellStyle style;
            // statistic table body
            final Object[][] rows = ReportAdapter.adaptApplicationViewTOArray(applicationViewTO, true);
            for (int i = 0; i < rows.length; i++) {
                Object[] objects = rows[i];
                row = sheet.createRow(currentRow++);
                for (int j = 0; j < objects.length; j++) {
                    Object object = objects[j];
                    cell = row.createCell((short) j);
                    if (j >= 2) {
                        cell.setCellValue(object.toString());
                        cell.setCellStyle(this.dataCellStyle);
                    }
                    else {
                        cell.setCellStyle(this.getColorStyle(object.toString()));
                    }
                }
            }
        }
        return currentRow;
    }

    /**
     * Install custom palette style for workbook.
     * @param colors, hex rgb colors
     */
    private void installPalette(String[] colors){

        short redValue;
        short greenValue;
        short blueValue;

        HSSFPalette palette = this.workBook.getCustomPalette();
        int index =1;
        for (String color : colors){
            redValue = Short.parseShort(color.substring(0, 2),16);
            greenValue = Short.parseShort(color.substring(2, 4),16);
            blueValue = Short.parseShort(color.substring(4, 6),16);
            short colorIndex = (short) (HSSFColor.RED.index + index++);
            palette.setColorAtIndex(
                         colorIndex ,
                        (byte)(redValue),
                        (byte)(greenValue),
                        (byte)(blueValue));
            colorMap.put(color, colorIndex);
        }
    }

    /**
     * Get custom colored style for cell.
     * @param hexCodeValue - the string of hexValue for color
     * @return HSSFCellStyle - the custom style
     */
    private HSSFCellStyle getColorStyle(String hexCodeValue){
        HSSFCellStyle style = this.workBook.createCellStyle();
        style.setFillForegroundColor(colorMap.get(hexCodeValue));
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        return style;
    }
    
    /**
     * Load image by given URL.
     * 
     * @param path - the image path location
     * @param wb - the current workbook
     * @return integer - the picture index
     * @throws IOException when I/O problem occured.
     */
    private int loadPicture(String path, HSSFWorkbook wb) throws IOException {
        return wb.addPicture(getImageByUrl(path), HSSFWorkbook.PICTURE_TYPE_PNG);
    }

    /**
     * Add application level diagrams.
     * 
     * @param sheet - the current sheet.
     * @param currentRow - the start record number.
     * @param applicationViewTO - the transfer object for application layer
     *            view.
     * @param mode - current module
     * @return - the new row index.
     */
    private int addApplicationLevelDiagrams(HSSFSheet sheet, int currentRow,
                    ApplicationViewTO applicationViewTO, String mode) {
        if (getReportConfigTO().isDiagrams()) {
            /* insert diagrams label */
            currentRow = this.insertDiagramsLabelRow(sheet, currentRow);

            HSSFRow row1 = sheet.createRow(currentRow++);
            HSSFCell cellByTotalTimeLabel = row1.createCell((short) 0);
            cellByTotalTimeLabel.setCellValue(ReportConstants.LAYERS_BY_TOTAL_TIME);
            cellByTotalTimeLabel.setCellStyle(this.diagramsLabelStyle);
            
            HSSFClientAnchor anchor;
            int endRow = currentRow + 19;
            anchor = new HSSFClientAnchor(0, 0, 0, 255, (short) 2, row1.getRowNum(), (short) 9,
                            endRow);
            currentRow = endRow;
            try {
                if (mode.equals(Constant.MODULE_ABSOLUTE)) {
                    this.absoluteDrawingPatriarch.createPicture(anchor, this.loadPicture((String) applicationViewTO
                                    .getImgByTimeUrl().keySet().iterator().next(), this.workBook));
                }
                if (mode.equals(Constant.MODULE_HIERARCHICAL)) {
                    this.hierarchicalDrawingPatriarch.createPicture(anchor, this.loadPicture((String) applicationViewTO
                                    .getImgByTimeUrl().keySet().iterator().next(), this.workBook));
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                logger.error(e.getMessage(), e);
            }

            currentRow = this.insertBlankRow(sheet, currentRow, 1);
            
            HSSFRow row2 = sheet.createRow(currentRow++);
            HSSFCell cellByCountLabel = row2.createCell((short) 0);
            cellByCountLabel.setCellValue(ReportConstants.LAYERS_BY_COUNT);
            cellByCountLabel.setCellStyle(this.diagramsLabelStyle);
            endRow = currentRow + 19;
            anchor = new HSSFClientAnchor(0, 0, 0, 255, (short) 2, row2.getRowNum(), (short) 9,
                            endRow);

            currentRow = endRow;
            try {
                if (mode.equals(Constant.MODULE_ABSOLUTE)) {
                    this.absoluteDrawingPatriarch.createPicture(anchor, this.loadPicture((String) applicationViewTO
                                    .getImgByCountUrl().keySet().iterator().next(), this.workBook));
                }
                if (mode.equals(Constant.MODULE_HIERARCHICAL)) {
                    this.hierarchicalDrawingPatriarch.createPicture(anchor, this.loadPicture((String) applicationViewTO
                                    .getImgByCountUrl().keySet().iterator().next(), this.workBook));
                }
                
            } catch (IOException e) {
                // TODO Auto-generated catch block
            	logger.error(e.getMessage(), e);
            }
        }
        return currentRow;
    }

    /**
     * Add SQL query column.
     * 
     * @param sheet - the current HSSFSheet
     * @param headerRow - the current HSSFRow
     * @param headerRowIndx - first row index
     * @param headerRowIndx1 - second row index
     */
    private void addSQLQueryColumn(HSSFSheet sheet, HSSFRow headerRow, int headerRowIndx,
                    int headerRowIndx1) {
        // define the area for the header data(row1->row2, col0-->col0)
        Region regionSQL = new Region(headerRowIndx, (short) 0, headerRowIndx1, (short) 0);
        sheet.addMergedRegion(regionSQL);

        /* SQL query column */
        HSSFCell cell_SQLQuery = headerRow.createCell((short) 0);
        cell_SQLQuery.setCellStyle(this.columnHeadingStyle);
        cell_SQLQuery.setCellValue(ReportConstants.SQL_QUERY);
    }

    /**
     * Add ID column.
     * 
     * @param sheet - the current HSSFSheet
     * @param headerRow - the current HSSFRow
     * @param headerRowIndx - first row index
     * @param headerRowIndx1 - second row index
     */
    private void addIDColumn(HSSFSheet sheet, HSSFRow headerRow, int headerRowIndx,
                    int headerRowIndx1) {
        Region regionID = new Region(headerRowIndx, (short) 1, headerRowIndx1, (short) 1);
        sheet.addMergedRegion(regionID);

        /* ID column */
        HSSFCell cell_ID = headerRow.createCell((short) 1);
        cell_ID.setCellStyle(this.columnHeadingStyle);
        cell_ID.setCellValue(ReportConstants.ID);
    }

    /**
     * Add Average time column.
     * 
     * @param sheet - the current HSSFSheet
     * @param headerRow - the current HSSFRow
     * @param headerRow1 - the second HSSFRow
     * @param headerRowIndx - first row index
     */
    private void addAverageTimeColumn(HSSFSheet sheet, HSSFRow headerRow, HSSFRow headerRow1,
                    int headerRowIndx) {
        Region regionAverageTime = new Region(headerRowIndx, (short) 2, headerRowIndx, (short) 4);
        sheet.addMergedRegion(regionAverageTime);

        /* Average time column */
        HSSFCell cell_averageTime = headerRow.createCell((short) 2);
        cell_averageTime.setCellStyle(this.columnHeadingStyle);
        cell_averageTime.setCellValue(ReportConstants.AVERAGE_TIME);

        HSSFCell cell_total = headerRow1.createCell((short) 2);
        cell_total.setCellStyle(this.columnHeadingStyle);
        cell_total.setCellValue(ReportConstants.TOTAL);

        HSSFCell cell_AverageTimeExecute = headerRow1.createCell((short) 3);
        cell_AverageTimeExecute.setCellStyle(this.columnHeadingStyle);
        cell_AverageTimeExecute.setCellValue(ReportConstants.EXECUTE);

        HSSFCell cell_AverageTimePrepare = headerRow1.createCell((short) 4);
        cell_AverageTimePrepare.setCellStyle(this.columnHeadingStyle);
        cell_AverageTimePrepare.setCellValue(ReportConstants.PREPARE);
    }

    /**
     * Add count column.
     * 
     * @param sheet - the current HSSFSheet
     * @param headerRow - the current HSSFRow
     * @param headerRow1 - the second HSSFRow
     * @param headerRowIndx - first row index
     */
    private void addCountColumn(HSSFSheet sheet, HSSFRow headerRow, HSSFRow headerRow1,
                    int headerRowIndx) {
        // define the area for the header data(row1->row2, col1-->col1)
        Region regionCount = new Region(headerRowIndx, (short) 5, headerRowIndx, (short) 6);
        sheet.addMergedRegion(regionCount);

        /* Count column */
        HSSFCell cell_Count = headerRow.createCell((short) 5);
        cell_Count.setCellStyle(this.columnHeadingStyle);
        cell_Count.setCellValue(ReportConstants.COUNT);

        HSSFCell cell_CountExecute = headerRow1.createCell((short) 5);
        cell_CountExecute.setCellStyle(this.columnHeadingStyle);
        cell_CountExecute.setCellValue(ReportConstants.EXECUTE);

        HSSFCell cell_CountPrepare = headerRow1.createCell((short) 6);
        cell_CountPrepare.setCellStyle(this.columnHeadingStyle);
        cell_CountPrepare.setCellValue(ReportConstants.PREPARE);
    }

    /**
     * Add max time column.
     * 
     * @param sheet - the current HSSFSheet
     * @param headerRow - the current HSSFRow
     * @param headerRow1 - the second HSSFRow
     * @param headerRowIndx - first row index
     */
    private void addMaxTimeColumn(HSSFSheet sheet, HSSFRow headerRow, HSSFRow headerRow1,
                    int headerRowIndx) {
        // define the area for the header data(row1->row2, col1-->col1)
        Region regionMaxTime = new Region(headerRowIndx, (short) 7, headerRowIndx, (short) 8);
        sheet.addMergedRegion(regionMaxTime);

        /* Maximum time */
        HSSFCell cell_MaxTime = headerRow.createCell((short) 7);
        cell_MaxTime.setCellStyle(this.columnHeadingStyle);
        cell_MaxTime.setCellValue(ReportConstants.MAXIMUM_TIME);

        HSSFCell cell_MaxTimeExecute = headerRow1.createCell((short) 7);
        cell_MaxTimeExecute.setCellStyle(this.columnHeadingStyle);
        cell_MaxTimeExecute.setCellValue(ReportConstants.EXECUTE);

        HSSFCell cell_MaxTimePrepare = headerRow1.createCell((short) 8);
        cell_MaxTimePrepare.setCellStyle(this.columnHeadingStyle);
        cell_MaxTimePrepare.setCellValue(ReportConstants.PREPARE);
    }

    /**
     * Add minimum time column.
     * 
     * @param sheet - the current HSSFSheet
     * @param headerRow - the current HSSFRow
     * @param headerRow1 - the second HSSFRow
     * @param headerRowIndx - first row index
     */
    private void addMinimumTimeColumn(HSSFSheet sheet, HSSFRow headerRow, HSSFRow headerRow1,
                    int headerRowIndx) {
        // define the area for the header data(row1->row2, col1-->col1)
        Region regionMinTime = new Region(headerRowIndx, (short) 9, headerRowIndx, (short) 10);
        sheet.addMergedRegion(regionMinTime);

        /* Minimum time column */
        HSSFCell cell_MinTime = headerRow.createCell((short) 9);
        cell_MinTime.setCellStyle(this.columnHeadingStyle);
        cell_MinTime.setCellValue(ReportConstants.MINIMUM_TIME);

        HSSFCell cell_MinTimeExecute = headerRow1.createCell((short) 9);
        cell_MinTimeExecute.setCellStyle(this.columnHeadingStyle);
        cell_MinTimeExecute.setCellValue(ReportConstants.EXECUTE);

        HSSFCell cell_MinTimePrepare = headerRow1.createCell((short) 10);
        cell_MinTimePrepare.setCellStyle(this.columnHeadingStyle);
        cell_MinTimePrepare.setCellValue(ReportConstants.PREPARE);
    }

    /**
     * Add first execution column.
     * 
     * @param sheet - the current HSSFSheet
     * @param headerRow - the current HSSFRow
     * @param headerRow1 - the second HSSFRow
     * @param headerRowIndx - first row index
     */
    private void addFirstExecutionColumn(HSSFSheet sheet, HSSFRow headerRow, HSSFRow headerRow1,
                    int headerRowIndx) {
        // define the area for the header data(row1->row2, col1-->col1)
        Region regionFirstExecution = new Region(headerRowIndx, (short) 11, headerRowIndx,
                        (short) 12);
        sheet.addMergedRegion(regionFirstExecution);

        /* First execution column */
        HSSFCell cell_FirstExecution = headerRow.createCell((short) 11);
        cell_FirstExecution.setCellStyle(this.columnHeadingStyle);
        cell_FirstExecution.setCellValue(ReportConstants.FIRST_EXECUTION_TIME);

        HSSFCell cell_FirstExecutionExecute = headerRow1.createCell((short) 11);
        cell_FirstExecutionExecute.setCellStyle(this.columnHeadingStyle);
        cell_FirstExecutionExecute.setCellValue(ReportConstants.EXECUTE);

        HSSFCell cell_FirstExecutionPrepare = headerRow1.createCell((short) 12);
        cell_FirstExecutionPrepare.setCellStyle(this.columnHeadingStyle);
        cell_FirstExecutionPrepare.setCellValue(ReportConstants.PREPARE);
    }

    /**
     * Add last execution column.
     * 
     * @param sheet - the current HSSFSheet
     * @param headerRow - the current HSSFRow
     * @param headerRow1 - the second HSSFRow
     * @param headerRowIndx - first row index
     */
    private void addLastExecutionColumn(HSSFSheet sheet, HSSFRow headerRow, HSSFRow headerRow1,
                    int headerRowIndx) {
        // define the area for the header data(row1->row2, col1-->col1)
        Region regionLastExecution = new Region(headerRowIndx, (short) 13, headerRowIndx,
                        (short) 14);
        sheet.addMergedRegion(regionLastExecution);

        /* Last execution column */
        HSSFCell cell_LastExecution = headerRow.createCell((short) 13);
        cell_LastExecution.setCellStyle(this.columnHeadingStyle);
        cell_LastExecution.setCellValue(ReportConstants.LAST_EXECUTION_TIME);

        HSSFCell cell_LastExecutionExecute = headerRow1.createCell((short) 13);
        cell_LastExecutionExecute.setCellStyle(this.columnHeadingStyle);
        cell_LastExecutionExecute.setCellValue(ReportConstants.EXECUTE);

        HSSFCell cell_LastExecutionPrepare = headerRow1.createCell((short) 14);
        cell_LastExecutionPrepare.setCellStyle(this.columnHeadingStyle);
        cell_LastExecutionPrepare.setCellValue(ReportConstants.PREPARE);
    }

    /**
     * Add SQL table header.
     * 
     * @param sheet - the current HSSFSheet
     * @param currentRow - the current row index
     * @return current row.
     */
    private int addSQLTableHeader(HSSFSheet sheet, int currentRow) {
        int headerRowIndx = 0;
        int headerRowIndx1 = 0;
        headerRowIndx = currentRow++;
        HSSFRow headerRow = sheet.createRow(headerRowIndx);
        headerRowIndx1 = currentRow++;
        HSSFRow headerRow1 = sheet.createRow(headerRowIndx1);
        /* Add columns */
        this.addSQLQueryColumn(sheet, headerRow, headerRowIndx, headerRowIndx1);
        this.addIDColumn(sheet, headerRow, headerRowIndx, headerRowIndx1);
        this.addAverageTimeColumn(sheet, headerRow, headerRow1, headerRowIndx);
        this.addCountColumn(sheet, headerRow, headerRow1, headerRowIndx);
        this.addMaxTimeColumn(sheet, headerRow, headerRow1, headerRowIndx);
        this.addMinimumTimeColumn(sheet, headerRow, headerRow1, headerRowIndx);
        this.addFirstExecutionColumn(sheet, headerRow, headerRow1, headerRowIndx);
        this.addLastExecutionColumn(sheet, headerRow, headerRow1, headerRowIndx);
        return currentRow;
    }

    /**
     * Label current queries for build graphic.
     * @param sheet - the current sheet.
     * @param currentRow - the current row.
     * @return - new current row index.
     */
    private int queriesGraphicLabel(HSSFSheet sheet, int currentRow) {
        HSSFRow row = sheet.createRow(currentRow++);
        HSSFCell cellByTotalTimeLabel = row.createCell((short) 0);
        cellByTotalTimeLabel.setCellValue(ReportConstants.GRAPHIC_FOR_5_QUERIES);
        cellByTotalTimeLabel.setCellStyle(this.diagramsLabelStyle);
        return currentRow;
    }
    
    /**
     * Add SQL layer diagrams.
     * 
     * @param sheet - the current sheet.
     * @param currentRow - the current row.
     * @param sqlViewTO - the SQL view transfer object.
     * @param mode - module type.
     * @return - new current row index.
     */
    private int addSQLLayerDiagrams(HSSFSheet sheet, int currentRow, SqlViewTO sqlViewTO, String mode) {
        if (getReportConfigTO().isDiagrams()) {
            currentRow = this.insertDiagramsLabelRow(sheet, currentRow);
            HSSFRow row1 = sheet.createRow(currentRow++);
            HSSFCell cellByTotalTimeLabel = row1.createCell((short) 0);
            cellByTotalTimeLabel.setCellValue(ReportConstants.QUERIES_BY_AVERAGE_EXECUTION_TIME);
            cellByTotalTimeLabel.setCellStyle(this.diagramsLabelStyle);
            currentRow = this.queriesGraphicLabel(sheet, currentRow);
            HSSFClientAnchor anchor;
            int endRow = currentRow + 19;
            anchor = new HSSFClientAnchor(0, 0, 0, 255, (short) 2, row1.getRowNum(), (short) 12,
                            endRow);
            currentRow = endRow;
            try {
                if (mode.equals(Constant.MODULE_ABSOLUTE)) {
                    this.absoluteDrawingPatriarch.createPicture(anchor, this.loadPicture((String) sqlViewTO
                                    .getImgUrlByTime().keySet().iterator().next(), this.workBook));
                }
                if (mode.equals(Constant.MODULE_HIERARCHICAL)) {
                    this.hierarchicalDrawingPatriarch.createPicture(anchor, this.loadPicture((String) sqlViewTO
                                    .getImgUrlByTime().keySet().iterator().next(), this.workBook));
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
            	logger.error(e.getMessage(), e);
            }

            currentRow = this.insertBlankRow(sheet, currentRow, 1);
            
            HSSFRow row2 = sheet.createRow(currentRow++);
            HSSFCell cellByCountLabel = row2.createCell((short) 0);
            cellByCountLabel.setCellValue(ReportConstants.QUERIES_BY_EXECUTION_COUNT);
            cellByCountLabel.setCellStyle(this.diagramsLabelStyle);
            currentRow = this.queriesGraphicLabel(sheet, currentRow);
            endRow = currentRow + 19;
            anchor = new HSSFClientAnchor(0, 0, 0, 255, (short) 2, row2.getRowNum(), (short) 12,
                            endRow);
            currentRow = endRow;
            try {
                if (mode.equals(Constant.MODULE_ABSOLUTE)) {
                    this.absoluteDrawingPatriarch.createPicture(anchor, this.loadPicture((String) sqlViewTO
                                    .getImgUrlByCount().keySet().iterator().next(), this.workBook));
                    
                }
                if (mode.equals(Constant.MODULE_HIERARCHICAL)) {
                    this.hierarchicalDrawingPatriarch.createPicture(anchor, this.loadPicture((String) sqlViewTO
                                    .getImgUrlByCount().keySet().iterator().next(), this.workBook));
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
            	logger.error(e.getMessage(), e);
            }
        }
        currentRow = this.insertBlankRow(sheet, currentRow, 1);
        return currentRow;
    }

    /**
     * Add other layer diagrams.
     * 
     * @param sheet - the current sheet.
     * @param currentRow - the current row.
     * @param otherViewTO - the other view transfer object.
     * @param inclusive - the inclusive/exclusive flag.
     * @param mode - current module type.
     * @return - new current row index.
     */
    private int addOtherLayerDiagrams(HSSFSheet sheet, int currentRow, OtherViewTO otherViewTO,
                    boolean inclusive, String mode) {
        if (getReportConfigTO().isDiagrams()) {
            currentRow = this.insertBlankRow(sheet, currentRow, 1);
            currentRow = this.insertDiagramsLabelRow(sheet, currentRow);
            HSSFRow row1 = sheet.createRow(currentRow++);
            HSSFCell cellByTotalTimeLabel = row1.createCell((short) 0);
            cellByTotalTimeLabel.setCellValue(ReportConstants.OPERATIONS_BY_TOTAL_TIME);
            cellByTotalTimeLabel.setCellStyle(this.diagramsLabelStyle);
            int endRow = currentRow + 19;
            HSSFClientAnchor anchor;
            anchor = new HSSFClientAnchor(0, 0, 0, 255, (short) 2, row1.getRowNum(), (short) 9,
                            endRow);
            currentRow = endRow;
            try {
                if (inclusive) {
                    if (mode.equals(Constant.MODULE_ABSOLUTE)) {
                        this.absoluteDrawingPatriarch.createPicture(anchor, this.loadPicture(
                                        (String) otherViewTO.getImgInclusiveOperationByTimeUrl()
                                                        .keySet().iterator().next(), this.workBook));
                        
                    }
                    if (mode.equals(Constant.MODULE_HIERARCHICAL)) {
                        this.hierarchicalDrawingPatriarch.createPicture(anchor, this.loadPicture(
                                        (String) otherViewTO.getImgInclusiveOperationByTimeUrl()
                                                        .keySet().iterator().next(), this.workBook));
                    }
                } else {
                    if (mode.equals(Constant.MODULE_ABSOLUTE)) {
                        this.absoluteDrawingPatriarch.createPicture(anchor, this.loadPicture(
                                        (String) otherViewTO.getImgExclusiveOperationByTimeUrl()
                                                        .keySet().iterator().next(), this.workBook));
                    }
                    if (mode.equals(Constant.MODULE_HIERARCHICAL)) {
                        this.hierarchicalDrawingPatriarch.createPicture(anchor, this.loadPicture(
                                        (String) otherViewTO.getImgExclusiveOperationByTimeUrl()
                                                        .keySet().iterator().next(), this.workBook));
                    }
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
            	logger.error(e.getMessage(), e);
            }

            currentRow = this.insertBlankRow(sheet, currentRow, 1);
            
            HSSFRow row2 = sheet.createRow(currentRow++);
            HSSFCell cellByCountLabel = row2.createCell((short) 0);
            cellByCountLabel.setCellValue(ReportConstants.OPERATIONS_BY_COUNT);
            cellByCountLabel.setCellStyle(this.diagramsLabelStyle);
            endRow = currentRow + 19;
            anchor = new HSSFClientAnchor(0, 0, 0, 255, (short) 2, row2.getRowNum(), (short) 9,
                            endRow);
            currentRow = endRow;
            try {
                if (inclusive) {
                    if (mode.equals(Constant.MODULE_ABSOLUTE)) {
                        this.absoluteDrawingPatriarch.createPicture(anchor, this.loadPicture((String) otherViewTO
                                        .getImgInclusiveOperationByCountUrl().keySet().iterator()
                                        .next(), this.workBook));
                    }
                    if (mode.equals(Constant.MODULE_HIERARCHICAL)) {
                        this.hierarchicalDrawingPatriarch.createPicture(anchor, this.loadPicture((String) otherViewTO
                                        .getImgInclusiveOperationByCountUrl().keySet().iterator()
                                        .next(), this.workBook));
                    }
                } else {
                    if (mode.equals(Constant.MODULE_ABSOLUTE)) {
                        this.absoluteDrawingPatriarch.createPicture(anchor, this.loadPicture((String) otherViewTO
                                        .getImgExclusiveOperationByCountUrl().keySet().iterator()
                                        .next(), workBook));
                    }
                    if (mode.equals(Constant.MODULE_HIERARCHICAL)) {
                        this.hierarchicalDrawingPatriarch.createPicture(anchor, this.loadPicture((String) otherViewTO
                                        .getImgExclusiveOperationByCountUrl().keySet().iterator()
                                        .next(), workBook));
                    }
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
            	logger.error(e.getMessage(), e);
            }
        }
        return currentRow;
    }

    /**
     * Add layer level table header.
     * 
     * @param sheet - the current HSSFSheet.
     * @param currentRow - the current row
     * @param isTimeColor - determine belonging of color.
     * @return - the new current row index
     */
    private int otherLayerLevelTableHeader(HSSFSheet sheet, int currentRow, boolean isTimeColor) {
        HSSFRow row = sheet.createRow(currentRow++);
        if (isTimeColor) {
            /* Time color column */
            HSSFCell cell_TimeColor = row.createCell((short) 0);
            cell_TimeColor.setCellStyle(this.columnHeadingStyle);
            cell_TimeColor.setCellValue(ReportConstants.TIME_COLOR);

        }
        else {
            /* Count color column */
            HSSFCell cell_CountColore = row.createCell((short) 0);
            cell_CountColore.setCellStyle(this.columnHeadingStyle);
            cell_CountColore.setCellValue(ReportConstants.COUNT_COLOR);

        }
        /* Operation name column */
        HSSFCell cell_OperationName = row.createCell((short) 1);
        cell_OperationName.setCellStyle(this.columnHeadingStyle);
        cell_OperationName.setCellValue(ReportConstants.OPERATION_NAME);

        /* Total time column */
        HSSFCell cell_TotalTime = row.createCell((short) 2);
        cell_TotalTime.setCellStyle(this.columnHeadingStyle);
        cell_TotalTime.setCellValue(ReportConstants.TOTAL_TIME);

        /* Count column */
        HSSFCell cell_Count = row.createCell((short) 3);
        cell_Count.setCellStyle(this.columnHeadingStyle);
        cell_Count.setCellValue(ReportConstants.COUNT);

        /* Average Time column */
        HSSFCell cell_AverageTime = row.createCell((short) 4);
        cell_AverageTime.setCellStyle(this.columnHeadingStyle);
        cell_AverageTime.setCellValue(ReportConstants.AVERAGE_TIME);

        /* Adjusted Average Time column */
        HSSFCell cell_AdjustedAverageTime = row.createCell((short) 5);
        cell_AdjustedAverageTime.setCellStyle(this.columnHeadingStyle);
        cell_AdjustedAverageTime.setCellValue(ReportConstants.ADJUSTED_AVERAGE_TIME);

        /* Minimum Execution Time column */
        HSSFCell cell_MinimumExecutionTime = row.createCell((short) 6);
        cell_MinimumExecutionTime.setCellStyle(this.columnHeadingStyle);
        cell_MinimumExecutionTime.setCellValue(ReportConstants.MINIMUM_EXECUTION_TIME);

        /* Maximum Execution Time column */
        HSSFCell cell_MaximumExecutionTime = row.createCell((short) 7);
        cell_MaximumExecutionTime.setCellStyle(this.columnHeadingStyle);
        cell_MaximumExecutionTime.setCellValue(ReportConstants.MAXIMUM_EXECUTION_TIME);

        /* First Execution Time column */
        HSSFCell cell_FirstExecutionTime = row.createCell((short) 8);
        cell_FirstExecutionTime.setCellStyle(this.columnHeadingStyle);
        cell_FirstExecutionTime.setCellValue(ReportConstants.FIRST_EXECUTION_TIME);

        /* Last Execution Time column */
        HSSFCell cell_LastExecutionTime = row.createCell((short) 9);
        cell_LastExecutionTime.setCellStyle(this.columnHeadingStyle);
        cell_LastExecutionTime.setCellValue(ReportConstants.LAST_EXECUTION_TIME);

        return currentRow;
    }

    /**
    * Creates header of for application, instance and module name.
    * 
    * @param navigatorEntryTO - transfer object, contains module type, application title and
    *            instance title.
    */
    @Override
    protected void createApplicationInstanceHeader(NavigatorEntryTO navigatorEntryTO)
                    throws Exception {
        String applicationTitle = null;
        if (getReportConfigTO().getMergedApplicationTitle()!=null) {
            applicationTitle = ReportConstants.MERGED_FOR + getReportConfigTO().getMergedApplicationTitle();
        }
        else {
            // For example: Application 1, instance 11 (if divided by application is
            // selected)
            final ApplicationEntryTO applications = navigatorEntryTO.getApplications();
            if (applications != null) {
                applicationTitle = applications.getApplicationTitle();
                if (applications.getInstanceList() != null && applications.getInstanceList().length > 0) {
                    final InstanceEntryTO instance = applications.getInstanceList()[0];
                    if (instance != null) {
                        applicationTitle += ", " + instance.getName();
                    }
                }
            }
        }
        this.insertBlankRow(this.currentSheet, this.currentRow++, 1);
        HSSFRow headerRow = this.currentSheet.createRow(this.currentRow++);
        HSSFCell cellHeader = headerRow.createCell((short) 0);
        cellHeader.setCellStyle(this.mainHeadingStyle);
        cellHeader.setCellValue(applicationTitle);
    }

    /**
     * Creates report for application layer.
     * 
     * @param applicationViewTO - the transfer object for application layer
     *            view.
     */
    @Override
    protected void createApplicationLayer(ApplicationViewTO applicationViewTO) throws Exception {
        this.currentRow = this.addApplicationLevelBody(applicationViewTO, this.currentSheet, this.currentRow);
        this.currentRow = this.insertBlankRow(this.currentSheet, this.currentRow, 1);
        this.currentRow = this.addApplicationLevelDiagrams(this.currentSheet, this.currentRow, applicationViewTO, this.currentMode);
    }
    
    /**
     * Create module title row.
     * 
     * @param qualifiedModuleName - the module name.
     */
    @Override
    protected void createModuleTitle(String qualifiedModuleName) throws Exception {
        /* prepare sheets and diagrams */
        this.currentRow = 0;
        if (qualifiedModuleName.equals(ReportConstants.ABSOLUTE_MODULE)) {
            this.currentMode = Constant.MODULE_ABSOLUTE;
            this.currentSheet = workBook.createSheet(ReportConstants.ABSOLUTE_MODULE);
            if (getReportConfigTO().isDiagrams()) {
                this.absoluteDrawingPatriarch = this.currentSheet.createDrawingPatriarch();
            }
        }
        if (qualifiedModuleName.equals(ReportConstants.HIERARCHICAL_MODULE)) {
            this.currentMode = Constant.MODULE_HIERARCHICAL;
            this.currentSheet = workBook.createSheet(ReportConstants.HIERARCHICAL_MODULE);
            if (getReportConfigTO().isDiagrams()) {
                this.hierarchicalDrawingPatriarch = this.currentSheet.createDrawingPatriarch();
            }
        }

        HSSFRow titleRow = currentSheet.createRow(currentRow++);
        HSSFCell cell_Title = titleRow.createCell((short) 0);
        cell_Title.setCellStyle(this.mainHeadingStyle);
        cell_Title.setCellValue(qualifiedModuleName);
    }

    /**
     * Add other layer level.
     * 
     * @param otherViewTO - the other view transfer object.
     * @param b - flag for inclusive/exclusive mode.
     */
    @Override
    protected void createOtherLayer(OtherViewTO otherViewTO, boolean b) throws Exception {
        /* insert blank row */
        this.currentRow = this.insertBlankRow(this.currentSheet, this.currentRow, 1);

        HSSFRow row = this.currentSheet.createRow(this.currentRow++);
        HSSFCell cell_Label = row.createCell((short) 0);
        String title = otherViewTO.getNavigatorEntryTO().getTitle();
        if (b) {
            title += ReportConstants.LAYER_LEVEL_INCLUSIVE;
            /* Inclusive mode */
            cell_Label.setCellStyle(this.subHeadingStyle);
            cell_Label.setCellValue(title);
        } else {
            title += ReportConstants.LAYER_LEVEL_EXCLUSIVE;
            /* Exclusive mode */
            cell_Label.setCellStyle(this.subHeadingStyle);
            cell_Label.setCellValue(title);
        }
        if (getReportConfigTO().isStatistics()) {
            this.currentRow = this.insertStatisticLabelRow(this.currentSheet, this.currentRow);
            HSSFRow topOperationsByTotalTimeRow = this.currentSheet.createRow(currentRow++);
            HSSFCell cell_topOperationsByTotalTime = topOperationsByTotalTimeRow
                            .createCell((short) 0);
            cell_topOperationsByTotalTime
                            .setCellValue(ReportConstants.OPERATIONS_BY_TOTAL_TIME);
            cell_topOperationsByTotalTime.setCellStyle(this.topQueriesStyle);

            /* create statistic table */
            this.currentRow = this.otherLayerLevelTableHeader(this.currentSheet, this.currentRow, true);

            final SummaryRowTO[] tableRows;
            if (b)
                tableRows = otherViewTO.getInclusiveTableRows();
            else
                tableRows = otherViewTO.getExclusiveTableRows();

            /* statistic table body */
            Arrays.sort(tableRows, new ByTimeComparator());
            
            final Object[][] rowsByTime = ReportAdapter.adaptSummaryRowTOToArray(ViewUtil.getTopNOperationsByParam((b)?otherViewTO.getInclusiveTableRows():
                otherViewTO.getExclusiveTableRows(), ReportConstants.OTHER_LAYER_OPERATION_COUNT,"getTotalTime"), true, true);
            HSSFCell cell;
            for (int i = 0; i < rowsByTime.length; i++) {
                Object[] objects = rowsByTime[i];
                HSSFRow bodyByTotalTimeRow = this.currentSheet.createRow(this.currentRow++);
                for (int j = 0; j < objects.length; j++) {
                    Object object = objects[j];
                    cell = bodyByTotalTimeRow.createCell((short) j);
                    if (j != 0) {
                        cell.setCellValue(object.toString());
                        if (j > 1) {
                            cell.setCellStyle(this.dataCellStyle);
                        }
                    }
                    else {
                        cell.setCellStyle(this.getColorStyle(object.toString()));
                    }
                }
            }

            /* insert blank row */
            this.currentRow = this.insertBlankRow(this.currentSheet, this.currentRow, 1);

            HSSFRow topOperationsByCountRow = this.currentSheet.createRow(this.currentRow++);
            HSSFCell cell_topOperationsByCount = topOperationsByCountRow.createCell((short) 0);
            cell_topOperationsByCount.setCellValue(ReportConstants.OPERATIONS_BY_COUNT);
            cell_topOperationsByCount.setCellStyle(this.topQueriesStyle);

            /* create statistic table */
            this.currentRow = this.otherLayerLevelTableHeader(this.currentSheet, this.currentRow, false);

            /* statistic table body */
            Arrays.sort(tableRows, new ByTimeComparator());
            
            final Object[][] rowsByCount = ReportAdapter.adaptSummaryRowTOToArray(ViewUtil.getTopNOperationsByParam((b)?otherViewTO.getInclusiveTableRows():
                otherViewTO.getExclusiveTableRows(), ReportConstants.OTHER_LAYER_OPERATION_COUNT,"getCount"), true, false);
            HSSFCell countCell;
            for (int i = 0; i < rowsByCount.length; i++) {
                Object[] objects = rowsByCount[i];
                HSSFRow bodyByTotalCountRow = this.currentSheet.createRow(this.currentRow++);
                for (int j = 0; j < objects.length; j++) {
                    Object object = objects[j];
                    countCell = bodyByTotalCountRow.createCell((short) j);
                    if (j != 0) {
                        countCell.setCellValue(object.toString());
                        if (j > 1) {
                            countCell.setCellStyle(this.dataCellStyle);
                        }
                    }
                    else {
                        countCell.setCellStyle(this.getColorStyle(object.toString()));
                    }
                }
            }

            /* view diagrams if is checked */
            this.currentRow = this.addOtherLayerDiagrams(this.currentSheet, this.currentRow, otherViewTO,
                    b, this.currentMode);
        }
    }

    /**
     * Add SQL layer level.
     * 
     * @param sqlViewTO - data source.
     */
    @Override
    protected void createSqlLayer(SqlViewTO sqlViewTO) throws Exception {
        this.currentRow = this.insertBlankRow(this.currentSheet, currentRow, 2);
        HSSFRow row = this.currentSheet.createRow(this.currentRow++);
        HSSFCell cell_SQLLayer_label = row.createCell((short) 0);
        cell_SQLLayer_label.setCellStyle(this.mainHeadingStyle);
        cell_SQLLayer_label.setCellValue(ReportConstants.LAYER_LEVEL_SQL_LAYERS);

        HSSFRow rowSQL = this.currentSheet.createRow(this.currentRow++);
        HSSFCell cell_SQL_label = rowSQL.createCell((short) 0);
        cell_SQL_label.setCellStyle(this.subHeadingStyle);
        cell_SQL_label.setCellValue(ReportConstants.SQL);

        if (getReportConfigTO().isStatistics()) {
            this.currentRow = this.insertStatisticLabelRow(this.currentSheet, this.currentRow);
            HSSFRow rowAverageExecuteTime = this.currentSheet.createRow(this.currentRow++);
            HSSFCell cell_AverageExecuteTime_label = rowAverageExecuteTime.createCell((short) 0);
            cell_AverageExecuteTime_label
                            .setCellValue(ReportConstants.TOP + " " + getReportConfigTO().getQueries()+" " + ReportConstants.QUERIES_BY_AVERAGE_EXECUTION_TIME);
            cell_AverageExecuteTime_label.setCellStyle(this.topQueriesStyle);

            /* table header */
            this.currentRow = this.addSQLTableHeader(this.currentSheet, this.currentRow);

            /* table body by execution time */
            SqlStatistics[] toNQueriesByTime = ViewUtil.getTopNQueriesByExecutionTime(Adapter
                            .getSqlStatisticsArray(sqlViewTO.getAllSqlStatistics()), Integer
                            .parseInt(getReportConfigTO().getQueries()));
            final Object[][] rowsByTime = ReportAdapter.adaptSqlViewTOArray(toNQueriesByTime);
            HSSFCell cellExecutionData;
            HSSFRow rowExecutionData;
            for (int i = 0; i < rowsByTime.length; i++) {
                Object[] objects = rowsByTime[i];
                rowExecutionData = this.currentSheet.createRow(this.currentRow++);
                for (int j = 0; j < objects.length; j++) {
                    Object object = objects[j];
                    cellExecutionData = rowExecutionData.createCell((short) j);
                    cellExecutionData.setCellValue(object.toString());
                    if (j > 1) {
                        cellExecutionData.setCellStyle(this.dataCellStyle);
                    }
                }
            }

            this.currentRow = this.insertBlankRow(this.currentSheet, this.currentRow, 1);

            HSSFRow executionCountLabelRow = currentSheet.createRow(currentRow++);
            HSSFCell cell_ExecutionLabel = executionCountLabelRow.createCell((short) 0);
            cell_ExecutionLabel.setCellValue(ReportConstants.TOP + " " + getReportConfigTO().getQueries()+" " + ReportConstants.QUERIES_BY_EXECUTION_COUNT);
            cell_ExecutionLabel.setCellStyle(this.topQueriesStyle);

            this.currentRow = this.addSQLTableHeader(this.currentSheet, this.currentRow);
            
            SqlStatistics[] toNQueriesByCount = ViewUtil.getTopNQueriesByCount(Adapter.getSqlStatisticsArray(sqlViewTO.getAllSqlStatistics()),
                            Integer.parseInt(getReportConfigTO().getQueries()));

            /* table body by execution count */
            final Object[][] rowsByCount = ReportAdapter.adaptSqlViewTOArray(toNQueriesByCount);
            for (int i = 0; i < rowsByCount.length; i++) {
                Object[] objects = rowsByCount[i];
                rowExecutionData = this.currentSheet.createRow(this.currentRow++);
                for (int j = 0; j < objects.length; j++) {
                    Object object = objects[j];
                    cellExecutionData = rowExecutionData.createCell((short) j);
                    cellExecutionData.setCellValue(object.toString());
                    if (j > 1) {
                        cellExecutionData.setCellStyle(this.dataCellStyle);
                    }
                }
            }
        }
        /* SQL diagrams */
        this.currentRow = this.insertBlankRow(this.currentSheet, this.currentRow, 1);
        this.currentRow = this.addSQLLayerDiagrams(this.currentSheet, this.currentRow, sqlViewTO, this.currentMode);
    }
}
