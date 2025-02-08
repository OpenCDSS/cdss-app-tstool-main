// TSToolConstants - user interface constants

/* NoticeStart

TSTool
TSTool is a part of Colorado's Decision Support Systems (CDSS)
Copyright (C) 1994-2023 Colorado Department of Natural Resources

TSTool is free software:  you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    TSTool is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with TSTool.  If not, see <https://www.gnu.org/licenses/>.

NoticeEnd */

package DWR.DMI.tstool;

/**
 * TSTool user interface constants.
 * Many strings are used to define menus and other interface features.
 * These definitions require many code lines and make the main interface code more difficult to maintain.
 * Therefore, define in this separate code file.
 */
public class TSToolConstants {

	// Used with HEC-DSS because filters provide data type and time step choices.
	public static final String

	DATA_TYPE_USE_FILTERS = "Use filters below",

	// Input types for the __input_type_JComboBox.
	// Datastores are NOT listed here; consequently, the following are files or databases that have not been converted to datastores.

	INPUT_TYPE_DateValue = "DateValue",
	INPUT_TYPE_HECDSS = "HEC-DSS",
	INPUT_TYPE_HydroBase = "HydroBase",
	INPUT_TYPE_MODSIM = "MODSIM",
	INPUT_TYPE_NWSCARD = "NWSCARD",
	INPUT_TYPE_NWSRFS_FS5Files = "NWSRFS_FS5Files",
	INPUT_TYPE_NWSRFS_ESPTraceEnsemble = "NWSRFS_ESPTraceEnsemble",
	INPUT_TYPE_RiverWare = "RiverWare",
	INPUT_TYPE_StateCU = "StateCU",
	INPUT_TYPE_StateCUB = "StateCUB",
	INPUT_TYPE_StateMod = "StateMod",
	INPUT_TYPE_StateModB = "StateModB",
	INPUT_TYPE_UsgsNwisRdb = "UsgsNwisRdb",
	INPUT_TYPE_WaterML = "WaterML",

	// Time steps for __time_step_JComboBox.

	TIMESTEP_AUTO = "Auto",
	TIMESTEP_MINUTE = "Minute",
	TIMESTEP_HOUR = "Hour",
	TIMESTEP_DAY = "Day",
	TIMESTEP_MONTH = "Month",
	TIMESTEP_YEAR = "Year",
	TIMESTEP_IRREGULAR = "Irregular", // Use for real-time where interval is not known.

	// Used when data type and time step choices are not available but are automatically handled,
	// such as in DateValue time series files.
	DATA_TYPE_AUTO = "Auto";

	/**
	Path to icons/graphics in class path.
	*/
	public static final String TOOL_ICON_PATH = "/DWR/DMI/tstool";

	/**
	 * Command indent number of spaces for each level.
	 */
	public static final String INDENT_SPACES = "    ";

	/**
	Maximum number of files in recent files, taken from TSToolSession history.
	*/
	public static final int MAX_RECENT_FILES = 20;

	/**
	Fixed-width font - courier
	*/
	public static final String FIXED_WIDTH_FONT = "Courier";

	/**
	Command font - looks better than courier
	*/
	public static final String COMMANDS_FONT = "Lucida Console";

	/**
	 * TODO smalers 2022-11-23 can this be removed since menus have been reworked long ago?
	 */
	public static final String TAB = "";

	/**
	String to display in input name combo box to indicate that the NWSRFS_FS5Files
	input type should use the current apps defaults to find the files to read.
 	*/
	public static final String USE_APPS_DEFAULTS = "Use Apps Defaults";

	/**
	String to display in input name combo box to indicate that something should be selected.
	The selection will force an event to happen, which will allow the proper settings.
	*/
	public static final String PLEASE_SELECT = "Please select...";

	/**
	String to display in input name combo box to browse for a new file.
	*/
	public static final String BROWSE = "Browse...";

	// Button: String labels for buttons and menus.

	// Buttons (in order from top to bottom of GUI).

	public static final String

	BUTTON_TOP_GET_TIME_SERIES = "Get Time Series List",
	BUTTON_TOP_COPY_ALL_TO_COMMANDS = "Copy All to Commands",
	BUTTON_TOP_COPY_SELECTED_TO_COMMANDS = "Copy Selected to Commands",

	Button_RunSelectedCommands_String = "Run Selected Commands",
	Button_RunAllCommands_String = "Run All Commands",
	Button_ClearCommands_String = "Clear Commands",

	// TODO smalers 2017-04-16 Why is this called a button?  Seems to only be in menus.
	BUTTON_TS_SELECT_ALL = "Select All for Output",
	BUTTON_TS_DESELECT_ALL = "Deselect All",

	// Menus in order that they appear in the user interface.
	// "ActionString"s are defined where needed to avoid ambiguity in the menu labels.

	// Popup only.

	CommandsPopup_Edit_AsText_String = "Edit - as text (do not use command editor)",
	CommandsPopup_FindCommands_String = "Find Command(s)...",

	CommandsPopup_ShowCommandStatus_String = "Show Command Status (Success/Warning/Failure)",

	CommandsPopup_IndentRight_String = "> Indent Right",
	CommandsPopup_IndentLeft_String = "< Indent Left",

	// Menu: File

	File_String = "File",
	   File_New_String = "New",
           File_New_CommandFile_String = "Command File",
		File_Open_String = "Open",
			File_Open_CommandFile_String = "Command File...",
			File_Open_CommandFileNoDiscovery_String = "Command File (no discovery)...",
			File_Open_HydroBase_String = "HydroBase...",
			File_Open_ReclamationHDB_String = "Reclamation HDB...",
		File_Save_String = "Save",
			File_Save_Commands_String = "Commands",
			File_Save_CommandsAs_String = "Commands As...",
			// TODO smalers 2023-02-19 remove ASAP.
			//File_Save_CommandsAsVersion9_String = "Commands As (Version 9 Syntax)...",
			File_Save_TimeSeriesAs_String = "Time Series As...",
		File_CheckForUpdate_String = "Check for Updated Command File",
		File_Print_String = "Print",
			File_Print_Commands_String = "Commands...",
		File_Properties_String = "Properties",
			File_Properties_CommandsRun_String="Commands Run",
			File_Properties_TSToolSession_String="TSTool Session",
			File_Properties_HydroBase_String ="HydroBase",
			File_Properties_NWSRFSFS5Files_String = "NWSRFS FS5 Files",
		File_SetWorkingDirectory_String = "Set Working Directory...",
		File_Exit_String = "Exit",

	// Menu: Edit

	Edit_String = "Edit",
		Edit_CutCommands_String = "Cut Command(s)",
		Edit_CopyCommands_String = "Copy Command(s)",
		Edit_PasteCommands_String = "Paste Command(s) (after last selected)",
		Edit_DeleteCommands_String = "Delete Command(s)",
		Edit_SelectAllCommands_String ="Select All Commands",
		Edit_DeselectAllCommands_String = "Deselect All Commands",
		Edit_CommandWithErrorChecking_String = "Command...",
		Edit_ConvertSelectedCommandsToComments_String = "Convert selected commands to # comments",
		Edit_ConvertSelectedCommandsFromComments_String =	"Convert selected commands from # comments",
		Edit_ConvertTSIDTo_ReadTimeSeries_String = "Convert TSID command to general ReadTimeSeries() command",
		Edit_ConvertTSIDTo_ReadCommand_String = "Convert TSID command to specific Read...() command",

	// Menu: View

	View_String = "View",
		View_CommandFileDiff_String = "Command File Diff",
		View_CommandFileSourceDiff_String = "Command File Source Diff",
		View_DataStores_String = "Datastores",
		View_DataUnits_String = "Data Units",
	    View_Map_String = "Map",
	    View_CloseAllViewWindows_String = "Close All View Windows",

	// Menu: Commands

	Commands_String = "Commands",

	// Menu: Commands / Select, Free, Sort Time Series

	Commands_SelectTimeSeries_String = "Select, Free, Sort Time Series",
	Commands_Select_DeselectTimeSeries_String = TAB + "DeselectTimeSeries()... <deselect time series for output/processing>",
	Commands_Select_SelectTimeSeries_String = TAB + "SelectTimeSeries()... <select time series for output/processing>",
	Commands_Select_Free_String = TAB + "Free()... <free time series (will not be available to later commands)>",
	Commands_Select_SortTimeSeries_String = TAB + "SortTimeSeries()... <sort time series>",

	// Menu: Commands / Create Time Series

	Commands_CreateTimeSeries_String = "Create Time Series",
	Commands_Create_NewPatternTimeSeries_String = TAB + "NewPatternTimeSeries()... <create a time series with repeating data values>",
    Commands_Create_NewTimeSeries_String = TAB + "NewTimeSeries()... <create and initialize a new time series>",
    Commands_Create_TSID_String = TAB + "TSID... <add a time series identifier command>",
	Commands_Create_CreateFromList_String = TAB + "CreateFromList()... <read 1+ time series using a list of identifiers>",
	Commands_Create_ChangeInterval_String = TAB + "ChangeInterval()... <create time series with new interval (timestep)>",
	Commands_Create_ChangeIntervalToLarger_String = TAB + "ChangeIntervalToLarger()... <create time series with larger interval (timestep)>",
	Commands_Create_ChangeIntervalToSmaller_String = TAB + "ChangeIntervalToSmaller()... <create time series with smaller interval (timestep)>",
	Commands_Create_ChangeIntervalIrregularToRegular_String = TAB + "ChangeIntervalIrregularToRegular()... <create regular interval time series from irregular>",
	Commands_Create_ChangeIntervalRegularToIrregular_String = TAB + "ChangeIntervalRegularToIrregular()... <create irregular interval time series from regular>",
	Commands_Create_Copy_String = TAB + "Copy()... <copy a time series>",
	Commands_Create_Delta_String = TAB + "Delta()... <create new time series as delta between values>",
	Commands_Create_Disaggregate_String = TAB + "Disaggregate()... <disaggregate longer interval to shorter>",
	Commands_Create_LookupTimeSeriesFromTable_String = TAB + "LookupTimeSeriesFromTable()... <create a time series using a lookup table>",
	Commands_Create_NewDayTSFromMonthAndDayTS_String = TAB + "NewDayTSFromMonthAndDayTS()... <create daily time series from monthly total and daily pattern>",
	Commands_Create_NewEndOfMonthTSFromDayTS_String = TAB + "NewEndOfMonthTSFromDayTS()... <convert daily data to end of month time series>",
	Commands_Create_Normalize_String = TAB + "Normalize()... <Normalize time series to unitless values>",
	Commands_Create_RelativeDiff_String = TAB + "RelativeDiff()... <relative difference of time series>",
    Commands_Create_ResequenceTimeSeriesData_String = TAB + "ResequenceTimeSeriesData()... <resequence years to create new scenarios>",
    Commands_Create_RunningStatisticTimeSeries_String = TAB + "RunningStatisticTimeSeries()... <create a statistic time series from a running sample>",
	Commands_Create_NewStatisticTimeSeries_String = TAB + "NewStatisticTimeSeries()... <create a time series as repeating statistics from a time series>",
	Commands_Create_NewStatisticMonthTimeSeries_String = TAB + "NewStatisticMonthTimeSeries()... <create a time series where each Month value is a statistic>",
	Commands_Create_NewStatisticYearTS_String = TAB + "NewStatisticYearTS()... <create a time series where Year value is a statistic>",

	// Menu: Commands / Read Time Series

	Commands_Read_SetIncludeMissingTS_String = TAB + "SetIncludeMissingTS()... <create empty time series if no data>",
	Commands_Read_SetInputPeriod_String = TAB + "SetInputPeriod()... <for reading data>",

	Commands_ReadTimeSeries_String = "Read Time Series",
	Commands_Read_ReadColoradoHydroBaseRest_String = TAB + "ReadColoradoHydroBaseRest()... <read 1+ time series from Colorado HydroBase web services>",
	Commands_Read_ReadDateValue_String = TAB + "ReadDateValue()... <read 1+ time series from a DateValue file>",
	Commands_Read_ReadDelftFewsPiXml_String = TAB + "ReadDelftFewsPiXml()... <read 1+ time series from a Delft FEWS PI XML file>",
    Commands_Read_ReadDelimitedFile_String = TAB + "ReadDelimitedFile()... <read 1+ time series from a delimited file (under development)>",
    Commands_Read_ReadHecDss_String = TAB + "ReadHecDss()... <read 1+ time series from a HEC-DSS database file>",
    Commands_Read_ReadHydroBase_String = TAB + "ReadHydroBase()... <read 1+ time series from HydroBase>",
	Commands_Read_ReadMODSIM_String = TAB + "ReadMODSIM()... <read 1+ time ries from a MODSIM output file>",
	Commands_Read_ReadNrcsAwdb_String = TAB + "ReadNrcsAwdb()... <read 1+ time series from an NRCS AWDB datastore>",
	Commands_Read_ReadNwsCard_String = TAB + "ReadNwsCard()... <read 1+ time series from an NWS CARD file>",
	Commands_Read_ReadNwsrfsFS5Files_String = TAB + "ReadNwsrfsFS5Files()... <read 1 time series from NWSRFS FS5 files>",
	Commands_Read_ReadRccAcis_String = TAB + "ReadRccAcis()... <read 1+ time series from the RCC ACIS web service>",
	Commands_Read_ReadReclamationHDB_String = TAB + "ReadReclamationHDB()... <read 1+ time series a Reclamation HDB database>",
	Commands_Read_ReadReclamationPisces_String = TAB + "ReadReclamationPisces()... <read 1+ time series a Reclamation Pisces database>",
	Commands_Read_ReadRiverWare_String = TAB + "ReadRiverWare()... <read 1 time series from a RiverWare file>",
	Commands_Read_ReadStateCU_String = TAB + "ReadStateCU()... <read 1+ time series from a StateCU file>",
	Commands_Read_ReadStateCUB_String = TAB + "ReadStateCUB()... <read 1+ time series from a StateCU binary output file>",
	Commands_Read_ReadStateMod_String = TAB +	"ReadStateMod()... <read 1+ time series from a StateMod file>",
	Commands_Read_ReadStateModB_String = TAB + "ReadStateModB()... <read 1+ time series from a StateMod binary output file>",
	Commands_Read_ReadTimeSeries_String = TAB + "ReadTimeSeries()... <read 1 time series given a full TSID>",
	// See also __Commands_Datastore_ReadTimeSeriesFromDataStore() - which is added in this menu slot.
	Commands_Read_ReadTimeSeriesList_String = TAB + "ReadTimeSeriesList()... <read 1+ time series using location IDs from a table>",
    Commands_Read_ReadUsgsNwisDaily_String = TAB + "ReadUsgsNwisDaily()... <read 1+ time series from USGS NWIS daily values web service>",
    Commands_Read_ReadUsgsNwisGroundwater_String = TAB + "ReadUsgsNwisGroundwater()... <read 1+ time series from USGS NWIS groundwater web service>",
    Commands_Read_ReadUsgsNwisInstantaneous_String = TAB + "ReadUsgsNwisInstantaneous()... <read 1+ time series from USGS NWIS instantaneous values web service>",
	Commands_Read_ReadUsgsNwisRdb_String = TAB + "ReadUsgsNwisRdb()... <read 1 time series from a USGS NWIS RDB file>",
    Commands_Read_ReadWaterML_String = TAB + "ReadWaterML()... <read 1+ time series from a WaterML file>",
    Commands_Read_ReadWaterML2_String = TAB + "ReadWaterML2()... <read 1+ time series from a WaterML 2 file>",
    Commands_Read_ReadWaterOneFlow_String = TAB + "ReadWaterOneFlow()... <read 1+ time series from a WaterOneFlow web service>",
	Commands_Read_StateModMax_String = TAB + "StateModMax()... <generate 1+ time series as Max() of TS in two StateMod files>",

