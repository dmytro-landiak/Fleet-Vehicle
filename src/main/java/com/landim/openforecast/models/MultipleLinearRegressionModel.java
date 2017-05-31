package com.landim.openforecast.models;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import com.landim.openforecast.DataPoint;
import com.landim.openforecast.DataSet;


public class MultipleLinearRegressionModel extends AbstractForecastingModel
{
    /**
     * The intercept for the linear regression model. Initialized following a
     * call to init.
     */
    private double intercept = 0.0;
    
    /**
     * An mapping of variable names to coefficients for this multiple variable
     * linear regression model. These are initialized following a call to init.
     */
    private Hashtable<String,Double> coefficient;
    

    public MultipleLinearRegressionModel() 
    {
        coefficient = null;
    }
    

    public MultipleLinearRegressionModel( String[] independentVariable )
    {
        setIndependentVariables( independentVariable );
    }
    

    public void init( DataSet dataSet )
    {
        String varNames[] = dataSet.getIndependentVariables();
        
        // If no coefficients have been defined for this model,
        //  use all that exist in this data set
        if ( coefficient == null )
            setIndependentVariables( varNames );
        
        int n = varNames.length;
        double a[][] = new double[n+1][n+2];
        
        // Iterate through dataSet
        Iterator<DataPoint> it = dataSet.iterator();
        while ( it.hasNext() )
            {
                // Get next data point
                DataPoint dp = it.next();
                
                // For each row in the matrix, a
                for ( int row=0; row<n+1; row++ )
                    {
                        double rowMult = 1.0;
                        if ( row != 0 )
                            {
                                // Get value of independent variable, row
                                String rowVarName = varNames[row-1];
                                rowMult = dp.getIndependentValue(rowVarName);
                            }
                        
                        // For each column in the matrix, a
                        for ( int col=0; col<n+2; col++ )
                            {
                                double colMult = 1.0;
                                if ( col == n+1 )
                                    {
                                        // Special case, for last column
                                        //  use value of dependent variable
                                        colMult = dp.getDependentValue();
                                    }
                                else if ( col > 0 )
                                    {
                                        // Get value of independent variable, col
                                        String colVarName = varNames[col-1];
                                        colMult = dp.getIndependentValue(colVarName);
                                    }
                                
                                a[row][col] += rowMult * colMult;
                            }
                    }
            }
        
        // Solve equations to derive coefficients
        double coeff[] = Utils.GaussElimination( a );
        
        // Assign coefficients to independent variables
        intercept = coeff[0];
        for ( int i=1; i<n+1; i++ )
            coefficient.put( varNames[i-1], new Double(coeff[i]) );
        
        // Calculate the accuracy indicators
        calculateAccuracyIndicators( dataSet );
    }

    public void init( double intercept, Hashtable<String,Double> coefficients )
    {
        // If no coefficients have been defined for this model,
        //  use all that exist in the list of coefficients
        if ( coefficient == null )
            {
                // Iterate through the set of keys, building a String[]
                Enumeration<String> keysEnum = coefficients.keys();
                String[] keys = new String[ coefficients.size() ];
                int k = 0;
                while ( keysEnum.hasMoreElements() )
                    keys[k++] = keysEnum.nextElement();
                
                setIndependentVariables( keys );
            }
        
        this.intercept = intercept;
        
        // Iterate through the variable names in this.coefficient
        Iterator<String> it = coefficient.keySet().iterator();
        while ( it.hasNext() )
            {
                // Get variable name
                String name = it.next();
                
                // Look up variable in coefficients Hashtable
                double coeff = ((Number)coefficients.get(name)).doubleValue();
                
                // Assign value back into this.coefficient
                coefficient.put( name, new Double(coeff) );
            }
        
        // Indicate that the model has been initialized
        initialized = true;
        
        // Note that accuracy indicators by default are initialized to
        //  their worst possible values, so no need to update them here
    }

    public double getIntercept()
    {
        if ( !initialized )
            throw new ModelNotInitializedException();
        
        return intercept;
    }

    public Hashtable<String,Double> getCoefficients()
    {
        if ( !initialized )
            throw new ModelNotInitializedException();
        
        return new Hashtable<String,Double>( coefficient );
    }
    
    /**
     * Returns the number of predictors used by the underlying model.
     * @return the number of predictors used by the underlying model.
     */
    public int getNumberOfPredictors()
    {
        return coefficient.size();
    }
    

    public double forecast( DataPoint dataPoint )
    {
        if ( !initialized )
            throw new ModelNotInitializedException();
        
        double forecastValue = intercept;
        
        Iterator< Map.Entry<String,Double> > it = coefficient.entrySet().iterator();
        while ( it.hasNext() )
            {
                Map.Entry<String,Double> entry = it.next();
                
                // Get value of independent variable
                double x = dataPoint.getIndependentValue( (String)entry.getKey() );
                
                // Get coefficient for this variable
                double coeff = ((Double)entry.getValue()).doubleValue();
                forecastValue += coeff * x;
            }
        
        dataPoint.setDependentValue( forecastValue );
        
        return forecastValue;
    }
    
    /**
     * A helper function that initializes the set of independent variables used
     * in this model to the given array of independent variable names.
     * @param independentVariable an array of independent variables to use in
     *        this MultipleLinearRegressionModel.
     */
    private void setIndependentVariables(String[] independentVariable)
    {
        // Create a new hashtable of just the right size
        coefficient = new Hashtable<String,Double>( independentVariable.length );
        
        // Add each independent variable with an initial coefficient of 0.0
        for ( int v=0; v<independentVariable.length; v++ )
            coefficient.put( independentVariable[v], new Double(0.0) );
    }
    
    /**
     * Returns a short name for this type of forecasting model. A more detailed
     * explanation is provided by the toString method.
     * @return a short string describing this type of forecasting model.
     */
    public String getForecastType()
    {
        return "Multiple variable linear regression";
    }
    
    /**
     * Returns a detailed description of this forcasting model, including the
     * intercept and slope. A shortened version of this is provided by the
     * getForecastType method.
     * @return a description of this forecasting model.
     */
    public String toString()
    {
        String desc = "Multiple variable linear regression model with the following equation:\n"
            +"  y="+intercept;
        
        Set< Map.Entry<String,Double> > coeffSet = coefficient.entrySet();
        Iterator< Map.Entry<String,Double> > it = coeffSet.iterator();
        while ( it.hasNext() )
            {
                Map.Entry<String,Double> entry = it.next();
                double coeff = ((Double)entry.getValue()).doubleValue();
                if ( coeff < -TOLERANCE )
                    desc += coeff + "*" + entry.getKey();
                else if ( coeff > TOLERANCE )
                    desc += "+" + coeff + "*" + entry.getKey();
                // else coeff == 0.0
            }
        
        return desc;
    }
}
// Local variables:
// tab-width: 4
// End:
