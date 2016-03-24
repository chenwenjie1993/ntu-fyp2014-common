package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.util;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class LogFormatter extends Formatter {
	@Override
	public String format(LogRecord record) {
		return record.getMessage() + "\r\n";
	}
}
