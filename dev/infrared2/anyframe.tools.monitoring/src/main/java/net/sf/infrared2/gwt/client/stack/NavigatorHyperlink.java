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
package net.sf.infrared2.gwt.client.stack;

import net.sf.infrared2.gwt.client.to.NavigatorEntryTO;

import com.google.gwt.user.client.ui.HTML;

/**
 * <b>NavigatorHyperlink</b><p>
 * NavigatorHyperlink is extended from com.google.gwt.user.client.ui.HTML
 * component. It needs to associate NavigatorEntryTO with hyper link in navigator
 * panel.
 * 
 * @author Birukov Sergey
 * @author Sergey Evluhin
 */
public class NavigatorHyperlink extends HTML {

    /** Transfer object associated with this NavigatorHyperlink instance. */
    private NavigatorEntryTO navigatorEntryTO;
    
    private String sourceText;

    /**
     * Default constructor. Creates navigator hyper link with associated transfer
     * object.
     * 
     * @param navigatorEntryTO - transfer object associated with this
     *            NavigatorHyperlink instance.
     */
    public NavigatorHyperlink(NavigatorEntryTO navigatorEntryTO) {
        super(navigatorEntryTO.getTitle());
        setStyleName("navigator-link");
        this.navigatorEntryTO = navigatorEntryTO;
        sourceText = navigatorEntryTO.getTitle();
    }

    /**
     * @return transfer object associated with this NavigatorHyperlink instance.
     */
    public NavigatorEntryTO getNavigatorEntryTO() {
        return navigatorEntryTO;
    }

    /**
     * Gets the source text.
     * 
     * @return the source text
     */
    public String getSourceText() {
        return sourceText;
    }
    
    
}