	// Menu: Commands / Fill Time Series Missing Data

	Commands_FillTimeSeries_String = "Fill Time Series Missing Data",
	Commands_Fill_FillConstant_String = TAB + "FillConstant()... <fill TS with constant>",
	Commands_Fill_FillDayTSFrom2MonthTSAnd1DayTS_String = TAB + "FillDayTSFrom2MonthTSAnd1DayTS()... <fill daily time series using D1 = D2*M1/M2>",
	Commands_Fill_FillFromTS_String = TAB + "FillFromTS()... <fill time series with values from another time series>",
	Commands_Fill_FillHistMonthAverage_String = TAB +	"FillHistMonthAverage()... <fill monthly TS using historic average>",
	Commands_Fill_FillHistYearAverage_String = TAB + "FillHistYearAverage()... <fill yearly TS using historic average>",
	Commands_Fill_FillInterpolate_String = TAB + "FillInterpolate()... <fill TS using interpolation>",
	Commands_Fill_FillMixedStation_String = TAB + "FillMixedStation()... <fill TS using mixed stations (under development)>",
	Commands_Fill_FillMOVE1_String = TAB + "FillMOVE1()... <fill TS using MOVE1 method>",
	Commands_Fill_FillMOVE2_String = TAB + "FillMOVE2()... <fill TS using MOVE2 method>",
	Commands_Fill_FillPattern_String = TAB + "FillPattern()... <fill TS using WET/DRY/AVG pattern>",
	Commands_Fill_ReadPatternFile_String = TAB + "  ReadPatternFile()... <for use with FillPattern() >",
	Commands_Fill_FillProrate_String = TAB + "FillProrate()... <fill TS by prorating another time series>",
	Commands_Fill_FillRegression_String = TAB + "FillRegression()... <fill TS using regression>",
	Commands_Fill_FillRepeat_String = TAB + "FillRepeat()... <fill TS by repeating values>",
	Commands_Fill_FillUsingDiversionComments_String = TAB + "FillUsingDiversionComments()... <use diversion comments as data  - HydroBase ONLY>",

	Commands_Fill_SetAutoExtendPeriod_String = TAB + "SetAutoExtendPeriod()... <for data filling and manipulation>",
	Commands_Fill_SetAveragePeriod_String = TAB +	"SetAveragePeriod()... <for data filling>",
	Commands_Fill_SetIgnoreLEZero_String = TAB + "SetIgnoreLEZero()... <ignore values <= 0 in historical averages>",
	Commands_SetTimeSeries_String = "Set Time Series Contents",
	Commands_Set_ReplaceValue_String = TAB + "ReplaceValue()... <replace value (range) with constant in TS>",
	Commands_Set_SetConstant_String = TAB + "SetConstant()... <set all values to constant in TS>",
	Commands_Set_SetDataValue_String = TAB + "SetDataValue()... <set a single data value in a TS>",
	Commands_Set_SetFromTS_String = TAB + "SetFromTS()... <set time series values from another time series>",
	Commands_Set_SetTimeSeriesValuesFromLookupTable_String = TAB + "SetTimeSeriesValuesFromLookupTable()... <set values using lookup table>",
	Commands_Set_SetTimeSeriesValuesFromTable_String = TAB + "SetTimeSeriesValuesFromTable()... <set values using table>",
	Commands_Set_SetToMax_String = TAB + "SetToMax()... <set values to maximum of time series>",
	Commands_Set_SetToMin_String = TAB + "SetToMin()... <set values to minimum of time series>",
    Commands_Set_SetTimeSeriesProperty_String = TAB + "SetTimeSeriesProperty()... <set time series properties>",

	// Menu: Commands / Manipulate Time Series

	Commands_Manipulate_Add_String = TAB + "Add()... <add one or more TS to another>",
	Commands_Manipulate_AddConstant_String = TAB + "AddConstant()... <add a constant value to a TS>",
	Commands_Manipulate_AdjustExtremes_String = TAB + "AdjustExtremes()... <adjust extreme values>",
	Commands_Manipulate_ARMA_String = TAB + "ARMA()... <lag/attenuate a time series using ARMA>",
	Commands_Manipulate_Blend_String = TAB + "Blend()... <blend one TS with another>",
    Commands_Manipulate_ChangePeriod_String = TAB + "ChangePeriod()... <change the period of record>",
    Commands_Manipulate_ChangeTimeZone_String = TAB + "ChangeTimeZone()... <change the time zone for time series>",
	Commands_Manipulate_ConvertDataUnits_String = TAB + "ConvertDataUnits()... <convert data units>",
	Commands_Manipulate_Cumulate_String = TAB + "Cumulate()... <cumulate values over time>",
	Commands_Manipulate_Divide_String = TAB +	"Divide()... <divide one TS by another TS>",
	Commands_Manipulate_Multiply_String = TAB + "Multiply()... <multiply one TS by another TS>",
	Commands_Manipulate_Scale_String = TAB + "Scale()... <scale TS by a constant>",
	Commands_Manipulate_ShiftTimeByInterval_String = TAB + "ShiftTimeByInterval()... <shift TS by an even interval>",
	Commands_Manipulate_Subtract_String = TAB + "Subtract()... <subtract one or more TS from another>",

