package de.snx.crashReport;

import java.awt.Dialog.ModalityType;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;

public class CrashReport {

	private static String[] crashMessage = { "You hurt me ;(", "Something bad happened",
			"CANAN!\nWhat have you done again?" };

	public CrashReport(Exception exception, JFrame window, String path) {
		String crashText = createCrashText(exception);
		createCrashWindow(crashText, window);
		File file = createFiles(path);
		writeFile(file, crashText);
	}

	private String createCrashText(Exception exception) {
		String crashText = new String();
		crashText += crashMessage[(int) (Math.random() * crashMessage.length)] + "\n\n";
		crashText += exception.toString() + "\n";
		StackTraceElement[] el = exception.getStackTrace();
		for (StackTraceElement stackTraceElement : el) {
			crashText += "\t" + stackTraceElement.toString() + "\n";
		}
		return crashText;
	}
	
	private void createCrashWindow(String crashText, JFrame window) {
		new CrashWindow(window, "Game Crashed!", crashText, ModalityType.APPLICATION_MODAL);
	}
	
	private File createFiles(String path) {
		File dictonary = new File(path + "/crash-report");
		if (dictonary.exists() == false)
			dictonary.mkdirs();
		File file = new File(dictonary.getAbsolutePath() + "/crash-report_" + getTime() + ".txt");
		return file;
	}
	
	private String getTime() {
		return new SimpleDateFormat("MM-DD-YYYY HH.mm.ss.SS").format(new Date());
	}
	
	private void writeFile(File file, String crashText) {
		try (FileWriter writer = new FileWriter(file)) {
			writer.write(crashText);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
