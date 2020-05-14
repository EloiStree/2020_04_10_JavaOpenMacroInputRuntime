package be.eloistree.openmacroinput.window;
import java.io.IOException; 

/**
 Source: https://codes-sources.commentcamarche.net/faq/10905-lancement-d-un-commande-avec-runtime-exec
 */
public class CmdUtility {

	boolean m_listenToOutput;
	boolean m_listenToError;

	public CmdUtility(boolean listenToOutput, boolean listenToError) {

		m_listenToOutput=listenToOutput;
		m_listenToError=listenToError;
	}
	
	public boolean ping(String target) {
		
		return execute("ping "+target);
	}
	public boolean beep() {
		return execute("echo ^G");
		
	}
	public boolean openUrl(String url) {
		return execute ("start "+url);
		
	}public boolean openApplication(String appNameWithExe) {
		
		return execute ("start \"\" /normal \""+appNameWithExe+"\"");
	}public boolean killApplication(String appNameWithExe) {
		return execute ("taskkill /f /im \""+appNameWithExe+"\"" );
		
	}
	
	
	public boolean execute(String cmd){
		int exitValue=1;
		
		Runtime runtime = Runtime.getRuntime();
		
		try {
			Process process = runtime.exec("cmd /c "+cmd);
			if( m_listenToOutput)
				new RecoverProcessOutput(process.getInputStream()).start();
			if( m_listenToError)
				new RecoverProcessOutput(process.getErrorStream()).start();
			exitValue=process.waitFor();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		return exitValue==0;
	}
	
	public static void main(String[] args){
		CmdUtility exemple = new CmdUtility(false,false);
		exemple.openApplication("obs32.exe");
		boolean result=exemple.beep();
		System.out.println(result);
		result=exemple.ping("localhost");
		System.out.println(result);
		exemple.openUrl("https://stackoverflow.com/questions/44513063/how-to-open-a-url-with-cmd");
		exemple.killApplication("obs32.exe");
	}
} 