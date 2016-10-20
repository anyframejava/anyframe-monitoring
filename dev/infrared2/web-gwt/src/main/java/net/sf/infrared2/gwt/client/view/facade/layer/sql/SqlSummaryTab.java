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
 * SqlSummaryTab.java Date created: 20.02.2008
 * Last modified by: $Author: soyon.lim $ 
 * $Revision: 14286 $ $Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.gwt.client.view.facade.layer.sql;

import com.google.gwt.user.client.ui.*;
import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.event.PanelListenerAdapter;
import com.gwtext.client.widgets.layout.BorderLayout;
import com.gwtext.client.widgets.layout.BorderLayoutData;
import com.gwtext.client.widgets.layout.VerticalLayout;

import java.util.Arrays;

import net.sf.infrared2.gwt.client.WindowResizeManagerFactory;
import net.sf.infrared2.gwt.client.grid.*;
import net.sf.infrared2.gwt.client.i18n.ApplicationMessages;
import net.sf.infrared2.gwt.client.to.sql.SqlViewTO;
import net.sf.infrared2.gwt.client.utils.UIUtils;
import net.sf.infrared2.gwt.client.view.facade.SplitPanelsResizeListener;

/**
 * <b>SqlSummaryTab</b>
 * <p>
 * Summary tab for SQL layer view
 * 
 * @author Sergey Evluhin
 */
public class SqlSummaryTab extends Panel {
    /** Transfer object for SQL view. */
    private SqlViewTO to;
    /**
     * Represents table with two-level header column nesting. Need for summary
     * info representation
     */
    private ComplexGrid summaryGrid;
    /** Graphic and diagrams presentation. */
    private Image image;
    /**
     * Represents implementation of grid's column renderer which is applies
     * 'clickable' style to the column's text.
     */
    private ClickableRenderer clickableRenderer = new ClickableRenderer();

    /** List box with possible sort modes. */
    private ListBox listBox;

    private Label top5;

    /**
     * Default constructor.
     * 
     * @param to - transfer object for SQL layer view.
     */
    public SqlSummaryTab(SqlViewTO to) {
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
                if (summaryGrid != null)
                    if (summaryGrid.getContentGrid().getView() != null)
                        summaryGrid.getContentGrid().getView().refresh();
            }
        });
    }

    /**
     * Creates content for the center region.
     */
    private void createCenter() {
        final Panel vertPanel = new Panel();
        vertPanel.setLayout(new VerticalLayout(0));
        vertPanel.setAutoScroll(true);
        vertPanel.setPaddings(5, 5, 0, 0);
        vertPanel.setBorder(false);
        vertPanel.setHeight(UIUtils.getCenterPanelHeight() / 2);
        vertPanel.setWidth("100%");

        top5 = new Label(ApplicationMessages.MESSAGES.topSQLqueriesByAverageExecutionTime());
        top5.addStyleName("bold-underline");

        HorizontalPanel horPanel = new HorizontalPanel();
        horPanel.setSpacing(15);
        horPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);

        horPanel.add(new Label(" " + ApplicationMessages.MESSAGES.selectTopCriterion() + " "));
        listBox = new ListBox();
        listBox.addItem(ApplicationMessages.MESSAGES.byAverageExecutionTimeItem(), "0");
        listBox.addItem(ApplicationMessages.MESSAGES.byExecutionCountItem(), "1");
        listBox.addChangeListener(new ChangeListener() {

            public void onChange(Widget sender) {
                ListBox list = (ListBox) sender;
                int selected = list.getSelectedIndex();
                if (selected == 0) {
                    summaryGrid.getContentGrid().getStore().setDataProxy(
                                    new MemoryProxy(SqlViewTO.convertSqlRows(to
                                                    .getSqlRowsByExecutionTime())));
                    top5
                                    .setText(ApplicationMessages.MESSAGES
                                                    .topSQLqueriesByAverageExecutionTime());

                    image.setUrl((String) to.getImgUrlByTime().keySet().iterator().next());
                } else {
                    summaryGrid.getContentGrid().getStore().setDataProxy(
                                    new MemoryProxy(SqlViewTO.convertSqlRows(to
                                                    .getSqlRowsByExecutionCount())));
                    top5.setText(ApplicationMessages.MESSAGES.topSQLqueriesByExecutionCount());
                    image.setUrl((String) to.getImgUrlByCount().keySet().iterator().next());
                }
                summaryGrid.getContentGrid().getStore().reload();
            }

        });

        horPanel.add(listBox);

        vertPanel.add(horPanel);

        vertPanel.add(top5);

        summaryGrid = (ComplexGrid) createSummaryGrid(SqlViewTO.convertSqlRows(to
                        .getSqlRowsByExecutionTime()));
        summaryGrid.setWidth(UIUtils.getCenterPanelWidth() - UIUtils.SCROLL_WIDTH);
        vertPanel.add(summaryGrid);
        this.add(vertPanel, new BorderLayoutData(RegionPosition.CENTER));
    }

    /**
     * Creates content for the bottom region.
     */
    private void createSouth() {
        Panel cp = new Panel();
        cp.setHeight(UIUtils.getCenterPanelHeight() / 2);
        cp.setAutoScroll(true);
        image = new Image((String) to.getImgUrlByTime().keySet().iterator().next());
        cp.add(image);
        final BorderLayoutData borderLayoutData = new BorderLayoutData(RegionPosition.SOUTH);
        borderLayoutData.setSplit(true);
        WindowResizeManagerFactory.getResizeManager(WindowResizeManagerFactory.PANELS_MANAGER)
                        .addListener(new SplitPanelsResizeListener(cp));
        this.add(cp, borderLayoutData);
    }

    /**
     * Creates the summary grid with SQL queries.
     * 
     * @param sqlData - data for the grid.
     * @return table component with data.
     */
    private ComplexGrid createSummaryGrid(Object[][] sqlData) {
        Relation rel = new Relation();
        rel.addRelation(ApplicationMessages.MESSAGES.ID(), 0, 1, 35);
        rel.addRelation(ApplicationMessages.MESSAGES.sqlQuery(), 1, 1, 250);
        rel.addRelation(ApplicationMessages.MESSAGES.averageTime(), 2, 3);
        rel.addRelation(ApplicationMessages.MESSAGES.countOfCalls(), 3, 2);
        rel.addRelation(ApplicationMessages.MESSAGES.maximumTime(), 4, 2);
        rel.addRelation(ApplicationMessages.MESSAGES.minimumTime(), 5, 2);
        rel.addRelation(ApplicationMessages.MESSAGES.firstExecutionTime(), 6, 2);
        rel.addRelation(ApplicationMessages.MESSAGES.lastExecutionTime(), 7, 2);

        ColumnDef[] colDefs = new ColumnDef[] {
                        new ColumnDef("-", 35, ColumnDef.STRING),

                        new ColumnDef("-", 250, ColumnDef.STRING),

                        new ColumnDef(ApplicationMessages.MESSAGES.total(), 60, ColumnDef.FLOAT),
                        new ColumnDef(ApplicationMessages.MESSAGES.execute(), 60, ColumnDef.FLOAT),
                        new ColumnDef(ApplicationMessages.MESSAGES.prepare(), 60, ColumnDef.FLOAT),

                        new ColumnDef(ApplicationMessages.MESSAGES.execute(), 60, ColumnDef.INTEGER),
                        new ColumnDef(ApplicationMessages.MESSAGES.prepare(), 60, ColumnDef.INTEGER),

                        new ColumnDef(ApplicationMessages.MESSAGES.execute(), 60, ColumnDef.INTEGER),
                        new ColumnDef(ApplicationMessages.MESSAGES.prepare(), 60, ColumnDef.INTEGER),

                        new ColumnDef(ApplicationMessages.MESSAGES.execute(), 60, ColumnDef.INTEGER),
                        new ColumnDef(ApplicationMessages.MESSAGES.prepare(), 60, ColumnDef.INTEGER),

                        new ColumnDef(ApplicationMessages.MESSAGES.execute(), 60, ColumnDef.INTEGER),
                        new ColumnDef(ApplicationMessages.MESSAGES.prepare(), 60, ColumnDef.INTEGER),

                        new ColumnDef(ApplicationMessages.MESSAGES.execute(), 60, ColumnDef.INTEGER),
                        new ColumnDef(ApplicationMessages.MESSAGES.prepare(), 60, ColumnDef.INTEGER),

        };

        final GridGenerationConfig conf = new GridGenerationConfig(Arrays.asList(colDefs), rel);
        final ComplexGrid grid = (ComplexGrid) TableFactory.getGrid(sqlData, conf);
        grid.getContentGrid().getColumnModel().setRenderer(1, clickableRenderer);
        return grid;
    }

    /**
     * @return summary table component.
     */
    public ComplexGrid getSummaryGrid() {
        return summaryGrid;
    }

    /**
     * @return graphic with statistic by SQL queries.
     */
    public Image getImage() {
        return image;
    }

    /**
     * Method updateTab - update current sql view
     * 
     * @param to of type SqlViewTO
     */
    public void updateTab(SqlViewTO to) {
        this.to = to;
        listBox.setSelectedIndex(0);
        top5.setText(ApplicationMessages.MESSAGES.topSQLqueriesByAverageExecutionTime());
        summaryGrid.getContentGrid().getStore().setDataProxy(
                        new MemoryProxy(SqlViewTO.convertSqlRows(to.getSqlRowsByExecutionTime())));
        summaryGrid.setWidth(UIUtils.getCenterPanelWidth() - UIUtils.SCROLL_WIDTH);
        summaryGrid.getContentGrid().getStore().reload();
        image.setUrl((String) to.getImgUrlByTime().keySet().iterator().next());
    }
}
