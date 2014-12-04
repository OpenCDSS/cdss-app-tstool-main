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

Var rivertrak_dir

Section "Rivertrak® System Base Components" BaseComponents

    # set the cursor to hour glass so user knows the application is installing
    SetCursor::System WAIT

    # set the Registry key value for rivertrak
    #WriteRegStr HKLM "SOFTWARE\rivertrak" Path $INSTDIR

    # make sure only newer files are installed
    SetOverwrite ifnewer

    # set main program directories
    strcpy $rivertrak_dir $INSTDIR
    
    # create the logs directory
    SetOutPath $rivertrak_dir
    SetOverWrite off
    CreateDirectory $rivertrak_dir\logs
    
    # create graphics directory
    #CreateDirectory $rivertrak_dir\graphics
    
    SetOverwrite ifnewer
    SetOutPath $rivertrak_dir\system
    #File ..\..\externals\Rivertrak\graphics\waterMark.bmp
    #File ..\..\externals\rivertrak\graphics\waterMark.jpg
    File ..\..\externals\Rivertrak\graphics\RTi.ico
    
    # Add $INSTDIR\bin to the PATH ENV Variable
    Push "$INSTDIR\bin"
    Call AddToPath
    
    # set the cursor back to normal
    SetCursor::System NORMAL
    
SectionEnd
