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

package net.sf.infrared2.gwt.client.view.facade.layer.other;

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

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.Node;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.TabPanel;
import com.gwtext.client.widgets.event.PanelListenerAdapter;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.event.GridCellListenerAdapter;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.tree.TreeNode;
import com.gwtext.client.widgets.tree.event.TreePanelListenerAdapter;

/**
 * <b>OtherLayerViewImpl</b>
 * <p>
 * Presents data of other layers (different for SQL and Application) of all
 * modules except Last Invocation.
 * 
 * @author Sergey Evluhin
 */
public class OtherLayerViewImpl {
    /** Listener for the summary grid (in summary tab). */
    private SummaryGridCellListener summaryGridCellListener;
    /** Summary tab for other layer. */
    private SummaryTab summaryTab;
    /** Trace tab for other layer. */
    private TraceTab traceTab;
    /** Transfer object for trace tree node. */
    private TraceTreeNodeTO currentTraceTreeNodeTo;
    /** Transfer object that contains details data for trace tree node. */
    private DetailOtherViewTO detailTo;
    /** A lightweight tab container. */
    private TabPanel tabPanel;
    /** Listener for the trace tree component. */
    private TraceTreeListener traceTreeListener;

    /** Panel that contains all components for this view. */
    private Panel center;

    /** Instance of OtherLayerViewImpl. */
    private static OtherLayerViewImpl view;

    /**
     * Builds view based on transfer object.
     * 
     * @param to - transfer object for this view.
     */
    public static final void buildView(OtherViewTO to) {
        if (view == null) {
            view = new OtherLayerViewImpl(to);
        } else {
            view.updateView(to);
        }
    }

    /**
     * Default constructor.
     * @param to - transfer object for this view.
     */
    private OtherLayerViewImpl(OtherViewTO to) {
        center = new Panel();
        center.setBorder(false);
        center.setBodyBorder(false);
        center.setLayout(new FitLayout());
        tabPanel = new TabPanel();

        summaryTab = new SummaryTab(to);
        tabPanel.add(summaryTab);
        summaryGridCellListener = new SummaryGridCellListener();
        summaryTab.getSummaryGrid().addGridCellListener(summaryGridCellListener);

        traceTab = new TraceTab(to);
        tabPanel.add(traceTab);

        tabPanel.activate(0);

        center.add(tabPanel);

        traceTreeListener = new TraceTreeListener();
        traceTab.getTraceTree().addListener(traceTreeListener);

        ApplicationViewFacade.addCenterPanel(center);
        // WindowResizeManagerFactory.getResizeManager(WindowResizeManagerFactory.GRIDS_MANAGER)
        // .complete();
        // WindowResizeManagerFactory.getResizeManager(WindowResizeManagerFactory.PANELS_MANAGER)
        // .complete();
        defaultSettings();

    }

    /**
     * Creates view based on data received from transfer object.
     * 
     * @param to - transfer object for this view.
     */
    void updateView(OtherViewTO to) {
        tabPanel.activate(0);
        summaryTab.update(to);
        traceTab.update(to);
        traceTab.getTraceTree().addListener(traceTreeListener);
        currentTraceTreeNodeTo = null;
        WindowResizeManagerFactory.getResizeManager(WindowResizeManagerFactory.PANELS_MANAGER).fireResize();
        ApplicationViewFacade.addCenterPanel(center);

        summaryTab.getSummaryGrid().getView().refresh();
    }

    /**
     * Default settings for this layer.
     */
    private void defaultSettings() {
        currentTraceTreeNodeTo = null;
        traceTab.addListener(new PanelListenerAdapter() {

            public void onActivate(Panel panel) {

                super.onActivate(panel);
                if (summaryTab.getSummaryGrid().getSelectionModel().getSelected() == null && currentTraceTreeNodeTo == null) {

                    final TreeNode firstChild = (TreeNode) traceTab.getTraceTree().getRootNode().getFirstChild();

                    selectTreeNode(firstChild, false, false);
                    traceTab.getTraceTree().collapseAll();
                }
            }
        });
    }

