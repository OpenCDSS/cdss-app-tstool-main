#!/bin/bash
#
# Create the following files on GCP bucket:
#
#   opencdss.state.co.us/tstool/index.html
#   opencdss.state.co.us/tstool/index.csv
#
# The former is used as the default web page.
# The latter is used by TSTool to check versions when "Help / Check for Updates" is used.

# Supporting functions, alphabetized.

# Determine the operating system that is running the script:
# - sets the variable operatingSystem to cygwin, linux, or mingw (Git Bash)
checkOperatingSystem()
{
  if [ ! -z "${operatingSystem}" ]; then
    # Have already checked operating system so return.
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
  echoStderr ""
  echoStderr "Detected operatingSystem=${operatingSystem} operatingSystemShort=${operatingSystemShort}"
  echoStderr ""
}

# Echo a string to standard error (stderr).
# This is done so that output printed to stdout is not mixed with stderr.
echoStderr() {
  echo "$@" >&2
}

# Check whether a file exists on GCP storage:
# - function argument should be Google storage URL gs:opencdss... etc.
gcpUtilFileExists() {
  local fileToCheck
  fileToCheck=$1
  # The following will return 0 if the file exists, 1 if not.
  gsutil.cmd -q stat ${fileToCheck}
  return $?
}

# Get the user's login to local temporary files:
# - Git Bash apparently does not set ${USER} environment variable
# - Set USER as script variable only if environment variable is not already set
# - See: https://unix.stackexchange.com/questions/76354/who-sets-user-and-username-environment-variables
getUserLogin() {
  if [ -z "${USER}" ]; then
    if [ ! -z "${LOGNAME}" ]; then
      USER=${LOGNAME}
    fi
  fi
  if [ -z "${USER}" ]; then
    USER=$(logname)
  fi
  # Else - not critical since used for temporary files.
}

# Parse the command parameters:
# - use the getopt command line program so long options can be handled
parseCommandLine() {
  local optstring optstringLong
  local exitCode
  local GETOPT_OUT

  # Single character options.
  optstring="dhv"
  # Long options.
  optstringLong="debug,dryrun,help,version"
  # Parse the options using getopt command.
  GETOPT_OUT=$(getopt --options ${optstring} --longoptions ${optstringLong} -- "$@")
  exitCode=$?
  if [ ${exitCode} -ne 0 ]; then
    # Error parsing the parameters such as unrecognized parameter.
    echoStderr ""
    printUsage
    exit 1
  fi
  # The following constructs the command by concatenating arguments.
  eval set -- "${GETOPT_OUT}"
  # Loop over the options.
  while true; do
    #echo "Command line option is ${opt}"
    case "${1}" in
      --debug) # --debug  Indicate to output debug messages.
        echoStderr "--debug detected - will print debug messages."
        debug="true"
        shift 1
        ;;
      -d|--dryrun) # -d or --dryrun  Indicate to do a dryrun but not actually upload.
        echoStderr "--dryrun detected - will not change files on GCP"
        dryrun="-n"
        shift 1
        ;;
      -h|--help) # -h or --help  Print the program usage.
        printUsage
        exit 0
        ;;
      -v|--version) # -v or --version  Print the program version.
        printVersion
        exit 0
        ;;
      --) # No more arguments.
        shift
        break
        ;;
      *) # Unknown option.
        echoStderr ""
        echoStderr "Invalid option: ${1}" >&2
        printUsage
        exit 1
        ;;
    esac
  done
}

# Print the usage.
printUsage() {
  echoStderr ""
  echoStderr "Usage: ${scriptName}"
  echoStderr ""
  echoStderr "Create the opencdss.state.co.us/tstool/index.html file."
  echoStderr ""
  echoStderr "-d, --dryrun      Dry run (do not clobber files on GCP)."
  echoStderr "-h, --help        Print usage."
  echoStderr ""
}

# Print the usage.
printVersion() {
  echoStderr "${version}"
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

  # First do a listing of the software folder.
  tmpGcpSoftwareCatalogPath="/tmp/${USER}-tstool-software-catalog-ls.txt"
  echoStderr "tmpGcpSoftwareCatalogPath=${tmpGcpSoftwareCatalogPath}"
  # Match exe and tar.gz files to include Windows and Linux.
  gsutil.cmd ls -l "${gcpFolderUrl}/*/software" | grep -E -v '^gs*' | grep gs | grep -E 'exe|tar.gz' > ${tmpGcpSoftwareCatalogPath}
  if [ "${PIPESTATUS[0]}" -ne 0 ]; then
    echoStderr ""
    echoStderr "[Error] Error listing TSTool download files to create catalog."
    exit 1
  fi

  # Listing of the doc-user folders.
  tmpGcpDocUserCatalogPath="/tmp/${USER}-tstool-doc-user-catalog-ls.txt"
  echoStderr "tmpGcpDocUserCatalogPath=${tmpGcpDocUserCatalogPath}"
  # Match index.html paths.
  gsutil.cmd ls -l "${gcpFolderUrl}/*/doc-user" | grep -E -v '^gs*' | grep gs | grep index.html > ${tmpGcpDocUserCatalogPath}
  if [ "${PIPESTATUS[0]}" -ne 0 ]; then
    echoStderr ""
    echoStderr "[Error] Error listing TSTool user documentation files to create catalog."
    exit 1
  fi

  # Listing of the doc-dev folders.
  tmpGcpDocDevCatalogPath="/tmp/${USER}-tstool-doc-dev-catalog-ls.txt"
  echoStderr "tmpGcpDocDevCatalogPath=${tmpGcpDocDevCatalogPath}"
  # Match index.html paths.
  gsutil.cmd ls -l "${gcpFolderUrl}/*/doc-dev" | grep -E -v '^gs*' | grep gs | grep index.html > ${tmpGcpDocDevCatalogPath}
  if [ "${PIPESTATUS[0]}" -ne 0 ]; then
    echoStderr ""
    echoStderr "[Error] Error listing TSTool developer documentation files to create catalog."
    exit 1
  fi

  # Create an index.html file for upload.
  indexHtmlTmpFile="/tmp/${USER}-tstool-index.html"
  gcpIndexHtmlUrl="${gcpFolderUrl}/index.html"

  # Also create index as CSV to facilitate listing available versions.
  indexCsvTmpFile="/tmp/${USER}-tstool-index.csv"
  gcpIndexCsvUrl="${gcpFolderUrl}/index.csv"
  echo '<!DOCTYPE html>
<head>
<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta charset="utf-8"/>
<link id="cdss-favicon" rel="shortcut icon" href="http://opencdss.state.co.us/opencdss/images/opencdss-favicon.ico" type="image.ico">
<style>
   body {
     font-family: "Trebuchet MS", Helvetica, sans-serif !important;
   }
   table {
     border-collapse: collapse;
   }
   th {
     border-right: solid 1px;
     border-left: solid 1px;
     border-bottom: solid 1px;
     padding-left: 5px;
     padding-right: 5px;
   }
   td {
     border-right: solid 1px;
     border-left: solid 1px;
     padding-left: 5px;
     padding-right: 5px;
   }
   #installersize {
     border-right: solid 1px;
     border-left: solid 1px;
     padding-left: 5px;
     padding-right: 5px;
     text-align: right;
   }
   tr {
     border: none;
   }
</style>
<title>OpenCDSS TSTool Downloads</title>
</head>
<body>
<h1>TSTool Software Downloads</h1>
<p>
<a href="http://opencdss.state.co.us/opencdss/tstool/">See also the OpenCDSS TSTool page</a>,
which provides additional information about TSTool.
</p>
<p>
<a href="http://www.colorado.gov/pacific/cdss/tstool">See also the CDSS TSTool page</a>,
which provides access to TSTool releases used in State of Colorado projects.
</p>
<p>
The TSTool software is available for Windows and Linux.
See the <a href="http://opencdss.state.co.us/tstool/latest/doc-user/appendix-install/install/">latest TSTool documentation</a>
for installation information (or follow a documentation link below for specific version documentation).
</p>
<p>
<ul>
<li>Multiple versions of TSTool can be installed on a computer.
<li>Versions before 14 used zero-padding in version numbers.
<li>TSTool user configuration files are shared between each major version.
<li>Download files that include <code>dev</code> in the version are development versions that can be
installed to test the latest features and bug fixes that are under development.</li>
<li><b>If clicking on a file download link does not download the file,
right-click on the link and use "Save link as..." (or similar).</b></li>
</ul>

<hr>
<h2>Windows Download</h2>
<p>
Install TSTool by downloading the executable setup file and then run using File Explorer.
Then run TSTool from the Windows <b><i>Start / CDSS / TSTool-Version</i></b> menu.
</p>' > ${indexHtmlTmpFile}

  # Generate a table of available versions for Windows.

  uploadIndexHtmlFile_Table win Windows

  # TODO smalers 2019-04-29 need to enable downloads for other operating systems.

echo '<hr>
<h2>Linux Download</h2>
<p>
Linux versions of TSTool are not currently provided by the State of Colorado.
Contact the <a href="https://openwaterfoundation.org">Open Water Foundation</a> if interested.
</p>' >> ${indexHtmlTmpFile}

#<p>
#Install TSTool by downloading the *.run file, and run to install in /opt/tstool-version.
#Then run tstool from the Linux command line.
#</p>' >> ${indexHtmlTmpFile}

  # Generate a table of available versions for Linux.

#  uploadIndexHtmlFile_Table lin Linux

  #echo '<hr>' >> ${indexHtmlTmpFile}
  #echo '<h2>Cygwin Download</h2>' >> ${indexHtmlTmpFile}
  #echo '<p>' >> ${indexHtmlTmpFile}
  #echo 'Install the GeoProcessor on Cygwin by downloading the <a href="download-gp.sh">download-gp.sh script</a> and running it in a shell window.' >> ${indexHtmlTmpFile}
  #echo 'You will be prompted for options for where to install the software.' >> ${indexHtmlTmpFile}
  #echo 'Once installed, run the GeoProcessor using scripts in the <code>scripts</code> folder under the install folder.' >> ${indexHtmlTmpFile}
  #echo '<b>Do not download directly using files below (the list is provided as information).</b>' >> ${indexHtmlTmpFile}
  #echo '</p>' >> ${indexHtmlTmpFile}
  ## Generate a table of available versions for Cygwin
  #uploadIndexHtmlFile_Table cyg Cygwin

  echo '</body>' >> ${indexHtmlTmpFile}
  echo '</html>' >> ${indexHtmlTmpFile}

  echoStderr ""
  echoStderr "Uploading the index file:"
  echoStderr "  from: ${indexHtmlTmpFile}"
  echoStderr "    to: ${gcpIndexHtmlUrl}"
  echoStderr ""
  read -p "Continue with upload (Y/n)? " answer
  if [ -z "${answer}" -o "${answer}" = "Y" -o "${answer}" = "y" ]; then
    # Will continue.
    :
  else
    # Exit the script.
    exit 0
  fi

  # If here, continue with the copy.
  # set -x
  gsutil.cmd cp ${indexHtmlTmpFile} ${gcpIndexHtmlUrl}
  # { set +x; } 2> /dev/null
  if [ "${PIPESTATUS[0]}" -ne 0 ]; then
    echoStderr ""
    echoStderr "[Error] Error uploading index.html file."
    exit 1
  fi

  # Similarly create the index.csv file.

  # First create the index:
  # - use some string replacements to make sure that dev versions are listed after non-dev release
  #   with the same number is listed first
  echo '"Version"' > ${indexCsvTmpFile}
  cat ${tmpGcpSoftwareCatalogPath} | awk '{print $3}' | cut -d '/' -f 5 | sed -E 's/([0-9]$)/\1-zzz/g' | sed 's/0dev/0-dev/g' | sort -r | sed 's/-zzz//g' >> ${indexCsvTmpFile}

  # Then upload to the website.
  gsutil.cmd cp ${indexCsvTmpFile} ${gcpIndexCsvUrl}
  if [ "${PIPESTATUS[0]}" -ne 0 ]; then
    echoStderr ""
    echoStderr "[Error] Error uploading index.csv file."
    exit 1
  fi
}

# Create a table of downloads for an operating system to be used in the index.html file:
# - first argument is operating system short name to match installers:  "win", "lin", or "cyg"
# - second argument is operating system long name to match installers:  "Windows", "Linux", or "Cygwin"
uploadIndexHtmlFile_Table() {
  local downloadOs dowloadPattern
  local indexHtmlTmpCatalogFile
  # Operating system is passed in as the required first argument.
  downloadOs=$1
  downloadOsLong=$2
  # The following allows sorting the list in reverse order.
  indexHtmlTmpCatalogFile="/tmp/${USER}-tstool-catalog-${downloadOs}.html"
  if [ "${downloadOs}" = "win" ]; then
    downloadPattern="exe"
  elif [ "${downloadOs}" = "lin" ]; then
    downloadPattern="tar.gz"
  fi
  if [ "${debug}" = "true" ]; then
    echoStderr "[DEBUG] downloadPattern=${downloadPattern}"
  fi
  echo '<table>' >> ${indexHtmlTmpFile}
  # List the available download files.
  # Listing local files does not show all available files on GCP but may be useful for testing.
  catalogSource="gcp"  # "gcp" or "local"
  if [ "${catalogSource}" = "gcp" ]; then
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
  tmpGcpDocDevCatalogPath="/tmp/${USER}-tstool-doc-dev-catalog-ls.txt"
  echo '<tr><th>Download File</th><th>Product</th><th>Version</th><th>File Timestamp</th><th>Size (bytes)</th><th>Operating System</th><th>User Doc</th><th>Dev Doc</th></tr>' >> ${indexHtmlTmpFile}
    cat "${tmpGcpSoftwareCatalogPath}" | grep "${downloadPattern}" | awk '{ printf "%s %s %s\n", $1, $2, $3 }' | sed -E 's|([0-9][0-9]/)|\1-zzz|g' | sed 's|/-zzz|-zzz|g' | sed 's|dev|-dev|g' | sort -r -k3,3 | sed 's|-zzz||g' | sed 's|-dev|dev|g' | awk -v debug=${debug} -v tmpGcpDocUserCatalogPath=${tmpGcpDocUserCatalogPath} -v tmpGcpDocDevCatalogPath=${tmpGcpDocDevCatalogPath} '
      BEGIN {
        if ( debug == "true" ) {
          printf("<!-- [DEBUG] tmpGcpDocUserCatalogPath=%s -->\n", tmpGcpDocUserCatalogPath)
          printf("<!-- [DEBUG] tmpGcpDocDevCatalogPath=%s -->\n", tmpGcpDocDevCatalogPath)
        }
      }
      {
        # Download file is the full line.
        downloadFileSize = $1
        downloadFileDateTime = $2
        downloadFilePath = $3
        # Split the download file path into parts to get the download file without path:
        # - index is 1+
        nparts=split(downloadFilePath,downloadFilePathParts,"/")
        downloadFile = downloadFilePathParts[nparts]
        downloadFileUrl=downloadFilePath
        gsub("gs:","http:",downloadFileUrl)
        # Split the download file into parts to get other information:
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

        # --- User documentation.
        cmd=sprintf("cat %s | grep 'tstool/%s/doc-user/index.html' | wc -l", tmpGcpDocUserCatalogPath, downloadFileVersion)
        cmd | getline docCount
        if ( docCount > 0 ) {
          docUserUrl=sprintf("http://opencdss.state.co.us/tstool/%s/doc-user",downloadFileVersion)
          docUserHtml=sprintf("<a href=\"%s\">View</a>",docUserUrl)
        }
        else {
          docUserHtml=""
        }

        # --- Developer  documentation.
        cmd=sprintf("cat %s | grep 'tstool/%s/doc-dev/index.html' | wc -l", tmpGcpDocDevCatalogPath, downloadFileVersion)
        cmd | getline docCount
        if ( docCount > 0 ) {
          docDevUrl=sprintf("http://opencdss.state.co.us/tstool/%s/doc-dev",downloadFileVersion)
          docDevHtml=sprintf("<a href=\"%s\">View</a>",docDevUrl)
        }
        else {
          docDevHtml=""
        }

        #if ( downloadFileOs == "cyg" ) {
        #  downloadFileOs = "Cygwin"
        #}
        #else if ( downloadFileOs == "lin" ) {
        #  downloadFileOs = "Linux"
        #}
        #else if ( downloadFileOs == "win" ) {
        #  downloadFileOs = "Windows"
        #}
        printf "<tr><td><a href=\"%s\"><code>%s</code></a></td><td>%s</td><td>%s</td><td>%s</td><td id=\"installersize\">%s</td><td>%s</td><td>%s</td><td>%s</td></tr>\n", downloadFileUrl, downloadFile, downloadFileProduct, downloadFileVersion, downloadFileDateTime, downloadFileSize, downloadFileOs, docUserHtml, docDevHtml
      }' >> ${indexHtmlTmpFile}
  fi
  echo '</table>' >> ${indexHtmlTmpFile}
}

# Entry point for the script.

# Get the location where this script is located since it may have been run from any folder.
scriptFolder=$(cd $(dirname "$0") && pwd)
scriptName=$(basename $0)
repoFolder=$(dirname "${scriptFolder}")
srcFolder="${repoFolder}/src"

# Version, mainly used to help understand changes over time when comparing files.
version="1.1.0 (2021-09-01)"

echoStderr "scriptFolder=${scriptFolder}"
echoStderr "repoFolder=${repoFolder}"
echoStderr "srcFolder=${srcFolder}"

# Whether or not debug messages are printed.
debug="false"

# Whether or not dry run is done.
dryrun=""

# Root location where files are to be uploaded.
gcpFolderUrl="gs://opencdss.state.co.us/tstool"

# Parse the command line parameters.
parseCommandLine $@

# Determine the user login, used for temporary file location.
getUserLogin

# Upload the created index file to GCP bucket.
uploadIndexHtmlFile

# Exit with status from the upload function.
exit $?
