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
 * ApplicationLayerView.java Date created: 17.01.2008
 * Last modified by: $Author: soyon.lim $ 
 * $Revision: 14286 $ $Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.gwt.client.view.facade.layer.application;

import java.util.Arrays;

import net.sf.infrared2.gwt.client.WindowResizeManagerFactory;
import net.sf.infrared2.gwt.client.grid.ColumnDef;
import net.sf.infrared2.gwt.client.grid.GridGenerationConfig;
import net.sf.infrared2.gwt.client.grid.GridWindowResizeListenerImpl;
import net.sf.infrared2.gwt.client.grid.SimpleGridWrapper;
import net.sf.infrared2.gwt.client.grid.TableFactory;
import net.sf.infrared2.gwt.client.i18n.ApplicationMessages;
import net.sf.infrared2.gwt.client.to.application.ApplicationViewTO;
import net.sf.infrared2.gwt.client.utils.UIUtils;
import net.sf.infrared2.gwt.client.view.facade.ApplicationViewFacade;
import net.sf.infrared2.gwt.client.view.facade.SplitPanelsResizeListener;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.TabPanel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.layout.BorderLayout;
import com.gwtext.client.widgets.layout.BorderLayoutData;

/**
 * <b>ApplicationLayerView</b>
 * <p>
 * It represents view for data of application layer.
 * 
 * @author Sergey Evluhin
 */
class ApplicationLayerView extends AbstractApplicationLayer {

    private Image imgByTime;

    private FlexTable tblByTime;

    private Image imgByCount;

    private FlexTable tblByCount;

    private static AbstractApplicationLayer view;
    
    private Grid gridByTime;
    
    private Grid gridByCount;

    public static AbstractApplicationLayer getInstance(ApplicationViewTO to) {
        if (view == null) {
            view = new ApplicationLayerView(to);
        }
        return view;
    }

    /**
     * Create application view object.
     * 
     * @param to - transfer object for this view.
     */
    private ApplicationLayerView(ApplicationViewTO to) {
        super(to);
    }

    /**
     * Creates view based on transfer object data.
     */
    public void createView() {

        createContent();

        // WindowResizeManagerFactory.getResizeManager(WindowResizeManagerFactory.PANELS_MANAGER)
        // .complete();
        ApplicationViewFacade.addCenterPanel(center);
        rendered = true;

    }

    /**
     * Creates view.
     */
    private void createContent() {
        center = new Panel();
        center.setLayout(new BorderLayout());
        TabPanel top = getTopPanel();
        top.setBodyBorder(true);
        top.setBorder(true);

        Panel bottomPanel1 = new Panel();
        bottomPanel1.setTitle(ApplicationMessages.MESSAGES.layersByTotalTime());
        bottomPanel1.setSize("100%", "100%");
        bottomPanel1.setAutoScroll(true);
        imgByTime = new Image();
        tblByTime = UIUtils.getColoredLegendTable(to.getRows(), true);
        gridByTime = new Grid(1, 2);
        gridByTime.setWidget(0, 0, imgByTime);
        gridByTime.setWidget(0, 1, tblByTime);

        bottomPanel1.add(gridByTime);

        bottomPanel1.addListener(new ActivatePanelListenerImpl(grid, true, true));
        Panel bottomPanel2 = new Panel();
        bottomPanel2.setTitle(ApplicationMessages.MESSAGES.layersByCount());
        bottomPanel2.setSize("100%", "100%");
        bottomPanel2.setAutoScroll(true);

        imgByCount = new Image();
        tblByCount = UIUtils.getColoredLegendTable(to.getRows(), false);
        gridByCount = new Grid(1, 2);
        gridByCount.setWidget(0, 0, imgByCount);
        gridByCount.setWidget(0, 1, tblByCount);
        bottomPanel2.add(gridByCount);

        bottomPanel2.addListener(new ActivatePanelListenerImpl(grid, false));

        bottom = new TabPanel();
        bottom.setBodyBorder(true);
        bottom.setBorder(true);
        bottom.setHeight(UIUtils.getCenterPanelHeight() / 2);
        bottom.setActiveTab(0);
        bottom.add(bottomPanel1);
        bottom.add(bottomPanel2);

        final BorderLayoutData topLayoutData = new BorderLayoutData(RegionPosition.CENTER);
        topLayoutData.setSplit(false);

        final BorderLayoutData bottomLayoutData = new BorderLayoutData(RegionPosition.SOUTH);
        bottomLayoutData.setSplit(true);

        center.add(top, topLayoutData);

        WindowResizeManagerFactory.getResizeManager(WindowResizeManagerFactory.PANELS_MANAGER)
                        .addListener(new SplitPanelsResizeListener(bottom));
        center.add(bottom, bottomLayoutData);

        center.doLayout();
    }

    /**
     * {@inheritDoc}
     */
    public void updateView(ApplicationViewTO to) {
        this.to = to;
        bottom.activate(0);
        MemoryProxy proxy = new MemoryProxy(ApplicationViewTO.convertApplicationRowTO(to.getRows()));
        grid.getStore().setDataProxy(proxy);
        grid.getStore().reload();
//        imgByTime.setHTML((String) to.getImgByTimeUrl().get(
//                        (to.getImgByTimeUrl().keySet().iterator().next())));
        tblByTime.removeFromParent();
        tblByTime = UIUtils.getColoredLegendTable(to.getRows(), true);
        gridByTime.setWidget(0, 1, tblByTime);
        
//        imgByCount.setHTML((String) to.getImgByCountUrl().get(
//                        (to.getImgByCountUrl().keySet().iterator().next())));
        tblByCount.removeFromParent();
        tblByCount = UIUtils.getColoredLegendTable(to.getRows(), false);
        gridByCount.setWidget(0, 1, tblByCount);
        
    }

    /**
     * Creates top panel of the view.
     * 
     * @return top panel of the view.
     */
    private TabPanel getTopPanel() {
        TabPanel top = new TabPanel();
        Panel topPanel = new Panel();
        topPanel.setSize("100%", "100%");
        topPanel.setTitle(ApplicationMessages.MESSAGES.summary());
        topPanel.setAutoScroll(true);
        topPanel.setBorder(true);
        topPanel.setPaddings(5, 5, 0, 0);
        ColumnDef[] colDefs = new ColumnDef[4];
        colDefs[0] = new ColumnDef(ApplicationMessages.MESSAGES.color(), 40, false);
        colDefs[1] = new ColumnDef(ApplicationMessages.MESSAGES.layer(), 170);
        colDefs[2] = new ColumnDef(ApplicationMessages.MESSAGES.totalTime(), 0, ColumnDef.INTEGER);
        colDefs[3] = new ColumnDef(ApplicationMessages.MESSAGES.count(), 0, ColumnDef.INTEGER);

        final Object[][] data = ApplicationViewTO.convertApplicationRowTO(to.getRows());
        grid = (GridPanel) TableFactory.getGrid(data, new GridGenerationConfig(Arrays
                        .asList(colDefs)));

        grid.addGridCellListener(new ApplicationGridCellListenerImpl(1,
                        ApplicationMessages.MESSAGES.layer()));
        // renderer for colored column
        grid.getColumnModel().setRenderer(0, new ColorRenderer());
        grid.getColumnModel().setRenderer(1, clickableRenderer);
        grid.setWidth(grid.getColumnModel().getTotalWidth() + 3);
        grid.setAutoScroll(false);
        // this grid height usually less than panel height, no reserved space
        // for scroll needed
        grid.setHeight(UIUtils.calculateGridHeight(grid) - (UIUtils.SCROLL_WIDTH - 1));
        
        gridContainer = new Panel();
        gridContainer.setAutoScroll(false);
        gridContainer.setBorder(false);
        gridContainer.setBodyBorder(false);
        gridContainer.setWidth(UIUtils.getCenterPanelWidth() - UIUtils.SCROLL_WIDTH);
        gridContainer.setHeight(UIUtils.calculateGridHeight(grid));
        gridContainer.add(grid);
        
        SimpleGridWrapper gridWrapper = new SimpleGridWrapper(grid, colDefs,gridContainer);
        gridWrapper.adjustGridSize(false);
        WindowResizeManagerFactory.getResizeManager(WindowResizeManagerFactory.GRIDS_MANAGER)
                        .addListener(new GridWindowResizeListenerImpl(gridWrapper));
        
        gridContainer.add(grid);
        topPanel.add(gridContainer);
        topPanel.setHeight(UIUtils.getCenterPanelHeight() / 2);

        top.setBorder(false);
        top.add(topPanel);
        return top;
    }


	public void updateImageURLs() {
		imgByTime.setUrl((String) to.getImgByTimeUrl().keySet().iterator().next());
		imgByCount.setUrl((String) to.getImgByCountUrl().keySet().iterator().next());
		
	}

}
