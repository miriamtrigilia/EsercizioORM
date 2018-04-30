package it.polito.tdp.esercizioorm.model;

import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.esercizioorm.dao.CorsoDAO;
import it.polito.tdp.esercizioorm.dao.StudenteDAO;

public class Model {

	private CorsoDAO cdao;
	private StudenteDAO sdao;
	
	private List<Corso> corsi;
	private List<Studente> studenti;
	
	private CorsoIdMap corsomap;
	private StudenteIdMap studentemap;
	
	public Model() {
		
		cdao = new CorsoDAO();
		sdao = new StudenteDAO();
		
		corsomap = new CorsoIdMap();
		studentemap = new StudenteIdMap();
		
		corsi = cdao.getTuttiCorsi(corsomap);
		System.out.println(corsi.size());
		
		studenti = sdao.getTuttiStudenti(studentemap);
		System.out.println(studenti.size());
		
		for (Studente s : studenti) {
			cdao.getCorsiFromStudente(s, corsomap);
		}
		
		for (Corso c : corsi) {
			sdao.getStudentiFromCorso(c, studentemap);
		}
	}
	
	public List<Studente> getStudentiFromCorso(String codins) {
		Corso c = corsomap.get(codins);
		
		if (c == null) {
			return new ArrayList<Studente>();
		}
		
		return c.getStudenti();
	}
	
	public List<Corso> getCorsiFromStudente(int matricola) {
		Studente s = studentemap.get(matricola);
		
		if (s == null) {
			return new ArrayList<Corso>();
		}
		
		return s.getCorsi();		
	}
	
	public int getTotCreditiFromStudente(int matricola) {
		
		int sum = 0;
		
		for (Studente s : studenti) {
			if (s.getMatricola() == matricola) {
				for (Corso c : s.getCorsi()) {
					sum += c.getCrediti();
				}
				return sum;
			}
		}
		
		return -1;
	}

	public boolean iscriviStudenteACorso(int matricola, String codins) {
		
		Studente studente = studentemap.get(matricola);
		Corso corso = corsomap.get(codins);
		
		if (studente == null || corso == null) {
			// non posso iscrivere uno studente ad un corso
			return false;
		}
		
		// Aggiorno il DB
		boolean result = sdao.iscriviStudenteACorso(studente, corso);
		
		if (result) {
			// aggiornamento db effettuato con successo
			
			// Aggiorno i riferimenti in memoria
			if (!studente.getCorsi().contains(corso)) {
				studente.getCorsi().add(corso);
			}
			if (! corso.getStudenti().contains(studente)) {
				corso.getStudenti().add(studente);
			}
			return true;
		}
		
		return false;
	}

	/*
	 * Questo metodo viene utilizzato solo per testare le performance di ConnectDBCP.
	 */
	public void testCP() {
		double avgTime = 0;
		for (int i = 0; i < 10; i++) {
			long start = System.nanoTime();
			List<Studente> studenti = sdao.getTuttiStudenti(new StudenteIdMap());
			for (Studente s : studenti) {
				sdao.studenteIscrittoACorso(s.getMatricola(), "01NBAPG");
			}
			double tt = (System.nanoTime() - start) / 10e9;
			System.out.println(tt);
			avgTime += tt;
		}
		System.out.println("AvgTime (mean on 10 loops): " + avgTime/10);
	}
	
	
}
