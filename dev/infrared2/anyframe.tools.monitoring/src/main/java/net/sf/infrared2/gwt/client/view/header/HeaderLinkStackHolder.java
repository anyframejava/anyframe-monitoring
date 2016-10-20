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
 * HeaderLinkStrackHolder.java Date created: 18.01.2008
 * Last modified by: $Author: soyon.lim $ 
 * $Revision: 14286 $ $Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.gwt.client.view.header;

import net.sf.infrared2.gwt.client.i18n.ApplicationMessages;

import com.google.gwt.user.client.ui.HTML;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.layout.HorizontalLayout;

/**
 * <b>HeaderLinkStackHolder</b><p>Singleton class for hold and customize count(add)<br>
 * of links in right side of header panel<br>
 * Now available report and refresh links.
 * 
 * @author Andrey Zavgorodniy<br>
 * Copyright(c) 2008, Exadel Inc
 */
public class HeaderLinkStackHolder extends Panel{
    /**
     * Current instance of <code>HeaderLinkStackHolder</code>class
     */
    private static final HeaderLinkStackHolder headerLinkStackInstance = new HeaderLinkStackHolder();

    /**
     * @return instance of HeaderLinkStackHolder
     */
    public static HeaderLinkStackHolder getInstance() {
        return headerLinkStackInstance;
    }
    
    /**
     * Default constructor.
     */
    public HeaderLinkStackHolder() {
        super();
        this.setLayout(new HorizontalLayout(3));
    }
    
    /**
     * Add HTML object for perform as hyper link widget in to stack header holder
     * @param links - array of HTML[] widgets for perform as a link 
     */
    public void addLinks(final HTML[] links) {
        for (int i = 0; i < links.length; i++) {
            HTML hyperLink = links[i];
            this.add(hyperLink);
            if (i < links.length-1) {
                HTML separator = new HTML(ApplicationMessages.MESSAGES.headerLinksSeparator());
                separator.setStylePrimaryName("menu-separator");
                this.add(separator);
            }
        }
    }
       
    /**
     * Getter for return horizontal panel contain header links.
     * 
     * @return the links stack panel as panel with horizontal layout
     */
    public Panel getLinksStackPanel() {
        return this;
    }

}
