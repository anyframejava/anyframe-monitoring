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
 * SQLToHtml.java Date created: 22.01.2008
 * Last modified by: $Author: soyon.lim $
 * $Revision: 14286 $ $Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */
package net.sf.infrared2.server.util.sql;

import java.io.IOException;

import org.apache.log4j.Logger;

/**
 * <b>SQLToHtml</b>
 * <p>
 * Most of the code in this package is taken from <a href="http://ostermiller.org/syntax/">syntax highlighting</a> package. The only change that is
 * done is the SQLLexer has been changed to allow double quotes for enclosing strings. For this the regular expression for string has been tweaked.
 * <p/> Also a new categaroy of quoted strings is added <p/> This package also uses <a href="http://home.comcast.net/~danmeany/sqlformatter.html">
 * SQLFormatter</a> for SQL formatting.
 */
public class SQLToHtml {

    /** Field logger */
    private static final Logger logger = Logger.getLogger(SQLToHtml.class.getName());
    /** Field SPAN_OPEN */
    private static final String SPAN_OPEN = "<span class=";
    /** Field SPAN_CLOSE */
    private static final String SPAN_CLOSE = "</span>";
    /** Field GT */
    private static final String GT = ">";

    /**
     * Method convertToHtml adds to sql html tags.
     * 
     * @param sql of type String
     * @return String
     */
    public static String convertToHtml(String sql) {
        SQLFormatter formatter = new SQLFormatter();
        formatter.setText(writeEscapedHTML(sql));
        formatter.format();
        sql = formatter.getText();
        String formattedString = null;
        try {
            formattedString = parseSql(sql);
        } catch (IOException e) {
            formattedString = sql;
            logger.error("Not able to parse the sql", e);
        }
        return formattedString;
    }

    /**
     * Method formatSql fromat sql.
     * 
     * @param sql of type String
     * @return String
     * @throws IOException when
     */
    public static String formatSql(String sql) throws IOException {
        return sql;
    }

    /**
     * Method formats Sql to plain string.
     * 
     * @param sql of type String
     * @return String
     * @throws IOException when
     */
    public static String formatSqlToPlainString(String sql) throws IOException {
        return sql;
    }

    /**
     * Method parseSql check is sql is parceble.
     * 
     * @param sql of type String
     * @return String
     * @throws IOException when
     */
    public static String parseSql(String sql) throws IOException {
        StringBuffer sb = new StringBuffer();
        sb.append("<pre>");
        sb.append(sql);
        sb.append("</pre>");
        return sb.toString();
    }

    /**
     * Method openSpan open span.
     * 
     * @param description of type String
     * @param sb of type StringBuffer
     */
    private static void openSpan(String description, StringBuffer sb) {
        sb.append(SPAN_OPEN).append(description).append(GT);
    }

    /**
     * Method closeSpan close span.
     * 
     * @param sb of type StringBuffer
     */
    private static void closeSpan(StringBuffer sb) {
        sb.append(SPAN_CLOSE);
    }

    /**
     * Write the string after escaping characters that would hinder it from rendering in html.
     * 
     * @param text The string to be escaped and written
     * @param sb output gets written here
     */
    public static void writeEscapedHTML(String text, StringBuffer sb) {
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            switch (ch) {
            case '<': {
                sb.append("&lt;");
                break;
            }
            case '>': {
                sb.append("&gt;");
                break;
            }
            case '&': {
                sb.append("&amp;");
                break;
            }
            case '"': {
                sb.append("&quot;");
                break;
            }
            default: {
                sb.append(ch);
                break;
            }
            }
        }
    }
    
    /**
     * Write escaped html.
     * 
     * @param text the text
     * 
     * @return the string
     */
    public static String writeEscapedHTML(String text){
        StringBuffer buffer = new StringBuffer();
        writeEscapedHTML(text, buffer);
        return buffer.toString();
    }

}
