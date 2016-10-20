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
 * SQLFormatter.java Date created: 22.01.2008
 * Last modified by: $Author: soyon.lim $
 * $Revision: 14286 $ $Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */
package net.sf.infrared2.server.util.sql;

import java.util.StringTokenizer;
import java.util.Vector;

/**
 * <b>SQLFormatter</b><p>
 * Class SQLFormatter utility class for formatting of sql queries
 *
 * @author gzgonikov
 */
class SQLFormatter {
    
    /** Field SPAN_OPEN  */
    private static final String SPAN_OPEN = "<span class=";
    
    /** Field SPAN_CLOSE  */
    private static final String SPAN_CLOSE = "</span>";
    
    /** Field GT  */
    private static final String GT = ">";
    
    /**
     * Surround with span.
     * 
     * @param sqlWord the sql word
     */
    private static String surroundWithSpan(String sqlWord) {
        if (sqlWord == null) {
            return "";
        }
        if (isMajor(sqlWord) || isMinor(sqlWord)) {
            return surroundWithSpan(sqlWord.toUpperCase(), "reservedWord");
        }
        return sqlWord;
    }
    
    /**
     * Surround with span.
     * 
     * @param sqlWord the sql word
     * @param styleClass the style class
     */
    private static String surroundWithSpan(String sqlWord, String styleClass){
        return (SPAN_OPEN + styleClass + GT + sqlWord + SPAN_CLOSE);
    }

    /**
     * Method isName determines is given String a name.
     *
     * @param s of type String
     * @return boolean
     */
    private static boolean isName(String s) {
        return !isIn(s, "|SELECT|FROM|WHERE|ORDER BY|GROUP BY|HAVING|UPDATE|SET|INSERT|INTO|VALUES|DELETE|UNION|ALL|MINUS|") && !isIn(s, "|COUNT|SUM|AVG|MIN|MAX|DISTINCT|AS|ANY|AND|OR|XOR|NOT|LIKE|IN|EXISTS|IS|NULL|");
    }

    /**
     * Method isFunction determines is given String a function.
     *
     * @param s of type String
     * @return boolean
     */
    private static boolean isFunction(String s) {
        return isIn(s, "|COUNT|SUM|AVG|MIN|MAX|");
    }

    /**
     * Method format transform given sql.
     */
    public void format() {
        String s = sText;
        Vector vector = new Vector();
        for (int i = 0; i < s.length(); i++) {
            for (int j = 0; j < sEscapes.length; j++) {
                String s1 = sEscapes[j][0];
                String s3 = sEscapes[j][1];
                String s5 = sEscapes[j][2];
                if (!s.regionMatches(i, s1, 0, s1.length()))
                    continue;
                int j1 = i + s1.length();
                int k1 = s.indexOf(s3, j1);
                if (k1 == -1) {
                    k1 = s.indexOf("\n", j1);
                    if (k1 == -1) {
                        k1 = s.indexOf("\r", j1);
                        if (k1 == -1)
                            k1 = s.length() - 1;
                    }
                    k1++;
                } else {
                    k1 += s3.length();
                }
                String s6 = s.substring(i, k1);
                if (s5.equals("2"))
                    s6 = "/*" + s6.trim().substring(2) + " */";
                vector.addElement(s6);
                String s7 = s.substring(0, i);
                String s8;
                if (k1 < s.length())
                    s8 = s.substring(k1);
                else
                    s8 = "";
                String s9 = "\001";
                if (!s5.equals("")) {
                    if (!s7.endsWith(" "))
                        s9 = " " + s9;
                    if (!s8.startsWith(" "))
                        s9 = s9 + " ";
                }
                s = s7 + s9 + s8;
                break;
            }

        }

        Vector vector1 = new Vector();
        for (StringTokenizer stringtokenizer = new StringTokenizer(s); stringtokenizer.hasMoreTokens();)
        {
            String s2 = stringtokenizer.nextToken();
            for (StringTokenizer stringtokenizer1 = new StringTokenizer(s2, "(),", true); stringtokenizer1.hasMoreTokens(); vector1.addElement(stringtokenizer1.nextToken()))
                ;
        }

        for (int k = 0; k < vector1.size() - 1; k++) {
            String s4 = (String) vector1.elementAt(k) + " " + (String) vector1.elementAt(k + 1);
            if (isMajor(s4)) {
                vector1.setElementAt(s4, k);
                vector1.removeElementAt(k + 1);
            }
        }

        int l = vector1.size();
        String as[] = new String[l += 2];
        as[0] = "";
        as[l - 1] = "";
        for (int i1 = 0; i1 < vector1.size(); i1++)
            as[i1 + 1] = (String) vector1.elementAt(i1);

        int ai[] = new int[l];
        int ai1[] = new int[l];
        for (int l1 = 0; l1 < l; l1++) {
            boolean flag = false;
            if (isMajor(as[l1]))
                flag = bCapitalizeMajor;
            if (isMinor(as[l1]))
                flag = bCapitalizeMinor;
            if (isName(as[l1]))
                flag = bCapitalizeNames;
            if (flag)
                as[l1] = as[l1].toUpperCase();
        }

        for (int i2 = 1; i2 < l - 1; i2++) {
            ai[i2] = 1;
            if (isMajor(as[i2])) {
                ai[i2 - 1] = 2;
                ai[i2] = 2;
            } else if (as[i2].equals(",")) {
                ai[i2] = 2;
                ai[i2 - 1] = 0;
            } else if (as[i2].equals("(")) {
                ai[i2] = 0;
                if (isFunction(as[i2 - 1]) || isName(as[i2 - 1]))
                    ai[i2 - 1] = 0;
            } else if (as[i2].equals(")"))
                ai[i2 - 1] = 0;
            else if (as[i2].equalsIgnoreCase("AND"))
                if (bNewLineBeforeAnd)
                    ai[i2 - 1] = 2;
                else
                    ai[i2] = 2;
        }

        ai[l - 2] = 2;
        int j2 = 0;
        int ai2[] = new int[16];
        for (int k2 = 0; k2 < l; k2++) {
            if (as[k2].equals(")"))
                if (ai2[j2] == 0) {
                    j2--;
                    if (k2 > 0)
                        ai[k2 - 1] = 2;
                } else {
                    ai2[j2]--;
                }
            if (isMajor(as[k2]))
                ai1[k2] = j2 * 2;
            else
                ai1[k2] = j2 * 2 + 1;
            if (as[k2].equals("("))
                if (isSubSelect(as[k2 + 1])) {
                    if (j2 < 16)
                        j2++;
                    ai2[j2] = 0;
                } else {
                    ai2[j2]++;
                }
        }

        String as1[] = new String[3];
        as1[0] = "";
        as1[1] = " ";
        as1[2] = sNewLine;
        StringBuffer stringbuffer = new StringBuffer();
        for (int l2 = 1; l2 < l - 1; l2++) {
            if (ai[l2 - 1] == 2)
                stringbuffer.append(repeatString(sIndent, ai1[l2]));
            stringbuffer.append(surroundWithSpan(as[l2]) + surroundWithSpan(as1[ai[l2]]));
        }

        s = stringbuffer.toString();
        for (int i3 = 0; i3 < vector.size(); i3++) {
            int j3 = s.indexOf("\001");
            s = surroundWithSpan(s.substring(0, j3)) + surroundWithSpan((String) vector.elementAt(i3)) + surroundWithSpan(s.substring(j3 + 1));
        }

        sText = s;
        if (bDebug) {
            StringBuffer stringbuffer1 = new StringBuffer();
            stringbuffer1.append("Tokens:\r\n");
            for (int k3 = 1; k3 < l - 1; k3++)
                stringbuffer1.append("" + ai1[k3] + " [" + as[k3] + "] " + ai[k3] + "\r\n");

            stringbuffer1.append("Escapes:\r\n");
            for (int l3 = 0; l3 < vector.size(); l3++)
                stringbuffer1.append((String) vector.elementAt(l3) + "\r\n");

            sDebugString = stringbuffer1.toString();
        }
    }

    /**
     * Method setNewLineBeforeAnd sets the newLineBeforeAnd of this SQLFormatter object.
     *
     * @param flag the newLineBeforeAnd of this SQLFormatter object.
     *
     */
    public void setNewLineBeforeAnd(boolean flag) {
        bNewLineBeforeAnd = flag;
    }

    /**
     * Method getDebugString returns the debugString of this SQLFormatter object.
     *
     * @return the debugString (type String) of this SQLFormatter object.
     */
    public String getDebugString() {
        return sDebugString;
    }

    /**
     * Method setNewLine sets the newLine of this SQLFormatter object.
     *
     * @param s the newLine of this SQLFormatter object.
     *
     */
    public void setNewLine(String s) {
        for (int i = 0; i < sEscapes.length; i++) {
            for (int j = 0; j < sEscapes[0].length; j++)
                if (sEscapes[i][j].equals(sNewLine))
                    sEscapes[i][j] = s;

        }

        sNewLine = s;
    }

    /**
     * Constructor SQLFormatter creates a new SQLFormatter instance.
     */
    SQLFormatter() {
        sText = "";
        sNewLine = "\r\n";
        sIndent = "   ";
        sDebugString = "";
        bCapitalizeMajor = false;
        bCapitalizeMinor = false;
        bCapitalizeNames = false;
        bNewLineBeforeAnd = true;
        bDebug = false;
    }

    /**
     * Method isMinor ...
     *
     * @param s of type String
     * @return boolean
     */
    private static boolean isMinor(String s) {
        return isIn(s, "|COUNT|SUM|AVG|MIN|MAX|DISTINCT|AS|ANY|AND|OR|XOR|NOT|LIKE|IN|EXISTS|IS|NULL|");
    }

    /**
     * Method isIn ...
     *
     * @param s of type String
     * @param s1 of type String
     * @return boolean
     */
    private static boolean isIn(String s, String s1) {
        return s1.indexOf("|" + s.toUpperCase() + "|") > -1;
    }

    /**
     * Method isSubSelect ...
     *
     * @param s of type String
     * @return boolean
     */
    private static boolean isSubSelect(String s) {
        return isIn(s, "|SELECT|");
    }

    /**
     * Method setCapitalizeMajor sets the capitalizeMajor of this SQLFormatter object.
     *
     * @param flag the capitalizeMajor of this SQLFormatter object.
     *
     */
    public void setCapitalizeMajor(boolean flag) {
        bCapitalizeMajor = flag;
    }

    /**
     * Method setCapitalizeNames sets the capitalizeNames of this SQLFormatter object.
     *
     * @param flag the capitalizeNames of this SQLFormatter object.
     *
     */
    public void setCapitalizeNames(boolean flag) {
        bCapitalizeNames = flag;
    }

    /**
     * Method setIndent sets the indent of this SQLFormatter object.
     *
     * @param i the indent of this SQLFormatter object.
     *
     */
    public void setIndent(int i) {
        if (i < 0)
            sIndent = "\t";
        else
            sIndent = repeatString(" ", i);
    }

    /**
     * Method setDebug sets the debug of this SQLFormatter object.
     *
     * @param flag the debug of this SQLFormatter object.
     *
     */
    public void setDebug(boolean flag) {
        bDebug = flag;
    }

    /**
     * Method repeatString ...
     *
     * @param s of type String
     * @param i of type int
     * @return String
     */
    private String repeatString(String s, int i) {
        if (i < 1)
            return "";
        StringBuffer stringbuffer = new StringBuffer(s.length() * i);
        for (int j = 0; j < i; j++)
            stringbuffer.append(s);

        return stringbuffer.toString();
    }

    /**
     * Method setCapitalizeMinor sets the capitalizeMinor of this SQLFormatter object.
     *
     * @param flag the capitalizeMinor of this SQLFormatter object.
     *
     */
    public void setCapitalizeMinor(boolean flag) {
        bCapitalizeMinor = flag;
    }

    /**
     * Method isMajor ...
     *
     * @param s of type String
     * @return boolean
     */
    private static boolean isMajor(String s) {
        return isIn(s, "|SELECT|FROM|WHERE|ORDER BY|GROUP BY|HAVING|UPDATE|SET|INSERT|INTO|VALUES|DELETE|UNION|ALL|MINUS|");
    }

    /**
     * Method getText returns the text of this SQLFormatter object.
     *
     * @return the text (type String) of this SQLFormatter object.
     */
    public String getText() {
        return sText;
    }

    /**
     * Method setText sets the text of this SQLFormatter object.
     *
     * @param s the text of this SQLFormatter object.
     *
     */
    public void setText(String s) {
        sText = s;
    }

    private static final String MAJOR_WORDS = "|SELECT|FROM|WHERE|ORDER BY|GROUP BY|HAVING|UPDATE|SET|INSERT|INTO|VALUES|DELETE|UNION|ALL|MINUS|";
    private static final String MINOR_WORDS = "|COUNT|SUM|AVG|MIN|MAX|DISTINCT|AS|ANY|AND|OR|XOR|NOT|LIKE|IN|EXISTS|IS|NULL|";
    private static final String FUNCTION_WORDS = "|COUNT|SUM|AVG|MIN|MAX|";
    private static final String SUB_SELECT = "|SELECT|";
    private static final String ESCAPE_TOKEN = "\001";
    private static final String DELIMITERS = "(),";
    private static final int MAX_INDENTS = 16;
    private static final int NOTHING = 0;
    private static final int SPACE = 1;
    private static final int NEW_LINE = 2;
    private String sEscapes[][] = {
            {
                    "'", "'", ""
            }, {
            "\"", "\"", ""
    }, {
            "/*", "*/", "1"
    }, {
            "--", "\r\n", "2"
    }
    };
    private String sText;
    private String sNewLine;
    private String sIndent;
    private String sDebugString;
    private boolean bCapitalizeMajor;
    private boolean bCapitalizeMinor;
    private boolean bCapitalizeNames;
    private boolean bNewLineBeforeAnd;
    private boolean bDebug;
}

