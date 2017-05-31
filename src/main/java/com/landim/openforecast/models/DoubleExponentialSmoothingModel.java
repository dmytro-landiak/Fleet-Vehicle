package com.landim.openforecast.models;


import java.util.Iterator;
import com.landim.openforecast.DataPoint;
import com.landim.openforecast.DataSet;
import com.landim.openforecast.Observation;


public class DoubleExponentialSmoothingModel extends AbstractTimeBasedModel
{
    /**
     * The default value of the tolerance permitted in the estimates of the
     * smoothing constants in the {@link #getBestFitModel} methods.
     */
    private static double DEFAULT_SMOOTHING_CONSTANT_TOLERANCE = 0.001;
    
    /**
     * The smoothing constant used in this exponential smoothing model.
     */
    private double alpha;
    
    /**
     * The second smoothing constant (gamma) used in this exponential
     * smoothing model. This is used to smooth the trend.
     */
    private double gamma;
    
    /**
     * Provides a cache of calculated slopeValues. Since these values are
     * used very frequently when calculating forecast values, it is more
     * efficient to cache the previously calculated slope values for future
     * use.
     */
    private DataSet slopeValues;

    public static DoubleExponentialSmoothingModel
        getBestFitModel( DataSet dataSet )
    {
        return getBestFitModel( dataSet,
                                DEFAULT_SMOOTHING_CONSTANT_TOLERANCE,
                                DEFAULT_SMOOTHING_CONSTANT_TOLERANCE );
    }
    

    public static DoubleExponentialSmoothingModel
        getBestFitModel( DataSet dataSet,
                         double alphaTolerance, double gammaTolerance )
    {
        DoubleExponentialSmoothingModel model1
            = findBestGamma( dataSet, 0.0, 0.0, 1.0, gammaTolerance );
        DoubleExponentialSmoothingModel model2
            = findBestGamma( dataSet, 0.5, 0.0, 1.0, gammaTolerance );
        DoubleExponentialSmoothingModel model3
            = findBestGamma( dataSet, 1.0, 0.0, 1.0, gammaTolerance );
        
        // First rough estimate of alpha and gamma to the nearest 0.1
        DoubleExponentialSmoothingModel bestModel
            = findBest( dataSet, model1, model2, model3,
                        alphaTolerance, gammaTolerance );
        
        return bestModel;
    }

    private static DoubleExponentialSmoothingModel findBest(
        DataSet dataSet,
        DoubleExponentialSmoothingModel modelMin,
        DoubleExponentialSmoothingModel modelMid,
        DoubleExponentialSmoothingModel modelMax,
        double alphaTolerance,
        double gammaTolerance )
    {
        double alphaMin = modelMin.getAlpha();
        double alphaMid = modelMid.getAlpha();
        double alphaMax = modelMax.getAlpha();
        
        // If we're not making much ground, then we're done
        if (Math.abs(alphaMid-alphaMin)<alphaTolerance
            && Math.abs(alphaMax-alphaMid)<alphaTolerance )
            return modelMid;
        
        DoubleExponentialSmoothingModel model[]
            = new DoubleExponentialSmoothingModel[5];
        model[0] = modelMin;
        model[1] = findBestGamma( dataSet, (alphaMin+alphaMid)/2.0,
                                  0.0, 1.0, gammaTolerance );
        model[2] = modelMid;
        model[3] = findBestGamma( dataSet, (alphaMid+alphaMax)/2.0,
                                  0.0, 1.0, gammaTolerance );
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
                return findBest( dataSet, model[0], model[1], model[2],
                                 alphaTolerance, gammaTolerance );
                
            case 2:
                // Can discard models 0 and 4
                model[0] = null;
                model[4] = null;
                return findBest( dataSet, model[1], model[2], model[3],
                                 alphaTolerance, gammaTolerance );
                
            case 3:
                // Reduce minimums
                // Can discard models 0 and 1
                model[0] = null;
                model[1] = null;
                return findBest( dataSet, model[2], model[3], model[4],
                                 alphaTolerance, gammaTolerance );
                
            case 0:
            case 4:
                // We're done???
                break;
            }
        
        // Release all but the best model constructed so far
        for ( int m=0; m<5; m++ )
            if ( m != bestModelIndex )
                model[m] = null;
        
        return model[bestModelIndex];
    }
    

    private static DoubleExponentialSmoothingModel findBestGamma(
                                                                 DataSet dataSet, double alpha,
                                                                 double gammaMin, double gammaMax,
                                                                 double gammaTolerance )
    {
        int stepsPerIteration = 10;
        
        if ( gammaMin < 0.0 )
            gammaMin = 0.0;
        if ( gammaMax > 1.0 )
            gammaMax = 1.0;
        
        DoubleExponentialSmoothingModel bestModel
            = new DoubleExponentialSmoothingModel( alpha, gammaMin );
        bestModel.init(dataSet);
        
        double initialMSE = bestModel.getMSE();
        
        boolean gammaImproving = true;
        double gammaStep = (gammaMax-gammaMin)/stepsPerIteration;
        double gamma = gammaMin + gammaStep;
        for ( ; gamma<=gammaMax || gammaImproving; )
            {
                DoubleExponentialSmoothingModel model
                    = new DoubleExponentialSmoothingModel( alpha, gamma );
                model.init( dataSet );
                
                if ( model.getMSE() < bestModel.getMSE() )
                    bestModel = model;
                else
                    gammaImproving = false;
                
                gamma += gammaStep;
                if ( gamma > 1.0 )
                    gammaImproving = false;
            }
        
        // If we're making progress, then try to refine the gamma estimate
        if ( bestModel.getMSE() < initialMSE
             && gammaStep > gammaTolerance )
            {
                // Can this be further refined - improving efficiency - by
                //  only searching in the range gamma-gammaStep/2 to
                //  gamma+gammaStep/2 ?
                return findBestGamma( dataSet, bestModel.getAlpha(),
                                      bestModel.getGamma()-gammaStep,
                                      bestModel.getGamma()+gammaStep,
                                      gammaTolerance );
            }
        
        return bestModel;
    }

    public DoubleExponentialSmoothingModel( double alpha,
                                            double gamma )
    {
        if ( alpha < 0.0  ||  alpha > 1.0 )
            throw new IllegalArgumentException("DoubleExponentialSmoothingModel: Invalid smoothing constant, " + alpha + " - must be in the range 0.0-1.0.");
        
        if ( gamma < 0.0  ||  gamma > 1.0 )
            throw new IllegalArgumentException("DoubleExponentialSmoothingModel: Invalid smoothing constant, gamma=" + gamma + " - must be in the range 0.0-1.0.");
        
        slopeValues = new DataSet();
        
        this.alpha = alpha;
        this.gamma = gamma;
    }
    

    protected double forecast( double t )
        throws IllegalArgumentException
    {
        double previousTime = t - getTimeInterval();
        
        // As a starting point, we set the first forecast value to be
        //  the same as the observed value
        if ( previousTime < getMinimumTimeValue()+TOLERANCE )
            return getObservedValue( t );
        
        try
            {
                double b = getSlope( previousTime );
                
                double forecast
                    = alpha*getObservedValue(t)
                    + (1.0-alpha)*(getForecastValue(previousTime)+b);
                
                return forecast;
            }
        catch ( IllegalArgumentException iaex )
            {
                double maxTimeValue = getMaximumTimeValue();
                
                double b = getSlope( maxTimeValue-getTimeInterval() );
                double forecast
                    = getForecastValue(maxTimeValue)
                    + (t-maxTimeValue)*b;
                
                return forecast;
            }
    }
    

    private double getSlope( double time )
        throws IllegalArgumentException
    {
        // TODO: Optimize this search by having data set sorted by time
        
        // Search for previously calculated - and saved - slope value
        String timeVariable = getTimeVariable();
        Iterator<DataPoint> it = slopeValues.iterator();
        while ( it.hasNext() )
            {
                DataPoint dp = it.next();
                double dpTimeValue = dp.getIndependentValue( timeVariable );
                if ( Math.abs(time-dpTimeValue) < TOLERANCE )
                    return dp.getDependentValue();
            }
        
        // Saved slope not found, so calculate it
        //  (and save it for future reference)
        double previousTime = time - getTimeInterval();
        double slope = 0.0;
        
        // Initial condition for first periods
        if ( previousTime < getMinimumTimeValue()+TOLERANCE )
            slope = getObservedValue(time)-getObservedValue(previousTime);
        else
            slope
                = gamma*(forecast(time)-forecast(previousTime))
                + (1-gamma)*getSlope(previousTime);
        
        DataPoint dp = new Observation( slope );
        dp.setIndependentValue( timeVariable, time );
        slopeValues.add( dp );
        
        return slope;
    }
    

    protected int getNumberOfPeriods()
    {
        return 2;
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
    

    protected void calculateAccuracyIndicators( DataSet dataSet )
    {
        // Note that the model has been initialized
        initialized = true;
        
        // Reset various helper summations
        double sumErr = 0.0;
        double sumAbsErr = 0.0;
        double sumAbsPercentErr = 0.0;
        double sumErrSquared = 0.0;
        
        String timeVariable = getTimeVariable();
        double timeDiff = getTimeInterval();
        
        // Calculate the Sum of the Absolute Errors
        Iterator<DataPoint> it = dataSet.iterator();
        while ( it.hasNext() )
            {
                // Get next data point
                DataPoint dp = it.next();
                double x = dp.getDependentValue();
                double time = dp.getIndependentValue( timeVariable );
                double previousTime = time - timeDiff;
                
                // Get next forecast value, using one-period-ahead forecast
                double forecastValue
                    = getForecastValue( previousTime )
                    + getSlope( previousTime );
                
                // Calculate error in forecast, and update sums appropriately
                double error = forecastValue - x;
                sumErr += error;
                sumAbsErr += Math.abs( error );
                sumAbsPercentErr += Math.abs( error / x );
                sumErrSquared += error*error;
            }
        
        // Initialize the accuracy indicators
        int n = dataSet.size();
        
        accuracyIndicators.setBias( sumErr / n );
        accuracyIndicators.setMAD( sumAbsErr / n );
        accuracyIndicators.setMAPE( sumAbsPercentErr / n );
        accuracyIndicators.setMSE( sumErrSquared / n );
        accuracyIndicators.setSAE( sumAbsErr );
    }
    
    /**
     * Returns the value of the smoothing constant, alpha, used in this model.
     * @return the value of the smoothing constant, alpha.
     * @see #getGamma
     */
    public double getAlpha()
    {
        return alpha;
    }
    
    /**
     * Returns the value of the trend smoothing constant, gamma, used in this
     * model.
     * @return the value of the trend smoothing constant, gamma.
     * @see #getAlpha
     */
    public double getGamma()
    {
        return gamma;
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
        return "double exponential smoothing";
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
        return "Double exponential smoothing model, with smoothing constants of alpha="
            + alpha + ", gamma="
            + gamma + ", and using an independent variable of "
            + getIndependentVariable();
    }
}
// Local Variables:
// tab-width: 4
// End:
