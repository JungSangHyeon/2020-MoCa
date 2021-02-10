package com.onandon.moca.model;

import android.net.Uri;

import com.onandon.moca.Constant;

import java.io.Serializable;

public class MRingtone implements Serializable, Cloneable {

    // Attribute
    private boolean bChecked;
    private String name, uriString;
    private int volume;

    @Override
    public MRingtone clone() {
        try { return (MRingtone) super.clone(); }
        catch (CloneNotSupportedException e) { return null; }
    }

    // Constructor
    public MRingtone(){
        this.setChecked(true);
        this.setName("");
        this.setUri(null);
        this.setVolume(Constant.NotDefined);
    }

    // Getter & Setter
    public boolean isChecked() { return this.bChecked; }
    public void setChecked(boolean bOn) { this.bChecked = bOn; }
    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }
    public Uri getUri() {
        if (this.uriString == null) { return null; }
        else { return Uri.parse(this.uriString); }
    }
    public void setUri(Uri uri) {
        if (uri == null) { this.uriString = null; }
        else { this.uriString = uri.toString(); }
    }
    public int getVolume() { return this.volume; }
    public void setVolume(int volume) { this.volume = volume; }
}
