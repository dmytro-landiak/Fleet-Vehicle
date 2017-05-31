package com.landim.openforecast.models;


import java.util.Iterator;

import com.landim.openforecast.DataPoint;
import com.landim.openforecast.DataSet;
import com.landim.openforecast.Observation;


public abstract class AbstractTimeBasedModel extends AbstractForecastingModel
{
    /**
     * The name of the independent (time) variable used in this model.
     */
    private String timeVariable = null;
    
    /**
     * Initialized to the time difference (in whatever units time is reported
     * in) between two consecutive data points. You could also think of this
     * as the "delta time" between data points.
     */
    private double timeDiff = 0.0;
    
    /**
     * Stores the minimum number of prior periods of data required to produce a
     * forecast. Since this varies depending on the details of the model, any
     * subclass must call setMinimumNumberOfPeriods - usually from the
     * constructor - before init is invoked to provide the correct information.
     */
    private int minPeriods = 0;
    
    /**
     * The observed values are stored for future reference. In this model,
     * unlike most others, we store all observed values. This is because these
     * models don't derive any formula from the data, so the values may be
     * needed later in order to derive future forecasts.
     */
    private DataSet observedValues;
    
    /**
     * The forecast values are stored to save recalculation. In this model,
     * unlike most others, we store all forecast values. This is because these
     * models don't derive any formula from the data.
     */
    private DataSet forecastValues;

    private double minTimeValue;
    

    private double maxTimeValue;
    
    /**
     * Constructs a new time based forecasting model. For a valid model to be
     * constructed, you should call init and pass in a data set containing a
     * series of data points. The data set should also have the time variable
     * initialized to the independent time variable name.
     */
    public AbstractTimeBasedModel()
    {
    }

    public AbstractTimeBasedModel( String timeVariable )
    {
        this.timeVariable = timeVariable;
    }
    
    /**
     * Returns the current number of periods used in this model. This is also
     * the minimum number of periods required in order to produce a valid
     * forecast. Since this varies depending on the details of the model, any
     * subclass must override this to provide the correct information.
     * @return the minimum number of periods used in this model.
     */
    protected abstract int getNumberOfPeriods();

    public void init( DataSet dataSet )
    {
        initTimeVariable( dataSet );
        
        if ( dataSet == null  || dataSet.size() == 0 )
            throw new IllegalArgumentException("Data set cannot be empty in call to init.");
        
        int minPeriods = getNumberOfPeriods();
        
        if ( dataSet.size() < minPeriods )
            throw new IllegalArgumentException("Data set too small. Need "
                                               +minPeriods
                                               +" data points, but only "
                                               +dataSet.size()
                                               +" passed to init.");
        
        observedValues = new DataSet( dataSet );
        observedValues.sort( timeVariable );
        
        // Check that intervals between data points are consistent
        //  i.e. check for complete data set
        Iterator<DataPoint> it = observedValues.iterator();
        
        DataPoint dp = it.next();  // first data point
        double lastValue = dp.getIndependentValue(timeVariable);
        
        dp = it.next();  // second data point
        double currentValue = dp.getIndependentValue(timeVariable);
        
        // Create data set in which to save new forecast values
        forecastValues = new DataSet();
        
        // Determine "standard"/expected time difference between observations
        timeDiff = currentValue - lastValue;
        
        // Min. time value is first observation time
        minTimeValue = lastValue;
        
        while ( it.hasNext() )
            {
                lastValue = currentValue;
                
                // Get next data point
                dp = it.next();
                currentValue = dp.getIndependentValue(timeVariable);
                
                double diff = currentValue - lastValue;
                if ( Math.abs(timeDiff - diff) > TOLERANCE )
                    throw new IllegalArgumentException( "Inconsistent intervals found in time series, using variable '"+timeVariable+"'" );
                
                try
                    {
                        initForecastValue( currentValue );
                    }
                catch (IllegalArgumentException ex)
                    {
                        // We can ignore these during initialization
                    }
            }
        
        // Create test data set for determining accuracy indicators
        //  - same as input data set, but without the first n data points
        DataSet testDataSet = new DataSet( observedValues );
        int count = 0;
        while ( count++ < minPeriods )
            testDataSet.remove( (testDataSet.iterator()).next() );
        
        // Calculate accuracy
        calculateAccuracyIndicators( testDataSet );
    }
    

    protected void initTimeVariable( DataSet dataSet )
        throws IllegalArgumentException
    {
        if ( timeVariable == null )
            {
                // Time variable not set, so look at independent variables
                timeVariable = dataSet.getTimeVariable();
                if ( timeVariable == null )
                    {
                        String[] independentVars
                            = dataSet.getIndependentVariables();
                        
                        if ( independentVars.length != 1 )
                            throw new IllegalArgumentException("Unable to determine the independent time variable for the data set passed to init for "+toString()+". Please use DataSet.setTimeVariable before invoking model.init.");
                        
                        timeVariable = independentVars[0];
                    }
            }
    }
    

    private double initForecastValue( double timeValue )
        throws IllegalArgumentException
    {
        // Temporary store for current forecast value
        double forecast = forecast(timeValue);
        
        // Create new forecast data point
        DataPoint dpForecast = new Observation( forecast );
        dpForecast.setIndependentValue( timeVariable, timeValue );
        
        // Add new data point to forecast set
        forecastValues.add( dpForecast );
        
        // Update maximum time value, if necessary
        if ( timeValue > maxTimeValue )
            maxTimeValue = timeValue;
        
        return forecast;
    }
    

    public double forecast( DataPoint dataPoint )
        throws IllegalArgumentException
    {
        if ( !initialized )
            throw new ModelNotInitializedException();
        
        // Get value of independent variable (the time variable)
        double t = dataPoint.getIndependentValue( timeVariable );
        
        return getForecastValue( t );
    }
    

    protected abstract double forecast( double timeValue )
        throws IllegalArgumentException;
    

    protected double getForecastValue( double timeValue )
        throws IllegalArgumentException
    {
        if ( timeValue>=minTimeValue-TOLERANCE
             && timeValue<=maxTimeValue+TOLERANCE )
            {
                // Find required forecast value in set of
                //  pre-computed forecasts
                Iterator<DataPoint> it = forecastValues.iterator();
                while ( it.hasNext() )
                    {
                        DataPoint dp = it.next();
                        double currentTime
                            = dp.getIndependentValue( timeVariable );
                        
                        // If required data point found,
                        //  return pre-computed forecast
                        if ( Math.abs(currentTime-timeValue) < TOLERANCE )
                            return dp.getDependentValue();
                    }
            }
        
        try
            {
                return initForecastValue( timeValue );
            }
        catch ( IllegalArgumentException idex )
            {
                throw new IllegalArgumentException(
                                                   "Time value (" + timeValue
                                                   + ") invalid for Time Based forecasting model. Valid values are in the range "
                                                   + minTimeValue + "-" + maxTimeValue
                                                   + " in increments of " + timeDiff + "." );
            }
    }
    

    protected double getObservedValue( double timeValue )
        throws IllegalArgumentException
    {
        // Find required forecast value in set of
        //  pre-computed forecasts
        Iterator<DataPoint> it = observedValues.iterator();
        while ( it.hasNext() )
            {
                DataPoint dp = it.next();
                double currentTime
                    = dp.getIndependentValue( timeVariable );
                
                // If required data point found,
                //  return pre-computed forecast
                if ( Math.abs(currentTime-timeValue) < TOLERANCE )
                    return dp.getDependentValue();
            }
        
        throw new
            IllegalArgumentException("No observation found for time value, "
                                     +timeVariable+"="+timeValue);
    }
    
    /**
     * Returns the name of the independent variable representing the time
     * value used by this model.
     * @return the name of the independent variable representing the time
     * value.
     */
    public String getTimeVariable()
    {
        return timeVariable;
    }
    
    /**
     * Returns the minimum value of the independent time variable currently
     * forecast by this model.
     * @return the minimum value of the independent time variable.
     */
    public double getMinimumTimeValue()
    {
        return minTimeValue;
    }
    
    /**
     * Returns the maximum value of the independent time variable currently
     * forecast by this model.
     * @return the maximum value of the independent time variable.
     */
    public double getMaximumTimeValue()
    {
        return maxTimeValue;
    }
    
    /**
     * Returns the independent variable - or the time variable - used in this
     * model.
     * @return the independent variable in this model.
     */
    public String getIndependentVariable()
    {
        return timeVariable;
    }
    
    /**
     * Returns the current time interval between observations.
     * @return the current time interval between observations.
     */
    protected double getTimeInterval()
    {
        return timeDiff;
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
        return "Time Based Model";
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
        return "time based model, spanning " + getNumberOfPeriods()
            + " periods and using a time variable of "
            + timeVariable+".";
    }
}
// Local Variables:
// tab-width: 4
// End:
