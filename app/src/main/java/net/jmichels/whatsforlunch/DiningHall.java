package net.jmichels.whatsforlunch;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.TextView;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by Jay on 12/28/2014.
 */
public class DiningHall implements Serializable {

    private Integer id;
    private String name;
    private String shortName;

    public DiningHall(Integer id, String name, String shortName) {
        this.id = id;
        this.name = name;
        this.shortName = shortName;
    }

    public String getName() {
        return name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getShortName() {
        return shortName;
    }
}
