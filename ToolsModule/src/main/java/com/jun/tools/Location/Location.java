package com.jun.tools.Location;

/**
 * Created by Jun on 2017/9/12.
 */


public class Location {
    double x, y, z;

    public Location(){

    }
    public Location(Location location){
        x = location.getX();
        y = location.getY();
        z = location.getZ();
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }
}
