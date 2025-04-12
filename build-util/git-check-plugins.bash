#!/bin/bash
(set -o igncr) 2>/dev/null && set -o igncr; # This comment is required.
# The above line ensures that the script can be run on Cygwin/Linux even with Windows CRNL.
#
# git-check-plugins
# - check the TSTool plugin repositories for status
# - run the 'git-check-product' script in each found plugin

# Get the location where this script is located since it may have been run from any folder.
scriptFolder=$(cd $(dirname "$0") && pwd)

# Set debug="true" to troubleshoot, "false" otherwise.
debug="false"
if [ "${debug}" = "true" ]; then
  debugOpt="--debug"
fi

# Git utilities folder is relative to the user's files in a standard development files location:
# - determine based on location relative to the script folder
# Specific repository folder for this repository.
repoFolder=$(dirname ${scriptFolder})
# Want the parent folder to the specific Git repository folder
gitReposFolder=$(dirname ${repoFolder})

# List repository folders that have 'plugin' in the name:
# - list the names but not the contents of the folders
# - if a check script is found for the repository, run it
# - pass command line parameters from this script to the check script so that --debug, etc. can be used
cd ${gitReposFolder}
if [ "${debug}" = "true" ]; then
  echo "Now in folder: ${gitReposFolder}"
fi
ls -1 -d *-plugin | while read pluginFolder; do
  # Use for development:
  # - only process one repository
  # - specifically pass --debug at the front of the command line to turn on before other arguments are parsed
  #   (this is redundant if --debug is used when calling this script)
  #if [ "${pluginFolder}" != "trilynx-tstool-nsdataws-plugin" ]; then
  #  continue
  #fi
  # Likely names of scripts to check the plugin's Git status.
  candidateScript1="${pluginFolder}/build-util/git-check-product.sh"
  candidateScript2="${pluginFolder}/build-util/git-check-product.bash"
  if [ -f "${candidateScript1}" ]; then
    # Candidate check script exists.
    echo ""
    echo "Checking plugin:  ${pluginFolder}"
    if [ "${debug}" = "true" ]; then
      echo "Calling: ${candidateScript1} ${debugOpt} --onlyplugin $@"
    fi
    ${candidateScript1} ${debugOpt} --onlyplugin $@
  elif [ -f "${candidateScript2}" ]; then
    # Candidate check script exists.
    echo ""
    echo "Checking plugin:  ${pluginFolder}"
    if [ "${debug}" = "true" ]; then
      echo "Calling: ${candidateScript2} ${debugOpt} --onlyplugin $@"
    fi
    ${candidateScript2} ${debugOpt} --onlyplugin $@
  else
    # No candidate check script exists.
    echo ""
    echo "Plugin ${pluginFolder}/build-util does not contain one of the following to check the repository:"
    echo "  ${candidateScript1}"
    echo "  ${candidateScript2}"
  fi
done

exit 0
