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
 * DataFetchUtil.java Date created: 22.01.2008
 * Last modified by: $Author: soyon.lim $
 * $Revision: 14286 $ $Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.server.util;


import java.awt.*;
import java.util.*;
import java.util.List;

import net.sf.infrared2.gwt.client.Constants;
import net.sf.infrared2.gwt.client.to.NavigatorEntryTO;
import net.sf.infrared2.gwt.client.to.application.ApplicationRowTO;
import net.sf.infrared2.gwt.client.to.application.ApplicationViewTO;
import net.sf.infrared2.gwt.client.to.comparator.ByCountComparator;
import net.sf.infrared2.gwt.client.to.comparator.ByTimeComparator;
import net.sf.infrared2.gwt.client.to.other.OtherViewTO;
import net.sf.infrared2.gwt.client.to.other.SummaryRowTO;
import net.sf.infrared2.gwt.client.to.sql.SqlRowTO;
import net.sf.infrared2.gwt.client.to.sql.SqlViewTO;
import net.sf.infrared2.server.Constant;
import net.sf.infrared2.server.adapter.Adapter;
import net.sf.infrared2.server.chart.GraphicGenerator;
import net.sf.infrared2.server.chart.SmartImageMap;
import net.sf.infrared2.server.model.PieSectorEntity;

/**
 * <b>GraphicUtil</b><p>
 * Class GraphicUtil contains utility methods for graphic generation.
 *
 * @author gzgonikov
 */
public class GraphicUtil {


    /**
     * smartImageMap keeps map of images. Located in Servlet Context.
     */
    private static SmartImageMap smartImageMap;

    /** Field isGraphLabelEnabled  graph labels - be or not to be ? */
    static boolean isGraphLabelEnabled;

    /**
     * chartDimensionsMap keeps sizes (width and height) all pie3D and Bar
     * Charts.
     */
    private static final HashMap<String, Integer> chartDimensionsMap = new HashMap<String, Integer>();

    private static String graphicServletPattern = "/chart/";

    public static final String imageExtentionPng = ".png";

    private static final String SQL_TOTAL = "Total";
    private static final String SQL_EXECUTE = "Execute";
    private static final String SQL_PREPARE = "Prepare";

    private static final String SEQUENCE = "";

    private static final int MAX_OPERATIONS_IN_GRAPHIC = 10;

    /**
     * Method isGraphLabelEnabled returns the graphLabelEnabled of this GraphicUtil object.
     *
     * @return the graphLabelEnabled (type boolean) of this GraphicUtil object.
     */
    public static boolean isGraphLabelEnabled() {
        return isGraphLabelEnabled;
    }

    /**
     * Method setGraphLabelEnabled sets the graphLabelEnabled of this GraphicUtil object.
     *
     * @param graphLabelEnabled the graphLabelEnabled of this GraphicUtil object.
     *
     */
    public static void setGraphLabelEnabled(boolean graphLabelEnabled) {
        isGraphLabelEnabled = graphLabelEnabled;
    }

    /**
     * Method getSmartImageMap returns the smartImageMap of this GraphicUtil object.
     *
     * @return the smartImageMap (type SmartImageMap) of this GraphicUtil object.
     */
    public static SmartImageMap getSmartImageMap() {
        return smartImageMap;
    }

    /**
     * Method setSmartImageMap sets the smartImageMap of this GraphicUtil object.
     *
     * @param smartImageMap the smartImageMap of this GraphicUtil object.
     *
     */
    public static void setSmartImageMap(SmartImageMap smartImageMap) {
        GraphicUtil.smartImageMap = smartImageMap;
    }

    /**
     * Method getChartDimensionsMap returns the chartDimensionsMap of this GraphicUtil object.
     *
     * @return the chartDimensionsMap (type HashMap<String, Integer>) of this GraphicUtil object.
     */
    public static HashMap<String, Integer> getChartDimensionsMap() {
        return chartDimensionsMap;
    }

    /**
     * Method getGraphicServletPattern returns the graphicServletPattern of this GraphicUtil object.
     *
     * @return the graphicServletPattern (type String) of this GraphicUtil object.
     */
    public static String getGraphicServletPattern() {
        return graphicServletPattern;
    }

    /**
     * Method setGraphicServletPattern sets the graphicServletPattern of this GraphicUtil object.
     *
     * @param graphicServletPattern the graphicServletPattern of this GraphicUtil object.
     *
     */
    public static void setGraphicServletPattern(String graphicServletPattern) {
        GraphicUtil.graphicServletPattern = graphicServletPattern;
    }

    /**
     * Method getImageExtention returns the imageExtention of this GraphicUtil object.
     *
     * @return the imageExtention (type String) of this GraphicUtil object.
     */
    public static String getImageExtention() {
        return imageExtentionPng;
    }

    /**
     * Method getImgUrl forms url of image.
     *
     * @param imgName of type String
     * @param protHostPortContext of type String
     * @return String
     */
    public static String getImgUrl(String imgName, String protHostPortContext) {
        return protHostPortContext + graphicServletPattern + imgName + imageExtentionPng;
    }

    /**
     * Method getOtherViewDataImages generates images for other layer.
     *
     * @param otherViewTO of type OtherViewTO
     * @param sessionId of type String
     * @param contextPath of type String
     * @param otherOperationsName of type String
     */
    public static void getOtherViewDataImages(OtherViewTO otherViewTO, String sessionId,
                                              String contextPath, String otherOperationsName) {

        int width = chartDimensionsMap.get(Constant.PIE3D_CHART_WIDTH_LABEL);
        int height = chartDimensionsMap.get(Constant.PIE3D_CHART_HEIGHT_LABEL);
        Integer timeOut = chartDimensionsMap.get(Constant.IMAGE_TIME_OUT);

        HashMap<String, Number> byTimeIncl = new HashMap<String, Number>();
        HashMap<String, Number> byCountIncl = new HashMap<String, Number>();
        HashMap<String, Number> byTimeExcl = new HashMap<String, Number>();
        HashMap<String, Number> byCountExcl = new HashMap<String, Number>();

        List inclusiveRowsListByTime = Arrays.asList(otherViewTO.getInclusiveTableRows());
        List exclusiveRowsListByTime = Arrays.asList(otherViewTO.getExclusiveTableRows());
        List inclusiveRowsListByCount = new ArrayList(inclusiveRowsListByTime);
        List exclusiveRowsListByCount = new ArrayList(exclusiveRowsListByTime);

        Collections.sort(inclusiveRowsListByTime, new ByTimeComparator());
        Collections.sort(exclusiveRowsListByTime, new ByTimeComparator());
        Collections.sort(inclusiveRowsListByCount, new ByCountComparator());
        Collections.sort(exclusiveRowsListByCount, new ByCountComparator());

        int index=1;
        double excByTimeCounter=0;
        double incByTimeCounter=0;
        double excByCountCounter=0;
        double incByCountCounter=0;

        SummaryRowTO incByTimeRowTO;
        SummaryRowTO excByTimeRowTO;
        SummaryRowTO incByCountRowTO;
        SummaryRowTO excByCountRowTO;

        double totalInclTime = getTotalTime(inclusiveRowsListByTime);
        double totalExclTime = getTotalTime(exclusiveRowsListByTime);
        int totalCount = getTotalCount(inclusiveRowsListByCount);

        Set<PieSectorEntity> byTimeInclusiveSet = new LinkedHashSet<PieSectorEntity>();
        Set<PieSectorEntity> byCountInclusiveSet = new LinkedHashSet<PieSectorEntity>();
        Set<PieSectorEntity> byTimeExclusiveSet = new LinkedHashSet<PieSectorEntity>();
        Set<PieSectorEntity> byCountExclusiveSet = new LinkedHashSet<PieSectorEntity>();
        boolean otherByTimePresents = false;
        boolean otherByCountPresents = false;
        boolean otherPresents = false;
        for (Iterator incByTimeIt = inclusiveRowsListByTime.iterator(), excByTimeIt = exclusiveRowsListByTime.iterator(),
                    incByCountIt = inclusiveRowsListByCount.iterator(), excByCountIt = exclusiveRowsListByCount.iterator();
                    incByTimeIt.hasNext();) {
            incByTimeRowTO = (SummaryRowTO)incByTimeIt.next();
            excByTimeRowTO = (SummaryRowTO)excByTimeIt.next();
            incByCountRowTO = (SummaryRowTO)incByCountIt.next();
            excByCountRowTO = (SummaryRowTO)excByCountIt.next();
            if (index<=MAX_OPERATIONS_IN_GRAPHIC){
                if (lessThan95Percent(totalInclTime, byTimeIncl)
                                && incByTimeRowTO.getTotalTime() > 0) {
                    byTimeInclusiveSet.add(new PieSectorEntity(incByTimeRowTO.getOperationName(),
                                    incByTimeRowTO.getTotalTime(), getNextColor(byTimeInclusiveSet
                                                    .size())));
                } else {
                    incByTimeCounter += incByTimeRowTO.getTotalTime();
                    otherByTimePresents = true;
                }

                if (lessThan95Percent(totalExclTime, byTimeExcl)
                                && excByTimeRowTO.getTotalTime() > 0) {
                    byTimeExclusiveSet.add(new PieSectorEntity(excByTimeRowTO.getOperationName(),
                                    excByTimeRowTO.getTotalTime(), getNextColor(byTimeExclusiveSet
                                                    .size())));
                } else {
                    excByTimeCounter += excByTimeRowTO.getTotalTime();
                    otherByTimePresents = true;
                }

                if (lessThan95Percent(totalCount, byCountIncl) && incByCountRowTO.getCount() > 0) {
                    byCountInclusiveSet.add(new PieSectorEntity(incByCountRowTO.getOperationName(),
                                    incByCountRowTO.getCount(), getNextColor(byCountInclusiveSet
                                                    .size())));
                } else {
                    incByCountCounter += incByCountRowTO.getTotalTime();
                    otherByCountPresents = true;
                }
                if (lessThan95Percent(totalCount, byCountExcl) && excByCountRowTO.getCount() > 0) {
                    byCountExclusiveSet.add(new PieSectorEntity(excByCountRowTO.getOperationName(),
                                    excByCountRowTO.getCount(), getNextColor(byCountExclusiveSet
                                                    .size())));
                } else {
                    excByCountCounter += excByCountRowTO.getTotalTime();
                    otherByCountPresents = true;
                }
            } else {
                incByTimeCounter+=incByTimeRowTO.getTotalTime();
                excByTimeCounter+=excByTimeRowTO.getTotalTime();
                incByCountCounter+=incByCountRowTO.getCount();
                excByCountCounter+=excByCountRowTO.getCount();
                otherPresents = true;
            }
            if (index == inclusiveRowsListByTime.size()
                            && (otherPresents || otherByCountPresents || otherByTimePresents)) {
                if (otherByTimePresents || otherPresents) {
                    byTimeInclusiveSet.add(new PieSectorEntity(otherOperationsName,
                                    incByTimeCounter, getNextColor(byTimeInclusiveSet.size())));
                    byTimeExclusiveSet.add(new PieSectorEntity(otherOperationsName,
                                    excByTimeCounter, getNextColor(byTimeExclusiveSet.size())));
                }
                if (otherByCountPresents || otherPresents) {
                    byCountInclusiveSet.add(new PieSectorEntity(otherOperationsName,
                                    incByCountCounter, getNextColor(byCountInclusiveSet.size())));
                    byCountExclusiveSet.add(new PieSectorEntity(otherOperationsName,
                                    excByCountCounter, getNextColor(byCountExclusiveSet.size())));
                }
            }
            index++;
        }

        GraphicGenerator byTimeInclGraphic = new GraphicGenerator(byTimeInclusiveSet);
        GraphicGenerator byCountInclGraphic = new GraphicGenerator(byCountInclusiveSet);

        GraphicGenerator byTimeExclGraphic = new GraphicGenerator(byTimeExclusiveSet);
        GraphicGenerator byCountExclGraphic = new GraphicGenerator(byCountExclusiveSet);

        Map<String,  String> byTimeInclColors = byTimeInclGraphic.getColorToSectorMap();
        Map<String,  String> byCountInclColors = byCountInclGraphic.getColorToSectorMap();
        Map<String,  String> byTimeExclColors = byTimeExclGraphic.getColorToSectorMap();
        Map<String,  String> byCountExclColors = byCountExclGraphic.getColorToSectorMap();

        setByTimeInclColors(byTimeInclColors, otherViewTO, otherOperationsName);
        setByCountInclColors(byCountInclColors, otherViewTO, otherOperationsName);
        setByTimeExclColors(byTimeExclColors, otherViewTO, otherOperationsName);
        setByCountExclColors(byCountExclColors, otherViewTO, otherOperationsName);

        Map<String, String> byTimeInclImgName = byTimeInclGraphic.burnSmartImage(smartImageMap, width, height, sessionId, timeOut);
        Map<String, String> byCountInclImgName = byCountInclGraphic.burnSmartImage(smartImageMap, width, height, sessionId, timeOut);

        Map<String, String> byTimeExclImgName = byTimeExclGraphic.burnSmartImage(smartImageMap, width, height, sessionId, timeOut);
        Map<String, String> byCountExclImgName = byCountExclGraphic.burnSmartImage(smartImageMap, width, height, sessionId, timeOut);

        String name = byTimeInclImgName.keySet().iterator().next();
        Map<String, String> img = new HashMap<String, String>();
        img.put(getImgUrl(name, contextPath), getImgMapTag(byTimeInclImgName.get(name), width, height, name, contextPath));
        otherViewTO.setImgInclusiveOperationByTimeUrl(img);

        name = byCountInclImgName.keySet().iterator().next();
        img = new HashMap<String, String>();
        img.put(getImgUrl(name, contextPath), getImgMapTag(byCountInclImgName.get(name), width, height, name, contextPath));
        otherViewTO.setImgInclusiveOperationByCountUrl(img);

        name = byTimeExclImgName.keySet().iterator().next();
        img = new HashMap<String, String>();
        img.put(getImgUrl(name, contextPath), getImgMapTag(byTimeExclImgName.get(name), width, height, name, contextPath));
        otherViewTO.setImgExclusiveOperationByTimeUrl(img);

        name = byCountExclImgName.keySet().iterator().next();
        img = new HashMap<String, String>();
        img.put(getImgUrl(name, contextPath), getImgMapTag(byCountExclImgName.get(name), width, height, name, contextPath));
        otherViewTO.setImgExclusiveOperationByCountUrl(img);
    }

    /**
     * Method getApplicationDataImages generates images for application layer.
     *
     * @param applViewTO of type ApplicationViewTO
     * @param sessionId of type String
     * @param contextPath of type String
     * @param navigatorEntryTO of type NavigatorEntryTO
     * @param otherOperationsName of type String
     */
    public static void getApplicationDataImages(ApplicationViewTO applViewTO, String sessionId,
                                                String contextPath, NavigatorEntryTO navigatorEntryTO,
                                                String otherOperationsName) {

        int width = GraphicUtil.getChartDimensionsMap().get(Constant.PIE3D_CHART_WIDTH_LABEL);
        int height = GraphicUtil.getChartDimensionsMap().get(Constant.PIE3D_CHART_HEIGHT_LABEL);
        Integer timeOut = GraphicUtil.getChartDimensionsMap().get(Constant.IMAGE_TIME_OUT);

        GraphicGenerator byTimeGraphic;
        GraphicGenerator byCountGraphic;

        if (Constants.MODULE_LAST_INV.equals(navigatorEntryTO.getModuleType())){
            ArrayList<String> series = new ArrayList<String>();
            series.add(SEQUENCE);

            Double[][] barDataArray = new Double[series.size()][applViewTO.getRows().length];
            ArrayList<String> categories = new ArrayList<String>(applViewTO.getRows().length);

            for (int i = 0; i < applViewTO.getRows().length; i++) {
                ApplicationRowTO applRowTO = applViewTO.getRows()[i];
                categories.add(applRowTO.getLayer());
                barDataArray[0][i] = applRowTO.getTotalTime();
            }
            byTimeGraphic = new GraphicGenerator(series, categories, barDataArray, Constant.GRAPHIC_TITLE_MS);
            final Map<String, String> byTimeImgName = byTimeGraphic.burnSmartImage(smartImageMap, width, height, sessionId, timeOut);
            Map<String, String> img = new HashMap<String, String>();
            img.put(getImgUrl(byTimeImgName.keySet().iterator().next(), contextPath), null);
            applViewTO.setImgByTimeUrl(img);

            barDataArray = new Double[series.size()][applViewTO.getRows().length];
            for (int i = 0; i < applViewTO.getRows().length; i++) {
                ApplicationRowTO applRowTO = applViewTO.getRows()[i];
                barDataArray[0][i] = new Double(applRowTO.getCount());
            }
            byCountGraphic = new GraphicGenerator(series, categories, barDataArray, Constant.GRAPHIC_TITLE_COUNTS);
            final Map<String, String> byCountImgName = byCountGraphic.burnSmartImage(smartImageMap, width, height, sessionId, timeOut);
            img = new HashMap<String, String>();
            img.put(getImgUrl(byCountImgName.keySet().iterator().next(), contextPath), null);
            applViewTO.setImgByCountUrl(img);
            return;
        }

        HashMap<String, Number> byTime = new HashMap<String, Number>();
        HashMap<String, Number> byCount = new HashMap<String, Number>();

        Set<PieSectorEntity> byTimeSet = new LinkedHashSet<PieSectorEntity>();
        Set<PieSectorEntity> byCountSet = new LinkedHashSet<PieSectorEntity>();

        double totalTime = Adapter.getApplicationLayerTotalTime(applViewTO.getRows());
        double totalCount= Adapter.getApplicationLayerTotalCount(applViewTO.getRows());

        double timeOther=0;
        double countOther=0;

        List<ApplicationRowTO> byTimeList = Arrays.asList(applViewTO.getRows());
        List<ApplicationRowTO> byCountList = new ArrayList<ApplicationRowTO>(byTimeList);
        Collections.sort(byTimeList, new net.sf.infrared2.gwt.client.to.comparator.ByTimeComparator());
        Collections.sort(byCountList, new net.sf.infrared2.gwt.client.to.comparator.ByCountComparator());

        Iterator <ApplicationRowTO> byTimeIter = byTimeList.iterator();
        Iterator <ApplicationRowTO> byCountIter = byCountList.iterator();
        boolean otherPresents = false;
        boolean otherByTimePresents = false;
        boolean otherByCountPresents = false;
        for (int i = 0; i < applViewTO.getRows().length; i++) {
            ApplicationRowTO byTimeRowTO = byTimeIter.next();
            ApplicationRowTO byCountRowTO = byCountIter.next();
            if (i<MAX_OPERATIONS_IN_GRAPHIC){
                if (lessThan95Percent(totalTime, byTime) && byTimeRowTO.getTotalTime()>0){
                    byTimeSet.add(new PieSectorEntity(byTimeRowTO.getLayer(), byTimeRowTO.getTotalTime(), getNextColor(byTimeSet.size())));
                } else {
                    timeOther+=byTimeRowTO.getTotalTime();
                    otherByTimePresents = true;
                }
                if (lessThan95Percent(totalCount, byCount) && byCountRowTO.getCount()>0){
                    byCountSet.add(new PieSectorEntity(byCountRowTO.getLayer(), byCountRowTO.getCount(), getNextColor(byCountSet.size())));
                }else {
                    countOther+=byCountRowTO.getCount();
                    otherByCountPresents = true;
                }
            }
            else {
                timeOther+=byTimeRowTO.getTotalTime();
                countOther+=byCountRowTO.getCount();
                otherPresents = true;
            }
            if (i==applViewTO.getRows().length-1&&(otherPresents||otherByCountPresents||otherByTimePresents)){
                if(otherPresents||otherByTimePresents){
                    byTimeSet.add(new PieSectorEntity(otherOperationsName, timeOther, getNextColor(byTimeSet.size())));
                }
                if(otherPresents||otherByCountPresents){
                    byCountSet.add(new PieSectorEntity(otherOperationsName, countOther, getNextColor(byCountSet.size())));
                }
            }
        }
        byTimeGraphic = new GraphicGenerator(byTimeSet);
        byCountGraphic = new GraphicGenerator(byCountSet);

        Map<String,  String> byTimeColors = byTimeGraphic.getColorToSectorMap();
        Map<String,  String> byCountColors = byCountGraphic.getColorToSectorMap();

        setByTimeColors(byTimeColors, applViewTO, otherOperationsName);
        setByCountColors(byCountColors, applViewTO, otherOperationsName);

        Map<String, String> byTimeImgName = byTimeGraphic.burnSmartImage(GraphicUtil.getSmartImageMap(), width, height, sessionId, timeOut);
        Map<String, String> byCountImgName = byCountGraphic.burnSmartImage(GraphicUtil.getSmartImageMap(), width, height, sessionId, timeOut);
        String byTimeKey = byTimeImgName.keySet().iterator().next();
        String byCountKey = byCountImgName.keySet().iterator().next();
        String byTimeHtml = byTimeImgName.get(byTimeKey);
        String byCountHtml = byCountImgName.get(byCountKey);

        Map<String, String> img = new HashMap<String, String>();
        img.put(getImgUrl(byTimeKey, contextPath), getImgMapTag(byTimeHtml, width, height, byTimeKey, contextPath));
        applViewTO.setImgByTimeUrl(img);

        img = new HashMap<String, String>();
        img.put(getImgUrl(byCountKey, contextPath), getImgMapTag(byCountHtml, width, height, byCountKey, contextPath));
        applViewTO.setImgByCountUrl(img);
    }

    /**
     * Method getSqlViewDataImages generates images for sql layer.
     *
     * @param sqlViewTO of type SqlViewTO
     * @param sessionId of type String
     * @param contextPath of type String
     */
    public static void getSqlViewDataImages(SqlViewTO sqlViewTO, String sessionId, String contextPath) {
        ArrayList<String> series = new ArrayList<String>();
        series.add(SQL_TOTAL);
        series.add(SQL_EXECUTE);
        series.add(SQL_PREPARE);

        Double[][] barDataArrayByTime = new Double[series.size()][sqlViewTO.getSqlRowsByExecutionTime().length];
        ArrayList<String> categories = new ArrayList<String>(sqlViewTO.getSqlRowsByExecutionTime().length);
        for (int i = 0; i < sqlViewTO.getSqlRowsByExecutionTime().length; i++) {
            SqlRowTO sqlRowTO = sqlViewTO.getSqlRowsByExecutionTime()[i];
            categories.add(sqlRowTO.getId());

            barDataArrayByTime[0][i] = sqlRowTO.getAvgTotalTime();
            barDataArrayByTime[1][i] = sqlRowTO.getAvgExecuteTime();
            barDataArrayByTime[2][i] = sqlRowTO.getAvgPrepareTime();

        }

        GraphicGenerator byTimeGraphic = new GraphicGenerator(series, categories, barDataArrayByTime, Constant.GRAPHIC_TITLE_MS);

        Double[][] barDataArrayByCount = new Double[series.size()][sqlViewTO.getSqlRowsByExecutionCount().length];
        ArrayList<String> categories2 = new ArrayList<String>(sqlViewTO.getSqlRowsByExecutionCount().length);
        for (int i = 0; i < sqlViewTO.getSqlRowsByExecutionCount().length; i++) {
            SqlRowTO sqlRowTO = sqlViewTO.getSqlRowsByExecutionCount()[i];
            categories2.add(sqlRowTO.getId());

            barDataArrayByCount[0][i] = sqlRowTO.getAvgTotalTime();
            barDataArrayByCount[1][i] = sqlRowTO.getAvgExecuteTime();
            barDataArrayByCount[2][i] = sqlRowTO.getAvgPrepareTime();

        }

        GraphicGenerator byCountGraphic = new GraphicGenerator(series, categories2, barDataArrayByCount, Constant.GRAPHIC_TITLE_MS);

        int width = chartDimensionsMap.get(Constant.PIE3D_CHART_WIDTH_LABEL);
        int height = chartDimensionsMap.get(Constant.PIE3D_CHART_HEIGHT_LABEL);
        Integer timeOut = chartDimensionsMap.get(Constant.IMAGE_TIME_OUT);

        Map<String, String> byTimeImgName = byTimeGraphic.burnSmartImage(smartImageMap, width, height, sessionId, timeOut);
        Map<String, String> byCountImgName = byCountGraphic.burnSmartImage(smartImageMap, width, height, sessionId, timeOut);

        Map<String, String> img = new HashMap<String, String>();
        img.put(getImgUrl(byTimeImgName.keySet().iterator().next(), contextPath),
                getImgTag(width, height, byTimeImgName.keySet().iterator().next(), contextPath));
        sqlViewTO.setImgUrlByTime(img);

        img = new HashMap<String, String>();
        img.put(getImgUrl(byCountImgName.keySet().iterator().next(), contextPath),
                getImgTag(width, height, byCountImgName.keySet().iterator().next(), contextPath));
        sqlViewTO.setImgUrlByCount(img);
    }

    /**
     * Method setByCountColors sets counl colors.
     *
     * @param byCountColors of type Map<String, String>
     * @param applViewTO of type ApplicationViewTO
     * @param otherOperationsName of type String
     */
    private static void setByCountColors(Map<String, String> byCountColors, ApplicationViewTO applViewTO,
                                         String otherOperationsName) {
        for (ApplicationRowTO to : applViewTO.getRows()){
            if (byCountColors.get(to.getLayer())==null){
                to.setCountColor(byCountColors.get(otherOperationsName));
            }
            else {
                to.setCountColor(byCountColors.get(to.getLayer()));
            }
        }
    }

    /**
     * Method setByTimeColors sets time colors.
     *
     * @param byTimeColors of type Map<String, String>
     * @param applViewTO of type ApplicationViewTO
     * @param otherOperationsName of type String
     */
    private static void setByTimeColors(Map<String, String> byTimeColors, ApplicationViewTO applViewTO,
                                        String otherOperationsName) {
        for (ApplicationRowTO to : applViewTO.getRows()){
            if (byTimeColors.get(to.getLayer())==null){
                to.setTimeColor(byTimeColors.get(otherOperationsName));
            }
            else {
                to.setTimeColor(byTimeColors.get(to.getLayer()));
            }
        }
    }

    /**
     * Method setByCountExclColors sets exclusive count colors.
     *
     * @param byCountExclColors of type Map<String, String>
     * @param otherViewTO of type OtherViewTO
     * @param otherOperationsName of type String
     */
    private static void setByCountExclColors(Map<String, String> byCountExclColors, OtherViewTO otherViewTO, String otherOperationsName) {
        for (SummaryRowTO to : otherViewTO.getExclusiveTableRows()){
            if (byCountExclColors.get(to.getOperationName())==null){
                to.setCountColor(byCountExclColors.get(otherOperationsName));
            }
            else {
                to.setCountColor(byCountExclColors.get(to.getOperationName()));
            }
        }
    }

    /**
     * Method setByTimeExclColors sets exclusive time colors.
     *
     * @param byTimeExclColors of type Map<String, String>
     * @param otherViewTO of type OtherViewTO
     * @param otherOperationsName of type String
     */
    private static void setByTimeExclColors(Map<String, String> byTimeExclColors, OtherViewTO otherViewTO, String otherOperationsName) {
        for (SummaryRowTO to : otherViewTO.getExclusiveTableRows()){
            if (byTimeExclColors.get(to.getOperationName())==null){
                to.setTimeColor(byTimeExclColors.get(otherOperationsName));
            }
            else {
                to.setTimeColor(byTimeExclColors.get(to.getOperationName()));
            }
        }
    }

    /**
     * Method setByCountInclColors sets inclusive count colors.
     *
     * @param byCountInclColors of type Map<String, String>
     * @param otherViewTO of type OtherViewTO
     * @param otherOperationsName of type String
     */
    private static void setByCountInclColors(Map<String, String> byCountInclColors, OtherViewTO otherViewTO, String otherOperationsName) {
        for (SummaryRowTO to : otherViewTO.getInclusiveTableRows()){
            if (byCountInclColors.get(to.getOperationName())==null){
                to.setCountColor(byCountInclColors.get(otherOperationsName));
            }
            else {
                to.setCountColor(byCountInclColors.get(to.getOperationName()));
            }
        }
    }

    /**
     * Method setByTimeInclColors sets inclusive time colors.
     *
     * @param byTimeInclColors of type Map<String, String>
     * @param otherViewTO of type OtherViewTO
     * @param otherOperationsName of type String
     */
    private static void setByTimeInclColors(Map<String, String> byTimeInclColors, OtherViewTO otherViewTO, String otherOperationsName) {
        for (SummaryRowTO to : otherViewTO.getInclusiveTableRows()){
            if (byTimeInclColors.get(to.getOperationName())==null){
                to.setTimeColor(byTimeInclColors.get(otherOperationsName));
            }
            else {
                to.setTimeColor(byTimeInclColors.get(to.getOperationName()));
            }
        }
    }

    /**
     * Method getNextColor returs next color from pallete.
     *
     * @param index of type int
     * @return Color
     */
    private static Color getNextColor(int index){
        int[] colorPalit = Constant.COLOR_SET[index];
        return new Color(colorPalit[0], colorPalit[1], colorPalit[2]);
    }

    /**
     * Method lessThan95Percent check is next operation fall into total 95%.
     *
     * @param total of type double
     * @param times of type HashMap<String, Number>
     * @return boolean
     */
    private static boolean lessThan95Percent(double total, HashMap<String, Number> times) {
        if (total==0) return false;
        double now = 0;
        Set <String> keys = times.keySet();
        for(String key : keys){
            now+=new Double(times.get(key).toString());
        }
        double percent = total/100;
        return now/percent<95;
    }

    /**
     * Method getTotalCount returs total count.
     *
     * @param rows of type List<SummaryRowTO>
     * @return int
     */
    private static int getTotalCount(List <SummaryRowTO> rows) {
        int result = 0;
        for (SummaryRowTO to : rows){
            result+=to.getCount();
        }
        return result;
    }

    /**
     * Method getTotalTime returs total time.
     *
     * @param rows of type List<SummaryRowTO>
     * @return double
     */
    private static double getTotalTime(List <SummaryRowTO> rows) {
        double result = 0;
        for (SummaryRowTO to : rows){
            result+=to.getTotalTime();
        }
        return result;
    }

    /**
     * Method getImgMapTag return html for image with tooltip mapping.
     *
     * @param html of type String
     * @param width of type int
     * @param height of type int
     * @param name of type String
     * @param contextPath of type String
     * @return String
     */
    private static String getImgMapTag(String html, int width, int height, String name, String contextPath){
        return html + "<IMG SRC='"
                            + GraphicUtil.getImgUrl(name, contextPath)
                            + "' style='filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src="+GraphicUtil.getImgUrl(name, contextPath)+");'"
                            
                            + " WIDTH='"
                            +width
                            +"' HEIGHT='"
                            +height
                            +"' BORDER='0' USEMAP='#"
                            +name
                            +"'>";
    }

    /**
     * Method getImgTag returns img tag without binding to map.
     *
     * @param width of type int
     * @param height of type int
     * @param name of type String
     * @param contextPath of type String
     * @return String
     */
    private static String getImgTag(int width, int height, String name, String contextPath){
        String imgUrl = GraphicUtil.getImgUrl(name, contextPath);
        return "<IMG SRC='"
                        + imgUrl
                        + "' style='filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src="+imgUrl+");'"
                        + " WIDTH='"
                        +width
                        +"' HEIGHT='"
                        +height
                        +"' BORDER='0'>";
    }

}
