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
 * AbstractResizeListener.java Date created: 07.04.2008
 * Last modified by: $Author: soyon.lim $ 
 * $Revision: 14286 $ $Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.gwt.client;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.WindowResizeListener;

/**
 * <b>AbstractResizeListener</b><p>
 * Abstract listener for WindowResize action. It needs to increase process of
 * resize actions.
 * 
 * @author Sergey Evluhin
 */
public abstract class AbstractResizeListener implements WindowResizeListener {
    /** Delay between user action and resize task. */
    private static final int DELAY = 100;

    /** Currently active timer object. */
    private Timer timer;

    /**
     * Resize action with schedule.
     */
    public void onWindowResized(int width, int height) {
        if (timer == null) {
            timer = getTimer(width, height);
        } else {
            timer.cancel();
            timer = null;
            timer = getTimer(width, height);
        }

        timer.schedule(DELAY);
    }

    /**
     * Create the Timer object that call "resize" method.
     * 
     * @param w - new width.
     * @param h - new height.
     * @return Timer object that call "resize" method.
     */
    private Timer getTimer(final int w, final int h) {
        return new Timer() {
            public void run() {
                resize(w, h);
            }
        };
    }

    /**
     * Fires the resize action without schedule.
     * 
     * @param w - new width.
     * @param h - new height.
     */
    public abstract void resize(int w, int h);
}
