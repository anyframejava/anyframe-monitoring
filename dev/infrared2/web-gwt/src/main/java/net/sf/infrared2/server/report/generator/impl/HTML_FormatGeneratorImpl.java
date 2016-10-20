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
 * HTML_FormatGeneratorImpl.java		Date created: 17.02.2008
 * Last modified by: $Author: soyon.lim $
 * $Revision: 14286 $	$Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.server.report.generator.impl;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.html.HtmlWriter;

import net.sf.infrared2.gwt.client.Constants;
import net.sf.infrared2.gwt.client.report.ReportConfigTO;
import net.sf.infrared2.server.report.generator.IFormatGenerator;
import net.sf.infrared2.server.report.generator.builder.impl.HtmlReportGenerationBuilder;
import net.sf.infrared2.server.report.model.ReportSourceContentTO;

import org.apache.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

/**
 * <b>HTML_FormatGeneratorImpl</b><p>
 * Generates report in HTML format.
 * 
 * @author Roman Ivanenko
 * @author Evluhin Sergey
 */
public class HTML_FormatGeneratorImpl implements IFormatGenerator {

    private static final long serialVersionUID = 3593563992309516083L;
    /** Field log */
    private static final transient Logger log = Logger.getLogger(HTML_FormatGeneratorImpl.class);
    /** Field CONTENT_MIME_TYPE */
    private static final String CONTENT_MIME_TYPE = "text/html";
    /** Field REPORT_FILE_EXTENTION */
    private static final String REPORT_FILE_EXTENTION = ".html";

    /**
     * @return the report format name.
     */
    public String getReportFormatName() {
        return Constants.HTML_FORMAT_NAME;
    }

    /**
     * @return the content MIME type.
     */
    public String getContentMIMEType() {
        return CONTENT_MIME_TYPE;
    }

    /**
     * @return the report file extention.
     */
    public String getReportFileExtention() {
        return REPORT_FILE_EXTENTION;
    }

    /**
     * Generate Buffered Report.
     * 
     * @param reportConfigTO a <code>ReportConfigTO</code>
     * @param reportSourseContentTO a <code>ReportSourceContentTO</code>
     * @return Array of byte. Buffered Report.
     * @see net.sf.infrared2.server.report.generator.IFormatGenerator#generateBufferedReport(ReportConfigTO,
     *      ReportSourceContentTO)
     */
    public byte[] generateBufferedReport(ReportConfigTO reportConfigTO,
                    ReportSourceContentTO reportSourseContentTO) {

        Document document = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            HtmlWriter writer = HtmlWriter.getInstance(document, baos);
            document.open();

            HtmlReportGenerationBuilder builder = new HtmlReportGenerationBuilder(
                            document, reportConfigTO, writer);
            builder.build(reportSourseContentTO);

        } catch (DocumentException e) {
            log.error(e.getMessage(), e);
        } catch (MalformedURLException e) {
            log.error(e.getMessage(), e);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } catch (Exception e) {
        	log.error(e.getMessage(), e);
        }

        document.close();

        byte[] bufferedReport = baos.toByteArray();
        // baos.close();
        return bufferedReport;
    }

}
