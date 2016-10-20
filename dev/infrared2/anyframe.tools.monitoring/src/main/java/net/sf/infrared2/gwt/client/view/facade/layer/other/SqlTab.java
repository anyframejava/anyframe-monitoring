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
 * SqlTab.java Date created: 19.02.2008
 * Last modified by: $Author: soyon.lim $ 
 * $Revision: 14286 $ $Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.gwt.client.view.facade.layer.other;

import java.util.Arrays;

import net.sf.infrared2.gwt.client.WindowResizeManagerFactory;
import net.sf.infrared2.gwt.client.grid.ColumnDef;
import net.sf.infrared2.gwt.client.grid.ComplexGrid;
import net.sf.infrared2.gwt.client.grid.GridGenerationConfig;
import net.sf.infrared2.gwt.client.grid.GridWindowResizeListenerImpl;
import net.sf.infrared2.gwt.client.grid.Relation;
import net.sf.infrared2.gwt.client.grid.TableFactory;
import net.sf.infrared2.gwt.client.i18n.ApplicationMessages;
import net.sf.infrared2.gwt.client.to.other.DetailOtherViewTO;
import net.sf.infrared2.gwt.client.to.sql.SqlRowTO;
import net.sf.infrared2.gwt.client.to.sql.SqlViewTO;
import net.sf.infrared2.gwt.client.utils.UIUtils;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.event.PanelListenerAdapter;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.layout.VerticalLayout;

/**
 * <b>SqlTab</b><p>
 * SQL tab panel.
 * 
 * @author Sergey Evluhin
 */
public class SqlTab extends Panel {
    /** Contain view for tab based on transfer object data. */
    private VerticalPanel contentPanel;
    /** Contain view for tab based on SQL query. */
    private Panel sqlPanel;
    /** The html. */
    private HTML html = new HTML();
    /** Grid for perform data selected by time. */
    private ComplexGrid complexGridByTime;
    /** Grid for perform data selected by count. */
    private ComplexGrid complexGridByCount;
    /** Show this panel if no information for display*/
    private Panel noInfo;

    /**
     * Creates panel with custom title.
     * 
     * @param title - title of tab panel.
     */
    public SqlTab(String title) {
        super(title);
        this.setAutoScroll(true);
        this.setLayout(new VerticalLayout());
        addListener(new PanelListenerAdapter() {
            public void onActivate(Panel panel) {
                if (complexGridByCount != null)
                    if (complexGridByCount.getContentGrid().getView() != null)
                        complexGridByCount.getContentGrid().getView().refresh();
                if (complexGridByTime != null)
                    if (complexGridByTime.getContentGrid().getView() != null)
                        complexGridByTime.getContentGrid().getView().refresh();
            }
        });
        createView("");
        sqlPanel.setVisible(false);
        DetailOtherViewTO viewTO = new DetailOtherViewTO();
        SqlRowTO[] rows = new SqlRowTO[1];
        rows[0] = new SqlRowTO();
        viewTO.setSqlRowsByExecutionCount(rows);
        viewTO.setSqlRowsByExecutionTime(rows);
        createView(viewTO);
        contentPanel.setVisible(false);
        addNoInfoPanel();
        noInfo.setVisible(false);
    }

    /**
     * Creates panel with default title.
     */
    public SqlTab() {
        this(ApplicationMessages.MESSAGES.sql());
    }

    /**
     * Creates view for this tab based on transfer object data.
     * 
     * @param detailTo - transfer object.
     */
    private void createView(DetailOtherViewTO detailTo) {
        contentPanel = new VerticalPanel();
        contentPanel.setSpacing(15);

        Label top5Time = new Label(ApplicationMessages.MESSAGES.topSQLqueriesByAverageExecutionTime());
        top5Time.addStyleName("bold-underline");
        contentPanel.add(top5Time);
        complexGridByTime = createComplexGrid(detailTo.getSqlRowsByExecutionTime());
        contentPanel.add(complexGridByTime);

        Label top5 = new Label(ApplicationMessages.MESSAGES.topSQLqueriesByExecutionCount());
        top5.addStyleName("bold-underline");
        contentPanel.add(top5);

        complexGridByCount = createComplexGrid(detailTo.getSqlRowsByExecutionCount());
        contentPanel.add(complexGridByCount);
        //contentPanel.doLayout();
        complexGridByCount.setHeight(complexGridByCount.calculateHeight() + 2);
        complexGridByTime.setHeight(complexGridByTime.calculateHeight() + 2);
        WindowResizeManagerFactory.getResizeManager(WindowResizeManagerFactory.GRIDS_MANAGER).addListener(
                new GridWindowResizeListenerImpl(complexGridByTime));

        WindowResizeManagerFactory.getResizeManager(WindowResizeManagerFactory.GRIDS_MANAGER).addListener(
                new GridWindowResizeListenerImpl(complexGridByCount));
        ScrollPanel scrollPanel = new ScrollPanel();
        scrollPanel.add(contentPanel);
        this.add(scrollPanel);
    }

    /**
     * Creates view for this tab based on SQL query.
     * 
     * @param sql - SQL query to show.
     */
    void createView(String sql) {
        sqlPanel = new Panel();
        sqlPanel.setLayout(new FitLayout());
        sqlPanel.setBodyBorder(false);
        sqlPanel.setBorder(false);
        html.setHTML(sql);
        sqlPanel.add(html);
        this.add(sqlPanel);
        sqlPanel.doLayout();
    }

    /**
     * Update SQL query.
     * 
     * @param sql - new SQL query.
     */
    public void updateViewSql(String sql) {
        noInfo.setVisible(false);
        contentPanel.setVisible(false);
        sqlPanel.setVisible(true);
        html.setHTML(sql);
        this.doLayout();
    }

    /**
     * Update view accordingly to the newly receive transfer object.
     * 
     * @param detailTo - transfer object.
     */
    public void updateView(DetailOtherViewTO detailTo) {
        if (detailTo == null || detailTo.getSqlRowsByExecutionCount() == null || detailTo.getSqlRowsByExecutionTime() == null
                || detailTo.getSqlRowsByExecutionCount().length == 0 || detailTo.getSqlRowsByExecutionTime().length == 0) {
            sqlPanel.setVisible(false);
            contentPanel.setVisible(false);
            noInfo.setVisible(true);
            return;
        }
        noInfo.setVisible(false);
        sqlPanel.setVisible(false);
        contentPanel.setVisible(true);

        complexGridByTime.getContentGrid().getStore().setDataProxy(new MemoryProxy(SqlViewTO.convertSqlRows(detailTo.getSqlRowsByExecutionTime())));
        complexGridByCount.getContentGrid().getStore().setDataProxy(new MemoryProxy(SqlViewTO.convertSqlRows(detailTo.getSqlRowsByExecutionCount())));

        complexGridByTime.getContentGrid().getStore().reload();
        complexGridByCount.getContentGrid().getStore().reload();

        UIUtils.updateGridHeight(complexGridByCount);
        UIUtils.updateGridHeight(complexGridByTime);
        complexGridByTime.getContentGrid().getView().refresh();
        complexGridByCount.getContentGrid().getView().refresh();

        this.doLayout();
    }

    /**
     * Create complex grid.
     * 
     * @param rows - rows data for the grid.
     * @return grid with data.
     */
    private ComplexGrid createComplexGrid(SqlRowTO[] rows) {
        Object[][] sqlData = SqlViewTO.convertSqlRows(rows);
        Relation rel = new Relation();
        rel.addRelation(ApplicationMessages.MESSAGES.ID(), 0, 1, 35);
        rel.addRelation(ApplicationMessages.MESSAGES.sqlQuery(), 1, 1, 230);
        rel.addRelation(ApplicationMessages.MESSAGES.averageTime(), 2, 3);
        rel.addRelation(ApplicationMessages.MESSAGES.count(), 3, 2);
        rel.addRelation(ApplicationMessages.MESSAGES.maximumTime(), 4, 2);
        rel.addRelation(ApplicationMessages.MESSAGES.minimumTime(), 5, 2);
        rel.addRelation(ApplicationMessages.MESSAGES.firstExecutionTime(), 6, 2);
        rel.addRelation(ApplicationMessages.MESSAGES.lastExecutionTime(), 7, 2);

        ColumnDef[] colDefs = new ColumnDef[] { new ColumnDef("-", 15, ColumnDef.STRING),

        new ColumnDef("-", 55, ColumnDef.STRING),

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
                new ColumnDef(ApplicationMessages.MESSAGES.prepare(), 60, ColumnDef.INTEGER), };

        final GridGenerationConfig conf = new GridGenerationConfig(Arrays.asList(colDefs), rel);
        final ComplexGrid grid = (ComplexGrid) TableFactory.getGrid(sqlData, conf);
        return grid;
    }

    /**
     * Hides all informational panels and show the default panel.
     */
    private void addNoInfoPanel() {
        if (noInfo == null) {
            noInfo = new Panel();
            noInfo.setPaddings(5);
            noInfo.setBorder(false);
            noInfo.setBodyBorder(false);
            noInfo.add(new HTML(ApplicationMessages.MESSAGES.noInformationAvailableForTab()));
            this.add(noInfo);
            this.doLayout();
        }
    }

    /**
     * Hides the default panel.
     */
    private void removeNoInfoPanel() {
        if (noInfo != null) {
            this.remove(noInfo.getId(), true);
            noInfo = null;
        }
    }

    public ComplexGrid getComplexGridByTime() {
        return complexGridByTime;
    }

    public ComplexGrid getComplexGridByCount() {
        return complexGridByCount;
    }
}
