package com.landim.openforecast.models;


public class NaiveForecastingModel extends MovingAverageModel
{

    public NaiveForecastingModel()
    {
        super( 1 );
    }

    public NaiveForecastingModel( String independentVariable )
    {
        super( independentVariable, 1 );
    }


    public String getForecastType()
    {
        return "Naive forecast";
    }
    

    public String toString()
    {
        return
            "Naive forecasting model (i.e. moving average with a period of 1)"
            + ", using an independent variable of " + getIndependentVariable()
            + ".";
    }
}
// Local Variables:
// tab-width: 4
// End:
