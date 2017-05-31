package com.landim.openforecast.models;


class AccuracyIndicators
{
    /** Akaike Information Criteria measure. */
    private double aic;

    /** Arithmetic mean of the errors. */
    private double bias;
    
    /** Mean Absolute Deviation. */
    private double mad;

    /** Mean Absolute Percentage Error. */
    private double mape;

    /** Mean Square of the Error. */
    private double mse;

    /** Sum of the Absolute Errors. */
    private double sae;

    /**
     * Default constructor. Initializes all accuracy indicators to their
     * "worst" possible values - generally this means some large number,
     * indicating "very" inaccurate.
     */
    public AccuracyIndicators()
    {
        aic = bias = mad = mape = mse = sae = Double.MAX_VALUE;
    }

    /**
     * Returns the AIC for the associated forecasting model.
     * @return the AIC.
     */
    public double getAIC()
    {
        return aic;
    }

    /**
     * Sets the AIC for the associated forecasting model to the given value.
     * @param aic the new value for the AIC.
     */
    public void setAIC(double aic)
    {
        this.aic = aic;
    }

    /**
     * Returns the bias for the associated forecasting model.
     * @return the bias.
     */
    public double getBias()
    {
        return bias;
    }

    /**
     * Sets the bias for the associated forecasting model to the given value.
     * @param bias the new value for the bias.
     */
    public void setBias(double bias)
    {
        this.bias = bias;
    }


    public double getMAD()
    {
        return mad;
    }


    public void setMAD(double mad)
    {
        this.mad = mad;
    }


    public double getMAPE()
    {
        return mape;
    }

    /**
     * Sets the Mean Absolute Percentage Error (MAPE) for the associated
     * forecasting model to the given value.
     * @param mape the new value for the Mean Absolute Percentage Error.
     */
    public void setMAPE(double mape)
    {
        this.mape = mape;
    }

    public double getMSE()
    {
        return mse;
    }

    public void setMSE(double mse)
    {
        this.mse = mse;
    }

    public double getSAE()
    {
        return sae;
    }

    /**
     * Sets the Sum of the Absolute Errors (SAE) for the associated
     * forecasting model to the new value.
     * @return the new value for the Mean Absolute Deviation.
     */
    public void setSAE(double sae)
    {
        this.sae = sae;
    }

    /**
     * Returns a string containing the accuracy indicators and their values.
     * Overridden to provide some useful output for debugging.
     * @return a string containing the accuracy indicators and their values.
     */
    public String toString()
    {
        return "AIC=" + aic
            + ", bias=" + bias
            + ", MAD=" + mad
            + ", MAPE=" + mape
            + ", MSE=" + mse
            + ", SAE=" + sae;
    }
}
// Local Variables:
// tab-width: 4
// End:
