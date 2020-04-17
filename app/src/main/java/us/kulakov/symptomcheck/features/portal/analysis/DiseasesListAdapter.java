package us.kulakov.symptomcheck.features.portal.analysis;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import us.kulakov.symptomcheck.R;
import us.kulakov.symptomcheck.data.entities.Disease;

public class DiseasesListAdapter extends RecyclerView.Adapter<DiseasesListAdapter.ViewHolder> {
    private List<Disease> diseases;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.disease_title)
        TextView title;

        @BindView(R.id.disease_score)
        TextView score;

        public Disease disease;

        public ViewHolder(View root) {
            super(root);
            ButterKnife.bind(this, root);
        }

        @Override
        public void onClick(View v) {

        }
    }

    public DiseasesListAdapter(List<Disease> diseases) {
        this.diseases = diseases;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.diseases_item, parent, false);

        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.itemView.setOnClickListener(holder);
        Disease disease = diseases.get(position);
        holder.disease = disease;
        holder.title.setText(disease.name);
        holder.score.setText(String.format(Locale.US, "%.4f", disease.value));

    }

    @Override
    public int getItemCount() {
        return diseases.size();
    }
}

