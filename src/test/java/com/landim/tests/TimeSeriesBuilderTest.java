
package com.landim.tests;


import org.jfree.data.time.Day;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;

import com.landim.openforecast.DataSet;
import com.landim.openforecast.input.TimeSeriesBuilder;



public class TimeSeriesBuilderTest extends OpenForecastTestCase
{

    public void testBuilder()
    {
        // Constants used to determine size of test
        int NUMBER_OF_TIME_PERIODS = 100;
        
        // Set up array for expected results
        double expectedValue[] = new double[ NUMBER_OF_TIME_PERIODS ];
        
        // Create test TimeSeries
        TimeSeries timeSeries = new TimeSeries("Simple time series");
        RegularTimePeriod period = new Day();
        
        for ( int d=0; d<NUMBER_OF_TIME_PERIODS; d++ )
            {
                expectedValue[d] = d;
                timeSeries.add(period,d);
                period = period.next();
            }
        
        // Create TimeSeriesBuilder and use it to create the DataSet
        String TIME_VARIABLE = "t";
        TimeSeriesBuilder builder
            = new TimeSeriesBuilder( timeSeries, TIME_VARIABLE );
        DataSet dataSet = builder.build();
        
        // Verify data set contains the correct number of entries
        assertEquals( "DataSet created is of the wrong size",
                      NUMBER_OF_TIME_PERIODS, dataSet.size() );
        
        // Vefify that only two independent variable names are reported
        String[] independentVariables = dataSet.getIndependentVariables();
        assertEquals( "Checking the correct number of independent variables",
                      1, independentVariables.length );
        assertEquals( "Independent variable not set as expected",
                      TIME_VARIABLE, independentVariables[0] );

        checkResults( dataSet, expectedValue );
    }
    
    public TimeSeriesBuilderTest( String name )
    {
        super(name);
    }
}
// Local variables:
// tab-width: 4
// End:
