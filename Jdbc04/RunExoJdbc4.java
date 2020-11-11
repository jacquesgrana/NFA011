import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;

public class RunExoJdbc4 {

	// *************************** identifiants de connection en local au serveur postgresql
	private final static String urlConnectionDebut = "jdbc:postgresql://";
	private final static String urlConnectionFin = "/comptedbtest";
	// private final static String urlConnectionFin = "/comptesdb";
	private final static String userConnection = "lambda";
	private final static String passwordConnection = "toto";
	// **************************************************************************************

	public static void main(String[] args) {
		int entierSaisi;
		boolean quitter = false, isIpSaisie = false;
		String adresseIp = "", requete = "", url = "";

		do {
			ToolsForFormatting.Clear(); // ne fonctionne pas (sous ubuntu)
			afficheMenu();
			entierSaisi = ToolsForFormatting.ReadInteger();

			if (entierSaisi == 1) {
				adresseIp = saisirIp();
				url = urlConnectionDebut + adresseIp + urlConnectionFin;
				System.out.println("adresse complète : " + url);
				isIpSaisie = true;
			}

			if (entierSaisi == 2) {
				if (!isIpSaisie) {
					System.out.println("Saisir l'adresse IP ! retour au menu");
				}
				else {
					requete = saisirRequete();
					boolean isOk = isRequeteOk(requete,"SELECT") || isRequeteOk(requete,"INSERT") || isRequeteOk(requete,"UPDATE") || isRequeteOk(requete,"DELETE");
					if (!isOk) {
						System.out.println("Requête invalide ! retour au menu");
					}
					else {
						if (isIpSaisie) {
							executeRequete(requete, adresseIp, url);
						}
					}

				}

			}

			if (entierSaisi == 3) {
				quitter = true;
			}
		}
		while ((entierSaisi < 1) || (entierSaisi > 3) || !quitter);
		ToolsForFormatting.PrintByeBye();
	}
	
	

	private static void executeRequete(String maRequete, String monIp, String monUrl) {
		int numeroTuple = 0, nombreColonne = 0;
		ResultSetMetaData metaData = null;
		ResultSet resultat = null;
		ToolsForFormatting.Clear();
		System.out.println("============================");
		System.out.println("|     Résultat requête     |");
		System.out.println("============================");
		System.out.println();
		System.out.println("Requête : " + maRequete);
		System.out.println();
		Connection connection = connectionBdd(monUrl, userConnection, passwordConnection);
		Statement statement;
		try {
			statement = connection.createStatement();

			// si requête de type select
			if (isRequeteOk(maRequete,"SELECT")) {
				resultat = statement.executeQuery(maRequete);
				
				if (resultat != null) {
					metaData = resultat.getMetaData();
					nombreColonne = metaData.getColumnCount();
				}

				if((nombreColonne > 0) && (statement != null) && (resultat != null) && (metaData != null)) {
					int[] tableauMax = new int[nombreColonne];
					String[] tableauNomColonne = new String[nombreColonne];
					ArrayList<String[]> listeTableauValeur = new ArrayList<String[]>();

					peuplementTableaux(nombreColonne, metaData, resultat, tableauMax, tableauNomColonne, listeTableauValeur);
					
					afficheLigneTrait(tableauMax, nombreColonne);
					afficheHautTableau(nombreColonne, tableauMax, tableauNomColonne);
					afficheLigneTrait(tableauMax, nombreColonne);
					afficheValeurs(nombreColonne, tableauMax, listeTableauValeur);
					afficheLigneTrait(tableauMax, nombreColonne);
					System.out.println();
				}

			}
			
			// sinon, requête de type update, insert ou delete
			else {
				int retourRequete = statement.executeUpdate(maRequete);
				System.out.println("Retour de la requête : " + retourRequete + " [nombre de tuple(s) affecté(s) par la requête]");
				System.out.println();
			}	

			statement.close();
			connection.close();
		}
		catch (SQLException e) {
			//e.printStackTrace();
			System.out.println("Erreur sur la requête !");
			System.out.println();
		}
		ToolsForFormatting.PressEnterKey();
	}



	public static void afficheHautTableau(int monNombreColonne, int[] monTableauMax, String[] monTableauNomColonne) {
		// affichage du haut du tableau avec les noms de colonnes
		AlignLeft(6, "| n°");
		for (int i=0; i<monNombreColonne; i++) {
			AlignLeft(monTableauMax[i] + 4, "| " + monTableauNomColonne[i]);
		}
		AlignLeft(1, "|");
		System.out.println();
	}



	public static void peuplementTableaux(int monNombreColonne, ResultSetMetaData mesMetaData, ResultSet monResultat,
			int[] monTableauMax, String[] monTableauNomColonne, ArrayList<String[]> maListeTableauValeur) throws SQLException {
		
		// boucle pour mettre les max à la longueur du nom de la colonne
		for (int i=0; i<monNombreColonne; i++) {
			monTableauNomColonne[i] = mesMetaData.getColumnName(i+1);
			monTableauMax[i] = monTableauNomColonne[i].length();					
		}

		// boucles pour récupérer les longueurs max des strings pour chaque colonne --> dans tableau + affectation de l'arrayList contenant les valeurs
		while (monResultat.next()) {
			String[] monTuple = new String[monNombreColonne];
			for (int i=0; i<monNombreColonne; i++) {
				if (monResultat.getString(monTableauNomColonne[i]) != null) {
					if (monResultat.getString(monTableauNomColonne[i]).length() > monTableauMax[i]) {
						monTableauMax[i] = monResultat.getString(monTableauNomColonne[i]).length();
					}
					monTuple[i] = monResultat.getString(monTableauNomColonne[i]);
				}
			}
			maListeTableauValeur.add(monTuple);	
		}
	}



	public static void afficheValeurs(int monNombreColonne, int[] monTableauMax, ArrayList<String[]> maListeTableauValeur) {
		// boucles d'affichage des valeurs
		for(int j=0; j<maListeTableauValeur.size(); j++) {
			AlignLeft(6, "| " + (j+1));
			for (int i=0; i<monNombreColonne; i++) {
				AlignLeft(monTableauMax[i] + 4, "| " + maListeTableauValeur.get(j)[i]);
			}
			AlignLeft(1, "|");
			System.out.println();
		}
	}

	// fonction qui affiche un trait composé de '-' et '+'
	private static void afficheLigneTrait(int[] monTableauMax, int monNombreColonne) {
		AlignLeft(6, "+-----");

		for(int i=0; i<monNombreColonne; i++) {
			AlignLeft(monTableauMax[i] + 4, "+" + renvoieString('-',monTableauMax[i] + 3));
		}
		AlignLeft(1, "+");
		System.out.println();

	}

	// fonction qui renvoie n fois le caractère choisi dans un String
	private static String renvoieString(char monCaractere, int monNombreCar) {
		String monString = "";
		for(int i=0; i<monNombreCar; i++) {
			monString += monCaractere;
		}
		return monString;
	}

	// fonction qui vérifie que la requête commence par la String 'aTester'
	private static boolean isRequeteOk(String maRequete, String aTester) {
		boolean isOk = false;
		int maLongueur = aTester.length();
		if (maRequete.length() >=  maLongueur) {
			if (maRequete.substring(0, maLongueur).toUpperCase().equals(aTester)) {
				isOk = true;
			}
		}
		return isOk;
	}

	private static String saisirRequete() {
		String maRequete = "";
		ToolsForFormatting.Clear();
		System.out.println("============================");
		System.out.println("|         Requête          |");
		System.out.println("============================");
		System.out.println();
		System.out.println("Saisir la requête de type SELECT, INSERT, UPDATE ou DELETE (pensez à validez) >");
		maRequete = ToolsForFormatting.ReadString();
		return maRequete;
	}

	private static String saisirIp() {
		String monIp = "";
		ToolsForFormatting.Clear();
		System.out.println("============================");
		System.out.println("|        Adresse IP        |");
		System.out.println("============================");
		System.out.println();
		System.out.println("Saisir l'adresse IP, par ex. 'localhost' ou 'xxx.xxx.xxx.xxx' (pensez à validez) >");
		monIp = ToolsForFormatting.ReadString();
		return monIp;
	}

	private static void afficheMenu() {
		System.out.println();
		System.out.println();
		System.out.println("============================");
		System.out.println("|           Menu           |");
		System.out.println("============================");
		System.out.println();
		System.out.println("1 : Saisir l'adresse IP du serveur Postgres");
		System.out.println("2 : Saisir et exécuter une requête Sql de type SELECT, INSERT, UPDATE ou DELETE");
		System.out.println("3 : Quitter");
		System.out.println();
		System.out.println("Votre choix (pensez à validez) >");

	}

	public static Connection connectionBdd(String monUrl, String monUser, String monPassword) {
		Connection connection = null;

		try {
			connection = DriverManager.getConnection(monUrl, monUser, monPassword);
		}
		catch(SQLException e){
			System.out.println("Connection ko !!");
			System.out.println();
			//System.err.println(e.getMessage());
		}
		return connection;
	}

	// j'ai intégré dans mon code la version que j'ai, un peu, modifié de cette fonction pour ne pas avoir à envoyer la classe modifiée 
	static void AlignLeft(int TotalLength, String Text)
	{
		System.out.print(Text);
		if (Text != null) {
			ToolsForFormatting.PrintNSpaces(ToolsForFormatting.CalcNbrOfSpaces(TotalLength, Text.length()));
			return;
		}
		else {
			ToolsForFormatting.PrintNSpaces(ToolsForFormatting.CalcNbrOfSpaces(TotalLength, 4));
			return;
		}

	}

}
