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
 * SqlViewTO.java Date created: 23.01.2008
 * Last modified by: $Author: soyon.lim $ 
 * $Revision: 14286 $ $Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.gwt.client.to.sql;

import java.io.Serializable;
import java.util.Map;

import net.sf.infrared2.gwt.client.to.INavigableTO;
import net.sf.infrared2.gwt.client.to.IResultStatus;
import net.sf.infrared2.gwt.client.to.NavigatorEntryTO;
import net.sf.infrared2.gwt.client.to.other.GeneralInformationRowTO;
import net.sf.infrared2.gwt.client.to.status.ResultStatus;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * <b>SqlViewTO</b><p>
 * Transfer object for SQL view.
 * 
 * @author Sergey Evluhin
 */
public class SqlViewTO implements Serializable, IsSerializable, INavigableTO, IResultStatus {
    /** Serial version UID */
    private static final long serialVersionUID = -4909458359354816513L;

    /** Present current NavigatorEntryTO */
    private NavigatorEntryTO navigatorEntryTO;

    /** Present current ResultStatus */
    private ResultStatus resultStatus;

    /** Rows for "detail" tab's table. */
    private GeneralInformationRowTO[] allSqlQueries;

    /** Rows for all queries statistic (need for reports). */
    private SqlRowTO[] allSqlStatistics;

    /** Rows for queries (sorted by execution time) statistic. */
    private SqlRowTO[] sqlRowsByExecutionTime;

    /** Rows for queries (sorted by execution count) statistic. */
    private SqlRowTO[] sqlRowsByExecutionCount;

    /**
     * The map where key - URL of image, value - HTML code for image builded by
     * time values.
     * 
     * @gwt.typeArgs <java.lang.String, java.lang.String>
     */
    private Map imgUrlByTime;

    /**
     * The map where key - URL of image, value - HTML code for image builded by
     * count values.
     * 
     * @gwt.typeArgs <java.lang.String, java.lang.String>
     */
    private Map imgUrlByCount;

    /**
     * The map where key - id of query, value - formatted query text.
     * 
     * @gwt.typeArgs <java.lang.String,java.lang.String>
     */
    private Map formattedSql;

    /**
     * Default constructor.
     */
    public SqlViewTO() {
        super();
    }

    /**
     * Creates transfer object with predefined fields.
     * 
     * @param allSqlQueries - rows for "detail" tab's table.
     * @param sqlRowsByExecutionTime - rows for queries (sorted by execution
     *            time) statistic.
     * @param sqlRowsByExecutionCount - rows for queries (sorted by execution
     *            count) statistic.
     * @param imgUrlByTime - the map where key - URL of image, value - HTML code
     *            for image builded by time values.
     * @param imgUrlByCount - the map where key - URL of image, value - HTML
     *            code for image builded by count values.
     * @param formattedSql - the map where key - id of query, value - formatted
     *            query text.
     */
    public SqlViewTO(GeneralInformationRowTO[] allSqlQueries, SqlRowTO[] sqlRowsByExecutionTime,
                    SqlRowTO[] sqlRowsByExecutionCount, Map imgUrlByTime, Map imgUrlByCount,
                    Map formattedSql) {
        this.allSqlQueries = allSqlQueries;
        this.sqlRowsByExecutionTime = sqlRowsByExecutionTime;
        this.sqlRowsByExecutionCount = sqlRowsByExecutionCount;
        this.imgUrlByTime = imgUrlByTime;
        this.imgUrlByCount = imgUrlByCount;
        this.formattedSql = formattedSql;
    }

    /**
     * @return rows for table "Top 5 sql queries by execution time"
     */
    public SqlRowTO[] getSqlRowsByExecutionTime() {
        return sqlRowsByExecutionTime;
    }

    /**
     * @param sqlRowsByExecutionTime - rows for table "Top 5 sql queries by
     *            execution time"
     */
    public void setSqlRowsByExecutionTime(SqlRowTO[] sqlRowsByExecutionTime) {
        this.sqlRowsByExecutionTime = sqlRowsByExecutionTime;
    }

    /**
     * @return rows for table "Top 5 sql queries by execution time"
     */
    public SqlRowTO[] getSqlRowsByExecutionCount() {
        return sqlRowsByExecutionCount;
    }

    /**
     * @param sqlRowsByExecutionCount - rows for table "Top 5 sql queries by
     *            execution time"
     */
    public void setSqlRowsByExecutionCount(SqlRowTO[] sqlRowsByExecutionCount) {
        this.sqlRowsByExecutionCount = sqlRowsByExecutionCount;
    }

    /**
     * @return rows for "detail" tab table of sql view
     */
    public GeneralInformationRowTO[] getAllSqlQueries() {
        return allSqlQueries;
    }

    /**
     * @param allSqlQueries - rows for "detail" tab table of sql view
     */
    public void setAllSqlQueries(GeneralInformationRowTO[] allSqlQueries) {
        this.allSqlQueries = allSqlQueries;
    }

    /**
     * @return map where key - id of query, value - formatted query text
     */
    public Map getFormattedSql() {
        return formattedSql;
    }

    /**
     * @param formattedSql - map where key - id of query, value - formatted
     *            query text
     */
    public void setFormattedSql(Map formattedSql) {
        this.formattedSql = formattedSql;
    }

    /**
     * @return SqlRowTO[] - statistics for all sql queries
     */
    public SqlRowTO[] getAllSqlStatistics() {
        return allSqlStatistics;
    }

    /**
     * @param allSqlStatistics - statistics for all sql queries
     */
    public void setAllSqlStatistics(SqlRowTO[] allSqlStatistics) {
        this.allSqlStatistics = allSqlStatistics;
    }

    /**
     * @return the map where key - URL of image, value - HTML code for image
     *         builded by time values.
     */
    public Map getImgUrlByTime() {
        return imgUrlByTime;
    }

    /**
     * @param imgUrlByTime - the map where key - URL of image, value - HTML code
     *            for image builded by time values.
     */
    public void setImgUrlByTime(Map imgUrlByTime) {
        this.imgUrlByTime = imgUrlByTime;
    }

    /**
     * @return the map where key - URL of image, value - HTML code for image
     *         builded by count values.
     */
    public Map getImgUrlByCount() {
        return imgUrlByCount;
    }

    /**
     * @param imgUrlByCount - the map where key - URL of image, value - HTML
     *            code for image builded by count values.
     */
    public void setImgUrlByCount(Map imgUrlByCount) {
        this.imgUrlByCount = imgUrlByCount;
    }

    /**
     * @return status result of call.
     */
    public ResultStatus getResultStatus() {
        return resultStatus;
    }

    /**
     * @param status result of call.
     */
    public void setResultStatus(ResultStatus resultStatus) {
        this.resultStatus = resultStatus;
    }

    /**
     * @return entry of navigator panel associated with view that was builded by
     *         using data from this object.
     */
    public NavigatorEntryTO getNavigatorEntryTO() {
        return navigatorEntryTO;
    }

    /**
     * @param navigatorTO - entry of navigator panel associated with view that
     *            was builded by using data from this object.
     */
    public void setNavigatorEntryTO(NavigatorEntryTO navigatorTO) {
        this.navigatorEntryTO = navigatorTO;
    }

    /**
     * Convert from transfer objects two-level array of Objects.
     * 
     * @param rows - array of rows
     * @return Object[][]
     */
    public static Object[][] convertSqlRows(SqlRowTO[] rows) {
        if (rows == null) {
            return null;
        }
        Object[][] res = new Object[rows.length][];
        for (int i = 0; i < rows.length; i++) {
            SqlRowTO row = rows[i];
            res[i] = new Object[] {

            row.getId(), row.getSqlQueryNotFormatted(), new Double(row.getAvgTotalTime()),
                            new Double(row.getAvgExecuteTime()),
                            new Double(row.getAvgPrepareTime()),
                            new Integer(row.getCountExecute()), new Integer(row.getCountPrepare()),
                            new Integer(row.getMaxTimeExecute()),
                            new Integer(row.getMaxTimePrepare()),
                            new Integer(row.getMinTimeExecute()),
                            new Integer(row.getMinTimePrepare()),
                            new Integer(row.getFirstExecTimeExecute()),
                            new Integer(row.getFirstExecTimePrepare()),
                            new Integer(row.getLastExecTimeExecute()),
                            new Integer(row.getLastExecTimePrepare()) };
        }
        return res;
    }

}
