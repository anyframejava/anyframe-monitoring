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
 * DesiredReportFormat.java		Date created: 18.02.2008
 * Last modified by: $Author: soyon.lim $
 * $Revision: 14286 $	$Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.gwt.client.report;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;


/**
 * <b>ReportConfigTO</b><p>
 * Hold desired report format settings.
 * @author Andrey Zavgorodniy
 * Copyright Exadel Inc, 2008
 */
public class ReportConfigTO implements Serializable, IsSerializable {

    private static final long serialVersionUID = -1661050040992342707L;
    /* for server side use only */
    private String keyInReportMap;
    
    /*DesiredReportFormat*/
    private boolean excelFormat = true;
    private boolean pdfFormat;
    private boolean htmlFormat;
    private boolean csvFormat;
    
    /*IncludeModes*/
    private boolean absoluteModuleMode = true;
    private boolean hierarchicalModuleMode = false;
    
    /*IncludeLevels*/
    private boolean applications = true;
    private boolean currentlySelected = true;
    private boolean allDisplayed;
    private boolean layer;
    private boolean inclusiveMode;
    private boolean exclusiveMode;
    private boolean inclusiveExclusiveMode;
    
    /*IncludeInformation*/
    private String queries="5";
    private boolean statistics;
    private boolean diagrams;
    
    private String currentlySelectedString;

    private String reportFormatName;

    /** Field mergedApplicationTitle cotains title when (!isDividedByApplications)
     *  else equals null    
     */
    private String mergedApplicationTitle;
    
    public static ReportConfigTO disaredFormat = new ReportConfigTO();

