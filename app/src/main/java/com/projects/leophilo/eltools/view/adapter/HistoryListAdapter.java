package com.projects.leophilo.eltools.view.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.projects.leophilo.eltools.R;
import com.projects.leophilo.eltools.model.entity.HistoryResultEntity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryListAdapter extends BaseQuickAdapter<HistoryResultEntity, HistoryListAdapter.MyViewHolder> {

    public HistoryListAdapter(int layoutResId, @Nullable List<HistoryResultEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(MyViewHolder helper, HistoryResultEntity item) {
        try {
            String LELStr = item.getLEL() + "%";
            String UELStr = item.getUEL() + "%";
            String sumStr = item.getSum() + "%";
            Long timeStamp = item.getId();
            helper.textLEL.setText(LELStr);
            helper.textUEL.setText(UELStr);
            helper.textVF.setText(sumStr);

            Calendar calendar = GregorianCalendar.getInstance();
            calendar.setTimeInMillis(timeStamp);

            @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyy年M月d日k时m分");

            helper.textTS.setText(dateFormat.format(calendar.getTime()));
        } catch (NullPointerException e) {
            helper.textLEL.setText(R.string.tip_error);
            helper.textUEL.setText(R.string.tip_error);
            helper.textVF.setText(R.string.tip_error);
            helper.textTS.setText(R.string.tip_error);
        }
    }

    class MyViewHolder extends BaseViewHolder {

        @BindView(R.id.textLEL)
        TextView textLEL;
        @BindView(R.id.textUEL)
        TextView textUEL;
        @BindView(R.id.textVF)
        TextView textVF;
        @BindView(R.id.textTS)
        TextView textTS;
        @BindView(R.id.divider)
        View divider;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, itemView);
        }

    }

}
