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
 * LimitRenderer.java Date created: 22.02.2008
 * Last modified by: $Author: soyon.lim $ 
 * $Revision: 14286 $ $Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.gwt.client.grid;

import com.gwtext.client.data.Record;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.grid.CellMetadata;

/**
 * <b>LimitRenderer</b><p>
 * Represents implementation of grid's column renderer which is highlighting
 * value of column by the red color in the case if this value more then LIMIT
 * value.
 * 
 * @author Sergey Evluhin
 */
public class LimitRenderer extends DoubleRenderer {
    /** This value used to render columns with red color if column value more
     than LIMIT. */
    private static int LIMIT = 200;

    /**
     * {@inheritDoc}
     */
    public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex,
                    int colNum, Store store) {
        final String render = super.render(value, cellMetadata, record, rowIndex, colNum, store);

        Float val = Float.valueOf(render);
        if (val.floatValue() > LIMIT) {
            return "<span style=\"color: red;\">" + val + "</span>";
        }
        return String.valueOf(val);
    }
}
