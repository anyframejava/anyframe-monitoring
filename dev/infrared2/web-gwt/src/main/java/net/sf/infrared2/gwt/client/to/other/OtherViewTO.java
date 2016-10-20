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
 * OtherViewTO.java Date created: 22.01.2008
 * Last modified by: $Author: soyon.lim $ 
 * $Revision: 14286 $ $Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.gwt.client.to.other;

import java.io.Serializable;
import java.util.Map;

import net.sf.infrared2.gwt.client.to.INavigableTO;
import net.sf.infrared2.gwt.client.to.IResultStatus;
import net.sf.infrared2.gwt.client.to.NavigatorEntryTO;
import net.sf.infrared2.gwt.client.to.status.ResultStatus;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * <b>OtherViewTO</b><p>
 * Transfer object for Other view (view is different to Application and SQL).
 * 
 * @author Sergey Evluhin
 */
public class OtherViewTO implements Serializable, IsSerializable, INavigableTO, IResultStatus {
    /**
     * Serial version UID
     */
    private static final long serialVersionUID = 4010787530323564697L;

    /** Current navigation entry transfer object */
    private NavigatorEntryTO navigatorEntryTO;

    /** Current result status object */
    private ResultStatus resultStatus;

    /** Rows for summary table in "Summary" tab only for inclusive mode. */
    private SummaryRowTO[] inclusiveTableRows;

    /** Rows for summary table in "Summary" tab only for exclusive mode. */
    private SummaryRowTO[] exclusiveTableRows;

    /** Trace tree. */
    private TraceTreeNodeTO trace;

    /**
     * The map where key - URL of image, value - HTML code for image that is
     * placed in "Operation by total time" tab. Only for inclusive mode.
     * 
     * @gwt.typeArgs <java.lang.String, java.lang.String>
     */
    private Map imgInclusiveOperationByTimeUrl;

    /**
     * The map where key - URL of image, value - HTML code for image that is
     * placed in "Operation by count". Only for inclusive mode.
     * 
     * @gwt.typeArgs <java.lang.String, java.lang.String>
     */
    private Map imgInclusiveOperationByCountUrl;

    /**
     * The map where key - URL of image, value - HTML code for image that is
     * placed in "Operation by total time" tab. Only for exclusive mode.
     * 
     * @gwt.typeArgs <java.lang.String, java.lang.String>
     */
    private Map imgExclusiveOperationByTimeUrl;

    /**
     * The map where key - URL of image, value - HTML code for image that is
     * placed in "Operation by count". Only for exclusive mode.
     * 
     * @gwt.typeArgs <java.lang.String, java.lang.String>
     */
    private Map imgExclusiveOperationByCountUrl;

    /**
     * Default constructor.
     */
    public OtherViewTO() {
        super();
    }

    /**
     * Creates transfer object with predefined parameters.
     * 
     * @param inclusiveTableRows - rows for summary table in "Summary" tab only
     *            for inclusive mode.
     * @param exclusiveTableRows - rows for summary table in "Summary" tab only
     *            for exclusive mode.
     * @param trace - trace tree.
     * @param imgInclusiveOperationByTimeUrl - the map where key - URL of image,
     *            value - HTML code for image that is placed in "Operation by
     *            total time" tab. Only for inclusive mode.
     * @param imgInclusiveOperationByCountUrl - the map where key - URL of
     *            image, value - HTML code for image that is placed in
     *            "Operation by count". Only for inclusive mode.
     * @param imgExclusiveOperationByTimeUrl - the map where key - URL of image,
     *            value - HTML code for image that is placed in "Operation by
     *            total time" tab. Only for exclusive mode.
     * @param imgExclusiveOperationByCountUrl - the map where key - URL of
     *            image, value - HTML code for image that is placed in
     *            "Operation by count". Only for exclusive mode.
     */
    public OtherViewTO(SummaryRowTO[] inclusiveTableRows, SummaryRowTO[] exclusiveTableRows,
                    TraceTreeNodeTO trace, Map imgInclusiveOperationByTimeUrl,
                    Map imgInclusiveOperationByCountUrl, Map imgExclusiveOperationByTimeUrl,
                    Map imgExclusiveOperationByCountUrl) {
        this.inclusiveTableRows = inclusiveTableRows;
        this.exclusiveTableRows = exclusiveTableRows;
        this.trace = trace;
        this.imgInclusiveOperationByTimeUrl = imgInclusiveOperationByTimeUrl;
        this.imgInclusiveOperationByCountUrl = imgInclusiveOperationByCountUrl;
        this.imgExclusiveOperationByTimeUrl = imgExclusiveOperationByTimeUrl;
        this.imgExclusiveOperationByCountUrl = imgExclusiveOperationByCountUrl;
    }

    /**
     * @return rows for summary table in "Summary" tab only for inclusive mode.
     */
    public SummaryRowTO[] getInclusiveTableRows() {
        return inclusiveTableRows;
    }

    /**
     * @param inclusiveTableRows - rows for summary table in "Summary" tab only
     *            for inclusive mode.
     */
    public void setInclusiveTableRows(SummaryRowTO[] inclusiveTableRows) {
        this.inclusiveTableRows = inclusiveTableRows;
    }

