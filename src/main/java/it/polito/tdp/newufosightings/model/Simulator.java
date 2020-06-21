package it.polito.tdp.newufosightings.model;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import it.polito.tdp.newufosightings.model.Event.EventType;

public class Simulator {
	
	//PARAMETRI DELLA SIMULAZIONE
	private int T1;
	private double alfa;
	
	//OUTPUT DA CALCOLARE
	
	
	//STATO DEL MONDO
	private Map<String, State> idMap;
	
	//CODA DEGLI EVENTI
	private PriorityQueue<Event> queue;
	
	//INIZIALIZZAZIONE
	public void init(List<Event> eventi, Integer t1, Integer alfa, Map<String, State> idMap) {
		this.queue = new PriorityQueue<>();
		this.T1 = t1;
		this.alfa = (double) alfa/100;
		this.idMap = idMap;
		for(Event e : eventi) {
			this.queue.add(e);
		}
	}
	
	public String run() {
		
		while(!this.queue.isEmpty()) {
			Event e = this.queue.poll();
			System.out.println(e);
			processEvent(e);
		}
		String output = "\nRisultati simulazione:\n";
		for(State s : idMap.values()) {
			output += s.toString() + " DEFCON: " + s.getDefcon() + "\n";
		}
		return output;
	}

	public void processEvent(Event e) {
		State s = e.getStato();
		
		switch(e.getTipo()) {
		
		case INIZIO:
			s.setUltima(e.getData());
			if(s.getDefcon()>=2)
				s.setDefcon(-1);
			if(probabilita()) {
				for(String x : s.getVicini()) {
					State y = idMap.get(x);
					if(y.getDefcon()>=1.5)
						y.setDefcon(-0.5);
				}
			}
			this.queue.add(new Event(s, EventType.FINE, e.getData().plusDays(T1)));
			break;
			
		case FINE:
			if(!Duration.between(s.getUltima(), e.getData()).minusDays(T1).isNegative()) {
				if(s.getDefcon()<=4) {
					s.setDefcon(1);
				}
				if(probabilita()) {
					for(String x : s.getVicini()) {
						State y = idMap.get(x);
						if(y.getDefcon()<=4.5)
							y.setDefcon(0.5);
					}
				}
			}
			break;
		
		}
		
	}

	private boolean probabilita() {
		Double prob = Math.random();
		if(prob<alfa){
			return true;
		}
		return false;
	}
	
}
