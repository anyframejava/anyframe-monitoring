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
 * GraphicServlet.java Date created: 31.01.2008
 * Last modified by: $Author: soyon.lim $
 * $Revision: 14286 $ $Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.server.chart;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.imageio.ImageIO;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;

/**
 * <b>GraphicServlet</b><p>
 * GraphicServlet listens request for chart image, get image from map located in
 * application context and put image to response.
 *
 * @author Roman Ivanenko
 */
public class GraphicServlet extends HttpServlet {
    private static final long serialVersionUID = -6013915536301352313L;

    /** Field logger  */
    private static transient Log logger = LogFactory.getLog(GraphicServlet.class);

    /**
     * smartImageMapAttributeName is a name contract for Servlet Context
     * attribute keeps map of images (SmartImageMap). Default value is
     * "SmartImageMap".
     */
    private String smartImageMapAttributeName = "SmartImageMap";

    /**
     * smartImageMap keeps map of images. Located in Servlet Context.
     */
    private SmartImageMap smartImageMap;

    /**
     * Standard servlet initiation expanded with initiation of image map.
     * @param config a <code>ServletConfig</code>
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext servletContext = config.getServletContext();
        String smartImageMapName = servletContext.getInitParameter("smartImageMapAttributeName");
        smartImageMapName = (smartImageMapName == null) ? this.smartImageMapAttributeName : smartImageMapName;
        smartImageMap = (SmartImageMap) servletContext.getAttribute(smartImageMapName);
        if (smartImageMap == null) {
            smartImageMap = new SmartImageMap();
            servletContext.setAttribute(smartImageMapName, smartImageMap);
        }
    }

    /**
     * Standard servlet doGet expanded with logger.
     * @param request a <code>HttpServletRequest</code>
     * @param response a <code>HttpServletResponse</code>
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logger.debug("doGet invocation marker");
        processRequest(request, response);
    }

    /**
     * Standard servlet doPost expanded with logger.
     * @param request a <code>HttpServletRequest</code>
     * @param response a <code>HttpServletResponse</code>
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logger.debug("doPost invocation marker");
        processRequest(request, response);
    }

    /**
     * processRequest retrives image name from request, gets requested image
     * from map located in application context and put image to response.
     * @param request a <code>HttpServletRequest</code>
     * @param response a <code>HttpServletResponse</code>
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        long startTime = System.currentTimeMillis();
        String chartName = null;
        String sessionId = request.getRequestedSessionId();
        String pathInfo = request.getPathInfo();
        logger.debug("\n\t requested URL=\"" + request.getRequestURL() + "\""
                + "\n\t called with sessionId=" + sessionId);
        if (pathInfo == null) {
            logger.error("\n\t ERROR: Can't get image file name from requested URL:" + "\n\t\""
                    + request.getRequestURL() + "\"");
            return;
        }

        OutputStream out = response.getOutputStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            chartName = pathInfo.substring(1, pathInfo.indexOf("."));
            BufferedImage buffImg = (BufferedImage) smartImageMap.getImage(chartName);
            if (buffImg != null) {
                response.setContentType("image/png");
                // Writing in BAOS to prevent IOException 
                // in case when IE6 aborts request
                ImageIO.write(buffImg, "png", baos);
                out.write(baos.toByteArray());
                out.flush();
            } else
                logger.error("\n\t ERROR: Can't retrive image \"" + chartName
                        + ".png\" from Servlet Context Attribute \"SmartImageMap\". \n");
        } catch (StringIndexOutOfBoundsException e) {
            logger.error("\n\t ERROR: Can't get image name from requested resource \"" + pathInfo
                    + "\".  \n");
        } catch (IOException e) {
            logger.info("\n\t ERROR: Can't write image into output stream. \n" + e.toString());
        } catch (NullPointerException e) {
            logger.error("\n\t ERROR: Can't get image from SmartImageMap. Image \"" + chartName
                    + "\" is absent. \n\t");
        } catch (Exception e) {
            logger.error("\n\t ERROR:" + e.toString());
        } finally {
            out.close();
            baos.close();
        }
        logger.debug("GraphicServlet::processRequest execution time: " + (System.currentTimeMillis() - startTime));
    }

}