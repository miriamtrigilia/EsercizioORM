package it.polito.tdp.esercizioorm.model;

import java.util.HashMap;
import java.util.Map;

public class StudenteIdMap {

	private Map<Integer,Studente> map;
	
	public StudenteIdMap() {
		this.map = new HashMap<>();
	}
	
	public Studente get(Studente studente) {
		Studente old = map.get(studente.getMatricola());
		if(old == null) {
			// nella mappa non c'è questo studente!
			this.map.put(studente.getMatricola(), studente);
			return studente;
		} 
		return old;
		
	}
	
	public void put(int matricola, Studente studente) {
		map.put(matricola, studente);
	}

	public Studente get(int matricola) {
		return map.get(matricola);
	}
}
