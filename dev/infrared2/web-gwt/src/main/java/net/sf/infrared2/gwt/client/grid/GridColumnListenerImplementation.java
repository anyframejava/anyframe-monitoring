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

import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.event.GridColumnListenerAdapter;

/**
 * <b>GridColumnListenerImplementation</b><p>
 * Implementation of GridColumnListener for ComplexGrid that allows resize
 * columns for the both grids - header and content.
 * 
 * @author Sergey Evluhin
 */
class GridColumnListenerImplementation extends GridColumnListenerAdapter {
    private final ComplexGrid complexGrid;

    /**
     * Creates listener implementation.
     * 
     * @param complexGrid - complex grid, that have to support resizing columns.
     */
    public GridColumnListenerImplementation(ComplexGrid complexGrid) {
        super();
        this.complexGrid = complexGrid;
        if (!complexGrid.getRelation().isInited()) {
            complexGrid.getRelation().init();
        }

    }

    /**
     * {@inheritDoc}
     */
    public void onColumnResize(GridPanel grid, int colIndex, int newSize) {
        final GridPanel contentGrid = complexGrid.getContentGrid();
        final GridPanel headerGrid = complexGrid.getHeaderGrid();
        final Relation relation = complexGrid.getRelation();
        ColumnModel columnModel = contentGrid.getColumnModel();
        if (grid.getId().equals(headerGrid.getId())) {
            final int countColumnSpan = relation.getChildColumnsCount(colIndex);

            final int startIndex = relation.getContentColumnIndexByTopIndex(colIndex);

            final int widthPerColumn = newSize / countColumnSpan;

            final int modWidth = newSize % countColumnSpan;

            for (int i = 0; i < countColumnSpan; i++) {
                if (i == 0) {
                    columnModel.setColumnWidth(startIndex, widthPerColumn + modWidth);
                } else {
                    columnModel.setColumnWidth(startIndex + i, widthPerColumn);
                }
            }
        } else {
            final int topIndex = relation.getTopColumnIndexByContentIndex(colIndex);
            final int countColumnSpan = relation.getChildColumnsCount(topIndex);

            final int startIndex = relation.getContentColumnIndexByTopIndex(topIndex);
            int width = 0;

            for (int i = 0; i < countColumnSpan; i++) {
                width += columnModel.getColumnWidth(i + startIndex);
            }

            headerGrid.getColumnModel().setColumnWidth(topIndex, width);
        }

        noScroll(contentGrid.getView().getJsObj());
        complexGrid.normalizeWidth();

    }

    /**
     * Hack to hide grid scroll when grid width is higher then panel width.
     * 
     * @param view - grid view.
     */
    private native void noScroll(com.google.gwt.core.client.JavaScriptObject view)/*-{
        view.scroller.dom.style.overflow = 'hidden';
    }-*/;
}
