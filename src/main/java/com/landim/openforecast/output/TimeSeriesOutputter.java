package com.landim.openforecast.output;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.Iterator;

import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesDataItem;

import com.landim.openforecast.DataPoint;
import com.landim.openforecast.DataSet;


public class TimeSeriesOutputter implements Outputter
{
    /**
     * Stores the result set from which this builder is to read its data.
     */
    private TimeSeries timeSeries;
    
    /**
     * The Constructor object representing the constructor to use for the
     * TimePeriod components of the TimeSeriesDataItems within the output
     * TimeSeries.
     */
    private Constructor<?> timePeriodConstructor;
    
    /**
     * Constructs a new TimeSeriesOutputter that adds its output to the named
     * TimeSeries.
     * @param timeSeries the TimeSeries to add output to.
     */
    public TimeSeriesOutputter( TimeSeries timeSeries,
                                Class<?> timePeriodClass )
        throws ClassNotFoundException, NoSuchMethodException
    {
        this.timeSeries = timeSeries;
        
        Class<?>[] args = new Class[1];
        args[0] = Class.forName("java.util.Date");
        timePeriodConstructor = timePeriodClass.getConstructor(args);
    }
    
    /**
     * Adds a DataSet - a collection of DataPoints - to the current TimeSeries.
     * The DataSet should contain all DataPoints to be output.
     * @param dataSet the DataSet to be output to the current TimeSeries.
     */
    public void output( DataSet dataSet )
        throws InstantiationException, IllegalAccessException,
        InvocationTargetException, InstantiationException
    {
        String timeVariable = dataSet.getTimeVariable();
        
        Iterator<DataPoint> it = dataSet.iterator();
        while ( it.hasNext() )
            {
                DataPoint dataPoint = it.next();
                output( dataPoint, timeVariable );
            }
    }
    
    /**
     * Outputs the given DataPoint to the current TimeSeries.
     * @param dataPoint the DataPoint to output to the current TimeSeries.
     */
    private void output( DataPoint dataPoint, String timeVariable )
        throws InstantiationException, IllegalAccessException,
        InvocationTargetException, InstantiationException
    {
        long timeValue = (long)dataPoint.getIndependentValue(timeVariable);
        
        Object[] args = new Object[1];
        args[0] = new Date( timeValue );
        RegularTimePeriod period
            = (RegularTimePeriod)timePeriodConstructor.newInstance(args);
        
        double value = dataPoint.getDependentValue();
        
        timeSeries.add( new TimeSeriesDataItem(period,value) );
    }
}
// Local variables:
// tab-width: 4
// End:
