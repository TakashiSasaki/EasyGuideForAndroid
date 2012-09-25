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
		this._children = null;
	}// a constructor

	static public void main(String[] args) {
		Logger.getGlobal().setLevel(Level.INFO);
		final String directory_path = "/C:/Users/sasaki/Google ドライブ/Billable/EasyGuide-contents/EASYGUIDE/www.yonden.co.jp/01 四国電力";
		final File directory = new File(directory_path);
		ContentUnit content_unit = new ContentUnit(directory, null);
	}
}// class ContentUnit
