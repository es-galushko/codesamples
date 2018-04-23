package com.effectivesoft.expensesmanager.ui.activity.main.chartsbrowser.chart;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.effectivesoft.expensesmanager.R;
import com.effectivesoft.expensesmanager.data.model.chart.FullChartData;
import com.effectivesoft.expensesmanager.ui.adapter.charts.expandable.ItemBrowserExpandableRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ChartsFragment extends Fragment implements ChartsContract.ChartsMvpView {

    private static final String TAG_DATE = "dateTag";
    private static final String TAG_POSITION = "positionTag";

    @BindView(R.id.charts_progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.charts_recycler_view)
    RecyclerView timeIntervalRecyclerView;

    @BindView(R.id.tv_no_operation)
    TextView noOperationTextView;

    private Unbinder unbinder;

    private String tag;
    private int[] colors;
    private int[] percentColors;

    private ChartsPresenter presenter;

    public static ChartsFragment newInstance(String tag, int position) {
        ChartsFragment chartsFragment = new ChartsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TAG_POSITION, position);
        bundle.putString(TAG_DATE, tag);
        chartsFragment.setArguments(bundle);

        return chartsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tag = getTag(TAG_DATE);

        colors = new int[]{
                ContextCompat.getColor(getContext(), R.color.indicatorColor1),
                ContextCompat.getColor(getContext(), R.color.indicatorColor3),
                ContextCompat.getColor(getContext(), R.color.indicatorColor4),
                ContextCompat.getColor(getContext(), R.color.indicatorColor2)
        };

        percentColors = new int[]{
                ContextCompat.getColor(getContext(), R.color.positivePercentColor),
                ContextCompat.getColor(getContext(), R.color.negativePercentColor)
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_browser, container, false);
        unbinder = ButterKnife.bind(this, view);

        initRecyclerView();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        int position = getArguments().getInt(TAG_POSITION, -1);

        presenter = new ChartsPresenter();
        presenter.attachView(this);

        presenter.initCharts(tag, position, colors);
    }

    @Override
    public void setMenuVisibility(boolean hasMenu) {
        super.setMenuVisibility(hasMenu);
    }

    @Override
    public void showCharts(List<FullChartData> charts) {
        ItemBrowserExpandableRecyclerAdapter adapter = new ItemBrowserExpandableRecyclerAdapter(
                percentColors,
                charts
        );
        timeIntervalRecyclerView.setAdapter(adapter);
    }

    @Override
    public void showNoOperationText() {
        noOperationTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideNoOperationText() {
        noOperationTextView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.detachView();
    }

    private void initRecyclerView() {
        timeIntervalRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ItemBrowserExpandableRecyclerAdapter adapter = new ItemBrowserExpandableRecyclerAdapter(
                percentColors, new ArrayList<>()
        );
        timeIntervalRecyclerView.setAdapter(adapter);
    }

    private String getTag(String tag) {
        return getArguments().getString(tag);
    }
}
