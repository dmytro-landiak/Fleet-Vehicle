
package com.landim.openforecast.models;


public class WeightedMovingAverageModel extends AbstractTimeBasedModel
{
    /**
     * The weights to assign to each period. These weights must add up to 1.0.
     */
    private double[] weights;
    

    public WeightedMovingAverageModel( double[] weights )
    {
        setWeights( weights );
    }
    

    public WeightedMovingAverageModel( String independentVariable,
                       double[] weights )
    {
        super( independentVariable );
        setWeights( weights );
    }


    protected WeightedMovingAverageModel()
    {
    }
    

    protected WeightedMovingAverageModel( String independentVariable )
    {
        super( independentVariable );
    }
    

    protected void setWeights( double[] weights )
    {
        int periods = weights.length;
        
        // Check sum of weights adds up to 1.0
        double sum = 0.0;
        for ( int w=0; w<periods; w++ )
            sum += weights[w];
        
        // If sum of weights does not add up to 1.0,
        //  then an adjustment is needed
        boolean adjust = false;
        if ( Math.abs( sum - 1.0 ) > TOLERANCE )
            adjust = true;
        
        // Save weights or adjusted weights
        this.weights = new double[ periods ];
        for ( int w=0; w<periods; w++ )
            this.weights[w] = (adjust ? weights[w]/sum : weights[w]);
    }
    

    protected double forecast( double timeValue )
        throws IllegalArgumentException
    {
        int periods = getNumberOfPeriods();
        double t = timeValue;
        double timeDiff = getTimeInterval();
        
        if ( timeValue - timeDiff*periods < getMinimumTimeValue() )
            return getObservedValue( t );
        
        double forecast = 0.0;
        for ( int p=periods-1; p>=0; p-- )
            {
                t -= timeDiff;
                try
                    {
                        forecast += weights[p]*getObservedValue( t );
                    }
                catch ( IllegalArgumentException iaex )
                    {
                        forecast += weights[p]*getForecastValue( t );
                    }
            }
        
        return forecast;
    }
    
    /**
     * Returns the number of predictors used by the underlying model.
     * @return the number of predictors used by the underlying model.
     */
    public int getNumberOfPredictors()
    {
        return 1;
    }
    
    /**
     * Returns the current number of periods used in this model.
     * @return the current number of periods used in this model.
     */
    protected int getNumberOfPeriods()
    {
        return weights.length;
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
        return "Weighted Moving Average";
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
        return "weighted moving average model, spanning "
            + getNumberOfPeriods()
            + " periods and using an independent variable of "
            + getIndependentVariable() + ".";
    }
}
// Local Variables:
// tab-width: 4
// End:
