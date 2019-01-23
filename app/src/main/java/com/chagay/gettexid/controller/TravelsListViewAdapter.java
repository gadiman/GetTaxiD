package com.chagay.gettexid.controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.location.Location;
import android.support.annotation.NonNull;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.chagay.gettexid.R;
import com.chagay.gettexid.model.backend.DB_Manager;
import com.chagay.gettexid.model.backend.FactoryMethod;
import com.chagay.gettexid.model.entities.Driver;
import com.chagay.gettexid.model.entities.Travel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public  class TravelsListViewAdapter extends ArrayAdapter<Travel> implements Filterable {


    private int listItemLayout;
    private Context context;
    private List<Travel> TravelList;
    private List<Travel> origTravelList;
    private Filter planetFilter;
    List<Travel> nTravelList;
    DB_Manager manager = FactoryMethod.getManager();



    public TravelsListViewAdapter(Context context_, int layoutId, List<Travel> travelList) {
        super(context_, layoutId, travelList);
        listItemLayout = layoutId;
        this.TravelList = travelList;
        this.origTravelList = travelList;
        this.context = context_;
    }

    public TravelsListViewAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    public int getCount() {
        return TravelList.size();
    }

    public Travel getItem(int position) {
        return TravelList.get(position);
    }


    @Override
    public long getItemId(int position) {
        return TravelList.get(position).hashCode();
    }


    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        Travel travel = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(listItemLayout, parent, false);

            viewHolder.item1 = (TextView) convertView.findViewById(R.id.row_item1);
            viewHolder.item2 = (TextView) convertView.findViewById(R.id.row_item2);


            convertView.setTag(viewHolder); // view lookup cache stored in tag
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Populate the data into the template view using the data object
        String Destination =String.format( "<b>" + "Destination: " + "</b> %s",travel.getDestinetionCityName());
        String Distance =String.format( "<b>" + "Distance: " + "</b> %s",String.valueOf(calculateDistance(travel)) +" Km");

        viewHolder.item1.setText(Html.fromHtml(Destination));
        viewHolder.item2.setText(Html.fromHtml(Distance));
        // Return the completed view to render on screen
        return convertView;
    }

    private static class ViewHolder {
        TextView item1;
        TextView item2;

    }

    public void resetData()
    {
        TravelList = origTravelList;
    }

    /*
     * We create our filter
     */

    @Override
    public Filter getFilter() {
        if (planetFilter == null) //Filter class
            planetFilter = new PlanetFilter();

        return (Filter) planetFilter;
    }


    private class PlanetFilter extends Filter  {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            // We implement here the filter logic
            if (constraint == null || constraint.length() == 0) {
                // No filter implemented we return all the list
                results.values = origTravelList;
                results.count = origTravelList.size();
            } else {
                // We perform filtering operation
                nTravelList = new ArrayList<Travel>();

                    for (Travel it : TravelList) {
                        if (it.getDestinetionCityName().toUpperCase().startsWith(constraint.toString().toUpperCase())) {
                            nTravelList.add(it);
                        }
                    }

                    results.values = nTravelList;
                    results.count = nTravelList.size();

            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            // Now we have to inform the adapter about the new list filtered
            if (results.count == 0)
                notifyDataSetInvalidated();
            else {
                TravelList = (List<Travel>) results.values;
                notifyDataSetChanged();
            }


        }


    }

    private  int calculateDistance(Travel travel) {

        Driver driver = manager.getCurrentDriver();
        double a = driver.getLongitude();
        double b = driver.getLatitude();


        Location locationA = new Location("point A");
        locationA.setLatitude(b);
        locationA.setLongitude(a);

        Location locationB = new Location("point B");
        double a_ = travel.getInitialLocationLongitude();
        double b_ = travel.getIntialLocationLatitude();

        locationB.setLatitude(b_);
        locationB.setLongitude(a_);



        return Math.round(locationA.distanceTo(locationB)/1000);
    }

}
