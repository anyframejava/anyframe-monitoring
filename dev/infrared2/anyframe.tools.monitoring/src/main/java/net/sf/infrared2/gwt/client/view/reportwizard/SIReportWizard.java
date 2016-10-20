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
 * SIReportWizard.java		Date created: 18.02.2008
 * Last modified by: $Author: mike $
 * $Revision: 14572 $	$Date: 2009-06-10 20:06:30 +0900 (수, 10 6월 2009) $
 */

package net.sf.infrared2.gwt.client.view.reportwizard;

import net.sf.infrared2.gwt.client.Constants;
import net.sf.infrared2.gwt.client.Engine;
import net.sf.infrared2.gwt.client.callback.InfraredCallback;
import net.sf.infrared2.gwt.client.i18n.ApplicationMessages;
import net.sf.infrared2.gwt.client.report.ReportConfigTO;
import net.sf.infrared2.gwt.client.stack.StackPanelHolder;
import net.sf.infrared2.gwt.client.to.ApplicationEntryTO;
import net.sf.infrared2.gwt.client.to.report.ReportResultTO;
import net.sf.infrared2.gwt.client.users.session.UserSessionBean;
import net.sf.infrared2.gwt.client.utils.UIUtils;
import net.sf.infrared2.gwt.client.view.facade.ApplicationViewFacade;
import net.sf.infrared2.gwt.client.view.header.HeaderPanel;
import net.sf.infrared2.gwt.client.view.images.ApplicationImages;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Position;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.Checkbox;
import com.gwtext.client.widgets.form.FieldSet;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.CheckboxListenerAdapter;
import com.gwtext.client.widgets.layout.CardLayout;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.layout.HorizontalLayout;
import com.gwtext.client.widgets.layout.VerticalLayout;

/**
 * <b>SIReportWizard</b><p>Singleton class for generate modal window <br>
 * with CardLayout - holds five steps of different report formats 
 * 
 * @author Andrey Zavgorodniy <br>
 * Copyright Exadel Inc, 2008
 */
public class SIReportWizard extends Window {

    /** Current instance of <code>SIReportWizard</code> */
    public static SIReportWizard reportWizard = new SIReportWizard();
    /**
     * Constant perform default top queries value for fourth report wizard step -
     * spin button control
     */
    public static final String DEFAULT_TOP_QUERIES = "10";
    /** Maximum top queries or operations for spin button of the fourth step */
    public static final int MAX_TOP_QUERIES_OPERATIONS_VALUE = 50;

    /** Cancel ToolbarButton control */
    private static ToolbarButton cancelButton;
    /** Back ToolbarButton control */
    private static ToolbarButton backButton;
    /** Next ToolbarButton control */
    private static ToolbarButton nextButton;
    /** Finish ToolbarButton control */
    private static ToolbarButton finishButton;
    /** Down ToolbarButton control */
    private static ToolbarButton downloadButton;

    /**
     * Main wizard panel with CardLayout - contain all report steps
     */
    private static Panel wizardPanel;

    /** DesiredReportFormat section - first step content*/
    /**Perform uses MSExcel report format*/
    private static RadioButton excelFormatRB;
    /**Perform uses PDF report format*/
    private static RadioButton pdfFormatRB;
    /**Perform uses HTML report format*/
    private static RadioButton htmlFormatRB;
    /**Perform uses CSV report format*/
    private static RadioButton csvFormatRB;

    /** IncludeModes - second step content */
    /** Perform use in report statistic for absolute module data*/
    private static Checkbox absoluteModuleModeCheckBox;
    /** Perform use in report statistic for hierarchical module data*/
    private static Checkbox hierarchicalModuleModeCheckBox;

    /** IncludeLevels - third step content*/
    /** Perform data in report divided by applications */
    private static Checkbox applicationsCheckBox;
    /** Perform data in report for current selected application */
    private static RadioButton currentlySelectedRB;
    /** Perform data in report for all displayed */    
    private static RadioButton allDisplayedRB;

    /** Perform data in report divided by layers */
    private static Checkbox layerCheckBox;
    /** Include report data only for inclusive layer*/
    private static RadioButton inclusiveModeRB;
    /** Include report data only for exclusive layer*/
    private static RadioButton exclusiveModeRB;
    /** Include report data only for both layer*/
    private static RadioButton inclusiveExclusiveModeRB;

    /** IncludeInformation - fourth step content*/
    /** Perform in report only statistics information */
    private static CheckBox statisticsCheckBox;
    /** Perform in report only diagrams information */
    private static CheckBox diagramsCheckBox;
    /**
     * Widget consists of check box and label separated - horizontal layout for
     * statistic perform
     */
    private static Widget statisticComposite;
    /**
     * Widget consists of check box and label separated - horizontal layout for
     * diagrams perform
     */
    private static Widget diagramsComposite;    

    /** String for report header - show current step number in self*/
    private String headerWizardLabel = ApplicationMessages.MESSAGES.reportWizardHeader() + " "
                    + SIReportStep.FIRST_STEP;
    /** Index of current step */
    private int currentStep = 1;
    /** Transfer object for store all settings from report wizard steps and transfer to server*/
    private static ReportConfigTO reportConfig;
    /** Value of spin button control - need for top queries or operation perform*/
    private static int spinButtonValue = 10;
    /** Text editor for spin button control*/
    private static TextField spinComponentTextField;
    /** Text editor for show currently selected applications string - always disabled */
    private static TextField currSelecApplInfoTextField;
    /** Show instead of diagrams and statistics in step 4 when uses CSV mode*/
    private static Label csvInformationLineLabel;
    /** Final string for sending report Servlet to execution report */
    private String generatedLink;

    /** Indicates if there is only one application selected or merged mode */
    private static boolean isOnlyOneApplication = false;

    /**
     * @return the current instance of SIReportWizard
     */
    public static SIReportWizard getInstance() {
        return reportWizard;
    }

    /**
     * Default constructor.
     */
    public SIReportWizard() {
        this.setResizable(false);
        this.setClosable(false);
        this.setHeader(false);
        this.setBorder(false);
        this.setDraggable(false);
        this.setBodyBorder(false);
        this.setModal(true);
        this.setLayout(new FitLayout());
        this.setPixelSize(500, 250);
        initConrols();
        this.add(getWizardPanel());
        setInitialReportsState();
    }

    /**
     * ToolBar buttons click listener
     */
    private ButtonListenerAdapter listener = new ButtonListenerAdapter() {
        public void onClick(Button button, EventObject e) {
            String btnID = button.getId();
            CardLayout cardLayout = (CardLayout) wizardPanel.getLayout();
            String panelID = cardLayout.getActiveItem().getId();
            if (btnID.equals("move-prev")) {
                if (panelID.equals("card-1")) {
                    backButton.disable();
                    if (nextButton.isDisabled()) {
                        nextButton.enable();
                    }
                    if (finishButton.isDisabled()) {
                        finishButton.enable();
                    }
                    wizardPanel.setTitle(ApplicationMessages.MESSAGES.reportWizardHeader() + " "
                                    + SIReportStep.FIRST_STEP);
                } else if (panelID.equals("card-2")) {
                    cardLayout.setActiveItem(0);
                    backButton.disable();
                    nextButton.enable();
                    if (isCorrectState())
                        finishButton.enable();
                    wizardPanel.setTitle(ApplicationMessages.MESSAGES.reportWizardHeader() + " "
                                    + SIReportStep.FIRST_STEP);
                } else if (panelID.equals("card-3")) {
                    cardLayout.setActiveItem(1);
                    nextButton.enable();
                    if (isCorrectState())
                        finishButton.enable();
                    wizardPanel.setTitle(ApplicationMessages.MESSAGES.reportWizardHeader() + " "
                                    + SIReportStep.SECOND_STEP);
                } else {
                    cardLayout.setActiveItem(2);
                    nextButton.enable();
                    if (isCorrectState())
                        finishButton.enable();
                    wizardPanel.setTitle(ApplicationMessages.MESSAGES.reportWizardHeader() + " "
                                    + SIReportStep.THIRD_STEP);
                }
            } else if (btnID.equals("cancel")) {
                hideWizard();
                resetTodefaultSettings(cardLayout);
            }

            else if (btnID.equals("move-next")) {
                if (panelID.equals("card-1")) {
                    cardLayout.setActiveItem(1);
                    backButton.enable();
                    if (isCorrectState())
                        finishButton.enable();
                    if (absoluteModuleModeCheckBox.getValue()
                                    || hierarchicalModuleModeCheckBox.getValue())
                        nextButton.enable();
                    else
                        nextButton.disable();
                    wizardPanel.setTitle(ApplicationMessages.MESSAGES.reportWizardHeader() + " "
                                    + SIReportStep.SECOND_STEP);
                } else if (panelID.equals("card-2")) {
                    thirdStepInitialState();
                    cardLayout.setActiveItem(2);
                    if (isCorrectState())
                        finishButton.enable();
                    if (applicationsCheckBox.getValue() || layerCheckBox.getValue())
                        nextButton.enable();
                    else
                        nextButton.disable();
                    wizardPanel.setTitle(ApplicationMessages.MESSAGES.reportWizardHeader() + " "
                                    + SIReportStep.THIRD_STEP);
                } else if (panelID.equals("card-3")) {
                    cardLayout.setActiveItem(3);
                    fourthStepInitialState();
                    if (isCorrectState())
                        finishButton.enable();
                    wizardPanel.setTitle(ApplicationMessages.MESSAGES.reportWizardHeader() + " "
                                    + SIReportStep.FOURTH_STEP);
                }
            }
            wizardPanel.doLayout();
        }
    };

    /**
     * Perform report is report in correct state.
     * @return valid or not-valid report state
     */
    private static boolean isCorrectState() {
        boolean result;
        try {
            result = (absoluteModuleModeCheckBox.getValue() || hierarchicalModuleModeCheckBox
                            .getValue())
                            && (applicationsCheckBox.getValue() || layerCheckBox.getValue())
                            && (statisticsCheckBox.isChecked() || diagramsCheckBox.isChecked())
                            && Integer.parseInt(spinComponentTextField.getValueAsString()) > 0;
        } catch (NumberFormatException e) {
            result = false;
        }
        return result;
    }

    /**
     * Reset to default settings - first step displayed. 
     * @param cardLayout - the first card layout as initial state 
     */
    private void resetTodefaultSettings(CardLayout cardLayout) {
        cardLayout.setActiveItem(0);
        wizardPanel.setTitle(ApplicationMessages.MESSAGES.reportWizardHeader() + " "
                        + SIReportStep.FIRST_STEP);
        setInitialReportsState();
    }

