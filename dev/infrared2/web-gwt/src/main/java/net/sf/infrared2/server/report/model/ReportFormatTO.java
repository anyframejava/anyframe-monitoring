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
 * ReportFormatTO.java		Date created: 17.02.2008
 * Last modified by: $Author: soyon.lim $
 * $Revision: 14286 $	$Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.server.report.model;


import java.io.Serializable;

import net.sf.infrared2.server.report.generator.IFormatGenerator;

/**
 * <b>ReportFormatTO</b><p>
 * Keeps data about particular report format.
 * @author Roman Ivanenko
 *
 */
public class ReportFormatTO implements Serializable {

    /** Field reportFormatID  */
    private int reportFormatID;
    /** Field reportFormatName  */
    private String reportFormatName;
    /** Field contentMIMEType  */
    private String contentMIMEType;
    /** Field reportFileExtention  */
    private String reportFileExtention;
    /** Field formatGenerator  */
    private IFormatGenerator formatGenerator;
    /** Field serialVersionUID  */
    private static final long serialVersionUID = 1898493215389960898L;

    /**
     * Constructor.
     *
     * @param reportFormatID the reportFormatID to set
     * @param reportFormatName the reportFormatName to set
     * @param contentMIMEType  the contentMIMEType to set
     * @param reportFileExtention the reportFileExtention to set
     * @param formatGenerator the formatGenerator to set
     */
    public ReportFormatTO(int reportFormatID, String reportFormatName, String contentMIMEType, String reportFileExtention, IFormatGenerator formatGenerator){
        this.reportFormatID = reportFormatID;
        this.reportFormatName = reportFormatName;
        this.contentMIMEType = contentMIMEType;
        this.reportFileExtention = reportFileExtention;
        this.formatGenerator = formatGenerator;
    }

    /**
     * Constructor ReportFormatTO creates a new ReportFormatTO instance.
     */
    public ReportFormatTO() {
    }

    /**
     * @return the reportFormatID
     */
    public int getReportFormatID() {
        return reportFormatID;
    }

    /**
     * @param reportFormatID the reportFormatID to set
     */
    public void setReportFormatID(int reportFormatID) {
        this.reportFormatID = reportFormatID;
    }

    /**
     * @return the reportFormatName
     */
    public String getReportFormatName() {
        return reportFormatName;
    }

    /**
     * @param reportFormatName the reportFormatName to set
     */
    public void setReportFormatName(String reportFormatName) {
        this.reportFormatName = reportFormatName;
    }

    /**
     * @return the contentMIMEType
     */
    public String getContentMIMEType() {
        return contentMIMEType;
    }

    /**
     * @param contentMIMEType the contentMIMEType to set
     */
    public void setContentMIMEType(String contentMIMEType) {
        this.contentMIMEType = contentMIMEType;
    }

    /**
     * @return the reportFileExtention
     */
    public String getReportFileExtention() {
        return reportFileExtention;
    }

    /**
     * @param reportFileExtention the reportFileExtention to set
     */
    public void setReportFileExtention(String reportFileExtention) {
        this.reportFileExtention = reportFileExtention;
    }

    /**
     * @return the reportGenerator
     */
    public IFormatGenerator getFormatGenerator() {
        return formatGenerator;
    }

    /**
     * @param formatGenerator the reportGenerator to set
     */
    public void setFormatGenerator(IFormatGenerator formatGenerator) {
        this.formatGenerator = formatGenerator;
    }

}
