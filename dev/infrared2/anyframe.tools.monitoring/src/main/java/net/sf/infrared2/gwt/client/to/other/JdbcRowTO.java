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
 * JdbcRowTO.java Date created: 22.01.2008
 * Last modified by: $Author: soyon.lim $ 
 * $Revision: 14286 $ $Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.gwt.client.to.other;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

/**
 * <b>JdbcRowTO</b><p>
 * Presents row of the table that is placed in "JDBC" tab.
 * 
 * @author Sergey Evluhin
 */
public class JdbcRowTO implements Serializable, IsSerializable {

    /**
     * Serial version UID
     */
    private static final long serialVersionUID = -4206083283067382711L;

    /** Name of operation */
    private String apiName;
    /** Average time value */
    private double avgTime;
    /** Count of executions value */
    private int count;

    /**
     * Default constructor.
     */
    public JdbcRowTO() {
        super();
    }

    /**
     * Create row for table that is placed in "JDBC".
     * 
     * @param apiName - name of operation.
     * @param avgTime - average time value.
     * @param count - count of executions value.
     */
    public JdbcRowTO(String apiName, double avgTime, int count) {
        this();
        this.apiName = apiName;
        this.avgTime = avgTime;
        this.count = count;
    }

    /**
     * @return name of operation.
     */
    public String getApiName() {
        return apiName;
    }

    /**
     * @param apiName - name of operation.
     */
    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    /**
     * @return average time value.
     */
    public double getAvgTime() {
        return avgTime;
    }

    /**
     * @param avgTime - average time value.
     */
    public void setAvgTime(double avgTime) {
        this.avgTime = avgTime;
    }

    /**
     * @return count of executions value.
     */
    public int getCount() {
        return count;
    }

    /**
     * @param count - count of executions value.
     */
    public void setCount(int count) {
        this.count = count;
    }

}
