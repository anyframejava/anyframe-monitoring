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
 * UserSessionBean.java Date created: 22.01.2008
 * Last modified by: $Author: soyon.lim $ 
 * $Revision: 14286 $ $Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.gwt.client.users.session;


import java.util.HashMap;
import java.util.Map;

import net.sf.infrared2.gwt.client.report.ReportSettingsCriteria;
import net.sf.infrared2.gwt.client.to.ApplicationConfigTO;

/**
 * <b>UserSessionBean</b><p>
 * Session user bean - contain current user settings
 *
 * @author Andrey Zavgorodniy
 */
public class UserSessionBean {

    /** Field sessionDuration  */
    private static int sessionDuration=30*60;

    /** Configuration bean property */
    private static ApplicationConfigTO configBean;
    
    /** Report configuration property */
    private static ReportSettingsCriteria reportCriteria;
    
    /** Cookies exists */
    private static boolean cookiesAvailable;

    /**
     * client cashe
     * @gwt.typeArgs <net.sf.infrared2.gwt.client.to.NavigatorEntryTO, java.io.Serializable> 
     */
    private static Map cache = new HashMap();

    /** Field isSessionExpired  */
    private static boolean isSessionExpired;

    /**
     * Method getCache returns the cache of this UserSessionBean object.
     *
     * @return the cache (type Map) of this UserSessionBean object.
     */
    public static Map getCache() {
        if (cache==null) cache = new HashMap();
        return cache;
    }

    /**
     * Method clearCache reset cache.
     */
    public static void clearCache(){
        getCache().clear();
    }

    /**
     * @return application config bean instance
     */
    public static ApplicationConfigTO getApplConfigBeanInstance() {
        return configBean;
    }

    /**
     * @param configBean - the application config bean instance
     */
    public static void setConfigBean(ApplicationConfigTO configBean) {
        UserSessionBean.configBean = configBean;
    }

    /**
     * @return the reportCriteria
     */
    public static ReportSettingsCriteria getReportCriteria() {
        return reportCriteria;
    }

    /**
     * @param reportCriteria the reportCriteria to set
     */
    public static void setReportCriteria(ReportSettingsCriteria reportCriteria) {
        UserSessionBean.reportCriteria = reportCriteria;
    }

    /**
     * @return the cookiesAvailable
     */
    public static boolean isCookiesAvailable() {
        return cookiesAvailable;
    }

    /**
     * @param cookiesAvailable the cookiesAvailable to set
     */
    public static void setCookiesAvailable(boolean cookiesAvailable) {
        UserSessionBean.cookiesAvailable = cookiesAvailable;
    }
    
    /**
     * Method isAnyApplSelected returns the anyApplSelected of this UserSessionBean object.
     *
     * @return the anyApplSelected (type boolean) of this UserSessionBean object.
     */
    public static boolean isAnyApplSelected(){
        return getApplConfigBeanInstance() != null &&
                getApplConfigBeanInstance().getApplicationList() != null &&
                getApplConfigBeanInstance().getApplicationList().length >0;
    }

    /**
     * Method isSessionExpired returns the sessionExpired of this UserSessionBean object.
     *
     * @return the sessionExpired (type boolean) of this UserSessionBean object.
     */
    public static boolean isSessionExpired() {
        return isSessionExpired;
    }

    /**
     * Method setSessionExpired sets the sessionExpired of this UserSessionBean object.
     *
     * @param sessionExpired the sessionExpired of this UserSessionBean object.
     *
     */
    public static void setSessionExpired(boolean sessionExpired) {
        isSessionExpired = sessionExpired;
    }

    /**
     * Method getSessionDuration returns the sessionDuration of this UserSessionBean object.
     *
     * @return the sessionDuration (type int) of this UserSessionBean object.
     */
    public static int getSessionDuration() {
        return sessionDuration;
    }

    /**
     * Method setSessionDuration sets the sessionDuration of this UserSessionBean object.
     *
     * @param sessionDuration the sessionDuration of this UserSessionBean object.
     *
     */
    public static void setSessionDuration(int sessionDuration) {
        UserSessionBean.sessionDuration = sessionDuration;
    }
}
