
package com.landim.tests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


public class OpenForecastTestSuite
{
    private OpenForecastTestSuite()
    {
    }
    
    public static Test suite()
    {
        TestSuite suite = new TestSuite();
        try
            {
                // Test models and core classes
                suite.addTestSuite( (Class<TestCase>) Class.forName( "net.sourceforge.openforecast.tests.BasicTest" ) );
                suite.addTestSuite( (Class<TestCase>) Class.forName( "net.sourceforge.openforecast.tests.DataSetTest" ) );
                suite.addTestSuite( (Class<TestCase>) Class.forName( "net.sourceforge.openforecast.tests.SimpleExponentialSmoothingTest" ) );
                suite.addTestSuite( (Class<TestCase>) Class.forName( "net.sourceforge.openforecast.tests.DoubleExponentialSmoothingTest" ) );
                suite.addTestSuite( (Class<TestCase>) Class.forName( "net.sourceforge.openforecast.tests.TripleExponentialSmoothingTest" ) );
                suite.addTestSuite( (Class<TestCase>) Class.forName( "net.sourceforge.openforecast.tests.MovingAverageTest" ) );
                suite.addTestSuite( (Class<TestCase>) Class.forName( "net.sourceforge.openforecast.tests.MultipleLinearRegressionTest" ) );
                suite.addTestSuite( (Class<TestCase>) Class.forName( "net.sourceforge.openforecast.tests.PolynomialRegressionTest" ) );
                suite.addTestSuite( (Class<TestCase>) Class.forName( "net.sourceforge.openforecast.tests.WeightedMovingAverageTest" ) );

                // Test builder(s)
                suite.addTestSuite( (Class<TestCase>) Class.forName( "net.sourceforge.openforecast.tests.CSVBuilderTest" ) );
                suite.addTestSuite( (Class<TestCase>) Class.forName( "net.sourceforge.openforecast.tests.TimeSeriesBuilderTest" ) );
                
                // Test outputter(s)
                suite.addTestSuite( (Class<TestCase>) Class.forName( "net.sourceforge.openforecast.tests.DelimitedTextOutputterTest" ) );
                suite.addTestSuite( (Class<TestCase>) Class.forName( "net.sourceforge.openforecast.tests.TimeSeriesOutputterTest" ) );
            }
        catch ( ClassNotFoundException exCNF )
            {
                System.err.println( "Check setting of CLASSPATH environment variable" );
                System.err.println( "Unable to locate test class:" );
                System.err.println( "  "+exCNF.getMessage() );
            }
        
        return suite;
    }
}
// Local Variables:
// tab-width: 4
// End:
