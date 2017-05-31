
package com.landim.openforecast.models;


public class InsufficientDataException extends Exception
{
    private static final long serialVersionUID = 500L;
    
    /**
     * Default constructor. If possible, use the one argument constructor and
     * give more information about the lack of data.
     */
    public InsufficientDataException()
    {
        super();
    }
    
    /**
     * Constructs a new InsufficientDataException with the given reason.
     * This is the preferred constructor.
     * @param reason the reason for, or details about, the
     * InsufficientDataException.
     */
    public InsufficientDataException( String reason )
    {
        super( reason );
    }
}
// Local Variables:
// tab-width: 4
// End:
