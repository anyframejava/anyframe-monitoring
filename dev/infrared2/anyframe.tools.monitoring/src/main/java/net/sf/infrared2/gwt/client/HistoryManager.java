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
 * HistoryManager.java Date created: 05.03.2008
 * Last modified by: $Author: soyon.lim $ 
 * $Revision: 14286 $ $Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.gwt.client;

import java.util.HashMap;
import java.util.Map;

import net.sf.infrared2.gwt.client.stack.StackPanelHolder;
import net.sf.infrared2.gwt.client.view.dialog.DialogHelper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.HistoryListener;

/**
 * <b>HistoryManager</b><p>
 * This class contains methods for manipulations with browser history listener.
 * 
 * @author Sergey Evluhin
 */
public class HistoryManager {
    /** Instance of history listener. */
    private static final IAHistoryListenerImpl historyImpl = new IAHistoryListenerImpl();
    /** Indicates when listener for history already added. */
    private static boolean isAdded;

    private static Integer key = new Integer(0);
    private static Map relation = new HashMap();
    
    /**
     * Removes history listener.
     */
    public static void removeListener() {
        if (!isAdded)
            return;
        historyImpl.setValidAction(false);
        History.removeHistoryListener(historyImpl);
        isAdded = false;
    }

    /**
     * Adds history listener.
     */
    public static void addListener() {
        if (isAdded)
            return;
        historyImpl.setValidAction(true);
        History.addHistoryListener(historyImpl);
        isAdded = true;
    }

    /**
     * Adds to history new item.
     * 
     * @param historyTokenFromLink - item of history.
     */
    public static void newItem(String historyToken) {
        try {
            relation.put(key, historyToken);
            removeListener();
            History.newItem(key.toString());
            addListener();
            key = new Integer(key.intValue()+1);
        } catch (Throwable e) {
            reload();
        }
    }

    private static native void reload() /*-{
           $wnd.document.location.reload();
    }-*/;

    /**
     * Class represents basic implementation of history support for application.
     * 
     * @author Sergey Evluhin
     */
    private static class IAHistoryListenerImpl implements HistoryListener {
        /** Indicates when calling of method "onHistoryChanged" is valid. */
        private boolean validAction;

        public void setValidAction(boolean validAction) {
            this.validAction = validAction;
        }

        /**
         * Fires when user clicks by hyperlink or forward or back browser's
         * buttons.
         */
        public void onHistoryChanged(String historyToken) {
            if (historyToken != null && historyToken.length() > 0) {
                if (validAction) {
                    try {
                        int parseInt = Integer.parseInt(historyToken);
                        String token = (String)relation.get(new Integer(parseInt));
                        
                        StackPanelHolder.getInstance().restoreHistory(token);
                    } catch (NumberFormatException e) {
                        GWT.log("History token-key can not be parsed", e);
                        DialogHelper.showErrorWindow("NumberFormatException in HistoryManager.onHistoryChanged(String historyToken)", e.getMessage(), "");
                    }
                }

            }
        }

    }
}
