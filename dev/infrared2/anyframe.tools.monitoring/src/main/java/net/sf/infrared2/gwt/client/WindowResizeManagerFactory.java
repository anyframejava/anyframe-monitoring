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
 * WindowResizeManagerFactory.java Date created: 07.04.2008
 * Last modified by: $Author: soyon.lim $ 
 * $Revision: 14286 $ $Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.gwt.client;

import net.sf.infrared2.gwt.client.view.dialog.DialogHelper;

import com.google.gwt.core.client.GWT;

/**
 * <b>WindowResizeManagerFactory</b><p>
 * Represents factory for the managers of window resize listeners.
 * 
 * @author Sergey Evluhin
 */
public class WindowResizeManagerFactory {

    /** Type for manager of listeners of grids components. */
    public static final int GRIDS_MANAGER = 1;
    /** Type for manager of listeners of divided panels. */
    public static final int PANELS_MANAGER = 2;

    /** Manager of listeners of grids components. */
    private static final WindowResizeManager gridsManager = new WindowResizeManager();
    /** Manager of listeners of divided panels. */
    private static final WindowResizeManager panelsManager = new WindowResizeManager();

    /**
     * Returns the manager by type. All types are enumerated in the
     * WindowResizeManagerFactory within constants.
     * 
     * @param managerType - type of manager need to get.
     * @return - manager of window resize listeners.
     */
    public static WindowResizeManager getResizeManager(int managerType) {
        if (managerType == GRIDS_MANAGER) {
            return gridsManager;
        } else if (managerType == PANELS_MANAGER) {
            return panelsManager;
        }

        GWT.log("No managers found with such type value '" + managerType + "'", new RuntimeException());
        DialogHelper.showErrorWindow("No managers found with such type value '" + managerType + "'","", "");
        return null;
    }

}
