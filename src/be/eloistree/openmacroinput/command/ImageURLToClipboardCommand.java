package be.eloistree.openmacroinput.command;

public class ImageURLToClipboardCommand extends RobotCommand{

	public String m_imageUrl;
	
	
	public ImageURLToClipboardCommand(String url){
		
		m_imageUrl = url;
	}
	
	@Override
	public String toString() {
		return String.format("%s:%s", "Image 2 Clipboard",m_imageUrl);
	}

	public String getUrl() {
		return m_imageUrl;
	}
}
