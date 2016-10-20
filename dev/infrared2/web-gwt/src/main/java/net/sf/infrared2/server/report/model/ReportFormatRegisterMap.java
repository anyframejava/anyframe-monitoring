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
 * ReportFormatRegisterMap.java		Date created: 18.02.2008
 * Last modified by: $Author: soyon.lim $
 * $Revision: 14286 $	$Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.server.report.model;

import java.util.HashMap;

/**
 * <b>ReportFormatRegisterMap</b><p>
 * Keeps available format.
 * @author Roman Ivanenko
 *
 */
public class ReportFormatRegisterMap extends HashMap<String, ReportFormatTO> {

    /** Field serialVersionUID  */
    private static final long serialVersionUID = -1637994782101248547L;

    /**
     * @param reportFormatName - the name of format of report.
     * @return true if format of report is correct.
     */
    public boolean isFormatRegestered(String reportFormatName) {
        return (this.get(reportFormatName) != null) ? true : false;
    }

}
