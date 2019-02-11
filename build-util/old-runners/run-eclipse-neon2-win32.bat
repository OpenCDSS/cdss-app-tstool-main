@echo off
rem Configure and run Eclipse Neon 2 32-bit Windows for standard TSTool development environment.
rem -32-bit is used until some legacy issues can be resolved, such as 32-bit library dlls

rem It is assumed that the development environment follows the standard folder structure, for example:
rem - %USERPROFILE%\cdss-dev\TSTool\git-repos\cdss-app-tstool-main
rem - HECLIB_FOLDER environment variable is used in cdss-app-tstool-main run configuration to find
rem   dlls for HEC DSS support

rem TODO smalers 2017-03-11 Need to evaluate how to handle 64-bit and Linux versions of libraries
rem SET HECLIB_FOLDER=%USERPROFILE%\cdss-dev\TSTool\git-repos\cdss-lib-processor-ts-java\lib\heclib\win32
SET HECLIB_FOLDER=%USERPROFILE%\cdss-dev\TSTool\git-repos\cdss-lib-processor-ts-java\lib\heclib

rem Run Eclipse
rem - the following specifically sets the VM location, which works fine if developers follow that convention
rem - TODO smalers 2017-03-18 could change this script to fall back to using JAVA_HOME
echo HECLIB_FOLDER=%HECLIB_FOLDER%
@echo on
"C:\Program Files (x86)\eclipse-java-neon-2-win32\eclipse\eclipse.exe" –vm "C:\Program Files (x86)\Java\jdk8\bin\javaw.exe" -vmargs -Xmx1024M
