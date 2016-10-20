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
 * ApplicationEntryTO.java Date created: 22.01.2008
 * Last modified by: $Author: soyon.lim $ 
 * $Revision: 14286 $ $Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.gwt.client.to;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

/**
 * <b>ApplicationEntryTO</b><p>
 * Transfer object for Application entry for transfer application title and<br>
 * its instances list between layers 
 *
 * @author Andrey Zavgorodniy
 */
public class ApplicationEntryTO implements Serializable, IsSerializable {
    /**
     * Serial version ID
     */
    private static final long serialVersionUID = -3024267181665849022L;

    /** Present name of application */
    private String applicationTitle;

    /** List of instances belong one application */
    private InstanceEntryTO[] instanceList;

    /**
     * Default constructor
     */
    public ApplicationEntryTO() {
        super();
    }

    /**
     * Constructor
     *
     * @param title           - the application name
     * @param listOfInstances - the list of instances for current application
     */
    public ApplicationEntryTO(String title, InstanceEntryTO[] listOfInstances) {
        this();
        this.applicationTitle = title;
        this.instanceList = listOfInstances;
    }

    /**
     * @return the applicationTitle
     */
    public String getApplicationTitle() {
        return this.applicationTitle;
    }

    /**
     * @param applicationTitle the applicationTitle to set
     */
    public void setApplicationTitle(String applicationTitle) {
        this.applicationTitle = applicationTitle;
    }

    /**
     * @return the instanceList
     */
    public InstanceEntryTO[] getInstanceList() {
        return this.instanceList;
    }

    /**
     * @param instanceList the instanceList to set
     */
    public void setInstanceList(InstanceEntryTO[] instanceList) {
        this.instanceList = instanceList;
    }

    /**
     * 
     * {@inheritDoc}
     */
    public boolean equals(Object obj) {
        if (this==obj) return true;
        if((obj == null) || (!(obj instanceof ApplicationEntryTO))) return false;
        ApplicationEntryTO to = (ApplicationEntryTO)obj;
        if ((this.applicationTitle!=null)?this.applicationTitle.equals(to.getApplicationTitle()):to.getApplicationTitle()==null){
            if (compare(this.instanceList, to.getInstanceList())){
                return true;
            }
        }
        return false;
    }

    /**
     * 
     * {@inheritDoc}
     */
    public int hashCode() {
        int hash = 9;
        hash = 31*hash+(applicationTitle != null ? applicationTitle.hashCode() : 0);
        hash = 31*hash+(instanceList==null ? 0 : getArrayHashCode(instanceList));
        return hash;
    }

    /**
     * Transform array to HashCode.
     * @param objArray - the array as object
     * @return the array hash code
     */
    private int getArrayHashCode(Object[] objArray) {
        int result=11;
        if (objArray==null) return 0;
        for (int i =0; i<objArray.length; i++){
            if (objArray[i]!=null)
                result+=objArray[i].hashCode();
        }
        return result + objArray.length;
    }

    /**
     * Special comparator for array objects.
     * @param a - array to compare.
     * @param a2 - array to compare
     * @return - true if object equals
     */
    private boolean compare(Object[] a, Object[] a2) {
        if (a==a2)
            return true;
        if (a==null || a2==null)
            return false;

        int length = a.length;
        if (a2.length != length)
            return false;

        for (int i=0; i<length; i++) {
            Object o1 = a[i];
            Object o2 = a2[i];
            if (!(o1==null ? o2==null : o1.equals(o2)))
                return false;
        }

        return true;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString(){
        String sep = "\n";
        String result = "ApplicationEntryTO : [" + sep;
        result+= applicationTitle + sep;
        result+= "instanceList : [ " + sep;
        if (instanceList!=null){
            for (int i=0;i<instanceList.length;i++) {
                result += instanceList[i].toString() + sep;
            }
        }
        result += "]";
        result += "]";
        return result;
    }
}
