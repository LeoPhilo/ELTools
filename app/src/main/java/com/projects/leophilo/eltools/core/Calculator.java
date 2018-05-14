package com.projects.leophilo.eltools.core;

import com.projects.leophilo.eltools.app.Elements;
import com.projects.leophilo.eltools.model.entity.NormalCompositionItemEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Calculator {


    public static void calculate(final List<NormalCompositionItemEntity> what, final OnCalculateCallBack callBack) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //原始混合气体
                List<NormalCompositionItemEntity> copyList = new ArrayList<>(what);

                //原始混合气体总体积分数
                float sum = 100.0f;
                //需要有空气基混合气体中按空气氮氧比例扣除的N2体积分数
                float airN = 0f;
                //空气氮氧比例
                float AirNDO = 78.0f / 21;

                boolean hasO2 = false;
                boolean hasN2 = false;
                for (NormalCompositionItemEntity e : what) {
                    switch (e.getFormula()) {
                        case Elements.O2:
                            hasO2 = true;
                            //从混合气体成分中剔除氧气
                            sum -= e.getValue();
                            //混合气体空气基中氮气所占体积分数
                            airN = e.getValue() * AirNDO;
                            copyList.remove(e);
                            break;
                        case Elements.NobleGas.N2:
                            hasN2 = true;
                            NormalCompositionItemEntity copy = e.simpleCopy();
                            if (airN <= e.getValue()) {
                                sum -= airN;
                                copy.setValue(e.getValue() - airN);
                            } else {
                                callBack.onError("空气基氮气含量不足");
                                return;
                            }
                            copyList.remove(e);
                            copyList.add(copy);
                            break;
                    }
                }

                if (hasO2 && !hasN2) {
                    callBack.onError("空气基氮气含量不足");
                    return;
                }

                ELData result = calculateNoAir(copyList, sum);
                callBack.onResult(result, sum);
            }
        }).start();
    }

    private static ELData calculateNoAir(List<NormalCompositionItemEntity> what, float sum) {
        List<NormalCompositionItemEntity> copyList = new ArrayList<>(what);
        float nobleSum = 0;
        for (NormalCompositionItemEntity e : what) {
            if (e.getType() == Elements.Type.NobleGas) {
                copyList.remove(e);
                sum -= e.getValue();
                nobleSum += e.getValue();
            }
        }

        ELData data = calculateNoAirNoNoble(copyList, sum);
        float LEL = data.getLEL();
        float UEL = data.getUEL();
        float noble = nobleSum == 100 ? 0 : nobleSum / (100 - nobleSum);
        LEL = LEL * (1 + noble) * 100 / (100 + LEL * noble);
        UEL = UEL * (1 + noble) * 100 / (100 + UEL * noble);
        copyList.clear();
        return new ELData(LEL, UEL);
    }

    private static ELData calculateNoAirNoNoble(List<NormalCompositionItemEntity> what, float sum) {
        float denominatorSum1 = 0;
        float denominatorSum2 = 0;
        for (NormalCompositionItemEntity e :
                what) {
            denominatorSum1 += e.getValue() / (e.getLEL() * sum);
            denominatorSum2 += e.getValue() / (e.getUEL() * sum);
        }
        return new ELData(
                denominatorSum1 == 0 ? 0 : 1 / denominatorSum1
                , denominatorSum2 == 0 ? 0 : 1 / denominatorSum2);
    }

    public static class ELData {
        float LEL;
        float UEL;

        ELData(float LEL, float UEL) {
            this.LEL = LEL;
            this.UEL = UEL;
        }

        public float getLEL() {
            return LEL;
        }

        public float getUEL() {
            return UEL;
        }
    }

    public interface OnCalculateCallBack {
        void onResult(ELData result, float sum);

        void onError(String description);
    }
}
