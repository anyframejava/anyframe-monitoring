@echo off
SET CATALINA_HOME=D:\projects\Samsung_Infrared\Documents\project\dev01\tmp\apache-tomcat-5.5.25
SET SOURCE_DIR=D:\projects\Samsung_Infrared\Samsung_Infrared_svn\modules\Infrared\web-samsung-gui
SET WAR_NAME=samsung-infrared-web-1.0
SET WAR_DIR=%SOURCE_DIR%\target
SET JAVA_OPTS=-Xms128m -Xmx256m
SET SLEEP_TIME=60
@echo .
@echo =================================================
@echo ===      Begin Redeploy SAMSUNG INFRARED      ===
@echo =================================================
@echo .
@echo .
@echo USING:
@echo    WAR_NAME=%WAR_NAME%
@echo    WAR_DIR=%WAR_DIR%
@echo    JAVA_OPTS=%JAVA_OPTS%
@echo    SLEEP_TIME=%SLEEP_TIME%
@echo ================================

cd %CATALINA_HOME%/

@echo Stoping Tomcat...
call .\bin\shutdown.bat
echo sleeping for %SLEEP_TIME% sec. to Tomcat shutdown ...
call %SOURCE_DIR%\sleep %SLEEP_TIME%
echo .

@echo deleting old deployment and cleaning cash ...
RD /S  /Q %CATALINA_HOME%\work\Catalina\localhost\%WAR_NAME%
RD /S  /Q %CATALINA_HOME%\webapps\%WAR_NAME%
DEL /F /S  /Q %CATALINA_HOME%\webapps\%WAR_NAME%.war

@echo copying new WAR ...
copy %WAR_DIR%\%WAR_NAME%.war %CATALINA_HOME%\webapps
@echo starting Tomcat ...
call .\bin\startup.bat
@echo .
exit
