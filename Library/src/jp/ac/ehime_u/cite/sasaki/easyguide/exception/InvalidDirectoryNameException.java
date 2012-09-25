package jp.ac.ehime_u.cite.sasaki.easyguide.exception;

public class InvalidDirectoryNameException extends RuntimeException{
	public InvalidDirectoryNameException(String directory_name){
		super(directory_name);
	}
}
