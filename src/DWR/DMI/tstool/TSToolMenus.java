// TSToolMenus - user interface menu objects

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

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 * TSTool user interface menu objects.
 * Putting in this file reduces the length of the TSTool_JFrame code.
 */
public class TSToolMenus {

	//================================
	// Menu items and strings, list in order of the menus.
	//================================

	public static JPopupMenu
		Commands_JPopupMenu;

	public static JMenuItem
    	CommandsPopup_ShowCommandStatus_JMenuItem,

    	CommandsPopup_IndentRight_JMenuItem,
    	CommandsPopup_IndentLeft_JMenuItem,

    	CommandsPopup_Edit_CommandWithErrorChecking_JMenuItem,
    	CommandsPopup_Edit_CommandAsText_JMenuItem,

		CommandsPopup_Cut_JMenuItem,
		CommandsPopup_Copy_JMenuItem,
		CommandsPopup_Paste_JMenuItem,

		CommandsPopup_Delete_JMenuItem,
		CommandsPopup_FindCommands_JMenuItem,

		CommandsPopup_SelectAll_JMenuItem,
		CommandsPopup_DeselectAll_JMenuItem,

		CommandsPopup_Run_AllCommandsCreateOutput_JMenuItem,
		CommandsPopup_Run_AllCommandsIgnoreOutput_JMenuItem,
		CommandsPopup_Run_SelectedCommandsCreateOutput_JMenuItem,
		CommandsPopup_Run_SelectedCommandsIgnoreOutput_JMenuItem,

		CommandsPopup_CancelCommandProcessing_WaitForCommand_JMenuItem,

		CommandsPopup_ConvertSelectedCommandsToComments_JMenuItem,
		CommandsPopup_ConvertSelectedCommandsFromComments_JMenuItem,
    	CommandsPopup_ConvertTSIDTo_ReadTimeSeries_JMenuItem,
    	CommandsPopup_ConvertTSIDTo_ReadCommand_JMenuItem;

	// Menu: File

	public static JMenu File_JMenu = null,
	File_New_JMenu = null;
    	public static JMenuItem
          	File_New_CommandFile_JMenuItem = null;
    public static JMenu File_Open_JMenu = null;
		public static JMenuItem
	   	File_Open_CommandFile_JMenuItem = null,
	   	File_Open_CommandFileNoDiscovery_JMenuItem = null,
	   	File_Open_CommandFileRecent_JMenuItem[] = null,
	   	File_Open_HydroBase_JMenuItem = null,
	   	File_Open_ReclamationHDB_JMenuItem = null;
	public static JMenu File_Save_JMenu = null;
		public static JMenuItem
		File_Save_Commands_JMenuItem = null,
		File_Save_CommandsAs_JMenuItem = null,
		// TODO smalers 2023-02-19 remove ASAP since old and not used.
		//File_Save_CommandsAsVersion9_JMenuItem = null,
		File_Save_TimeSeriesAs_JMenuItem = null;
	public static JMenuItem
		File_CheckForUpdate_JMenuItem = null;
	public static JMenu File_Print_JMenu = null;
		public static JMenuItem
    	File_Print_Commands_JMenuItem = null;
	public static JMenu File_Properties_JMenu = null;
	public static JMenuItem
		File_Properties_CommandsRun_JMenuItem = null,
		File_Properties_TSToolSession_JMenuItem = null,
		File_Properties_HydroBase_JMenuItem = null,
		File_Properties_NWSRFSFS5Files_JMenuItem = null;
	public static JMenuItem
		File_SetWorkingDirectory_JMenuItem = null,
		File_Exit_JMenuItem = null;

	// Menu: Edit

	public static JMenu Edit_JMenu = null;
	public static JMenuItem
		Edit_CutCommands_JMenuItem = null,
		Edit_CopyCommands_JMenuItem = null,
		Edit_PasteCommands_JMenuItem = null,
		// --
		Edit_DeleteCommands_JMenuItem = null,
		// --
		Edit_SelectAllCommands_JMenuItem = null,
		Edit_DeselectAllCommands_JMenuItem = null,
		// --
		Edit_CommandWithErrorChecking_JMenuItem = null,
		// --
		Edit_ConvertSelectedCommandsToComments_JMenuItem = null,
		Edit_ConvertSelectedCommandsFromComments_JMenuItem = null,
   		Edit_ConvertTSIDTo_ReadTimeSeries_JMenuItem = null,
   		Edit_ConvertTSIDTo_ReadCommand_JMenuItem = null;

	// Menu: View

	public static JMenu View_JMenu = null;
	public static JMenuItem
    	View_CommandFileDiff_JMenuItem = null,
    	View_CommandFileSourceDiff_JMenuItem = null,
    	View_DataStores_JMenuItem = null,
    	View_DataUnits_JMenuItem = null;
	public static JCheckBoxMenuItem
		View_MapInterface_JCheckBoxMenuItem = null;
	public static JMenuItem
    	View_CloseAllViewWindows_JMenuItem = null;

	// Menu: Commands / Create Time Series

	public static JMenu
		Commands_JMenu = null,
		Commands_SelectTimeSeries_JMenu = null;
	public static JMenuItem
    	Commands_Select_DeselectTimeSeries_JMenuItem,
    	Commands_Select_SelectTimeSeries_JMenuItem,
    	Commands_Select_Free_JMenuItem,
    	Commands_Select_SortTimeSeries_JMenuItem;
	public static JMenu
		Commands_CreateTimeSeries_JMenu = null;
	public static JMenuItem
    	Commands_Create_NewPatternTimeSeries_JMenuItem,
    	Commands_Create_NewTimeSeries_JMenuItem,
    	Commands_Create_TSID_JMenuItem,
		Commands_Create_ChangeInterval_JMenuItem,
		Commands_Create_ChangeIntervalToLarger_JMenuItem,
		Commands_Create_ChangeIntervalToSmaller_JMenuItem,
		Commands_Create_ChangeIntervalIrregularToRegular_JMenuItem,
		Commands_Create_ChangeIntervalRegularToIrregular_JMenuItem,
		Commands_Create_Copy_JMenuItem,
		Commands_Create_Delta_JMenuItem,
		Commands_Create_Disaggregate_JMenuItem,
		Commands_Create_LookupTimeSeriesFromTable_JMenuItem,
		Commands_Create_NewDayTSFromMonthAndDayTS_JMenuItem,
		Commands_Create_NewEndOfMonthTSFromDayTS_JMenuItem,
		Commands_Create_Normalize_JMenuItem,
		Commands_Create_RelativeDiff_JMenuItem,
    	Commands_Create_ResequenceTimeSeriesData_JMenuItem,
    	Commands_Create_RunningStatisticTimeSeries_JMenuItem,
		Commands_Create_NewStatisticTimeSeries_JMenuItem,
		Commands_Create_NewStatisticMonthTimeSeries_JMenuItem,
		Commands_Create_NewStatisticYearTS_JMenuItem;

	// Menu: Commands / Read Time Series

	public static JMenu Commands_ReadTimeSeries_JMenu = null;
	public static JMenuItem
    	Commands_Read_SetIncludeMissingTS_JMenuItem,
    	Commands_Read_SetInputPeriod_JMenuItem,
		//--
    	Commands_Read_CreateFromList_JMenuItem,
		Commands_Read_ReadColoradoHydroBaseRest_JMenuItem,
		Commands_Read_ReadDateValue_JMenuItem,
		Commands_Read_ReadDelftFewsPiXml_JMenuItem,
    	Commands_Read_ReadDelimitedFile_JMenuItem,
    	Commands_Read_ReadHecDss_JMenuItem,
		Commands_Read_ReadHydroBase_JMenuItem,
		Commands_Read_ReadMODSIM_JMenuItem,
		Commands_Read_ReadNrcsAwdb_JMenuItem,
		Commands_Read_ReadNwsCard_JMenuItem,
		Commands_Read_ReadNwsrfsFS5Files_JMenuItem,
		Commands_Read_ReadRccAcis_JMenuItem,
		Commands_Read_ReadReclamationHDB_JMenuItem,
		Commands_Read_ReadReclamationPisces_JMenuItem,
		Commands_Read_ReadRiverWare_JMenuItem,
		Commands_Read_ReadStateCU_JMenuItem,
		Commands_Read_ReadStateCUB_JMenuItem,
		Commands_Read_ReadStateMod_JMenuItem,
		Commands_Read_ReadStateModB_JMenuItem,
		Commands_Read_ReadTimeSeries_JMenuItem,
    	Commands_Read_ReadTimeSeriesFromDataStore_JMenuItem, // Duplicate of menu in Commands_Datastore.
		Commands_Read_ReadTimeSeriesList_JMenuItem,
		Commands_Read_ReadUsgsNwisDaily_JMenuItem,
		Commands_Read_ReadUsgsNwisGroundwater_JMenuItem,
		Commands_Read_ReadUsgsNwisInstantaneous_JMenuItem,
		Commands_Read_ReadUsgsNwisRdb_JMenuItem,
		Commands_Read_ReadWaterML_JMenuItem,
		Commands_Read_ReadWaterML2_JMenuItem,
		Commands_Read_ReadWaterOneFlow_JMenuItem,
		Commands_Read_StateModMax_JMenuItem;

	// Menu: Commands / Fill Time Series Missing Data

	public static JMenu
		Commands_FillTimeSeries_JMenu = null;
	public static JMenuItem
		Commands_Fill_FillConstant_JMenuItem,
		Commands_Fill_FillDayTSFrom2MonthTSAnd1DayTS_JMenuItem,
		Commands_Fill_FillFromTS_JMenuItem,
		Commands_Fill_FillHistMonthAverage_JMenuItem,
		Commands_Fill_FillHistYearAverage_JMenuItem,
		Commands_Fill_FillInterpolate_JMenuItem,
		Commands_Fill_FillMixedStation_JMenuItem,
		Commands_Fill_FillMOVE1_JMenuItem,
		Commands_Fill_FillMOVE2_JMenuItem,
		Commands_Fill_FillPattern_JMenuItem,
    	Commands_Fill_ReadPatternFile_JMenuItem,
		Commands_Fill_FillProrate_JMenuItem,
		Commands_Fill_FillRegression_JMenuItem,
		Commands_Fill_FillRepeat_JMenuItem,
		Commands_Fill_FillUsingDiversionComments_JMenuItem,

		Commands_Fill_SetAutoExtendPeriod_JMenuItem,
		Commands_Fill_SetAveragePeriod_JMenuItem,
		Commands_Fill_SetIgnoreLEZero_JMenuItem;

	// Menu: Commands / Set Time Series Contents

	public static JMenu
		Commands_SetTimeSeries_JMenu = null;
	public static JMenuItem
		//--
		Commands_Set_ReplaceValue_JMenuItem,
		//--
		Commands_Set_SetConstant_JMenuItem,
		Commands_Set_SetDataValue_JMenuItem,
    	Commands_Set_SetFromTS_JMenuItem,
    	Commands_Set_SetTimeSeriesValuesFromLookupTable_JMenuItem,
    	Commands_Set_SetTimeSeriesValuesFromTable_JMenuItem,
		Commands_Set_SetToMax_JMenuItem,
		Commands_Set_SetToMin_JMenuItem,
    	Commands_Set_SetTimeSeriesProperty_JMenuItem;

	// Menu: Commands / Manipulate Time Series

	public static JMenu
		Commands_ManipulateTimeSeries_JMenu = null;
	public static JMenuItem
		Commands_Manipulate_Add_JMenuItem,
		Commands_Manipulate_AddConstant_JMenuItem,
		Commands_Manipulate_AdjustExtremes_JMenuItem,
		Commands_Manipulate_ARMA_JMenuItem,
		Commands_Manipulate_Blend_JMenuItem,
    	Commands_Manipulate_ChangePeriod_JMenuItem,
    	Commands_Manipulate_ChangeTimeZone_JMenuItem,
		Commands_Manipulate_ConvertDataUnits_JMenuItem,
		Commands_Manipulate_Cumulate_JMenuItem,
		Commands_Manipulate_Divide_JMenuItem,
		Commands_Manipulate_Multiply_JMenuItem,
		Commands_Manipulate_Scale_JMenuItem,
		Commands_Manipulate_ShiftTimeByInterval_JMenuItem,
		Commands_Manipulate_Subtract_JMenuItem;

	// Menu: Commands / Analyze Time Series

	public static JMenu
		Commands_AnalyzeTimeSeries_JMenu = null;
	public static JMenuItem
		Commands_Analyze_AnalyzePattern_JMenuItem = null,
		Commands_Analyze_CalculateTimeSeriesStatistic_JMenuItem = null,
		Commands_Analyze_CompareTimeSeries_JMenuItem = null,
		Commands_Analyze_ComputeErrorTimeSeries_JMenuItem = null;

	// Menu: Commands / Models - Routing

	public static JMenu
		Commands_Models_Routing_JMenu = null;
	public static JMenuItem
		Commands_Models_Routing_LagK_JMenuItem = null,
		Commands_Models_Routing_VariableLagK_JMenuItem = null;

	// Menu: Commands / Output Time Series

	public static JMenu
		Commands_OutputTimeSeries_JMenu = null;
	public static JMenuItem
		Commands_Output_SetOutputDetailedHeaders_JMenuItem,
		Commands_Output_SetOutputPeriod_JMenuItem,
		Commands_Output_SetOutputYearType_JMenuItem,
		Commands_Output_WriteDateValue_JMenuItem,
		//Commands_Output_WriteDelftFewsPiXml_JMenuItem,
		Commands_Output_WriteDelimitedFile_JMenuItem,
		Commands_Output_WriteHecDss_JMenuItem,
		Commands_Output_WriteNwsCard_JMenuItem,
		Commands_Output_WriteReclamationHDB_JMenuItem,
		Commands_Output_WriteRiverWare_JMenuItem,
    	Commands_Output_WriteSHEF_JMenuItem,
		Commands_Output_WriteStateCU_JMenuItem,
		Commands_Output_WriteStateMod_JMenuItem,
		Commands_Output_WriteSummary_JMenuItem,
		Commands_Output_WriteTimeSeriesToDataStore_JMenuItem, // Also duplicated as Commands_Datastore_WriteTimeSeriesToDataStore.
		Commands_Output_WriteTimeSeriesToDataStream_JMenuItem,
		Commands_Output_WriteTimeSeriesToHydroJSON_JMenuItem,
		Commands_Output_WriteTimeSeriesToJson_JMenuItem,
		Commands_Output_WriteWaterML_JMenuItem,
		Commands_Output_WriteWaterML2_JMenuItem,
		Commands_Output_WriteTimeSeriesPropertiesToFile_JMenuItem; // Also duplicated in testing commands Commands_General_TestProcessing_WriteTimeSeriesPropertiesToFile.

	// Menu: Commands / Check Time Series

	public static JMenu
    	Commands_Check_CheckTimeSeries_JMenu = null;
	public static JMenuItem
    	Commands_Check_CheckingResults_CheckTimeSeries_JMenuItem = null,
    	Commands_Check_CheckingResults_CheckTimeSeriesStatistic_JMenuItem = null,
    	Commands_Check_CheckingResults_WriteCheckFile_JMenuItem = null;

	// Menu: Commands / Datastore Processing

	public static JMenu
    	Commands_Datastore_JMenu = null;
	public static JMenuItem
    	Commands_Datastore_NewAccessDatabase_JMenuItem,
    	Commands_Datastore_NewDerbyDatabase_JMenuItem,
    	Commands_Datastore_NewSQLiteDatabase_JMenuItem,
    	Commands_Datastore_OpenDataStore_JMenuItem,
    	Commands_Datastore_ReadTableFromDataStore_JMenuItem,
    	Commands_Datastore_WriteTableToDataStore_JMenuItem,
    	Commands_Datastore_DeleteDataStoreTableRows_JMenuItem,
    	Commands_Datastore_RunSql_JMenuItem,
    	Commands_Datastore_ReadTimeSeriesFromDataStore_JMenuItem,
    	Commands_Datastore_WriteTimeSeriesToDataStore_JMenuItem,
    	Commands_Datastore_CloseDataStore_JMenuItem,
    	Commands_Datastore_CreateDataStoreDataDictionary_JMenuItem,
    	Commands_Datastore_SetPropertyFromDataStore_JMenuItem;

	// Menu: Commands / Ensemble Processing

	public static JMenu
    	Commands_Ensemble_JMenu = null;
	public static JMenuItem
    	Commands_Ensemble_CreateEnsembleFromOneTimeSeries_JMenuItem,
    	Commands_Ensemble_CopyEnsemble_JMenuItem,
    	Commands_Ensemble_NewEnsemble_JMenuItem,
    	Commands_Ensemble_ReadNwsrfsEspTraceEnsemble_JMenuItem,
    	Commands_Ensemble_InsertTimeSeriesIntoEnsemble_JMenuItem,
    	Commands_Ensemble_SetEnsembleProperty_JMenuItem,
    	Commands_Ensemble_NewStatisticEnsemble_JMenuItem,
    	Commands_Ensemble_NewStatisticTimeSeriesFromEnsemble_JMenuItem,
    	Commands_Ensemble_WeightTraces_JMenuItem,
    	Commands_Ensemble_WriteNwsrfsEspTraceEnsemble_JMenuItem;

	// Menu: Commands / Network Processing

	public static JMenu
    	Commands_Network_JMenu = null;
	public static JMenuItem
		Commands_Network_CreateNetworkFromTable_JMenuItem,
    	Commands_Network_AnalyzeNetworkPointFlow_JMenuItem;

	// Menu: Commands / Object Processing

	public static JMenu
    	Commands_Object_JMenu = null;
	public static JMenuItem
    	Commands_Object_NewObject_JMenuItem,
    	Commands_Object_FreeObject_JMenuItem,
    	// ----
    	Commands_Object_ReadTableFromJSON_JMenuItem,
    	// ----
    	Commands_Object_SetObjectProperty_JMenuItem,
    	Commands_Object_SetObjectPropertiesFromTable_JMenuItem,
    	Commands_Object_SetPropertyFromObject_JMenuItem,
    	// ----
    	Commands_Object_WriteObjectToJSON_JMenuItem;

	// Menu: Commands / Spatial Processing

	public static JMenu
    	Commands_Spatial_JMenu = null;
	public static JMenuItem
		Commands_Spatial_WriteTableToGeoJSON_JMenuItem,
    	Commands_Spatial_WriteTableToKml_JMenuItem,
    	Commands_Spatial_WriteTableToShapefile_JMenuItem,
    	Commands_Spatial_WriteTimeSeriesToGeoJSON_JMenuItem,
    	Commands_Spatial_WriteTimeSeriesToKml_JMenuItem,
    	// ----
    	Commands_Spatial_GeoMapProject_JMenuItem,
    	Commands_Spatial_GeoMap_JMenuItem;

	// Menu: Commands / Spreadsheet Processing

	public static JMenu
    	Commands_Spreadsheet_JMenu = null;
	public static JMenuItem
    	Commands_Spreadsheet_NewExcelWorkbook_JMenuItem,
    	Commands_Spreadsheet_ReadExcelWorkbook_JMenuItem,
    	Commands_Spreadsheet_ReadTableFromExcel_JMenuItem,
    	Commands_Spreadsheet_ReadTableCellsFromExcel_JMenuItem,
    	Commands_Spreadsheet_ReadPropertiesFromExcel_JMenuItem,
    	Commands_Spreadsheet_SetExcelCell_JMenuItem,
    	Commands_Spreadsheet_SetExcelWorksheetViewProperties_JMenuItem,
    	Commands_Spreadsheet_WriteTableToExcel_JMenuItem,
    	Commands_Spreadsheet_WriteTableCellsToExcel_JMenuItem,
    	Commands_Spreadsheet_WriteTimeSeriesToExcel_JMenuItem,
    	Commands_Spreadsheet_WriteTimeSeriesToExcelBlock_JMenuItem,
    	Commands_Spreadsheet_CloseExcelWorkbook_JMenuItem;

	// Menu: Commands / Template Processing

	public static JMenu
    	Commands_Template_JMenu = null;
	public static JMenuItem
    	Commands_Template_ExpandTemplateFile_JMenuItem,
    	Commands_Template_Comments_Template_JMenuItem;

	// Menu: Commands / Visualization Processing

	public static JMenu
    	Commands_Visualization_JMenu = null;
	public static JMenuItem
    	Commands_Visualization_ProcessTSProduct_JMenuItem,
    	Commands_Visualization_ProcessRasterGraph_JMenuItem,
    	Commands_Visualization_NewTreeView_JMenuItem;

	// Menu: Commands / General - Comments

	public static JMenu
    	Commands_General_Comments_JMenu = null;
	public static JMenuItem
		Commands_General_Comments_Comment_JMenuItem = null,
		Commands_General_Comments_StartComment_JMenuItem = null,
		Commands_General_Comments_EndComment_JMenuItem = null,
		// ------------------
		Commands_General_Comments_AuthorComment_JMenuItem = null,
		Commands_General_Comments_VersionComment_JMenuItem = null,
		Commands_General_Comments_VersionDateComment_JMenuItem = null,
		Commands_General_Comments_SourceUrlComment_JMenuItem = null,
		Commands_General_Comments_DocUrlComment_JMenuItem = null,
		// ------------------
		Commands_General_Comments_ReadOnlyComment_JMenuItem = null,
		Commands_General_Comments_RunDiscoveryFalseComment_JMenuItem = null,
		Commands_General_Comments_TemplateComment_JMenuItem = null,
		// ------------------
		Commands_General_Comments_EnabledComment_JMenuItem = null,
		Commands_General_Comments_EnabledIfApplicationComment_JMenuItem = null,
		Commands_General_Comments_EnabledIfDatastoreComment_JMenuItem = null,
		// ------------------
		Commands_General_Comments_ExpectedStatusFailureComment_JMenuItem = null,
		Commands_General_Comments_ExpectedStatusWarningComment_JMenuItem = null,
		//Commands_General_Comments_FileModTimeComment_JMenuItem = null,
		//Commands_General_Comments_FileSizeComment_JMenuItem = null,
		Commands_General_Comments_IdComment_JMenuItem = null,
		Commands_General_Comments_OrderComment_JMenuItem = null,
		// ------------------
		Commands_General_Comments_RequireApplicationComment_JMenuItem = null,
		Commands_General_Comments_RequireDatastoreComment_JMenuItem = null,
		Commands_General_Comments_RequireUserComment_JMenuItem = null,
		// ------------------
		Commands_General_Comments_FixMeComment_JMenuItem = null,
		Commands_General_Comments_ToDoComment_JMenuItem = null,
		// ------------------
		Commands_General_Comments_DocExampleComment_JMenuItem = null,
		// ------------------
		Commands_General_Comments_Empty_JMenuItem = null;

	// Menu: Commands / General - File Handling

	public static JMenu
    	Commands_General_FileHandling_JMenu = null;
	public static JMenuItem
    	Commands_General_FileHandling_FTPGet_JMenuItem = null,
    	Commands_General_FileHandling_WebGet_JMenuItem = null,
    	Commands_General_FileHandling_CreateFolder_JMenuItem = null,
    	Commands_General_FileHandling_RemoveFolder_JMenuItem = null,
    	Commands_General_FileHandling_AppendFile_JMenuItem = null,
    	Commands_General_FileHandling_CheckFile_JMenuItem = null,
    	Commands_General_FileHandling_CopyFile_JMenuItem = null,
    	Commands_General_FileHandling_FormatFile_JMenuItem = null,
    	Commands_General_FileHandling_ListFiles_JMenuItem = null,
    	Commands_General_FileHandling_RemoveFile_JMenuItem = null,
    	Commands_General_FileHandling_TextEdit_JMenuItem = null,
    	Commands_General_FileHandling_PDFMerge_JMenuItem = null,
    	Commands_General_FileHandling_UnzipFile_JMenuItem = null,
    	Commands_General_FileHandling_PrintTextFile_JMenuItem = null;

	// Menu: Commands / General - Logging and Messaging

	public static JMenu
		Commands_General_Logging_JMenu = null;
	public static JMenuItem
		Commands_General_Logging_ConfigureLogging_JMenuItem = null,
		Commands_General_Logging_StartLog_JMenuItem = null,
		Commands_General_Logging_SetDebugLevel_JMenuItem = null,
		Commands_General_Logging_SetWarningLevel_JMenuItem = null,
		Commands_General_Logging_Message_JMenuItem = null,
		Commands_General_Logging_SendEmailMessage_JMenuItem = null;

	// Menu: Commands / General - Running and Properties

	public static JMenu
    	Commands_General_Running_JMenu = null;
	public static JMenuItem
    	Commands_General_Running_ReadPropertiesFromFile_JMenuItem = null,
    	Commands_General_Running_SetProperty_JMenuItem = null,
    	Commands_General_Running_SetPropertyFromNwsrfsAppDefault_JMenuItem = null,
    	Commands_General_Running_SetPropertyFromEnsemble_JMenuItem = null,
    	Commands_General_Running_SetPropertyFromTimeSeries_JMenuItem = null,
    	Commands_General_Running_FormatDateTimeProperty_JMenuItem = null,
    	Commands_General_Running_FormatStringProperty_JMenuItem = null,
    	Commands_General_Running_WritePropertiesToFile_JMenuItem = null,
		Commands_General_Running_RunCommands_JMenuItem = null,
		Commands_General_Running_RunProgram_JMenuItem = null,
    	Commands_General_Running_RunPython_JMenuItem = null,
    	Commands_General_Running_RunR_JMenuItem = null,
    	Commands_General_Running_RunDSSUTL_JMenuItem = null,
    	Commands_General_Running_If_JMenuItem = null,
    	Commands_General_Running_EndIf_JMenuItem = null,
    	Commands_General_Running_For_JMenuItem = null,
    	Commands_General_Running_EndFor_JMenuItem = null,
    	Commands_General_Running_Break_JMenuItem = null,
    	Commands_General_Running_Continue_JMenuItem = null,
    	Commands_General_Running_Exit_JMenuItem = null,
    	Commands_General_Running_Wait_JMenuItem = null,
    	Commands_General_Running_SetWorkingDir_JMenuItem = null,
    	Commands_General_Running_ProfileCommands_JMenuItem = null;

	// Menu: Commands / General - Test Processing

	public static JMenu
    	Commands_General_TestProcessing_JMenu = null;
	public static JMenuItem
    	Commands_General_TestProcessing_CompareFiles_JMenuItem = null,
    	Commands_General_TestProcessing_CompareTables_JMenuItem = null,
    	Commands_General_TestProcessing_CompareTimeSeries_JMenuItem = null,
    	Commands_General_TestProcessing_WriteTimeSeriesPropertiesToFile_JMenuItem = null,
    	Commands_General_TestProcessing_WriteTimeSeriesProperty_JMenuItem = null,
    	//-- separator ---
    	Commands_General_TestProcessing_CreateRegressionTestCommandFile_JMenuItem = null,
    	Commands_General_TestProcessing_StartRegressionTestResultsReport_JMenuItem = null,
    	//-- separator ---
    	Commands_General_TestProcessing_TestCommand_JMenuItem = null;

	// Menu: Commands (Deprecated) - legacy commands that will be phased out.

	public static JMenu
		Commands_Deprecated_JMenu = null;
	public static JMenuItem
		Commands_Deprecated_OpenHydroBase_JMenuItem,
		Commands_Deprecated_RunningAverage_JMenuItem;

	// Menu: Commands (Table) / Create, Copy, Free Table

	public static JMenu
		Commands_Table_JMenu = null;
	public static JMenu
		Commands_TableCreate_JMenu = null;
	public static JMenuItem
    	Commands_TableCreate_NewTable_JMenuItem,
    	Commands_TableCreate_CopyTable_JMenuItem,
		Commands_TableCreate_FreeTable_JMenuItem;

	// Menu: Commands (Table) / Read Table

	public static JMenu
		Commands_TableRead_JMenu = null;
	public static JMenuItem
    	Commands_TableRead_ReadTableFromDataStore_JMenuItem, // Uses string from Commands_Datastore_ReadTableFromDataStore
    	Commands_TableRead_ReadTableFromDelimitedFile_JMenuItem,
    	Commands_TableRead_ReadTableFromDBF_JMenuItem,
    	Commands_TableRead_ReadTableFromExcel_JMenuItem, // Uses string from Commands_Spreadsheet_ReadTableFromExcel
    	Commands_TableRead_ReadTableFromFixedFormatFile_JMenuItem,
    	Commands_TableRead_ReadTableFromJSON_JMenuItem,
    	Commands_TableRead_ReadTableFromXML_JMenuItem;

	// Menu: Commands (Table) / Append, Join Tables

	public static JMenu
		Commands_TableJoin_JMenu = null;
	public static JMenuItem
    	Commands_TableJoin_AppendTable_JMenuItem,
    	Commands_TableJoin_JoinTables_JMenuItem,
    	Commands_TableManipulate_SortTable_JMenuItem;

	// Menu: Commands (Table) / Table, Time Series Processing

	public static JMenu
		Commands_TableTimeSeries_JMenu = null;
	public static JMenuItem
    	Commands_TableTimeSeries_TimeSeriesToTable_JMenuItem,
    	Commands_TableTimeSeries_TableToTimeSeries_JMenuItem,
    	Commands_TableTimeSeries_CreateTimeSeriesEventTable_JMenuItem,
		Commands_TableTimeSeries_SetTimeSeriesPropertiesFromTable_JMenuItem,
		Commands_TableTimeSeries_CopyTimeSeriesPropertiesToTable_JMenuItem;

	// Menu: Commands (Table) / Manipulate Table Values

	public static JMenu
		Commands_TableManipulate_JMenu = null;
	public static JMenuItem
    	Commands_TableManipulate_FormatTableDateTime_JMenuItem,
    	Commands_TableManipulate_FormatTableString_JMenuItem,
    	Commands_TableManipulate_ManipulateTableString_JMenuItem,
    	Commands_TableManipulate_InsertTableColumn_JMenuItem,
    	Commands_TableManipulate_DeleteTableColumns_JMenuItem,
    	Commands_TableManipulate_DeleteTableRows_JMenuItem,
    	Commands_TableManipulate_InsertTableRow_JMenuItem,
    	Commands_TableManipulate_RenameTableColumns_JMenuItem,
    	Commands_TableManipulate_SetTableColumnProperties_JMenuItem,
    	Commands_TableManipulate_SetTableValues_JMenuItem,
    	Commands_TableManipulate_SplitTableColumn_JMenuItem,
    	Commands_TableManipulate_SplitTableRow_JMenuItem,
    	Commands_TableManipulate_TableMath_JMenuItem,
    	Commands_TableManipulate_TableTimeSeriesMath_JMenuItem;

	// Menu: Commands (Table) / Analyze Table

	public static JMenu
		Commands_TableAnalyze_JMenu;
	public static JMenuItem
    	Commands_TableAnalyze_CompareTables_JMenuItem;

	// Menu: Commands (Table) / Output Table

	public static JMenu
		Commands_TableOutput_JMenu;
	public static JMenuItem
    	Commands_TableOutput_WriteTableToDataStore_JMenuItem, // Also duplicated in Commands_Datastore_WriteTableToDataStore
    	Commands_TableOutput_WriteTableToExcel_JMenuItem, // Also duplicated in Commands_Spreadsheet_WriteTableToExcel
    	Commands_TableOutput_WriteTableToDelimitedFile_JMenuItem,
    	Commands_TableOutput_WriteTableToMarkdown_JMenuItem,
    	Commands_TableOutput_WriteTableToHTML_JMenuItem;

	// Menu: Commands (Table) / Running and Properties

	public static JMenu
		Commands_TableRunning_JMenu;
	public static JMenuItem
		Commands_TableRunning_SetPropertyFromTable_JMenuItem,
		Commands_TableRunning_CopyPropertiesToTable_JMenuItem;

	// Menu: Commands(Plugin)

	public static JMenu
		Commands_Plugin_JMenu = null;

	// Menu: Run

	public static JMenu
		Run_JMenu = null;
	public static JMenuItem
		Run_AllCommandsCreateOutput_JMenuItem,
		Run_AllCommandsIgnoreOutput_JMenuItem,
		Run_SelectedCommandsCreateOutput_JMenuItem,
		Run_SelectedCommandsIgnoreOutput_JMenuItem,
		Run_CancelCommandProcessing_WaitForCommand_JMenuItem,
		Run_CancelCommandProcessing_InterruptProcessor_JMenuItem,
		Run_CommandsFromFile_JMenuItem,
		Run_ProcessTSProductPreview_JMenuItem,
		Run_ProcessTSProductOutput_JMenuItem;

	// Menu: Results

	public static JMenu
		Results_JMenu = null,
		Results_Graph_JMenu = null;
	public static JMenuItem
		Results_Graph_AnnualTraces_JMenuItem = null,
		Results_Graph_Area_JMenuItem = null,
		Results_Graph_AreaStacked_JMenuItem = null,
		Results_Graph_BarsLeft_JMenuItem = null,
		Results_Graph_BarsCenter_JMenuItem = null,
		Results_Graph_BarsRight_JMenuItem = null,
		Results_Graph_DoubleMass_JMenuItem = null,
		Results_Graph_Duration_JMenuItem = null,
		Results_Graph_Ensemble_JMenuItem = null,
		Results_Graph_Line_JMenuItem = null,
		Results_Graph_LineLogY_JMenuItem = null,
		Results_Graph_PercentExceedance_JMenuItem = null,
		Results_Graph_PeriodOfRecord_JMenuItem = null,
		Results_Graph_Point_JMenuItem = null,
		Results_Graph_PredictedValue_JMenuItem = null,
		Results_Graph_PredictedValueResidual_JMenuItem = null,
		Results_Graph_XYScatter_JMenuItem = null,
		Results_Graph_Raster_JMenuItem = null,
		Results_Table_JMenuItem = null,
		Results_Report_Summary_JMenuItem = null,
		Results_TimeSeries_FindTimeSeries_JMenuItem = null,
		Results_SelectAllForOutput_JMenuItem = null,
		Results_DeselectAll_JMenuItem = null,
		Results_TimeSeriesProperties_JMenuItem = null;

	// Menu: Tools

	public static JMenu
		Tools_JMenu = null,
		Tools_Analysis_JMenu = null;
	public static JMenuItem
		Tools_Analysis_PrincipalComponentAnalysis_JMenuItem = null,
		Tools_Analysis_MixedStationAnalysis_JMenuItem = null;
	public static JMenu Tools_Commands_JMenu = null;
	public static JMenu
		Tools_Datastore_JMenu = null;
	public static JMenuItem
		Tools_Datastore_ERDiagram_JMenuItem = null;
	public static JMenuItem
		Tools_DateTimeTools_JMenuItem = null;
	public static JMenu
		Tools_Report_JMenu = null;
	public static JMenuItem
		Tools_Report_DataCoverageByYear_JMenuItem = null,
		Tools_Report_DataLimitsSummary_JMenuItem = null,
		Tools_Report_MonthSummaryDailyMeans_JMenuItem = null,
		Tools_Report_MonthSummaryDailyTotals_JMenuItem = null,
		Tools_Report_YearToDateTotal_JMenuItem = null,
		Tools_Report_YearMeanMeanStatistic = null,
		Tools_Report_YearTotalMeanStatistic = null;
	public static JMenu
		Tools_NWSRFS_JMenu = null;
	public static JMenuItem
		Tools_NWSRFS_ConvertNWSRFSESPTraceEnsemble_JMenuItem = null,
		Tools_NWSRFS_ConvertJulianHour_JMenuItem = null;
	public static JMenuItem
		Tools_SelectOnMap_JMenuItem = null,

		Tools_Options_JMenuItem = null,
		Tools_ViewLogFile_Startup_JMenuItem = null;

	// Menu: Help

	public static JMenu
		Help_JMenu = null;
	public static JMenuItem
		Help_AboutTSTool_JMenuItem = null,
		Help_ViewDocumentation_JMenuItem = null,
		Help_ViewDocumentation_ReleaseNotes_JMenuItem = null,
		Help_ViewDocumentation_UserManual_JMenuItem = null,
		Help_ViewDocumentation_CommandReference_JMenuItem = null,
		Help_ViewDocumentation_DatastoreReference_JMenuItem = null,
		Help_ViewDocumentation_Troubleshooting_JMenuItem = null,
		Help_View_TrainingMaterials_JMenuItem = null,
		Help_CheckForUpdates_JMenuItem = null,
		Help_ImportConfiguration_JMenuItem = null;

}