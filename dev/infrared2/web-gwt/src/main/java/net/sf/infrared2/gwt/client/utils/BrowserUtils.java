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
 * BrowserUtils.java		Date created: 24.04.2008
 * Last modified by: gzgonikov
 * $Revision: 14286 $	Date: 24.04.2008
 */

package net.sf.infrared2.gwt.client.utils;

/**
 * BrowserUtils.java Date created: 24.04.2008
 *
 * @author: gzgonikov
 */

public class BrowserUtils {

    /**
     * Method reloadPage, the same as cntl F5
     */
    public static native void reloadPage()/*-{
        $wnd.location.reload();
    }-*/;
}
