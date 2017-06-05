package com.landim.crud;

import com.landim.entity.*;
import com.landim.persistence.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import com.landim.openforecast.Forecaster;
import com.landim.openforecast.ForecastingModel;
import com.landim.openforecast.DataSet;
import com.landim.openforecast.DataPoint;
import com.landim.openforecast.Observation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EnterPoint {
    private static SessionFactory factory = null;

    public static void main(String[] args) {
        CrudTrip crudTrip = new CrudTrip();
        List<Integer> arrayList = new ArrayList<Integer>();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        try {
            factory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }

        arrayList = crudTrip.selectForForecast(session, "Monday", "12-00", "LA-NY");
        System.out.println("\n\n\nCount of passengers : " + arrayList);


        DataSet observedData = new DataSet();
        DataPoint dp;
        double tmp = 0.0;

        for (Integer aList : arrayList){
            dp = new Observation(aList);
            dp.setIndependentValue("x", tmp);
            observedData.add(dp);
            tmp++;
        }


        System.out.println("Input data, observed values");
        System.out.println( observedData );

        // Obtain a good forecasting model given this data set
        ForecastingModel forecaster
                = Forecaster.getBestForecast( observedData );
        System.out.println("Forecast model type selected: "+forecaster.getForecastType());
        System.out.println( forecaster.toString() );


        // Create additional data points for which forecast values are required
        DataSet requiredDataPoints = new DataSet();
        for ( int count=4; count<5; count++ )
        {
            dp = new Observation( 0 );
            dp.setIndependentValue( "x", count );

            requiredDataPoints.add( dp );
        }

        // Dump data set before forecast
        System.out.println("Required data set before forecast");
        System.out.println( requiredDataPoints );

        // Use the given forecasting model to forecast values for
        //  the required (future) data points
        forecaster.forecast( requiredDataPoints );
        double generatedValue = 0.0;
        for (DataPoint dataPoint : requiredDataPoints) {
            generatedValue = dataPoint.getDependentValue();
        }
        int forecastedValue = (int) generatedValue;
        System.out.println( forecastedValue );
        // Output the results
        System.out.println("Output data, forecast values");
        System.out.println( requiredDataPoints );

        CrudRoute crudRoute = new CrudRoute();
        Route route = crudRoute.searchRoute(session,"LA-NY");
        CrudForecast crudForecast = new CrudForecast();
        crudForecast.addForecast("Monday", "12-00", forecastedValue, route);
        tx.commit();
        session.close();
    }
}