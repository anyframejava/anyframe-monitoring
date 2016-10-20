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
 * AbstractApplicationLayer.java Date created: 18.04.2008
 * Last modified by: $Author: soyon.lim $ 
 * $Revision: 14286 $ $Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.gwt.client.view.facade.layer.application;

import net.sf.infrared2.gwt.client.Constants;
import net.sf.infrared2.gwt.client.WindowResizeManagerFactory;
import net.sf.infrared2.gwt.client.grid.ClickableRenderer;
import net.sf.infrared2.gwt.client.to.application.ApplicationViewTO;
import net.sf.infrared2.gwt.client.utils.UIUtils;
import net.sf.infrared2.gwt.client.view.facade.ApplicationViewFacade;

import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.TabPanel;
import com.gwtext.client.widgets.grid.GridPanel;

/**
 * Super class for application layers views.
 * 
 * @author Sergey Evluhin
 */
public abstract class AbstractApplicationLayer {

    /**
     * Represents implementation of grid's column renderer which is applies
     * 'clickable' style to the column's text.
     */
    protected ClickableRenderer clickableRenderer = new ClickableRenderer();

    /** Transfer object for Application view. */
    protected ApplicationViewTO to;

    /** Application data grid. */
    protected GridPanel grid;

    protected Panel center;

    protected boolean rendered;

    protected TabPanel bottom;
    
    protected Panel gridContainer; 

    /**
     * Create Application view for concrete module.
     * 
     * @param to - transfer object for view.
     */
    public static void buildView(ApplicationViewTO to) {
        AbstractApplicationLayer view;
        if (Constants.MODULE_LAST_INV.equals(to.getModuleType())) {
            view = LastInvocationApplicationLayer.getInstance(to);
        } else {
            view = ApplicationLayerView.getInstance(to);
        }

        if (view.isRendered()) {
            view.updateView(to);
            view.getGridContainer().getBody().setHeight(UIUtils.calculateGridHeight(view.getGrid()), false);
            UIUtils.updateGridHeight(view.getGrid());
        } else {
            view.createView();
        }

        WindowResizeManagerFactory.getResizeManager(WindowResizeManagerFactory.PANELS_MANAGER).fireResize();

        ApplicationViewFacade.addCenterPanel(view.getCenter());
        view.updateImageURLs();
        UIUtils.updateGridHeight(view.getGrid());
    }

    /**
     * Default constructor.
     * @param to - transfer object for this view.
     */
    AbstractApplicationLayer(ApplicationViewTO to) {
        this.to = to;
    }

    /**
     * 
     * @return panel which contains all components for this view.
     */
    public Panel getCenter() {
        return center;
    }

    /**
     * 
     * @return true if this view already been shown.
     */
    public boolean isRendered() {
        return rendered;
    }

    /**
     * 
     * @return tab panel from SOUTH region.
     */
    public TabPanel getBottom() {
        return bottom;
    }
    
    /**
     * 
     * @return summary grid component.
     */
    public GridPanel getGrid(){
        return grid;
    }

    /**
     * Start the process of building UI.
     */
    public abstract void createView();

    /**
     * Update view for this layer.
     * @param to - new transfer object for this view.
     */
    public abstract void updateView(ApplicationViewTO to);

    public Panel getGridContainer() {
        return gridContainer;
    }
    
    public abstract void updateImageURLs();

}
