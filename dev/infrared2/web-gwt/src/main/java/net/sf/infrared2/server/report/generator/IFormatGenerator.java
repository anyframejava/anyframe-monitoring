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
 * IFormatGenerator.java		Date created: 22.02.2008
 * Last modified by: $Author: soyon.lim $
 * $Revision: 14286 $	$Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.server.report.generator;


import java.io.Serializable;

import net.sf.infrared2.gwt.client.report.ReportConfigTO;
import net.sf.infrared2.server.report.model.ReportSourceContentTO;

/**
 * <b>IFormatGenerator</b><p>
 * Defines API interface for various format generators.
 * @author Roman Ivanenko
 *
 */
public interface IFormatGenerator extends Serializable {

    /**
     * @return the reportFormatName
     */
    public String getReportFormatName();

    /**
     * @return the contentMIMEType
     */
    public String getContentMIMEType();

    /**
     * @return the reportFileExtention
     */
    public String getReportFileExtention();

    /**
     * Generates Buffered Report.
     *
     * @param reportConfigTO a <code>ReportConfigTO</code>
     * @param reportSourseContentTO  a <code>ReportSourceContentTO</code>
     * @return Array of byte. Buffered Report.
     */
    public byte[] generateBufferedReport(ReportConfigTO reportConfigTO, ReportSourceContentTO reportSourseContentTO);

}
