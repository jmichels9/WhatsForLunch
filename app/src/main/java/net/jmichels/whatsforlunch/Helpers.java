package net.jmichels.whatsforlunch;

import java.util.ArrayList;

/**
 * Created by Jay on 12/28/2014.
 */
public class Helpers {
    public static final String CLIENT_URL = "http://menu.unl.edu/";
    public static final String FETCHING_MENU = "Fetching menu...";

    public static final String CURRENT_BREAKFAST = "currentBreakfast";
    public static final String CURRENT_LUNCH = "currentLunch";
    public static final String CURRENT_DINNER = "currentDinner";

    public static final String MENU_UNAVAILABLE_MESSAGE = "Sorry, but there was a problem fetching" +
            " the menu. Meals might not be served at this time. Please try another date.";
    public static final String MENU_CLIENT_ERROR = "There was a problem with the client protocol.";
    public static final String MENU_POST_ERROR = "There was an issue with the POST request";
    public static final String MENU_LOADING_DATA = "Loading menu data...";

    public static final String CLIENT_PARAM_MONTH = "Month";
    public static final String CLIENT_PARAM_DAY = "Day";
    public static final String CLIENT_PARAM_COMPLEX = "gComplex";

    public static final String CLIENT_RESULT_BREAKFAST = "cphMain_pnlBreakfastItems";
    public static final String CLIENT_RESULT_LUNCH = "cphMain_pnlLunchItems";
    public static final String CLIENT_RESULT_DINNER = "cphMain_pnlDinnerItems";
    public static final String CLIENT_RESULT_UNAVAILABLE = "no menu is available";

    public static final String MENU_YEAR = "menuYear";
    public static final String MENU_MONTH = "menuMonth";
    public static final String MENU_DAY = "menuDay";

    public static final String DINING_HALL_POSITION = "diningHallPosition";
    public static final String FIRST_RUN = "firstRun";

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
