package be.eloistree.openmacroinput.window;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
Source: https://codes-sources.commentcamarche.net/faq/10905-lancement-d-un-commande-avec-runtime-exec
*/
public class RecoverProcessOutput extends Thread {  

	private InputStream flux;

	public RecoverProcessOutput(InputStream flux){
		this.flux = flux;
	}
	
	@Override
	public void run(){
		try {    
			InputStreamReader reader = new InputStreamReader(flux);
			BufferedReader br = new BufferedReader(reader);
			String ligne=null;
			while ( (ligne = br.readLine()) != null){
				System.out.println(ligne);
			}
		}
		catch (IOException ioe){
			ioe.printStackTrace();
		}
	}
} 