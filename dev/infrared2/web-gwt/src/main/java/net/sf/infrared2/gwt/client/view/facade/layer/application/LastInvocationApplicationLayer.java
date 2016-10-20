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
 * LastInvocationApplicationLayer.java Date created: 18.04.2008
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
import net.sf.infrared2.gwt.client.view.facade.SplitPanelsResizeListener;

import com.google.gwt.user.client.ui.Image;
import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.TabPanel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.layout.BorderLayout;
import com.gwtext.client.widgets.layout.BorderLayoutData;

/**
 * View for the application layer of last invocation module.
 * 
 * @author Sergey Evluhin
 */
class LastInvocationApplicationLayer extends AbstractApplicationLayer {

    private Image byCountImg, byTimeImg;

    private static AbstractApplicationLayer view;

    public static AbstractApplicationLayer getInstance(ApplicationViewTO to) {
        if (view == null || (to != null && !to.equals(view.to))) {
            view = new LastInvocationApplicationLayer(to);
        }
        return view;
    }

    /**
     * Creates view object.
     * 
     * @param to - transfer object for this view.
     */
    private LastInvocationApplicationLayer(ApplicationViewTO to) {
        super(to);
    }

    /**
     * Creates view based on transfer object data.
     */
    public void createView() {

        createLastInvModuleView();
        // WindowResizeManagerFactory.getResizeManager(WindowResizeManagerFactory.PANELS_MANAGER)
        // .complete();

    }

    /**
     * Creates view for last invocation module.
     */
    private void createLastInvModuleView() {
        center = new Panel();
        center.setLayout(new BorderLayout());
        TabPanel top = getTopPanel();
        Panel bottomPanel1 = new Panel();

        bottomPanel1.setSize("100%", "100%");
        bottomPanel1.setAutoScroll(true);

        byTimeImg = new Image();
        bottomPanel1.add(byTimeImg);

        Panel bottomPanel2 = new Panel();

        bottomPanel2.setSize("100%", "100%");
        bottomPanel2.setAutoScroll(true);
        byCountImg = new Image();
        bottomPanel2.add(byCountImg);

        bottomPanel1.setTitle(ApplicationMessages.MESSAGES.sequencesTotalTime());
        bottomPanel2.setTitle(ApplicationMessages.MESSAGES.sequencesCount());

        bottom = new TabPanel();
        bottom.setBorder(false);
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
        MemoryProxy proxy = new MemoryProxy(ApplicationViewTO
                        .convertApplicationRowTOWithoutColors(to.getRows()));
        grid.getStore().setDataProxy(proxy);
        grid.getStore().reload();
//       byTimeImg.setUrl((String) to.getImgByTimeUrl().keySet().iterator().next());
//        byCountImg.setUrl((String) to.getImgByCountUrl().keySet().iterator().next());
    }

    /**
     * Creates top panel of the view for last invocation module.
     * 
     * @return top panel of the view.
     */
    private TabPanel getTopPanel() {
        TabPanel top = new TabPanel();
        Panel topPanel = new Panel();
        topPanel.setAutoScroll(true);
        topPanel.setSize("100%", "100%");
        topPanel.setTitle(ApplicationMessages.MESSAGES.summary());
        topPanel.setPaddings(5, 5, 0, 0);
        ColumnDef[] colDefs = new ColumnDef[3];
        colDefs[0] = new ColumnDef(ApplicationMessages.MESSAGES.invocationSequence(), 120);
        colDefs[1] = new ColumnDef(ApplicationMessages.MESSAGES.totalTime(), 0, ColumnDef.INTEGER);
        colDefs[2] = new ColumnDef(ApplicationMessages.MESSAGES.count(), 0, ColumnDef.INTEGER);

        final Object[][] data = ApplicationViewTO
                        .convertApplicationRowTOWithoutColors(to.getRows());
        grid = (GridPanel) TableFactory.getGrid(data, new GridGenerationConfig(Arrays
                        .asList(colDefs)));
        grid.addGridCellListener(new ApplicationGridCellListenerImpl(0,
                        ApplicationMessages.MESSAGES.invocationSequence()));
        grid.getColumnModel().setRenderer(0, clickableRenderer);
        grid.setWidth(grid.getColumnModel().getTotalWidth() + 3);
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
		byTimeImg.setUrl((String) to.getImgByTimeUrl().keySet().iterator().next());
        byCountImg.setUrl((String) to.getImgByCountUrl().keySet().iterator().next());
	}

}
