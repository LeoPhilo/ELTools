package com.projects.leophilo.eltools.app;

public interface Elements {

    interface Type {
        int Air = 0x0;
        int NobleGas = 0x1;
        int CombustibleGas = 0x2;
    }

    String O2 = "O2/";

    interface NobleGas {
        String N2 = "N2/";
        String CO2 = "CO2/";
        String H2O = "H2/O";
    }

    interface CombustibleGas {
        String CO = "CO";
        String H2 = "H2/";
        String H2S = "H2/S";
        String CH4 = "CH4/";
        String C2H6 = "C2/H6/";
        String C2H4 = "C2/H4/";
        String C3H8 = "C3/H8/";
        String C3H6 = "C3/H6/";
        String CmHn = C3H6;
    }

}
