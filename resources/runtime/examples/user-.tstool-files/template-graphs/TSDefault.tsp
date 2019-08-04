# TSDefault.tsp
#
# Default graph template to be used with individual time series.
# Select a time series in TSTool and use the "Graph with template:" button with "TSDefault.tsp" template.

[Product]

ProductType = "Graph"
TotalWidth = "600"
TotalHeight = "400"
<#assign TemplateTSDescription = TemplateTSDescriptionList[0]>
<#assign TemplateTSLocationID = TemplateTSLocationIDList[0]>
MainTitleString = "${TemplateTSDescription} (${TemplateTSLocationID})"

# This is probably not needed by default for single time series
#TemplatePreprocessCommandFile = "TSDefault.TSTool"

[SubProduct 1]

GraphType = "Line"
LeftYAxisTitlePosition = "LeftOfAxis"
LeftYAxisTitleRotation = "270"
RightYAxisGraphType = "None"
RightYAxisIgnoreUnits = "false"

# The following uses TSID without the trailing ~DataStore (or ~InputType) so read is not attempted

<#list TemplateTSAliasList as TemplateTSAlias>
[Data 1.${TemplateTSAlias_index + 1}]

<#assign TemplateTSIDShort = TemplateTSIDShortList[TemplateTSAlias_index]>
TSID = "${TemplateTSIDShort}"
TSAlias = "${TemplateTSAlias}"

</#list>
