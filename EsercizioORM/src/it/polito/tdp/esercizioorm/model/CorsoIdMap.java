package it.polito.tdp.esercizioorm.model;

import java.util.HashMap;
import java.util.Map;

public class CorsoIdMap {

	private Map<String,Corso> map;
	
	public CorsoIdMap() {
		this.map = new HashMap<>();
	}
	
	public Corso get(Corso corso) {
		Corso old = map.get(corso.getCodIns());
		if(old == null) {
			// nella mappa non c'è questo corso!
			this.map.put(corso.getCodIns(), corso);
			return corso;
		} 
		return old;
		
	}
	
	public void put(String codins, Corso corso) {
		map.put(codins, corso);
	}

	public Corso get(String codins) {
		return map.get(codins);
	}
}
