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
 * ApplicationViewTO.java Date created: 22.01.2008
 * Last modified by: $Author: soyon.lim $ 
 * $Revision: 14286 $ $Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.gwt.client.to.application;

import java.io.Serializable;
import java.util.Map;

import net.sf.infrared2.gwt.client.to.INavigableTO;
import net.sf.infrared2.gwt.client.to.IResultStatus;
import net.sf.infrared2.gwt.client.to.NavigatorEntryTO;
import net.sf.infrared2.gwt.client.to.status.ResultStatus;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * <b>ApplicationViewTO</b><p>
 * Transfer object for Application view.
 * 
 * @author Sergey Evluhin
 */
public class ApplicationViewTO implements Serializable, IsSerializable, INavigableTO, IResultStatus {
    
    /** Serial Version UID */
    private static final long serialVersionUID = 6421149289097409730L;

    /** Rows for summary table. */
    private ApplicationRowTO[] rows;

    /** Present module type */
    private String moduleType;

    /**
     * The map where key - URL of image, value - HTML code for image that is
     * placed in "Layers By Total Time" tab.
     * 
     * @gwt.typeArgs <java.lang.String, java.lang.String>
     */
    private Map imgByTimeUrl;
    /**
     * The map where key - URL of image, value - HTML code for image that is
     * placed in "Layers By Total Count" tab.
     * 
     * @gwt.typeArgs <java.lang.String, java.lang.String>
     */
    private Map imgByCountUrl;

    /**
     * Entry of navigator panel associated with view that was builded by using
     * data from this object.
     */
    private NavigatorEntryTO navigatorEntryTO;

    private ResultStatus resultStatus;

    /**
     * Default constructor.
     */
    public ApplicationViewTO() {
        super();
    }

    /**
     * Creates transfer object.
     * 
     * @param rows - rows for summary table.
     * @param moduleType - type of the module (absolute, hierarchical or last
     *            invocation).
     * @param imgByTimeUrl - data for the image builded by total time values.
     * @param imgByCountUrl - data for the image builded by total count values.
     */
    public ApplicationViewTO(ApplicationRowTO[] rows, String moduleType, Map imgByTimeUrl,
                    Map imgByCountUrl) {
        this();
        this.rows = rows;
        this.moduleType = moduleType;
        this.imgByTimeUrl = imgByTimeUrl;
        this.imgByCountUrl = imgByCountUrl;
    }

    /**
     * @return the map where key - URL of image, value - HTML code for image
     *         that is placed in "Layers By Total Time" tab.
     */
    public Map getImgByTimeUrl() {
        return imgByTimeUrl;
    }

    /**
     * @param imgByTimeUrl - the map where key - URL of image, value - HTML code
     *            for image that is placed in "Layers By Total Time" tab.
     */
    public void setImgByTimeUrl(Map imgByTimeUrl) {
        this.imgByTimeUrl = imgByTimeUrl;
    }

    /**
     * @return the map where key - URL of image, value - HTML code for image
     *         that is placed in "Layers By Total Count" tab.
     */
    public Map getImgByCountUrl() {
        return imgByCountUrl;
    }

    /**
     * @param imgByCountUrl - the map where key - URL of image, value - HTML
     *            code for image that is placed in "Layers By Total Count" tab.
     */
    public void setImgByCountUrl(Map imgByCountUrl) {
        this.imgByCountUrl = imgByCountUrl;
    }

    /**
     * @return rows for summary table.
     */
    public ApplicationRowTO[] getRows() {
        return rows;
    }

    /**
     * @param rows - array rows for summary table.
     */
    public void setRows(ApplicationRowTO[] rows) {
        this.rows = rows;
    }

    /**
     * @return type of the module.
     */
    public String getModuleType() {
        return moduleType;
    }

    /**
     * @param moduleType - type of the module.
     */
    public void setModuleType(String moduleType) {
        this.moduleType = moduleType;
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
     * @return the result status of the call.
     */
    public ResultStatus getResultStatus() {
        return resultStatus;
    }

    /**
     * @param the result status of the call.
     */
    public void setResultStatus(ResultStatus resultStatus) {
        this.resultStatus = resultStatus;
    }

    /**
     * Convert array of ApplicationRowTO to two level Object array.
     * 
     * @param rows - rows for table.
     * @return rows converted to Object[][].
     */
    public static Object[][] convertApplicationRowTO(ApplicationRowTO[] rows) {
        if (rows == null) {
            return null;
        }
        Object[][] res = new Object[rows.length][];
        for (int i = 0; i < rows.length; i++) {
            ApplicationRowTO row = rows[i];
            res[i] = new Object[] { row.getTimeColor() + "|" + row.getCountColor(), row.getLayer(),
                            new Integer(new Double(row.getTotalTime()).intValue()),
                            new Integer(row.getCount()) };
        }
        return res;
    }

    /**
     * Convert array of ApplicationRowTO to two level Object array without
     * colors values.
     * 
     * @param rows - rows for table.
     * @return rows converted to Object[][].
     */
    public static Object[][] convertApplicationRowTOWithoutColors(ApplicationRowTO[] rows) {
        if (rows == null) {
            return null;
        }
        Object[][] res = new Object[rows.length][];
        for (int i = 0; i < rows.length; i++) {
            ApplicationRowTO row = rows[i];
            res[i] = new Object[] { row.getLayer(),
                            new Integer(new Double(row.getTotalTime()).intValue()),
                            new Integer(row.getCount()) };
        }
        return res;
    }

}
