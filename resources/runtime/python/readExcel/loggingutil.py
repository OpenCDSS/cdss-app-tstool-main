# Logging utilities
# TODO SAM 2010-07-06 Probably a better way to do this (custom handler?

warningCount = 0

def warning ( logger, warning ):
    """
    Utility method to print a warning to the logger and keep a count of warnings.
    """
    logger.warning ( warning )
    global warningCount
    warningCount += 1
    return