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
 * CSV_FormatGeneratorImpl.java		Date created: 17.02.2008
 * Last modified by: $Author: soyon.lim $
 * $Revision: 14286 $	$Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.server.report.generator.impl;


import java.io.ByteArrayOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.infrared2.gwt.client.report.ReportConfigTO;
import net.sf.infrared2.server.report.generator.IFormatGenerator;
import net.sf.infrared2.server.report.generator.builder.impl.CSVReportGeneratorBuilder;
import net.sf.infrared2.server.report.generator.builder.impl.ExcelReportGenerationBuilder;
import net.sf.infrared2.server.report.generator.impl.csv.StringArrayToCSVWriter;
import net.sf.infrared2.server.report.model.ReportSourceContentTO;

/**
 * <b>CSV_FormatGeneratorImpl</b><p>
 * Generates report in CSV format.
 * @author Roman Ivanenko
 *
 */
public class CSV_FormatGeneratorImpl implements IFormatGenerator {
	
	/**
     * The logger.
     */
    private static Log logger = LogFactory.getLog(CSV_FormatGeneratorImpl.class);

    /** Field reportFormatName  */
    private static final String reportFormatName = "CSV";
    /** Field contentMIMEType  */
    private static final String contentMIMEType = "text/csv";
    /** Field reportFileExtention  */
    private static final String reportFileExtention = ".csv";

    /**
     * @return the reportFormatName
     */
    public String getReportFormatName() {
        return reportFormatName;
    }

    /**
     * @return the contentMIMEType
     */
    public String getContentMIMEType() {
        return contentMIMEType;
    }

    /**
     * @return the reportFileExtention
     */
    public String getReportFileExtention() {
        return reportFileExtention;
    }

    /**
     * Generate Buffered Report.
     *
     * @param reportConfigTO  a <code>ReportConfigTO</code>
     * @param reportSourseContentTO a <code>ReportSourceContentTO</code>
     * @return Array of byte. Buffered Report.
     *
     * @see net.sf.infrared2.server.report.generator.IFormatGenerator#generateBufferedReport(ReportConfigTO, ReportSourceContentTO)
     */
    public byte[] generateBufferedReport(ReportConfigTO reportConfigTO, ReportSourceContentTO reportSourseContentTO) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        StringArrayToCSVWriter csvWriter = new StringArrayToCSVWriter(baos);

        try {

            CSVReportGeneratorBuilder builder = new CSVReportGeneratorBuilder(reportConfigTO, csvWriter);
            builder.build(reportSourseContentTO);            

        } catch (Exception e) {
        	logger.error(e.getMessage(), e);
        }

        return baos.toByteArray();
    }

}
