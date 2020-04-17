package us.kulakov.symptomcheck.features.portal.features;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import timber.log.Timber;
import us.kulakov.symptomcheck.R;
import us.kulakov.symptomcheck.data.entities.Feature;
import us.kulakov.symptomcheck.data.model.response.endless.FeatureDetails;
import us.kulakov.symptomcheck.features.base.BaseFragment;
import us.kulakov.symptomcheck.injection.component.FragmentComponent;

public class FeaturesFragment extends BaseFragment implements FeaturesMvpView, FeatureActionListener {

    public static class DialogUtil {
        public static double value(int dialogProgress, FeatureDetails details) {
            return Math.max(details.min, Math.min(details.max, details.min + (double)dialogProgress * details.step));
        }

        public static int defaultProgress(FeatureDetails details, int totalSteps) {
            double range = details.max - details.min;
            if (range <= 0) return 0;

            double normalized = (details.defaultValue - details.min) / range;
            return (int)(totalSteps * normalized);
        }
    }

    @BindView(R.id.features_search)
    AutoCompleteTextView featuresSearch;

    @BindView(R.id.selected_features_recycler)
    RecyclerView featuresRecycler;

    private RecyclerView.Adapter featuresAdapter;

    @Inject
    FeaturesPresenter presenter;

    public static FeaturesFragment newInstance() {
        Bundle args = new Bundle();
//        TODO: Add arguments e.g. args.putParcelable(Constants.ID, id)
        FeaturesFragment fragment = new FeaturesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_features;
    }

    @Override
    protected void inject(FragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @Override
    protected void attachView() {
        presenter.attachView(this);
    }


    @Override
    protected void detachPresenter() {
        presenter.detachView();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter.getFeatures();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        featuresRecycler.setLayoutManager(layoutManager);
        featuresAdapter = new FeaturesListAdapter(presenter.getSavedFeatures(), this);
        featuresRecycler.setAdapter(featuresAdapter);
    }

    private void createAdapter(Feature[] features) {
        Context ctx = getActivity();
        if (ctx == null) return;

        ArrayAdapter<Feature> adapter = new ArrayAdapter<>(ctx,
                R.layout.autocomplete_item, R.id.autocomplete_text, features);
        featuresSearch.setThreshold(1);
        featuresSearch.setAdapter(adapter);
        featuresSearch.setOnItemClickListener((parent, view, position, id) -> {
            if (position >= parent.getCount()) return;

            Object obj = parent.getItemAtPosition(position);
            if (obj instanceof Feature) {
                Feature feature = (Feature)obj;
                presenter.selectFeature(feature);
                Timber.d(String.format("Feature %s selected", feature.toString()));
            }
        });
    }

    public void showDoubleDialog(Feature feature) {
        if (getActivity() == null) return;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(feature.friendly);

        final View customLayout = getLayoutInflater().inflate(R.layout.dialog_double, null);
        builder.setView(customLayout);

        builder.setNegativeButton("Cancel", (dialog, which) -> clearText());
        builder.setPositiveButton("OK", (dialog1, which) -> {
            SeekBar sb = customLayout.findViewById(R.id.dialog_seekbar);
            if (sb != null) {
                double val = DialogUtil.value(sb.getProgress(), feature.details);
                Timber.d("Progress: " + val);
                presenter.addDoubleFeature(feature, val);
            }
            clearText();
        });

        AlertDialog dialog = builder.create();
        dialog.show();

        SeekBar sb = dialog.findViewById(R.id.dialog_seekbar);
        TextView dialogValue = dialog.findViewById(R.id.dialog_value);

        if (sb != null && dialogValue != null) {
            double range = feature.details.max - feature.details.min;
            int steps = feature.details.step > 0 ? (int) (range / feature.details.step) : 50;

            sb.setMax(steps);
            sb.incrementProgressBy(1);

            int defaultValue = DialogUtil.defaultProgress(feature.details, steps);
            sb.setProgress(defaultValue);
            dialogValue.setText(String.format(Locale.US, "%.2f", feature.details.defaultValue));

            sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    double val = DialogUtil.value(progress, feature.details);
                    dialogValue.setText(String.format(Locale.US, "%.2f", val));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) { }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) { }
            });
        }
    }

    public void showIntegerDialog(Feature feature) {
        if (getActivity() == null) return;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(feature.friendly);

        final View customLayout = getLayoutInflater().inflate(R.layout.dialog_integer, null);
        builder.setView(customLayout);

        builder.setNegativeButton("Cancel", (dialog, which) -> clearText());
        builder.setPositiveButton("OK", (dialog1, which) -> {
            EditText et = customLayout.findViewById(R.id.dialog_edittext);
            if (et != null) {
                int val = Integer.parseInt(et.getText().toString());
                Timber.d("Progress: " + val);
                presenter.addIntegerFeature(feature, val);
            }
            clearText();
        });

        AlertDialog dialog = builder.create();
        dialog.show();

        EditText et = dialog.findViewById(R.id.dialog_edittext);

        if (et != null) {
            et.setFilters(new InputFilter[] { new InputFilterMinMax((int)feature.details.min, (int)feature.details.max)});
            et.setText(String.format(Locale.US, "%d", (int)feature.details.defaultValue));
        }
    }

    public void showMultiDialog(Feature feature, int selected, CharSequence[] options) {
        if (getActivity() == null) return;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(feature.friendly);

        builder.setSingleChoiceItems(options, selected, (dialog, which) -> {
            ListView lv = ((AlertDialog)dialog).getListView();
            lv.setTag(which);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> clearText());
        builder.setPositiveButton("OK", (dialog1, which) -> {
            ListView lv = ((AlertDialog)dialog1).getListView();
            Integer choice = (Integer)lv.getTag();

            presenter.addMultiFeature(feature, choice == null ? selected : choice);
            clearText();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void clearText() {
        featuresSearch.setText("");
    }

    @Override
    public void showError() {

    }

    @Override
    public void updateFeatures(Feature[] features) {
        createAdapter(features);
    }

    @Override
    public void updateSavedFeatures() {
        featuresAdapter.notifyDataSetChanged();
    }

    @Override
    public void deleteFeature(int position) {
        presenter.deleteSavedFeature(position);
    }
}
