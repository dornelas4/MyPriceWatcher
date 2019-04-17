package cs4330.cs.utep.edu.mypricewatcher;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * @Author : Daniel Ornelas
 * Strategy pricefinder to find item prices onlune
 */
public class OnlinePriceFinder extends AsyncTask<Void,Void,Void> implements PriceFinder  {

    private Context ctx;
    private double currentPrice;
    private double initialPrice;
    private DatabaseItem item;
    private  ProgressDialog dialog ;
    private String textPrice;
    private String initTextPrice;

    private ItemViewerActivity.ItemAdapter adapter;

    private  final String HOME_DEPOT = "homedepot";
    private  final String AMAZON = "amazon";
    private  final String WALMART = "walmart";

    /**
     * Constructor
     * @param ctx context where it is called
     * @param adapter listview adapter
     */
   public OnlinePriceFinder(Context ctx,ItemViewerActivity.ItemAdapter adapter){
       this.ctx = ctx;

       this.adapter = adapter;
   }

    /**
     * Find price of item
     * @param item the url of the item to look up price
     * @return price updated

     */
    @Override
    public double findPrice(DatabaseItem item)  {
        this.item = item;
        this.dialog = new ProgressDialog(ctx);
        this.execute();
        return this.currentPrice;
    }

    /**
     * Sets loading dialog
     */
    @Override
    protected void onPreExecute(){
        super.onPreExecute();
        this.dialog.setMessage("Searching for current price");
        this.dialog.show();

    }


    /**
     * Looks for item prices
     * @param voids
     * @return
     */
    @Override
    protected Void doInBackground(Void... voids) {
        Document document;
        Elements elements = null;
        try{
            String url = item.getUrl();
            if(url.contains(HOME_DEPOT)){
                document = Jsoup.connect(url).cookie("zip","79902").get();
                elements = document.select("#ajaxPrice"); //Homedepot
                this.textPrice = elements.text();
                elements = document.select(".pStrikeThru");
                this.initTextPrice = elements.text();
            }
            else if (url.contains(AMAZON)){
                document = Jsoup.connect(url).timeout(0).get();
                elements = document.select("#priceblock_ourprice");//amazon
                this.textPrice = elements.text();
            }
            else if (url.contains(WALMART)){
                document = Jsoup.connect(url).timeout(0).get();
                elements = document.select(".price-group");//bestbuy
                this.textPrice = elements.text();
            }
            else{
                this.textPrice = "";


            }






        }
        catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Updates item prices and refresh GUI
     * @param aVoid
     */
    @Override
    protected void onPostExecute(Void aVoid){
        super.onPostExecute(aVoid);
        this.dialog.dismiss();
        String url = this.item.getUrl();
        if(url.contains(HOME_DEPOT)){
            String[] values = this.textPrice.split(" ");
            double dollars = Double.parseDouble(values[1]);
            double cents = Double.parseDouble(values[2])/100;
            this.currentPrice = dollars + cents;
            this.initTextPrice = this.initTextPrice.replace(",",(""));
            this.initialPrice = Double.parseDouble(initTextPrice.substring(1));
            Toast.makeText(ctx,"Done updating",Toast.LENGTH_LONG).show();
        }
        else if (url.contains(AMAZON)){
            this.currentPrice = Double.parseDouble(this.textPrice.substring(1));//amazon
            Toast.makeText(ctx,"Done updating",Toast.LENGTH_LONG).show();
        }
        else if (url.contains(WALMART)){
            String[] values = this.textPrice.split(" ");
            this.currentPrice = Double.parseDouble(values[0].substring(1));
            this.initialPrice = Double.parseDouble(values[2].substring(1));
            Toast.makeText(ctx,"Done updating",Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(ctx,"Store Not supported",Toast.LENGTH_LONG).show();
            this.currentPrice = 0.0;

        }


        if(this.initTextPrice == null){
            this.initialPrice = this.currentPrice;
        }



        item.setInitialPrice(this.initialPrice);
        item.setCurrentPrice(this.currentPrice);
        item.calculatePercentageChange();
        item.setChange();
        DatabaseItemManager d = new DatabaseItemManager(ctx);
        d.updateItem(item);
        adapter.notifyDataSetChanged();



    }
}
