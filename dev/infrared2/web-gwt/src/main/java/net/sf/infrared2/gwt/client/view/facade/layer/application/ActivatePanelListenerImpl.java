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
 * ActivatePanelListenerImpl.java Date created: 18.04.2008
 * Last modified by: $Author: soyon.lim $ 
 * $Revision: 14286 $ $Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.gwt.client.view.facade.layer.application;

import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.event.PanelListenerAdapter;
import com.gwtext.client.widgets.grid.GridPanel;

/**
 * Listener for the image tab panels.
 * @author Sergey Evluhin
 */
public class ActivatePanelListenerImpl  extends PanelListenerAdapter{
    /** Flag indicates which value for parameter <b>isByTimeGraph</b> will be set. */
    private boolean flag;
    
    private GridPanel grid;
    /** Define is by time graph mode */
    private static boolean isByTimeGraph = true;
    
    /**
     * Creates listener.
     * @param grid - grid, that view will refresh.
     * @param byTimeGraph - value that will be set to <b>isByTimeGraph</b> when onActivate event fired.
     */
    ActivatePanelListenerImpl(GridPanel grid, boolean byTimeGraph){
        this.grid = grid;
        this.flag = byTimeGraph;
    }
    
    /**
     * Creates listener with predefined value for <b>isByTimeGraph</b> field.
     * @param grid - grid, that view will refresh.
     * @param byTimeGraph - value that will be set to <b>isByTimeGraph</b> when onActivate event fired.
     */
    ActivatePanelListenerImpl(GridPanel grid, boolean byTimeGraph, boolean defaultValue){
        this.grid = grid;
        this.flag = byTimeGraph;
        isByTimeGraph = defaultValue;
    }
    
    /**
     * {@inheritDoc}
     */
    public void onActivate(Panel panel) {
        isByTimeGraph = flag;
        grid.getView().refresh();
    }

    /**
     * 
     * @return indicates active tab. 
     */
    public static boolean isByTimeGraph() {
        return isByTimeGraph;
    }
}
