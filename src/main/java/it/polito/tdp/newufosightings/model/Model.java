package it.polito.tdp.newufosightings.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.newufosightings.db.NewUfoSightingsDAO;

public class Model {
	
	private NewUfoSightingsDAO dao;
	private Graph<State, DefaultWeightedEdge> grafo;
	private Map<String, State> idMap;
	
	public Model() {
		this.dao = new NewUfoSightingsDAO();
		this.idMap = new HashMap<>();
	}

	public List<String> getForme(Integer anno) {
		return this.dao.getShapes(anno);
	}

	public String creaGrafo(Integer anno, String forma) {
		
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		this.dao.loadStates(idMap);
		Graphs.addAllVertices(grafo, this.idMap.values());
		for(Adiacenza a : this.dao.getAdiacenze(anno, forma, idMap)) {
			Graphs.addEdge(grafo, a.getS1(), a.getS2(), a.getPeso());
		}
		String output = "Elenco avvistamenti:\n";
		for(State s : this.grafo.vertexSet()) {
			Double tot = 0.0;
			for(State x : Graphs.neighborListOf(grafo, s)) {
				tot += this.grafo.getEdgeWeight(this.grafo.getEdge(s, x));
			}
			if(tot != 0.0)
				output += s + "  peso: " + tot +"\n";
		}
		return output;
	}

	public String simula(Integer anno, String forma, Integer t1, Integer alfa) {
		List<Event> eventi = this.dao.getEvents(anno, forma, idMap);
		Simulator sim = new Simulator();
		sim.init(eventi, t1, alfa, idMap);
		String output = sim.run();
		return output;
	}

}
