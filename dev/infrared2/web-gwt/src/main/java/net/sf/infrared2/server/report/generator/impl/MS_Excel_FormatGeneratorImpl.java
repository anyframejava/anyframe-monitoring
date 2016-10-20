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
 * MS_Excel_FormatGeneratorImpl.java		Date created: 17.02.2008
 * Last modified by: $Author: soyon.lim $
 * $Revision: 14286 $	$Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.server.report.generator.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import net.sf.infrared2.gwt.client.Constants;
import net.sf.infrared2.gwt.client.report.ReportConfigTO;
import net.sf.infrared2.server.report.generator.IFormatGenerator;
import net.sf.infrared2.server.report.generator.builder.impl.ExcelReportGenerationBuilder;
import net.sf.infrared2.server.report.model.ReportSourceContentTO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;


/**
 * <b>MS_Excel_FormatGeneratorImpl</b><p>
 * Generates report in MS Excel spreadsheet format.
 * @author Roman Ivanenko
 * @author Andrey Zavgorodniy
 */
public class MS_Excel_FormatGeneratorImpl implements IFormatGenerator {

    /**
     * Generated serial version UID
     */
    private static final long serialVersionUID = 7947031922577296416L;
    
    /**
     * The logger.
     */
    private static Log logger = LogFactory.getLog(MS_Excel_FormatGeneratorImpl.class);
    
    /** Field CONTENT_MIME_TYPE  */
    private static final String CONTENT_MIME_TYPE = "application/vnd.ms-excel";
    /** Field REPORT_FILE_EXTENTION  */
    private static final String REPORT_FILE_EXTENTION = ".xls";
    
    /**
     * @return the reportFormatName
     */
    public String getReportFormatName() {
        return Constants.MS_EXCEL_FORMAT_NAME;
    }

    /**
     * @return the contentMIMEType
     */
    public String getContentMIMEType() {
        return CONTENT_MIME_TYPE;
    }

    /**
     * @return the reportFileExtention
     */
    public String getReportFileExtention() {
        return REPORT_FILE_EXTENTION;
    }

    /**
     * Generate Buffered Report.
     *
     * @param reportConfigTO a <code>ReportConfigTO</code>
     * @param reportSourseContentTO a <code>ReportSourceContentTO</code>
     * @return Array of byte. Buffered Report.
     *
     * @see net.sf.infrared2.server.report.generator.IFormatGenerator#generateBufferedReport(ReportConfigTO, ReportSourceContentTO)
     */
    public byte[] generateBufferedReport(ReportConfigTO reportConfigTO, ReportSourceContentTO reportSourseContentTO) {

        /* Stub. Test simple data */
        //Create an Excel style sheet workbook:
        HSSFWorkbook wb = new HSSFWorkbook();
        ExcelReportGenerationBuilder excelBuilder = new ExcelReportGenerationBuilder(wb, reportConfigTO);
        try {
            excelBuilder.build(reportSourseContentTO);
        } catch (Exception e1) {
        	logger.error(e1.getMessage(), e1);
        }
        
        // TODO remove this test stub line.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            wb.write(baos);
        } catch (IOException e) {
        	logger.error(e.getMessage(), e);
        }
        byte[] bufferedReport = baos.toByteArray();

        return bufferedReport;
    }

    
}
