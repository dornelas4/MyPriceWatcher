package cs4330.cs.utep.edu.mypricewatcher;

import android.content.Context;

import java.net.URL;

/**
 * Pricefinder strategy to find prices offline
 */
public class OfflinePriceFinder implements PriceFinder {

    /**
     *
     * @param item the url of the item to look up price
     * @return Random price for item
     */
    @Override
    public double findPrice(DatabaseItem item) {
        return Math.floor(Math.random() * ((60-10)+1)+10) /1;
    }
}
