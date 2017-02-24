package com.android.test.model;

import android.os.Parcelable;

public abstract class AppBaseModel implements Parcelable {

    public AppBaseModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }


}
