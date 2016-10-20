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


import junit.framework.TestCase;

import java.util.List;

public class GridGenerationConfigTest extends TestCase {

    public void testCompile() {
        String[] mp = new String[]{"H1: 1,2,3", "H2:4,5"};

        final GridGenerationConfig conf = GridGenerationConfig.compile(mp);
        final List columnDefs = conf.getColumnDefs();
        final Relation relation = conf.getRelation();

        assertNotNull(conf);
        assertNotNull(columnDefs);
        assertNotNull(relation);

        if (!relation.isInited()) {
            relation.init();
        }

        assertEquals(5, columnDefs.size());

        for (int i = 0; i < columnDefs.size(); i++) {
            ColumnDef def = (ColumnDef) columnDefs.get(i);

            assertEquals("" + (i + 1), def.getTitle());
        }

        final int childColumnsCount0 = relation.getChildColumnsCount(0);

        assertEquals(3, childColumnsCount0);

        final int childColumnsCount1 = relation.getChildColumnsCount(1);

        assertEquals(2, childColumnsCount1);

        final int contentColumnIndexByTopIndex1 = relation.getContentColumnIndexByTopIndex(1);

        assertEquals(3, contentColumnIndexByTopIndex1);


        final int topColumnIndexByContentIndex2 = relation.getTopColumnIndexByContentIndex(2);


        assertEquals(0, topColumnIndexByContentIndex2);


        final int topColumnIndexByContentIndex4 = relation.getTopColumnIndexByContentIndex(4);


        assertEquals(1, topColumnIndexByContentIndex4);

        final List mappings = relation.getMappings();


        assertNotNull(mappings);

        assertEquals(2, mappings.size());


        final ColumnMapping map0 = (ColumnMapping) mappings.get(0);

        assertNotNull(map0);

        assertEquals(3, map0.getCountColumnSpan());

        assertEquals(0, map0.getTopRowColumnIndex());

        assertEquals("H1", map0.getTopRowColumnTitle());


        final ColumnMapping map1 = (ColumnMapping) mappings.get(1);

        assertNotNull(map1);

        assertEquals(2, map1.getCountColumnSpan());

        assertEquals(1, map1.getTopRowColumnIndex());

        assertEquals("H2", map1.getTopRowColumnTitle());

    }

}
