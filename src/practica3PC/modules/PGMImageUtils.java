package practica3PC.modules;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import practica3PC.utils.FileAndFolderUtils;

public class PGMImageUtils extends PGMImage implements Cloneable {

	public PGMImageUtils(List<String> comments, int width, int height, int maxGreyValue, List<List<Integer>> pixelsGreyValues) {
		super(comments, height, width, maxGreyValue, pixelsGreyValues);
	}
	
	public int getPixelValue(int rowIndex, int columnIndex) {
		if (rowIndex >= getHeight() && columnIndex >= getWidth() 
				|| 
			rowIndex < 0 && columnIndex < 0)
				return -1;
		else
			return getPixelsGreyValues().get(rowIndex).get(columnIndex);
	}
	
	public int[][] getNeighbourPixels(int rowIndex, int columnIndex, int outputSize) {
		if (outputSize < 1 || getPixelValue(rowIndex, columnIndex) == -1)
			return new int[][] {{-1}, {-1}};
		else {
			int[][] neighbourPixels = new int[outputSize][outputSize];
			
			for (int i = 0; i < outputSize; i++)
				for (int j = 0; j < outputSize; j++) {
					if ( ((rowIndex-(1-i)) < getPixelsGreyValues().size()) && (rowIndex-(1-i)) >= 0 )  {
						if ((columnIndex-(1-j)) < getPixelsGreyValues().get( rowIndex-(1-i) ).size() && ((columnIndex-(1-j)) >= 0))  {
							neighbourPixels[i][j] = getPixelsGreyValues().get(rowIndex-(1-i)).get(columnIndex-(1-j));						
						} else {
							neighbourPixels[i][j] = 0;
						}
					}						
					else {
						neighbourPixels[i][j] = 0;
					}							
				}
			
			return neighbourPixels;			
		}
			
	}
	
	public int getMaxRows() {
		return getHeight()-1;
	}
	
	public int getMaxColumns() {
		return getWidth()-1;
	}
	
	public void setValueToPixel(int rowIndex, int columnIndex, int newValue) {
		getPixelsGreyValues().get(rowIndex).set(columnIndex, newValue);
	}
	
	public static PGMImageUtils parsePGMFile(String[] linesFromPGMFile) {
		boolean size = false;
		boolean value = false;
		boolean p2Line = true;
		
		List<String> comments = new ArrayList<>();
		
		int width = 0, height = 0, maxValue = 0;
		
		int lineNumber = 0, rowNumber = 0;
		
		List<Integer> row = new ArrayList<>();
		
		String[] values;
		
		List<List<Integer>> rows = new ArrayList<>();
		
		for (String line : linesFromPGMFile) {
			if (!p2Line) { //Si es la primera línea, no hago nada
				if (line.charAt(0) == '#') {
					comments.add(line);
					size = true;
				} else {
					if (!value && !size) { //Esta parte se encarga de parsear las líneas de valores
						values = line.split("\\s+");								
					
						for (String val : values) {
							if (rowNumber == 0)
								row = new ArrayList<>();
							
							if (! val.equals(""))
								row.add(Integer.parseInt(val));
							
							rowNumber++;
							
							if (rowNumber == width || (lineNumber == linesFromPGMFile.length-1 && rowNumber == (values.length-1))) {
								rowNumber = 0;
								rows.add(row);
							}										
						}								
					} else {
						if (size) { //Esta parte se encarga de obtener el ancho y el alto
							String[] widthAndHeight = line.split("\\s+");
							
							width = Integer.parseInt(widthAndHeight[0]);
							height = Integer.parseInt(widthAndHeight[1]); 
							
							value = true;
							
							size = false;
							
							continue;
						} else {
							if (value) { //Esta parte se encarga de obtener el valor máximo de grises para los píxeles
								maxValue = Integer.parseInt(line);
								
								value = false;
								
								continue;
							}							
						}										
					}
				}
			} else {
				if (line.equals("P2")) //Si la primera línea no es "P2", eso quiere decir que el archivo no es un archivo PGM válido
					p2Line = false;
				else 
					break;
			}
			
			lineNumber++;
		}	
		
		return new PGMImageUtils(comments, width, height, maxValue, rows);
	}	
	
	public void writePGMObjectInFile(String filePath) throws IOException {
		FileAndFolderUtils.writeAtEndOfFile(filePath, "P2");
		
		for (String comment : getComments()) {
			FileAndFolderUtils.writeAtEndOfFile(filePath, comment);
		}
		
		FileAndFolderUtils.writeAtEndOfFile(filePath, Integer.toString(getWidth()) + " " + Integer.toString(getHeight()));
		
		FileAndFolderUtils.writeAtEndOfFile(filePath, Integer.toString(getMaxGreyValue()));
		
		List<String> pixels = new ArrayList<>();
		
		String auxString;
		
		for (List<Integer> row : getPixelsGreyValues()) {
			auxString = "";
			for (int number : row) {
				auxString = (auxString.equals(""))? Integer.toString(number) : auxString + " " + Integer.toString(number);
			}
			pixels.add(auxString);
		}
		
		for (String line : pixels) {
			FileAndFolderUtils.writeAtEndOfFile(filePath, line);
		}
	}
	
	@Override
    public Object clone(){
		List<List<Integer>> auxList = new ArrayList<>();
		
        return new PGMImageUtils(getComments(), getWidth(), getHeight(), getMaxGreyValue(), auxList);
    }	
	
//	public PGMImage applyMask(PGMMask mask, boolean pixelBaseCase) {
//		if (pixelBaseCase) 
//			if (getHeight() > getWidth())
//				return applyMaskByRow(mask, 0, getHeight());
//			else
//				return applyMaskByColumn(mask, 0, getWidth());
//		else 
//			return applyMaskByPixel(mask, 0, getHeight(), 0, getWidth());
//	}
//	


}
