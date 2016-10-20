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
 * ViewUtil.java Date created: 22.01.2008
 * Last modified by: $Author: soyon.lim $
 * $Revision: 14286 $ $Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */
package net.sf.infrared2.server.util;


import net.sf.infrared.aspects.jdbc.SqlExecuteContext;
import net.sf.infrared.aspects.jdbc.SqlPrepareContext;
import net.sf.infrared.base.model.AggregateExecutionTime;
import net.sf.infrared.base.model.ExecutionContext;
import net.sf.infrared.base.util.TreeNode;
import net.sf.infrared2.gwt.client.Constants;
import net.sf.infrared2.gwt.client.to.other.SummaryRowTO;
import net.sf.infrared2.server.Constant;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * <b>ViewUtil</b><p>
 * Utility methods used by InfraRED web GUI.
 */
public class ViewUtil {

    /**
     * Method getJDBCSummary returns AggregateExecutionTime[] for JDBC summary.
     *
     * @param node of type TreeNode
     * @return AggregateExecutionTime[]
     */
    public static AggregateExecutionTime[] getJDBCSummary(TreeNode node) {
        if (node == null) return null;
        Map jdbcMap = new HashMap();
        getJDBCSummary(node, jdbcMap);

        AggregateExecutionTime[] jdbcExecTime = new AggregateExecutionTime[jdbcMap.size()];
        jdbcMap.values().toArray(jdbcExecTime);

        return jdbcExecTime;
    }

    /**
     * Method getJDBCSummary modify a copy of AggregateExecutionTime.
     *
     * @param node of type TreeNode
     * @param jdbcMap of type Map
     */
    private static void getJDBCSummary(TreeNode node, Map jdbcMap) {
        AggregateExecutionTime originalExecTime = (AggregateExecutionTime) node.getValue();

        // Needs to make modifications on a copy of this execTime, since the reference to  
        // the same is stored in the session.        
        AggregateExecutionTime execTime = (AggregateExecutionTime) originalExecTime.clone();

        if (execTime.getContext().getLayer().endsWith("JDBC")) {
            AggregateExecutionTime jdbcTime = (AggregateExecutionTime)
                    jdbcMap.get(execTime.getContext());
            if (jdbcTime == null) {
                jdbcMap.put(execTime.getContext(), execTime);
            } else {
                jdbcTime.merge(execTime);
            }
        }
        for (Iterator itr = node.getChildren().iterator(); itr.hasNext();) {
            getJDBCSummary((TreeNode) itr.next(), jdbcMap);
        }

        return;
    }

    /**
     * Method getSqlStatistics returns statistics for sql
     *
     * @param node of type TreeNode
     * @return SqlStatistics[]
     */
    public static SqlStatistics[] getSqlStatistics(TreeNode node) {
        if (node == null) return null;
        Map sqlMap = new HashMap();
        getSqlStatistics(node, sqlMap);
        SqlStatistics[] sqlExecTime = new SqlStatistics[sqlMap.size()];

        sqlMap.values().toArray(sqlExecTime);
        for (int i=0; i<sqlExecTime.length; i++){
            sqlExecTime[i].setId("Q"+(i+1));
        }
        return sqlExecTime;
    }

    /**
     * Method getSqlStatistics returns statistics for sql
     *
     * @param node of type TreeNode
     * @param sqlMap of type Map
     */
    public static void getSqlStatistics(TreeNode node, Map sqlMap) {
        AggregateExecutionTime originalSqlExec = (AggregateExecutionTime) node.getValue();

        // Needs to make modifications on a copy of this sqlExec, since the reference to  
        // the same is stored in the session.
        AggregateExecutionTime sqlExec = (AggregateExecutionTime) originalSqlExec.clone();

        if (sqlExec.getContext().getLayer().equals("SQL")) {
            ExecutionContext sqlContext = sqlExec.getContext();
            String sql = sqlContext.getName();

            SqlStatistics sqlStats = (SqlStatistics) sqlMap.get(sql);
            if (sqlStats == null) {
                sqlStats = new SqlStatistics();
                sqlMap.put(sql, sqlStats);
            }
            if (sqlContext instanceof SqlPrepareContext) {
                sqlStats.mergePrepareTime(sqlExec);
            }
            if (sqlContext instanceof SqlExecuteContext) {
                sqlStats.mergeExecuteTime(sqlExec);
            }
        }
        for (Iterator itr = node.getChildren().iterator(); itr.hasNext();) {
            getSqlStatistics((TreeNode) itr.next(), sqlMap);
        }

        return;
    }

    /**
     * Method getTopNQueriesByExecutionTime returns statistics for sql.
     *
     * @param sqlStatistics of type SqlStatistics[]
     * @param n of type int
     * @return SqlStatistics[]
     */
    public static SqlStatistics[] getTopNQueriesByExecutionTime(SqlStatistics[] sqlStatistics,
                                                                int n) {
        if (sqlStatistics == null) return null;
        Arrays.sort(sqlStatistics, new BeanComparator("getAvgExecuteTime", false));
        if (n != 0) {
            return getTopN(sqlStatistics, n);
        }
        return sqlStatistics;
    }

    /**
     * Method getTopNQueriesByCount returns statistics for sql for top N queries by count.
     *
     * @param sqlStatistics of type SqlStatistics[]
     * @param n of type int
     * @return SqlStatistics[]
     */
    public static SqlStatistics[] getTopNQueriesByCount(SqlStatistics[] sqlStatistics, int n) {
        if (sqlStatistics == null) return null;
        Arrays.sort(sqlStatistics, new BeanComparator("getNoOfExecutes", false));
        if (n != 0) {
            return getTopN(sqlStatistics, n);
        }
        return sqlStatistics;
    }

    /**
     * Method getTopN returns statistics for sql for top N queries.
     *
     * @param sqlStatistics of type SqlStatistics[]
     * @param n of type int
     * @return SqlStatistics[]
     */
    private static SqlStatistics[] getTopN(SqlStatistics[] sqlStatistics, int n) {
        SqlStatistics[] result = sqlStatistics;
        if (sqlStatistics.length > n) {
            SqlStatistics[] temp = new SqlStatistics[n];
            System.arraycopy(sqlStatistics, 0, temp, 0, n);
            result = temp;
        }
        return result;
    }

    /**
     * Method getTopNOperationsByParam returns statistics for operations for top N queries.
     *
     * @param summaryRowTOs of type SummaryRowTO[]
     * @param n of type int
     * @param parameterMethod of type String
     * @return SummaryRowTO[]
     */
    public static SummaryRowTO[] getTopNOperationsByParam(SummaryRowTO[] summaryRowTOs, int n, String parameterMethod) {
        if (summaryRowTOs == null) return new SummaryRowTO[0];
        Arrays.sort(summaryRowTOs, new BeanComparator(parameterMethod, false));
        if (n != 0) {
            return getTopNOperations(summaryRowTOs, n);
        }
        return summaryRowTOs;
    }

    /**
     * Method getTopNOperations returns statistics for operations for top N queries.
     *
     * @param summaryRowTOs of type SummaryRowTO[]
     * @param n of type int
     * @return SummaryRowTO[]
     */
    private static SummaryRowTO[] getTopNOperations(SummaryRowTO[] summaryRowTOs, int n) {
        if(summaryRowTOs==null) return new SummaryRowTO[0];
        if(n==0) return summaryRowTOs;
        SummaryRowTO[] result = summaryRowTOs;
        if (summaryRowTOs.length > n) {
            SummaryRowTO[] temp = new SummaryRowTO[n];
            System.arraycopy(summaryRowTOs, 0, temp, 0, n);
            result = temp;
        }
        return result;
    }

    /**
     * Method getQualifiedModuleName ...
     *
     * @param moduleType of type String
     * @return String
     */
    public static String getQualifiedModuleName(String moduleType){
        if(Constant.MODULE_ABSOLUTE.equals(moduleType)){
            return "Absolute module";
        }else if(Constant.MODULE_HIERARCHICAL.equals(moduleType)){
            return "Hierarchical module";
        }else if(Constants.MODULE_LAST_INV.equals(moduleType)){
            return "Last invocation module";
        }
        return "Unknown";
    }

}
