package de.slag.updown;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

@ManagedBean
@SessionScoped
public class UadController {

	private static final Log LOG = LogFactory.getLog(UadController.class);
	
	private static final String version = "0.0.1";
	
	private static final String NOT_SUCCESSFUL = "Not Successful";
	private static final String SUCCESFUL = "Succesful";

	private final String workdir = "/tmp/up-and-down/";

	private Collection<Msg> messages = new ArrayList<>();
	
	public String getAppVersion() throws IOException {
		return version;
	}

	public String getMessages() {
		final List<Msg> c = new ArrayList<>(messages);
		final Comparator<Msg> comparator = new Comparator<Msg>() {

			@Override
			public int compare(Msg o1, Msg o2) {
				return o2.date.compareTo(o1.date);
			}
		};
		Collections.sort(c, comparator);
		final StringBuilder sb = new StringBuilder();
		c.forEach(m -> sb.append(m.date + ": " + m.message + "\n"));
		return sb.toString();
	}

	public void fileUploadListener(FileUploadEvent e) {
		assertWorkdir();
		UploadedFile file = e.getFile();
		try {
			file.write(workdir + file.getFileName());
		} catch (Exception e1) {
			LOG.error("error write file", e1);
			message(NOT_SUCCESSFUL, e1.getMessage());
			return;
		}
		message(SUCCESFUL,
				"Uploaded File Name Is :: " + file.getFileName() + " :: Uploaded File Size :: " + file.getSize());
	}

	private void assertWorkdir() {
		final File file = new File(workdir);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	private void message(final String title, final String message) {
		appendMessage(title + ": " + message);
	}

	private void appendMessage(String s) {
		messages.add(new Msg(s));
	}

	public void downloadFile(final String s) throws IOException {
		File file = new File(s);

		FacesContext facesContext = FacesContext.getCurrentInstance();

		HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();

		final List<String> asList = Arrays.asList(s.split("/"));
		String filename = "file.bin";
		for (String string : asList) {
			filename = string;
		}

		response.reset();
		response.setHeader("Content-Type", "application/octet-stream");
		response.setHeader("Content-Disposition", "attachment;filename=" + filename);

		OutputStream responseOutputStream = response.getOutputStream();

		InputStream fileInputStream = new FileInputStream(file);

		byte[] bytesBuffer = new byte[2048];
		int bytesRead;
		while ((bytesRead = fileInputStream.read(bytesBuffer)) > 0) {
			responseOutputStream.write(bytesBuffer, 0, bytesRead);
		}

		responseOutputStream.flush();

		fileInputStream.close();
		responseOutputStream.close();

		facesContext.responseComplete();
		message(SUCCESFUL, "download: " + s);
	}

	public Collection<String> getFiles() {
		assertWorkdir();
		final File file = new File(workdir);
		final String[] list = file.list();
		final List<String> asList = Arrays.asList(list);
		return asList.stream().map(f -> workdir + f).collect(Collectors.toList());
	}

	private class Msg {

		public Msg(final String s) {
			this.message = s;
			this.date = new Date();
		}

		Date date;

		String message;
	}

}
