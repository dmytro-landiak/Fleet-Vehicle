package com.landim.openforecast.models;


import java.util.Iterator;

import com.landim.openforecast.DataPoint;
import com.landim.openforecast.DataSet;
import com.landim.openforecast.Observation;


public class TripleExponentialSmoothingModel extends AbstractTimeBasedModel
{
    /**
     * The default value of the tolerance permitted in the estimates of the
     * smoothing constants in the {@link #getBestFitModel} methods.
     */
    private static double DEFAULT_SMOOTHING_CONSTANT_TOLERANCE = 0.001;

    /**
     * Minimum number of years of data required.
     */
    private static int NUMBER_OF_YEARS = 2;

    /**
     * The overall smoothing constant, alpha, used in this exponential
     * smoothing model.
     */
    private double alpha;
    
    /**
     * The second smoothing constant, beta, used in this exponential
     * smoothing model for trend smoothing.
     */
    private double beta;
    private double gamma;
    private int periodsPerYear = 0;
    private double maxObservedTime;
    private DataSet baseValues;
    private DataSet trendValues;
    private DataSet seasonalIndex;


    public static TripleExponentialSmoothingModel
        getBestFitModel( DataSet dataSet )
    {
        return getBestFitModel( dataSet,
                                DEFAULT_SMOOTHING_CONSTANT_TOLERANCE,
                                DEFAULT_SMOOTHING_CONSTANT_TOLERANCE );
    }

    public static TripleExponentialSmoothingModel
        getBestFitModel( DataSet dataSet,
                         double alphaTolerance, double betaTolerance )
    {
        // Check we have the minimum amount of data points
        if ( dataSet.size() < NUMBER_OF_YEARS*dataSet.getPeriodsPerYear() )
            throw new IllegalArgumentException("TripleExponentialSmoothing models require a minimum of a full two years of data in the data set.");

        // Check alphaTolerance is in the expected range
        if ( alphaTolerance < 0.0  || alphaTolerance > 0.5 )
            throw new IllegalArgumentException("The value of alphaTolerance must be significantly less than 1.0, and no less than 0.0. Suggested value: "+DEFAULT_SMOOTHING_CONSTANT_TOLERANCE);

        // Check betaTolerance is in the expected range
        if ( betaTolerance < 0.0  || betaTolerance > 0.5 )
            throw new IllegalArgumentException("The value of betaTolerance must be significantly less than 1.0, and no less than 0.0. Suggested value: "+DEFAULT_SMOOTHING_CONSTANT_TOLERANCE);

        TripleExponentialSmoothingModel model1
            = findBestBeta( dataSet, 0.0, 0.0, 1.0, betaTolerance );
        TripleExponentialSmoothingModel model2
            = findBestBeta( dataSet, 0.5, 0.0, 1.0, betaTolerance );
        TripleExponentialSmoothingModel model3
            = findBestBeta( dataSet, 1.0, 0.0, 1.0, betaTolerance );

        // First rough estimate of alpha and beta to the nearest 0.1
        TripleExponentialSmoothingModel bestModel
            = findBest( dataSet, model1, model2, model3,
                        alphaTolerance, betaTolerance );

        return bestModel;
    }


    private static TripleExponentialSmoothingModel findBest(
                        DataSet dataSet,
                        TripleExponentialSmoothingModel modelMin,
                        TripleExponentialSmoothingModel modelMid,
                        TripleExponentialSmoothingModel modelMax,
                        double alphaTolerance,
                        double betaTolerance)
    {
        double alphaMin = modelMin.getAlpha();
        double alphaMid = modelMid.getAlpha();
        double alphaMax = modelMax.getAlpha();

        // If we're not making much ground, then we're done
        if (Math.abs(alphaMid-alphaMin)<alphaTolerance
            && Math.abs(alphaMax-alphaMid)<alphaTolerance )
            return modelMid;

        TripleExponentialSmoothingModel model[]
            = new TripleExponentialSmoothingModel[5];
        model[0] = modelMin;
        model[1] = findBestBeta( dataSet, (alphaMin+alphaMid)/2.0,
                                  0.0, 1.0, betaTolerance );
        model[2] = modelMid;
        model[3] = findBestBeta( dataSet, (alphaMid+alphaMax)/2.0,
                                  0.0, 1.0, betaTolerance );
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
                                 alphaTolerance, betaTolerance );

            case 2:
                // Can discard models 0 and 4
                model[0] = null;
                model[4] = null;
                return findBest( dataSet, model[1], model[2], model[3],
                                 alphaTolerance, betaTolerance );
                
            case 3:
                // Reduce minimums
                // Can discard models 0 and 1
                model[0] = null;
                model[1] = null;
                return findBest( dataSet, model[2], model[3], model[4],
                                 alphaTolerance, betaTolerance );

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


    private static TripleExponentialSmoothingModel findBestBeta(
                        DataSet dataSet, double alpha,
                        double betaMin, double betaMax,
                        double betaTolerance )
    {
        int stepsPerIteration = 10;

        if ( betaMin < 0.0 )
            betaMin = 0.0;
        if ( betaMax > 1.0 )
            betaMax = 1.0;

        TripleExponentialSmoothingModel bestModel
            = new TripleExponentialSmoothingModel( alpha, betaMin, 0.0 );
        bestModel.init(dataSet);

        double initialMSE = bestModel.getMSE();

        boolean betaImproving = true;
        double betaStep = (betaMax-betaMin)/stepsPerIteration;
        double beta = betaMin + betaStep;
        for ( ; beta<=betaMax || betaImproving; )
            {
                TripleExponentialSmoothingModel model
                    = new TripleExponentialSmoothingModel( alpha, beta, 0.0 );
                model.init( dataSet );
                
                if ( model.getMSE() < bestModel.getMSE() )
                    bestModel = model;
                else
                    betaImproving = false;
                
                beta += betaStep;
                if ( beta > 1.0 )
                    betaImproving = false;
            }

        // If we're making progress, then try to refine the beta estimate
        if ( bestModel.getMSE() < initialMSE
             && betaStep > betaTolerance )
            {
                // Can this be further refined - improving efficiency - by
                //  only searching in the range beta-betaStep/2 to
                //  beta+betaStep/2 ?
                return findBestBeta( dataSet, bestModel.getAlpha(),
                                      bestModel.getBeta()-betaStep,
                                      bestModel.getBeta()+betaStep,
                                      betaTolerance );
            }
        
        return bestModel;
    }


    public TripleExponentialSmoothingModel( double alpha,
                                            double beta,
                                            double gamma )
    {
        if ( alpha < 0.0  ||  alpha > 1.0 )
            throw new IllegalArgumentException("TripleExponentialSmoothingModel: Invalid smoothing constant, " + alpha + " - must be in the range 0.0-1.0.");
        
        if ( beta < 0.0  ||  beta > 1.0 )
            throw new IllegalArgumentException("TripleExponentialSmoothingModel: Invalid smoothing constant, beta=" + beta + " - must be in the range 0.0-1.0.");
        
        if ( gamma < 0.0  ||  gamma > 1.0 )
            throw new IllegalArgumentException("TripleExponentialSmoothingModel: Invalid smoothing constant, gamma=" + gamma + " - must be in the range 0.0-1.0.");
        
        baseValues = new DataSet();
        trendValues = new DataSet();
        seasonalIndex = new DataSet();
        
        this.alpha = alpha;
        this.beta = beta;
        this.gamma = gamma;
    }
    

    public void init( DataSet dataSet )
    {
        initTimeVariable( dataSet );
        String timeVariable = getTimeVariable();

        if ( dataSet.getPeriodsPerYear() <= 1 )
            throw new IllegalArgumentException("Data set passed to init in the triple exponential smoothing model does not contain seasonal data. Don't forget to call setPeriodsPerYear on the data set to set this.");

        periodsPerYear = dataSet.getPeriodsPerYear();

        // Check we have the minimum amount of data points
        if ( dataSet.size() < NUMBER_OF_YEARS*periodsPerYear )
            throw new IllegalArgumentException("TripleExponentialSmoothing models require a minimum of a full two years of data to initialize the model.");

        // Calculate initial values for base and trend
        initBaseAndTrendValues( dataSet );

        // Initialize seasonal indices using data for all complete years
        initSeasonalIndices( dataSet );

        Iterator<DataPoint> it = dataSet.iterator();
        maxObservedTime = Double.NEGATIVE_INFINITY;
        while ( it.hasNext() )
            {
                DataPoint dp = it.next();
                if ( dp.getIndependentValue(timeVariable) > maxObservedTime )
                    maxObservedTime = dp.getIndependentValue(timeVariable);
            }

        super.init( dataSet );
    }


    private void initBaseAndTrendValues( DataSet dataSet )
    {
        String timeVariable = getTimeVariable();
        double trend = 0.0;
        Iterator<DataPoint> it = dataSet.iterator();
        for ( int p=0; p<periodsPerYear; p++ )
            {
                DataPoint dp = it.next();
                trend -= dp.getDependentValue();
            }

        double year2Average = 0.0;
        for ( int p=0; p<periodsPerYear; p++ )
            {
                DataPoint dp = it.next();
                trend += dp.getDependentValue();

                year2Average += dp.getDependentValue();
            }
        trend /= periodsPerYear;
        trend /= periodsPerYear;
        year2Average /= periodsPerYear;

        it = dataSet.iterator();
        for ( int p=0; p<periodsPerYear*NUMBER_OF_YEARS; p++ )
            {
                DataPoint obs = it.next();
                double time = obs.getIndependentValue( timeVariable );

                DataPoint dp = new Observation( trend );
                dp.setIndependentValue( timeVariable, time );
                trendValues.add( dp );

                // Initialize base values for second year only
                if ( p >= periodsPerYear )
                    {
                        // This formula gets a little convoluted partly due to
                        // the fact that p is zero-based, and partly because
                        // of the generalized nature of the formula
                        dp.setDependentValue(year2Average
                                             + (p+1-periodsPerYear
                                                -(periodsPerYear+1)/2.0)*trend);
                        //dp.setIndependentValue( timeVariable, time );
                        baseValues.add( dp );
                    }
            }
    }


    private void initSeasonalIndices( DataSet dataSet )
    {
        String timeVariable = getTimeVariable();

        double yearlyAverage[] = new double[NUMBER_OF_YEARS];
        Iterator<DataPoint> it = dataSet.iterator();
        for ( int year=0; year<NUMBER_OF_YEARS; year++ )
            {
                double sum = 0.0;
                for ( int p=0; p<periodsPerYear; p++ )
                    {
                        DataPoint dp = it.next();
                        sum += dp.getDependentValue();
                    }

                yearlyAverage[year] = sum / (double)periodsPerYear;
            }

        it = dataSet.iterator();
        double index[] = new double[periodsPerYear];
        for ( int year=0; year<NUMBER_OF_YEARS; year++ )
            for ( int p=0; p<periodsPerYear; p++ )
                {
                    DataPoint dp = it.next();
                    index[p]
                        += (dp.getDependentValue()/yearlyAverage[year])
                        / NUMBER_OF_YEARS;
                }

        it = dataSet.iterator();

        // Skip over first n-1 years
        for ( int year=0; year<NUMBER_OF_YEARS-1; year++ )
            for ( int p=0; p<periodsPerYear; p++ )
                it.next();

        for ( int p=0; p<periodsPerYear; p++ )
            {
                DataPoint dp = (DataPoint)it.next();
                double time = dp.getIndependentValue( timeVariable );

                Observation obs = new Observation( index[p] );
                obs.setIndependentValue( timeVariable, time );

                seasonalIndex.add( obs );
            }
    }
    

    protected double forecast( double time )
        throws IllegalArgumentException
    {
        double previousTime = time - getTimeInterval();
        double previousYear = time - getTimeInterval()*periodsPerYear;
        
        
        // As a starting point, we set the first forecast value to be
        //  the same as the observed value
        if ( previousTime < getMinimumTimeValue()-TOLERANCE )
            return getObservedValue( time );

        try
            {
                double base = getBase( previousTime );
                double trend = getTrend( previousTime );
                double si = getSeasonalIndex( previousYear );

                double forecast = (base+trend)*si;
                return forecast;
            }
        catch ( IllegalArgumentException idex )
            {
                double base = getBase( maxObservedTime );
                double trend = getTrend( maxObservedTime-getTimeInterval() );
                double si = getSeasonalIndex( previousYear );
                
                double forecast = (base+(time-maxObservedTime)*trend) * si;
                return forecast;
            }
    }
    

    private double getBase( double time )
        throws IllegalArgumentException
    {
        // TODO: Optimize this search by having data set sorted by time
        
        // Search for previously calculated - and saved - base value
        String timeVariable = getTimeVariable();
        Iterator<DataPoint> it = baseValues.iterator();
        while ( it.hasNext() )
            {
                DataPoint dp = it.next();
                double dpTimeValue = dp.getIndependentValue( timeVariable );
                if ( Math.abs(time-dpTimeValue) < TOLERANCE )
                    return dp.getDependentValue();
            }
        
        if ( time <
             getMinimumTimeValue()
             +periodsPerYear*getTimeInterval()
             +TOLERANCE )
            throw new IllegalArgumentException(
                         "Attempt to forecast for an invalid time "
                         +time
                         +" - before sufficient observations were made ("
                         +getMinimumTimeValue()
                         +periodsPerYear*getTimeInterval()+").");

        // Saved base value not found, so calculate it
        //  (and save it for future reference)
        double previousTime = time - getTimeInterval();
        double previousYear = time - periodsPerYear*getTimeInterval();

        double base
            = alpha*(getObservedValue(time)/getSeasonalIndex(previousYear))
            + (1-alpha)*(getBase(previousTime)+getTrend(previousTime));

        DataPoint dp = new Observation( base );
        dp.setIndependentValue( timeVariable, time );
        baseValues.add( dp );
        
        return base;
    }


    private double getTrend( double time )
        throws IllegalArgumentException
    {
        // TODO: Optimize this search by having data set sorted by time

        // Search for previously calculated - and saved - trend value
        String timeVariable = getTimeVariable();
        Iterator<DataPoint> it = trendValues.iterator();
        while ( it.hasNext() )
            {
                DataPoint dp = it.next();
                double dpTimeValue = dp.getIndependentValue( timeVariable );
                if ( Math.abs(time-dpTimeValue) < TOLERANCE )
                    return dp.getDependentValue();
            }
        
        if ( time < getMinimumTimeValue()+TOLERANCE )
            throw new IllegalArgumentException("Attempt to forecast for an invalid time - before the observations began ("+getMinimumTimeValue()+").");

        // Saved trend not found, so calculate it
        //  (and save it for future reference)
        double previousTime = time - getTimeInterval();
        double trend
            = beta*(getBase(time)-getBase(previousTime))
            + (1-beta)*getTrend(previousTime);
        
        DataPoint dp = new Observation( trend );
        dp.setIndependentValue( timeVariable, time );
        trendValues.add( dp );
        
        return trend;
    }

    /**
     * Returns the seasonal index for the given time period.
     */    
    private double getSeasonalIndex( double time )
        throws IllegalArgumentException
    {
        // TODO: Optimize this search by having data set sorted by time

        // Handle initial conditions for seasonal index
        if ( time
             < getMinimumTimeValue()
             +(NUMBER_OF_YEARS-1)*periodsPerYear-TOLERANCE )
            return getSeasonalIndex( time
                                     + periodsPerYear*getTimeInterval() );
        
        // Search for previously calculated - and saved - seasonal index
        String timeVariable = getTimeVariable();
        Iterator<DataPoint> it = seasonalIndex.iterator();
        while ( it.hasNext() )
            {
                DataPoint dp = it.next();
                double dpTimeValue = dp.getIndependentValue( timeVariable );
                if ( Math.abs(time-dpTimeValue) < TOLERANCE )
                    return dp.getDependentValue();
            }
        
        // Saved seasonal index not found, so calculate it
        //  (and save it for future reference)
        double previousYear = time - getTimeInterval()*periodsPerYear;
        double index = gamma*(getObservedValue(time)/getForecastValue(time))
                       + (1-gamma)*getSeasonalIndex(previousYear);
        
        DataPoint dp = new Observation( index );
        dp.setIndependentValue( timeVariable, time );
        seasonalIndex.add( dp );
        
        return index;
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
    

    protected int getNumberOfPeriods()
    {
        return 2*periodsPerYear;
    }

    /**
     * Since this version of triple exponential smoothing uses the current
     * observation to calculate a smoothed value, we must override the
     * calculation of the accuracy indicators.
     * @param dataSet the DataSet to use to evaluate this model, and to
     *        calculate the accuracy indicators against.
     */
    protected void calculateAccuracyIndicators( DataSet dataSet )
    {
        // WARNING: THIS STILL NEEDS TO BE VALIDATED

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
                    + getTrend( previousTime );

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
     * @see #getBeta
     * @see #getGamma
     */
    public double getAlpha()
    {
        return alpha;
    }

    /**
     * Returns the value of the trend smoothing constant, beta, used in this
     * model.
     * @return the value of the trend smoothing constant, beta.
     * @see #getAlpha
     * @see #getGamma
     */
    public double getBeta()
    {
        return beta;
    }

    /**
     * Returns the value of the seasonal smoothing constant, gamma, used in
     * this model.
     * @return the value of the seasonal smoothing constant, gamma.
     * @see #getAlpha
     * @see #getBeta
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
        return "triple exponential smoothing";
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
        return "Triple exponential smoothing model, with smoothing constants of alpha="
            + alpha + ", beta="
            + beta  + ", gamma="
            + gamma + ", and using an independent variable of "
            + getIndependentVariable();
    }
}
// Local Variables:
// tab-width: 4
// End:
