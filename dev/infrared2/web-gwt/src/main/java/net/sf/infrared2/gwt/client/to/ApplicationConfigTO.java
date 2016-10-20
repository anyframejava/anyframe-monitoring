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
 * ApplicationConfigTO.java Date created: 22.01.2008
 * Last modified by: $Author: soyon.lim $ 
 * $Revision: 14286 $ $Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.gwt.client.to;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

import net.sf.infrared2.gwt.client.to.status.ResultStatus;

/**
 * <b>ApplicationConfigTO</b>
 * <p>
 * User application configuration transfer object bean.
 * 
 * @author Andrey Zavgorodniy
 */
public class ApplicationConfigTO implements Serializable, IsSerializable, IResultStatus {

    /**
     * Generated serial version UID
     */
    private static final long serialVersionUID = 1104678670674634956L;

    private ResultStatus resultStatus;

    private ApplicationEntryTO[] applicationList;

    /**
     * Divided by applications flag
     */
    private boolean dividedByApplications = true;

    /** Field isGraphLabelEnabled */
    private boolean isGraphLabelEnabled = false;

    /**
     * Live Data selected flag
     */
    private boolean liveDate = true;

    /**
     * Archive Date selected flag
     */
    private boolean archiveDate = false;

    /**
     * Period from select monitoring
     */
    private String startDate;
    
    /** The start time. */
    private String startTime;

    /**
     * Period to select monitoring
     */
    private String endDate;
    
    /** The end time. */
    private String endTime;
    
    /** The reset interval min. */
    private Long resetIntervalMin;

    /**
     * Default constructor.
     */
    public ApplicationConfigTO() {
    }

    public ApplicationConfigTO(boolean liveData, boolean dividedByApplications) {
        this.liveDate = liveData;
        this.dividedByApplications = dividedByApplications;
    }

    /**
     * @return the applicationList
     */
    public ApplicationEntryTO[] getApplicationList() {
        return this.applicationList;
    }

    /**
     * @param applicationList the applicationList to set
     */
    public void setApplicationList(ApplicationEntryTO[] applicationList) {
        this.applicationList = applicationList;
    }

    /**
     * @return the dividedByApplications
     */
    public boolean isDividedByApplications() {
        return this.dividedByApplications;
    }

    /**
     * @param dividedByApplications the dividedByApplications to set
     */
    public void setDividedByApplications(boolean dividedByApplications) {
        this.dividedByApplications = dividedByApplications;
    }

    /**
     * @return the liveDate
     */
    public boolean isLiveDate() {
        return this.liveDate;
    }

    /**
     * @param liveDate the liveDate to set
     */
    public void setLiveDate(boolean liveData) {
        this.liveDate = liveData;
    }

    /**
     * @return the archiveDate
     */
    public boolean isArchiveDate() {
        return archiveDate;
    }

    /**
     * @param archiveDate the archiveDate to set
     */
    public void setArchiveDate(boolean archiveDate) {
        this.archiveDate = archiveDate;
    }

    /**
     * @return the startData
     */
    public String getStartDate() {
        return this.startDate;
    }
    
    /**
     * Gets the start date time.
     * 
     * @return the start date time
     */
    public String getStartDateTime(){
        return (startDate + " " + startTime);
    }

    /**
     * @param startData the startData to set
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the endData
     */
    public String getEndDate() {
        return this.endDate;
    }

    /**
     * @param endData the endData to set
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
    
    /**
     * Gets the end date time.
     * 
     * @return the end date time
     */
    public String getEndDateTime(){
        return (endDate + " " + endTime);
    }

    /**
     * Gets the start time.
     * 
     * @return the start time
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * Sets the start time.
     * 
     * @param startTime the new start time
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * Gets the end time.
     * 
     * @return the end time
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * Sets the end time.
     * 
     * @param endTime the new end time
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    /**
     * Method getResultStatus returns the resultStatus of this IResultStatus object.
     * 
     * @return the resultStatus (type ResultStatus) of this IResultStatus object.
     */
    public ResultStatus getResultStatus() {
        return resultStatus;
    }

    /**
     * Method setResultStatus sets the resultStatus of this IResultStatus object.
     * 
     * @param resultStatus the resultStatus of this IResultStatus object.
     * 
     */
    public void setResultStatus(ResultStatus resultStatus) {
        this.resultStatus = resultStatus;
    }

    /**
     * Method isGraphLabelEnabled returns the graphLabelEnabled of this ApplicationConfigTO object.
     * 
     * @return the graphLabelEnabled (type boolean) of this ApplicationConfigTO object.
     */
    public boolean isGraphLabelEnabled() {
        return isGraphLabelEnabled;
    }

    /**
     * Method setGraphLabelEnabled sets the graphLabelEnabled of this ApplicationConfigTO object.
     * 
     * @param graphLabelEnabled the graphLabelEnabled of this ApplicationConfigTO object.
     * 
     */
    public void setGraphLabelEnabled(boolean graphLabelEnabled) {
        isGraphLabelEnabled = graphLabelEnabled;
    }

    /**
     * Gets the reset interval min.
     * 
     * @return the reset interval min
     */
    public Long getResetIntervalMin() {
        return resetIntervalMin;
    }

    /**
     * Sets the reset interval min.
     * 
     * @param resetIntervalMin the new reset interval min
     */
    public void setResetIntervalMin(Long resetIntervalMin) {
        this.resetIntervalMin = resetIntervalMin;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    public String toString() {
        String sep = "\n";
        String result = "ApplicationConfigTO : [" + sep;
        result += (resultStatus != null ? resultStatus.toString() : "") + sep;
        result+= "instanceList : [ " + sep;
        if (applicationList != null) {
            for (int i=0; i<applicationList.length;i++){
                result += applicationList[i].toString() + sep;
            }
        }
        result += "]";
        result += "]";
        return result;
    }
}
