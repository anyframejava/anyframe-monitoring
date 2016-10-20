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
 * BeanComparator.java Date created: 22.01.2008
 * Last modified by: $Author: soyon.lim $
 * $Revision: 14286 $ $Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.server.util;

import org.apache.commons.beanutils.MethodUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;

/**
 * <b>BeanComparator</b><p>
 * A simple comparator implementation that enables comparing two
 * bean instances based on a specified property.
 */
public class BeanComparator implements Comparator {

    /** Field methodName  */
    String methodName = null;
    /** Field ascending  */
    boolean ascending = true;

    /**
     * Constructor BeanComparator creates a new BeanComparator instance.
     *
     * @param methodName of type String
     * @param ascending of type boolean
     */
    public BeanComparator(String methodName, boolean ascending) {
        this.methodName = methodName;
        this.ascending = ascending;
    }

    /**
     * Method compare - acts as any standard compare method.
     *
     * @param o1 of type Object
     * @param o2 of type Object
     * @return int
     */
    public int compare(Object o1, Object o2) {
        Object fieldOne;
        Object fieldTwo;
        try {
            fieldOne = MethodUtils.invokeExactMethod(o1, methodName, null);
            fieldTwo = MethodUtils.invokeExactMethod(o2, methodName, null);
        }
        catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
        catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        if (fieldOne != null
                && fieldTwo != null
                && fieldOne.getClass().equals(fieldTwo.getClass())
                && fieldOne instanceof Comparable
                && fieldTwo instanceof Comparable) {
            int retValue = ((Comparable) fieldOne).compareTo(fieldTwo);
            return (ascending ? retValue : (retValue * -1));
        } else {
            throw new IllegalArgumentException("Error comparing object "
                    + o1.toString()
                    + " with "
                    + o2.toString());
        }
    }

    /**
     * Method setAscending sets the ascending of this BeanComparator object.
     *
     * @param ascending the ascending of this BeanComparator object.
     *
     */
    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }

}
