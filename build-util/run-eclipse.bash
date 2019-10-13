#!/bin/bash
#
# run-eclipse - run Eclipse Java for TSTool development
#
# For now hard code paths to software but can add more intelligence to
# check for Java and Eclipse in standard locations.

# For now just make sure that the Java version used is 8,
# regardless of which version is installed because Java 8 has been tested.
# If OpenJDK, java -version is similar to:
#     openjdk version "1.8.0_171"
# First check the version assuming that openjdk is used.
# - the -version output is apparently printed to standard error so combine output streams
java8Program=java
javaVersion=$(java -version 2>&1 | grep 'openjdk version' | cut -d ' ' -f 3 | tr -d '"' | cut -d '.' -f 2)
if [ "$javaVersion" != "8" ]; then
	echo "Java version is not 8.  Exiting."
	exit 1
fi

# For now hardcode the Eclipse program paths for likely locations
# - list the most recent first
# - the folder structure was created by the Eclipse installer
eclipseProgram=""
if [ -e "$HOME/eclipse/java-2019-12/eclipse/eclipse" ]; then
	eclipseProgram="$HOME/eclipse/java-2019-12/eclipse/eclipse"
elif [ -e "$HOME/eclipse/java-2019-09/eclipse/eclipse" ]; then
	eclipseProgram="$HOME/eclipse/java-2019-09/eclipse/eclipse"
elif [ -e "$HOME/eclipse/java-2018-12/eclipse/eclipse" ]; then
	eclipseProgram="$HOME/eclipse/java-2018-12/eclipse/eclipse"
fi

if [ -z "$eclipseProgram" ]; then
	echo ""
	echo "Could not find Eclipse in standard locations.  Exiting."
	exit 1
fi

# Found an eclipse to run so start with specific Java found above
echo "Running Eclipse:  ${eclipseProgram}"
${eclipseProgram} -vm ${java8Program} -vmargs -Xmx1024M

exit 0
