# cdss-app-tstool-main #

This repository contains the TSTool main application source code and supporting files for the development environment.
Multiple other repositories are used to create the application.
Eclipse is used for development and repositories currently contain Eclipse project files to facilitate
setting up the Eclipse development environment.

TSTool is part of Colorado's Decision Support Systems (CDSS).
The TSTool software is being migrated to an open source software project as part of the OpenCDSS project.
See the following online resources:

* [CDSS](http://cdss.state.co.us)
* [OpenCDSS](http://learn.openwaterfoundation.org/cdss-emod-dev/)
* [TSTool Developer Documentation](http://learn.openwaterfoundation.org/cdss-app-tstool-doc-dev/)
* [TSTool User Documentation](http://learn.openwaterfoundation.org/cdss-app-tstool-doc-user/)

The developer documentation and guidelines will be updated as the development environment is used in development.  See the following sections in this page:

* [Repository Folder Structure](#repository-folder-structure)
* [Repository Dependencies](#repository-dependencies)
* [Development Environment Folder Structure](#development-environment-folder-structure)
* [Contributing](#contributing)
* [License](#license)
* [Contact](#contact)

-----

## Repository Folder Structure ##

The following are the main folders and files in this repository, listed alphabetically.
See also the [Development Environment Folder Structure](#development-environment-folder-structure)
for overall folder structure recommendations.

```
cdss-app-tstool-main/         TSTool source code and development working files.
  .classpath                  Eclipse configuration file.
  .git/                       Git repository folder (DO NOT MODIFY THIS except with Git tools).
  .gitattributes              Git configuration file for repository.
  .gitignore                  Git configuration file for repository.
  .project                    Eclipse configuration file.
  .pydevproject               Eclipse configuration file for Python integration (experimental).
  bin/                        Eclipse folder for compiled files (dynamic so ignored from repo).
  build-util/                 Utility scripts used in development environment.
  conf/                       Configuration files for installer build tools.
  dist/                       Folder used to build distributable installer (ignored from repo).
  externals/                  Third-party libraries and tools (may remove/move in future).
  graphics/                   Images (may remove/move in future).
  installer/                  TSTool-specific files used to create installer.
  lib/                        Third-party libraries.
  LICENSE.txt                 TSTool license file.
  nbproject/                  NetBeans project (legacy, may be removed).
  README.md                   This file.
  resources/                  Additional resources, such as runtime files for installer.
  scripts/                    Eclipse run and external tools configurations.
  src/                        TSTool main application source code.
  test/                       Unit tests using JUnit.
```

## Repository Dependencies ##

Repository dependencies fall into three categories as indicated below.

### TSTool Repository Dependencies ###

The main TSTool code depends on other repositories
The following repositories are used to create the main TSTool application.
Some repositories correspond to Eclipse projects and others are not used within Eclipse,
indicated as follows:

* Y - repository is included as Eclipse project.
* Y2 - repository is currently included as Eclipse project but may be phased out or
converted to a plugin because code is obsolete or is specific to third parties.
* y - repository is included as Eclipse project but does not need to be.  The project may have been added to Eclipse to use the Git client,
but files are often edited external to Eclipse.
* N - repository is managed outside if Eclipse,
such as documentation managed with command line Git or other Git tools.

|**Repository**|**Eclipse project?**|**Description**|
|-------------------------------------------------------------------------------------------------------------|--|----------------------------------------------------|
|[`cdss-app-tstool-doc`](https://github.com/OpenWaterFoundation/cdss-app-tstool-doc)                          |y |Legacy TSTool documentation and training (Word/PDF).|
|[`cdss-app-tstool-doc-dev`](https://github.com/OpenWaterFoundation/cdss-app-tstool-doc-dev)                  |N |TSTool developer documentation (Markdown/MkDocs).|
|[`cdss-app-tstool-doc-user`](https://github.com/OpenWaterFoundation/cdss-app-tstool-user)                    |N |TSTool user documentation (Markdown/MkDocs).|
|`cdss-app-tstool-main`                                                                                       |Y |TSTool main application code (this repo).|
|[`cdss-app-tstool-test`](https://github.com/OpenWaterFoundation/cdss-app-tstool-test)                        |y |TSTool functional tests using TSTool testing framework.|
|[`cdss-archive-nsis-2.46`](https://github.com/OpenWaterFoundation/cdss-archive-nsis-2.46)                    |N |Archive of NSIS 2.46, to set up development environment.|
|[`cdss-lib-cdss-java`](https://github.com/OpenWaterFoundation/cdss-lib-cdss-java)                            |Y |Library that is shared between CDSS components.|
|[`cdss-lib-common-java`](https://github.com/OpenWaterFoundation/cdss-lib-common-java)                        |Y |Library of core utility code used by multiple repos.|
|[`cdss-lib-dmi-hydrobase-java`](https://github.com/OpenWaterFoundation/cdss-lib-dmi-hydrobase-java)          |Y |Library to directly access Colorado's HydroBase database.|
|[`cdss-lib-dmi-hydrobase-rest-java`](https://github.com/OpenWaterFoundation/cdss-lib-dmi-hydrobase-rest-java)|Y |Library to access Colorado's HydroBase REST API.|
|[`cdss-lib-dmi-nwsrfs-java`](https://github.com/OpenWaterFoundation/cdss-lib-dmi-nwsrfs-java)                |Y2|Legacy library to access National Weather Service River Forecast System (NWSRFS) data files.|
|[`cdss-lib-dmi-riversidedb-java`](https://github.com/OpenWaterFoundation/cdss-lib-dmi-riversidedb-java)      |Y2|Legacy library to access Riverside Technology forecast system database.|
|[`cdss-lib-dmi-satmonsys-java`](https://github.com/OpenWaterFoundation/cdss-lib-dmi-satmonsys-java)          |Y2|Legacy library to directly access Colorado's Satellite Monitoring System database.|
|[`cdss-lib-models-java`](https://github.com/OpenWaterFoundation/cdss-lib-models-java)                        |Y |Library to read/write CDSS StateCU and StateMod model files.|
|[`cdss-lib-processor-ts-java`](https://github.com/OpenWaterFoundation/cdss-lib-processor-ts-java)            |Y |Library containing processor code for TSTool commands.|
|[`cdss-util-buildtools`](https://github.com/OpenWaterFoundation/cdss-util-buildtools)                        |Y |Tools to create CDSS Java software installers.|

### Plugin Repositories ###

Plugins are a new design feature that allows third parties to develop command and datastore plugins.
Plugins therefore allow functionality to be added to TSTool for specific systems without burdening
the core TSTool team with maintaining those plugins.

This section will be updated as plugins are developed.

### Repositories that Depend on TSTool Repository ###

This repository is not known to be a dependency for any other projects.

## Development Environment Folder Structure ##

The following folder structure is recommended for TSTool development.
Top-level folders should be created as necessary.
Repositories are expected to be on the same folder level to allow cross-referencing
scripts in those repositories to work.

```
C:\Users\user\                               Windows user home folder (typical development environment).
/home/user/                                  Linux user home folder (not tested).
/cygdrive/C/Users/user                       Cygdrive home folder (not tested).
  cdss-dev/                                  Projects that are part of Colorado's Decision Support Systems.
    TSTool/                                  TSTool product folder.
      eclipse-workspace/                     Folder for Eclipse workspace, which references Git repository folders.
                                             The workspace folder is not maintained in Git.
      git-repos/                             Git repositories for TSTool.
        cdss-app-tstool-doc/                 See repository dependency list above.
        cdss-app-tstool-doc-dev/
        cdss-app-tstool-doc-user/
        cdss-app-tstool-main/
        cdss-app-tstool-test/
        cdss-archive-nsis-2.46/
        cdss-lib-cdss-java/
        cdss-lib-common-java/
        cdss-lib-dmi-hydrobase-java/
        cdss-lib-dmi-hydrobase-rest-java/
        cdss-lib-dmi-nwsrfs-java/
        cdss-lib-dmi-riversidedb-java/
        cdss-lib-dmi-satmonsys-java/
        cdss-lib-models-java/
        cdss-lib-processor-ts-java/
        cdss-util-buildtools/
        ...others may be added...

```

## Contributing ##

Contributions to this project can be submitted using the following options:

1. TSTool software developers with commit privileges can write to this repository
as per normal OpenCDSS development protocols.
2. Post an issue on GitHub with suggested change.  Provide information using the issue template.
3. Email a development contact.
4. Fork the repository, make changes, and do a pull request.
Contents of the current master branch should be merged with the fork to minimize
code review before committing the pull request.

See also the [OpenCDSS / TSTool protocols](http://learn.openwaterfoundation.org/cdss-website-opencdss/tstool/tstool/).

## License ##

A license for the software is being determined as part of the OpenCDSS project.
GPL 3.0 has been recommended.

## Contact ##

See the [OpenCDSS TSTool information for product contacts](http://learn.openwaterfoundation.org/cdss-website-opencdss/tstool/tstool/#product-leadership).
