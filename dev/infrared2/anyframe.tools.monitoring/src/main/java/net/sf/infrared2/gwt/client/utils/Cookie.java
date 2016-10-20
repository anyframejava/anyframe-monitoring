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
 * Cookie.java		Date created: 06.02.2008
 * Last modified by: $Author: soyon.lim $
 * $Revision: 14286 $	$Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.gwt.client.utils;

import java.util.Date;

/**
 * <b>Cookie</b><p>
 * Immutable object representing a single browser cookie.
 * 
 * @author Andrey Zavgorodniy Copyright Exadel Inc, 2008
 */
public class Cookie {
    /** Contain cookie name */
    private String name;
    /** Contain cookie value */
    private String value;

    /**
     * Default constructor.
     * 
     * @param name - the cookie name.
     * @param value - the cookie value.
     */
    public Cookie(String name, String value) {
        super();
        this.name = name;
        this.value = value;
    }

    /**
     * Gets the name of the cookie
     * 
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Get's the cookie's value
     * 
     * @return
     */
    public String getValue() {
        return value;
    }

    /**
     * Write cookie - with live data  equals expires.
     * @param expires - the expire date.
     */
    public void write(Date expires) {
        CookieUtils.write(this, expires);
    }
}
