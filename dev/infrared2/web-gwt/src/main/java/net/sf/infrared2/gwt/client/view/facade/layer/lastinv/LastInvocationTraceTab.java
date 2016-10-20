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
 * TraceTab.java Date created: 19.02.2008
 * Last modified by: $Author: soyon.lim $
 * $Revision: 14286 $ $Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.gwt.client.view.facade.layer.lastinv;

import net.sf.infrared2.gwt.client.WindowResizeManagerFactory;
import net.sf.infrared2.gwt.client.i18n.ApplicationMessages;
import net.sf.infrared2.gwt.client.to.other.OtherViewTO;
import net.sf.infrared2.gwt.client.tree.TreeFactory;
import net.sf.infrared2.gwt.client.utils.UIUtils;
import net.sf.infrared2.gwt.client.view.facade.layer.other.GeneralInformationTab;

import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.TabPanel;
import com.gwtext.client.widgets.event.PanelListenerAdapter;
import com.gwtext.client.widgets.layout.BorderLayout;
import com.gwtext.client.widgets.layout.BorderLayoutData;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.tree.TreePanel;

/**
 * <b>LastInvocationTraceTab</b><p>
 * Trace tab panel for last invocation module.
 * 
 * @author Gleb Zgonikov
 */
public class LastInvocationTraceTab extends Panel {
    /** A Tree widget for trace perform. */
    private TreePanel traceTree;
    /** Transfer object for Other view (view is different to Application and SQL).*/
    private OtherViewTO to;
    /** General information tab panel. */
    private GeneralInformationTab generalInformationTab;
    /** Tabbed panel for performing general information. */
    private TabPanel southTabPanel;
    /** Panel that contains trace tree. */
    private Panel traceTreePanel;

    /**
     * Default constructor.
     */
    public LastInvocationTraceTab(OtherViewTO to) {
        super(ApplicationMessages.MESSAGES.trace());
        // this.setMargins(5, 5, 0, 0);
        this.setBodyBorder(false);
        this.setBorder(false);
        this.setLayout(new BorderLayout());
        this.setAutoScroll(false);
        this.to = to;
        createCenter();
        createSouth();
        this.addListener(new PanelListenerAdapter(){
            /**
             * {@inheritDoc}
             */
            public void onActivate(Panel panel) {
                if(generalInformationTab.getGrid()!=null)
                    if(generalInformationTab.getGrid().getView()!=null)
                        generalInformationTab.getGrid().getView().refresh();
            }
        });
    }

    /**
     * Creates content for the center region.
     */
    void createCenter() {
        traceTreePanel = new Panel();
        traceTreePanel.setBorder(false);
        traceTreePanel.setAutoScroll(false);
        traceTreePanel.setLayout(new FitLayout());
        
        traceTree = TreeFactory.createTree(to.getTrace());
        traceTree.collapseAll();
        traceTree.setAutoScroll(true);
        
        traceTreePanel.add(traceTree);
        
        this.add(traceTreePanel, new BorderLayoutData(RegionPosition.CENTER));
    }

    /**
     * Creates content for the bottom region.
     */
    private void createSouth() {
        generalInformationTab = new GeneralInformationTab();

        southTabPanel = new TabPanel();
        southTabPanel.setHeight(230);
        southTabPanel.add(generalInformationTab);

        southTabPanel.activate(0);
        final BorderLayoutData borderLayoutData = new BorderLayoutData(RegionPosition.SOUTH);
        borderLayoutData.setSplit(true);
        this.add(southTabPanel, borderLayoutData);
    }

    /**
     * @return trace tree UI object.
     */
    public TreePanel getTraceTree() {
        return traceTree;
    }

    /**
     * @return general information tab panel.
     */
    public GeneralInformationTab getGeneralInformationTab() {
        return generalInformationTab;
    }

    /**
     * Update view according to new transfer object.
     * @param to - new transfer object.
     */
    public void update(OtherViewTO to){
        this.to = to;
        traceTreePanel.clear();
        traceTree = TreeFactory.createTree(to.getTrace());
        traceTree.collapseAll();
        traceTree.setAutoScroll(true);
        traceTree.setHeight(UIUtils.getCenterPanelHeight() / 2);
        traceTreePanel.add(traceTree);
        WindowResizeManagerFactory.getResizeManager(WindowResizeManagerFactory.PANELS_MANAGER).fireResize();
    }
}
