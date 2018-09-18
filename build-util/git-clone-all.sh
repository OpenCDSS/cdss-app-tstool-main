#!/bin/sh
#
# Clone all repositories
# - this is used to set up a new development environment
# - it is assumed that cdss-app-tstool-main is already clone

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
# TSTool GitHub repo URL root
githubRootUrl="https://github.com/OpenWaterFoundation"
# TSTool product home is relative to the users files in a standard CDSS development files location
productHome="$home2/cdss-dev/TSTool"
# Git repos are located in the following
gitReposFolder="${productHome}/git-repos"
# Main repository in a group of repositories for a product
# - this is where the product repository list file will live
mainRepoFolder="${productHome}/git-repos/cdss-app-tstool-main"
gitRepoFolder=`dirname ${mainRepoFolder}`
# The following is a list of repositories including the main repository
# - one repo per line, no URL, just the repo name
# - repositories must have previously been cloned to local files
repoList="${mainRepoFolder}/build-util/product-repo-list.txt"

# Make sure that expected folders exist
if [ ! -d "${productHome}" ]
	then
	echo ""
	echo "Product folder \"${productHome}\" does not exist.  Exiting."
	exit 1
fi
if [ ! -d "${gitReposFolder}" ]
	then
	echo ""
	echo "Product git-repos folder \"${gitReposFolder}\" does not exist.  Exiting."
	exit 1
fi
if [ ! -d "${mainRepoFolder}" ]
	then
	echo ""
	echo "Main repo folder \"${mainRepoFolder}\" does not exist.  Exiting."
	exit 1
fi
if [ ! -f "${repoList}" ]
	then
	echo ""
	echo "Product repo list file \"${repoList}\" does not exist.  Exiting."
	exit 1
fi

while [ "1" = "1" ]
do
	echo ""
	echo "All TSTool repositories that don't alredy exist will be cloned to ${gitReposFolder}."
	echo "Repositories will use GitHub URL ${githubRootUrl}"
	read -p "Continue [y/n]?: " answer
	if [ "${answer}" = "y" ]
	then
		# Want to continue
		break
	else
		# Don't want to continue
		exit 0
	fi
done

# Change to the main git-repos folder
cd "${gitReposFolder}"
# Clone each repository in the product
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
	# Clone the repo
	repoFolder="${productHome}/git-repos/${repoName}"
	repoUrl="${githubRootUrl}/${repoName}"
	echo "================================================================================"
	echo "Cloning repo:  ${repoName}"
	echo "Repository folder:  ${repoFolder}"
	echo "Repository Url:  ${repoUrl}"
	if [ -d "${repoFolder}" ]
	then
		# The repository folder exists so don't cone
		echo "Repo folder already exists so skipping:  ${repoFolder}"
		continue
	else
		if [ ${dryRun} = "true" ]
		then
			echo "Dry run:  cloning repo with:  git clone \"${repoUrl}\""
		else
			git clone ${repoUrl}
		fi
	fi
done < ${repoList}
