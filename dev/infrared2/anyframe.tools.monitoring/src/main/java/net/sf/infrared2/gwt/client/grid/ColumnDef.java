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

import java.io.Serializable;

/**
 * <b>ColumnDef</b><p>
 * This class represents simple column definition. It needs to create Grids by
 * TableFactory.
 * 
 * @author Sergey Evluhin
 */
public class ColumnDef implements Serializable {
    private static final long serialVersionUID = 333965419366348962L;

    /** Type for boolean value of column. */
    public static final int BOOLEAN = 0;

    /** Type for date value of column. */
    public static final int DATE = 1;

    /** Type for float value of column. */
    public static final int FLOAT = 2;

    /** Type for integer value of column. */
    public static final int INTEGER = 3;

    /** Type for string value of column. */
    public static final int STRING = 4;

    /** Title of the header column. */
    private String title;

    /** Column width. */
    private int width;

    /** Type of column data. */
    private int type;

    /** Indicates when column has sorting capabilities. */
    private boolean sortable;

    /**
     * Default constructor.
     */
    public ColumnDef() {

    }

    /**
     * Create column definition with default type STRING.
     * 
     * @param title - title of the header column.
     */
    public ColumnDef(String title) {
        this(title, 0, STRING, true);

    }

    /**
     * Create column definition with default type STRING.
     * 
     * @param title - title of the header column.
     * @param width - column width.
     */
    public ColumnDef(String title, int width) {
        this(title, width, STRING, true);
    }

    /**
     * Create column definition with default type STRING.
     * 
     * @param title - title of the header column.
     * @param width - column width.
     * @param sortable - available or not the sorting capability.
     */
    public ColumnDef(String title, int width, boolean sortable) {
        this(title, width, STRING, sortable);
    }

    /**
     * Create column definition. List of variables types:
     * <ul>
     * <li> ColumnDef.Boolean </li>
     * <li> ColumnDef.DATE </li>
     * <li> ColumnDef.FLOAT </li>
     * <li> ColumnDef.INTEGER </li>
     * <li> ColumnDef.STRING </li>
     * </ul>
     * 
     * @param title - title of the header column.
     * @param width - column width.
     * @param type - type of variable in column.
     */
    public ColumnDef(String title, int width, int type) {
        this(title, width, type, true);
    }

    /**
     * Create column definition. List of variables types:
     * <ul>
     * <li> ColumnDef.Boolean </li>
     * <li> ColumnDef.DATE </li>
     * <li> ColumnDef.FLOAT </li>
     * <li> ColumnDef.INTEGER </li>
     * <li> ColumnDef.STRING </li>
     * </ul>
     * 
     * @param title - title of the header column.
     * @param width - column width.
     * @param type - type of variable in column.
     * @param sortable - available or not the sorting capability.
     */
    public ColumnDef(String title, int width, int type, boolean sortable) {
        this.title = title;
        this.width = width;
        this.type = type;
        this.sortable = sortable;
    }

    /**
     * @return header's title for the grid.
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title - header's title for the grid.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return column's width.
     */
    public int getWidth() {
        return width;
    }

    /**
     * @param width - column's width.
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * @return type of the column's.
     */
    public int getType() {
        return type;
    }

    /**
     * @param type - type of the column's.
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * 
     * @return true - if sorting is enabled.
     */
    public boolean isSortable() {
        return sortable;
    }

    /**
     * 
     * @param sortable - true - if need to enable sorting.
     */
    public void setSortable(boolean sortable) {
        this.sortable = sortable;
    }
}
