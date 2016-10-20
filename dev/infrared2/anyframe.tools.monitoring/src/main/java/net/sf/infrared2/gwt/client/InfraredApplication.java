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
package net.sf.infrared2.gwt.client;

import com.google.gwt.core.client.EntryPoint;

/**
 * <b>InfraredApplication</b><p>
 * Main class of the client side application.
 * 
 * @author Sergey Evluhin
 */
public class InfraredApplication implements EntryPoint {

    /**
     * Application entry point Builds application layout, init
     * AbsoluteModulesStack (accordeon with links) and put the layout to
     * RootPanel (show application layout).
     */
    public void onModuleLoad() {
        Engine.start();
//        HistoryManager.addListener();
    }
}
