# EnsembleCalcStatsDefault.tsp
#
# Default graph template to be used with time series ensembles,
# when preprocessing command file should run.
# 
# Select an ensemble in TSTool results and use the "Graph with Template: EnsembleCalcStatsDefault.tsp" choice.
# Statistics will be calculated for the ensemble and added as time series.

[Product]

ProductType = "Graph"
TotalWidth = "700"
TotalHeight = "550"
<#if TemplateTSEnsembleNameList?size != 0>
<#assign TemplateTSEnsembleName = TemplateTSEnsembleNameList[0]>
MainTitleString = "${TemplateTSEnsembleName}"
<#else>
<#assign TemplateTSDescription = TemplateTSDescriptionList[0]>
<#assign TemplateTSLocationID = TemplateTSLocationIDList[0]>
MainTitleString = "${TemplateTSDescription} (${TemplateTSLocationID})"
</#if>
# Run the preprocessor command file to calculate statistics on the ensemble.
TemplatePreprocessCommandFile = "EnsembleCalcStatsDefault.TSTool"

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
