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
package net.sf.infrared2.gwt.client.view.facade.layer;

import net.sf.infrared2.gwt.client.WindowResizeManagerFactory;
import net.sf.infrared2.gwt.client.grid.GridWindowResizeListenerImpl;
import net.sf.infrared2.gwt.client.i18n.ApplicationMessages;
import net.sf.infrared2.gwt.client.to.sql.SqlViewTO;
import net.sf.infrared2.gwt.client.utils.UIUtils;
import net.sf.infrared2.gwt.client.view.facade.ApplicationViewFacade;
import net.sf.infrared2.gwt.client.view.facade.layer.sql.SqlDetailsTab;
import net.sf.infrared2.gwt.client.view.facade.layer.sql.SqlSummaryTab;

import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.ExtElement;
import com.gwtext.client.data.Record;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.TabPanel;
import com.gwtext.client.widgets.event.PanelListenerAdapter;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.event.GridCellListenerAdapter;
import com.gwtext.client.widgets.layout.FitLayout;
 
/**
 * <b>SQLLayerViewImpl</b><p>
 * Presentation for SQL data.
 *
 * @author Sergey Evluhin
 */
public class SQLLayerViewImpl {

    private static SQLLayerViewImpl sqlView;

    private Panel center;

    /**
     * A lightweight tab container.
     */
    private TabPanel tabPanel; 
    /**
     * Transfer object for SQL view.
     */
    private SqlViewTO to;
    /**
     * Summary tab for SQL layer view.
     */
    private SqlSummaryTab sqlSummaryTab;
    /**
     * Details tab of SQL layer view.
     */
    private SqlDetailsTab sqlDetailsTab;
    /**
     * Listener for details table (in details tab).
     */
    private DetailsGridCellListener detailsGridCellListener = new DetailsGridCellListener();
    /**
     * Listener class for summaryGrid.
     */
    private SummaryGridCellListener summaryGridCellListener = new SummaryGridCellListener();

    /**
     * Constructor SQLLayerViewImpl creates a new SQLLayerViewImpl instance.
     */
    private SQLLayerViewImpl() {
    }

    /**
     * Method getInstance returns the instance of this SQLLayerViewImpl object.
     *
     * @return the instance (type SQLLayerViewImpl) of this SQLLayerViewImpl object.
     */
    public static SQLLayerViewImpl getInstance() {
        if (sqlView == null) sqlView = new SQLLayerViewImpl();
        return sqlView;
    }

    /**
     * Creates view based on data received from transfer object.
     *
     * @param to - transfer object for this view.
     */
    public void createView(SqlViewTO to) {
        this.to = to;

        if (center == null) {
            center = new Panel();

            center.setBorder(false);
            center.setBodyBorder(false);
            center.setLayout(new FitLayout());
            tabPanel = new TabPanel();
            sqlSummaryTab = new SqlSummaryTab(to);
            sqlDetailsTab = new SqlDetailsTab(to);

            tabPanel.add(sqlSummaryTab);
            tabPanel.add(sqlDetailsTab);
            tabPanel.activate(0);

            sqlDetailsTab.getDetailsGrid().addGridCellListener(detailsGridCellListener);
            sqlSummaryTab.getSummaryGrid().getContentGrid()
                    .addGridCellListener(summaryGridCellListener);

            center.add(tabPanel);


            WindowResizeManagerFactory.getResizeManager(WindowResizeManagerFactory.GRIDS_MANAGER)
                    .addListener(
                            new GridWindowResizeListenerImpl(sqlSummaryTab
                                    .getSummaryGrid()));

//            WindowResizeManagerFactory.getResizeManager(WindowResizeManagerFactory.GRIDS_MANAGER)
//                    .complete();
//
//            WindowResizeManagerFactory.getResizeManager(WindowResizeManagerFactory.PANELS_MANAGER)
//                    .complete();
            defaultState();
        } else {
            updateView();
        }

        ApplicationViewFacade.addCenterPanel(center);
        sqlSummaryTab.getSummaryGrid().getContentGrid().getView().refresh();
    }

