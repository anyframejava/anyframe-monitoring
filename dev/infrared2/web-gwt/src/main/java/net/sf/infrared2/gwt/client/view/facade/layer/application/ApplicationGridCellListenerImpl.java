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
 * ApplicationGridCellListenerImpl.java Date created: 19.04.2008
 * Last modified by: $Author: soyon.lim $ 
 * $Revision: 14286 $ $Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.gwt.client.view.facade.layer.application;

import net.sf.infrared2.gwt.client.stack.StackPanelHolder;
import net.sf.infrared2.gwt.client.to.ApplicationEntryTO;
import net.sf.infrared2.gwt.client.to.InstanceEntryTO;
import net.sf.infrared2.gwt.client.to.NavigatorEntryTO;

import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.event.GridCellListenerAdapter;

/**
 * Listener for the application layer grids, that listen for click on cell that
 * contains some layer name.
 * 
 * @author Sergey Evluhin
 */
class ApplicationGridCellListenerImpl extends GridCellListenerAdapter {
    /** Index of column which need to listen. */
    private int index;
    
    /** Data index name for the column (without digit in the end).*/
    private String colDataIndex;

    /**
     * Create listener.
     * @param index - index of column which need to listen.
     * @param colDataIndex - data index name for the column (without digit in the end).
     */
    ApplicationGridCellListenerImpl(int index, String colDataIndex) {
        this.index = index;
        this.colDataIndex = colDataIndex.toLowerCase();
    }

    /**
     * 
     * {@inheritDoc}
     */
    public void onCellClick(GridPanel grid, int rowIndex, int colindex, EventObject e) {

        super.onCellClick(grid, rowIndex, colindex, e);
        if (colindex == index) {
            String text = grid.getStore().getAt(rowIndex).getAsString(colDataIndex + index);
            final NavigatorEntryTO clone = cloneNavigatorEntryTO(StackPanelHolder.getInstance()
                            .getSelectedLink().getNavigatorEntryTO());
            clone.setTitle(text);
            StackPanelHolder.getInstance().selectLink(clone);
        }
    }

    /**
     * Clone the NavigatorEntryTO object.
     * @param e - source object.
     * @return copy of source object.
     */
    NavigatorEntryTO cloneNavigatorEntryTO(NavigatorEntryTO e) {
        NavigatorEntryTO t = new NavigatorEntryTO();
        t.setModuleType(e.getModuleType());
        t.setTitle(e.getTitle());
        if (e.getApplications() != null) {
            final ApplicationEntryTO applicationEntryTO = new ApplicationEntryTO();
            if (e.getApplications().getApplicationTitle() != null) {
                applicationEntryTO.setApplicationTitle(e.getApplications().getApplicationTitle());
            }
            if (e.getApplications().getInstanceList() != null
                            && e.getApplications().getInstanceList().length > 0) {

                final InstanceEntryTO instanceEntryTO = new InstanceEntryTO(e.getApplications()
                                .getInstanceList()[0].getName());
                final InstanceEntryTO[] instanceEntryTOs = new InstanceEntryTO[] { instanceEntryTO };
                applicationEntryTO.setInstanceList(instanceEntryTOs);
            }
            t.setApplications(applicationEntryTO);
        }
        return t;
    }
}
