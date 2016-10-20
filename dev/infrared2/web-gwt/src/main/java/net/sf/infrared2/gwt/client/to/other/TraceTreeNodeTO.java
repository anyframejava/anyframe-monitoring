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
 * TraceTO.java Date created: 24.01.2008
 * Last modified by: $Author: soyon.lim $ 
 * $Revision: 14286 $ $Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.gwt.client.to.other;

import java.io.Serializable;
import java.util.Date;

import net.sf.infrared2.gwt.client.Constants;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * <b>TraceTreeNodeTO</b><p>
 * Transfer object for trace tree node.
 * 
 * @author Sergey Evluhin
 */
public class TraceTreeNodeTO implements Serializable, IsSerializable {
    /**
     * Serial version UID
     */
    private static final long serialVersionUID = -6158712970506508729L;
    /** Text of node, without any statistic. */
    private String text;
    /** Name of layer. */    
    private String layerName;
    /** Percent value. */
    private String percent;
    /** Exclusive time value. */
    private int exclusiveTime;
    /** Class name */
    private String className;
    /** Count value. */
    private int count;
    /** Total time value. */ 
    private int totalTime;
    /** Layer name label */
    private String actualLayerName;

    /** General information row TO */
    private GeneralInformationRowTO generalInformationRow;

    /** Array of TraceTreeNodeTO */
    private TraceTreeNodeTO[] children;

    /**
     * Default constructor.
     */
    public TraceTreeNodeTO() {
        super();
    }

    /**
     * Creates transfer object for trace tree node.
     * 
     * @param text - text of node, without any statistic.
     * @param layerName - name of layer.
     * @param percent - percent value.
     * @param exclusiveTime - the value of exclusive time.
     * @param className - the class name
     * @param count - count value.
     * @param totalTime - total time value.
     * @param generalInformationRow - general information row object.
     * @param children- children nodes.
     */
    public TraceTreeNodeTO(String text, String layerName, String percent, int exclusiveTime,
                    String className, int count, int totalTime,
                    GeneralInformationRowTO generalInformationRow, TraceTreeNodeTO[] children) {
        this.text = text;
        this.layerName = layerName;
        this.percent = percent;
        this.exclusiveTime = exclusiveTime;
        this.className = className;
        this.count = count;
        this.totalTime = totalTime;
        this.generalInformationRow = generalInformationRow;
        this.children = children;
    }

    /**
     * @return text of node, without any statistic information.
     */
    public String getText() {
        return text;
    }

    /**
     * @param text - text of node, without any statistic information.
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return name of the layer.
     */
    public String getLayerName() {
        return layerName;
    }

    /**
     * @param layerName - name of the layer.
     */
    public void setLayerName(String layerName) {
        this.layerName = layerName;
    }

    /**
     * @return the current layer name
     */
    public String getActualLayerName() {
        return actualLayerName;
    }

    /**
     * @param actualLayerName
     */
    public void setActualLayerName(String actualLayerName) {
        this.actualLayerName = actualLayerName;
    }

    /**
     * @return percent value.
     */
    public String getPercent() {
        return percent;
    }

    /**
     * @param percent - percent value.
     */
    public void setPercent(String percent) {
        this.percent = percent;
    }

    /**
     * @return total time value.
     */
    public int getTotalTime() {
        return totalTime;
    }

    /**
     * @param totalTime - total time value.
     */
    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    /**
     * @return exclusive time value.
     */
    public int getExclusiveTime() {
        return exclusiveTime;
    }

    /**
     * @param exclusiveTime - exclusive time value.
     */
    public void setExclusiveTime(int exclusiveTime) {
        this.exclusiveTime = exclusiveTime;
    }

    /**
     * @return count value.
     */
    public int getCount() {
        return count;
    }

    /**
     * @param count - count value.
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * @return true if the name of the layer ends with "SQL" keyword.
     */
    public boolean isSqlNode() {
        return this.layerName.endsWith(Constants.TITLE_SQL);
    }

    /**
     * @return array of children (nested) nodes.
     */
    public TraceTreeNodeTO[] getChildren() {
        return children;
    }

    /**
     * @param children - array of children (nested) nodes.
     */
    public void setChildren(TraceTreeNodeTO[] children) {
        this.children = children;
    }

    /**
     * @return class name.
     */
    public String getClassName() {
        return className;
    }

    /**
     * @param className - class name.
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * @return data for table in general information tab.
     */
    public GeneralInformationRowTO getGeneralInformationRow() {
        return generalInformationRow;
    }

    /**
     * @param generalInformationRow - data for table in general information tab.
     */
    public void setGeneralInformationRow(GeneralInformationRowTO generalInformationRow) {
        this.generalInformationRow = generalInformationRow;
    }

    /**
     * Presentation for tree node.
     */
    public String toString() {
        String res = this.text;
        res += "[" + this.layerName + "][";
        if (this.percent != null) {
            res += this.percent + "% - ";
        }
        res += "<font color=\"red\">TotalTime = " + this.totalTime;
        res += "</font>&nbsp;<font color=\"blue\">Exclusive Time = " + this.exclusiveTime;
        res += "</font>&nbsp;<font color=\"green\">Count = " + this.count + "</font>]";
        return res;
    }

    /**
     * Convert array TraceTreeNodeTO objects to two level array of Object-s.
     * 
     * @param rows - array of TraceTreeNodeTO objects.
     * @return Object[][].
     */
    public static Object[][] convertGeneralInformationRows(TraceTreeNodeTO[] rows) {
        if (rows == null) {
            return null;
        }
        Object[][] res = new Object[rows.length][];
        for (int i = 0; i < rows.length; i++) {
            TraceTreeNodeTO row = rows[i];
            if (row == null || row.getGeneralInformationRow() == null)
                continue;
            res[i] = new Object[] {
                            new Integer(row.getGeneralInformationRow().getCount()),
                            new Integer(row.getGeneralInformationRow().getTotalInclusiveTime()),
                            new Integer(row.getGeneralInformationRow().getMaxInclusive()),
                            new Integer(row.getGeneralInformationRow().getMinExclusive()),
                            new Integer(row.getGeneralInformationRow().getTotalExclusiveTime()),
                            new Integer(row.getGeneralInformationRow().getMaxExclusive()),
                            new Integer(row.getGeneralInformationRow().getMinInclusive()),
                            new Date(row.getGeneralInformationRow().getFirstExecutionTime()),
                            new Date(row.getGeneralInformationRow().getLastExecutionTime()),
                            new Integer(row.getGeneralInformationRow()
                                            .getFirstExecutionInclusiveTime()),
                            new Integer(row.getGeneralInformationRow()
                                            .getLastExecutionInclusiveTime()),
                            new Integer(row.getGeneralInformationRow()
                                            .getFirstExecutionExclusiveTime()),
                            new Integer(row.getGeneralInformationRow()
                                            .getLastExecutionExclusiveTime()) };
        }
        return res;
    }

    /**
     * {@inheritDoc}
     */
    public int hashCode() {
        int hash = 4;
        hash = 7 * hash + (text != null ? text.hashCode() : 0);
        hash = 7 * hash + (layerName != null ? layerName.hashCode() : 0);
        hash = 7 * hash + (className != null ? className.hashCode() : 0);
        return hash;
    }

    /**
     * Return true if the text, layerName and className are equals.
     */
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if ((obj == null) || (!(obj instanceof TraceTreeNodeTO)))
            return false;
        TraceTreeNodeTO to = (TraceTreeNodeTO) obj;
        if ((this.text != null) ? this.text.equals(to.getText()) : to.getText() == null) {
            if ((this.layerName != null) ? this.layerName.equals(to.getLayerName()) : to
                            .getLayerName() == null) {
                if ((this.className != null) ? this.className.equals(to.getClassName()) : to
                                .getClassName() == null) {
                    return true;
                }
            }
        }
        return false;
    }
}
