package com.projects.leophilo.eltools.view.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.projects.leophilo.eltools.model.entity.NormalCompositionItemEntity;
import com.projects.leophilo.eltools.model.entity.NormalCompositionItemEntityDao;
import com.projects.leophilo.eltools.model.greendao.GreenDaoHelper;
import com.projects.leophilo.eltools.tools.ChemicalTool;

import org.greenrobot.greendao.query.QueryBuilder;

public class EditBarAutoAdapter extends CursorAdapter {

    private int columnIndex;

    public EditBarAutoAdapter(Context context, Cursor c) {
        super(context, c, true);
        this.columnIndex = NormalCompositionItemEntityDao.Properties.Formula.ordinal;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final TextView view = (TextView) inflater.inflate(android.R.layout.simple_dropdown_item_1line, parent, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ((TextView) view).setText(ChemicalTool.toChemicalFormula(cursor.getString(columnIndex)));
    }

    @Override
    public CharSequence convertToString(Cursor cursor) {
        return cursor.getString(columnIndex);
    }

    @Override
    public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
        if (constraint != null) {
            //Every time runQueryOnBackgroundThread was called will be in different Filter Thread
            //So do not Executing Queries multiple times
            //Otherwise we build CursorQuery each time
            QueryBuilder<NormalCompositionItemEntity> qb = GreenDaoHelper.getDaoSession()
                    .getNormalCompositionItemEntityDao()
                    .queryBuilder().where(
                            NormalCompositionItemEntityDao
                                    .Properties.Formula.like(constraint.toString() + "%")
                    ).limit(100);
            return qb.buildCursor().query();
            //从表train查询
        } else {
            return null;
        }
    }
}
