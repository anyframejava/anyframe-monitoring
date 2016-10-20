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
package net.sf.infrared2.gwt.client.to.comparator;

import java.io.Serializable;
import java.util.Comparator;

import net.sf.infrared2.gwt.client.view.facade.ColoredRows;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * <b>ByCountComparator</b><p>
 * Implementation of Comparator, compare ApplicationRowTO objects by totalCount
 * value.
 * 
 * @author Sergey Evluhin
 */
public class ByCountComparator implements Comparator, Serializable, IsSerializable {
    private static final long serialVersionUID = -1228625000705850291L;
    private String otherOperationColor;
    
    
    /**
     * 
     */
    public ByCountComparator() {
    }


    /**
     * @param otherOperationColor
     */
    public ByCountComparator(String otherOperationColor) {
        this.otherOperationColor = otherOperationColor;
    }


    /**
     * {@inheritDoc}
     */
    public int compare(Object o1, Object o2) {
        ColoredRows to1 = (ColoredRows) o1;
        ColoredRows to2 = (ColoredRows) o2;
        if (to1.getCount() > to2.getCount())
            return -1;
        if (to1.getCount() < to2.getCount())
            return 1;
        
        if(this.otherOperationColor!=null){
            if(this.otherOperationColor.equals(to1.getCountColor())){
                return 1;
            }
            if(this.otherOperationColor.equals(to2.getCountColor())){
                return -1;
            }
        }
        return 0;
    }
}
