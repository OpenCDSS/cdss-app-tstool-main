# build-util #

This folder contains useful scripts and other files used in the development/build environment.

| **File/Folder** | **Description** |
| -- | -- |
| `copy-to-co-dnr-gcp.bash` | Copy the TSTool installer to State of Colorado GCP server.  The istaller is in the repository `/dist` folder and the TSTool version correspond to current code version. |
| `create-gcp-tstool-index.bash` | Create and upload an index of TSTool installers. |
| `git-check-tstool.sh` | Determine status of TSTool repositories compared to remote repositories. |
| `git-clone-all-tstool.sh | Clone all TSTool repositories, helpful when first setting up a development environment. |
| `git-tag-all-tstool.sh` | Tag all TSTool repositories with the same tag, used to coordinate releases. |
| `git-util/` | General Git utilities called by `git-util*` scripts. |
| `old-runners/` | Old Eclipse run scripts. |
| `product-repo-list.txt` | List of repositories comprising TSTool, used by `git-*.sh` scripts. |
| `run-eclipse.bash` | Experimental script to run Eclipse from Git Bash (**the `.bat` file is known to work). |
| `run-eclipse-win64.cmd` | Run Eclipse 64-bit IDE for Windows - latest tested development environment. |
| `x-run-eclipse-win32.bat` | Run Eclipse 32-bit for Windows - latest tested development environment. **Obsolete - use 64-bit environment for development .** |

