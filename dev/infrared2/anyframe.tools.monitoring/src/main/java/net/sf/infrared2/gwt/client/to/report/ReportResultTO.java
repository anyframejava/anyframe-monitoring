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
 * ReportResultTO.java		Date created: 27.03.2008
 * Last modified by: gzgonikov
 * $Revision: 14286 $	Date: 27.03.2008
 */

package net.sf.infrared2.gwt.client.to.report;

import java.io.Serializable;

import net.sf.infrared2.gwt.client.to.IResultStatus;
import net.sf.infrared2.gwt.client.to.status.ResultStatus;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * <b>ReportResultTO</b><p>
 * Transfer object that contains information for getting this report from
 * server.
 * 
 * @author Sergey Evluhin
 */
public class ReportResultTO implements Serializable, IsSerializable, IResultStatus {
    /**
     * Serial version UID
     */
    private static final long serialVersionUID = 2247747485653471336L;

    /** Status result of call. */
    private ResultStatus resultStatus;
    /** Unique key for this report. */
    private String reportKey;

    /**
     * Default constructor.
     */
    public ReportResultTO() {
    }

    /**
     * Create transfer object with predefined resultStatus parameter.
     * 
     * @param resultStatus - status result of call.
     */
    public ReportResultTO(ResultStatus resultStatus) {
        this.resultStatus = resultStatus;
    }

    /**
     * Create transfer object with predefined parameters.
     * 
     * @param resultStatus - status result of call.
     * @param reportKey - unique key for this report.
     */
    public ReportResultTO(ResultStatus resultStatus, String reportKey) {
        this.resultStatus = resultStatus;
        this.reportKey = reportKey;
    }

    /**
     * @return unique key for this report.
     */
    public String getReportKey() {
        return reportKey;
    }

    /**
     * @param reportKey - unique key for this report
     */
    public void setReportKey(String reportKey) {
        this.reportKey = reportKey;
    }

    /**
     * @return status result of call.
     */
    public ResultStatus getResultStatus() {
        return resultStatus;
    }

    /**
     * @param status result of call.
     */
    public void setResultStatus(ResultStatus resultStatus) {
        this.resultStatus = resultStatus;
    }
}
