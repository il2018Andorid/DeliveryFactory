package com.delivery.factory.deliveryfactory.ModelPOJO;

/**
 * Created by vimoxpc on 25-Feb-18.
 */

public class MenuListModel {

    String Name,Desciption,imageID;

    public MenuListModel(String name, String desciption, String imageID) {
        Name = name;
        Desciption = desciption;
        this.imageID = imageID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDesciption() {
        return Desciption;
    }

    public void setDesciption(String desciption) {
        Desciption = desciption;
    }

    public String getImageID() {
        return imageID;
    }

    public void setImageID(String imageID) {
        this.imageID = imageID;
    }
}
