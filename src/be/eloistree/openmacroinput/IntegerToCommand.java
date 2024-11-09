package be.eloistree.openmacroinput;

import java.util.ArrayList;

import be.eloistree.debug.CDebug;
import be.eloistree.openmacroinput.convertiontables.KeyEventAsString;
import be.eloistree.openmacroinput.enums.PressType;

public class IntegerToCommand {
	 public int m_integerValue;
	 public String m_nameAsString;
	 public String m_command;
	 
	 public IntegerToCommand(String integerId, String command) {
		 m_command = command;
		 m_integerValue =  Integer.parseInt(integerId);
		 m_integerValue = KeyEventAsString.ConvertStringToKeyEvent(integerId);
		 m_nameAsString =integerId; 
	 }
	 
	 public int getId() {return m_integerValue;}
	 public String getIntegerAsString() {return m_nameAsString; }
	 public String getCommand() {return m_command; }
	
	 
	 public static String [] GetAllEnumNames() {
		 return KeyEventAsString.GetAllEnumNames();
	 }
	 public static boolean IsRegistered(String keyName) {
		 for (int i = 0; i < KeyEventAsString.ALLENUM.length; i++) {
			if(keyName.equalsIgnoreCase(KeyEventAsString.ALLENUM[i]))
				return true;
		}
		 return false;
	 }
	 
	 public static ArrayList<IntegerToCommand> getKeysFromDefaultTextTable(){
		 
		 return getKeysFromTextTable(DEFAULTINTEGERTOCOMMANDS);
	 } 
	 public static ArrayList<IntegerToCommand> getKeysFromTextTable(String text){
		 ArrayList<IntegerToCommand> result = new ArrayList<IntegerToCommand>();
		 String name="", cmd="";
		 String [] lines = text.split("\n");
		 for (int i = 0; i < lines.length; i++) {
			 
			 String line = lines[i];
		 // Delimiter for splitting
	        String split = ":";

	        // Variables to hold the split parts
	        String leftPart = "";
	        String rightPart = "";

	        // Find the index of the delimiter
	        int splitIndex = line.indexOf(split);

	        // If delimiter is found, extract the left and right parts
	        if (splitIndex != -1) {
	            leftPart = line.substring(0, splitIndex); // Part before the delimiter
	            rightPart = line.substring(splitIndex + split.length()); // Part after the delimiter
	        } else {
	            // If no delimiter is found, assign the entire line to leftPart
	            leftPart = line;
	        }
			if(rightPart.length()>0)
			{
				name = leftPart.trim();
				cmd = rightPart.trim();
				if(name.length()>0 && cmd.length()>0) {
					//System.out.println("?: "+name+">"+cmd);
					//if(IsRegistered(name))
					{
						
						try {
							
						result .add(new IntegerToCommand(name, cmd));
						}catch(Exception e) {}
					}
				}
				
			}
		}
		 return result;
	 }
	 public static String getDefaultIntegerToCommands() {return DEFAULTINTEGERTOCOMMANDS; }
	 
