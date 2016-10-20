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
 * PDF_FormatGeneratorImpl.java		Date created: 17.02.2008
 * Last modified by: $Author: soyon.lim $
 * $Revision: 14286 $	$Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.server.report.generator.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import net.sf.infrared2.gwt.client.Constants;
import net.sf.infrared2.gwt.client.report.ReportConfigTO;
import net.sf.infrared2.server.report.generator.IFormatGenerator;
import net.sf.infrared2.server.report.generator.builder.impl.PdfReportGenerationBuilder;
import net.sf.infrared2.server.report.model.ReportSourceContentTO;

import org.apache.log4j.Logger;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfWriter;

/**
 * <b>PDF_FormatGeneratorImpl</b><p>
 * Generates report in PDF format.
 * 
 * @author Roman Ivanenko
 * @author Evluhin Sergey
 */
public class PDF_FormatGeneratorImpl implements IFormatGenerator {

    private static final long serialVersionUID = -3160233098349564791L;
    /** Field log  */
    private static final Logger log = Logger.getLogger(PDF_FormatGeneratorImpl.class);
    /** Field contentMIMEType  */
    private static final String contentMIMEType = "application/pdf";
    /** Field reportFileExtention  */
    private static final String reportFileExtention = ".pdf";

    /**
     * @return the reportFormatName
     */
    public String getReportFormatName() {
        return Constants.PDF_FORMAT_NAME;
    }

    /**
     * @return the contentMIMEType
     */
    public String getContentMIMEType() {
        return contentMIMEType;
    }

    /**
     * @return the reportFileExtention
     */
    public String getReportFileExtention() {
        return reportFileExtention;
    }

    /**
     * Generate Buffered Report.
     *
     * @param reportConfigTO a <code>ReportConfigTO</code>
     * @param reportSourseContentTO a <code>ReportSourceContentTO</code>
     * @return Array of byte. Buffered Report.
     *
     * @see net.sf.infrared2.server.report.generator.IFormatGenerator#generateBufferedReport(ReportConfigTO, ReportSourceContentTO)
     */
    public byte[] generateBufferedReport(ReportConfigTO reportConfigTO,
                    ReportSourceContentTO reportSourseContentTO) {

        Document document = new Document(PageSize.A2.rotate());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            PdfWriter.getInstance(document, baos);
            document.open();

            PdfReportGenerationBuilder builder = new PdfReportGenerationBuilder(document, reportConfigTO);
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

        // TODO remove this test stub line.
        byte[] bufferedReport = baos.toByteArray();
        // baos.close();
        return bufferedReport;
    }

}
