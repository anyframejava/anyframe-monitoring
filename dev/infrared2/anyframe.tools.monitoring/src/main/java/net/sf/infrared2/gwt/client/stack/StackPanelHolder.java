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
package net.sf.infrared2.gwt.client.stack;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import net.sf.infrared2.gwt.client.Engine;
import net.sf.infrared2.gwt.client.HistoryManager;
import net.sf.infrared2.gwt.client.i18n.ApplicationMessages;
import net.sf.infrared2.gwt.client.to.ApplicationConfigTO;
import net.sf.infrared2.gwt.client.to.ApplicationEntryTO;
import net.sf.infrared2.gwt.client.to.InstanceEntryTO;
import net.sf.infrared2.gwt.client.to.NavigatorEntryTO;
import net.sf.infrared2.gwt.client.users.session.UserSessionBean;
import net.sf.infrared2.gwt.client.view.facade.ApplicationViewFacade;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.util.Format;
import com.gwtext.client.widgets.BoxComponent;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.ToolTip;
import com.gwtext.client.widgets.event.PanelListenerAdapter;
import com.gwtext.client.widgets.layout.AccordionLayout;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.layout.VerticalLayout;

/**
 * <b>StackPanelHolder</b><p>
 * Accordion with modules and links. It represents navigation panel of the
 * application.
 * 
 * @author Sergey Evluhin
 */
public class StackPanelHolder extends Panel {
    /** Delimiter for the URL's history tokens. */
    private static final String HISTORY_SEPARATOR = ";";

    /** Instance of this class. */
    private static final StackPanelHolder stackPanel = new StackPanelHolder();

    /** Listener for all links. */
    private HLinkClickListener linkClickListener = new HLinkClickListener();
    /** Current selected link. */
    private NavigatorHyperlink selectedLink = null;
    /** Listener for panel (that is representing concrete module). */
    private PanelListenerImpl panelListenerImpl = new PanelListenerImpl();
    /**
     * Structure model of the stack panel (key - moduleType, value - list of
     * associated links).
     */
    private Map model;

    /**
     * @return instance of stack panel component.
     */
    public static StackPanelHolder getInstance() {
        return stackPanel;
    }

    /**
     * Default constructor.
     */
    private StackPanelHolder() {
        super();

        this.setLayout(new FitLayout());
        final AccordionLayout accordionLayout = new AccordionLayout(false);
        accordionLayout.setHideCollapseTool(true);
        this.setLayout(accordionLayout);
        model = new HashMap(3);

    }

    /**
     * Fill the stack with new data. If panels contains links or modules it will
     * be deleted before filling it with new data.
     * 
     * @param appConfigTo application configuration transfer object.
     */
    public void fillStack(ApplicationConfigTO appConfigTo) {
        Engine.getClient().getNavigator(appConfigTo, new StackAsyncCallBack());
    }

    /**
     * Sets default view for this component. Flushes all modules.
     */
    public void setDefault() {
        reset();
        addLinks(null, ApplicationMessages.MESSAGES.absoluteModuleStackLabel());
        addLinks(null, ApplicationMessages.MESSAGES.hierarchicalModuleStackLabel());
        addLinks(null, ApplicationMessages.MESSAGES.lastInvocationStackLabel());
    }

    /**
     * Clear all content of stack and reset all counters and variables.
     */
    public void reset() {
        this.clear();
        this.removeAll(true);
        selectedLink = null;
        model.clear();
        // model = new HashMap(3);
    }

    /**
     * Add new module to the stack panel.
     * 
     * @param links - array of links.
     * @param label - title of the module.
     */
    public void addLinks(final NavigatorEntryTO[] links, String label) {
        Panel vPanel = new Panel();

        vPanel.addListener(new PanelListenerAdapter() {
            public void onBodyResize(Panel panel, String width, String height) {
                super.onBodyResize(panel, width, height);
                panel.getBody().setWidth(StackPanelHolder.getInstance().getWidth() - 2, false);
            }
        });
        vPanel.setTitle(label);
        vPanel.setCollapsible(false);
        vPanel.setAutoScroll(true);
        vPanel.setLayout(new VerticalLayout(0));
        if (links != null && links.length > 0) {
            Arrays.sort(links, new Comparator() {
                public int compare(Object o1, Object o2) {
                    NavigatorEntryTO link1 = (NavigatorEntryTO) o1;
                    NavigatorEntryTO link2 = (NavigatorEntryTO) o2;
                    if ((link1 != null) && (link2 == null)) {
                        return 1;
                    }
                    if ((link1 == null) && (link2 != null)) {
                        return -1;
                    }
                    if ((link1 == null) && (link2 == null)) {
                        return 0;
                    }
                    String app1 = link1.getApplicationKey();
                    String app2 = link2.getApplicationKey();
                    if ((app1 != null) && (app2 == null)) {
                        return 1;
                    }
                    if ((app1 == null) && (app2 != null)) {
                        return -1;
                    }
                    if (((app1 == null) && (app2 == null)) || (app1.equals(app2))) {
                        if (link1.isRoot()) {
                            return -1;
                        }
                        if (link2.isRoot()) {
                            return 1;
                        }
                        String title1 = link1.getTitle();
                        String title2 = link2.getTitle();
                        if ((title1 != null) && (title2 == null)) {
                            return 1;
                        }
                        if ((title1 == null) && (title2 != null)) {
                            return -1;
                        }
                        if ((title1 == null) && (title2 == null)) {
                            return 0;
                        }
                        return title1.toUpperCase().compareTo(title2.toUpperCase());
                    }
                    if (!app1.equals(app2)) {
                        return app1.toUpperCase().compareTo(app2.toUpperCase());
                    }
                    return 0;
                }
            });
            vPanel.setId(links[0].getModuleType());
            NavigatorHyperlink[] hLinks = new NavigatorHyperlink[links.length];
            for (int i = 0; i < links.length; i++) {
                final NavigatorEntryTO navigatorEntryTO = links[i];

                final NavigatorHyperlink hyperlink = new NavigatorHyperlink(navigatorEntryTO);
                hLinks[i] = hyperlink;
                if (navigatorEntryTO.isRoot()) {
                    hyperlink.addStyleName("first-menu-link");
                    // ToolTip toolTip = new ToolTip(hyperlink.getHTML());
                    // toolTip.applyTo(hyperlink.getElement());
                } else {
                    hyperlink.addStyleName("menu-link");
                }

                hyperlink.addClickListener(linkClickListener);

                vPanel.add(hyperlink);
                if (selectedLink == null) {
                    selectLink(hyperlink);
                    addToHistory(hyperlink);
                }
            }
            model.put(vPanel.getId(), hLinks);

            vPanel.addListener(panelListenerImpl);
        }
        this.add(vPanel);
        this.doLayout();
    }

    /**
     * @return currently selected link.
     */
    public NavigatorHyperlink getSelectedLink() {
        return selectedLink;
    }

    /**
     * Just convert instance of NavigatorEntryTO to String object.
     * 
     * @param navigatorEntryTO - object to be converted.
     * @return string with sequence
     *         'moduleType';'linkName(title)'[;'applicationTitle';'instanceName'].<br>
     *         Right part ";'applicationTitle';'instanceName'" will be presents
     *         if "Divided by application" checkbox is selected.
     */
    public static String getHistoryTokenFromLink(NavigatorEntryTO navigatorEntryTO) {
        String token = navigatorEntryTO.getModuleType() + HISTORY_SEPARATOR + navigatorEntryTO.getTitle();

        if (UserSessionBean.getApplConfigBeanInstance().isDividedByApplications()) {
            token += HISTORY_SEPARATOR + navigatorEntryTO.getApplications().getApplicationTitle();
            token += HISTORY_SEPARATOR + navigatorEntryTO.getApplications().getInstanceList()[0].getName();
        }

        return token;
    }

    /**
     * Just convert String object to instance of NavigatorEntryTO. Reverse
     * operation for "getHistoryTokenFromLink" method.
     * 
     * @param token - string to be converted to NavigatorEntryTO object
     * @return NavigatorEntryTo instance.
     */
    public static NavigatorEntryTO getTOFromString(String token) {
        if (token == null || token.trim().length() == 0) {
            throw new RuntimeException("Cannot convert string: " + token + "; to NavigatorEntryTO object.");
        }

        final String[] split = token.split(HISTORY_SEPARATOR);

        NavigatorEntryTO to = new NavigatorEntryTO(split[1], split[0]);

        if (split.length > 2) {
            ApplicationEntryTO appsTo = new ApplicationEntryTO(split[2], new InstanceEntryTO[] {new InstanceEntryTO(split[3]) });
            to.setApplications(appsTo);
        }

        return to;
    }

    /**
     * Find the link in navigator by associated with that link transfer object.
     * 
     * @param to - associated with link transfer object.
     * @return hyperlink object if it was find or null not if not.
     */
    public NavigatorHyperlink findByNavigatorEntryTO(NavigatorEntryTO to) {
        final NavigatorHyperlink[] links = (NavigatorHyperlink[]) model.get(to.getModuleType());
        for (int i = 0; i < links.length; i++) {
            NavigatorHyperlink navigatorHyperlink = links[i];
            if (equalsEntryTOs(navigatorHyperlink.getNavigatorEntryTO(), to)) {
                return navigatorHyperlink;
            }
        }
        return null;
    }

    /**
     * Check on equals two NavigatorEntryTO objects.
     * 
     * @param to1 - first NavigatorEntryTO object.
     * @param to2 - second NavigatorEntryTO object.
     * @return true if equals, false if not.
     */
    private boolean equalsEntryTOs(NavigatorEntryTO to1, NavigatorEntryTO to2) {
        if (!to1.getModuleType().equals(to2.getModuleType()))
            return false;
        if (!to1.getTitle().equals(to2.getTitle()))
            return false;

        if (UserSessionBean.getApplConfigBeanInstance().isDividedByApplications()) {
            if (!to1.getApplications().getApplicationTitle().equals(to2.getApplications().getApplicationTitle()))
                return false;
            if (!to1.getApplications().getInstanceList()[0].getName().equals(to2.getApplications().getInstanceList()[0].getName()))
                return false;
        }
        return true;
    }

