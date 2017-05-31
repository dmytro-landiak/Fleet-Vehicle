package com.landim.openforecast.models;

import java.util.Iterator;

import com.landim.openforecast.ForecastingModel;
import com.landim.openforecast.DataPoint;
import com.landim.openforecast.DataSet;

public abstract class AbstractForecastingModel implements ForecastingModel
{

    static double TOLERANCE = 0.00000001;

    protected AccuracyIndicators accuracyIndicators = new AccuracyIndicators();
    

    protected boolean initialized = false;

    protected AbstractForecastingModel() {}

    public double getAIC()
    {
        if ( !initialized )
            throw new ModelNotInitializedException();
        
        return accuracyIndicators.getAIC();
    }
    

    public double getBias()
    {
        if ( !initialized )
            throw new ModelNotInitializedException();
        
        return accuracyIndicators.getBias();
    }

    public double getMAD()
    {
        if ( !initialized )
            throw new ModelNotInitializedException();
        
        return accuracyIndicators.getMAD();
    }
    

    public double getMAPE()
    {
        if ( !initialized )
            throw new ModelNotInitializedException();
        
        return accuracyIndicators.getMAPE();
    }
    

    public double getMSE()
    {
        if ( !initialized )
            throw new ModelNotInitializedException();
        
        return accuracyIndicators.getMSE();
    }
    

    public double getSAE()
    {
        if ( !initialized )
            throw new ModelNotInitializedException();
        
        return accuracyIndicators.getSAE();
    }
    

    public DataSet forecast( DataSet dataSet )
    {
        if ( !initialized )
            throw new ModelNotInitializedException();
        
        Iterator<DataPoint> it = dataSet.iterator();
        while ( it.hasNext() )
            {
                DataPoint dp = it.next();
                dp.setDependentValue( forecast(dp) );
            }
        
        return dataSet;        
    }
    
    /**
     * A helper method to calculate the various accuracy indicators when
     * applying the given DataSet to the current forecasting model.
     * @param dataSet the DataSet to use to evaluate this model, and to
     *        calculate the accuracy indicators against.
     */
    protected void calculateAccuracyIndicators( DataSet dataSet )
    {
        // Note that the model has been initialized
        initialized = true;
        
        // Reset various helper summations
        double sumErr = 0.0;
        double sumAbsErr = 0.0;
        double sumAbsPercentErr = 0.0;
        double sumErrSquared = 0.0;
        
        // Obtain the forecast values for this model
        DataSet forecastValues = new DataSet( dataSet );
        forecast( forecastValues );
        
        // Calculate the Sum of the Absolute Errors
        Iterator<DataPoint> it = dataSet.iterator();
        Iterator<DataPoint> itForecast = forecastValues.iterator();
        while ( it.hasNext() )
            {
                // Get next data point
                DataPoint dp = it.next();
                double x = dp.getDependentValue();
                
                // Get next forecast value
                DataPoint dpForecast = itForecast.next();
                double forecastValue = dpForecast.getDependentValue();
                
                // Calculate error in forecast, and update sums appropriately
                double error = forecastValue - x;
                sumErr += error;
                sumAbsErr += Math.abs( error );
                sumAbsPercentErr += Math.abs( error / x );
                sumErrSquared += error*error;
            }
        
        // Initialize the accuracy indicators
        int n = dataSet.size();
        int p = getNumberOfPredictors();

        accuracyIndicators.setAIC( n*Math.log(2*Math.PI)
                   + Math.log(sumErrSquared/n)
                   + 2 * ( p+2 ) );
        accuracyIndicators.setBias( sumErr / n );
        accuracyIndicators.setMAD( sumAbsErr / n );
        accuracyIndicators.setMAPE( sumAbsPercentErr / n );
        accuracyIndicators.setMSE( sumErrSquared / n );
        accuracyIndicators.setSAE( sumAbsErr );
    }
}
// Local Variables:
// tab-width: 4
// End:
