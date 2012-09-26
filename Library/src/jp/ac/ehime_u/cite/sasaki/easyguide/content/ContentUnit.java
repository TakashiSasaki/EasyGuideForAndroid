package jp.ac.ehime_u.cite.sasaki.easyguide.content;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import jp.ac.ehime_u.cite.sasaki.easyguide.exception.InvalidDirectoryNameException;

public class ContentUnit {
	private Classifier _classifier;
	private DirectoryName _directoryName;
	public int[] identifier = new int[7]; // 0=organization 1=facility
											// 2=building 3=floor
											// 4=room 5=equipment 6=panel
	private int _level;
	private ContentUnit _parent;
	private ContentUnit[] _children;
	private File _directory;

	public int siblingIndex() {
		return this.identifier[this._level];
	}

	public ContentUnit(File directory, ContentUnit parent) {
		this._directory = directory;
		this._parent = parent;
		this._directoryName = new DirectoryName(directory.getName());
		this._classifier = new Classifier(directory);

		// detecting level consistency
		if (parent == null) {
			this._level = 0;
		} else {
			for (this._level = 0; this._level < parent.identifier.length; this._level++) {
				if (parent.identifier[this._level] == 0)
					break;
				this.identifier[this._level] = parent.identifier[this._level];
			}
			assert (this._level == parent._level + 1);
		}// if

		this.identifier[this._level] = this._directoryName.number;

		Logger.getGlobal().info("directory = " + directory.getAbsolutePath());

		// traversing children
		ArrayList<ContentUnit> children = new ArrayList<ContentUnit>();
		for (File child_directory : directory.listFiles()) {
			if (!child_directory.isDirectory())
				continue;
			try {
				ContentUnit child_content_unit = new ContentUnit(
						child_directory, this);
				children.add(child_content_unit);
			} catch (InvalidDirectoryNameException e) {
				Logger.getGlobal().info(e.toString());
				break;
			}
		}

		// TODO: care for sparse numbering
		this._children = new ContentUnit[children.size()];
		for (ContentUnit child : children) {
			this._children[child.siblingIndex() - 1] = child;
		}
		children = null;
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

	public ContentUnit getParent() {
		return this._parent;
	}

	public ContentUnit[] getSiblings() {
		if (this._parent == null) {
			return new ContentUnit[] {};
		}
		return this._parent._children;
	}

	public ContentUnit[] getChildren() {
		return this._children;
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
		return this._children[index - 1];
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

	static public void main(String[] args) {
		Logger.getGlobal().setLevel(Level.INFO);
		final String directory_path = "/C:/Users/sasaki/Google ドライブ/Billable/EasyGuide-contents/EASYGUIDE/www.yonden.co.jp/01 四国電力/01 四国電力保安研修所";
		final File directory = new File(directory_path);
		ContentUnit content_unit = new ContentUnit(directory, null);
		ContentUnit content_unit_2 = content_unit.getChild(1).getChild(2);
		Logger.getGlobal().info(
				"children " + content_unit_2.getChildren().length);
		Logger.getGlobal().info(
				"siblings " + content_unit_2.getSiblings().length);
		Logger.getGlobal().info(
				"ancestors " + content_unit_2.getAncestors().size());
		for (ContentUnit cu : content_unit_2.getAncestors()) {
			Logger.getGlobal().info(cu.getName());
		}
		Logger.getGlobal().info(
				"siblings and ancestors "
						+ content_unit_2.getSiblingsAndAncestors().size());
	}
}// class ContentUnit
