package com.projects.leophilo.eltools.view.dialog;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.projects.leophilo.eltools.R;
import com.projects.leophilo.eltools.app.Elements;
import com.projects.leophilo.eltools.model.entity.NormalCompositionItemEntity;
import com.projects.leophilo.eltools.model.greendao.GreenDaoHelper;
import com.projects.leophilo.eltools.view.base.BaseDialogFragment;

import butterknife.BindView;
import butterknife.OnClick;

public class CreateItemDialog extends BaseDialogFragment {

    public static final String TAG = "CreateItemDialog";
    private boolean checkPass = false;
    private int type;

    @BindView(R.id.textFormula)
    EditText textFormula;
    @BindView(R.id.textLEL)
    EditText textLEL;
    @BindView(R.id.textUEL)
    EditText textUEL;
    @BindView(R.id.typeGroup)
    RadioGroup types;

    @OnClick(R.id.button_cancel)
    void cancel() {
        this.dismiss();
    }

    @OnClick(R.id.button_complete)
    void editComplete() {
        checkPass = false;
        if (textLEL.isEnabled() && textUEL.isEnabled()) {
            checkValue();
        } else {
            checkPass = true;
        }

        checkFormula();

        if (checkPass) {
            NormalCompositionItemEntity entity = new NormalCompositionItemEntity();
            entity.setFormula(textFormula.getText().toString());
            switch (types.getCheckedRadioButtonId()) {
                case R.id.type_air:
                    entity.setType(Elements.Type.Air);
                    break;
                case R.id.type_noble_gas:
                    entity.setType(Elements.Type.NobleGas);
                    break;
                case R.id.type_combustible_gas:
                    entity.setType(Elements.Type.CombustibleGas);
                    break;
            }
            entity.setLEL(Float.parseFloat(textLEL.getText().toString()));
            entity.setUEL(Float.parseFloat(textUEL.getText().toString()));

            GreenDaoHelper.getDaoSession().getNormalCompositionItemEntityDao().insertOrReplace(entity);
            Toast.makeText(getContext(), "已添加", Toast.LENGTH_SHORT).show();
            this.dismiss();
        } else {
            Toast.makeText(getContext(), "请检查数据", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkFormula() {
        checkPass = !TextUtils.isEmpty(textFormula.getText().toString());
    }

    private void checkValue() {
        float lel = Float.parseFloat(textLEL.getText().toString());
        float uel = Float.parseFloat(textUEL.getText().toString());

        if (inRange(lel) && inRange(uel) && lel < uel) {
            checkPass = true;
        }
    }

    private boolean inRange(float value) {
        return value > 0 && value < 100;
    }


    public static CreateItemDialog newInstance(String formula) {

        Bundle args = new Bundle();

        CreateItemDialog fragment = new CreateItemDialog();
        args.putString("FORMULA", formula);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void startEvent() {
        Bundle args = getArguments();

        if (null != args) {
            String formula = args.getString("FORMULA");
            textFormula.setText(formula);
        }

        types.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.type_air:
                    case R.id.type_noble_gas:
                        unableEditValue();
                        break;
                    case R.id.type_combustible_gas:
                        enableEditValue();
                        break;
                }
            }
        });
    }

    private void unableEditValue() {
        textLEL.setText(R.string.default_value);
        textUEL.setText(R.string.default_value);
        textLEL.setEnabled(false);
        textUEL.setEnabled(false);
    }

    private void enableEditValue() {
        textLEL.setText("");
        textUEL.setText("");
        textLEL.setEnabled(true);
        textUEL.setEnabled(true);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.dialog_create_new_item;
    }
}
