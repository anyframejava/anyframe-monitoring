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
 * SqlStatistics.java Date created: 22.01.2008
 * Last modified by: $Author: soyon.lim $
 * $Revision: 14286 $ $Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.server.util;

import net.sf.infrared.base.model.AggregateExecutionTime;

/**
 * <b>SqlStatistics</b><p>
 * Class SqlStatistics represents sql statistics.
 *
 * @author gzgonikov
 * Created on 08.04.2008
 */
public class SqlStatistics {

    /** Field sql  */
    private String sql = null;
    /** Field noOfExecutes  */
    private long noOfExecutes;
    /** Field noOfPrepares  */
    private long noOfPrepares;
    /** Field maxExecuteTime  */
    private long maxExecuteTime;
    /** Field maxPrepareTime  */
    private long maxPrepareTime;
    /** Field minExecuteTime  */
    private long minExecuteTime = Long.MAX_VALUE;
    /** Field minPrepareTime  */
    private long minPrepareTime = Long.MAX_VALUE;
    /** Field lastExecuteTime  */
    private long lastExecuteTime;
    /** Field lastPrepareTime  */
    private long lastPrepareTime;
    /** Field firstExecuteTime  */
    private long firstExecuteTime;
    /** Field firstPrepareTime  */
    private long firstPrepareTime;
    /** Field totalExecuteTime  */
    private long totalExecuteTime;
    /** Field totalPrepareTime  */
    private long totalPrepareTime;
    /** Field timeOfFirstPrepare  */
    private long timeOfFirstPrepare = Long.MAX_VALUE;
    /** Field timeOfLastPrepare  */
    private long timeOfLastPrepare;
    /** Field timeOfFirstExecute  */
    private long timeOfFirstExecute = Long.MAX_VALUE;
    /** Field timeOfLastExecute  */
    private long timeOfLastExecute;
    /** Field id  */
    private String id;

    /**
     * Method getAvgExecuteTime returns the avgExecuteTime of this SqlStatistics object.
     *
     * @return the avgExecuteTime (type double) of this SqlStatistics object.
     */
    public double getAvgExecuteTime() {
        if (this.noOfExecutes == 0) {
            return 0;
        } else {
            return ((double) this.totalExecuteTime / this.noOfExecutes);
        }
    }

    /**
     * Method getId returns the id of this SqlStatistics object.
     *
     * @return the id (type String) of this SqlStatistics object.
     */
    public String getId() {
        return id;
    }

    /**
     * Method setId sets the id of this SqlStatistics object.
     *
     * @param id the id of this SqlStatistics object.
     *
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Method getAvgTotalTime returns the avgTotalTime of this SqlStatistics object.
     *
     * @return the avgTotalTime (type double) of this SqlStatistics object.
     */
    public double getAvgTotalTime() {
        return getAvgExecuteTime() + getAvgPrepareTime();
    }

    /**
     * Method setTotalExecuteTime sets the totalExecuteTime of this SqlStatistics object.
     *
     * @param totalExecuteTime the totalExecuteTime of this SqlStatistics object.
     *
     */
    public void setTotalExecuteTime(long totalExecuteTime) {
        this.totalExecuteTime = totalExecuteTime;
    }

    /**
     * Method getAvgPrepareTime returns the avgPrepareTime of this SqlStatistics object.
     *
     * @return the avgPrepareTime (type double) of this SqlStatistics object.
     */
    public double getAvgPrepareTime() {
        if (this.noOfPrepares == 0) {
            return 0;
        } else {
            return ((double) this.totalPrepareTime / this.noOfPrepares);
        }
    }

    /**
     * Method setTotalPrepareTime sets the totalPrepareTime of this SqlStatistics object.
     *
     * @param totalPrepareTime the totalPrepareTime of this SqlStatistics object.
     *
     */
    public void setTotalPrepareTime(long totalPrepareTime) {
        this.totalPrepareTime = totalPrepareTime;
    }

    /**
     * Method getFirstExecuteTime returns the firstExecuteTime of this SqlStatistics object.
     *
     * @return the firstExecuteTime (type long) of this SqlStatistics object.
     */
    public long getFirstExecuteTime() {
        return firstExecuteTime;
    }

    /**
     * Method setFirstExecuteTime sets the firstExecuteTime of this SqlStatistics object.
     *
     * @param firstExecuteTime the firstExecuteTime of this SqlStatistics object.
     *
     */
    public void setFirstExecuteTime(long firstExecuteTime) {
        this.firstExecuteTime = firstExecuteTime;
    }

    /**
     * Method getFirstPrepareTime returns the firstPrepareTime of this SqlStatistics object.
     *
     * @return the firstPrepareTime (type long) of this SqlStatistics object.
     */
    public long getFirstPrepareTime() {
        return firstPrepareTime;
    }

    /**
     * Method setFirstPrepareTime sets the firstPrepareTime of this SqlStatistics object.
     *
     * @param firstPrepareTime the firstPrepareTime of this SqlStatistics object.
     *
     */
    public void setFirstPrepareTime(long firstPrepareTime) {
        this.firstPrepareTime = firstPrepareTime;
    }

    /**
     * Method getLastExecuteTime returns the lastExecuteTime of this SqlStatistics object.
     *
     * @return the lastExecuteTime (type long) of this SqlStatistics object.
     */
    public long getLastExecuteTime() {
        return lastExecuteTime;
    }

    /**
     * Method setLastExecuteTime sets the lastExecuteTime of this SqlStatistics object.
     *
     * @param lastExecuteTime the lastExecuteTime of this SqlStatistics object.
     *
     */
    public void setLastExecuteTime(long lastExecuteTime) {
        this.lastExecuteTime = lastExecuteTime;
    }

    /**
     * Method getLastPrepareTime returns the lastPrepareTime of this SqlStatistics object.
     *
     * @return the lastPrepareTime (type long) of this SqlStatistics object.
     */
    public long getLastPrepareTime() {
        return lastPrepareTime;
    }

    /**
     * Method setLastPrepareTime sets the lastPrepareTime of this SqlStatistics object.
     *
     * @param lastPrepareTime the lastPrepareTime of this SqlStatistics object.
     *
     */
    public void setLastPrepareTime(long lastPrepareTime) {
        this.lastPrepareTime = lastPrepareTime;
    }

    /**
     * Method getMaxExecuteTime returns the maxExecuteTime of this SqlStatistics object.
     *
     * @return the maxExecuteTime (type long) of this SqlStatistics object.
     */
    public long getMaxExecuteTime() {
        return maxExecuteTime;
    }

    /**
     * Method setMaxExecuteTime sets the maxExecuteTime of this SqlStatistics object.
     *
     * @param maxExecuteTime the maxExecuteTime of this SqlStatistics object.
     *
     */
    public void setMaxExecuteTime(long maxExecuteTime) {
        this.maxExecuteTime = maxExecuteTime;
    }

    /**
     * Method getMaxPrepareTime returns the maxPrepareTime of this SqlStatistics object.
     *
     * @return the maxPrepareTime (type long) of this SqlStatistics object.
     */
    public long getMaxPrepareTime() {
        return maxPrepareTime;
    }

    /**
     * Method setMaxPrepareTime sets the maxPrepareTime of this SqlStatistics object.
     *
     * @param maxPrepareTime the maxPrepareTime of this SqlStatistics object.
     *
     */
    public void setMaxPrepareTime(long maxPrepareTime) {
        this.maxPrepareTime = maxPrepareTime;
    }

    /**
     * Method getMinExecuteTime returns the minExecuteTime of this SqlStatistics object.
     *
     * @return the minExecuteTime (type long) of this SqlStatistics object.
     */
    public long getMinExecuteTime() {
        if (this.noOfExecutes == 0) {
            return 0;
        } else {
            return minExecuteTime;
        }

    }

    /**
     * Method setMinExecuteTime sets the minExecuteTime of this SqlStatistics object.
     *
     * @param minExecuteTime the minExecuteTime of this SqlStatistics object.
     *
     */
    public void setMinExecuteTime(long minExecuteTime) {
        this.minExecuteTime = minExecuteTime;
    }

    /**
     * Method getMinPrepareTime returns the minPrepareTime of this SqlStatistics object.
     *
     * @return the minPrepareTime (type long) of this SqlStatistics object.
     */
    public long getMinPrepareTime() {
        if (this.noOfPrepares == 0) {
            return 0;
        } else {
            return minPrepareTime;
        }

    }

    /**
     * Method setMinPrepareTime sets the minPrepareTime of this SqlStatistics object.
     *
     * @param minPrepareTime the minPrepareTime of this SqlStatistics object.
     *
     */
    public void setMinPrepareTime(long minPrepareTime) {
        this.minPrepareTime = minPrepareTime;
    }

    /**
     * Method getNoOfExecutes returns the noOfExecutes of this SqlStatistics object.
     *
     * @return the noOfExecutes (type long) of this SqlStatistics object.
     */
    public long getNoOfExecutes() {
        return noOfExecutes;
    }

    /**
     * Method setNoOfExecutes sets the noOfExecutes of this SqlStatistics object.
     *
     * @param noOfExecutes the noOfExecutes of this SqlStatistics object.
     *
     */
    public void setNoOfExecutes(long noOfExecutes) {
        this.noOfExecutes = noOfExecutes;
    }

    /**
     * Method getNoOfPrepares returns the noOfPrepares of this SqlStatistics object.
     *
     * @return the noOfPrepares (type long) of this SqlStatistics object.
     */
    public long getNoOfPrepares() {
        return noOfPrepares;
    }

    /**
     * Method setNoOfPrepares sets the noOfPrepares of this SqlStatistics object.
     *
     * @param noOfPrepares the noOfPrepares of this SqlStatistics object.
     *
     */
    public void setNoOfPrepares(long noOfPrepares) {
        this.noOfPrepares = noOfPrepares;
    }

    /**
     * Method getSql returns the sql of this SqlStatistics object.
     *
     * @return the sql (type String) of this SqlStatistics object.
     */
    public String getSql() {
        return sql;
    }

    /**
     * Method setSql sets the sql of this SqlStatistics object.
     *
     * @param sql the sql of this SqlStatistics object.
     *
     */
    public void setSql(String sql) {
        this.sql = sql;
    }

    /**
     * Method mergePrepareTime ...
     *
     * @param aggrExec of type AggregateExecutionTime
     */
    public void mergePrepareTime(AggregateExecutionTime aggrExec) {
        if (sql == null) {
            setSql(aggrExec.getContext().getName());
        }
        if (aggrExec.getTimeOfFirstExecution() < timeOfFirstPrepare) {
            timeOfFirstPrepare = aggrExec.getTimeOfFirstExecution();
            firstPrepareTime = aggrExec.getInclusiveFirstExecutionTime();
        }
        if (aggrExec.getTimeOfLastExecution() > timeOfLastPrepare) {
            timeOfLastPrepare = aggrExec.getTimeOfLastExecution();
            lastPrepareTime = aggrExec.getInclusiveLastExecutionTime();
        }
        if (aggrExec.getMaxInclusiveTime() > maxPrepareTime) {
            maxPrepareTime = aggrExec.getMaxInclusiveTime();
        }
        if (aggrExec.getMinInclusiveTime() < minPrepareTime) {
            minPrepareTime = aggrExec.getMinInclusiveTime();
        }
        noOfPrepares += aggrExec.getExecutionCount();
        totalPrepareTime += aggrExec.getTotalInclusiveTime();
    }

    /**
     * Method mergeExecuteTime ...
     *
     * @param aggrExec of type AggregateExecutionTime
     */
    public void mergeExecuteTime(AggregateExecutionTime aggrExec) {
        if (sql == null) {
            setSql(aggrExec.getContext().getName());
        }
        if (aggrExec.getTimeOfFirstExecution() < timeOfFirstExecute) {
            timeOfFirstExecute = aggrExec.getTimeOfFirstExecution();
            firstExecuteTime = aggrExec.getInclusiveFirstExecutionTime();
        }
        if (aggrExec.getTimeOfLastExecution() > timeOfLastExecute) {
            timeOfLastExecute = aggrExec.getTimeOfLastExecution();
            lastExecuteTime = aggrExec.getInclusiveLastExecutionTime();
        }
        if (aggrExec.getMaxInclusiveTime() > maxExecuteTime) {
            maxExecuteTime = aggrExec.getMaxInclusiveTime();
        }
        if (aggrExec.getMinInclusiveTime() < minExecuteTime) {
            minExecuteTime = aggrExec.getMinInclusiveTime();
        }
        noOfExecutes += aggrExec.getExecutionCount();
        totalExecuteTime += aggrExec.getTotalInclusiveTime();

    }

    /**
     * Method getTotalExecuteTime returns the totalExecuteTime of this SqlStatistics object.
     *
     * @return the totalExecuteTime (type long) of this SqlStatistics object.
     */
    public long getTotalExecuteTime() {
        return totalExecuteTime;
    }

    /**
     * Method getTotalPrepareTime returns the totalPrepareTime of this SqlStatistics object.
     *
     * @return the totalPrepareTime (type long) of this SqlStatistics object.
     */
    public long getTotalPrepareTime() {
        return totalPrepareTime;
    }
}
