# CDSS #

Skeleton folders and files for Colorado's Decision Support Systems (CDSS) that
allow TSTool to be run in the Eclipse development environment and access
HydroBase database(s) installed on the local machine,
as well as web service datastores, etc.

Because TSTool versions 12, 13, and later support software installation configuration
(in folders relative to `-home` option) and user configuration files
(in `.tstool/` folder), it should be possible to minimize the configuration
information in this folder and rely on user configuration more for developers.
The traditional `system/CDSS.cfg` file controls the HydroBase selector
shown at TSTool startup and has a default form that is generic and should work on most systems.
However, some configuration may still be specific to developers.
The goal is to move developer-specific configuration into user `.tstool` files
so that the repository does not have configurations for specific developers.
