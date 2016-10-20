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
 * ApplicationTreeTransformer.java Date created: 29.01.2008
 * Last modified by: $Author: soyon.lim $ 
 * $Revision: 14286 $ $Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.gwt.client.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.infrared2.gwt.client.to.ApplicationEntryTO;
import net.sf.infrared2.gwt.client.to.InstanceEntryTO;

import com.gwtext.client.data.Node;
import com.gwtext.client.widgets.tree.TreeNode;

/**
 * <b>ApplicationTreeTransformer</b>
 * <p>
 * The singleton class, transforming presentation application tree<br>
 * in to ApplicationEntry and InstanceEntry TOs
 * 
 * @author Andrey Zavgorodniy
 */
public class ApplicationTreeTransformer {

    /** Current instance of application tree transformer. */
    private static final ApplicationTreeTransformer transformer = new ApplicationTreeTransformer();

    /**
     * Get current instance of application tree transformer
     * 
     * @return - the ApplicationTreeTransformer instance
     */
    public static ApplicationTreeTransformer getInstance() {
        return transformer;
    }

    /**
     * Generator instances and applications from tree view.
     * 
     * @param treeNodes - tree presentation - array of TreeNode
     * @return - arrays of application entry TO
     */
    public ApplicationEntryTO[] transform(TreeNode[] treeNodes) {
        List checkedApplList = new ArrayList();
        List chekedInstanceList = new ArrayList();
        Map map = new HashMap();
        if (treeNodes != null) {
            for (int i = 0; i < treeNodes.length; i++) {
                TreeNode treeNode = treeNodes[i];
                if(!treeNode.getUI().isChecked()){
                    continue;
                }
                Object obj = treeNode.getUserObject();
                if (obj instanceof ApplicationEntryTO) {
                    ApplicationEntryTO applTO = (ApplicationEntryTO) obj;
                    checkedApplList.add(applTO);
                    map.put(applTO.getApplicationTitle(), new ArrayList());
                } else if (obj instanceof InstanceEntryTO) {
                    InstanceEntryTO instanceTO = (InstanceEntryTO) obj;
                    Node parent = treeNode.getParentNode();
                    if (parent != null) {
                        ApplicationEntryTO appl = (ApplicationEntryTO) parent.getUserObject();
                        if (appl != null) {
                            if (map.containsKey(appl.getApplicationTitle())) {
                                List value = (List) map.get(appl.getApplicationTitle());
                                value.add(instanceTO);
                            }
                        }
                    }
                }
            }
        }
        for (Iterator it = map.entrySet().iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            String applName = (String) entry.getKey();
            List instancesLst = (ArrayList) entry.getValue();
            InstanceEntryTO[] entries = (InstanceEntryTO[]) instancesLst
                            .toArray(new InstanceEntryTO[instancesLst.size()]);
            for (Iterator iterator = checkedApplList.iterator(); iterator.hasNext();) {
                ApplicationEntryTO application = (ApplicationEntryTO) iterator.next();
                if (application.getApplicationTitle().equals(applName)) {
                    application.setInstanceList(entries);
                }
            }
        }

        return (ApplicationEntryTO[]) checkedApplList
                        .toArray(new ApplicationEntryTO[checkedApplList.size()]);
    }
}
