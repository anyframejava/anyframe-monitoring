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
 * DetailOtherViewTO.java Date created: 24.01.2008
 * Last modified by: $Author: soyon.lim $ 
 * $Revision: 14286 $ $Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.gwt.client.to.other;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

import net.sf.infrared2.gwt.client.to.INavigableTO;
import net.sf.infrared2.gwt.client.to.IResultStatus;
import net.sf.infrared2.gwt.client.to.NavigatorEntryTO;
import net.sf.infrared2.gwt.client.to.sql.SqlRowTO;
import net.sf.infrared2.gwt.client.to.status.ResultStatus;

/**
 * <b>DetailOtherViewTO</b><p>
 * Transfer object that contains details data for trace tree node.
 * 
 * @author Sergey Evluhin
 */
public class DetailOtherViewTO implements Serializable, IsSerializable, INavigableTO, IResultStatus {
    /**
     * Serial version UID
     */
    private static final long serialVersionUID = 3459619495801637071L;
    /**
     * Entry of navigator panel associated with view that was builded by using
     * data from this object.
     */
    private NavigatorEntryTO navigatorEntryTO;

    private ResultStatus resultStatus;

    /** Rows for table that is placed in "JDBC" tab. */
    private JdbcRowTO[] jdbcRows;

    /** Rows for table "Top 5 SQL queries by execution time". */
    private SqlRowTO[] sqlRowsByExecutionTime;

    /** Rows for table "Top 5 SQL queries by execution count". */
    private SqlRowTO[] sqlRowsByExecutionCount;

    /** Formatted and highlighted SQL query. */
    private String formattedSql;

    /**
     * Default constructor.
     */
    public DetailOtherViewTO() {
        super();
    }

    /**
     * Create transfer object with predefined fields.
     * 
     * @param jdbcRows - rows for table that is placed in "JDBC" tab.
     * @param sqlRowsByExecutionTime - rows for table "Top 5 SQL queries by
     *            execution time".
     * @param sqlRowsByExecutionCount - rows for table "Top 5 SQL queries by
     *            execution count".
     */
    public DetailOtherViewTO(JdbcRowTO[] jdbcRows, SqlRowTO[] sqlRowsByExecutionTime,
                    SqlRowTO[] sqlRowsByExecutionCount) {
        this();
        this.jdbcRows = jdbcRows;
        this.sqlRowsByExecutionTime = sqlRowsByExecutionTime;
        this.sqlRowsByExecutionCount = sqlRowsByExecutionCount;
    }

    /**
     * Create transfer object for click action by sql node.
     * 
     * @param formattedSql
     */
    public DetailOtherViewTO(String formattedSql) {
        this();
        this.formattedSql = formattedSql;
    }

    /**
     * @return rows for table "Top 5 SQL queries by execution time".
     */
    public SqlRowTO[] getSqlRowsByExecutionTime() {
        return sqlRowsByExecutionTime;
    }

    /**
     * @param sqlRowsByExecutionTime - rows for table "Top 5 SQL queries by
     *            execution time".
     */
    public void setSqlRowsByExecutionTime(SqlRowTO[] sqlRowsByExecutionTime) {
        this.sqlRowsByExecutionTime = sqlRowsByExecutionTime;
    }

    /**
     * @return rows for table "Top 5 SQL queries by execution count".
     */
    public SqlRowTO[] getSqlRowsByExecutionCount() {
        return sqlRowsByExecutionCount;
    }

    /**
     * @param sqlRowsByExecutionCount - rows for table "Top 5 SQL queries by
     *            execution count".
     */
    public void setSqlRowsByExecutionCount(SqlRowTO[] sqlRowsByExecutionCount) {
        this.sqlRowsByExecutionCount = sqlRowsByExecutionCount;
    }

    /**
     * @return rows for table that is placed in "JDBC" tab.
     */
    public JdbcRowTO[] getJdbcRows() {
        return jdbcRows;
    }

    /**
     * @param jdbcRows - rows for table that is placed in "JDBC" tab.
     */
    public void setJdbcRows(JdbcRowTO[] jdbcRows) {
        this.jdbcRows = jdbcRows;
    }

    /**
     * @return formatted and highlighted sql query
     */
    public String getFormattedSql() {
        return formattedSql;
    }

    /**
     * @param formattedSql - formatted and highlighted sql query
     */
    public void setFormattedSql(String formattedSql) {
        this.formattedSql = formattedSql;
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
     * Convert array JdbcRowTO objects to two level array of Objects.
     * 
     * @param rows - array of JdbcRowTO objects.
     * @return Object[][].
     */
    public static Object[][] convertJdbcRows(JdbcRowTO[] rows) {
        if (rows == null) {
            return null;
        }
        Object[][] res = new Object[rows.length][];
        for (int i = 0; i < rows.length; i++) {
            JdbcRowTO row = rows[i];
            res[i] = new Object[] { row.getApiName(), new Double(row.getAvgTime()),
                            new Integer(row.getCount()) };
        }
        return res;
    }

}
