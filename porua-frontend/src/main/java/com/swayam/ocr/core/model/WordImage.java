package com.swayam.ocr.core.model;

public class WordImage {

	private long id;
	private String imageFileName;
	private String tesseractValue;
	private String actualVale;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getImageFileName() {
		return imageFileName;
	}

	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}

	public String getTesseractValue() {
		return tesseractValue;
	}

	public void setTesseractValue(String tesseractValue) {
		this.tesseractValue = tesseractValue;
	}

	public String getActualVale() {
		return actualVale;
	}

	public void setActualVale(String actualVale) {
		this.actualVale = actualVale;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WordImage other = (WordImage) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
