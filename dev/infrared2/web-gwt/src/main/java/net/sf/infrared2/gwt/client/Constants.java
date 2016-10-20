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
 * Constants.java Date created: 05.02.2008
 * Last modified by: $Author: soyon.lim $ 
 * $Revision: 14286 $ $Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.gwt.client;

/**
 * <b>Constants</b><p>
 * Contains constants values for the client (in some cases and server) side of
 * application.
 * 
 * @author Sergey Evluhin
 */
public class Constants {

    /** Short name for last invocation module. */
    public static final String MODULE_LAST_INV = "lastinv";
    /** Short name for last absolute module. */
    public static final String MODULE_ABSOLUTE = "abs";
    /** Short name for last hierarchical module. */
    public static final String MODULE_HIERARCHICAL = "hier";
    /** Title of PDF format report. */
    public static final String PDF_FORMAT_NAME = "PDF";
    /** Title of Microsoft Excel format report. */
    public static final String MS_EXCEL_FORMAT_NAME = "MS-Excel";
    /** Title of HTML format report. */
    public static final String HTML_FORMAT_NAME = "HTML";
    /** Title of CSV format report. */
    public static final String CSV_FORMAT_NAME = "CSV";
    /** SQL label constant. */
    public static final String TITLE_SQL = "SQL";

    /* ResultStatus constants */
    /** Code of success callback result. */
    public static final int SUCCESS_CODE = 111;
    /** Code of session expired error. */
    public static final int SESSION_EXPIRED_CODE = 333;
}
