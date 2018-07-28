package com.example.akash.rxflightticket;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toolbar;

import com.example.akash.rxflightticket.networkUtils.ApiClient;
import com.example.akash.rxflightticket.networkUtils.ApiService;
import com.example.akash.rxflightticket.pojo.Price;
import com.example.akash.rxflightticket.pojo.Ticket;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String from = "DEL";
    private static final String to = "HYD";
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private CompositeDisposable disposable = new CompositeDisposable();
    private Unbinder unbinder;

    private ApiService apiService;
    private TicketsAdapter mAdapter;
    private ArrayList<Ticket> ticketsList = new ArrayList<>();
    private ConnectableObservable<List<Ticket>> ticketObservable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        apiService = ApiClient.getRetrofit().create(ApiService.class);

        mAdapter = new TicketsAdapter(this, ticketsList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        ticketObservable= apiService.getAirlineTickets(from, to)
                .toObservable()
                .subscribeOn(Schedulers.io())
                .replay();

        /* call to fetch the tickets without price and seat availability */

        disposable.add(ticketObservable
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribeWith(new DisposableObserver<List<Ticket>>(){
                                            @Override
                                            public void onNext(List<Ticket> tickets) {
                                                ticketsList.clear();
                                                ticketsList.addAll(tickets);
                                                mAdapter.notifyDataSetChanged();
                                            }

                                            @Override
                                            public void onError(Throwable e) {

                                            }

                                            @Override
                                            public void onComplete() {
                                                updatePrice();
                                            }
                                        }));
        ticketObservable.connect();
    }

    private void updatePrice(){
        /* we need to update price and seats
        1. Get observable for each ticket (can be achieved by using Observable.fromIterable(listContainingTickets))
        2. Use concatMap to combine this with priceObservable
     */
disposable.add(
        ticketObservable
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<List<Ticket>, ObservableSource<Ticket>>() {
                    @Override
                    public ObservableSource<Ticket> apply(List<Ticket> tickets) throws Exception {
                        return Observable.fromIterable(tickets);
                    }
                })
                .concatMap(new Function<Ticket, ObservableSource<Ticket>>() {
                    @Override
                    public ObservableSource<Ticket> apply(Ticket ticket) throws Exception {
                        return getPriceObservable(ticket);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Ticket>() {
                    @Override
                    public void onNext(Ticket ticket) {
                        int position = ticketsList.indexOf(ticket);

                        if (position == -1) {
                            return;
                        }

                        ticketsList.set(position, ticket);
                        mAdapter.notifyItemChanged(position);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                })
        );
    }


    private Observable<Ticket> getPriceObservable(final Ticket ticket){
        return apiService
                .getAirlineTicketPrice(ticket.getFlightNumber(), from, to)
                .toObservable()
                .map(new Function<Price, Ticket>() {
                    @Override
                    public Ticket apply(Price price) throws Exception {
                        ticket.setPrice(price);
                        return ticket;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
