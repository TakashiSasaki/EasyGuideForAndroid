package jp.ac.ehime_u.cite.sasaki.easyguide.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import android.graphics.Bitmap;
import android.graphics.Point;
import jp.ac.ehime_u.cite.sasaki.easyguide.exception.ItemNotFoundException;
import jp.ac.ehime_u.cite.sasaki.easyguide.ui.TocItem;
import jp.ac.ehime_u.cite.sasaki.easyguide.util.Log;

public class ItemBase<T extends ItemBase<?>> implements Collection<T> {
	private File directory;
	protected DirectoryName directoryName;
	protected DirectoryImage directoryImage;
	protected ArrayList<T> items;

	// protected ItemType itemType;

	// public ItemType getItemType() {
	// return this.itemType;
	// }

	protected ItemBase(File directory) {
		super();
		this.items = new ArrayList<T>();
		this.directory = directory;
		this.directoryName = new DirectoryName(directory.getName());
		this.directoryImage = new DirectoryImage(directory);
	}

	protected ItemBase() {
		super();
	}

	@Override
	public String toString() {
		if (this.isDummy())
			return "Please choose an item.";
		return this.getTitle();
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

	public boolean isDummy() {
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

	@Override
	public boolean add(T e) {
		Log.v(new Throwable(), "adding (1) " + e.getClass().getSimpleName()
				+ " to " + this.getClass().getSimpleName());
		if (e.getClass().isInstance(Organization.class)) {
			Log.v(new Throwable(), "adding (3) " + e.getClass());
		}
		if (this.items == null) {
			Log.v(new Throwable(), "this.item is null");
		}
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

	public String getTitle() {
		return this.directoryName.getName();
	}

	protected T getByIndex(int index, ItemBase<?> default_item) {
		try {
			return (T) getByIndex(index);
		} catch (ItemNotFoundException e) {
			return (T) default_item;
		}
	}// getByIndex

	protected T getByIndex(int index) throws ItemNotFoundException {
		for (ItemBase<?> i : this.items) {
			if (i.getIndex() == index) {
				return (T) i;
			}
		}
		throw new ItemNotFoundException("Can't find index " + index + " in "
				+ this.getTitle());
	}// getByIndex

	protected T getByTitle(String title, ItemBase<?> default_item) {
		try {
			return (T) getByTitle(title);
		} catch (ItemNotFoundException e) {
			return (T) default_item;
		}
	}// getByTitle

	protected T getByTitle(String title) throws ItemNotFoundException {
		for (ItemBase<?> i : this.items) {
			if (i.getTitle().equals(title)) {
				return (T) i;
			}
		}// for
		throw new ItemNotFoundException("Can't find " + title + " in "
				+ this.getTitle());
	}// getByTitle

	public File getDirectory() {
		return this.directory;
	}

	public int getIndex() {
		return this.directoryName.getNumber();
	}

	public int getX() {
		return this.directoryName.getX();
	}

	public int getY() {
		return this.directoryName.getY();
	}

	public Bitmap getImage() {
		return this.directoryImage.getImage();
	}

	public Bitmap getThumbnail() {
		return this.directoryImage.getThumbnail();
	}

	public TocItem getTocItem() {
		ItemType item_type;
		if (this.getClass().isInstance(Organization.class)) {
			item_type = ItemType.ORGANIZATION_TYPE;
		} else if (this.getClass().isInstance(Building.class)) {
			item_type = ItemType.BUILDING_TYPE;
		} else if (this.getClass().isInstance(Equipment.class)) {
			item_type = ItemType.EQUIPMENT_TYPE;
		} else if (this.getClass().isInstance(Facility.class)) {
			item_type = ItemType.EQUIPMENT_TYPE;
		} else if (this.getClass().isInstance(Floor.class)) {
			item_type = ItemType.FLOOR_TYPE;
		} else if (this.getClass().isInstance(Room.class)) {
			item_type = ItemType.ROOM_TYPE;
		} else {
			item_type = ItemType.UNKNOWN_TYPE;
		}
		// this.layerType = item_base.getItemType();
		TocItem toc_item = new TocItem(item_type, this.getIndex(),
				this.getTitle(), this.getX(), this.getY(), this.getDirectory(),
				this.getThumbnail());
		return toc_item;
	}// getTocItem

	public T getNearest(Point point) throws ItemNotFoundException {
		if (this.items.size() == 0)
			throw new ItemNotFoundException(this.getTitle()
					+ "has no item as children");
		T nearest = this.items.get(0);
		for (T item : this.items) {
			float old_distance_squared = (nearest.getX() - point.x) ^ 2
					+ (nearest.getY() - point.y) ^ 2;
			float new_distance_squared = (item.getX() - point.x) ^ 2
					+ (item.getY() - point.y) ^ 2;
			if (old_distance_squared > new_distance_squared) {
				nearest = item;
			}
		}// for
		return nearest;
	}

	@Override
	public boolean isEmpty() {
		if (items == null)
			return true;
		return this.items.isEmpty();
	}
}// ItemBase
