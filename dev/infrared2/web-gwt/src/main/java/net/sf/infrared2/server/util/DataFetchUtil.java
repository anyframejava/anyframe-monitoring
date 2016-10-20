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

import net.sf.infrared.base.model.StatisticsSnapshot;
import net.sf.infrared.collector.Collector;
import net.sf.infrared2.gwt.client.to.ApplicationConfigTO;
import net.sf.infrared2.gwt.client.to.ApplicationEntryTO;
import net.sf.infrared2.gwt.client.to.InstanceEntryTO;
import net.sf.infrared2.server.Constant;
import net.sf.infrared2.server.adapter.Adapter;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * <b>DataFetchUtil</b><p>
 * Class DataFetchUtil contains utility methods for fetching data from collector.
 *
 * @author gzgonikov
 */
public class DataFetchUtil {

    /** Field collector  */
    static Collector collector;
    /** Field SEPERATOR  */
    static final String SEPERATOR = "#";
    /** Field logger  */
    private static final Logger logger = Logger.getLogger(DataFetchUtil.class);

    /**
     * @param config a <code>ApplicationConfigTO</code>
     * @param session a <code>HttpSession</code>
     * @param toBeSavedinSession a <code>boolean</code>
     * @return statistics data stored in session
     */
    public static Map<String, PerformanceDataSnapshot> getPerfomanceData(ApplicationConfigTO config,
                                                                         HttpSession session,
                                                                         boolean toBeSavedinSession) {
        PerformanceDataSnapshot perfData;
//        HttpSession session = getThreadLocalRequest().getSession();

        if (session.getAttribute(Constant.DATA_SNAPSHOT) != null){
            return (Map<String, PerformanceDataSnapshot>) session.getAttribute(Constant.DATA_SNAPSHOT);
        }

        Map <String, PerformanceDataSnapshot> dataSnapshotMap = new HashMap<String, PerformanceDataSnapshot>();
        if (config.isDividedByApplications()){
            for (int i = 0; i<config.getApplicationList().length;i++){
                ApplicationEntryTO application = config.getApplicationList()[i];
                Set <String> selectedApplications = Adapter.getStringAsSet(application.getApplicationTitle());
                for (int j = 0; j<application.getInstanceList().length;j++){
                    InstanceEntryTO instance = application.getInstanceList()[j];
                    Set <String> selectedInstances = Adapter.getStringAsSet(instance.getName());
                    if (config.isLiveDate()){
                        perfData = DataFetchUtil.getPerfData(selectedApplications, selectedInstances);
                    }else {
                        perfData = DataFetchUtil.getDataFromDB(selectedApplications, selectedInstances,
                        Adapter.getDateFromString(config.getStartDateTime()),
                        Adapter.getDateFromString(config.getEndDateTime()));
                    }
                    dataSnapshotMap.put(Adapter.getDataSnapshotApplicationKey(
                            new ApplicationEntryTO[]{new ApplicationEntryTO(
                                    application.getApplicationTitle(), new InstanceEntryTO[]{instance})}),
                            perfData);
                }
            }
            //TODO for testing only; remove after first use
//            try {
//                if (dataSnapshotMap!=null) SerializeHelper.serializeToFile(dataSnapshotMap, SerializeHelper.ARCHIVE_DATA_FILE_NAME);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            if (toBeSavedinSession) session.setAttribute(Constant.DATA_SNAPSHOT, dataSnapshotMap);
        }
        else{
            Set selectedApplications = Adapter.getSelectedApplicationsSet(config);
            Set selectedInstances = Adapter.getSelectedInstancesSet(config);
            if (config.isLiveDate()) {
                perfData = DataFetchUtil.getPerfData(selectedApplications, selectedInstances);


            }else{
                perfData = DataFetchUtil.getDataFromDB(selectedApplications, selectedInstances,
                        Adapter.getDateFromString(config.getStartDateTime()),
                        Adapter.getDateFromString(config.getEndDateTime()));
                //TODO for testing only; remove after first use
//                try {
//                    if (perfData!=null) SerializeHelper.serializeToFile(perfData, SerializeHelper.ARCHIVE_DATA_FILE_NAME);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }
            dataSnapshotMap.put(Adapter.getDataSnapshotApplicationKey(config.getApplicationList()), perfData);
            //TODO for testing only; remove after first use
//                try {
//                    if (perfData!=null) SerializeHelper.serializeToFile(dataSnapshotMap, SerializeHelper.LIVE_DATA_FILE_NAME);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            if (toBeSavedinSession) session.setAttribute(Constant.DATA_SNAPSHOT, dataSnapshotMap);
        }
        return dataSnapshotMap;
    }

    /**
     * Method getPerfData returns corresponding data snapshot from a map.
     *
     * @param appNameSet of type Set
     * @param instNameSet of type Set
     * @return PerformanceDataSnapshot
     */
    public static PerformanceDataSnapshot getPerfData(Set appNameSet, Set instNameSet) {
        //[BINIL] ApplicationStatistics mergedStats = getCollector().fetchStats(appNameSet, instNameSet);
        StatisticsSnapshot mergedStats = getCollector().fetchStats(appNameSet, instNameSet);
        PerformanceDataSnapshot snapShot = new PerformanceDataSnapshot();
        snapShot.setApplicationNames(appNameSet);
        snapShot.setInstanceNames(instNameSet);
        snapShot.setStats(mergedStats);

        return snapShot;
    }

    /**
     * Method reset clear  collector statistics.
     * @return PerformanceDataSnapshot
     */
    public static PerformanceDataSnapshot reset() {
        getCollector().clearStats();

        PerformanceDataSnapshot snapShot = new PerformanceDataSnapshot();
        return snapShot;
    }

    /**
     * Method getDataFromDB fetch data from DB.
     *
     * @param applications of type Set
     * @param instances of type Set
     * @param fromDate of type Date
     * @param toDate of type Date
     * @return PerformanceDataSnapshot
     */
    public static PerformanceDataSnapshot getDataFromDB(Set applications, Set instances,
                                                        Date fromDate, Date toDate) {
        StatisticsSnapshot stats = getCollector().fetchStatsFromDB(applications, instances,
                fromDate, toDate);
        PerformanceDataSnapshot snapShot = new PerformanceDataSnapshot();
        snapShot.setApplicationNames(applications);
        snapShot.setInstanceNames(instances);
        snapShot.setStats(stats);

        return snapShot;

    }

    /**
     * Method getApplicationNames returns the applicationNames of this DataFetchUtil object.
     *
     * @return the applicationNames (type Set) of this DataFetchUtil object.
     */
    public static Set getApplicationNames() {
        return getCollector().getApplicationNames();
    }

    /**
     * Method getInstanceNames returns names of actual instances.
     *
     * @param applicationNames of type Set
     * @return Set
     */
    public static Set getInstanceNames(Set applicationNames) {
        return getCollector().getInstanceNames(applicationNames);
    }

    /**
     * Method setCollector sets the collector of this DataFetchUtil object.
     *
     * @param collectorImpl the collector of this DataFetchUtil object.
     *
     */
    public static void setCollector(Collector collectorImpl) {
        collector = collectorImpl;
    }

    /**
     * Method getCollector returns the collector of this DataFetchUtil object.
     *
     * @return the collector (type Collector) of this DataFetchUtil object.
     */
    public static Collector getCollector() {
        if (collector == null) {
            logger.error("Collector has not been initialized.");
            throw new RuntimeException("Collector has not been initialized");
        }
        return collector;
    }


}
