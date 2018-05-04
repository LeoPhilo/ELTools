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
    private float LEL;
    private float UEL;
    private float sum;
    private String detail;
    @Generated(hash = 729260748)
    public HistoryResultEntity(Long id, float LEL, float UEL, float sum,
            String detail) {
        this.id = id;
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
    public float getLEL() {
        return this.LEL;
    }
    public void setLEL(float LEL) {
        this.LEL = LEL;
    }
    public float getUEL() {
        return this.UEL;
    }
    public void setUEL(float UEL) {
        this.UEL = UEL;
    }
    public float getSum() {
        return this.sum;
    }
    public void setSum(float sum) {
        this.sum = sum;
    }
    public String getDetail() {
        return this.detail;
    }
    public void setDetail(String detail) {
        this.detail = detail;
    }

}
