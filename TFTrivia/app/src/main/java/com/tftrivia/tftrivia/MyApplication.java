package com.tftrivia.tftrivia;

import android.app.Application;

/**
 * Created by Geometrix on 12/7/15.
 */
public class MyApplication extends Application {

    private String objectID;
    private String category;
    private boolean sound = true;

    public String getObjectID() {
        return objectID;
    }

    public String getCategory() {
        return category;
    }

    public boolean getSound() {
        return sound;
    }

    public void setObjectID(String someVariable) {
        this.objectID = someVariable;
    }

    public void setCategory(String someVariable) {
        this.category = someVariable;
    }

    public void setSound(boolean someVariable) {
        this.sound = someVariable;
    }
}