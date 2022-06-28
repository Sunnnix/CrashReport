package de.snx.crashReport;

import java.awt.Dialog.ModalityType;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

public class CrashReport {

	private static String[] crashMessages = { "You hurt me ;(", "Something bad happened",
			"CANAN!\nWhat have you done again?" };

	private static String filePath = "";
	private static String message_path = "";
	private static boolean window = true;
	private static boolean file = true;
	private static boolean message = false;
	private static boolean message_ext = false;
	private static Runnable onCrashWindowClose = () -> System.exit(-1);

	public static void setProperties(boolean showWindow, boolean createFile, boolean showMessage,
			boolean useExternalMessage) {
		window = showWindow;
		file = createFile;
		message = showMessage;
		message_ext = useExternalMessage;
	}

	/**
	 * the path where the crash reports are saved
	 */
	public static void setFilePath(String path) {
		filePath = path;
	}

	/**
	 * each line of this file is a message that is displayed before the actual crash
	 * report
	 */
	public static void setExternalMessageFile(String filePath) {
		message_path = filePath;
	}

	/**
	 * onCrashWindowClose is executed when the crash window is closed. <br>
	 * By default, the application is closed here.
	 */
	public static void setOnCrashWindowClose(Runnable onCrashWindowClose) {
		CrashReport.onCrashWindowClose = onCrashWindowClose;
	}

	/**
	 * returns a new unhandledExceptionHandler with the current properties
	 */
	public static UncaughtExceptionHandler getExceptionHandler(Runnable preReport, Runnable postReport) {
		final String i_filePath = filePath;
		final String i_message_path = message_path;
		final boolean i_showWindow = window;
		final boolean i_createFile = file;
		final boolean i_showMessage = message;
		final boolean i_useExternalMessage = message_ext;
		final String[] i_crashMessages = i_useExternalMessage ? readStrings(i_message_path) : crashMessages;
		final Runnable i_onCrashWindowClose = onCrashWindowClose;
		return (t, e) -> {
			if (preReport != null)
				preReport.run();
			String crashText = createCrashText(e, i_showMessage, i_crashMessages);
			if (i_showWindow)
				createWindow(crashText, i_onCrashWindowClose);
			if (i_createFile)
				writeFile(createFiles(i_filePath), crashText);
			if (postReport != null)
				postReport.run();
		};
	}

	private static String[] readStrings(String path) {
		LinkedList<String> list = new LinkedList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(new File(path)))) {
			reader.lines().forEach(list::add);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list.toArray(String[]::new);
	}

	private static String createCrashText(Throwable e, boolean showMessage, String[] crashMessages) {
		StringBuilder builder = new StringBuilder();
		if (showMessage && crashMessages.length > 0)
			builder.append(crashMessages[(int) (Math.random() * crashMessages.length)] + "\n\n");
		builder.append(e.toString() + "\n");
		for (StackTraceElement el : e.getStackTrace())
			builder.append("\t" + el.toString() + "\n");
		return builder.toString();
	}

	private static void createWindow(String crashText, Runnable i_onCrashWindowClose) {
		new CrashWindow(null, "Game Crashed!", crashText, ModalityType.APPLICATION_MODAL, i_onCrashWindowClose);
	}

	private static File createFiles(String path) {
		File dictonary = new File(path);
		if (dictonary.exists() == false)
			dictonary.mkdirs();
		File file = new File(dictonary.getAbsolutePath() + "/crash-report_" + getTime() + ".txt");
		return file;
	}

	private static String getTime() {
		return new SimpleDateFormat("MM-dd-yyyy HH.mm.ss.SSS").format(new Date());
	}

	private static void writeFile(File file, String crashText) {
		try (FileWriter writer = new FileWriter(file)) {
			writer.write(crashText);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
