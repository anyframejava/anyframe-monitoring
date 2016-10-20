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
 * SummaryTab.java Date created: 19.02.2008
 * Last modified by: $Author: soyon.lim $ 
 * $Revision: 14286 $ $Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.gwt.client.view.facade.layer.other;

import com.google.gwt.user.client.ui.*;
import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.TabPanel;
import com.gwtext.client.widgets.event.PanelListenerAdapter;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtext.client.widgets.layout.BorderLayout;
import com.gwtext.client.widgets.layout.BorderLayoutData;
import com.gwtext.client.widgets.layout.VerticalLayout;

import java.util.Arrays;

import net.sf.infrared2.gwt.client.WindowResizeManagerFactory;
import net.sf.infrared2.gwt.client.grid.*;
import net.sf.infrared2.gwt.client.i18n.ApplicationMessages;
import net.sf.infrared2.gwt.client.to.other.OtherViewTO;
import net.sf.infrared2.gwt.client.utils.UIUtils;
import net.sf.infrared2.gwt.client.view.facade.SplitPanelsResizeListener;

/**
 * <b>SummaryTab</b> <gridContainer> Summary tab for other layer.
 * 
 * @author Sergey Evluhin
 */
public class SummaryTab extends Panel {
    /**
     * Represents implementation of grid's column renderer which is highlighting
     * value of column by the red color in the case if this value more then
     * LIMIT value.
     */
    private static LimitRenderer limitRenderer = new LimitRenderer();
    /** Main grid for data presentation. */
    private GridPanel grid;
    /**
     * Transfer object for Other view (view is different to Application and
     * SQL).
     */
    private OtherViewTO to;
    /** Place holder for image marker by count. */
    private HTML imageByCount;
    /** Place holder for image marker by time. */
    private HTML imageByTime;
    /** Inclusive/exclusive mode representation. */
    private boolean inclusiveMode = true;
    /** Is by time graph */
    private boolean isByTimeGraph = true;
    /**
     * Creates table for graphic by time, that consists from two columns, 1-
     * colored rectangle,2-description.
     */
    private FlexTable byTimeColoredTable;
    /**
     * Creates table for graphic by count, that consists from two columns, 1-
     * colored rectangle,2-description.
     */
    private FlexTable byCountColoredTable;
    /**
     * Represents implementation of grid's column renderer which is applies
     * 'clickable' style to the column's text.
     */
    private ClickableRenderer clickableRenderer = new ClickableRenderer();

    /** Switcher between inclusive and exclusive modes. */
    private HTML inclusiveModeLink;

    /** Tab panel for the SOUTH region. */
    private TabPanel bottom;

    private Grid gridByTime;

    private Grid gridByCount;

    private Panel gridContainer;

    /**
     * Default constructor.
     * 
     * @param to - transfer object for other layer.
     */
    public SummaryTab(OtherViewTO to) {
        super(ApplicationMessages.MESSAGES.summary());
        this.setSize("100%", "100%");
        this.setBodyBorder(false);
        this.setBorder(false);
        this.setLayout(new BorderLayout());
        this.to = to;
        createCenter();
        createSouth();
        this.doLayout();
        this.addListener(new PanelListenerAdapter() {
            /**
             * {@inheritDoc}
             */
            public void onActivate(Panel panel) {
                if (grid != null)
                    if (grid.getView() != null)
                        grid.getView().refresh();
            }
        });
    }

    /**
     * Creates content for the center region.
     */
    private void createCenter() {
        final Object[][] data = OtherViewTO.convertSummaryRows(to.getInclusiveTableRows());
        ColumnDef[] colDefs = new ColumnDef[] {
                        new ColumnDef(ApplicationMessages.MESSAGES.color(), 40, false),
                        new ColumnDef(ApplicationMessages.MESSAGES.operationName(), 200,
                                        ColumnDef.STRING),
                        new ColumnDef(ApplicationMessages.MESSAGES.totalTime(), 0, ColumnDef.FLOAT),
                        new ColumnDef(ApplicationMessages.MESSAGES.count(), 0, ColumnDef.INTEGER),
                        new ColumnDef(ApplicationMessages.MESSAGES.averageTime(), 0,
                                        ColumnDef.FLOAT),
                        new ColumnDef(ApplicationMessages.MESSAGES.adjustedAverageTime(), 0,
                                        ColumnDef.FLOAT),
                        new ColumnDef(ApplicationMessages.MESSAGES.minimumExecutionTime(), 0,
                                        ColumnDef.INTEGER),
                        new ColumnDef(ApplicationMessages.MESSAGES.maximumExecutionTime(), 0,
                                        ColumnDef.INTEGER),
                        new ColumnDef(ApplicationMessages.MESSAGES.firstExecutionTime(), 0,
                                        ColumnDef.INTEGER),
                        new ColumnDef(ApplicationMessages.MESSAGES.lastExecutionTime(), 0,
                                        ColumnDef.INTEGER) };

        GridGenerationConfig conf = new GridGenerationConfig();
        conf.setColumnDefs(Arrays.asList(colDefs));
        grid = (GridPanel) TableFactory.getGrid(data, conf);
        grid.setAutoScroll(true);
        grid.setWidth(grid.getColumnModel().getTotalWidth() + 3);
        grid.setHeight(UIUtils.calculateGridHeight(grid));
        grid.getColumnModel().setRenderer(0, new Renderer() {

            public String render(Object value, CellMetadata cellMetadata, Record record,
                            int rowIndex, int colNum, Store store) {
                String v = value.toString();

                int end = v.indexOf('|');
                String byTimeColor = v.substring(0, end);
                String byCountColor = v.substring(end + 1);
                String res;
                if (isByTimeGraph) {
                    res = "<div class=\"colored-panel\" style=\"background-color: " + byTimeColor
                                    + ";\">&nbsp;</div>";
                } else {
                    res = "<div class=\"colored-panel\" style=\"background-color: " + byCountColor
                                    + ";\">&nbsp;</div>";
                }

                return res;
            }

        });
        grid.getColumnModel().setRenderer(1, clickableRenderer);
        grid.getColumnModel().setRenderer(4, limitRenderer);

        Panel panel = new Panel();
        panel.setAutoScroll(true);
        panel.setHeight(UIUtils.getCenterPanelHeight() / 2);
        panel.setLayout(new VerticalLayout());
        inclusiveModeLink = new HTML();
        inclusiveModeLink.setText(ApplicationMessages.MESSAGES.exclusiveModeLabel());
        inclusiveModeLink.setStylePrimaryName("button-inclusive-mode");
        inclusiveModeLink.addClickListener(new ClickListener() {
            public void onClick(Widget sender) {
                byCountColoredTable.removeFromParent();
                byTimeColoredTable.removeFromParent();

                if (inclusiveMode) {
                    grid.getStore().setDataProxy(
                                    new MemoryProxy(OtherViewTO.convertSummaryRows(to
                                                    .getExclusiveTableRows())));
                    inclusiveModeLink.setText(ApplicationMessages.MESSAGES.inclusiveModeLabel());
                    inclusiveMode = false;
                    imageByCount.setHTML((String) to.getImgExclusiveOperationByCountUrl().get(
                                    to.getImgExclusiveOperationByCountUrl().keySet().iterator()
                                                    .next()));
                    imageByTime.setHTML((String) to.getImgExclusiveOperationByTimeUrl().get(
                                    to.getImgExclusiveOperationByTimeUrl().keySet().iterator()
                                                    .next()));

                    byTimeColoredTable = UIUtils.getColoredLegendTable(to.getExclusiveTableRows(),
                                    true);
                    byCountColoredTable = UIUtils.getColoredLegendTable(to.getExclusiveTableRows(),
                                    false);

                } else {
                    grid.getStore().setDataProxy(
                                    new MemoryProxy(OtherViewTO.convertSummaryRows(to
                                                    .getInclusiveTableRows())));
                    inclusiveModeLink.setText(ApplicationMessages.MESSAGES.exclusiveModeLabel());
                    inclusiveMode = true;
                    imageByCount.setHTML((String) to.getImgInclusiveOperationByCountUrl().get(
                                    to.getImgInclusiveOperationByCountUrl().keySet().iterator()
                                                    .next()));
                    imageByTime.setHTML((String) to.getImgInclusiveOperationByTimeUrl().get(
                                    to.getImgInclusiveOperationByTimeUrl().keySet().iterator()
                                                    .next()));

                    byTimeColoredTable = UIUtils.getColoredLegendTable(to.getInclusiveTableRows(),
                                    true);
                    byCountColoredTable = UIUtils.getColoredLegendTable(to.getInclusiveTableRows(),
                                    false);

                }

                gridByTime.setWidget(0, 1, byTimeColoredTable);
                gridByCount.setWidget(0, 1, byCountColoredTable);

                grid.getStore().reload();
                grid.getView().refresh();
                doLayout();
            }
        });

        gridContainer = new Panel();
        gridContainer.setAutoScroll(false);
        gridContainer.setBorder(false);
        gridContainer.setBodyBorder(false);
        gridContainer.setWidth(UIUtils.getCenterPanelWidth() - UIUtils.SCROLL_WIDTH);
        gridContainer.setHeight(UIUtils.calculateGridHeight(grid));
        gridContainer.add(grid);

        SimpleGridWrapper gridWrapper = new SimpleGridWrapper(grid, colDefs, gridContainer);
        gridWrapper.adjustGridSize(false);
        WindowResizeManagerFactory.getResizeManager(WindowResizeManagerFactory.GRIDS_MANAGER)
                        .addListener(new GridWindowResizeListenerImpl(gridWrapper));
        panel.add(inclusiveModeLink);
        panel.add(gridContainer);
        this.add(panel, new BorderLayoutData(RegionPosition.CENTER));
    }

