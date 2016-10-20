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
 * InfraRedListenerServlet.java Date created: 22.01.2008
 * Last modified by: $Author: soyon.lim $
 * $Revision: 14457 $ $Date: 2009-06-08 16:45:33 +0900 (월, 08 6월 2009) $
 */
package net.sf.infrared2.server.util;

import java.io.IOException;
import java.net.BindException;
import java.util.Iterator;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.infrared.collector.Collector;
import net.sf.infrared.collector.CollectorConfig;
import net.sf.infrared.collector.impl.CollectorImpl;
import net.sf.infrared2.gwt.server.service.SIRServiceStubImpl;
import net.sf.infrared2.server.Constant;
import net.sf.infrared2.server.chart.SmartImageMap;

import org.apache.log4j.Logger;

/**
 * <b>InfraRedListenerServlet</b><p>
 * Class InfraRedListenerServlet initiate starting of applications, initialize global stores.
 *
 * @author kaushal.kumar (Tavant Technologies)
 * @author modified by gzgonikov
 */
public class InfraRedListenerServlet extends HttpServlet {

    /** Field PORT_KEY  */
    public static final String PORT_KEY = "port";

    /** Field DEFAULT_PORT  */
    public static final int DEFAULT_PORT = 7777;

    /** Field logger  */
    private static final transient Logger logger = Logger.getLogger(InfraRedListenerServlet.class.getName());

    private int port = DEFAULT_PORT;

    /** The Constant RESET_CACHE_INTERVAL. */
    private static final String RESET_CACHE_INTERVAL = "resetCacheInterval";

    /**
     * Method init initiates global variables.
     *
     * @param config of type ServletConfig
     * @throws ServletException when
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        initCollector(config);
        resetResetStatisticTimer(config);
        initGraphic(config);
        initDataMode(config);
    }

    /**
     * Method initDataMode set mode of data fetching.
     *
     * @param config of type ServletConfig
     */
    private void initDataMode(ServletConfig config) {
        ServletContext servletContext = config.getServletContext();
        String isRealMode = servletContext.getInitParameter(Constant.IS_REAL_MODE);
        SIRServiceStubImpl.setRealMode("true".equals(isRealMode));
        String isServerCacheEnabled = servletContext.getInitParameter(Constant.SERVER_CACHE_ENABLED);
        CacheUtil.setServerCasheEnabled("true".equals(isServerCacheEnabled));
    }

    /**
     * Method initGraphic initiate graphics.
     *
     * @param config of type ServletConfig
     */
    private void initGraphic(ServletConfig config) {
        ServletContext servletContext = config.getServletContext();
        String smartImageMapName = servletContext.getInitParameter(Constant.smartImageMapContextAttributeName);
        smartImageMapName = (smartImageMapName == null) ? Constant.smartImageMapAttributeName : smartImageMapName;
        GraphicUtil.setSmartImageMap((SmartImageMap) servletContext.getAttribute(smartImageMapName));

        if (GraphicUtil.getSmartImageMap() == null) {
            GraphicUtil.setSmartImageMap(new SmartImageMap());
            servletContext.setAttribute(smartImageMapName, GraphicUtil.getSmartImageMap());
        }

        /* Set default value of pie3D Chart Dimension */
        GraphicUtil.getChartDimensionsMap().put(Constant.PIE3D_CHART_HEIGHT_LABEL, 250);
        GraphicUtil.getChartDimensionsMap().put(Constant.PIE3D_CHART_WIDTH_LABEL, 540);

        /* Set default value of Bar Chart Dimension */
        GraphicUtil.getChartDimensionsMap().put(Constant.BAR_CHART_HEIGHT_LABEL, 200);
        GraphicUtil.getChartDimensionsMap().put(Constant.BAR_CHART_WIDTH_LABEL, 500);

        String initParam;
        Iterator iterator = GraphicUtil.getChartDimensionsMap().keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next().toString();
            initParam = servletContext.getInitParameter(key);
            try {
                GraphicUtil.getChartDimensionsMap().put(key, Integer.valueOf(initParam));
                logger.debug("\n\t Set \"" + key + "\" = \"" + initParam + "\"");
            } catch (NumberFormatException e) {
                logger.info("\n\t ERROR: Can't convert initial Chart Dimension parameter." + "\n\t Set default value = "
                        + GraphicUtil.getChartDimensionsMap().get(key) + "\n");
            }
        }

