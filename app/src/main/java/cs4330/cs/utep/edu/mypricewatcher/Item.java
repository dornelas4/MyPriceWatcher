package cs4330.cs.utep.edu.mypricewatcher;

import android.util.Log;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

/**
 * @author Daniel Ornelas
 * @version 1.0
 * Item - Structure for items being tracked by the users
 */
public class Item {
    private String name;
    private String url;
    private double initialPrice;
    private double currentPrice;
    private String change;



    /**
     * Constructor
     * @param name Name of the item
     * @param initialPrice Initial Price of the item
     * @param url URL from website where the item is posted
     */
    public Item(String name,double initialPrice,String url){

            this.name = name;
            this.initialPrice = initialPrice;
            this.currentPrice = initialPrice;
            this.url = url;
            this.change =" ";



    }
    public Item(){
        
    }
    /**
     * Constructor
     * @param url url from item website
     */
    public Item(String url){

            this.name = "WEB ITEM";
            this.initialPrice = 20.0;
            this.currentPrice = 20.0;
            this.url = url;
            this.change = " ";

    }

    public Item(String name, String url){

            this.name = name;
            this.initialPrice = 20.0;
            this.currentPrice = 20.0;
            this.url = url;
            this.change = " ";


    }


    public void setUrl(String url) {

            this.url = url;


    }

    public void setName(String name){
        this.name = name;
    }

    public void setChange(){

        this.change = this.currentPrice > this.initialPrice ? "Increase" : "Decrease";
    }

    public void setInitialPrice(double initialPrice){
        this.initialPrice = initialPrice;
    }
    public String getChange(){
        return this.change;
    }
    /**
     *
     * @return the name of the Item
     */
    public String getName() {
        return this.name;
    }

    /**
     *
     * @return the item currentPrice
     */
    public double getCurrentPrice() {
        return this.currentPrice;
    }

    /**
     *
     * @return The item initial price
     */
    public double getInitialPrice() {
        return this.initialPrice;
    }


    /**
     *
     * @return item url
     */
    public String getUrl(){
        return this.url;
    }

    /**
     * Set new current price for item
     * @param currentPrice item current price
     */
    public void setCurrentPrice(double currentPrice){
        this.currentPrice = currentPrice;

    }

    /**
     *
     * @return the percentage change between current  price and initial price
     */
    public Double calculatePercentageChange(){
        return Math.floor(Math.abs(((this.currentPrice - this.initialPrice) / Math.abs(this.initialPrice)) * 100));
    }
}
