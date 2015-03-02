package com.aware.plugin.InOutdoor;

import java.util.LinkedList;

/**
 * Created by Armand on 26/02/2015.
 */
public class Singleton {
    private LinkedList inOutdoor = new LinkedList();
    private double[] linear = new double[3];
    private double[] accelerometer = new double[3];
    private double[] gyro = new double[3];
    private double temp;
    private double[] magnetic = new double[3];
    private double[] location = new double[5];
    private double baro;
    private double[] gravity = new double[3];
    private String wifi;
    private String battery;
    private ArbreBinaire tree;
    private InOutDoor inOut;

    private Singleton() {
    }

    /**
     * Point d'accès pour l'instance unique du singleton
     */
    public static Singleton getInstance() {
        return SingletonHolder.instance;
    }

    public LinkedList getInOutdoor() {
        return inOutdoor;
    }

    public void setInOutdoor(LinkedList inOutdoor) {
        this.inOutdoor = inOutdoor;
    }

    public double[] getLinear() {
        return linear;
    }

    public void setLinear(double[] linear) {
        this.linear = linear;
    }

    public double[] getAccelerometer() {
        return accelerometer;
    }

    public void setAccelerometer(double[] accelerometer) {
        this.accelerometer = accelerometer;
    }

    public double[] getGyro() {
        return gyro;
    }

    public void setGyro(double[] gyro) {
        this.gyro = gyro;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double[] getMagnetic() {
        return magnetic;
    }

    public void setMagnetic(double[] magnetic) {
        this.magnetic = magnetic;
    }

    public double[] getLocation() {
        return location;
    }

    public void setLocation(double[] location) {
        this.location = location;
    }

    public double getBaro() {
        return baro;
    }

    public void setBaro(double baro) {
        this.baro = baro;
    }

    public double[] getGravity() {
        return gravity;
    }

    public void setGravity(double[] gravity) {
        this.gravity = gravity;
    }

    public String getWifi() {
        return wifi;
    }

    public void setWifi(String wifi) {
        this.wifi = wifi;
    }

    public String getBattery() {
        return battery;
    }

    public void setBattery(String battery) {
        this.battery = battery;
    }

    public ArbreBinaire getTree() {
        return tree;
    }

    public void setTree(ArbreBinaire tree) {
        this.tree = tree;
    }

    public InOutDoor getInOut() {
        return inOut;
    }

    public void setInOut(InOutDoor inOut) {
        this.inOut = inOut;
    }

    /**
     * Holder
     */
    private static class SingletonHolder {
        /**
         * Instance unique non préinitialisée
         */
        private final static Singleton instance = new Singleton();
    }
}
