package com.projects.leophilo.eltools.tools;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.text.style.SubscriptSpan;

public class ChemicalTool {

    public static SpannableStringBuilder toChemicalFormula(String target) {

        if (TextUtils.isEmpty(target)) {
            throw new NullPointerException("目标字符串为空");
        }

        SpannableStringBuilder ssb = new SpannableStringBuilder();
        final String[] s1 = target.split("/");

        for (String ms : s1) {
            String[] s2 = ms.split("[^0-9]+");
            if (s2.length > 0) {
                ssb.append(ms.subSequence(0, ms.length() - s2[s2.length - 1].length()));
                int start = ssb.length();
                ssb.append(s2[s2.length - 1], new SubscriptSpan(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                ssb.setSpan(new RelativeSizeSpan(0.65f),start, ssb.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            } else {
                ssb.append(ms);
            }
        }

        return ssb;

    }

}
