package jp.ac.ehime_u.cite.sasaki.easyguide.content;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Logger;

import jp.ac.ehime_u.cite.sasaki.easyguide.exception.InvalidDirectoryNameException;

public class ContentUnit {
	private DirectoryImage _directoryImage;
	private DirectoryName _directoryName;
	public int[] identifier = new int[6]; // 0=facility 1=building 2=floor
											// 3=room 4=equipment 5=panel
	private int _level;
	private ContentUnit _parent;
	private ContentUnit[] _children;
	private File _directory;

	public ContentUnit(File directory, ContentUnit parent) {
		this._directory = directory;
		this._parent = parent;
		this._directoryName = new DirectoryName(directory.getName());
		this._directoryImage = new DirectoryImage(directory);

		// detecting level consistency
		if (parent == null) {
			this._level = 0;
		} else {
			for (this._level = 0; this._level < parent.identifier.length; this._level++) {
				if (parent.identifier[this._level] == 0)
					break;
			}
			assert (this._level == parent._level + 1);
		}// if

		// traversing children
		ArrayList<ContentUnit> children = new ArrayList<ContentUnit>();
		for (File child_directory : directory.listFiles()) {
			ContentUnit child_content_unit = new ContentUnit(child_directory,
					this);
			children.add(child_content_unit);
		}
		this._children = (ContentUnit[]) children.toArray();
		this._children = null;
	}// a constructor
}// class ContentUnit
