package com.landim.tests;

import com.landim.openforecast.ForecastingModel;
import com.landim.openforecast.DataPoint;
import com.landim.openforecast.DataSet;
import com.landim.openforecast.Observation;
import com.landim.openforecast.models.WeightedMovingAverageModel;

public class WeightedMovingAverageTest extends OpenForecastTestCase
{
    public WeightedMovingAverageTest( String name )
    {
        super(name);
    }
    

    public void testConstantWeightedMovingAverage()
    {
        DataSet observedData = new DataSet();
        DataPoint dp;
        
        dp = new Observation( 5.0 );
        dp.setIndependentValue( "t", 1.0 );
        observedData.add( dp );
        
        dp = new Observation( 5.0 );
        dp.setIndependentValue( "t", 2.0 );
        observedData.add( dp );
        
        dp = new Observation( 5.0 );
        dp.setIndependentValue( "t", 3.0 );
        observedData.add( dp );
        
        dp = new Observation( 5.0 );
        dp.setIndependentValue( "t", 4.0 );
        observedData.add( dp );
        
        dp = new Observation( 5.0 );
        dp.setIndependentValue( "t", 5.0 );
        observedData.add( dp );
        
        dp = new Observation( 5.0 );
        dp.setIndependentValue( "t", 6.0 );
        observedData.add( dp );
        
        double weights[] = { 0.4, 0.3, 0.2, 0.1 };
        ForecastingModel model = new WeightedMovingAverageModel( weights );
        
        // Initialize the model
        model.init( observedData );
        
        // Create a data set for forecasting
        DataSet fcValues = new DataSet();
        
        dp = new Observation( 0.0 );
        dp.setIndependentValue( "t", 7.0 );
        fcValues.add( dp );
        
        dp = new Observation( 0.0 );
        dp.setIndependentValue( "t", 8.0 );
        fcValues.add( dp );
        
        dp = new Observation( 0.0 );
        dp.setIndependentValue( "t", 9.0 );
        fcValues.add( dp );
        
        // Get forecast values
        DataSet results = model.forecast( fcValues );
        
        // These are the expected results
        double expectedResult[] = { 5.0, 5.0, 5.0 };
        
        checkResults( results, expectedResult );
    }
    

    public void testWeightedMovingAverage()
    {
        DataSet observedData = new DataSet();
        DataPoint dp;
        
        dp = new Observation( 4.0 );
        dp.setIndependentValue( "t", 101.0 );
        observedData.add( dp );
        
        dp = new Observation( 5.0 );
        dp.setIndependentValue( "t", 102.0 );
        observedData.add( dp );
        
        dp = new Observation( 6.0 );
        dp.setIndependentValue( "t", 103.0 );
        observedData.add( dp );
        
        dp = new Observation( 9.0 );
        dp.setIndependentValue( "t", 104.0 );
        observedData.add( dp );
        
        dp = new Observation( 3.0 );
        dp.setIndependentValue( "t", 105.0 );
        observedData.add( dp );
        
        dp = new Observation( 7.0 );
        dp.setIndependentValue( "t", 106.0 );
        observedData.add( dp );
        
        dp = new Observation( 5.0 );
        dp.setIndependentValue( "t", 107.0 );
        observedData.add( dp );
        
        dp = new Observation( 6.0 );
        dp.setIndependentValue( "t", 108.0 );
        observedData.add( dp );
        
        dp = new Observation( 7.0 );
        dp.setIndependentValue( "t", 109.0 );
        observedData.add( dp );
        
        dp = new Observation( 9.0 );
        dp.setIndependentValue( "t", 110.0 );
        observedData.add( dp );
        
        dp = new Observation( 3.0 );
        dp.setIndependentValue( "t", 111.0 );
        observedData.add( dp );
        
        dp = new Observation( 4.0 );
        dp.setIndependentValue( "t", 112.0 );
        observedData.add( dp );
        
        dp = new Observation( 6.0 );
        dp.setIndependentValue( "t", 113.0 );
        observedData.add( dp );
        
        dp = new Observation( 7.0 );
        dp.setIndependentValue( "t", 114.0 );
        observedData.add( dp );
        
        dp = new Observation( 7.0 );
        dp.setIndependentValue( "t", 115.0 );
        observedData.add( dp );
        
        dp = new Observation( 4.0 );
        dp.setIndependentValue( "t", 116.0 );
        observedData.add( dp );
        
        double weights[] = { 0.25, 0.25, 0.25, 0.25 };
        
        ForecastingModel model = new WeightedMovingAverageModel( weights );
        
        // Initialize the model
        model.init( observedData );
        
        // Create a data set for forecasting
        DataSet fcValues = new DataSet();
        
        for ( int t=105; t<=120; t ++ )
            {
                dp = new Observation( 0.0 );
                dp.setIndependentValue( "t", t );
                fcValues.add( dp );
            }
        
        // Get forecast values
        DataSet results = model.forecast( fcValues );
        
        // These are the expected results
        double expectedResult[] = { 6.00, 5.75, 6.25, 6.00, 5.25,
                                    6.25, 6.75, 6.25, 5.75, 5.50,
                                    5.00, 6.00, 6.00, 6.00, 5.75,
                                    5.4375 };
        
        checkResults( results, expectedResult );
    }
    
    // TODO: Implement a testForecaster (need suitable data set)
}
// Local Variables:
// tab-width: 4
// End:
