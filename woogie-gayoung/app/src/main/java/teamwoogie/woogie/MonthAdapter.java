package teamwoogie.woogie;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import teamwoogie.woogie.model.MonthDiseaseData;

public class MonthAdapter  extends RecyclerView.Adapter<MonthAdapter.CustomViewHolder>{
    private ArrayList<MonthDiseaseData> mList = null;
    private Activity context = null;


    public MonthAdapter(Activity context, ArrayList<MonthDiseaseData> list) {
        this.context = context;
        this.mList = list;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView month;
        protected TextView diseaseName;
        protected TextView precaution;


        public CustomViewHolder(View view) {
            super(view);
            this.month= (TextView) view.findViewById(R.id.textView_list_month);
            this.diseaseName= (TextView) view.findViewById(R.id.textView_list_name);
            this.precaution = (TextView) view.findViewById(R.id.textView_list_precaution);
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

        viewholder.month.setText(mList.get(position).getMonth());
        viewholder.diseaseName.setText(mList.get(position).getDisease_name());
        viewholder.precaution.setText(mList.get(position).getDisease_precaution());
    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }

}
