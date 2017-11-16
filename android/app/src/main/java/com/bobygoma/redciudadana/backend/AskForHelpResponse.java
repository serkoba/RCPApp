package com.bobygoma.redciudadana.backend;

import com.google.gson.annotations.SerializedName;

/**
 * Created by daniel.streitenberger on 22/03/2017.
 */
public class AskForHelpResponse {

    @SerializedName("count")
    int count;

    public AskForHelpResponse(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }
}
