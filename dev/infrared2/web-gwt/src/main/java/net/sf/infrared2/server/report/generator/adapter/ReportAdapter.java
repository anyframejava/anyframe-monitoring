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
 * ReportAdapter.java		Date created: 17.02.2008
 * Last modified by: $Author: soyon.lim $
 * $Revision: 14286 $	$Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.server.report.generator.adapter;

import net.sf.infrared2.gwt.client.to.application.ApplicationRowTO;
import net.sf.infrared2.gwt.client.to.application.ApplicationViewTO;
import net.sf.infrared2.gwt.client.to.other.SummaryRowTO;
import net.sf.infrared2.server.util.SqlStatistics;

/**
 * <b>ReportAdapter</b><p>
 * Class ReportAdapter ...
 *
 * @author gzgonikov
 */
public class ReportAdapter {

    /**
     * Method adaptSqlViewTOArray  adapt ApplicationViewTO to  String[][]
     *
     * @param applicationViewTO a <code>ApplicationViewTO</code>
     * @param isColorEnabled    a <code>boolean</code>
     * @return  String[][] from ApplicationViewTO adapted for StringArrayToCSVWriter
     */
    public static String[][] adaptApplicationViewTOArray(ApplicationViewTO applicationViewTO, boolean isColorEnabled) {
        if (applicationViewTO==null || applicationViewTO.getRows()==null) return new String[0][];
        String[][] result = new String[applicationViewTO.getRows().length][];
        int index=0;
        for (ApplicationRowTO to:applicationViewTO.getRows()){
            if (to==null) continue;
            int rowIndex=0;
            String[] row;
            if (isColorEnabled) row = new String[5];
            else row = new String[3];
            if (isColorEnabled){
                row[rowIndex++]=to.getTimeColor();
                row[rowIndex++]=to.getCountColor();
            }
            row[rowIndex++]=to.getLayer();
            row[rowIndex++]=Double.toString(to.getTotalTime());
            row[rowIndex]=Integer.toString(to.getCount());
            result[index++]=row;
        }
        return result;
    }

    /**
     * Method adaptSqlViewTOArray  adapt SqlStatistics[] to  String[][]
     *
     * @param   sqlViewTO a <code>SqlStatistics[]</code>
     * @return  String[][], from SqlStatistics[] adapted for StringArrayToCSVWriter
     */
    public static String[][] adaptSqlViewTOArray(SqlStatistics[] sqlViewTO) {
        if (sqlViewTO==null) return new String[0][];
        String[][] result = new String[sqlViewTO.length][];
        int index=0;
        for (SqlStatistics to:sqlViewTO){
            result[index++]=new String[]{
                    to.getSql(),
                    to.getId(),
                    Double.toString(to.getAvgTotalTime()),
                    Double.toString(to.getAvgExecuteTime()),
                    Double.toString(to.getAvgPrepareTime()),
                    Long.toString(to.getNoOfExecutes()),
                    Long.toString(to.getNoOfPrepares()),
                    Double.toString(to.getMaxExecuteTime()),
                    Double.toString(to.getMaxPrepareTime()),
                    Double.toString(to.getMinExecuteTime()),
                    Double.toString(to.getMinPrepareTime()),
                    Long.toString(to.getFirstExecuteTime()),
                    Long.toString(to.getFirstPrepareTime()),
                    Long.toString(to.getLastExecuteTime()),
                    Long.toString(to.getLastPrepareTime())
            };
        }
        return result;
    }

    /**
     * Method adaptSummaryRowTOToArray adapt SummaryRowTO[] to  String[][]
     *
     * @param summaryRowTOs  a <code>SummaryRowTO[]</code>
     * @param isColorEnabled a <code>boolean</code>
     * @param isTimeColor    a <code>boolean</code>
     * @return  String[][], from SummaryRowTO[] adapted for StringArrayToCSVWriter
     */
    public static String[][] adaptSummaryRowTOToArray(SummaryRowTO[] summaryRowTOs, boolean isColorEnabled, boolean isTimeColor) {
        if (summaryRowTOs==null) return new String[0][];
        String[][] result = new String[summaryRowTOs.length][];
        int index=0;
        for (SummaryRowTO to : summaryRowTOs){
            if (to==null) continue;
        int rowIndex=0;
            String[] row;
            if (isColorEnabled) row = new String[10];
            else row = new String[9];
            if (isColorEnabled && isTimeColor){
                row[rowIndex++]=to.getTimeColor();
            }
            else if (isColorEnabled){
                row[rowIndex++]=to.getCountColor();
            }
            row[rowIndex++]=to.getOperationName();
            row[rowIndex++]=Double.toString(to.getTotalTime());
            row[rowIndex++]=Integer.toString(to.getCount());
            row[rowIndex++]=Double.toString(to.getAvg());
            row[rowIndex++]=Double.toString(to.getAdjAvg());
            row[rowIndex++]=Integer.toString(to.getMin());
            row[rowIndex++]=Integer.toString(to.getMax());
            row[rowIndex++]=Integer.toString(to.getFirst());
            row[rowIndex]=Integer.toString(to.getLast());
            result[index++]=row;
        }
        return result;
    }

    /**
     * Method getHexColors adapt integer representation of colors to String one.
     *
     * @param intColors of type int[][]
     * @return String[]
     */
    public static String[] getHexColors(int [][] intColors){
        String[] result = new String[intColors.length];
        int index=0;
        for (int[] intColor: intColors){
            String red=null, green=null, blue=null;
            for (int component: intColor){
                if (red==null){
                    red = Integer.toHexString(component);
                    if (red.length()<2) red = "0"+red;
                    continue;
                }
                if (green==null){
                    green = Integer.toHexString(component);
                    if (green.length()<2) green = "0"+green;
                    continue;
                }
                if (blue==null) blue = Integer.toHexString(component);
                if (blue.length()<2) blue = "0"+blue;
            }
            result[index++] = red+green+blue;
        }
        return result;
    }

}
