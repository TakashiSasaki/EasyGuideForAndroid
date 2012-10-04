package jp.ac.ehime_u.cite.sasaki.easyguide.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import jp.ac.ehime_u.cite.sasaki.easyguide.content.DirectoryImage;
import jp.ac.ehime_u.cite.sasaki.easyguide.content.DirectoryName;
import jp.ac.ehime_u.cite.sasaki.easyguide.exception.ItemNotFoundException;
import jp.ac.ehime_u.cite.sasaki.easyguide.ui.TocItem;
import jp.ac.ehime_u.cite.sasaki.easyguide.util.Log;

public class ItemBase<T extends ItemBase<?, ?>, S extends ItemBase<?, ?>>
		implements Collection<S> {
	private File directory;
	protected DirectoryName directoryName;
	protected DirectoryImage directoryImage;
	protected ArrayList<S> items;

	// protected ItemType itemType;

	// public ItemType getItemType() {
	// return this.itemType;
	// }

	protected ItemBase(File directory) {
		super();
		this.items = new ArrayList<S>();
		this.directory = directory;
		this.directoryName = new DirectoryName(directory.getName());
		//this.directoryImage = new DirectoryImage(); // it should not work
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
	public Iterator<S> iterator() {
		return this.items.iterator();
	}

	protected File[] listFiles() {
		return this.directory.listFiles();
	}

	public String getTitle() {
		return this.directoryName.name;
	}

	protected S getByIndex(int index, S default_item) {
		try {
			return getByIndex(index);
		} catch (ItemNotFoundException e) {
			return default_item;
		}
	}// getByIndex

	protected S getByIndex(int index) throws ItemNotFoundException {
		for (S i : this.items) {
			if (i.getIndex() == index) {
				return i;
			}
		}
		throw new ItemNotFoundException("Can't find index " + index + " in "
				+ this.getTitle());
	}// getByIndex

	protected S getByTitle(String title, S default_item) {
		try {
			return getByTitle(title);
		} catch (ItemNotFoundException e) {
			return default_item;
		}
	}// getByTitle

	protected S getByTitle(String title) throws ItemNotFoundException {
		for (S s : this.items) {
			if (s.getTitle().equals(title)) {
				return s;
			}
		}// for
		throw new ItemNotFoundException("Can't find " + title + " in "
				+ this.getTitle());
	}// getByTitle

	public File getDirectory() {
		return this.directory;
	}

	public int getIndex() {
		return this.directoryName.number;
	}

	public int getX() {
		return this.directoryName.x;
	}

	public int getY() {
		return this.directoryName.y;
	}

	public Bitmap getImage(Context context) throws Exception {
		return this.directoryImage.getBitmap(context);
	}

	public Bitmap getThumbnail(Context context) throws Exception {
		return this.directoryImage.getThumbnail(context);
	}

	public TocItem getTocItem(Context context) throws Exception {
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
				this.getThumbnail(context));
		return toc_item;
	}// getTocItem

	public S getNearest(Point point) throws ItemNotFoundException {
		if (this.items.size() == 0)
			throw new ItemNotFoundException(this.getTitle()
					+ "has no item as children");
		S nearest = this.items.get(0);
		for (S item : this.items) {
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
		if (this.items == null)
			return true;
		return this.items.isEmpty();
	}

	@Override
	public boolean add(S e) {
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
	public boolean addAll(Collection<? extends S> c) {
		return this.items.addAll(c);
	}

}// ItemBase
