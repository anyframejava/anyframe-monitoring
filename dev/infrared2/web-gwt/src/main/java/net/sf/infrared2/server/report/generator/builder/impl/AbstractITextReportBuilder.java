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
 * AbstractITextReportBuilder.java		Date created: 21.04.2008
 * Last modified by: $Author: soyon.lim $
 * $Revision: 14286 $	$Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.server.report.generator.builder.impl;

import java.io.IOException;

import net.sf.infrared2.gwt.client.report.ReportConfigTO;
import net.sf.infrared2.server.report.generator.builder.AbstractGeneratorBuilder;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.codec.PngImage;

/**
 * <b>AbstractITextReportBuilder</b><p>
 * Class for generate HTML and PDF reports uses IText library.
 * @author Andrey Zavgorodniy
 * Copyright Exadel Inc, 2008
 */
public abstract class AbstractITextReportBuilder extends AbstractGeneratorBuilder{
    /** Field CHAPTER_FONT */
    protected static final Font CHAPTER_FONT = new Font(Font.HELVETICA, 16);
    /** Field SECTION_FONT */
    protected static final Font SECTION_FONT = new Font(Font.TIMES_ROMAN, 14, Font.ITALIC);
    /** Field PARAGRAPH_FONT */
    protected static final Font PARAGRAPH_FONT = new Font(Font.TIMES_ROMAN, 12);
    /** Field TEXT_FONT */
    protected static final Font TEXT_FONT = new Font(Font.TIMES_ROMAN, 10);
    /** Field TABLE_HEADER_FONT */
    protected static final Font TABLE_HEADER_FONT = new Font(Font.TIMES_ROMAN, 10, Font.BOLD);

    /** Field document */
    protected Document document;
    
    /**
     * Default constructor.
     * @param reportConfigTO - the <code>ReportConfigTO</code> transfer object hold all settings for report
     */
    protected AbstractITextReportBuilder(ReportConfigTO reportConfigTO) {
        super(reportConfigTO);
    }
    
    /**
     * Method getCell returns cell based on chunk.
     * 
     * @param chunk of type Chunk
     * @return Cell
     * @throws BadElementException when
     */
    protected Cell getCell(Chunk chunk) throws BadElementException {
        Cell cell = new Cell(chunk);
        cell.setHorizontalAlignment(Cell.ALIGN_CENTER);
        cell.setVerticalAlignment(Cell.ALIGN_MIDDLE);
        cell.setMaxLines(5);
        return cell;
    }

    /**
     * Method getHeaderCell returns header cell based on chunk.
     * 
     * @param chunk of type Chunk
     * @return Cell
     * @throws BadElementException when
     */
    protected Cell getHeaderCell(Chunk chunk) throws BadElementException {
        Cell cell = new Cell(chunk);
        cell.setHorizontalAlignment(Cell.ALIGN_CENTER);
        cell.setVerticalAlignment(Cell.ALIGN_MIDDLE);
        cell.setHeader(true);
        return cell;
    }

    /**
     * Method getHeaderCell returns header cell for PdfPTable based on title.
     * @param title - the text for cell value 
     * @return PdfPCell - the well formed cell
     */
    protected PdfPCell getPDFHeaderCell(String title) {
        PdfPCell cell = new PdfPCell(new Paragraph(title, TABLE_HEADER_FONT));
        cell.setHorizontalAlignment(Cell.ALIGN_CENTER);
        cell.setVerticalAlignment(Cell.ALIGN_MIDDLE);
        return cell;
    }
    
    /**
     * Method normalizeValue adapt Object to String.
     * 
     * @param object of type Object
     * @return String
     */
    protected String normalizeValue(Object object) {
        String value = object.toString();

        try {
            Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return value;
        }

        final int indexOf = value.indexOf(".");
        if (indexOf > 0 && value.substring(indexOf).length() > 3) {
            value = value.substring(0, indexOf + 2);
        }

        return value;
    }

    /**
     * Method returns PNG Image.
     * 
     * @param url of type String
     * @return Image
     * @throws BadElementException when
     * @throws IOException when
     */
    protected Image getImage(String url) throws BadElementException, IOException {
        Image im;
        if (getReportConfigTO().isHtmlFormat()) im = PngImage.getImage(url);
        else im = PngImage.getImage(getImageByUrl(url));
        im.setAlignment(Image.ALIGN_CENTER);
        return im;
    }
    
    /**
     * Method newPage create new page.
     * 
     * @throws DocumentException when
     */
    protected void newPage() throws DocumentException {
        document.add(Chunk.NEXTPAGE);
    }
    
}
