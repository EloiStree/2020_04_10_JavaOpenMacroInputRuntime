package be.eloistree.openmacroinput.audio;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

//Source: https://stackoverflow.com/questions/13789063/get-sound-from-a-url-with-java
public class PlaySoundFromUrl {
	
	

	public static String urlGlobal="";
	public static void PlayWav(String url) {
		urlGlobal=url;
		Thread t = new Thread() {
			 public void run() {
		 AudioInputStream din = null;
		    try {
		        AudioInputStream in = AudioSystem.getAudioInputStream(new URL(urlGlobal));
		        AudioFormat baseFormat = in.getFormat();
		        AudioFormat decodedFormat = new AudioFormat(
		                AudioFormat.Encoding.PCM_SIGNED,
		                baseFormat.getSampleRate(), 16, baseFormat.getChannels(),
		                baseFormat.getChannels() * 2, baseFormat.getSampleRate(),
		                false);
		        din = AudioSystem.getAudioInputStream(decodedFormat, in);
		        DataLine.Info info = new DataLine.Info(SourceDataLine.class, decodedFormat);
		        SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
		        if(line != null) {
		            line.open(decodedFormat);
		            byte[] data = new byte[4096];
		            // Start
		            line.start();

		            int nBytesRead;
		            while ((nBytesRead = din.read(data, 0, data.length)) != -1) {
		                line.write(data, 0, nBytesRead);
		            }
		            // Stop
		            line.drain();
		            line.stop();
		            line.close();
		            din.close();
		        }

		    }
		    catch(Exception e) {
		        e.printStackTrace();
		    }
		    finally {
		        if(din != null) {
		            try { din.close(); } catch(IOException e) { }
		        }
		    }
		    }
		};
		
		t.start();
	}
}
