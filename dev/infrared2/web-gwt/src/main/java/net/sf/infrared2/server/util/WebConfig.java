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
 * WebConfig.java Date created: 22.01.2008
 * Last modified by: $Author: soyon.lim $
 * $Revision: 14286 $ $Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.server.util;

import org.apache.log4j.Logger;

/**
 * <b>WebConfig</b><p>
 * Class WebConfig provide configuration params for infrared.
 *
 * @author gzgonikov
 * Created on 08.04.2008
 */
public class WebConfig {

    /** Field COLLECTOR_PERSIST_INTERVAL  */
    public static final String COLLECTOR_PERSIST_INTERVAL = "persist-interval";
    /** Field WEB_COLOR_THRESHOLD  */
    public static final String WEB_COLOR_THRESHOLD = "web.color-threshold";
    /** Field WEB_NUM_OF_SQL_QUERIES  */
    public static final String WEB_NUM_OF_SQL_QUERIES = "web.num_of_sql_queries";
    /** Field WEB_NUM_OF_LAST_INVOCATIONS  */
    public static final String WEB_NUM_OF_LAST_INVOCATIONS = "web.num_of_last_invoke";
    /** Field DEFAULT_WEB_CONFIG_LOCATION  */
    public static final String DEFAULT_WEB_CONFIG_LOCATION = "infrared-web.properties";

    /** Field DEFAULT_COLLECTOR_PERSIST_INTERVAL  */
    public static final String DEFAULT_COLLECTOR_PERSIST_INTERVAL = "600000";
    /** Field DEFAULT_WEB_COLOR_THRESHOLD  */
    public static final String DEFAULT_WEB_COLOR_THRESHOLD = "25";
    /** Field DEFAULT_NUM_OF_LAST_INVOCATIONS  */
    public static final String DEFAULT_NUM_OF_LAST_INVOCATIONS = "5";
    /** Field DEFAULT_NUM_OF_SQL_QUERIES  */
    public static final String DEFAULT_NUM_OF_SQL_QUERIES = "5";

    /** Field log  */
    public static Logger log = Logger.getLogger(WebConfig.class);
    /** Field propertyUtil  */
    private static PropertyUtil propertyUtil;

    static {
        propertyUtil = new PropertyUtil(DEFAULT_WEB_CONFIG_LOCATION);
    }

    /**
     * Method getPersistInterval returns the persistInterval of this WebConfig object.
     *
     * @return the persistInterval (type long) of this WebConfig object.
     */
    public static long getPersistInterval() {
        String interval = propertyUtil.getProperty(COLLECTOR_PERSIST_INTERVAL,
                DEFAULT_COLLECTOR_PERSIST_INTERVAL);
        return Long.parseLong(interval);
    }

    /**
     * Method getNumOfSqlQueries returns the numOfSqlQueries of this WebConfig object.
     *
     * @return the numOfSqlQueries (type int) of this WebConfig object.
     */
    public static int getNumOfSqlQueries() {
        String sqlQueries = propertyUtil.getProperty(WEB_NUM_OF_SQL_QUERIES,
                DEFAULT_NUM_OF_SQL_QUERIES);
        return Integer.parseInt(sqlQueries);
    }

}
