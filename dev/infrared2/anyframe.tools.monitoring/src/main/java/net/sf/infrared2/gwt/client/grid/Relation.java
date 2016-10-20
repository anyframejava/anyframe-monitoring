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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * <b>Relation</b><p>
 * Relation class represents relations between header columns indexes of header
 * and content tables. It is targeting to maximum performance.
 * 
 * @author Sergey Evluhin
 * @see net.sf.infrared2.gwt.client.grid.ColumnMapping
 */
public class Relation implements Serializable {

    private static final long serialVersionUID = -5858503871998361737L;
    /** Indicates when relations is initialized. */
    private boolean inited;

    /** List of ColumnMapping beans. */
    private List mappings;
    /**
     * Relation between top table header column index and start index of content
     * table column header. For example, table has three top columns and four
     * content columns, where first top columns has one child column and second
     * top column has two child columns, third top column has a one child
     * column. key=0,value=0; key=1,value=1; key=2,value=3.
     */
    private Map relationTopIndexes;

    /**
     * Relation between content column indexes and indexes of top table header
     * column. For example, table has three top columns and four content
     * columns, where first top columns has one child column and second top
     * column has two child columns, third top column has a one child column.
     * key=0,value=0; key=1,value=1; key=2,value=1; key=3,value=2.
     */
    private Map relationContentIndexes;

    /**
     * Default constructor.
     */
    public Relation() {
        super();
    }

    /**
     * Creates relation between grids in ComplexGrid with such columns mappings.
     * 
     * @param mappings - array of columns mappings.
     */
    public Relation(ColumnMapping[] mappings) {
        this.mappings = Arrays.asList(mappings);
        init();
    }

    /**
     * Creates relation between grids in ComplexGrid with such columns mappings.
     * 
     * @param mappings - list of columns mappings.
     */
    public Relation(List mappings) {
        this.mappings = mappings;
        init();
    }

    /**
     * Initialize properties of this class. Need to call only when used default
     * constructor and addRelation method.
     */
    public void init() {

        if (this.mappings == null || this.mappings.size() == 0) {
            throw new RuntimeException("ColumnMappings list is empty or null.");
        }
        this.relationContentIndexes = new HashMap();
        this.relationTopIndexes = new HashMap();
        this.generateIndexes();
        this.inited = true;
    }

    /**
     * Fill maps with data.
     */
    private void generateIndexes() {
        int i = 0;
        int ind = 0;

        Collections.sort(mappings);
        for (Iterator iterator = mappings.iterator(); iterator.hasNext();) {
            ColumnMapping mapping = (ColumnMapping) iterator.next();
            Integer topIndex = new Integer(mapping.getTopRowColumnIndex());
            int countColumnSpan = mapping.getCountColumnSpan();
            relationTopIndexes.put(topIndex, new Integer(i));
            i += countColumnSpan;
            for (; ind < i; ind++) {
                relationContentIndexes.put(new Integer(ind), topIndex);
            }
        }

    }

    /**
     * Get index of top column by index of child content column index.
     * 
     * @param index - index of content column.
     * @return index of top column.
     */
    public int getTopColumnIndexByContentIndex(int index) {
        return ((Integer) relationContentIndexes.get(new Integer(index))).intValue();
    }

    /**
     * Get first (start) index of content table column that is covered by top
     * table column.
     * 
     * @param index - index of top table column.
     * @return start index of the first content table column that is covered by
     *         top table column.
     */
    public int getContentColumnIndexByTopIndex(int index) {
        return ((Integer) relationTopIndexes.get(new Integer(index))).intValue();
    }

    /**
     * Getting count of column span. It means, get count of child (content
     * table) columns by top table column index.
     * 
     * @param topIndex - index of the top table column.
     * @return count of child columns.
     */
    public int getChildColumnsCount(int topIndex) {
        ColumnMapping mapping = (ColumnMapping) mappings.get(topIndex);
        if (topIndex != mapping.getTopRowColumnIndex()) {
            throw new RuntimeException("Invalid column mappings.");
        }
        return mapping.getCountColumnSpan();
    }

    /**
     * Add relation between columns. WARNING: after all relations added you must
     * call the "init" method to fill the maps.
     * 
     * @param mapping - mapping bean
     */
    public void addRelation(ColumnMapping mapping) {
        this.inited = false;
        if (this.mappings == null) {
            this.mappings = new ArrayList();
        }
        if (this.mappings.contains(mapping)) {
            throw new DuplicateColumnDefinitionException("Column with index "
                            + mapping.getTopRowColumnIndex() + " already exists.");
        }

        this.mappings.add(mapping);
    }

    /**
     * Add relation to the exists list of it. WARNING: after all relations added
     * you must call the "init" method to fill the maps.
     * 
     * @param title - title of top table column header.
     * @param topIndex - index of top table column.
     * @param countColSpan - count child columns.
     */
    public void addRelation(String title, int topIndex, int countColSpan) {
        this.addRelation(title, topIndex, countColSpan, 0);
    }

    /**
     * Add relation to the exists list of it. WARNING: after all relations added
     * you must call the "init" method to fill the maps.
     * 
     * @param title - title of top table column header.
     * @param topIndex - index of top table column.
     * @param countColSpan - count child columns.
     * @param width - width of header column
     */
    public void addRelation(String title, int topIndex, int countColSpan, int width) {
        ColumnMapping mapping = new ColumnMapping(title, topIndex, countColSpan, width);
        this.addRelation(mapping);
    }

    /**
     * Return mappings.
     * 
     * @return all mappings between top table columns and content table columns.
     */
    public List getMappings() {
        return this.mappings;
    }

    /**
     * Indicates when "init" method was called and maps were filled.
     * 
     * @return true if have been called, otherwise false.
     */
    public boolean isInited() {
        return inited;
    }
}
