package com.ads.sapp.ads;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

public class CommonAdConfig {

    //switch mediation use for app
    public static final int PROVIDER_ADMOB = 0;
    public static final int PROVIDER_MAX = 1;

    /**
     * config ad mediation using for app
     */
    private int mediationProvider = PROVIDER_ADMOB;

    private boolean isVariantDev = false;

    private String idAdResume;
    private List<String> listDeviceTest = new ArrayList();

    private Application application;
    private boolean enableAdResume = false;

    public void setMediationProvider(int mediationProvider){
        this.mediationProvider = mediationProvider;
    }

    public void setVariant(Boolean isVariantDev){
        this.isVariantDev = isVariantDev;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public int getMediationProvider() {
        return mediationProvider;
    }

    public Boolean isVariantDev() {
        return isVariantDev;
    }

    public String getIdAdResume() {
        return idAdResume;
    }

    public List<String> getListDeviceTest() {
        return listDeviceTest;
    }

    public void setListDeviceTest(List<String> listDeviceTest) {
        this.listDeviceTest = listDeviceTest;
    }

    public void setIdAdResume(String idAdResume) {
        this.idAdResume = idAdResume;
        enableAdResume =true;
    }

    public Boolean isEnableAdResume(){
        return enableAdResume;
    }

}
