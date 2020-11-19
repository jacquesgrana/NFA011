import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class RunExoJdbc5 {

	// *************************** identifiants de connection en local au serveur postgresql
	private final static String urlConnectionOldDb = "jdbc:postgresql://localhost/comptesdb";
	private final static String urlConnectionNewDb = "jdbc:postgresql://localhost/secondedb";
	private final static String userConnection = "lambda";
	private final static String passwordConnection = "toto";
	// **************************************************************************************

	public static void main(String[] args) {
		int entierSaisi;
		boolean quitter = false;
		
		do {
			ToolsForFormatting.Clear(); // ne fonctionne pas (sous ubuntu)
			afficheMenu();
			entierSaisi = ToolsForFormatting.ReadInteger();

			if (entierSaisi == 1) {
				copieTableClient();
			}

			if (entierSaisi == 2) {
				afficheTableClient("ancienne");
			}
			
			if (entierSaisi == 3) {
				afficheTableClient("nouvelle");
			}

			if (entierSaisi == 4) {
				quitter = true;
			}
		}
		while ((entierSaisi < 1) || (entierSaisi > 4) || !quitter);
		ToolsForFormatting.PrintByeBye();
		
	}
	
	private static void afficheTableClient(String typeTable) {
		String monUrl = "";
		String requeteRecuperationTuples = "SELECT code_client, nom, prenom, adresse, code_postal, ville, email  FROM client;";
		
		switch (typeTable) {
		case "ancienne" :
			monUrl = urlConnectionOldDb;
			break;
		case "nouvelle" :
			monUrl = urlConnectionNewDb;
			break;
		default :
			monUrl = "";
			break;
		}
		
		ToolsForFormatting.Clear();
		System.out.println("============================");
		System.out.println("|  Affiche " + typeTable + " Table  |");
		System.out.println("============================");
		System.out.println();

		Connection connection = connectionBdd(monUrl, userConnection, passwordConnection);
		Statement statement;
		try {
			statement = connection.createStatement();
			ResultSet resultat = statement.executeQuery(requeteRecuperationTuples);
			while (resultat.next()) {
				AlignLeft(3, resultat.getString("code_client"));
				AlignLeft(12, resultat.getString("nom"));
				AlignLeft(12, resultat.getString("prenom"));
				AlignLeft(32, resultat.getString("adresse"));
				AlignLeft(7, resultat.getString("code_postal"));
				AlignLeft(12, resultat.getString("ville"));
				AlignLeft(36, resultat.getString("email"));
				System.out.println();
            }
			connection.close();
		}
		catch (SQLException e) {
			//e.printStackTrace();
			System.out.println("Erreur sur la requête !");
			System.out.println();
		}	
		System.out.println();
		ToolsForFormatting.PressEnterKey();
		
	}

	private static void copieTableClient() {
		String requeteRecuperationTuples = "SELECT code_client, nom, prenom, adresse, code_postal, ville, email  FROM client;";
		String requeteInsert = "";
		
		ToolsForFormatting.Clear();
		System.out.println("============================");
		System.out.println("     Copie Table Client     ");
		System.out.println("============================");
		System.out.println();
		
		// requete pour recuperer les tuples de client de l'ancienne bdd
		Connection connection = connectionBdd(urlConnectionOldDb, userConnection, passwordConnection);
		Statement statement;
		try {
			statement = connection.createStatement();
			ResultSet resultat = statement.executeQuery(requeteRecuperationTuples);
			while (resultat.next()) {
				// calcul requete INSERT dans nouvelle table client
				requeteInsert += "INSERT INTO client (code_client, nom, prenom, adresse, code_postal, ville, email) "
						+ "VALUES (" + resultat.getString("code_client") + ", "
						+ "'" + resultat.getString("nom") + "', "
						+ "'" + resultat.getString("prenom") + "', "
						+ "'" + resultat.getString("adresse") + "', "
						+ "'" + resultat.getString("code_postal") + "', "
						+ "'" + resultat.getString("ville") + "', "
						+ "'" + resultat.getString("email") + "'); \r";
            }
			//System.out.println("requete :\r" + requeteInsert);
			//System.out.println();
			connection.close();
			
		}
		catch (SQLException e) {
			//e.printStackTrace();
			System.out.println("Erreur sur la requête !");
			System.out.println();
		}
		
		// exécution de la requête INSERT sur la nouvelle bdd
		connection = connectionBdd(urlConnectionNewDb, userConnection, passwordConnection);
		try {
			statement = connection.createStatement();
			int retourRequete = statement.executeUpdate(requeteInsert);
			if (retourRequete > 0) {
				System.out.println("Copie exécutée.");
				System.out.println();
			}
			
			connection.close();
		}
		catch (SQLException e) {
			//e.printStackTrace();
			System.out.println("Erreur sur la requête !");
			System.out.println();
		}
		System.out.println();
		ToolsForFormatting.PressEnterKey();
			
	}

	public static void afficheMenu() {

		System.out.println();
		System.out.println();
		System.out.println("============================");
		System.out.println("|           Menu           |");
		System.out.println("============================");
		System.out.println("1 : Copier la table Client");
		System.out.println("2 : Afficher la table Client de départ");
		System.out.println("3 : Afficher la table Client d'arrivée");
		System.out.println("4 : Quitter");
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
			System.err.println(e.getMessage());
		}
		return connection;
	}
	
	// j'ai intégré dans mon code la version que j'ai, un peu, modifié de cette fonction pour ne pas avoir à envoyer la classe modifiée 
		static void AlignLeft(int TotalLength, String Text) {
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
