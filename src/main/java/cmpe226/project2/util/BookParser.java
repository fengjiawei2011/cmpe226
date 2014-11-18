package cmpe226.project2.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * Note: Errors in file's information part: 
 *  1104.txt: Change "Tile:" to "Title:"
 *  1922.txt.: Change "Author" to "Title" and "Title" to "Author
 *  
 * Usage: new BookParser(file_path).processLineByLine()
 * Return: HashMap<String, String>
 */
public class BookParser {
	private final Path filePath;
	private final static Charset ENCODING = StandardCharsets.UTF_8;
	private HashMap<String, String> info = new HashMap<>();
	private final static Pattern start = Pattern.compile("^Title:.*");
	private final static Pattern end = Pattern.compile(".*\\*");
	private final static Pattern keyValue = Pattern.compile("([^:\\[]*):([^\\[\\]]*).*");
	private final static Pattern date = Pattern.compile("([a-zA-Z]+\\s*\\d*\\s*,\\s*\\d+)");
	private final static Pattern author = Pattern.compile("(?:by\\s)?(?:\\(.*\\))?(((?!\\sand|,|\\().)*)((?:\\(.*\\)\\s)(.+))?");

	public BookParser(String path) {
		filePath = Paths.get(path);
	}

	public HashMap<String, String> processLineByLine() {
		try (Scanner scanner = new Scanner(filePath, ENCODING.name())) {

			// find start line
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine().trim();
				if (line.equals("")) {
//					System.out.println("[Empty Line]");
				} else if (start.matcher(line).matches()) {
//					System.out.println("[Start Parse]");
					processLine(line);
					break;
				}
			}

			// process information
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine().trim();
				if (line.equals("")) {
//					System.out.println("[Empty line]");
				} else if (end.matcher(line).matches()) {
//					System.out.println(line);
//					System.out.println("[End Parse]");
					break;
				} else {
					processLine(line);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// clean author: by, and, (...), keep first author
		if (info.containsKey("author")) {
			Matcher authorMatcher = author.matcher(info.get("author"));
			
			if (authorMatcher.find()) {
				String firstAuthor = authorMatcher.group(1).trim();
				String tmp = authorMatcher.group(4);
				if (tmp != null)
					firstAuthor += " " + tmp;
				info.put("first_author", firstAuthor);
			}
		}
		else {
			info.put("author", "No Author");
		}
		return info;
	}

	private void processLine(String line) {
//		System.out.println("[Process]" + line);
		Matcher matcher = keyValue.matcher(line);
		if (matcher.matches() && matcher.groupCount() == 2) {
			String key = matcher.group(1).trim().toLowerCase().replace(" ", "_");
			// abandon long keys
			if (key.length() < 23) {
				info.put(key, matcher.group(2).trim());
			}
			else {
				System.out.println("[Abandon] " + key);
			}
			
		} else if (!info.containsKey("release_date")) {
			// find date
//			System.out.println("Date Matching...");
			Matcher dateMatcher = date.matcher(line);
			if (dateMatcher.find()) {
//				System.out.println("[Date] " + dateMatcher.group(1));
				info.put("release_date", dateMatcher.group(1));
			}
		}
	}

	public static void main(String[] args) throws IOException {
		// 1981, 17135, 1593, 1670, 1952, 2347, 1271, 1272, 1494, 1019
		String[] books = new File("/Users/lan/Desktop/226/books/").list(); //{"35.txt"};
		int count = 0;
		File file = new File("./books.txt");
		BufferedWriter output = new BufferedWriter(new FileWriter(file));

		for (String book : books) {
			BookParser parser = new BookParser("/Users/lan/Desktop/226/books/" + book);
			HashMap<String, String> result = parser.processLineByLine();
			output.append("\n" + book + "\n");
			for (String k : result.keySet()) {
				output.append(k + " : " + result.get(k) + "\n");
			}
			count++;
		}
		output.close();
		System.out.println(count + " books parsed.");
	}

}