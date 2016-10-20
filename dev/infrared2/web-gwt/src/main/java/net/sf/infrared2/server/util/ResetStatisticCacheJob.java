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
package net.sf.infrared2.server.util;

import java.util.Timer;
import java.util.TimerTask;

import net.sf.infrared2.server.util.DataFetchUtil;

/**
 * The Class ResetStatisticCacheJob.
 */
public class ResetStatisticCacheJob {

    /** The timer. */
    private static Timer timer = new Timer();

    /** The reset interval. */
    private static Long resetIntervalSec = null;

    /**
     * Sets the reset cache interval.
     * 
     * @param resetInterval the new reset cache interval
     */
    synchronized static public void setResetCacheInterval(long resetInterval) {
        if ((resetIntervalSec != null)&&(resetIntervalSec.longValue() ==  resetInterval)){
            return;
        }
        if (resetInterval <= 0)
            throw new IllegalArgumentException();
        timer.cancel();
        timer = new Timer();
        resetIntervalSec = resetInterval;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // reset cache;
                DataFetchUtil.reset();
            }
        }, resetInterval * 1000, resetInterval * 1000);
    }

    /**
     * Gets the reset cache interval.
     * 
     * @return the reset cache interval
     */
    static public Long getResetCacheInterval() {
        return resetIntervalSec;
    }

}
