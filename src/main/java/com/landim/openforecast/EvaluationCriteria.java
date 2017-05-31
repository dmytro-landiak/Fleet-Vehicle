package com.landim.openforecast;



public class EvaluationCriteria
{
    /**
     * EvaluationCriteria referring to the Akaike Information Criteria measure.
     */
    public final static EvaluationCriteria AIC
        = new EvaluationCriteria("Akaike Information Citeria");

    /**
     * EvaluationCriteria referring to Bias.
     */
    public final static EvaluationCriteria BIAS
        = new EvaluationCriteria("Bias");
    
    /**
     * EvaluationCriteria referring to the Mean Absolute Deviation.
     */
    public final static EvaluationCriteria MAD
        = new EvaluationCriteria("Mean Absolute Deviation");
    
    /**
     * EvaluationCriteria referring to the Mean Absolute Percentage Error.
     */
    public final static EvaluationCriteria MAPE
        = new EvaluationCriteria("Mean Absolute Percentage Error");
    
    /**
     * EvaluationCriteria referring to the Mean Squared Error.
     */
    public final static EvaluationCriteria MSE
        = new EvaluationCriteria("Mean Squared Error");
    
    /**
     * EvaluationCriteria referring to the Sum of the Absolute Error.
     */
    public final static EvaluationCriteria SAE
        = new EvaluationCriteria("Sum Absolute Error");
    
    /**
     * EvaluationCriteria referring to a reasonable blend of all other
     * evaluation criteria. Rather than rely solely on one specific evaluation
     * criteria to pick a "good" model, assess them all and select the model
     * generally regarded as "better" by the majority of the criteria.
     */
    public final static EvaluationCriteria BLEND
        = new EvaluationCriteria("Blend of Bias, MAD, MAPE, MSE, and SAE");
    // Not really sure about the best short description for this one???
    
    private String description;
    
    /**
     * Creates a new EvaluationCriteria with the given description.
     * @param description a description of the evaluation criteria.
     */
    private EvaluationCriteria(String description)
    {
        this.description = description;
    }
    
    /**
     * Compares two EvaluationCriteria objects for equality.
     * @return true if the given Object is an EvaluationCriteria equivalent to
     * this EvaluationCriteria; otherwise returns false.
     */
    public boolean equals(Object obj)
    {
        if ( obj instanceof EvaluationCriteria
             && obj == this )
            return true;
        
        return false;
    }
    
    /**
     * Returns a textual description of this evaluation criteria object.
     * @return a string representation of the object.
     */
    public String toString()
    {
        return description;
    }
}
// Local Variables:
// tab-width: 4
// End:
