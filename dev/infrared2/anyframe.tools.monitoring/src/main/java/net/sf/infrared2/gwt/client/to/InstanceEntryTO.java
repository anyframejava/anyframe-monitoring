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
 * ApplicationInstanceEntity.java Date created: 22.01.2008
 * Last modified by: $Author: soyon.lim $ 
 * $Revision: 14286 $ $Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.gwt.client.to;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

/**
 * <b>InstanceEntryTO</b><p>
 * Class hold instance entry transfer object.
 * @author Andrey Zavgorodniy
 */
public class InstanceEntryTO implements Serializable, IsSerializable {
    private static final long serialVersionUID = 7026849032897770221L;

    /**
     * Title of instance
     */
    private String title;

    /**
     * Default constructor.
     */
    public InstanceEntryTO() {
    }

    /**
     * Constructor.
     */
    public InstanceEntryTO(String instanceName) {
        this.title = instanceName;
    }

    /**
     * @return the name
     */
    public String getName() {
        return this.title;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.title = name;
    }

    /**
     * 
     * {@inheritDoc}
     */
    public boolean equals(Object obj) {
        if (this==obj) return true;
        if((obj == null) || (!(obj instanceof InstanceEntryTO))) return false;
        InstanceEntryTO to = (InstanceEntryTO)obj;
        if ((this.title!=null)?this.title.equals(to.getName()):to.getName()==null){
            return true;       
        }
        return false;
    }

    /**
     * 
     * {@inheritDoc}
     */
    public int hashCode() {
        int hash=7;
        return (title!=null)?hash+title.hashCode():hash;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString(){
        return title;
    }
}