    /**
     * Create content for the bottom region.
     */
    private void createSouth() {
        bottom = new TabPanel();
        bottom.setHeight(UIUtils.getCenterPanelHeight() / 2);
        Panel opByTimePanel = new Panel(ApplicationMessages.MESSAGES.operationsByTotalTime());
        opByTimePanel.setAutoScroll(true);
        imageByTime = new HTML((String) to.getImgInclusiveOperationByTimeUrl().get(
                        to.getImgInclusiveOperationByTimeUrl().keySet().iterator().next()));
        byTimeColoredTable = UIUtils.getColoredLegendTable(to.getInclusiveTableRows(), true);
        gridByTime = new Grid(1, 2);

        gridByTime.setWidget(0, 0, imageByTime);
        gridByTime.setWidget(0, 1, byTimeColoredTable);
        opByTimePanel.add(gridByTime);

        Panel opByCountPanel = new Panel(ApplicationMessages.MESSAGES.operationsByCount());
        opByCountPanel.setAutoScroll(true);
        imageByCount = new HTML((String) to.getImgInclusiveOperationByCountUrl().get(
                        to.getImgInclusiveOperationByCountUrl().keySet().iterator().next()));
        byCountColoredTable = UIUtils.getColoredLegendTable(to.getInclusiveTableRows(), false);

        gridByCount = new Grid(1, 2);
        gridByCount.setWidget(0, 0, imageByCount);
        gridByCount.setWidget(0, 1, byCountColoredTable);

        opByCountPanel.add(gridByCount);

        bottom.add(opByTimePanel);
        bottom.add(opByCountPanel);
        bottom.activate(0);
        final BorderLayoutData borderLayoutData = new BorderLayoutData(RegionPosition.SOUTH);
        borderLayoutData.setSplit(true);
        borderLayoutData.setMinSize(100);

        opByTimePanel.addListener(new PanelListenerAdapter() {

            public void onActivate(Panel panel) {
                super.onActivate(panel);
                if (!isByTimeGraph) {
                    isByTimeGraph = true;
                    grid.getView().refresh();
                }
            }
        });

        opByCountPanel.addListener(new PanelListenerAdapter() {
            public void onActivate(Panel panel) {
                super.onActivate(panel);
                isByTimeGraph = false;
                grid.getView().refresh();
            }
        });

        WindowResizeManagerFactory.getResizeManager(WindowResizeManagerFactory.PANELS_MANAGER)
                        .addListener(new SplitPanelsResizeListener(bottom));
        this.add(bottom, borderLayoutData);
    }

    /**
     * @return summary table.
     */
    public GridPanel getSummaryGrid() {
        return grid;
    }

    /**
     * Update this tab accordingly to the new received transfer object.
     * 
     * @param to - new transfer object.
     */
    public void update(OtherViewTO to) {
        this.to = to;
        bottom.activate(0);
        grid.getStore()
                        .setDataProxy(
                                        new MemoryProxy(OtherViewTO.convertSummaryRows(to
                                                        .getInclusiveTableRows())));
        grid.getStore().reload();

        isByTimeGraph = true;
        inclusiveMode = true;
        inclusiveModeLink.setText(ApplicationMessages.MESSAGES.exclusiveModeLabel());
        imageByTime.setHTML((String) to.getImgInclusiveOperationByTimeUrl().get(
                        to.getImgInclusiveOperationByTimeUrl().keySet().iterator().next()));

        byTimeColoredTable.removeFromParent();
        byTimeColoredTable = UIUtils.getColoredLegendTable(to.getInclusiveTableRows(), true);
        gridByTime.setWidget(0, 1, byTimeColoredTable);

        imageByCount.setHTML((String) to.getImgInclusiveOperationByCountUrl().get(
                        to.getImgInclusiveOperationByCountUrl().keySet().iterator().next()));

        byCountColoredTable.removeFromParent();
        byCountColoredTable = UIUtils.getColoredLegendTable(to.getInclusiveTableRows(), false);
        gridByCount.setWidget(0, 1, byCountColoredTable);

        gridContainer.getBody().setHeight(UIUtils.calculateGridHeight(grid), false);
        UIUtils.updateGridHeight(grid);

        this.doLayout();

    }
}
