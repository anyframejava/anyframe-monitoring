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
 * GeneralInformationTab.java Date created: 01.02.2008
 * Last modified by: $Author: soyon.lim $ 
 * $Revision: 14286 $ $Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.gwt.client.view.facade.layer.other;

import java.util.Arrays;

import net.sf.infrared2.gwt.client.WindowResizeManagerFactory;
import net.sf.infrared2.gwt.client.grid.ColumnDef;
import net.sf.infrared2.gwt.client.grid.DateRenderer;
import net.sf.infrared2.gwt.client.grid.GridGenerationConfig;
import net.sf.infrared2.gwt.client.grid.GridWindowResizeListenerImpl;
import net.sf.infrared2.gwt.client.grid.SimpleGridWrapper;
import net.sf.infrared2.gwt.client.grid.TableFactory;
import net.sf.infrared2.gwt.client.i18n.ApplicationMessages;
import net.sf.infrared2.gwt.client.stack.StackPanelHolder;
import net.sf.infrared2.gwt.client.to.other.TraceTreeNodeTO;
import net.sf.infrared2.gwt.client.utils.UIUtils;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.event.PanelListenerAdapter;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.layout.VerticalLayout;

/**
 * <b>GeneralInformationTab</b>
 * <p>
 * General information tab panel.
 * 
 * @author Sergey Evluhin
 */
public class GeneralInformationTab extends Panel {
    /** Label present selected trace node text. */
    private Label selectedTraceNodeText;
    /** Hold layer name. */
    private HTML htmlLayerName;
    /** Grid of values. */
    private GridPanel grid;
    /** Indicates when tab already been shown. */
    private boolean created = false;

    /** Panel that contains all components fot this tab. */
    private Panel contentPanel;
    /**
     * Renderer implementation for the columns with data of the date's type.
     * This class render the date to special format.
     */
    private DateRenderer dateRenderer = new DateRenderer();

    /**
     * Default constructor.
     */
    public GeneralInformationTab() {
        super(ApplicationMessages.MESSAGES.generalInfo());
        this.setPaddings(5, 5, 0, 0);
        this.setBodyBorder(false);
        this.setBorder(false);
        this.setLayout(new FitLayout());
        this.setAutoScroll(false);

        contentPanel = new Panel();
        contentPanel.setBodyBorder(false);
        contentPanel.setBorder(false);
        contentPanel.setLayout(new VerticalLayout(0));
        contentPanel.setAutoScroll(true);

        selectedTraceNodeText = new Label();
        htmlLayerName = new HTML();
        this.addListener(new PanelListenerAdapter(){
            /**
             * {@inheritDoc}
             */
            public void onActivate(Panel panel) {
                if(grid!=null)
                    if(grid.getView()!=null)
                        grid.getView().refresh();
            }
        });
    }

    /**
     * Fill the panel with values.
     * 
     * @param traceNode - trace tree node element.
     */
    void createPanel(TraceTreeNodeTO traceNode) {
        created = true;
        HorizontalPanel hp = new HorizontalPanel();
        hp.setSpacing(3);
        Label lName = new Label(ApplicationMessages.MESSAGES.name() + ":");
        lName.setStyleName("bold-text");
        hp.add(lName);
        hp.add(new Label(" "));
        selectedTraceNodeText.setText(traceNode.getText());
        hp.add(selectedTraceNodeText);
        contentPanel.add(hp);

        Label callHierarchy = new Label(ApplicationMessages.MESSAGES.callHierarchy() + ": ");
        callHierarchy.setStyleName("bold-text");
        contentPanel.add(callHierarchy);

        String html = "&nbsp;&nbsp;&nbsp;&nbsp;";

        html += StackPanelHolder.getInstance().getSelectedLink().getText();
        html += "&rarr;";
        html += traceNode.getLayerName();
        htmlLayerName.setHTML(html);
        htmlLayerName.setStyleName("bold-text");
        contentPanel.add(htmlLayerName);

        Label statistic = new Label(ApplicationMessages.MESSAGES.statistic() + ": ");
        statistic.setStyleName("bold-text");
        contentPanel.add(statistic);

        final Object[][] data = TraceTreeNodeTO
                        .convertGeneralInformationRows(new TraceTreeNodeTO[] { traceNode });

        ColumnDef[] columnDefs = new ColumnDef[] {
                        new ColumnDef(ApplicationMessages.MESSAGES.count(), 0, ColumnDef.INTEGER),
                        new ColumnDef(ApplicationMessages.MESSAGES.totalInclusiveTime(), 0,
                                        ColumnDef.INTEGER),
                        new ColumnDef(ApplicationMessages.MESSAGES.maxInclusiveTime(), 0,
                                        ColumnDef.INTEGER),
                        new ColumnDef(ApplicationMessages.MESSAGES.minInclusiveTime(), 0,
                                        ColumnDef.INTEGER),
                        new ColumnDef(ApplicationMessages.MESSAGES.totalExclusiveTime(), 0,
                                        ColumnDef.INTEGER),
                        new ColumnDef(ApplicationMessages.MESSAGES.minExclusiveTime(), 0,
                                        ColumnDef.INTEGER),
                        new ColumnDef(ApplicationMessages.MESSAGES.maxExclusiveTime(), 0,
                                        ColumnDef.INTEGER),
                        new ColumnDef(ApplicationMessages.MESSAGES.timeOfFirstExecution(), 0,
                                        ColumnDef.DATE),
                        new ColumnDef(ApplicationMessages.MESSAGES.timeOfLastExecution(), 0,
                                        ColumnDef.DATE),
                        new ColumnDef(ApplicationMessages.MESSAGES.firstExecutionInclusiveTime(),
                                        0, ColumnDef.INTEGER),
                        new ColumnDef(ApplicationMessages.MESSAGES.lastExecutionInclusiveTime(),
                                        0, ColumnDef.INTEGER),
                        new ColumnDef(ApplicationMessages.MESSAGES.firstExecutionExclusiveTime(),
                                        0, ColumnDef.INTEGER),
                        new ColumnDef(ApplicationMessages.MESSAGES.lastExecutionExclusiveTime(),
                                        0, ColumnDef.INTEGER) };
        GridGenerationConfig config = new GridGenerationConfig(Arrays.asList(columnDefs));
        grid = (GridPanel) TableFactory.getGrid(data, config);
        grid.getColumnModel().setRenderer(7, dateRenderer);
        grid.getColumnModel().setRenderer(8, dateRenderer);

        grid.setWidth(grid.getColumnModel().getTotalWidth() + 3);
        Panel p = new Panel();
        p.setAutoScroll(true);
        p.setBorder(false);
        p.setBodyBorder(false);
        p.setWidth(UIUtils.getCenterPanelWidth() - UIUtils.SCROLL_WIDTH);
        p.setHeight(UIUtils.calculateGridHeight(grid));
        p.add(grid);

        SimpleGridWrapper gridWrapper = new SimpleGridWrapper(grid, columnDefs,p);
        gridWrapper.adjustGridSize(false);
        WindowResizeManagerFactory.getResizeManager(WindowResizeManagerFactory.GRIDS_MANAGER)
                        .addListener(new GridWindowResizeListenerImpl(gridWrapper));

        contentPanel.add(p);
        contentPanel.doLayout();
        this.add(contentPanel);
        this.doLayout();
    }

    /**
     * Update panel values according to the node values.
     * 
     * @param node - trace tree node.
     */
    public void updateView(TraceTreeNodeTO node) {
        if (!created) {
            createPanel(node);
        } else {
            selectedTraceNodeText.setText(node.getText());
            String html = "&nbsp;&nbsp;&nbsp;&nbsp;";

            html += StackPanelHolder.getInstance().getSelectedLink().getText();
            html += "&rarr;";
            html += node.getLayerName();
            htmlLayerName.setHTML(html);

            final Store store = grid.getStore();
            store.setDataProxy(new MemoryProxy(TraceTreeNodeTO
                            .convertGeneralInformationRows(new TraceTreeNodeTO[] { node })));
            store.reload();
        }

    }

    public GridPanel getGrid() {
        return grid;
    }
}
