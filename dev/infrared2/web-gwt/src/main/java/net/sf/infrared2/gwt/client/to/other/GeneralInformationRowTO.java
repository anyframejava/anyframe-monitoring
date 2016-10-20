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
 * GeneralInformationRowTO.java Date created: 22.01.2008
 * Last modified by: $Author: soyon.lim $ 
 * $Revision: 14286 $ $Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.gwt.client.to.other;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;
import java.util.Date;

/**
 * <b>GeneralInformationRowTO</b><p>
 * Transfer object represents row of table that is placed into "General
 * information" tab of views different to SQL and Application.
 * 
 * @author Sergey Evluhin
 */
public class GeneralInformationRowTO implements Serializable, IsSerializable {

    private static final long serialVersionUID = 2233094920664301393L;
    /** Id of operation or SQL query */
    private String id;
    /** Name of operation or SQL query */
    private String name;
    /** Name of operation or SQL query */
    private String notFormattedSQL;
    /** Count times of executions */
    private int count;
    /** Total time value (inclusive mode) */
    private int totalInclusiveTime;
    /** Maximum execution time (inclusive mode) */
    private int maxInclusive;
    /** Minimum execution time (inclusive mode)*/
    private int minInclusive;
    /** Total time value (exclusive mode) */
    private int totalExclusiveTime;
    /** Maximum execution time (exclusive mode) */
    private int maxExclusive;
    /** Minimum execution time (exclusive mode) */
    private int minExclusive;
    /** Time of first execution */
    private long firstExecutionTime;
    /** Time of last execution */
    private long lastExecutionTime;
    /** Time of first execution (inclusive mode)*/
    private int firstExecutionInclusiveTime;
    /** Time of last execution (inclusive mode)*/
    private int lastExecutionInclusiveTime;
    /** Time of first execution (exclusive mode)*/
    private int firstExecutionExclusiveTime;
    /** Time of last execution (exclusive mode)*/
    private int lastExecutionExclusiveTime;

    /**
     * Default constructor.
     */
    public GeneralInformationRowTO() {
        super();
    }

    /**
     * @param id - id of operation or SQL query.
     * @param name - name of operation or SQL query.
     * @param count - count times of executions.
     * @param totalInclusiveTime - total time value (inclusive mode).
     * @param totalExclusiveTime - total time value (exclusive mode).
     * @param maxInclusive - maximum execution time (inclusive mode).
     * @param minInclusive - minimum execution time (inclusive mode).
     * @param maxExclusive - maximum execution time (exclusive mode).
     * @param minExclusive - minimum execution time (exclusive mode).
     * @param firstExecutionTime - time of first execution.
     * @param lastExecutionTime - time of last execution.
     * @param firstExecutionInclusiveTime - time of first execution (inclusive
     *            mode).
     * @param lastExecutionInclusiveTime - time of last execution (inclusive
     *            mode).
     * @param firstExecutionExclusiveTime - time of first execution (exclusive
     *            mode).
     * @param lastExecutionExclusiveTime - time of last execution (exclusive
     *            mode).
     */
    public GeneralInformationRowTO(String id, String name, int count, int totalInclusiveTime,
                    int totalExclusiveTime, int maxInclusive, int minInclusive, int maxExclusive,
                    int minExclusive, long firstExecutionTime, long lastExecutionTime,
                    int firstExecutionInclusiveTime, int lastExecutionInclusiveTime,
                    int firstExecutionExclusiveTime, int lastExecutionExclusiveTime) {
        this.id = id;
        this.name = name;
        this.count = count;
        this.totalInclusiveTime = totalInclusiveTime;
        this.totalExclusiveTime = totalExclusiveTime;
        this.maxInclusive = maxInclusive;
        this.minInclusive = minInclusive;
        this.maxExclusive = maxExclusive;
        this.minExclusive = minExclusive;
        this.firstExecutionTime = firstExecutionTime;
        this.lastExecutionTime = lastExecutionTime;
        this.firstExecutionInclusiveTime = firstExecutionInclusiveTime;
        this.lastExecutionInclusiveTime = lastExecutionInclusiveTime;
        this.firstExecutionExclusiveTime = firstExecutionExclusiveTime;
        this.lastExecutionExclusiveTime = lastExecutionExclusiveTime;
    }

    /**
     * @return maximum execution time (inclusive mode).
     */
    public int getMaxInclusive() {
        return maxInclusive;
    }

    /**
     * @param maxInclusive - maximum execution time (inclusive mode).
     */
    public void setMaxInclusive(int maxInclusive) {
        this.maxInclusive = maxInclusive;
    }

    /**
     * @return minimum execution time (inclusive mode).
     */
    public int getMinInclusive() {
        return minInclusive;
    }

    /**
     * @param minInclusive - minimum execution time (inclusive mode).
     */
    public void setMinInclusive(int minInclusive) {
        this.minInclusive = minInclusive;
    }

    /**
     * @return maximum execution time (exclusive mode).
     */
    public int getMaxExclusive() {
        return maxExclusive;
    }

    /**
     * @param maxExclusive - maximum execution time (exclusive mode).
     */
    public void setMaxExclusive(int maxExclusive) {
        this.maxExclusive = maxExclusive;
    }

    /**
     * @return minimum execution time (exclusive mode).
     */
    public int getMinExclusive() {
        return minExclusive;
    }

    /**
     * @param minExclusive - minimum execution time (exclusive mode).
     */
    public void setMinExclusive(int minExclusive) {
        this.minExclusive = minExclusive;
    }

    /**
     * @return count times of executions.
     */
    public int getCount() {
        return count;
    }

    /**
     * @param count - count times of executions.
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * @return total time value (inclusive mode).
     */
    public int getTotalInclusiveTime() {
        return totalInclusiveTime;
    }

    /**
     * @param totalInclusiveTime - total time value (inclusive mode).
     */
    public void setTotalInclusiveTime(int totalInclusiveTime) {
        this.totalInclusiveTime = totalInclusiveTime;
    }

    /**
     * @return total time value (exclusive mode).
     */
    public int getTotalExclusiveTime() {
        return totalExclusiveTime;
    }

    /**
     * @param totalExclusiveTime - total time value (exclusive mode).
     */
    public void setTotalExclusiveTime(int totalExclusiveTime) {
        this.totalExclusiveTime = totalExclusiveTime;
    }

    /**
     * @return time of first execution.
     */
    public long getFirstExecutionTime() {
        return firstExecutionTime;
    }

    /**
     * @param firstExecutionTime - time of first execution.
     */
    public void setFirstExecutionTime(long firstExecutionTime) {
        this.firstExecutionTime = firstExecutionTime;
    }

    /**
     * @return time of last execution.
     */
    public long getLastExecutionTime() {
        return lastExecutionTime;
    }

    /**
     * @param lastExecutionTime - time of last execution.
     */
    public void setLastExecutionTime(long lastExecutionTime) {
        this.lastExecutionTime = lastExecutionTime;
    }

    /**
     * @return time of first execution (inclusive mode).
     */
    public int getFirstExecutionInclusiveTime() {
        return firstExecutionInclusiveTime;
    }

    /**
     * @param firstExecutionInclusiveTime - time of first execution (inclusive
     *            mode).
     */
    public void setFirstExecutionInclusiveTime(int firstExecutionInclusiveTime) {
        this.firstExecutionInclusiveTime = firstExecutionInclusiveTime;
    }

    /**
     * @return time of last execution (inclusive mode).
     */
    public int getLastExecutionInclusiveTime() {
        return lastExecutionInclusiveTime;
    }

    /**
     * @param lastExecutionInclusiveTime - time of last execution (inclusive
     *            mode).
     */
    public void setLastExecutionInclusiveTime(int lastExecutionInclusiveTime) {
        this.lastExecutionInclusiveTime = lastExecutionInclusiveTime;
    }

    /**
     * @return time of first execution (exclusive mode).
     */
    public int getFirstExecutionExclusiveTime() {
        return firstExecutionExclusiveTime;
    }

    /**
     * @param firstExecutionExclusiveTime - time of first execution (exclusive
     *            mode).
     */
    public void setFirstExecutionExclusiveTime(int firstExecutionExclusiveTime) {
        this.firstExecutionExclusiveTime = firstExecutionExclusiveTime;
    }

    /**
     * @return time of last execution (exclusive mode).
     */
    public int getLastExecutionExclusiveTime() {
        return lastExecutionExclusiveTime;
    }

    /**
     * @param lastExecutionExclusiveTime - time of last execution (exclusive
     *            mode).
     */
    public void setLastExecutionExclusiveTime(int lastExecutionExclusiveTime) {
        this.lastExecutionExclusiveTime = lastExecutionExclusiveTime;
    }

    /**
     * @return id of operation or SQL query.
     */
    public String getId() {
        return id;
    }

    /**
     * @param id - id of operation or SQL query.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return name of operation or SQL query.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name - name of operation or SQL query.
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Gets the not formatted sql.
     * @return the not formatted sql
     */
    public String getNotFormattedSQL() {
		return notFormattedSQL;
	}

	/**
	 * Sets the not formatted sql.
	 * @param notFormattedSQL the new not formatted sql
	 */
	public void setNotFormattedSQL(String notFormattedSQL) {
		this.notFormattedSQL = notFormattedSQL;
	}

	/**
     * Convert array of GeneralInformationRowTO objects to two level array of
     * Objects.
     * 
     * @param rows - array of GeneralInformationRowTO objects.
     * @return Object[][].
     */
    public static Object[][] convertSqlGeneralInformationRows(GeneralInformationRowTO[] rows) {
        if (rows == null) {
            return null;
        }
        Object[][] res = new Object[rows.length][];
        for (int i = 0; i < rows.length; i++) {
            GeneralInformationRowTO row = rows[i];
            res[i] = new Object[] { row.getId(), row.getNotFormattedSQL(), new Integer(row.getCount()),
                            new Integer(row.getTotalInclusiveTime()),
                            new Integer(row.getMaxInclusive()), new Integer(row.getMinExclusive()),
                            new Integer(row.getTotalExclusiveTime()),
                            new Integer(row.getMaxExclusive()), new Integer(row.getMinInclusive()),
                            new Date(row.getFirstExecutionTime()),
                            new Date(row.getLastExecutionTime()),
                            new Integer(row.getFirstExecutionInclusiveTime()),
                            new Integer(row.getLastExecutionInclusiveTime()),
                            new Integer(row.getFirstExecutionExclusiveTime()),
                            new Integer(row.getLastExecutionExclusiveTime())

            };
        }
        return res;
    }

}
