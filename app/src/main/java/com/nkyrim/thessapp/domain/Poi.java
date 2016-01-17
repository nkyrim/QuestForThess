package com.nkyrim.thessapp.domain;

import java.io.Serializable;

public class Poi implements Serializable {
    // Type constants
    public final static int TYPE_MONUMENT = 0;
    public final static int TYPE_MUSEUM = 1;
    public final static int TYPE_PARK = 2;

    private final int id;
    private final String title;
    private final String desc;
    private final String info;
    private final String imgUri;
    private final String imgSmallUri;
    private final String refUri;
    private final double lat;
    private final double lng;
    private final int type;

    public Poi(int id, String title, String desc, String info, String refUri, String imgUri,
               String imgSmallUri,double lat, double lng, int type) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.info = info;
        this.imgUri = imgUri;
        this.imgSmallUri = imgSmallUri;
        this.refUri = refUri;
        this.lat = lat;
        this.lng = lng;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public String getInfo() {
        return info;
    }

    public String getImgUri() {
        return imgUri;
    }

    public String getImgSmallUri() {
        return imgSmallUri;
    }

    public String getRefUri() {
        return refUri;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public int getType() {
        return type;
    }
}
