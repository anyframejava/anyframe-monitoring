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
 * TreeFactory.java Date created: 14.01.2008
 * Last modified by: $Sergey Evluhin $ 
 * $Revision: 14286 $ $Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.gwt.client.tree;

import net.sf.infrared2.gwt.client.to.ApplicationEntryTO;
import net.sf.infrared2.gwt.client.to.InstanceEntryTO;
import net.sf.infrared2.gwt.client.to.other.TraceTreeNodeTO;
import net.sf.infrared2.gwt.client.view.dialog.DialogHelper;

import com.gwtext.client.core.Ext;
import com.gwtext.client.data.Node;
import com.gwtext.client.widgets.tree.TreeNode;
import com.gwtext.client.widgets.tree.TreePanel;
import com.gwtext.client.widgets.tree.event.TreePanelListenerAdapter;

/**
 * TreeFactory acts as a factory for creating objects of Tree model.
 * 
 * @author Sergey Evluhin
 * @author Andrey Zavgorodniy
 */
public class TreeFactory {
    /** Constant present maximum width of bar component in pixel */
    private static final int MAX_BAR_WIDTH = 200;
    /** Current instance of helper class for working with select application tree */
    private static SelectAppTreeHelper treeHelper = SelectAppTreeHelper.getInstance();

    /**
     * Creates trace tree (UI component) from tree of transfer objects.
     * 
     * @param tree - tree of transfer objects.
     * @return trace tree component.
     */
    public static TreePanel createTree(TraceTreeNodeTO tree) {
        TreePanel treePanel = new TreePanel(Ext.generateId());
        treePanel.setBorder(false);
        treePanel.setBodyBorder(false);
        treePanel.setRootVisible(false);
        if (tree == null) {
            throw new RuntimeException("TreeFactory: createTree: tree is null!");
        }
        TreeNode root = getRoot(tree);
        treePanel.setRootNode(root);
        return treePanel;
    }

    public static TreeNode getRoot(TraceTreeNodeTO tree) {
        TreeNode root = new TreeNode();
        root.setUserObject(new TraceTreeNodeTO());
        for (int i = 0; i < tree.getChildren().length; i++) {
            final TraceTreeNodeTO traceTreeNodeTO = tree.getChildren()[i];
            final int totalTime = traceTreeNodeTO.getTotalTime();
            final float K = (float) MAX_BAR_WIDTH / totalTime;
            root.appendChild(getTree(traceTreeNodeTO, -1, K));
        }
        return root;
    }

    /**
     * Returns tree node. There is the recursive call of this method to build
     * node with children.
     * 
     * @param node - current transfer object (current node in tree of transfer
     *            object).
     * @param parentTotalTime - total time parameter of the parent's node.
     * @param k - coefficient, indicates relation between width of colored bar
     *            and total time. I.e. how pixels quantity is in 1 point of
     *            total time value.
     * @return tree node of the trace tree.
     */
    private static TreeNode getTree(TraceTreeNodeTO node, int parentTotalTime, final float k) {

        TreeNode treeNode = new TreeNode(node.toString()
                        + getPercentBarHTML(node.getTotalTime(), parentTotalTime, k));
        treeNode.setUserObject(node);
        TraceTreeNodeTO[] nodes = node.getChildren();
        if (nodes != null) {
            for (int i = 0; i < nodes.length; i++) {
                TraceTreeNodeTO tree = nodes[i];
                if (tree != null) {
                    treeNode.appendChild(getTree(tree, node.getTotalTime(), k));
                }
            }
        }
        return treeNode;
    }

    /**
     * Returns HTML code of colored bar that indicates percent relations between
     * parent's node total time value and current node total time value.
     * 
     * @param totalTime - total time value of current node.
     * @param parentTotalTime - total time value of parent node.
     * @param k - coefficient, indicates relation between width of colored bar
     *            and total time. I.e. how pixels quantity is in 1 point of
     *            total time value.
     * @return HTML code of colored bar.
     */
    private static String getPercentBarHTML(int totalTime, int parentTotalTime, final float k) {
        if (parentTotalTime == 0) {
            return "";
        } else if (parentTotalTime < 0) {
            parentTotalTime = totalTime;
        }

        int allwidth = Math.round(k * parentTotalTime);
        int currentWidth = Math.round(k * totalTime);

        String html = "";// "<span class=\"time-percentage\">";
        if (currentWidth > 0) {
            html += "<span class=\"child-time\" style=\"width: " + currentWidth + "px;\"></span>";
        }
        if (allwidth - currentWidth > 0) {
            html += "<span class=\"parent-time\" style=\"width: " + (allwidth - currentWidth)
                            + "px;\"></span>";
        }
        return html;
    }

    /**
     * Create check box tree panel presents application as node and instances as
     * a child.
     * 
     * @param recevedApplData - the array of all received applications from
     *            server side.
     * @param storedApplData - the array of stored in session user bean
*            applications needed for rendered as "checked"
     * @param checkUncheckRendered - flag show rendered as "checked" or not. @return - the select application tree panel presents application as node
     */
    public static TreePanel createSelectApplTree(ApplicationEntryTO[] recevedApplData,
                                                 ApplicationEntryTO[] storedApplData, boolean checkUncheckRendered) {
        if (recevedApplData == null) {
            return null;
        }
        TreePanel treePanel;
        treePanel = new TreePanel(Ext.generateId());
        treePanel.setBorder(false);
        treePanel.setSize("100%", "100%");
        treePanel.setRootVisible(false);
        treePanel.setAutoScroll(true);


        TreeNode root = getApplicationsTree(recevedApplData, storedApplData, checkUncheckRendered);
        treePanel.setRootNode(root);

        initListener(treePanel);

        treePanel.doLayout();

        return treePanel;
    }

