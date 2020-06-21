package it.polito.tdp.newufosightings.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.newufosightings.model.Adiacenza;
import it.polito.tdp.newufosightings.model.Event;
import it.polito.tdp.newufosightings.model.Event.EventType;
import it.polito.tdp.newufosightings.model.Sighting;
import it.polito.tdp.newufosightings.model.State;

public class NewUfoSightingsDAO {

	public List<Sighting> loadAllSightings() {
		String sql = "SELECT * FROM sighting";
		List<Sighting> list = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);	
			ResultSet res = st.executeQuery();

			while (res.next()) {
				list.add(new Sighting(res.getInt("id"), res.getTimestamp("datetime").toLocalDateTime(),
						res.getString("city"), res.getString("state"), res.getString("country"), res.getString("shape"),
						res.getInt("duration"), res.getString("duration_hm"), res.getString("comments"),
						res.getDate("date_posted").toLocalDate(), res.getDouble("latitude"),
						res.getDouble("longitude")));
			}

			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}

		return list;
	}

	public List<State> loadAllStates() {
		String sql = "SELECT * FROM state";
		List<State> result = new ArrayList<State>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				State state = new State(rs.getString("id"), rs.getString("Name"), rs.getString("Capital"),
						rs.getDouble("Lat"), rs.getDouble("Lng"), rs.getInt("Area"), rs.getInt("Population"),
						rs.getString("Neighbors"));
				result.add(state);
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<String> getShapes(Integer anno) {
		
		final String sql = "SELECT DISTINCT shape " + 
				"FROM sighting " + 
				"WHERE YEAR(datetime) = ?";
		List<String> result = new ArrayList<>();
		
		try {
			
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				if(rs.getString("shape").compareToIgnoreCase("")!=0){
					result.add(rs.getString("shape"));
				}
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error Connection Database");
		}
		
	}

	public void loadStates(Map<String, State> idMap) {
		String sql = "SELECT * FROM state";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				State state = new State(rs.getString("id"), rs.getString("Name"), rs.getString("Capital"),
						rs.getDouble("Lat"), rs.getDouble("Lng"), rs.getInt("Area"), rs.getInt("Population"),
						rs.getString("Neighbors"));
				idMap.put(state.getId(), state);
			}

			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<Adiacenza> getAdiacenze(Integer anno, String forma, Map<String, State> idMap) {
		final String sql = "SELECT s1.state id1, s2.state id2, COUNT(*) AS peso " + 
				"FROM sighting s1, sighting s2, neighbor n " + 
				"WHERE YEAR(s1.datetime) = ? AND " + 
				"YEAR(s2.datetime) = ? AND " + 
				"s1.shape = ? AND s2.shape = ? AND " + 
				"((s1.state = n.state1 AND s2.state = n.state2) OR " + 
				"(s1.state = n.state2 AND s2.state = n.state1)) AND " + 
				"s1.state < s2.state " + 
				"GROUP BY s1.state, s2.state";
		List<Adiacenza> adiacenze = new ArrayList<>();
		
		try {
			
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			st.setInt(2, anno);
			st.setString(3, forma);
			st.setString(4, forma);
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				State s1 = idMap.get(rs.getString("id1").toUpperCase());
				State s2 = idMap.get(rs.getString("id2").toUpperCase());
				adiacenze.add(new Adiacenza(s1, s2,(double) rs.getInt("peso")));
			}
			conn.close();
			return adiacenze;
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<Event> getEvents(Integer anno, String forma, Map<String, State> idMap) {
		
		final String sql = "SELECT state, DATETIME AS data " + 
				"FROM sighting " + 
				"WHERE YEAR(DATETIME) = ? AND " + 
				"shape = ?";
		List<Event> eventi = new ArrayList<>();
		
		try {
			
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			st.setString(2, forma);
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				State s = idMap.get(rs.getString("state").toUpperCase());
				eventi.add(new Event(s, EventType.INIZIO, rs.getTimestamp("data").toLocalDateTime()));
			}
			conn.close();
			return eventi;
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

}

