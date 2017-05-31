
package com.landim.openforecast.models;


import com.landim.openforecast.DataSet;


public class SimpleExponentialSmoothingModel extends AbstractTimeBasedModel
{

    private static double DEFAULT_SMOOTHING_CONSTANT_TOLERANCE = 0.001;


    public static final int HUNTER = 1;
    public static final int ROBERTS = 2;
    private double alpha;
    

    private int approach;


    public static SimpleExponentialSmoothingModel
        getBestFitModel( DataSet dataSet )
    {
        return getBestFitModel( dataSet,
                                DEFAULT_SMOOTHING_CONSTANT_TOLERANCE );
    }


    public static SimpleExponentialSmoothingModel
        getBestFitModel( DataSet dataSet, double alphaTolerance )
    {
        SimpleExponentialSmoothingModel model1
            = new SimpleExponentialSmoothingModel( 0.0 );
        SimpleExponentialSmoothingModel model2
            = new SimpleExponentialSmoothingModel( 0.5 );
        SimpleExponentialSmoothingModel model3
            = new SimpleExponentialSmoothingModel( 1.0 );

        return findBestFit( dataSet, model1, model2, model3, TOLERANCE );
    }


    private static SimpleExponentialSmoothingModel findBestFit(
                        DataSet dataSet,
                        SimpleExponentialSmoothingModel modelMin,
                        SimpleExponentialSmoothingModel modelMid,
                        SimpleExponentialSmoothingModel modelMax,
                        double alphaTolerance)
    {
        double alphaMin = modelMin.getAlpha();
        double alphaMid = modelMid.getAlpha();
        double alphaMax = modelMax.getAlpha();

        // If we're not making much ground, then we're done
        if (Math.abs(alphaMid-alphaMin)<alphaTolerance
            && Math.abs(alphaMax-alphaMid)<alphaTolerance )
            return modelMid;

        SimpleExponentialSmoothingModel model[]
            = new SimpleExponentialSmoothingModel[5];
        model[0] = modelMin;
        model[1]
            = new SimpleExponentialSmoothingModel((alphaMin+alphaMid)/2.0);
        model[2] = modelMid;
        model[3]
            = new SimpleExponentialSmoothingModel((alphaMid+alphaMax)/2.0);
        model[4] = modelMax;

        for ( int m=0; m<5; m++ )
            model[m].init(dataSet);

        int bestModelIndex = 0;
        for ( int m=1; m<5; m++ )
            if ( model[m].getMSE() < model[bestModelIndex].getMSE() )
                bestModelIndex = m;

        switch ( bestModelIndex )
            {
            case 1:
                // Reduce maximums
                // Can discard models 3 and 4
                model[3] = null;
                model[4] = null;
                return findBestFit( dataSet, model[0], model[1], model[2],
                                    alphaTolerance );

            case 2:
                // Can discard models 0 and 4
                model[0] = null;
                model[4] = null;
                return findBestFit( dataSet, model[1], model[2], model[3],
                                    alphaTolerance );
                
            case 3:
                // Reduce minimums
                // Can discard models 0 and 1
                model[0] = null;
                model[1] = null;
                return findBestFit( dataSet, model[2], model[3], model[4],
                                    alphaTolerance );

            case 0:
            case 4:
                // We're done???
                //  - these cases should not occur, unless the MSE for model[0]
                //    and model[1], or model[3] and model[4] are equal
                break;
            }

        // Release all but the best model constructed so far
        for ( int m=0; m<5; m++ )
            if ( m != bestModelIndex )
                model[m] = null;
        
        return model[bestModelIndex];
    }


    public SimpleExponentialSmoothingModel( double alpha )
    {
        this(alpha,HUNTER);
    }
    

    public SimpleExponentialSmoothingModel( String independentVariable,
                                            double alpha )
    {
        this(independentVariable,alpha,HUNTER);
    }
    

    public SimpleExponentialSmoothingModel( double alpha,
                                            int approach )
    {
        if ( alpha < 0.0 || alpha > 1.0 )
            throw new IllegalArgumentException("SimpleExponentialSmoothingModel: Invalid smoothing constant, " + alpha + " - must be in the range 0.0-1.0.");
        
        this.alpha = alpha;
        this.approach = approach;
    }
    

    public SimpleExponentialSmoothingModel( String independentVariable,
                                            double alpha,
                                            int approach )
    {
        super(independentVariable);
        
        if ( alpha < 0.0 || alpha > 1.0 )
            throw new IllegalArgumentException("SimpleExponentialSmoothingModel: Invalid smoothing constant, " + alpha + " - must be in the range 0.0-1.0.");
        
        this.alpha = alpha;
        this.approach = approach;
    }
    

    protected double forecast( double timeValue )
        throws IllegalArgumentException
    {
        // As a starting point, we set the first forecast value to be the
        //  same as the first observed value
        if ( timeValue-getMinimumTimeValue() < TOLERANCE )
            return getObservedValue( timeValue );

        double previousTime = timeValue - getTimeInterval();
        
        double forecast;
        try
            {
                if ( approach == ROBERTS )
                    forecast
                        = alpha*getObservedValue(timeValue)
                        + (1.0-alpha)*getForecastValue(previousTime);
                else // Default - Hunter's formula
                    forecast
                        = alpha*getObservedValue(previousTime)
                        + (1.0-alpha)*getForecastValue(previousTime);
            }
        catch ( IllegalArgumentException iaex )
            {
                // For "future" forecasts, all we can do is use the forecast
                //  from the last period
                if ( timeValue > getMaximumTimeValue()-TOLERANCE )
                    return getForecastValue( getMaximumTimeValue() );

                throw iaex;
            }
        
        
        return forecast;
    }
    

    protected int getNumberOfPeriods()
    {
        return 1;
    }
    
    /**
     * Returns the number of predictors used by the underlying model.
     * @return the number of predictors used by the underlying model.
     * @since 0.5
     */
    public int getNumberOfPredictors()
    {
        return 1;
    }
    
    /**
     * Returns the value of the smoothing constant, alpha, used in this model.
     * @return the value of the smoothing constant, alpha.
     */
    public double getAlpha()
    {
        return alpha;
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
        return "simple exponential smoothing";
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
        return "Simple exponential smoothing model (using "
            + (approach==ROBERTS?"Roberts":"Hunters")
            + " formula), with a smoothing constant of "
            + alpha + " and using an independent variable of "
            + getIndependentVariable();
    }
}
// Local Variables:
// tab-width: 4
// End:
