package it.polito.tdp.newufosightings.model;

import java.time.LocalDateTime;

public class Event implements Comparable<Event>{
	
	public enum EventType{
		INIZIO, FINE,
	}

	private State stato;
	private EventType tipo;
	private LocalDateTime data;
	
	public Event(State stato, EventType tipo, LocalDateTime data) {
		super();
		this.stato = stato;
		this.tipo = tipo;
		this.data = data;
	}

	public State getStato() {
		return stato;
	}

	public EventType getTipo() {
		return tipo;
	}

	public LocalDateTime getData() {
		return data;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((stato == null) ? 0 : stato.hashCode());
		result = prime * result + ((tipo == null) ? 0 : tipo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Event other = (Event) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (stato == null) {
			if (other.stato != null)
				return false;
		} else if (!stato.equals(other.stato))
			return false;
		if (tipo != other.tipo)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Event [stato=" + stato + ", tipo=" + tipo + ", data=" + data + "]";
	}

	@Override
	public int compareTo(Event other) {
		return this.data.compareTo(other.data);
	}

	
	
}
