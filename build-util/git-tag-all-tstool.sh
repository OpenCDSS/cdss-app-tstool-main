#!/bin/sh
(set -o igncr) 2>/dev/null && set -o igncr; # this comment is required
# The above line ensures that the script can be run on Cygwin/Linux even with Windows CRNL
#
# Tag all repositories
# - interactively prompt for the tag ID and commit message

# Variables
dryRun=false # Default is to run operationally
#dryRun=true  # for testing

# Determine the OS that is running the script
# - mainly care whether Cygwin
checkOperatingSystem()
{
	if [ ! -z "${operatingSystem}" ]
	then
		# Have already checked operating system so return
		return
	fi
	operatingSystem="unknown"
	case "$(uname)" in
		CYGWIN*) operatingSystem="cygwin";;
		MINGW*) operatingSystem="mingw";;
	esac
	echo "operatingSystem=$operatingSystem (used to check for Cygwin)"
	if [ "${operatingSystem}" = "cygwin" ]
	then
		echo "RECOMMEND not using Cygwin git commands for development for this product"
	fi
}

# Main script entry point

# Product development main folder
# - top-level folder for the product
home2=$HOME
if [ "${operatingSystem}" = "cygwin" ]
	then
	# Expect product files to be in Windows user files location (/cygdrive/...), not Cygwin user files (/home/...)
	home2="/cygdrive/C/Users/$USER"
fi
# TSTool product home is relative to the users files in a standard CDSS development files location
productHome="$home2/cdss-dev/TSTool"
# Main repository in a group of repositories for a product
# - this is where the product repository list file will live
mainRepo="${productHome}/git-repos/cdss-app-tstool-main"
gitRepoFolder=`dirname ${mainRepo}`
# The following is a list of repositories including the main repository
# - one repo per line, no URL, just the repo name
# - repositories must have previously been cloned to local files
repoList="${mainRepo}/build-util/product-repo-list.txt"

# Get the TSTool version from the main source code file as an FYI
tstoolVersionFromCode=`grep -m 1 PROGRAM_VERSION ../src/DWR/DMI/tstool/TSToolMain.java`

# Ask the user for the tag name and commit message
while [ "1" = "1" ]
do
	echo ""
	echo "TSTool version from code: ${tstoolVersionFromCode}"
	read -p "Enter tag name with no spaces (e.g.: TSTool-12.06.00): " tagName
	read -p "Enter commit message (e.g,: TSTool 12.06.00 release): " tagMessage
	echo ""
	echo "Tag name=${tagName}"
	echo "Commit message=${tagMessage}"
	read -p "Continue with tag commit [y/n]?: " answer
	if [ "${answer}" = "y" ]
	then
		# Want to continue
		break
	else
		# Don't want to continue
		exit 0
	fi
done

# Make sure that the tag name and message are specified
if [ -z "${tagName}" ]
then
	echo "Tag name must be specified.  Exiting."
	exit 1
fi
if [ -z "${tagMessage}" ]
then
	echo "Tag commit message must be specified.  Exiting."
	exit 1
fi

# Commit the tag on each repository in the product
while IFS= read -r repoName
do
	# Make sure there are no carriage returns in the string
	# - can happen because file may have Windows-like endings but Git Bash is Linux-like
	# - use sed because it is more likely to be installed than dos2unix
	repoName=`echo ${repoName} | sed 's/\r$//'`
	if [ -z "${repoName}" ]
	then
		# Blank line
		continue
	fi
	firstChar=`expr substr "${repoName}" 1 1` 
	if [ "${firstChar}" = "#" ]
	then
		# Comment line
		continue
	fi
	# Commit the tag
	repoFolder="${productHome}/git-repos/${repoName}"
	#repoFolder="${gitRepoFolder}/${repoName}"
	echo "================================================================================"
	echo "Committing tag for repo:  ${repoName}"
	echo "Repository folder:  ${repoFolder}"
	if [ -d "${repoFolder}" ]
	then
		# The repository folder exists so cd to it and do the tag
		cd "${repoFolder}"
		if [ ${dryRun} = "true" ]
		then
			echo "Dry run:  Adding tag with:  git tag -a \"${tagName}\" -m \"${tagMessage}\""
			echo "Dry run:  git push origin --tags"
		else
			echo "Adding tag with:  git tag -a \"${tagName}\" -m \"${tagMessage}\""
			git tag -a "${tagName}" -m "${tagMessage}"
			git push origin --tags
		fi
	else
		# Git repository folder does not exist so skip
		echo "Repository folder does not exist.  Skipping"
		continue
	fi
done < ${repoList}
