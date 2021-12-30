package com.codetutor.geolocationdemos.model;

public class DriverRegistrationModel {

    private String name;
    private int age;
    private String vehicleNumber;
    private int carryWater ;

    private String email;
    private String imageBase64;

    public DriverRegistrationModel() {
    }

    public DriverRegistrationModel(String name, int age, String vehicleNumber, int carryWater, String email, String imageBase64) {
        this.name = name;
        this.age = age;
        this.vehicleNumber = vehicleNumber;
        this.carryWater = carryWater;
        this.email = email;
        this.imageBase64 = imageBase64;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public int getCarryWater() {
        return carryWater;
    }

    public void setCarryWater(int carryWater) {
        this.carryWater = carryWater;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    @Override
    public String toString() {
        return "DriverRegistrationModel{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", vehicleNumber='" + vehicleNumber + '\'' +
                ", carryWater=" + carryWater +
                ", email='" + email + '\'' +
                ", imageBase64='" + imageBase64 + '\'' +
                '}';
    }
}
