package it.polito.tdp.esercizioorm.model;

import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.esercizioorm.dao.CorsoDAO;
import it.polito.tdp.esercizioorm.dao.StudenteDAO;

public class Model {
	
	private List<Corso> corsi ;
	private List<Studente> studenti;
	
	private CorsoDAO cdao;
	private StudenteDAO sdao;
	
	
	private StudenteIdMap studenteMap;
	private CorsoIdMap corsoMap;
	
	public Model() {
		cdao = new CorsoDAO();
		sdao = new StudenteDAO();
		
		corsoMap = new CorsoIdMap();
		studenteMap = new StudenteIdMap();
		
		corsi = cdao.getTuttiCorsi(corsoMap); // così li inserisce nella mappa se necessario.
		System.out.println(corsi.size());
		
		this.studenti = sdao.getTuttiStudenti(studenteMap);
		System.out.println(studenti.size());
		
		// riferimenti incorciati
		for(Studente s : this.studenti) {
			cdao.getCorsiFromStudente(s,corsoMap);
		}
		
		for(Corso c : this.corsi) {
			sdao.getStudentiFromCorso(c,studenteMap);
		}
		
	}
	
	public List<Studente> getStudentiFromCorso(String codins) {
		Corso c = corsoMap.get(codins);
		if(c == null) { // è importante
			return new ArrayList<Studente>();
		}
		return c.getStudenti();
	}
	
	public List<Corso> getCorsiFromStudente(int matricola) {
		Studente s = studenteMap.get(matricola);
		if(s == null) { // è importante
			return new ArrayList<Corso>();
		}
		return s.getCorsi();
	}
	
	public int getTotCreditiFromStudente(int matricola) {
		int sum = 0;
		
		for(Studente s : this.studenti) {
			if(s.getMatricola() == matricola) {
				for(Corso c : s.getCorsi()) {
					sum += c.getCrediti();
				}
				return sum;
			}
		}
		
		
		return -1;
	}

	public boolean iscriviStudenteACorso(int matricola, String codins) {
		
		Corso corso = corsoMap.get(codins);
		Studente studente = studenteMap.get(matricola);
		
		if(studente == null || corso == null)
			// non posso iscrivere uno studente ad un corso
			return false ; 
		
		// aggiorno in DB
		boolean result = sdao.iscriviStudenteACorso(studente,corso);
		if(result) {
			// aggiornamento DB effettuato con successo
			
			// aggiorno in memoria i riferimenti (solo dopo, perche se non aggiorno in DB non devo farlo in memoria)
			if(!studente.getCorsi().contains(corso) )
				studente.getCorsi().add(corso);
			if(!corso.getStudenti().contains(studente))
				corso.getStudenti().add(studente);
			return true;
		}
		
		return false;
	}
}
