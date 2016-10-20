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
package net.sf.infrared2.gwt.client.view.dialog;

import net.sf.infrared2.gwt.client.i18n.ApplicationMessages;
import net.sf.infrared2.gwt.client.view.facade.ApplicationViewFacade;

import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Position;
import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.widgets.*;
import com.gwtext.client.widgets.event.ButtonListener;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.layout.BorderLayout;
import com.gwtext.client.widgets.layout.BorderLayoutData;


/**
 * <b>DialogHelper</b><p>
 * Class for information dialog representation.
 * @author gzgonikov
 * Copyright Exadel Inc, 2008
 */
public class DialogHelper {

    /**
     * Show window contain two tab layout: summary and details.
     * @param summary - the text in title Summary tab 
     * @param details - the text in title Details tab
     * @param help - the panel of south in layout(split panel)
     */
    public static void showErrorWindow(String summary, String details, String help) {

        final Window systemErrorWindow = new Window();

        //center panel
        TabPanel tabPanel = new TabPanel();
        tabPanel.setActiveTab(0);

        Panel tab1 = new Panel();
        tab1.setTitle(ApplicationMessages.MESSAGES.summary());
        tab1.setHtml(summary);
        tab1.setAutoScroll(true);
        tab1.setMargins(3);

        Panel tab2 = new Panel();
        tab2.setTitle(ApplicationMessages.MESSAGES.details());
        tab2.setHtml(details);
        tab2.setAutoScroll(true);
        tab2.setMargins(3);

        tabPanel.add(tab1);
        tabPanel.add(tab2);

        //south panel
        Panel navPanel = new Panel();
        navPanel.setTitle(ApplicationMessages.MESSAGES.help());
        navPanel.setHeight(110);
        navPanel.setCollapsible(true);
//        navPanel.setCollapsed(true);
        navPanel.setAutoHeight(true);
        navPanel.setHtml(help);
        navPanel.setAutoScroll(true);
        navPanel.setPaddings(3);

        BorderLayoutData centerData = new BorderLayoutData(RegionPosition.CENTER);
        centerData.setMargins(3, 0, 3, 3);

        BorderLayoutData westData = new BorderLayoutData(RegionPosition.SOUTH);
//        westData.setSplit(true);
        westData.setMargins(3, 3, 0, 3);
        westData.setCMargins(3, 3, 3, 3);

        ButtonListener buttonListener = new ButtonListenerAdapter(){
            public void onClick(Button button, EventObject e) {
                ApplicationViewFacade.unmaskAll();
                systemErrorWindow.hide();
            }
        };
        Button okButton = new Button(ApplicationMessages.MESSAGES.okButtonLabel());
        okButton.addListener(buttonListener);

        systemErrorWindow.setButtons(new Button[]{okButton});
        systemErrorWindow.setButtonAlign(Position.CENTER);
        systemErrorWindow.setResizable(false);
        systemErrorWindow.setDraggable(false);        
        systemErrorWindow.setTitle(ApplicationMessages.MESSAGES.systemErrorTitle());
        systemErrorWindow.setClosable(false);
        systemErrorWindow.setWidth(600);
        systemErrorWindow.setHeight(350);
        //systemErrorWindow.setPlain(true);
        systemErrorWindow.setModal(true);
        systemErrorWindow.setLayout(new BorderLayout());
        systemErrorWindow.add(tabPanel, centerData);
        systemErrorWindow.add(navPanel, westData);
        //systemErrorWindow.center();
        systemErrorWindow.show();


    }

    /**
     * Show error message box.
     */
    public static void showErrorAlert() {
        MessageBox.show(new MessageBoxConfig() {
            {
                setCls("error_popup");
                setClosable(false);
                setTitle(ApplicationMessages.MESSAGES.errorDlgTitleLabel());
                setMsg("<div class='appl-error-message'><img src='images/appl_error.png' alt=''/>" +
                        " " + ApplicationMessages.MESSAGES.errorDlgMessageLabel() + "</div>");
                setWidth(300);
                setButtons(MessageBox.OK);
                setIconCls("error-img");
                setCallback(new MessageBox.PromptCallback() {
                    public void execute(String btnID, String text) {

                    }
                });
            }
        });
    }

}
