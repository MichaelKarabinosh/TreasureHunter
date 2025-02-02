import java.util.Scanner;

/**
 * The Town Class is where it all happens.
 * The Town is designed to manage all of the things a Hunter can do in town.
 */
public class Town
{
    //instance variables
    private Hunter hunter;
    private Shop shop;
    private Terrain terrain;
    private String printMessage;
    private boolean toughTown;
    private boolean searchedForTreasure;
    private int goldWon;
    private int goldDiff;


    //Constructor
    /**
     * The Town Constructor takes in a shop and the surrounding terrain, but leaves the hunter as null until one arrives.
     * @param shop The town's shoppe.
     * @param toughness The surrounding terrain.
     */
    public Town(Shop shop, double toughness)
    {
        this.shop = shop;
        this.terrain = getNewTerrain();

        // the hunter gets set using the hunterArrives method, which
        // gets called from a client class
        hunter = null;

        printMessage = "";

        // higher toughness = more likely to be a tough town
        toughTown = (Math.random() < toughness);
    }

    public String getLatestNews()
    {
        return printMessage;
    }

    /**
     * Assigns an object to the Hunter in town.
     * @param hunter The arriving Hunter.
     */
    public void hunterArrives(Hunter hunter)
    {
        this.hunter = hunter;
        printMessage = "Welcome to town, " + hunter.getHunterName() + ".";

        if (toughTown)
        {
            printMessage += "\nIt's pretty rough around here, so watch yourself.";
        }
        else
        {
            printMessage += "\nWe're just a sleepy little town with mild mannered folk.";
        }
    }

    /**
     * Handles the action of the Hunter leaving the town.
     * @return true if the Hunter was able to leave town.
     */
    public boolean leaveTown()
    {
        boolean canLeaveTown = terrain.canCrossTerrain(hunter);
        if (canLeaveTown)
        {
            String item = terrain.getNeededItem();
            printMessage = "You used your " + item + " to cross the " + terrain.getTerrainName() + ".";
            if (checkItemBreak())
            {
                hunter.removeItemFromKit(item);
                printMessage += "\nUnfortunately, your " + item + " broke.";
            }

            return true;
        }

        printMessage = "You can't leave town, " + hunter.getHunterName() + ". You don't have a " + terrain.getNeededItem() + ".";
        return false;
    }

    public void enterShop(String choice)
    {
        printMessage = "";
        shop.enter(hunter, choice);
    }

    /**
     * Gives the hunter a chance to fight for some gold.<p>
     * The chances of finding a fight and winning the gold are based on the toughness of the town.<p>
     * The tougher the town, the easier it is to find a fight, and the harder it is to win one.
     */
    public void lookForTrouble()
    {
        double noTroubleChance;
        if (toughTown)
        {
            noTroubleChance = 0.66;
        }
        else
        {
            noTroubleChance = 0.33;
        }
        if (TreasureHunter.cheatMode || TreasureHunter.easyMode)
        {
            noTroubleChance = 1;
        }

        if (Math.random() > noTroubleChance)
        {
            printMessage = "You couldn't find any trouble";
        }
        else
        {
            noTroubleChance = 0.5;
            if (toughTown)
            {
                noTroubleChance = 0.66;
            }
            if (TreasureHunter.easyMode)
            {
                noTroubleChance = 0.3;
            }
            printMessage = "You want trouble, stranger!  You got it!\nOof! Umph! Ow!\n";
            goldDiff = (int)((int)(Math.random() * 10) + 1 * TreasureHunter.goldMulti);
            if (TreasureHunter.cheatMode)
            {
                goldDiff = 100;
                noTroubleChance = 0.00;
            }

            if (Math.random() > noTroubleChance)
            {
                printMessage += "Okay, stranger! You proved yer mettle. Here, take my gold.";
                printMessage += "\nYou won the brawl and receive " +  goldDiff + " gold.";
                hunter.changeGold(goldDiff);
            }
            else
            {
                printMessage += "That'll teach you to go lookin' fer trouble in MY town! Now pay up!";
                printMessage += "\nYou lost the brawl and pay " +  goldDiff + " gold.";
                hunter.changeGold(-1 * goldDiff);
            }
        }
    }
    public void lookForTroubleTest()
    {
        int brawlsWon = 0;
        int brawls = 100;
        int goldWonTotal = 0;
        for (int i = 0; i < 100; i++)
        {
            lookForTrouble();
            if (printMessage.contains("mettle"))
            {
                brawlsWon++;
                goldWonTotal += goldDiff;
        }
            if (printMessage.contains("couldn't"))
            {
                brawls--;
            }

        }
        System.out.println("brawls won: " + brawlsWon + " out of " + brawls);
        System.out.println("brawls lost: " +  (brawls - brawlsWon) + " out of " + brawls);
        System.out.println("gold won average: " + (double) goldWonTotal / brawlsWon);
    }

    public String toString()
    {
        return "This nice little town is surrounded by " + terrain.getTerrainName() + ".";
    }

    /**
     * Determines the surrounding terrain for a town, and the item needed in order to cross that terrain.
     *
     * @return A Terrain object.
     */
    private Terrain getNewTerrain()
    {
        double rnd = Math.random();
        if (rnd < .2)
        {
            return new Terrain("Mountains", "Rope");
        }
        else if (rnd < .4)
        {
            return new Terrain("Ocean", "Boat");
        }
        else if (rnd < .6)
        {
            return new Terrain("Plains", "Horse");
        }
        else if (rnd < .8)
        {
            return new Terrain("Desert", "Water");
        }
        else
        {
            return new Terrain("Jungle", "Machete");
        }
    }

    /**
     * Determines whether or not a used item has broken.
     * @return true if the item broke.
     */
    private boolean checkItemBreak()
    {
        double rand = Math.random();
        return (rand < 0.5);
    }

    public void setSearchedForTreasure(boolean bool)
    {
        searchedForTreasure = bool;
    }

    public void searchForTreasure() {
        printMessage = "";
        if (!searchedForTreasure) {
            int tNum = (int) (Math.random() * 100) + 1;
            if (tNum <= 25 - hunter.getLuckNum()) {
                printMessage = "You find nothing. Better luck in the next town!";
            }
            else {
                int whichTreasure = (int) (Math.random() * 3) + 1;
                if (whichTreasure == 1) {
                    if (!hunter.getInventory().contains("Necklace")) {
                        hunter.addItem("Necklace");
                        printMessage = "You find a necklace! It has been added to your inventory.";
                    } else {
                        printMessage = "You find a necklace! You already have a necklace in your inventory, so you discard this one.";
                    }
                }
                if (whichTreasure == 2) {
                    if (!hunter.getInventory().contains("Watch")) {
                        hunter.addItem("Watch");
                        printMessage = "You find a watch! It has been added to your inventory.";
                    } else {
                        printMessage = "You find a watch! You already have a watch in your inventory, so you discard this one.";
                    }
                }
                if (whichTreasure == 3) {
                    if (!hunter.getInventory().contains("Ring")) {
                        hunter.addItem("Ring");
                        printMessage = "You find a ring! It has been added to your inventory.";
                    } else {
                        printMessage = "You find a ring! You already have a ring in your inventory, so you discard this one.";
                    }
                }
            }
        }
        else {
            printMessage = "You cannot search for another treasure until you leave and go to the next town.";
        }
    }

    public void searchForTreasureTest() {
        int foundNun = 0;
        int foundN = 0;
        int foundW = 0;
        int foundR = 0;
        for (int i = 0; i < 100; i++) {
            int tNum = (int) (Math.random() * 100) + 1;
            if (tNum <= 25 - hunter.getLuckNum()) {
                foundNun++;
            }
            else {
                int whichT = (int) (Math.random() * 3) + 1;
                if (whichT == 1)
                {
                    foundW++;
                }
                if (whichT == 2)
                {
                    foundN++;
                }
                if (whichT == 3)
                {
                    foundR++;
                }
            }
        }
        System.out.println("Nothing found: " + foundNun);
        System.out.println("Necklace found: " + foundN);
        System.out.println("Watch found: " + foundW);
        System.out.println("Ring found: " + foundR );
        System.out.println("Luck number:" + hunter.getLuckNum());
    }


    public void luckyDice(Hunter hunter)
    {
        printMessage = "";
        if (!hunter.hunterHasGold(0))
        {
            System.out.println("You do not have any gold");
        }
        else {
            Scanner s = new Scanner(System.in);
            System.out.print("How much gold do you want to wager? ");
            String input = s.nextLine();
            int inputNum = Integer.parseInt(input);
            if (hunter.hunterHasGold(inputNum - 1))
            {
                hunter.changeGold(-inputNum);
                System.out.println("Pick a random number between 1 and 12.");
                int userDNum = Integer.parseInt(s.nextLine());
                int dice1 = (int)(Math.random() * 6) + 1;
                int dice2 = (int)(Math.random() * 6) + 1;
                int diceTotal = dice1 + dice2;
                if (TreasureHunter.cheatMode)
                {
                    diceTotal = userDNum;
                }
                if (userDNum == diceTotal)
                {
                    goldWon += inputNum * 2;
                    System.out.println("The number was " + diceTotal + ".");
                    System.out.print("Since you got the number spot on, you win " + inputNum * 2 + " gold!");
                    if (goldWon >= 10) {
                        hunter.changeLuckNum(((goldWon * 2) / 10));
                        goldWon = 0;
                    }
                    hunter.changeGold(inputNum * 2);
                }
                else if (diceTotal <= userDNum + 2 && diceTotal >= userDNum - 2)
                {
                    System.out.println("The number was " + diceTotal + ".");
                    System.out.print("Since you were within 2 of the number, you get your gold back!");
                    hunter.changeGold(inputNum);
                }
                else {
                    goldWon += -inputNum;
                    System.out.println("The number was " + diceTotal + ".");
                    if (goldWon <= -10) {
                        hunter.changeLuckNum((goldWon / 10) * 2);
                        goldWon = 0;
                    }
                    System.out.print("Since you were more than within 2 of the number, you lose your gold!");
                }
            }
            else {
                System.out.print("You cannot wager more than you have!");
            }
        }
    }
}


