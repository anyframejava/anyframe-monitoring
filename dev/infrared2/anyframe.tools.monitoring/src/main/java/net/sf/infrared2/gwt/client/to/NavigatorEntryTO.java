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

import net.sf.infrared2.gwt.client.Constants;
import net.sf.infrared2.gwt.client.to.other.TraceTreeNodeTO;

/**
 * <b>NavigatorEntryTO</b><p>
 * Transfer object for Navigator (Accordeon) Entry
 *
 * @author Sergey
 */
public class NavigatorEntryTO implements Serializable, IsSerializable {

    /**
     * Serial version UID
     */
    private static final long serialVersionUID = -8984384621142470513L;

    /**
     * Flag show if navigation entry is root
     */
    private boolean isRoot;

    /**
     * Title of navigation entry
     */
    private String title;

    /**
     * Type of module : abstract or hierarchical
     */
    private String moduleType;

    /**
     * Application entry TO belong current navigation entry
     */
    private ApplicationEntryTO applications;

    /** need for cache capability only */
    private TraceTreeNodeTO operationNode;
    
    /** The application key. */
    private String applicationKey;


    /**
     * Default constructor.
     * @param title - title of navigation entry
     * @param moduleType - the type of module : abstract or hierarchical
     * @param applications - the application entry TO belong current navigation entry
     */
    public NavigatorEntryTO(String title, String moduleType, ApplicationEntryTO applications) {
        this.title = title;
        this.moduleType = moduleType;
        this.applications = applications;
    }

    /**
     * Constructor.
     * @param navigatorEntryTO - the current navigator entry TO 
     */
    public NavigatorEntryTO(NavigatorEntryTO navigatorEntryTO) {
        this.isRoot = navigatorEntryTO.isRoot();
        this.title = navigatorEntryTO.getTitle();
        this.moduleType = navigatorEntryTO.getModuleType();
        this.applications = navigatorEntryTO.getApplications();
        this.operationNode = navigatorEntryTO.getOperationNode();
    }

    /**
     * @return the trace tree node TO 
     */
    public TraceTreeNodeTO getOperationNode() {
        return operationNode;
    }

    /**
     * Setter for operation node.
     * @param operationNode - the Trace Tree Node TO as parametr
     */
    public void setOperationNode(TraceTreeNodeTO operationNode) {
        this.operationNode = operationNode;
    }

    /**
     * Default constructor.
     */
    public NavigatorEntryTO() {
        super();
    }

    /**
     * Create stack entry.
     *
     * @param title       - label for entry
     * @param moduleType - the type of module
     */
    public NavigatorEntryTO(String title, String moduleType) {
        this();
        this.title = title;
        this.moduleType = moduleType;
    }


    /**
     * @return the isRoot
     */
    public boolean isRoot() {
        return this.isRoot;
    }

    /**
     * @param isRoot the isRoot to set
     */
    public void setRoot(boolean isRoot) {
        this.isRoot = isRoot;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the moduleType
     */
    public String getModuleType() {
        return this.moduleType;
    }

    /**
     * @param moduleType the moduleType to set
     */
    public void setModuleType(String moduleType) {
        this.moduleType = moduleType;
    }
    
    /**
     * @return the applications
     */
    public ApplicationEntryTO getApplications() {
        return this.applications;
    }

    /**
     * @param applications the applications to set
     */
    public void setApplications(ApplicationEntryTO applications) {
        this.applications = applications;
    }

    /**
     * Define if title is SQL.
     * @return - true if title contain word SQL
     */
    public boolean isSQL() {
        return title.indexOf(Constants.TITLE_SQL) > -1;
    }


    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (this==obj) return true;
        if((obj == null) || (!(obj instanceof NavigatorEntryTO))) return false;
        NavigatorEntryTO to = (NavigatorEntryTO)obj;
        if (this.isRoot==to.isRoot){
            if ((this.moduleType!=null)?this.moduleType.equals(to.getModuleType()):to.getModuleType()==null){
                if ((this.title!=null)?this.title.equals(to.getTitle()):to.getTitle()==null){
                    if ((this.applications!=null)?this.applications.equals(to.getApplications()):to.getApplications()==null){
                        if ((this.operationNode!=null)?this.operationNode.equals(to.getOperationNode()):to.getOperationNode()==null){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        int hash = 4;
        hash = 7*hash+(isRoot ? 1 : 0);
        hash = 7*hash+(title!=null ? title.hashCode() : 0);
        hash = 7*hash+(moduleType!=null ? moduleType.hashCode() : 0);
        hash = 7*hash+(applications!=null ? applications.hashCode() : 0);
        hash = 7*hash+(operationNode!=null ? operationNode.hashCode() : 0);
        return hash;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#clone()
     */
    public NavigatorEntryTO clone() {
        return new NavigatorEntryTO(this);
    }

    /**
     * Gets the application key.
     * 
     * @return the application key
     */
    public String getApplicationKey() {
        return applicationKey;
    }

    /**
     * Sets the application key.
     * 
     * @param applicationKey the new application key
     */
    public void setApplicationKey(String applicationKey) {
        this.applicationKey = applicationKey;
    }
    
    
}