package com.example.admin.testapp;

/**
 * Created by admin on 19.09.2017.
 */

public enum TrainType {
    International("Международные линии",R.drawable.international),
    InterregionalEconomy("Межрегиональные линии экономкласса",R.drawable.interregional_economy),
    RegionalBusiness("Межрегиональные линии бизнес-класса",R.drawable.regional_business),
    RegionalEconomy("Региональные линии экономкласса",R.drawable.regional_economy),
    NULL("NULL",R.drawable.null_train);

    private String title;
    private int resourceImage;


    TrainType(String title, int resourceImage) {
        this.title = title;
        this.resourceImage = resourceImage;
    }

    public String getTitle(){
        return this.title;
    }

    public int getResourceImage(){
        return this.resourceImage;
    }
}
