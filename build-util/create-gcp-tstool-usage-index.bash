#!/bin/bash
#
# Create the following files on GCP bucket, such as
#
#   opencdss.state.co.us/tstool/14.8.0/usage/index.html
#
# These index.html file contain Google Analytics 4 tracking and are accessed by TSTool
# at startup to track usage by version.

# Supporting functions, alphabetized.

# Determine the operating system that is running the script:
# - sets the variable operatingSystem to cygwin, linux, or mingw (Git Bash)
checkOperatingSystem() {
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
        echoStderr "--debug detected - will print debug messages."
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
  echoStderr "Create the product GCP usage index file:  ${gcpIndexHtmlUrl}"
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

# Upload the index.html file for the static website download page
# - this is basic at the moment but can be improved in the future such as
#   software.openwaterfoundation.org page, but for only one product, with list of variants and versions
uploadIndexHtmlFile() {
  local indexHtmlTmpFile gcpIndexHtmlUrl

  # Create an index.html file for upload.
  indexHtmlTmpFile="/tmp/${USER}-tstool-usage-index.html"
  gcpIndexHtmlUrl="${gcpFolderUrl}/${tstoolVersion}/usage/index.html"

  echo '<!DOCTYPE html>
<head>
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
</style>' > ${indexHtmlTmpFile}

echo "
<!-- Start Google Analytics 4 property. -->
<script async src=\"https://www.googletagmanager.com/gtag/js?id=G-PTJLXFDPDL\"></script>
<script>
window.dataLayer = window.dataLayer || [];

function gtag(){dataLayer.push(arguments);}

gtag('js', new Date());

gtag('config', 'G-PTJLXFDPDL');

</script>
<!-- End Google Analytics 4 property. -->
" >> ${indexHtmlTmpFile}

echo '
<title>OpenCDSS TSTool Usage Tracking</title>
</head>
<body>
<h1>TSTool Software Usage Tracking</h1>
<p>
<a href="https://opencdss.state.co.us/opencdss/tstool/">See the OpenCDSS TSTool page</a>,
which provides additional information about TSTool.
</p>
<p>
This page is accessed by TSTool at startup to track software usage.
The information helps software developers and support understand which TSTool versions are being used.
</p>' >> ${indexHtmlTmpFile}

  echo '</body>' >> ${indexHtmlTmpFile}
  echo '</html>' >> ${indexHtmlTmpFile}

  echoStderr ""
  echoStderr "Uploading the usage/index file:"
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
}

# Entry point for the script.

# Get the location where this script is located since it may have been run from any folder.
scriptFolder=$(cd $(dirname "$0") && pwd)
scriptName=$(basename $0)
repoFolder=$(dirname "${scriptFolder}")
srcFolder="${repoFolder}/src"

# Version, mainly used to help understand changes over time when comparing files.
version="1.1.0 (2021-09-01)"

# Get the TSTool version for the GCP product version folder.
srcMainFolder="${srcFolder}/DWR/DMI/tstool"
tstoolFile="${srcMainFolder}/TSToolMain.java"
if [ -f "${tstoolFile}" ]; then
  tstoolVersion=$(cat ${tstoolFile} | grep -m 1 'PROGRAM_VERSION' | cut -d '=' -f 2 | cut -d '(' -f 1 | tr -d " " | tr -d '"')
  tstoolModifierVersion=$(getVersionModifier "${tstoolVersion}")
else
  echoStderr "[ERROR] Cannot determine TSTool version because file not found:"
  echoStderr "[ERROR]   ${tstoolFile}"
  exit 1
fi
if [ -z "${tstoolVersion}" ]; then
  echoStderr "[ERROR] Cannot determine TSTool version by scanning:"
  echoStderr "  ${tstoolFile}"
  exit 1
fi

echoStderr "scriptFolder=${scriptFolder}"
echoStderr "repoFolder=${repoFolder}"
echoStderr "srcFolder=${srcFolder}"
echoStderr "tstoolVersion=${tstoolVersion}"

# Whether or not debug messages are printed.
debug="false"

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
