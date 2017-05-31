package com.landim.tests;


import java.lang.reflect.InvocationTargetException;

import org.jfree.data.time.Day;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesDataItem;

import com.landim.openforecast.DataPoint;
import com.landim.openforecast.DataSet;
import com.landim.openforecast.Observation;
import com.landim.openforecast.output.TimeSeriesOutputter;


public class TimeSeriesOutputterTest extends OpenForecastTestCase
{
    // Constants used to determine size of test
    private double TOLERANCE = 0.000001;
    

    public void testOutput()
        throws ClassNotFoundException, NoSuchMethodException,
        InstantiationException, IllegalAccessException,
        InvocationTargetException, InstantiationException
    {
        // Constants used to determine size of test
        int NUMBER_OF_TIME_PERIODS = 10;
        String TIME_VARIABLE = "t";
        
        // Set up array for expected results
        double expectedValue[] = new double[ NUMBER_OF_TIME_PERIODS ];

        // We'll set up periods starting from today
        RegularTimePeriod period = new Day();

        DataSet dataSet = new DataSet();
        dataSet.setTimeVariable( TIME_VARIABLE );
        for ( int d=0; d<NUMBER_OF_TIME_PERIODS; d++ )
            {
                double value = (double)d;
                DataPoint obs = new Observation( value );
                obs.setIndependentValue( TIME_VARIABLE,
                                         period.getMiddleMillisecond() );
                dataSet.add( obs );

                period = period.next();
                expectedValue[d] = value;
            }

        assertEquals("Checking only one independent variable exists in dataSet",
                     1,dataSet.getIndependentVariables().length);


        assertEquals("Checking dataSet has correct number of entries",
                     NUMBER_OF_TIME_PERIODS,
                     dataSet.size());

        // Create TimeSeriesOutputter and use it to output dataSet
        TimeSeries timeSeries = new TimeSeries("test");
        TimeSeriesOutputter outputter
            = new TimeSeriesOutputter( timeSeries,
                                       period.getClass() );
        outputter.output( dataSet );

        assertEquals("Checking number of items in time series",
                     NUMBER_OF_TIME_PERIODS,
                     timeSeries.getItemCount());

        // Reset period to start checking from today onwards
        period = new Day();
        for ( int d=0; d<NUMBER_OF_TIME_PERIODS; d++ )
            {
                TimeSeriesDataItem dataItem = timeSeries.getDataItem(d);

                period = dataItem.getPeriod();
                assertNotNull("Checking time period",period);
                
                long timeValue = period.getMiddleMillisecond();
                assertTrue("Checking time periods match",
                           (double)timeValue>=period.getFirstMillisecond()
                           && (double)timeValue<=period.getLastMillisecond());

                assertEquals("Checking values for period "+dataItem.getPeriod()
                             +" match",
                             expectedValue[d],
                             dataItem.getValue().doubleValue(),
                             TOLERANCE);

                period = period.next();
            }
    }
    
    public TimeSeriesOutputterTest( String name )
    {
        super(name);
    }
}
// Local variables:
// tab-width: 4
// End:
