package model;

import exception.AppException;

/**
 * Objet ordonnance de la pharmacie
 * 
 * @author SRet
 */
public class Ordonnance extends Achat {

	/**
	 * medecin qui a redige l'ordonnance
	 */
	Medecin medecin;
	/**
	 * specialiste qui a co-redige l'ordonnance
	 */
	Specialiste specialiste;

	/**
	 * getter pour le medecin traitant du client
	 * 
	 * @return le {@link Medecin} traitant du client
	 */
	public Medecin getMedecin() {
		return medecin;
	}

	/**
	 * setter pour le medecin traitant du client
	 * 
	 * @param medecin : {@link Medecin} traitant du client
	 * @throws AppException : aucun medecin selectionne
	 */
	public void setMedecin(Medecin medecin) throws AppException {
		if (medecin == null)
			throw new AppException(
					"Erreur ordonnance : veuillez contacter le SAV");
		this.medecin = medecin;
	}

	/**
	 * getter pour le specialiste qui a ecrit l'ordonnance si il y a
	 * 
	 * @return le {@link Specialiste} qui a ecrit l'ordonnance
	 */
	public Specialiste getSpecialiste() {
		return specialiste;
	}

	/**
	 * setter pour le pecialiste qui a ecrit l'ordonnance
	 * 
	 * @param specialiste : {@link Specialiste} qui a ecrit l'ordonnance
	 */
	public void setSpecialiste(Specialiste specialiste) {
		this.specialiste = specialiste;
	}

	/**
	 * Constructeur de la classe ordonnance
	 * 
	 * @param id       : id de l'ordonnance
	 * @param acheteur : {@link Client} qui a achete l'ordonnance
	 * @param medecin  : medecin traitant du client
	 * @param date : date de l'ordonnance
	 * @throws AppException : erreurs de l'utilisateur
	 */
	public Ordonnance(Integer id, Client acheteur, Medecin medecin, String date)
			throws AppException {
		super(id, acheteur, date);
		this.setMedecin(medecin);
		medecin.ajouterOrdonnance(this);
	}

	/**
	 * setter pour les medicaments a ajouter
	 * 
	 * @param medicament : medicament a ajouter
	 * @throws AppException : erreur dans le medicament a ajouter
	 */
	@Override
	public void setMedicaments(Medicament medicament, int quantite)
			throws AppException {
		if (medicament == null)
			throw new AppException(
					"Erreur ordonnance : veuillez contacter le SAV");
		Medicament medicamentAchete = new Medicament(medicament.getId(),
				medicament.getNom(), medicament.getCategorie(),
				medicament.getPrix(),
				quantite, medicament.dateToString());
		this.getMedicaments().add(medicamentAchete);
	}

	@Override
	public double getPrixTotal() {
		double prixTotal = 0;
		for (Medicament medicament : this.getMedicaments()) {
			try {
				medicament.setPrixReduit(this.getAcheteur().getMutuelle());
			} catch (AppException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			prixTotal = prixTotal
					+ (medicament.getQuantite() * medicament.getPrixReduit());
		}
		return prixTotal;
	}
	
	/**
	 * Transformer l'ordonnance en {@link String}
	 */
	@Override
	public String toString() {
		String temp = "";
		if (this.specialiste == null)
			temp = "Medecin = " + this.getMedecin() + "\n" + "\n"
					+ super.toString();
		else
			temp = "Medecin = " + this.getMedecin() + "\n" + "Specialiste = "
					+ this.getSpecialiste() + "\n" + super.toString();
		return temp;
	}

}
