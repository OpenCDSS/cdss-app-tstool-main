#!/bin/sh
#
# git-clone-all-tstool - clone all TSTool repositories for new development environment setup
# - this script calls the general git tools script

# TSTool product home is relative to the user's files in a standard CDSS development files location
# - $HOME/${productHome}
productHome="cdss-dev/TSTool"

# TSTool GitHub repo URL root
githubRootUrl="https://github.com/OpenWaterFoundation"

# Main TSTool repository
mainRepo="cdss-app-tstool-main"

# TODO smalers 2018-10-12 The following may need to be made absolute to run from any folder
# - also pass the command parameters so that -h, etc. are recognized
git-tools/git-check.sh -m "${mainRepo}" -p "${productHome}" -g "${githubRootUrl}" $@
