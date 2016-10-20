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
 * ColoredRows.java Date created: 10.04.2008
 * Last modified by: $Author: soyon.lim $ 
 * $Revision: 14286 $ $Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.gwt.client.view.facade;

/**
 * <b>ColoredRows</b><p>
 * Main interface for the object represented the rows of the tables and contains
 * colored rectangle and description for this color. Interface provide getting
 * data for sorting by count and time values.
 * 
 * @author Sergey Evluhin
 */
public interface ColoredRows {

    /**
     * @return information about this color.
     */
    public String getDescription();

    /**
     * @return color of piece of the pie graph builded by time value.
     */
    public String getTimeColor();

    /**
     * @return color of piece of the pie graph builded by total count value.
     */
    public String getCountColor();

    /**
     * @return count of calls of operation.
     */
    public int getCount();

    /**
     * @return total execution time of operation.
     */
    public double getTotalTime();

}
