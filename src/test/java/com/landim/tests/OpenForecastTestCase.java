
package com.landim.tests;


import java.util.Iterator;
import junit.framework.TestCase;
import com.landim.openforecast.DataPoint;
import com.landim.openforecast.DataSet;


public abstract class OpenForecastTestCase extends TestCase
{

    private static double TOLERANCE = 0.00001;
    

    public OpenForecastTestCase( String name )
    {
        super(name);
    }

    protected void checkResults( DataSet actualResults,
                                 double[] expectedResults )
    {
        checkResults( actualResults, expectedResults, TOLERANCE );
    }
    

    protected void checkResults( DataSet actualResults,
                                 double[] expectedResults,
                                 double tolerance )
    {
        // This is just to safeguard against a bug in the test case!  :-)
        assertNotNull( "Checking expected results is not null",
                       expectedResults );
        assertTrue( "Checking there are some expected results",
                    expectedResults.length > 0 );
        
        assertEquals( "Checking the correct number of results returned",
                      expectedResults.length, actualResults.size() );
        
        // Iterate through the results, checking each one in turn
        Iterator<DataPoint> it = actualResults.iterator();
        int i=0;
        while ( it.hasNext() )
            {
                // Check that the results are within specified tolerance
                //  of the expected values
                DataPoint fc = (DataPoint)it.next();
                double fcValue = fc.getDependentValue();
                
                assertEquals( "Checking result",
                              expectedResults[i], fcValue, tolerance );
                i++;
            }
    }
}
// Local variables:
// tab-width: 4
// End:
