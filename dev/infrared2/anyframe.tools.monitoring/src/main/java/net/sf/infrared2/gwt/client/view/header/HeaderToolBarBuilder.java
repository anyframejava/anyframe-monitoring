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
 * HeaderToolBarBuilder.java		Date created: 30.01.2008
 * Last modified by: $Author: soyon.lim $
 * $Revision: 14286 $	$Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.gwt.client.view.header;

import net.sf.infrared2.gwt.client.i18n.ApplicationMessages;
import net.sf.infrared2.gwt.client.to.ApplicationConfigTO;
import net.sf.infrared2.gwt.client.to.ApplicationEntryTO;
import net.sf.infrared2.gwt.client.to.InstanceEntryTO;

/**
 * <b>HeaderToolBarBuilder</b><p>Class build string of selected application <br>
 * after chose in select application dialog according special rules.<br>
 * For example: application 1\\ instance1, instance2 etc <br>
 * Info string needs for select application info tool bar in header panel.
 * 
 * @author Andrey Zavgorodniy<br>
 * Copyright(c) 2008, Exadel Inc
 */
public class HeaderToolBarBuilder {

    /** Delimiter between each instances. */
    private static final String DELIMETR = ", ";

    /** Separator inserts after each new application title. */
    private static final String SEPARATOR = "\\";

    /** Divider for separate application string and data section. */
    private static final String DATA_MARKER_SEPARATOR = ":";

    /** Array of selected after SA dialog application entry transfer objects. */
    private ApplicationEntryTO[] selectedApplications;

    /**
     * Current application configuration transfer object - contain list of
     * selected applications for show in tool bar.
     */
    private ApplicationConfigTO currentConfigTo;

    /**
     * Accumulate selected information and show it in section of header panel.
     */
    private String selectedApplLabelText;

    /**
     * Live or archive date - read from <code>ApplicationConfigTO</code>
     */
    private boolean liveDateMode = true;

    /** Field headerTBBuilder - instance of this class  */
    private static HeaderToolBarBuilder headerTBBuilder;

    /**
     * Method getInstance - create or refresh instance of this class
     *
     * @param config of type ApplicationConfigTO
     * @return HeaderToolBarBuilder
     */
    public static HeaderToolBarBuilder getInstance(ApplicationConfigTO config){
        if (headerTBBuilder==null) headerTBBuilder = new HeaderToolBarBuilder(config);
        else {
            headerTBBuilder.currentConfigTo = config;
            if (config != null) {
                if (config.getApplicationList() != null) {
                    headerTBBuilder.selectedApplications = new ApplicationEntryTO[config.getApplicationList().length];
                    headerTBBuilder.selectedApplications = config.getApplicationList();
                }
                headerTBBuilder.liveDateMode = config.isLiveDate();
                headerTBBuilder.buildToolBarByArray();
            }
        }
        return headerTBBuilder;
    }

    /**
     * Default constructor.
     * 
     * @param config - the current session configuration TO from UserSessinBean
     */
    private HeaderToolBarBuilder(ApplicationConfigTO config) {
        this.currentConfigTo = config;
        if (config != null) {
            if (config.getApplicationList() != null) {
                this.selectedApplications = new ApplicationEntryTO[config.getApplicationList().length];
                this.selectedApplications = config.getApplicationList();
            }
            this.liveDateMode = config.isLiveDate();
            this.buildToolBarByArray();
        }
    }

    /**
     * Stick together in one string all applications and it's instances plus
     * live or archive date information
     * 
     * @return the selected applications as text
     */
    public String getSelectedApplLabelText() {
        if (this.selectedApplLabelText != null) {
            if (!this.selectedApplLabelText.equals("")) {
                String dataMarker = this.liveDateMode ? ApplicationMessages.MESSAGES
                                .liveDataLabelText() : " ";
                this.selectedApplLabelText += DATA_MARKER_SEPARATOR + " " + dataMarker;
            } else {
                this.selectedApplLabelText = ApplicationMessages.MESSAGES
                                .noApplicationSelectedLabel();
            }
        }
        if (!this.liveDateMode) {
            this.selectedApplLabelText += " [ " + this.currentConfigTo.getStartDateTime() + " - "
                            + this.currentConfigTo.getEndDateTime() + " ] ";
        }
        return this.selectedApplLabelText;
    }

    /**
     * Build string of applications and instances according rules for delimiters
     * uses array of selected after SA dialog application entry transfer
     * objects.
     */
    private void buildToolBarByArray() {
        int length = this.selectedApplications.length;
        StringBuffer sb = new StringBuffer();
        int i = 0;
        for (i = 0; i < length; i++) {
            ApplicationEntryTO item = this.selectedApplications[i];
            if (item.getInstanceList().length > 0) {
                for (int j = 0; j < item.getInstanceList().length; j++) {
                    sb.append(item.getApplicationTitle() + SEPARATOR);
                    InstanceEntryTO entryItem = item.getInstanceList()[j];
                    sb.append(entryItem.getName());
                    if (i + 1 < this.selectedApplications.length) {
                        sb.append(DELIMETR);
                    }
                }
            } else {
                if (i == length - 1) {
                    sb.append(item.getApplicationTitle());
                } else {
                    sb.append(item.getApplicationTitle() + DELIMETR);
                }
            }
        }
        String s = sb.toString();
        this.selectedApplLabelText = s;
    }
}
