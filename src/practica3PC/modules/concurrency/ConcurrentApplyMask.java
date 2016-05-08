package practica3PC.modules.concurrency;

import java.security.InvalidParameterException;
import java.util.concurrent.RecursiveAction;

import practica3PC.modules.PGMImageUtils;
import practica3PC.modules.PGMMask;

public class ConcurrentApplyMask 
								extends RecursiveAction 
								implements Runnable {
	
	private PGMImageUtils pgmImageUtils;
	private int rowStartIndex, rowEndIndex, columnStartIndex, columnEndIndex;
	private PGMMask pgmMask;
	private PGMImageUtils outputPGMImage;
	
	public ConcurrentApplyMask(PGMImageUtils pgmImageUtils, PGMMask pgmMask, int rowStartIndex, int rowEndIndex, int columnStartIndex, int columnEndIndex, PGMImageUtils outputPGMImage) {
		if (rowStartIndex >= 0 && columnStartIndex >= 0 && rowEndIndex >= 0 && columnEndIndex >= 0 
				&& 
			rowStartIndex <= pgmImageUtils.getMaxRows()+1 && columnStartIndex <= pgmImageUtils.getMaxColumns()+1 && rowEndIndex <= pgmImageUtils.getMaxRows()+1 && columnEndIndex <= pgmImageUtils.getMaxColumns()+1
				&& 
			rowStartIndex <= rowEndIndex && columnStartIndex <= columnEndIndex) {
			this.rowStartIndex = rowStartIndex;
			this.rowEndIndex = rowEndIndex;
			this.columnStartIndex = columnStartIndex;
			this.columnEndIndex = columnEndIndex;
		} else throw new InvalidParameterException("Índices no válidos");
		
		this.pgmImageUtils = pgmImageUtils;
		this.pgmMask = pgmMask;
		this.outputPGMImage = outputPGMImage;
	}

	@Override
	public void run() {
		calculateNewValueForPixel(getRowStartIndex(), getColumnStartIndex());
	}

	@Override
	public void compute() {
		if (getPgmImageUtils().getMaxRows() < getPgmImageUtils().getMaxColumns())
			applyMaskByRows();
		else
			applyMaskByColumns();
	}
	
	private void applyMaskByRows() {
		if (Math.abs(getRowStartIndex() - getRowEndIndex()) == 1) 		
			for (int j = 0; j <= getPgmImageUtils().getMaxColumns(); j++)  //Itero por los distintos elementos de la fila
				calculateNewValueForPixel(getRowStartIndex(), j);		
	    else {
	    	int startRowFirstHalf = 0, endRowFirstHalf = 0, startRowSecondHalf = 0, endRowSecondHalf = 0;
	    	
			if (getRowStartIndex() < getRowEndIndex()/2) {
				startRowFirstHalf = getRowStartIndex();
				endRowFirstHalf = getRowEndIndex()/2;
				
				startRowSecondHalf = getRowEndIndex()/2;
				endRowSecondHalf = getRowEndIndex();
			} else 
				if (getRowStartIndex() == getRowEndIndex()/2) {
					startRowFirstHalf = getRowStartIndex();
					endRowFirstHalf = (getRowEndIndex()/2)+1;
					
					startRowSecondHalf = ((getRowEndIndex()/2)+1);
					endRowSecondHalf = getRowEndIndex();									
				} else 
					if (getRowStartIndex() > getRowEndIndex()/2) {
						startRowFirstHalf = getRowStartIndex();
						endRowFirstHalf = (getRowEndIndex()-1);
						
						startRowSecondHalf = (getRowEndIndex()-1);
						endRowSecondHalf = getRowEndIndex();													
					}			
			
			ConcurrentApplyMask firstHalf = new ConcurrentApplyMask(getPgmImageUtils(), getPgmMask(), startRowFirstHalf, endRowFirstHalf, getColumnStartIndex(), getColumnEndIndex(), getOutputPGMImage());
			
			firstHalf.fork();
			new ConcurrentApplyMask(getPgmImageUtils(), getPgmMask(), startRowSecondHalf, endRowSecondHalf, getColumnStartIndex(), getColumnEndIndex(), getOutputPGMImage()).compute();
			firstHalf.join();
	    }
	}
	
	private void applyMaskByColumns() {
		if (Math.abs(getColumnStartIndex() - getColumnEndIndex()) == 1) 			
			for (int i = 0; i <= getPgmImageUtils().getMaxRows(); i++)  //Itero por los distintos elementos de la columna
				calculateNewValueForPixel(i, getColumnStartIndex());			 
	    else {
	    	int startColumnFirstHalf = 0, endColumnFirstHalf = 0, startColumnSecondHalf = 0, endColumnSecondHalf = 0;
	    	
			if (getColumnStartIndex() < getColumnEndIndex()/2) {
				startColumnFirstHalf = getColumnStartIndex();
				endColumnFirstHalf = getColumnEndIndex()/2;
				
				startColumnSecondHalf = getColumnEndIndex()/2;
				endColumnSecondHalf = getColumnEndIndex();
			} else 
				if (getColumnStartIndex() == getColumnEndIndex()/2) {
					startColumnFirstHalf = getColumnStartIndex();
					endColumnFirstHalf = ((getColumnEndIndex()/2)+1);
					
					startColumnSecondHalf = ((getColumnEndIndex()/2)+1);
					endColumnSecondHalf = getColumnEndIndex();			
				} else 
					if (getColumnStartIndex() > getColumnEndIndex()/2) {
						startColumnFirstHalf = getColumnStartIndex();
						endColumnFirstHalf = (getColumnEndIndex()-1);
						
						startColumnSecondHalf = getColumnEndIndex()-1;
						endColumnSecondHalf = getColumnEndIndex();									
					}	
			
			ConcurrentApplyMask firstHalf = new ConcurrentApplyMask(getPgmImageUtils(), getPgmMask(), getRowStartIndex(), getRowEndIndex(), startColumnFirstHalf, endColumnFirstHalf, getOutputPGMImage());	
			firstHalf.fork();
			new ConcurrentApplyMask(getPgmImageUtils(), getPgmMask(), getRowStartIndex(), getRowEndIndex(), startColumnSecondHalf, endColumnSecondHalf, getOutputPGMImage()).compute();	
			firstHalf.join();
	    }	
	}
	
	private void calculateNewValueForPixel(int rowIndex, int columnIndex) {
		int[][] neighbourPixels = getPgmImageUtils().getNeighbourPixels(rowIndex,
																		columnIndex,
																		getPgmMask().getMaskSize());			

		int newValue = getPgmMask().applyMaskToPixel(neighbourPixels);

		getOutputPGMImage().setValueToPixel(rowIndex,
											columnIndex,
											newValue);			
	}

	private PGMImageUtils getOutputPGMImage() {
		return outputPGMImage;
	}

	private PGMImageUtils getPgmImageUtils() {
		return pgmImageUtils;
	}

	private int getRowStartIndex() {
		return rowStartIndex;
	}

	private int getRowEndIndex() {
		return rowEndIndex;
	}

	private int getColumnStartIndex() {
		return columnStartIndex;
	}

	private int getColumnEndIndex() {
		return columnEndIndex;
	}
	
	private PGMMask getPgmMask() {
		return pgmMask;
	}
	
}
