#
CATALINA_HOME="/home/samsung/projects/infrared/apache-tomcat-5.5.25"
SOURCE_DIR="/home/samsung/projects/infrared/svn_source/web-samsung-gui"
WAR_NAME="samsung-infrared-web-1.0"
WAR_DIR="$SOURCE_DIR/target"
JAVA_OPTS="-Xms128m -Xmx256m"
SLEEP_TIME="60"
#
echo .
echo =================================================================================================
echo ===========================      Begin Redeploy SAMSUNG INFRARED      ===========================
echo =================================================================================================
echo .
date
echo .
echo USING:
echo    WAR_NAME="$WAR_NAME"
echo    WAR_DIR=$WAR_DIR
echo    JAVA_OPTS=$JAVA_OPTS
echo    SLEEP_TIME=$SLEEP_TIME
echo ================================
#
cd "$CATALINA_HOME"/
#
echo Stoping Tomcat...
#ls -la
./bin/shutdown.sh
echo sleeping for "$SLEEP_TIME" sec. to Tomcat shutdown ...
sleep "$SLEEP_TIME"
echo .
#
echo deleting old deployment and cleaning cash ...
rm -rf "$CATALINA_HOME"/work/Catalina/localhost/"$WAR_NAME"
rm -rf "$CATALINA_HOME"/webapps/"$WAR_NAME"
rm -f "$CATALINA_HOME"/webapps/"$WAR_NAME".war
rm -rf "$CATALINA_HOME"/logs/*
#
echo copying new WAR ...
cp -f "$WAR_DIR"/"$WAR_NAME".war "$CATALINA_HOME"/webapps
sleep "$SLEEP_TIME"
echo starting Tomcat ...
nohup ./bin/startup.sh &
echo .
echo .
exit
