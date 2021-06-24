package com.johan.view.spider;

import android.view.ViewStub;

public abstract class ViewStubSpider {

    protected ViewStub viewStub;
    protected boolean inflated;

    public ViewStubSpider(ViewStub viewStub) {
        this.viewStub = viewStub;
    }

    public boolean isInflated() {
        return inflated;
    }

    public abstract void inflate();

}
