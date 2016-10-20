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
 * SummaryRowTO.java Date created: 22.01.2008
 * Last modified by: $Author: soyon.lim $ 
 * $Revision: 14286 $ $Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.gwt.client.to.other;

import java.io.Serializable;

import net.sf.infrared2.gwt.client.view.facade.ColoredRows;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * <b>SummaryRowTO</b><p>
 * Transfer object that presents one row of summary table situated on "Summary"
 * tab of Other view.
 * 
 * @author Sergey Evluhin
 */
public class SummaryRowTO implements Serializable, IsSerializable, ColoredRows {

    /**
     * Serial version UID
     */
    private static final long serialVersionUID = 2245869017517270874L;

    /** Context class of the operation. */
    private String contextClass;
    /** Name of the operation. */
    private String operationName;
    /** Total time value of operation execution. */
    private double totalTime;
    /** Count of calls. */
    private int count;
    /** Average time of execution. */
    private double avg;
    /** Adjusted average time of execution. */
    private double adjAvg;
    /** Minimum time of execution. */
    private int min;
    /** Maximum time of execution. */
    private int max;
    /** Time of the first operation execution time. */
    private int first;
    /** Time of the last operation execution time. */
    private int last;

    /** Color of piece of the pie graph builded by count value. */
    private String countColor;

    /** Color of piece of the pie graph builded by time value. */
    private String timeColor;

    /**
     * Default constructor.
     */
    public SummaryRowTO() {
        super();
    }

    /**
     * Create row for table that situated on summary tab of view different to
     * SQL and Application view.
     * 
     * @param operationName - name of the operation.
     * @param totalTime - total time value of operation execution.
     * @param count - count of calls.
     * @param avg - average time of execution.
     * @param adjAvg - adjusted average time of execution.
     * @param min - minimum time of execution.
     * @param max - maximum time of execution.
     * @param first - time of the first operation execution time.
     * @param last - time of the last operation execution time.
     */
    public SummaryRowTO(String operationName, double totalTime, int count, double avg,
                    double adjAvg, int min, int max, int first, int last) {
        this();
        this.operationName = operationName;
        this.totalTime = totalTime;
        this.count = count;
        this.avg = avg;
        this.adjAvg = adjAvg;
        this.min = min;
        this.max = max;
        this.first = first;
        this.last = last;
    }

    /**
     * @return color of piece of the pie graph builded by count value.
     */
    public String getCountColor() {
        return countColor;
    }

    /**
     * @param countColor - color of piece of the pie graph builded by count
     *            value.
     */
    public void setCountColor(String countColor) {
        this.countColor = countColor;
    }

    /**
     * @return color of piece of the pie graph builded by time value.
     */
    public String getTimeColor() {
        return timeColor;
    }

    /**
     * @param timeColor - color of piece of the pie graph builded by time value.
     */
    public void setTimeColor(String timeColor) {
        this.timeColor = timeColor;
    }

    /**
     * @return context class of the operation.
     */
    public String getContextClass() {
        return contextClass;
    }

    /**
     * @param contextClass - context class of the operation.
     */
    public void setContextClass(String contextClass) {
        this.contextClass = contextClass;
    }

    /**
     * @return name of the operation.
     */
    public String getOperationName() {
        return operationName;
    }

    /**
     * @param operationName - the name of the operation.
     */
    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    /**
     * @return total time value.
     */
    public double getTotalTime() {
        return totalTime;
    }

    /**
     * @param totalTime - total time value.
     */
    public void setTotalTime(double totalTime) {
        this.totalTime = totalTime;
    }

    /**
     * @return count of calls.
     */
    public int getCount() {
        return count;
    }

    /**
     * @param count - count of calls.
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * @return average time of execution.
     */
    public double getAvg() {
        return avg;
    }

    /**
     * @param avg - average time of execution.
     */
    public void setAvg(double avg) {
        this.avg = avg;
    }

    /**
     * @return adjusted average time of execution.
     */
    public double getAdjAvg() {
        return adjAvg;
    }

    /**
     * @param adjAvg - adjusted average time of execution.
     */
    public void setAdjAvg(double adjAvg) {
        this.adjAvg = adjAvg;
    }

    /**
     * @return minimum time of execution.
     */
    public int getMin() {
        return min;
    }

    /**
     * @param min - minimum time of execution.
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * @return maximum time of execution.
     */
    public int getMax() {
        return max;
    }

    /**
     * @param max - maximum time of execution.
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * @return time of the first operation execution time.
     */
    public int getFirst() {
        return first;
    }

    /**
     * @param first - time of the first operation execution time.
     */
    public void setFirst(int first) {
        this.first = first;
    }

    /**
     * @return time of the last operation execution time.
     */
    public int getLast() {
        return last;
    }

    /**
     * @param last - time of the last operation execution time.
     */
    public void setLast(int last) {
        this.last = last;
    }

    /**
     * {@inheritDoc}
     */
    public String getDescription() {
        return operationName;
    }
}
