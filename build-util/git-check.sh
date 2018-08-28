#!/bin/sh
#
# git-check.sh
#
# Check the status of multiple repositories for this project and indicate whether pull
# or push or other action is needed.
# - currently must adhere to prescribed folder structure
# - useful when multiple repositories form a product
# - this script does not do anything to change repositories

# Product development main folder
# - top-level folder for the product
productHome="$HOME/cdss-dev/TSTool"
# Main repository in a group of repositories for a product
# - this is where the product repository list file will live
mainRepo="${productHome}/git-repos/cdss-app-tstool-main"
# The following is a list of repositories including the main repository
# - one repo per line, no URL, just the repo name
# - repositories must have previously been cloned to local files
repoList="${mainRepo}/build-util/product-repo-list.txt"

# Function to check the status of local compared to remote repository
# - see:  https://stackoverflow.com/questions/3258243/check-if-pull-needed-in-git
checkRepoStatus()
{
	# Get the remote information
	git remote update
	# Start code from above
	UPSTREAM=${1:-'@{u}'}
	LOCAL=$(git rev-parse @)
	REMOTE=$(git rev-parse "$UPSTREAM")
	BASE=$(git merge-base @ "$UPSTREAM")

	if [ "$LOCAL" = "$REMOTE" ]; then
		echo "------------------"
		echo "Up-to-date"
		echo "------------------"
	elif [ "$LOCAL" = "$BASE" ]; then
		echo "------------------"
		echo "Need to pull"
		echo "------------------"
	elif [ "$REMOTE" = "$BASE" ]; then
		echo "------------------"
		echo "Need to push"
		echo "------------------"
	else
		echo "------------------"
		echo "Diverged"
		echo "------------------"
	fi
	# End code from above
}

# Check for local folder existence and exit if not as expected
# - ensures that other logic will work as expected in folder structure

if [ ! -d "${productHome}" ]
	then
	echo ""
	echo "Product home folder does not exit:  ${productHome}"
	echo "Exiting."
	echo ""
	exit 1
fi
if [ ! -d "${mainRepo}" ]
	then
	echo ""
	echo "Main repo folder does not exit:  ${mainRepo}"
	echo "Exiting."
	echo ""
	exit 1
fi
if [ ! -f "${repoList}" ]
	then
	echo ""
	echo "Product repo list file does not exit:  ${repoList}"
	echo "Exiting."
	echo ""
	exit 1
fi

# Change folders to each repository and run the function to check that repository status
# against its upstream repository.
# - ignore comment lines starting with #
grep -v '^#' ${repoList} | while read repoName
	do
	# Check the status on the specific repository
	productRepoFolder="${productHome}/git-repos/${repoName}"
	echo "========================================================================"
	echo "Checking status of repo:  $repoName"
	if [ ! -d "${productRepoFolder}" ]
		then
		echo ""
		echo "Product repo folder does not exit:  ${productRepoFolder}"
		echo "Skipping."
		continue
	else
		cd ${productRepoFolder}
	fi
	checkRepoStatus
done

# Done
exit 0
