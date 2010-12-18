#
# Simple script to convert legacy CDSS Mixed Station Model summary file (*.sum) to a CSV
# This allows the legacy output to be compard with output from the TSTool FillRegression(),
# FillMOVE2(), and FillMixedStation() commands.
# A sample of the *.sum file is:
#
# # ********************************************************************************
# #
# # *.sum       Mixed Station Summary Information
# #
# #  Filename:  ColoF.sum
# #
# #  **** = 95% Confidence Interval Not Met
# #
# # ********************************************************************************
# # File History:
# # ********************************************************************************
# #
# # *.xbf       Filled Base Flow at Stream Gages - Mixed Station Model
# #
# #  Mixed Station Model Version:  PC (89/07/18) (Modified 6/8/99)
# #  Run Date and Time:  9/13/99 16:21:15
# #  Filename:  ColoF-1.xbf
# #  Regression:  Simple Linear Regression (Log Transform)
# #  Method:  SEP Dependent
# #  Independent Stations:  All
# #  First Year of Extended Record:  1909
# #  Min. No. of Concurrent Values Required:  5
# #  Confidence Interval:  95%
# #
# # ********************************************************************************
# #
# # 
# # ********************************************************************************
# # *.xbg       Base Flow at Stream Gages                        
# #
# #  UPPER COLORADO RIVER BASIN                                                     
# #  Historic Diversions made at respective historic diversion location             
# #
# #  Statemod Version:  8.24 (1999/07/19)
# #  Run date and time:    9/ 1/99  8: 5:50
# # 
# # ********************************************************************************
# # ********************************************************************************
# #
# # Individual Gage Mixed Station Summary Information:
# 
#   Basin Key:
#   K - KeyGages
#   C - ColoF
# 
# 
# *** Dependent Station Number 09010500, Variables 18 (cyclic) & 122 (non-cyclic) ***
# 
#           Original Sample     Filled Data
# Size =           520              1056
# Mean =          3.27              3.29
# Std Dev =      0.622             0.622
# 
# Average Standard Error of Prediction (SEP) =       17.1
# 
#                        Non-   Con-       # of Predictors
# Indep.          Cycl   Cycl   current  Cyclic    Non-Cycl                Cyclic Correlation Coefficients                Non-
# Station  Basin  Var #  Var #  Values  Pot  Act   Pot  Act   Oct  Nov  Dec  Jan  Feb  Mar  Apr  May  Jun  Jul  Aug  Sep  Cycl
# 
# 09112500   K       5    109     520   368   20   368    0  0.78 0.62 0.72 0.60 0.57 0.75 0.84 0.63 0.78 0.91 0.74 0.52  0.94
# 09239500   K      10    114     520   519   10   519    0  0.83 0.72 0.59 0.49 0.51 0.67 0.43 0.60 0.81 0.91 0.73 0.63  0.79
# 09251000   K      11    115     520   445   30   445    0  0.86 0.78 0.71 0.68 0.46 0.62 0.41 0.36 0.83 0.94 0.75 0.58  0.71
# 09340500   K      13    117     520   524    1   524    0  0.59 0.49 0.49 0.41 0.43 0.53 0.57 0.50 0.79 0.86 0.73 0.44  0.78
# 09011000   C      19    123     400   344  248   344   20  0.97 0.95 0.85 0.80 0.79 0.78 0.97 0.90 0.94 0.98 0.95 0.97  0.99
# 09019500   C      20    124     185   106    1   257    0  **** **** **** **** **** **** **** 0.89 0.40 0.95 0.90 0.83  0.94
# 09024000   C      22    126     520   512   25   512    1  0.78 0.67 0.52 0.44 0.39 0.63 0.83 0.91 0.84 0.93 0.80 0.58  0.97
# 09032500   C      27    131      88   186   64   224    0  0.84 **** **** 0.76 0.89 0.88 0.81 0.77 0.93 0.99 0.96 0.87  0.95
# 09034500   C      31    135     495   488   52   488    0  0.88 0.83 0.48 0.37 0.41 0.56 0.78 0.81 0.84 0.96 0.92 0.84  0.95
#
# ... repeat until blank lines and then another block started with "*** Dependent station..."

import sys
import re

# Get the input file from the command line
if ( len(sys.argv) == 1 ):
    print "Usage:  msm-sum-to-csv file.sum"
    print ""
    print "Specify the filename to convert."
    print "The output name will be the same with .csv for dependent values (general before/after statistics)"
    print "A second file ending in 2.csv will contain the dependent-indepedent values (relationship statistics)"
    print ""
# Open the input file
msmFile = sys.argv[1]
input = open(msmFile,"r")

# Open the output file
output = open(msmFile + ".csv", "w")
output2 = open(msmFile + "2.csv", "w")
output.write("# File created by converting " + msmFile + " to a CSV file, using msm-sum-to-csv.py\n")
output2.write("# File created by converting " + msmFile + " to a CSV file, using msm-sum-to-csv.py\n")
# Write the old header...
output.write("#----------------------------------------------------------\n")
output2.write("#----------------------------------------------------------\n")

lineCount = 0
firstDependentFound = False # Used to detect the end of the header
# Independent statistics
tsidDep = ""
sizeOrig = ""
sizeFilled = ""
meanOrig = ""
meanFilled = ""
sizeOrig = ""
sizeFilled = ""
stdDevOrig = ""
stdDevFilled = ""
avgSEP = ""

# Read each line in the input and process to CSV files...
# Save data lines in a list so they can be sorted - output at the end
output1List = []
output2List = []
for line in input:
    #print line
    lineCount = lineCount + 1
    if ( line[0:21] == '*** Dependent Station' ):
        tsidDep = line[29:]
        commaPos = tsidDep.find(',')
        tsidDep = tsidDep[0:commaPos].strip();
        if ( firstDependentFound == False ):
            # First dependent so write the main column headings...
            # Output has depedent statistics
            output.write("# Note that the statistics will be on the transformed data.\n")
            output.write("# See MSM information above for whether transformed.\n")
            output.write("#--------------------------------------------------------------------------------------\n")
            output.write("#Dependent,Size,SizeFilled,Mean,MeanFilled,StdDev,StdDevFilled,AverageSEP\n")
            output.write('"TSID","N","Nfilled","MeanX","MeanXfilled","SX","SXfilled","SEP"\n')
            # Output 2 has relationship statistics
            # Write the column headings, original and consistent with TSTool
            # Note that the original is in water years and TSTool is in calendar
            output2.write("# The original ****, which indicates not passing the T test, is replaced with blanks below\n")
            output2.write("#--------------------------------------------------------------------------------------\n")
            output2.write("#Dependent,IndepStation,Basin,ConcurrentValues,R_Jan,R_Feb,R_Mar,R_Apr,R_May,R_Jun,R_Jul,R_Aug,R_Sep,R_Oct,R_Nov,R_Dec,R\n")
            output2.write('"TSID","TSID-Independent","Basin","N1","R_1","R_2","R_3","R_4","R_5","R_6","R_7","R_8","R_9","R_10","R_11","R_12","R"\n')
            firstDependentFound = True
    else:
        # Not a new station so either in the main header or processing a dependent
        if ( firstDependentFound == False ):
            # Output the main header
            output.write( "# " + line )
            output2.write( "# " + line )
        else:
            # Process a data section line (not main header)
            # Put the blank line start first
            if ( line[0:5].strip() == '' ):
                # Blank or description lines in dependent section - ignore line
                pass
            elif ( line[0:5] == '# ***' ):
                # End of file
                break
            elif ( (line[0:6] == 'Indep.') or (line[0:7].strip() == 'Station') ):
                # Section header
                continue
            elif ( line[0:6] == 'Size =' ):
                sizeOrig = line[10:25].strip()
                sizeFilled = line[25:].strip()
            elif ( line[0:6] == 'Mean =' ):
                meanOrig = line[10:25].strip()
                meanFilled = line[25:].strip()
            elif ( line[0:9] == 'Std Dev =' ):
                stdDevOrig = line[10:25].strip()
                stdDevFilled = line[25:].strip()
            elif ( line[0:22] == 'Average Standard Error' ):
                avgSEP = line[45:].strip()
                # Now have all the dependent statistics
                output1List.append(tsidDep + "," + sizeOrig + "," + sizeFilled + "," + meanOrig + "," + meanFilled + "," +
                    stdDevOrig + "," + stdDevFilled + "," + avgSEP )
            else:
                # Process the dependent line (replace spaces with commas and **** with blanks)
                # First replace multiple spaces with one space
                lineOneSpace = re.sub("\s+", " ", line).strip()
                print lineOneSpace
                # Now split with the space as the delimiter
                parts = lineOneSpace.split(' ')
                # Dependent from earlier read, independent, basin, and concurrent (overlapping) values
                outputLine = tsidDep + "," + parts[0] + "," + parts[1] + "," + parts[4] + ","
                # Output R, converting water year to calendar year
                # TODO SAM 2010-12-17 might need to remove leading zero but code commented out for now
                for part in parts[12:21]:
                    # Convert **** to space
                    if ( part == '****' ):
                         part = ''
                    #elif ( part[0:1] == '0' ):
                    #     part = part[1:]
                    outputLine = outputLine + part + ","
                for part in parts[9:12]:
                    # Convert **** to space
                    if ( part == '****' ):
                         part = ''
                    #elif ( part[0:1] == '0' ):
                    #     part = part[1:]
                    outputLine = outputLine + part + ","
                # Now do the year total
                part = parts[21]
                if ( part == '****' ):
                     part = ''
                #elif ( part[0:1] == '0' ):
                #     part = part[1:]
                outputLine = outputLine + part + ","
                # Add to the list (take off trailing comma)...
                output2List.append(outputLine[:len(outputLine) - 1])

# Sort the output lines and output

output1List.sort()
for outline in output1List:
    output.write(outline + "\n")

output2List.sort()
for outline in output2List:
    output2.write(outline + "\n")

input.close()
output.close()
output2.close()
