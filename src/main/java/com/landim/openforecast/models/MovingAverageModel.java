package com.landim.openforecast.models;


import com.landim.openforecast.DataSet;


public class MovingAverageModel extends WeightedMovingAverageModel
{

    public MovingAverageModel()
    {
    }
    

    public MovingAverageModel( String independentVariable )
    {
        super( independentVariable );
    }
    

    public MovingAverageModel( int period )
    {
        double[] weights = new double[period];
        for ( int p=0; p<period; p++ )
            weights[p] = 1.0/period;
        
        setWeights( weights );
    }

    public MovingAverageModel( String independentVariable, int period )
    {
        super( independentVariable );
        
        double[] weights = new double[period];
        for ( int p=0; p<period; p++ )
            weights[p] = 1.0/period;
        
        setWeights( weights );
    }

    public void init( DataSet dataSet )
    {
        if ( getNumberOfPeriods() <= 0 )
            {
                // Number of periods has not yet been defined
                //  - what's a reasonable number to use?
                
                // Use maximum number of periods as a default
                int period = getNumberOfPeriods();
                
                // Set weights for moving average model
                double[] weights = new double[period];
                for ( int p=0; p<period; p++ )
                    weights[p] = 1/period;
                
                setWeights( weights );
            }
        
        super.init( dataSet );
    }
    
    /**
     * Returns a one or two word name of this type of forecasting model. Keep
     * this short. A longer description should be implemented in the toString
     * method.
     * @return a string representation of the type of forecasting model
     *         implemented.
     */
    public String getForecastType()
    {
        return "Moving average";
    }
    
    /**
     * This should be overridden to provide a textual description of the
     * current forecasting model including, where possible, any derived
     * parameters used.
     * @return a string representation of the current forecast model, and its
     *         parameters.
     */
    public String toString()
    {
        return "Moving average model, spanning " + getNumberOfPeriods()
            + " periods and using an independent variable of "
            + getIndependentVariable()+".";
    }
}
// Local Variables:
// tab-width: 4
// End:
