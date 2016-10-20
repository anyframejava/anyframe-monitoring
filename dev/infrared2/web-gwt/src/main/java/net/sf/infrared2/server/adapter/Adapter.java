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
 * ReportAdapter.java Date created: 22.01.2008
 * Last modified by: $Author: soyon.lim $
 * $Revision: 14286 $ $Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.server.adapter;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.infrared.base.model.AggregateExecutionTime;
import net.sf.infrared.base.model.AggregateOperationTree;
import net.sf.infrared.base.model.LayerTime;
import net.sf.infrared.base.model.StatisticsSnapshot;
import net.sf.infrared.base.util.Tree;
import net.sf.infrared.base.util.TreeNode;
import net.sf.infrared2.gwt.client.Constants;
import net.sf.infrared2.gwt.client.to.ApplicationConfigTO;
import net.sf.infrared2.gwt.client.to.ApplicationEntryTO;
import net.sf.infrared2.gwt.client.to.InstanceEntryTO;
import net.sf.infrared2.gwt.client.to.NavigatorEntryTO;
import net.sf.infrared2.gwt.client.to.NavigatorTO;
import net.sf.infrared2.gwt.client.to.application.ApplicationRowTO;
import net.sf.infrared2.gwt.client.to.application.ApplicationViewTO;
import net.sf.infrared2.gwt.client.to.other.DetailOtherViewTO;
import net.sf.infrared2.gwt.client.to.other.GeneralInformationRowTO;
import net.sf.infrared2.gwt.client.to.other.JdbcRowTO;
import net.sf.infrared2.gwt.client.to.other.SummaryRowTO;
import net.sf.infrared2.gwt.client.to.other.TraceTreeNodeTO;
import net.sf.infrared2.gwt.client.to.sql.SqlRowTO;
import net.sf.infrared2.gwt.client.to.sql.SqlViewTO;
import net.sf.infrared2.server.Constant;
import net.sf.infrared2.server.model.AggregateExecutionTimeWithId;
import net.sf.infrared2.server.util.DataFetchUtil;
import net.sf.infrared2.server.util.PerformanceDataSnapshot;
import net.sf.infrared2.server.util.SqlStatistics;
import net.sf.infrared2.server.util.TreeUtil;
import net.sf.infrared2.server.util.sql.SQLToHtml;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <b>Adapter</b><p>
 *
 * This class adapt collector data-structures to needed by web-appl ones
 *
 * @author gzgonikov
 */
public class Adapter {

    /**
     * The logger.
     */
    private static Log logger = LogFactory.getLog(Adapter.class);

    /**
     * Method getDateFromString parse string with
     * inner date format to get date.
     *
     * @param dateString of type String
     * @return Date
     */
    public static Date getDateFromString(String dateString) {
        if (dateString == null) return null;
        DateFormat dateFormatter = new SimpleDateFormat(Constant.DATE_PATTERN);
        Date result = null;
        try {
            result = dateFormatter.parse(dateString);
        } catch (ParseException e) {
        	logger.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * Method getDateTimeFromString parse string with
     * inner date format to get datetime.
     *
     * @param dateString of type String
     * @return Date
     */
    public static Date getDateTimeFromString(String dateString) {
        if (dateString == null) return null;
        DateFormat dateTimeFormatter = new SimpleDateFormat(Constant.DATE_TIME_PATTERN);
        Date result = null;
        try {
            result = dateTimeFormatter.parse(dateString);
        } catch (ParseException e) {
        	logger.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * Method getAggregateExecutionTimeArray adapt times for the given layer.
     *
     * @param layerName of type String
     * @param perfData of type PerformanceDataSnapshot
     * @param moduleType of type String
     * @return AggregateExecutionTime[]
     */
    public static AggregateExecutionTime[] getAggregateExecutionTimeArray(String layerName,
                                                                          PerformanceDataSnapshot perfData,
                                                                          String moduleType) {
        if (layerName==null || perfData==null || moduleType==null) return null;
        AggregateExecutionTime[] aggregateExecutionTimeArray = null;
        if (Constant.MODULE_ABSOLUTE.equals(moduleType)) {
            StatisticsSnapshot ss = perfData.getStats();
            if (ss!=null) aggregateExecutionTimeArray = ss.getExecutionsInAbsoluteLayer(layerName);
        } else if (Constant.MODULE_HIERARCHICAL.equals(moduleType)) {
            aggregateExecutionTimeArray = perfData.getStats().getExecutionsInHierarchicalLayer(layerName);
        } else if (Constants.MODULE_LAST_INV.equals(moduleType)) {
            int index = Adapter.getSequenceIndex(new NavigatorEntryTO(layerName, moduleType));
            TreeNode headNode = Adapter.getLastInvocationMergedHead(perfData, index);
            List <AggregateExecutionTime> aggregateExecutionTimeList = Adapter.getAggregateExecutionTimeList(headNode);
            aggregateExecutionTimeArray = new AggregateExecutionTime[aggregateExecutionTimeList.size()];
            aggregateExecutionTimeArray = aggregateExecutionTimeList.toArray(aggregateExecutionTimeArray);
        }
        return aggregateExecutionTimeArray;
    }

    /**
     * Method getAggregateExecutionTimeList adapt tree node to the times.
     *
     * @param headNode of type TreeNode
     * @return List<AggregateExecutionTime>
     */
    private static List<AggregateExecutionTime> getAggregateExecutionTimeList(TreeNode headNode) {
        List <AggregateExecutionTime> result = new ArrayList<AggregateExecutionTime>();
        AggregateExecutionTime times = (AggregateExecutionTime) headNode.getValue();
        result.add(times);
        List children = headNode.getChildren();
        getAggregateExecutionTime(children, result);
        return result;
    }

    /**
     * Method getAggregateExecutionTime ...
     *
     * @param children of type List<TreeNode>
     * @param result of type List<AggregateExecutionTime>
     */
    private static void getAggregateExecutionTime(List <TreeNode> children, List <AggregateExecutionTime> result) {
        if (children!=null){
            for (TreeNode aChildren : children) {
                AggregateExecutionTime times = (AggregateExecutionTime) aChildren.getValue();
                result.add(times);
                getAggregateExecutionTime(aChildren.getChildren(), result);
            }
        }
    }

    /**
     * Method getApplicationConfigTO adapt array of applications to corresponding TO.
     *
     * @param applicationEntryTOArray of type ApplicationEntryTO[]
     * @return ApplicationConfigTO
     */
    public static ApplicationConfigTO getApplicationConfigTO(ApplicationEntryTO[] applicationEntryTOArray) {
        if (applicationEntryTOArray == null) return null;
        ApplicationConfigTO applicationConfigTO = new ApplicationConfigTO();
        applicationConfigTO.setApplicationList(applicationEntryTOArray);
        return applicationConfigTO;
    }

    /**
     * Method getInstanceEntryTOArray get all instances for the given application.
     *
     * @param applName of type String
     * @return InstanceEntryTO[]
     */
    public static InstanceEntryTO[] getInstanceEntryTOArray(String applName) {
        if (applName == null) return null;
        Set instances = DataFetchUtil.getInstanceNames(Adapter.getStringAsSet(applName));
        InstanceEntryTO[] result = new InstanceEntryTO[instances.size()];
        int index = 0;
        for (Iterator instanceIterator = instances.iterator(); instanceIterator.hasNext();) {
            result[index++] = Adapter.getInstanceEntryTO((String) instanceIterator.next());
        }
        return result;
    }

    /**
     * Method getInstanceEntryTO get InstanceEntryTO from instance name.
     *
     * @param instanceName of type String
     * @return InstanceEntryTO
     */
    public static InstanceEntryTO getInstanceEntryTO(String instanceName) {
        if (instanceName == null) return null;
        InstanceEntryTO instanceEntryTO;
        instanceEntryTO = new InstanceEntryTO();
        instanceEntryTO.setName(instanceName);
        return instanceEntryTO;
    }

    /**
     * Method getApplicationEntryTO adapt application name to ApplicationEntryTO.
     *
     * @param applName of type String
     * @return ApplicationEntryTO
     */
    public static ApplicationEntryTO getApplicationEntryTO(String applName) {
        if (applName == null) return null;
        ApplicationEntryTO applicationEntryTO;
        applicationEntryTO = new ApplicationEntryTO();
        applicationEntryTO.setApplicationTitle(applName);
        return applicationEntryTO;
    }

    /**
     * Method getStringAsSet makes a <code>String</code> to become a <code>Set</code>
     *
     * @param makeMeSet of type String
     * @return Set<String>
     */
    public static Set <String> getStringAsSet(String makeMeSet) {
        if (makeMeSet == null) return null;
        Set <String> result = new HashSet<String>();
        result.add(makeMeSet);
        return result;
    }

    /**
     * Method getSelectedApplicationsSet return a  <code>Set</code> of
     * selected applications based on given ApplicationConfigTO
     *
     * @param config of type ApplicationConfigTO
     * @return Set
     */
    public static Set getSelectedApplicationsSet(ApplicationConfigTO config) {
        if (config == null || config.getApplicationList() == null) return null;
        Set result = new HashSet();
        ApplicationEntryTO[] applicationEntryTOs = config.getApplicationList();
        for (int i = 0; i < applicationEntryTOs.length; i++) {
            if (applicationEntryTOs[i] != null) {
                result.add(applicationEntryTOs[i].getApplicationTitle());
            }
        }
        return result;
    }

    /**
     * Method getSelectedInstancesSet return a  <code>Set</code> of
     * selected instances based on given ApplicationConfigTO
     *
     * @param config of type ApplicationConfigTO
     * @return Set
     */
    public static Set getSelectedInstancesSet(ApplicationConfigTO config) {
        if (config == null || config.getApplicationList() == null) return null;
        Set result = new HashSet();
        ApplicationEntryTO[] applicationEntryTOs = config.getApplicationList();
        InstanceEntryTO[] instanceEntryTOs;
        for (int i = 0; i < applicationEntryTOs.length; i++) {
            if (applicationEntryTOs[i] == null) continue;
            instanceEntryTOs = applicationEntryTOs[i].getInstanceList();
            for (int j = 0; j < instanceEntryTOs.length; j++) {
                if (instanceEntryTOs[j] != null) {
                    result.add(instanceEntryTOs[j].getName());
                }
            }
        }
        return result;
    }

    /**
     * Method getHierarchicalLayerTimesMap adapt PerformanceDataSnapshot
     * to get times for hierarchical module.
     *
     * @param perfData of type PerformanceDataSnapshot
     * @return Map
     */
    public static Map getHierarchicalLayerTimesMap(PerformanceDataSnapshot perfData) {
        if (perfData == null) return null;
        Map layerTimes = new HashMap();
        String[] layers = perfData.getStats().getHierarchicalLayers();
        for (int j = 0; j < layers.length; j++) {
            String aLayer = layers[j];
            long itsTime = perfData.getStats().getTimeInHierarchicalLayer(aLayer);
            LayerTime lt = new LayerTime(aLayer);
            lt.setTime(itsTime);
            layerTimes.put(aLayer, lt);
        }
        return layerTimes;
    }

    /**
     * Method getAbsoluteLayerTimesMap adapt PerformanceDataSnapshot
     * to get times for absolute module.
     *
     * @param perfData of type PerformanceDataSnapshot
     * @return Map
     */
    public static Map getAbsoluteLayerTimesMap(PerformanceDataSnapshot perfData) {
        if (perfData == null) return null;
        Map layerTimes = new HashMap();
        String[] layers = perfData.getStats().getAbsoluteLayers();
        for (int j = 0; j < layers.length; j++) {
            String aLayer = layers[j];
            long itsTime = perfData.getStats().getTimeInAbsoluteLayer(aLayer);
            LayerTime lt = new LayerTime(aLayer);
            lt.setTime(itsTime);
            layerTimes.put(aLayer, lt);
        }
        return layerTimes;
    }

    /**
     * Method getNavigatorTO adapt infrared-collected data to get web - navigator.
     *
     * @param perfDataMap of type Map<String, PerformanceDataSnapshot>
     * @param sequenceTitle of type String
     * @param config of type ApplicationConfigTO
     * @return NavigatorTO
     */
    public static NavigatorTO getNavigatorTO(Map<String, PerformanceDataSnapshot> perfDataMap,
                                             String sequenceTitle, ApplicationConfigTO config) {
        if (perfDataMap == null) return null;
        NavigatorTO navigatorTO = new NavigatorTO();
        Map hierLayerTimes, absLayerTimes, lastLayerTimes;
        if (config.isDividedByApplications()){
            List <NavigatorEntryTO> absoluteDevidedFinal = new LinkedList <NavigatorEntryTO>();
            List <NavigatorEntryTO> hierDevidedFinal = new LinkedList <NavigatorEntryTO>();
            List <NavigatorEntryTO> lastDevidedFinal = new LinkedList <NavigatorEntryTO>();
            for (String key : perfDataMap.keySet()){
                PerformanceDataSnapshot perfData = perfDataMap.get(key);
                String applicationTitle = Adapter.getApplicationTitle(key);
                hierLayerTimes = Adapter.getHierarchicalLayerTimesMap(perfData);
                absLayerTimes = Adapter.getAbsoluteLayerTimesMap(perfData);
                lastLayerTimes = Adapter.getLastInvokeLayerTimesMap(perfData, sequenceTitle);

                NavigatorEntryTO[] absoluteDevided = Adapter.getNavigatorEntryTOArray(absLayerTimes, Constant.MODULE_ABSOLUTE, applicationTitle);
                NavigatorEntryTO[] hierDevided = Adapter.getNavigatorEntryTOArray(hierLayerTimes, Constant.MODULE_HIERARCHICAL, applicationTitle);
                NavigatorEntryTO[] lastDevided = Adapter.getNavigatorEntryTOArray(lastLayerTimes, Constants.MODULE_LAST_INV, applicationTitle);

                if (absoluteDevided!=null)
                    absoluteDevidedFinal.addAll(Arrays.asList(absoluteDevided));
                if (hierDevided!=null)
                    hierDevidedFinal.addAll(Arrays.asList(hierDevided));
                if (lastDevided!=null)
                    lastDevidedFinal.addAll(Arrays.asList(lastDevided));
            }
            navigatorTO.setAbsolute(absoluteDevidedFinal.toArray(new NavigatorEntryTO[absoluteDevidedFinal.size()]));
            navigatorTO.setHierarchModule(hierDevidedFinal.toArray(new NavigatorEntryTO[hierDevidedFinal.size()]));
            if (config.isLiveDate()){
                navigatorTO.setLastInv(lastDevidedFinal.toArray(new NavigatorEntryTO[lastDevidedFinal.size()]));
            }
        }else{
            PerformanceDataSnapshot perfData = perfDataMap.get(
                    Adapter.getDataSnapshotApplicationKey(config.getApplicationList()));
            hierLayerTimes = Adapter.getHierarchicalLayerTimesMap(perfData);
            absLayerTimes = Adapter.getAbsoluteLayerTimesMap(perfData);
            lastLayerTimes = Adapter.getLastInvokeLayerTimesMap(perfData, sequenceTitle);
            navigatorTO.setAbsolute(Adapter.getNavigatorEntryTOArray(absLayerTimes, Constant.MODULE_ABSOLUTE, Constant.TITLE_APPLICATION));
            navigatorTO.setHierarchModule(Adapter.getNavigatorEntryTOArray(hierLayerTimes, Constant.MODULE_HIERARCHICAL, Constant.TITLE_APPLICATION));
            if (config.isLiveDate()){
                navigatorTO.setLastInv(Adapter.getNavigatorEntryTOArray(lastLayerTimes, Constants.MODULE_LAST_INV, Constant.TITLE_APPLICATION));
            }    
        }
        return navigatorTO;
    }

    /**
     * Method getApplicationTitle return application name from key.
     *
     * @param key of type String
     * @return String
     */
    private static String getApplicationTitle(String key) {
        return key.replaceAll(Constant.APPLICATION_KEY_SEPARATOR, ", ").replaceAll(Constant.INSTANCE_KEY_SEPARATOR, "");
    }

    /**
     * Method getLastInvokeLayerTimesMap adapt PerformanceDataSnapshot
     * to get times for last invocation module.
     *
     * @param perfData of type PerformanceDataSnapshot
     * @param sequenceTitle of type String
     * @return Map
     */
    private static Map getLastInvokeLayerTimesMap(PerformanceDataSnapshot perfData, String sequenceTitle) {
        List lastInvTreeList = perfData.getStats().getLastInvocations();
        Map result = new LinkedHashMap();
        int i=1;
        for (Object aLastInvTreeList : lastInvTreeList) {
            AggregateOperationTree lastInvocationtree = new AggregateOperationTree();
            lastInvocationtree.merge((Tree) aLastInvTreeList);
            TreeNode headNode=null;
            if (lastInvocationtree.getAggregateTree()!=null && lastInvocationtree.getAggregateTree().getRoot()!=null){
                headNode = (TreeNode) lastInvocationtree.getAggregateTree().getRoot().getChildren().get(0);
            }    
            LayerTime lt = new LayerTime(sequenceTitle + i);
            if (headNode!=null){
                if (headNode.getValue()!=null){
                    lt.setTime(((AggregateExecutionTime)headNode.getValue()).getTotalInclusiveTime());
                    result.put(sequenceTitle + i++, lt);
                }
            }
        }
        return result;
    }

    /**
     * Method getNavigatorEntryTO initialize new NavigatorEntryTO
     * based on params given.
     *
     * @param title of type String
     * @param moduleType of type String
     * @param isRoot of type boolean
     * @param applicationKey of type String
     * @return NavigatorEntryTO
     */
    public static NavigatorEntryTO getNavigatorEntryTO(String title, String moduleType, boolean isRoot, String applicationKey) {
        if (title == null || moduleType == null) return null;
        NavigatorEntryTO navigatorEntryTO = new NavigatorEntryTO();
        navigatorEntryTO.setModuleType(moduleType);
        navigatorEntryTO.setTitle(title);
        navigatorEntryTO.setRoot(isRoot);
        navigatorEntryTO.setApplicationKey(applicationKey);
        if (!Constant.TITLE_APPLICATION.equals(applicationKey)){
            ApplicationEntryTO application = Adapter.getApplicationEntryTO(
                    Adapter.getApplicationNameFromTitle(applicationKey));
            navigatorEntryTO.setApplications(application);
            application.setInstanceList(new InstanceEntryTO[]{
                    Adapter.getInstanceEntryTO(Adapter.getInstanceNameFromTitle(applicationKey))});
        }
        return navigatorEntryTO;
    }

    /**
     * Method getInstanceNameFromTitle return application name from key.
     *
     * @param titleApplication of type String
     * @return String
     */
    public static String getInstanceNameFromTitle(String titleApplication) {
        int index = titleApplication.indexOf(",");
        return titleApplication.substring(index+2);
    }

    /**
     * Method getApplicationNameFromTitle ...
     *
     * @param titleApplication of type String
     * @return String
     */
    public static String getApplicationNameFromTitle(String titleApplication) {
        int index = titleApplication.indexOf(",");
        return titleApplication.substring(0, index);
    }

    /**
     * Method getNavigatorEntryTOArray return navigator links list for the given module.
     *
     * @param layerTimes of type Map
     * @param moduleType of type String
     * @param titleApplication of type String
     * @return NavigatorEntryTO[]
     */
    public static NavigatorEntryTO[] getNavigatorEntryTOArray(Map layerTimes, String moduleType, String titleApplication) {
        if (layerTimes == null || moduleType == null || layerTimes.size() == 0) return null;
        NavigatorEntryTO[] navigatorEntryTOs = new NavigatorEntryTO[layerTimes.size() + 1];
        navigatorEntryTOs[0] = Adapter.getNavigatorEntryTO(titleApplication, moduleType, true, titleApplication);
        int index = 1;
        for (Iterator iterator = layerTimes.entrySet().iterator(); iterator.hasNext();) {
            navigatorEntryTOs[index++] = Adapter.getNavigatorEntryTO((String) ((Map.Entry) iterator.next()).getKey(),
                    moduleType, false, titleApplication);
        }
        return navigatorEntryTOs;
    }

    /**
     * Method getApplicationViewTO adapt infrared data to the form needed by application layer.
     *
     * @param perfData of type PerformanceDataSnapshot
     * @param navigatorEntryTO of type NavigatorEntryTO
     * @return ApplicationViewTO
     */
    public static ApplicationViewTO getApplicationViewTO(PerformanceDataSnapshot perfData, NavigatorEntryTO navigatorEntryTO) {
        if (perfData == null || navigatorEntryTO == null) return null;
        ApplicationViewTO applicationViewTO = new ApplicationViewTO();
        Map layerTimes = null;
        if (Constant.MODULE_HIERARCHICAL.equals(navigatorEntryTO.getModuleType())) {
            layerTimes = Adapter.getHierarchicalLayerTimesMap(perfData);
        } else if (Constant.MODULE_ABSOLUTE.equals(navigatorEntryTO.getModuleType())) {
            layerTimes = Adapter.getAbsoluteLayerTimesMap(perfData);
        }else if (Constants.MODULE_LAST_INV.equals(navigatorEntryTO.getModuleType())) {
            layerTimes = Adapter.getLastInvokeLayerTimesMap(perfData, Constant.TITLE_SEQUENCE);
        }
        applicationViewTO.setRows(Adapter.getApplicationRowTOArray(layerTimes, perfData, navigatorEntryTO.getModuleType()));
        return applicationViewTO;
    }

    /**
     * Method getApplicationRowTO forms ApplicationRowTO
     *
     * @param key of type String
     * @param data of type LayerTime
     * @param perfData of type PerformanceDataSnapshot
     * @param moduleType of type String
     * @return ApplicationRowTO
     */
    public static ApplicationRowTO getApplicationRowTO(String key, LayerTime data, PerformanceDataSnapshot perfData,
                                                       String moduleType) {
        if (key == null || data==null || perfData==null || moduleType==null) return null;
        ApplicationRowTO applicationRowTO;
        applicationRowTO = new ApplicationRowTO();
        applicationRowTO.setLayer(key);
        applicationRowTO.setTotalTime(data.getTime());
        applicationRowTO.setCount(Adapter.getApplicationRowTOTotalCount(key, perfData,
                moduleType));
        return applicationRowTO;
    }

    /**
     * Method getApplicationRowTOTotalCount forms ApplicationRow sorted by total count.
     *
     * @param layerName of type String
     * @param perfData of type PerformanceDataSnapshot
     * @param moduleType of type String
     * @return int
     */
    public static int getApplicationRowTOTotalCount(String layerName, PerformanceDataSnapshot perfData,
                                                    String moduleType) {
        if (layerName==null || perfData==null || moduleType==null) return 0;
        AggregateExecutionTime[] aggregateExecutionTimeArray;
        int result = 0;
        aggregateExecutionTimeArray = Adapter.getAggregateExecutionTimeArray(layerName, perfData, moduleType);
        if (aggregateExecutionTimeArray != null) {
            for (int i = 0; i < aggregateExecutionTimeArray.length; i++) {
                result += aggregateExecutionTimeArray[i].getExecutionCount();
            }
        }
        return result;
    }


    /**
     * Method getApplicationRowTOArray forms array of ApplicationRow.
     *
     * @param layerTimes of type Map
     * @param perfData of type PerformanceDataSnapshot
     * @param moduleType of type String
     * @return ApplicationRowTO[]
     */
    public static ApplicationRowTO[] getApplicationRowTOArray(Map layerTimes, PerformanceDataSnapshot perfData,
                                                              String moduleType) {
        if (layerTimes == null) return null;
        ApplicationRowTO[] applicationRowTOs = new ApplicationRowTO[layerTimes.size()];
        int index = 0;
        for (Iterator iterator = layerTimes.entrySet().iterator(); iterator.hasNext();) {
            Map.Entry entry = (Map.Entry) iterator.next();
            applicationRowTOs[index++] = Adapter.getApplicationRowTO((String)entry.getKey(), (LayerTime)entry.getValue(), perfData, moduleType);
        }
        return applicationRowTOs;
    }

    /**
     * Method getTraceTreeNodeTO adapt infrared tree to web tree.
     *
     * @param tree of type TreeNode
     * @return TraceTreeNodeTO
     */
    public static TraceTreeNodeTO getTraceTreeNodeTO(TreeNode tree, SummaryRowTO summaryRow) {
        if (tree == null) return null;
        long OVERALL_TIME=0;
        OVERALL_TIME = ((AggregateExecutionTime) tree.getValue()).getTotalInclusiveTime();
        if (tree.getParent()!=null && tree.getParent().getValue()!=null){
            if (!(tree.getParent().getValue() instanceof String))
                OVERALL_TIME = ((AggregateExecutionTime)tree.getParent().getValue()).getTotalInclusiveTime();
        }
        final NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        AggregateExecutionTime aggExecTime = (AggregateExecutionTime) tree.getValue();
        TraceTreeNodeTO traceTreeNodeTO = new TraceTreeNodeTO();

        if ((summaryRow!=null)&&(summaryRow.getOperationName().equals(aggExecTime.getName()))){
            traceTreeNodeTO.setCount(summaryRow.getCount());
        }else{
            traceTreeNodeTO.setCount(aggExecTime.getExecutionCount());
        }
        traceTreeNodeTO.setExclusiveTime(new Long(aggExecTime.getTotalExclusiveTime()).intValue());
        traceTreeNodeTO.setLayerName(aggExecTime.getLayerName());

        if (aggExecTime.getContext()!=null){
            traceTreeNodeTO.setActualLayerName(aggExecTime.getContext().getLayer());
        }    
        traceTreeNodeTO.setPercent((OVERALL_TIME != 0) ? nf.format((aggExecTime.getTotalInclusiveTime() * 100.0)
                / OVERALL_TIME) : "0");
        traceTreeNodeTO.setText(aggExecTime.getName());
        if ((summaryRow!=null)&&(summaryRow.getOperationName().equals(aggExecTime.getName()))){
            traceTreeNodeTO.setTotalTime((int)summaryRow.getTotalTime());
        }else{
            traceTreeNodeTO.setTotalTime(new Long(aggExecTime.getTotalInclusiveTime()).intValue());
        }
        traceTreeNodeTO.setClassName(aggExecTime.getContext().getClass().getName());

        traceTreeNodeTO.setGeneralInformationRow(Adapter.getGeneralInformationRowTO(Adapter.adapt(aggExecTime)));

        List<TreeNode> children = tree.getChildren();
        if (children.size() > 0) {
            traceTreeNodeTO.setChildren(new TraceTreeNodeTO[children.size()]);
            int i = 0;
            for (Iterator it = children.iterator(); it.hasNext();) {
                traceTreeNodeTO.getChildren()[i++] = getTraceTreeNodeTO((TreeNode) it.next(), summaryRow);
            }
        }
        return traceTreeNodeTO;
    }

    /**
     * Method adapt transform  AggregateExecutionTime to have an ID.
     *
     * @param aggExecTime of type AggregateExecutionTime
     * @return AggregateExecutionTimeWithId
     */
    private static AggregateExecutionTimeWithId adapt(AggregateExecutionTime aggExecTime) {
        AggregateExecutionTimeWithId result = new AggregateExecutionTimeWithId();
        try {
            BeanUtils.copyProperties(result, aggExecTime);
        } catch (IllegalAccessException e) {
        	logger.error(e.getMessage(), e);
        } catch (InvocationTargetException e) {
        	logger.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * Method getSummaryRowTOArray adapt array of TO for summary.
     *
     * @param aggregateExecutionTimeArray of type AggregateExecutionTime[]
     * @param inclusiveMode of type boolean
     * @return SummaryRowTO[]
     */
    public static SummaryRowTO[] getSummaryRowTOArray(AggregateExecutionTime[] aggregateExecutionTimeArray,
                                                      boolean inclusiveMode) {
        if (aggregateExecutionTimeArray == null) return null;
        SummaryRowTO[] summaryRowTOArray = new SummaryRowTO[aggregateExecutionTimeArray.length];
        for (int i = 0; i < aggregateExecutionTimeArray.length; i++) {
            summaryRowTOArray[i] = Adapter.getSummaryRowTO(aggregateExecutionTimeArray[i], inclusiveMode);
        }
        return summaryRowTOArray;
    }

    /**
     * Method getSummaryRowTO adapt TO for summary.
     *
     * @param aggregateExecutionTime of type AggregateExecutionTime
     * @param inclusiveMode of type boolean
     * @return SummaryRowTO
     */
    public static SummaryRowTO getSummaryRowTO(AggregateExecutionTime aggregateExecutionTime, boolean inclusiveMode) {
        if (aggregateExecutionTime == null) return null;
        SummaryRowTO summaryRowTO = new SummaryRowTO();
        if (inclusiveMode) {
            summaryRowTO.setAdjAvg(aggregateExecutionTime.getAdjAverageInclusiveTime());
            summaryRowTO.setAvg(aggregateExecutionTime.getAverageInclusiveTime());
            summaryRowTO.setCount(aggregateExecutionTime.getExecutionCount());
            summaryRowTO.setFirst(new Long(aggregateExecutionTime.getInclusiveFirstExecutionTime()).intValue());
            summaryRowTO.setLast(new Long(aggregateExecutionTime.getInclusiveLastExecutionTime()).intValue());
            summaryRowTO.setMax(new Long(aggregateExecutionTime.getMaxInclusiveTime()).intValue());
            summaryRowTO.setMin(new Long(aggregateExecutionTime.getMinInclusiveTime()).intValue());
            summaryRowTO.setOperationName(aggregateExecutionTime.getName());
            summaryRowTO.setTotalTime(aggregateExecutionTime.getTotalInclusiveTime());
            summaryRowTO.setContextClass(aggregateExecutionTime.getContext().getClass().getName());
        } else {
            summaryRowTO.setAdjAvg(aggregateExecutionTime.getAdjAverageExclusiveTime());
            summaryRowTO.setAvg(aggregateExecutionTime.getAverageExclusiveTime());
            summaryRowTO.setCount(aggregateExecutionTime.getExecutionCount());
            summaryRowTO.setFirst(new Long(aggregateExecutionTime.getExclusiveFirstExecutionTime()).intValue());
            summaryRowTO.setLast(new Long(aggregateExecutionTime.getExclusiveLastExecutionTime()).intValue());
            summaryRowTO.setMax(new Long(aggregateExecutionTime.getMaxExclusiveTime()).intValue());
            summaryRowTO.setMin(new Long(aggregateExecutionTime.getMinExclusiveTime()).intValue());
            summaryRowTO.setOperationName(aggregateExecutionTime.getName());
            summaryRowTO.setTotalTime(aggregateExecutionTime.getTotalExclusiveTime());
            summaryRowTO.setContextClass(aggregateExecutionTime.getContext().getClass().getName());
        }
        return summaryRowTO;
    }

    /**
     * Method getSqlViewTO adapt TO for SQL layer.
     *
     * @param allQueries of type AggregateExecutionTimeWithId[]
     * @param topNQueriesByExecution of type SqlStatistics[]
     * @param topNQueriesByCount of type SqlStatistics[]
     * @param sqlStats of type SqlStatistics[]
     * @return SqlViewTO
     */
    public static SqlViewTO getSqlViewTO(AggregateExecutionTimeWithId[] allQueries, SqlStatistics[] topNQueriesByExecution,
                                         SqlStatistics[] topNQueriesByCount, SqlStatistics[] sqlStats) {
        SqlViewTO sqlViewTO = new SqlViewTO();
        sqlViewTO.setSqlRowsByExecutionCount(Adapter.getSqlRowTOArray(topNQueriesByExecution));
        sqlViewTO.setSqlRowsByExecutionTime(Adapter.getSqlRowTOArray(topNQueriesByCount));
        sqlViewTO.setAllSqlQueries(Adapter.getSqlGeneralInformationRowTOArray(allQueries));
        sqlViewTO.setAllSqlStatistics(Adapter.getSqlRowTOArray(sqlStats));
        return sqlViewTO;
    }

    /**
     * Method getSqlGeneralInformationRowTOArray adapt array of TO for general information tab.
     *
     * @param allQueries of type AggregateExecutionTimeWithId[]
     * @return GeneralInformationRowTO[]
     */
    public static GeneralInformationRowTO[] getSqlGeneralInformationRowTOArray(AggregateExecutionTimeWithId[] allQueries) {
        GeneralInformationRowTO[] sqlGeneralInfoRowTO = new GeneralInformationRowTO[allQueries.length];
        for (int i = 0; i < sqlGeneralInfoRowTO.length; i++) {
            sqlGeneralInfoRowTO[i] = Adapter.getGeneralInformationRowTO(allQueries[i]);
        }
        return sqlGeneralInfoRowTO;
    }

    /**
     * Method getGeneralInformationRowTO adapt TO for general information tab.
     *
     * @param query of type AggregateExecutionTimeWithId
     * @return GeneralInformationRowTO
     */
    public static GeneralInformationRowTO getGeneralInformationRowTO(AggregateExecutionTimeWithId query) {
        GeneralInformationRowTO sqlGeneralInfoRowTO = new GeneralInformationRowTO();
        sqlGeneralInfoRowTO.setCount(query.getExecutionCount());
        sqlGeneralInfoRowTO.setFirstExecutionExclusiveTime(new Long(query.getExclusiveFirstExecutionTime()).intValue());
        sqlGeneralInfoRowTO.setFirstExecutionInclusiveTime(new Long(query.getInclusiveFirstExecutionTime()).intValue());
        sqlGeneralInfoRowTO.setFirstExecutionTime(query.getTimeOfFirstExecution());
        sqlGeneralInfoRowTO.setLastExecutionTime(query.getTimeOfLastExecution());
        sqlGeneralInfoRowTO.setId(query.getId());
        sqlGeneralInfoRowTO.setLastExecutionExclusiveTime(new Long(query.getExclusiveLastExecutionTime()).intValue());
        sqlGeneralInfoRowTO.setLastExecutionInclusiveTime(new Long(query.getInclusiveLastExecutionTime()).intValue());
        sqlGeneralInfoRowTO.setMaxExclusive(new Long(query.getMaxInclusiveTime()).intValue());
        sqlGeneralInfoRowTO.setMaxInclusive(new Long(query.getMaxInclusiveTime()).intValue());
        sqlGeneralInfoRowTO.setMinExclusive(new Long(query.getMinExclusiveTime()).intValue());
        sqlGeneralInfoRowTO.setMinInclusive(new Long(query.getMinInclusiveTime()).intValue());
        try {
            sqlGeneralInfoRowTO.setName(SQLToHtml.formatSql(query.getContext().getName()));
            sqlGeneralInfoRowTO.setNotFormattedSQL(SQLToHtml.formatSqlToPlainString(query.getContext().getName()));
        } catch (IOException e) {
        	logger.error(e.getMessage(), e);
        }
        sqlGeneralInfoRowTO.setTotalInclusiveTime(new Long(query.getTotalInclusiveTime()).intValue());
        sqlGeneralInfoRowTO.setTotalExclusiveTime(new Long(query.getTotalExclusiveTime()).intValue());
        return sqlGeneralInfoRowTO;
    }

    /**
     * Method getSqlRowTOArray adapt array of TO for SQL layer.
     *
     * @param queries of type SqlStatistics[]
     * @return SqlRowTO[]
     */
    public static SqlRowTO[] getSqlRowTOArray(SqlStatistics[] queries) {
        if (queries == null) return null;
        SqlRowTO[] sqlRowTOArray = new SqlRowTO[queries.length];
        for (int i = 0; i < queries.length; i++) {
            sqlRowTOArray[i] = Adapter.getSqlRowTO(queries[i]);
        }
        return sqlRowTOArray;
    }

    /**
     * Method getSqlRowTO adapt TO for SQL layer.
     *
     * @param query of type SqlStatistics
     * @return SqlRowTO
     */
    public static SqlRowTO getSqlRowTO(SqlStatistics query) {
        if (query == null) return null;
        SqlRowTO sqlRowTO = new SqlRowTO();
        sqlRowTO.setId(query.getId());
        sqlRowTO.setTotalExecuteTime((int) query.getTotalExecuteTime());
        sqlRowTO.setTotalPrepareTime((int) query.getTotalPrepareTime());
        sqlRowTO.setAvgExecuteTime(query.getAvgExecuteTime());
        sqlRowTO.setAvgPrepareTime(query.getAvgPrepareTime());
        sqlRowTO.setAvgTotalTime(query.getAvgTotalTime());
        sqlRowTO.setCountExecute(new Long(query.getNoOfExecutes()).intValue());
        sqlRowTO.setCountPrepare(new Long(query.getNoOfPrepares()).intValue());
        sqlRowTO.setFirstExecTimeExecute(new Long(query.getFirstExecuteTime()).intValue());
        sqlRowTO.setFirstExecTimePrepare(new Long(query.getFirstPrepareTime()).intValue());
        sqlRowTO.setLastExecTimeExecute(new Long(query.getLastExecuteTime()).intValue());
        sqlRowTO.setLastExecTimePrepare(new Long(query.getLastPrepareTime()).intValue());
        sqlRowTO.setMaxTimeExecute(new Long(query.getMaxExecuteTime()).intValue());
        sqlRowTO.setMaxTimePrepare(new Long(query.getMaxPrepareTime()).intValue());
        sqlRowTO.setMinTimeExecute(new Long(query.getMinExecuteTime()).intValue());
        sqlRowTO.setMinTimePrepare(new Long(query.getMinPrepareTime()).intValue());
        try {
            sqlRowTO.setSqlQuery(SQLToHtml.formatSql(query.getSql()));
            sqlRowTO.setSqlQueryNotFormatted(SQLToHtml.formatSqlToPlainString(query.getSql()));
        } catch (IOException e) {
        	logger.error(e.getMessage(), e);
        }
        return sqlRowTO;
    }

    /**
     * Method getDetailOtherViewTO adapt TO for details about operation.
     *
     * @param mergedTreeJdbcSummaries of type AggregateExecutionTime[]
     * @param topNQueriesByExecution of type SqlStatistics[]
     * @param topNQueriesByCount of type SqlStatistics[]
     * @return DetailOtherViewTO
     */
    public static DetailOtherViewTO getDetailOtherViewTO(AggregateExecutionTime[] mergedTreeJdbcSummaries,
                                                         SqlStatistics[] topNQueriesByExecution,
                                                         SqlStatistics[] topNQueriesByCount) {
        if (mergedTreeJdbcSummaries == null && (topNQueriesByExecution == null || topNQueriesByCount == null))
            return null;
        DetailOtherViewTO detailOtherViewTO = new DetailOtherViewTO();
        detailOtherViewTO.setJdbcRows(Adapter.getJdbcRowTOArray(mergedTreeJdbcSummaries));
        detailOtherViewTO.setSqlRowsByExecutionCount(Adapter.getSqlRowTOArray(topNQueriesByCount));
        detailOtherViewTO.setSqlRowsByExecutionTime(Adapter.getSqlRowTOArray(topNQueriesByExecution));
        return detailOtherViewTO;
    }

    /**
     * Method getJdbcRowTOArray adapt TO for JDBC tab.
     *
     * @param mergedTreeJdbcSummaries of type AggregateExecutionTime[]
     * @return JdbcRowTO[]
     */
    public static JdbcRowTO[] getJdbcRowTOArray(AggregateExecutionTime[] mergedTreeJdbcSummaries) {
        if (mergedTreeJdbcSummaries == null) return null;
        JdbcRowTO[] jdbcRowTOArray = new JdbcRowTO[mergedTreeJdbcSummaries.length];
        for (int i = 0; i < mergedTreeJdbcSummaries.length; i++) {
            jdbcRowTOArray[i] = Adapter.getJdbcRowTO(mergedTreeJdbcSummaries[i]);
        }
        return jdbcRowTOArray;
    }

    /**
     * Method getJdbcRowTO adapt TO for row in JDBC tab.
     *
     * @param mergedTreeJdbcSummary of type AggregateExecutionTime
     * @return JdbcRowTO
     */
    public static JdbcRowTO getJdbcRowTO(AggregateExecutionTime mergedTreeJdbcSummary) {
        JdbcRowTO jdbcRowTO = new JdbcRowTO();
        jdbcRowTO.setCount(mergedTreeJdbcSummary.getExecutionCount());
        jdbcRowTO.setAvgTime(mergedTreeJdbcSummary.getAverageInclusiveTime());
        jdbcRowTO.setApiName(mergedTreeJdbcSummary.getName());
        return jdbcRowTO;
    }

    /**
     * Method getTraceForLastInvocations adapt TO for last invocation module,
     * trace section.
     *
     * @param navigatorEntryTO of type NavigatorEntryTO
     * @param perfData of type PerformanceDataSnapshot
     * @return TraceTreeNodeTO
     */
    public static TraceTreeNodeTO getTraceForLastInvocations(NavigatorEntryTO navigatorEntryTO,
                                                             PerformanceDataSnapshot perfData) {
        if (navigatorEntryTO==null || perfData==null) return null;
        int index = Adapter.getSequenceIndex(navigatorEntryTO);
        TreeNode headNode = getLastInvocationMergedHead(perfData, index);
        TraceTreeNodeTO traceTreeNodeTO = Adapter.getTraceTreeNodeTO(headNode, null);
        return traceTreeNodeTO;
    }

    /**
     * Method getLastInvocationMergedHead for inner usage.
     *
     * @param perfData of type PerformanceDataSnapshot
     * @param index of type int
     * @return TreeNode
     */
    private static TreeNode getLastInvocationMergedHead(PerformanceDataSnapshot perfData, int index) {
        if (perfData==null) return null;
        List lastInvTreeList = perfData.getStats().getLastInvocations();
        Tree [] lastInvocationTrees = new Tree[lastInvTreeList.size()];
		lastInvTreeList.toArray(lastInvocationTrees);
        AggregateOperationTree lastInvocationtree = new AggregateOperationTree();
        lastInvocationtree.merge(lastInvocationTrees[index-1]);
        TreeNode headNode = (TreeNode)lastInvocationtree.getAggregateTree().getRoot().getChildren().get(0);
        return headNode;
    }

    /**
     * Method getSequenceIndex for inner usage.
     *
     * @param navigatorEntryTO of type NavigatorEntryTO
     * @return int
     */
    private static int getSequenceIndex(NavigatorEntryTO navigatorEntryTO) {
        if (navigatorEntryTO==null) return -1;
        int result = 0;
        for (int i =0; i<navigatorEntryTO.getTitle().length();i++){
            try {
                result = Integer.parseInt(navigatorEntryTO.getTitle().substring(i));
                break;
            } catch (NumberFormatException e) {
            	logger.debug("Can't convert value: " + navigatorEntryTO.getTitle().substring(i) + " to Integer. Retry value without first letter.");
            }
        }
        return result;
    }

    /**
     * Method getTreeNode forms TreeNode.
     *
     * @param perfomanceData of type PerformanceDataSnapshot
     * @param node of type TraceTreeNodeTO
     * @param navTo of type NavigatorEntryTO
     * @return TreeNode
     */
    public static TreeNode getTreeNode(PerformanceDataSnapshot perfomanceData, TraceTreeNodeTO node, NavigatorEntryTO navTo) {
        TreeNode mergedHead;
        if (Constants.MODULE_LAST_INV.equals(navTo.getModuleType())){
            mergedHead = getLastInvocationMergedHead(perfomanceData, Adapter.getSequenceIndex(navTo));
        }
        else {
            mergedHead = TreeUtil.getMergedExecutionContextTreeNode
                (
                        perfomanceData.getStats().getTree(),
                        node.getText(),
                        node.getActualLayerName(),
                        node.getClassName(),
                        navTo.getModuleType().equals(Constant.MODULE_HIERARCHICAL) ? navTo.getTitle() : null
                );
        }
        return mergedHead;
    }

    /**
     * Method getDataSnapshotApplicationKey adapt uniquie key
     * for each application&instance combination.
     *
     * @param to of type ApplicationEntryTO[]
     * @return String
     */
    public static String getDataSnapshotApplicationKey(ApplicationEntryTO[] to) {
        StringBuffer key = new StringBuffer();
        for (ApplicationEntryTO application : to){
            key.append(application.getApplicationTitle());
            key.append(Constant.APPLICATION_KEY_SEPARATOR);
            for (InstanceEntryTO instance : application.getInstanceList()){
                key.append(instance.getName());
                key.append(Constant.INSTANCE_KEY_SEPARATOR);
            }
        }
        return key.toString();
    }

    /**
     * Method getApplicationLayerTotalTime summarize total time
     * for operations in the given layer.
     *
     * @param rows of type ApplicationRowTO[]
     * @return double
     */
    public static double getApplicationLayerTotalTime(ApplicationRowTO[] rows) {
        double result = 0;
        for (ApplicationRowTO to : rows){
            result+=to.getTotalTime();
        }
        return result;
    }

    /**
     * Method getApplicationLayerTotalCount summarize total count
     * for operations in the given layer.
     *
     * @param rows of type ApplicationRowTO[]
     * @return double
     */
    public static double getApplicationLayerTotalCount(ApplicationRowTO[] rows) {
        double result = 0;
        for (ApplicationRowTO to : rows){
            result+=to.getCount();
        }
        return result;
    }

    /**
     * Method getSqlStatisticsArray adapt SqlRowTO array to array of SqlStatistics.
     *
     * @param allSqlStatistics of type SqlRowTO[]
     * @return SqlStatistics[]
     */
    public static SqlStatistics[] getSqlStatisticsArray(SqlRowTO[] allSqlStatistics) {
        if (allSqlStatistics==null) return new SqlStatistics[0];
        SqlStatistics[] result = new SqlStatistics[allSqlStatistics.length];
        int index=0;
        for (SqlRowTO to : allSqlStatistics){
            result[index++] = Adapter.getSqlStatistics(to);
        }
        return result;
    }

    /**
     * Method getSqlStatistics adapt SqlRowTO to SqlStatistics.
     *
     * @param to of type SqlRowTO
     * @return SqlStatistics
     */
    private static SqlStatistics getSqlStatistics(SqlRowTO to) {
        if (to==null) return null;
        SqlStatistics result = new SqlStatistics();
        result.setFirstExecuteTime(to.getFirstExecTimeExecute());
        result.setFirstPrepareTime(to.getFirstExecTimePrepare());
        result.setId(to.getId());
        result.setLastExecuteTime(to.getLastExecTimeExecute());
        result.setLastPrepareTime(to.getLastExecTimePrepare());
        result.setMaxExecuteTime(to.getMaxTimeExecute());
        result.setMaxPrepareTime(to.getMaxTimePrepare());
        result.setMinExecuteTime(to.getMinTimeExecute());
        result.setMinPrepareTime(to.getMinTimePrepare());
        result.setNoOfExecutes(to.getCountExecute());
        result.setNoOfPrepares(to.getCountPrepare());
        result.setSql(to.getSqlQuery());
        result.setTotalExecuteTime((long) to.getTotalExecuteTime());
        result.setTotalPrepareTime((long) to.getTotalPrepareTime());
        return result;
    }
}
