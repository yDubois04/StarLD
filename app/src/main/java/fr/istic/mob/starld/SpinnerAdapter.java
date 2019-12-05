package fr.istic.mob.starld;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fr.istic.mob.starld.fr.istic.mob.starld.model.BusRoute;

public class SpinnerAdapter extends BaseAdapter {

    List<BusRoute> buses;
    Context context;

    public SpinnerAdapter (ArrayList<BusRoute> buses, Context context) {
        this.buses = buses;
        this.context= context;
    }


    @Override
    public int getCount() {
        return buses.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View viewList =  View.inflate(context, R.layout.support_simple_spinner_dropdown_item, null);
        BusRoute bus = buses.get(i);

        int color = Color.parseColor("#"+bus.getColor());
        viewList.setBackgroundColor(color);

        TextView textView = (TextView) viewList;
        textView.setText(bus.getShortName());

        color = Color.parseColor("#"+bus.getTextColor());
        textView.setTextColor(color);

        return textView;
    }
}
