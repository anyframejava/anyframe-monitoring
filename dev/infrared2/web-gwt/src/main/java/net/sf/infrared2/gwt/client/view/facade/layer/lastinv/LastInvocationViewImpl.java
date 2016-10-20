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
 * JSPLayerViewImp.java Date created: 18.01.2008
 * Last modified by: $Author: soyon.lim $
 * $Revision: 14286 $ $Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.gwt.client.view.facade.layer.lastinv;

import net.sf.infrared2.gwt.client.Engine;
import net.sf.infrared2.gwt.client.WindowResizeManagerFactory;
import net.sf.infrared2.gwt.client.callback.CacheAsyncCallback;
import net.sf.infrared2.gwt.client.i18n.ApplicationMessages;
import net.sf.infrared2.gwt.client.stack.NavigatorHyperlink;
import net.sf.infrared2.gwt.client.stack.StackPanelHolder;
import net.sf.infrared2.gwt.client.to.ApplicationConfigTO;
import net.sf.infrared2.gwt.client.to.other.DetailOtherViewTO;
import net.sf.infrared2.gwt.client.to.other.OtherViewTO;
import net.sf.infrared2.gwt.client.to.other.TraceTreeNodeTO;
import net.sf.infrared2.gwt.client.users.session.UserSessionBean;
import net.sf.infrared2.gwt.client.view.facade.ApplicationViewFacade;
import net.sf.infrared2.gwt.client.view.facade.layer.other.JdbcTab;
import net.sf.infrared2.gwt.client.view.facade.layer.other.SqlTab;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.TabPanel;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.tree.TreeNode;
import com.gwtext.client.widgets.tree.event.TreePanelListenerAdapter;

/**
 * <b>LastInvocationViewImpl</b>
 * <p>
 * Represents view for sequences of last invocation module.
 * 
 * @author Gleb Zgonikov
 */
public class LastInvocationViewImpl {
    /** Trace tab panel for last invocation module. */
    private LastInvocationTraceTab traceTab;
    /** JDBC tab panel. */
    private JdbcTab jdbcTab;
    /** SQL tab panel. */
    private SqlTab sqlTab;
    /** Transfer object for trace tree node. */
    private TraceTreeNodeTO currentTraceTreeNodeTo;
    /** Transfer object that contains details data for trace tree node. */
    private DetailOtherViewTO detailTo;
    /** Main tab panel. */
    private TabPanel tabPanel;

    /** Panel that contains all components for this view. */
    private Panel center;

    /** Panel that contains trace tree. */
    private TraceTreeListener traceTreeListener;
    
    /** Instance of LastInvocationViewImpl view. */
    private static LastInvocationViewImpl view;
    
    /**
     * Builds view for layer according to transfer object. 
     * @param to - transfer object.
     */
    public static void buildView(OtherViewTO to){
        if(view == null){
            view = new LastInvocationViewImpl(to);
        }else{
            view.updateView(to);
        }
    }

    /**
     * Default constructor.
     * @param to - transfer object for this view.
     */
    public LastInvocationViewImpl(OtherViewTO to) {
        center = new Panel();
        center.setBorder(false);
        center.setBodyBorder(false);
        center.setLayout(new FitLayout());
        tabPanel = new TabPanel();

        traceTab = new LastInvocationTraceTab(to);
        tabPanel.add(traceTab);

        center.add(tabPanel);
        traceTreeListener = new TraceTreeListener();
        traceTab.getTraceTree().addListener(traceTreeListener);

        jdbcTab = new JdbcTab(ApplicationMessages.MESSAGES.jdbc() + " "
                        + ApplicationMessages.MESSAGES.summary());
        sqlTab = new SqlTab(ApplicationMessages.MESSAGES.sql() + " "
                        + ApplicationMessages.MESSAGES.summary());

        tabPanel.add(jdbcTab);
        tabPanel.add(sqlTab);
        tabPanel.activate(0);
        ApplicationViewFacade.addCenterPanel(center);
//        WindowResizeManagerFactory.getResizeManager(WindowResizeManagerFactory.GRIDS_MANAGER)
//                        .complete();
//        WindowResizeManagerFactory.getResizeManager(WindowResizeManagerFactory.PANELS_MANAGER)
//                        .complete();
        defaultView();
    }

    /**
     * Creates view by transfer object data.
     * 
     * @param to - transfer object for this view.
     */
    public void updateView(OtherViewTO to) {
        traceTab.update(to);
        traceTab.getTraceTree().addListener(traceTreeListener);
        tabPanel.activate(0);
//        isNodeSelected = false;
        WindowResizeManagerFactory.getResizeManager(WindowResizeManagerFactory.PANELS_MANAGER).fireResize();
        ApplicationViewFacade.addCenterPanel(center);
        defaultView();
    }

    /**
     * Creates default view.
     */
    private void defaultView() {
        final ApplicationConfigTO applConfigBeanInstance = UserSessionBean
                        .getApplConfigBeanInstance();
        final NavigatorHyperlink selectedHyperlink = StackPanelHolder.getInstance()
                        .getSelectedLink();

//        if (!isNodeSelected) {
            selectTreeNode((TreeNode) traceTab.getTraceTree().getRootNode().getFirstChild());
            traceTab.getTraceTree().collapseAll();
//        }

        Engine.getClient().getDetailsOtherViewData(applConfigBeanInstance,
                        selectedHyperlink.getNavigatorEntryTO(), currentTraceTreeNodeTo,
                        getDetailsAsyncCallback());
    }

    /**
     * @return tab that contains trace tree for this sequence.
     */
    public LastInvocationTraceTab getTraceTab() {
        return traceTab;
    }

    /**
     * @return main tabbed panel for this view.
     */
    public TabPanel getTabPanel() {
        return tabPanel;
    }

    /**
     * Update information on UI.
     */
    public void updateDetails() {
        sqlTab.updateView(detailTo);
        jdbcTab.updateView(detailTo);
        traceTab.getGeneralInformationTab().updateView(currentTraceTreeNodeTo);
        ApplicationViewFacade.unMaskCenter();
    }

    /**
     * Selects the tree node.
     * 
     * @param node - node of tree to select.
     */
    private void selectTreeNode(TreeNode node) {
        ApplicationViewFacade.maskCenter();
        node.select();
        currentTraceTreeNodeTo = (TraceTreeNodeTO) node.getUserObject();

        getTraceTab().getTraceTree().expandPath(node.getPath(), null);
        traceTab.getGeneralInformationTab().updateView(currentTraceTreeNodeTo);
        ApplicationViewFacade.unMaskCenter();
    }

    /**
     * Listener for the tree component.
     * 
     * @author Sergey Evluhin
     */
    private class TraceTreeListener extends TreePanelListenerAdapter {

        public void onClick(TreeNode node, EventObject e) {
            super.onClick(node, e);
            selectTreeNode(node);
//            isNodeSelected = true;

        }

    }

    /**
     * Callback, that acts on click by tree node.
     */
    private AsyncCallback getDetailsAsyncCallback(){
    	return new CacheAsyncCallback() {
            public void atSuccess(Object result) {
                if (result == null) {
                    detailTo = null;
                } else {
                    detailTo = (DetailOtherViewTO) result;
                }
                updateDetails();
            }
        };
    } 
}
