package com.example.akash.rxflightticket.pojo;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class Ticket{

	@SerializedName("duration")
	private String duration;

	@SerializedName("instructions")
	private String instructions;

	@SerializedName("arrival")
	private String arrival;

	@SerializedName("flight_number")
	private String flightNumber;

	@SerializedName("from")
	private String from;

	@SerializedName("to")
	private String to;

	@SerializedName("departure")
	private String departure;

	@SerializedName("stops")
	private int stops;

	@SerializedName("airline")
	private Airline airline;

	private Price price;

	public void setDuration(String duration){
		this.duration = duration;
	}

	public String getDuration(){
		return duration;
	}

	public void setInstructions(String instructions){
		this.instructions = instructions;
	}

	public String getInstructions(){
		return instructions;
	}

	public void setArrival(String arrival){
		this.arrival = arrival;
	}

	public String getArrival(){
		return arrival;
	}

	public void setFlightNumber(String flightNumber){
		this.flightNumber = flightNumber;
	}

	public String getFlightNumber(){
		return flightNumber;
	}

	public void setFrom(String from){
		this.from = from;
	}

	public String getFrom(){
		return from;
	}

	public void setTo(String to){
		this.to = to;
	}

	public String getTo(){
		return to;
	}

	public void setDeparture(String departure){
		this.departure = departure;
	}

	public String getDeparture(){
		return departure;
	}

	public void setStops(int stops){
		this.stops = stops;
	}

	public int getStops(){
		return stops;
	}

	public void setAirline(Airline airline){
		this.airline = airline;
	}

	public Airline getAirline(){
		return airline;
	}

	public Price getPrice() {
		return price;
	}

	public void setPrice(Price price) {
		this.price = price;
	}

	@Override
 	public String toString(){
		return 
			"Ticket{" + 
			"duration = '" + duration + '\'' + 
			",instructions = '" + instructions + '\'' + 
			",arrival = '" + arrival + '\'' + 
			",flight_number = '" + flightNumber + '\'' + 
			",from = '" + from + '\'' + 
			",to = '" + to + '\'' + 
			",departure = '" + departure + '\'' + 
			",stops = '" + stops + '\'' + 
			",airline = '" + airline + '\'' + 
			"}";
		}
}