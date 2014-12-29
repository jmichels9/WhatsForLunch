package net.jmichels.whatsforlunch;

import java.util.ArrayList;

/**
 * Created by Jay on 12/28/2014.
 */
public class Helpers {

    public static final DiningHall selleck = new DiningHall(23, "Selleck");
    public static final DiningHall abel_sandoz = new DiningHall(22, "Abel/Sandoz");
    public static final DiningHall hss = new DiningHall(20, "Harper/Schramm/Smith");
    public static final DiningHall cpn = new DiningHall(24,"Cather/Pound/Neihardt");
    public static final DiningHall east_cafe = new DiningHall(101,"East Union Cafe & Grill");
    public static final DiningHall east_deli = new DiningHall(106,"East Union Corner Deli");

    // List of all the dining halls
    public static final DiningHall[] diningHalls = { selleck, abel_sandoz, hss, cpn, east_cafe, east_deli };

    public static ArrayList<String> getDiningHallNames() {
        ArrayList<String> diningHallNames = new ArrayList<>(6);
        for(DiningHall d : diningHalls) {
            diningHallNames.add(d.getName());
        }
        return diningHallNames;
    }
}