	// Menu: Commands / Output Time Series

	Commands_OutputTimeSeries_String = "Output Time Series",
	Commands_Output_SetOutputDetailedHeaders_String = TAB + "SetOutputDetailedHeaders()... <in summary reports>",
	Commands_Output_SetOutputPeriod_String = TAB + "SetOutputPeriod()... <for output products>",
	Commands_Output_SetOutputYearType_String = TAB + "SetOutputYearType()... <e.g., Calendar and others>",
	Commands_Output_WriteDateValue_String = TAB + "WriteDateValue()... <write time series to DateValue file>",
	//Commands_Output_WriteDelftFewsPiXml_String = TAB + "WriteDelftFewsPiXml()... <write time series to Delft FEWS PI XML file>",
	Commands_Output_WriteDelimitedFile_String = TAB + "WriteDelimitedFile()... <write time series to a delimited file>",
	Commands_Output_WriteHecDss_String = TAB + "WriteHecDss()... <write time series to HEC-DSS file>",
	Commands_Output_WriteNwsCard_String = TAB + "WriteNwsCard()... <write time series to NWS Card file>",
	Commands_Output_WriteReclamationHDB_String = TAB + "WriteReclamationHDB()... <write time series to a Reclamation HDB database>",
	Commands_Output_WriteRiverWare_String = TAB +	"WriteRiverWare()... <write time series to RiverWare file>",
    Commands_Output_WriteSHEF_String = TAB + "WriteSHEF()... <write time series to SHEF file (under development)>",
	Commands_Output_WriteStateCU_String = TAB + "WriteStateCU()... <write time series to StateCU file>",
	Commands_Output_WriteStateMod_String = TAB + "WriteStateMod()... <write time series to StateMod file>",
	Commands_Output_WriteSummary_String = TAB + "WriteSummary()... <write time series to Summary file>",
	// See Commands_Datastore_WriteTimeSeriesToDataStore, which is used for this menu.
	Commands_Output_WriteTimeSeriesToDataStream_String = TAB + "WriteTimeSeriesToDataStream()... <write time series as stream of data records>",
	Commands_Output_WriteTimeSeriesToHydroJSON_String = TAB + "WriteTimeSeriesToHydroJSON()... <write time series to HydroJSON file>",
	Commands_Output_WriteTimeSeriesToJson_String = TAB + "WriteTimeSeriesToJson()... <write time series to JSON file>",
	Commands_Output_WriteWaterML_String = TAB + "WriteWaterML()... <write time series to WaterML file>",
	Commands_Output_WriteWaterML2_String = TAB + "WriteWaterML2()... <write time series to WaterML 2 file>",
	// See Commands_General_TestProcessing_WriteTimeSeriesPropertiesToFile_String, which is used for this menu.

    // Menu: Commands / Check Time Series

    Commands_Check_CheckingResults_String = "Check Time Series",
    Commands_Check_CheckingResults_CheckTimeSeries_String = TAB + "CheckTimeSeries()... <check time series data values>",
    Commands_Check_CheckingResults_CheckTimeSeriesStatistic_String = TAB + "CheckTimeSeriesStatistic()... <check time series statistic>",
    Commands_Check_CheckingResults_WriteCheckFile_String = TAB + "WriteCheckFile()... <write check file>",

	// Menu: Commands / Analyze Time Series

	Commands_AnalyzeTimeSeries_String = "Analyze Time Series",
	Commands_Analyze_CalculateTimeSeriesStatistic_String = TAB + "CalculateTimeSeriesStatistic()... <determine statistic for time series>",
	Commands_Analyze_AnalyzePattern_String = TAB + "AnalyzePattern()... <determine pattern(s) for FillPattern()>",
	Commands_Analyze_CompareTimeSeries_String = TAB + "CompareTimeSeries()... <find differences between time series>",
	Commands_Analyze_ComputeErrorTimeSeries_String = TAB + "ComputeErrorTimeSeries()... <compute error between time series>",

	// Menu: Commands / Models - Routing

	Commands_Models_Routing_String = "Models - Routing",
	Commands_Models_Routing_LagK_String = "LagK()... <lag and attenuate (route)>",
	Commands_Models_Routing_VariableLagK_String = "VariableLagK()... <lag and attenuate (route)>",

	// Menu: Commands / Datastore Processing

    Commands_Datastore_String = "Datastore Processing",
    Commands_Datastore_NewAccessDatabase_String = TAB + "NewAccessDatabase()... <create a Microsoft Access database>",
    Commands_Datastore_NewDerbyDatabase_String = TAB + "NewDerbyDatabase()... <create a Derby database (built-in Java database)>",
    Commands_Datastore_NewSQLiteDatabase_String = TAB + "NewSQLiteDatabase()... <create a SQLite database>",
    Commands_Datastore_OpenDataStore_String = TAB + "OpenDataStore()... <open a database datastore>",
    Commands_Datastore_ReadTableFromDataStore_String = TAB + "ReadTableFromDataStore()... <read a table from a database datastore>",
    Commands_Datastore_WriteTableToDataStore_String = TAB + "WriteTableToDataStore()... <write a table to a database datastore>",
    Commands_Datastore_DeleteDataStoreTableRows_String = TAB + "DeleteDataStoreTableRows()... <delete database datastore table rows>",
    Commands_Datastore_RunSql_String = TAB + "RunSql()... <run an SQL statement for a database datastore>",
    Commands_Datastore_ReadTimeSeriesFromDataStore_String = TAB + "ReadTimeSeriesFromDataStore()... <read 1+ time series from a database datastore>",
    Commands_Datastore_WriteTimeSeriesToDataStore_String = TAB + "WriteTimeSeriesToDataStore()... <write time series to database datastore>",
   	Commands_Datastore_CloseDataStore_String = TAB + "CloseDataStore()... <close a database datastore>",
    Commands_Datastore_CreateDataStoreDataDictionary_String = TAB + "CreateDataStoreDataDictionary()... <create a data dictionary for a datastore>",
    Commands_Datastore_SetPropertyFromDataStore_String = TAB + "SetPropertyFromDataStore()... <set a processor property from a datastore>",

    // Menu: Commands / Ensemble Processing

    Commands_Ensemble_String = "Ensemble Processing",
    Commands_Ensemble_CreateEnsembleFromOneTimeSeries_String =
        TAB + "CreateEnsembleFromOneTimeSeries()... <convert 1 time series into an ensemble>",
    Commands_Ensemble_CopyEnsemble_String = TAB + "CopyEnsemble()... <create a copy of an ensemble>",
    Commands_Ensemble_NewEnsemble_String = TAB + "NewEnsemble()... <create a new ensemble from 0+ time series>",
    Commands_Ensemble_ReadNwsrfsEspTraceEnsemble_String = TAB + "ReadNwsrfsEspTraceEnsemble()... <read 1(+) time series from an NWSRFS ESP trace ensemble file>",
    Commands_Ensemble_SetEnsembleProperty_String = TAB + "SetEnsembleProperty()... <set ensemble property>",
    Commands_Ensemble_InsertTimeSeriesIntoEnsemble_String = TAB + "InsertTimeSeriesIntoEnsemble()... <insert 1+ time series into an ensemble>",
    Commands_Ensemble_NewStatisticEnsemble_String = TAB + "NewStatisticEnsemble()... <create an ensemble of statistic time series>",
    Commands_Ensemble_NewStatisticTimeSeriesFromEnsemble_String = TAB + "NewStatisticTimeSeriesFromEnsemble()... <create a time series as a statistic from an ensemble>",
    Commands_Ensemble_WeightTraces_String = TAB + "WeightTraces()... <weight traces to create a new time series>",
    Commands_Ensemble_WriteNwsrfsEspTraceEnsemble_String = TAB + "WriteNwsrfsEspTraceEnsemble()... <write NWSRFS ESP trace ensemble file>",

    // Menu: Commands / Network Processing

    Commands_Network_String = "Network Processing",
    Commands_Network_CreateNetworkFromTable_String = TAB + "CreateNetworkFromTable()... <create network from table>",
    Commands_Network_AnalyzeNetworkPointFlow_String = TAB + "AnalyzeNetworkPointFlow()... <perform point flow analysis>",

    // Menu: Commands / Object Processing

    Commands_Object_String = "Object Processing",
    Commands_Object_NewObject_String = TAB + "NewObject()... <create a new object>",
    Commands_Object_FreeObject_String = TAB + "FreeObject()... <free memory for an object>",
    Commands_Object_SetObjectProperty_String = TAB + "SetObjectProperty()... <set an object's property>",
    Commands_Object_SetObjectPropertiesFromTable_String = TAB + "SetObjectPropertiesFromTable()... <set object properties from a table>",
    Commands_Object_SetPropertyFromObject_String = TAB + "SetPropertyFromObject()... <set a property from an object>",
    Commands_Object_WriteObjectToJSON_String = TAB + "WriteObjectToJSON()... <write an object to a JSON file>",

    // Menu: Commands / Spatial Data Processing

    Commands_Spatial_String = "Spatial Data Processing",
    Commands_Spatial_WriteTableToGeoJSON_String = TAB + "WriteTableToGeoJSON()... <write table to a GeoJSON file>",
    Commands_Spatial_WriteTableToKml_String = TAB + "WriteTableToKml()... <write table to a KML file>",
    Commands_Spatial_WriteTableToShapefile_String = TAB + "WriteTableToShapefile()... <write table to a Shapefile>",
    Commands_Spatial_WriteTimeSeriesToGeoJSON_String = TAB + "WriteTimeSeriesToGeoJSON()... <write 1+ time series to a GeoJSON file>",
    Commands_Spatial_WriteTimeSeriesToKml_String = TAB + "WriteTimeSeriesToKml()... <write 1+ time series to a KML file>",
    Commands_Spatial_GeoMapProject_String = TAB + "GeoMapProject()... <process a GeoMap project>",
    Commands_Spatial_GeoMap_String = TAB + "GeoMap()... <process a GeoMap>",

    // Menu: Commands / Spreadsheet Processing

    Commands_Spreadsheet_String = "Spreadsheet Processing",
    Commands_Spreadsheet_NewExcelWorkbook_String = TAB + "NewExcelWorkbook()... <create a new Excel workbook file>",
    Commands_Spreadsheet_ReadExcelWorkbook_String = TAB + "ReadExcelWorkbook()... <read an Excel workbook file>",
    Commands_Spreadsheet_ReadTableFromExcel_String = TAB + "ReadTableFromExcel()... <read a table from an Excel file>",
    Commands_Spreadsheet_ReadTableCellsFromExcel_String = TAB + "ReadTableCellsFromExcel()... <read a table's cells from an Excel file>",
    Commands_Spreadsheet_ReadPropertiesFromExcel_String = TAB + "ReadPropertiesFromExcel()... <read processor properties from an Excel file>",
    Commands_Spreadsheet_SetExcelCell_String = TAB + "SetExcelCell()... <set single Excel value and formatting>",
    Commands_Spreadsheet_SetExcelWorksheetViewProperties_String = TAB + "SetExcelWorksheetViewProperties()... <set Excel view properties>",
    Commands_Spreadsheet_WriteTableToExcel_String = TAB + "WriteTableToExcel()... <write a table to an Excel file>",
    Commands_Spreadsheet_WriteTableCellsToExcel_String = TAB + "WriteTableCellsToExcel()... <write a table's cells to an Excel file>",
    Commands_Spreadsheet_WriteTimeSeriesToExcel_String = TAB + "WriteTimeSeriesToExcel()... <write 1+ time series to an Excel file>",
    Commands_Spreadsheet_WriteTimeSeriesToExcelBlock_String = TAB + "WriteTimeSeriesToExcelBlock()... <write 1+ time series to an Excel file as data block(s)>",
    Commands_Spreadsheet_CloseExcelWorkbook_String = TAB + "CloseExcelWorkbook()... <close and optionally write an Excel file>",

    // Menu: Commands / Template Processing

    Commands_Template_String = "Template Processing",
    Commands_Template_ExpandTemplateFile_String = TAB + "ExpandTemplateFile()... <expand a template to the full file>",
    //Commands_Template_Comments_Template_String = TAB + "#@template <comment that indicates a template command file>",

    // Menu: Commands / Visualization Processing

    Commands_Visualization_String = "Visualization Processing",
    Commands_Visualization_ProcessTSProduct_String = TAB + "ProcessTSProduct()... <process a time series product file>",
    Commands_Visualization_ProcessRasterGraph_String = TAB + "ProcessRasterGraph()... <process time series product file for raster graph>",
    Commands_Visualization_NewTreeView_String = TAB + "NewTreeView()... <create a tree view for time series results>",

	// Menu: Commands / General - Comments

    Commands_General_Comments_String = "General - Comments",
    Commands_General_Comments_Comment_String = TAB + "# comment(s) <insert 1+ comments, each starting with #>",
    Commands_General_Comments_StartComment_String = TAB + "/* <start multi-line comment section>",
    Commands_General_Comments_EndComment_String = TAB + "*/ <end multi-line comment section>",
    // -----------------
    Commands_General_Comments_AuthorComment_String = TAB + "#@author <author of the command file>",
    Commands_General_Comments_VersionComment_String = TAB + "#@version <command file version>",
    Commands_General_Comments_VersionDateComment_String = TAB + "#@versionDate <command file version date/time>",
    Commands_General_Comments_SourceUrlComment_String = TAB + "#@sourceUrl <URL for the source>",
    Commands_General_Comments_DocUrlComment_String = TAB + "#@docUrl <URL for the documentation for the command file>",
    // -----------------
    Commands_General_Comments_ReadOnlyComment_String = TAB + "#@readOnly <protect command file from saving>",
    Commands_General_Comments_RunDiscoveryFalseComment_String = TAB + "#@runDiscovery False <used to disable running discovery at load>",
   	Commands_General_Comments_TemplateComment_String = TAB + "#@template <indicate that command file is a template file>",
    // -----------------
    Commands_General_Comments_EnabledComment_String = TAB + "#@enabled False <used to disable command for tests>",
    Commands_General_Comments_EnabledIfApplicationComment_String = "#@enabledif application ... <enable command file for application version>",
    Commands_General_Comments_EnabledIfDatastoreComment_String = "#@enabledif datastore ... <enable command file for datastore version>",
    // -----------------
    Commands_General_Comments_ExpectedStatusFailureComment_String = TAB + "#@expectedStatus Failure <used to test commands>",
    Commands_General_Comments_ExpectedStatusWarningComment_String = TAB + "#@expectedStatus Warning <used to test commands>",
    //__Commands_General_Comments_FileModTimeComment_String = TAB + "#@fileModTime ... <used to enforce dependencies>",
    //__Commands_General_Comments_FileSizeComment_String = TAB + "#@fileSize ... <used to enforce dependencies>",
    Commands_General_Comments_IdComment_String = TAB + "#@id CommandFileId <command file identifier>",
    Commands_General_Comments_OrderComment_String = TAB + "#@order before/after CommandFileId <control test order>",
    // -----------------
    Commands_General_Comments_RequireApplicationComment_String = "#@require application ... <check application version dependency>",
    Commands_General_Comments_RequireDatastoreComment_String = "#@require datastore ... <check datastore version dependency>",
    Commands_General_Comments_RequireUserComment_String = "#@require user ... <check user requirement>",
    // -----------------
    Commands_General_Comments_FixMeComment_String = "#@fixme ... <indicate something to fix>",
    Commands_General_Comments_ToDoComment_String = "#@todo ... <indicate something to do>",
    // -----------------
    Commands_General_Comments_DocExampleComment_String = "#@docExample ... <command file is an example for documentation>",
    // -----------------
    Commands_General_Comments_Empty_String = TAB + "<empty line>",

	// Menu: Commands / General - File Handling

    Commands_General_FileHandling_String = "General - File Handling",
    Commands_General_FileHandling_FTPGet_String = TAB + "FTPGet()... <get file(s) using FTP>",
    Commands_General_FileHandling_WebGet_String = TAB + "WebGet()... <get file(s) from the web>",
    Commands_General_FileHandling_CreateFolder_String = TAB + "CreateFolder()... <create a folder>",
    Commands_General_FileHandling_RemoveFolder_String = TAB + "RemoveFolder()... <remove a folder>",
    Commands_General_FileHandling_AppendFile_String = TAB + "AppendFile()... <append file(s)>",
    Commands_General_FileHandling_CheckFile_String = TAB + "CheckFile()... <check a file's contents>",
    Commands_General_FileHandling_CopyFile_String = TAB + "CopyFile()... <copy file(s)>",
    Commands_General_FileHandling_FormatFile_String = TAB + "FormatFile()... <format a file>",
    Commands_General_FileHandling_ListFiles_String = TAB + "ListFiles()... <list file(s) to a table>",
    Commands_General_FileHandling_RemoveFile_String = TAB + "RemoveFile()... <remove file(s)>",
    Commands_General_FileHandling_TextEdit_String = TAB + "TextEdit()... <edit a text file>",
    Commands_General_FileHandling_PDFMerge_String = TAB + "PDFMerge()... <merge PDF files>",
    Commands_General_FileHandling_UnzipFile_String = TAB + "UnzipFile()... <unzip file>",
    Commands_General_FileHandling_PrintTextFile_String = TAB + "PrintTextFile()... <print a text file>",

	// Menu: Commands / General - Logging and Messaging

	Commands_General_Logging_String = "General - Logging and Messaging",
	Commands_General_Logging_ConfigureLogging_String = TAB + "ConfigureLogging()... <configure logging>",
	Commands_General_Logging_StartLog_String = TAB + "StartLog()... <(re)start the log file>",
	Commands_General_Logging_SetDebugLevel_String = TAB +	"SetDebugLevel()... <set debug message level>",
	Commands_General_Logging_SetWarningLevel_String = TAB + "SetWarningLevel()... <set debug message level>",
	Commands_General_Logging_Message_String = TAB + "Message()... <print a message>",
	Commands_General_Logging_SendEmailMessage_String = TAB + "SendEmailMessage()... <send an email message>",

	// Menu: Commands / General - Running and Properties

    Commands_General_Running_String = "General - Running and Properties",
    Commands_General_Running_ReadPropertiesFromFile_String = TAB + "ReadPropertiesFromFile()... <read processor properties from file>",
    Commands_General_Running_SetProperty_String = TAB + "SetProperty()... <set a processor property>",
    Commands_General_Running_SetPropertyFromNwsrfsAppDefault_String =
        TAB + "SetPropertyFromNwsrfsAppDefault()... <set a processor property from an NWSRFS App Default>",
    Commands_General_Running_SetPropertyFromEnsemble_String = TAB + "SetPropertyFromEnsemble()... <set a processor property from ensemble properties>",
    Commands_General_Running_SetPropertyFromTimeSeries_String = TAB + "SetPropertyFromTimeSeries()... <set a processor property from time series properties>",
    Commands_General_Running_FormatDateTimeProperty_String = TAB + "FormatDateTimeProperty()... <format date/time property as string property>",
    Commands_General_Running_FormatStringProperty_String = TAB + "FormatStringProperty()... <format a string property>",
    Commands_General_Running_WritePropertiesToFile_String = TAB + "WritePropertiesToFile()... <write processor properties to file>",
    Commands_General_Running_RunCommands_String = TAB + "RunCommands()... <run a command file>",
	Commands_General_Running_RunProgram_String = TAB + "RunProgram()... <run an external program>",
    Commands_General_Running_RunPython_String = TAB + "RunPython()... <run a Python script>",
    Commands_General_Running_RunR_String = TAB + "RunR()... <run an R script>",
    Commands_General_Running_RunDSSUTL_String = TAB + "RunDSSUTL()... <run the HEC DSSUTL program>",
    Commands_General_Running_If_String = TAB + "If() <check a condition and start a block of commands>",
    Commands_General_Running_EndIf_String = TAB + "EndIf() <end an If() block>",
    Commands_General_Running_For_String = TAB + "For() <repeatedly execute a block of commands>",
    Commands_General_Running_EndFor_String = TAB + "EndFor() <end a For() block>",
    Commands_General_Running_Break_String = TAB + "Break() <break out of a For() block>",
    Commands_General_Running_Continue_String = TAB + "Continue() <continue to next iteration of a For() block>",
    Commands_General_Running_Exit_String = TAB + "Exit() <end processing>",
    Commands_General_Running_Wait_String = TAB + "Wait() <pause processing within the command>",
    Commands_General_Running_SetWorkingDir_String = TAB + "SetWorkingDir()... <set the working directory for relative paths>",
    Commands_General_Running_ProfileCommands_String = TAB + "ProfileCommands()... <profile command performance>",

	// Menu: Commands / General - Test Processing

    Commands_General_TestProcessing_String = "General - Test Processing",
    Commands_General_TestProcessing_StartRegressionTestResultsReport_String = TAB + "StartRegressionTestResultsReport()... <for test results>",
	Commands_General_TestProcessing_CompareFiles_String = TAB + "CompareFiles()... <compare files, to test software>",
	Commands_General_TestProcessing_WriteProperty_String = TAB + "WriteProperty()... <write processor property, to test software>",
	Commands_General_TestProcessing_WriteTimeSeriesPropertiesToFile_String = TAB + "WriteTimeSeriesPropertiesToFile()... <write time series properties to file>",
	Commands_General_TestProcessing_WriteTimeSeriesProperty_String = TAB + "WriteTimeSeriesProperty()... <write time series property, to test software>",
	Commands_General_TestProcessing_CreateRegressionTestCommandFile_String = TAB + "CreateRegressionTestCommandFile()... <to test software>",
    Commands_General_TestProcessing_TestCommand_String = TAB + "TestCommand()... <to test software>",

    // Menu: Commands / Deprecated Commands

    Commands_Deprecated_String = "Deprecated Commands",
    Commands_Deprecated_OpenHydroBase_String = TAB + "OpenHydroBase()... <open HydroBase database connection - PHASING OUT>",
    Commands_Deprecated_RunningAverage_String = TAB + "RunningAverage()... <convert TS to running average - PHASING OUT>",

    // Menu: Commands(Table) / Create, Copy, Free Table

    Commands_Table_String = "Commands(Table)",
    Commands_TableCreate_String = "Create, Copy, Free Table",
    Commands_TableCreate_NewTable_String = TAB + "NewTable()... <create a new empty table>",
    Commands_TableCreate_CopyTable_String = TAB + "CopyTable()... <create a new table as a full/partial copy of another>",

    // Menu: Commands(Table) / Read Table

    Commands_TableRead_String = "Read Table",
    // Menu inserted here uses __Commands_Datastore_ReadTableFromDataStore string.
    Commands_TableRead_ReadTableFromDelimitedFile_String = TAB + "ReadTableFromDelimitedFile()... <read a table from a delimited file>",
    Commands_TableRead_ReadTableFromDBF_String = TAB + "ReadTableFromDBF()... <read a table from a dBASE file>",
    Commands_TableRead_ReadTableFromFixedFormatFile_String = TAB + "ReadTableFromFixedFormatFile()... <read a table from a fixed format file>",
    Commands_TableRead_ReadTableFromJSON_String = TAB + "ReadTableFromJSON()... <read a table from a JSON file>",
    Commands_TableRead_ReadTableFromXML_String = TAB + "ReadTableFromXML()... <read a table from an XML file>",

    // Menu: Commands(Table) / Append, Join Table

    Commands_TableJoin_String = "Append, Join Tables",
    Commands_TableJoin_AppendTable_String = TAB + "AppendTable()... <append a table's rows to another table>",
    Commands_TableJoin_JoinTables_String = TAB + "JoinTables()... <join a table's rows to another table by matching column value(s)>",

    // Menu: Commands(Table) / Table, Time Series Processing

    Commands_TableTimeSeries_String = "Table, Time Series Processing",
    Commands_TableTimeSeries_TimeSeriesToTable_String = TAB + "TimeSeriesToTable()... <copy time series to a table>",
    Commands_TableTimeSeries_TableToTimeSeries_String = TAB + "TableToTimeSeries()... <create time series from a table>",
	Commands_TableTimeSeries_SetTimeSeriesPropertiesFromTable_String =
        TAB + "SetTimeSeriesPropertiesFromTable()... <set time series properties from table>",
    Commands_TableTimeSeries_CopyTimeSeriesPropertiesToTable_String =
        TAB + "CopyTimeSeriesPropertiesToTable()... <copy time series properties to table>",
    Commands_TableTimeSeries_CreateTimeSeriesEventTable_String = TAB + "CreateTimeSeriesEventTable()... <create time series event table>",

    // Menu: Commands(Table) / Manipulate Table Values

    Commands_TableManipulate_String = "Manipulate Table Values",
    Commands_TableManipulate_FormatTableDateTime_String = TAB + "FormatTableDateTime()... <format table date/time column into output column>",
    Commands_TableManipulate_FormatTableString_String = TAB + "FormatTableString()... <format table columns into a string column>",
    Commands_TableManipulate_ManipulateTableString_String = TAB + "ManipulateTableString()... <perform simple manipulation on table strings>",
    Commands_TableManipulate_InsertTableColumn_String = TAB + "InsertTableColumn()... <insert table column>",
    Commands_TableManipulate_DeleteTableColumns_String = TAB + "DeleteTableColumns()... <delete table column(s)>",
    Commands_TableManipulate_DeleteTableRows_String = TAB + "DeleteTableRows()... <delete table row(s)>",
    Commands_TableManipulate_InsertTableRow_String = TAB + "InsertTableRow()... <insert table row(s)>",
    Commands_TableManipulate_RenameTableColumns_String = TAB + "RenameTableColumns()... <rename table column(s)>",
    Commands_TableManipulate_SetTableColumnProperties_String = TAB + "SetTableColumnProperties()... <set table column properties>",
    Commands_TableManipulate_SetTableValues_String = TAB + "SetTableValues()... <set table cell values>",
    Commands_TableManipulate_SplitTableColumn_String = TAB + "SplitTableColumn()... <split a column into multiple columns>",
    Commands_TableManipulate_SortTable_String = TAB + "SortTable()... <sort a table's rows>",
    Commands_TableManipulate_SplitTableRow_String = TAB + "SplitTableRow()... <split a row into multiple rows>",
    Commands_TableManipulate_TableMath_String = TAB + "TableMath()... <perform simple math on table columns>",
    Commands_TableManipulate_TableTimeSeriesMath_String = TAB + "TableTimeSeriesMath()... <perform simple math on table columns and time series>",

    // Menu: Commands(Table) / Analyze Table

    Commands_TableAnalyze_String = "Analyze Table",
    Commands_TableAnalyze_CompareTables_String = TAB + "CompareTables()... <compare two tables (indicate differences)>",

    // Menu: Commands(Table) / Output Table

    Commands_TableOutput_String = "Output Table",
    // See __Commands_Datastore_WriteTableToDataStore, which is used to define menu here.
    Commands_TableOutput_WriteTableToDelimitedFile_String = TAB + "WriteTableToDelimitedFile()... <write a table to a delimited file>",
    Commands_TableOutput_WriteTableToHTML_String = TAB + "WriteTableToHTML()... <write a table to an HTML file>",
    Commands_TableOutput_WriteTableToMarkdown_String = TAB + "WriteTableToMarkdown()... <write a table to a Markdown file>",
    Commands_TableCreate_FreeTable_String = TAB + "FreeTable()... <free a table (will not be available to later commands)>",
    Commands_TableRunning_String = "Running and Properties",
    Commands_TableRunning_SetPropertyFromTable_String = TAB + "SetPropertyFromTable()... <set a processor property from a table>",
    Commands_TableRunning_CopyPropertiesToTable_String = TAB + "CopyPropertiesToTable()... <copy processor properties to a table>",

    // Menu: Commands(Plugin)

    Commands_Plugin_String = "Commands(Plugin)",

	// Menu: Results

	Results_Graph_AnnualTraces_String = "Graph - Annual Traces...",
	Results_Graph_Area_String = "Graph - Area",
	Results_Graph_AreaStacked_String = "Graph - Area (stacked)",
	Results_Graph_BarsLeft_String = "Graph - Bar (left of date)",
	Results_Graph_BarsCenter_String = "Graph - Bar (center on date)",
	Results_Graph_BarsRight_String = "Graph - Bar (right of date)",
	Results_Graph_DoubleMass_String = "Graph - Double Mass Curve",
	Results_Graph_Duration_String = "Graph - Duration",
	Results_Graph_Ensemble_String = "Graph - Ensemble",
	Results_Graph_Line_String = "Graph - Line",
	Results_Graph_LineLogY_String = "Graph - Line (log Y-axis)",
	Results_Graph_PeriodOfRecord_String = "Graph - Period of Record",
	Results_Graph_Point_String = "Graph - Point",
	Results_Graph_PredictedValue_String =	"Graph - Predicted Value (under development)",
	Results_Graph_PredictedValueResidual_String =	"Graph - Predicted Value Residual (under development)",
	Results_Graph_Raster_String = "Graph - Raster (heat map)",
	Results_Graph_XYScatter_String = "Graph - XY-Scatter",

	Results_Ensemble_Graph_Line_String = "Graph - Line (Ensemble Graph)",
	Results_Ensemble_Table_String = "Table",
	Results_Ensemble_Properties_String = "Ensemble Properties",

	Results_Object_Properties_String = "Object Properties",

	Results_OutputFile_FindOutputFiles_String = "Find Output Files...",

	Results_Network_Properties_String = "Network Properties",

	Results_Table_Properties_String = "Table Properties...",
	Results_Table_FindTables_String = "Find Tables...",

	// TODO smalers 2012-10-12 Need to clarify name on the following so grouped with time series choices.
	Results_Table_String = "Table",
	Results_Report_Summary_Html_String = "Report - Summary (HTML)",
	Results_Report_Summary_Text_String = "Report - Summary (Text)",
	Results_TimeSeriesProperties_String = "Time Series Properties",

	// Only for popup Results menu.

	Results_TimeSeries_FindTimeSeries_String = "Find Time Series...",
	Results_SelectAllForOutput_String = BUTTON_TS_SELECT_ALL,
	Results_DeselectAll_String = BUTTON_TS_DESELECT_ALL,

	// Menu: Run

	Run_AllCommandsCreateOutput_String = "All Commands (create all output)",
	Run_AllCommandsIgnoreOutput_String = "All Commands (ignore output commands)",
	Run_SelectedCommandsCreateOutput_String =	"Selected Commands (create all output)",
	Run_SelectedCommandsIgnoreOutput_String =	"Selected Commands (ignore output commands)",
	Run_CancelCommandProcessing_WaitForCommand_String = "Cancel Command Processing (wait for command to finish)",
	Run_CancelCommandProcessing_InterruptProcessor_String = "Cancel Command Processing (interrupt processor)",
	Run_CancelCommandProcessesExternal_String = "Cancel Called Processes (external to TSTool)",
	Run_CommandsFromFile_String = "Commands From File...",
	Run_ProcessTSProductPreview_String = "Process TS Product File (preview)...",
	Run_ProcessTSProductOutput_String = "Process TS Product File (create output)...",

	// Menu: Tools

	Tools_String = "Tools",
		Tools_Analysis_String = "Analysis",
			Tools_Analysis_MixedStationAnalysis_String = "Mixed Station Analysis... (under development)",
			Tools_Analysis_PrincipalComponentAnalysis_String = "Principal Component Analysis... (under development)",
		Tools_Datastore_String = "Datastore",
			Tools_Datastore_ERDiagram_String = "Entity Relationship Diagram (under development)",
		Tools_DateTimeTools_String = "Date/time Tools",
		Tools_Report_String = "Report",
			Tools_Report_DataCoverageByYear_String = "Data Coverage by Year...",
			Tools_Report_DataLimitsSummary_String = "Data Limits Summary...",
			Tools_Report_MonthSummaryDailyMeans_String = "Month Summary (Daily Means)...",
			Tools_Report_MonthSummaryDailyTotals_String =	"Month Summary (Daily Totals)...",
			Tools_Report_YearToDateTotal_String =	"Year to Date Total... <Daily or real-time CFS Only!>",
		Tools_NWSRFS_String = "NWSRFS",
			Tools_NWSRFS_ConvertNWSRFSESPTraceEnsemble_String = "Convert NWSRFS ESP Trace Ensemble File to Text...",
			Tools_NWSRFS_ConvertJulianHour_String = "Convert Julian Hour...",
		Tools_SelectOnMap_String = "Select on Map",
		Tools_Options_String = "Options...",
		Tools_ViewLogFile_Startup_String = "Diagnostics - View Log File (Startup)...",

	// Menu: Help

	Help_String = "Help",
		Help_AboutTSTool_String = "About TSTool",
		Help_ViewDocumentation_String = "View Documentation",
		Help_ViewDocumentation_ReleaseNotes_String = "View Documentation - Release Notes",
		Help_ViewDocumentation_UserManual_String = "View Documentation - User Manual",
		Help_ViewDocumentation_CommandReference_String = "View Documentation - Command Reference",
		Help_ViewDocumentation_DatastoreReference_String = "View Documentation - Datastore Reference",
		Help_ViewDocumentation_Troubleshooting_String = "View Documentation - Troubleshooting",
		Help_View_TrainingMaterials_String = "View Training Materials",
		Help_CheckForUpdates_String = "Check for Updates...",
		Help_ImportConfiguration_String = "Import Configuration...",

	// Strings used in popup menu for other components.

	InputName_BrowseHECDSS_String = "Browse for a HEC-DSS file...",
	InputName_BrowseStateModB_String = "Browse for a StateMod binary file...",
	InputName_BrowseStateCUB_String = "Browse for a StateCU binary file...";

	// Status for UI message area.

	/**
	General status string to indicate that the GUI is ready for user input.
	*/
	public static final String STATUS_READY = "Ready";

	/**
	General status string to indicate that the user should wait for the GUI to finish a task.
	*/
	public static final String STATUS_BUSY = "Wait";

	/**
	General status string to indicate that command processing is being canceled.
	*/
	public static final String STATUS_CANCELING = "Canceling";

	/**
	General status string to indicate that command processing has been canceled.
	*/
	public static final String STATUS_CANCELED = "Canceled";

}