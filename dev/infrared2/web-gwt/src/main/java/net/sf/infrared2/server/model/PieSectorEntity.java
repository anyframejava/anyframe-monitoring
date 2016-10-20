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
 * PieSectorEntity.java Date created: 31.01.2008
 * Last modified by: $Author: soyon.lim $
 * $Revision: 14286 $ $Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.server.model;

import java.awt.*;

/**
 * <b>PieSectorEntity</b><p>
 * Class PieSectorEntity represent model for encapsulating
 * data about sector of pie diagram.
 *
 * @author gzgonikov
 */
public class PieSectorEntity {

    /** Field name  */
    private String name;
    /** Field number  */
    private Number number;
    /** Field color  */
    private Color color;

    /**
     * Constructor PieSectorEntity creates a new PieSectorEntity instance.
     */
    public PieSectorEntity() {
    }

    /**
     * Constructor PieSectorEntity creates a new PieSectorEntity instance.
     *
     * @param name of type String
     * @param number of type Number
     * @param color of type Color
     */
    public PieSectorEntity(String name, Number number, Color color) {
        this.name = name;
        this.number = number;
        this.color = color;
    }

    /**
     * Method getName returns the name of this PieSectorEntity object.
     *
     * @return the name (type String) of this PieSectorEntity object.
     */
    public String getName() {
        return name;
    }

    /**
     * Method setName sets the name of this PieSectorEntity object.
     *
     * @param name the name of this PieSectorEntity object.
     *
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Method getNumber returns the number of this PieSectorEntity object.
     *
     * @return the number (type Number) of this PieSectorEntity object.
     */
    public Number getNumber() {
        return number;
    }

    /**
     * Method setNumber sets the number of this PieSectorEntity object.
     *
     * @param number the number of this PieSectorEntity object.
     *
     */
    public void setNumber(Number number) {
        this.number = number;
    }

    /**
     * Method getColor returns the color of this PieSectorEntity object.
     *
     * @return the color (type Color) of this PieSectorEntity object.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Method setColor sets the color of this PieSectorEntity object.
     *
     * @param color the color of this PieSectorEntity object.
     *
     */
    public void setColor(Color color) {
        this.color = color;
    }
}
