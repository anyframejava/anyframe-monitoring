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
import java.util.List;

/**
 * <b>GridGenerationConfig</b><p>
 * Configuration object that is taking part in grid's building process. Contains
 * relations between grids in ComplexGrid object and column definitions for
 * content grid.
 * 
 * @author Sergey Evluhin
 */
public class GridGenerationConfig implements Serializable {
    private static final long serialVersionUID = -1673127427782957904L;

    /** Relations between grids in ComplexGrid. */
    private Relation relation;

    /** List of columns definitions. */
    private List columnDefs;

    /**
     * Default constructor.
     */
    public GridGenerationConfig() {
        super();
    }

    /**
     * Creates configuration object with columns definitions.
     * 
     * @param columnDefs - list of columns definitions.
     */
    public GridGenerationConfig(List columnDefs) {
        this.columnDefs = columnDefs;
    }

    /**
     * Creates configuration object with columns definitions and relations
     * between grids in ComplexGrid.
     * 
     * @param columnDefs - list of columns definitions.
     * @param rel - relation object, that presents relations between grids in
     *            ComplexGrid.
     */
    public GridGenerationConfig(List columnDefs, Relation rel) {
        this.columnDefs = columnDefs;
        this.relation = rel;
    }

    /**
     * @return list of columns definitions.
     */
    public List getColumnDefs() {
        return columnDefs;
    }

    /**
     * @param columnDefs - list of columns definitions.
     */
    public void setColumnDefs(List columnDefs) {
        this.columnDefs = columnDefs;
    }

    /**
     * @return relation object, that presents relations between grids in
     *         ComplexGrid.
     */
    public Relation getRelation() {
        return relation;
    }

    /**
     * @param relation - relation object, that presents relations between grids
     *            in ComplexGrid.
     */
    public void setRelation(Relation relation) {
        this.relation = relation;
    }

    /**
     * Convert string array like String[]{"Header 1 : Company", "Header 2 :
     * Price, Change, % Change"} to GridGenerationConfig object. Use it only for
     * creating complex grid.
     * 
     * @param metaData - array of meta data.
     * @return GridGenerationConfig object.
     */
    public static GridGenerationConfig compile(String[] metaData) {
        if (metaData == null || metaData.length == 0)
            return null;

        List colDefs = new ArrayList();
        Relation rel = new Relation();
        for (int i = 0; i < metaData.length; i++) {
            // 0-title,1-list of child columns
            String[] split = metaData[i].split(":");

            if (split.length != 2) {
                throw new IllegalArgumentException("Relation with top index " + i
                                + " is incorrect. Separator char ':' not found");
            }
            // split child meta
            String childrenString = split[1].trim();
            if ("".equals(childrenString)) {

            }
            String[] children = childrenString.split(",");

            rel.addRelation(split[0].trim(), i, children.length);
            for (int j = 0; j < children.length; j++) {
                colDefs.add(new ColumnDef(children[j]));
            }
        }

        return new GridGenerationConfig(colDefs, rel);
    }

}
