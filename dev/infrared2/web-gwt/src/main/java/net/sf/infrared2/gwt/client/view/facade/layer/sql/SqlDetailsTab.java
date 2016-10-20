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
 * SqlDetailsTab.java Date created: 20.02.2008
 * Last modified by: $Author: soyon.lim $ 
 * $Revision: 14286 $ $Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.gwt.client.view.facade.layer.sql;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.layout.BorderLayout;
import com.gwtext.client.widgets.layout.BorderLayoutData;

import java.util.Arrays;

import net.sf.infrared2.gwt.client.WindowResizeManagerFactory;
import net.sf.infrared2.gwt.client.grid.*;
import net.sf.infrared2.gwt.client.i18n.ApplicationMessages;
import net.sf.infrared2.gwt.client.to.other.GeneralInformationRowTO;
import net.sf.infrared2.gwt.client.to.sql.SqlViewTO;
import net.sf.infrared2.gwt.client.utils.UIUtils;
import net.sf.infrared2.gwt.client.view.facade.SplitPanelsResizeListener;

/**
 * <b>SqlDetailsTab</b><p>
 * Details tab of SQL layer view.
 * 
 * @author Sergey Evluhin
 */
public class SqlDetailsTab extends Panel {
    /** Transfer object for SQL view. */
    private SqlViewTO to;
    /** A Grid widget for display details information in tab .*/
    private GridPanel detailsGrid;
    /** Display SQL syntax */
    private HTML sqlHTML = new HTML("");
    /**  Renderer implementation for the columns with data of the date's type. 
     * This class render the date to special format.*/
    private DateRenderer dateRenderer = new DateRenderer();
    /** Represents implementation of grid's column renderer which is applies
    'clickable' style to the column's text.*/
    private ClickableRenderer clickableRenderer = new ClickableRenderer();

    /**
     * Default constructor.
     */
    public SqlDetailsTab(SqlViewTO to) {
        super(ApplicationMessages.MESSAGES.details());
        this.setSize("100%", "100%");
        this.setBodyBorder(false);
        this.setBorder(false);
        this.setAutoScroll(false);
        this.setLayout(new BorderLayout());
        this.to = to;
        createCenter();
        createSouth();
        this.doLayout();
    }

    /**
     * Creates content for the center region.
     */
    private void createCenter() {
        final ScrollPanel cp = new ScrollPanel();
        cp.setStyleName("sql-details-tab-content");
        //cp.setAutoScroll(true);
        //cp.setPaddings(5, 5, 0, 30);

        ColumnDef[] columnDefs = new ColumnDef[] {
                        new ColumnDef(ApplicationMessages.MESSAGES.ID(), 30),
                        new ColumnDef(ApplicationMessages.MESSAGES.query(), 180),
                        new ColumnDef(ApplicationMessages.MESSAGES.count(), 0, ColumnDef.INTEGER),
                        new ColumnDef(ApplicationMessages.MESSAGES.totalInclusiveTime(), 0,
                                        ColumnDef.INTEGER),
                        new ColumnDef(ApplicationMessages.MESSAGES.maxInclusiveTime(), 0,
                                        ColumnDef.INTEGER),
                        new ColumnDef(ApplicationMessages.MESSAGES.minInclusiveTime(), 0,
                                        ColumnDef.INTEGER),
                        new ColumnDef(ApplicationMessages.MESSAGES.totalExclusiveTime(), 0,
                                        ColumnDef.INTEGER),
                        new ColumnDef(ApplicationMessages.MESSAGES.maxExclusiveTime(), 0,
                                        ColumnDef.INTEGER),
                        new ColumnDef(ApplicationMessages.MESSAGES.minExclusiveTime(), 0,
                                        ColumnDef.INTEGER),
                        new ColumnDef(ApplicationMessages.MESSAGES.timeOfFirstExecution(), 0,
                                        ColumnDef.DATE),
                        new ColumnDef(ApplicationMessages.MESSAGES.timeOfLastExecution(), 0,
                                        ColumnDef.DATE),
                        new ColumnDef(ApplicationMessages.MESSAGES.firstExecutionInclusiveTime(),
                                        0, ColumnDef.INTEGER),
                        new ColumnDef(ApplicationMessages.MESSAGES.lastExecutionInclusiveTime(),
                                        0, ColumnDef.INTEGER),
                        new ColumnDef(ApplicationMessages.MESSAGES.firstExecutionExclusiveTime(),
                                        0, ColumnDef.INTEGER),
                        new ColumnDef(ApplicationMessages.MESSAGES.lastExecutionExclusiveTime(),
                                        0, ColumnDef.INTEGER) };

        GridGenerationConfig config = new GridGenerationConfig(Arrays.asList(columnDefs));
        detailsGrid = (GridPanel) TableFactory.getGrid(GeneralInformationRowTO
                        .convertSqlGeneralInformationRows(to.getAllSqlQueries()), config);

        detailsGrid.getColumnModel().setRenderer(9, dateRenderer);
        detailsGrid.getColumnModel().setRenderer(10, dateRenderer);
        detailsGrid.setAutoHeight(true);
        detailsGrid.setWidth(detailsGrid.getColumnModel().getTotalWidth());

        detailsGrid.getColumnModel().setRenderer(1, clickableRenderer);
        
        
        Panel p = new Panel();
        p.setAutoScroll(true);
        p.setBorder(false);
        p.setBodyBorder(false);
        p.setWidth(UIUtils.getCenterPanelWidth() - UIUtils.SCROLL_WIDTH);
        p.setHeight(UIUtils.calculateGridHeight(detailsGrid));
        p.add(detailsGrid);
        SimpleGridWrapper gridWrapper = new SimpleGridWrapper(detailsGrid, columnDefs,p);
        gridWrapper.adjustGridSize(false);
        WindowResizeManagerFactory.getResizeManager(WindowResizeManagerFactory.GRIDS_MANAGER)
                        .addListener(new GridWindowResizeListenerImpl(gridWrapper));
        cp.add(p);

        this.add(cp, new BorderLayoutData(RegionPosition.CENTER));
    }

    /**
     * Creates content for the bottom region.
     */
    private void createSouth() {
        Panel cp = new Panel();
        cp.setHeight(UIUtils.getCenterPanelHeight() / 2);
        cp.setAutoScroll(true);
        cp.setPaddings(5);
        cp.add(sqlHTML);
        final BorderLayoutData borderLayoutData = new BorderLayoutData(RegionPosition.SOUTH);
        borderLayoutData.setSplit(true);
        WindowResizeManagerFactory.getResizeManager(WindowResizeManagerFactory.PANELS_MANAGER)
                        .addListener(new SplitPanelsResizeListener(cp));
        this.add(cp, borderLayoutData);
    }

    /**
     * @return table with detailed information about SQL queries.
     */
    public GridPanel getDetailsGrid() {
        return detailsGrid;
    }

    /**
     * @return container for SQL query.
     */
    public HTML getSqlHTML() {
        return sqlHTML;
    }

    public void updateTab(SqlViewTO to) {
        this.to = to;
        detailsGrid.getStore().setDataProxy(
                new MemoryProxy(GeneralInformationRowTO.convertSqlGeneralInformationRows(
                        to.getAllSqlQueries())));
        detailsGrid.getStore().reload();
    }
}