    /**
     * @return trace tree.
     */
    public TraceTreeNodeTO getTrace() {
        return trace;
    }

    /**
     * @param trace - trace tree.
     */
    public void setTrace(TraceTreeNodeTO trace) {
        this.trace = trace;
    }

    /**
     * @return the map where key - URL of image, value - HTML code for image
     *         that is placed in "Operation by total time" tab. Only for
     *         inclusive mode.
     */
    public Map getImgInclusiveOperationByTimeUrl() {
        return imgInclusiveOperationByTimeUrl;
    }

    /**
     * @param imgInclusiveOperationByTimeUrl - the map where key - URL of image,
     *            value - HTML code for image that is placed in "Operation by
     *            total time" tab. Only for inclusive mode.
     */
    public void setImgInclusiveOperationByTimeUrl(Map imgInclusiveOperationByTimeUrl) {
        this.imgInclusiveOperationByTimeUrl = imgInclusiveOperationByTimeUrl;
    }

    /**
     * @return the map where key - URL of image, value - HTML code for image
     *         that is placed in "Operation by count". Only for inclusive mode.
     */
    public Map getImgInclusiveOperationByCountUrl() {
        return imgInclusiveOperationByCountUrl;
    }

    /**
     * @param imgInclusiveOperationByCountUrl - the map where key - URL of
     *            image, value - HTML code for image that is placed in
     *            "Operation by count". Only for inclusive mode.
     */
    public void setImgInclusiveOperationByCountUrl(Map imgInclusiveOperationByCountUrl) {
        this.imgInclusiveOperationByCountUrl = imgInclusiveOperationByCountUrl;
    }

    /**
     * @return the map where key - URL of image, value - HTML code for image
     *         that is placed in "Operation by total time" tab. Only for
     *         exclusive mode.
     */
    public Map getImgExclusiveOperationByTimeUrl() {
        return imgExclusiveOperationByTimeUrl;
    }

    /**
     * @param imgExclusiveOperationByTimeUrl - the map where key - URL of image,
     *            value - HTML code for image that is placed in "Operation by
     *            total time" tab. Only for inclusive mode.
     */
    public void setImgExclusiveOperationByTimeUrl(Map imgExclusiveOperationByTimeUrl) {
        this.imgExclusiveOperationByTimeUrl = imgExclusiveOperationByTimeUrl;
    }

    /**
     * @return the map where key - URL of image, value - HTML code for image
     *         that is placed in "Operation by count". Only for exclusive mode.
     */
    public Map getImgExclusiveOperationByCountUrl() {
        return imgExclusiveOperationByCountUrl;
    }

    /**
     * @param imgExclusiveOperationByCountUrl - the map where key - URL of
     *            image, value - HTML code for image that is placed in
     *            "Operation by count". Only for exclusive mode.
     */
    public void setImgExclusiveOperationByCountUrl(Map imgExclusiveOperationByCountUrl) {
        this.imgExclusiveOperationByCountUrl = imgExclusiveOperationByCountUrl;
    }

    /**
     * @return rows for summary table in "Summary" tab only for exclusive mode.
     */
    public SummaryRowTO[] getExclusiveTableRows() {
        return exclusiveTableRows;
    }

    /**
     * @param exclusiveTableRows - rows for summary table in "Summary" tab only
     *            for exclusive mode.
     */
    public void setExclusiveTableRows(SummaryRowTO[] exclusiveTableRows) {
        this.exclusiveTableRows = exclusiveTableRows;
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
     * Convert array of SummaryRowTO objects to two level array of Objects.
     * 
     * @param rows - array of SummaryRowTO objects.
     * @return Object[][].
     */
    public static Object[][] convertSummaryRows(SummaryRowTO[] rows) {
        if (rows == null) {
            return null;
        }
        Object[][] res = new Object[rows.length][];
        for (int i = 0; i < rows.length; i++) {
            SummaryRowTO row = rows[i];
            res[i] = new Object[] { row.getTimeColor() + "|" + row.getCountColor(),
                            row.getOperationName(), new Double(row.getTotalTime()),
                            new Integer(row.getCount()), new Double(row.getAvg()),
                            new Double(row.getAdjAvg()), new Integer(row.getMin()),
                            new Integer(row.getMax()), new Integer(row.getFirst()),
                            new Integer(row.getLast()) };
        }
        return res;
    }

    /**
     * Convert array of SummaryRowTO objects to two level array of Objects
     * without color value.
     * 
     * @param rows - array of SummaryRowTO objects.
     * @return Object[][].
     */
    public static Object[][] convertSummaryRowsWithoutColors(SummaryRowTO[] rows) {
        if (rows == null) {
            return null;
        }
        Object[][] res = new Object[rows.length][];
        for (int i = 0; i < rows.length; i++) {
            SummaryRowTO row = rows[i];
            res[i] = new Object[] { row.getOperationName(), new Double(row.getTotalTime()),
                            new Integer(row.getCount()), new Double(row.getAvg()),
                            new Double(row.getAdjAvg()), new Integer(row.getMin()),
                            new Integer(row.getMax()), new Integer(row.getFirst()),
                            new Integer(row.getLast()) };
        }
        return res;
    }

}
