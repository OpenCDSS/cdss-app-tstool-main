#
# Simple script to convert legacy CDSS Mixed Station Model statistics summary file (*.sts) to a CSV
# This allows the legacy output to be compard with output from the TSTool FillRegression(),
# FillMOVE2(), and FillMixedStation() commands.
# A sample of the *.sts file is:
#
## ********************************************************************************
##
## *.sts       Mixed Station Statistics Summary
##
##  Filename:  ColoF.sts
##
##  **** = 95% Confidence Interval Not Met
##
## ********************************************************************************
## File History:
## ********************************************************************************
##
## *.xbf       Filled Base Flow at Stream Gages - Mixed Station Model
##
##  Mixed Station Model Version:  PC (89/07/18) (Modified 6/8/99)
##  Run Date and Time:  9/13/99 16:21:15
##  Filename:  ColoF-1.xbf
##  Regression:  Simple Linear Regression (Log Transform)
##  Method:  SEP Dependent
##  Independent Stations:  All
##  First Year of Extended Record:  1909
##  Min. No. of Concurrent Values Required:  5
##  Confidence Interval:  95%
##
## ********************************************************************************
##
## 
## ********************************************************************************
## *.xbg       Base Flow at Stream Gages                        
##
##  UPPER COLORADO RIVER BASIN                                                     
##  Historic Diversions made at respective historic diversion location             
##
##  Statemod Version:  8.24 (1999/07/19)
##  Run date and time:    9/ 1/99  8: 5:50
## 
## ********************************************************************************
## ********************************************************************************
## Summary Statistics, Original and Extended Record:
#
#  Definitions:
#  Mean -  measures the central tendency of the sample
#  Standard Deviation -  measures the dispersion of sample values around the mean
#  Skewness Coefficient -  measures the degree of asymmetry of the frequency distribution of the data
#  Lag-1 Serial Coefficient -  measures the degree of linear dependence among successive values of the series
#
#*** Station No:  09010500 ***
#*** Number of Years:   88 ***
#
#                            Oct     Nov     Dec     Jan     Feb     Mar     Apr     May     Jun     Jul     Aug     Sep   Total
#
# Original No.Months/Yrs      43      43      43      43      43      43      43      43      44      44      44      44      43
# Extended No.Months/Yrs      88      88      88      88      88      88      88      88      88      88      88      88      88
#
# Original Mean             1449.    893.    596.    478.    395.    460.   1694.  11683.  26127.  13468.   4165.   2365.  64002.
# Extended Mean             1484.    933.    639.    525.    411.    453.   1892.  12617.  27351.  13704.   4177.   2355.  66540.
# Difference - Means %       2.4%    4.4%    7.2%    9.9%    4.1%   -1.5%   11.7%    8.0%    4.7%    1.8%    0.3%   -0.4%    4.0%
#
# Orginal Median            1218.    766.    541.    455.    392.    426.   1706.  10731.  26343.  11007.   3424.   1689.  63955.
# Extended Median           1343.    843.    580.    510.    391.    432.   1799.  12424.  25329.  12435.   3528.   1778.  63561.
#
# Original StD.              829.    376.    204.    141.    103.    118.    877.   4233.   7316.   7902.   2175.   2905.  16084.
# Extended StD.              715.    381.    216.    184.    143.    111.   1118.   4261.   8976.   7399.   1972.   2431.  18311.
# Difference - StDs. %     -13.8%    1.2%    5.9%   30.1%   38.8%   -5.9%   27.5%    0.6%   22.7%   -6.4%   -9.3%  -16.3%   13.8%
#
# Original Coef. of Var.     0.57    0.42    0.34    0.30    0.26    0.26    0.52    0.36    0.28    0.59    0.52    1.23    0.25
# Extended Corf. of Var.     0.48    0.41    0.34    0.35    0.35    0.25    0.59    0.34    0.33    0.54    0.47    1.03    0.28
#
# Difference - C.V.  %     -15.8%   -3.1%   -1.2%   18.4%   33.3%   -4.5%   14.1%   -6.8%   17.2%   -8.0%   -9.6%  -16.0%    9.5%
#
# Original Skewness          2.46    1.51    0.90    0.31    0.08    0.56    0.75    0.41    0.00    0.91    1.43    5.68    0.06
# Extended Skewness          1.91    1.18    0.86    1.45    2.12    0.71    2.12    0.47    1.02    0.97    1.08    5.63    0.69
# Difference - Skews  %    -22.3%  -22.4%   -4.6%  366.5% 2458.1%   27.5%  180.4%   14.2%    0.0%    6.3%  -24.8%   -0.8% 1098.4%
#
# Original Lag-1 Correl.     0.16    0.93    0.88    0.91    0.93    0.76    0.30    0.26   -0.04    0.63    0.85    0.03    0.12
# Extended Lag-1 Correl.     0.27    0.89    0.88    0.86    0.87    0.54    0.16    0.24    0.12    0.68    0.81    0.11    0.24
# Difference - Corrs. %     68.9%   -4.3%   -0.5%   -5.3%   -6.6%  -28.7%  -45.6%   -4.5%    0.0%    8.6%   -4.3%  255.3%  105.0%
#
# All Data was Filled with the Mixed Station Model
#
#
# ... repeat until blank lines and then another block started with "*** Dependent station..."

import sys
import re

def processDataString ( line ):
    """
    Process a data line and return the 13 data (Jan-Dec and annual),
    suitable for output to a CSV.
    """
    # Strip off the text to the left and the total, which can be smashed
    # against the last month...
    lineData = line[24:120].strip()
    total = line[120:].strip()
    # Replace multiple spaces with one space
    lineDataOneSpace = re.sub("\s+", " ", lineData)
    #print lineDataOneSpace
    parts = lineDataOneSpace.split(' ')
    # Concatenate each value, converting water year to calendar year and
    # adjusting strings if necessary
    outputLine = ""
    for part in parts[3:]:
        # Convert **** to space
        #if ( part == '****' ):
        #    part = ''
        #elif ( part[0:1] == '0' ):
        #     part = part[1:]
        outputLine = outputLine + part + ","
    for part in parts[0:3]:
        # Convert **** to space
        #if ( part == '****' ):
        #    part = ''
        #elif ( part[0:1] == '0' ):
        #     part = part[1:]
        outputLine = outputLine + part + ","
    # Now do the year total
    outputLine = outputLine + total + ","
    # Return without the trailing comma
    return outputLine[:len(outputLine) - 1]

# Start the main script

# Get the input file from the command line
if ( len(sys.argv) == 1 ):
    print ""
    print "Usage:  msm-sts-to-csv file.sts"
    print ""
    print "Specify the filename to convert."
    print "The output name will be the same with .csv"
    print ""
# Open the input file
msmFile = sys.argv[1]
input = open(msmFile,"r")

# Open the output file
output = open(msmFile + ".csv", "w")
output.write("# File created by converting " + msmFile + " to a CSV file, using msm-sts-to-csv.py\n")
# Write the old header...
output.write("#----------------------------------------------------------\n")

lineCount = 0
firstDependentFound = False # Used to detect the end of the header

# Read each line in the input and process to CSV files...
# Save data lines in a list so they can be sorted - output at the end
outputList = []
for line in input:
    #print line
    lineCount = lineCount + 1
    if ( line[0:14] == '*** Station No' ):
        tsidDep = line[17:]
        astPos = tsidDep.find('*')
        tsidDep = tsidDep[0:astPos].strip();
        if ( firstDependentFound == False ):
            # First dependent so write the main column headings...
            output.write("# Statistics are on the original data units - see MSM information above.\n")
            output.write("#--------------------------------------------------------------------------------------\n")
            # Put together headers and then concatenate
            NOrig='"NOrig_1","NOrig_2","NOrig_3","NOrig_4","NOrig_5","NOrig_6","NOrig_7","NOrig_8","NOrig_9","NOrig_10","NOrig_11","NOrig_12","NOrig"'
            NFilled='"NFilled_1","NFilled_2","NFilled_3","NFilled_4","NFilled_5","NFilled_6","NFilled_7","NFilled_8","NFilled_9","NFilled_10","NFilled_11","NFilled_12","NFilled"'
            MeanOrig='"MeanOrig_1","MeanOrig_2","MeanOrig_3","MeanOrig_4","MeanOrig_5","MeanOrig_6","MeanOrig_7","MeanOrig_8","MeanOrig_9","MeanOrig_10","MeanOrig_11","MeanOrig_12","MeanOrig"'
            MeanFilled='"MeanFilled_1","MeanFilled_2","MeanFilled_3","MeanFilled_4","MeanFilled_5","MeanFilled_6","MeanFilled_7","MeanFilled_8","MeanFilled_9","MeanFilled_10","MeanFilled_11","MeanFilled_12","MeanFilled"'
            MedianOrig='"MedianOrig_1","MedianOrig_2","MedianOrig_3","MedianOrig_4","MedianOrig_5","MedianOrig_6","MedianOrig_7","MedianOrig_8","MedianOrig_9","MedianOrig_10","MedianOrig_11","MedianOrig_12","MedianOrig"'
            MedianFilled='"MedianFilled_1","MedianFilled_2","MedianFilled_3","MedianFilled_4","MedianFilled_5","MedianFilled_6","MedianFilled_7","MedianFilled_8","MedianFilled_9","MedianFilled_10","MedianFilled_11","MedianFilled_12","MedianFilled"'
            StdDevOrig='"StdDevOrig_1","StdDevOrig_2","StdDevOrig_3","StdDevOrig_4","StdDevOrig_5","StdDevOrig_6","StdDevOrig_7","StdDevOrig_8","StdDevOrig_9","StdDevOrig_10","StdDevOrig_11","StdDevOrig_12","StdDevOrig"'
            StdDevFilled='"StdDevFilled_1","StdDevFilled_2","StdDevFilled_3","StdDevFilled_4","StdDevFilled_5","StdDevFilled_6","StdDevFilled_7","StdDevFilled_8","StdDevFilled_9","StdDevFilled_10","StdDevFilled_11","StdDevFilled_12","StdDevFilled"'
            CVOrig='"CVOrig_1","CVOrig_2","CVOrig_3","CVOrig_4","CVOrig_5","CVOrig_6","CVOrig_7","CVOrig_8","CVOrig_9","CVOrig_10","CVOrig_11","CVOrig_12","CVOrig"'
            CVFilled='"CVFilled_1","CVFilled_2","CVFilled_3","CVFilled_4","CVFilled_5","CVFilled_6","CVFilled_7","CVFilled_8","CVFilled_9","CVFilled_10","CVFilled_11","CVFilled_12","CVFilled"'
            SkewOrig='"SkewOrig_1","SkewOrig_2","SkewOrig_3","SkewOrig_4","SkewOrig_5","SkewOrig_6","SkewOrig_7","SkewOrig_8","SkewOrig_9","SkewOrig_10","SkewOrig_11","SkewOrig_12","SkewOrig"'
            SkewFilled='"SkewFilled_1","SkewFilled_2","SkewFilled_3","SkewFilled_4","SkewFilled_5","SkewFilled_6","SkewFilled_7","SkewFilled_8","SkewFilled_9","SkewFilled_10","SkewFilled_11","SkewFilled_12","SkewFilled"'
            Lag1Orig='"Lag1Orig_1","Lag1Orig_2","Lag1Orig_3","Lag1Orig_4","Lag1Orig_5","Lag1Orig_6","Lag1Orig_7","Lag1Orig_8","Lag1Orig_9","Lag1Orig_10","Lag1Orig_11","Lag1Orig_12","Lag1Orig"'
            Lag1Filled='"Lag1Filled_1","Lag1Filled_2","Lag1Filled_3","Lag1Filled_4","Lag1Filled_5","Lag1Filled_6","Lag1Filled_7","Lag1Filled_8","Lag1Filled_9","Lag1Filled_10","Lag1Filled_11","Lag1Filled_12","Lag1Filled"'
            output.write('"TSID",'+NOrig+","+NFilled+","+MeanOrig+","+MeanFilled+","+MedianOrig+","+MedianFilled+","+
                StdDevOrig+","+StdDevFilled+","+CVOrig+","+CVFilled+","+SkewOrig+","+SkewFilled+","+Lag1Orig+","+Lag1Filled+"\n")
            firstDependentFound = True
    else:
        # Not a new station so either in the main header or processing a dependent
        if ( firstDependentFound == False ):
            # Output the main header
            output.write( "# " + line )
        else:
            # Process a data section line (not main header)
            # Put the blank line start first
            if ( (len(line) < 1) or (line[0:5].strip() == '') ):
                # Blank or description lines in dependent section - ignore line
                pass
            elif ( line[0:4] == '*** ' ):
                # Other section header
                continue
            elif ( line[0:12] == ' Original No' ):
                NOrigVals = processDataString(line)
            elif ( line[0:12] == ' Extended No' ):
                NFilledVals = processDataString(line)
            elif ( line[0:14] == ' Original Mean' ):
                MeanOrigVals = processDataString(line)
            elif ( line[0:14] == ' Extended Mean' ):
                MeanFilledVals = processDataString(line)
            elif ( line[0:15] == ' Orginal Median' ):
                MedianOrigVals = processDataString(line)
            elif ( line[0:16] == ' Extended Median' ):
                MedianFilledVals = processDataString(line)
            elif ( line[0:13] == ' Original StD' ):
                StdDevOrigVals = processDataString(line)
            elif ( line[0:13] == ' Extended StD' ):
                StdDevFilledVals = processDataString(line)
            elif ( line[0:22] == ' Original Coef. of Var' ):
                CVOrigVals = processDataString(line)
            elif ( line[0:22] == ' Extended Corf. of Var' ):
                CVFilledVals = processDataString(line)
            elif ( line[0:18] == ' Original Skewness' ):
                SkewOrigVals = processDataString(line)
            elif ( line[0:18] == ' Extended Skewness' ):
                SkewFilledVals = processDataString(line)
            elif ( line[0:15] == ' Original Lag-1' ):
                Lag1OrigVals = processDataString(line)
            elif ( line[0:15] == ' Extended Lag-1' ):
                Lag1FilledVals = processDataString(line)
                # Now have all the statistics so create the full line
                outputList.append(tsidDep+","+NOrigVals+","+NFilledVals+","+
                    MeanOrigVals+","+MeanFilledVals+","+MedianOrigVals+","+MedianFilledVals+","+
                    StdDevOrigVals+","+StdDevFilledVals+","+CVOrigVals+","+CVFilledVals+","+
                    SkewOrigVals+","+SkewFilledVals+","+Lag1OrigVals+","+Lag1FilledVals)

# Sort the output lines and output

outputList.sort()
for outline in outputList:
    output.write(outline + "\n")

input.close()
output.close()
