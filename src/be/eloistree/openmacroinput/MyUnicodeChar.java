package be.eloistree.openmacroinput;

public class MyUnicodeChar {

	
		// GIST IF IT IS MISSING: https://gist.github.com/EloiStree/37b8e4d02b144284bf729c0491e614c8
		//↕↓↑
			public static final char press='↓';
			public static final char stroke='↕';
			public static final char release='↑';
			public static final char releaseThenPress='⇅';
			public static final char pressThenRelease='⇵';
			public static final String youRock="🤘";
			public static final char watchTime='⏰';
			public static final char split='裂';
			public static final char sandTime= '⌛';
			public static final String mouse="🖱";
			
			public static String arrows() {
				return ""+press+stroke+release;
			}
}	

