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
 * GridWindowResizeListenerImpl.java Date created: 02.04.2008
 * Last modified by: $Author: soyon.lim $ 
 * $Revision: 14286 $ $Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.gwt.client.grid;

import net.sf.infrared2.gwt.client.AbstractResizeListener;

/**
 * <b>GridWindowResizeListenerImpl</b><p>
 * Basic listener for the grids that have to resize to all center panel width value
 * when window (or center panel) changes width.
 * 
 * @author Sergey Evluhin
 */
public class GridWindowResizeListenerImpl extends AbstractResizeListener {

    /** Wrapper object that contains grid element. */
    private SimpleGridWrapper gridWrap;
    
    private ComplexGrid complexGrid;
     
 
    /**
     * 
     * @param wrap - wrapper object that contains grid element.
     */
    public GridWindowResizeListenerImpl(SimpleGridWrapper wrap){
        this.gridWrap = wrap;
    }
    
    /**
     * Default constructor.
     * 
     * @param panel - grid's panel that have to resize.
     */
    public GridWindowResizeListenerImpl(ComplexGrid gridsPanel) {
        this.complexGrid = gridsPanel;
    }

    /**
     *
     * Method resize panel width to all center panel width value.
     */
    public void resize(int width, int height) {
        if(complexGrid==null){
            gridWrap.adjustGridSize(true);
        }else{
            newStyle();
        }
    }

    private void newStyle(){
        complexGrid.initSizeByHeaderTable();
        complexGrid.normalizeWidth();
    }
    
//    /**
//     * Resize used before 
//     */
//    private void oldStyle(Panel p) {
//        final int w = UIUtils.getCenterPanelWidth() - UIUtils.SCROLL_WIDTH;
//        final Element c1 = DOM.getChild(p.getElement(), 0);
//        p.getEl().setStyle(paramName, (w + 1) + "px");
//        final Element c2 = DOM.getChild(c1, 0);
//        ExtElement el = new ExtElement(c2);
//        el.setStyle(paramName, w + "px");
//    }
//    
   
}