    /**
     * 
     * {@inheritDoc}
     */
    public boolean equals(Object obj) {
        if (this==obj) return true;
        if((obj == null) || (!(obj instanceof ReportConfigTO))) return false;
        ReportConfigTO to = (ReportConfigTO)obj;
        if (this.excelFormat==to.excelFormat){
            if (this.pdfFormat==to.pdfFormat){
                if (this.htmlFormat==to.htmlFormat){
                    if (this.csvFormat==to.csvFormat){
                        if (this.absoluteModuleMode==to.absoluteModuleMode){
                            if (this.hierarchicalModuleMode==to.hierarchicalModuleMode){
                                if (this.applications==to.applications){
                                    if (this.currentlySelected==to.currentlySelected){
                                        if (this.allDisplayed==to.allDisplayed){
                                            if (this.layer==to.layer){
                                                if (this.inclusiveMode==to.inclusiveMode){
                                                    if (this.exclusiveMode==to.exclusiveMode){
                                                        if (this.inclusiveExclusiveMode==to.inclusiveExclusiveMode){
                                                            if (this.statistics==to.statistics){
                                                                if (this.diagrams==to.diagrams){
                                                                    if ((this.queries!=null)?this.queries.equals(to.getQueries()):to.getQueries()==null){
                                                                        if ((this.reportFormatName!=null)?this.reportFormatName.equals(to.getReportFormatName()):to.getReportFormatName()==null){
                                                                            return true;
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * 
     * {@inheritDoc}
     */
    public int hashCode() {
        int hash = 4;
        hash = 7*hash+(excelFormat ? 1 : 0);
        hash = 7*hash+(pdfFormat ? 1 : 0);
        hash = 7*hash+(htmlFormat ? 1 : 0);
        hash = 7*hash+(csvFormat ? 1 : 0);
        hash = 7*hash+(absoluteModuleMode ? 1 : 0);
        hash = 7*hash+(hierarchicalModuleMode ? 1 : 0);
        hash = 7*hash+(applications ? 1 : 0);
        hash = 7*hash+(currentlySelected ? 1 : 0);
        hash = 7*hash+(allDisplayed ? 1 : 0);
        hash = 7*hash+(layer ? 1 : 0);
        hash = 7*hash+(inclusiveMode ? 1 : 0);
        hash = 7*hash+(exclusiveMode ? 1 : 0);
        hash = 7*hash+(inclusiveExclusiveMode ? 1 : 0);
        hash = 7*hash+(queries!=null ? queries.hashCode() : 0);
        hash = 7*hash+(statistics ? 1 : 0);
        hash = 7*hash+(diagrams ? 1 : 0);
        hash = 7*hash+(reportFormatName!=null ? reportFormatName.hashCode() : 0);
        return hash;
    }

    /**
     * Get current instance of DesiredReportFormat bean. 
     * @return the DesiredReportFormat bean
     */
    public static ReportConfigTO getInstance() {
        return disaredFormat;
    }
    
    /**
     * @return the excelFormat
     */
    public boolean isExcelFormat() {
        return this.excelFormat;
    }
    /**
     * @param excelFormat the excelFormat to set
     */
    public void setExcelFormat(boolean excelFormat) {
        this.excelFormat = excelFormat;
    }
    /**
     * @return the pdfFormat
     */
    public boolean isPdfFormat() {
        return this.pdfFormat;
    }
    /**
     * @param pdfFormat the pdfFormat to set
     */
    public void setPdfFormat(boolean pdfFormat) {
        this.pdfFormat = pdfFormat;
    }
    /**
     * @return the htmlFormat
     */
    public boolean isHtmlFormat() {
        return this.htmlFormat;
    }
    /**
     * @param htmlFormat the htmlFormat to set
     */
    public void setHtmlFormat(boolean htmlFormat) {
        this.htmlFormat = htmlFormat;
    }
    /**
     * @return the csvFormat
     */
    public boolean isCsvFormat() {
        return this.csvFormat;
    }
    /**
     * @param csvFormat the csvFormat to set
     */
    public void setCsvFormat(boolean csvFormat) {
        this.csvFormat = csvFormat;
    }
    
    /**
     * @return the queries
     */
    public String getQueries() {
        return this.queries;
    }
    /**
     * @param queries the queries to set
     */
    public void setQueries(String queries) {
        this.queries = queries;
    }
    /**
     * @return the statistics
     */
    public boolean isStatistics() {
        return this.statistics;
    }
    /**
     * @param statistics the statistics to set
     */
    public void setStatistics(boolean statistics) {
        this.statistics = statistics;
    }
    /**
     * @return the diagrams
     */
    public boolean isDiagrams() {
        return this.diagrams;
    }
    /**
     * @param diagrams the diagrams to set
     */
    public void setDiagrams(boolean diagrams) {
        this.diagrams = diagrams;
    }

    /**
     * @return the applications
     */
    public boolean isApplications() {
        return this.applications;
    }

    /**
     * @param applications the applications to set
     */
    public void setApplications(boolean applications) {
        this.applications = applications;
    }

    /**
     * @return the currentlySelected
     */
    public boolean isCurrentlySelected() {
        return this.currentlySelected;
    }

    /**
     * @param currentlySelected the currentlySelected to set
     */
    public void setCurrentlySelected(boolean currentlySelected) {
        this.currentlySelected = currentlySelected;
    }

    /**
     * @return the allDisplayed
     */
    public boolean isAllDisplayed() {
        return this.allDisplayed;
    }

    /**
     * @param allDisplayed the allDisplayed to set
     */
    public void setAllDisplayed(boolean allDisplayed) {
        this.allDisplayed = allDisplayed;
    }

    /**
     * @return the layer
     */
    public boolean isLayer() {
        return this.layer;
    }

    /**
     * @param layer the layer to set
     */
    public void setLayer(boolean layer) {
        this.layer = layer;
    }

    /**
     * @return the inclusiveMode
     */
    public boolean isInclusiveMode() {
        return this.inclusiveMode;
    }

    /**
     * @param inclusiveMode the inclusiveMode to set
     */
    public void setInclusiveMode(boolean inclusiveMode) {
        this.inclusiveMode = inclusiveMode;
    }

    /**
     * @return the exclusiveMode
     */
    public boolean isExclusiveMode() {
        return this.exclusiveMode;
    }

    /**
     * @param exclusiveMode the exclusiveMode to set
     */
    public void setExclusiveMode(boolean exclusiveMode) {
        this.exclusiveMode = exclusiveMode;
    }

    /**
     * @return the inclusiveExclusiveMode
     */
    public boolean isInclusiveExclusiveMode() {
        return this.inclusiveExclusiveMode;
    }

    /**
     * @param inclusiveExclusiveMode the inclusiveExclusiveMode to set
     */
    public void setInclusiveExclusiveMode(boolean inclusiveExclusiveMode) {
        this.inclusiveExclusiveMode = inclusiveExclusiveMode;
    }

    /**
     * @return the currentlySelectedString
     */
    public String getCurrentlySelectedString() {
        return this.currentlySelectedString;
    }

    /**
     * @param currentlySelectedString the currentlySelectedString to set
     */
    public void setCurrentlySelectedString(String currentlySelectedString) {
        this.currentlySelectedString = currentlySelectedString;
    }

    /**
     * @return the absoluteModuleMode
     */
    public boolean isAbsoluteModuleMode() {
        return this.absoluteModuleMode;
    }
    /**
     * @param absoluteModuleMode the absoluteModuleMode to set
     */
    public void setAbsoluteModuleMode(boolean absoluteModuleMode) {
        this.absoluteModuleMode = absoluteModuleMode;
    }
    /**
     * @return the hierarchicalModuleMode
     */
    public boolean isHierarchicalModuleMode() {
        return this.hierarchicalModuleMode;
    }
    /**
     * @param hierarchicalModuleMode the hierarchicalModuleMode to set
     */
    public void setHierarchicalModuleMode(boolean hierarchicalModuleMode) {
        this.hierarchicalModuleMode = hierarchicalModuleMode;
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

    public String getKeyInReportMap() {
        return keyInReportMap;
    }

    public void setKeyInReportMap(String keyInReportMap) {
        this.keyInReportMap = keyInReportMap;
    }

    /**
     * Method getMergedApplicationTitle returns the mergedApplicationTitle of this ReportConfigTO object.
     *
     * @return the mergedApplicationTitle (type String) of this ReportConfigTO object.
     */
    public String getMergedApplicationTitle() {
        return mergedApplicationTitle;
    }

    /**
     * Method setMergedApplicationTitle sets the mergedApplicationTitle of this ReportConfigTO object.
     *
     * @param mergedApplicationTitle the mergedApplicationTitle of this ReportConfigTO object.
     *
     */
    public void setMergedApplicationTitle(String mergedApplicationTitle) {
        this.mergedApplicationTitle = mergedApplicationTitle;
    }
}
