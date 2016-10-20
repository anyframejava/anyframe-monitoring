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
package net.sf.infrared2.gwt.client.to;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

import net.sf.infrared2.gwt.client.to.status.ResultStatus;

/**
 * <b>NavigatorTO</b><p>
 * Transfer object for accordeon navigation panel
 * 
 * @author Birukov Sergey
 */
public class NavigatorTO implements Serializable, IsSerializable, IResultStatus {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -2704486898930590300L;

    /** Array of NavigatorEntryTO for absolute module. */
    private NavigatorEntryTO[] absolute;
    /** Array of NavigatorEntryTO for last invocation module. */
    private NavigatorEntryTO[] lastInv;
    /** Array of NavigatorEntryTO for hierarchical module. */
    private NavigatorEntryTO[] hierarchModule;
    /** Result object */
    private ResultStatus resultStatus;

    /**
     * Default constructor.
     */
    public NavigatorTO() {
        super();
    }

    /**
     * Constructor.
     * 
     * @param absolute - the array of NavigatorEntryTO for absolute module
     * @param lastInv - the array of NavigatorEntryTO for last invocation module
     * @param hierarchModule - the array of NavigatorEntryTO for hierarchical
     *            module
     */
    public NavigatorTO(NavigatorEntryTO[] absolute, NavigatorEntryTO[] lastInv,
                    NavigatorEntryTO[] hierarchModule) {
        this();
        this.absolute = absolute;
        this.lastInv = lastInv;
        this.hierarchModule = hierarchModule;
    }

    /**
     * @return the absolute
     */
    public NavigatorEntryTO[] getAbsolute() {
        return this.absolute;
    }

    /**
     * @param absolute the absolute to set
     */
    public void setAbsolute(NavigatorEntryTO[] absolute) {
        this.absolute = absolute;
    }

    /**
     * @return the lastInv
     */
    public NavigatorEntryTO[] getLastInv() {
        return this.lastInv;
    }

    /**
     * @param lastInv the lastInv to set
     */
    public void setLastInv(NavigatorEntryTO[] lastInv) {
        this.lastInv = lastInv;
    }

    /**
     * @return the hierarchModule
     */
    public NavigatorEntryTO[] getHierarchModule() {
        return this.hierarchModule;
    }

    /**
     * @param hierarchModule the hierarchModule to set
     */
    public void setHierarchModule(NavigatorEntryTO[] hierarchModule) {
        this.hierarchModule = hierarchModule;
    }

    /**
     * @return the resultStatus
     */
    public ResultStatus getResultStatus() {
        return this.resultStatus;
    }

    /**
     * @param resultStatus the resultStatus to set
     */
    public void setResultStatus(ResultStatus resultStatus) {
        this.resultStatus = resultStatus;
    }
}
