# Documentation for test environment

For now include some PDFs for the main documentation

It is recommended that the html folder is symbolically linked to the documentation repository location.
For example if developing on Windows/Cygwin, create the link in a Windows shell with the following
(may need administrator priveleges):

C:\owf-gitrepos\cdss-app-tstool-main\test\operational\CDSS\doc\UserManual>mklink /d html  ..\..\..\..\..\..\cdss-app-tstool-doc\doc\UserManual\html
symbolic link created for html <<===>> ..\..\..\..\..\..\cdss-app-tstool-doc\doc\UserManual\html

Theoretically, if created on Windows it will be recognized by Cygwin.
However, if committed to git, hooks need to be coded to handle properly.
Thereefore, use a .gitnore to ignore the file and recreate in the developer environment.

TODO SAM 2016-03-24 Need a script that runs on Windows and Linux to create such developer files.
Use rmdir to remove the symbolic link if it needs to be recreated.
