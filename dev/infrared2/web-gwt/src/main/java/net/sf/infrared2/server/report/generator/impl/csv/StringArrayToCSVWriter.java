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
 * StringArrayToCSVWriter.java		Date created: 17.02.2008
 * Last modified by: $Author: soyon.lim $
 * $Revision: 14286 $	$Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.server.report.generator.impl.csv;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * <b>StringArrayToCSVWriter</b><p>
 * Converts 2D String Array values in CSV formatted text.
 * @author Roman Ivanenko
 */
public class StringArrayToCSVWriter {

    /**
     * Default state of auto flush
     */
    private static final boolean AUTO_FLUSH_DEFAULT = true;

    /** The Constant LINE_SEPARATOR_PROPERTY. */
    private static final String LINE_SEPARATOR_PROPERTY = "line.separator";

    /**
     * If auto flushing is enabled.
     */
    private boolean autoFlush = AUTO_FLUSH_DEFAULT;

    /**
     * Default state of always quote
     */
    private static final boolean ALWAYS_QUOTE_DEFAULT = false;

    /**
     * If auto flushing is enabled.
     */
    private boolean alwaysQuote = ALWAYS_QUOTE_DEFAULT;

    /**
     * true if an error has occurred.
     */
    private boolean error = false;

    /**
     * Default delimiter character
     */
    private static final char DELIMITER_DEFAULT = ',';

    /**
     * Character written as field delimiter.
     */
    private char delimiterChar = DELIMITER_DEFAULT;

    /**
     * Default quoting character
     */
    private static final char QUOTE_DEFAULT = '"';

    /**
     * Quoting character.
     */
    private char quoteChar = QUOTE_DEFAULT;

    /**
     * True iff we just began a new line.
     */
    private boolean newLine = true;

    /**
     * Default start of comments
     */
    private static final char COMMENT_START_DEFAULT = '#';

    /**
     * Character used to start comments. (Default is '#')
     */
    private char commentStart = COMMENT_START_DEFAULT;

    /**
     * Line ending default
     */
    private static final String LINE_ENDING_DEFAULT = "\n";

    /**
     * Line ending indicating the system line ending should be chosen
     */
    private static final String LINE_ENDING_SYSTEM = null;

    /**
     * The line ending, must be one of "\n", "\r", or "\r\n"
     */
    private String lineEnding = LINE_ENDING_DEFAULT;

    /**
     * The place that the values get written.
     */
    protected Writer out;

    /**
     * Conctructor.
     * Character to byte conversion is done using the default character encoding.
     * Comments will be written using the default comment character '#'.
     * The delimiter will be the comma.
     * The line ending will be the default system line ending.
     * The quote character will be double quotes, quotes will be used when needed.
     * And auto flushing will be enabled.
     *
     * @param out a <code>OutputStream</code>.
     */
    public StringArrayToCSVWriter(OutputStream out) {
        this.out = new OutputStreamWriter(out);
    }

    /**
     * Conctructor.
     * The delimiter will be the comma,
     * the line ending will be the default system line ending,
     * the quote character will be double quotes,
     * quotes will be used when needed, and
     * auto flushing will be enabled.
     *
     * @param out stream to which to print.
     * @param commentStart Character used to start comments.
     */
    public StringArrayToCSVWriter(Writer out, char commentStart) {
        this(out, commentStart, QUOTE_DEFAULT, DELIMITER_DEFAULT);
    }

    /**
     * Conctructor.
     * The comment character will be the number sign, the delimiter will
     * be the comma, the line ending will be the default
     * system line ending, and the quote character will be double quotes.
     *
     * @param out stream to which to print.
     * @param alwaysQuote true if quotes should be used even when not strictly needed.
     * @param autoFlush should auto flushing be enabled.
     */
    public StringArrayToCSVWriter(Writer out, boolean alwaysQuote, boolean autoFlush) {
        this(out, COMMENT_START_DEFAULT, QUOTE_DEFAULT, DELIMITER_DEFAULT, alwaysQuote, autoFlush);
    }

    /**
     * Conctructor.
     * The line ending will be the default system line ending,
     * quotes will be used when needed,
     * and auto flushing will be enabled.
     *
     * @param out stream to which to print.
     * @param commentStart Character used to start comments.
     * @param delimiter The new delimiter character to use.
     * @param quote The new character to use for quoting.
     * @throws IllegalArgumentException if the character cannot be used as a quote or delimiter.
     */
    public StringArrayToCSVWriter(Writer out, char commentStart, char quote, char delimiter) throws IllegalArgumentException {
        this(out, commentStart, quote, delimiter, LINE_ENDING_SYSTEM);
    }

    /**
     * Conctructor.
     * Quotes will be used when needed, and auto flushing will be enabled.
     *
     * @param out stream to which to print.
     * @param commentStart Character used to start comments.
     * @param delimiter The new delimiter character to use.
     * @param quote The new character to use for quoting.
     * @param lineEnding The new line ending, or null to use the default line ending.
     * @throws IllegalArgumentException if the character cannot be used as a quote or delimiter.
     */
    public StringArrayToCSVWriter(Writer out, char commentStart, char quote, char delimiter, String lineEnding) throws IllegalArgumentException {
        this(out, commentStart, quote, delimiter, lineEnding, ALWAYS_QUOTE_DEFAULT, AUTO_FLUSH_DEFAULT);
    }

    /**
     * Conctructor.
     * The line ending will be the default system line ending.
     *
     * @param out a <code>Writer</code>
     * @param commentStart Character used to start comments.
     * @param delimiter The new delimiter character to use.
     * @param quote The new character to use for quoting.
     * @param alwaysQuote true if quotes should be used even when not strictly needed.
     * @param autoFlush should auto flushing be enabled.
     * @throws IllegalArgumentException if the character cannot be used as a quote or delimiter.
     */
    public StringArrayToCSVWriter(Writer out, char commentStart, char quote, char delimiter, boolean alwaysQuote, boolean autoFlush)
            throws IllegalArgumentException {
        this(out, commentStart, quote, delimiter, LINE_ENDING_SYSTEM, alwaysQuote, autoFlush);
    }

    /**
     * Conctructor.
     *
     * @param out a <code>Writer</code>
     * @param commentStart Character used to start comments.
     * @param delimiter The new delimiter character to use.
     * @param lineEnding The new line ending, or null to use the default line ending.
     * @param quote The new character to use for quoting.
     * @param alwaysQuote true if quotes should be used even when not strictly needed.
     * @param autoFlush should auto flushing be enabled.
     * @throws IllegalArgumentException if the character cannot be used as a quote or delimiter.
     */
    public StringArrayToCSVWriter(Writer out, char commentStart, char quote, char delimiter, String lineEnding, boolean alwaysQuote, boolean autoFlush)
            throws IllegalArgumentException {
        this.out = out;
        this.commentStart = commentStart;
        setNewQuoteChar(quote);
        setNewDelimiterChar(delimiter);
        setNewLineEnding(lineEnding);
        setAlwaysQuote(alwaysQuote);
        setAutoFlush(autoFlush);
    }

    /**
     * @param alwaysQuote the alwaysQuote to set
     */
    private void setAlwaysQuote(boolean alwaysQuote) {
        this.alwaysQuote = alwaysQuote;
    }

    /**
     * @param autoFlush the autoFlush to set
     */
    private void setAutoFlush(boolean autoFlush) {
        this.autoFlush = autoFlush;
    }

    /**
     * Sets the new delimiter char.
     * 
     * @param newDelimiterChar the new new delimiter char
     * 
     * @throws IllegalArgumentException the illegal argument exception
     */
    public void setNewDelimiterChar(char newDelimiterChar) throws IllegalArgumentException {
        if (delimiterChar != newDelimiterChar) {
            if ((newDelimiterChar == '\n') || (newDelimiterChar == '\r') || (newDelimiterChar == delimiterChar) || (newDelimiterChar == quoteChar)) {
                throw new IllegalArgumentException();
            }
            delimiterChar = newDelimiterChar;
        }
    }

    /**
     * Sets the new quote char.
     * 
     * @param newQuoteChar the new new quote char
     * 
     * @throws IllegalArgumentException the illegal argument exception
     */
    public void setNewQuoteChar(char newQuoteChar) throws IllegalArgumentException {
        if (quoteChar != newQuoteChar) {
            if ((newQuoteChar == '\n') || (newQuoteChar == '\r') || (newQuoteChar == delimiterChar) || (newQuoteChar == quoteChar)) {
                throw new IllegalArgumentException();
            }
            quoteChar = newQuoteChar;
        }
    }

    /**
     * Sets the new line ending.
     * 
     * @param newLineEnding the new new line ending
     * 
     * @throws IllegalArgumentException the illegal argument exception
     */
    public void setNewLineEnding(String newLineEnding) throws IllegalArgumentException {
        boolean isDefault = ((newLineEnding == null) ? true : false);
        if (isDefault == true) {
            newLineEnding = System.getProperty(LINE_SEPARATOR_PROPERTY);
        }
        if ((!"\n".equals(newLineEnding)) && (!"\r".equals(newLineEnding)) && (!"\r\n".equals(newLineEnding))) {
            if (isDefault == true) {
                newLineEnding = LINE_ENDING_DEFAULT;
            } else {
                throw new IllegalArgumentException();
            }
        }
        this.lineEnding = newLineEnding;
    }

    /**
     * Output a blank line.
     * 
     * @throws IOException if an error occurs while writing.
     */
    public void writeln() throws IOException {
        try {
            out.write(lineEnding);
            if (autoFlush) {
                flush();
            }
            newLine = true;
        } catch (IOException iox) {
            error = true;
            throw iox;
        }
    }

    /**
     * Flush any data written out to underlying streams.
     *
     * @throws IOException if IO error occurs
     */
    public void flush() throws IOException {
        out.flush();
    }

    /**
     * Close any underlying streams.
     *
     * @throws IOException if IO error occurs
     */
    public void close() throws IOException {
        out.close();
    }

    /**
     * Print the string as the last value on the line.
     * The value will be quoted if needed.
     *
     * @param value value to be outputted.
     * @throws IOException if an error occurs while writing.
     */
    public void writeln(String value) throws IOException {
        try {
            write(value);
            writeln();
        } catch (IOException iox) {
            error = true;
            throw iox;
        }
    }

    /**
     * Write.
     * 
     * @param line the line
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void write(String line) throws IOException {
        try {
            if (line == null) {
                line = "";
            }
            boolean isQuote = isQuote(line);
            if (newLine == true) {
                newLine = false;
            } else {
                this.out.write(delimiterChar);
            }
            if (isQuote == true) {
                this.out.write(escapeAndQuoteText(line));
            } else {
                this.out.write(line);
            }
            if (autoFlush == true) {
                this.flush();
            }
        } catch (IOException e) {
            error = true;
            throw e;
        }
    }

    /**
     * Checks if is quote.
     * 
     * @param line the line
     * 
     * @return true, if is quote
     */
    private boolean isQuote(String line) {
        boolean result = false;
        if (alwaysQuote == true) {
            result = true;
        } else {
            if (line.length() > 0) {
                char symbol = line.charAt(0);
                if ((newLine && (symbol < '0' || (symbol > '9' && symbol < 'A') || (symbol > 'Z' && symbol < 'a') || (symbol > 'z'))) || (symbol == ' ' || symbol == '\f' || symbol == '\t')) {
                    result = true;
                }
                for (int i = 0; i < line.length(); i++) {
                    symbol = line.charAt(i);
                    if (symbol == quoteChar || symbol == delimiterChar || symbol == '\n' || symbol == '\r') {
                        result = true;
                    }
                }
                if (symbol == ' ' || symbol == '\f' || symbol == '\t') {
                    result = true;
                }
            } else if (newLine == true) {
                result = true;
            }
        }
        return result;
    }

    /**
     * Escape and quote text.
     * 
     * @param text the text
     * 
     * @return the string
     */
    private String escapeAndQuoteText(String text) {
        StringBuffer buffer = new StringBuffer(text.length() + findQuote(text));
        buffer.append(quoteChar);
        for (int i = 0; i < text.length(); i++) {
            char symbol = text.charAt(i);
            writeEscapedSymbol(buffer, symbol);

        }
        buffer.append(quoteChar);
        return (buffer.toString());
    }

    /**
     * Find quote.
     * 
     * @param text the text
     * 
     * @return the int
     */
    private int findQuote(String text) {
        int result = 2;
        for (int i = 0; i < text.length(); i++) {
            char symbol = text.charAt(i);
            if (symbol == '\\') {
                result++;
                break;
            } else {
                if (symbol == quoteChar) {
                    result++;
                }
                break;
            }
        }
        return result;
    }

    /**
     * Write escaped symbol.
     * 
     * @param buffer the buffer
     * @param symbol the symbol
     */
    private void writeEscapedSymbol(StringBuffer buffer, char symbol) {
        if (symbol == '\n') {
            buffer.append("\\n");
        } else if (symbol == '\r') {
            buffer.append("\\r");
        } else if (symbol == '\\') {
            buffer.append("\\\\");
        } else {
            if (symbol == quoteChar) {
                buffer.append("\\" + quoteChar);
            } else {
                buffer.append(symbol);
            }
        }
    }

    /**
     * Print a single line of comma separated values. The values will be quoted
     * if needed. Quotes and and other characters that need it will be escaped.
     *
     * @param values values to be outputted.
     * @throws IOException if an error occurs while writing.
     */
    public void write(String[] values) throws IOException {
        try {
            for (int i = 0; i < values.length; i++) {
                write(values[i]);
            }
        } catch (IOException iox) {
            error = true;
            throw iox;
        }
    }

    /**
     * Print a single line of comma separated values. The values will be quoted
     * if needed. Quotes and and other characters that need it will be escaped.
     *
     * @param values values to be outputted.
     * @throws IOException if an error occurs while writing.
     */
    public void writeln(String[] values) throws IOException {
        try {
            write(values);
            writeln();
        } catch (IOException iox) {
            error = true;
            throw iox;
        }
    }

    /**
     * Print several lines of comma separated values. The values will be quoted
     * if needed. Quotes and newLine characters will be escaped.
     *
     * @param values values to be outputted.
     * @throws IOException if an error occurs while writing.
     */
    public void writeln(String[][] values) throws IOException {
        try {
            for (int i = 0; i < values.length; i++) {
                writeln(values[i]);
            }
            if (values.length == 0) {
                writeln();
            }
        } catch (IOException iox) {
            error = true;
            throw iox;
        }
    }

    /**
     * Put a comment among the comma separated values. Comments will always
     * begin on a new line and occupy a least one full line. The character
     * specified to star comments and a space will be inserted at the beginning
     * of each new line in the comment. If the comment is null, an empty comment
     * is outputted.
     *
     * @param comment the comment to output.
     * @throws IOException if an error occurs while writing.
     */
    public void writelnComment(String comment) throws IOException {
        try {
            if (comment == null) {
                comment = "";
            }
            if (!newLine) {
                writeln();
            }
            out.write(commentStart);
            out.write(' ');
            for (int i = 0; i < comment.length(); i++) {
                char c = comment.charAt(i);
                switch (c) {
                case '\r':
                    if (i + 1 < comment.length() && comment.charAt(i + 1) == '\n') {
                        i++;
                    }
                case '\n':
                    writeln();
                    out.write(commentStart);
                    out.write(' ');
                    break;
                default:
                    out.write(c);
                    break;
                }
            }
            writeln();
        } catch (IOException iox) {
            error = true;
            throw iox;
        }
    }

}
