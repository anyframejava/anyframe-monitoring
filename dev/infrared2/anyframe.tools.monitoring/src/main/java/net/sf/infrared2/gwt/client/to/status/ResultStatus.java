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
 * ResultStatus.java		Date created: 27.03.2008
 * Last modified by: gzgonikov
 * $Revision: 14286 $	Date: 27.03.2008
 */

package net.sf.infrared2.gwt.client.to.status;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * <b>ResultStatus</b><p>
 * Object that contains additional information about status results of service
 * method call.
 * 
 * @author Sergey Evluhin
 */
public class ResultStatus implements Serializable, IsSerializable {
    /** Serial version UID */
    private static final long serialVersionUID = -1497572112128961810L;

    /** Code result of call. */
    private int statusCode;
    /** Additional informational message about calling results. */
    private String statusMessage;

    /**
     * Default constructor.
     */
    public ResultStatus() {
    }

    /**
     * Creates object with predefined status code field.
     * 
     * @param statusCode - code result of call.
     */
    public ResultStatus(int statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * Creates object with predefined fields.
     * 
     * @param statusCode - code result of call.
     * @param statusMessage - additional informational message about calling
     *            results.
     */
    public ResultStatus(int statusCode, String statusMessage) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }

    /**
     * @return code result of call.
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * @param statusCode - code result of call.
     */
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * @return additional informational message about calling results.
     */
    public String getStatusMessage() {
        return statusMessage;
    }

    /**
     * @param statusMessage - additional informational message about calling
     *            results.
     */
    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString(){
        return statusMessage;
    }
}
