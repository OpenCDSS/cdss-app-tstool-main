package DWR.DMI.tstool;

/**
This class provides an enumeration of possible command editing actions.
*/
public enum CommandEditType {
	
    /**
     * Convert from one command to another (e.g., TSID to ReadXXX() command).
     */
    CONVERT("Convert"),
    
    /**
     * Insert a new command. 
     */
    INSERT("Insert"),
	
	/**
	 * Update an existing command.
	 */
	UPDATE("Update");
    
    /**
     * The string name that should be displayed.
     */
    private final String displayName;
    
    /**
     * Construct an enumeration value.
     * @param displayName name that should be displayed in choices, etc.
     */
    private CommandEditType(String displayName) {
        this.displayName = displayName;
    }
    
    /**
     * Prevent common programming error of using the equals method instead of ==.
     */
    public boolean equals ( String editType ) {
        if ( editType.equalsIgnoreCase(this.displayName) ) {
            return true;
        }
        else {
            return false;
        }
    }
	
    /**
     * Return the display name for the edit type.  This is usually the same as the
     * value but using appropriate mixed case.
     * @return the display name.
     */
    @Override
    public String toString() {
        return displayName;
    }
	
	/**
	 * Return the enumeration value given a string name (case-independent).
	 * @return the enumeration value given a string name (case-independent), or null if not matched.
	 */
	public static CommandEditType valueOfIgnoreCase(String name)
	{
	    if ( name == null ) {
	        return null;
	    }
	    // Currently supported values
	    for ( CommandEditType t : values() ) {
	        if ( name.equalsIgnoreCase(t.toString()) ) {
	            return t;
	        }
	    } 
	    return null;
	}
}