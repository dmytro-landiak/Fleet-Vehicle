
package com.landim.tests;


import com.landim.openforecast.Forecaster;
import com.landim.openforecast.ForecastingModel;
import com.landim.openforecast.DataPoint;
import com.landim.openforecast.DataSet;
import com.landim.openforecast.Observation;
import com.landim.openforecast.models.MovingAverageModel;


public class MovingAverageTest extends OpenForecastTestCase
{
    public  MovingAverageTest( String name )
    {
        super(name);
    }
    

    public void testConstantMovingAverage()
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
        
        ForecastingModel model = new MovingAverageModel( 3 );
        
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
    

    public void testMovingAverage()
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
        
        ForecastingModel model = new MovingAverageModel( 4 );
        
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
    

    public void _testForecaster() // Disabled until we get a good data set
    {
        DataSet observedData = new DataSet();
        DataPoint dp;
        // TODO: come up with a good data set for moving average model
        double values[] = { 20,21,20,20,20,20,20,20,20,20,
                            22,20,20,20,20,20,20,20,20,19,
                            20,20,19,20,20,20,20,20,20,21,
                            20,20,20,20,20,20,20,20,20,20,
                            20,20,20,20,20,20,20,20,20,20,
                            20,20,20,20,19,20,20,20,20,20,
                            20,20,20,20,20,20,20,20,20,20 };
        
        for ( int count=0; count<values.length; count++ )
            {
                dp = new Observation( values[count] );
                dp.setIndependentValue( "t", count );
                observedData.add( dp );
            }
        
        // Set time variable and periods per year
        observedData.setTimeVariable( "t" );
        observedData.setPeriodsPerYear( 4 );
        
        ForecastingModel model
            = Forecaster.getBestForecast(observedData);
        
        // Initialize the model
        model.init( observedData );
        
        assertTrue( "Validate that a moving average model was chosen",
                    model
                    .getClass()
                    .getName()
                    .equals("net.sourceforge.openforecast.models.MovingAverageModel") );
    }
}
// Local Variables:
// tab-width: 4
// End:
