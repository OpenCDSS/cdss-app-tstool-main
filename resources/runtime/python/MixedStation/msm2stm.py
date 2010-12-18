#
# Simple script to convert legacy CDSS Mixed Station Model
# time series file (fill-in.in or fill-in.out) to StateMod format
# Data are assumed to be in water year format (Oct - Sep)
# The output can then be compared to the results of the TSTool FillMixedStation command
# A sample of the fillin.in file is (skip first line, fill period grouped for a station):
#
#0010010000010530010190900005.025
#        091105001909   1     -999     -999     -999     -999     -999     -999
#        091105001909   2     -999     -999     -999     -999     -999     -999
#        091105001910   1     -999     -999     -999     -999     -999     -999
#        091105001910   2     -999     -999     -999     -999     -999     -999
#
# A sample of the fillin.out file is (note extra 1 in front of identifiers,
# different column widths, and exponential notation):
#
#       1091105001909   1   2791.0e   -999.0e   -999.0e   -999.0e   -999.0e   -999.0e
#       1091105001909   2   -999.0e  22114.2e   -999.0e   -999.0e   6550.9e   3703.8e
#       1091105001910   1   2791.0e   -999.0e   -999.0e   -999.0e   -999.0e   -999.0e
#       1091105001910   2   -999.0e  22114.2e   -999.0e   -999.0e   5299.8e   3792.9e

import sys

# Get the input file from the command line
if ( len(sys.argv) == 1 ):
    print "Usage:  msm2stm fillin.in|fillin.out"
    print ""
    print "Specify the filename to convert."
    print "The output name will be the same with \".stm"
    print ""
msmFile = sys.argv[1]
f = open(msmFile,"r")

# Check the file type because fill-in.in has an extra line at the top
isInput = False
if ( msmFile.upper() == "FILL-IN.IN" ):
    isInput = True
    valWidth = 9
elif ( msmFile.upper() == "FILL-IN.OUT" ):
    isInput = False
    valWidth = 10
else:
    print "Input file " + sys.argv[1] + " is unknown, must be fill-in.in or fill-in.out"
    quit()

lineCount = 0
# For data values, initialize with missing
v = [-999.0]*12
# Formatted StateMod lines, which will have year and ID.
# Therefore can sort the lines after formatting and output in correct order
newlines = []
year1 = -1
for line in f:
    lineCount = lineCount + 1
    if ( (isInput == True) and (lineCount == 1 ) ):
        # fill-in.in file so skip the first line
        continue
    # ID starts in column 8 (0 index), so skips the leading 1 in output files
    id = line[8:16]
    year = line[16:20]
    if ( year1 < 0 ):
        year1 = int(year)
    card = int(line[20:24].strip())
    for i in range(0,6):
        start = 24 + i*valWidth
        end = start + valWidth
        pos = (card - 1)*6 + i
        val = line[start:end]
        #print "start=" + str(start) + " end=" + str(end) + " pos=" + str(pos) + " val='" + val + "'"
        # Strip off trailing "e" (seen in output)
        val = val.replace("e","")
        v[pos] = float(val)
    if ( card == 1 ):
        # Start a new record
        newline = year + " " + "%-12s" % id 
    elif ( card == 2 ):
        # Add a line in StateMod format (2 input lines per 1 output line)
        # Concatenate the values
        for i in range(0,12):
            newline += "%7.0f." % v[i]
        newlines.append ( newline )
f.close()

# Output all the new lines
# Sort first because MSM input has one station full period, followed by next station, etc
# Sorting will properly sort by year and station, as per StateMod conventions

newlines.sort()

f = open(msmFile + ".stm", "w")
f.write("# File created by converting " + msmFile + " to a StateMod file, using msm2stm.py\n")
# Write water year header
f.write("   10/" + str(year1 - 1) + "  -      9/" + year +  "  af   WYR\n")
for newline in newlines:
    f.write(newline + "\n")
f.close()
