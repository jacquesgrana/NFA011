import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RunExoJdbc1 {
	
	/* 
	
	J'ai modifié (légèrement) la classe ToolsForFormatting :
	
	J'ai remplacé ligne Ligne 50 :
	  
	  PrintNSpaces(CalcNbrOfSpaces(TotalLength, Text.length()));
	 
	Par : 
	 
	 if (Text != null) {
			PrintNSpaces(CalcNbrOfSpaces(TotalLength, Text.length()));
			return;
		}
		else {
			PrintNSpaces(CalcNbrOfSpaces(TotalLength, 4));
			return;
		}
		
		(Cf le fichier modifié de la classe ci-joint)
		
		Sinon, la fonction CalcNbrOfSpaces() plante dans le cas où le String est à null (cas présents dans les tables).
		J'ai trouvé ça plus simple que de protéger, dans mon code, les appels à la fonction AlignLeft().
		
	 */
	
	
	// *************************** identifiants de connexion en local au serveur postgresql
	private final static String urlConnexion = "jdbc:postgresql://localhost/comptesdb";
    private final static String userConnexion = "postgres";
    private final static String passwordConnexion = "5485K";
    // **************************************************************************************

	public static void main(String[] args) {
		int entierSaisi;
		boolean quitter = false;

		do {
			ToolsForFormatting.Clear(); // ne marche pas (sous ubuntu)
			afficheMenu();
			entierSaisi = ToolsForFormatting.ReadInteger();

			if (entierSaisi == 1) {
				afficheTable("client");
			}
			
			if (entierSaisi == 2) {
				afficheTable("compte");
			}
			
			if (entierSaisi == 3) {
				afficheTable("ecriture");
			}
			
			if (entierSaisi == 4) {
				afficheTable("associer");
			}
			
			if (entierSaisi == 5) {
				afficheTable("produit");
			}

			if (entierSaisi == 6) {
				quitter = true;
			}
		}
		while ((entierSaisi < 1) || (entierSaisi > 6) || !quitter);
		ToolsForFormatting.PrintByeBye();
	}

	private static void afficheTable(String nomTable) {
		String SQL = "SELECT * FROM " + nomTable;
		
		ToolsForFormatting.Clear(); // ne marche pas (sous ubuntu)
		switch (nomTable){
			case "client" : {
				ToolsForFormatting.Clear();
				System.out.println("============================");
				System.out.println("        Table Client        ");
				System.out.println("============================");
				System.out.println();
				Connection connection = connectionDB(urlConnexion, userConnexion, passwordConnexion);
				Statement statement;
				try {
					statement = connection.createStatement();
					ResultSet resultat = statement.executeQuery(SQL);
					while (resultat.next()) {
						ToolsForFormatting.AlignLeft(3, resultat.getString("code_client"));
						ToolsForFormatting.AlignLeft(12, resultat.getString("nom"));
						ToolsForFormatting.AlignLeft(12, resultat.getString("prenom"));
						ToolsForFormatting.AlignLeft(12, resultat.getString("date_naissance"));
						ToolsForFormatting.AlignLeft(32, resultat.getString("adresse"));
						ToolsForFormatting.AlignLeft(7, resultat.getString("code_postal"));
						ToolsForFormatting.AlignLeft(12, resultat.getString("ville"));
						ToolsForFormatting.AlignLeft(12, resultat.getString("telephone"));
						ToolsForFormatting.AlignLeft(36, resultat.getString("email"));
						System.out.println();
                    }
					connection.close();
				}
				catch (SQLException e) {
					e.printStackTrace();
				}	
				System.out.println();
				ToolsForFormatting.PressEnterKey();
				break;
			}
			
			case "compte" : {
				ToolsForFormatting.Clear();
				System.out.println("============================");
				System.out.println("        Table Compte        ");
				System.out.println("============================");
				System.out.println();
				Connection connection = connectionDB(urlConnexion, userConnexion, passwordConnexion);
				Statement statement;
				try {
					statement = connection.createStatement();
					ResultSet resultat = statement.executeQuery(SQL);
					while (resultat.next()) {
						ToolsForFormatting.AlignLeft(3, resultat.getString("no_compte"));
						ToolsForFormatting.AlignLeft(12, resultat.getString("date_creation"));
						ToolsForFormatting.AlignLeft(8, resultat.getString("code_client"));
						System.out.println();
                    }
					connection.close();
				}
				catch (SQLException e) {
					e.printStackTrace();
				}	
				System.out.println();
				ToolsForFormatting.PressEnterKey();
				break;
			}
			
			case "ecriture" : {
				ToolsForFormatting.Clear();
				System.out.println("============================");
				System.out.println("       Table Ecriture       ");
				System.out.println("============================");
				System.out.println();
				Connection connection = connectionDB(urlConnexion, userConnexion, passwordConnexion);
				Statement statement;
				try {
					statement = connection.createStatement();
					ResultSet resultat = statement.executeQuery(SQL);
					while (resultat.next()) {
						ToolsForFormatting.AlignLeft(4, resultat.getString("no_ecriture"));
						ToolsForFormatting.AlignLeft(12, resultat.getString("date_ecriture"));
						ToolsForFormatting.AlignLeft(14, resultat.getString("debit"));
						ToolsForFormatting.AlignLeft(14, resultat.getString("credit"));
						ToolsForFormatting.AlignLeft(4, resultat.getString("no_compte"));
						System.out.println();
                    }
					connection.close();
				}
				catch (SQLException e) {
					e.printStackTrace();
				}	
				System.out.println();
				ToolsForFormatting.PressEnterKey();
				break;
			}
			
			case "associer" : {
				ToolsForFormatting.Clear();
				System.out.println("============================");
				System.out.println("       Table Associer       ");
				System.out.println("============================");
				System.out.println();
				Connection connection = connectionDB(urlConnexion, userConnexion, passwordConnexion);
				Statement statement;
				try {
					statement = connection.createStatement();
					ResultSet resultat = statement.executeQuery(SQL);
					while (resultat.next()) {
						ToolsForFormatting.AlignLeft(4, resultat.getString("no_compte"));
						ToolsForFormatting.AlignLeft(12, resultat.getString("code_produit"));
						System.out.println();
                    }
					connection.close();
				}
				catch (SQLException e) {
					e.printStackTrace();
				}	
				System.out.println();
				ToolsForFormatting.PressEnterKey();
				break;
			}
			
			case "produit" : {
				ToolsForFormatting.Clear();
				System.out.println("============================");
				System.out.println("       Table Produit        ");
				System.out.println("============================");
				System.out.println();
				Connection connection = connectionDB(urlConnexion, userConnexion, passwordConnexion);
				Statement statement;
				try {
					statement = connection.createStatement();
					ResultSet resultat = statement.executeQuery(SQL);
					while (resultat.next()) {
						ToolsForFormatting.AlignLeft(4, resultat.getString("code_produit"));
						ToolsForFormatting.AlignLeft(36, resultat.getString("designation"));
						System.out.println();
                    }
					connection.close();
				}
				catch (SQLException e) {
					e.printStackTrace();
				}	
				System.out.println();
				ToolsForFormatting.PressEnterKey();
				break;
			}
			
			default : {
				break;
			}
		}

	}

	public static void afficheMenu() {

		System.out.println("============================");
		System.out.println("|           Menu           |");
		System.out.println("============================");
		System.out.println("1 : Client");
		System.out.println("2 : Compte");
		System.out.println("3 : Ecriture");
		System.out.println("4 : Associer");
		System.out.println("5 : Produit");
		System.out.println("6 : Quitter");
		System.out.println();
		System.out.println("Votre choix (pensez à validez) >");
	}
	
	public static Connection connectionDB(String monUrl, String monUser, String monPassword) {
        Connection connection = null;

        try {
        	connection = DriverManager.getConnection(monUrl, monUser, monPassword);
        }
        catch(SQLException e){
            System.err.println(e.getMessage());
        }
        return connection;
    }

}
