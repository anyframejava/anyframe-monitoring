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
 * ReportGeneratorFactory.java		Date created: 18.02.2008
 * Last modified by: $Author: soyon.lim $
 * $Revision: 14286 $	$Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.server.report.engine;

import net.sf.infrared2.server.report.generator.IFormatGenerator;
import net.sf.infrared2.server.report.generator.impl.CSV_FormatGeneratorImpl;
import net.sf.infrared2.server.report.generator.impl.HTML_FormatGeneratorImpl;
import net.sf.infrared2.server.report.generator.impl.MS_Excel_FormatGeneratorImpl;
import net.sf.infrared2.server.report.generator.impl.PDF_FormatGeneratorImpl;
import net.sf.infrared2.server.report.model.ReportFormatRegisterMap;
import net.sf.infrared2.server.report.model.ReportFormatTO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * <b>ReportGeneratorFactory</b><p>
 * ReportGeneratorFactory provides an instance of requested format generator.
 * @author Roman Ivanenko
 */
public class ReportGeneratorFactory {

    /**
     * The logger.
     */
    private static Log logger = LogFactory.getLog(ReportGeneratorFactory.class);
    /** Field reportGeneratorFactory  */
    private static ReportGeneratorFactory reportGeneratorFactory;
    /** Field formatRegister  */
    private static ReportFormatRegisterMap formatRegister = new ReportFormatRegisterMap();
    /** Field generatorsList  */
    private static ArrayList<IFormatGenerator> generatorsList = new ArrayList<IFormatGenerator>();
    /** Field reportFormatID  */
    private static int reportFormatID = 0;

    /**
     * Constructor.
     */
    private ReportGeneratorFactory() {

        /* Test initiation */
        /* TODO: Replace with plug-in initiation */
        generatorsList.add(new CSV_FormatGeneratorImpl());
        generatorsList.add(new PDF_FormatGeneratorImpl());
        generatorsList.add(new HTML_FormatGeneratorImpl());
        generatorsList.add(new MS_Excel_FormatGeneratorImpl());

        /* Register all known format generators in ReportFormatRegisterMap */
        Iterator<IFormatGenerator> i = generatorsList.iterator();
        IFormatGenerator g;
        while (i.hasNext()) {
            g = i.next();
            createReportFormat(g.getReportFormatName(), g.getContentMIMEType(), g.getReportFileExtention(), g);
        }
    }

    /**
     * Provides an instance of generator factory.
     * @return the reportFormatFactory
     */
    public static ReportGeneratorFactory getInstance() {
        if (reportGeneratorFactory == null) {
            reportGeneratorFactory = new ReportGeneratorFactory();
        }
        return reportGeneratorFactory;
    }

    /**
     * Provides requested format transfer object.
     * @param reportFormatName  a <code>String</code>
     * @return the reportFormat
     */
    public ReportFormatTO getReportFormat(String reportFormatName) {
        if (formatRegister.isFormatRegestered(reportFormatName)) {
            return formatRegister.get(reportFormatName);
        } else {
            logger.error("\n\t ERROR: Requested report format \"" + reportFormatName + "\" is NOT regestered." );
            return null;
        }
    }

    /**
     * Creates report format transfer object and regesters it in format regester map.
     *
     * @param reportFormatName a <code>String</code>
     * @param contentMIMEType  a <code>String</code>
     * @param reportFileExtention a <code>String</code>
     * @param reportGenerator a <code>IFormatGenerator</code>
     * @return the reportFormat
     */
    public synchronized ReportFormatTO createReportFormat(String reportFormatName, String contentMIMEType, String reportFileExtention, IFormatGenerator reportGenerator) {
        ReportFormatTO newReportFormat;
        if (!formatRegister.isFormatRegestered(reportFormatName)) {
            newReportFormat = new ReportFormatTO(++reportFormatID, reportFormatName, contentMIMEType, reportFileExtention, reportGenerator);
            formatRegister.put(reportFormatName, newReportFormat);
            logger.info("\n\t Report format \"" + reportFormatName + "\" is created and regestered." );
            return newReportFormat;
        } else {
            logger.info("\n\t Report format \"" + reportFormatName + "\" is ALREADY  regestered." );
            return formatRegister.get(reportFormatName);
        }
    }

}