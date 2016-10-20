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
 * SmartImageMap.java Date created: 31.01.2008
 * Last modified by: $Author: soyon.lim $
 * $Revision: 14286 $ $Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.server.chart;

import java.awt.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * <b>SmartImageMap</b><p>
 * SmartImageMap is special HashMap for storing chart image objects (type
 * SmartImage) and theirs keys.
 *
 * @author Roman Ivanenko
 */
public class SmartImageMap extends HashMap<String, SmartImage> {

    /**
     * Constructor. Calls setTestImages() for test purposes.
     */
    public SmartImageMap() {
        super();
    }

    /**
     * Get Images from map by its name.
     *
     * @param chartName a name of a chart
     * @return Image object specified with its name.
     */
    public Image getImage(String chartName) {
        return this.getSmartImage(chartName).getImage();
    }

    /**
     * Get SmartImages from map by its name.
     *
     * @param chartName a name of a chart
     * @return SmartImage object specified with its name.
     */
    public SmartImage getSmartImage(String chartName) {
        return this.get(chartName);
    }


    /**
     * Remove SmartImages from map by its sessionId.
     * @param sessionId an id of session
     */
    public synchronized void clear(String sessionId){
        if (sessionId==null) return;
        Set<Map.Entry<String, SmartImage>> entrySet = this.entrySet();
        for (Iterator<Map.Entry<String, SmartImage>> entryIt = entrySet.iterator();entryIt.hasNext();){
            Map.Entry<String, SmartImage> entry = entryIt.next();
            if (entry==null){
                entryIt.remove();
                continue;
            }
            SmartImage smartImage = entry.getValue();
            if (smartImage != null) {
                if (sessionId.equals(smartImage.getSesionId())){
                   entryIt.remove();
                }
            }
            else entryIt.remove();
        }
    }

}
