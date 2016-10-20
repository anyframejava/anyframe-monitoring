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
 * WindowResizeManager.java Date created: 07.04.2008
 * Last modified by: $Author: soyon.lim $ 
 * $Revision: 14286 $ $Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.gwt.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.Window;

/**
 * <b>WindowResizeManager<b><p>
 * This class represents the manager of WindowResizeListener's. It contains only
 * currently active listeners of the components.
 * 
 * @author Sergey Evluhin
 */
public class WindowResizeManager {
    /** Indicates when all currently shown components listeners is added. */
    private boolean finished;

    /** List of all currently shown components listeners. */
    private List listeners;

    /**
     * Default constructor.
     */
    WindowResizeManager() {
        listeners = new ArrayList();
    }

    /**
     * Adds new resize listener to Window object.
     * 
     * @param listener - listener object.
     */
    public void addListener(AbstractResizeListener listener) {
        if (finished) {
            cleanListeners();
        }

        listeners.add(listener);
        Window.addWindowResizeListener(listener);
    }

    /**
     * Removes old listeners from Window objects.
     */
    private void cleanListeners() {
        for (int i = 0; i < listeners.size(); i++) {
            AbstractResizeListener listener = (AbstractResizeListener) listeners.get(i);
            Window.removeWindowResizeListener(listener);
        }
        listeners.clear();
        finished = false;
    }

    /**
     * Indicates that all listeners for this view is already added. After call
     * of this method, next call of addListener(X) will removes all listeners
     * except X.
     */
    public void complete() {
        finished = true;
    }

    /**
     * Fires the resize event for the all listeners with timer scheduling.
     */
    public void fireResize() {
        for (int i = 0; i < listeners.size(); i++) {
            AbstractResizeListener listener = (AbstractResizeListener) listeners.get(i);
            listener.onWindowResized(0, 0);
        }
    }
}
