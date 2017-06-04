package com.landim.openforecast;


import java.util.ArrayList;
import java.util.Iterator;
import com.landim.openforecast.models.MovingAverageModel;
import com.landim.openforecast.models.MultipleLinearRegressionModel;
import com.landim.openforecast.models.PolynomialRegressionModel;
import com.landim.openforecast.models.RegressionModel;
import com.landim.openforecast.models.SimpleExponentialSmoothingModel;
import com.landim.openforecast.models.DoubleExponentialSmoothingModel;
import com.landim.openforecast.models.TripleExponentialSmoothingModel;

public class Forecaster
{
    /**
     * Make constructor private to prevent this class from being instantiated
     * directly.
     */
    private Forecaster()
    {
    }

    public static ForecastingModel getBestForecast( DataSet dataSet )
    {
        return getBestForecast( dataSet, EvaluationCriteria.BLEND );
    }

    public static ForecastingModel getBestForecast( DataSet dataSet, EvaluationCriteria evalMethod )
    {
        String independentVariable[] = dataSet.getIndependentVariables();
        ForecastingModel bestModel = null;
        
        // Try single variable models
        for (String anIndependentVariable1 : independentVariable) {
            ForecastingModel model;

            // Try the Regression Model
            model = new RegressionModel(anIndependentVariable1);
            model.init(dataSet);
            if (betterThan(model, bestModel, evalMethod))
                bestModel = model;

            // Try the Polynomial Regression Model
            // Note: if order is about the same as dataSet.size() then
            //  we'll get a good/great fit, but highly variable - and
            //  unreliable - forecasts. We "guess" at a reasonable order
            //  for the polynomial curve based on the number of
            //  observations.
            int order = 10;
            if (dataSet.size() < order * order)
                order = (int) (Math.sqrt(dataSet.size())) - 1;
            model = new PolynomialRegressionModel(anIndependentVariable1,
                    order);
            model.init(dataSet);
            if (betterThan(model, bestModel, evalMethod))
                bestModel = model;
        }
        
        
        // Try multiple variable models
        
        // Create a list of available variables
        ArrayList<String> availableVariables
            = new ArrayList<String>(independentVariable.length);
        for (String anIndependentVariable : independentVariable) availableVariables.add(anIndependentVariable);
        
        // Create a list of variables to use - initially empty
        ArrayList<String> bestVariables = new ArrayList<String>(independentVariable.length);
        
        // While some variables still available to consider
        while ( availableVariables.size() > 0 )
            {
                int count = bestVariables.size();
                String workingList[] = new String[count+1];
                if ( count > 0 )
                    for ( int i=0; i<count; i++ )
                        workingList[i] = (String)bestVariables.get(i);
                
                String bestAvailVariable = null;
                
                // For each available variable
                for (String currentVar : availableVariables) {
                    // Get current variable
                    // Add variable to list to use for regression
                    workingList[count] = currentVar;

                    // Do multiple variable linear regression
                    ForecastingModel model
                            = new MultipleLinearRegressionModel(workingList);
                    model.init(dataSet);

                    //  If best so far, then save best variable
                    if (betterThan(model, bestModel, evalMethod)) {
                        bestModel = model;
                        bestAvailVariable = currentVar;
                    }

                    // Remove the current variable from the working list
                    workingList[count] = null;
                }
                
                // If no better model could be found (by adding another
                //     variable), then we're done
                if ( bestAvailVariable == null )
                    break;
                
                // Remove best variable from list of available vars
                int bestVarIndex = availableVariables.indexOf( bestAvailVariable );
                availableVariables.remove( bestVarIndex );
                
                // Add best variable to list of vars. to use
                bestVariables.add( count, bestAvailVariable );
                
                count++;
            }
        
        
        // Try time-series models
        if ( dataSet.getTimeVariable() != null )
            {
                // Try moving average model
                ForecastingModel model = new MovingAverageModel();
                model.init( dataSet );
                if ( betterThan( model, bestModel, evalMethod ) )
                    bestModel = model;
                
                // Try moving average model using periods per year if avail.
                if ( dataSet.getPeriodsPerYear() > 0 )
                    {
                        model = new MovingAverageModel( dataSet.getPeriodsPerYear() );
                        model.init( dataSet );
                        if ( betterThan( model, bestModel, evalMethod ) )
                            bestModel = model;
                    }
                
                // TODO: Vary the period and try other MA models
                // TODO: Consider appropriate use of time period in this
                
                // Try the best fit simple exponential smoothing model
                model = SimpleExponentialSmoothingModel.getBestFitModel(dataSet);
                if ( betterThan( model, bestModel, evalMethod ) )
                    bestModel = model;
                
                // Try the best fit double exponential smoothing model
                model = DoubleExponentialSmoothingModel.getBestFitModel(dataSet);
                if ( betterThan( model, bestModel, evalMethod ) )
                    bestModel = model;
                
                // Try the best fit triple exponential smoothing model
                model = TripleExponentialSmoothingModel.getBestFitModel(dataSet);
                if ( betterThan( model, bestModel, evalMethod ) )
                    bestModel = model;
                
                
            }
        
        return bestModel;
    }

    private static boolean betterThan( ForecastingModel model1,
                                       ForecastingModel model2,
                                       EvaluationCriteria evalMethod )
    {
        // Special case. Any model is better than no model!
        if ( model2 == null )
            return true;
        
        double tolerance = 0.00000001;
        
        // Use evaluation method as requested by user
        if ( evalMethod == EvaluationCriteria.BIAS )
            return ( model1.getBias() <= model2.getBias() );
        else if ( evalMethod == EvaluationCriteria.MAD )
            return ( model1.getMAD() <= model2.getMAD() );
        else if ( evalMethod == EvaluationCriteria.MAPE )
            return ( model1.getMAPE() <= model2.getMAPE() );
        else if ( evalMethod == EvaluationCriteria.MSE )
            return ( model1.getMSE() <= model2.getMSE() );
        else if ( evalMethod == EvaluationCriteria.SAE )
            return ( model1.getSAE() <= model2.getSAE() );
        else if ( evalMethod == EvaluationCriteria.AIC )
            return ( model1.getAIC() <= model2.getAIC() );
        
        // Default evaluation method is a combination
        int score = 0;
        if ( model1.getAIC()-model2.getAIC() <= tolerance )
            score++;
        else if ( model1.getAIC()-model2.getAIC() >= tolerance )
            score--;
        
        if ( model1.getBias()-model2.getBias() <= tolerance )
            score++;
        else if ( model1.getBias()-model2.getBias() >= tolerance )
            score--;
        
        if ( model1.getMAD()-model2.getMAD() <= tolerance )
            score++;
        else if ( model1.getMAD()-model2.getMAD() >= tolerance )
            score--;
        
        if ( model1.getMAPE()-model2.getMAPE() <= tolerance )
            score++;
        else if ( model1.getMAPE()-model2.getMAPE() >= tolerance )
            score--;
        
        if ( model1.getMSE()-model2.getMSE() <= tolerance )
            score++;
        else if ( model1.getMSE()-model2.getMSE() >= tolerance )
            score--;
        
        if ( model1.getSAE()-model2.getSAE() <= tolerance )
            score++;
        else if ( model1.getSAE()-model2.getSAE() >= tolerance )
            score--;
        
        if ( score == 0 )
            {
                // At this point, we're still unsure which one is best
                //  so we'll take another approach
                double diff = model1.getAIC() - model2.getAIC()
                    + model1.getBias() - model2.getBias()
                    + model1.getMAD()  - model2.getMAD()
                    + model1.getMAPE() - model2.getMAPE()
                    + model1.getMSE()  - model2.getMSE()
                    + model1.getSAE()  - model2.getSAE();
                return ( diff < 0 );
            }
        
        return ( score > 0 );
    }
}
// Local Variables:
// tab-width: 4
// End:
