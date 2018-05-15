package com.projects.leophilo.eltools.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.projects.leophilo.eltools.R;
import com.projects.leophilo.eltools.core.Arith;
import com.projects.leophilo.eltools.core.Calculator;
import com.projects.leophilo.eltools.tools.SPTool;
import com.projects.leophilo.eltools.view.base.BaseActivity;
import com.projects.leophilo.eltools.view.dialog.CreateItemDialog;
import com.projects.leophilo.eltools.view.fragment.MainFragment;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void startEvent() {
        if (toolbar != null) {
            toolbar.setTitle(R.string.app_name);
            toolbar.setTitleTextColor(Color.WHITE);
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commit();
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_base;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tool_bar_operations, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_operation_history:
                showHistory();
                break;
            case R.id.menu_operation_adjust:
                adjust();
                break;
//            case R.id.menu_operation_pressure:
//                adjustPressure();
//                break;
//            case R.id.menu_operation_temperature:
//                adjustTemperature();
//                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void adjust() {
        LayoutInflater inflater = getLayoutInflater();
        View contentView = inflater.inflate(R.layout.dialog_adjust, null, false);
        final EditText temperatureText = contentView.findViewById(R.id.editText_temperature);
        final EditText pressureText = contentView.findViewById(R.id.editText_pressure);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(contentView)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String inputTemperature = temperatureText.getText().toString();
                        String inputPressure = pressureText.getText().toString();
                        int checkState = checkInput(inputTemperature, inputPressure);
                        switch (checkState) {
                            case state_pass:
                                SharedPreferences.Editor editor = SPTool.getAppSP(getApplicationContext())
                                        .edit();
                                if (!TextUtils.isEmpty(inputTemperature)) {
                                    Double inputTemperatureValue = Double.parseDouble(inputTemperature);
                                    inputTemperature = Arith.round(inputTemperatureValue, Calculator.DecimalScale) + "";
                                    editor.putString(Calculator.TEMPERATURE, inputTemperature);
                                }
                                if (!TextUtils.isEmpty(inputPressure)) {
                                    Double inputPressureValue = Double.parseDouble(inputPressure);
                                    inputPressure = Arith.round(inputPressureValue, Calculator.DecimalScale) + "";
                                    editor.putString(Calculator.PRESSURE, inputPressure);
                                }
                                editor.apply();
                                break;
                            case state_temperature_error:
                            case state_pressure_error:
                                break;
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();
        if (temperatureText != null) {
            String currentTemperature = SPTool.getAppSP(this).getString(Calculator.TEMPERATURE, "25");
            String temperatureStr = "当前温度为" + currentTemperature +
                    "℃";
            temperatureText.setHint(temperatureStr);
        }
        if (pressureText != null) {
            String currentPressure = SPTool.getAppSP(this).getString(Calculator.PRESSURE, "0.1");
            String pressureStr = "当前压力为" + currentPressure +
                    "Mpa";
            pressureText.setHint(pressureStr);
        }
        dialog.show();
    }

    private final int state_pass = 0x0;
    private final int state_temperature_error = 0x1;
    private final int state_pressure_error = 0x2;

    private int checkInput(String inputTemperature, String inputPressure) {
        if (!TextUtils.isEmpty(inputTemperature)) {
            Double inputTemperatureValue = Double.parseDouble(inputTemperature);
            if (inputTemperatureValue < 25) {
                showToast("最低温度为25℃");
                return state_temperature_error;
            }
        }
        if (!TextUtils.isEmpty(inputPressure)) {
            Double inputPressureValue = Double.parseDouble(inputPressure);
            if (inputPressureValue < 0.1) {
                showToast("最小压力为0.1Mpa");
                return state_pressure_error;
            }
        }
        return state_pass;
    }

    private void showToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(
                        MainActivity.this
                        , msg
                        , Toast.LENGTH_SHORT).show();
            }
        });
    }

//    private void adjustTemperature() {
//
//    }
//
//    private void adjustPressure() {
//
//    }

    public void showHistory() {
        Intent intent = new Intent(this, HistoryActivity.class);
        startActivity(intent);
    }


}
