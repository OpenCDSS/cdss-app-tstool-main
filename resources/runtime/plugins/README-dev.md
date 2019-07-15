# plugins #

**This `README-dev.md` file is not intended for distribution in the TSTool installer.**

This folder contains plugin datastores that are provided in the TSTool install folder.

* Plugins are typically installed after installing TSTool because they
often are provided by a third party, for example to integrate with a specific datastore
that is not part of the built-in functionality.
* The folder is useful when TSTool is installed in a system that shares software,
for example, when installing TSTool in Linux, by installing relevant plugin files into the folder.
* Users can install plugins in the `.tstool/N/plugins` folder.
* See the similar `test/operational/CDSS/plugins` folder for development environment.
* The `conf/build.xml` file controls copying plugin files into `dist` folder when packaging the installer.