    /**
     * Updates UI components according to the newly received data.
     */
    void updateDetails() {
        if (currentTraceTreeNodeTo.isSqlNode() && detailTo != null) {
            traceTab.getSqlTab().updateViewSql(detailTo.getFormattedSql());
        } else {
            traceTab.getSqlTab().updateView(detailTo);
        }
        traceTab.getJdbcTab().updateView(detailTo);
        traceTab.getGeneralInformationTab().updateView(currentTraceTreeNodeTo);
        ApplicationViewFacade.unMaskCenter();
    }

    /**
     * Finds the node from children of current node by text.
     * 
     * @param node - current node.
     * @param nodeText - text of the node.
     * @return Node object if was find, null - if not.
     */
    private Node findChild(Node node, String nodeText) {
        if (node.getChildNodes() != null)
            for (int i = 0; i < node.getChildNodes().length; i++) {
                Node x = node.getChildNodes()[i];
                if (x != null) {
                    TraceTreeNodeTO traceTreeNodeTO = ((TraceTreeNodeTO) x.getUserObject());
                    String text = traceTreeNodeTO.getText();
                    if (text == null) {
                        text = "";
                    }
                    if (text.equals(nodeText))
                        return x;
                }
            }
        return null;
    }

    /**
     * Method that making selection for the node of trace tree.
     * 
     * @param node - node to select.
     * @param depth - indicates to select one level of children (false), or all
     *            depth of children (true).
     */
    private void selectTreeNode(TreeNode node, boolean depth, boolean fromListener) {
        ApplicationViewFacade.maskCenter();
        if ((node.getChildNodes() != null) && (node.getChildNodes().length > 0)) {
            node.ensureVisible();
            node.expand(false, true);
            node.select();
        } else {
            node.select();
        }
        if (!fromListener) {
            int panelTop = DOM.getAbsoluteTop(traceTab.getTraceTreePanel().getElement());
            int nodeTop = DOM.getAbsoluteTop(node.getUI().getEl()) - traceTab.getTraceTreePanel().getOffsetHeight()/2 ;
            traceTab.getTraceTreePanel().setScrollPosition(nodeTop - panelTop + traceTab.getTraceTreePanel().getScrollPosition());
        }

        currentTraceTreeNodeTo = (TraceTreeNodeTO) node.getUserObject();

        final ApplicationConfigTO applConfigBeanInstance = UserSessionBean.getApplConfigBeanInstance();
        final NavigatorHyperlink selectedHyperlink = StackPanelHolder.getInstance().getSelectedLink();

        Engine.getClient().getDetailsOtherViewData(applConfigBeanInstance, selectedHyperlink.getNavigatorEntryTO(), currentTraceTreeNodeTo,
                getDetailsAsyncCallback());

    }

    /**
     * Listener for the summary grid (in summary tab).
     * 
     * @author Sergey Evluhin
     */
    private class SummaryGridCellListener extends GridCellListenerAdapter {

        /**
         * {@inheritDoc}
         */
        public void onCellClick(GridPanel grid, int rowIndex, int colindex, EventObject e) {

            super.onCellClick(grid, rowIndex, colindex, e);
            if (colindex == 1) {
                String nodeText = grid.getStore().getAt(rowIndex).getAsString(ApplicationMessages.MESSAGES.operationName().toLowerCase() + "1");

                Node nodeById = findChild(traceTab.getTraceTree().getRootNode(), nodeText);

                if (nodeById != null) {
                    tabPanel.activate(1);
                    selectTreeNode((TreeNode) nodeById, true, false);
                }
            }
        }
    }

    /**
     * Listener for the trace tree component.
     * 
     * @author Sergey Evluhin
     */
    private class TraceTreeListener extends TreePanelListenerAdapter {
        public void onClick(TreeNode node, EventObject e) {
            super.onClick(node, e);
            selectTreeNode(node, false, true);
        }

    }

    /**
     * Callback for click on trace tree node.
     */
    private AsyncCallback getDetailsAsyncCallback() {
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
