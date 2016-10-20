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
 * SelectAppTreeHelper.java		Date created: 22.02.2008
 * Last modified by: $Author: soyon.lim $
 * $Revision: 14286 $	$Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.gwt.client.tree;

import net.sf.infrared2.gwt.client.to.ApplicationEntryTO;
import net.sf.infrared2.gwt.client.to.InstanceEntryTO;

import com.gwtext.client.data.Node;
import com.gwtext.client.widgets.tree.TreeNode;

/**
 * <b>SelectAppTreeHelper</b><p>
 * Hold helper methods for Select application dialog tree.
 * 
 * @author Andrey Zavgorodniy Copyright Exadel Inc, 2008
 */
public class SelectAppTreeHelper {

    /** Current instance of select application tree helper class */
    public static SelectAppTreeHelper selApplTreeHelper = new SelectAppTreeHelper();

    /**
     * Get current instance.
     * 
     * @return SelectAppTreeHelper instance
     */
    public static SelectAppTreeHelper getInstance() {
        return selApplTreeHelper;
    }

    /**
     * Compare target and session stored applicationEntryTO array.
     * 
     * @param targetApplEntryTO - ApplicationEntryTO needed for check
     * @param sessionStored - array of session stored ApplicationEntryTO
     * @return true if session stored applicationEntryTO array contain
     *         targetApplEntryTO
     */
    public boolean isApplicationRealChecked(ApplicationEntryTO targetApplEntryTO,
                    ApplicationEntryTO[] sessionStored) {
        boolean result = false;
        for (int i = 0; i < sessionStored.length; i++) {
            ApplicationEntryTO applicationEntryTO = sessionStored[i];
            if (applicationEntryTO != null) {
                if (applicationEntryTO.getApplicationTitle().equals(
                                targetApplEntryTO.getApplicationTitle())) {
                    result = true;
                }
            }
        }
        return result;
    }

    /**
     * Compare target and session stored InstanceEntryTO array.
     * 
     * @param targetApplEntryTO - ApplicationEntryTO needed for check
     * @param targetInstance - InstanceEntryTO needed for check
     * @param sessionStored - array of session stored ApplicationEntryTO
     * @return - true if session stored applicationEntryTO array contain
     *         targetInstanceTO
     */
    public boolean isInstanceRealChecked(ApplicationEntryTO targetApplEntryTO,
                    InstanceEntryTO targetInstance, ApplicationEntryTO[] sessionStored) {
        boolean result = false;
        for (int i = 0; i < sessionStored.length; i++) {
            ApplicationEntryTO applicationEntryTO = sessionStored[i];
            if (applicationEntryTO != null) {
                if (applicationEntryTO.getApplicationTitle().equals(
                                targetApplEntryTO.getApplicationTitle())) {
                    for (int j = 0; j < applicationEntryTO.getInstanceList().length; j++) {
                        InstanceEntryTO instanceEntryTO = applicationEntryTO.getInstanceList()[j];
                        if (instanceEntryTO != null) {
                            if (instanceEntryTO.getName().equals(targetInstance.getName())) {
                                result = true;
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * Renderer for tree nodes - check/un-check according special rules.
     * 
     * @param node - the current clicked tree node
     * @param checked - the check box value
     */
    public void selectApplicationTreeRender(TreeNode node, boolean checked) {
        if (node != null) {
            if (node.getParentNode().getUserObject() == null) {
                /* application */
                Node[] childs = node.getChildNodes();
                for (int i = 0; i < childs.length; i++) {
                    TreeNode child = (TreeNode) childs[i];
                    child.setAttribute("checked", new Boolean(checked));
                    child.getUI().toggleCheck(checked);
                }
            } else {
                /* Child node */
                int unCheckedCount = 0;
                if (checked) {
                    TreeNode parent = (TreeNode) node.getParentNode();
                    if (!parent.getUI().isChecked()) {
                        Node[] childs = parent.getChildNodes();
                        for (int i = 0; i < childs.length; i++) {
                            TreeNode child = (TreeNode) childs[i];
                            if (!child.getUI().isChecked()) {
                                unCheckedCount++;
                            }
                        }
                        if ((unCheckedCount + 1) == childs.length) {
                            parent.setAttribute("checked", new Boolean(true));
                            parent.getUI().toggleCheck(true);
                        }
                    }
                } else {
                    TreeNode parent = (TreeNode) node.getParentNode();
                    Node[] childs = parent.getChildNodes();
                    for (int i = 0; i < childs.length; i++) {
                        TreeNode child = (TreeNode) childs[i];
                        if (!child.getUI().isChecked()) {
                            unCheckedCount++;
                        }
                    }
                    if (unCheckedCount == childs.length) {
                        parent.setChecked(false);
                        parent.setAttribute("checked", new Boolean(false));
                        parent.getUI().toggleCheck(false);
                    }
                    node.setChecked(false);
                    node.setAttribute("checked", new Boolean(false));
                    node.getUI().toggleCheck(false);
                }
            }
        }
    }
}
