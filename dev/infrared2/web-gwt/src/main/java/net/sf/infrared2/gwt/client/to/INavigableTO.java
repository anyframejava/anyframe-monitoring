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

/**
 * <b>INavigableTO</b><p>
 * Interface is used for support cache.
 * 
 * @author Sergey Evluhin
 */
public interface INavigableTO {

    /**
     * Returns transfer object associated with link in navigator.
     * 
     * @return transfer object associated with link in navigator.
     */
    public NavigatorEntryTO getNavigatorEntryTO();

    /**
     * Sets transfer object associated with link in navigator.
     * 
     * @param navigatorTO - transfer object associated with link in navigator.
     */
    public void setNavigatorEntryTO(NavigatorEntryTO navigatorTO);

}
