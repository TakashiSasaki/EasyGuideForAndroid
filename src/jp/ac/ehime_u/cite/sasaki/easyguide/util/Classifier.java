package jp.ac.ehime_u.cite.sasaki.easyguide.util;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Classifier {
	private Pattern movieFilePattern = Pattern.compile("^.+\\.m4v$");
	private Pattern htmlFilePattern = Pattern.compile("^(.+\\.html)|(.+\\.htm)$");
	private Pattern textFilePattern = Pattern.compile("^.+\\.txt$");
	private Pattern imageFilePattern = Pattern.compile("^(.+\\.jpg)|(.+\\.jpeg)|(.+\\.png)$");

	private ArrayList<File> movieFiles = new ArrayList<File>();
	private ArrayList<File> htmlFiles = new ArrayList<File>();
	private ArrayList<File> textFiles = new ArrayList<File>();
	private ArrayList<File> imageFiles = new ArrayList<File>();

	File directory;

	public Classifier(File directory) {
		this.directory = directory;

		for (File f : directory.listFiles()) {
			Log.v(new Throwable(), "classyfing " + f.getName());
			Matcher match_movie = this.movieFilePattern.matcher(f.getName());
			if (match_movie.find()) {
				Log.v(new Throwable(), "movie file " + f.getName());
				this.movieFiles.add(f);
				continue;
			}
			Matcher match_html = this.htmlFilePattern.matcher(f.getName());
			if (match_html.find()) {
				Log.v(new Throwable(), "html file" + f.getName());
				this.htmlFiles.add(f);
				continue;
			}
			Matcher match_text = this.textFilePattern.matcher(f.getName());
			if (match_text.find()) {
				Log.v(new Throwable(), "text file " + f.getName());
				this.textFiles.add(f);
				continue;
			}
			Matcher match_image = this.imageFilePattern.matcher(f.getName());
			if (match_image.find()) {
				Log.v(new Throwable(), "image file " + f.getName());
				this.imageFiles.add(f);
				continue;
			}
			Log.v(new Throwable(), "not classified, " + f.getName());
		}// for
	}// a constructor

	public ArrayList<File> getMovieFiles() {
		return this.movieFiles;
	}

	public ArrayList<File> getHtmlFiles() {
		return this.htmlFiles;
	}

	public ArrayList<File> getTextFiles() {
		return this.textFiles;
	}

	public ArrayList<File> getImageFiles() {
		return this.imageFiles;
	}

	public void FilterTest() {
		for (File file : this.directory.listFiles(new FileFilter() {
			@Override
			public boolean accept(File arg0) {
				return arg0.getName().matches(
						"(\\.png^|\\.gif^|\\.jpg^|\\.jpeg^)");
			}// accept
		}// FileFilter()
				)// listFiles
		) {
			// body of for loop
		}// for
	}// FilterTest

}// Classifier