	 //↕↓↑
	 //https://github.com/EloiStree/2024_08_29_ScratchToWarcraft/tree/main
	 //Default is based on the Scratch To Warcraft that is based on Window Input
	 private static String DEFAULTINTEGERTOCOMMANDS=""
			 +"1009 :sc:VK_TAB↓ \r\n  2009 :sc:VK_TAB↑ \r\n "                             
			 +"1012 :sc:VK_CLEAR↓ \r\n  2012 :sc:VK_CLEAR↑ \r\n "
			 +"1013 :sc:VK_ENTER↓ \r\n  2013 :sc:VK_ENTER↑ \r\n "
			 +"1016 :sc:VK_SHIFT↓ \r\n  2016 :sc:VK_SHIFT↑ \r\n "
			 +"1017 :sc:VK_CONTROL↓ \r\n  2017 :sc:VK_CONTROL↑ \r\n "
			 +"1018 :sc:VK_ALT↓ \r\n  2018 :sc:VK_ALT↑ \r\n "
			 +"1019 :sc:VK_PAUSE↓ \r\n  2019 :sc:VK_PAUSE↑ \r\n "
			 +"1020 :sc:VK_CAPS_LOCK↓ \r\n  2020 :sc:VK_CAPS_LOCK↑ \r\n "
			 +"1027 :sc:VK_ESCAPE↓ \r\n  2027 :sc:VK_ESCAPE↑ \r\n "
			 +"1032 :sc:VK_SPACE↓ \r\n  2032 :sc:VK_SPACE↑ \r\n "
			 +"1033 :sc:VK_PAGE_UP↓ \r\n  2033 :sc:VK_PAGE_UP↑ \r\n "
			 +"1034 :sc:VK_PAGE_DOWN↓ \r\n  2034 :sc:VK_PAGE_DOWN↑ \r\n "
			 +"1035 :sc:VK_END↓ \r\n  2035 :sc:VK_END↑ \r\n "
			 +"1036 :sc:VK_HOME↓ \r\n  2036 :sc:VK_HOME↑ \r\n "
			 +"1037 :sc:Left↓ \r\n  2037 :sc:Left↑ \r\n "
			 +"1038 :sc:Up↓ \r\n  2038 :sc:Up↑ \r\n "
			 +"1039 :sc:Right↓ \r\n  2039 :sc:Right↑ \r\n "
			 +"1040 :sc:Down↓ \r\n  2040 :sc:Down↑ \r\n "
			 +"1041 :sc:Select↓ \r\n  2041 :sc:Select↑ \r\n "
			 +"1042 :sc:PRINTSCREEN↓ \r\n  2042 :sc:PRINTSCREEN↑ \r\n "
			 +"1043 :sc:Execute↓ \r\n  2043 :sc:Execute↑ \r\n "
			 +"1044 :sc:PRINTSCREEN↓ \r\n  2044 :sc:PRINTSCREEN↑ \r\n "
			 +"1045 :sc:VK_INSERT↓ \r\n  2045 :sc:VK_INSERT↑ \r\n "
			 +"1046 :sc:VK_DELETE↓ \r\n  2046 :sc:VK_DELETE↑ \r\n "
			 +"1048 :sc:0↓ \r\n  2048 :sc:0↑ \r\n "
			 +"1049 :sc:1↓ \r\n  2049 :sc:1↑ \r\n "
			 +"1050 :sc:2↓ \r\n  2050 :sc:2↑ \r\n "
			 +"1051 :sc:3↓ \r\n  2051 :sc:3↑ \r\n "
			 +"1052 :sc:4↓ \r\n  2052 :sc:4↑ \r\n "
			 +"1053 :sc:5↓ \r\n  2053 :sc:5↑ \r\n "
			 +"1054 :sc:6↓ \r\n  2054 :sc:6↑ \r\n "
			 +"1055 :sc:7↓ \r\n  2055 :sc:7↑ \r\n "
			 +"1056 :sc:8↓ \r\n  2056 :sc:8↑ \r\n "
			 +"1057 :sc:9↓ \r\n  2057 :sc:9↑ \r\n "
			 +"1065 :sc:A↓ \r\n  2065 :sc:A↑ \r\n "
			 +"1066 :sc:B↓ \r\n  2066 :sc:B↑ \r\n "
			 +"1067 :sc:C↓ \r\n  2067 :sc:C↑ \r\n "
			 +"1068 :sc:D↓ \r\n  2068 :sc:D↑ \r\n "
			 +"1069 :sc:E↓ \r\n  2069 :sc:E↑ \r\n "
			 +"1070 :sc:F↓ \r\n  2070 :sc:F↑ \r\n "
			 +"1071 :sc:G↓ \r\n  2071 :sc:G↑ \r\n "
			 +"1072 :sc:H↓ \r\n  2072 :sc:H↑ \r\n "
			 +"1073 :sc:I↓ \r\n  2073 :sc:I↑ \r\n "
			 +"1074 :sc:J↓ \r\n  2074 :sc:J↑ \r\n "
			 +"1075 :sc:K↓ \r\n  2075 :sc:K↑ \r\n "
			 +"1076 :sc:L↓ \r\n  2076 :sc:L↑ \r\n "
			 +"1077 :sc:M↓ \r\n  2077 :sc:M↑ \r\n "
			 +"1078 :sc:N↓ \r\n  2078 :sc:N↑ \r\n "
			 +"1079 :sc:O↓ \r\n  2079 :sc:O↑ \r\n "
			 +"1080 :sc:P↓ \r\n  2080 :sc:P↑ \r\n "
			 +"1081 :sc:Q↓ \r\n  2081 :sc:Q↑ \r\n "
			 +"1082 :sc:R↓ \r\n  2082 :sc:R↑ \r\n "
			 +"1083 :sc:S↓ \r\n  2083 :sc:S↑ \r\n "
			 +"1084 :sc:T↓ \r\n  2084 :sc:T↑ \r\n "
			 +"1085 :sc:U↓ \r\n  2085 :sc:U↑ \r\n "
			 +"1086 :sc:V↓ \r\n  2086 :sc:V↑ \r\n "
			 +"1087 :sc:W↓ \r\n  2087 :sc:W↑ \r\n "
			 +"1088 :sc:X↓ \r\n  2088 :sc:X↑ \r\n "
			 +"1089 :sc:Y↓ \r\n  2089 :sc:Y↑ \r\n "
			 +"1090 :sc:Z↓ \r\n  2090 :sc:Z↑ \r\n "
			 +"1091 :sc:VK_WINDOWS↓ \r\n  2091 :sc:VK_WINDOWS↑ \r\n "
			 +"1092 :sc:RightWindows↓ \r\n  2092 :sc:RightWindows↑ \r\n "
			 +"1093 :sc:CONTEXT_MENU↓ \r\n  2093 :sc:CONTEXT_MENU↑ \r\n "
			 +"1095 :sc:Sleep↓ \r\n  2095 :sc:Sleep↑ \r\n "
			 +"1096 :sc:VK_NUMPAD0↓ \r\n  2096 :sc:VK_NUMPAD0↑ \r\n "
			 +"1097 :sc:VK_NUMPAD1↓ \r\n  2097 :sc:VK_NUMPAD1↑ \r\n "
			 +"1098 :sc:VK_NUMPAD2↓ \r\n  2098 :sc:VK_NUMPAD2↑ \r\n "
			 +"1099 :sc:VK_NUMPAD3↓ \r\n  2099 :sc:VK_NUMPAD3↑ \r\n "
			 +"1100 :sc:VK_NUMPAD4↓ \r\n  2100 :sc:VK_NUMPAD4↑ \r\n "
			 +"1101 :sc:VK_NUMPAD5↓ \r\n  2101 :sc:VK_NUMPAD5↑ \r\n "
			 +"1102 :sc:VK_NUMPAD6↓ \r\n  2102 :sc:VK_NUMPAD6↑ \r\n "
			 +"1103 :sc:VK_NUMPAD7↓ \r\n  2103 :sc:VK_NUMPAD7↑ \r\n "
			 +"1104 :sc:VK_NUMPAD8↓ \r\n  2104 :sc:VK_NUMPAD8↑ \r\n "
			 +"1105 :sc:VK_NUMPAD9↓ \r\n  2105 :sc:VK_NUMPAD9↑ \r\n "
			 +"1106 :sc:VK_MULTIPLY↓ \r\n  2106 :sc:VK_MULTIPLY↑ \r\n "
			 +"1107 :sc:VK_ADD↓ \r\n  2107 :sc:VK_ADD↑ \r\n "
			 +"1108 :sc:Separator↓ \r\n  2108 :sc:Separator↑ \r\n "
			 +"1109 :sc:VK_SUBTRACT↓ \r\n  / :sc:VK_SUBTRACT↑ \r\n "
			 +"1110 :sc:VK_DECIMAL↓ \r\n  2110 :sc:VK_DECIMAL↑ \r\n "
			 +"1111 :sc:VK_DIVIDE↓ \r\n  2111 :sc:VK_DIVIDE↑ \r\n "
			 +"1112 :sc:VK_F1↓ \r\n  2112 :sc:VK_F1↑ \r\n "
			 +"1113 :sc:VK_F2↓ \r\n  2113 :sc:VK_F2↑ \r\n "
			 +"1114 :sc:VK_F3↓ \r\n  2114 :sc:VK_F3↑ \r\n "
			 +"1115 :sc:VK_F4↓ \r\n  2115 :sc:VK_F4↑ \r\n "
			 +"1116 :sc:VK_F5↓ \r\n  2116 :sc:VK_F5↑ \r\n "
			 +"1117 :sc:VK_F6↓ \r\n  2117 :sc:VK_F6↑ \r\n "
			 +"1118 :sc:VK_F7↓ \r\n  2118 :sc:VK_F7↑ \r\n "
			 +"1119 :sc:VK_F8↓ \r\n  2119 :sc:VK_F8↑ \r\n "
			 +"1120 :sc:VK_F9↓ \r\n  2120 :sc:VK_F9↑ \r\n "
			 +"1121 :sc:VK_F10↓ \r\n  2121 :sc:VK_F10↑ \r\n "
			 +"1122 :sc:VK_F11↓ \r\n  2122 :sc:VK_F11↑ \r\n "
			 +"1123 :sc:VK_F12↓ \r\n  2123 :sc:VK_F12↑ \r\n "
			 +"1144 :sc:VK_NUM_LOCK↓ \r\n  2144 :sc:VK_NUM_LOCK↑ \r\n "
			 +"1145 :sc:VK_SCROLL_LOCK↓ \r\n  2145 :sc:VK_SCROLL_LOCK↑ \r\n "
			 +"1160 :sc:VK_SHIFT↓ \r\n  2160 :sc:VK_SHIFT↑ \r\n "
			 +"1161 :sc:RightShift↓ \r\n  2161 :sc:RightShift↑ \r\n "
			 +"1162 :sc:VK_CONTROL↓ \r\n  2162 :sc:VK_CONTROL↑ \r\n "
			 +"1163 :sc:RightControl↓ \r\n  2163 :sc:RightControl↑ \r\n "
			 +"1164 :sc:VK_ALT↓ \r\n  2164 :sc:VK_ALT↑ \r\n "
			 +"1165 :sc:VK_ALT_GRAPH↓ \r\n  2165 :sc:VK_ALT_GRAPH↑ \r\n "
			 +"1164 :sc:LeftMenu↓ \r\n  2164 :sc:LeftMenu↑ \r\n "
			 +"1165 :sc:RightMenu↓ \r\n  2165 :sc:RightMenu↑ \r\n "
			 +"1166 :sc:BrowserBack↓ \r\n  2166 :sc:BrowserBack↑ \r\n "
			 +"1167 :sc:BrowserForward↓ \r\n  2167 :sc:BrowserForward↑ \r\n "
			 +"1168 :sc:BrowserRefresh↓ \r\n  2168 :sc:BrowserRefresh↑ \r\n "
			 +"1169 :sc:BrowserStop↓ \r\n  2169 :sc:BrowserStop↑ \r\n "
			 +"1170 :sc:BrowserSearch↓ \r\n  2170 :sc:BrowserSearch↑ \r\n "
			 +"1171 :sc:BrowserFavorites↓ \r\n  2171 :sc:BrowserFavorites↑ \r\n "
			 +"1172 :sc:BrowserHome↓ \r\n  2172 :sc:BrowserHome↑ \r\n "
			 +"1173 :sc:VolumeMute↓ \r\n  2173 :sc:VolumeMute↑ \r\n "
			 +"1174 :sc:VolumeDown↓ \r\n  2174 :sc:VolumeDown↑ \r\n "
			 +"1175 :sc:VolumeUp↓ \r\n  2175 :sc:VolumeUp↑ \r\n "
			 +"1176 :sc:MediaNextTrack↓ \r\n  2176 :sc:MediaNextTrack↑ \r\n "
			 +"1177 :sc:MediaPreviousTrack↓ \r\n  2177 :sc:MediaPreviousTrack↑ \r\n "
			 +"1178 :sc:MediaStop↓ \r\n  2178 :sc:MediaStop↑ \r\n "
			 +"1179 :sc:MediaPlay↓ \r\n  2179 :sc:MediaPlay↑ \r\n "
			 +"1180 :sc:LaunchMail↓ \r\n  2180 :sc:LaunchMail↑ \r\n "
			 +"1181 :sc:LaunchMediaSelect↓ \r\n  2181 :sc:LaunchMediaSelect↑ \r\n "
			 +"1182 :sc:LaunchApp1↓ \r\n  2182 :sc:LaunchApp1↑ \r\n "
			 +"1183 :sc:LaunchApp2↓ \r\n  2183 :sc:LaunchApp2↑ \r\n "
			 +"1186 :sc:OEM1↓ \r\n  2186 :sc:OEM1↑ \r\n "
			 +"1187 :sc:OEMPlus↓ \r\n  2187 :sc:OEMPlus↑ \r\n "
			 +"1188 :sc:OEMComma↓ \r\n  2188 :sc:OEMComma↑ \r\n "
			 +"1189 :sc:OEMMinus↓ \r\n  2189 :sc:OEMMinus↑ \r\n "
			 +"1190 :sc:OEMPeriod↓ \r\n  2190 :sc:OEMPeriod↑ \r\n "
			 +"1191 :sc:OEM2↓ \r\n  2191 :sc:OEM2↑ \r\n "
			 +"1192 :sc:OEM3↓ \r\n  2192 :sc:OEM3↑ \r\n "
			 +"1219 :sc:OEM4↓ \r\n  2219 :sc:OEM4↑ \r\n "
			 +"1220 :sc:OEM5↓ \r\n  2220 :sc:OEM5↑ \r\n "
			 +"1221 :sc:OEM6↓ \r\n  2221 :sc:OEM6↑ \r\n "
			 +"1222 :sc:OEM7↓ \r\n  2222 :sc:OEM7↑ \r\n "
			 +"1223 :sc:OEM8↓ \r\n  2223 :sc:OEM8↑ \r\n "
			 +"1226 :sc:OEM102↓ \r\n  2226 :sc:OEM102↑ \r\n "
			 +"1229 :sc:ProcessKey↓ \r\n  2229 :sc:ProcessKey↑ \r\n "
			 +"1231 :sc:Packet↓ \r\n  2231 :sc:Packet↑ \r\n "
			 +"1246 :sc:Attn↓ \r\n  2246 :sc:Attn↑ \r\n "
			 +"1247 :sc:CrSel↓ \r\n  2247 :sc:CrSel↑ \r\n "
			 +"1248 :sc:ExSel↓ \r\n  2248 :sc:ExSel↑ \r\n "
			 +"1249 :sc:EraseEOF↓ \r\n  2249 :sc:EraseEOF↑ \r\n "
			 +"1250 :sc:Play↓ \r\n  2250 :sc:Play↑ \r\n "
			 +"1251 :sc:Zoom↓ \r\n  2251 :sc:Zoom↑ \r\n "
			 +"1253 :sc:PA1↓ \r\n  2253 :sc:PA1↑ \r\n " 
			 
	 		;
}



