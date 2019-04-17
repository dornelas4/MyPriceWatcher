package cs4330.cs.utep.edu.mypricewatcher;

import android.content.Context;

import java.net.URL;
import java.util.Random;
import java.util.concurrent.ExecutionException;

/**
 * @author Daniel Ornelas
 * @version 1.0
 * PriceFinder will look for an Item price and update it.
 */
public interface PriceFinder {
    /**
     * Constructor
     */


    /**
     * Generates a Random Price
     * @param item the url of the item to look up price
     * @return the updated item price
     */
    public double findPrice(DatabaseItem item) throws ExecutionException, InterruptedException;


}
