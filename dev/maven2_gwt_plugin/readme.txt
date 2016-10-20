Changes:
1) AbstractGWTMojo.java. Field "compileTarget" changed type from "String []"  to "Map<String,String>".
2) Added class "ReplaceMojo". It add possibility to customize directory paths to the modules.
It just replace default path (such as <pck1.pck2.MyModule>) by user defined path (gwt, ajax etc).  
3) Changed pom.xml.
4) Changed GWTSetup. Added functionality to remove folder ".gwt-cache".
5) Change GWTMojo. Now external library add to the runtime classpath that place to "lib" and/or 
"libs" directory from the root project path. 