package be.eloistree.openmacroinput;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShortCutTransformer {

	public static boolean m_useLog;

	public static void main(String[] args) {

		String command = "C↕600 B↓600 80> A↑600  A↑ 🐁←0.1↓0.2 1000> 🐁A←0.1↓0.2 (🐁left)x2  🐁right 🐁middle ([[Hello !]] [[World]] (a 10> )x3 )x10 2000> ( a↓ 90> a↑  90> space↓  space↑ 1500>)x5";
		if (m_useLog)
			System.out.println(command);
		command = ParseComplexeShortcutToBasicShortCut(command);
		if (m_useLog)
			System.out.println(command);

	}

	public static String ParseComplexeShortcutToBasicShortCut(String text) {

		LinkedList<IdToCompressGroup> compressGroup = new LinkedList<IdToCompressGroup>();
		text = CompressTextGroup(text, compressGroup);
		if (m_useLog)
			System.out.println("Compress:" + text);

		text = ReplaceMouseTypeByMouse(text);
		{
			text = text.toLowerCase();
			text = text.replaceAll(MyUnicodeChar.mouse+"l", MyUnicodeChar.mouse+"leftclick");
			text = text.replaceAll(MyUnicodeChar.mouse+"r", MyUnicodeChar.mouse+"rightclick");
			text = text.replaceAll(MyUnicodeChar.mouse+"c", MyUnicodeChar.mouse+"middleclick");
			text = text.replaceAll(MyUnicodeChar.mouse+"m", MyUnicodeChar.mouse+"middleclick");
	
			text = text.replaceAll(MyUnicodeChar.mouse+"leftclick↓", MyUnicodeChar.mouse+"leftclick↓");
			text = text.replaceAll(MyUnicodeChar.mouse+"rightclick↓", MyUnicodeChar.mouse+"rightclick↓");
			text = text.replaceAll(MyUnicodeChar.mouse+"middleclick↓", MyUnicodeChar.mouse+"middleclick↓");
			text = text.replaceAll(MyUnicodeChar.mouse+"leftclick↑", MyUnicodeChar.mouse+"leftclick↑");
			text = text.replaceAll(MyUnicodeChar.mouse+"rightclick↑", MyUnicodeChar.mouse+"rightclick↑");
			text = text.replaceAll(MyUnicodeChar.mouse+"middleclick↑", MyUnicodeChar.mouse+"middleclick↑");
			text = text.replaceAll(MyUnicodeChar.mouse+"leftclick↕", MyUnicodeChar.mouse+"leftclick");
			text = text.replaceAll(MyUnicodeChar.mouse+"rightclick↕", MyUnicodeChar.mouse+"rightclick");
			text = text.replaceAll(MyUnicodeChar.mouse+"middleclick↕", MyUnicodeChar.mouse+"middleclick");
			
		
			
		}
		text = ReplaceMouseByShortcutEquivalent(text);

		text.replace("scrollup","scroll"+MyUnicodeChar.arrowUp );
		text.replace("scrolldown","scroll"+MyUnicodeChar.arrowDown );
		
		text = replaceMaintainToShortcutEquivalent(text);
		text = replaceMouseMoveShortToShortcutEquivalent(text);

	
		

		text = ReplaceTimeBiggerThenNotation(text);
		IntegerWrapper foundIteration = new IntegerWrapper(1);
		for (int i = 0; foundIteration.m_value > 0 && i < 200; i++) {
			text = UnzipMultiplicator(text, foundIteration);
			// System.out.println("Iteration: "+foundIteration.m_value);
		}

		text = UncompressTextGroup(text, compressGroup);
		 if (m_useLog)
		System.out.println("Uncompress:" + text);
		return text;
	}

	private static String replaceMouseMoveShortToShortcutEquivalent(String text) {

		// 🐁←0.1↓0.2 by default is relative
		// 🐁A←0.1↓0.2 = Mouse move in absolute at 10% of right and 20% of top;
		// 🐁R←0.1↓0.2 = Mouse move in relative to current 10% to the right and 20% to
		// the top;
		// 🖵←0.1↓0.2 = 🐁A←0.1↓0.2
		// Giving the possibility to write:
		// 🐁←0.1 80> 🐁↓0.2 🐁left 🐁←0.3 80> 🐁↓0.5 80> 🐁right 80> 🐁↓500
		// To Do when I have time
		// TODO Auto-generated method stub
		return text;
	}

	private static String regexMaintainStart = "([\\s]+|^)+[a-zA-Z0-9]+";
	private static String regexMaintainEnd = "([\\s]+|$)+";

	private static String replaceMaintainToShortcutEquivalent(String text) {

		// Backspace↓200 = Backspace↓ ⌛600 Backspace↑

		String r = regexMaintainStart + "[" + (MyUnicodeChar.arrows() + "]\\d+") + regexMaintainEnd;

		if (m_useLog) {
			System.out.println("----Replace Maintaint to classic  --");

			System.out.println("Regex: " + r);
		}
		Matcher matcher = Pattern.compile(r).matcher(text);

		int i = 0;
		while (matcher.find()) {
			for (int j = 0; j <= matcher.groupCount(); j++) {

				String gtext = matcher.group(j).trim();
				if (gtext.length() > 0) {
					if (m_useLog) {
						System.out.println("------------------------------------");
						System.out.println("Group " + i + ": " + gtext);
					}
					int index = -1;
					char c = ' ';
					if (c == ' ' && (index = gtext.indexOf(MyUnicodeChar.press)) > 0) {
						c = gtext.charAt(index);
					}
					if (c == ' ' && (index = gtext.indexOf(MyUnicodeChar.release)) > 0) {
						c = gtext.charAt(index);
					}
					if (c == ' ' && (index = gtext.indexOf(MyUnicodeChar.stroke)) > 0) {
						c = gtext.charAt(index);
					}

					if (c != ' ') {

						String name = gtext.substring(0, index);
						String value = gtext.substring(index + 1);
						String toReplaceBy = "";
						if (c == MyUnicodeChar.press || c == MyUnicodeChar.stroke) {

							toReplaceBy = String.format("%s%s %s %s%s", name, MyUnicodeChar.press,
									MyUnicodeChar.sandTime + value, name, MyUnicodeChar.release);
						}
						if (c == MyUnicodeChar.release) {
							toReplaceBy = String.format("%s%s %s %s%s", name, MyUnicodeChar.release,
									MyUnicodeChar.sandTime + value, name, MyUnicodeChar.press);
						}

						text = text.replaceAll(gtext, toReplaceBy);

					}

				}
				i++;
			}
		}

		return text;
	}

	private static String ReplaceMouseByShortcutEquivalent(String text) {
		text = replace(text, MyUnicodeChar.mouse + "left",
				" LeftClick" + MyUnicodeChar.press + " " + "LeftClick" + MyUnicodeChar.release);
		text = replace(text, MyUnicodeChar.mouse + "right",
				" RightClick" + MyUnicodeChar.press + " " + "RightClick" + MyUnicodeChar.release);
		text = replace(text, MyUnicodeChar.mouse + "middle",
				" MiddleClick" + MyUnicodeChar.press + " " + "MiddleClick" + MyUnicodeChar.release);
		return text;
	}

	public static String replace(String source, String target, String replacement) {
		StringBuilder sbSource = new StringBuilder(source);
		StringBuilder sbSourceLower = new StringBuilder(source.toLowerCase());
		String searchString = target.toLowerCase();

		int idx = 0;
		while ((idx = sbSourceLower.indexOf(searchString, idx)) != -1) {
			sbSource.replace(idx, idx + searchString.length(), replacement);
			sbSourceLower.replace(idx, idx + searchString.length(), replacement);
			idx += replacement.length();
		}
		sbSourceLower.setLength(0);
		sbSourceLower.trimToSize();
		sbSourceLower = null;

		return sbSource.toString();
	}

	private static String ReplaceMouseTypeByMouse(String text) {

		return text.replaceAll(
				"[" + MyUnicodeChar.mouseType2 + MyUnicodeChar.mouseType3 + MyUnicodeChar.mouseType4 + "]",
				MyUnicodeChar.mouse);
	}

	private static String regexMultiplicator = "\\([^\\(\\)]+\\)\\s?x\\s?\\d+";

	private static String UnzipMultiplicator(String text, IntegerWrapper foundIteration) {

		foundIteration.m_value = 0;
		if (m_useLog)
			System.out.println("--------------Process: Multiplicator -----------");
		Matcher matcher = Pattern.compile(regexMultiplicator).matcher(text);
		int i = 0;
		while (matcher.find()) {
			for (int j = 0; j <= matcher.groupCount(); j++) {

				String gtext = matcher.group(j).trim();
				// gtext = gtext.replaceAll("[^\\d]","");
				if (gtext.length() > 0) {
					if (m_useLog) {

						System.out.println("------------------------------------");
						System.out.println("Group " + i + ": " + gtext);
					}

					// try {

					int firstIndex = gtext.indexOf('(') + 1;
					int lastIndex = gtext.indexOf(')');

					String content = gtext.substring(firstIndex, lastIndex);

					int multiplyNumber = 5;
					multiplyNumber = Integer.parseInt(gtext.substring(lastIndex).replaceAll("[^\\d]", ""));

					text = text.replace(gtext, " " + textMultiply(content, multiplyNumber));
					// }catch(Exception e) {}
					i++;
				}
			}
		}
		foundIteration.m_value = i;
		return text;
	}

	public static String textMultiply(String text, int multi) {

		String result = "";
		for (int i = 0; i < multi; i++) {
			result += text;
		}
		return result;
	}

	private static String ReplaceTimeBiggerThenNotation(String text) {
		String regex = "([\\s]+|^)\\d+>([\\s]+|$|[^a-zA-Z])";
		// 80> (A↓B↓B↑A↑)x10 80> A↑B↑A↑ ctrl + a | ctrl + k + m↑ 80>
		Matcher matcher = Pattern.compile(regex).matcher(text);
		int i = 0;
		while (matcher.find()) {
			for (int j = 0; j <= matcher.groupCount(); j++) {

				String gtext = matcher.group(j).trim();
				gtext = gtext.replaceAll("[^\\d]", "");
				if (gtext.length() > 0) {
					if (m_useLog) {

						System.out.println("------------------------------------");
						System.out.println("Group " + i + ": " + gtext);
					}
					text = text.replaceAll("([\\s]+|^)" + gtext + ">", " " + MyUnicodeChar.sandTime + gtext + " ");
				}
				i++;
			}
		}

		return text;
	}

	private static String UncompressTextGroup(String text, LinkedList<IdToCompressGroup> compressGroup) {

		for (IdToCompressGroup idToCompressGroup : compressGroup) {
			text = text.replace(MyUnicodeChar.textDocument + idToCompressGroup.m_id, idToCompressGroup.m_text);
		}

		return text;
	}

	private static String regexText = "\\[\\[[^\\[\\]]+\\]\\]";

	private static String CompressTextGroup(String text, LinkedList<IdToCompressGroup> compressGroup) {
		compressGroup.clear();
		int idIndex = 0;

		// [[Hello]] [[World ]] =zip=> 🖹0 🖹1 =unzip=> [[Hello]] [[World ]]
		Matcher matcher = Pattern.compile(regexText).matcher(text);
		int i = 0;
		while (matcher.find()) {
			for (int j = 0; j <= matcher.groupCount(); j++) {

				String gtext = matcher.group(j).trim();
				// gtext = gtext.replaceAll("[^\\d]","");
				if (gtext.length() > 0) {
					compressGroup.add(new IdToCompressGroup(idIndex, gtext));
					if (m_useLog) {

						System.out.println("------------------------------------");
						System.out.println("Group " + i + ": " + gtext);
					}
					text = text.replace(gtext, " " + (MyUnicodeChar.textDocument) + "" + idIndex + " ");
					idIndex++;
				}
				i++;
			}
		}

		return text;
	}

}
