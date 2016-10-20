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
 * VersionProvider.java		Date created: Apr 1, 2008
 * Last modified by: $Author: soyon.lim $
 * $Revision: 14286 $	$Date: 2009-06-05 21:48:40 +0900 (금, 05 6월 2009) $
 */

package net.sf.infrared2.gwt.server.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.text.SimpleDateFormat;
import java.net.URL;
import java.security.CodeSource;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <b>VersionProvider</b><p>
 * VersionProvider Class read version information from manifest file and
 * provide the version as string.
 * @author Roman Ivanenko
 */
public class VersionProvider {

    /**
     * The logger.
     */
    private static Log logger = LogFactory.getLog(VersionProvider.class);
    
    /**
     * Keeps version information.
     */
    private static String versionString = getVersion();

    /**
     * Returns version string.
     * 
     * @param config the ServletConfig to set
     * @return the version string
     */    
    public static String getVersion(ServletConfig config) {
    	ServletContext servletContext = config.getServletContext();
    	return getVersion(servletContext);
    }

    /**
     * Returns version string.
     * 
     * @param servletContext the ServletContext to set
     * @return the version string
     */   
    public static String getVersion(ServletContext servletContext) {
    	if (versionString != null) {
    		return versionString;
    	}
        String appServerHome = getAppServerHome(servletContext);
        versionString = getVersion(appServerHome);
        return versionString;
    }

    /**
     * Returns version string.
     * 
     * @param appServerHome the root directory of application to set
     * @return the version string
     */   
    public static String getVersion(String appServerHome) {
    	if (versionString != null) {
    		return versionString;
    	}
        File manifestFile = getManifestFile(appServerHome);
        versionString = getVersionFromManifestFile(manifestFile);
    	return versionString;
    }

    /**
     * Returns version string.
     * 
     * @return the version string
     */ 
    public static String getVersion() {
    	if (versionString != null) {
    		return versionString;
    	}
        String appServerHome = getAppServerHome();
        versionString = getVersion(appServerHome);
        return versionString;
    }

    /**
     * Returns version string.
     * 
     * @return the appServerHome string (root directory of application)
     */ 
    private static String getAppServerHome() {
    	String appServerHome = null;
    	//try {
    	Class<VersionProvider> vp = VersionProvider.class;
    	String className = vp.getName();
    	CodeSource source = vp.getProtectionDomain().getCodeSource();
    	URL location = source.getLocation();
    	appServerHome = location.getPath().replaceFirst("/WEB-INF/classes/" + className + ".class", "");
//    	} catch (Exception e) {
//    		logger.error("\n\t ERROR: Unable to locate VersionProvider.class directory");
//    	}
		return appServerHome;
	}

	public static String getAppServerHome(ServletContext servletContext) {
    	return servletContext.getRealPath("/");
    }

    /**
     * Returns version string.
     * 
     * @param appServerHome the root directory of application to set
     * @return the manifestFile 
     */   	
    public static File getManifestFile(String appServerHome) {
    	File manifestFile = null;
		try {
			manifestFile = new File(appServerHome, "META-INF/MANIFEST.MF");
		} catch (RuntimeException e) {
			logger.error("\n\t ERROR: Unable to get Manifest File");
		}
    	return manifestFile;
    }

    /**
     * Returns version string.
     * 
     * @param manifestFile the manifest file
     * @return the version string
     */ 
    public static String getVersionFromManifestFile(File manifestFile) {
    	String version = "";
        Manifest mf = new Manifest();
        try {
            mf.read(new FileInputStream(manifestFile));
        } catch (FileNotFoundException e) {
            logger.error("\n\t ERROR: File Not Found: " + manifestFile);
        } catch (IOException e) {
            logger.error("\n\t ERROR: General I/O error.");
        }
        Attributes atts = mf.getMainAttributes();
        
        try {
            String implementationVersion = atts.getValue("Implementation-Version");
            String implementationBuild = atts.getValue("Implementation-Build");
            String iterationNumber = atts.getValue("Iteration-Number");
            String timestampBuildStr = atts.getValue("Timestamp-Build");

            if (timestampBuildStr != null) {
            	Date timestampBuild = new Date(Long.parseLong(timestampBuildStr));
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss z");
                timestampBuildStr = formatter.format(timestampBuild);
            }

            version = "v." + implementationVersion + "." + iterationNumber + "." + implementationBuild + " at " + timestampBuildStr;
            logger.info("\n\t Version: " + version);
        } catch (NumberFormatException e) {
            logger.warn("\n\t WARNING: Version information in Manifest file is not valid. See also Maven POM script for folloving tags: <Implementation-Version>, <Implementation-Build>, <Iteration-Number>, <Timestamp-Build>.");
        }

    	return version;
    }
}
