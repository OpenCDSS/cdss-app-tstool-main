# test #

This folder contains test configuration for TSTool development environment.
This allows TSTool to be run from Eclipse as if in a production environment,
without having to do a full build and install.

See the Eclipse ***Run / Run Configurations*** such as `TSTool_CDSS`,
which specifies `-home test/operational/CDSS` to run TSTool from Eclipse.

These files should not be confused with functional tests that are in the
`cdss-app-tstool-test` repository, although those tests can be run
from TSTool that is started from Eclipse.
