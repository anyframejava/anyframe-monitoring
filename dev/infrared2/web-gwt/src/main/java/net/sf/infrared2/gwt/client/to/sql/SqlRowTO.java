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
 * SqlRowTO.java Date created: 22.01.2008
 * Last modified by: $Author: soyon.lim $ 
 * $Revision: 14286 $ $Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.gwt.client.to.sql;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

/**
 * <b>SqlRowTO</b><p>
 * Presents row for table that is placed in "SQL" tab.
 * 
 * @author Sergey Evluhin
 */
public class SqlRowTO implements Serializable, IsSerializable {
    /**
     * Serial version UID
     */
    private static final long serialVersionUID = 7105582812095466419L;
    /** Id of SQL query. */
    private String id;
    /** SQL query text. */
    private String sqlQuery;
    /** SQL query text. */
    private String sqlQueryNotFormatted;
    /** Average total time value. */
    private double avgTotalTime;
    /** Average execution time of query. */
    private double avgExecuteTime;
    /** Average preparing time of query. */
    private double avgPrepareTime;
    /** Count of executions. */
    private int countExecute;
    /** Count of preparations. */
    private int countPrepare;
    /** Maximum execution time. */
    private int maxTimeExecute;
    /** Maximum preparation time. */
    private int maxTimePrepare;
    /** Minimum execution time. */
    private int minTimeExecute;
    /** Minimum preparation time. */
    private int minTimePrepare;
    /** First execution time of execute. */
    private int firstExecTimeExecute;
    /** First execution time of prepare. */
    private int firstExecTimePrepare;
    /** Last execution time of execute.*/
    private int lastExecTimeExecute;
    /** Last execution time of prepare.*/
    private int lastExecTimePrepare;
    /** Total execution time.*/
    private int totalExecuteTime;
    /** Total prepare time.*/    
    private int totalPrepareTime;

    /**
     * Default constructor.
     */
    public SqlRowTO() {
        super();
    }

    /**
     * Creates object with predefined parameters.
     * 
     * @param id - id of SQL query.
     * @param sqlQuery - SQL query text.
     * @param avgTotalTime - average total time value.
     * @param avgExecuteTime - average execution time of query.
     * @param avgPrepareTime - average preparing time of query.
     * @param countExecute - count of executions.
     * @param countPrepare - count of preparations.
     * @param maxTimeExecute - maximum execution time.
     * @param maxTimePrepare - maximum preparation time.
     * @param minTimeExecute - minimum execution time.
     * @param minTimePrepare - minimum preparation time.
     * @param firstExecTimeExecute - first execution time of execute.
     * @param firstExecTimePrepare - first execution time of prepare.
     * @param lastExecTimeExecute - last execution time of execute.
     * @param lastExecTimePrepare - last execution time of prepare.
     */
    public SqlRowTO(String id, String sqlQuery, double avgTotalTime, double avgExecuteTime,
                    double avgPrepareTime, int countExecute, int countPrepare, int maxTimeExecute,
                    int maxTimePrepare, int minTimeExecute, int minTimePrepare,
                    int firstExecTimeExecute, int firstExecTimePrepare, int lastExecTimeExecute,
                    int lastExecTimePrepare) {
        this();
        this.id = id;
        this.sqlQuery = sqlQuery;
        this.avgTotalTime = avgTotalTime;
        this.avgExecuteTime = avgExecuteTime;
        this.avgPrepareTime = avgPrepareTime;
        this.countExecute = countExecute;
        this.countPrepare = countPrepare;
        this.maxTimeExecute = maxTimeExecute;
        this.maxTimePrepare = maxTimePrepare;
        this.minTimeExecute = minTimeExecute;
        this.minTimePrepare = minTimePrepare;
        this.firstExecTimeExecute = firstExecTimeExecute;
        this.firstExecTimePrepare = firstExecTimePrepare;
        this.lastExecTimeExecute = lastExecTimeExecute;
        this.lastExecTimePrepare = lastExecTimePrepare;
    }

    /**
     * @return id of SQL query.
     */
    public String getId() {
        return id;
    }

    /**
     * @param id - id of SQL query.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return SQL query text.
     */
    public String getSqlQuery() {
        return sqlQuery;
    }

    /**
     * @param sqlQuery - SQL query text.
     */
    public void setSqlQuery(String sqlQuery) {
        this.sqlQuery = sqlQuery;
    }
    
    /**
     * Gets the sql query not formatted.
     * @return the sql query not formatted
     */
    public String getSqlQueryNotFormatted() {
		return sqlQueryNotFormatted;
	}

	/**
	 * Sets the sql query not formatted.
	 * @param sqlQueryNotFormatted the new sql query not formatted
	 */
	public void setSqlQueryNotFormatted(String sqlQueryNotFormatted) {
		this.sqlQueryNotFormatted = sqlQueryNotFormatted;
	}

	/**
     * @return average total time value.
     */
    public double getAvgTotalTime() {
        return avgTotalTime;
    }

    /**
     * @param avgTotalTime - average total time value.
     */
    public void setAvgTotalTime(double avgTotalTime) {
        this.avgTotalTime = avgTotalTime;
    }

    /**
     * @return average execution time of query.
     */
    public double getAvgExecuteTime() {
        return avgExecuteTime;
    }

    /**
     * @param avgExecuteTime - average execution time of query.
     */
    public void setAvgExecuteTime(double avgExecuteTime) {
        this.avgExecuteTime = avgExecuteTime;
    }

    /**
     * @return average preparing time of query.
     */
    public double getAvgPrepareTime() {
        return avgPrepareTime;
    }

    /**
     * @param avgPrepareTime - average preparing time of query.
     */
    public void setAvgPrepareTime(double avgPrepareTime) {
        this.avgPrepareTime = avgPrepareTime;
    }

    /**
     * @return count of executions.
     */
    public int getCountExecute() {
        return countExecute;
    }

    /**
     * @param countExecute - count of executions.
     */
    public void setCountExecute(int countExecute) {
        this.countExecute = countExecute;
    }

    /**
     * @return count of preparations.
     */
    public int getCountPrepare() {
        return countPrepare;
    }

    /**
     * @param countPrepare - count of preparations.
     */
    public void setCountPrepare(int countPrepare) {
        this.countPrepare = countPrepare;
    }

    /**
     * @return maximum execution time.
     */
    public int getMaxTimeExecute() {
        return maxTimeExecute;
    }

    /**
     * @param maxTimeExecute - maximum execution time.
     */
    public void setMaxTimeExecute(int maxTimeExecute) {
        this.maxTimeExecute = maxTimeExecute;
    }

    /**
     * @return maximum preparation time.
     */
    public int getMaxTimePrepare() {
        return maxTimePrepare;
    }

    /**
     * @param maxTimePrepare - maximum preparation time.
     */
    public void setMaxTimePrepare(int maxTimePrepare) {
        this.maxTimePrepare = maxTimePrepare;
    }

    /**
     * @return minimum execution time.
     */
    public int getMinTimeExecute() {
        return minTimeExecute;
    }

    /**
     * @param minTimeExecute - minimum execution time.
     */
    public void setMinTimeExecute(int minTimeExecute) {
        this.minTimeExecute = minTimeExecute;
    }

    /**
     * @return minimum preparation time.
     */
    public int getMinTimePrepare() {
        return minTimePrepare;
    }

    /**
     * @param minTimePrepare - minimum preparation time.
     */
    public void setMinTimePrepare(int minTimePrepare) {
        this.minTimePrepare = minTimePrepare;
    }

    /**
     * @return first execution time of execute.
     */
    public int getFirstExecTimeExecute() {
        return firstExecTimeExecute;
    }

    /**
     * @param firstExecTimeExecute - first execution time of execute.
     */
    public void setFirstExecTimeExecute(int firstExecTimeExecute) {
        this.firstExecTimeExecute = firstExecTimeExecute;
    }

    /**
     * @return first execution time of prepare.
     */
    public int getFirstExecTimePrepare() {
        return firstExecTimePrepare;
    }

    /**
     * @param firstExecTimePrepare - first execution time of prepare.
     */
    public void setFirstExecTimePrepare(int firstExecTimePrepare) {
        this.firstExecTimePrepare = firstExecTimePrepare;
    }

    /**
     * @return last execution time of execute.
     */
    public int getLastExecTimeExecute() {
        return lastExecTimeExecute;
    }

    /**
     * @param lastExecTimeExecute - last execution time of execute.
     */
    public void setLastExecTimeExecute(int lastExecTimeExecute) {
        this.lastExecTimeExecute = lastExecTimeExecute;
    }

    /**
     * @return last execution time of prepare.
     */
    public int getLastExecTimePrepare() {
        return lastExecTimePrepare;
    }

    /**
     * @param lastExecTimePrepare - last execution time of prepare.
     */
    public void setLastExecTimePrepare(int lastExecTimePrepare) {
        this.lastExecTimePrepare = lastExecTimePrepare;
    }

    /**
     * Method getTotalExecuteTime returns the totalExecuteTime of this SqlRowTO object.
     *
     * @return the totalExecuteTime (type int) of this SqlRowTO object.
     */
    public int getTotalExecuteTime() {
        return totalExecuteTime;
    }

    /**
     * Method setTotalExecuteTime sets the totalExecuteTime of this SqlRowTO object.
     *
     * @param totalExecuteTime the totalExecuteTime of this SqlRowTO object.
     *
     */
    public void setTotalExecuteTime(int totalExecuteTime) {
        this.totalExecuteTime = totalExecuteTime;
    }

    /**
     * @return the totalPrepareTime
     */
    public int getTotalPrepareTime() {
        return this.totalPrepareTime;
    }

    /**
     * @param totalPrepareTime the totalPrepareTime to set
     */
    public void setTotalPrepareTime(int totalPrepareTime) {
        this.totalPrepareTime = totalPrepareTime;
    }
}
