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
 * ResultStatusTO.java		Date created: 27.03.2008
 * Last modified by: gzgonikov
 * $Revision: 14286 $	Date: 27.03.2008
 */

package net.sf.infrared2.gwt.client.to.status;

import java.io.Serializable;

import net.sf.infrared2.gwt.client.to.IResultStatus;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * <b>ResultStatusTO</b><p>
 * Transfer object for receiving status of execution service's methods.
 * 
 * @author Sergey Evluhin
 */
public class ResultStatusTO implements IsSerializable, Serializable, IResultStatus {
    /** Serial version UID */
    private static final long serialVersionUID = 1641749149669665730L;

    /** Result status field value */
    private ResultStatus resultStatus;

    /**
     * Default constructor.
     */
    public ResultStatusTO() {
    }

    /**
     * Creates object from another object with the same type.
     * 
     * @param resultStatus - object with the same type.
     */
    public ResultStatusTO(ResultStatus resultStatus) {
        this.resultStatus = resultStatus;
    }

    /**
     * {@inheritDoc}
     */
    public ResultStatus getResultStatus() {
        return resultStatus;
    }

    /**
     * {@inheritDoc}
     */
    public void setResultStatus(ResultStatus resultStatus) {
        this.resultStatus = resultStatus;
    }
}
