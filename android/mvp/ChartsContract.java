package com.effectivesoft.expensesmanager.ui.activity.main.chartsbrowser.chart;

import com.effectivesoft.expensesmanager.data.model.chart.FullChartData;
import com.effectivesoft.expensesmanager.ui.MvpPresenter;
import com.effectivesoft.expensesmanager.ui.MvpView;

import java.util.List;

public interface ChartsContract {

    interface ChartsMvpPresenter extends MvpPresenter<ChartsMvpView> {

        void initCharts(String tag, int position, int[] colors);
    }

    interface ChartsMvpView extends MvpView {

        void showCharts(List<FullChartData> charts);

        void showNoOperationText();

        void hideNoOperationText();

        void hideProgressBar();
    }
}
