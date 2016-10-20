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
 * CookieSupport.java		Date created: 31.01.2008
 * Last modified by: $Author: soyon.lim $
 * $Revision: 14457 $	$Date: 2009-06-08 16:45:33 +0900 (월, 08 6월 2009) $
 */

package net.sf.infrared2.gwt.client.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.google.gwt.user.client.Cookies;

/**
 * <b>CookieUtils</b><p>
 * Singleton class that is responsible for reading and writing cookies, <br>
 * has other useful helper util.
 * 
 * @author Robert Hanson <iamroberthanson AT gmail.com>
 * @author modified by Andrey Zavgorodniy
 */
public class CookieUtils {
    /** Constant representation select application cookie name  */
    final String SELECT_APPLICATIONS_COOKIE_NAME = "SELECTED_APPL";
    
    /** Constant representation live date state cookie name  */
    final String LIVE_DATE_STATE_COOKIE_NAME = "LIVE_DATE";
    
    /** Constant representation archive date state cookie name  */
    final String ARCHIVE_DATE_COOKIE_NAME = "ARCHIVE_DATE";
    
    /** Constant representation archive start date value cookie name  */
    final String ARCHIVE_START_DATE_COOKIE_NAME = "START_DATE";
    
    /** The ARCHIV e_ star t_ tim e_ cooki e_ name. */
    final String ARCHIVE_START_TIME_COOKIE_NAME = "START_TIME";
    
    /** Constant representation archive end date value cookie name  */
    final String ARCHIVE_END_DATE_COOKIE_NAME = "END_DATE";
    
    /** The ARCHIV e_ en d_ tim e_ cooki e_ name. */
    final String ARCHIVE_END_TIME_COOKIE_NAME = "END_TIME";
    
    /** Constant representation divided by application value cookie name  */
    final String DIVIDED_BY_APPLICATION_COOKIE_NAME = "DIVIDED_BY_APPLICATION";
    
    /** Constant representation is graph label enable state value cookie name  */
    final String IS_GRAPH_LABEL_ENABLED_COOKIE_NAME = "GRAPH_LABEL_ENABLED";
    
    /** Expire date for cookie live*/
    final long DURATION = 1000 * 60 * 60 * 24 * 365; // duration remembering
                                                        // login. 365 days

    /** Current instance of cookie util. */
    public static CookieUtils cookiesSupport = new CookieUtils();

    /**
     * Get current instance of cookie util.
     * @return the CookieUtil current instance representation
     */
    public static CookieUtils getInstance() {
        return cookiesSupport;
    }


    /**
     * Write cookie method.
     * 
     * @param cookieName - the name of cookie
     * @param cookieValue - the value of current cookie
     */
    public void writeCookie(String cookieName, String cookieValue) {
        // write(cookieName, cookieValue, date);
        Date expires = new Date(System.currentTimeMillis() + DURATION);
        Cookies.setCookie(cookieName, cookieValue, expires, null, "/", false);
    }

    /**
     * Read cookie method.
     * 
     * @param cookieName - cookie name
     * @return value of cookie by name
     */
    public String readCookie(String cookieName) {
        return Cookies.getCookie(cookieName);
        // return readValue(cookieName);
    }

    /**
     * Check if cookies exists.
     * 
     * @return true if cookie present
     */
    public boolean isCookieExists() {
        return readCookie(ARCHIVE_DATE_COOKIE_NAME) != null
                        && readCookie(LIVE_DATE_STATE_COOKIE_NAME) != null
                        && readCookie(ARCHIVE_START_DATE_COOKIE_NAME) != null
                        && readCookie(ARCHIVE_END_DATE_COOKIE_NAME) != null
                        && readCookie(SELECT_APPLICATIONS_COOKIE_NAME) != null;
    }

    /**
     * Creates, sets, and returns the Cookie object
     * 
     * @param name - cookie name
     * @param value - cookie value
     * @param expires - expire date for cookie live
     * @param path - the path of cookie host
     * @return the Cookie created or reset
     */
    public static Cookie write(String name, String value, Date expires, String path) {
        boolean useExp = false;
        int year = -1, month = -1, day = -1;
        if (expires != null) {
            useExp = true;
            year = expires.getYear() + 1900;
            month = expires.getMonth();
            day = expires.getDate();
        }
        writeNative(name, value, useExp, year, month, day, path);
        return new Cookie(name, value);
    }

