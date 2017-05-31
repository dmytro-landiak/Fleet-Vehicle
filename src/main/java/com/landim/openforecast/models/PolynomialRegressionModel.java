package com.landim.openforecast.models;


import java.util.Iterator;

import com.landim.openforecast.DataPoint;
import com.landim.openforecast.DataSet;



public class PolynomialRegressionModel extends AbstractForecastingModel
{
    /**
     * The name of the independent variable used in this regression model.
     */
    private String independentVariable;

    /**
     * The order of the polynomial to fit in this regression model.
     */
    private int order = 0;

    /**
     * An array of coefficients for this polynomial regression model. These are
     * initialized following a call to init.
     */
    private double coefficient[];


    public PolynomialRegressionModel( String independentVariable )
    {
        this( independentVariable, 10 );
    }


    public PolynomialRegressionModel( String independentVariable, int order )
    {
        this.independentVariable = independentVariable;
        this.order = order;
    }


    public void init( DataSet dataSet )
    {
        double a[][] = new double[order][order+1];

        for ( int i=0; i<order; i++ )
            {
                for ( int j=0; j<order; j++ )
                    {
                        int k = i + j;

                        Iterator<DataPoint> it = dataSet.iterator();
                        while ( it.hasNext() )
                            {
                            DataPoint dp = it.next();
                            
                            double x = dp.getIndependentValue( independentVariable );
                            
                            a[i][j] = a[i][j] + Math.pow(x,k);
                            }
                    }

                Iterator<DataPoint> it = dataSet.iterator();
                while ( it.hasNext() )
                    {
                        DataPoint dp = it.next();
                        
                        double x = dp.getIndependentValue( independentVariable );
                        double y = dp.getDependentValue();
                        
                        a[i][order] += y*Math.pow(x,i);
                    }
            }

        coefficient = Utils.GaussElimination( order, a );

        // Calculate the accuracy indicators
        calculateAccuracyIndicators( dataSet );
    }


    public double forecast( DataPoint dataPoint )
    {
        if ( !initialized )
            throw new ModelNotInitializedException();

        double x = dataPoint.getIndependentValue( independentVariable );
        double forecastValue = 0.0;
        for ( int i=0; i<order; i++ )
            forecastValue += coefficient[i] * Math.pow(x,i);

        dataPoint.setDependentValue( forecastValue );

        return forecastValue;
    }

    /**
     * Returns the number of predictors used by the underlying model.
     * @return the number of predictors used by the underlying model.
     * @since 0.5
     */
    public int getNumberOfPredictors()
    {
        return 1;
    }
    
    /**
     * Returns a short name for this type of forecasting model. A more detailed
     * explanation is provided by the toString method.
     * @return a short string describing this type of forecasting model.
     */
    public String getForecastType()
    {
        return "Single variable polynomial regression";
    }

    /**
     * Returns a detailed description of this forcasting model, including the
     * intercept and slope. A shortened version of this is provided by the
     * getForecastType method.
     * @return a description of this forecasting model.
     */
    public String toString()
    {
        String description = "Single variable polynomial regression model";
        if ( !initialized )
            return description + " (uninitialized)";

        description += " with an equation of: y = "+coefficient[0];
        for ( int i=1; i<coefficient.length; i++ )
             if ( Math.abs(coefficient[i]) > 0.001 )
                  description += (coefficient[i]<0 ? "" : "+")
                        + coefficient[i] + "*"
                        + independentVariable + (i>1 ? "^"+i : "" );

        return description;
    }
}
// Local variables:
// tab-width: 4
// End:
