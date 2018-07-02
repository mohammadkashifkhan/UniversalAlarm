package com.mdkashif.alarm.battery.pojo;

public class BatteryStats {
    int level;
    float temp;
    String status;

    public BatteryStats(int level, float temp, String status) {
        this.level = level;
        this.temp = temp;
        this.status = status;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public float getTemp() {
        return temp;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
