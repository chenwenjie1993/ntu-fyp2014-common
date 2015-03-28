package sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;

public class FileWriter {

	public void writeAminoSequence() {
		// TODO - implement FileWriter.writeAminoSequence
		throw new UnsupportedOperationException();
	}

	public static void writePDBFile(File file, String modelToPDB) {
		try {
			BufferedWriter writer = new BufferedWriter(new java.io.FileWriter(file.getAbsoluteFile()));
			writer.write(modelToPDB);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}