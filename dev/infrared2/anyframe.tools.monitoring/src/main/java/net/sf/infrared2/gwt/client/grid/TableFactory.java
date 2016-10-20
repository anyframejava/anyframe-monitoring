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
package net.sf.infrared2.gwt.client.grid;

import java.util.List;

import net.sf.infrared2.gwt.client.utils.UIUtils;

import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.core.Ext;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.BooleanFieldDef;
import com.gwtext.client.data.DateFieldDef;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.FloatFieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.event.GridColumnListenerAdapter;
import com.gwtext.client.widgets.layout.FitLayout;

/**
 * <b>TableFactory</b><p>
 * Creates grids - complex or simple.
 * 
 * @author Sergey Evluhin
 */
public class TableFactory {
    /** Default width of column. */
    private static final int DEFULT_WIDTH = 60;
    /** Renderer for columns with double values. */
    private static final DoubleRenderer doubleRenderer = new DoubleRenderer();

    /**
     * Creates complex grid if config.getRelation() parameter is not null.
     * Returns either ComplexGrid object or GridPanel object.
     * 
     * @param data - data for the grid's rows.
     * @param config - configuration for the complex grid.
     * @return widget that is grid - complex or simple.
     */
    public static Widget getGrid(Object[][] data, GridGenerationConfig config) {

        GridPanel contentGrid = contentGrid(data, config);

        // if true will be returned simple grid
        if (config.getRelation() == null) {
            contentGrid.addGridColumnListener(new GridColumnListenerAdapter() {
                /**
                 * Method of listener that calls special hack to change all
                 * table width when total width of all columns is higher then
                 * table width.
                 */
                public void onColumnResize(GridPanel grid, int colIndex, int newSize) {
                    super.onColumnResize(grid, colIndex, newSize);
                    UIUtils.adjustGridWidth(grid);
                    grid.getView().refresh();
                }
            });

            return contentGrid;
        }

        ComplexGrid grid = new ComplexGrid(contentGrid, config.getRelation());
        return grid;
    }

    /**
     * Create field definition for grid by type of column's data.
     * 
     * @param type - type of column's data.
     * @param dataIndexName - data index. By this index data will be read from
     *            array.
     * @return field definition.
     */
    private static FieldDef getFieldDef(int type, String dataIndexName) {
        switch (type) {
            case ColumnDef.STRING:
                return new StringFieldDef(dataIndexName);

            case ColumnDef.INTEGER:
                return new IntegerFieldDef(dataIndexName);

            case ColumnDef.FLOAT:
                return new FloatFieldDef(dataIndexName);

            case ColumnDef.BOOLEAN:
                return new BooleanFieldDef(dataIndexName);

            case ColumnDef.DATE:
                return new DateFieldDef(dataIndexName);

            default:
                return new StringFieldDef(dataIndexName);
        }
    }



    /**
     * Creates inner (nested) grid in case for complex grid, or content grid if
     * needed the simple grid. Returned grid contains headers and data.
     * 
     * @param data - data array with content data.
     * @param config - grid configuration.
     * @return grid with body and data.
     */
    private static GridPanel contentGrid(Object[][] data, GridGenerationConfig config) {
        MemoryProxy proxy = new MemoryProxy(data);
        List columnDefs = config.getColumnDefs();
        FieldDef[] fieldDefs = new FieldDef[columnDefs.size()];
        ColumnConfig[] columnConfigs = new ColumnConfig[columnDefs.size()];
        // creates from column definition object, configuration object for the
        // column
        for (int i = 0; i < columnDefs.size(); i++) {
            ColumnDef colDef = (ColumnDef) columnDefs.get(i);
            String dataIndexName = colDef.getTitle().toLowerCase() + i;
            ColumnConfig columnConfig = new ColumnConfig();

            int width = colDef.getWidth();
            if (width == 0) {
                //width = DEFULT_WIDTH;
            }else{
                columnConfig.setWidth(width);
            }

            columnConfig.setId(Ext.generateId());
            columnConfig.setHeader(colDef.getTitle());
            columnConfig.setTooltip(colDef.getTitle());
            columnConfig.setDataIndex(dataIndexName);
            columnConfig.setSortable(colDef.isSortable());
            final int type = colDef.getType();
            if (type == ColumnDef.FLOAT) {
                columnConfig.setRenderer(doubleRenderer);
            }
            fieldDefs[i] = getFieldDef(type, dataIndexName);
            columnConfigs[i] = columnConfig;
        }

        RecordDef recordDef = new RecordDef(fieldDefs);

        ArrayReader reader = new ArrayReader(recordDef);
        Store store = new Store(proxy, reader);

        ColumnModel columnModel = new ColumnModel(columnConfigs);

        columnModel.setDefaultSortable(true);
        // creates and configure grid
        GridPanel grid = new GridPanel(store, columnModel);
        grid.setLayout(new FitLayout());
        grid.setBorder(false);
        grid.setBodyBorder(false);
        grid.setClosable(false);
        grid.setCollapsed(false);
        grid.setAutoScroll(false);
        grid.setCollapsible(false);
        grid.setHeader(false);
        grid.setStripeRows(true);
        grid.setTrackMouseOver(true);
        grid.setEnableColumnMove(false);
        grid.setEnableColumnHide(false);
        grid.setHeight("100%");

        store.load();

        return grid;
    }
}
