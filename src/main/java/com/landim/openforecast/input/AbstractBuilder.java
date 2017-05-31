package com.landim.openforecast.input;


import java.util.ArrayList;



public abstract class AbstractBuilder implements Builder
{
    /** The number of variables to be defined in this Builder. */
    private int n = 0;
    
    /**
     * An array of (independent) variable names.
     */
    private ArrayList<String> varName = new ArrayList<String>();
    
    /**
     * Adds the variable name to the end of the list of variables currently
     * defined for this Builder.
     * @param name the name of the new variable.
     */
    protected void addVariable( String name )
    {
        setVariableName( n, name );
    }
    
    /**
     * Sets the name for the variable with the given index to the given name.
     * @param index the index of the variable whose name to set.
     * @param name the name of the given variable.
     */
    protected void setVariableName( int index, String name )
    {
        varName.add( index, name );
        n = varName.size();
    }
    
    /**
     * Returns the number of variables currently defined in this
     * AbstractBuilder.
     * @return the number of variables currently defined.
     */
    protected int getNumberOfVariables()
    {
        return n;
    }
    
    /**
     * Sets the number of variables to be defined/referenced by this
     * AbstractBuilder. If no variable names are later defined, then this
     * is used to return the number of variables defined.
     * @param n the number of variables to be defined.
     */
    protected void setNumberOfVariables( int n )
    {
        this.n = n;
    }
    
    /**
     * Returns the variable name with the given index. If one or more variable
     * names have already been defined, then this method will only return one
     * of the variable names that were previously defined. If a variable name
     * for the given index does not exist, then an IndexOutOfBoundsException
     * will be thrown.
     *
     * <p>If no variable names have been defined, then it is assumed that the
     * "default" names, x<sub>i</sub>, should be used. In this case, an
     * exception will only be thrown if the index number requested exceeds
     * the number of variables set using a previous call to
     * {@link #setNumberOfVariables}.
     * @param index the index of the variable whose name to get.
     * @throws IndexOutOfBoundsException if one or more variable names have
     * been specified, but not one with the given index; or the number of
     * variables has been defined (with no variable names being specified)
     * and the index exceeds the number of variables defined.
     */
    protected String getVariableName( int index )
    {
        if ( varName.size() == 0 )
            {
                if ( index >= n )
                    throw new
                        IndexOutOfBoundsException("getVariableName("+index
                                                  +") called with only "+n
                                                  +" variables defined");
                
                return "x"+(index+1);
            }
        
        if ( index > n )
            throw new 
                IndexOutOfBoundsException("getVariableName("+index
                                          +") called with only "+n
                                          +" variables defined");
        
        
        return (String)varName.get( index );
    }
}
// Local variables:
// tab-width: 4
// End:
