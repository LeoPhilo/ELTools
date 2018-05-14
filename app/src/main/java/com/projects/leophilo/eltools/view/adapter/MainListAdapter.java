package com.projects.leophilo.eltools.view.adapter;

import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.projects.leophilo.eltools.R;
import com.projects.leophilo.eltools.app.Elements;
import com.projects.leophilo.eltools.model.entity.NormalCompositionItemEntity;
import com.projects.leophilo.eltools.tools.ChemicalTool;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainListAdapter extends BaseQuickAdapter<NormalCompositionItemEntity, MainListAdapter.MyViewHolder> {

    public MainListAdapter(int layoutResId, @Nullable List<NormalCompositionItemEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(MyViewHolder helper, NormalCompositionItemEntity item) {
        try {
            String name = item.getFormula();
            String value = item.getValue() + "%";
            SpannableStringBuilder formula = ChemicalTool.toChemicalFormula(name);
            helper.formula.setText(formula);
            helper.value.setText(value);
            if (null != helper.logoBg && null != helper.logoText && null != helper.deleteBtn) {
                if (helper.logoText.getVisibility() == View.GONE) {
                    helper.logoText.setVisibility(View.VISIBLE);
                    helper.deleteBtn.setVisibility(View.GONE);
                }
                switch (item.getType()) {
                    case Elements.Type.Air:
                        helper.logoBg.setBackgroundResource(R.drawable.bg_air);
                        helper.logoText.setText("空");
                        break;
                    case Elements.Type.NobleGas:
                        helper.logoBg.setBackgroundResource(R.drawable.bg_noble_gas);
                        helper.logoText.setText("惰");
                        break;
                    case Elements.Type.CombustibleGas:
                        helper.logoBg.setBackgroundResource(R.drawable.bg_combustible_gas);
                        helper.logoText.setText("燃");
                        break;
                }
            }
        } catch (NullPointerException e) {
            helper.formula.setText(R.string.tip_error);
            helper.value.setText(R.string.tip_error);
        }
    }

    class MyViewHolder extends BaseViewHolder {

        @BindView(R.id.tagGasComposition)
        TextView formula;
        @BindView(R.id.valueVolumeFraction)
        TextView value;
        @Nullable
        @BindView(R.id.logo_img)
        ImageView logoBg;
        @Nullable
        @BindView(R.id.logo_txt)
        TextView logoText;
        @Nullable
        @BindView(R.id.button_delete)
        ImageButton deleteBtn;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, itemView);
        }

    }

}
