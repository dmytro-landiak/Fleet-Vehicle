package com.landim.tests;

import com.landim.openforecast.DataPoint;
import com.landim.openforecast.DataSet;
import com.landim.openforecast.ForecastingModel;
import com.landim.openforecast.Observation;
import com.landim.openforecast.models.PolynomialRegressionModel;


public class PolynomialRegressionTest extends OpenForecastTestCase
{
    public void testPolynomialRegression()
    {
        DataSet observedData = new DataSet();
        
        for ( int x=0; x<10; x++ )
            {
                double y = 10.6 + 3.5*x + 0.5*Math.pow(x,3);
                
                DataPoint dp = new Observation( y );
                dp.setIndependentValue( "x1", x );
                
                // Fill x2 with random data - it should be ignored
                dp.setIndependentValue( "x2", (Math.random()-0.5)*100 );
                observedData.add( dp );
            }
        
        ForecastingModel model = new PolynomialRegressionModel( "x1", 5 );
        model.init( observedData );
        
        DataSet fcValues = new DataSet();
        double expectedResult[] = new double[10];
        for ( int x=10; x<20; x++ )
            {
                double y = 10.6 + 3.5*x + 0.5*Math.pow(x,3);
                
                DataPoint dp = new Observation( 0.0 );
                dp.setIndependentValue( "x1", x );
                
                // Fill x2 with random data - it should be ignored
                dp.setIndependentValue( "x2", (Math.random()-0.5)*100 );
                fcValues.add( dp );
                
                // Save expected value
                expectedResult[x-10] = y;
            }
        
        DataSet results = model.forecast( fcValues );
        
        checkResults( results, expectedResult );
    }
    
    public PolynomialRegressionTest( String name )
    {
        super(name);
    }
}
// Local variables:
// tab-width: 4
// End:
