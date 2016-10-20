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
/**
 * InitialStateTO.java		Date created: 09.04.2008
 * Last modified by: gzgonikov
 * $Revision: 14286 $	Date: 09.04.2008
 */

package net.sf.infrared2.gwt.client.to;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

import net.sf.infrared2.gwt.client.to.status.ResultStatus;

/**
 * <b>InitialStateTO</b><p>
 * The class present initial states for different application settings
 * 
 * @author: gzgonikov
 */
public class InitialStateTO implements Serializable, IsSerializable, IResultStatus {

    /** Field isCacheClientEnabled */
    private boolean isCacheClientEnabled;

    /** Field resultStatus */
    private ResultStatus resultStatus;

    /** Field serialVersionUID */
    private static final long serialVersionUID = -6590965037025054046L;

    /** Field sessionDuration */
    private int sessionDuration;

    /**
     * Method getResultStatus returns the resultStatus of this InitialStateTO
     * object.
     * 
     * @return the resultStatus (type ResultStatus) of this InitialStateTO
     *         object.
     */
    public ResultStatus getResultStatus() {
        return resultStatus;
    }

    /**
     * Method setResultStatus sets the resultStatus of this InitialStateTO
     * object.
     * 
     * @param resultStatus the resultStatus of this InitialStateTO object.
     */
    public void setResultStatus(ResultStatus resultStatus) {
        this.resultStatus = resultStatus;
    }

    /**
     * Method isCacheClientEnabled returns the cacheClientEnabled of this
     * InitialStateTO object.
     * 
     * @return the cacheClientEnabled (type boolean) of this InitialStateTO
     *         object.
     */
    public boolean isCacheClientEnabled() {
        return isCacheClientEnabled;
    }

    /**
     * Method setCacheClientEnabled sets the cacheClientEnabled of this
     * InitialStateTO object.
     * 
     * @param cacheClientEnabled the cacheClientEnabled of this InitialStateTO
     *            object.
     */
    public void setCacheClientEnabled(boolean cacheClientEnabled) {
        isCacheClientEnabled = cacheClientEnabled;
    }

    /**
     * Method getSessionDuration returns the sessionDuration of this
     * InitialStateTO object.
     * 
     * @return the sessionDuration (type int) of this InitialStateTO object.
     */
    public int getSessionDuration() {
        return sessionDuration;
    }

    /**
     * Method setSessionDuration sets the sessionDuration of this InitialStateTO
     * object.
     * 
     * @param sessionDuration the sessionDuration of this InitialStateTO object.
     */
    public void setSessionDuration(int sessionDuration) {
        this.sessionDuration = sessionDuration;
    }
}
