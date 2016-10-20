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
 * ReportServlet.java Date created: 31.01.2008
 * Last modified by: $Author: soyon.lim $
 * $Revision: 14286 $ $Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.server.report;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.infrared2.gwt.client.Constants;
import net.sf.infrared2.gwt.client.report.ReportConfigTO;
import net.sf.infrared2.server.report.model.ReportTO;
import net.sf.infrared2.server.util.CacheUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * ReportServlet listens request for report, get report from report engine in
 * application context and put image to response.
 *
 * @author Roman Ivanenko
 */
public class ReportServlet extends HttpServlet {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -5694797692575398040L;

    /**
     * The logger.
     */
    private static Log logger = LogFactory.getLog(ReportServlet.class);

    /**
     * Standard servlet doGet expanded with logger.
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.debug("doGet invocation marker");
        processRequest(request, response);
    }

    /**
     * Standard servlet doPost expanded with logger.
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.debug("doPost invocation marker");
        processRequest(request, response);
    }

    /**
     * processRequest retrieves report request,
     * gets requested report document,
     * and put report to response.
     * @param request a <code>HttpServletRequest</code>
     * @param response a <code>HttpServletResponse</code>
     * @throws java.io.IOException when I/O problem occured.
     * @throws javax.servlet.ServletException when Servlet inner problem occured.
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.debug("\n\t requested URL=\"" + request.getRequestURL() + "\"" );
        OutputStream out = response.getOutputStream();
        String pathInfo = request.getContextPath()+request.getServletPath()+request.getPathInfo();
        ReportConfigTO reportConfig = getReportConfigByKey(pathInfo, request.getSession());
        try {
            ReportTO reportTO = CacheUtil.getFromReportCache(reportConfig, request.getSession());
            this.setHTTPResponse(response, reportTO);
            out.write(reportTO.getBufferedReport());
            out.flush();
            //CacheUtil.clearReportCache(request.getSession());
        } catch (NullPointerException e) {
            logger.error("\n\t ERROR: Can't create report.",e);
        } catch (IOException e) {
            logger.error("\n\t ERROR: Can't write report document into output stream. \n", e);
        } catch (Exception e) {
            logger.error("\n\t ERROR:" + e.toString(), e);
        } finally {
            out.close();
        }
    }

    /**
     * Method getReportConfigByKey returns ReportConfigTO based on pathInfo.
     *
     * @param pathInfo of type String
     * @param session of type HttpSession
     * @return ReportConfigTO
     */
    private ReportConfigTO getReportConfigByKey(String pathInfo, HttpSession session) {
        return CacheUtil.getReportConfigKey(pathInfo, session);
    }

    /**
     * Method setHTTPResponse sets response.
     *
     * @param response of type HttpServletResponse
     * @param reportTO of type ReportTO
     */
    private void setHTTPResponse(HttpServletResponse response, ReportTO reportTO) {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setBufferSize(reportTO.getBufferedReport().length);
        response.setContentType(reportTO.getReportFormat().getContentMIMEType());

        boolean isHtml = ((reportTO != null) && (reportTO.getReportFormat() != null)
                && (Constants.HTML_FORMAT_NAME.equals(reportTO.getReportFormat().getReportFormatName())));
        String disposition = (isHtml ? "inline" : "attachment");

        response.setHeader("content-disposition", disposition + "; filename=" + reportTO.getReportFileNameWithExtention());
        
        response.setHeader("Expires", "0");
        response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
        response.setHeader("Pragma", "public");
    }

}
