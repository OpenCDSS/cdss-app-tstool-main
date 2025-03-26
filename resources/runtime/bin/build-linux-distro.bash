#!/bin/bash
(set -o igncr) 2>/dev/null && set -o igncr; # This comment is required.
# The above line ensures that the script can be run on Cygwin/Linux even with Windows CRNL.
# If this does not work and an error results, try using 'dos2unix' to convert CRNL to NL
# on the script before running.
#
# build-linux-distro - build the Linux distribution for TSTool
#
# The script typically is run on a Linux computer that has access
# to a Windows installation of TSTool, for example a Linux virtual
# machine running in VirtualBox that has access to the Windows TSTool installation
# via a shared folder.
#
# This script has been developed and tested on the following and may need to be enhanced
# for other Linux distributions:
#
# - Debian (Jessie, Stretch)
#
# The script performs the following steps:
#
# 1) Copy all the files from the windows installation to a temporary folder.
# 2) Remove the Windows JRE and optionally replace with the contents of the JRE folder
#    specified with the -j option
#    - if not specified, 'java' will be found on the system at runtime
#      when the 'tstool' script is run
# 3) Create a tar.gz file that can be distributed using the name from the -z option
#    if specified
# 4) Package the installation with 'makeself' so that the installer can be downloaded and run
#

# Supporting functions, alphabetized.

# Make sure Java is available to get TSTool version:
# - Debian prints nothing if not found or something like /usr/bin/java if found
checkJava() {
  local javaPath javaVersion java6Count java7Count
  javaPath=$(which java)
  if ! type java > /dev/null; then
    echo ""
    echo "'java' is not found in PATH.  Need 'java' to determine TSTool version."
    echo "Try running this script on a system where Java is installed."
    exit 1
  else
    # Need Java 11 or later to run TSTool and get version:
    # - version is printed to standard error so combine output
    # - not sure what version will look like for 11, 12, etc. so check for older versions
    # - on Debian Bullseye, output is as follows (-version as shown is different from --version):
    #   openjdk version "11.0.18" 2023-01-17
    #   OpenJDK Runtime Environment (build 11.0.18+10-post-Debian-1deb11u1)
    #   OpenJDK 64-Bit Server VM (build 11.0.18+10-post-Debian-1deb11u1, mixed mode, sharing)
    javaVersion=$(java -version 2>&1 | grep -i "version" | cut -d ' ' -f 3 | tr -d '"')
    if [ "${javaVersion}" ]; then
      echo "Java version is: ${javaVersion}"
    fi
    # Check for older versions, which will cause problems.
    java6Count=$(echo ${javaVersion} | grep '1\.6' | wc -l)
    java7Count=$(echo ${javaVersion} | grep '1\.7' | wc -l)
    java8Count=$(echo ${javaVersion} | grep '1\.8' | wc -l)
    if [ "${java6Count}" -gt 0 ]; then
      ${echo2} "${warnColor}'java' program is version 6.  Version 11 or later is required.${endColor}"
      exit 1
    elif [ "${java7Count}" -gt 0 ]; then
      ${echo2} "${warnColor}'java' program is version 7.  Version 11 or later is required.${endColor}"
      exit 1
    elif [ "${java8Count}" -gt 0 ]; then
      ${echo2} "${warnColor}'java' program is version 8.  Version 11 or later is required.${endColor}"
      exit 1
    fi
  fi
}

# Make sure input exists.
checkInput() {
  # If JRE is specified, make sure it exists.
  if [ ! -z "${jreFolder}" -a ! -d "${jreFolder}" ]; then
    ${echo2} "${warnColor}JRE folder does not exist:${endColor}"
    ${echo2} "${warnColor}  ${jreFolder}${endColor}"
    printUsage
    exit 1
  fi

  # Make sure that setup script exists.
  if [ ! -f "${setupScriptPath}" ]; then
    echo ""
    ${echo2} "${warnColor}The 'makeself' setup script does not exist:${endColor}"
    ${echo2} "${warnColor}  ${setupScriptPath}${endColor}"
    exit 1
  fi
  # Make sure the setup script has Linux end of line:
  # - the Git repository .gitattributes file tries to enforce but make sure here
  dos2unix ${setupScriptPath}
  if [ $? -ne 0 ]; then
    echo "Error running dos2unix on setup script: ${setupScriptPath}"
    echo "Exiting because issues may result later."
    echo "Maybe dos2unix is not installed?"
    echo "Maybe a permissions issue writing the file?"
    exit 1
  fi
}

# Determine which echo to use, needs to support -e to output colored text:
# - normally built-in shell echo is OK, but on Debian Linux dash is used, and it does not support -e
configureEcho() {
  echo2='echo -e'
  testEcho=`echo -e test`
  if [ "${testEcho}" = '-e test' ]; then
    # The -e option did not work as intended.
    # -using the normal /bin/echo should work
    # -printf is also an option
    echo2='/bin/echo -e'
    # The following does not seem to work.
    #echo2='printf'
  fi

  # Strings to change colors on output, to make it easier to indicate when actions are needed:
  # - Colors in Git Bash:  https://stackoverflow.com/questions/21243172/how-to-change-rgb-colors-in-git-bash-for-windows
  # - Useful info:  http://webhome.csc.uvic.ca/~sae/seng265/fall04/tips/s265s047-tips/bash-using-colors.html
  # - See colors:  https://en.wikipedia.org/wiki/ANSI_escape_code#Unix-like_systems
  # - Set the background to black to ensure that white background window will clearly show colors contrasting on black.
  # - Yellow "33" in Linux can show as brown, see:  https://unix.stackexchange.com/questions/192660/yellow-appears-as-brown-in-konsole
  # - Tried to use RGB but could not get it to work - for now live with "yellow" as it is
  warnColor='\e[0;40;93m' # user needs to do something, 40=background black, 33=yellow, 93=bright yellow
  errorColor='\e[0;40;31m' # serious issue, 40=background black, 31=red
  menuColor='\e[0;40;36m' # menu highlight 40=background black, 36=light cyan
  okColor='\e[0;40;32m' # status is good, 40=background black, 32=green
  endColor='\e[0m' # To switch back to default color
}

# Configure TSTool, such as disabling State of Colorado direct HydroBase database and unneeded datastores:
# - currently options are offered for common tasks since the Linux environment is different from Windows
# - upcoming TSTool releases will be distributed with command files to facilitate configuration,
#   but that is not yet enabled
configureTSTool() {
  # For now disable HydroBase local database features since often not available on Linux
  # and slows down startup.
  # - /I ignores case
  echo "Disabling HydroBase database (can enable later if needed)."
  tstoolCfgFile="${tmpBuildFolder}/${installFolderNoPathLower}/system/TSTool.cfg"
  sed -i 's/^HydroBaseEnabled.*/HydroBaseEnabled = false/I' ${tstoolCfgFile}
}

# Copy the Windows TSTool files into Linux temporary folder.
copyWindowsFilesToLinux() {
  # Remove the build folder.

  if [ -d "${tmpBuildFolder}" ]; then
    echo "Removing previous build folder:"
    echo "  ${tmpBuildFolder}"
    rm -rf "${tmpBuildFolder}"
  fi

  # Create the build folder.
  echo "Creating build folder:"
  echo "  ${tmpBuildFolder}"
  mkdir -p ${tmpBuildFolder}

  # Copy the files from the original install to the build folder.

  echo "Copying files:"
  echo "  from: ${installFolder}"
  echo "    to: ${tmpBuildFolder}"
  cp -r "${installFolder}" "${tmpBuildFolder}"

  # Rename the folder to lowercase (e.g., TSTool-Version to tstool-version)
  mv ${tmpBuildFolder}/${installFolderNoPath} ${tmpBuildFolder}/${installFolderNoPathLower}

  # Remove files don't need to be distributed with Linux.

  echo "Removing Windows files that don't need to be included in Linux distribution."
  echo "Some of these files may generate errors and need to be cleaned up in Windows version."
  echo "${tmpBuildFolder}/${installFolderNoPathLower}/Uninstall*"
  rm -f ${tmpBuildFolder}/${installFolderNoPathLower}/Uninstall*
  echo "${tmpBuildFolder}/${installFolderNoPathLower}/bin/*.DLL"
  rm -f ${tmpBuildFolder}/${installFolderNoPathLower}/bin/*.DLL
  echo "${tmpBuildFolder}/${installFolderNoPathLower}/bin/*.dll"
  rm -f ${tmpBuildFolder}/${installFolderNoPathLower}/bin/*.dll
  echo "${tmpBuildFolder}/${installFolderNoPathLower}/bin/TSTool.bat"
  rm -f ${tmpBuildFolder}/${installFolderNoPathLower}/bin/TSTool.bat
  echo "${tmpBuildFolder}/${installFolderNoPathLower}/bin/*.exe"
  rm -f ${tmpBuildFolder}/${installFolderNoPathLower}/bin/*.exe
  echo "${tmpBuildFolder}/${installFolderNoPathLower}/bin/TSTool.l4j.ini"
  rm -f ${tmpBuildFolder}/${installFolderNoPathLower}/bin/TSTool.l4j.ini
  echo "${tmpBuildFolder}/${installFolderNoPathLower}/bin/tstool-linux"
  rm -f ${tmpBuildFolder}/${installFolderNoPathLower}/bin/tstool-linux
  echo "${tmpBuildFolder}/${installFolderNoPathLower}/bin/tstool-latest.bat"
  rm -f ${tmpBuildFolder}/${installFolderNoPathLower}/bin/tstool-latest.bat
  echo ${tmpBuildFolder}/${installFolderNoPathLower}/jre*
  rm -rf ${tmpBuildFolder}/${installFolderNoPathLower}/jre*

  # Replace the old JRE with the specified one if provided:
  # - otherwise the 'tstool' script will use 'java' found on the installed system

  if [ ! -z "${jreFolder}" -a -d "${jreFolder}" ]; then
    echo "Copying Linux JRE ${jreFolder} to distribution folder ${tmpBuildFolder}"
    cp -rp ${jreFolder} ${tmpBuildFolder}
  fi

  # Make sure file permissions are correct for Linux.
  chmod a+x ${tmpBuildFolder}/${installFolderNoPathLower}/bin/tstool
  chmod a+x ${tmpBuildFolder}/${installFolderNoPathLower}/bin/${setupScript}
  # Make sure log folder is writeable.
  chmod a+w ${tmpBuildFolder}/${installFolderNoPathLower}/logs
  # Make sure directories are executable.
  chmod -R +rX ${tmpBuildFolder}
}

# Create the self-extracting installer with 'makeself':
# - see: https://makeself.io/
createMakeselfInstaller() {
  # Create the installer using 'makeself':
  # - might need to use the following options:
  #  --license file?
  #  --header file
  #  --help-header file
  #
  # Want the top level folder in the installer to contain tstool-13.00.00 or similar,
  # without additional parent folders:
  # - seems to be cleaner to work with relative files
  cd ${tmpBuildFolder}
  # The following will be something like:
  #   makeself ./tstool-13.00.00 /tmp/tstool-dist/TSTool-linux-13.00.00.sh "TSTool Software" bin/setup-linux-tstool.bash
  #makeself ./${installFolderNoPathLower} ${selfExtractingShFile} "TSTool Software" bin/${setupScript}
  makeself . ${selfExtractingShFile} "TSTool Software" ${installFolderNoPathLower}/bin/${setupScript}
}

# Run custom build steps, for example to package plugins, etc.:
# - this is used to create a bundled TSTool that can be installed
#   on a system without further configuration
# - can do several things such as auto-finding custom build script and
#   supporting command line option to add
# - in any case, this will require that a custom build probably use
#   a wrapper script that calls this script
customBuildSteps() {
  echo "Custom build steps are not yet enabled."
}

# Get the TSTool version from string:
# - run:  tstool --version
# - TSTool output has a lot of extra text but contains a string similar to:
#      TSTool version: 13.00.00 (2019-07-12)
# - this should work because 'tstool' script will run the program
# - awk is used to remove surrounding whitespace
# - version is echoed so can be assigned in calling code
getTstoolVersion() {
  local version
  # Version is printed to stderr.
  version=$(${binFolder}/tstool --version 2>&1 | grep -i 'TSTool version:' | cut -d ':' -f 2 | awk '{$1=$1};1' | cut -d ' ' -f 1)
  # Echo so the output can be assigned to the calling code.
  echo ${version}
}

# Parse the command line.
parseCommandLine () {
  local opt optstring OPTARG
  optstring="hj:s:w:z:"
  while getopts ${optstring} opt; do
    case "${opt}" in
      h) # Print usage and exit.
        printUsage
        exit 0
        ;;
      j) # Specify JRE folder to package with the installation:
         # - optional with default being to use system default java
        jreFolder="${OPTARG}"
        ;;
      s) # Self-extracting executable file to create.
        installerFile="${OPTARG}"
        ;;
      w) # Windows TSTool installation folder:
         # - TODO smalers 2019-07-30, need to evaluate phasing this out since script can figure out its home
        windowsTstoolInstallFolder="${OPTARG}"
        ;;
      z) # Zip file to create.
        zipFile="${OPTARG}"
        ;;
      \?) # Unknown - print the usage.
        echo "" 
        echo "Invalid option:  -${OPTARG}" >&2
        printUsage
        exit 1
        ;;
      :)
        echo "" 
        echo "Option -${OPTARG} requires an argument" >&2
        printUsage
        exit 1
        ;;
    esac
  done
}

# Print the program usage.
printUsage () {
  echo ""
  echo "Usage: ${scriptName} [options]"
  echo ""
  echo "Create the distribution (installer) for Linux TSTool from a Windows installation."
  echo "This script can be run on Linux while accessing the shared Windows TSTool 'bin' folder."
  echo "The resulting installer is a shell script with internally-attached zipped archive."
  echo ""
  echo "Example:"
  echo "  /path/to/shared/folder/TSTool-Version/bin/${scriptName} -s TSTool-linux-14.00.00-2109270058.run"
  echo "  /path/to/shared/folder/TSTool-Version/bin/${scriptName} -s TSTool-jre-linux-14.00.00-2109270058.run  (if JRE is packaged)"
  echo "  Result will be located in:  /tmp/tstool-dist/"
  echo ""
  echo "-j JREInstallFolder           (optional) Path to the Linux JRE install folder."
  echo "                              The default is to use 'java' on the installed system."
  echo "-w WindowsTSToolInstallFolder (optional) Path to the TSTool Windows install folder."
  echo "                              This is being phased out."
  echo "-s distro.sh                  (optional) distribution file to create (specify .sh to avoid confusion)."
  echo "                              The default is:  TSTool-linux-NN.NN.NN-YYMMDDhhmm.run"
  echo "-z distro.tar.gz              (optional) tar.gz file to create (specify .tar.gz to avoid confusion)."
  echo "                              The default is TSTool-linux-NN.NN.NN.tar.gz".
  echo "                              THIS IS BEING PHASED OUT BECAUSE -s IS USED."
  echo ""
  exit 0
}

# Main entry point into script.

# Folder where tstool script is located, should be the "bin" folder in a TSTool install.
scriptFolder=$(cd $(dirname "$0") && pwd)
scriptName=$(basename $0)
binFolder=${scriptFolder}
tstoolScript=${binFolder}/tstool
setupScript=setup-linux-tstool.bash
setupScriptPath=${binFolder}/${setupScript}
# Installation home for TSTool, under which will be bin, logs, system, etc.
installFolder=$(dirname ${scriptFolder})
installFolderNoPath=$(basename ${installFolder})
installFolderNoPathLower=$(echo ${installFolderNoPath} | tr '[:upper:]' '[:lower:]')

# Configure the echo command for colored output:
# - do this up front because results are used in messages
configureEcho

# Parse the command line.
parseCommandLine $@

# TODO smalers 2019-07-30 need to check for dos2unix being installed.
# Make sure the 'tstool' script has Linux end of line:
# - the script tries to handle this but not all Linux OS recognize
# - the Git repository .gitattributes file tries to enforce but make sure here
dos2unix ${tstoolScript}
if [ $? -ne 0 ]; then
  echo "Error running dos2unix on tstool script: ${tstoolScript}"
  echo "Exiting because issues may result later."
  echo "Maybe dos2unix is not installed?"
  echo "Maybe a permissions issue writing the file?"
  exit 1
fi

# Check that java exists to run TSTool to get version:
# - TODO smalers, 2019-07-30 maybe Windows TSTool distribution should have version
#   file so software does not need to run
checkJava

# Get the TSTool version.
tstoolVersion=$(getTstoolVersion)
if [ -z "${tstoolVersion}" ]; then
  echo "Unable to determine TSTool version."
  exit 1
else
  echo "TSTool version determined to be:  ${tstoolVersion}"
fi

# Default values.
# Folder where Linux files will be assembled.
tmpDistFolder="/tmp/tstool-dist"
tmpBuildFolder="${tmpDistFolder}/build"
# JRE if being packaged with TSTool:
# - default is to use system 'java' at runtime in 'tstool' script
jreFolder=""
# Zip file for package:
# - default is to not include 32 or 64 bit in name since focusing on 64bit
#   but could add that if necessary
# - this is not currently used since 'makeself' is used
#zipFile="${tmpDistFolder}/TSTool-linux-${tstoolVersion}.tar.gz"

# Self-extracting file:
# - use makeself convention of using 'run'
# - the installer filename is of format:
#      TSTool-linux-13.03.00.dev-YYMMDDHHMM.run
timeStamp=$(date '+%y%m%d%H%M')
selfExtractingShFile="${tmpDistFolder}/tstool-linux-${tstoolVersion}-${timeStamp}.run"

echo ""
echo "Original TSTool files are in folder: '${installFolder}'"
echo "(optional) JRE files for Linux are in folder: '${jreFolder}'"
echo "Temporary TSTool distribution folder: '${tmpDistFolder}'"
echo "Temporary TSTool build folder: '${tmpBuildFolder}'"
echo "TSTool self-extracting makeself (sh) file: '${selfExtractingShFile}'"
echo ""

# Make sure input exists.
checkInput

# Copy the Windows TSTool files into Linux temporary folder.
copyWindowsFilesToLinux

# Run custom build steps, for example to package plugins, etc.
customBuildSteps

# Configure TSTool, such as disabling HydroBase.
configureTSTool

# Compress the install:
# - this is not used since makeself is now used
#tar -czvf $zipFile $buildFolder

# Create the installer with 'makeself'.
createMakeselfInstaller

# Exit script.
exit 0
