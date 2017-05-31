
package com.landim.openforecast.models;

public class ModelNotInitializedException extends RuntimeException
{
    private static final long serialVersionUID = 500L;
    
    /**
     * Constructs a new ModelNotInitializedException with no detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to java.lang.Throwable.initCause(). 
     */
    public ModelNotInitializedException()
    {
    }
    
    /**
     * Constructs a new ModelNotInitializedException with the specified detail
     * message.
     * @param msg the detail message. The detail message is saved for later
     *        retrieval by the java.lang.Throwable.getMessage() method.
     */
    public ModelNotInitializedException( String msg )
    {
        super( msg );
    }
}
// Local variables:
// tab-width: 4
// End:
