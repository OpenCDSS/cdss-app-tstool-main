#!/bin/bash
#
# Create the following files on GCP bucket:
#
#   opencdss.state.co.us/tstool/index.html
#   opencdss.state.co.us/tstool/index.csv
#
# The former is used as the default web page.
# The latter is used by TSTool to check versions when "Help / Check for Updates" is used.

# Supporting functions, alphabetized

# Determine the operating system that is running the script
# - sets the variable operatingSystem to cygwin, linux, or mingw (Git Bash)
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
			operatingSystemShort="cyg"
			;;
		LINUX*)
			operatingSystem="linux"
			operatingSystemShort="lin"
			;;
		MINGW*)
			operatingSystem="mingw"
			operatingSystemShort="min"
			;;
	esac
	echo ""
	echo "Detected operatingSystem=$operatingSystem operatingSystemShort=$operatingSystemShort"
	echo ""
}

# Check whether a file exists on GCP storage
# - function argument should be Google storage URL gs:opencdss... etc.
gcpUtilFileExists() {
	local fileToCheck
	fileToCheck=$1
	# The following will return 0 if the file exists, 1 if not
	gsutil.cmd -q stat $fileToCheck
	return $?
}

# Get the user's login.
# - Git Bash apparently does not set $USER environment variable
# - Set USER as script variable only if environment variable is not already set
# - See: https://unix.stackexchange.com/questions/76354/who-sets-user-and-username-environment-variables
getUserLogin() {
	if [ -z "$USER" ]; then
		if [ ! -z "$LOGNAME" ]; then
			USER=$LOGNAME
		fi
	fi
	if [ -z "$USER" ]; then
		USER=$(logname)
	fi
	# Else - not critical since used for temporary files
}

# Parse the command parameters
parseCommandLine() {
	while getopts :dhl opt; do
		#echo "Command line option is ${opt}"
		case $opt in
			d) # Don't clobber files on receiving end, essentially a dry run
				dryrun="-n"
				;;
			h) # Usage
				printUsage
				exit 0
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
	echo "Usage:  $0"
	echo ""
	echo "Create the opencdss.state.co.us/tstool/index.html file."
	echo ""
	echo "-d   Dry run (do not clobber files on GCP)."
	echo "-h   Print usage."
	echo ""
}

# Upload the index.html file for the static website download page
# - this is basic at the moment but can be improved in the future such as
#   software.openwaterfoundation.org page, but for only one product, with list of variants and versions
uploadIndexHtmlFile() {
	local indexHtmlTmpFile gcpIndexHtmlUrl
	local indexCsvTmpFile gcpIndexCsvlUrl
	# List available software installer files
	# - $gcpFolderUrl ends with /tstool
	# - the initial output will look like the following, with size, timestamp, resource URL:
	#
	# gs://opencdss.state.co.us/tstool/12.06.00/software/:
	#         11  2019-04-27T10:01:42Z  gs://opencdss.state.co.us/tstool/12.06.00/software/
	#   94612246  2019-04-27T10:01:42Z  gs://opencdss.state.co.us/tstool/12.06.00/software/TSTool_CDSS_12.06.00_Setup.exe
	#
	# gs://opencdss.state.co.us/tstool/latest/software/:
	#   94612246  2019-04-27T10:01:42Z  gs://opencdss.state.co.us/tstool/12.06.00/software/TSTool_CDSS_12.06.00_Setup.exe
	# TOTAL: 2 objects, 94612246 bytes (90.27 MiB)
	#
	#   after filtering, the output looks like the following:
	#
	# 94612246  2019-04-27T10:01:42Z  gs://opencdss.state.co.us/tstool/12.06.00/software/TSTool_CDSS-12.06.00_Setup.exe
	# 94612246  2019-04-27T10:01:47Z  gs://opencdss.state.co.us/tstool/latest/software/TSTool_CDSS-12.06.00_Setup.exe
	# TODO smalers 2019-04-29 need to use Bash PIPESTATUS for error code
	tmpGcpCatalogPath="/tmp/$USER-tstool-catalog-ls.txt"
	# Match exe and tar.gz files to include Windows and Linux
	gsutil.cmd ls -l "${gcpFolderUrl}/*/software" | grep -E -v '^gs*' | grep gs | grep -E 'exe|tar.gz' > $tmpGcpCatalogPath
	if [ "${PIPESTATUS[0]}" -ne 0 ]; then
		echo ""
		echo "[Error] Error listing TSTool download files to create catalog."
		exit 1
	fi
	# Create an index.html file for upload
	indexHtmlTmpFile="/tmp/$USER-tstool-index.html"
	gcpIndexHtmlUrl="${gcpFolderUrl}/index.html"
	# Also create index as CSV to facilitate listing available versions
	indexCsvTmpFile="/tmp/$USER-tstool-index.csv"
	gcpIndexCsvUrl="${gcpFolderUrl}/index.csv"
	echo '<!DOCTYPE html>' > $indexHtmlTmpFile
	echo '<head>' >> $indexHtmlTmpFile
	echo '<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />' >> $indexHtmlTmpFile
	echo '<meta http-equiv="Pragma" content="no-cache" />' >> $indexHtmlTmpFile
	echo '<meta http-equiv="Expires" content="0" />' >> $indexHtmlTmpFile
	echo '<meta charset="utf-8"/>' >> $indexHtmlTmpFile
	echo '<link id="cdss-favicon" rel="shortcut icon" href="http://opencdss.state.co.us/opencdss/images/opencdss-favicon.ico" type="image.ico">' >> $indexHtmlTmpFile
	echo '<style>' >> $indexHtmlTmpFile
	echo '   body { font-family: "Trebuchet MS", Helvetica, sans-serif !important; }' >> $indexHtmlTmpFile
	echo '   table { border-collapse: collapse; }' >> $indexHtmlTmpFile
	echo '   tr { border: none; }' >> $indexHtmlTmpFile
	echo '   th {' >> $indexHtmlTmpFile
	echo '     border-right: solid 1px;' >> $indexHtmlTmpFile
	echo '     border-left: solid 1px;' >> $indexHtmlTmpFile
	echo '     border-bottom: solid 1px;' >> $indexHtmlTmpFile
	echo '   }' >> $indexHtmlTmpFile
	echo '   td {' >> $indexHtmlTmpFile
	echo '     border-right: solid 1px;' >> $indexHtmlTmpFile
	echo '     border-left: solid 1px;' >> $indexHtmlTmpFile
	echo '   }' >> $indexHtmlTmpFile
	echo '</style>' >> $indexHtmlTmpFile
	echo '<title>OpenCDSS TSTool Downloads</title>' >> $indexHtmlTmpFile
	echo '</head>' >> $indexHtmlTmpFile
	echo '<body>' >> $indexHtmlTmpFile
	echo '<h1>TSTool Software Downloads</h1>' >> $indexHtmlTmpFile
	echo '<p>' >> $indexHtmlTmpFile
	echo '<a href="http://opencdss.state.co.us/opencdss/tstool/">See also the OpenCDSS TSTool page</a>, which provides additional information about TSTool.' >> $indexHtmlTmpFile
	echo '</p>' >> $indexHtmlTmpFile
	echo '<a href="http://www.colorado.gov/pacific/cdss/tstool">See also the CDSS TSTool page</a>, which provides access to TSTool releases used in State of Colorado projects.' >> $indexHtmlTmpFile
	echo '<p>' >> $indexHtmlTmpFile
	echo '</p>' >> $indexHtmlTmpFile
	echo '<p>' >> $indexHtmlTmpFile
	echo 'The TSTool software is available Windows 10 and Linux.' >> $indexHtmlTmpFile
	echo 'See the latest <a href="http://opencdss.state.co.us/tstool/latest/doc-user/appendix-install/install/">TSTool documentation</a> for installation information (or follow a link below for specific version documentation).' >> $indexHtmlTmpFile
	echo '</p>' >> $indexHtmlTmpFile
	echo '<p>' >> $indexHtmlTmpFile
	echo '<ul>' >> $indexHtmlTmpFile
	echo '<li>Multiple versions of TSTool can be installed on a computer.' >> $indexHtmlTmpFile
	echo '<li>Download files that include <code>dev</code> in the version are development versions that can be installed to test the latest features and bug fixes that are under development.</li>' >> $indexHtmlTmpFile
	echo '<li><b>If clicking on a file download link does not download the file, right-click on the link and use "Save link as..." (or similar).</b></li>' >> $indexHtmlTmpFile
	echo '</ul>' >> $indexHtmlTmpFile

	echo '<hr>' >> $indexHtmlTmpFile
	echo '<h2>Windows Download</h2>' >> $indexHtmlTmpFile
	echo '<p>' >> $indexHtmlTmpFile
	echo 'Install TSTool by downloading the executable setup file and then run using File Explorer.' >> $indexHtmlTmpFile
	echo 'Then run TSTool from the Windows Start / CDSS / TSTool-Version menu.' >> $indexHtmlTmpFile
	echo '</p>' >> $indexHtmlTmpFile
	# Generate a table of available versions for Windows
	uploadIndexHtmlFile_Table win Windows

	# TODO smalers 2019-04-29 need to enable downloads for other operating systems
	echo '<hr>' >> $indexHtmlTmpFile
	echo '<h2>Linux Download</h2>' >> $indexHtmlTmpFile
	echo '<p>' >> $indexHtmlTmpFile
	echo 'Install TSTool by downloading the *.run file, and run to install in /opt/tstool-version.' >> $indexHtmlTmpFile
	echo 'Then run tstool from the Linux command line.' >> $indexHtmlTmpFile
	echo '</p>' >> $indexHtmlTmpFile
	# Generate a table of available versions for Linux
	uploadIndexHtmlFile_Table lin Linux

	#echo '<hr>' >> $indexHtmlTmpFile
	#echo '<h2>Cygwin Download</h2>' >> $indexHtmlTmpFile
	#echo '<p>' >> $indexHtmlTmpFile
	#echo 'Install the GeoProcessor on Cygwin by downloading the <a href="download-gp.sh">download-gp.sh script</a> and running it in a shell window.' >> $indexHtmlTmpFile
	#echo 'You will be prompted for options for where to install the software.' >> $indexHtmlTmpFile
	#echo 'Once installed, run the GeoProcessor using scripts in the <code>scripts</code> folder under the install folder.' >> $indexHtmlTmpFile
	#echo '<b>Do not download directly using files below (the list is provided as information).</b>' >> $indexHtmlTmpFile
	#echo '</p>' >> $indexHtmlTmpFile
	## Generate a table of available versions for Cygwin
	#uploadIndexHtmlFile_Table cyg Cygwin

	echo '</body>' >> $indexHtmlTmpFile
	echo '</html>' >> $indexHtmlTmpFile
	# set -x
	gsutil.cmd cp $indexHtmlTmpFile $gcpIndexHtmlUrl
	# { set +x; } 2> /dev/null
	if [ "${PIPESTATUS[0]}" -ne 0 ]; then
		echo ""
		echo "[Error] Error uploading index.html file."
		exit 1
	fi

	# Similarly create the index.csv file

	# First create the index
	# - use some string replacements to make sure that dev versions are listed after non-dev release
	#   with the same number is listed first
	echo '"Version"' > $indexCsvTmpFile
	cat $tmpGcpCatalogPath | awk '{print $3}' | cut -d '/' -f 5 | sed -E 's/([0-9]$)/\1-zzz/g' | sed 's/0dev/0-dev/g' | sort -r | sed 's/-zzz//g' >> $indexCsvTmpFile
	# Then upload to the website
	gsutil.cmd cp $indexCsvTmpFile $gcpIndexCsvUrl
	if [ "${PIPESTATUS[0]}" -ne 0 ]; then
		echo ""
		echo "[Error] Error uploading index.csv file."
		exit 1
	fi
}

# Create a table of downloads for an operating system to be used in the index.html file.
# - first argument is operating system short name to match installers:  "win", "lin", or "cyg"
# - second argument is operating system long name to match installers:  "Windows", "Linux", or "Cygwin"
uploadIndexHtmlFile_Table() {
	local downloadOs dowloadPattern
	local indexHtmlTmpCatalogFile
	# Operating system is passed in as the required first argument
	downloadOs=$1
	downloadOsLong=$2
	# The following allows sorting the list in reverse order
	indexHtmlTmpCatalogFile="/tmp/$USER-tstool-catalog-${downloadOs}.html"
	if [ "${downloadOs}" = "win" ]; then
		downloadPattern="exe"
	elif [ "${downloadOs}" = "lin" ]; then
		downloadPattern="tar.gz"
	fi
	echo "downloadPattern=$downloadPattern"
	echo '<table>' >> $indexHtmlTmpFile
	# List the available download files
	# Listing local files does not show all available files on GCP but may be useful for testing
	catalogSource="gcp"  # "gcp" or "local"
	if [ "$catalogSource" = "gcp" ]; then
		# Use GCP list from catalog file for the index.html file download file list, with format like
		# the following (no space at beginning of the line):
		#
		# 12464143  2019-04-27T10:01:42Z  gs://opencdss.state.co.us/tstool/12.06.00/software/TSTool_CDSS_12.06.00_Setup.exe
		#
		# Use awk below to print the line with single space between tokens.
		# Replace normal version to have -zzz at end and "dev" version to be "-dev" so that sort is correct,
		#   then change back to previous strings for output.
		# The use space as the delimiter and sort on the 3rd token.
		#
		echo '<tr><th>Download File</th><th>Product</th><th>Version</th><th>File Timestamp</th><th>Size (KB)</th><th>Operating System</th><th>User Doc</th><th>Dev Doc</th><th>API Doc</th></tr>' >> $indexHtmlTmpFile
		cat "${tmpGcpCatalogPath}" | grep "${downloadPattern}" | awk '{ printf "%s %s %s\n", $1, $2, $3 }' | sed -E 's|([0-9][0-9]/)|\1-zzz|g' | sed 's|/-zzz|-zzz|g' | sed 's|dev|-dev|g' | sort -r -k3,3 | sed 's|-zzz||g' | sed 's|-dev|dev|g' | awk '
			{
				# Download file is the full line
				downloadFileSize = $1
				downloadFileDateTime = $2
				downloadFilePath = $3
				# Split the download file path into parts to get the download file without path
				# - index is 1+
				nparts=split(downloadFilePath,downloadFilePathParts,"/")
				downloadFile = downloadFilePathParts[nparts]
				downloadFileUrl=downloadFilePath
				gsub("gs:","http:",downloadFileUrl)
				# Split the download file into parts to get other information
				# - index is 1+
				split(downloadFile,downloadFileParts,"_")
				downloadFileProduct=downloadFileParts[1]
				downloadFileVersion=downloadFileParts[3]
				#downloadFileCompiler=downloadFileParts[3]
				if ( substr(downloadFile,length(downloadFile) - 3 + 1) == "exe") {
					downloadFileOs="Windows"
				}
				else if ( substr(downloadFile,length(downloadFile) - 6 + 1) == "tar.gz" ) {
					downloadFileOs="Linux"
				}
				# Currently always 32-bit but 64-bit will be added
				# downloadFileArch=downloadFileParts[5]
				# Documentation links for development and user documentation are only shown if exist
				# - the file returned by curl is actually the index.html file
				docDevUrl=sprintf("http://opencdss.state.co.us/tstool/%s/doc-dev",downloadFileVersion)
				docDevCurl=sprintf("curl --output /dev/null --silent --head --fail \"%s\"",docDevUrl)
				returnStatus=system(docDevCurl)
				if ( returnStatus == 0 ) {
					docDevHtml=sprintf("<a href=\"%s\">View</a>",docDevUrl)
				}
				else {
					docDevHtml=""
				}
				docUserUrl=sprintf("http://opencdss.state.co.us/tstool/%s/doc-user",downloadFileVersion)
				docDevCurl=sprintf("curl --output /dev/null --silent --head --fail \"%s\"",docUserUrl)
				returnStatus=system(docDevCurl)
				if ( returnStatus == 0 ) {
					docUserHtml=sprintf("<a href=\"%s\">View</a>",docUserUrl)
				}
				else {
					docUserHtml=""
				}
				docApiUrl=""
				#if ( downloadFileOs == "cyg" ) {
				#	downloadFileOs = "Cygwin"
				#}
				#else if ( downloadFileOs == "lin" ) {
				#	downloadFileOs = "Linux"
				#}
				#else if ( downloadFileOs == "win" ) {
				#	downloadFileOs = "Windows"
				#}
				printf "<tr><td><a href=\"%s\"><code>%s</code></a></td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>\n", downloadFileUrl, downloadFile, downloadFileProduct, downloadFileVersion, downloadFileDateTime, downloadFileSize, downloadFileOs, docUserHtml, docDevHtml, docApiUrl
			}' >> $indexHtmlTmpFile
	fi
	echo '</table>' >> $indexHtmlTmpFile
}

# Entry point for the script

# Get the location where this script is located since it may have been run from any folder
scriptFolder=`cd $(dirname "$0") && pwd`
repoFolder=$(dirname "$scriptFolder")
srcFolder="$repoFolder/src"

echo "scriptFolder=$scriptFolder"
echo "repoFolder=$repoFolder"
echo "srcFolder=$srcFolder"

dryrun=""

# Root location where files are to be uploaded
gcpFolderUrl="gs://opencdss.state.co.us/tstool"

parseCommandLine

getUserLogin

uploadIndexHtmlFile

exit $?
