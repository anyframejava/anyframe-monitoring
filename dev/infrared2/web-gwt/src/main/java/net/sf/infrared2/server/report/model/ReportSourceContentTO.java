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
 * ReportSourseContentTO.java		Date created: 22.02.2008
 * Last modified by: $Author: soyon.lim $
 * $Revision: 14286 $	$Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.server.report.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import net.sf.infrared2.gwt.client.report.ReportConfigTO;
import net.sf.infrared2.gwt.client.to.ApplicationConfigTO;
import net.sf.infrared2.gwt.client.to.NavigatorEntryTO;
import net.sf.infrared2.gwt.client.to.NavigatorTO;
import net.sf.infrared2.server.Constant;
import net.sf.infrared2.server.adapter.Adapter;

/**
 * <b>ReportSourceContentTO</b><p>
 * Keeps raw data as content for report.
 * @author Gleb Zgonikov
 *
 */
public class ReportSourceContentTO implements ReportSourceI{

    /** Field data  */
    private Map<NavigatorEntryTO, Serializable> data;
    /** Field navigator  */
    private NavigatorTO navigator;
    /** Field applConfig  */
    private ApplicationConfigTO applConfig;
    /** Field reportConfig  */
    private ReportConfigTO reportConfig;

    /**
     * Constructor.
     */
    public ReportSourceContentTO() {
    }

    /**
     * Constructor.
     * @param data - data container.
     * @param navigator - list of navigator links.
     * @param applConfig - client side application configuration.
     * @param reportConfig - client side report configuration.
     */
    public ReportSourceContentTO(Map<NavigatorEntryTO, Serializable> data, NavigatorTO navigator,
                                 ApplicationConfigTO applConfig, ReportConfigTO reportConfig) {
        this.data = data;
        this.navigator = navigator;
        this.applConfig = applConfig;
        this.reportConfig = reportConfig;
    }

    /**
     * Method getTO returns corresponding TO based on navigator.
     *
     * @param navTO of type NavigatorEntryTO
     * @return Serializable
     */
    public Serializable getTO(NavigatorEntryTO navTO) {
        return getData().get(navTO);
    }

    /**
     * Method getNavigatorEntryTOArray returns List
     * of NavigatorEntryTO based on module type.
     *
     * @param moduleType of type String
     * @return List<NavigatorEntryTO>
     *
     * @see ReportSourceI#getNavigatorEntryTOArray(String)
     */
    public List<NavigatorEntryTO> getNavigatorEntryTOArray(String moduleType) {
        if (moduleType==null) return null;
        if (moduleType.equals(Constant.MODULE_ABSOLUTE)){
            if (reportConfig.isAllDisplayed()){
                return getNavigatorEntryTOArray(getNavigator().getAbsolute());
            }
            else return getNavigatorEntryTOArray(getReportConfig().getCurrentlySelectedString(),
                    getNavigator().getAbsolute());
        }
        if (moduleType.equals(Constant.MODULE_HIERARCHICAL)){
            if (reportConfig.isAllDisplayed()){
                return getNavigatorEntryTOArray(getNavigator().getHierarchModule());
            }
            else return getNavigatorEntryTOArray(getReportConfig().getCurrentlySelectedString(),
                    getNavigator().getHierarchModule());
        }
        return null;
    }

    /**
     * Method getNavigatorEntryTOArray List
     * of NavigatorEntryTO based on currently selected string.
     *
     * @param currentlySelectedString of type String
     * @param navEntries of type NavigatorEntryTO[]
     * @return List<NavigatorEntryTO>
     */
    private List<NavigatorEntryTO> getNavigatorEntryTOArray(String currentlySelectedString,
                                                            NavigatorEntryTO[] navEntries) {
        if (navEntries==null || currentlySelectedString==null) return null;
        List<NavigatorEntryTO> result = new ArrayList<NavigatorEntryTO>();
        if (!applConfig.isDividedByApplications()){
            return result = Arrays.asList(navEntries);
        }
        String applName = Adapter.getApplicationNameFromTitle(currentlySelectedString);
        String instanceName = Adapter.getInstanceNameFromTitle(currentlySelectedString);
        if (applName==null || instanceName==null) return null;
        for (NavigatorEntryTO entry : navEntries){
            if (entry==null) continue;
                if (entry.getApplications().getApplicationTitle().equals(applName)){
                    if (entry.getApplications().getInstanceList()==null ||
                            entry.getApplications().getInstanceList().length==0) continue;
                    if (entry.getApplications().getInstanceList()[0].getName().equals(instanceName)){
                        result.add(entry);
                    }
                }
        }
        return result;

    }

    /**
     * Method getNavigatorEntryTOArray adapt NavigatorEntryTO [] to List<NavigatorEntryTO>
     *
     * @param entiesArray of type NavigatorEntryTO[]
     * @return List<NavigatorEntryTO>
     */
    private List<NavigatorEntryTO> getNavigatorEntryTOArray(NavigatorEntryTO [] entiesArray){
        if (entiesArray==null) return null;
        return Arrays.asList(entiesArray);
    }

    /**
     * @see ReportSourceI#getApplConfig()
     */
    public ApplicationConfigTO getApplConfig() {
        return applConfig;
    }

    /**
     * @see ReportSourceI#setApplConfig(ApplicationConfigTO)
     */
    public void setApplConfig(ApplicationConfigTO applConfig) {
        this.applConfig = applConfig;
    }

    /**
     * @see ReportSourceI#getData()
     */
    public Map<NavigatorEntryTO, Serializable> getData() {
        return data;
    }

    /**
     * @see ReportSourceI#setData(Map<NavigatorEntryTO, Serializable>)
     */
    public void setData(Map<NavigatorEntryTO, Serializable> data) {
        this.data = data;
    }

    /**
     * @see ReportSourceI#getNavigator()
     */
    public NavigatorTO getNavigator() {
        return navigator;
    }

    /**
     * @see ReportSourceI#setNavigator(NavigatorTO)
     */
    public void setNavigator(NavigatorTO navigator) {
        this.navigator = navigator;
    }

    /**
     * Method getReportConfig returns the reportConfig of this ReportSourceContentTO object.
     *
     * @return the reportConfig (type ReportConfigTO) of this ReportSourceContentTO object.
     */
    public ReportConfigTO getReportConfig() {
        return reportConfig;
    }

    /**
     * Method setReportConfig sets the reportConfig of this ReportSourceContentTO object.
     *
     * @param reportConfig the reportConfig of this ReportSourceContentTO object.
     *
     */
    public void setReportConfig(ReportConfigTO reportConfig) {
        this.reportConfig = reportConfig;
    }
}