    private static void initListener(final TreePanel treePanel) {
        treePanel.addListener(new TreePanelListenerAdapter() {

            public void onCheckChange(TreeNode node, boolean checked) {
                boolean isChildNode = node.getChildNodes() == null
                                || node.getChildNodes().length == 0;

                Boolean value = new Boolean(node.getUI().isChecked());
                node.setAttribute("checked", value);
                node.setChecked(value.booleanValue());
                if (!isChildNode) {
                    checkParent(node);
                } else {
                    TreeNode parentNode = (TreeNode) node.getParentNode();

                    Node[] childNodes = parentNode.getChildNodes();
                    boolean res = false;
                    for (int i = 0; i < childNodes.length; i++) {
                        TreeNode node2 = (TreeNode) childNodes[i];
                        if (node2.getUI().isChecked()) {
                            res = true;
                            break;
                        }
                    }
                    parentNode.getUI().toggleCheck(res);
                    parentNode.setAttribute("checked", new Boolean(res));
                    parentNode.setChecked(res);

                }
            }

            public void onExpandNode(TreeNode node) {
                validateParent(node);
            }

            /**
             * {@inheritDoc}
             */
            public boolean doBeforeCollapseNode(TreeNode node, boolean deep, boolean anim) {
                validateParent(node);
                return true;
            }

            /**
             * {@inheritDoc}
             */
            public boolean doBeforeExpandNode(TreeNode node, boolean deep, boolean anim) {
                validateParent(node);
                return true;
            }

            /**
             * {@inheritDoc}
             */
            public void onCollapseNode(TreeNode node) {
                validateParent(node);
            }

            private  void validateParent(TreeNode node) {
                if (node.getParentNode() == null)
                    return;
                
//                System.out.println("Validate.");
                validateNode(node);
                
                if(node.getChildNodes()==null)
                    return;
                
                Node[] childNodes = node.getChildNodes();
//                System.out.println("-" + node.getText() + "[UI:" + node.getUI().isChecked()
//                                + ";attr: " + node.getAttribute("checked") + "]");
                for (int i = 0; i < childNodes.length; i++) {
                    TreeNode node2 = (TreeNode) childNodes[i];
                    validateNode(node2);
                }
//                System.out.println("------------------------------------------------------");

            }

          

            private  void checkParent(TreeNode node) {
                boolean checked = node.getUI().isChecked();

                Node[] childNodes = node.getChildNodes();

                for (int i = 0; i < childNodes.length; i++) {
                    TreeNode node2 = (TreeNode) childNodes[i];
                    Boolean value = new Boolean(checked);
                    node2.setAttribute("checked", value);
                    node2.setChecked(value.booleanValue());
                    node2.getUI().toggleCheck(checked);
                }
            }
            
            private   void validateNode(TreeNode node2) {
                if (node2.getAttribute("checked") != null) {
//                    System.out.println("         -" + node2.getText() + "[UI:"
//                                    + node2.getUI().isChecked() + ";attr: "
//                                    + node2.getAttribute("checked") + "]");
                    boolean checked = Boolean.valueOf(node2.getAttribute("checked"))
                    .booleanValue();
                    node2.getUI().toggleCheck(checked);
                    node2.setChecked(checked);
                } else {
//                    System.out.println("         -" + node2.getText() + "[UI:"
//                                    + node2.getUI().isChecked() + ";attr: "
//                                    + node2.getAttribute("checked") + "]");
                    boolean checked = false;
                    node2.getUI().toggleCheck(checked);
                    node2.setChecked(checked);
                }
            }

        });
    }

   
    
    /**
     * Get selected application tree..
     * 
     *@param recevedApplications - the array of all received applications from
     *            server side.
     * @param sessionStoredApplications - the array of stored in session user
 *            bean applications needed for rendered as "checked"
     * @param needForRender - flag show rendered as "checked" or not. @return the root select application TreeNode with all children.
     */
    private static TreeNode getApplicationsTree(ApplicationEntryTO[] recevedApplications,
                                                ApplicationEntryTO[] sessionStoredApplications, boolean needForRender) {
        TreeNode rootTreeNode = new TreeNode("root");
        for (int i = 0; i < recevedApplications.length; i++) {
            ApplicationEntryTO applicationEntryTO = recevedApplications[i];
            TreeNode applicationTreeNode = new TreeNode(applicationEntryTO.getApplicationTitle());
            rootTreeNode.appendChild(applicationTreeNode);

            if (needForRender) {
                applicationTreeNode.setChecked(treeHelper.isApplicationRealChecked(
                                applicationEntryTO, sessionStoredApplications));
            } else {
                applicationTreeNode.setChecked(false);
            }

            applicationTreeNode.setUserObject(applicationEntryTO);
            InstanceEntryTO[] instanceNodes = applicationEntryTO.getInstanceList();
            for (int j = 0; j < instanceNodes.length; j++) {
                InstanceEntryTO tree = instanceNodes[j];
                if (tree != null) {
                    TreeNode childNode = new TreeNode(tree.getName());
                    if (needForRender) {
                        childNode.setChecked(treeHelper.isInstanceRealChecked(
                                        applicationEntryTO, tree, sessionStoredApplications));
                    } else {
                        childNode.setChecked(false);
                    }
                    childNode.setUserObject(tree);
                    applicationTreeNode.appendChild(childNode);
                }
            }

        }
        return rootTreeNode;
    }

}
