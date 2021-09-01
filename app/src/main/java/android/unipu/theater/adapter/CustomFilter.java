package android.unipu.theater.adapter;

import android.unipu.theater.model.TheaterModel;
import android.widget.Filter;

import java.util.ArrayList;

public class CustomFilter extends Filter {

    UserPerformanceAdapter adapter;
    ArrayList<TheaterModel> filterList;

    public CustomFilter(ArrayList<TheaterModel> filterList, UserPerformanceAdapter adapter){
        this.adapter = adapter;
        this.filterList = filterList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {

        FilterResults results = new FilterResults();

        if(constraint != null && constraint.length()>0){
            constraint = constraint.toString().toLowerCase();

            ArrayList<TheaterModel> filter = new ArrayList<>();
            for(int i=0; i<filterList.size(); i++){
                if(filterList.get(i).getNaziv().toLowerCase().contains(constraint)){
                    filter.add(filterList.get(i));
                }
            }
            results.count = filter.size();
            results.values = filter;
        }else{
            results.count = filterList.size();
            results.values = filterList;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
            adapter.naslov = (ArrayList<TheaterModel>)results.values;
            adapter.notifyDataSetChanged();
    }
}
