package teamwoogie.woogie;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import teamwoogie.woogie.model.DiseaseRecordData;


public class DiseaseRecordAdapter extends RecyclerView.Adapter<DiseaseRecordAdapter.CustomViewHolder>{
    private ArrayList<DiseaseRecordData> mList = null;
    private Activity context = null;


    public DiseaseRecordAdapter(Activity context, ArrayList<DiseaseRecordData> list) {
        this.context = context;
        this.mList = list;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView diseaseName;
        protected TextView date;


        public CustomViewHolder(View view) {
            super(view);
            this.diseaseName= (TextView) view.findViewById(R.id.textView_list_name);

            this.date = (TextView) view.findViewById(R.id.textView_list_precaution);
        }
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {

        viewholder.diseaseName.setText(mList.get(position).getDisease_name());
        viewholder.date.setText(mList.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }

}