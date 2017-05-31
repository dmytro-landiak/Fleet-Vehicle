package com.landim.tests;


import com.landim.openforecast.ForecastingModel;
import com.landim.openforecast.DataPoint;
import com.landim.openforecast.DataSet;
import com.landim.openforecast.Observation;
import com.landim.openforecast.models.TripleExponentialSmoothingModel;

public class TripleExponentialSmoothingTest extends OpenForecastTestCase
{

    private static double TOLERANCE = 0.0005;

    public TripleExponentialSmoothingTest( String name )
    {
        super(name);
    }

    public void testConstantTripleExponentialSmoothing()
    {
        final int NUMBER_OF_OBSERVATIONS = 20;
        
        // Set up some constant observed values
        DataSet observedData = new DataSet();
        DataPoint dp;
        
        for ( int t=0; t<NUMBER_OF_OBSERVATIONS; t++ )
            {
                dp = new Observation( 5.0 );
                dp.setIndependentValue( "t",  t+1 );
                observedData.add( dp );
            }
        observedData.setPeriodsPerYear( 4 );
        
        // These are the expected results
        double expectedResult[] = { 5.0, 5.0 };
        
        // Try the forecasting model with smoothing constants ranging
        //  from 0.1 to 0.9 (in 0.1 increments)
        double beta = 0.1;
        double gamma = 0.1;
        double alpha = 0.1;
        for ( ; alpha<0.95; alpha+=0.1 )
            {
                ForecastingModel model
                    = new TripleExponentialSmoothingModel(alpha,
                                                          gamma,
                                                          beta);
                
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
    

    public void testAirConditioning2YearExample()
    {
        DataSet observedData = new DataSet();
        DataPoint dp;
        
        // Two years of sales data for air conditioning units
        // - for periods -23 ... 0
        double[] observations = { 4, 3,10,14,25,26,38,40,28,17,16,13,
                                  9, 6,18,27,48,50,75,77,52,33,31,24 };
        
        for ( int t=0; t<observations.length; t++ )
            {
                dp = new Observation( observations[t] );
                dp.setIndependentValue( "t",  t-23);
                observedData.add( dp );
            }
        observedData.setPeriodsPerYear( 12 );
        
        ForecastingModel model
            = new TripleExponentialSmoothingModel( 0.5, 0.4, 0.6 );
        
        // Initialize the model
        model.init( observedData );

        //assertEquals( "Checking the accuracy of the model",
        //              3.36563849, model.getMSE(), MSE_TOLERANCE );
        
        // Create a data set for forecasting
        DataSet fcValues = new DataSet();
        
        for ( int t=0; t<12; t ++ )
            {
                dp = new Observation( 0.0 );
                dp.setIndependentValue( "t", t+1 );
                fcValues.add( dp );
            }
        
        // Get forecast values
        DataSet results = model.forecast( fcValues );
        
        // These are the expected results
        double expectedResult[]
            = { 10.516154,  7.650000,  /* t =  1, 2 */
                24.944615, 37.206923,  /* t =  3, 4 */
                68.214615, 73.000000,  /* t =  5, 6 */
               111.057692,118.520769,  /* t =  7, 8 */
                83.618462, 53.210769,  /* t =  9,10 */
                51.268462, 41.650000 };/* t = 11,12 */
        
        checkResults( results, expectedResult, TOLERANCE );
    }


    public void testAirConditioning3YearExample()
    {
        DataSet observedData = new DataSet();
        DataPoint dp;
        
        // Two years of sales data for air conditioning units
        // - for periods -23 ... 12
        double[] observations = { 4, 3,10,14,25,26,38,40,28,17,16,13,
                                  9, 6,18,27,48,50,75,77,52,33,31,24,
                                 13, 7,23,32,58,60,90,93,63,39,37,29 };
        
        for ( int t=0; t<observations.length; t++ )
            {
                dp = new Observation( observations[t] );
                dp.setIndependentValue( "t",  t-23);
                observedData.add( dp );
            }
        observedData.setPeriodsPerYear( 12 );
        
        ForecastingModel model
            = new TripleExponentialSmoothingModel( 0.5, 0.4, 0.6 );
        
        // Initialize the model
        model.init( observedData );

        //assertEquals( "Checking the accuracy of the model",
        //              3.36563849, model.getMSE(), MSE_TOLERANCE );
        
        // Create a data set for forecasting
        DataSet fcValues = new DataSet();
        
        for ( int t=0; t<12; t ++ )
            {
                dp = new Observation( 0.0 );
                dp.setIndependentValue( "t", t+1 );
                fcValues.add( dp );
            }
        
        // Get forecast values
        DataSet results = model.forecast( fcValues );
        
        // These are the expected results
        double expectedResult[]
            = { 10.516154,  8.875898,  /* t =  1, 2 */
                25.776672, 35.482731,  /* t =  3, 4 */
                59.162286, 59.736113,  /* t =  5, 6 */
                86.897135, 90.762761,  /* t =  7, 8 */
                62.680566, 38.729109,  /* t =  9,10 */
                36.338738, 29.031300 };/* t = 11,12 */
        
        checkResults( results, expectedResult, TOLERANCE );
    }
}
// Local Variables:
// tab-width: 4
// End:
