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
 * Constant.java Date created: 22.01.2008
 * Last modified by: $Author: soyon.lim $
 * $Revision: 14286 $ $Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */
package net.sf.infrared2.server;

/**
 * <b>Constant</b><p>
 * Generic constants for all server side code.
 *
 * @author: gzgonikov
 */

public interface Constant {

    /**
     * smartImageMapAttributeName is a name contract for Servlet Context
     * attribute keeps map of images (SmartImageMap). Default value is
     * "SmartImageMap".
     */
    public static final String smartImageMapAttributeName = "SmartImageMap";

    public static final String smartImageMapContextAttributeName = "smartImageMapAttributeName";

    public static final String DATA_SNAPSHOT = "dataSnapshot";

    public static final String DATE_PATTERN = "MM/dd/yyyy HH:mm";

    public static final String DATE_TIME_PATTERN = "MM/dd/yyyy HH:mm:ss";

    public static final String APPLICATION_KEY_SEPARATOR = "#";

    public static final String INSTANCE_KEY_SEPARATOR = "@";
    
    public static final String DATA_CACHE = "dataChache";

    public static final String REPORT_CACHE = "reportChache";

    /** Chart Dimension constants to read from web.xml */
    public static final String PIE3D_CHART_HEIGHT_LABEL = "pie3DChartHeight";

    public static final String PIE3D_CHART_WIDTH_LABEL = "pie3DChartWidth";

    public static final String BAR_CHART_HEIGHT_LABEL = "barChartHeight";

    public static final String BAR_CHART_WIDTH_LABEL = "barChartWidth";

    public static final String IMAGE_TIME_OUT = "imageTimeOut";

    public static final int PIE_DATA_SET = 1;
    
    public static final int BAR_DATA_SET = 2;

    /** Report constants */

    public static final String REPORT_SERVLET_PATTERN = "/report/";

    /** arrays of colors in RGB sequence */
    public static final int[][] COLOR_SET = new int[][]{
            {254,84,172}, //{R,G,B}
            {223,25,25},
            {243,159,13},
            {228,228,15},
            {180,224,23},
            {20,216,15},
            {3,217,236},
            {18,155,251},
            {48,86,228},
            {203,154,250},
            {189,83,239}
    };

    /* Graphic constants */
    public static final String GRAPHIC_TITLE_MS = "Milliseconds";

    public static final String GRAPHIC_TITLE_COUNTS = "Counts";

    public static final String MODULE_ABSOLUTE = "abs";

    public static final String MODULE_HIERARCHICAL = "hier";

    public static final String TITLE_QUERY = "Q";

    public static final String TITLE_APPLICATION = "Application(s)";

    public static final String TITLE_SEQUENCE = "Sequence";

    public static final String CLIENT_CACHE_ENABLED = "clientCacheEnabled";

    public static final String SERVER_CACHE_ENABLED = "serverCacheEnabled";

    public static final String IS_REAL_MODE = "isRealMode";

}
