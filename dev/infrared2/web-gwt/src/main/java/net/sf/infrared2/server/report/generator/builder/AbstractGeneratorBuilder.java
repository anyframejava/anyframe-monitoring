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
 * AbstractGeneratorBuilder.java Date created: 31.01.2008
 * Last modified by: $Author: soyon.lim $
 * $Revision: 14286 $ $Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */


package net.sf.infrared2.server.report.generator.builder;


import javax.imageio.ImageIO;

import net.sf.infrared2.gwt.client.report.ReportConfigTO;
import net.sf.infrared2.gwt.client.to.NavigatorEntryTO;
import net.sf.infrared2.gwt.client.to.application.ApplicationViewTO;
import net.sf.infrared2.gwt.client.to.other.OtherViewTO;
import net.sf.infrared2.gwt.client.to.sql.SqlViewTO;
import net.sf.infrared2.server.Constant;
import net.sf.infrared2.server.report.model.ReportSourceContentTO;
import net.sf.infrared2.server.util.GraphicUtil;
import net.sf.infrared2.server.util.ViewUtil;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * <b>AbstractGeneratorBuilder</b><p>
 * Class AbstractGeneratorBuilder ...
 *
 * @author gzgonikov
 */
public abstract class AbstractGeneratorBuilder {

    /** Field reportConfigTO - Config of report wizard  */
    private ReportConfigTO reportConfigTO;

    /**
     * Constructor AbstractGeneratorBuilder creates a new AbstractGeneratorBuilder instance.
     *
     * @param reportConfigTO of type ReportConfigTO
     */
    protected AbstractGeneratorBuilder(ReportConfigTO reportConfigTO) {
        this.reportConfigTO = reportConfigTO;
    }

    /**
     *@return  ReportConfigTO
     */
    public ReportConfigTO getReportConfigTO() {
        return reportConfigTO;
    }

    /**
     * Create specific module with layers.
     *
     * @param reportSourseContentTO - resources object
     * @param moduleType - type of the module
     * @throws java.lang.Exception when some problem occured.
     */
    private void buildModule(ReportSourceContentTO reportSourseContentTO, String moduleType)
                    throws Exception{
        createModuleTitle(ViewUtil.getQualifiedModuleName(moduleType));
        final List<NavigatorEntryTO> navigatorEntryTOArray = reportSourseContentTO
                        .getNavigatorEntryTOArray(moduleType);

        String appTitle = "";

        boolean needCheck = reportSourseContentTO.getApplConfig().isDividedByApplications();

        for (NavigatorEntryTO navigatorEntryTO : navigatorEntryTOArray) {
            if (needCheck
                            && !appTitle
                                            .equals(navigatorEntryTO.getApplications()
                                                            .getApplicationTitle()
                                                            + navigatorEntryTO.getApplications()
                                                                            .getInstanceList()[0]
                                                                            .getName())) {
                appTitle = navigatorEntryTO.getApplications().getApplicationTitle()
                                + navigatorEntryTO.getApplications().getInstanceList()[0].getName();
                createApplicationInstanceHeader(navigatorEntryTO);
            } else if (appTitle.equals("")) {
                // cycle go into this place at one time
                appTitle = "_";
                createApplicationInstanceHeader(navigatorEntryTO);
            }

            final Serializable to = reportSourseContentTO.getTO(navigatorEntryTO);

            if (to.getClass().getName().equals(ApplicationViewTO.class.getName())) {
                if (!this.reportConfigTO.isApplications())
                    continue;
                createApplicationLayer((ApplicationViewTO) to);

            } else if (to.getClass().getName().equals(SqlViewTO.class.getName())) {
                if (!this.reportConfigTO.isLayer())
                    continue;

                createSqlLayer((SqlViewTO) to);
            } else if (to.getClass().getName().equals(OtherViewTO.class.getName())) {

                if (!this.reportConfigTO.isLayer())
                    continue;

                if (this.reportConfigTO.isInclusiveExclusiveMode()) {

                    createOtherLayer((OtherViewTO) to, true);
                    createOtherLayer((OtherViewTO) to, false);

                } else if (this.reportConfigTO.isInclusiveMode()) {
                    createOtherLayer((OtherViewTO) to, true);
                } else if (this.reportConfigTO.isExclusiveMode()) {
                    createOtherLayer((OtherViewTO) to, false);
                }
            }

        }
    }

    /**
     * Method createOtherLayer generate data for other layer.
     *
     * @param otherViewTO of type OtherViewTO
     * @param b indicates what data need to be shown - inclusive or exclusive.
     * @throws Exception when
     */
    protected abstract void createOtherLayer(OtherViewTO otherViewTO, boolean b) throws Exception;

    /**
     * Method createSqlLayer generate data for sql layer.
     *
     * @param sqlViewTO of type SqlViewTO
     * @throws Exception when some problems occured.
     */
    protected abstract void createSqlLayer(SqlViewTO sqlViewTO) throws Exception;

    /**
     * Method createApplicationLayer generate data for application layer.
     *
     * @param applicationViewTO of type ApplicationViewTO
     * @throws Exception when some problem occured.
     */
    protected abstract void createApplicationLayer(ApplicationViewTO applicationViewTO) throws Exception;

    /**
     * Method createApplicationInstanceHeader creates header for table in application layer
     *
     * @param navigatorEntryTO of type NavigatorEntryTO
     * @throws Exception when some problem occured.
     */
    protected abstract void createApplicationInstanceHeader(NavigatorEntryTO navigatorEntryTO) throws Exception;

    /**
     * Method createModuleTitle crates title for module.
     *
     * @param qualifiedModuleName of type String
     * @throws Exception when some problem occured.
     */
    protected abstract void createModuleTitle(String qualifiedModuleName) throws Exception;

    /**
     * Method build top level report generation method.
     * Starts process of generation.
     *
     * @param reportSourseContentTO of type ReportSourceContentTO
     * @throws Exception when
     */
    public void build(ReportSourceContentTO reportSourseContentTO) throws Exception{

        if (this.reportConfigTO.isAbsoluteModuleMode()) {
            buildModule(reportSourseContentTO, Constant.MODULE_ABSOLUTE);
        }

        if (this.reportConfigTO.isHierarchicalModuleMode()) {
            buildModule(reportSourseContentTO, Constant.MODULE_HIERARCHICAL);
        }
    }

    /**
     * Method getImageByUrl returns bytes of image.
     *
     * @param url of type String - url of image to GraphicServlet
     * @return byte[]  - result image
     * @throws IOException when wrong format on image etc.
     */
    protected byte[] getImageByUrl(String url) throws IOException {
        if (GraphicUtil.getSmartImageMap()==null) return null;
        String imageKey = url.substring(url.lastIndexOf("/")+1, url.indexOf(GraphicUtil.imageExtentionPng));
        BufferedImage buffImg = (BufferedImage) GraphicUtil.getSmartImageMap().getImage(imageKey);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(buffImg, GraphicUtil.imageExtentionPng.substring(1), out);
        out.flush();
        return out.toByteArray();
    }

    
}
