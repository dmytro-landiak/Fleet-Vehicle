
package com.landim.tests;

import java.util.Hashtable;

import com.landim.openforecast.Forecaster;
import com.landim.openforecast.ForecastingModel;
import com.landim.openforecast.DataPoint;
import com.landim.openforecast.DataSet;
import com.landim.openforecast.Observation;
import com.landim.openforecast.models.MultipleLinearRegressionModel;

public class MultipleLinearRegressionTest extends OpenForecastTestCase
{

    private DataSet observedData;



    protected void setUp()
    {
        observedData = new DataSet();
        DataPoint dp;

        dp = new Observation( 5.0 );
        dp.setIndependentValue( "x1", 0.0 );
        dp.setIndependentValue( "x2", 0.0 );
        dp.setIndependentValue( "x3", 0.0 );
        observedData.add( dp );

        dp = new Observation( 10.0 );
        dp.setIndependentValue( "x1", 2.0 );
        dp.setIndependentValue( "x2", 1.0 );
        dp.setIndependentValue( "x3", 1.0 );
        observedData.add( dp );

        dp = new Observation( 9.0 );
        dp.setIndependentValue( "x1", 2.5 );
        dp.setIndependentValue( "x2", 2.0 );
        dp.setIndependentValue( "x3", 2.0 );
        observedData.add( dp );

        dp = new Observation( 0.0 );
        dp.setIndependentValue( "x1", 1.0 );
        dp.setIndependentValue( "x2", 3.0 );
        dp.setIndependentValue( "x3", 3.0 );
        observedData.add( dp );

        dp = new Observation( 3.0 );
        dp.setIndependentValue( "x1", 4.0 );
        dp.setIndependentValue( "x2", 6.0 );
        dp.setIndependentValue( "x3", 4.0 );
        observedData.add( dp );

        dp = new Observation( 27.0 );
        dp.setIndependentValue( "x1", 7.0 );
        dp.setIndependentValue( "x2", 2.0 );
        dp.setIndependentValue( "x3", 5.0 );
        observedData.add( dp );
    }


    public void testMultipleLinearRegression()
    {
        // Obtain a Multiple Linear Regression forecasting model
        //  given this data set
        String independentVars[] = { "x1", "x2", "x3" };
        MultipleLinearRegressionModel forecaster
            = new MultipleLinearRegressionModel( independentVars );

        // Initialize the model
        forecaster.init( observedData );

        // Create a data set for forecasting
        DataSet fcValues = new DataSet();

        DataPoint dp = new Observation( 0.0 );
        dp.setIndependentValue( "x1", 5.0 );
        dp.setIndependentValue( "x2", 5.0 );
        dp.setIndependentValue( "x3", 5.0 );
        fcValues.add( dp );

        dp = new Observation( 0.0 );
        dp.setIndependentValue( "x1", 2.0 );
        dp.setIndependentValue( "x2", 5.0 );
        dp.setIndependentValue( "x3", 7.0 );
        fcValues.add( dp );

        dp = new Observation( 0.0 );
        dp.setIndependentValue( "x1", 8.0 );
        dp.setIndependentValue( "x2", 16.0 );
        dp.setIndependentValue( "x3", 22.0 );
        fcValues.add( dp );

        // Get forecast values
        DataSet results = forecaster.forecast( fcValues );
        assertTrue( fcValues.size() == results.size() );

        // These are the expected results
        double expectedResult[] = { 10.0, -2.0, -11.0 };

        // Check results against expected results
        checkResults( results, expectedResult );
    }

    public void testForecaster()
    {
        // Obtain a ForecastingModel
        ForecastingModel forecaster = Forecaster.getBestForecast( observedData );

        // Ensure that a Multiple Linear Regression Model was chosen
        assertTrue( forecaster
                    .getClass()
                    .getName()
                    .equals("com.landim.openforecast.models.MultipleLinearRegressionModel") );

        // Create a data set for forecasting
        DataSet fcValues = new DataSet();

        DataPoint dp = new Observation( 0.0 );
        dp.setIndependentValue( "x1", 5.0 );
        dp.setIndependentValue( "x2", 5.0 );
        dp.setIndependentValue( "x3", 5.0 );
        fcValues.add( dp );

        dp = new Observation( 0.0 );
        dp.setIndependentValue( "x1", 2.0 );
        dp.setIndependentValue( "x2", 5.0 );
        dp.setIndependentValue( "x3", 7.0 );
        fcValues.add( dp );

        dp = new Observation( 0.0 );
        dp.setIndependentValue( "x1", 8.0 );
        dp.setIndependentValue( "x2", 16.0 );
        dp.setIndependentValue( "x3", 22.0 );
        fcValues.add( dp );

        // Get forecast values
        DataSet results = forecaster.forecast( fcValues );
        assertTrue( fcValues.size() == results.size() );

        // These are the expected results
        double expectedResult[] = { 10.0, -2.0, -11.0 };

        // Check results against expected results
        checkResults( results, expectedResult );
    }

    /**
     * Tests the use of user-defined coefficients with the multiple
     * variable linear regression model.
     */
    public void testUserDefinedCoefficientsWithNamedVars()
    {
        // Reset the observedData, to ensure that it is *not* used
        observedData.clear();
        observedData = null;
        
        // Initialize coefficients
        final int NUMBER_OF_COEFFS = 5;
        double intercept = 0.12345;
        Hashtable<String,Double> coeffs = new Hashtable<String,Double>();
        String varNames[] = new String[NUMBER_OF_COEFFS];
        for ( int c=0; c<NUMBER_OF_COEFFS; c++ )
            {
                varNames[c] = new String( "param"+(c+1) );
                coeffs.put( varNames[c], new Double( Math.pow(10,c) ) );
            }

        // Create a data set for forecasting
        DataSet fcValues = new DataSet();

        for ( int count=0; count<10; count++ )
            {
                DataPoint dp = new Observation( 0.0 );
                dp.setIndependentValue( "param1", count+4 );
                dp.setIndependentValue( "param2", count+3 );
                dp.setIndependentValue( "param3", count+2 );
                dp.setIndependentValue( "param4", count+1 );
                dp.setIndependentValue( "param5", count   );
                fcValues.add( dp );
            }

        // Get forecast values
        MultipleLinearRegressionModel model
            = new MultipleLinearRegressionModel( varNames );

        model.init( intercept, coeffs );
        DataSet results = model.forecast( fcValues );
        assertTrue( fcValues.size() == results.size() );

        // These are the expected results
        double expectedResult[] = {   1234.12345,
                                     12345.12345,
                                     23456.12345,
                                     34567.12345,
                                     45678.12345,
                                     56789.12345,
                                     67900.12345,
                                     79011.12345,
                                     90122.12345,
                                    101233.12345 };

        // Check results against expected results
        checkResults( results, expectedResult );
    }

    /**
     * Tests the use of user-defined coefficients with the multiple
     * variable linear regression model.
     */
    public void testUserDefinedCoefficients()
    {
        // Reset the observedData, to ensure that it is *not* used
        observedData.clear();
        observedData = null;
        
        // Initialize coefficients
        final int NUMBER_OF_COEFFS = 5;
        double intercept = 0.12345;
        Hashtable<String,Double> coeffs = new Hashtable<String,Double>();
        String varNames[] = new String[NUMBER_OF_COEFFS];
        for ( int c=0; c<NUMBER_OF_COEFFS; c++ )
            {
                varNames[c] = new String( "param"+(c+1) );
                coeffs.put( varNames[c], new Double( Math.pow(10,c) ) );
            }

        // Create a data set for forecasting
        DataSet fcValues = new DataSet();

        for ( int count=0; count<10; count++ )
            {
                DataPoint dp = new Observation( 0.0 );
                dp.setIndependentValue( "param1", count+4 );
                dp.setIndependentValue( "param2", count+3 );
                dp.setIndependentValue( "param3", count+2 );
                dp.setIndependentValue( "param4", count+1 );
                dp.setIndependentValue( "param5", count   );
                fcValues.add( dp );
            }

        // Get forecast values
        MultipleLinearRegressionModel model
            = new MultipleLinearRegressionModel();

        model.init( intercept, coeffs );
        DataSet results = model.forecast( fcValues );
        assertTrue( fcValues.size() == results.size() );

        // These are the expected results
        double expectedResult[] = {   1234.12345,
                                     12345.12345,
                                     23456.12345,
                                     34567.12345,
                                     45678.12345,
                                     56789.12345,
                                     67900.12345,
                                     79011.12345,
                                     90122.12345,
                                    101233.12345 };

        // Check results against expected results
        checkResults( results, expectedResult );
    }

    public MultipleLinearRegressionTest( String name )
    {
        super(name);
    }
}
// Local variables:
// tab-width: 4
// End:
