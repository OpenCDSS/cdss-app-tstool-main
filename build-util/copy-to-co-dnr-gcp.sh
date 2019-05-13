#!/bin/sh
#
# Copy the TSTool installer to the CO DNR GCP website
# - replace all the files on the web with local files

# Supporting functions, alphabetized.

# Determine the operating system that is running the script
# - mainly care whether Cygwin
checkOperatingSystem()
{
	if [ ! -z "${operatingSystem}" ]; then
		# Have already checked operating system so return
		return
	fi
	operatingSystem="unknown"
	os=`uname | tr [a-z] [A-Z]`
	case "${os}" in
		CYGWIN*)
			operatingSystem="cygwin"
			;;
		LINUX*)
			operatingSystem="linux"
			;;
		MINGW*)
			operatingSystem="mingw"
			;;
	esac
	echo "operatingSystem=$operatingSystem (used to check for Cygwin and filemode compatibility)"
}

# Get the version modifier:
# - for example, from "12.00.00dev", "12.00.00 dev", 12.00.00beta", or "12.00.00 beta"
# - the first function argument is the full version, possibly including a space
# - the modifier is echoed, so capture by assigning in the calling code
getVersionModifier() {
	local fullVersion
	fullVersion="$1"
	# grep will print each found character on a separate line so concatenate output
	modifier=$(echo $fullVersion | grep -o -E '[[:alpha:]]' | tr -d '\n' | tr -d ' ')
	echo $modifier
}

# Parse the command parameters
parseCommandLine() {
	local d h l opt
	while getopts :dhl opt; do
		#echo "Command line option is ${opt}"
		case $opt in
			d) # Indicate that this should be copied to the latest release and version
				dryrun="-n"
				;;
			h) # Usage
				printUsage
				exit 0
				;;
			l) # Indicate that this should be copied to the latest release and version
				copyToLatest="yes"
				;;
			\?)
				echo "Invalid option:  -$OPTARG" >&2
				exit 1
				;;
			:)
				echo "Option -$OPTARG requires an argument" >&2
				exit 1
				;;
		esac
	done
}

# Print the usage
printUsage() {
	echo ""
	echo "Usage:  $scriptName"
	echo ""
	echo "Copy the TSTool installer to the latest website folder if -l specified:  $gsFolderLatest"
	echo "Copy the TSTool installer to the versioned website folder:  $gsFolderVersion"
	echo "A version with 'dev' or 'beta' in the filename cannot be copied to latest."
	echo ""
	echo "-d dry run (print actions but don't copy over existing files)"
	echo "-h print usage"
	echo "-l copy to 'latest' folder in addition to auto-detected version folder"
	echo ""
}

# Sync the files to GCP
syncFiles() {
	# Copy the local files up to Google Cloud
	# - the -m option causes operations to run in parallel, which can be much faster
	# - the -d option means delete extra files in destination
	# - the -r option means recursive to sync the whole folder tree
	if [ ${copyToLatest} = "yes" ]; then
		if [ -f "$installerFile32" ]; then
			gsutil.cmd cp ${dryrun} $installerFile32 ${gsFileLatest32}
		else
			echo "File does not exist for 'latest' upload:  $installerFile32"
		fi
	fi
	# For now always upload to the versioned copy
	if [ -f "$installerFile32" ]; then
		echo "gsutil.cmd cp ${dryrun} $installerFile32 ${gsFileVersion32}"
		gsutil.cmd cp ${dryrun} $installerFile32 ${gsFileVersion32}
	else
		echo "File does not exist for versioned upload:  $installerFile32"
	fi
}

# Entry point for the script

# Check the operating system
checkOperatingSystem

# Get the location where this script is located since it may have been run from any folder
scriptFolder=`cd $(dirname "$0") && pwd`
scriptName=$(basename $0)
repoFolder=$(dirname "$scriptFolder")
srcFolder="$repoFolder/src"
srcMainFolder="${srcFolder}/DWR/DMI/tstool"
tstoolFile="${srcMainFolder}/TSToolMain.java"
if [ -f "${tstoolFile}" ]; then
	tstoolVersion=$(cat ${tstoolFile} | grep -m 1 'PROGRAM_VERSION' | cut -d '=' -f 2 | cut -d '(' -f 1 | tr -d " " | tr -d '"')
	tstoolModifierVersion=$(getVersionModifier "$tstoolVersion")
else
	echo "Cannot determine TSTool version because file not found:  ${tstoolFile}"
	exit 1
fi
if [ -z "${tstoolVersion}" ]; then
	echo "Cannot determine TSTool version by scanning:  ${tstoolFile}"
	exit 1
fi

echo "scriptFolder=$scriptFolder"
echo "repoFolder=$repoFolder"
echo "srcFolder=$srcFolder"
echo "srcMainFolder=$srcMainFolder"
echo "tstoolVersion=$tstoolVersion"

dryrun=""
gsFolderLatest="gs://opencdss.state.co.us/tstool/latest/software"
# Use historical installer file name
# - might change this to follow other software, 32/64 bit, etc.
gsFolderVersion="gs://opencdss.state.co.us/tstool/${tstoolVersion}/software"
if [ "$operatingSystem" = "mingw" ]; then
	gsFileLatest32="$gsFolderLatest/TSTool_CDSS_${tstoolVersion}_Setup.exe"
	gsFileVersion32="$gsFolderVersion/TSTool_CDSS_${tstoolVersion}_Setup.exe"
else
	echo ""
	echo "Don't know how to handle operating system:  $operatingSystem"
	exit 1
fi

# Whether to copy to latest in addition to the specific version
# - default to no because the script can be run on any version, and can't assume latest
copyToLatest="no"

# Parse the command line
parseCommandLine $@

if [ ! -z "${tstoolVersionModifier}" -a "$copyToLatest" = "yes" ]; then
	# The version contains "dev" or "beta" so don't allow to be used for "latest"
	echo "TSTool version $tstoolVersion contains modifier $tstoolVersionModifier- not copying to latest."
	copyToLatest="no"
fi

# Sync the files to the cloud
installerFile32="$repoFolder/dist/TSTool_CDSS_${tstoolVersion}_Setup.exe"
syncFiles

exit $?
