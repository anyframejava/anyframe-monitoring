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
 * ReportResultTO.java		Date created: 17.02.2008
 * Last modified by: $Author: soyon.lim $
 * $Revision: 14286 $	$Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.server.report.model;

import java.io.Serializable;


/**
 * <b>ReportTO</b><p>
 * Keeps data of particular report.
 * @author Roman Ivanenko
 *
 */
public class ReportTO implements Serializable {

    /** Field reportFormat  */
    private ReportFormatTO reportFormat;
    /** Field reportFileName  */
    private String reportFileName;
    /** Field bufferedReport  */
    private byte[] bufferedReport;
    /** Field serialVersionUID  */
    private static final long serialVersionUID = -7674343595234978442L;

    /**
     * Constructor.
     *
     * @param reportFormat the reportFormat to set
     * @param reportFileName the reportFileName to set
     * @param bufferedReport the bufferedReport to set
     */
    public ReportTO(ReportFormatTO reportFormat, String reportFileName, byte[] bufferedReport){
        this.reportFormat = reportFormat;
        this.reportFileName = reportFileName;
        this.bufferedReport = bufferedReport;
    }

    /**
     * Constructor ReportTO creates a new ReportTO instance.
     */
    public ReportTO() {
    }

    /**
     * @return the reportFormat
     */
    public ReportFormatTO getReportFormat() {
        return reportFormat;
    }

    /**
     * @param reportFormat the reportFormat to set
     */
    public void setReportFormat(ReportFormatTO reportFormat) {
        this.reportFormat = reportFormat;
    }

    /**
     * @return the reportFileName
     */
    public String getReportFileName() {
        return reportFileName;
    }

    /**
     * @param reportFileName the reportFileName to set
     */
    public void setReportFileName(String reportFileName) {
        this.reportFileName = reportFileName;
    }

    /**
     * @return Report File Name With Extention
     */
    public String getReportFileNameWithExtention() {
        return reportFileName + reportFormat.getReportFileExtention();
    }

    /**
     * @return the bufferedReport
     */
    public byte[] getBufferedReport() {
        return bufferedReport;
    }

    /**
     * @param bufferedReport the bufferedReport to set
     */
    public void setBufferedReport(byte[] bufferedReport) {
        this.bufferedReport = bufferedReport;
    }

}
