#!/bin/sh
(set -o igncr) 2>/dev/null && set -o igncr; # this comment is required
# The above line ensures that the script can be run on Cygwin/Linux even with Windows CRNL
#
# git-tag-all-tstool - tag all TSTool repositories
# - this script calls the general git utilities script

# Get the location where this script is located since it may have been run from any folder
scriptFolder=`cd $(dirname "$0") && pwd`

# Git utilities folder is relative to the user's files in a standard development files location
# - determine based on location relative to the script folder
# Specific repository folder for this repository
repoHome=$(dirname ${scriptFolder})
# Want the parent folder to the specific Git repository folder
gitReposHome=$(dirname ${repoHome})

# Main TSTool repository
mainRepo="cdss-app-tstool-main"
mainRepoFolder="$gitReposHome/$mainRepo"

# Determine the version from the software product
# - code line looks like:
#   public static final String PROGRAM_VERSION = "12.07.00 (2018-09-19)";
# - this is used as information to help the user specify an intelligent tag name and commit message
# - grep -m 1 means stop after first occurrence
productVersion=$(cat $mainRepoFolder/src/DWR/DMI/tstool/TSToolMain.java | grep -m 1 'PROGRAM_VERSION' | cut -d '=' -f 2 | cut -d '(' -f 1 | tr -d " " | tr -d '"')
productName="TSTool"

# Run the generic utility script
${scriptFolder}/git-util/git-tag-all.sh -m "${mainRepo}" -g "${gitReposHome}" -N "$productName" -V "$productVersion" $@
