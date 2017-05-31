package com.landim.openforecast;

public interface DataPoint
{
    /**
     * Sets the dependent variables' value to the given value.
     * @param value the new value for the dependent variable.
     */
    public void setDependentValue( double value );

    /**
     * Returns the current value assigned to the dependent variable. This value
     * can be changed by calling setDependentValue.
     * @return the current value of the dependent variable.
     */
    public double getDependentValue();

    /**
     * Sets the named independent variables' value to the given value. Each
     * data point can have one or more name-value pairs that represent the
     * independent variables and their associated values.
     * @param value the new value for the dependent variable.
     */
    public void setIndependentValue( String name, double value );

    /**
     * Returns the current value assigned to the named independent variable.
     * This value can be changed by calling setIndependentValue.
     * @param name the name of the independent variable required.
     * @return the current value of the named independent variable.
     */
    public double getIndependentValue( String name );

    /**
     * Returns an array of all independent variable names. No checks are made
     * to ensure that the names are unique. Rather, the names are extracted
     * directly from the names used in defining and initializing the data point.
     * @return an array of independent variable names for this data point.
     */
    public String[] getIndependentVariableNames();

    /**
     * Compares the given DataPoint object to the current DataPoint object,
     * and returns true if, and only if, the two data points represent the same
     * data point. That is, the dependent value matches for the matching
     * independent values.
     * @param dp the DataPoint to compare this DataPoint object to.
     * @return true if the given DataPoint object represents the same data
     *         point as this DataPoint object.
     */
    public boolean equals( DataPoint dp );
}
// Local Variables:
// tab-width: 4
// End:
