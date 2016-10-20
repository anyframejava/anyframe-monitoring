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
 * ApplicationRowTO.java Date created: 22.01.2008
 * Last modified by: $Author: soyon.lim $ 
 * $Revision: 14286 $ $Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.gwt.client.to.application;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

import net.sf.infrared2.gwt.client.view.facade.ColoredRows;

/**
 * <b>ApplicationRowTO</b><p>
 * Presents row of summary table for application view.
 * 
 * @author Sergey Evluhin
 */
public class ApplicationRowTO implements Serializable, IsSerializable, ColoredRows {
    private static final long serialVersionUID = 6475178032406091010L;
    /** The name of the layer. */
    private String layer;
    /** Total time value. */
    private double totalTime;
    /** Total count value. */
    private int count;
    /** Color of piece of the pie graph builded by total time value. */
    private String timeColor;
    /** Color of piece of the pie graph builded by total count value. */
    private String countColor;

    /**
     * Default constructor.
     */
    public ApplicationRowTO() {
        super();
    }

    /**
     * Create row for applications view table.
     * 
     * @param layer - name of layer.
     * @param totalTime - total time.
     * @param count - total count.
     */
    public ApplicationRowTO(String layer, double totalTime, int totalCount) {
        this();
        this.layer = layer;
        this.totalTime = totalTime;
        this.count = totalCount;
    }

    /**
     * @return the layer name.
     */
    public String getLayer() {
        return layer;
    }

    /**
     * @param layer - the layer name.
     */
    public void setLayer(String layer) {
        this.layer = layer;
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
     * @return total time value.
     */
    public int getCount() {
        return count;
    }

    /**
     * @param count - total count value.
     */
    public void setCount(int totalCount) {
        this.count = totalCount;
    }

    /**
     * @return color of piece of the pie graph builded by total time value.
     */
    public String getTimeColor() {
        return timeColor;
    }

    /**
     * @param timeColor - color of piece of the pie graph builded by total time
     *            value.
     */
    public void setTimeColor(String timeColor) {
        this.timeColor = timeColor;
    }

    /**
     * @return color of piece of the pie graph builded by total count value.
     */
    public String getCountColor() {
        return countColor;
    }

    /**
     * @param countColor - color of piece of the pie graph builded by total time
     *            value.
     */
    public void setCountColor(String countColor) {
        this.countColor = countColor;
    }

    /**
     * {@inheritDoc}
     */
    public String getDescription() {
        return layer;
    }
}
