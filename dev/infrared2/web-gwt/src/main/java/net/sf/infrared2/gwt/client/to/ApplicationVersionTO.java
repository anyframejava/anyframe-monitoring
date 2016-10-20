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
 * ApplicationVersionTO.java		Date created: 11.04.2008
 * Last modified by: gzgonikov
 * $Revision: 14286 $	Date: 11.04.2008
 */

package net.sf.infrared2.gwt.client.to;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

import net.sf.infrared2.gwt.client.to.status.ResultStatus;

/**
 * <b>ApplicationVersionTO</b><p>
 * ApplicationVersionTO.java Date created: 11.04.2008
 *
 * @author: gzgonikov
 */

public class ApplicationVersionTO  implements Serializable, IsSerializable, IResultStatus {

    /** Field version  */
    private String version;

    /** Field resultStatus  */
    private ResultStatus resultStatus;

    /** Field serialVersionUID  */
    private static final long serialVersionUID = 1593477305556636975L;

    /**
     * Method getResultStatus returns the resultStatus of this ApplicationVersionTO object.
     *
     * @return the resultStatus (type ResultStatus) of this ApplicationVersionTO object.
     *
     * @see IResultStatus#getResultStatus()
     */
    public ResultStatus getResultStatus() {
        return resultStatus;
    }

    /**
     * Method setResultStatus sets the resultStatus of this ApplicationVersionTO object.
     *
     * @param resultStatus the resultStatus of this ApplicationVersionTO object.
     *
     *
     * @see IResultStatus#setResultStatus(ResultStatus)
     */
    public void setResultStatus(ResultStatus resultStatus) {
        this.resultStatus=resultStatus;
    }

    /**
     * Method getVersion returns the version of this ApplicationVersionTO object.
     *
     * @return the version (type String) of this ApplicationVersionTO object.
     */
    public String getVersion() {
        return version;
    }

    /**
     * Method setVersion sets the version of this ApplicationVersionTO object.
     *
     * @param version the version of this ApplicationVersionTO object.
     *
     */
    public void setVersion(String version) {
        this.version = version;
    }
}
