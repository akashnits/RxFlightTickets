package com.example.akash.rxflightticket;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.akash.rxflightticket.pojo.Ticket;
import com.github.ybq.android.spinkit.SpinKitView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TicketsAdapter extends RecyclerView.Adapter<TicketsAdapter.TicketViewHolder> {

    private Context context;
    private List<Ticket> ticketList;

    public TicketsAdapter(Context context, List<Ticket> ticketList) {
        this.context = context;
        this.ticketList = ticketList;
    }

    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ticket_row, viewGroup, false);
        return new TicketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder ticketViewHolder, int i) {

        final Ticket ticket = ticketList.get(i);

        Glide.with(context)
                .load(ticket.getAirline().getLogo())
                .apply(RequestOptions.circleCropTransform())
                .into(ticketViewHolder.logo);

        ticketViewHolder.airlineName.setText(ticket.getAirline().getName());

        ticketViewHolder.departure.setText(ticket.getDeparture() + " Dep");
        ticketViewHolder.arrival.setText(ticket.getArrival() + " Dest");

        ticketViewHolder.duration.setText(ticket.getFlightNumber());
        ticketViewHolder.duration.append(", " + ticket.getDuration());
        ticketViewHolder.numberOfStops.setText(ticket.getStops() + " Stops");

        if (!TextUtils.isEmpty(ticket.getInstructions())) {
            ticketViewHolder.duration.append(", " + ticket.getInstructions());
        }

        if (ticket.getPrice() != null) {
            ticketViewHolder.price.setText("₹" + String.format("%.0f", ticket.getPrice().getPrice()));
            ticketViewHolder.numberOfSeats.setText(ticket.getPrice().getSeats() + " Seats");
            ticketViewHolder.loader.setVisibility(View.INVISIBLE);
        } else {
            ticketViewHolder.loader.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return ticketList.size();
    }

    class TicketViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.logo)
        ImageView logo;
        @BindView(R.id.airline_name)
        TextView airlineName;
        @BindView(R.id.number_of_stops)
        TextView numberOfStops;
        @BindView(R.id.departure)
        TextView departure;
        @BindView(R.id.arrival)
        TextView arrival;
        @BindView(R.id.price)
        TextView price;
        @BindView(R.id.loader)
        SpinKitView loader;
        @BindView(R.id.duration)
        TextView duration;
        @BindView(R.id.number_of_seats)
        TextView numberOfSeats;
        @BindView(R.id.card_view)
        CardView cardView;

        public TicketViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
