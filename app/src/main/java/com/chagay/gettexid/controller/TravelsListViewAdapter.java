package com.chagay.gettexid.controller;

import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.chagay.gettexid.R;
import com.chagay.gettexid.model.entities.Travel;

import java.util.ArrayList;
import java.util.List;

public  class TravelsListViewAdapter extends ArrayAdapter<Travel> implements Filterable {


    private int listItemLayout;
    private Context context;
    private List<Travel> TravelList;
    private List<Travel> origTravelList;

    private LayoutInflater.Filter planetFilter;



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
        viewHolder.item1.setText("Destination: "+travel.getDestinetionCityName());
        viewHolder.item2.setText("Distance: "+ String.valueOf(calculateDistance(travel)) +" Km");
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
        if (planetFilter == null) //Filter claSS
            planetFilter = (LayoutInflater.Filter) new PlanetFilter();

        return (Filter) planetFilter;
    }


    private class PlanetFilter extends Filter {


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
                   List<Travel> nTravelList = new ArrayList<Travel>();
                    int j = 0;
                    for (Travel it : TravelList) {
                        j++;
                        if (it.getDestinetionCityName().toUpperCase().startsWith(constraint.toString().toUpperCase())) {
                            nTravelList.add(it);

                            int i = 3;
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

    private static int calculateDistance(Travel travel) {
        Location locationA = new Location("point A");
        double a = travel.getInitialLocationLongitude();
        double b = travel.getIntialLocationLatitude();
        locationA.setLatitude(b);
        locationA.setLongitude(a);

        Location locationB = new Location("point B");
        double a_ = travel.getDestinetionLongitude();
        double b_ = travel.getDestinetionLatitude();

        locationB.setLatitude(b_);
        locationB.setLongitude(a_);



        return Math.round(locationA.distanceTo(locationB)/1000);
    }

}
