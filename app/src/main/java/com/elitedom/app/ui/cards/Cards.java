package com.elitedom.app.ui.cards;

import android.net.Uri;

class Cards {
    // Member variables representing the title and information about the sport.
    private String title;
    private String info;
    private final String imageResource;

    Cards(String title, String info, String imageResource) {
        this.title = title;
        this.info = info;
        this.imageResource = imageResource;
    }

    String getTitle() {
        return title;
    }

    String getInfo() {
        return info;
    }

    Uri getImageResource() { return Uri.parse(imageResource); }
}