# TSAsEnsembleCalcStatsDefault.tsp
#
# Default graph template to be used with times series converted to an ensemble on the fly.
# Select a time series in TSTool and use the "Graph with template:  TSAsEnsembleDefault.tsp" choice.
# The TSAsEnsembleCalcStatsDefault.TSTool command file will be run to convert the time series
# to an ensemble, and also calculate some standard statistics.
# The resulting time series traces will be graphed using this template.

[Product]

ProductType = "Graph"
TotalWidth = "700"
TotalHeight = "550"
<#assign TemplateTSDescription = TemplateTSDescriptionList[0]>
<#assign TemplateTSLocationID = TemplateTSLocationIDList[0]>
#MainTitleString = "${TemplateTSDescription} (${TemplateTSLocationID})"
# The TemplateGraphTitle property is set in TSAsEnsembleCalcStatsDefault.TSTool
MainTitleString = "${TemplateGraphTitle}"
TemplatePreprocessCommandFile = "TSAsEnsembleCalcStatsDefault.TSTool"

[SubProduct 1]

GraphType = "Line"
LeftYAxisLegendPosition = "Right"
LeftYAxisTitlePosition = "LeftOfAxis"
LeftYAxisTitleRotation = "270"
LegendFormat = "%z"
LeftYAxisLabelPrecision = "0"
RightYAxisGraphType = "None"
RightYAxisIgnoreUnits = "false"

<#list TemplateTSAliasList as TemplateTSAlias>
[Data 1.${TemplateTSAlias_index + 1}]

<#assign TemplateTSID = TemplateTSIDList[TemplateTSAlias_index]>
TSID = "${TemplateTSID}"
TSAlias = "${TemplateTSAlias}"

</#list>
