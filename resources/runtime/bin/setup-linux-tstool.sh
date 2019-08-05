#!/bin/sh
#
# setup-linux-tstool.sh - install TSTool from makeself-created installer
#
# Can run as root (sudo) if installing in system location like /opt.
# Or run as normal user for user software install.

# Check whether the script is being run as root
# - Need to run as root to install software in system folders
# - Running 'sudo whomami' as a normal user shows "root"
# - The SUDO_USER environment variable holds the user that ran sudo command
checkSudo() {
	user=`whoami`
	if [ "$user" != "root" ]; then
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
	echo "Continuing in 5 seconds (CTRL-c if you want to read usage above)..."
	sleep 5
}

# Create symbolic links
# - For example: /usr/bin/tstool -> /opt/tstool-13.00.00/bin/tstool
createSymbolicLinks() {
	# TODO smalers 2019-07-30 Need to only do if installed to system folder
	while [ "1" = "1" ]; do
		echo ""
		echo "Create symbolic link /usr/bin/tstool -> ${installFolder}/bin/tstool?"
		echo "This will ensure that 'tstool' is found in the PATH."
		read -p "Create link [Y/n/q]? " answer
		if [ "$answer" = "q" ]; then
			exit 0
		elif [ "$answer" = "n" ]; then
			break
		elif [ -z "${answer}" -o "${answer}" = "y" -o "${answer}" = "Y" ]; then
			# Default action
			ln -sf ${installFolder}/bin/tstool /usr/bin/tstool
			if [ $? -ne 0 ]; then
				${echo2} "${warnColor}Error creating symbolic link.${endColor}"
			fi
			break
		fi
	done
}

# Install the files
# - copy the temporary files to install folder
installFiles() {
	local answer installFolderParent
	# Create the parent folder for the install
	installFolderParent=$(dirname $installFolder)
	if [ ! -d "$installFolderParent" ]; then
		echo "Creating parent folder:  $installFolderParent"
		mkdir -p $installFolderParent
		if [ $? -ne 0 ]; then
			echo "Error creating parent folder for install:  $installFolderParent"
			exit 1
		fi
	fi
	# First remove existing folder so new files are all current
	while [ "1" = "1" ]; do
		if [ -d "${installFolder}" ]; then
			echo "Install folder exists:  ${installFolder}"
			read -p "Remove before reinstalling [Y/q]? " answer
			if [ "${answer}" = "q" ]; then
				exit 0
			elif [ -z "${answer}" -o "${answer}" = "y" -o "${answer}" = "Y" ]; then
				# Default action
				rm -rf ${installFolder}
			fi
		else
			# Folder does not exist
			break
		fi
	done
	# Copy the files
	echo "Copying files to:  ${installFolder}"
	cp -r ${tmpFolderWithPath} ${installFolder}
	if [ $? -ne 0 ]; then
		echo ""
		echo "Error copying files to installation folder."
		echo "  From: ${tmpFolderWithPath}"
		echo "    To: ${installFolder}"
		exit 1
	fi
	# Make sure permissions are as needed
	# - not sure why they get changed oddly
	# - make all files rw by owner and group and r by other (mode xxx)
	# - make all folders additionally be executable by all
	find ${installFolder} -type f | xargs chmod 0664
	find ${installFolder} -type d | xargs chmod 0755
	chmod a+x ${installFolder}/bin/tstool

	# Remove unneeded files
	rm ${installFolder}/bin/build-linux-distro.bash
	rm ${installFolder}/bin/setup-linux-tstool.sh
}

# Parse the command line and set variables to control logic
parseCommandLine() {
	# Indicate specification for single character options
	# - 1 colon after an option indicates that an argument is required
	# - 2 colons after an option indicates that an argument is optional, must use -o=argument syntax
	optstring="hv"
	# Indicate specification for long options
	# - 1 colon after an option indicates that an argument is required
	# - 2 colons after an option indicates that an argument is optional, must use --option=argument syntax
	optstringLong="help,version"
	# Parse the options using getopt command
	# - the -- is a separator between getopt options and parameters to be parsed
	# - output is simple space-delimited command line
	# - error message will be printed if unrecognized option or missing parameter but status will be 0
	# - if an optional argument is not specified, output will include empty string ''
	GETOPT_OUT=$(getopt --options $optstring --longoptions $optstringLong -- "$@")
	exitCode=$?
	if [ $exitCode -ne 0 ]; then
		echo ""
		printUsage
		exit 1
	fi
	# The following constructs the command by concatenating arguments
	# - the $1, $2, etc. variables are set as if typed on the command line
	# - special cases like --option=value and missing optional arguments are generically handled
	#   as separate parameters so shift can be done below
	eval set -- "$GETOPT_OUT"
	# Loop over the options
	# - the error handling will catch cases were argument is missing
	# - shift over the known number of options/arguments
	while true; do
		#echo "Command line option is $opt"
		case "$1" in
			-h|--help) # -h or --help  Print usage
				printUsage
				exit 0
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
	# Get a list of all command line options that do not correspond to dash options.
	# - These are "non-option" arguments.
	# - For example, one or more file or folder names that need to be processed.
	# - If multiple values, they will be delimited by spaces.
	# - Command line * will result in expansion to matching files and folders.
	shift $((OPTIND-1))
	additionalOpts=$*
}

