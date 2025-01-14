/**
 * The Shop class controls the cost of the items in the Treasure Hunt game.<p>
 * The Shop class also acts as a go between for the Hunter's buyItem() method.<p>
 */
import java.awt.color.ICC_ColorSpace;
import java.util.Scanner;

public class Shop
{
    // constants
    private static int WATER_COST = 2;
    private static int ROPE_COST = 4;
    private static  int MACHETE_COST = 6;
    private static  int HORSE_COST = 12;
    private static int BOAT_COST = 20;

    // instance variables
    private double markdown;
    private Hunter customer;

    //Constructor
    public Shop(double markdown)
    {
        this.markdown = markdown;
        customer = null;
        if (TreasureHunter.cheatMode)
        {
            WATER_COST = 1;
            ROPE_COST = 1;
            MACHETE_COST = 1;
            HORSE_COST = 1;
            BOAT_COST = 1;
        }
        if (TreasureHunter.easyMode)
        {
            WATER_COST /= 2;
            ROPE_COST /= 2;
            MACHETE_COST /= 2;
            HORSE_COST /= 2;
            BOAT_COST /= 2;
        }
    }

    /** method for entering the shop
     * @param hunter  the Hunter entering the shop
     * @param buyOrSell  String that determines if hunter is "B"uying or "S"elling
     */
    public void enter(Hunter hunter, String buyOrSell)
    {
        customer = hunter;

        Scanner scanner = new Scanner(System.in);
        if (buyOrSell.equals("B") || buyOrSell.equals("b"))
        {
            System.out.println("Welcome to the shop! We have the finest wares in town.");
            System.out.println("Currently we have the following items:");
            System.out.println(inventory());
            System.out.print("What're you lookin' to buy? ");
            String itemChar = scanner.nextLine();
            String item = itemMapper(itemChar);
            int cost = checkMarketPrice(item, true);
            if (cost == 0)
            {
                System.out.println("We ain't got none of those.");
            }
            else
            {
                System.out.print("A " + item.toLowerCase() + " will cost you " + cost + " gold. Buy it (y/n)? ");
                String option = scanner.nextLine();

                if (option.equals("y") || option.equals("Y"))
                {
                    buyItem(item);
                }
            }
        }
        else
        {
            System.out.println("What're you lookin' to sell? ");
            System.out.println("You currently have the following items: " + customer.getInventory());
            String itemChar = scanner.nextLine();
            String item = itemMapper(itemChar);
            int cost = checkMarketPrice(item, false);
            if (cost == 0)
            {
                System.out.println("We don't want none of those.");
            }
            else
            {
                System.out.print("A " + item.toLowerCase() + " will get you " + cost + " gold. Sell it (y/n)? ");
                String option = scanner.nextLine();

                if (option.equals("y") || option.equals("Y"))
                {
                    sellItem(item);
                }
            }
        }
    }

    /** A method that returns a string showing the items available in the shop (all shops sell the same items)
     *
     * @return  the string representing the shop's items available for purchase and their prices
     */
    public String inventory()
    {
        String str = "";
            str = "(W)ater: " + WATER_COST + " gold\n";
            str += "(R)ope: " + ROPE_COST + " gold\n";
            str += "(M)achete: " + MACHETE_COST + " gold\n";
            str += "(H)orse: " + HORSE_COST + " gold\n";
            str += "(B)oat: " + BOAT_COST + " gold\n";
            return str;
    }

    private String itemMapper(String x)
    {
        x = x.toLowerCase();
        if (x.equals("w") || x.equals("water"))
        {
            return "Water";
        }
        if (x.equals("r") || x.equals("rope"))
        {
            return "Rope";
        }
        if (x.equals("m") || x.equals("machete"))
        {
            return "Machete";
        }
        if (x.equals("h") || x.equals("horse"))
        {
            return "Horse";
        }
        if (x.equals("b") || x.equals("boat"))
        {
            return "Boat";
        }
        else return x;
    }




    /**
     * A method that lets the customer (a Hunter) buy an item.
     * @param item The item being bought.
     */
    public void buyItem(String item)
    {
        int costOfItem = checkMarketPrice(item, true);
        if (customer.buyItem(item, costOfItem))
        {
            System.out.print("Ye' got yerself a " + item + ". Come again soon.");
        }
        else
        {
            System.out.print("Hmm, either you don't have enough gold or you've already got one of those!");
        }
    }

    /**
     * A pathway method that lets the Hunter sell an item.
     * @param item The item being sold.
     */
    public void sellItem(String item)
    {
        int buyBackPrice = checkMarketPrice(item, false);
        if (customer.sellItem(item, buyBackPrice))
        {
            System.out.print("Pleasure doin' business with you.");
        }
        else
        {
            System.out.print("Stop stringin' me along!");
        }
    }

    /**
     * Determines and returns the cost of buying or selling an item.
     * @param item The item in question.
     * @param isBuying Whether the item is being bought or sold.
     * @return The cost of buying or selling the item based on the isBuying parameter.
     */
    public int checkMarketPrice(String item, boolean isBuying)
    {
        if (isBuying)
        {
            return getCostOfItem(item);
        }
        else
        {
            return getBuyBackCost(item);
        }
    }



    /**
     * Checks the item entered against the costs listed in the static variables.
     *
     * @param item The item being checked for cost.
     * @return The cost of the item or 0 if the item is not found.
     */
    public int getCostOfItem(String item)
    {
        item = item.toLowerCase();
        if (item.equals("water"))
        {
            return WATER_COST;
        }
        else if (item.equals("rope"))
        {
            return ROPE_COST;
        }
        else if (item.equals("machete"))
        {
            return MACHETE_COST;
        }
        else if (item.equals("horse"))
        {
            return HORSE_COST;
        }
        else if (item.equals("boat"))
        {
            return BOAT_COST;
        }
        else
        {
            return 0;
        }
    }

    /**
     * Checks the cost of an item and applies the markdown.
     *
     * @param item The item being sold.
     * @return The sell price of the item.
     */
    public int getBuyBackCost(String item)
    {
        int cost;
        if (!TreasureHunter.cheatMode) {
            cost = (int) (getCostOfItem(item) * markdown);
            if (cost == 0)
            {
                cost = 1;
            }
        }
        else cost = 100;
        return cost;
    }
}