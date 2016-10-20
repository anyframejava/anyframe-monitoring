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
package net.sf.infrared2.gwt.client.grid;

import net.sf.infrared2.gwt.client.i18n.ApplicationMessages;

/**
 * <b>DuplicateColumnDefinitionException</b><p>
 * Throws when index of top row column mapping already exist in the definition
 * list.
 * 
 * @author Sergey Evluhin
 */
public class DuplicateColumnDefinitionException extends RuntimeException {

    private static final long serialVersionUID = -455673992330037358L;

    /**
     * Default constructor.
     */
    public DuplicateColumnDefinitionException() {
        super(ApplicationMessages.MESSAGES.duplicateColumnExceptionLabel());
    }

    /**
     * Constructor parameterized by message value.
     * 
     * @param msg - default message of the exception.
     */
    public DuplicateColumnDefinitionException(String msg) {
        super(msg);
    }
}