# Print the program usage
# - calling code needs to exit with the appropriate status
printUsage() {
	echo ""
	echo "$scriptName [makeself-options] -- [options]"
	echo ""
	echo "Install TSTool software on Linux from self-extracting executable."
	echo "The installer was created with 'makeself', which provides options."
	echo "Additinal options are provided but must follow -- on the command line."
	echo ""
	echo "Options are:"
	echo ""
	echo "-h, --help      Print help."
	echo "-v, --version   Print version."
	echo ""
	echo "makeself options that makeself-constructed installer recognizes (see: https://makeself.io/):"
	echo ""
	echo "--check         Check the archive for integrity using the embedded checksums,"
	echo "                without extracting."
	echo "--confirm       Prompt the user for confirmation before extracting the archive and running command."
	# Not in GitHub page but in code
	echo "--dumpconf      Print configuration information for makeself install script."
	echo "--help          Print usage for makeself-generated installer."
	echo "--info          Print out general information about the archive (does not extract)."
	echo "--keep          Prevent extracted files in temporary folder (current folder) from being removed."
	echo "                Normally a unique folder in /tmp is created to extract files and is then removed."
	# Not in GitHub page but in code
	echo "--list          List the files in the archive and exit without uncompressing or running command."
	echo "--lsm           Print out the LSM entry, if it is present."
	echo "--nochown       By default 'chown -R' is run on the install folder so all files are owned"
	echo "                by the user running the installer.  Disable this operation."
	echo "--nodiskspace   Do not run for disk space before attempting to extract."
	echo "--noexec        Do not run the embedded script after extraction."
	# Not in GitHub page but in code
	echo "--noprogress    Do not show progress when uncompressing files."
	echo "--nox11         Do not spawn an X11 terminal."
	# Not in GitHub page but in code
	echo "--quiet         Only print error messages."
	echo "--tar opts      Run the tar command on the contents of the archive with remaining options used for tar."
	# In GitHub page but not in code?
	echo "--verbose       Verbose output (not enabled?)."
	# The following will be suggested if cannot write to current folder
	echo "--target dir    Extract the archive to an arbitrary place."
	echo ""
}

# Prompt for the install folder
promptForInstallFolder() {
	local answer
	echo ""
	while [ "1" = "1" ]; do
		echo ""
		echo "Indicate the installation folder."
		echo "Default is:  ${defaultInstallFolder}"
		echo "Install folder [return to use default/q]:"
		read answer
		if [ -z "$answer" -o "$answer" = "" ]; then
			installFolder=${defaultInstallFolder}
			break
		elif [ "$answer" = "q" ]; then
			exit 0
		else
			# User-specified the folder
			installFolder=$answer
			break
		fi
	done
}

# Remove other TSTool versions if installed
# - currently does nothing
removeOtherVersions() {
	:
}

# Main entry point into script

# Folder where tstool script is located, should be the "bin" folder in temporary folder
# - something like:  /tmp/selfgz23660/tstool-13.00.00/bin
scriptFolder=$(cd $(dirname "$0") && pwd)
scriptName=$(basename $0)
# Temporary folder under which software files are located
# - something like:  /tmp/selfgz23660/tstool-13.00.00
tmpFolderWithPath=$(dirname $scriptFolder)
# Temporary folder without path
tmpFolderWithoutPath=$(basename $tmpFolderWithPath)
# Default install folder
defaultInstallFolder="/opt/${tmpFolderWithoutPath}"

echo "Setup script folder: ${scriptFolder}"
echo "Tmp install folder: ${tmpFolder}"
echo "Files in ${tmpFolder}:"
ls -1 ${tmpFolder}
echo "The following is the usage for ${scriptName} (note options after --)."
printUsage

# Check whether running as sudo
checkSudo

# Parse the command line
# - defaults are used in most cases except during development and troubleshooting
parseCommandLine $@

# Prompt for the location to install TSTool
promptForInstallFolder

# Install files
installFiles

# Remove other (including older versions)
removeOtherVersions

# Create symbolic links
createSymbolicLinks

# Explain how to run
echo ""
echo "Run TSTool with:  ${installFolder}/bin/tstool"
if [ -L "/usr/bin/tstool" ]; then
	echo "or run TSTool with:  tstool"
fi
