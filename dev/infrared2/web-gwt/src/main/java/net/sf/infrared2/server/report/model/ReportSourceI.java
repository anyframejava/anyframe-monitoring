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
package net.sf.infrared2.server.report.model;


import java.io.Serializable;
import java.util.List;
import java.util.Map;

import net.sf.infrared2.gwt.client.to.ApplicationConfigTO;
import net.sf.infrared2.gwt.client.to.NavigatorEntryTO;
import net.sf.infrared2.gwt.client.to.NavigatorTO;

/**
 * <b>ReportSourceI</b><p>
 * Interface ReportSourceI is the base of data source for report generator.
 *
 * @author gzgonikov
 */
public interface ReportSourceI {

    /**
     * Method getTO retun TO based on navigator link
     *
     * @param navTO of type NavigatorEntryTO
     * @return Serializable
     */
    public Serializable getTO(NavigatorEntryTO navTO);

    /**
     * Method getData returns the data of this ReportSourceContentTO object.
     *
     * @return the data (type Map<NavigatorEntryTO, Serializable>) of this ReportSourceContentTO object.
     */
    public Map<NavigatorEntryTO, Serializable> getData();

    /**
     * Method setData sets the data of this ReportSourceContentTO object.
     *
     * @param data the data of this ReportSourceContentTO object.
     *
     */
    public void setData(Map<NavigatorEntryTO, Serializable> data);

    /**
     * Method getNavigator returns the navigator of this ReportSourceContentTO object.
     *
     * @return the navigator (type NavigatorTO) of this ReportSourceContentTO object.
     */
    public NavigatorTO getNavigator();

    /**
     * Method setNavigator sets the navigator of this ReportSourceContentTO object.
     *
     * @param navigator the navigator of this ReportSourceContentTO object.
     *
     */
    public void setNavigator(NavigatorTO navigator);

    /**
     * Method getApplConfig returns the applConfig of this ReportSourceContentTO object.
     *
     * @return the applConfig (type ApplicationConfigTO) of this ReportSourceContentTO object.
     */
    public ApplicationConfigTO getApplConfig();

    /**
     * Method setApplConfig sets the applConfig of this ReportSourceContentTO object.
     *
     * @param applConfig the applConfig of this ReportSourceContentTO object.
     *
     */
    public void setApplConfig(ApplicationConfigTO applConfig);

    /**
     * Method getNavigatorEntryTOArray returns List<NavigatorEntryTO>
     * based on current module type.
     *
     * @param moduleType of type String
     * @return List<NavigatorEntryTO>
     */
    public List<NavigatorEntryTO> getNavigatorEntryTOArray(String moduleType);

}
