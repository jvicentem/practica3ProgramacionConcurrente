package practica3PC.modules;

import java.util.List;

public class PGMImage {
	private List<String> comments;
	private int height, width;
	private int maxGreyValue;
	private List<List<Integer>> pixelsGreyValues;
	
	public PGMImage(List<String> comments, int width, int height, int maxGreyValue, List<List<Integer>> pixelsGreyValues) {
		this.comments = comments;
		this.height = height;
		this.width = width;
		this.maxGreyValue = maxGreyValue;
		this.pixelsGreyValues = pixelsGreyValues;
	}
		
	public boolean isAValidPGMImage() {
		return validMaxGreyValue() && validComments() && validPixelsGreyValues();
	}
	
	private boolean validMaxGreyValue() {
		return getMaxGreyValue() < 256;
	}
	
	private boolean validComments() {
		for (String comment : getComments())
			if (comment.charAt(0) == '#') return false;
		
		return true;
	}
	
	private boolean validPixelsGreyValues() {
		for (List<Integer> row : getPixelsGreyValues())
			for (int val : row)
				if (val > 256 || val < 0) 
					return false;
		
		return true;
	}
	
	protected List<String> getComments() {
		return comments;
	}

	protected int getHeight() {
		return height;
	}

	protected int getWidth() {
		return width;
	}

	protected int getMaxGreyValue() {
		return maxGreyValue;
	}

	protected List<List<Integer>> getPixelsGreyValues() {
		return pixelsGreyValues;
	}

}
