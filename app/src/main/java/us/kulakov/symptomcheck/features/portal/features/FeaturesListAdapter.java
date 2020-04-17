package us.kulakov.symptomcheck.features.portal.features;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import us.kulakov.symptomcheck.R;
import us.kulakov.symptomcheck.features.portal.SavedFeature;

public class FeaturesListAdapter extends RecyclerView.Adapter<FeaturesListAdapter.ViewHolder> {
    private List<SavedFeature> savedFeatures;
    private FeatureActionListener actionListener;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.saved_feature_title)
        TextView savedFeatureTitle;

        @BindView(R.id.saved_feature_value)
        TextView savedFeatureValue;

        public SavedFeature savedFeature;

        public ViewHolder(View root) {
            super(root);
            ButterKnife.bind(this, root);
        }

        @OnClick(R.id.saved_feature_delete)
        public void onDelete() {
            actionListener.deleteFeature(getAdapterPosition());
        }

        @Override
        public void onClick(View v) {

        }
    }

    public FeaturesListAdapter(List<SavedFeature> savedFeatures, FeatureActionListener featureActionListener) {
        this.savedFeatures = savedFeatures;
        this.actionListener = featureActionListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_feature_item, parent, false);

        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.itemView.setOnClickListener(holder);
        SavedFeature sf = savedFeatures.get(position);
        holder.savedFeature = sf;
        holder.savedFeatureTitle.setText(sf.feature.friendly);
        holder.savedFeatureValue.setText(sf.value);

    }

    @Override
    public int getItemCount() {
        return savedFeatures.size();
    }
}

