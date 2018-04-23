package com.effectivesoft.expensesmanager.ui.activity.main.chartsbrowser.chart;

import com.effectivesoft.expensesmanager.data.model.chart.FullChartData;
import com.effectivesoft.expensesmanager.data.source.local.ChartDataSource;
import com.effectivesoft.expensesmanager.data.source.local.ChartRepository;
import com.effectivesoft.expensesmanager.data.source.local.SmsDataSource;
import com.effectivesoft.expensesmanager.data.source.local.SmsRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.effectivesoft.expensesmanager.ui.activity.main.chartsbrowser.ChartsBrowserPresenter.TAG_MONTH;
import static com.effectivesoft.expensesmanager.ui.activity.main.chartsbrowser.ChartsBrowserPresenter.TAG_WEEK;
import static com.effectivesoft.expensesmanager.ui.activity.main.chartsbrowser.ChartsBrowserPresenter.TAG_YEAR;

public class ChartsPresenter implements ChartsContract.ChartsMvpPresenter {

    private ChartsContract.ChartsMvpView itemMvpView;

    private ChartDataSource chartDataSource;
    private SmsDataSource smsDataSource;
    private Disposable chartsDisposable;

    private String tag;
    private int position;
    private int[] colors;

    @Override
    public void attachView(ChartsContract.ChartsMvpView view) {
        chartDataSource = ChartRepository.getInstance();
        smsDataSource = SmsRepository.getInstance();
        itemMvpView = view;
    }

    @Override
    public void detachView() {
        itemMvpView = null;
        chartsDisposable.dispose();
    }

    @Override
    public void initCharts(String tag,
                           int position,
                           int[] colors) {
        this.tag = tag;
        this.position = position;
        this.colors = colors;

        smsDataSource.getDateLastSms()
                .flatMap(this::getChartsByDate)
                .map(this::getParentList)
                .map(this::sortCharts)
                .map(this::setColors)
                .toObservable()
                .flatMap(this::setChildListToParentList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ChartObserver());
    }

    private Single<List<List<FullChartData>>> getChartsByDate(Date date) {
        switch (tag) {
            case TAG_WEEK:
                return chartDataSource.getChartsAllWeek(date);
            case TAG_MONTH:
                return chartDataSource.getChartsAllMonth(date);
            case TAG_YEAR:
                return chartDataSource.getChartsAllYear(date);
            default:
                return Single.just(new ArrayList<List<FullChartData>>());
        }
    }

    private List<FullChartData> getParentList(List<List<FullChartData>> charts) {
        List<FullChartData> parentList = charts.get(position);
        if (parentList != null) {
            return parentList;
        } else {
            return new ArrayList<>();
        }
    }

    private List<FullChartData> sortCharts(List<FullChartData> parentList) {
        Collections.sort(parentList,
                (c1, c2) -> c2.getPayment().compareTo(c1.getPayment()));
        return parentList;
    }

    private List<FullChartData> setColors(List<FullChartData> parentList) {
        int i = 0;
        for (FullChartData fullChartData : parentList) {
            fullChartData.setColor(colors[(i++) % colors.length]);
        }
        return parentList;
    }

    private Observable<FullChartData> setChildListToParentList(List<FullChartData> parentList) {
        return Observable.fromIterable(parentList)
                .flatMap(fullChartData ->
                        chartDataSource.getPlacesByDate(
                                fullChartData.getFirstDate(),
                                fullChartData.getLastDate(),
                                fullChartData.getName())
                                .toObservable()
                                .map(placeChartData -> {
                                    fullChartData.setmChildrenList(placeChartData);
                                    return fullChartData;
                                }));
    }

    private class ChartObserver implements Observer<FullChartData> {

        private List<FullChartData> fullChartDataList;

        @Override
        public void onSubscribe(Disposable d) {
            chartsDisposable = d;
            fullChartDataList = new ArrayList<>();
            itemMvpView.hideNoOperationText();
        }

        @Override
        public void onNext(FullChartData fullChartData) {
            fullChartDataList.add(fullChartData);
        }

        @Override
        public void onError(Throwable e) {
            itemMvpView.hideProgressBar();
            itemMvpView.showNoOperationText();
        }

        @Override
        public void onComplete() {
            if (fullChartDataList.isEmpty()) {
                itemMvpView.showNoOperationText();
            } else {
                itemMvpView.showCharts(fullChartDataList);
            }
            itemMvpView.hideProgressBar();
        }
    }
}