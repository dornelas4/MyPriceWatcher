/**
 * @Author : Daniel Ornelas
 * Structure for items stored in database. Inherits item
 * @see Item
 */
package cs4330.cs.utep.edu.mypricewatcher;





public class DatabaseItem extends Item {
    private long ID;

    /**
     * Constructor
     * @param name name of item
     * @param url url of item
     * @param ID id in database
     */
    public DatabaseItem(String name, String url, int ID){
        super(name,url);
        this.ID = ID;
    }

    /**
     * Constructor
     */
    public DatabaseItem(){
        super();
    }

    /**
     *
     * @return item ID
     */
    public long getID(){
        return this.ID;
    }

    /**
     * Set Item ID
     * @param ID to be set
     */
    public void setID(long ID){
        this.ID = ID;
    }
}
