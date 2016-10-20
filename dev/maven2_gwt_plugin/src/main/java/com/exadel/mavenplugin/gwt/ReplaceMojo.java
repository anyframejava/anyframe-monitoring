package com.exadel.mavenplugin.gwt;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * @goal replace
 * @phase compile
 * @requiresDependencyResolution compile
 * @author Sergey Evluhin
 */
public class ReplaceMojo extends AbstractGWTMojo {

    /** Creates a new instance of ReplaceMojo */
    public ReplaceMojo() {
        super();
    }

    public void execute() throws MojoExecutionException, MojoFailureException {
        Properties cts = this.getCompileTarget();
        Set keySet = cts.keySet();
        for (Object target : keySet) {
            File fto = new File(this.getOutput(), (String)cts.get(target));
            fto.mkdir();
            String path = this.getOutput().getAbsolutePath() + File.separator + target;
            File ffrom = new File(path);
            try {
                FileUtils.copyDirectory(ffrom, fto);
                FileUtils.deleteDirectory(ffrom);
            } catch (IOException e) {
                e.printStackTrace();
            }

            File fwebxmlOld = new File(this.getBuildDir(), "web.xml");
            String fwebxmlpath = fwebxmlOld.getAbsolutePath();
            try {
                String webxml = FileUtils.readFileToString(fwebxmlOld, "utf-8");
                File fwebxmlNew = new File(fwebxmlpath);
                webxml = webxml.replace("<url-pattern>/" + target, "<url-pattern>/"
                                + cts.get(target));
                FileUtils.writeStringToFile(fwebxmlNew, webxml);
            } catch (IOException e) {
                throw new MojoExecutionException(e.getMessage());
            }
        }

    }
}
