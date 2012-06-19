Instructions for creating Linux TSTool installer.  These files are meant to
be copied and used on a Linux machine.

1) Copy the Windows "dist/install-cdss" folder to this folder.
   Optionally add "-version" at the end so it is easier to see what
   version is being processed.  For example:  install-cdss-10.10.00beta
   This is done because Linux cannot access Windows (?).

2) If necessary, copy the buildLinuxInstaler to this folder.
   Make sure the buildLinuxInstaller is configured properly by editing
   the variables at the top of the script.

3) Run the buildLinuxInstaller script on Linux.  It should create a
   stand-alone installation and a *.tar.gz file that can be used to
   install the software.
