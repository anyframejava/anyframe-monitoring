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
 * JdbcTab.java Date created: 19.02.2008
 * Last modified by: $Author: soyon.lim $ 
 * $Revision: 14286 $ $Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.gwt.client.view.facade.layer.other;

import java.util.Arrays;

import net.sf.infrared2.gwt.client.WindowResizeManagerFactory;
import net.sf.infrared2.gwt.client.grid.ColumnDef;
import net.sf.infrared2.gwt.client.grid.GridGenerationConfig;
import net.sf.infrared2.gwt.client.grid.GridWindowResizeListenerImpl;
import net.sf.infrared2.gwt.client.grid.SimpleGridWrapper;
import net.sf.infrared2.gwt.client.grid.TableFactory;
import net.sf.infrared2.gwt.client.i18n.ApplicationMessages;
import net.sf.infrared2.gwt.client.to.other.DetailOtherViewTO;
import net.sf.infrared2.gwt.client.to.other.JdbcRowTO;
import net.sf.infrared2.gwt.client.utils.UIUtils;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.event.PanelListenerAdapter;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.layout.VerticalLayout;

/**
 * <b>JdbcTab</b>
 * <p>
 * JDBC tab panel.
 * 
 * @author Sergey Evluhin
 */
public class JdbcTab extends Panel {
    /** Grid for value data perform. */
    private GridPanel grid;
    /** Main content panel. */
    private ScrollPanel contentPanel;
    /** Show when no information for display. */
    private Panel noInfo;

    private Panel gridContainer;

    private boolean created;

    /**
     * Create panel with custom tab title.
     * 
     * @param title - title of the tab.
     */
    public JdbcTab(String title) {
        super(title);
        this.setLayout(new VerticalLayout());
        this.setBorder(false);
        this.setBodyBorder(false);
        this.setPaddings(5, 5, 0, 0);
        addNoInfoPanel();
        noInfo.setVisible(false);
        DetailOtherViewTO viewTO = new DetailOtherViewTO();
        JdbcRowTO[] rows = new JdbcRowTO[1];
        rows[0] = new JdbcRowTO();
        viewTO.setJdbcRows(rows);
        createView(viewTO);
        contentPanel.setVisible(false);
    }

    private void createView(DetailOtherViewTO to) {
        contentPanel = new ScrollPanel();

        ColumnDef[] columnDefs = new ColumnDef[] { new ColumnDef(ApplicationMessages.MESSAGES.apiName(), 250),
                new ColumnDef(ApplicationMessages.MESSAGES.averageTime(), 0, ColumnDef.FLOAT),
                new ColumnDef(ApplicationMessages.MESSAGES.count(), 0, ColumnDef.INTEGER) };

        GridGenerationConfig config = new GridGenerationConfig(Arrays.asList(columnDefs));

        grid = (GridPanel) TableFactory.getGrid(DetailOtherViewTO.convertJdbcRows(to.getJdbcRows()), config);
        grid.setAutoScroll(false);
        grid.setWidth(grid.getColumnModel().getTotalWidth() + 3);

        grid.setHeight(80);
        gridContainer = new Panel();
        gridContainer.setAutoScroll(false);
        gridContainer.setBorder(false);
        gridContainer.setBodyBorder(false);
        gridContainer.setWidth(UIUtils.getCenterPanelWidth() - UIUtils.SCROLL_WIDTH);
        gridContainer.setHeight(UIUtils.calculateGridHeight(grid));
        gridContainer.add(grid);

        SimpleGridWrapper gridWrapper = new SimpleGridWrapper(grid, columnDefs, gridContainer);
        gridWrapper.adjustGridSize(false);
        WindowResizeManagerFactory.getResizeManager(WindowResizeManagerFactory.GRIDS_MANAGER).addListener(
                new GridWindowResizeListenerImpl(gridWrapper));
        gridContainer.add(grid);
        contentPanel.add(gridContainer);
        this.add(contentPanel);
        this.doLayout();
        //        contentPanel.hide();

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
        created = true;
    }

    /**
     * Create panel with default title.
     */
    public JdbcTab() {
        this(ApplicationMessages.MESSAGES.jdbc());
    }

    /**
     * Update UI components according to the newly received transfer object
     * data.
     * 
     * @param to - transfer object.
     */
    public void updateView(DetailOtherViewTO to) {
        if (to == null || to.getJdbcRows() == null || to.getJdbcRows().length == 0) {
            contentPanel.setVisible(false);
            noInfo.setVisible(true);
            return;
        }
        noInfo.setVisible(false);
        contentPanel.setVisible(true);

        final Store store = grid.getStore();
        store.setDataProxy(new MemoryProxy(DetailOtherViewTO.convertJdbcRows(to.getJdbcRows())));
        store.reload();

        if (grid != null && grid.getView() != null) {
            if (gridContainer.getBody() != null)
                gridContainer.getBody().setHeight(UIUtils.calculateGridHeight(grid), false);
            UIUtils.updateGridHeight(grid);
        }
    }

    /**
     * Add in to layout "no info panel" when absent data for display.
     */
    private void addNoInfoPanel() {
        if (noInfo == null) {
            noInfo = new Panel();
            noInfo.setBorder(false);
            noInfo.setBodyBorder(false);
            noInfo.add(new HTML(ApplicationMessages.MESSAGES.noInformationAvailableForTab()));
            this.add(noInfo);
            this.doLayout();
        }
    }

    public GridPanel getGrid() {
        return grid;
    }

}
