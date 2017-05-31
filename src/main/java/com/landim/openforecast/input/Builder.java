package com.landim.openforecast.input;

import com.landim.openforecast.DataSet;

public interface Builder
{
    /**
     * Retrieves a DataSet - a collection of DataPoints - from the current
     * input source. The DataSet should contain all DataPoints defined by
     * the input source.
     * @return a DataSet built from the current input source.
     * @throws Exception if an error occurred reading from the input source.
     */
    public DataSet build()
        throws Exception;
}
// Local Variables:
// tab-width: 4
// End:
