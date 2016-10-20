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
 * ReportEngine.java		Date created: 17.02.2008
 * Last modified by: $Author: soyon.lim $
 * $Revision: 14286 $	$Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.server.report.engine;

import net.sf.infrared2.gwt.client.report.ReportConfigTO;
import net.sf.infrared2.server.report.ReportConstants;
import net.sf.infrared2.server.report.generator.IFormatGenerator;
import net.sf.infrared2.server.report.model.ReportFormatTO;
import net.sf.infrared2.server.report.model.ReportSourceContentTO;
import net.sf.infrared2.server.report.model.ReportTO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <b>ReportEngine</b><p>
 * ReportEngine provides report generator and
 * set up HTTP response meta data.
 * @author Roman Ivanenko
 *
 */
public class ReportEngine {

    /**
     * The logger.
     */
    private static Log logger = LogFactory.getLog(ReportEngine.class);
    /** Field reportEngine  */
    private static ReportEngine reportEngine;
    /** Field reportTO  */
    private ReportTO reportTO;

    /**
     * @return the reportFormatFactory
     */
    public static ReportEngine getInstance() {
        if (reportEngine == null) {
            reportEngine = new ReportEngine();
        }
        return reportEngine;
    }

    /**
     * generateReport generates report and provide it as byte array.
     * @param reportConfigTO a <code>ReportConfigTO</code>
     * @param reportSourseContentTO a <code>ReportSourceContentTO</code>
     *
     * @return the report as byte array.
     */
    public byte[] generateReport(ReportConfigTO reportConfigTO, ReportSourceContentTO reportSourseContentTO){
        ReportGeneratorFactory reportGeneratorFactory;
        ReportFormatTO reportFormat;
        String reportFormatName;
        IFormatGenerator reportGenerator;

        /* Set report configuration */
        if (reportConfigTO != null) {
            reportFormatName = reportConfigTO.getReportFormatName();
        } else {
            logger.error("\n\t ERROR: Report Configuration is NOT specified");
            return null;
        }

        if (reportSourseContentTO == null) {
            logger.error("\n\t ERROR: Report Sourse Content is NOT specified");
            return null;
        }
        reportGeneratorFactory = ReportGeneratorFactory.getInstance();
        reportFormat = reportGeneratorFactory.getReportFormat(reportFormatName);
        if (reportFormat != null) {

            /* Generate report */
            reportGenerator = reportFormat.getFormatGenerator();
            byte[] bufferedReport = reportGenerator.generateBufferedReport(reportConfigTO, reportSourseContentTO);

            /* Prepare report file name label */
            DateFormat dateFormatter = new SimpleDateFormat(ReportConstants.REPORT_TIME_STAMP);
            String fileName = dateFormatter.format(new Date()) + ReportConstants.REPORT_FILE_NAME_SUFFIX;
            /* Set report TO */
            reportTO = new ReportTO(reportFormat, fileName, bufferedReport);
            return reportTO.getBufferedReport();
        } else {
            logger.error("\n\t ERROR: Requested report format \""+ reportFormatName +"\" is NOT supported");
            reportTO = null;
            return null;
        }
    }

    /**
     * Set up HTTP response metadata.
     * @param response a <code>HttpServletResponse</code>
     */
    public void setHTTPResponse(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setBufferSize(reportTO.getBufferedReport().length);
        response.setContentType(reportTO.getReportFormat().getContentMIMEType());
        response.setHeader("content-disposition", "attachment; filename=" + reportTO.getReportFileNameWithExtention());
        response.setHeader("Expires", "0");
        response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
        response.setHeader("Pragma", "public");
    }

    /**
     * @return the reportTO
     */
    public ReportTO getReportTO() {
        return reportTO;
    }

    /**
     * @param reportTO the reportTO to set
     */
    public void setReportTO(ReportTO reportTO) {
        this.reportTO = reportTO;
    }

}
