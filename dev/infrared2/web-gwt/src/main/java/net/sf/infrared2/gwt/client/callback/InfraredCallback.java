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
 * InfraredCallback.java		Date created: 26.03.2008
 * Last modified by: gzgonikov
 * $Revision: 14286 $	Date: 26.03.2008
 */
package net.sf.infrared2.gwt.client.callback;

import net.sf.infrared2.gwt.client.Constants;
import net.sf.infrared2.gwt.client.Engine;
import net.sf.infrared2.gwt.client.i18n.ApplicationMessages;
import net.sf.infrared2.gwt.client.to.IResultStatus;
import net.sf.infrared2.gwt.client.users.session.UserSessionBean;
import net.sf.infrared2.gwt.client.view.dialog.DialogHelper;
import net.sf.infrared2.gwt.client.view.facade.ApplicationViewFacade;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * <b>InfraredCallback</b><p>
 * The class receive infrared results of async call to infrared server.
 * @author gzgonikov
 * Copyright Exadel Inc, 2008
 */
public abstract class InfraredCallback implements AsyncCallback {

    /**
     * Done when call is success.
     * @param o - the any object
     */
    public abstract void doSuccess(Object o);

    /**
     * Done when call is failure .
     * @param throwable -  the instance of Throwable object
     */
    public void doFailure(Throwable throwable){}

    private static final Timer timer = new Timer(){
        public void run() {
            UserSessionBean.setSessionExpired(true);
        }
    };

    /**
     * Method onFailure ...
     *
     * @param throwable of type Throwable.
     */
    /* (non-Javadoc)
    * @see com.google.gwt.user.client.rpc.AsyncCallback#onFailure(java.lang.Throwable)
    */
    public void onFailure(Throwable throwable) {
        ApplicationViewFacade.maskAll(null);
        String summary = ApplicationMessages.MESSAGES.unexpectedException();
        String details="Exception in "+this.toString()+" : "+throwable.toString()+" : "+throwable.getMessage();
        if (details==null || "".equals(details)) details = ApplicationMessages.MESSAGES.serverNotRespond();
        String help = getStandardHelp();
        DialogHelper.showErrorWindow(summary, details, help);
        this.doFailure(throwable);
    }

    /**
     * Method onSuccess ...
     *
     * @param o of type Object
     */
    /* (non-Javadoc)
    * @see com.google.gwt.user.client.rpc.AsyncCallback#onSuccess(java.lang.Object)
    */
    public void onSuccess(Object o) {
        if (o instanceof IResultStatus){
            IResultStatus result = (IResultStatus)o;
            if (result.getResultStatus()!=null){
                //TODO here may go some expected server exceptions, to process them call this.onFailure with correct Throwable
                if(Constants.SESSION_EXPIRED_CODE==result.getResultStatus().getStatusCode()){
                    Engine.start();
                }
                else if(Constants.SUCCESS_CODE==result.getResultStatus().getStatusCode()){
                    timer.cancel();
                    timer.schedule(UserSessionBean.getSessionDuration()*1000);
                    this.doSuccess(o);
                }
            }
        }
    }

    /**
     * Create error text for show in dialog error.
     * @return - the complex help string 
     */
    private String getStandardHelp(){

        StringBuffer help = new StringBuffer("<p> ");

        help.append(ApplicationMessages.MESSAGES.helpTipSummary());

        help.append("<ul>");

        help.append("<li>");
        help.append("1) ");
        help.append(ApplicationMessages.MESSAGES.helpTipContinue());
        help.append("</li>");

        help.append("<li>");
        help.append("2) ");
        help.append(ApplicationMessages.MESSAGES.helpTipClean());
        help.append("</li>");

        help.append("<li>");
        help.append("3) ");
        help.append(ApplicationMessages.MESSAGES.helpTipNetwork());
        help.append("</li>");

        help.append("<li>");
        help.append("4) ");
        help.append(ApplicationMessages.MESSAGES.helpTipAdmin());
        help.append("</li>");

        help.append("</ul>");

//        help.append("</p>");

        return help.toString();
    }

}