    /**
     * Restore the state of application by history token.
     * 
     * @param historyToken - history token.
     */
    public void restoreHistory(String historyToken) {
        final NavigatorEntryTO entryTO = getTOFromString(historyToken);
        final NavigatorHyperlink link = findByNavigatorEntryTO(entryTO);
        if (!selectedLink.getNavigatorEntryTO().getModuleType().equals(entryTO.getModuleType())) {
            this.panelListenerImpl.setSelectThisLink(link);
            ((Panel) this.getComponent(entryTO.getModuleType())).expand(true);
        } else {
            selectLink(link);
        }

    }

    /**
     * Select link. This process accompanied with putting token to history.
     * 
     * @param to - transfer object associated with hyperlink.
     */
    public void selectLink(NavigatorEntryTO to) {
        final NavigatorHyperlink link = findByNavigatorEntryTO(to);
        addToHistory(link);
        selectLink(link);
    }

    /**
     * This method do not put to history token. (Hyperlink by default put token
     * by itself).
     * @param link - the link has to be selected.
     */
    private void selectLink(NavigatorHyperlink link) {
        if (selectedLink == link)
            return;
        if (selectedLink != null) {
            downlightLink(selectedLink);
        }
        highlightLink(link);
        selectedLink = link;
        ApplicationViewFacade.processStack(link.getNavigatorEntryTO());
    }

    /**
     * Visually select the link.
     * 
     * @param link - link, must be highlighted
     */
    private void highlightLink(NavigatorHyperlink link) {
        link.addStyleName("navigator-link-selected");
    }

    /**
     * Visually fade the link.
     * 
     * @param link - link, must be faded
     */
    private void downlightLink(NavigatorHyperlink link) {
        link.removeStyleName("navigator-link-selected");
    }

    /**
     * Listener implementation for the links in modules.
     * 
     * @author Sergey Evluhin
     */
    private class HLinkClickListener implements ClickListener {
        /**
         * {@inheritDoc}
         */
        public void onClick(Widget sender) {
            NavigatorHyperlink link = (NavigatorHyperlink) sender;
            addToHistory(link);
            selectLink(link);
        }
    }

    /**
     * Listener implementation for the panels in stack panel.
     * 
     * @author Sergey Evluhin
     */
    private class PanelListenerImpl extends PanelListenerAdapter {
        // link that must be selected
        private NavigatorHyperlink selectThisLink = null;

        /**
         * Set the link that must be selected
         * 
         * @param select - link that must be selected
         */
        public void setSelectThisLink(NavigatorHyperlink select) {
            this.selectThisLink = select;
        }

        /**
         * {@inheritDoc}
         */
        public boolean doBeforeExpand(Panel panel, boolean animate) {
            if (selectThisLink == null) {
                NavigatorHyperlink[] links = (NavigatorHyperlink[]) model.get(panel.getId());
                addToHistory(links[0]);
                StackPanelHolder.getInstance().selectLink(links[0]);
            } else {
                StackPanelHolder.getInstance().selectLink(selectThisLink);
                selectThisLink = null;
            }
            return true;
        }

        /**
         * {@inheritDoc}
         */
        public boolean doBeforeCollapse(Panel panel, boolean animate) {
            if (selectedLink == null)
                return true;

            return !(selectThisLink == null && selectedLink.getNavigatorEntryTO().getModuleType().equals(panel.getId()));
        }

        public void onBodyResize(Panel panel, String width, String height) {
            // Window.alert(panel.getWidth()+"");
            Object linksObject = model.get(panel.getId());
            if ((linksObject != null) && (linksObject instanceof NavigatorHyperlink[])) {
                NavigatorHyperlink[] links = (NavigatorHyperlink[]) linksObject;
                for (int i = 0; i < links.length; i++) {
                    NavigatorHyperlink link = links[i];
                    link.setText(cuttoffLink(link, stackPanel.getWidth()));
                }
            }
            super.onBodyResize(panel, width, height);
        }

    }

    private String cuttoffLink(NavigatorHyperlink link, int preferedWidth) {
        String result = link.getSourceText();
        double symbolLength = 6.3;
        if (link.getNavigatorEntryTO().isRoot()){
            preferedWidth -=10;
        }else{
            preferedWidth -=40;
        }
        if (preferedWidth < (result.length()+3) * symbolLength) {
            double resultSymbols = preferedWidth / symbolLength - 7;
            if ((resultSymbols > 0) && (resultSymbols < result.length())) {
                result = result.substring(0, (int)resultSymbols) + "...";
            }
            link.setTitle(link.getSourceText());
        } else {
            link.setTitle(null);
        }
        return result;
    }

    private void addToHistory(NavigatorHyperlink link) {
        final String historyTokenFromLink = getHistoryTokenFromLink(link.getNavigatorEntryTO());
        HistoryManager.newItem(historyTokenFromLink);
    }
}
