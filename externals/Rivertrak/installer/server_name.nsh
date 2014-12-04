!define TEMP1 $R0  
Var server_name
Var default_server_name
Var HWND
Var DLGITEM

Function SetCustom

  MessageBox MB_YESNO "Do you want to use a HydroBase database that is installed on another computer? $\r$\nSelect Yes if you know the name of an available HydroBase server." IDYES true IDNO false
   false:
   Goto skip
   true:
 
  !insertmacro MUI_HEADER_TEXT "Configure HydroBase Settings" "Specify the HydroBase servers for software"
  !insertmacro MUI_INSTALLOPTIONS_DISPLAY "server_name.ini"
  
  strcpy $server_name ""
  strcpy $default_server_name ""
  
  # get the user input string for the external DB
  ReadINIStr ${TEMP1} "$PLUGINSDIR\server_name.ini" "Field 2" "State"
  strcpy $server_name ${TEMP1}
  ReadINIStr ${TEMP1} "$PLUGINSDIR\server_name.ini" "Field 4" "State"
  strcpy $default_server_name ${TEMP1}
  
  # default the two servers to local if user supplied no input
  strcmp $server_name "" 0 +2
    strcpy $server_name "local"
  strcmp $default_server_name "" 0 +2
    strcpy $default_server_name "local"
  
  # replace/rewrite CDSS.cfg file 
  ClearErrors
  FileOpen $0 $cdss_dir\system\CDSS.cfg w
  IfErrors finish
  FileWrite $0 "# Configuration file for CDSS (Colorado's Decision Support Systems)$\r$\n"
  FileWrite $0 "#$\r$\n"
  FileWrite $0 "# This file stores shared information for the system, including:$\r$\n"
  FileWrite $0 "#$\r$\n"
  FileWrite $0 "# * HydroBase database properties, defining connection defaults.$\r$\n"
  FileWrite $0 "# * Satellite Monitoring System (ColoradoSMS) properties, defining connection$\r$\n"
  FileWrite $0 "#   defaults.$\r$\n"
  FileWrite $0 "$\r$\n"
  FileWrite $0 "[HydroBase]$\r$\n"
  FileWrite $0 "$\r$\n"
  FileWrite $0 "ServerNames=$\"$server_name$\"$\r$\n"
  FileWrite $0 "DefaultServerName=$\"$default_server_name$\"$\r$\n"
  FileWrite $0 "DefaultDatabaseName=$\"HydroBase$\"$\r$\n"
  FileClose $0
  finish:
  
 
  # user chose not to configure an external DB
  skip:  
    

FunctionEnd

Function ValidateCustom

  ReadINIStr ${TEMP1} "$PLUGINSDIR\server_name.ini" "Field 2" "State"
  StrCmp ${TEMP1} 1 done
  
  done:
  
FunctionEnd