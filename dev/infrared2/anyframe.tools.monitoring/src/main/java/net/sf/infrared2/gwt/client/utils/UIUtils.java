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
 * UIUtils.java Date created: 30.01.2008
 * Last modified by: $Author: soyon.lim $ 
 * $Revision: 14286 $ $Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.gwt.client.utils;

import java.util.Arrays;
import java.util.Date;

import net.sf.infrared2.gwt.client.grid.ComplexGrid;
import net.sf.infrared2.gwt.client.i18n.ApplicationMessages;
import net.sf.infrared2.gwt.client.stack.StackPanelHolder;
import net.sf.infrared2.gwt.client.to.comparator.ByCountComparator;
import net.sf.infrared2.gwt.client.to.comparator.ByTimeComparator;
import net.sf.infrared2.gwt.client.view.facade.ApplicationViewFacade;
import net.sf.infrared2.gwt.client.view.facade.ColoredRows;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.core.ExtElement;
import com.gwtext.client.util.DateUtil;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.GridView;
import com.gwtext.client.widgets.layout.HorizontalLayout;

/**
 * <b>UIUtils</b>
 * <p>
 * Utility class for UI components.
 * 
 * @author Sergey Evluhin
 */
public class UIUtils {
    /** Pixel size of browser scroll. */
    public static final int SCROLL_WIDTH = 20;
    /** Default height of table header. */
    public static final int HEADER_GRID_HEIGHT = 23;
    /** Default height of one row data of the table. */
    public static final int ROW_HEIGHT = 21;

    /** Date format pattern */
    public static final String DATE_PATTERN = "m/d/Y";

    /**
     * Return date by needed format.
     * 
     * @param date - current date
     * @return formatted string
     */
    public static String getStringDateFormatter(Date date) {
        if (date == null)
            return null;
        String result = null;
        try {
            result = DateUtil.format(date, DATE_PATTERN);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return result;
    }

    /**
     * Return composite radio button control with label in panel view for right
     * vertical alignment.
     * 
     * @param radioBtn - the RadioButton component
     * @param label - the String label presentation
     * @return the Panel object
     */
    public static Widget getCompositeRadioButton(Widget radioBtn, String label) {
        Panel panel = new Panel();
        panel.setBorder(false);
        panel.setLayout(new HorizontalLayout(2));
        panel.add(radioBtn);
        Label radioBtnLabel = new Label(label);
        radioBtnLabel.setStylePrimaryName("gwt-RadioButton");
        panel.add(radioBtnLabel);
        return panel;
    }

    /**
     * Return composite check box component - consists of check box cell and
     * it's cell label.
     * 
     * @param checkBox - the CheckBox component
     * @param label - the Label() component
     * @return - the Panel object with Horizontal layout
     */
    public static Widget getCompositeCheckBox(Widget checkBox, String label) {
        Panel panel = new Panel();
        panel.setBorder(false);
        panel.setLayout(new HorizontalLayout(5));
        panel.add(checkBox);
        Label checkBoxabel = new Label(label);
        panel.add(checkBoxabel);
        return panel;
    }

    /**
     * Calculates the width of the center panel.
     * 
     * @return width of the center panel.
     */
    public static int getCenterPanelWidth() {
        return Window.getClientWidth() - StackPanelHolder.getInstance().getOffsetWidth() - 20;
    }

    /**
     * Calculates the height of the center panel.
     * 
     * @return height of the center panel.
     */
    public static int getCenterPanelHeight() {
        return ApplicationViewFacade.getCenterPanel().getOffsetHeight();
    }

    /**
     * Calculates the height of the grid panel (inclusive header, scroll height
     * and sum of all rows heights)
     * 
     * @param grid - grid whose height will be calculate.
     * @return height of the grid.
     */
    public static int calculateGridHeight(GridPanel grid) {
        int res = HEADER_GRID_HEIGHT + SCROLL_WIDTH;

        final int length = grid.getStore().getRecords().length;

        res += length * ROW_HEIGHT;
        return res;
    }

    /**
     * Set to the grid such height value that this grid is has.
     * 
     * @param grid - the grid whose height will be normalized.
     */
    public static void adjustGridHeight(GridPanel grid) {
        grid.setHeight(calculateGridHeight(grid));
    }

    /**
     * Updates the height style of existing grid (already rendered). Adjust
     * method used for non existing grids.
     * 
     * @param grid - the grid whose height will be updated.
     */
    public static void updateGridHeight(GridPanel grid) {
        if (grid.getBody() != null)
            grid.getBody().setStyle("height", calculateGridHeight(grid) + "px");
        if (grid.getView() != null)
            grid.getView().refresh();
    }

    public static void updateGridHeight(ComplexGrid grid) {
        int h = grid.calculateHeight();
        grid.setHeight(h);
        if (grid.getBody() != null) {
            grid.getBody().setStyle("height", h + "px");
        }
        grid.doLayout();

        GridPanel contentGrid = grid.getContentGrid();
        int ch = (calculateGridHeight(contentGrid) - SCROLL_WIDTH) + 2;
        ExtElement gridBody = contentGrid.getBody();
        if (gridBody != null) {
            gridBody.setStyle("height", ch + "px");
            gridBody.setStyle("width", (contentGrid.getColumnModel().getTotalWidth() + 3) + "px");
        }
        GridView view = contentGrid.getView();
        if (view != null) {
            view.refresh();
        }
    }

    public static void updateGridHeightWORKED(ComplexGrid grid) {
        int h = grid.calculateHeight();
        grid.setHeight(h);
        grid.getBody().setStyle("height", h + "px");
        // grid.doLayout();

        GridPanel contentGrid = grid.getContentGrid();
        int ch = (calculateGridHeight(contentGrid) - SCROLL_WIDTH) + 2;
        contentGrid.getBody().setStyle("height", ch + "px");
        contentGrid.getBody().setStyle("width",
                        (contentGrid.getColumnModel().getTotalWidth() + 3) + "px");
        contentGrid.getView().refresh();
    }

    /**
     * Set to the grid such width value that this grid is has.
     * 
     * @param grid - the grid whose width will be normalized.
     */
    public static void adjustGridWidth(final GridPanel grid) {
        final int totalWidth = grid.getColumnModel().getTotalWidth() + 3;

        final String widthParameter = "width";
        final String widthValue = totalWidth + "px";
        ExtElement e = new ExtElement(grid.getElement());
        e.setStyle(widthParameter, widthValue);

        final Element tmp = DOM.getChild(grid.getElement(), 0);

        final Element c11 = DOM.getChild(tmp, 0);
        ExtElement e1 = new ExtElement(c11);
        e1.setStyle(widthParameter, widthValue);
        final Element c111 = DOM.getChild(c11, 0);
        ExtElement e2 = new ExtElement(c111);
        e2.setStyle(widthParameter, widthValue);

        final Element tmp1 = DOM.getChild(c111, 0);

        final Element tmp2 = DOM.getChild(tmp1, 0);

        final Element c1111 = DOM.getChild(tmp2, 0);

        ExtElement e3 = new ExtElement(c1111);
        e3.setStyle(widthParameter, widthValue);
    }

    /**
     * Creates table for graphic, that consists from two columns, 1- colored
     * rectangle,2-description.
     * 
     * @param rows - rows for the table.
     * @param byTime - true if the graphic shows statistic by operation time.
     * @return FlexTable component with data.
     */
    public static FlexTable getColoredLegendTable(ColoredRows[] rows, boolean byTime) {
        FlexTable grid;
        grid = new FlexTable();

        if (rows == null || rows.length == 0) {
            return grid;
        }
        String lastColor = "";
        String otherOperationsColor = null;
        for (int i = 0; i < rows.length; i++) {
            String color;
            if (byTime) {
                color = rows[i].getTimeColor();
            } else {
                color = rows[i].getCountColor();
            }

            if (lastColor.equals(color)) {
                otherOperationsColor = lastColor;
                break;
            }
            lastColor = color;
        }

        if (byTime) {
            Arrays.sort(rows, new ByTimeComparator(otherOperationsColor));
        } else {
            Arrays.sort(rows, new ByCountComparator(otherOperationsColor));
        }
        if (otherOperationsColor == null) {
            otherOperationsColor = "";
        }

        for (int i = 0; i < rows.length; i++) {
            String color;
            double value = 0;
            if (byTime) {
                color = rows[i].getTimeColor();
                value = rows[i].getTotalTime();
            } else {
                color = rows[i].getCountColor();
                value = rows[i].getCount();
            }
            if (value == 0) {
                grid.setWidget(i, 0, new HTML(
                                "<div class=\"colored-panel\" style=\"background-color: " + color
                                                + ";\">&nbsp;</div>"));
                grid.setText(i, 1, ApplicationMessages.MESSAGES.otherOperationsGraphic());
                break;
            }

            grid.setWidget(i, 0, new HTML("<div class=\"colored-panel\" style=\"background-color: "
                            + color + ";\">&nbsp;</div>"));
            if (otherOperationsColor.equals(color)) {
                grid.setText(i, 1, ApplicationMessages.MESSAGES.otherOperationsGraphic());
                break;
            }

            grid.setText(i, 1, rows[i].getDescription());

        }

        grid.getCellFormatter().setWidth(0, 0, "20px");
        grid.setStylePrimaryName("colored-legend-grid");

        return grid;
    }
}
