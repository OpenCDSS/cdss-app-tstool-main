#!/bin/bash
#
# setup-linux-tstool.sh - install TSTool from makeself-created installer
#
# Can run as root (sudo) if installing in system location like /opt.
# Or run as normal user for user software install.

# Check whether the script is being run as root:
# - need to run as root to install software in system folders
# - running 'sudo whomami' as a normal user shows "root"
# - the SUDO_USER environment variable holds the user that ran sudo command
checkSudo() {
  installUser=$(whoami)
  if [ "${installUser}" != "root" ]; then
    echo ""
    echo "You are not running as root.  To install in system location such as /opt:"
    echo "Run with:          sudo ${scriptName}"
    echo "or, if necessary:  sudo ./${scriptName}"
    echo ""
  else
    echo ""
    echo "You are running as root or sudo."
    echo "There should be no permissions issues installing in system folders such as /opt."
    echo ""
  fi
  echo "CTRL-c now or quit using 'q' in later steps if need to restart the process."
  echo "Continuing in 5 seconds."
  sleep 5
}

# Create symbolic links:
# - For example: /usr/bin/tstool -> /opt/tstool-13.00.00/bin/tstool
createSymbolicLinks() {
  local installUser

  installUser=$(whoami)

  echo ""
  if [ "${installUser}" = 'root' ]; then
    echo "Create symbolic link /usr/bin/tstool -> ${installFolder}/bin/tstool?"
    echo "This will ensure that 'tstool' is found in the PATH for all users on the system."
    read -p "Create link [Y/n/q]? " answer
    if [ "${answer}" = "q" ]; then
      exit 0
    elif [ "${answer}" = "n" ]; then
      break
    elif [ -z "${answer}" -o "${answer}" = "y" -o "${answer}" = "Y" ]; then
      # Default action.
      ln -sf ${installFolder}/bin/tstool /usr/bin/tstool
      if [ $? -ne 0 ]; then
        echo "Error creating symbolic link."
      fi
    fi
  else
    echo "Create symbolic link ${HOME}/bin/tstool -> ${installFolder}/bin/tstool?"
    echo "This will ensure that the latest 'tstool' is found in the PATH for ${USER}."
    read -p "Create link [Y/n/q]? " answer
    if [ "${answer}" = "q" -o "${answer}" = "Q" ]; then
      exit 0
    elif [ -z "${answer}" -o "${answer}" = "y" -o "${answer}" = "Y" ]; then
      # Default action.
      ln -sf ${installFolder}/bin/tstool ${HOME}/bin/tstool
      if [ $? -ne 0 ]; then
        echo "Error creating symbolic link."
      fi
    fi
  fi
}

# Install the files:
# - copy the temporary files to install folder
installFiles() {
  local answer installFolderParent
  # Create the parent folder for the install.
  installFolderParent=$(dirname ${installFolder})
  if [ ! -d "${installFolderParent}" ]; then
    echo "Creating install parent folder:  ${installFolderParent}"
    mkdir -p ${installFolderParent}
    if [ $? -ne 0 ]; then
      echo "Error creating parent folder for install:  ${installFolderParent}"
      exit 1
    fi
  fi
  # First remove existing folder so new files are all current.
  while true; do
    if [ -d "${installFolder}" ]; then
      echo "Install folder exists:  ${installFolder}"
      echo "Select Y (default) to do a clean install."
      echo "Select n to install over the existing version"
      echo "  and keep previously installed plugins and datastore configurations."
      read -p "Remove before reinstalling [Y/n/q]? " answer
      if [ "${answer}" = "q" ]; then
        exit 0
      elif [ -z "${answer}" -o "${answer}" = "y" -o "${answer}" = "Y" ]; then
        # Default action.
        rm -rf ${installFolder}
      elif [ "${answer}" = "n" -o "${answer}" = "N" ]; then
        # Continue with install without removing.
        break
      fi
    else
      # Install folder does not exist so no need for decision about removing.
      break
    fi
  done
  # Copy the files:
  # - make sure the parent folder of the install folder exists
  installFolderParent=$(dirname ${installFolder})
  if [ ! -d "${installFolderParent}" ]; then
    echo "Creating parent of install folder:"
    mkdir -vp "${installFolderParent}"
  fi
  # Copy to the parent folder to make sure that it always works without
  # adding a redundant installation folder.
  echo "Copying files to:  ${installFolder}"
  cp -r ${tmpFolderWithPath} ${installFolderParent}
  if [ $? -ne 0 ]; then
    echo ""
    echo "Error copying files to installation folder."
    echo "  From: ${tmpFolderWithPath}"
    echo "    To: ${installFolder}"
    exit 1
  fi
  # Make sure permissions are as needed:
  # - not sure why they get changed oddly
  # - make all files rw by owner and group and r by other (mode xxx)
  # - make all folders additionally be executable by all
  chmod 0755 ${installFolderParent}
  find ${installFolder} -type f | xargs chmod 0664
  find ${installFolder} -type d | xargs chmod 0755
  chmod a+x ${installFolder}/bin/tstool

  # Remove unneeded files since these files are only used on Windows to create Linux installer:
  # - need to check for existence because installing over an existing install will warn
  if [ -f "${installFolder}/bin/build-linux-distro.bash" ]; then
    rm ${installFolder}/bin/build-linux-distro.bash
  fi
  if [ -f "${installFolder}/bin/setup-linux-tstool.bash" ]; then
    rm ${installFolder}/bin/setup-linux-tstool.bash
  fi

  echo "Finished installing TSTool files."
}

# Parse the command line and set variables to control logic.
parseCommandLine() {
  # Indicate specification for single character options:
  # - 1 colon after an option indicates that an argument is required
  # - 2 colons after an option indicates that an argument is optional, must use -o=argument syntax
  optstring="hv"
  # Indicate specification for long options:
  # - 1 colon after an option indicates that an argument is required
  # - 2 colons after an option indicates that an argument is optional, must use --option=argument syntax
  optstringLong="help,installdefault:,installhint:,version"
  # Parse the options using getopt command:
  # - the -- is a separator between getopt options and parameters to be parsed
  # - output is simple space-delimited command line
  # - error message will be printed if unrecognized option or missing parameter but status will be 0
  # - if an optional argument is not specified, output will include empty string ''
  GETOPT_OUT=$(getopt --options ${optstring} --longoptions ${optstringLong} -- "$@")
  exitCode=$?
  if [ ${exitCode} -ne 0 ]; then
    echo ""
    printUsage
    exit 1
  fi
  # The following constructs the command by concatenating arguments:
  # - the $1, $2, etc. variables are set as if typed on the command line
  # - special cases like --option=value and missing optional arguments are generically handled
  #   as separate parameters so shift can be done below
  eval set -- "${GETOPT_OUT}"
  # Loop over the options:
  # - the error handling will catch cases were argument is missing
  # - shift over the known number of options/arguments
  while true; do
    #echo "Command line option is $opt"
    case "$1" in
      -h|--help) # -h or --help  Print usage
        printUsage
        exit 0
        ;;
      --installdefault)
        case "$2" in
          "") # Nothing specified so error
            echoStderr "--installdefault=folder is missing folder."
            exit 1
            ;;
          *)
            installdefault=$2
            #logDebug "Detected using install default folder: ${installdefault}"
            shift 2
            ;;
        esac
        ;;
      --installhint)
        case "$2" in
          "") # Nothing specified so error
            echoStderr "--installhint=hint is missing hint."
            exit 1
            ;;
          *)
            installhint=$2
            #logDebug "Detected using install hint: ${installhint}"
            shift 2
            ;;
        esac
        ;;
      -v|--version) # -v or --version  Print the version
        printVersion
        exit 0
        ;;
      --) # No more arguments
        shift
        break
        ;;
      *) # Unknown option - will never get here because getopt catches up front
        echo ""
        echo "Invalid option $1." >&2
        printUsage
        exit 1
        ;;
    esac
  done
  # Get a list of all command line options that do not correspond to dash options:
  # - These are "non-option" arguments.
  # - For example, one or more file or folder names that need to be processed.
  # - If multiple values, they will be delimited by spaces.
  # - Command line * will result in expansion to matching files and folders.
  shift $((OPTIND-1))
  additionalOpts=$*
}

# Print the program usage:
# - calling code needs to exit with the appropriate status
printUsage() {
  echo ""
  echo "${scriptName} [makeself-options] -- [options]"
  echo ""
  echo "Install TSTool software on Linux from self-extracting executable."
  echo "The installer was created with 'makeself'."
  echo ""
  echo "--------------------------------------------------------------------------------------------------"
  echo "TSTool installer specific 'options' that must follow -- are:"
  echo ""
  echo "-h, --help               Print help."
  echo "--installdefault=ENVVAR  Indicate the default installation folder,"
  echo "                         using an environment variable."
  echo "--installhint=ENVVAR     Provide a hint to print for the suggested installation folder,"
  echo "                         using an environment variable."
  echo "-v, --version            Print version."
  echo ""
  echo "--------------------------------------------------------------------------------------------------"
  echo "'makeself-options' that the makeself-constructed installer recognizes (see: https://makeself.io/):"
  echo ""
  echo "--check         Check the archive for integrity using the embedded checksums,"
  echo "                without extracting."
  echo "--confirm       Prompt the user for confirmation before extracting the archive and running command."
  # Not in GitHub page but in code.
  echo "--dumpconf      Print configuration information for makeself install script."
  echo "--help          Print usage for makeself-generated installer."
  echo "--info          Print out general information about the archive (does not extract)."
  echo "--keep          Prevent extracted files in temporary folder (current folder) from being removed."
  echo "                Normally a unique folder in /tmp is created to extract files and is then removed."
  # Not in GitHub page but in code.
  echo "--list          List the files in the archive and exit without uncompressing or running command."
  echo "--lsm           Print out the LSM entry, if it is present."
  echo "--nochown       By default 'chown -R' is run on the install folder so all files are owned"
  echo "                by the user running the installer.  Disable this operation."
  echo "--nodiskspace   Do not run for disk space before attempting to extract."
  echo "--noexec        Do not run the embedded script after extraction."
  # Not in GitHub page but in code.
  echo "--noprogress    Do not show progress when uncompressing files."
  echo "--nox11         Do not spawn an X11 terminal."
  # Not in GitHub page but in code.
  echo "--quiet         Only print error messages."
  echo "--tar opts      Run the tar command on the contents of the archive with remaining options used for tar."
  # In GitHub page but not in code?
  echo "--verbose       Verbose output (not enabled?)."
  # The following will be suggested if cannot write to current folder.
  echo "--target dir    Extract the archive to an arbitrary place."
  echo "--------------------------------------------------------------------------------------------------"
  echo ""
}

# Prompt for the install folder.
promptForInstallFolder() {
  local answer installUser

  echo ""
  installUser=$(whoami)
  while true; do
    echo ""
    echo "Specify the installation folder for TSTool."
    if [ -n "${installhint}" ]; then
      # Check for environment variable specified by the --installhint parameter for a hint to print,
      # used because passing hint on command line with whitespace can be challenging.
      # TODO smalers 2021-11-11 the following works in bash:
      installhintenv="${!installhint}"
      # The following works for sh:
      #installhintenv=$(eval "echo \"\$${installhint}\"")
      if [ -n "${installhintenv}" ]; then
        echo "${installhintenv}"
      fi
    fi
    if [ -n "${installdefault}" ]; then
      # Check for environment variable specified by the --installdefault for default install folder,
      # used because passing hint on command line with whitespace can be challenging.
      # TODO smalers 2021-11-11 the following works in bash:
      installdefaultenv="${!installdefault}"
      # The following works for sh:
      #installdefaultenv=$(eval "echo \"\$${installdefault}\"")
    else
      # Use the normal default.
      if [ "${installUser}" = 'root' ]; then
        echo "Default system install folder (since running as root):  ${defaultSystemInstallFolder}"
      else
        echo "Default user install folder (since running as ${installUser}):  ${defaultUserInstallFolder}"
      fi
    fi
    echo "Install folder [return to use default/q to quit]:"
    # TODO smalers 2021-11-12 evaluate using read -e for file completion.
    read answer
    if [ -z "${answer}" -o "${answer}" = "" ]; then
      if [ "${installUser}" = 'root' ]; then
        installFolder=${defaultSystemInstallFolder}
      else
        installFolder=${defaultUserInstallFolder}
      fi
      if [ -n "${installdefaultenv}" ]; then
        # Use the custom default.
        installFolder="${installdefaultenv}"
      fi
      break
    elif [ "${answer}" = "q" ]; then
      exit 0
    else
      # User specified the folder.
      installFolder=${answer}
      # Because this is 'sh' and not 'bash', replace ~ with home folder in case user has used ~.
      installFolder=$(echo ${installFolder} | sed "s#~#${HOME}#")
      break
    fi
  done
}

# Remove other TSTool versions if installed:
# - currently does nothing
removeOtherVersions() {
  echo "Other TSTool versions (if installed) are left on the system where previously installed."
}

# Main entry point into script.

# Folder where tstool script is located, should be the "bin" folder in temporary folder:
# - something like:  /tmp/selfgz23660/tstool-13.00.00/bin
scriptFolder=$(cd $(dirname "$0") && pwd)
scriptName=$(basename $0)
# Temporary folder under which software files are located:
# - something like:  /tmp/selfgz23660/tstool-13.00.00
tmpFolderWithPath=$(dirname ${scriptFolder})
# Temporary folder without path.
tmpFolderWithoutPath=$(basename ${tmpFolderWithPath})
# Default install folders.
defaultSystemInstallFolder="/opt/${tmpFolderWithoutPath}"
defaultUserInstallFolder="${HOME}/${tmpFolderWithoutPath}"
# Install hint and default folder for custom installations.
installdefault=""
installhint=""

echo "Setup script folder: ${scriptFolder}"
echo "Tmp install folder: ${tmpFolder}"
echo "Files in ${tmpFolder}:"
ls -1 ${tmpFolder}
echo "The following is the usage for ${scriptName} (note options after --)."
printUsage

# Check whether running as sudo.
checkSudo

# Parse the command line:
# - defaults are used in most cases except during development and troubleshooting
parseCommandLine $@

# Prompt for the location to install TSTool.
promptForInstallFolder

# Install files.
installFiles

# Remove other (including older versions).
removeOtherVersions

# Create symbolic links.
createSymbolicLinks

# Explain how to run.
echo ""
echo "Run TSTool with one of the following:"
echo "  ${installFolder}/bin/tstool"
echo "  ~/bin/tstool"
if [ "${installUser}" = 'root' ]; then
  # Installing as root.
  if [ -L "/usr/bin/tstool" ]; then
    echo "  tstool"
  fi
else
  # Installing as a normal user.
  if [ -L "${HOME}/${SUDO_USER}/bin/tstool" ]; then
    echo "  tstool"
  fi
fi
