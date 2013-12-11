package com.gmail.takashi316.easyguide.content;

import java.util.HashMap;

@SuppressWarnings("serial")
public class Glossary extends HashMap<String, ContentUnit> {
	public Glossary(ContentUnit root_content_unit) {
		this.put(root_content_unit);
	}

	private void put(ContentUnit content_unit) {
		this.put(content_unit.getName(), content_unit);
		for (ContentUnit cu : content_unit.getChildren()) {
			this.put(cu);
		}
	}
}
