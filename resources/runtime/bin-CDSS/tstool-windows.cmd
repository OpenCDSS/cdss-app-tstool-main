rem TSTool run script for Windows.
rem Use this to test startup to make sure installer is including all needed files.

set javaCommand=C:\CDSS\TSTool-14.4.0\jre_18\bin\java.exe -Xmx1024m -Djava.net.useSystemProxies=true
set homeFolder=C:\CDSS\TSTool-14.4.0
set binFolder=C:\CDSS\TSTool-14.4.0\bin
set mainClass=DWR.DMI.tstool.TSToolMain
%javaCommand% -classpath %binFolder%\* %mainClass% --home %homeFolder%