        /* Set value for image time out */
        GraphicUtil.getChartDimensionsMap().put(Constant.IMAGE_TIME_OUT, 20);
        initParam = servletContext.getInitParameter(Constant.IMAGE_TIME_OUT);
        try {
            GraphicUtil.getChartDimensionsMap().put(Constant.IMAGE_TIME_OUT, Integer.valueOf(initParam));
            logger.debug("\n\t Set \"" + Constant.IMAGE_TIME_OUT + "\" = \"" + initParam + "\"");
        } catch (NumberFormatException e) {
            logger.info("\n\t ERROR: Can't convert initial Image Time Out parameter." + "\n\t Set default value = "
                    + GraphicUtil.getChartDimensionsMap().get(Constant.IMAGE_TIME_OUT) + "\n");
        }
    }

    /**
     * Produces an HTML page which lists the agents which are currently connected to the
     * central collector. This can be used as a deployment aid.
     */
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        //@TODO provide an implementation that lists the agent that are 
        // connected to it.
    }

    /**
     * Method initCollector initiate collector.
     *
     * @param config of type ServletConfig
     * @throws ServletException when
     */
    void initCollector(ServletConfig config) throws ServletException {
        // The value of the port is read from the servlet config 
        // which is set in web.xml
        String portVal = config.getInitParameter(PORT_KEY);
        try {
            if (portVal != null) {
                port = Integer.parseInt(portVal);
            }
        } catch (NumberFormatException numex) {
            String msg = "The value of 'port' init parameter, '" + portVal + "', is not valid one. " + "Legal values are non-zero positive integers";
            logger.error(msg, numex);
            throw new ServletException(msg, numex); // stop the deployment
        }
        if (port <= 0) {
            String msg = "The value of 'port' init parameter, '" + portVal + "', is not valid one. " + "Legal values are non-zero positive integers";
            logger.error(msg);
            throw new ServletException(msg);
        }
        try {
            startListening();
            if (logger.isDebugEnabled()) {
                logger.debug("AgentListener thread spawned to listen on port " + port);
            }
        } catch (BindException bindex) {
            String msg = "Error while attempting to start listening; port specified is " + port + ". "
                    + "Check if this port is in use by another application";
            logger.error(msg, bindex);
            throw new ServletException(msg, bindex);
        } catch (IOException e) {
            String msg = "Error while attempting to start listening";
            logger.error(msg, e);
            throw new ServletException(msg, e);
        }

    }

    /**
     * Method startListening starts collector.
     * @throws IOException when
     */
    void startListening() throws IOException {
        Collector collector = new CollectorImpl();
        collector.start(new CollectorConfig() {
            public int getAgentListenPort() {
                return port;
            }

            public long getPersistInterval() {
                return WebConfig.getPersistInterval();
            }
        });
        DataFetchUtil.setCollector(collector);
    }

    /**
     * Method getPort returns the port of this InfraRedListenerServlet object.
     *
     * @return the port (type int) of this InfraRedListenerServlet object.
     */
    int getPort() {
        return this.port;
    }
    
    /**
     * Reset reset statistic timer.
     * 
     * @param config the config
     */
    private void resetResetStatisticTimer(ServletConfig config){
        Long resetInterval = Long.parseLong(config.getInitParameter(RESET_CACHE_INTERVAL));
        if ((resetInterval != null) && (resetInterval > 0)) {
            ResetStatisticCacheJob.setResetCacheInterval(resetInterval);
        }
    }

}
