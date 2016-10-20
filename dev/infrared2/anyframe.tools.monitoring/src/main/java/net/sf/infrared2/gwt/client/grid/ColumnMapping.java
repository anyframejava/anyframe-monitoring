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

/**
 * <b>ColumnMapping</b><p>
 * Column mapping class used to linking headers of two tables. For example, top
 * table header (name it "Header 1") contain three bottom's table headers. You
 * need to set topRowColumnTitle = "Header 1", topRowColumnIndex=0 (start
 * indexes from 0) and countColumnSpan=3. Now your complex table will contains
 * one top header with title "Header 1" and three columns in bottom table.
 * 
 * @author Sergey Evluhin
 * @see net.sf.infrared2.gwt.client.grid.Relation
 */
public class ColumnMapping implements Comparable {
    /** Column index of top table header. */
    private int topRowColumnIndex;
    /** Length of span columns of bottom table. (as HTML "colspan" attribute). */
    private int countColumnSpan;
    /** Title of the top table header. */
    private String topRowColumnTitle;
    /** Width of header column. */
    private int topRowColumnWidth;

    /**
     * Creates column mappings.
     * 
     * @param title - title of top table header.
     * @param topRowColumnIndex - length of span columns of bottom table. (as
     *            HTML "colspan" attribute).
     * @param countColumnSpan - title of top table header.
     */
    public ColumnMapping(String title, int topRowColumnIndex, int countColumnSpan) {
        this.topRowColumnIndex = topRowColumnIndex;
        this.countColumnSpan = countColumnSpan;
        this.topRowColumnTitle = title;
    }

    /**
     * Creates column mappings.
     * 
     * @param title - title of top table header.
     * @param topRowColumnIndex - length of span columns of bottom table. (as
     *            HTML "colspan" attribute).
     * @param countColumnSpan - title of top table header.
     * @param width - width of header column.
     */
    public ColumnMapping(String title, int topRowColumnIndex, int countColumnSpan, int width) {
        this(title, topRowColumnIndex, countColumnSpan);
        this.topRowColumnWidth = width;
    }

    /**
     * @return title of the top table header.
     */
    public String getTopRowColumnTitle() {
        return topRowColumnTitle;
    }

    /**
     * @param topRowColumnTitle - title of the top table header.
     */
    public void setTopRowColumnTitle(String topRowColumnTitle) {
        this.topRowColumnTitle = topRowColumnTitle;
    }

    /**
     * @return column index of top table header.
     */
    public int getTopRowColumnIndex() {
        return topRowColumnIndex;
    }

    /**
     * @param topRowColumnIndex - column index of top table header.
     */
    public void setTopRowColumnIndex(int topRowColumnIndex) {
        this.topRowColumnIndex = topRowColumnIndex;
    }

    /**
     * @return length of span columns of bottom table. (as HTML "colspan"
     *         attribute).
     */
    public int getCountColumnSpan() {
        return countColumnSpan;
    }

    /**
     * @param countColumnSpan - length of span columns of bottom table. (as HTML
     *            "colspan" attribute).
     */
    public void setCountColumnSpan(int countColumnSpan) {
        this.countColumnSpan = countColumnSpan;
    }

    /**
     * @return width of header column.
     */
    public int getTopRowColumnWidth() {
        return topRowColumnWidth;
    }

    /**
     * @param topRowColumnWidth - width of header column.
     */
    public void setTopRowColumnWidth(int topRowColumnWidth) {
        this.topRowColumnWidth = topRowColumnWidth;
    }

    /**
     * {@inheritDoc}
     */
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + topRowColumnIndex;
        return result;
    }

    /**
     * Return true if topRowColumnIndex's are equals.
     */
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof ColumnMapping))
            return false;
        final ColumnMapping other = (ColumnMapping) obj;
        if (topRowColumnIndex != other.topRowColumnIndex)
            return false;
        return true;
    }

    /**
     * Compare two objects by topRowColumnIndex's.
     */
    public int compareTo(Object arg0) {

        if (!(arg0 instanceof ColumnMapping))
            return 0;

        if (arg0 instanceof ColumnMapping) {
            return new Integer(topRowColumnIndex).compareTo(new Integer(((ColumnMapping) arg0)
                            .getTopRowColumnIndex()));
        }
        return 0;
    }

}
