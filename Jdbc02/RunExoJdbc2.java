import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RunExoJdbc2 {

	// *************************** identifiants de connection en local au serveur postgresql
	private final static String urlConnection = "jdbc:postgresql://localhost/comptesdb";
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
				afficheNomSgbd();
			}

			if (entierSaisi == 2) {
				afficheVersionSgbd();
			}

			if (entierSaisi == 3) {
				afficheNomUtil();
			}

			if (entierSaisi == 4) {
				afficheTables();
			}

			if (entierSaisi == 5) {
				quitter = true;
			}
		}
		while ((entierSaisi < 1) || (entierSaisi > 5) || !quitter);
		ToolsForFormatting.PrintByeBye();
	}

	private static void afficheNomSgbd() {
		ToolsForFormatting.Clear();
		System.out.println("============================");
		System.out.println("        Nom du SGBD         ");
		System.out.println("============================");
		System.out.println();
		Connection connection = connectionBdd(urlConnection, userConnection, passwordConnection);
		try {
			DatabaseMetaData metaData = connection.getMetaData();
			System.out.println("--> " + metaData.getDatabaseProductName());
			System.out.println();
			connection.close();
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		ToolsForFormatting.PressEnterKey();
	}

	private static void afficheVersionSgbd() {
		ToolsForFormatting.Clear();
		System.out.println("============================");
		System.out.println("      Version du SGBD       ");
		System.out.println("============================");
		System.out.println();
		Connection connection = connectionBdd(urlConnection, userConnection, passwordConnection);
		try {
			DatabaseMetaData metaData = connection.getMetaData();
			System.out.println("--> " + metaData.getDatabaseProductVersion());
			System.out.println();
			connection.close();
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		ToolsForFormatting.PressEnterKey();
	}

	private static void afficheNomUtil() {
		ToolsForFormatting.Clear();
		System.out.println("============================");
		System.out.println("      Nom Utilisateur       ");
		System.out.println("============================");
		System.out.println();
		Connection connection = connectionBdd(urlConnection, userConnection, passwordConnection);
		try {
			DatabaseMetaData metaData = connection.getMetaData();
			System.out.println("--> " + metaData.getUserName());
			System.out.println();
			connection.close();
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		ToolsForFormatting.PressEnterKey();
	}

	private static void afficheTables() {
		ToolsForFormatting.Clear();
		System.out.println("============================");
		System.out.println("     Détails des tables     ");
		System.out.println("============================");
		System.out.println();

		Connection connection = connectionBdd(urlConnection, userConnection, passwordConnection);
		try {
			String[] tableTypes = { "TABLE" };
			DatabaseMetaData metaData = connection.getMetaData();
			ResultSet resultat = metaData.getTables(connection.getCatalog(), null, "%", tableTypes);
			while(resultat.next()) {
				System.out.println();
				System.out.println("============================");
				System.out.println("> Table " + resultat.getString(3) + " :");
				System.out.println("============================");
				System.out.println();
				
				ResultSet resultatTable = metaData.getPrimaryKeys(connection.getCatalog(), null, resultat.getString(3));
				System.out.print("--> Clé(s) primaire(s) de la table " + resultat.getString(3));
				while(resultatTable.next()) {
					System.out.print(" : " + resultatTable.getString(4));
				}
				System.out.println();
				System.out.println();
				System.out.println("--> Attributs de la table " + resultat.getString(3) + " : ");
				System.out.println();
				
				ResultSet resultatAttribut = metaData.getColumns(connection.getCatalog(), null, resultat.getString(3), "%");
				ToolsForFormatting.AlignLeft(16, "| (Nom)");
				ToolsForFormatting.AlignLeft(14, "| (Type)");
				ToolsForFormatting.AlignLeft(14, "| (Nullable)");
				ToolsForFormatting.AlignLeft(1, "|");
				System.out.println();
				ToolsForFormatting.AlignLeft(16, "|---------------");
				ToolsForFormatting.AlignLeft(14, "|-------------");
				ToolsForFormatting.AlignLeft(14, "|-------------");
				ToolsForFormatting.AlignLeft(1, "|");
				System.out.println();
				
				while(resultatAttribut.next()) {
					String isNullable ="?";
					if(resultatAttribut.getString(11).equals("1")) {
						isNullable = "Oui";
					}
					else {
						if (resultatAttribut.getString(11).equals("0")) {
							isNullable = "Non";
						}
					}
					ToolsForFormatting.AlignLeft(16, "| " + resultatAttribut.getString(4));
					ToolsForFormatting.AlignLeft(14, "| " + resultatAttribut.getString(6));
					ToolsForFormatting.AlignLeft(14, "| " + isNullable);
					ToolsForFormatting.AlignLeft(1, "|");
					System.out.println();
				}
				
				System.out.println();
				ToolsForFormatting.PressEnterKey();
				System.out.println();
			}
			System.out.println();
			connection.close();
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void afficheMenu() {

		System.out.println();
		System.out.println();
		System.out.println("============================");
		System.out.println("|           Menu           |");
		System.out.println("============================");
		System.out.println("1 : Nom du SGBD");
		System.out.println("2 : Version du SGBD");
		System.out.println("3 : Nom de l'utilisateur du SGBD");
		System.out.println("4 : Clés primaires et attibuts des tables");
		System.out.println("5 : Quitter");
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

}
