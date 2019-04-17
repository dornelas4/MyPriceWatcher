package cs4330.cs.utep.edu.mypricewatcher;

import android.content.Context;
import java.util.List;

/**
 * @Author : Daniel Orneals
 * Class to manage DatabaseItems , extends ItemManager
 * @see ItemManager
 */
public class DatabaseItemManager extends ItemManager {
    private ItemDatabaseHelper itemDatabaseHelper;

    /**
     * Constructor
     * @param ctx context where the class is instantiated
     */
    public DatabaseItemManager(Context ctx){

        super();
        itemDatabaseHelper = new ItemDatabaseHelper(ctx);


    }

    /**
     * Update item in database
     * @param i item to be updated
     */
    public void updateItem(DatabaseItem i){
        itemDatabaseHelper.updateItem((int)i.getID(),i);
    }

    /**
     * Add item to database
     * @param i item to be added
     */
    public void addItem(DatabaseItem i){
        super.addItem(i);
        i.setID(itemDatabaseHelper.addItem(i));

    }

    /**
     * Remove item from database
     * @param i item to be deleted
     */
    public void removeItem(DatabaseItem i){
        itemDatabaseHelper.deleteItem((int)i.getID());

    }

    /**
     * Get items from database
     * @return all items in database
     */
    public List<DatabaseItem> getAllItems(){
        return this.itemDatabaseHelper.getItems();

    }
}

