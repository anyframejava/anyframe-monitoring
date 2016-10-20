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
 * PerformanceDataSnapshot.java Date created: 22.01.2008
 * Last modified by: $Author: soyon.lim $
 * $Revision: 14286 $ $Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.server.util;

import net.sf.infrared.aspects.jdbc.SqlExecuteContext;
import net.sf.infrared.aspects.jdbc.SqlPrepareContext;
import net.sf.infrared.base.model.AggregateExecutionTime;
import net.sf.infrared.base.model.ExecutionContext;
import net.sf.infrared.base.model.StatisticsSnapshot;
import net.sf.infrared2.server.Constant;
import net.sf.infrared2.server.model.AggregateExecutionTimeWithId;
import net.sf.infrared2.server.report.generator.impl.CSV_FormatGeneratorImpl;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * <b>PerformanceDataSnapshot</b><p>
 * Application data structure
 * that represents all statistics need.
 */

public class PerformanceDataSnapshot implements Serializable{

    /** Field serialVersionUID  */
    private static final long serialVersionUID = -2589922031610174644L;
    
    /**
     * The logger.
     */
    private static Log logger = LogFactory.getLog(PerformanceDataSnapshot.class);

    /** Field stats  */
    private StatisticsSnapshot stats;
    /** Field applicationNames  */
    private Set applicationNames;
    /** Field instanceNames  */
    private Set instanceNames;

    /**
     * Method getApplicationNames returns the applicationNames of this PerformanceDataSnapshot object.
     *
     * @return the applicationNames (type Set) of this PerformanceDataSnapshot object.
     */
    public Set getApplicationNames() {
        return applicationNames;
    }

    /**
     * Method setApplicationNames sets the applicationNames of this PerformanceDataSnapshot object.
     *
     * @param applicationNames the applicationNames of this PerformanceDataSnapshot object.
     *
     */
    public void setApplicationNames(Set applicationNames) {
        this.applicationNames = applicationNames;
    }

    /**
     * Method getInstanceNames returns the instanceNames of this PerformanceDataSnapshot object.
     *
     * @return the instanceNames (type Set) of this PerformanceDataSnapshot object.
     */
    public Set getInstanceNames() {
        return instanceNames;
    }

    /**
     * Method setInstanceNames sets the instanceNames of this PerformanceDataSnapshot object.
     *
     * @param instanceNames the instanceNames of this PerformanceDataSnapshot object.
     *
     */
    public void setInstanceNames(Set instanceNames) {
        this.instanceNames = instanceNames;
    }

    /**
     * Method getStats returns the stats of this PerformanceDataSnapshot object.
     *
     * @return the stats (type StatisticsSnapshot) of this PerformanceDataSnapshot object.
     */
    public StatisticsSnapshot getStats() {
        return stats;
    }

    /**
     * Method setStats sets the stats of this PerformanceDataSnapshot object.
     *
     * @param stats the stats of this PerformanceDataSnapshot object.
     *
     */
    public void setStats(StatisticsSnapshot stats) {
        this.stats = stats;
    }

    /**
     * Method getSqlStatistics returns the sqlStatistics of this PerformanceDataSnapshot object.
     *
     * @return the sqlStatistics (type SqlStatistics[]) of this PerformanceDataSnapshot object.
     */
    public SqlStatistics[] getSqlStatistics() {
        Map sqlStatsMap = new HashMap();
        SqlStatistics sqlStats;
        String[] layers = stats.getHierarchicalLayers();
        for (int i = 0; i < layers.length; i++) {
            String layer = layers[i];
            if (!layer.endsWith("SQL")) {
                continue;
            }
            AggregateExecutionTime[] sqlTimes = stats.getExecutionsInHierarchicalLayer(layer);
            for (int j = 0; j < sqlTimes.length; j++) {
                AggregateExecutionTime aet = (AggregateExecutionTime) sqlTimes[j];
                ExecutionContext ctx = aet.getContext();

                if (ctx instanceof SqlPrepareContext) {
                    String sql = ctx.getName();
                    sqlStats = getSqlStatsFromMap(sqlStatsMap, sql);
                    sqlStats.mergePrepareTime(aet);
                } else if (ctx instanceof SqlExecuteContext) {
                    String sql = ctx.getName();
                    sqlStats = getSqlStatsFromMap(sqlStatsMap, sql);
                    sqlStats.mergeExecuteTime(aet);
                }
            }
        }
        return (SqlStatistics[]) sqlStatsMap.values().toArray(new SqlStatistics[0]);
    }

    /**
     * Method getSqlStatisticsForLayer returns sql statistics for the given layer.
     *
     * @param layer of type String
     * @param layerType of type String
     * @return SqlStatistics[]
     */
    public SqlStatistics[] getSqlStatisticsForLayer(String layer, String layerType) {

        Map sqlStatsMap = new HashMap();
        SqlStatistics sqlStats;
        AggregateExecutionTime[] sqlTimes = null;
        if (layerType.equals("hier")) {
            sqlTimes = stats.getExecutionsInHierarchicalLayer(layer);
        } else if (layerType.equals("abs")) {
            sqlTimes = stats.getExecutionsInAbsoluteLayer(layer);
        }
        for (int j = 0; j < sqlTimes.length; j++) {
            AggregateExecutionTime aet = (AggregateExecutionTime) sqlTimes[j];
            ExecutionContext ctx = aet.getContext();

            if (ctx instanceof SqlPrepareContext) {
                String sql = ctx.getName();
                sqlStats = getSqlStatsFromMap(sqlStatsMap, sql);
                sqlStats.mergePrepareTime(aet);
            } else if (ctx instanceof SqlExecuteContext) {
                String sql = ctx.getName();
                sqlStats = getSqlStatsFromMap(sqlStatsMap, sql);
                sqlStats.mergeExecuteTime(aet);
            }
        }
        SqlStatistics[] result = (SqlStatistics[]) sqlStatsMap.values().toArray(new SqlStatistics[0]);
        setSqlStatsIds(result);
        return result;
    }

    /**
     * Method setSqlStatsIds sets the sqlStatsIds of this PerformanceDataSnapshot object.
     *
     * @param result the sqlStatsIds of this PerformanceDataSnapshot object.
     *
     */
    private void setSqlStatsIds(SqlStatistics[] result) {
        for (int i = 0; i < result.length; i++) {
            result[i].setId(Constant.TITLE_QUERY + (i + 1));
        }
    }

    /**
     * Method setAggStatsIds sets the aggStatsIds of this PerformanceDataSnapshot object.
     *
     * @param result the aggStatsIds of this PerformanceDataSnapshot object.
     *
     */
    private void setAggStatsIds(AggregateExecutionTimeWithId[] result) {
        for (int i = 0; i < result.length; i++) {
            result[i].setId(Constant.TITLE_QUERY + (i + 1));
        }
    }

    /**
     * Method getAggregateExecutionTimeWithId adapt AggregateExecutionTime[] to AggregateExecutionTimeWithId[]
     *
     * @param sqlTimes of type AggregateExecutionTime[]
     * @return AggregateExecutionTimeWithId[]
     */
    public static AggregateExecutionTimeWithId[] getAggregateExecutionTimeWithId(AggregateExecutionTime[] sqlTimes) {
        if (sqlTimes == null) return null;
        AggregateExecutionTimeWithId[] aetWithId = new AggregateExecutionTimeWithId[sqlTimes.length];
        for (int i = 0; i < sqlTimes.length; i++) {
            try {
                aetWithId[i] = new AggregateExecutionTimeWithId(sqlTimes[i].getContext());
                BeanUtils.copyProperties(aetWithId[i], sqlTimes[i]);
            } catch (IllegalAccessException e) {
            	logger.error(e.getMessage(), e);
            } catch (InvocationTargetException e) {
            	logger.error(e.getMessage(), e);
            }
        }
        return aetWithId;
    }

    /**
     * Method getSqlDetailsForLayer returs sql details for layer.
     *
     * @param layer of type String
     * @param layerType of type String
     * @return AggregateExecutionTimeWithId[]
     */
    public AggregateExecutionTimeWithId[] getSqlDetailsForLayer(String layer, String layerType) {
        AggregateExecutionTime[] sqlTimes = null;
        if (layerType.equals("hier")) {
            sqlTimes = stats.getExecutionsInHierarchicalLayer(layer);
        } else if (layerType.equals("abs")) {
            sqlTimes = stats.getExecutionsInAbsoluteLayer(layer);
        }
        Map sqlStatsMap = new HashMap();
        AggregateExecutionTimeWithId aetStats;
        AggregateExecutionTimeWithId[] aetWithId = getAggregateExecutionTimeWithId(sqlTimes);
        for (int j = 0; j < aetWithId.length; j++) {
            AggregateExecutionTimeWithId aet = (AggregateExecutionTimeWithId) aetWithId[j];
            ExecutionContext ctx = aet.getContext();

            if (ctx instanceof SqlPrepareContext) {
                aetStats = getAetStatsFromMap(sqlStatsMap, aet);
                if (!aetStats.equals(aet)) {
                    aetStats.merge(aet);
                }
            } else if (ctx instanceof SqlExecuteContext) {
                aetStats = getAetStatsFromMap(sqlStatsMap, aet);
                if (!aetStats.equals(aet)) {
                    aetStats.merge(aet);
                }
            }
        }
        AggregateExecutionTimeWithId[] result = (AggregateExecutionTimeWithId[]) sqlStatsMap.values().toArray(new AggregateExecutionTimeWithId[0]);
        setAggStatsIds(result);
        return result;
    }

    /**
     * Method getAetStatsFromMap returs statistics for operations.
     *
     * @param sqlStatsMap of type Map
     * @param aetWithId of type AggregateExecutionTimeWithId
     * @return AggregateExecutionTimeWithId
     */
    public AggregateExecutionTimeWithId getAetStatsFromMap(Map sqlStatsMap, AggregateExecutionTimeWithId aetWithId) {
        AggregateExecutionTimeWithId stats;
        if (sqlStatsMap.get(aetWithId.getContext().getName()) == null) {
            stats = aetWithId;
            sqlStatsMap.put(aetWithId.getContext().getName(), stats);
        } else {
            stats = (AggregateExecutionTimeWithId) sqlStatsMap.get(aetWithId.getContext().getName());
        }
        return stats;
    }

    /**
     * Method getSqlStatsFromMap returs statistics for sql.
     *
     * @param sqlStatsMap of type Map
     * @param sql of type String
     * @return SqlStatistics
     */
    public SqlStatistics getSqlStatsFromMap(Map sqlStatsMap, String sql) {
        SqlStatistics stats;
        if (sqlStatsMap.get(sql) == null) {
            stats = new SqlStatistics();
            sqlStatsMap.put(sql, stats);
        } else {
            stats = (SqlStatistics) sqlStatsMap.get(sql);
        }
        return stats;
    }
}
