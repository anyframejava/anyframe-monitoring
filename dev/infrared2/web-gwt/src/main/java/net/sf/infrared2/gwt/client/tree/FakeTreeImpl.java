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
package net.sf.infrared2.gwt.client.tree;

import net.sf.infrared2.gwt.client.to.other.GeneralInformationRowTO;
import net.sf.infrared2.gwt.client.to.other.TraceTreeNodeTO;

/**
 * <b>FakeTreeImpl</b><p>
 * Contains fake data for tree generation.
 * 
 * @deprecated model not contains many important information for generation
 *             valid trees.
 * @author Sergey Evluhin
 */
public class FakeTreeImpl {
    public static TraceTreeNodeTO getTraceTree() {

        TraceTreeNodeTO t = new TraceTreeNodeTO();

        TraceTreeNodeTO t1 = new TraceTreeNodeTO();
        t1.setText("/exadel/");
        t1.setCount(1);
        t1.setExclusiveTime(132);
        t1.setLayerName("LayerOne");
        t1.setPercent("26.23%");
        t1.setTotalTime(1200);
        GeneralInformationRowTO to1 = new GeneralInformationRowTO("q1", "asdf", 1, 2, 3, 4, 5, 6,
                        7, 8, 9, 0, 1, 2, 3);
        t1.setClassName("classname");
        t1.setGeneralInformationRow(to1);

        // t1.setChildren(children);
        TraceTreeNodeTO tt = new TraceTreeNodeTO();
        tt.setText("/exadel/editCompany/wow.jsf");
        tt.setCount(21);
        tt.setExclusiveTime(323);
        tt.setLayerName("LayerX");
        tt.setPercent("23.23%");
        tt.setTotalTime(200);
        GeneralInformationRowTO toTT = new GeneralInformationRowTO("q423", "1v34zxc2", 211, 221,
                        231, 241, 521, 621, 721, 821, 921, 121, 121, 221, 231);
        tt.setClassName("cldasfassname111");
        tt.setGeneralInformationRow(toTT);

        t1.setChildren(new TraceTreeNodeTO[] { tt });

        TraceTreeNodeTO t11 = new TraceTreeNodeTO();
        t11.setText("/exadel/editCompany.jsf");
        t11.setCount(2);
        t11.setExclusiveTime(32);
        t11.setLayerName("LayerTwo");
        t11.setPercent("2.23%");
        t11.setTotalTime(600);
        GeneralInformationRowTO to11 = new GeneralInformationRowTO("q2", "asdasdff", 11, 21, 31,
                        41, 51, 61, 71, 81, 91, 11, 11, 21, 31);
        t11.setClassName("classname1");
        t11.setGeneralInformationRow(to11);

        TraceTreeNodeTO t111 = new TraceTreeNodeTO();
        t111.setText("/exadel/editCompany/edit.jsf");
        t111.setCount(21);
        t111.setExclusiveTime(322);
        t111.setLayerName("LayerTwo");
        t111.setPercent("23.23%");
        t111.setTotalTime(300);
        GeneralInformationRowTO to111 = new GeneralInformationRowTO("q3", "1vzxc2", 211, 221, 231,
                        241, 521, 621, 721, 821, 921, 121, 121, 221, 231);
        t111.setClassName("classname111");
        t111.setGeneralInformationRow(to111);

        t11.setChildren(new TraceTreeNodeTO[] { t111 });

        TraceTreeNodeTO t12 = new TraceTreeNodeTO();
        t12.setText("/exadel/companyList.jsf");
        t12.setCount(1);
        t12.setExclusiveTime(11);
        t12.setLayerName("LayerTwo");
        t12.setPercent("6.23");
        t12.setTotalTime(13);
        GeneralInformationRowTO to12 = new GeneralInformationRowTO("q4", "ads1vzaxc2", 2114, 2241,
                        2341, 2441, 5241, 6241, 7421, 8421, 9241, 1421, 1421, 2241, 2431);
        t12.setClassName("classname12");
        t12.setGeneralInformationRow(to12);
        // t12.setChildren(children);

        TraceTreeNodeTO t13 = new TraceTreeNodeTO();
        t13.setText("/exadel/result.jsf");
        t13.setCount(5);
        t13.setExclusiveTime(332);
        t13.setLayerName("LayerTwo");
        t13.setPercent("13.23");
        t13.setTotalTime(431);
        // t3.setChildren(children);
        GeneralInformationRowTO to13 = new GeneralInformationRowTO("q6", "Gh1vzaxc2", 2114, 2241,
                        2341, 2441, 5241, 6241, 7421, 8421, 9241, 1421, 1421, 2241, 2431);
        t13.setClassName("classname13");
        t13.setGeneralInformationRow(to13);

        TraceTreeNodeTO t14 = new TraceTreeNodeTO();
        t14.setText("update company set name=?");
        t14.setCount(2);
        t14.setExclusiveTime(302);
        t14.setLayerName("SQL");
        t14.setPercent("36.23");
        t14.setTotalTime(943);
        // t1.setChildren(children);
        GeneralInformationRowTO to14 = new GeneralInformationRowTO("q6", "Hvzaxc2", 2114, 2241,
                        2341, 2441, 5241, 6241, 7421, 8421, 9241, 1421, 1421, 2241, 2431);
        t14.setClassName("classname14");
        t14.setGeneralInformationRow(to14);

        t.setChildren(new TraceTreeNodeTO[] { t1, t11, t12, t13, t14 });
        return t;
    }

}
