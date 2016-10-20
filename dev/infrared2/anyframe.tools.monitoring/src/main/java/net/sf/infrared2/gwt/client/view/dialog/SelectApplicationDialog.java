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
 * SelectApplicationDialog.java Date created: 23.01.2008
 * Last modified by: $Author: mike $ 
 * $Revision: 14572 $ $Date: 2009-06-10 20:06:30 +0900 (수, 10 6월 2009) $
 */

package net.sf.infrared2.gwt.client.view.dialog;

import java.util.Date;

import net.sf.infrared2.gwt.client.Engine;
import net.sf.infrared2.gwt.client.callback.InfraredCallback;
import net.sf.infrared2.gwt.client.i18n.ApplicationMessages;
import net.sf.infrared2.gwt.client.stack.StackPanelHolder;
import net.sf.infrared2.gwt.client.to.ApplicationConfigTO;
import net.sf.infrared2.gwt.client.to.ApplicationEntryTO;
import net.sf.infrared2.gwt.client.tree.TreeFactory;
import net.sf.infrared2.gwt.client.users.session.UserSessionBean;
import net.sf.infrared2.gwt.client.utils.ApplicationTreeTransformer;
import net.sf.infrared2.gwt.client.utils.SIRConfigurationManager;
import net.sf.infrared2.gwt.client.utils.UIUtils;
import net.sf.infrared2.gwt.client.view.facade.ApplicationViewFacade;
import net.sf.infrared2.gwt.client.view.facade.HeaderActionLinksStack;
import net.sf.infrared2.gwt.client.view.header.HeaderPanel;
import net.sf.infrared2.gwt.client.view.header.HeaderToolBarBuilder;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.PaddedPanel;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.DateField;
import com.gwtext.client.widgets.form.FieldSet;
import com.gwtext.client.widgets.form.MultiFieldPanel;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.form.TimeField;
import com.gwtext.client.widgets.form.ValidationException;
import com.gwtext.client.widgets.form.Validator;
import com.gwtext.client.widgets.layout.ColumnLayout;
import com.gwtext.client.widgets.layout.ColumnLayoutData;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.layout.HorizontalLayout;
import com.gwtext.client.widgets.layout.VerticalLayout;
import com.gwtext.client.widgets.tree.TreePanel;

/**
 * <b>SelectApplicationDialog</b><p>
 * Select application dialog body entry - need for select monitored application,<br>
 * date of use and other custom settings
 * 
 * @author Andrey Zavgorodniy
 */
public class SelectApplicationDialog {
    
    private static final  DateTimeFormat  TIME_FOTMAT = DateTimeFormat.getFormat("HH:mm");
    
    private static SelectApplicationDialog dialog;
    /** Main dialog window layout */
    private  Window window;
    /** Live radio button presentation */
    private  RadioButton liveRadioButton;
    /** Archive date radio button presentation */
    private  RadioButton archiveDataRadioButton;
    /** Divided by application check box presentation */
    private  CheckBox divByAppCheckBox;

    /** Date field for start date selection */
    private DateField fromDatePicker;
    
    /** The from time field. */
    private TimeField fromTimeField;
  
    /** Date field for end date selection */
    private  DateField toDatePicker;
    
    /** The to time field. */
    private TimeField toTimeField;
    
    /** Panel holds select application tree */
    private  Panel treePanelHolder;
    /** Select application tree presentation */
    private  TreePanel applTreePanel;
    /** Label for error message show*/
    private  Label errorMessageText;
    /** Perform show or not labels in all application graphics and diagrams */
    private  CheckBox showLabelsInGraphCheckBox;
    
    /** The reset period field. */
    private NumberField resetPeriodField;

    /** Instance of SIRConfigurationManager class. */
    private  SIRConfigurationManager configManager = SIRConfigurationManager.getInstance();

    public static SelectApplicationDialog getInstance(){
        if (dialog==null) dialog = new SelectApplicationDialog();
        return dialog;
    }

    /**
     * Create controls action.
     */
    private  void createControls() {
            liveRadioButton = new RadioButton("date-picker-group");
            archiveDataRadioButton = new RadioButton("date-picker-group");

            fromDatePicker = new DateField(ApplicationMessages.MESSAGES.fromLabelText(), "m/d/Y");
            fromDatePicker.setWidth(100);
            fromDatePicker.setHideLabel(true);
            fromDatePicker.setReadOnly(true);
            fromDatePicker.disable();
            
            fromTimeField = new TimeField();
            fromTimeField.setWidth(80);
            fromTimeField.setFormat("G:i");
            fromTimeField.setHideLabel(true);
            fromTimeField.disable();

            toDatePicker = new DateField(ApplicationMessages.MESSAGES.toLabelText(), "m/d/Y");
            toDatePicker.setReadOnly(true);
            toDatePicker.disable();
            toDatePicker.setWidth(100);
            toDatePicker.setHideLabel(true);
            
            toTimeField = new TimeField();
            toTimeField.setWidth(80);
            toTimeField.setFormat("G:i");
            toTimeField.setHideLabel(true);
            toTimeField.disable();
            
            resetPeriodField = new NumberField(ApplicationMessages.MESSAGES.resetPeriod());
            resetPeriodField.setWidth(90);
            resetPeriodField.setHideLabel(true);
            resetPeriodField.setAllowBlank(false);
            resetPeriodField.setAllowNegative(false);
            resetPeriodField.setAllowDecimals(false);
            resetPeriodField.setValidator(new Validator(){

                public boolean validate(String value) throws ValidationException {
                    if ((value!=null)&&(value.trim().equals("0"))){
                        throw new ValidationException(ApplicationMessages.MESSAGES.valueLessZero());
                    }
                    return true;
                }
                
            });

            divByAppCheckBox = new CheckBox();
            showLabelsInGraphCheckBox = new CheckBox();
    }

    /**
     * Initialize error message label.
     */
    private  void initErrorHandlerHolder() {
        errorMessageText = new Label();
        errorMessageText.setVisible(false);
        errorMessageText.setStylePrimaryName("error-message-style");
    }

    /**
     * Get OK button component
     * 
     * @return OK button widget
     */
    private Widget getOkButton() {
        Button okButton = new Button(ApplicationMessages.MESSAGES.okButtonLabel());
        okButton.setText(ApplicationMessages.MESSAGES.okButtonLabel());
        okButton.addListener(new ButtonListenerAdapter() {
            public void onClick(Button button, EventObject e) {
                hideErrorMessage();
                DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat("MM/dd/yyyy HH:mm");

                if (!resetPeriodField.isValid()){
                    return;
                }

                if (archiveDataRadioButton.isChecked()) {
                    boolean isNotFilled = false;
                    if ((fromTimeField.getValueAsString() == null) || (fromTimeField.getValueAsString().trim().length() == 0)) {
                        fromTimeField.markInvalid(ApplicationMessages.MESSAGES.timeRequired());
                        isNotFilled = true;
                    }
                    if ((toTimeField.getValueAsString() == null) || (toTimeField.getValueAsString().trim().length() == 0)) {
                        toTimeField.markInvalid(ApplicationMessages.MESSAGES.timeRequired());
                        isNotFilled = true;
                    }
                    if (isNotFilled) {
                        return;
                    }
                    try {
                        if (fromTimeField.isValid() && toTimeField.isValid()) {
                            Date toDateTime = dateTimeFormat.parse(UIUtils.getStringDateFormatter(toDatePicker.getValue()) + " "
                                    + toTimeField.getValueAsString());
                            Date fromDateTime = dateTimeFormat.parse(UIUtils.getStringDateFormatter(fromDatePicker.getValue()) + " "
                                    + fromTimeField.getValueAsString());
                            if ((toDateTime.before(fromDateTime))) {
                                showErrorMessage(ApplicationMessages.MESSAGES.archiveDateRangeError());
                            } else if (fromTimeField.isValid() && toTimeField.isValid()) {
                                saveSelectedApplicationAction();
                                hideErrorMessage();
                                window.clear();
                                window.hide();
                                ApplicationViewFacade.unmaskAll();
                            }
                        }
                    } catch (IllegalArgumentException er) {
                        return;
                    }
                }else{
                    saveSelectedApplicationAction();
                    hideErrorMessage();
                    window.clear();
                    window.hide();
                    ApplicationViewFacade.unmaskAll();
                }
            }
        });
        return okButton;
    }

    /**
     * Get Cancel button component
     * 
     * @return Cancel button widget
     */
    private static Widget getCancelButton() {
        Button cancelButton = new Button(ApplicationMessages.MESSAGES.cancelButtonLabel());
        cancelButton.setText(ApplicationMessages.MESSAGES.cancelButtonLabel());
        cancelButton.setWidth("70px");
        cancelButton.addListener(new ButtonListenerAdapter() {
            public void onClick(Button button, EventObject e) {
                dialog.cancelSelectedApplicationAction();
            }
        });

        return cancelButton;
    }

    /**
     * Initialize archive data radio button widget
     * 
     * @return radio button widget as archive data holder
     */
    private  Widget initArchiveDataRadioBtn() {
        archiveDataRadioButton.addClickListener(new ClickListener() {
            public void onClick(Widget sender) {
                disableDatePickerSection(false);
            }
        });
        return UIUtils.getCompositeRadioButton(archiveDataRadioButton, ApplicationMessages.MESSAGES
                        .archiveDataLabelText());
    }

    /**
     * Initialize live date radio button widget
     * 
     * @return radio button widget as live date holder
     */
    private  Widget initLiveDateRadioBtn() {
        liveRadioButton.setChecked(true);
        liveRadioButton.addClickListener(new ClickListener() {
            public void onClick(Widget sender) {
                disableDatePickerSection(true);
                hideErrorMessage();
            }
        });
        return UIUtils.getCompositeRadioButton(liveRadioButton, ApplicationMessages.MESSAGES
                        .liveDataLabelText());
    }

    /**
     * Show error message.
     * @param errorText
     */
    private  void showErrorMessage(String errorText) {
        toDatePicker.markInvalid(errorText);
        toTimeField.markInvalid(errorText);
        errorMessageText.setText(errorText);
        errorMessageText.setVisible(true);
    }

    /**
     * Hide error message.
     */
    private  void hideErrorMessage() {
        toDatePicker.clearInvalid();
        toTimeField.clearInvalid();
        errorMessageText.setText("");
        errorMessageText.setVisible(false);
    }

    /**
     * Disable/enable date fields.
     * @param state - the state value
     */
    private  void disableDatePickerSection(boolean state) {
        if (!state) {
            fromDatePicker.enable();
            fromTimeField.enable();
            toDatePicker.enable();
            toTimeField.enable();
        } else {
            fromDatePicker.disable();
            fromTimeField.disable();
            toDatePicker.disable();
            toTimeField.disable();
        }
    }

    /**
     * Save selected applications in to config TO
     */
    private  void saveSelectedApplicationAction() {
        UserSessionBean.clearCache();
        ApplicationConfigTO config = UserSessionBean.getApplConfigBeanInstance();
        ApplicationTreeTransformer transformer = ApplicationTreeTransformer.getInstance();
        boolean archiveDateMode = archiveDataRadioButton.isChecked();
        if (config != null) {
            if (archiveDateMode) {
                config.setStartDate(UIUtils.getStringDateFormatter(fromDatePicker.getValue()));
                config.setStartTime(fromTimeField.getValueAsString());
                config.setEndDate(UIUtils.getStringDateFormatter(toDatePicker.getValue()));
                config.setEndTime(toTimeField.getValueAsString());
            }
            config.setDividedByApplications(divByAppCheckBox.isChecked());
            config.setLiveDate(liveRadioButton.isChecked());
            config.setArchiveDate(archiveDataRadioButton.isChecked());
            config.setGraphLabelEnabled(showLabelsInGraphCheckBox.isChecked());
            config.setResetIntervalMin(resetPeriodField.getValue()!=null? new Long(resetPeriodField.getValue().longValue()) : null);
            HeaderToolBarBuilder toolbarBuilder = null;
            /* Get checked items */
            if (applTreePanel != null) {
                //applTreePanel.doLayout();
                config.setApplicationList(transformer.transform(applTreePanel.getChecked()));
                toolbarBuilder = HeaderToolBarBuilder.getInstance(config);
            }else{
                config.setApplicationList(new ApplicationEntryTO[0]);
                toolbarBuilder = HeaderToolBarBuilder.getInstance(config);
            }
            /* Rewrite Header tool bar applications string */
            if (config.getApplicationList() != null && config.getApplicationList().length > 0) {
                if (toolbarBuilder != null) {
                    HeaderPanel.getInstance().showSelectedApplInfo(
                                    toolbarBuilder.getSelectedApplLabelText());
                }
                StackPanelHolder.getInstance().fillStack(config);
            } else {
                HeaderPanel.getInstance().hideSelectedApplInfo();
                ApplicationViewFacade.cleanAll();
                StackPanelHolder.getInstance().setDefault();
            }
            HeaderPanel.getInstance().doLayout();
            configManager.saveInCookie(config);
            UserSessionBean.setCookiesAvailable(true);
            HeaderActionLinksStack.init();
            
            saveResetInterval(config.getResetIntervalMin());
        }
    }
    
    /**
     * Save reset interval.
     * 
     * @param min the min
     */
    private void saveResetInterval(Long min) {
        if (min != null) {
            Engine.getClient().setResetCacheIntervalMin(min.longValue(), new AsyncCallback(){

                public void onFailure(Throwable caught) {
                }

                public void onSuccess(Object result) {
                }
                
            });
        }
    }

    /**
     * Cancel selected application action.
     */
    private  void cancelSelectedApplicationAction() {
        window.clear();
        window.hide();
        ApplicationViewFacade.unmaskAll();
    }

    /**
     * Load information for selected application settings from configuration TO
     */
    private  void loadInfoFromConfigTO() {
        /* Get configuration info from server */
        Engine.getClient().getApplicationConfigTO(new InfraredCallback() {

            public void doFailure(Throwable caught) {
                window.clear();
                window.hide();
            }

            public void doSuccess(Object result) {
                ApplicationConfigTO applConfigTo = (ApplicationConfigTO) result;
                ApplicationConfigTO sessionConfig = null;
                if (UserSessionBean.getApplConfigBeanInstance()==null) {
                    UserSessionBean.setConfigBean(new ApplicationConfigTO());
                    sessionConfig = UserSessionBean.getApplConfigBeanInstance();
                }
                else sessionConfig = UserSessionBean.getApplConfigBeanInstance();
                Date currentDate = new Date();
                String currentTime = TIME_FOTMAT.format(currentDate);
                if (sessionConfig != null && applConfigTo != null) {
                    if (sessionConfig.getStartDate() == null ||
                            "".equals(sessionConfig.getStartDate()) ||
                            "null".equals(sessionConfig.getStartDate())) {
                        fromDatePicker.setValue(currentDate);
                    } else {
                        fromDatePicker.setValue(sessionConfig.getStartDate());
                    }
                    if (sessionConfig.getStartTime() == null ||
                            "".equals(sessionConfig.getStartTime()) ||
                            "null".equals(sessionConfig.getStartTime())) {
                        fromTimeField.setValue(currentTime);
                    } else {
                        fromTimeField.setValue(sessionConfig.getStartTime());
                    }
                    if (sessionConfig.getEndDate() == null ||
                            "".equals(sessionConfig.getEndDate()) ||
                            "null".equals(sessionConfig.getEndDate())) {
                        toDatePicker.setValue(currentDate);
                        toTimeField.setValue(currentTime);
                    } else {
                        toDatePicker.setValue(sessionConfig.getEndDate());
                        toTimeField.setValue(sessionConfig.getEndTime());
                    }
                    if (sessionConfig.getEndTime() == null ||
                            "".equals(sessionConfig.getEndTime()) ||
                            "null".equals(sessionConfig.getEndTime())) {
                        toTimeField.setValue(currentTime);
                    } else {
                        toTimeField.setValue(sessionConfig.getEndTime());
                    }      
                    liveRadioButton.setChecked(sessionConfig.isLiveDate());
                    archiveDataRadioButton.setChecked(sessionConfig.isArchiveDate());
                    divByAppCheckBox.setChecked(sessionConfig.isDividedByApplications());
                    showLabelsInGraphCheckBox.setChecked(sessionConfig.isGraphLabelEnabled());
                    if (sessionConfig.isArchiveDate()) {
                        disableDatePickerSection(false);
                    }
                    /* Get configuration info from session bean */
                    ApplicationEntryTO[] sessionStoredAppl = UserSessionBean
                                    .getApplConfigBeanInstance().getApplicationList();
                    ApplicationEntryTO[] serverStoredAppl = applConfigTo.getApplicationList();
                    /* Merge configuration process */
                    ApplicationEntryTO[] configAfterMerging = configManager
                    .clientVsServerConfigComparator(sessionStoredAppl, serverStoredAppl);
                    if (treePanelHolder!=null) treePanelHolder.clear();
                    applTreePanel = TreeFactory.createSelectApplTree(serverStoredAppl,
                                    configAfterMerging, UserSessionBean.isCookiesAvailable());
                    if (applTreePanel != null) {
                        treePanelHolder.add(applTreePanel);
                        treePanelHolder.doLayout();
                        applTreePanel.expandAll();
                    }
                    resetPeriodField.setValue(applConfigTo.getResetIntervalMin());
                } else {
                    fromDatePicker.setValue(currentDate);
                    fromTimeField.setValue(currentTime);
                    toDatePicker.setValue(currentDate);
                    toTimeField.setValue(currentTime);
                    resetPeriodField.setValue((Long)null);
                }
            }
        });
    }

    /**
     * Parametrize main window.
     */
    private  void initWindowLayout() {
        if (window==null){
            window = new Window();
            window.setClosable(false);
            window.setDraggable(false);
            window.setTitle(ApplicationMessages.MESSAGES.selectApplicationLabelLower());
            window.setResizable(false);
            window.setWidth(500);
            window.setHeight(550);
            window.setLayout(new FitLayout());
            window.setPaddings(3);
            window.setMonitorResize(false);
        }
    }

    /**
     * Initilize tree panel for hold applications tree.
     */
    private  void initTreePanelHolder() {
        if (treePanelHolder==null){
            treePanelHolder = new Panel();
            treePanelHolder.setId("tree-panel-holder");
            treePanelHolder.setBorder(false);
            treePanelHolder.setLayout(new FitLayout());
            treePanelHolder.setHeight(380);
            treePanelHolder.setAutoScroll(false);
            treePanelHolder.setPaddings(0, 0, 0, 5);
        }
    }

    /**
     * Show wizard action.
     */
    public  void showDialog() {

//            if (window==null){
                initWindowLayout();
                createControls();
                initErrorHandlerHolder();


                Panel mainLayoutPanel = new Panel();
                mainLayoutPanel.setLayout(new ColumnLayout());
                mainLayoutPanel.setId("main-layout-panel");
                mainLayoutPanel.setHeight(500);
                mainLayoutPanel.setBorder(false);
                mainLayoutPanel.setPaddings(2);

                Panel columnOne = new Panel();
                columnOne.setId("column-one-panel");
                columnOne.setBorder(false);
                columnOne.setLayout(new FitLayout());
                columnOne.setHeight(500);

                Panel columnTwo = new Panel();
                columnTwo.setId("column-two-panel");
                columnTwo.setLayout(new FitLayout());
                columnTwo.setBorder(false);

                /* create tree field sets*/
                FieldSet treeHolderFieldset = new FieldSet();
                treeHolderFieldset.setId("tree-holder-fieldset");
                treeHolderFieldset.setTitle(ApplicationMessages.MESSAGES.selectApplicationsLabelLower());
                treeHolderFieldset.setLabelWidth(70);
                treeHolderFieldset.setHeight(380);
                treeHolderFieldset.setLayout(new FitLayout());

                initTreePanelHolder();

                treeHolderFieldset.add(treePanelHolder);
                treeHolderFieldset.add(UIUtils.getCompositeCheckBox(divByAppCheckBox, ApplicationMessages.MESSAGES.dividedByApplLabel()));

                /* create date selection field sets */
                FieldSet dateSelectionFieldset = new FieldSet();
                dateSelectionFieldset.setId("date-selection-fieldset");
                dateSelectionFieldset.setTitle(ApplicationMessages.MESSAGES
                                .selectMonitoringPeriodSelectLabelText());
                dateSelectionFieldset.setLabelWidth(70);
                Panel dateInnerPanel = new Panel();
                dateInnerPanel.setId("date-inner-panel");
                dateInnerPanel.setBorder(false);
                dateInnerPanel.setLayout(new VerticalLayout());
                dateInnerPanel.add(initLiveDateRadioBtn());
                dateInnerPanel.add(initArchiveDataRadioBtn());
                dateInnerPanel.add(new HTML("<span>&nbsp;</span>"));
                dateSelectionFieldset.add(dateInnerPanel);
                String periodLabel = "<div class=\"x-form-item\" tabindex=\"-1\">\n" +
                        "            <label class=\"x-form-item-label\" style=\"width: 70px;\">"+ApplicationMessages.MESSAGES.periodLabelText()+"</label>\n" +
                        "            <div class=\"x-form-clear-left\"/>\n" +
                        "        </div>" ;
                dateSelectionFieldset.add(new HTML(periodLabel));
                MultiFieldPanel dateFromTimePanel = new MultiFieldPanel();
                dateInnerPanel.setPaddings(3);
                dateFromTimePanel.setBorder(false);
                dateFromTimePanel.add(new Label(ApplicationMessages.MESSAGES.fromLabelText()+ " : "));
                dateFromTimePanel.addToRow(fromDatePicker, 100);
                dateFromTimePanel.addToRow(fromTimeField, new ColumnLayoutData(1));
                
                MultiFieldPanel dateToTimePanel = new MultiFieldPanel();
                dateToTimePanel.setPaddings(3);
                dateToTimePanel.setBorder(false);
                dateToTimePanel.add(new Label(ApplicationMessages.MESSAGES.toLabelText()+ " :    "));
                dateToTimePanel.addToRow(toDatePicker, 100);
                dateToTimePanel.addToRow(toTimeField, new ColumnLayoutData(1));
                
                dateSelectionFieldset.add(dateFromTimePanel);
                dateSelectionFieldset.add(dateToTimePanel);
                Panel errorHolderPanel = new Panel();
                errorHolderPanel.setBorder(false);
                errorHolderPanel.setLayout(new FitLayout());
                errorHolderPanel.setHeight(30);
                errorHolderPanel.add(errorMessageText);
                dateSelectionFieldset.add(errorHolderPanel);
                
                FieldSet resetPeriodFieldSet = new FieldSet();
                resetPeriodFieldSet.setId("reset-period-fieldset");
                resetPeriodFieldSet.setTitle(ApplicationMessages.MESSAGES.resetPeriod());
                resetPeriodFieldSet.setLabelWidth(70);
                MultiFieldPanel resetInnerPanel = new MultiFieldPanel();
                //resetInnerPanel.setLayout(new HorizontalLayout(5));
                resetInnerPanel.setBorder(false);
                resetInnerPanel.add(new Label(ApplicationMessages.MESSAGES.resetPeriod()+" : "));
                resetInnerPanel.addToRow(resetPeriodField, 100);
                resetInnerPanel.add(new Label(ApplicationMessages.MESSAGES.minutes()));
                resetPeriodFieldSet.add(resetInnerPanel);

                columnOne.add(treeHolderFieldset);
                columnTwo.add(dateSelectionFieldset);
                columnTwo.add(resetPeriodFieldSet);
                columnTwo.add(UIUtils.getCompositeCheckBox(showLabelsInGraphCheckBox, ApplicationMessages.MESSAGES.showLabelsInGraph()));

                Panel spacePanel = new Panel();
                spacePanel.setLayout(new FitLayout());
                spacePanel.setBorder(false);
                spacePanel.setHeight(150);

                Panel buttonsPanel = new Panel();
                buttonsPanel.setId("buttons-panel");
                buttonsPanel.setLayout(new HorizontalLayout(10));
                buttonsPanel.add(new HTML("<span>&nbsp;&nbsp;&nbsp;&nbsp;</span>"));
                buttonsPanel.add(getOkButton());
                buttonsPanel.add(getCancelButton());
                buttonsPanel.setStyle("text-align:right");
                columnTwo.add(spacePanel);
                columnTwo.add(buttonsPanel);

                mainLayoutPanel.add(columnOne, new ColumnLayoutData(0.4));
                mainLayoutPanel.add(new PaddedPanel(columnTwo, 0, 3, 0, 0), new ColumnLayoutData(0.6));


                window.add(mainLayoutPanel);
//            }
            loadInfoFromConfigTO();
            window.show();
    }

    /**
     * Hide wizard action.
     */
    public void hideDialog() {
        window.clear();
        window.hide();
        ApplicationViewFacade.getApplicationLayout().getEl().unmask();
    }

}
