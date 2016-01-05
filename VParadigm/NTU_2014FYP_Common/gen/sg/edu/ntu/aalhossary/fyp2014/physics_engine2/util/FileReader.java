package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.*;

public class FileReader {
	
	public static List<String> readFile(String filePath) {
		List<String> lines = new ArrayList<String>();
	    File file = new File(filePath);
		Charset charset = Charset.defaultCharset();
		Path path = Paths.get(file.getAbsolutePath());

		try {
			lines = Files.readAllLines(path, charset);
		} catch (IOException e) {
			System.out.println(e);
		}
//		for (String line : lines) {
//			System.out.println(line);
//		}
		return lines;
	}
}
