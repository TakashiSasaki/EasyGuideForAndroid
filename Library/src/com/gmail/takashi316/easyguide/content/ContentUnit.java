package com.gmail.takashi316.easyguide.content;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import com.gmail.takashi316.easyguide.exception.InvalidDirectoryNameException;

public class ContentUnit {
	private Classifier _classifier;
	private DirectoryName _directoryName;
	public ArrayList<Integer> contentPath; // 0=organization 1=facility
											// 2=building 3=floor
											// 4=room 5=equipment 6=panel
	// private int _level;
	private ContentUnit _parent;
	private ArrayList<ContentUnit> children;
	private File _directory;
	private int siblingIndex;

	// public int siblingIndex() {
	// return this.identifier[this._level];
	// }

	@SuppressWarnings({ "boxing", "unchecked" })
	public ContentUnit(File directory, ContentUnit parent, int sibling_index)
			throws FileNotFoundException {

		this._directory = directory;
		this._parent = parent;
		this._directoryName = new DirectoryName(directory.getName());
		this._classifier = new Classifier(directory);
		this.siblingIndex = sibling_index;

		// detecting level consistency
		if (parent != null) {
			this.contentPath = (ArrayList<Integer>) parent.contentPath.clone();
		} else {
			this.contentPath.add(this.siblingIndex);
		}// if

		MyLogger.info("directory = " + directory.getAbsolutePath());

		// traversing children
		this.children = new ArrayList<ContentUnit>();
		for (File child_directory : directory.listFiles()) {
			if (!child_directory.isDirectory())
				continue;
			try {
				ContentUnit child_content_unit = new ContentUnit(
						child_directory, this, children.size() + 1);
				this.children.add(child_content_unit);
			} catch (InvalidDirectoryNameException e) {
				MyLogger.info(e.toString());
				break;
			}// try
		}// for

	}// a constructor

	public String getName() {
		return this._directoryName.name;
	}

	public int getIndString() {
		return this._directoryName.number;
	}

	public int getX() {
		return this._directoryName.x;
	}

	public int getY() {
		return this._directoryName.y;
	}

	public File getDirectory() {
		return this._directory;
	}

	public ContentUnit getParent() {
		return this._parent;
	}

	public ArrayList<ContentUnit> getSiblings() {
		return this._parent.children;
	}

	public ArrayList<ContentUnit> getChildren() {
		return this.children;
	}

	public ArrayList<ContentUnit> getAncestors() {
		ArrayList<ContentUnit> ancestors = new ArrayList<ContentUnit>();
		for (ContentUnit p = this._parent; p != null; p = p._parent) {
			ancestors.add(p);
		}
		return ancestors;
	}

	public ArrayList<ContentUnit> getSiblingsAndAncestors() {
		ArrayList<ContentUnit> sa = new ArrayList<ContentUnit>();
		for (ContentUnit s : this.getSiblings()) {
			sa.add(s);
		}
		for (ContentUnit a : this.getAncestors()) {
			sa.add(a);
		}
		return sa;
	}

	public ContentUnit getChild(int index) {
		assert (index > 0);
		return this.children.get(index - 1);
	}

	public boolean hasMovie() {
		return this._classifier.getMovieFiles().size() > 0;
	}

	public boolean hasText() {
		return this._classifier.getTextFiles().size() > 0;
	}

	public boolean hasHtml() {
		return this._classifier.getHtmlFiles().size() > 0;
	}

	public boolean hasImageFile() {
		return this._classifier.getImageFiles().size() > 0;
	}

	public File getImageFile() {
		return this._classifier.getImageFiles().get(0);
	}

	public File getMovieFile() {
		return this._classifier.getMovieFiles().get(0);
	}

	public File getTextFile() {
		return this._classifier.getTextFiles().get(0);
	}

	public File getHtmlFile() {
		return this._classifier.getHtmlFiles().get(0);
	}

	public ContentUnit getNearestChild(float bitmap_x, float bitmap_y) {
		ContentUnit nearest_child = null;
		float min_distance_squared = 2000 * 2000;
		for (ContentUnit child : getChildren()) {
			if (child.getX() < 0 || child.getY() < 0)
				continue;
			float dx = child.getX() - bitmap_x;
			float dy = child.getY() - bitmap_y;
			float distance = dx * dx + dy * dy;
			if (min_distance_squared > distance) {
				min_distance_squared = distance;
				nearest_child = child;
			}
		}
		if (nearest_child != null)
			return nearest_child;
		if (getChildren().size() == 0)
			return null;
		return getChild(1);
	}// getNearestChild

	static public void main(String[] args) throws FileNotFoundException {
		final String directory_path = "/C:/Users/sasaki/Google ドライブ/Billable/EasyGuide-contents/EASYGUIDE/www.yonden.co.jp/01 四国電力/01 四国電力保安研修所";
		final File directory = new File(directory_path);
		ContentUnit content_unit = new ContentUnit(directory, null, 1);
		ContentUnit content_unit_2 = content_unit.getChild(1).getChild(2);
		MyLogger.info("children " + content_unit_2.getChildren().size());
		MyLogger.info("siblings " + content_unit_2.getSiblings().size());
		MyLogger.info("ancestors " + content_unit_2.getAncestors().size());
		for (ContentUnit cu : content_unit_2.getAncestors()) {
			MyLogger.info(cu.getName());
		}
		MyLogger.info("siblings and ancestors "
				+ content_unit_2.getSiblingsAndAncestors().size());
	}// main
}// class ContentUnit
