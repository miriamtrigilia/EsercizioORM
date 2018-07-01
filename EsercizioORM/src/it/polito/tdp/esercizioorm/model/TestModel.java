package it.polito.tdp.esercizioorm.model;

import java.util.List;

public class TestModel {

	public static void main(String[] args) {
		
		
		Model m = new Model();
		
		int matricola = 146101;
		int result = m.getTotCreditiFromStudente(matricola);
		System.out.println("Tot crediti: "+result);
		
		List<Studente> studenti = m.getStudentiFromCorso("01NBAPG");
		for(Studente s : studenti) {
			System.out.println(s);
		}
		
		List<Corso> corsi = m.getCorsiFromStudente(146101);
		for(Corso c : corsi) {
			System.out.println(c);
		}
	
	}

}
