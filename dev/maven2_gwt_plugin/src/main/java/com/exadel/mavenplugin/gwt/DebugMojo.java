/*
 * DebugMojo.java
 *
 * Created on January 12, 2007, 9:45 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.exadel.mavenplugin.gwt;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * @goal debug
 * @author cooper
 */
public class DebugMojo extends GWTMojo {
    public DebugMojo() {
        super();
    }

    public void execute() throws MojoExecutionException, MojoFailureException {
        System.out.println( "Starting debugger on port "+this.getPort() );
        super.execute();
    }
    
    
}
