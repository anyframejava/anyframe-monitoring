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

import net.sf.infrared2.gwt.client.utils.UIUtils;

import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;

/**
 * <b>ComplexGrid</b>
 * <p>
 * Represents table with two-level header column nesting.<br>
 * It means that grid will have two layers of headers. First layer of headers it
 * is just a table without body but with headers. Second layer of headers is a
 * table with headers and body. Tables are situated one under second and have
 * one listener on resize columns width.
 * 
 * @see net.sf.infrared2.gwt.client.grid.Relation
 * @author Sergey Evluhin
 */
public class ComplexGrid extends Panel {
    private static final int DEFAULT_COLUMN_WIDTH = 120;

    /** Header grid without body. */
    private GridPanel headerGrid;
    /** Content grid with header and data. */
    private GridPanel contentGrid;
    /** Relations between header and content grids. */
    private Relation relation;
    
    private GridColumnListenerImplementation gridColumnListenerImplementation;

    /**
     * Creates complex grid.
     * 
     * @param contentGrid - content grid with header and data.
     * @param relation - relations between header and content grids.
     */
    public ComplexGrid(GridPanel contentGrid, Relation relation) {
        super();
        this.setBorder(false);
        this.setAutoScroll(true);
        this.contentGrid = contentGrid;
        this.relation = relation;
        // initialize other settings
        init();
    }

    /**
     * Initialize settings for this component and for it's children.
     */
    private void init() {

        this.headerGrid = createHeaderGrid();
        headerGrid.setHeight(UIUtils.HEADER_GRID_HEIGHT);

        // initSize();
        initSizeByHeaderTable();
        initListeners();
        this.add(headerGrid);
        this.add(contentGrid);
    }

    /**
     * Creates header grid, columns are building by using ColumnMapping list in
     * Relation class.
     * 
     * @see net.sf.infrared2.gwt.client.grid.Relation
     * @see net.sf.infrared2.gwt.client.grid.ColumnMapping
     * @return grid that present one row header columns.
     */
    private GridPanel createHeaderGrid() {

        GridPanel grid = null;
        // Any field definition
        RecordDef recordDef = new RecordDef(new FieldDef[] { new StringFieldDef("company") });

        ArrayReader reader = new ArrayReader(recordDef);
        Store store = new Store(reader);

        ColumnConfig[] cfg = new ColumnConfig[this.relation.getMappings().size()];

        // configuring columns capabilities for the header grid's header
        for (int i = 0; i < this.relation.getMappings().size(); i++) {
            ColumnMapping mapping = (ColumnMapping) this.relation.getMappings().get(i);

            ColumnConfig colCfg = new ColumnConfig();
            colCfg.setHeader(mapping.getTopRowColumnTitle());

            if (mapping.getTopRowColumnWidth() > 0) {
                colCfg.setWidth(mapping.getTopRowColumnWidth());
            }

            colCfg.setSortable(false);
            colCfg.setTooltip(mapping.getTopRowColumnTitle());
            cfg[i] = colCfg;
        }

        ColumnModel columnModel = new ColumnModel(cfg);

        grid = new GridPanel(store, columnModel);
        grid.setEnableColumnHide(false);
        grid.setBorder(false);
        grid.setBodyBorder(false);
        grid.setEnableColumnMove(false);
        grid.setEnableCtxMenu(false);
        grid.setEnableDragDrop(false);
        return grid;
    }

    /**
     * Set initial width and heights for each tables and its columns.
     */
/*    
    private void initSize() {
        int contentColumnStartIndex = 0;
        for (int i = 0; i < relation.getMappings().size(); i++) {
            ColumnMapping mapping = (ColumnMapping) relation.getMappings().get(i);
            int headerIndex = mapping.getTopRowColumnIndex();
            int countCols = mapping.getCountColumnSpan();

            int headerWidth = headerGrid.getColumnModel().getColumnWidth(headerIndex);

            for (int j = 0; j < countCols; j++, contentColumnStartIndex++) {
                int contentColumnWidth = headerWidth / countCols;
                int contentColumnWidthMod = headerWidth % countCols;
                if (j == 0) {
                    contentGrid.getColumnModel().setColumnWidth(contentColumnStartIndex,
                                    contentColumnWidth + contentColumnWidthMod);
                } else {
                    contentGrid.getColumnModel().setColumnWidth(contentColumnStartIndex,
                                    contentColumnWidth);
                }
            }

        }

        int newWidth = headerGrid.getColumnModel().getTotalWidth() + 3;
        headerGrid.setWidth(newWidth);
        contentGrid.setWidth(newWidth);

        // normalize height of the complex grid
        normalizeHeight();
        // set the panel width equals center panel width
        int panelWidth = UIUtils.getCenterPanelWidth() - UIUtils.SCROLL_WIDTH;
        if (panelWidth > 0) {
            this.setWidth(panelWidth);
        }
    }
*/
    
    public void initSizeByHeaderTable() {

        int reservedWidth = 0;
        int countNonReservedContentColumns = 0;

        for (int i = 0; i < relation.getMappings().size(); i++) {
            ColumnMapping mapping = (ColumnMapping) relation.getMappings().get(i);
            int topRowColumnWidth = mapping.getTopRowColumnWidth();

            if (topRowColumnWidth > 0) {
                reservedWidth += topRowColumnWidth;
                // countNonReservedContentColumns-=countColumnSpan;
            } else {
                int countColumnSpan = mapping.getCountColumnSpan();
                countNonReservedContentColumns += countColumnSpan;
            }
        }

        int contentColumnStartIndex = 0;
        int centerPanelWidth = UIUtils.getCenterPanelWidth() - UIUtils.SCROLL_WIDTH;
        int newTableWidth = centerPanelWidth - reservedWidth - 3;
        int widthPerContentColumn = newTableWidth / countNonReservedContentColumns;
        int modWidthPerContentColumn = newTableWidth % countNonReservedContentColumns;

        int newHeaderWidth = 0;
        boolean modAdded = false;
        for (int i = 0; i < relation.getMappings().size(); i++) {
            ColumnMapping mapping = (ColumnMapping) relation.getMappings().get(i);
            int topRowColumnWidth = mapping.getTopRowColumnWidth();
            int countCols = mapping.getCountColumnSpan();
            newHeaderWidth = 0;
            for (int j = 0; j < countCols; j++, contentColumnStartIndex++) {
                if (topRowColumnWidth > 0) {
                    int contentColumnWidth = topRowColumnWidth / countCols;
                    int contentColumnWidthMod = topRowColumnWidth % countCols;
                    newHeaderWidth = topRowColumnWidth;

                    if (j == 0) {
                        contentGrid.getColumnModel().setColumnWidth(contentColumnStartIndex,
                                        topRowColumnWidth + contentColumnWidthMod);
                    } else {
                        contentGrid.getColumnModel().setColumnWidth(contentColumnStartIndex,
                                        contentColumnWidth);
                    }
                } else {
                    int widthOfColumn = widthPerContentColumn;
                    if (!modAdded) {
                        widthOfColumn += modWidthPerContentColumn;
                        contentGrid.getColumnModel().setColumnWidth(contentColumnStartIndex,
                                        widthOfColumn);
                        modAdded = true;
                    } else {
                        contentGrid.getColumnModel().setColumnWidth(contentColumnStartIndex,
                                        widthOfColumn);
                    }
                    newHeaderWidth += widthOfColumn;
                }
            }
            headerGrid.getColumnModel().setColumnWidth(i, newHeaderWidth);

        }

        headerGrid.setWidth(centerPanelWidth);
        contentGrid.setWidth(centerPanelWidth);

        normalizeHeight();

        this.setWidth(centerPanelWidth);
        if(this.getBody()!=null){
//            this.getBody().setStyle("width", centerPanelWidth+"px");
            this.getBody().setWidth(centerPanelWidth, false);
        }
    }

    /**
     * Calculates height of complex grid (with scroll).
     * 
     * @return height of complex grid.
     */
    public int calculateHeight() {
        return UIUtils.HEADER_GRID_HEIGHT + UIUtils.calculateGridHeight(this.contentGrid);
    }

    /**
     * Normalize height of whole grid.
     */
    public void normalizeHeight() {
        this.setHeight(calculateHeight());
    }

    /**
     * Normalize height of whole grid.
     */
    public void normalizeWidth() {
        UIUtils.adjustGridWidth(headerGrid);
        UIUtils.adjustGridWidth(contentGrid);
        contentGrid.getView().refresh();
    }

    /**
     * Initialize listeners for tables.
     */
    private void initListeners() {
        gridColumnListenerImplementation = new GridColumnListenerImplementation(
                        this);
        headerGrid.addGridColumnListener(gridColumnListenerImplementation);
        contentGrid.addGridColumnListener(gridColumnListenerImplementation);
    }

    /**
     * @return top grid (header grid).
     */
    public GridPanel getHeaderGrid() {
        return headerGrid;
    }

    /**
     * @return content grid (grid with data).
     */
    public GridPanel getContentGrid() {
        return contentGrid;
    }

    /**
     * @return relation object that contains relations between content and
     *         header grids.
     */
    public Relation getRelation() {
        return relation;
    }

    public GridColumnListenerImplementation getGridColumnListenerImplementation() {
        return gridColumnListenerImplementation;
    }
}
