package jp.ac.ehime_u.cite.sasaki.easyguide.downloader;

import android.app.AlertDialog;
import android.content.Context;

public class Glossary extends AlertDialog.Builder {
	Context context;

	public Glossary(Context context_) {
		super(context_);
		this.context = context_;
		setTitle(R.string.glossary);
		setItems(
				new String[] {
						GetItem(R.string.domain, R.string.domainDefinition),
						GetItem(R.string.url, R.string.urlDefinition),
						GetItem(R.string.lastModified,
								R.string.lastModifiedDefinition),
						GetItem(R.string.downloadedFile,
								R.string.downloadedFileDefinition),
						GetItem(R.string.downloadedTime,
								R.string.downloadTimeDefinition),
						GetItem(R.string.organization,
								R.string.organizationDefinition) }, null);
	}// a constructor

	private String GetItem(int term, int definition) {
		String term_string = this.context.getResources().getString(term);
		String definition_string = this.context.getResources().getString(
				definition);
		return term_string + ": " + definition_string;
	}// GetItem

}// Glossary
