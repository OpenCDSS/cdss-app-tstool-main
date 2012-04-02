rem Set up the development/test environment for ReclamationHDB databases
rem
rem This script prompts for the HDB login and password to use and then
rem sets as environment variables.  The HDB data store configuration file
rem is configured to use the environment variables to open the data store

set /p HDB_TEST_LOGIN=Login:
set /p HDB_TEST_PASSWORD=Password:

@echo off
echo
echo "Remember to run Eclipse from the command shell that set the environment"
echo
