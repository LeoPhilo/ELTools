package com.projects.leophilo.eltools.core;

import com.projects.leophilo.eltools.app.Elements;
import com.projects.leophilo.eltools.model.entity.NormalCompositionItemEntity;

import java.util.ArrayList;
import java.util.List;

public class Calculator {

    public static final String TEMPERATURE = "temperature";
    public static final String PRESSURE = "pressure";
    public static final int DecimalScale = 3;

    public static void calculate(
            final double temperature,
            final double pressure,
            final List<NormalCompositionItemEntity> what,
            final OnCalculateCallBack callBack) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //原始混合气体
                List<NormalCompositionItemEntity> copyList = new ArrayList<>(what);

                //原始混合气体总体积分数
                double sum = 100.0;
                //需要有空气基混合气体中按空气氮氧比例扣除的N2体积分数
                double airN = 0;
                //空气氮氧比例
                // FIXME: 2018/5/14 这里的比例要调整
                double AirNDO = 78.0 / 21;

                boolean hasO2 = false;
                boolean hasN2 = false;
                for (NormalCompositionItemEntity e : what) {
                    switch (e.getFormula()) {
                        case Elements.O2:
                            hasO2 = true;
                            //从混合气体成分中剔除氧气
                            sum = Arith.sub(sum, e.getValue());
                            //混合气体空气基中氮气所占体积分数
                            airN = Arith.mul(e.getValue(), AirNDO);
                            copyList.remove(e);
                            break;
                        case Elements.NobleGas.N2:
                            hasN2 = true;
                            NormalCompositionItemEntity copy = e.simpleCopy();
                            if (airN <= e.getValue()) {
                                sum = Arith.sub(sum, airN);
                                copy.setValue(Arith.sub(e.getValue(), airN));
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

                ELData data = calculateNoAir(copyList, sum);

                if (temperature > 25) {
                    data.LEL = Arith.mul(
                            data.LEL, Arith.sub(1, Arith.mul(
                                    0.0008, Arith.sub(temperature, 25)
                            ))
                    );
                }

                if (temperature > 25 || pressure > 0.1) {
                    data.UEL = Arith.mul(
                            Arith.add(
                                    data.UEL, Arith.mul(
                                            20.6, Math.log10(pressure) + 1)
                            ),
                            Arith.add(
                                    1, Arith.mul(
                                            0.0008, Arith.sub(temperature, 25)
                                    )
                            )
                    );
                }
                ELResult result = new ELResult(temperature, pressure, data, Arith.round(sum, DecimalScale));
                callBack.onResult(result);
            }
        }).start();
    }

    private static ELData calculateNoAir(List<NormalCompositionItemEntity> what, double sum) {
        List<NormalCompositionItemEntity> copyList = new ArrayList<>(what);
        double nobleSum = 0;
        for (NormalCompositionItemEntity e : what) {
            if (e.getType() == Elements.Type.NobleGas) {
                copyList.remove(e);
                sum = Arith.sub(sum, e.getValue());
                nobleSum = Arith.add(nobleSum, e.getValue());
            }
        }

        ELData data = calculateNoAirNoNoble(copyList, sum);
        double LEL = data.getLEL();
        double UEL = data.getUEL();
//        double noble = nobleSum == 100 ? 0 : nobleSum / (100 - nobleSum);
        double noble = nobleSum == 100 ? 0 : Arith.div(nobleSum, Arith.sub(100, nobleSum));
//        LEL = LEL * (1 + noble) * 100 / (100 + LEL * noble);
        LEL = Arith.div(
                Arith.mul(
                        Arith.mul(LEL,
                                Arith.add(1,
                                        noble)
                        ),
                        100),
                Arith.add(100,
                        Arith.mul(LEL,
                                noble)
                )
        );
//        UEL = UEL * (1 + noble) * 100 / (100 + UEL * noble);
        UEL = Arith.div(
                Arith.mul(
                        Arith.mul(UEL,
                                Arith.add(1,
                                        noble)
                        ),
                        100),
                Arith.add(100,
                        Arith.mul(UEL,
                                noble)
                )
        );
        copyList.clear();
        return new ELData(Arith.round(LEL, DecimalScale), Arith.round(UEL, DecimalScale));
    }

    private static ELData calculateNoAirNoNoble(List<NormalCompositionItemEntity> what, double sum) {
        double denominatorSum1 = 0;
        double denominatorSum2 = 0;
        for (NormalCompositionItemEntity e :
                what) {
//            denominatorSum1 += e.getValue() / (e.getLEL() * sum);
            denominatorSum1 = Arith.add(denominatorSum1,
                    Arith.div(e.getValue(),
                            Arith.mul(e.getLEL(),
                                    sum)
                    )
            );
//            denominatorSum2 += e.getValue() / (e.getUEL() * sum);
            denominatorSum2 = Arith.add(denominatorSum2,
                    Arith.div(e.getValue(),
                            Arith.mul(e.getUEL(),
                                    sum)
                    )
            );
        }
        return new ELData(
                denominatorSum1 == 0 ? 0 : Arith.div(1, denominatorSum1),
                denominatorSum2 == 0 ? 0 : Arith.div(1, denominatorSum2)
        );
    }

    public static class ELData {
        double LEL;
        double UEL;

        public ELData(double LEL, double UEL) {
            this.LEL = LEL;
            this.UEL = UEL;
        }

        public double getLEL() {
            return LEL;
        }

        public double getUEL() {
            return UEL;
        }
    }

    public static class ELResult {
        double temperature;
        double pressure;
        ELData date;
        double sum;

        public ELResult(double temperature, double pressure, ELData date, double sum) {
            this.temperature = temperature;
            this.pressure = pressure;
            this.date = date;
            this.sum = sum;
        }

        public double getTemperature() {
            return temperature;
        }

        public double getPressure() {
            return pressure;
        }

        public ELData getDate() {
            return date;
        }

        public double getSum() {
            return sum;
        }
    }

    public interface OnCalculateCallBack {
        void onResult(ELResult result);

        void onError(String description);
    }
}
