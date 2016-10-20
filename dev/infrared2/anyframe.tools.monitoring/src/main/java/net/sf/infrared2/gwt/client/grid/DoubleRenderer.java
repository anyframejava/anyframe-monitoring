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
 * DoubleRenderer.java Date created: 22.02.2008
 * Last modified by: $Author: soyon.lim $ 
 * $Revision: 14286 $ $Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.gwt.client.grid;

import com.gwtext.client.data.Record;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.Renderer;

/**
 * <b>DoubleRenderer</b><p>
 * Represents implementation of grid's column renderer which is applying style of
 * representation numbers of float types.
 * 
 * @author Sergey Evluhin
 */
public class DoubleRenderer implements Renderer {

    /**
     * {@inheritDoc}
     */
    public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex,
                    int colNum, Store store) {
        String s = value.toString();
        String res = s;
        int i = s.indexOf(".");
        if (i != -1) {

            if (s.substring(i, s.length()).length() >= 3) {
                res = s.substring(0, i) + s.substring(i, i + 3);
            }
        }
        return res;
    }

}
