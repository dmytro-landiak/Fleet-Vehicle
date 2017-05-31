package com.landim.openforecast.input;


import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesDataItem;

import com.landim.openforecast.DataPoint;
import com.landim.openforecast.DataSet;
import com.landim.openforecast.Observation;

public class TimeSeriesBuilder extends AbstractBuilder
{
    /**
     * Stores the result set from which this builder is to read its data.
     */
    private TimeSeries timeSeries;
    
    /**
     * Constructs a new TimeSeriesBuilder that reads its input from the given
     * TimeSeries object. This builder defaults the of the independent, time
     * variable to be the class name of the RegularTimePeriod used in the
     * TimeSeries. For examples, if a series of org.jfree.data.time.Day objects
     * are used, then the name of the independent variable will default to
     * "Day" (without the quotes).
     *
     * See the class description for more information.
     * @param timeSeries the TimeSeries object containing data to be used to
     * build the DataSet.
     * @throws IllegalArgumentException if the TimeSeries is empty.
     */
    public TimeSeriesBuilder( TimeSeries timeSeries )
    {
        if ( timeSeries.getItemCount() <= 0 )
            throw new IllegalArgumentException("TimeSeries cannot be empty.");
        
        this.timeSeries = timeSeries;
        
        // Use base name of TimePeriod class, as name of time variable
        RegularTimePeriod timePeriod = timeSeries.getTimePeriod(0);
        String name = timePeriod.getClass().getName();
        name = name.substring( name.lastIndexOf(".")+1 );
        addVariable( name );
    }
    
    /**
     * Constructs a new TimeSeriesBuilder that reads its input from the given
     * TimeSeries object. This builder uses the given name for the independent,
     * time variable in the DataPoints that are created.
     * @param timeSeries the TimeSeries object containing data to be used to
     * build the DataSet.
     * @param timeVariableName the name to use for the time variable.
     * @throws IllegalArgumentException if the TimeSeries is empty.
     */
    public TimeSeriesBuilder( TimeSeries timeSeries, String timeVariableName )
    {
        if ( timeSeries.getItemCount() <= 0 )
            throw new IllegalArgumentException("TimeSeries cannot be empty.");
        
        this.timeSeries = timeSeries;
        setTimeVariable( timeVariableName );
    }
    
    /**
     * Returns the name of the currently defined time variable.
     * @return the name currently defined for the time variable.
     */
    public String getTimeVariable()
    {
        return getVariableName( 0 );
    }
    
    /**
     * Used to change the time variable name.
     * @param name the new name for the time variable.
     */
    public void setTimeVariable( String name )
    {
        setVariableName( 0, name );
    }
    
    /**
     * Retrieves a DataSet - a collection of DataPoints - from the current
     * (JFreeChart) TimeSeries. The DataSet should contain all DataPoints
     * defined by the TimeSeries.
     *
     * <p>In general, build will attempt to convert all values in the
     * TimeSeries to data points.
     * @return a DataSet built from the current TimeSeries.
     */
    public DataSet build()
    {
        DataSet dataSet = new DataSet();
        
        dataSet.setTimeVariable( getTimeVariable() );
        
        // Iterate through TimeSeries,
        //  creating new DataPoint instance for each row
        int numberOfPeriods = timeSeries.getItemCount();
        for ( int t=0; t<numberOfPeriods; t++ )
            dataSet.add( build(timeSeries.getDataItem(t)) );
        
        return dataSet;
    }
    
    /**
     * Builds a DataPoint from the given TimeSeriesDataItem. The name used for
     * the independent, time variable can be changed using the setTimeVariable
     * method.
     * @param dataItem the TimeSeriesDataItem from which the values are to be
     * read and used to construct a new DataPoint.
     * @return a DataPoint object with values as specified by the given
     * TimeSeriesDataItem.
     */
    private DataPoint build( TimeSeriesDataItem dataItem )
    {
        DataPoint dataPoint
            = new Observation( dataItem.getValue().doubleValue() );
        
        // Get time value (at middle of time period)
        double timeValue = dataItem.getPeriod().getMiddleMillisecond();
        
        // Store time value as independent variable
        dataPoint.setIndependentValue( getVariableName(0),
                                       timeValue );
        
        return dataPoint;
    }
}
// Local variables:
// tab-width: 4
// End:
