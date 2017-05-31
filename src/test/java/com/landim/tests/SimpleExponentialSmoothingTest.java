
package com.landim.tests;


import com.landim.openforecast.ForecastingModel;
import com.landim.openforecast.DataPoint;
import com.landim.openforecast.DataSet;
import com.landim.openforecast.Observation;
import com.landim.openforecast.models.SimpleExponentialSmoothingModel;



public class SimpleExponentialSmoothingTest extends OpenForecastTestCase
{

    private static double TOLERANCE = 0.005;
    

    private static double MSE_TOLERANCE = 0.1;
    

    public SimpleExponentialSmoothingTest( String name )
    {
        super(name);
    }
    

    public void testConstantSimpleExponentialSmoothing()
    {
        final int NUMBER_OF_OBSERVATIONS = 25;
        
        // Set up some constant observed values
        DataSet observedData = new DataSet();
        DataPoint dp;
        
        for ( int t=0; t<NUMBER_OF_OBSERVATIONS; t++ )
            {
                dp = new Observation( 5.0 );
                dp.setIndependentValue( "t",  t+1 );
                observedData.add( dp );
            }
        
        // These are the expected results
        double expectedResult[] = { 5.0, 5.0 };
        
        // Try the forecasting model with smoothing constants ranging
        //  from 0.1 to 0.9 (in 0.1 increments)
        double smoothingConstant = 0.1;
        for ( ; smoothingConstant<0.95; smoothingConstant+=0.1 )
            {
                ForecastingModel model
                    = new SimpleExponentialSmoothingModel(smoothingConstant);
                
                // Initialize the model
                model.init( observedData );
                
                // Create a data set for forecasting
                DataSet fcValues = new DataSet();
                
                dp = new Observation( 0.0 );
                dp.setIndependentValue( "t", NUMBER_OF_OBSERVATIONS-1.0 );
                fcValues.add( dp );
                
                dp = new Observation( 0.0 );
                dp.setIndependentValue( "t", NUMBER_OF_OBSERVATIONS );
                fcValues.add( dp );
                
                // Get forecast values
                DataSet results = model.forecast( fcValues );
                
                checkResults( results, expectedResult );
            }
    }
    

    public void testSimpleExponentialSmoothing()
    {
        DataSet observedData = new DataSet();
        DataPoint dp;
        
        double[] observations
            = { 71.0, 70.0, 69.0, 68.0, 64.0, 65.0,
                72.0, 78.0, 75.0, 75.0, 75.0, 70.0 };
        
        for ( int t=0; t<observations.length; t++ )
            {
                dp = new Observation( observations[t] );
                dp.setIndependentValue( "t",  t+1 );
                observedData.add( dp );
            }
        
        ForecastingModel model = new SimpleExponentialSmoothingModel( 0.1 );
        
        // Initialize the model
        model.init( observedData );
        
        assertEquals( "Checking the accuracy of the model",
                      18.98349, model.getMSE(), MSE_TOLERANCE );
        
        // Create a data set for forecasting
        DataSet fcValues = new DataSet();
        
        for ( int t=2; t<=12; t ++ )
            {
                dp = new Observation( 0.0 );
                dp.setIndependentValue( "t", t );
                fcValues.add( dp );
            }
        
        // Get forecast values
        DataSet results = model.forecast( fcValues );
        
        // These are the expected results
        double expectedResult[]
            = { 71.00, 70.90, 70.71, 70.44,    /* t =  2, 3, 4, 5 */
                69.80, 69.32, 69.58, 70.43,    /* t =  6, 7, 8, 9 */
                70.88, 71.29, 71.67 };         /* t = 10,11,12    */
        
        checkResults( results, expectedResult, TOLERANCE );
    }
    

    public void testMusicStoreExample()
    {
        DataSet observedData = new DataSet();
        DataPoint dp;
        
        double[] observations
            = { 74.0, 69.0, 80.0, 91.0, 76.0,
                83.0, 79.0, 87.0, 89.0, 92.0 };
        
        for ( int t=0; t<observations.length; t++ )
            {
                dp = new Observation( observations[t] );
                dp.setIndependentValue( "t",  t+1 );
                observedData.add( dp );
            }
        
        ForecastingModel model
            = new SimpleExponentialSmoothingModel( 0.3,
                                                   SimpleExponentialSmoothingModel.ROBERTS );
        
        // Initialize the model
        model.init( observedData );
        
        assertEquals( "Checking the accuracy of the model",
                      29.9132, model.getMSE(), MSE_TOLERANCE );
        
        // Create a data set for forecasting
        DataSet fcValues = new DataSet();
        
        for ( int t=1; t<observations.length; t ++ )
            {
                dp = new Observation( 0.0 );
                dp.setIndependentValue( "t", t+1 );
                fcValues.add( dp );
            }
        
        // Get forecast values
        DataSet results = model.forecast( fcValues );
        
        // These are the expected results
        double expectedResult[]
            = { 72.50,
                74.75,
                79.62,
                78.5375,
                79.87625,
                79.613375,
                81.8293625,
                83.98055375,
                86.38638762 };
        
        checkResults( results, expectedResult, TOLERANCE );
    }
}
// Local Variables:
// tab-width: 4
// End:
