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
 * ColorRenderer.java Date created: 19.04.2008
 * Last modified by: $Author: soyon.lim $ 
 * $Revision: 14286 $ $Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.gwt.client.view.facade.layer.application;

import com.gwtext.client.data.Record;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.Renderer;

/**
 * Renderer for the colored column.
 * 
 * @author Sergey Evluhin
 */
public class ColorRenderer implements Renderer {

    /**
     * {@inheritDoc}
     */

    public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex,
                    int colNum, Store store) {
        String v = value.toString();
        int end = v.indexOf('|');
        String byTimeColor = v.substring(0, end);
        String byCountColor = v.substring(end + 1);
        String res;
        if (ActivatePanelListenerImpl.isByTimeGraph()) {
            res = "<div class=\"colored-panel\" style=\"background-color: " + byTimeColor
                            + ";\">&nbsp;</div>";
        } else {
            res = "<div class=\"colored-panel\" style=\"background-color: " + byCountColor
                            + ";\">&nbsp;</div>";
        }

        return res;

    }

}
