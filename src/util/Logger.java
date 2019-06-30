package util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
	
	private File file;
	private FileWriter writer;
	private DateTimeFormatter formatter;
	
	public Logger() {
		formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy - HH-mm-ss");
		try {
			this.file = new File(LocalDateTime.now().format(formatter) + ".log");
			this.writer = new FileWriter(this.file);
		}
		catch(IOException | NullPointerException e) {
			e.printStackTrace();
		}
		formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
	}
	
	public void log(String msg) {
		
		try {
			this.writer.write(LocalDateTime.now().format(formatter) + " - " + msg + "\n");
			this.writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