    /**
     * Third step initial state.
     */
    private void thirdStepInitialState() {
        if (!UserSessionBean.getApplConfigBeanInstance().isDividedByApplications()) {
            currSelecApplInfoTextField.setValue(ApplicationMessages.MESSAGES.mergedInformation());
            allDisplayedRB.setEnabled(false);
        } else {
            String currentSelection = "";
            if (StackPanelHolder.getInstance() != null
                            && StackPanelHolder.getInstance().getSelectedLink() != null) {
                ApplicationEntryTO applEntryTO = StackPanelHolder.getInstance().getSelectedLink()
                                .getNavigatorEntryTO().getApplications();
                if (applEntryTO != null) {
                    currentSelection = applEntryTO.getApplicationTitle() + ", "
                                    + applEntryTO.getInstanceList()[0].getName();
                } else {
                    currentSelection = HeaderPanel.getInstance().getSelectedApplicationInfoText();
                }
            } else
                currentSelection = HeaderPanel.getInstance().getSelectedApplicationInfoText();
            currSelecApplInfoTextField.setValue(currentSelection);
            ApplicationEntryTO[] applEntryTOArray = UserSessionBean.getApplConfigBeanInstance()
                            .getApplicationList();
            if ((applEntryTOArray == null || applEntryTOArray.length <= 1)
                            || !UserSessionBean.getApplConfigBeanInstance()
                                            .isDividedByApplications()) {
                isOnlyOneApplication = true;
                allDisplayedRB.setEnabled(false);
                // reportConfig.setCurrentlySelectedString(currSelecApplInfoTextField.getValueAsString());
            } else {
                allDisplayedRB.setEnabled(true);
            }
        }
    }

    /**
     * Fourth step initial state.
     */
    private void fourthStepInitialState() {
        nextButton.disable();
        if (csvFormatRB != null) {
            if (csvFormatRB.isChecked()) {
                finishButton.enable();
                statisticComposite.setVisible(false);
                diagramsComposite.setVisible(false);
                csvInformationLineLabel.setVisible(true);
            } else {
                statisticComposite.setVisible(true);
                diagramsComposite.setVisible(true);
                csvInformationLineLabel.setVisible(false);
            }
        }
    }

    /**
     * Finish step initial state.
     */
    private void fifthStepInitialState() {
        /* hide buttons */
        nextButton.hide();
        backButton.hide();
        finishButton.hide();
        downloadButton.enable();
    }

    /**
     * Init buttons ToolBar.
     * 
     * @return - the ToolBar panel holds all navigated ToolBar buttons
     */
    private Toolbar initButtonsToolBar() {
        Toolbar toolbar = new Toolbar();
        cancelButton = new ToolbarButton(ApplicationMessages.MESSAGES.cancelButtonLabel(), listener);
        cancelButton.setId("cancel");
        toolbar.addSpacer();
        toolbar.addSpacer();
        toolbar.addSpacer();
        toolbar.addSpacer();
        toolbar.addSpacer();
        toolbar.addButton(cancelButton);
        toolbar.addSpacer();
        toolbar.addSpacer();
        backButton = new ToolbarButton(ApplicationMessages.MESSAGES.backButtonLabel(), listener);
        backButton.setId("move-prev");
        toolbar.addButton(backButton);
        nextButton = new ToolbarButton(ApplicationMessages.MESSAGES.nextButtonLabel(), listener);
        nextButton.setId("move-next");
        toolbar.addButton(nextButton);
        toolbar.addSpacer();
        toolbar.addSpacer();
        finishButton = new ToolbarButton(ApplicationMessages.MESSAGES.finishButtonLabel(),
                        new ButtonListenerAdapter() {

                            public void onClick(Button button, EventObject e) {
                                wizardPanel.getEl().mask(ApplicationMessages.MESSAGES.generateReport());
                                saveReportConfig();
                                prepareSummaryInfo();
                                Engine
                                                .getClient()
                                                .generateReport(
                                                                UserSessionBean
                                                                                .getApplConfigBeanInstance(),
                                                                reportConfig,
                                                                ApplicationMessages.MESSAGES
                                                                                .otherOperationsGraphic(),
                                                                getCallback());

                            }

                        });
        finishButton.setId("finish");
        toolbar.addButton(finishButton);
        toolbar.addSpacer();
        downloadButton = new ToolbarButton(ApplicationMessages.MESSAGES.download(),
                        new ButtonListenerAdapter() {
                            public void onClick(Button button, EventObject e) {
                                if (generatedLink!=null){
                                    if (reportConfig.isHtmlFormat()){
                                      com.google.gwt.user.client.Window.open(generatedLink, "_blank", "");
                                    }else{
                                      changeLocation(generatedLink);
                                    }
                                }
                                resetTodefaultSettings((CardLayout) wizardPanel.getLayout());
                                hideWizard();
                            }
                        });
        downloadButton.setId("download");
        toolbar.addButton(downloadButton);
        downloadButton.hide();
        return toolbar;
    }

    /**
     * Create main report wizard layout.
     * 
     * @return - the report wizard panel
     */
    public Panel getWizardPanel() {
        wizardPanel = new Panel();
        wizardPanel.setPixelSize(500, 250);
        wizardPanel.setTitle(headerWizardLabel);
        wizardPanel.setLayout(new CardLayout());
        wizardPanel.setBodyBorder(false);
        wizardPanel.setBorder(false);
        wizardPanel.setActiveItem(0);
        wizardPanel.setPaddings(5);
        wizardPanel.setBottomToolbar(initButtonsToolBar());
        wizardPanel.setButtonAlign(Position.CENTER);

        Panel first = new Panel();
        first.setBorder(false);
        first.setId("card-1");
        first.setLayout(new FitLayout());
        first.add(firstStepReportContent());

        Panel second = new Panel();
        second.setBorder(false);
        second.setId("card-2");
        second.setLayout(new FitLayout());
        second.add(secondStepReportContent());

        Panel third = new Panel();
        third.setBorder(false);
        third.setId("card-3");
        third.setLayout(new FitLayout());
        third.add(thirdStepReportContent());

        Panel fourth = new Panel();
        fourth.setBorder(false);
        fourth.setLayout(new FitLayout());
        fourth.setId("card-4");
        fourth.add(fourthStepReportContent());

        Panel fifth = new Panel();
        fifth.setBorder(false);
        fifth.setLayout(new FitLayout());
        fifth.setId("card-5");
        fifth.add(fifthStepReportContent());

        wizardPanel.add(first);
        wizardPanel.add(second);
        wizardPanel.add(third);
        wizardPanel.add(fourth);
        wizardPanel.add(fifth);
        return wizardPanel;
    }

