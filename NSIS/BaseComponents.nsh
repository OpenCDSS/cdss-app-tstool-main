#####################################################
# Header file for use in all other CDSS installs
# for the base components.  
# BaseComponents includes the following:
#
#   shellcon.exe
#   HydroBaseDMI_XXX.jar
#   msbase.jar
#   mssqlserver.jar
#   msutil.jar
#   RTi_xxx.jar
#   JRE_xxx directory
#   CDSS.cfg
#   DATAUNIT
#
# OPERATION:  function first checks to make sure
#       that the current install is not newer than
#       the files being installed.  If the current
#       version is already up-to-date user should be
#       prompted and install of that file will abort 
#####################################################

Var cdss_dir

Section "CDSS Base Components"

    # set main program directories
    strcpy $cdss_dir $INSTDIR
    
    # copy important bat/jar files for base CDSS functionality
    SetOutPath $cdss_dir\bin
    
    ## check access time
    strcpy $cur_file "$cdss_dir\bin\shellcon.exe"
    strcpy $install_file "..\externals\shellcon.exe"
    Call CompareFileModificationDates
    strcmp "0" $AccessReturnVal +3 0
      DetailPrint "Local version $cdss_dir\bin\shellcon.exe is current"  
      Goto +2
      File ..\externals\shellcon.exe
   
    ## check access time  
    strcpy $cur_file "$cdss_dir\bin\HydroBaseDMI_142.jar"
    strcpy $install_file "..\externals\HydroBaseDMI_142.jar"
    Call CompareFileModificationDates
    strcmp "0" $AccessReturnVal +3 0
      DetailPrint "local version $cdss_dir\bin\HydroBaseDMI_142.jar is current."
      Goto +2
      File ..\externals\HydroBaseDMI_142.jar
    
    
    ## check access time
    strcpy $cur_file "$cdss_dir\bin\msbase.jar"
    strcpy $install_file "..\externals\msbase.jar"
    Call CompareFileModificationDates
    strcmp "0" $AccessReturnVal +3 0
      DetailPrint "Local version $cdss_dir\bin\msbase.jar is current"
      Goto +2
      File ..\externals\msbase.jar
    
    ## check access time    
    strcpy $cur_file "$cdss_dir\bin\mssqlserver.jar"
    strcpy $install_file "..\externals\mssqlserver.jar"
    Call CompareFileModificationDates
    strcmp "0" $AccessReturnVal +3 0
      DetailPrint "Local version $cdss_dir\bin\mssqlserver.jar is current"
      Goto +2
      File ..\externals\mssqlserver.jar
    
    ## check access time  
    strcpy $cur_file "$cdss_dir\bin\msutil.jar"
    strcpy $install_file "..\externals\msutil.jar"
    Call CompareFileModificationDates
    strcmp "0" $AccessReturnVal +3 0
      DetailPrint "Local version $cdss_dir\bin\msutil.jar is current"
      Goto +2
      File ..\externals\msutil.jar
    
    ## check access time    
    strcpy $cur_file "$cdss_dir\bin\RTi_142.jar"
    strcpy $install_file "..\externals\RTi_142.jar"
    Call CompareFileModificationDates
    strcmp "0" $AccessReturnVal +3 0
      DetailPrint "Local version $cdss_dir\bin\RTi_142.jar is current"
      Goto +2
      File ..\externals\RTi_142.jar
    
    # copy current JRE
    # check access time
    strcpy $cur_file "$cdss_dir\jre_142"
    strcpy $install_file "..\externals\jre_142"
    Call CompareFileModificationDates
    strcmp "0" $AccessReturnVal +3 0
      DetailPrint "Local version $cdss_dir\jre_142 is current"
      Goto +2
      Goto installJRE
    
    Goto +3
    
    installJRE:
    SetOutPath $cdss_dir\jre_142
    File /r ..\externals\jre_142\*
    
    ## copy system files
    SetOutPath $cdss_dir\system
    ## check access date
    strcpy $cur_file "$cdss_dir\system\DATAUNIT"
    strcpy $install_file "..\externals\DATAUNIT"
    Call CompareFileModificationDates
    strcmp "0" $AccessReturnVal +3 0
      DetailPrint "Local version $cdss_dir\system\DATAUNIT is current"
      Goto +2
      File ..\externals\DATAUNIT
    
    ## check access date
    strcpy $cur_file "$cdss_dir\system\CDSS.cfg"
    strcpy $install_file "..\externals\CDSS.cfg"
    Call CompareFileModificationDates
    strcmp "0" $AccessReturnVal +3 0
      DetailPrint "Local version $cdss_dir\system\CDSS.cfg is current"
      Goto +2
      File ..\externals\CDSS.cfg
     
    
    # create the logs directory
    SetOutPath $cdss_dir
    SetOverWrite off
    CreateDirectory $cdss_dir\logs
    
    
SectionEnd