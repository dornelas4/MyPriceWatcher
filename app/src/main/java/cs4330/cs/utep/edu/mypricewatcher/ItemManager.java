package cs4330.cs.utep.edu.mypricewatcher;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Daniel Ornelas
 * Manage Items
 */
public class ItemManager {
    private List<Item> items;

    /**
     * Constructor
     * @param items item array
     */
    public ItemManager(List<Item> items){
        this.items = items;
    }

    /**
     * Constructor
     */
    public ItemManager(){
        this.items = new ArrayList<Item>();
    }

    /**
     * Add item
     * @param item to be added
     */
    public void addItem(Item item){
        this.items.add(item);
    }

    public void addItem(String name, String url){
        Item item = new Item(name,url);
        this.items.add(item);
    }

    /**
     * Delete item
     * @param item to be deleted
     * @return item succesfully deleted or not
     */
    public boolean deleteItem(Item item){
        if(items.contains(item)){
            items.remove(item);
            return true;
        }
        return false;
    }




}
