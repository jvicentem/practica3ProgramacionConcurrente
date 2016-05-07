package practica3PC.modules;

public enum PGMMask {

	HORIZONTAL_SOBEL(new int[][] {{-1, -2, -1},
								  {0, 0, 0},
								  {1, 2, 1}}
	),
	VERTICAL_SOBEL(new int[][] {{-1, 0, 1},
							    {-2, 0, 2},
								{-1, 0, 1}}
	),	
	UNSHARP(new int[][] {{0, -1, 0},
						 {-1, 5, -1},
						 {0, -1, 0}}
	),
	EDGE_DETECTION(new int[][] {{0, 1, 0},
		   						{1, 4, 1},
		   						{0, 1, 0}}
	),
	EMBOSS(new int[][] {{-2, -1, 0},
		   			    {-1, 1, 1},
		   				{0, 1, 2}}
	),
	BLUR(new int[][] {{1, 1, 1},
		   			  {1, 1, 1},
		   			  {1, 1, 1}}
	);
	
	private int[][] maskValues; 	
	
	private PGMMask(int[][] maskValues) {
		this.maskValues = maskValues;
	}
	
	public int applyMaskToPixel(int[][] pixels) { //La máscara se aplica al píxel de la posición 1, 1 de la matriz pixels
		int positiveValuesSum = 0, negativeValuesSum = 0;
		
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				positiveValuesSum = (getMaskValues()[i][j] >= 0)? positiveValuesSum + getMaskValues()[i][j] : positiveValuesSum;
		
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				negativeValuesSum = (getMaskValues()[i][j] < 0)? negativeValuesSum + getMaskValues()[i][j] : negativeValuesSum;		
				
		int value = 0;
		
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				value = value + (getMaskValues()[i][j] * pixels[i][j]);
			}
		}
	
		return Math.abs(value) / Math.max(positiveValuesSum, Math.abs(negativeValuesSum));		
	}
	
	public int getMaskSize() {
		return getMaskValues().length;
	}
	
	private int[][] getMaskValues() {
		return maskValues;
	}
	
}
