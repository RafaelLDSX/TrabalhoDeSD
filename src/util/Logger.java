package util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
	
	private File file;
	private FileWriter writer;
	
	public Logger() {
		try {
			this.file = new File("log.log");
			this.writer = new FileWriter(this.file);
		}
		catch(IOException | NullPointerException e) {
			e.printStackTrace();
		}
	}
	
	public void log(String msg) {
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		try {
			this.writer.write(LocalDateTime.now().format(fmt) + " - " + msg + "\n");
			this.writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
