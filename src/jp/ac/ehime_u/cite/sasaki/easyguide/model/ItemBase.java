package jp.ac.ehime_u.cite.sasaki.easyguide.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import android.graphics.Bitmap;
import jp.ac.ehime_u.cite.sasaki.easyguide.exception.ItemNotFoundException;

public class ItemBase<T extends ItemBase<?>> implements Collection<T> {
	private File directory;
	protected DirectoryName directoryName;
	protected DirectoryImage directoryImage;
	protected ArrayList<T> items;
	protected ItemType itemType;

	public ItemType getItemType() {
		return this.itemType;
	}

	protected ItemBase(ItemType item_type, File directory) {
		this.itemType = item_type;
		this.items = new ArrayList<T>();
		this.directory = directory;
		this.directoryName = new DirectoryName(directory.getName());
		this.directoryImage = new DirectoryImage(directory);
	}

	public ItemBase(ItemType item_type) {
		this.itemType = item_type;
	}

	@Override
	public String toString() {
		if (this.items == null)
			return "";
		return this.directoryName.getName();
	}

	public String getTitle() {
		return this.directoryName.getName();
	}

	@Override
	public void clear() {
		this.items.clear();
	}

	@Override
	public boolean contains(Object o) {
		return this.items.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return this.items.containsAll(c);
	}

	@Override
	public boolean isEmpty() {
		return this.items == null;
	}

	@Override
	public boolean remove(Object o) {
		return this.items.remove(o);
	}// remove

	@Override
	public boolean removeAll(Collection<?> c) {
		return this.items.removeAll(c);
	}// removeAll

	@Override
	public boolean retainAll(Collection<?> c) {
		return this.items.retainAll(c);
	}// retainAll

	@Override
	public int size() {
		return this.items.size();
	}// size

	@Override
	public Object[] toArray() {
		return this.items.toArray();
	}// toArray

	@Override
	public <T> T[] toArray(T[] a) {
		return this.items.toArray(a);
	}// toArray

	public void sortByIndex() {
		Comparator<ItemBase> item_base_comparator = new Comparator<ItemBase>() {

			@Override
			public int compare(ItemBase arg0, ItemBase arg1) {
				return arg0.getIndex() - arg1.getIndex();
			}
		};
		Collections.sort(this.items, item_base_comparator);
	}// SortByPanelNumber

	public ItemBase<?> getByIndex(int index, ItemBase<?> default_item) {
		try {
			return getByIndex(index);
		} catch (ItemNotFoundException e) {
			return default_item;
		}
	}// getByIndex

	public ItemBase<?> getByIndex(int index) throws ItemNotFoundException {
		for (ItemBase<?> i : this.items) {
			if (i.getIndex() == index) {
				return i;
			}
		}
		throw new ItemNotFoundException("Can't find index " + index + " in "
				+ this.getTitle());
	}// getByIndex

	public ItemBase<?> getByTitle(String title, ItemBase<?> default_item) {
		try {
			return getByTitle(title);
		} catch (ItemNotFoundException e) {
			return default_item;
		}
	}// getByTitle

	public ItemBase<?> getByTitle(String title) throws ItemNotFoundException {
		for (ItemBase<?> i : this.items) {
			if (i.getTitle().equals(title)) {
				return i;
			}
		}// for
		throw new ItemNotFoundException("Can't find " + title + " in "
				+ this.getTitle());
	}// getByTitle

	@Override
	public boolean add(T e) {
		return this.items.add(e);
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		return this.items.addAll(c);
	}

	@Override
	public Iterator<T> iterator() {
		return this.items.iterator();
	}

	protected File[] listFiles() {
		return this.directory.listFiles();
	}

	public File getDirectory() {
		return this.directory;
	}

	@SuppressWarnings("javadoc")
	public int getIndex() {
		return this.directoryName.getNumber();
	}

	@SuppressWarnings("javadoc")
	public int getX() {
		return this.directoryName.getX();
	}

	@SuppressWarnings("javadoc")
	public int getY() {
		return this.directoryName.getY();
	}

	@SuppressWarnings("javadoc")
	public Bitmap getImage() {
		return this.directoryImage.getImage();
	}

	@SuppressWarnings("javadoc")
	public Bitmap getThumbnail() {
		return this.directoryImage.getThumbnail();
	}

}// ItemBase
