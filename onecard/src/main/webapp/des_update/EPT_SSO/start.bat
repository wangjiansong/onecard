@echo off
set CLASSPATH=.;.\ojdbc6.jar;
set JAVA=%JAVA_HOME%\bin\java
"%JAVA%" -classpath "%CLASSPATH%" com.interlib.sso.des.ept.client.EPTMain
pause