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
 * DefaultViewImpl.java Date created: 05.02.2008
 * Last modified by: $Author: soyon.lim $ 
 * $Revision: 14286 $ $Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.gwt.client.view.facade.layer;

import net.sf.infrared2.gwt.client.i18n.ApplicationMessages;
import net.sf.infrared2.gwt.client.view.facade.ApplicationViewFacade;

import com.google.gwt.user.client.ui.Label;
import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.TabPanel;
import com.gwtext.client.widgets.layout.BorderLayout;
import com.gwtext.client.widgets.layout.BorderLayoutData;

/**
 * <b>DefaultViewImpl</b><p>
 * View for initial states of application if no applications were selected or no
 * information are available.
 * 
 * @author Sergey Evluhin
 */  
public class DefaultViewImpl {

    /** Field instance  */
    private static DefaultViewImpl instance = new DefaultViewImpl();

    /** Main center panel layout */
    private Panel center;

    /**
     * Constructor DefaultViewImpl creates a new DefaultViewImpl instance.
     */
    private DefaultViewImpl(){

    }

    /**
     * Method createView - if default view does not exist,
     * create and add default veiw to the center,
     * else update center to show default view 
     */
    public static void createView(){
        if (instance.center == null) instance.createPanel();
        else ApplicationViewFacade.addCenterPanel(instance.center);
    }

    /**
     * Creates empty layout of application.
     */
    private void createPanel() {
        center = new Panel();
        center.setLayout(new BorderLayout());
        TabPanel top = getTopPanel();

        Panel bottomPanel1 = new Panel();
        bottomPanel1.setTitle(ApplicationMessages.MESSAGES.layersByTotalTime());
        bottomPanel1.setSize("100%", "100%");

        Panel bottomPanel2 = new Panel();
        bottomPanel2.setTitle(ApplicationMessages.MESSAGES.layersByCount());
        bottomPanel2.setSize("100%", "100%");

        TabPanel bottom = new TabPanel();
        bottom.setBorder(false);
        bottom.setActiveTab(0);
        bottom.add(bottomPanel1);
        bottom.add(bottomPanel2);

        final BorderLayoutData topLayoutData = new BorderLayoutData(RegionPosition.CENTER);
        topLayoutData.setSplit(false);

        final BorderLayoutData bottomLayoutData = new BorderLayoutData(RegionPosition.SOUTH);

        bottomLayoutData.setSplit(true);

        center.add(top, topLayoutData);

        center.add(bottom, bottomLayoutData);

        center.doLayout();

        ApplicationViewFacade.addCenterPanel(center);

    }

    /**
     * Creates top panel of this view.
     */
    private TabPanel getTopPanel() {
        Panel topPanel = new Panel();
        topPanel.setSize("100%", "100%");
        topPanel.setTitle(ApplicationMessages.MESSAGES.summary());

        Label noInfoLabel = new Label(ApplicationMessages.MESSAGES.noInformationAvailable());
        noInfoLabel.setStyleName("no-info-label");
        topPanel.add(noInfoLabel);

        TabPanel top = new TabPanel();
        top.setBorder(false);
        top.add(topPanel);
        return top;
    }
}
