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
 * SplitPanelsResizeListener.java Date created: 07.04.2008
 * Last modified by: $Author: soyon.lim $ 
 * $Revision: 14286 $ $Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.gwt.client.view.facade;

import net.sf.infrared2.gwt.client.AbstractResizeListener;
import net.sf.infrared2.gwt.client.utils.UIUtils;

import com.gwtext.client.widgets.Panel;

/**
 * <b>SplitPanelsResizeListener</b><p>
 * This class resize split panel to 50% on 50% size of center panel heights. It
 * acts when window size is changed.
 * 
 * @author Sergey Evluhin
 */
public class SplitPanelsResizeListener extends AbstractResizeListener {

    /** Spin panel view. */
    private Panel panel;

    /**
     * Default constructor.
     */
    public SplitPanelsResizeListener(Panel panel) {
        this.panel = panel;
    }

    /**
     * {@inheritDoc}
     */
    public void resize(int w, int h) {
        if (panel.getBody() != null) {
            int all = UIUtils.getCenterPanelHeight();
            panel.getBody().setHeight(all / 2 + all % 2, false);
            ApplicationViewFacade.getCenterPanel().doLayout();
        }
        ApplicationViewFacade.getApplicationLayout().doLayout();
    }

}
