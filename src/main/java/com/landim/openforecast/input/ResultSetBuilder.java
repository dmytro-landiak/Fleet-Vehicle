package com.landim.openforecast.input;


import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.landim.openforecast.DataPoint;
import com.landim.openforecast.DataSet;
import com.landim.openforecast.Observation;

public class ResultSetBuilder extends AbstractBuilder
{
     /**
      * Stores the result set from which this builder is to read its data.
      */
     private ResultSet rs;
     
     /**
      * Constructs a new ResultSetBuilder that reads its input from the given
      * ResultSet. The fields will be named according to the column labels
      * defined by the ResultSet's meta data.
      * @param resultSet the ResultSet containing data to be used to build the
      * DataSet.
      */
     public ResultSetBuilder( ResultSet resultSet )
     {
          this.rs = resultSet;
     }
     
     /**
      * Retrieves a DataSet - a collection of DataPoints - from the current
      * input source. The DataSet should contain all DataPoints defined by
      * the input source.
      *
      * <p>In general, build will attempt to convert all rows in the ResultSet
      * to data points. In this implementation, all columns are assumed to
      * contain numeric data. This restriction may be relaxed at a later date.
      * @return a DataSet built from the current input source.
      * @throws SQLException if a database access error occurs.
      */
     public DataSet build()
          throws SQLException
     {
          DataSet dataSet = new DataSet();
          
          setColumnNames();
          
          // Make sure we're on the first record
          if ( !rs.isBeforeFirst() )
                rs.beforeFirst();
          
          // Iterate through ResultSet,
          //  creating new DataPoint instance for each row
          while ( rs.next() )
                {
                     DataPoint dp = build( rs );
                     dataSet.add( dp );
                }
          
          return dataSet;
     }
     
     /**
      * Builds a DataPoint from the given row in the ResultSet. Assumes the
      * ResultSet has been positioned on the required row. This method does not
      * change the ResultSet, or the cursor within the ResultSet. The row is
      * expected/assumed to be made up of numeric fields only.
      * @param rs the ResultSet from which a row is to be read and used to
      * construct a new DataPoint.
      * @return a DataPoint object with values as specified by the current row
      * in the given ResultSet.
      * @throws SQLException if a database access error occurs.
      */
     private DataPoint build( ResultSet rs )
          throws SQLException
     {
          Observation dataPoint = new Observation( 0.0 );
          
          int n = getNumberOfVariables();
          for ( int column=0; column<n; column++ )
                {
                     // Treat all columns as numeric data
                     double value = rs.getDouble( column );
                     
                     // If this is the last value on the line, treat it
                     //  as the dependent variable value
                     if ( column == n )
                          dataPoint.setDependentValue( value );
                     else
                          dataPoint.setIndependentValue( getVariableName(column),
                                                                    value );
                }
          
          return dataPoint;
     }
     
     /**
      * Reads the column names from the meta data associated with the current
      * ResultSet, and initializes this object using those names.
      * @throws SQLException if a database access error occurs.
      */
     private void setColumnNames()
          throws SQLException
     {
          ResultSetMetaData metaData = rs.getMetaData();
          
          // Store names of independent variables. Last column is assumed
          //  to be the dependent variable value (we don't actually use that)
          int n = metaData.getColumnCount()-1;
          for ( int column=0; column<n; column++ )
                setVariableName( column, metaData.getColumnLabel(column) );
     }
}
// Local Variables:
// tab-width: 4
// End:
