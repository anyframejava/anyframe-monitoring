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
 * SIRConfigurationManager.java		Date created: 31.01.2008
 * Last modified by: $Author: soyon.lim $
 * $Revision: 14286 $	$Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.gwt.client.utils;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.core.client.GWT;

import net.sf.infrared2.gwt.client.to.ApplicationConfigTO;
import net.sf.infrared2.gwt.client.to.ApplicationEntryTO;
import net.sf.infrared2.gwt.client.to.InstanceEntryTO;
import net.sf.infrared2.gwt.client.view.dialog.DialogHelper;

/**
 * <b>SIRConfigurationManager</b><p>
 * Realize load configuration from cookie and merge data between server .
 *
 * @author Andrey Zavgorodniy
 *         Copyright Exadel Inc, 2008
 */
public class SIRConfigurationManager {
    /** Symbol of application data, after that symbol  starting instances*/
    private final String INSTANCES_SECTION_START_MARKER = ":";/* the &#58  symbol */
    /** Delimiter of instances string */
    private final String INSTANCES_DELIMITER = ",";
    /** Symbol of end one application section */
    private final String APPLICATION_SECTION_END_MARKER = ";";/* the &#59 symbol */
    /** Value for empty symbol */
    private static final String EMPTY_SYMBOL = "";

    /** One instance of  SIRConfigurationManager */
    public static SIRConfigurationManager configManager = new SIRConfigurationManager();
    /** Helper class for working with cookie */
    private static CookieUtils cookieSupporter;

    /**
     * Get current instance of configuration manager.
     *
     * @return SIRConfigurationManager instance
     */
    public static SIRConfigurationManager getInstance() {
        return configManager;
    }

    /**
     * Default constructor.
     */
    public SIRConfigurationManager() {
        cookieSupporter = CookieUtils.getInstance();
    }

    /**
     * Load configuration data stored in cookie.
     * @return - configTO with rewrote configuration
     */
    public ApplicationConfigTO loadFromCookie() {
        ApplicationConfigTO destinationConfigTo = new ApplicationConfigTO();
        if (cookieSupporter.readCookie(cookieSupporter.LIVE_DATE_STATE_COOKIE_NAME) != null) {
            if (cookieSupporter.readCookie(cookieSupporter.LIVE_DATE_STATE_COOKIE_NAME).equals(
                    "true")) {
                destinationConfigTo.setLiveDate(true);
                destinationConfigTo.setArchiveDate(false);
            } else {
                destinationConfigTo.setLiveDate(false);
                destinationConfigTo.setArchiveDate(true);
            }
        }
        destinationConfigTo.setStartDate(cookieSupporter
                .readCookie(cookieSupporter.ARCHIVE_START_DATE_COOKIE_NAME));
        destinationConfigTo.setStartTime(cookieSupporter
                .readCookie(cookieSupporter.ARCHIVE_START_TIME_COOKIE_NAME));
        destinationConfigTo.setEndDate(cookieSupporter
                .readCookie(cookieSupporter.ARCHIVE_END_DATE_COOKIE_NAME));
        destinationConfigTo.setEndTime(cookieSupporter
                .readCookie(cookieSupporter.ARCHIVE_END_TIME_COOKIE_NAME));
        destinationConfigTo
                .setApplicationList(this
                        .cookieSelectApplicationInfoParser(cookieSupporter
                        .readCookie(cookieSupporter.SELECT_APPLICATIONS_COOKIE_NAME)));
        destinationConfigTo.setDividedByApplications("true".equals(cookieSupporter.readCookie(cookieSupporter.DIVIDED_BY_APPLICATION_COOKIE_NAME)));
        destinationConfigTo.setGraphLabelEnabled("true".equals(cookieSupporter.readCookie(cookieSupporter.IS_GRAPH_LABEL_ENABLED_COOKIE_NAME)));
        return destinationConfigTo;
    }

    /**
     * Merge client stored entries array and received from server.
     * @param clientEntry - the array of application entry transfer object stored on client side.
     * @param serverEntry - the array of application entry transfer object received from server side.
     * @return - the merged array of entries presents in both sides
     */
    public ApplicationEntryTO[] clientVsServerConfigComparator(ApplicationEntryTO[] clientEntry,
                    ApplicationEntryTO[] serverEntry) {
        if (clientEntry != null) {
            List summary = new ArrayList();
            List fromClientList = Arrays.asList(clientEntry);
            if (serverEntry != null) {
                /* Hosted mode */
                List fromServerList = Arrays.asList(serverEntry);

                for (Iterator iterator = fromClientList.iterator(); iterator.hasNext();) {
                    ApplicationEntryTO clientStorageAppl = (ApplicationEntryTO) iterator.next();
                    if (clientStorageAppl != null) {
                        for (Iterator iterator2 = fromServerList.iterator(); iterator2.hasNext();) {
                            ApplicationEntryTO serverStorageAppl = (ApplicationEntryTO) iterator2.next();
                            if (serverStorageAppl != null) {
                                if (clientStorageAppl.getApplicationTitle().equals(
                                                serverStorageAppl.getApplicationTitle())) {
                                    ApplicationEntryTO summaryAppl = new ApplicationEntryTO();
                                    summaryAppl.setApplicationTitle(clientStorageAppl.getApplicationTitle());
                                    /* compare instances of applications */
                                    List clientApplInstancesList = Arrays.asList(clientStorageAppl
                                                    .getInstanceList());
                                    List serversApplInstancesList = Arrays.asList(serverStorageAppl
                                                    .getInstanceList());
                                    List instanceSummary = new ArrayList();
                                    for (Iterator iterator3 = clientApplInstancesList.iterator(); iterator3
                                                    .hasNext();) {
                                        InstanceEntryTO cookieInstane = (InstanceEntryTO) iterator3
                                                        .next();
                                        if (cookieInstane != null) {
                                            for (Iterator iterator4 = serversApplInstancesList
                                                            .iterator(); iterator4.hasNext();) {
                                                InstanceEntryTO serverInstance = (InstanceEntryTO) iterator4
                                                                .next();
                                                if (serverInstance != null) {
                                                    if (cookieInstane.getName().equals(
                                                                    serverInstance.getName())) {
                                                        instanceSummary.add(serverInstance);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    InstanceEntryTO[] comparedInstances = (InstanceEntryTO[]) instanceSummary
                                                    .toArray(new InstanceEntryTO[instanceSummary
                                                                    .size()]);
                                    summaryAppl.setInstanceList(comparedInstances);
                                    summary.add(summaryAppl);
                                }
                            }
                        }
                    }
                }
                return (ApplicationEntryTO[]) summary
                                .toArray(new ApplicationEntryTO[summary.size()]);
            } else {
                summary.addAll(fromClientList);
                return (ApplicationEntryTO[]) summary
                                .toArray(new ApplicationEntryTO[summary.size()]);
            }
        } else {
            return null;
        }
    }
    
    /**
     * Save select applications user configuration data in cookie.
     *
     * @param config - the ApplicationConfigTO object
     */
    public void saveInCookie(ApplicationConfigTO config) {
        /* clearDataCache before update */
        this.clearApplicationsCookies();
        try {
            cookieSupporter.writeCookie(cookieSupporter.ARCHIVE_START_DATE_COOKIE_NAME, config.getStartDate());
            cookieSupporter.writeCookie(cookieSupporter.ARCHIVE_START_TIME_COOKIE_NAME, config.getStartTime());
            cookieSupporter.writeCookie(cookieSupporter.ARCHIVE_END_DATE_COOKIE_NAME, config.getEndDate());
            cookieSupporter.writeCookie(cookieSupporter.ARCHIVE_END_TIME_COOKIE_NAME, config.getEndTime());
            cookieSupporter.writeCookie(cookieSupporter.LIVE_DATE_STATE_COOKIE_NAME, String.valueOf(config.isLiveDate()));
            cookieSupporter.writeCookie(cookieSupporter.ARCHIVE_DATE_COOKIE_NAME, String.valueOf(config.isArchiveDate()));
            cookieSupporter.writeCookie(cookieSupporter.DIVIDED_BY_APPLICATION_COOKIE_NAME, String.valueOf(config.isDividedByApplications()));
            cookieSupporter.writeCookie(cookieSupporter.IS_GRAPH_LABEL_ENABLED_COOKIE_NAME, String.valueOf(config.isGraphLabelEnabled()));
            /* Application TO save in cookie */
            ApplicationEntryTO[] entities = config.getApplicationList();
            if (entities != null) {
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < entities.length; i++) {
                    ApplicationEntryTO applicationEntryTO = entities[i];
                    if (applicationEntryTO != null) {
                        /* Application added */
                        sb.append(applicationEntryTO.getApplicationTitle());
                        if (applicationEntryTO.getInstanceList() != null) {
                            sb.append(INSTANCES_SECTION_START_MARKER);
                            for (int j = 0; j < applicationEntryTO.getInstanceList().length; j++) {
                                InstanceEntryTO instanceTO = (InstanceEntryTO) applicationEntryTO.getInstanceList()[j];
                                if (instanceTO != null) {
                                    if (j == applicationEntryTO.getInstanceList().length - 1) {
                                        sb.append(instanceTO.getName());
                                    } else {
                                        sb.append(instanceTO.getName() + INSTANCES_DELIMITER);
                                    }
                                }
                            }
                            sb.append(APPLICATION_SECTION_END_MARKER);
                        } else {
                            sb.append(APPLICATION_SECTION_END_MARKER);
                        }
                    }
                }
                cookieSupporter.writeCookie(cookieSupporter.SELECT_APPLICATIONS_COOKIE_NAME, sb.toString());
                //Window.alert("Writed string in cookie: " + sb.toString());
            }
        } catch (Exception e) {
            GWT.log(e.getMessage(), e);
            DialogHelper.showErrorWindow("Exception in SIRConfigurationManager.saveInCookie(ApplicationConfigTO config)", e.getMessage(), "");
        }
    }

    /**
     * Clear cookies holds select applications info.
     */
    private void clearApplicationsCookies() {
        cookieSupporter.erase(cookieSupporter.LIVE_DATE_STATE_COOKIE_NAME);
        cookieSupporter.erase(cookieSupporter.ARCHIVE_DATE_COOKIE_NAME);
        cookieSupporter.erase(cookieSupporter.ARCHIVE_START_DATE_COOKIE_NAME);
        cookieSupporter.erase(cookieSupporter.ARCHIVE_END_DATE_COOKIE_NAME);
        cookieSupporter.erase(cookieSupporter.SELECT_APPLICATIONS_COOKIE_NAME);
        cookieSupporter.erase(cookieSupporter.DIVIDED_BY_APPLICATION_COOKIE_NAME);
        cookieSupporter.erase(cookieSupporter.IS_GRAPH_LABEL_ENABLED_COOKIE_NAME);        
    }

    /**
     * Parse selected application cookie string in to ApplicationEntryTO[].
     *
     * @param valueFromCookie - cookie saved selected application string
     * @return ApplicationEntryTO[] - the array of ApplicationEntryTO objects
     */
    private ApplicationEntryTO[] cookieSelectApplicationInfoParser(String valueFromCookie) {
        if (valueFromCookie != null) {
            if (!valueFromCookie.equals("")) {
                String[] items = valueFromCookie.split(APPLICATION_SECTION_END_MARKER);
                ApplicationEntryTO[] applications = new ApplicationEntryTO[items.length];
                for (int i = 0; i < items.length; i++) {
                    String buf = items[i];
                    if (buf.equals(EMPTY_SYMBOL)) {
                        continue;
                    }
                    if (buf.indexOf(INSTANCES_SECTION_START_MARKER) != -1) {
                        String application = buf.substring(0, buf
                                        .indexOf(INSTANCES_SECTION_START_MARKER));
                        ApplicationEntryTO applicationEntry = new ApplicationEntryTO();
                        applicationEntry.setApplicationTitle(application);

                        String[] instances = buf.substring(
                                        buf.indexOf(INSTANCES_SECTION_START_MARKER) + 1,
                                        buf.length()).split(INSTANCES_DELIMITER);
                        InstanceEntryTO[] instanceEnties = new InstanceEntryTO[instances.length];
                        for (int j = 0; j < instances.length; j++) {
                            String instanceName = instances[j];
                            InstanceEntryTO instanceEntryTO = new InstanceEntryTO();
                            instanceEntryTO.setName(instanceName);
                            instanceEnties[j] = instanceEntryTO;
                        }
                        applicationEntry.setInstanceList(instanceEnties);
                        applications[i] = applicationEntry;
                    }
                }
                return applications;
            }
            return new ApplicationEntryTO[0];
        }
        else {
            return null;
        }
    }
}
