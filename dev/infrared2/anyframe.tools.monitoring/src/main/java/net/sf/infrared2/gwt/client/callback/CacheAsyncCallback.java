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
package net.sf.infrared2.gwt.client.callback;

import net.sf.infrared2.gwt.client.to.INavigableTO;
import net.sf.infrared2.gwt.client.users.session.UserSessionBean;

/*
* CacheAsyncCallback Date created: 03.03.2008
* Author: gzgonikov 
*/

/**
 * <b>CacheAsyncCallback</b><p>
 * The class receive cache results of async call to server.
 * @author gzgonikov 
 * Copyright Exadel Inc, 2008
 */
public abstract class CacheAsyncCallback extends InfraredCallback {

    /**
     * 
     * @param o - the any object
     */
    public abstract void atSuccess(Object o);    

    /* (non-Javadoc)
     * @see net.sf.infrared2.gwt.client.callback.InfraredCallback#doSuccess(java.lang.Object)
     */
    public void doSuccess(Object o) {
        if (o!=null && o instanceof INavigableTO){
            INavigableTO result = (INavigableTO)o;
            if (!UserSessionBean.getCache().containsKey(result.getNavigatorEntryTO())){
                UserSessionBean.getCache().put(result.getNavigatorEntryTO(), o);
            }
        }
        this.atSuccess(o);
    }
}
