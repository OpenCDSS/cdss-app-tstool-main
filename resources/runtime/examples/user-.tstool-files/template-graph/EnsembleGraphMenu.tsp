# EnsembleGraphMenu.tsp
#
# Default graph template to be used with time series ensembles,
# when no preprocessing command file should run.
# 
# Select an ensemble in TSTool results and use the "Graph - Line (Ensemble Graph)" choice.
# If statistics are provided in the wizard, then they will be computed and those
# time series will be available to this template.
# Otherwise, no statistics are created.
# 
# Alternatively, select the EnsembleCalcStatsDefault.tsp graph template,
# which runs a command file to calculate standard statistics.

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
# Do not run preprocessor with this version since statistics
# are expected to have been specified in the Time Series Results / Graph - Ensemble wizard
#TemplatePreprocessCommandFile = "EnsembleCalcStatsDefault.TSTool"

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
