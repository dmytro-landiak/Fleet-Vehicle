package com.landim.openforecast.models;

import java.util.ArrayList;


final class Utils
{
    /**
     * A private default constructor to prevent instantiation of this class.
     */
    private Utils()
    {
    }

    /**
     * Implements a Gaussian elimination on the given matrix. The last column
     * being the Right Hand Side values. All rows in the matrix are used.
     * @param a the matrix to be solved.
     */
    static double[] GaussElimination( double a[][] )
    {
        int n = a.length;

        return GaussElimination( n, a );
    }

    /**
     * Implements a Gaussian elimination on the given matrix. The matrix
     * <code>a</code> should be n rows by n+1 columns. Column <code>n+1</code>
     * being the Right Hand Side values.
     * @param n the number of rows in the matrix.
     * @param a the matrix to be solved.
     */
    static double[] GaussElimination( int n, double a[][] )
    {
        // Forward elimination
        for ( int k=0; k<n-1; k++ )
            {
                for ( int i=k+1; i<n; i++ )
                    {
                        double qt = a[i][k] / a[k][k];
                        for ( int j=k+1; j<n+1; j++ )
                            a[i][j] -= qt * a[k][j];

                        a[i][k] = 0.0;
                    }
            }

        /*
        // DEBUG
        for ( int i=0; i<n; i++ )
            for ( int j=0; j<n+1; j++ )
                System.out.println( "After forward elimination, a["+i+"]["+j+"]="+a[i][j] );
        */

        double x[] = new double[n];

        // Back-substitution
        x[n-1] = a[n-1][n] / a[n-1][n-1];
        for ( int k=n-2; k>=0; k-- )
            {
                double sum = 0.0;
                for ( int j=k+1; j<n; j++ )
                    sum += a[k][j]*x[j];
                
                x[k] = ( a[k][n] - sum ) / a[k][k];
            }

        /*
        // DEBUG
        for ( int k=0; k<n; k++ )
            System.out.println( "After back-substitution, x["+k+"]="+x[k] );
        */

        return x;
    }

    static double[] calculateSeasonalIndices( double observation[],
                                              int seasonalCycle )
    {
        int numberOfCycles = observation.length / seasonalCycle;
        if ( numberOfCycles < 2 )
            throw new IllegalArgumentException("Too few observations. Need at least "+seasonalCycle*2+" observations - preferably more - to calculate the seasonal indices");

        if ( seasonalCycle % 2 != 0 )
            throw new IllegalArgumentException("seasonalCycle must be even - for now at least");

        if ( observation.length % seasonalCycle > 0 )
            numberOfCycles++;

        double seasonalIndex[] = new double[ seasonalCycle ];


        // Calculate "Ratio to Moving Average" for each period and cycle
        ArrayList<Double> ratioToMovingAverage = new ArrayList<Double>( observation.length );

        double movingTotal = 0.0;
        for ( int t=0; t < seasonalCycle; t++ )
            {
                movingTotal += observation[t];
                ratioToMovingAverage.add(t,null);
            }
                
        int numberOfObservations = observation.length;
        for ( int t=seasonalCycle; t < numberOfObservations; t++ )
            {
                double movingAverage = movingTotal / (seasonalCycle*2);

                // For efficiency (to avoid recalculating sums), we
                // drop the oldest observation, and add the current one
                movingTotal -= observation[t-seasonalCycle];
                movingTotal += observation[t];

                movingAverage += movingTotal / (seasonalCycle*2);

                // The "+1" below is to correctly handle an odd number of
                // seasons within a cycle - e.g. 7 days in a week.
                int period = t - (seasonalCycle+1)/2;
                ratioToMovingAverage.add(period,
                            new Double(observation[period]/movingAverage));
            }

        // if more than 4 cycles, then drop min/max outliers
        // else we'll just average what we have
        boolean dropOutliers = (numberOfCycles > 4);

        // Calculate mean indices
        double sumIndices = 0.0;
        for ( int season=0; season<seasonalCycle; season++ )
            {
                int count = 0;
                double sum = 0.0;
                double minIndex = Double.POSITIVE_INFINITY;
                double maxIndex = Double.NEGATIVE_INFINITY;
                for ( int cycle=0; cycle<numberOfCycles; cycle++ )
                    {
                        int t = season + cycle*seasonalCycle;
                        if ( ratioToMovingAverage.get(t) != null )
                            {
                                double currentIndex
                                    = ((Double)ratioToMovingAverage.get(t)).doubleValue();

                                if ( dropOutliers )
                                    {
                                        if ( currentIndex < minIndex )
                                            minIndex = currentIndex;

                                        if ( currentIndex > maxIndex )
                                            maxIndex = currentIndex;
                                    }

                                sum += currentIndex;
                                count++;
                            }
                    }
                
                if ( dropOutliers )
                    {
                        sum -= minIndex;
                        count--;
                        
                        sum -= maxIndex;
                        count--;
                    }

                seasonalIndex[season] = sum / count;
                
                sumIndices += seasonalIndex[season];
            }
          
        // Scale indices to sum to seasonalCycle
        for ( int season=0; season<seasonalCycle; season++ )
            seasonalIndex[season] *= (seasonalCycle/sumIndices);

        return seasonalIndex;
    }
}
// Local Variables:
// tab-width: 4
// End:
