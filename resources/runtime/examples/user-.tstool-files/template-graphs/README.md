# template-graphs #

This folder contains template graph files that can be copied to a user's
`.tstool/template-graphs/` folder in their user files.
This enables creating graphs that are more specifically-formatted than the
built-in graph defaults.
See also:  [TSTool documentation](http://opencdss.state.co.us/tstool/latest/doc-user/appendix-tsview/tsview/)

Template files include:

| **Template File** | **Data** | **UI Action** | **Description** |
| ----------------- | -------- | ------------- | --------------- | 
| `EnsembleCalcStatsDefault.tsp` | 1 ensemble | ***Graph with Template*** | Simple template that formats a title for the graph and calculates ensemble basic statistics.  A companion TSTool command file is used to process the data. |
| `EnsembleGraphMenu.tsp` | 1 ensemble | ***Graph / Ensemble*** menu | Graph ensemble with options. |
| `TSAsEnsembleCalcStatsDefault.tsp` | 1 time series | ***Graph with Template*** button | Convert time series to ensemble and then graph using default.  A companion TSTool command file is used to process the data. |
| `TSAsEnsembleGraphMenu.tsp` | 1 time series | ***Graph / Ensemble*** menu | Convert a time series to an ensemble with options. | 
| `TSDefault.tsp` | 1 time series | ***Graph with Template*** button | Simple template that formats a title for the graph. |
