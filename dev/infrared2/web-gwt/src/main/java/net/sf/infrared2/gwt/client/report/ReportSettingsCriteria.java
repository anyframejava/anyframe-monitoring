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
 * ReportSettingsModel.java		Date created: 18.02.2008
 * Last modified by: $Author: soyon.lim $
 * $Revision: 14286 $	$Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.gwt.client.report;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

/**
 * <b>ReportSettingsCriteria</b><p>
 * Main class for hold all report settings configuration.
 * @author Andrey Zavgorodniy
 * Copyright Exadel Inc, 2008
 */
public class ReportSettingsCriteria implements Serializable, IsSerializable{
    
    private static ReportConfigTO reportConfigTO;

    private static final long serialVersionUID = -8682879316383626415L;

    /**
     * Constructor.
     */
    public ReportSettingsCriteria() {
        reportConfigTO = ReportConfigTO.getInstance();
    }

    /**
     * @return the disaredReportFormat
     */
    public static ReportConfigTO getDisaredReport() {
        return reportConfigTO;
    }

    /**
     * @param disaredReportFormat the disaredReportFormat to set
     */
    public static void setDisaredReport(ReportConfigTO disaredReportFormat) {
        ReportSettingsCriteria.reportConfigTO = disaredReportFormat;
    }

}
