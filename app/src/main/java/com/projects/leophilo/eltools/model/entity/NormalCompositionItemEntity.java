package com.projects.leophilo.eltools.model.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.projects.leophilo.eltools.core.Arith;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Unique;

@Entity(nameInDb = "Normal")
public class NormalCompositionItemEntity implements Parcelable, Comparable<NormalCompositionItemEntity> {

    @Id
    private Long id;

    @Unique
    private String formula;
    private String name;
    private int type;
    private double value;
    private double LEL;
    private double UEL;
    @Generated(hash = 2129216352)
    public NormalCompositionItemEntity(Long id, String formula, String name, int type, double value, double LEL,
            double UEL) {
        this.id = id;
        this.formula = formula;
        this.name = name;
        this.type = type;
        this.value = value;
        this.LEL = LEL;
        this.UEL = UEL;
    }
    @Generated(hash = 1123578656)
    public NormalCompositionItemEntity() {
    }

    protected NormalCompositionItemEntity(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        formula = in.readString();
        name = in.readString();
        type = in.readInt();
        value = in.readDouble();
        LEL = in.readDouble();
        UEL = in.readDouble();
    }

    public static final Creator<NormalCompositionItemEntity> CREATOR = new Creator<NormalCompositionItemEntity>() {
        @Override
        public NormalCompositionItemEntity createFromParcel(Parcel in) {
            return new NormalCompositionItemEntity(in);
        }

        @Override
        public NormalCompositionItemEntity[] newArray(int size) {
            return new NormalCompositionItemEntity[size];
        }
    };

    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getFormula() {
        return this.formula;
    }
    public void setFormula(String formula) {
        this.formula = formula;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getType() {
        return this.type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public double getValue() {
        return this.value;
    }
    public void setValue(double value) {
        this.value = value;
    }
    public double getLEL() {
        return this.LEL;
    }
    public void setLEL(double LEL) {
        this.LEL = LEL;
    }
    public double getUEL() {
        return this.UEL;
    }
    public void setUEL(double UEL) {
        this.UEL = UEL;
    }



    public NormalCompositionItemEntity simpleCopy() {
        return new NormalCompositionItemEntity(
                null
                , this.getFormula()
                , null
                , this.getType()
                , this.getValue()
                , this.getLEL()
                , this.getUEL());
    }

    @Override
    public int compareTo(@NonNull NormalCompositionItemEntity o) {
        return -Arith.compareTo(value, o.getValue());
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(formula);
        dest.writeString(name);
        dest.writeInt(type);
        dest.writeDouble(value);
        dest.writeDouble(LEL);
        dest.writeDouble(UEL);
    }
}
