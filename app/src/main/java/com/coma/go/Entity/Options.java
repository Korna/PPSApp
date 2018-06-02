package com.coma.go.Entity;

public class Options {

    private boolean receiveFcm;


    public Options(boolean receiveFcm) {
        this.receiveFcm = receiveFcm;
    }

    public boolean isReceiveFcm() {
        return receiveFcm;
    }



    public void setReceiveFcm(boolean receiveFcm) {
        this.receiveFcm = receiveFcm;
    }
}
