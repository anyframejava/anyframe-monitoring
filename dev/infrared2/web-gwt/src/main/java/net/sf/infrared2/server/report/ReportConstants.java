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
 * ReportConstants.java		Date created: 20.03.2008
 * Last modified by: $Author: soyon.lim $
 * $Revision: 14286 $	$Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.server.report;

/**
 * <b>ReportConstants</b><p>
 * Holds report constants for labels and text.
 * @author Andrey Zavgorodniy
 * Copyright Exadel Inc, 2008
 */
public class ReportConstants {
    /* Report file name time stamp */
    public static final String REPORT_TIME_STAMP = "yyyy_MM_dd";
    /* Report file name suffix */
    public static final String REPORT_FILE_NAME_SUFFIX = "_InfraRed_Report";
    /* Labels */
    public static final String TOP = "Top";
    public static final String LAYER = "Layer";
    public static final String ABSOLUTE_MODULE = "Absolute module";
    public static final String HIERARCHICAL_MODULE = "Hierarchical module";
    public static final String TOTAL_TIME = "Total Time (ms)";
    public static final String APPLICATION_LEVEL = "Application level";
    public static final String LAYERS_BY_TOTAL_TIME = "Layers by Total Time";
    public static final String LAYERS_BY_COUNT = "Layers by Count";
    public static final String LAYER_LEVEL_SQL_LAYERS = "Layer level - SQL layers";
    public static final String SQL = "SQL";
    
    public static final String GRAPHIC_FOR = "Graphic for";
    public static final String QUERIES = "queries";
    
    public static final String COLOR = "Color";
    public static final String TOP_OPERATIONS_BY_TOTAL_TIME = "Top 10 operations by Total Time";
    public static final String TOP_OPERATIONS_BY_COUNT = "Top 10 operations by Count";
    public static final String TOP_OPERATIONS_BY_ADJUSTED_AVERAGE_TIME = "Top 10 operations by Adjusted Average Time";
    public static final String TOP_QUERIES_AVERAGE_EXECUTE_TIME = "Top 10 Queries by Average Execute Time";
    public static final String QUERIES_AVERAGE_EXECUTE_TIME = "Queries by Average Execute Time";
    public static final String TOP_QUERIES_EXECUTION_COUNT = "Top 10 queries by Execution Count";
    public static final String QUERIES_EXECUTION_COUNT = "Queries by Execution Count";
    public static final String SQL_QUERY = "Sql Query";
    public static final String ID = "ID";
    public static final String AVERAGE_TIME = "Average Time (ms)";
    public static final String PREPARE = "Prepare";
    public static final String EXECUTE = "Execute";
    public static final String TOTAL = "Total";
    public static final String COUNT = "Count of Calls";
    public static final String STATISTIC = "Statistic";    
    public static final String MAXIMUM_TIME="Maximum Time (ms)";
    public static final String MINIMUM_TIME="Minimum Time (ms)";   
    public static final String FIRST_EXECUTION_TIME = "First Execution Time (ms)"; 
    public static final String LAST_EXECUTION_TIME = "Last Execution Time (ms)";  
    public static final String DIAGRAMS = "Diagrams";
    public static final String QUERIES_BY_AVERAGE_EXECUTION_TIME = "Queries by Average Execution Time";
    public static final String QUERIES_BY_EXECUTION_COUNT = "Queries by Execution Count";
    public static final String OPERATIONS_BY_COUNT = "Operations by Count";
    public static final String OPERATIONS_BY_TOTAL_TIME = "Operations by Total Time";
    public static final String LAYER_LEVEL_INCLUSIVE = " (Inclusive)";
    public static final String LAYER_LEVEL_EXCLUSIVE = " (Exclusive)";
    public static final String OPERATION_NAME = "Operation Name";
    public static final String ADJUSTED_AVERAGE_TIME = "Adjusted Average Time (ms)";
    public static final String MINIMUM_EXECUTION_TIME = "Minimum Execution Time (ms)";
    public static final String MAXIMUM_EXECUTION_TIME = "Maximum Execution Time (ms)";
    public static final String EXCLUSIVE = "Exclusive";
    public static final String INCLUSIVE = "Inclusive";
    public static final String AVERAGE_TIME_TOTAL = "Average Time Total (ms)";
    public static final String AVERAGE_TIME_EXECUTE = "Average Time Execute (ms)";
    public static final String AVERAGE_TIME_PREPARE = "Average Time Prepare (ms)";
    public static final String COUNT_EXECUTE = "Count Execute";
    public static final String COUNT_PREPARE = "Count Prepare";
    public static final String MAX_TIME_EXECUTE = "Maximum Time Execute (ms)";
    public static final String MAX_TIME_PREPARE = "Maximum Time Prepare (ms)";
    public static final String MIX_TIME_EXECUTE = "Minimum Time Execute (ms)";
    public static final String MIX_TIME_PREPARE = "Minimum Time Prepare (ms)";
    public static final String FIRST_EXEC_TIME_EXECUTE = "First Execution Time Execute (ms)";
    public static final String FIRST_EXEC_TIME_PREPARE = "First Execution Time Prepare (ms)";
    public static final String LAST_EXEC_TIME_EXECUTE = "Last Execution Time Execute (ms)";
    public static final String LAST_EXEC_TIME_PREPARE = "Last Execution Time Prepare (ms)";
    public static final int OTHER_LAYER_OPERATION_COUNT = 10;
    public static final String GRAPHIC_FOR_5_QUERIES = "For First 5 Queries Only";
    /* For color fields */
    public static final String TIME_COLOR = "Time color";
    public static final String COUNT_COLOR = "Count color";

    public static final String MERGED_FOR = "Merged Information For: ";

}

