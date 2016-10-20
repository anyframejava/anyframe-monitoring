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
 * RPCServletUtils.java		Date created: 03.04.2008
 * Last modified by: gzgonikov
 * $Revision: 14286 $	Date: 03.04.2008
 */

package net.sf.infrared2.gwt.server.util;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <b>RPCServletUtils</b><p>
 *
 * Keeps util methods for RPCServlet
 *
 * @author: gzgonikov
 */

public class RPCServletUtils {

    /**
     * Called when the servlet itself has a problem, rather than the invoked
     * third-party method.
     *
     * @param servletContext a <code>ServletContext</code>.
     * @param response       a <code>HttpServletResponse</code>.
     * @param failure        a <code>Throwable</code>.
     */
    public static void writeResponseForFailure(ServletContext servletContext,
                                               HttpServletResponse response, Throwable failure) {
        servletContext.log("Exception while dispatching incoming RPC call", failure);
        try {
            response.setContentType("text/plain");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(failure.getMessage());
        } catch (IOException ex) {
            servletContext.log("error respond failed while sending the previous failure to the client", ex);
        }
    }

}
