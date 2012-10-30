package com.gmail.takashi316.easyguide.content;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class TextLoader {
	String text;

	public TextLoader() {
		this.text = "";
	}

	public void loadTextFromFile(File file) throws IOException {
		FileReader file_reader = new FileReader(file);
		BufferedReader buffer_reader = new BufferedReader(file_reader);
		StringBuilder sb = new StringBuilder();
		String s;
		while ((s = buffer_reader.readLine()) != null) {
			sb.append(s + "\n");
		}// while
		buffer_reader.close();
		this.text = sb.toString();
	}

	public String getText() {
		return this.text;
	}

}
