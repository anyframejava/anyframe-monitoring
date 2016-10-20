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
 * PropertyUtil.java Date created: 22.01.2008
 * Last modified by: $Author: soyon.lim $
 * $Revision: 14286 $ $Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */
package net.sf.infrared2.server.util;

import java.net.URL;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * <b>PropertyUtil</b><p>
 * Class PropertyUtil contains utility methods for working with properties files.
 *
 * @author gzgonikov
 */
public class PropertyUtil {

    /** Field prop  */
    private Properties prop;
    /** Field config  */
    private URL config;
    /** Field log  */
    public static Logger log = Logger.getLogger(PropertyUtil.class);

    /**
     * Constructor PropertyUtil creates a new PropertyUtil instance.
     *
     * @param propertyFile of type String
     */
    public PropertyUtil(String propertyFile) {
        try {
            prop = new Properties();
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            config = loader.getResource(propertyFile);
            prop.load(config.openStream());
        }
        catch (Exception e) {
            log.info("Unable to load web configuration , will use default configuration");
        }
    }

    /**
     * Method getProperty returns corresponding property value.
     *
     * @param property of type String
     * @param defaultValue of type String
     * @return String
     */
    public String getProperty(String property, String defaultValue) {
        if (prop.getProperty(property) == null) {
            log.debug("The property " + property + " is not defined. " +
                    "Returning default value " + defaultValue);
            return defaultValue;
        } else {
            return prop.getProperty(property);
        }
    }


}
