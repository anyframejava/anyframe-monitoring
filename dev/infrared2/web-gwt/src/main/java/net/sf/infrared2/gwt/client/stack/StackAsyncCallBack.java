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
 * StackAsyncCallBack.java Date created: 29.01.2008
 * Last modified by: $Author: soyon.lim $ 
 * $Revision: 14286 $ $Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.gwt.client.stack;

import net.sf.infrared2.gwt.client.callback.InfraredCallback;
import net.sf.infrared2.gwt.client.i18n.ApplicationMessages;
import net.sf.infrared2.gwt.client.to.NavigatorTO;
import net.sf.infrared2.gwt.client.users.session.UserSessionBean;
import net.sf.infrared2.gwt.client.view.facade.ApplicationViewFacade;

/**
 * <b>StackAsyncCallBack</b><p>
 * Class represents implementation of asynchronous call back for the stack panel
 * holder (navigator panel). It uses for receiving lists of modules and transfer
 * objects associated with links in navigator panel.
 * 
 * @author Sergey Evluhin
 */
public class StackAsyncCallBack extends InfraredCallback {
    /**
     * {@inheritDoc}
     */
    public void doSuccess(Object result) {
        NavigatorTO navig = (NavigatorTO) result;
        if (navig.getAbsolute() == null || navig.getAbsolute().length == 0) {
            ApplicationViewFacade.cleanAll();
        }
        StackPanelHolder.getInstance().reset();
        StackPanelHolder.getInstance().addLinks(navig.getAbsolute(),
                        ApplicationMessages.MESSAGES.absoluteModuleStackLabel());
        StackPanelHolder.getInstance().addLinks(navig.getHierarchModule(),
                        ApplicationMessages.MESSAGES.hierarchicalModuleStackLabel());
        if (UserSessionBean.getApplConfigBeanInstance().isLiveDate()) {
            StackPanelHolder.getInstance().addLinks(navig.getLastInv(),
                            ApplicationMessages.MESSAGES.lastInvocationStackLabel());
        }
    }
}
