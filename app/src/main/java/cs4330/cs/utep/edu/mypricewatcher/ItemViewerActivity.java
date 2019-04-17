package cs4330.cs.utep.edu.mypricewatcher;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author Daniel Ornelas
 * ItemViewerActivity - Activity displaying list of items being tracked
 * @see ItemAdapter - Inner class to inflate Item Listview
 */
public class ItemViewerActivity extends AppCompatActivity   {

    ListView listItem;
    ItemAdapter adapter;
    DatabaseItem selected;
    PriceFinder pf;
    DatabaseItemManager DIM;
    Context ctx = this;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //managers



        //pf = new OnlinePriceFinder(this);
        DIM = new DatabaseItemManager(this);

        //views
        setContentView(R.layout.activity_item_viewer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        adapter = new ItemAdapter(this,DIM.getAllItems());

        //get item from new item activity
        Intent i = getIntent();
        DatabaseItem newItem = new DatabaseItem();
        String name = i.getStringExtra("name");
        String url = i.getStringExtra("url");
        boolean editMode = i.getBooleanExtra("edit",false);
        int position = i.getIntExtra("position",-1);

        if(position != -1){
            newItem = adapter.getAdapterItems().get(position);
        }
        if(name != null){
            newItem.setName(name);
            if(url != null){
                ConnectivityManager cm =
                        (ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();
                newItem.setUrl(url);
                if(isConnected){
                    pf = new OnlinePriceFinder(ctx,adapter);
                    try {
                        pf.findPrice(newItem);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                }


            }
            if(editMode){
                DIM.updateItem(newItem);
                adapter.setItems(DIM.getAllItems());
                adapter.notifyDataSetChanged();
            }
            else{

                newItem.setChange();
                DIM.addItem(newItem);
                adapter.add(newItem);
            }
        }
        //listview set
        listItem = findViewById(R.id.list_item);
        listItem.setAdapter(adapter);
        listItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected = adapter.getAdapterItems().get(position);
                PopupMenu popup = new PopupMenu(ItemViewerActivity.this,listItem);
                popup.getMenuInflater().inflate(R.menu.item_popup_menu,popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch(item.getTitle().toString()){
                            case "Delete Item":
                                AlertDialog.Builder builder = new AlertDialog.Builder(ItemViewerActivity.this);
                                builder.setMessage(R.string.dialog_message).setTitle(R.string.dialog_title);
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        DIM.removeItem(selected);
                                        adapter.getAdapterItems().remove(selected);
                                        adapter.notifyDataSetChanged();
                                    }
                                });
                                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        return;
                                    }
                                });

                                AlertDialog dialog = builder.create();
                                dialog.show();

                                return true;
                            case "Refresh Price":
                                ConnectivityManager cm =
                                        (ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

                                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                                boolean isConnected = activeNetwork != null &&
                                        activeNetwork.isConnectedOrConnecting();
                                if(isConnected){
                                    pf = new OnlinePriceFinder(ctx,adapter);

                                    try {
                                        pf.findPrice(selected);
                                    } catch (ExecutionException e) {
                                        e.printStackTrace();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    DIM.updateItem(selected);
                                    adapter.notifyDataSetChanged();
                                }
                                else{
                                    startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                                }

                                return true;
                            case "View Webpage":
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(selected.getUrl().toString())));
                                return true;
                            case "Edit Item":
                                Intent i = new Intent(ItemViewerActivity.this,NewItemActivity.class);
                                i.putExtra("name",selected.getName());
                                i.putExtra("url",selected.getUrl());
                                i.putExtra("position",position);
                                startActivity(i);
                                return true;
                        }
                        return true;
                    }
                });

                popup.show();
            }
        });


    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()){
            case R.id.add_item_btn:

                startActivity(new Intent(this,NewItemActivity.class));
                break;


        }

        return super.onOptionsItemSelected(item);
    }



    /**
     *  class to inflate Item Listview and modify items
     */
    public static class ItemAdapter extends ArrayAdapter<DatabaseItem> {
        private List<DatabaseItem> items;
        protected TextView mview;
        protected View row;
        protected ViewGroup parent;
        protected Context ctx;

        public void setItems(List<DatabaseItem> items){
            this.items = items;
        }
        public List<DatabaseItem> getAdapterItems(){
            return this.items;
        }
        //private static ItemAdapter adapter_instance = null;

        /**
         * Private Constructor for ItemAdapter
         * @param ctx Context of parent class
         * @param items Lit of Item to be displayed by adapter
         */
        private ItemAdapter(Context ctx, List<DatabaseItem> items){
            super(ctx,-1,items);
            this.items = items;
            this.ctx = ctx;
        }




        /**
         *
         * @param position position of selected item
         * @param convertView view to inflate
         * @param parent parent ViewGroup
         * @return Inflated View of row
         */
        public View getView(int position, View convertView, ViewGroup parent){

            this.parent = parent;
            row = convertView != null ? convertView
                    : LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.items,parent,false);



            DatabaseItem item = items.get(position);

            //Use mview to display values in row
            mview = row.findViewById(R.id.item_name);
            mview.setText("Item Name: " +item.getName());
            mview = row.findViewById(R.id.initial_price);
            mview.setText("Initial Price: $" + Double.toString(item.getInitialPrice()));
            mview = row.findViewById(R.id.curr_price);
            mview.setText("Current Price: $" + Double.toString(item.getCurrentPrice()));
            mview = row.findViewById(R.id.price_change);
            mview.setText(Double.toString(item.calculatePercentageChange()) + "%");
            mview.setTextColor(Color.GREEN);
            if(item.getChange().equals("Increase")){
                mview.setTextColor(Color.RED);
            }
            return row;
        }
    }
}
