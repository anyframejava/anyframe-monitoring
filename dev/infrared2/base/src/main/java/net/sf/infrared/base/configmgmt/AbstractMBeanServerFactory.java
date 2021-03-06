/* 
 * Copyright 2005 Tavant Technologies and Contributors
 * 
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 *
 *
 * Original Author:  subin.p (Tavant Technologies)
 * Contributor(s):   -;
 *
 */
package net.sf.infrared.base.configmgmt;

import javax.management.MBeanServer;

/**
 * Factory interface to get a JMX MBean server. 
 * 
 * Concrete implementations of this factory provide app-server-specific implementations of 
 * javax.management.MBeanServer.
 * This is an instance of the abstract factory design pattern.
 * 
 * @author subin.p
 */
public interface AbstractMBeanServerFactory {
    
    /**
     * Gets an MBeanServer implementation, specific to a JMX provider (usually app-servers).
     */
    public MBeanServer getMBeanServer();
}
