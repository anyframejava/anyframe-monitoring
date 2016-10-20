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
package net.sf.infrared2.gwt.client.view.header;

import net.sf.infrared2.gwt.client.Engine;
import net.sf.infrared2.gwt.client.callback.InfraredCallback;
import net.sf.infrared2.gwt.client.i18n.ApplicationMessages;
import net.sf.infrared2.gwt.client.to.InitialStateTO;
import net.sf.infrared2.gwt.client.utils.BrowserUtils;
import net.sf.infrared2.gwt.client.view.dialog.SelectApplicationDialog;
import net.sf.infrared2.gwt.client.view.facade.ApplicationViewFacade;
import net.sf.infrared2.gwt.client.view.facade.HeaderActionLinksStack;
import net.sf.infrared2.gwt.client.view.images.ApplicationImages;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HTMLTable.ColumnFormatter;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.layout.HorizontalLayout;

/**
 * <b>HeaderPanel</b><p>Class holds all header components: <br>
 * such as application logo, horizontal separator, <br>
 * select application hyper link, selected application information text, <br>
 * "Navigation" and "Performance" labels.
 * 
 * @author Sergey Evluhin
 * @author Andrey Zavgorodniy<br> 
 * Copyright(c) 2008, Exadel Inc
 */
public class HeaderPanel extends Panel {

    private static final HeaderPanel headerPanel = new HeaderPanel();

    /**
     * GWT Label control for perform text as string of selected in select
     * application dialog applications
     */
    private static Label selectedApplInfoLabel;

    /**
     * Label component for show standard text if no application selected in
     * select application dialog
     */
    private static Label noSelectedApplInfoLabel;
    
    /** The refresh timer. */
    private Timer refreshTimer;

    /**
     * Return current instance of HeaderPanel.
     * 
     * @return <code>HeaderPanel</code> the instance of HeaderPanel
     */
    public static HeaderPanel getInstance() {
        return headerPanel;
    }

    /**
     * Default constructor.
     */
    public HeaderPanel() {
        super();
        this.setWidth("100%");

        /** Vertical main header panel holder. */
        VerticalPanel headerVerticalPanel = new VerticalPanel();
        headerVerticalPanel.setWidth("100%");

        headerVerticalPanel.add(this.getLogoPanel());

        /** Horizontal separator */
        Widget divElement = new HTML();
        divElement.setStylePrimaryName("blue-separator");
        headerVerticalPanel.add(divElement);

        /** First header row */
        Grid firstRowHolderPanel = new Grid(1, 2);
        firstRowHolderPanel.setWidth("100%");
        firstRowHolderPanel.setWidget(0, 0, this.getSelectApplicationTitlePanel());
        firstRowHolderPanel.setWidget(0, 1, this.getSelectedApplInfoSection());
        firstRowHolderPanel.getCellFormatter().setHorizontalAlignment(0, 0,
                        HasHorizontalAlignment.ALIGN_LEFT);
        firstRowHolderPanel.getCellFormatter().setHorizontalAlignment(0, 1,
                        HasHorizontalAlignment.ALIGN_LEFT);
        firstRowHolderPanel.getCellFormatter().setVerticalAlignment(0, 0,
                        HasVerticalAlignment.ALIGN_MIDDLE);
        firstRowHolderPanel.getCellFormatter().setVerticalAlignment(0, 1,
                        HasVerticalAlignment.ALIGN_MIDDLE);
        firstRowHolderPanel.getColumnFormatter().setWidth(0, "170px");
        firstRowHolderPanel.setStylePrimaryName("application-menu");
        headerVerticalPanel.add(firstRowHolderPanel);

        /** Main table - hold navigation and perfomance title panels */
        Grid secondRowMainGridTable = new Grid(1, 2);
        ColumnFormatter mainColumnFormatter = secondRowMainGridTable.getColumnFormatter();
        mainColumnFormatter.setWidth(0, "198px");
        secondRowMainGridTable.setWidth("100%");
        secondRowMainGridTable.setWidget(0, 0, this.getNavigationTitlePanel());
        secondRowMainGridTable.setWidget(0, 1, this.getPerfomanceHeaderSubtable());
        headerVerticalPanel.add(secondRowMainGridTable);
        this.add(headerVerticalPanel);
        this.initRefreshPeriod();
    }
    
    private void initRefreshPeriod(){
        refreshTimer  = new Timer() {
            public void run() {
                    BrowserUtils.reloadPage();
            }
        };
        Engine.getClient().getRefreshIntervalMillis(new InfraredCallback() {
            public void doSuccess(Object result) {
               if ((result instanceof InitialStateTO)&&(result!=null)){
                   int periodMillis = ((InitialStateTO)result).getSessionDuration();
                   refreshTimer.scheduleRepeating(periodMillis);
               }
            }
        });
    }

    /**
     * Get space us empty label
     * 
     * @return empty label for space
     */
    public Widget getSpace() {
        return new Label(" ");
    }

    /**
     * Show selected application info panel with current text .
     * 
     * @param text - the text for showing
     */
    public void showSelectedApplInfo(String text) {
        noSelectedApplInfoLabel.setVisible(false);
        selectedApplInfoLabel.setText(text);
        selectedApplInfoLabel.setVisible(true);
        ApplicationViewFacade.getApplicationLayout().doLayout();
    }

    /**
     * Hide selected application info and display default string.
     */
    public void hideSelectedApplInfo() {
        noSelectedApplInfoLabel.setVisible(true);
        selectedApplInfoLabel.setText("");
        selectedApplInfoLabel.setVisible(false);
    }

    /**
     * Get selected application info text.
     * 
     * @return string info about selected applications and instances
     */
    public String getSelectedApplicationInfoText() {
        if (selectedApplInfoLabel != null) {
            return selectedApplInfoLabel.getText();
        }
        return "";
    }

    /**
     * Selected application and instances info panel holder.
     * 
     * @return selected application info panel as Horizontal layout.
     */
    private Widget getSelectedApplInfoSection() {
        noSelectedApplInfoLabel = new Label(ApplicationMessages.MESSAGES
                        .noApplicationSelectedLabel());
        noSelectedApplInfoLabel.setStylePrimaryName("select-appl-info-initial");
        noSelectedApplInfoLabel.setVisible(true);

        selectedApplInfoLabel = new Label("");
        selectedApplInfoLabel.setStylePrimaryName("select-appl-info");
        selectedApplInfoLabel.setVisible(false);

        HorizontalPanel selectedApplInfoPanel = new HorizontalPanel();
        selectedApplInfoPanel.add(noSelectedApplInfoLabel);
        selectedApplInfoPanel.add(selectedApplInfoLabel);
        selectedApplInfoPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        return selectedApplInfoPanel;
    }

    /**
     * Get logo and header panel holder
     * 
     * @return grid widget with two columns: logo and header panels.
     */
    private Widget getLogoPanel() {
        Image logoImage = ApplicationImages.IMAGES.getApplicationLogo().createImage();
        logoImage.setTitle(ApplicationMessages.MESSAGES.applicationLogoTitle());

        /* Init header link actions */
        HeaderActionLinksStack.init();

        Panel linksPanel = HeaderLinkStackHolder.getInstance().getLinksStackPanel();
        linksPanel.setPaddings(0, 0, 10, 0);
        Grid logoPanel = new Grid(1, 2);
        logoPanel.setBorderWidth(0);
        logoPanel.setSize("100%", "100%");
        logoPanel.setWidget(0, 0, logoImage);
        logoPanel.setWidget(0, 1, linksPanel);
        logoPanel.getCellFormatter().setAlignment(0, 0, HasHorizontalAlignment.ALIGN_LEFT,
                        HasVerticalAlignment.ALIGN_BOTTOM);
        logoPanel.setCellPadding(2);
        logoPanel.getCellFormatter().setStylePrimaryName(0, 0, "header");
        logoPanel.getCellFormatter().setAlignment(0, 1, HasHorizontalAlignment.ALIGN_RIGHT,
                        HasVerticalAlignment.ALIGN_BOTTOM);
        // logoPanel.getCellFormatter().setWidth(0, 1, "100px");
        logoPanel.getCellFormatter().setStylePrimaryName(0, 1, "ia-header-menu");
        return logoPanel;
    }

    /**
     * Get subtable of perfomance and inclusive mode title panels
     * 
     * @return perfomance header FlexTable widget.
     */
    private Widget getPerfomanceHeaderSubtable() {
        Grid rightGridTable = new Grid(1, 2);
        ColumnFormatter columnFormatter = rightGridTable.getColumnFormatter();
        columnFormatter.setWidth(0, "90%");
        columnFormatter.setWidth(1, "10%");
        rightGridTable.setWidth("100%");
        rightGridTable.getCellFormatter()
                        .setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_TOP);
        rightGridTable.getCellFormatter().setHorizontalAlignment(0, 1,
                        HasHorizontalAlignment.ALIGN_RIGHT);
        rightGridTable.setWidget(0, 0, this.getPerfomanceTitlePanel());
        // rightGridTable.setWidget(0, 1, this.getInclusiveModeTitlePanel());
        return rightGridTable;
    }

    /**
     * Get select application panel component
     * 
     * @return select application hyper link for select application showing as
     *         action
     */
    private Widget getSelectApplicationTitlePanel() {
        /** Select application link. */
        HTML link = new HTML();
        
        link.setText(ApplicationMessages.MESSAGES.selectApplicationLabel());
        link.setStylePrimaryName("button");
//        link.setStyleName("clickable");
        link.addClickListener(new ClickListener() {
            public void onClick(Widget sender) {
                showSelectApplDialog();
            }
        });
        return link;
    }

    /**
     * Show select application dialog.
     */
    private void showSelectApplDialog() {
        ApplicationViewFacade.maskAll(null);
        SelectApplicationDialog.getInstance().showDialog();
    }

    /**
     * Get perfomance information header panel, contain image and simple lable
     * inside.
     * 
     * @return perfomance information horizontal panel with two column
     */
    private Widget getPerfomanceTitlePanel() {
        Panel perfomanceTitleHorizPanel = new Panel();
        perfomanceTitleHorizPanel.setId("perfomance-title-horiz-panel");
        perfomanceTitleHorizPanel.setLayout(new HorizontalLayout(1));
        Label navPanelLabel2 = new Label(ApplicationMessages.MESSAGES.performanceInformationLabel());
        navPanelLabel2.setStyleName("title-header");
        perfomanceTitleHorizPanel.add(ApplicationImages.IMAGES.getTitleArrow().createImage());
        perfomanceTitleHorizPanel.add(navPanelLabel2);
        return perfomanceTitleHorizPanel;
    }

    /**
     * Get Navigation panel header holder, contain image and simple lable inside
     * 
     * @return Navigation title horizontal panel with two column
     */
    private Widget getNavigationTitlePanel() {
        Panel navigationTitleHorizPanel = new Panel();
        navigationTitleHorizPanel.setId("navigation-title-horiz-panel");
        navigationTitleHorizPanel.setLayout(new HorizontalLayout(1));
        Label navPanelLabel1 = new Label(ApplicationMessages.MESSAGES.navigationPanel());
        navPanelLabel1.setStylePrimaryName("title-header");
        navigationTitleHorizPanel.add(ApplicationImages.IMAGES.getTitleArrow().createImage());
        navigationTitleHorizPanel.add(navPanelLabel1);
        return navigationTitleHorizPanel;
    }
}