    /**
     * Method updateView - update SQL view according to transfer object. 
     */
    private void updateView() {
        tabPanel.activate(0);
        sqlSummaryTab.updateTab(to);
        sqlDetailsTab.updateTab(to);
        if (sqlSummaryTab.getSummaryGrid().getContentGrid().getSelectionModel()
                .getSelected() == null
                && sqlDetailsTab.getDetailsGrid().getSelectionModel().getSelected() == null) {
            if (tabPanel.getComponent(sqlDetailsTab.getId()).isVisible()){
                detailsGridCellListener.onCellClick(sqlDetailsTab.getDetailsGrid(), 0, 1, null);
                sqlDetailsTab.getDetailsGrid().getSelectionModel().selectFirstRow();
            }
        }
//        UIUtils.updateGridHeight(sqlSummaryTab.getSummaryGrid().getContentGrid());
//        sqlSummaryTab.getSummaryGrid().normalizeHeight();
//        sqlSummaryTab.getSummaryGrid().getContentGrid().getView().refresh();
//        sqlSummaryTab.doLayout();
//        sqlSummaryTab.getSummaryGrid().getBody().setStyle("height", sqlSummaryTab.getSummaryGrid().calculateHeight()+"px");
//        sqlSummaryTab.doLayout();
        UIUtils.updateGridHeight(sqlSummaryTab.getSummaryGrid());
        WindowResizeManagerFactory.getResizeManager(WindowResizeManagerFactory.PANELS_MANAGER).fireResize();
    }

    /**
     * Default view for this layer.
     */
    private void defaultState() {
        sqlDetailsTab.addListener(new PanelListenerAdapter() {
            /**
             * {@inheritDoc}
             */
            public void onActivate(Panel panel) {

                super.onActivate(panel);
                if (sqlSummaryTab.getSummaryGrid().getContentGrid().getSelectionModel()
                        .getSelected() == null
                        && sqlDetailsTab.getDetailsGrid().getSelectionModel().getSelected() == null) {
                	detailsGridCellListener.currentRowIndex = -1;
                    detailsGridCellListener.onCellClick(sqlDetailsTab.getDetailsGrid(), 0, 1, null);
                    sqlDetailsTab.getDetailsGrid().getSelectionModel().selectFirstRow();

                }
            }
        });

    }

    /**
     * Listener class for summaryGrid
     *
     * @author Sergey Evluhin
     */
    private class SummaryGridCellListener extends GridCellListenerAdapter {
        public void onCellClick(GridPanel grid, int rowIndex, int colindex, EventObject e) {
            super.onCellClick(grid, rowIndex, colindex, e);
            if (colindex == 1) {

                final String id = grid.getStore().getAt(rowIndex).getAsString("-0");

                final GridPanel detailsGrid = sqlDetailsTab.getDetailsGrid();
                final Record[] records = detailsGrid.getStore().getRecords();
                for (int i = 0; i < records.length; i++) {
                    Record r = records[i];
                    if (r.getAsString(ApplicationMessages.MESSAGES.ID().toLowerCase()+0).equals(id)) {

                        tabPanel.activate(1);
                        detailsGrid.getSelectionModel().selectRow(i);

                        detailsGridCellListener.onCellClick(grid, i, colindex, e);
                        break;
                    }
                }
            }
        }

    }

    /**
     * Listener for details table (in details tab).
     *
     * @author Sergey Evluhin
     */
    private class DetailsGridCellListener extends GridCellListenerAdapter {
        private int currentRowIndex = -1;

        public void onCellClick(GridPanel grid, int rowIndex, int colIndex, EventObject e) {
            if (colIndex == 1) {
                final GridPanel detailsGrid = sqlDetailsTab.getDetailsGrid();
                if (currentRowIndex >= 0) {
                    ExtElement prev = new ExtElement(detailsGrid.getView().getCell(currentRowIndex,
                            colIndex));
                    prev.setStyle("font-weight", "normal");
                }
                currentRowIndex = rowIndex;

                sqlDetailsTab.getSqlHTML().setHTML(
                        to.getFormattedSql().get(
                                detailsGrid.getStore().getAt(rowIndex).getAsString(
                                        ApplicationMessages.MESSAGES.ID().toLowerCase()+0)).toString());
                ExtElement el = new ExtElement(detailsGrid.getView().getCell(rowIndex, colIndex));
                el.setStyle("font-weight", "bold");
            }

        }
    }

}
