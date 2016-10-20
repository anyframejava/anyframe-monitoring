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
 * ClickableRenderer.java Date created: 30.01.2008
 * Last modified by: $Author: soyon.lim $ 
 * $Revision: 14286 $ $Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.gwt.client.grid;

import java.util.Date;

import com.gwtext.client.data.Record;
import com.gwtext.client.data.Store;
import com.gwtext.client.util.DateUtil;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.Renderer;

/**
 * <b>DateRenderer</b><p>
 * Renderer implementation for the columns with data of the date's type. This class
 * render the date to special format.
 * 
 * @author Sergey Evluhin
 */
public class DateRenderer implements Renderer {
    /** Format to date is converted. */
    private static final String DATA_RENDERED_FORMAT = "m/d/Y H:i:s a";

    /**
     * {@inheritDoc}
     */
    public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex,
                    int colNum, Store store) {
        Date date = (Date) value;
        return DateUtil.format(date, DATA_RENDERED_FORMAT);
    }

}
