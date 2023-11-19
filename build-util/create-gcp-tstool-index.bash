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

# Add a zero padded version column to the file listing:
# - this is used to help sort the installers for the table listing
# - input:   94612246  2019-04-27T10:01:42Z  gs://opencdss.state.co.us/tstool/12.06.00.dev1/software/TSTool_CDSS_12.06.00_Setup.exe
# - output:  94612246  2019-04-27T10:01:42Z  gs://opencdss.state.co.us/tstool/12.06.00.dev1/software/TSTool_CDSS_12.06.00_Setup.exe 012.006.000.dev1
# - if a normal release, add zzz as the fourth part so it will sort before "dev" when sorted in reverse,
#   which is what is done to get the largest version first
# - the date/time at the end is the tie-breaker, for example when multiple same "div" release is made
# - the first function parameter is the file to sort, it will be replaced
addZeroPaddedVersion () {
  local infile outfile

  infile=$1

  if [ -z "${infile}" ]; then
    echoStdout "[ERROR] No input file provided to addZeroPaddedVersion.  Exiting."
    exit 1
  fi

  outfile="${infile}.padded"

  # Use awk to add the new column.
  cat "${infile}" | awk '{
    # Version is the 2nd field of the fourth input field.
    downloadFilePath = $3
    # Split the download file path into parts to get the download file without path:
    # - index is 1+
    nparts=split(downloadFilePath,downloadFilePathParts,"/")
    downloadFile = downloadFilePathParts[nparts]
    downloadFileUrl=downloadFilePath
    gsub("gs:","https:",downloadFileUrl)
    # Split the download file into parts to get other information:
    # - index is 1+
    split(downloadFile,downloadFileParts,"_")
    downloadFileProduct=downloadFileParts[1]
    downloadFileVersion=downloadFileParts[3]

    nVersionParts = split(downloadFileVersion,versionParts,".")
    version1 = sprintf("%03d", versionParts[1])
    version2 = sprintf("%03d", versionParts[2])
    version3 = sprintf("%03d", versionParts[3])
    version4 = "zzz"
    if ( nVersionParts > 3 ) {
      # Also have something like 1.2.3.dev1.
      version4 = versionParts[4]
      version4
    }
    printf("%s %s %s %s.%s.%s.%s\n", $1, $2, $3, version1, version2, version3, version4)
  }
  ' > ${outfile}
  # Replace the original file.
  mv "${outfile}" "${infile}"
  echoStderr "[INFO] Added zero-padded version to:"
  echoStderr "[INFO]   ${outfile}"
}

# Determine the operating system that is running the script:
# - sets the variable operatingSystem to cygwin, linux, or mingw (Git Bash)
checkOperatingSystem() {
  if [ ! -z "${operatingSystem}" ]; then
    # Have already checked operating system so return.
    return
  fi
  operatingSystem="unknown"
  os=$(uname | tr [a-z] [A-Z])
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
  echoStderr "[INFO]"
  echoStderr "[INFO] Detected operatingSystem=${operatingSystem} operatingSystemShort=${operatingSystemShort}"
  echoStderr "[INFO]"
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

# Get the TSTool version:
# - the version is echoed to stdout and can be assigned in the calling code
# - sets the global 'tstoolVersion'
getTstoolVersion() {
  local tstoolFile

  tstoolFile="${srcMainFolder}/TSToolMain.java"
  if [ -f "${tstoolFile}" ]; then
    tstoolVersion=$(cat ${tstoolFile} | grep -m 1 'String PROGRAM_VERSION' | cut -d '=' -f 2 | cut -d '(' -f 1 | tr -d " " | tr -d '"')
  else
    echoStderr "[ERROR] Cannot determine TSTool version because file not found:"
    echoStderr "[ERROR]   ${tstoolFile}"
    exit 1
  fi
  return 0
}

# Get the user's login to use for local temporary files:
# - Git Bash apparently does not set ${USER} environment variable
# - set USER as script variable only if environment variable is not already set
# - see: https://unix.stackexchange.com/questions/76354/who-sets-user-and-username-environment-variables
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
  optstringLong="debug,help,version"
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
        echoStderr "[DEBUG] --debug detected - will print debug messages."
        debug="true"
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
        echoStderr "[ERROR]"
        echoStderr "[ERROR] Invalid option: ${1}" >&2
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
  echoStderr "Create the product GCP index file:  ${gcpIndexHtmlUrl}"
  echoStderr ""
  echoStderr "--debug           Print debug messages, for troubleshooting."
  echoStderr "-h, --help        Print usage."
  echoStderr "-v, --version     Print script version."
  echoStderr ""
}

# Print the version.
printVersion() {
  echoStderr "${version}"
}

# Upload the index.html file for the static website download page:
# - this is basic at the moment but can be improved in the future such as
#   software.openwaterfoundation.org page, but for only one product, with list of variants and versions
uploadIndexHtmlFile() {
  local indexHtmlTmpFile gcpIndexHtmlUrl
  local indexCsvTmpFile gcpIndexCsvlUrl

  # List available software installer files:
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

  # First do a listing of the software folder.
  tmpGcpSoftwareCatalogPath="/tmp/${USER}-tstool-software-catalog-ls.txt"
  echoStderr "[INFO] tmpGcpSoftwareCatalogPath=${tmpGcpSoftwareCatalogPath}"
  # Match exe and tar.gz files to include Windows and Linux.
  gsutil.cmd ls -l "${gcpFolderUrl}/*/software" | grep -E -v '^gs*' | grep gs | grep -E 'exe|tar.gz' > ${tmpGcpSoftwareCatalogPath}
  if [ "${PIPESTATUS[0]}" -ne 0 ]; then
    echoStderr "[ERROR]"
    echoStderr "[ERROR] Error listing TSTool download files to create catalog."
    exit 1
  fi

  # Add the zero-padded version on the end of the list to help with sorting.
  addZeroPaddedVersion ${tmpGcpSoftwareCatalogPath}

  # Check whether to add the pending row to the product file:
  # - doPending is global since it is used in other functions
  latestVersionCount=$(cat ${tmpGcpSoftwareCatalogPath} | grep ${tstoolVersion} | wc -l)
  doPending="false"
  if [ ${latestVersionCount} -eq 0 ]; then
    # Did not find the latest version in the catalog of installers:
    # - need to add a pending line because an installer has not been uploaded
    echoStderr "[INFO] Will add pending documentation."
    doPending="true"
  fi

  # Listing of the doc-user folders.
  tmpGcpDocUserCatalogPath="/tmp/${USER}-tstool-doc-user-catalog-ls.txt"
  echoStderr "[INFO] tmpGcpDocUserCatalogPath=${tmpGcpDocUserCatalogPath}"
  # Match index.html paths.
  gsutil.cmd ls -l "${gcpFolderUrl}/*/doc-user" | grep -E -v '^gs*' | grep gs | grep index.html > ${tmpGcpDocUserCatalogPath}
  if [ "${PIPESTATUS[0]}" -ne 0 ]; then
    echoStderr "[ERROR]"
    echoStderr "[ERROR] Error listing TSTool user documentation files to create catalog."
    exit 1
  fi

  # Listing of the doc-dev folders.
  tmpGcpDocDevCatalogPath="/tmp/${USER}-tstool-doc-dev-catalog-ls.txt"
  echoStderr "[INFO] tmpGcpDocDevCatalogPath=${tmpGcpDocDevCatalogPath}"
  # Match index.html paths.
  gsutil.cmd ls -l "${gcpFolderUrl}/*/doc-dev" | grep -E -v '^gs*' | grep gs | grep index.html > ${tmpGcpDocDevCatalogPath}
  if [ "${PIPESTATUS[0]}" -ne 0 ]; then
    echoStderr "[ERROR]"
    echoStderr "[ERROR] Error listing TSTool developer documentation files to create catalog."
    exit 1
  fi

  # Create an index.html file for upload.
  indexHtmlTmpFile="/tmp/${USER}-tstool-index.html"
  gcpIndexHtmlUrl="${gcpFolderUrl}/index.html"

  # Also create index as CSV to facilitate listing available versions.
  indexCsvTmpFile="/tmp/${USER}-tstool-index.csv"
  gcpIndexCsvUrl="${gcpFolderUrl}/index.csv"

  echo '<!DOCTYPE html>
<head>' > ${indexHtmlTmpFile}

  echo "
<!-- Start Google Analytics 4 property. -->
<script async src=\"https://www.googletagmanager.com/gtag/js?id=G-PTJLXFDPDL\"></script>
<script>
window.dataLayer = window.dataLayer || [];
function gtag(){dataLayer.push(arguments);}
gtag('js', new Date());
gtag('config', 'G-PTJLXFDPDL');
</script>
<!-- End Google Analytics 4 property. -->" >> ${indexHtmlTmpFile}

  echo '
<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta charset="utf-8"/>
<link id="cdss-favicon" rel="shortcut icon" href="https://opencdss.state.co.us/opencdss/images/opencdss-favicon.ico" type="image.ico">
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
  tr {
    border: none;
  }
  #installersize {
    border-right: solid 1px;
    border-left: solid 1px;
    padding-left: 5px;
    padding-right: 5px;
    text-align: right;
  }
</style>
<title>OpenCDSS TSTool Downloads</title>
</head>
<body>
<h1>TSTool Software Downloads</h1>
<p>
<a href="https://opencdss.state.co.us/opencdss/tstool/">See the OpenCDSS TSTool page</a>,
which provides additional information about TSTool.
</p>
<p>
<a href="https://www.colorado.gov/pacific/cdss/tstool">See the CDSS TSTool page</a>,
which provides access to TSTool releases used in State of Colorado projects.
</p>
<p>
The TSTool software is available for Windows and Linux.
See the <a href="https://opencdss.state.co.us/tstool/latest/doc-user/appendix-install/install/">latest TSTool documentation</a>
for installation information (or follow a documentation link below for specific version documentation).
</p>
<p>
<ul>
<li>Multiple versions of TSTool can be installed on a computer.</li>
<li>Versions before 14 used zero-padding in version numbers.</li>
<li>TSTool user configuration files are shared between each major version.</li>
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
</p>' >> ${indexHtmlTmpFile}

  # Generate a table of available versions for Windows.

  uploadIndexHtmlFile_Table win Windows

  # TODO smalers 2019-04-29 need to enable downloads for other operating systems.

  echo '<hr>

<h2>Linux Download</h2>
<p>
Linux versions of TSTool are not currently provided by the State of Colorado.
Download from the <a href="https://software.openwaterfoundation.org/tstool/">Open Water Foundation TSTool Downloads page</a>.
</p>' >> ${indexHtmlTmpFile}

  # Generate a table of available versions for Linux.

  # uploadIndexHtmlFile_Table lin Linux

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
    # Use GCP zero-padded list from catalog file for the index.html file download file list,
    # with format like the following (no space at beginning of the line):
    #
    # 12464143  2019-04-27T10:01:42Z  gs://opencdss.state.co.us/tstool/14.0.0/software/TSTool_CDSS_14.0.0_Setup.exe 014.000.000.zzz
    #
    # Use awk below to print the line with single space between tokens.
    #
    tmpGcpDocDevCatalogPath="/tmp/${USER}-tstool-doc-dev-catalog-ls.txt"
    echo '<tr><th>Download File</th><th>Product</th><th>Version</th><th>File Timestamp</th><th>Size (bytes)</th><th>Operating System</th><th>User Doc</th><th>Dev Doc</th></tr>' >> ${indexHtmlTmpFile}
    cat "${tmpGcpSoftwareCatalogPath}" | sort -r -k4,4 | awk -v debug=${debug} -v doPending=${doPending} -v tstoolVersion=${tstoolVersion} -v tmpGcpDocUserCatalogPath=${tmpGcpDocUserCatalogPath} -v tmpGcpDocDevCatalogPath=${tmpGcpDocDevCatalogPath} '
      BEGIN {
        if ( debug == "true" ) {
          printf("<!-- [DEBUG] tmpGcpDocUserCatalogPath=%s -->\n", tmpGcpDocUserCatalogPath)
          printf("<!-- [DEBUG] tmpGcpDocDevCatalogPath=%s -->\n", tmpGcpDocDevCatalogPath)
        }
        if ( doPending == "true" ) {
          # Output a row showing a pending release, with link to the draft documentation.
          downloadFileProduct="TSTool"
          downloadFileUrl="Pending"
          downloadFileVersion=tstoolVersion
          downloadFileDate="Pending"
          downloadFileTime=""
          downloadFileSize=""
          downloadFileOs=""
          # doc-dev
          cmd=sprintf("cat %s | grep 'tstool/%s/doc-dev/index.html' | wc -l", tmpGcpDocUserCatalogPath, downloadFileVersion)
          if ( debug == 1 ) {
             printf("<!-- [DEBUG] Command to check for doc-dev: %s -->\n", cmd)
          }
          cmd | getline docCount
          close(cmd)
          if ( debug == 1 ) {
             printf("<!-- [DEBUG] Count from above command: %d -->\n", docCount)
          }
          if ( docCount > 0 ) {
            docDevUrl=sprintf("https://opencdss.state.co.us/tstool/%s/doc-dev", downloadFileVersion)
            docDevHtml=sprintf("<a href=\"%s\">View</a>",docDevUrl)
          }
          else {
            docDevHtml=""
          }
          # doc-user
          # Get the count of matching doc-user in the original full bucket list.
          cmd=sprintf("cat %s | grep 'tstool/%s/doc-user/index.html' | wc -l", tmpGcpDocUserCatalogPath, downloadFileVersion)
          if ( debug == 1 ) {
             printf("<!-- [DEBUG] Command to check for doc-user: %s -->\n", cmd)
          }
          cmd | getline docCount
          close(cmd)
          if ( debug == 1 ) {
             printf("<!-- [DEBUG] Count from above command: %d -->\n", docCount)
          }
          if ( docCount > 0 ) {
            docUserUrl=sprintf("https://opencdss.state.co.us/tstool/%s/doc-user/", downloadFileVersion)
            docUserHtml=sprintf("<a href=\"%s\">View</a>",docUserUrl)
          }
          else {
            docUserHtml=""
          }
          #       Download File       Product     Version    Timestamp      Size     OS       User doc   Dev doc
          printf "<tr><td>Pending</td><td>%s</td><td>%s</td><td>Pending</td><td></td><td></td><td>%s</td><td>%s</td></tr>\n", downloadFileProduct, downloadFileVersion, docUserHtml, docDevHtml
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
        gsub("gs:","https:",downloadFileUrl)
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
          docUserUrl=sprintf("https://opencdss.state.co.us/tstool/%s/doc-user",downloadFileVersion)
          docUserHtml=sprintf("<a href=\"%s\">View</a>",docUserUrl)
        }
        else {
          docUserHtml=""
        }

        # --- Developer  documentation.
        cmd=sprintf("cat %s | grep 'tstool/%s/doc-dev/index.html' | wc -l", tmpGcpDocDevCatalogPath, downloadFileVersion)
        cmd | getline docCount
        if ( docCount > 0 ) {
          docDevUrl=sprintf("https://opencdss.state.co.us/tstool/%s/doc-dev",downloadFileVersion)
          docDevHtml=sprintf("<a href=\"%s\">View</a>",docDevUrl)
        }
        else {
          docDevHtml=""
        }

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
srcMainFolder="${srcFolder}/DWR/DMI/tstool"

# Version, mainly used to help understand changes over time when comparing files.
version="1.2.0 (2023-11-17)"

echoStderr "[INFO] scriptFolder=${scriptFolder}"
echoStderr "[INFO] repoFolder=${repoFolder}"
echoStderr "[INFO] srcFolder=${srcFolder}"
echoStderr "[INFO] srcMainFolder=${srcMainFolder}"

# Whether or not debug messages are printed.
debug="false"

# Root location where files are to be uploaded.
gcpFolderUrl="gs://opencdss.state.co.us/tstool"

# Parse the command line parameters.
parseCommandLine $@

# Determine the user login, used for temporary file location.
getUserLogin

# Get the TSTool version so can add the pending index row:
# - this sets the global tstoolVersion
getTstoolVersion
echoStderr "[INFO] Current TSTool version = ${tstoolVersion}"

# Upload the created index file to GCP bucket.
uploadIndexHtmlFile

# Exit with status from the upload function.
exit $?
