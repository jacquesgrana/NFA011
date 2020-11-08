// ToolsForFormatting.java
// Version 2.2 • Dernière mise à jour le 24 oct. 2015.
// Copyright (c) 2012 - 2015 Christophe Marty. All rights reserved.

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ToolsForFormatting
{
	// Affiche une espace.
	static void PrintSpace()
	{
		System.out.print(" ");
		return;
	}

	// Afficher n espaces.
	static void PrintNSpaces(int n)
	{
		while(n >= 10)
		{
			System.out.print("          ");
			n -= 10;
		}
		while(n >= 2)
		{
			System.out.print("  ");
			n -= 2;
		}
		while(n > 0)
		{
			System.out.printf(" ");
			n--;
		}
		return;
	}

	// Calcule le nombre d'espaces entre la longueur voulue et la longueur du texte.
	static int CalcNbrOfSpaces(int TotalLength, int TextLength)
	{
		if(TextLength > TotalLength)
			return 0;
		return TotalLength - TextLength;
	}

	// Aligne le texte à gauche et complète avec des espaces.
	static void AlignLeft(int TotalLength, String Text)
	{
		System.out.print(Text);
		if (Text != null) {
			PrintNSpaces(CalcNbrOfSpaces(TotalLength, Text.length()));
			return;
		}
		else {
			PrintNSpaces(CalcNbrOfSpaces(TotalLength, 4));
			return;
		}
		
	}

	// Aligne le texte à droite et complète avec des espaces.
	static void AlignRight(int TotalLength, String Text)
	{
		PrintNSpaces(CalcNbrOfSpaces(TotalLength, Text.length()));
		System.out.print(Text);
		return;
	}

	// Efface la console.
	static void Clear()
	{
		try
		{
			if(System.getProperty("os.name").startsWith("Windows"))
				Runtime.getRuntime().exec("cls");
			else
				Runtime.getRuntime().exec("clear");
		}
		catch(Exception e)
		{
			int indx;

			for(indx = 0; indx < 100; indx++)
				System.out.println();
		}
	}
	
	// Lecture d'un texte sur la console.
	static String ReadString()
	{
		try
		{
			BufferedReader BufferLecture = new BufferedReader(new InputStreamReader(System.in));
			return BufferLecture.readLine();
		}
		catch(Exception e)
		{
			System.err.println(e);
			return "";
		}
	}

	// Lecture d'un nombre entier sur la console.
	static int ReadInteger()
	{
		try
		{
			BufferedReader BufferLecture = new BufferedReader(new InputStreamReader(System.in));
			return Integer.parseInt(BufferLecture.readLine());
		}
		catch(Exception e)
		{
			System.err.println(e);
			return 0;
		}
	}

	// Attend l'appuie sur la touche entrée.
	static void PressEnterKey()
	{
		try
		{
			BufferedReader BufferLecture = new BufferedReader(new InputStreamReader(System.in));
			System.out.print("Appuyez sur la touche Entrée pour continuer... ");
			BufferLecture.read();
			return;
		}
		catch(Exception e)
		{
			System.err.println(e);
			return;
		}
	}

	// Sans commentaire.
	static void PrintByeBye()
	{
		System.out.println("Bye Bye !");
		return;
	}
}