    /**
     * Write cookie method.
     * @param name - cookie name
     * @param value - cookie value
     * @param expires - expire date for cookie live
     * @return the Cookie created or reset
     */
    public static Cookie write(String name, String value, Date expires) {
        return write(name, value, expires, "/");
    }

    /**
     * Write cookie uses native method.
     * @param name - cookie name
     * @param value - cookie value
     * @param useExpires - use cookie live date
     * @param year - number of years
     * @param month - number of month
     * @param day - number of day
     */
    private static void writeNative(String name, String value, boolean useExpires, int year,
                    int month, int day) {
        writeNative(name, value, useExpires, year, month, day, "/");
    }

    /**
     * Write cookie uses native method.
     * @param name - cookie name
     * @param value - cookie value
     * @param useExpires - use cookie live date
     * @param year - number of years
     * @param month - number of month
     * @param day - number of day
     * @param path - the path of cookie place
     */
    private static native void writeNative(String name, String value, boolean useExpires, int year,
                    int month, int day, String path) /*-{
           var expStr = (useExpires) ? "; expires=" + new Date(year, month, day).toGMTString() : "";
           $doc.cookie = name + "=" + escape(value) + expStr + "; path=" + path;
       }-*/;

    /**
     * Erases cookie with given name.
     * 
     * @param name - the cookie name
     */
    public static void erase(String name) {
        writeNative(name, "", true, 1970, 1, 1);
    }

    /**
     * Gets the value of the cookie with the given name.
     * 
     * @param name Name of cookie whose value should be retrieved
     * @return Cookie value or null if cookie is not found
     */
    public static String readValue(String name) {
        Cookie c = read(name);
        return c == null ? null : c.getValue();
    }

    /**
     * Gets the cookie with the given name.
     * 
     * @param name Name of cookie to get.
     * @return Cookie or null if the cookie is not found.
     */
    public static Cookie read(String name) {
        String val = getValue(name);
        return (val == null) ? null : new Cookie(name, val);
    }

    /**
     * Tries to load the cookie, but only sets fields if they are found
     * 
     * @param name - the cookie name
     */
    private static native String getValue(String name) /*-{
           var start = $doc.cookie.indexOf(name + '=');
           var len = start + name.length + 1;
           if ((!start) && (name != $doc.cookie.substring(0,name.length))) {
               return null;
           }
           if (start == -1) {
               return null;
           }
           var end = document.cookie.indexOf(';',len);
           if (end == -1) {
               end = $doc.cookie.length;
           }
           return unescape($doc.cookie.substring(len,end));
       }-*/;

    /**
     * Erase the given cookie
     * 
     * @param c - the Cookie object need to erase
     */
    public static void erase(Cookie c) {
        erase(c.getName());
    }

    /**
     * Write the given cookie.
     * @param c - the Cookie object need to write or create
     * @param expires - expire date for cookie live
     */
    public static void write(Cookie c, Date expires) {
        write(c.getName(), c.getValue(), expires);
    }

    /**
     * Get all exist cookies  
     * @return - the array of Cookie objects.
     */
    public static Cookie[] getAll() {
        Map cookies = getCookieMap();
        Cookie[] rVal = new Cookie[cookies.size()];
        int counter = 0;
        for (Iterator i = cookies.keySet().iterator(); i.hasNext(); counter++) {
            String name = (String) i.next();
            rVal[counter] = new Cookie(name, (String) cookies.get(name));
        }
        return rVal;
    }

    /**
     * Get cookie map presentation pairs name - value.
     * @return - the cookies map - pair name with its value
     */
    private static Map getCookieMap() {
        Map jar = new HashMap();
        fillCookieJar(jar);
        return jar;
    }

    /**
     * Write cookie map native method realization.
     * @param jar - the HashMap 
     */
    private static native void fillCookieJar(Map jar) /*-{
           var cookies = $doc.cookie;
           if (cookies && cookies != '') {
               var cl = cookies.split('; ');
               for (var i = 0; i < cl.length; ++i) {
                   var parts = cl[i].split('=');
                   jar.@java.util.Map::put(Ljava/lang/Object;Ljava/lang/Object;)(parts[0], unescape(parts[1]));
               }
           }
       }-*/;
}
