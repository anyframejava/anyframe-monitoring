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
 * AggregateExecutionTimeWithId.java Date created: 31.01.2008
 * Last modified by: $Author: soyon.lim $
 * $Revision: 14286 $ $Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.server.model;

import net.sf.infrared.base.model.AggregateExecutionTime;
import net.sf.infrared.base.model.ExecutionContext;

/**
 * <b>AggregateExecutionTimeWithId</b><p>
 * Class AggregateExecutionTimeWithId extends functionality
 * of AggregateExecutionTime to add ID of operation.
 * @see    net.sf.infrared.base.model.AggregateExecutionTime
 *
 * @author gzgonikov
 */
public class AggregateExecutionTimeWithId extends AggregateExecutionTime {

    /** Field id  */
    private String id;

    /**
     * Creates a new AggregateExecutionTime object with id property, which can track timing
     * information of multiple executions of the given ExecutionContext
     * @param ctx a <code>ExecutionContext</code>
     */
    public AggregateExecutionTimeWithId(ExecutionContext ctx) {
        super(ctx);
    }

    /**
     * Constructor AggregateExecutionTimeWithId creates a new AggregateExecutionTimeWithId instance.
     */
    public AggregateExecutionTimeWithId() {
        this(null);
    }

    /**
     * Method getId returns the id of this AggregateExecutionTimeWithId object.
     *
     * @return the id (type String) of this AggregateExecutionTimeWithId object.
     */
    public String getId() {
        return id;
    }

    /**
     * Method setId sets the id of this AggregateExecutionTimeWithId object.
     *
     * @param id the id of this AggregateExecutionTimeWithId object.
     *
     */
    public void setId(String id) {
        this.id = id;
    }
}
