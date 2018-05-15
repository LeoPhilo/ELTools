package com.projects.leophilo.eltools.model.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "History")
public class HistoryResultEntity {

    @Id
    private Long id;
    private String temperature;
    private String pressure;
    private String LEL;
    private String UEL;
    private String sum;
    private String detail;
    @Generated(hash = 1795981070)
    public HistoryResultEntity(Long id, String temperature, String pressure,
            String LEL, String UEL, String sum, String detail) {
        this.id = id;
        this.temperature = temperature;
        this.pressure = pressure;
        this.LEL = LEL;
        this.UEL = UEL;
        this.sum = sum;
        this.detail = detail;
    }
    @Generated(hash = 1528940118)
    public HistoryResultEntity() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTemperature() {
        return this.temperature;
    }
    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }
    public String getPressure() {
        return this.pressure;
    }
    public void setPressure(String pressure) {
        this.pressure = pressure;
    }
    public String getLEL() {
        return this.LEL;
    }
    public void setLEL(String LEL) {
        this.LEL = LEL;
    }
    public String getUEL() {
        return this.UEL;
    }
    public void setUEL(String UEL) {
        this.UEL = UEL;
    }
    public String getSum() {
        return this.sum;
    }
    public void setSum(String sum) {
        this.sum = sum;
    }
    public String getDetail() {
        return this.detail;
    }
    public void setDetail(String detail) {
        this.detail = detail;
    }




}
