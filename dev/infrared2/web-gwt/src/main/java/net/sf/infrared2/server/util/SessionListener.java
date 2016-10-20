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
 * PropertyUtil.java Date created: 22.01.2008
 * Last modified by: $Author: soyon.lim $
 * $Revision: 14286 $ $Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.server.util;


import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import net.sf.infrared2.gwt.server.service.SIRServiceImpl;

/**
 * <b>SessionListener</b><p>
 * Class SessionListener process all necessary
 *
 * @author gzgonikov
 * Created on 08.04.2008
 */
public class SessionListener implements HttpSessionListener {

    /**
     * Method sessionCreated run when session created
     *
     * @param event of type HttpSessionEvent
     */
    public void sessionCreated(HttpSessionEvent event) {
    }

    /**
     * Method sessionDestroyed run when session destroyed
     *
     * @param event of type HttpSessionEvent
     */
    public void sessionDestroyed(HttpSessionEvent event) {
        SIRServiceImpl.setSessionExpired(true);
        if (GraphicUtil.getSmartImageMap()!=null){
            GraphicUtil.getSmartImageMap().clear(event.getSession().getId());
        }    
    }
}
