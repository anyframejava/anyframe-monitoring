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
 * AbstractChart.java Date created: 31.01.2008
 * Last modified by: $Author: soyon.lim $
 * $Revision: 14286 $ $Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.server.chart;

import net.sf.infrared2.server.model.PieSectorEntity;
import net.sf.infrared2.server.util.GraphicUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.labels.PieToolTipGenerator;
import org.jfree.chart.labels.StandardPieToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.RectangleEdge;

import java.awt.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;
import java.util.List;

/**
 * <b>GraphicGenerator</b><p>
 * Abstract Chart class.
 *
 * @author Roman Ivanenko
 */
public class GraphicGenerator {

    /** Field logger  */
    private static Log logger = LogFactory.getLog(GraphicGenerator.class);

    /**
     * Keeps generated chart
     */
    private JFreeChart chart;

    /**
     * Constructor. Creates a new instance of Bar Chart Generator.
     *
     * @param title the frame title.
     */
    public GraphicGenerator(ArrayList<String> series, ArrayList<String> categories, Number[][] barDataArray, String title) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        logger.debug("\n\t === Bar Chart Data Set ===");
        int i = 0;
        for (String s : series) {
            int y = 0;
            for (String c : categories) {
                try {
                    dataset.addValue(barDataArray[i][y], s, c);
                }
                catch (IndexOutOfBoundsException e) {
                    logger.error("\n\t ERROR: Bar chart data set Format/Dimension is incorrect.");
                    dataset = null;
                    return;
                }
                logger.debug(barDataArray[i][y] + " ");
                y++;
            }
            logger.debug("\n");
            i++;
        }
        JFreeChart chart = createChart(dataset, title);
        CategoryPlot categoryPlot = (CategoryPlot) chart.getPlot();
        categoryPlot.setBackgroundPaint(Color.white);
        if (series.size()<2)chart.removeLegend();
        setChart(chart);
    }

    /**
     * Constructor. Creates a new instance of Pie 3D Chart Generator.
     * @param pieDataSet
     */
    public GraphicGenerator(Set<PieSectorEntity> pieDataSet) {
        final DefaultPieDataset dataset = new DefaultPieDataset();
        for (PieSectorEntity entity : pieDataSet) {
            dataset.setValue(entity.getName(), entity.getNumber());
        }

        /* Set sorting order */
//        dataset.sortByValues(SortOrder.DESCENDING);
//    	dataset.sortByKeys(SortOrder.ASCENDING);

        JFreeChart chart = createChart(dataset);
        PiePlot3D p3d = (PiePlot3D) chart.getPlot();
        p3d.setBackgroundPaint(Color.white);

        for (PieSectorEntity entity : pieDataSet){
            p3d.setSectionPaint(entity.getName(), entity.getColor());
        }
        setChart(chart);
    }

    /**
     * Method getColorToSectorMap returns the colorToSectorMap of this GraphicGenerator object.
     *
     * @return the colorToSectorMap (type Map<String, String>) of this GraphicGenerator object.
     */
    public Map<String, String> getColorToSectorMap(){
        if (chart==null) return null;
        PiePlot3D p3d = (PiePlot3D) chart.getPlot();
        PieDataset dataset = p3d.getDataset();
        Map<String, String> result = new HashMap<String, String>();
        List <String> keys = dataset.getKeys();
        for (String key :keys){
            Color color = (Color) p3d.getSectionPaint(key);
            result.put(key, getColorString(color));
        }
        return result;
    }

    /**
     * Method getColorString forms sting representation of color in RGB model.
     *
     * @param color of type Color
     * @return String
     */
    private String getColorString(Color color) {
        String result = "";
        int [] rgb = new int[]{color.getRed(), color.getGreen(), color.getBlue()};
        for (int aRgb : rgb) {
            if (Integer.toHexString(aRgb).length() < 2) {
                result += "0";
            }
            result += Integer.toHexString(aRgb);
        }
        return result;
    }

    

    /**
     * @return The chart.
     */
    public JFreeChart getChart() {
        return chart;
    }


    /**
     * @param chart the chart to set.
     */
    protected void setChart(JFreeChart chart) {
        this.chart = chart;
    }

    /**
     * @param width  the width to set.
     * @param height the height to set.
     * @return The chart.
     */
    public Map <String, Image> getImage(int width, int height, String name) {
        final ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
        if ( chart != null){
            Image image = chart.createBufferedImage(width, height, info);
            StringWriter out  = new StringWriter();
            final PrintWriter writer = new PrintWriter(out);
            try {
                ChartUtilities.writeImageMap(writer, name, info, false);                
                String html = out.toString();
                Map <String, Image> result = new HashMap<String, Image>();
                result.put(html, image);
                out.close();
                writer.close();
                return result;
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return null;
    }



    /**
     * Creates a Bar chart.
     *
     * @param dataset the dataset.
     * @param title
     * @return The chart.
     */
    protected JFreeChart createChart(CategoryDataset dataset, String title) {

        // create the chart...
        JFreeChart chart = ChartFactory.createBarChart(
                "",                         // chart title
                "",                         // domain axis label
                title,                      // range axis label
                dataset,                    // data
                PlotOrientation.VERTICAL,   // orientation
                true,                       // include legend
                true,                       // tooltips
                false);                     // URLs

        // set the background color for the chart...
        chart.setBackgroundPaint(Color.white);
        chart.getLegend().setPosition(RectangleEdge.RIGHT);

        // get a reference to the plot for further customisation...
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setRangeGridlinePaint(Color.darkGray);

        // set the range axis to display integers only...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        // disable bar outlines...
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);

        // set up gradient paints for series...
        GradientPaint gp0 = new GradientPaint(0.0f, 0.0f, Color.blue, 0.0f, 0.0f, new Color(0, 0, 96));
        GradientPaint gp1 = new GradientPaint(0.0f, 0.0f, Color.yellow, 0.0f, 0.0f, new Color(0, 96, 0));
        GradientPaint gp2 = new GradientPaint(0.0f, 0.0f, Color.red, 0.0f, 0.0f, new Color(96, 0, 0));
        renderer.setSeriesPaint(0, gp0);
        renderer.setSeriesPaint(1, gp1);
        renderer.setSeriesPaint(2, gp2);

        return chart;
    }

    /**
     * Creates a Pie Chart.
     *
     * @param dataset the dataset.
     * @return The chart.
     */
    protected JFreeChart createChart(PieDataset dataset) {

        JFreeChart chart = ChartFactory.createPieChart3D(
                "",         // chart title
                dataset,    // data
                false,       // include legend
                true,       // tooltips
                false);     // url

        PiePlot3D plot = (PiePlot3D) chart.getPlot();

        plot.setForegroundAlpha(0.5f);
        plot.setNoDataMessage("No data to display");
        if (!GraphicUtil.isGraphLabelEnabled()) plot.setLabelGenerator(null);

        PieToolTipGenerator generator = new StandardPieToolTipGenerator("{0}");
        plot.setToolTipGenerator(generator);
        // set the background color for the chart...
        chart.setBackgroundPaint(Color.white);
//        chart.getLegend().setPosition(RectangleEdge.RIGHT);
        chart.setBorderVisible(false);
        return chart;
    }

    /**
     * Creates a SmartImage object, creates name for the image and put it into provided SmartImageMap.
     *
     * @param smartMap  the SmartImageMap.
     * @param width     the width of image to set.
     * @param height    the height of image to set.
     * @param sessionId the session Id for SmartImage.
     * @param timeOut   the time out for SmartImage.
     * @return The name of SmartImage.
     */
    public Map<String, String> burnSmartImage(SmartImageMap smartMap, int width, int height, String sessionId, Integer timeOut) {
        if (smartMap == null) return null;
        String name;
        final long currentTimeMillis = System.currentTimeMillis();
        long random = currentTimeMillis+new java.util.Random().nextLong();
        if(random<0) random = -1*random;
        name = Long.toString(random);
        Map<String, Image> imageMap = getImage(width, height, name);
        Set<String> keys = imageMap.keySet();
        String html = keys.iterator().next();
        SmartImage smartImage = new SmartImage(imageMap.get(html), sessionId, timeOut, html);
        smartMap.put(name, smartImage);
        Map<String, String> result = new HashMap<String, String>();
        result.put(name, html);
        return  result;
    }
    
}
