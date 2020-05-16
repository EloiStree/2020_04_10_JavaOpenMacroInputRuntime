package be.eloistree.copyfromweb;

/**
 * @author Jigar
 */
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.image.*;
import java.io.*;
import java.net.URL;

import javax.imageio.ImageIO;

//Source: https://stackoverflow.com/questions/4552045/copy-bufferedimage-to-clipboard
public class ImageToClipboard implements ClipboardOwner {
    public ImageToClipboard(String wantedUrlImage) {
        try {
            Robot robot = new Robot();
            Dimension screenSize  = Toolkit.getDefaultToolkit().getScreenSize();
            Rectangle screen = new Rectangle( screenSize );
            
            //Source: https://stackoverflow.com/questions/35884305/why-use-imageio-cant-get-bufferedimage-from-url
            Image image = null;
            try {
                URL url = new URL(wantedUrlImage);
                image = ImageIO.read(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            //BufferedImage i = robot.createScreenCapture( screen );
            TransferableImage trans = new TransferableImage( image );
            Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
            c.setContents( trans, this );
        }
        catch ( AWTException x ) {
            x.printStackTrace();
            System.exit( 1 );
        }
    }

    public static void main( String[] arg ) {
    	ImageToClipboard ci = new ImageToClipboard("https://pic.clubic.com/v1/images/1730786/raw");
    }

    public void lostOwnership( Clipboard clip, Transferable trans ) {
        System.out.println( "Lost Clipboard Ownership" );
    }

    private class TransferableImage implements Transferable {

        Image i;

        public TransferableImage( Image i ) {
            this.i = i;
        }

        public Object getTransferData( DataFlavor flavor )
        throws UnsupportedFlavorException, IOException {
            if ( flavor.equals( DataFlavor.imageFlavor ) && i != null ) {
                return i;
            }
            else {
                throw new UnsupportedFlavorException( flavor );
            }
        }

        public DataFlavor[] getTransferDataFlavors() {
            DataFlavor[] flavors = new DataFlavor[ 1 ];
            flavors[ 0 ] = DataFlavor.imageFlavor;
            return flavors;
        }

        public boolean isDataFlavorSupported( DataFlavor flavor ) {
            DataFlavor[] flavors = getTransferDataFlavors();
            for ( int i = 0; i < flavors.length; i++ ) {
                if ( flavor.equals( flavors[ i ] ) ) {
                    return true;
                }
            }

            return false;
        }
    }
}