    /**
     * Init report wizard controls.
     */
    private static void initConrols() {
        /* First step */
        excelFormatRB = new RadioButton("desired-report-group");
        pdfFormatRB = new RadioButton("desired-report-group");
        htmlFormatRB = new RadioButton("desired-report-group");
        csvFormatRB = new RadioButton("desired-report-group");

        /* second step */
        absoluteModuleModeCheckBox = new Checkbox();
        hierarchicalModuleModeCheckBox = new Checkbox();

        /* third */
        currSelecApplInfoTextField = new TextField();
        currSelecApplInfoTextField.setWidth("300");
        currSelecApplInfoTextField.disable();
        applicationsCheckBox = new Checkbox();
        currentlySelectedRB = new RadioButton("incl-level-grp-appl");
        allDisplayedRB = new RadioButton("incl-level-grp-appl");
        layerCheckBox = new Checkbox();
        inclusiveModeRB = new RadioButton("incl-level-grp-layer");
        exclusiveModeRB = new RadioButton("incl-level-grp-layer");
        inclusiveExclusiveModeRB = new RadioButton("incl-level-grp-layer");

        /* fourth */
        csvInformationLineLabel = new Label();
        csvInformationLineLabel.setText(ApplicationMessages.MESSAGES.csvFormatOnlyInfo());
        csvInformationLineLabel.setVisible(false);
        statisticsCheckBox = new CheckBox();
        diagramsCheckBox = new CheckBox();
        statisticComposite = UIUtils.getCompositeCheckBox(statisticsCheckBox,ApplicationMessages.MESSAGES.statistic());
        diagramsComposite = UIUtils.getCompositeCheckBox(diagramsCheckBox, ApplicationMessages.MESSAGES.diagrams());


        String currentSelection = "";
        if (!UserSessionBean.getApplConfigBeanInstance().isDividedByApplications()) {
            currSelecApplInfoTextField.setValue(ApplicationMessages.MESSAGES.mergedInformation());
        } else if (StackPanelHolder.getInstance() != null
                        && StackPanelHolder.getInstance().getSelectedLink() != null) {
            ApplicationEntryTO applEntryTO = StackPanelHolder.getInstance().getSelectedLink()
                            .getNavigatorEntryTO().getApplications();
            if (applEntryTO != null) {
                currentSelection = applEntryTO.getApplicationTitle() + ", "
                                + applEntryTO.getInstanceList()[0].getName();
            } else {
                currentSelection = HeaderPanel.getInstance().getSelectedApplicationInfoText();
            }
        } else
            currentSelection = HeaderPanel.getInstance().getSelectedApplicationInfoText();
        currSelecApplInfoTextField.setValue(currentSelection);
    }

    /**
     * Set initial reports state.
     */
    private void setInitialReportsState() {
        /* First step */
        excelFormatRB.setChecked(true);
        pdfFormatRB.setChecked(false);
        htmlFormatRB.setChecked(false);
        csvFormatRB.setChecked(false);

        /* second step */
        absoluteModuleModeCheckBox.setChecked(true);
        hierarchicalModuleModeCheckBox.setChecked(false);

        /* third */
        applicationsCheckBox.setChecked(true);
        currentlySelectedRB.setChecked(true);
        allDisplayedRB.setChecked(false);
        layerCheckBox.setChecked(true);
        inclusiveModeRB.setChecked(true);
        exclusiveModeRB.setChecked(false);
        inclusiveExclusiveModeRB.setChecked(false);

        /* fourth */
        spinComponentTextField.setValue(DEFAULT_TOP_QUERIES);
        statisticsCheckBox.setChecked(true);
        diagramsCheckBox.setChecked(true);

        if (backButton != null) {
            backButton.disable();
        }
        if (nextButton != null) {
            nextButton.enable();
        }
        nextButton.show();
        backButton.show();
        finishButton.show();
        downloadButton.hide();
    }

    /**
     * Create first-step report content for wizard.
     * 
     * @return the FieldSet vertical layout contained first step report controls
     */
    private static Widget firstStepReportContent() {
        FieldSet selectDesiredFieldSet = new FieldSet(ApplicationMessages.MESSAGES
                        .selectDesiredRepoFormat());
        selectDesiredFieldSet.setLabelWidth(70);
        Panel radioButtonsPanel = new Panel();
        radioButtonsPanel.setBorder(false);
        radioButtonsPanel.setPaddings(2);
        radioButtonsPanel.setLayout(new VerticalLayout());
        radioButtonsPanel.add(UIUtils.getCompositeRadioButton(excelFormatRB,
                        ApplicationMessages.MESSAGES.excelFormat()));
        radioButtonsPanel.add(UIUtils.getCompositeRadioButton(pdfFormatRB,
                        ApplicationMessages.MESSAGES.pdfFormat()));
        radioButtonsPanel.add(UIUtils.getCompositeRadioButton(htmlFormatRB,
                        ApplicationMessages.MESSAGES.htmlFormat()));
        radioButtonsPanel.add(UIUtils.getCompositeRadioButton(csvFormatRB,
                        ApplicationMessages.MESSAGES.csvFormat()));
        selectDesiredFieldSet.add(radioButtonsPanel);
        return selectDesiredFieldSet;
    }

    /**
     * Create second-step report content panel.
     * 
     * @return the FieldSet vertical layout contained second step report controls
     */
    private static Panel secondStepReportContent() {
        FieldSet includeModesFieldSet = new FieldSet(ApplicationMessages.MESSAGES
                        .includeModeLabel());
        includeModesFieldSet.setLabelWidth(70);
        Panel radioButtonsPanel = new Panel();
        radioButtonsPanel.setBodyBorder(false);
        radioButtonsPanel.setBodyBorder(false);
        radioButtonsPanel.setLayout(new VerticalLayout());
        CheckboxListenerAdapter secondWizardButtonsListener = new CheckboxListenerAdapter() {
            public void onCheck(Checkbox field, boolean checked) {
                if (absoluteModuleModeCheckBox.getValue() == false
                                && hierarchicalModuleModeCheckBox.getValue() == false) {
                    nextButton.disable();
                    finishButton.disable();
                } else {
                    nextButton.enable();
                    if (isCorrectState())
                        finishButton.enable();
                }
            }
        };
        absoluteModuleModeCheckBox.addListener(secondWizardButtonsListener);
        hierarchicalModuleModeCheckBox.addListener(secondWizardButtonsListener);
        radioButtonsPanel.add(UIUtils.getCompositeCheckBox(absoluteModuleModeCheckBox, ApplicationMessages.MESSAGES.absoluteModuleMode()));
        radioButtonsPanel.add(UIUtils.getCompositeCheckBox(hierarchicalModuleModeCheckBox, ApplicationMessages.MESSAGES.hierarchicalModuleMode()));
        includeModesFieldSet.add(radioButtonsPanel);
        return includeModesFieldSet;
    }

    /**
     * Create third-step report content panel.
     * 
     * @return the FieldSet vertical layout contained third step report controls
     */
    private static Panel thirdStepReportContent() {
        FieldSet includeLevelsFieldSet = new FieldSet(ApplicationMessages.MESSAGES.includeLevels());
        includeLevelsFieldSet.setLabelWidth(70);
        /* content */
        Panel radioButtonsPanel = new Panel();
        radioButtonsPanel.setBorder(false);
        radioButtonsPanel.setBodyBorder(false);
        radioButtonsPanel.setLayout(new VerticalLayout());
        CheckboxListenerAdapter thirdWizardButtonsListener = new CheckboxListenerAdapter() {
            public void onCheck(Checkbox field, boolean checked) {
                ApplicationEntryTO[] applEntryTOArray = UserSessionBean.getApplConfigBeanInstance()
                                .getApplicationList();
                if ((applEntryTOArray == null || applEntryTOArray.length <= 1)
                                || !UserSessionBean.getApplConfigBeanInstance()
                                                .isDividedByApplications()) {
                    isOnlyOneApplication = true;
                }
                if (applicationsCheckBox.getValue() == false && layerCheckBox.getValue() == false) {
                    allDisplayedRB.setEnabled(false);
                    currentlySelectedRB.setEnabled(false);
                    inclusiveModeRB.setEnabled(false);
                    exclusiveModeRB.setEnabled(false);
                    inclusiveExclusiveModeRB.setEnabled(false);
                    finishButton.disable();
                    nextButton.disable();
                } else if (layerCheckBox.getValue() == false) {
                    inclusiveModeRB.setEnabled(false);
                    exclusiveModeRB.setEnabled(false);
                    inclusiveExclusiveModeRB.setEnabled(false);
                    if (!isOnlyOneApplication)
                        allDisplayedRB.setEnabled(true);
                    currentlySelectedRB.setEnabled(true);
                    if (isCorrectState())
                        finishButton.enable();
                    nextButton.enable();
                } else if (applicationsCheckBox.getValue() == false) {
                    inclusiveModeRB.setEnabled(true);
                    exclusiveModeRB.setEnabled(true);
                    inclusiveExclusiveModeRB.setEnabled(true);
                    if (!isOnlyOneApplication)
                        allDisplayedRB.setEnabled(true);
                    currentlySelectedRB.setEnabled(true);
                    if (isCorrectState())
                        finishButton.enable();
                    nextButton.enable();
                } else {
                    inclusiveModeRB.setEnabled(true);
                    exclusiveModeRB.setEnabled(true);
                    inclusiveExclusiveModeRB.setEnabled(true);
                    if (!isOnlyOneApplication)
                        allDisplayedRB.setEnabled(true);
                    currentlySelectedRB.setEnabled(true);
                    if (isCorrectState())
                        finishButton.enable();
                    nextButton.enable();
                }
            }
        };
        applicationsCheckBox.addListener(thirdWizardButtonsListener);
        
        radioButtonsPanel.add(UIUtils.getCompositeCheckBox(applicationsCheckBox, ApplicationMessages.MESSAGES.applications()));
        Panel currSelectedPanel = new Panel();
        currSelectedPanel.setBodyBorder(false);
        currSelectedPanel.setBorder(false);
        currSelectedPanel.setLayout(new HorizontalLayout(3));
        currSelectedPanel.add(UIUtils.getCompositeRadioButton(currentlySelectedRB,
                        ApplicationMessages.MESSAGES.currentlySelected()));
        currSelectedPanel.add(currSelecApplInfoTextField);
        radioButtonsPanel.add(currSelectedPanel);
        radioButtonsPanel.add(UIUtils.getCompositeRadioButton(allDisplayedRB,
                        ApplicationMessages.MESSAGES.allDisplayed()));
        layerCheckBox.addListener(thirdWizardButtonsListener);
        radioButtonsPanel.add(UIUtils.getCompositeCheckBox(layerCheckBox,ApplicationMessages.MESSAGES.layer()));
        radioButtonsPanel.add(UIUtils.getCompositeRadioButton(inclusiveModeRB,
                        ApplicationMessages.MESSAGES.inclusiveReportMode()));
        radioButtonsPanel.add(UIUtils.getCompositeRadioButton(exclusiveModeRB,
                        ApplicationMessages.MESSAGES.exclusiveReportMode()));
        radioButtonsPanel.add(UIUtils.getCompositeRadioButton(inclusiveExclusiveModeRB,
                        ApplicationMessages.MESSAGES.inclusiveExclusiveReportMode()));
        includeLevelsFieldSet.add(radioButtonsPanel);
        return includeLevelsFieldSet;
    }

    /**
     * Create fourth-step report content panel.
     * 
     * @return the FieldSet vertical layout contained fourth step report controls
     */
    private static Panel fourthStepReportContent() {
        FieldSet includeInformationFieldSet = new FieldSet(ApplicationMessages.MESSAGES
                        .includeInformation());
        includeInformationFieldSet.setLabelWidth(70);
        Panel radioButtonsPanel = new Panel();
        radioButtonsPanel.setId("fourth-radioBtns-panel");
        radioButtonsPanel.setBorder(false);
        radioButtonsPanel.setLayout(new VerticalLayout());
        radioButtonsPanel.add(spinButtonWidget(MAX_TOP_QUERIES_OPERATIONS_VALUE));
        ClickListener fourthWizardButtonsListener = new ClickListener() {
            public void onClick(Widget arg0) {
                if (!statisticsCheckBox.isChecked() && !diagramsCheckBox.isChecked()) {
                    finishButton.disable();
                } else {
                    if (isCorrectState())
                        finishButton.enable();
                }
            }
        };

        statisticsCheckBox.addClickListener(fourthWizardButtonsListener);
        diagramsCheckBox.addClickListener(fourthWizardButtonsListener);
        if (!csvFormatRB.isChecked()) {
            radioButtonsPanel.add(statisticComposite);
            radioButtonsPanel.add(diagramsComposite);
        }
        radioButtonsPanel.add(csvInformationLineLabel);
        includeInformationFieldSet.add(radioButtonsPanel);
        return includeInformationFieldSet;
    }

    /**
     * Create fifth step of wizard.
     * 
     * @return the panel vertical layout contained summary selected information
     */
    private Panel fifthStepReportContent() {
        Panel fifthStepMainVerticalPanel = new Panel();
        fifthStepMainVerticalPanel.setBodyBorder(false);
        fifthStepMainVerticalPanel.setBorder(false);
        fifthStepMainVerticalPanel.setId("fifth-step-main-panel");
        fifthStepMainVerticalPanel.setLayout(new VerticalLayout(5));
        /* top info bar */
        Panel topInfoPanel = new Panel();
        topInfoPanel.setLayout(new HorizontalLayout(1));
        Label generationCompleteLbl = new Label(ApplicationMessages.MESSAGES
                        .reportGenerationComplete());
        generationCompleteLbl.setStylePrimaryName("report-header-text ");
        topInfoPanel.add(generationCompleteLbl);
        fifthStepMainVerticalPanel.add(topInfoPanel);
        FieldSet summaryFieldSet = new FieldSet(ApplicationMessages.MESSAGES.summary());
        summaryFieldSet.setId("fifth-step-field-set");
        summaryFieldSet.setLabelWidth(70);
        summaryFieldSet.setWidth("100%");
        /* content */
        summaryFieldSet.add(this.summaryInfoPanel());
        fifthStepMainVerticalPanel.add(summaryFieldSet);
        /* bottom info bar */
        Panel bottomInfoPanel = new Panel();
        bottomInfoPanel.setLayout(new HorizontalLayout(1));
        bottomInfoPanel.setMargins(3);
        Label bottomLabel = new Label(ApplicationMessages.MESSAGES.askingDownloadReport());
        bottomLabel.setStylePrimaryName("report-header-text ");
        bottomInfoPanel.add(bottomLabel);
        fifthStepMainVerticalPanel.add(bottomInfoPanel);
        return fifthStepMainVerticalPanel;
    }

    /**
     * Body for fifth step summary content.
     * 
     * @return Panel - the vertical panel summary data.
     */
    private Panel summaryInfoPanel() {
        final Panel mainPanel = new Panel();
        mainPanel.setLayout(new VerticalLayout(5));
        mainPanel.setBodyBorder(false);
        mainPanel.setBorder(false);
        mainPanel.add(this.firstStepSummaryPanel());
        mainPanel.add(this.secondStepSummaryPanel());
        mainPanel.add(this.thirdStepSummaryPanel());
        mainPanel.add(this.fourthStepSummaryPanel());
        return mainPanel;
    }

    /**
     * Prepare summary info.
     */
    private void prepareSummaryInfo() {
        Panel panel = (Panel) wizardPanel.getComponent("card-5");
        if (panel != null) {
            // panel.clear();
            panel.remove("fifth-step-main-panel");
            panel.add(this.fifthStepReportContent());
            panel.doLayout();
        }
        wizardPanel.doLayout();
    }

    /**
     * First step summary panel.
     * 
     * @return Panel - the panel with HorizontalLayout for first step summary info.
     */
    private Panel firstStepSummaryPanel() {
        Panel firstStepPanel = new Panel();
        firstStepPanel.setLayout(new HorizontalLayout(1));
        firstStepPanel.setBodyBorder(false);
        firstStepPanel.setBorder(false);
        Label text = new Label(ApplicationMessages.MESSAGES.desiredRepoFormat() + ":");
        text.setStylePrimaryName("report-summary-label-text");
        String infoText = "";
        if (reportConfig != null) {
            if (reportConfig.isExcelFormat()) {
                infoText += ApplicationMessages.MESSAGES.excelFormat();
            }
            if (reportConfig.isPdfFormat()) {
                infoText += ApplicationMessages.MESSAGES.pdfFormat();
            }
            if (reportConfig.isHtmlFormat()) {
                infoText += ApplicationMessages.MESSAGES.htmlFormat();
            }
            if (reportConfig.isCsvFormat()) {
                infoText += ApplicationMessages.MESSAGES.csvFormat();
            }
        }
        firstStepPanel.add(text);
        firstStepPanel.add(new Label(infoText));
        return firstStepPanel;
    }

    /**
     * Second step summary panel.
     * 
     * @return - the panel with HorizontalLayout for second step summary info.
     */
    private Panel secondStepSummaryPanel() {
        Panel secondStepPanel = new Panel();
        secondStepPanel.setLayout(new HorizontalLayout(1));
        secondStepPanel.setBodyBorder(false);
        secondStepPanel.setBorder(false);
        Label headerLabel = new Label(ApplicationMessages.MESSAGES.reportIncludeModes());
        headerLabel.setStylePrimaryName("report-summary-label-text");
        String text = "";
        if (reportConfig != null) {
            if (reportConfig.isAbsoluteModuleMode()) {
                text += ApplicationMessages.MESSAGES.absoluteModuleStackLabel();
                if (reportConfig.isHierarchicalModuleMode()) {
                    text += ", " + ApplicationMessages.MESSAGES.hierarchicalModuleStackLabel();
                }
            } else {
                text += ApplicationMessages.MESSAGES.hierarchicalModuleStackLabel();
            }
        }
        secondStepPanel.add(headerLabel);
        secondStepPanel.add(new Label(text));
        return secondStepPanel;
    }

    /**
     * Third step summary panel.
     * 
     * @return - the panel with HorizontalLayout for third step summary info.
     */
    private Panel thirdStepSummaryPanel() {
        Panel thirdStepPanel = new Panel();
        thirdStepPanel.setLayout(new HorizontalLayout(1));
        thirdStepPanel.setBodyBorder(false);
        thirdStepPanel.setBorder(false);
        Label headerLabel = new Label(ApplicationMessages.MESSAGES.reportIncludeLevels());
        headerLabel.setStylePrimaryName("report-summary-label-text");
        String text = "";
        if (reportConfig != null) {
            if (reportConfig.isApplications()) {
                text += ApplicationMessages.MESSAGES.applicationLavel();
                if (reportConfig.isCurrentlySelected()) {
                    text += " for " + ApplicationMessages.MESSAGES.currentlySelected();
                } else {
                    text += " for " + ApplicationMessages.MESSAGES.allDisplayed() + " "
                                    + ApplicationMessages.MESSAGES.applications();
                }
            }
            if (reportConfig.isLayer()) {
                if (text.length() > 1) {
                    text += ", ";
                }
                text += ApplicationMessages.MESSAGES.layerLevel() + " in ";
                if (reportConfig.isInclusiveMode()) {
                    text += ApplicationMessages.MESSAGES.inclusiveReportMode();
                } else if (reportConfig.isExclusiveMode()) {
                    text += ApplicationMessages.MESSAGES.exclusiveReportMode();
                } else {
                    text += ApplicationMessages.MESSAGES.inclusiveExclusiveReportMode();
                }
            }
        }
        thirdStepPanel.add(headerLabel);
        thirdStepPanel.add(new Label(text));
        return thirdStepPanel;
    }

    /**
     * Third step summary panel.
     * 
     * @return - the panel with HorizontalLayout for fourth step summary info.
     */
    private Panel fourthStepSummaryPanel() {
        Panel fourthStepPanel = new Panel();
        fourthStepPanel.setBodyBorder(false);
        fourthStepPanel.setBorder(false);
        Label headerLabel = new Label(ApplicationMessages.MESSAGES.includeInformation() + ":");
        headerLabel.setStylePrimaryName("report-summary-label-text");
        String text = "";
        if (reportConfig != null) {
            if (reportConfig.isStatistics()) {
                text += ApplicationMessages.MESSAGES.statistic();
                text += " with Top " + reportConfig.getQueries() + " "
                                + ApplicationMessages.MESSAGES.reportOperationsQueries();
                if (reportConfig.isDiagrams()) {
                    text += ", " + ApplicationMessages.MESSAGES.diagrams();
                }
            } else {
                text += ApplicationMessages.MESSAGES.diagrams();
            }
        }
        fourthStepPanel.add(headerLabel);
        fourthStepPanel.add(new Label(text));
        return fourthStepPanel;
    }

    /**
     * Change URL.
     * 
     * @param loc - URL to the next location.
     */
    private native void changeLocation(String loc)/*-{
                      document.location.href=loc;
                      }-*/;

    /**
     * Create spin button widget .
     * 
     * @param maxValue - the max value of spin component
     * @return - the spin button widget
     */
    private static Widget spinButtonWidget(final int maxValue) {
        Panel mainPanel = new Panel();
        mainPanel.setLayout(new HorizontalLayout(1));
        // mainPanel.setWidth(400);
        mainPanel.add(new HTML(ApplicationMessages.MESSAGES.topQueriesOperationsCmbLabel()
                        + "<span>&nbsp;&nbsp;</span>"));
        spinComponentTextField = new TextField();
        spinComponentTextField.setReadOnly(true);
        spinComponentTextField.setValue(String.valueOf(DEFAULT_TOP_QUERIES));
        spinComponentTextField.setWidth(80);
        spinComponentTextField.setHeight(25);
        spinComponentTextField.setStylePrimaryName("spin-textfield-effect-style");
        spinComponentTextField.setAllowBlank(false);

        mainPanel.add(spinComponentTextField);
        Panel buttonsVerticalPanel = new Panel();
        buttonsVerticalPanel.setLayout(new VerticalLayout());
        buttonsVerticalPanel.setWidth(14);
        buttonsVerticalPanel.setHeight(26);
        buttonsVerticalPanel.add(upSpinButton());
        buttonsVerticalPanel.add(downSpinButton());
        mainPanel.add(buttonsVerticalPanel);
        return mainPanel;
    }

    /**
     * Up button widget for spin button control.
     * 
     * @return - the UIObject emulate "up" button
     */
    private static Widget upSpinButton() {
        Image button = ApplicationImages.IMAGES.getUpImage().createImage();
        button.setStylePrimaryName("spin-button-effect-style");
        button.addClickListener(new ClickListener() {
            public void onClick(Widget arg0) {
                if (Integer.parseInt(spinComponentTextField.getValueAsString()) < MAX_TOP_QUERIES_OPERATIONS_VALUE) {
                    spinButtonValue = Integer.parseInt(spinComponentTextField.getValueAsString());
                    spinButtonValue++;
                    spinComponentTextField.setValue(String.valueOf(spinButtonValue));
                }
            }
        });
        return button;
    }

    /**
     * Down button widget for spin button control.
     * 
     * @return - the UIObject emulate "down" button
     */
    private static Widget downSpinButton() {
        Image button = ApplicationImages.IMAGES.getDownImage().createImage();
        button.setStylePrimaryName("spin-button-effect-style");
        button.addClickListener(new ClickListener() {
            public void onClick(Widget arg0) {
                if (Integer.parseInt(spinComponentTextField.getValueAsString()) > 1) {
                    spinButtonValue = Integer.parseInt(spinComponentTextField.getValueAsString());
                    spinButtonValue--;
                    spinComponentTextField.setValue(String.valueOf(spinButtonValue));
                }
            }
        });
        return button;
    }

    /**
     * Save report configuration action in to Report configuration transfer object - <code>ReportConfigTO</code>.
     */
    private void saveReportConfig() {
        reportConfig = UserSessionBean.getReportCriteria().getDisaredReport();
        if (reportConfig == null) {
            reportConfig = new ReportConfigTO();
        }

        if (!UserSessionBean.getApplConfigBeanInstance().isDividedByApplications())
            reportConfig.setMergedApplicationTitle(HeaderPanel.getInstance().getSelectedApplicationInfoText());

        /* first step section */
        reportConfig.setExcelFormat(excelFormatRB.isChecked());

        reportConfig.setPdfFormat(pdfFormatRB.isChecked());
        reportConfig.setHtmlFormat(htmlFormatRB.isChecked());
        reportConfig.setCsvFormat(csvFormatRB.isChecked());

        if (reportConfig.isExcelFormat()) {
            reportConfig.setReportFormatName(Constants.MS_EXCEL_FORMAT_NAME);
        } else if (reportConfig.isPdfFormat()) {
            reportConfig.setReportFormatName(Constants.PDF_FORMAT_NAME);
        } else if (reportConfig.isHtmlFormat()) {
            reportConfig.setReportFormatName(Constants.HTML_FORMAT_NAME);
        } else if (reportConfig.isCsvFormat()) {
            reportConfig.setReportFormatName(Constants.CSV_FORMAT_NAME);
        }

        reportConfig.setCurrentlySelectedString(currSelecApplInfoTextField.getValueAsString());

        /* second step */
        reportConfig.setAbsoluteModuleMode(absoluteModuleModeCheckBox.getValue());
        reportConfig.setHierarchicalModuleMode(hierarchicalModuleModeCheckBox.getValue());
        /* third */
        reportConfig.setApplications(applicationsCheckBox.getValue());
        reportConfig.setCurrentlySelected(currentlySelectedRB.isChecked());
        reportConfig.setAllDisplayed(allDisplayedRB.isChecked());
        reportConfig.setLayer(layerCheckBox.getValue());
        reportConfig.setInclusiveMode(inclusiveModeRB.isChecked());
        reportConfig.setExclusiveMode(exclusiveModeRB.isChecked());
        reportConfig.setInclusiveExclusiveMode(inclusiveExclusiveModeRB.isChecked());
        /* fourth */
        reportConfig.setQueries(spinComponentTextField.getValueAsString());
        reportConfig.setStatistics(statisticsCheckBox.isChecked());
        reportConfig.setDiagrams(diagramsCheckBox.isChecked());
    }

    /**
     * Call back for send complete URL for report servlet
     */
    private AsyncCallback getCallback(){
    	return new InfraredCallback() {

            public void doFailure(Throwable caught) {
                wizardPanel.getEl().unmask();
            }

            public void doSuccess(Object result) {
                ReportResultTO to = (ReportResultTO) result;
                generatedLink = to.getReportKey();
                ((CardLayout) wizardPanel.getLayout()).setActiveItem(4);
                fifthStepInitialState();
                wizardPanel.setTitle(ApplicationMessages.MESSAGES.complete());
                wizardPanel.getEl().unmask();
                downloadButton.show();
            }

        };
    } 

    /**
     * Show wizard action.
     */
    public void showWizard() {
        super.show();
    }

    /**
     * Hide wizard action.
     */
    public void hideWizard() {
        super.hide();
        ApplicationViewFacade.getApplicationLayout().getEl().unmask();
    }

    /**
     * @return the current step index
     */
    public int getCurrentStep() {
        return this.currentStep;
    }

    /**
     * @param currentStep the current step index to set
     */
    public void setCurrentStep(int currentStep) {
        this.currentStep = currentStep;
    }
}
