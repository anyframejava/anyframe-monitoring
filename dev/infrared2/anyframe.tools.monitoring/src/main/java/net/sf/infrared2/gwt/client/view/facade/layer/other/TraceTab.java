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

package net.sf.infrared2.gwt.client.view.facade.layer.other;

import net.sf.infrared2.gwt.client.WindowResizeManagerFactory;
import net.sf.infrared2.gwt.client.i18n.ApplicationMessages;
import net.sf.infrared2.gwt.client.to.other.OtherViewTO;
import net.sf.infrared2.gwt.client.tree.TreeFactory;
import net.sf.infrared2.gwt.client.utils.UIUtils;
import net.sf.infrared2.gwt.client.view.facade.SplitPanelsResizeListener;

import com.google.gwt.user.client.ui.ScrollPanel;
import com.gwtext.client.core.Direction;
import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.TabPanel;
import com.gwtext.client.widgets.event.PanelListenerAdapter;
import com.gwtext.client.widgets.layout.BorderLayout;
import com.gwtext.client.widgets.layout.BorderLayoutData;
import com.gwtext.client.widgets.tree.TreePanel;

/**
 * <b>TraceTab</b>
 * <p>
 * Trace tab for other layer.
 * 
 * @author Sergey Evluhin
 */
public class TraceTab extends Panel {
    /** Tree for showing trace. */
    private TreePanel traceTree;
    /**
     * Transfer object for Other view (view is different to Application and
     * SQL).
     */
    private OtherViewTO to;
    /** General information tab panel. */
    private GeneralInformationTab generalInformationTab;
    /** JDBC tab panel. */
    private JdbcTab jdbcTab;
    /** SQL tab panel. */
    private SqlTab sqlTab;
    /** Tab panel on the south of layout. */
    private TabPanel southTabPanel;

    /** Panel that contains trace tree. */
    private ScrollPanel traceTreePanel;

    /**
     * Default constructor.
     */
    public TraceTab(OtherViewTO to) {
        super(ApplicationMessages.MESSAGES.trace());
        // this.setMargins(5, 5, 0, 0);
        this.setBodyBorder(false);
        this.setBorder(false);
        this.setLayout(new BorderLayout());
        this.setAutoScroll(false);
        this.to = to;
        createCenter();
        createSouth();
        this.addListener(new PanelListenerAdapter() {
            /**
             * {@inheritDoc}
             */
            public void onActivate(Panel panel) {
                traceTreePanel.setHorizontalScrollPosition(0);
                if(sqlTab.getComplexGridByCount()!=null)
                    if(sqlTab.getComplexGridByCount().getContentGrid().getView()!=null)
                        sqlTab.getComplexGridByCount().getContentGrid().getView().refresh();
                
                if(sqlTab.getComplexGridByTime()!=null)
                    if(sqlTab.getComplexGridByTime().getContentGrid().getView()!=null)
                        sqlTab.getComplexGridByTime().getContentGrid().getView().refresh();
                
                if(generalInformationTab.getGrid()!=null)
                    if(generalInformationTab.getGrid().getView()!=null)
                        generalInformationTab.getGrid().getView().refresh();
                    
                if(jdbcTab.getGrid()!=null)
                    if(jdbcTab.getGrid().getView()!=null)
                        jdbcTab.getGrid().getView().refresh();
            }
        });
    }

    /**
     * Creates content for the center region.
     */
    void createCenter() {
        traceTreePanel = new ScrollPanel();
        traceTreePanel.setStyleName("white-background");
//        traceTreePanel.setLayout(new FitLayout());

        traceTree = TreeFactory.createTree(to.getTrace());
        traceTree.collapseAll();
        //traceTree.setHeight(UIUtils.getCenterPanelHeight() / 2);


        traceTreePanel.add(traceTree);

        this.add(traceTreePanel, new BorderLayoutData(RegionPosition.CENTER));
    }

    /**
     * Creates content for the bottom region.
     */
    private void createSouth() {
        generalInformationTab = new GeneralInformationTab();
        jdbcTab = new JdbcTab();
        sqlTab = new SqlTab();
        southTabPanel = new TabPanel();
        southTabPanel.setHeight(UIUtils.getCenterPanelHeight() / 2);
        southTabPanel.add(generalInformationTab);
        southTabPanel.add(jdbcTab);
        southTabPanel.add(sqlTab);
        southTabPanel.activate(0);
        final BorderLayoutData borderLayoutData = new BorderLayoutData(RegionPosition.SOUTH);
        borderLayoutData.setSplit(true);
        WindowResizeManagerFactory.getResizeManager(WindowResizeManagerFactory.PANELS_MANAGER)
                        .addListener(new SplitPanelsResizeListener(southTabPanel));
        this.add(southTabPanel, borderLayoutData);
    }

    /**
     * @return trace tree UI object.
     */
    public TreePanel getTraceTree() {
        return traceTree;
    }

    /**
     * Gets the trace tree panel.
     * 
     * @return the trace tree panel
     */
    public ScrollPanel getTraceTreePanel() {
        return traceTreePanel;
    }

    /**
     * @return general information tab panel.
     */
    public GeneralInformationTab getGeneralInformationTab() {
        return generalInformationTab;
    }

    /**
     * @return JDBC tab panel.
     */
    public JdbcTab getJdbcTab() {
        return jdbcTab;
    }

    /**
     * @return SQL tab panel.
     */
    public SqlTab getSqlTab() {
        return sqlTab;
    }

    /**
     * Updates this tab according to the new transfer object.
     * 
     * @param to - new transfer object.
     */
    public void update(OtherViewTO to) {
        this.to = to;
        traceTreePanel.clear();
        traceTreePanel.setHorizontalScrollPosition(0);
        traceTree = TreeFactory.createTree(to.getTrace());
        traceTree.collapseAll();
        traceTreePanel.add(traceTree);
        southTabPanel.activate(0);
    }
    
    /**
     * Correct scroll bar.
     */
}
