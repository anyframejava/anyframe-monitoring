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
 * SmartImage.java Date created: 31.01.2008
 * Last modified by: $Author: soyon.lim $
 * $Revision: 14286 $ $Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.server.chart;

import java.awt.*;

/**
 * <b>SmartImage</b><p>
 * SmartImage is special object bean for storing chart image and its associated
 * user session ID and time out.
 *
 * @author Roman Ivanenko
 */
public class SmartImage {

    /** Field image  */
    private Image image;
    /** Field sesionId  */
    private String sesionId;
    /** Field timeOut  */
    private Integer timeOut;
    /** Field html  */
    private String html;

    /**
     * Constructor
     *
     * @param image an image
     * @param sesionId a session id
     * @param timeOut a timaout
     * @param html a html
     */
    public SmartImage(Image image, String sesionId, Integer timeOut, String html) {
        this.image = image;
        this.sesionId = sesionId;
        this.timeOut = timeOut;
        this.html = html;
    }

    /**
     * @return the image
     */
    public Image getImage() {
        return image;
    }

    /**
     * @param image the image to set
     */
    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * @return the timeOut
     */
    public Integer getTimeOut() {
        return timeOut;
    }

    /**
     * @param timeOut the timeOut to set
     */
    public void setTimeOut(Integer timeOut) {
        this.timeOut = timeOut;
    }

    /**
     * @return the sesionId
     */
    public String getSesionId() {
        return sesionId;
    }

    /**
     * @param sesionId the sesionId to set
     */
    public void setSesionId(String sesionId) {
        this.sesionId = sesionId;
    }

    /**
     * Method getHtml returns the html of this SmartImage object.
     *
     * @return the html (type String) of this SmartImage object.
     */
    public String getHtml() {
        return html;
    }

    /**
     * Method setHtml sets the html of this SmartImage object.
     *
     * @param html the html of this SmartImage object.
     *
     */
    public void setHtml(String html) {
        this.html = html;
    }
}
