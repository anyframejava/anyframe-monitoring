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
 * ApplicationImages.java		Date created: 11.01.2008
 * Last modified by: $Author: soyon.lim $
 * $Revision: 14286 $	$Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.gwt.client.view.images;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ImageBundle;

/**
 * <b>ApplicationImages</b><p>Singleton class for getting application images.
 *
 * @author Andrey Zavgorodniy<br>
 * Copyright(c) 2008, Exadel Inc
 */
public interface ApplicationImages extends ImageBundle {

    /**
     * This instance holds all application images.
     */
    public static final ApplicationImages IMAGES = (ApplicationImages) GWT
            .create(ApplicationImages.class);

    /**
     * Get image perform button with arrow.
     * @gwt.resource net/sf/infrared2/gwt/public/images/button_arrow.gif
     * @return button with arrow .
     */
    public AbstractImagePrototype getButtonArrow();

    /**
     * Get image perform disable button with arrow.
     * @gwt.resource net/sf/infrared2/gwt/public/images/button-arrow-disabled.gif
     * @return disable button with arrow.
     */
    public AbstractImagePrototype getDisabledButtonArrow();

    /**
     * Get image for decorate "Navigation" and "Presentation" title header lable.
     * @gwt.resource net/sf/infrared2/gwt/public/images/title-arrow.gif
     * @return title arrow image.
     */
    public AbstractImagePrototype getTitleArrow();

    /**
     * Get horizontal line delimiter for header panel.
     * @gwt.resource net/sf/infrared2/gwt/public/images/top-gradient.gif
     * @return horizontal line delimiter.
     */
    public AbstractImagePrototype getTopGradientDelimetr();

    /**
     * Get application Logo image. 
     * @gwt.resource net/sf/infrared2/gwt/public/images/samsung-logo.jpg
     * @return image for application logotip.
     */
    public AbstractImagePrototype getApplicationLogo();

    /**
     * Get image perform calendar widget.
     * @gwt.resource net/sf/infrared2/gwt/public/images/calendar.gif
     * @return calendar image
     */
    public AbstractImagePrototype getCalendarImage();

    /**
     * Get application error image for error dialog.
     * @gwt.resource net/sf/infrared2/gwt/public/images/appl_error.png
     * @return application error image
     */
    public AbstractImagePrototype getApplErrorImage();

    /**
     * Get image with up arrow. 
     * @gwt.resource net/sf/infrared2/gwt/public/images/up2-12x12.gif
     * @return up arrow image
     */
    public AbstractImagePrototype getUpImage();

    /**
     * Get image with down arrow.
     * @gwt.resource net/sf/infrared2/gwt/public/images/down2-12x12.gif
     * @return down arrow image
     */
    public AbstractImagePrototype getDownImage();
}
