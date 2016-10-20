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
 * SimpleGridWrapper.java Date created: 13.05.2008
 * Last modified by: $Author: soyon.lim $ 
 * $Revision: 14286 $ $Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.gwt.client.grid;

import net.sf.infrared2.gwt.client.utils.UIUtils;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.gwtext.client.core.ExtElement;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.grid.GridPanel;

/**
 * @author Sergey Evluhin
 */
public class SimpleGridWrapper {

    /** Width parameter name at styles rules. */
    private static final String PARAM_NAME = "width";

    private ColumnDef[] defs;
    private GridPanel grid;
    private Panel container;

    public SimpleGridWrapper(GridPanel grid, ColumnDef[] defs, Panel container) {
        this.defs = defs;
        this.grid = grid;
        this.container = container;
    }

    public GridPanel getGrid() {
        return grid;
    }

    public void setGrid(GridPanel grid) {
        this.grid = grid;
    }

    public ColumnDef[] getDefs() {
        return defs;
    }

    public void setDefs(ColumnDef[] defs) {
        this.defs = defs;
    }

    public void adjustGridSize(boolean renderered) {

        int reservedWidth = 0;
        int countReservedColumns = 0;

        for (int i = 0; i < defs.length; i++) {
            ColumnDef def = (ColumnDef) defs[i];
            int columnWidth = def.getWidth();
            if (columnWidth > 0) {
                reservedWidth += columnWidth;
                countReservedColumns++;
            }
        }

        if (countReservedColumns == grid.getColumnModel().getColumnCount()) {
            System.out
                            .println("Grid haven't dynamic column width: all is static (try to unhardcode the widths of columns");
            return;
        }

        int countNonReservedContentColumns = grid.getColumnModel().getColumnCount()
                        - countReservedColumns;

        int centerPanelWidth = UIUtils.getCenterPanelWidth() - UIUtils.SCROLL_WIDTH;
        int newTableWidth = centerPanelWidth - reservedWidth - 3;
        int widthPerColumn = newTableWidth / countNonReservedContentColumns;
        int modWidthPerColumn = newTableWidth % countNonReservedContentColumns;

        boolean modAdded = false;
        for (int i = 0; i < defs.length; i++) {
            ColumnDef columnDef = defs[i];
            if (columnDef.getWidth() > 0) {
                // MAYBE NEED TO SET FOR GRID WIDTH OF COLUMN
                continue;
            }

            if (!modAdded) {
                grid.getColumnModel().setColumnWidth(i, widthPerColumn + modWidthPerColumn);
                modAdded = true;
            } else {
                grid.getColumnModel().setColumnWidth(i, widthPerColumn);
            }
        }

        grid.setWidth(centerPanelWidth);

        if (renderered) {
            normalizeWidth(container, centerPanelWidth);
            UIUtils.adjustGridWidth(grid);
            grid.getView().refresh();
        }

    }

    private void normalizeWidth(Panel p, int centerPanelWidth) {
        final Element c1 = DOM.getChild(p.getElement(), 0);
        p.getEl().setStyle(PARAM_NAME, (centerPanelWidth + 1) + "px");
        final Element c2 = DOM.getChild(c1, 0);
        ExtElement el = new ExtElement(c2);
        el.setStyle(PARAM_NAME, centerPanelWidth + "px");
    }
}
