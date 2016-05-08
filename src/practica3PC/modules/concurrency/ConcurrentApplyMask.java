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
			rowStartIndex <= pgmImageUtils.getMaxRows() && columnStartIndex <= pgmImageUtils.getMaxColumns() && rowEndIndex <= pgmImageUtils.getMaxRows() && columnEndIndex <= pgmImageUtils.getMaxColumns()
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
			for (int j = 0; j < getPgmImageUtils().getMaxColumns(); j++)  //Itero por los distintos elementos de la fila
				calculateNewValueForPixel(getRowStartIndex(), j);			 
	    else 
			if (getRowStartIndex() < getRowEndIndex()/2) {
				new ConcurrentApplyMask(getPgmImageUtils(), getPgmMask(), getRowStartIndex(), getRowEndIndex()/2, getColumnStartIndex(), getColumnEndIndex(), getOutputPGMImage()).fork();	
				new ConcurrentApplyMask(getPgmImageUtils(), getPgmMask(), getRowEndIndex()/2, getRowEndIndex(), getColumnStartIndex(), getColumnEndIndex(), getOutputPGMImage()).compute();
			} else 
				if (getRowStartIndex() == getRowEndIndex()/2) {
					new ConcurrentApplyMask(getPgmImageUtils(), getPgmMask(), getRowStartIndex(), ((getRowEndIndex()/2)+1), getColumnStartIndex(), getColumnEndIndex(), getOutputPGMImage()).fork();	
					new ConcurrentApplyMask(getPgmImageUtils(), getPgmMask(), ((getRowEndIndex()/2)+1), getRowEndIndex(), getColumnStartIndex(), getColumnEndIndex(), getOutputPGMImage()).compute();					
				} else 
					if (getRowStartIndex() > getRowEndIndex()/2) {
						new ConcurrentApplyMask(getPgmImageUtils(), getPgmMask(), getRowStartIndex(), (getRowEndIndex()-1), getColumnStartIndex(), getColumnEndIndex(), getOutputPGMImage()).fork();			
						new ConcurrentApplyMask(getPgmImageUtils(), getPgmMask(), (getRowEndIndex()-1), getRowEndIndex(), getColumnStartIndex(), getColumnEndIndex(), getOutputPGMImage()).compute();							
					}			
	}
	
	private void applyMaskByColumns() {
		if (Math.abs(getColumnStartIndex() - getColumnEndIndex()) == 1) 			
			for (int i = 0; i < getPgmImageUtils().getMaxColumns(); i++)  //Itero por los distintos elementos de la columna
				calculateNewValueForPixel(i, getColumnStartIndex());			 
	    else 
			if (getColumnStartIndex() < getColumnEndIndex()/2) {
				new ConcurrentApplyMask(getPgmImageUtils(), getPgmMask(), getRowStartIndex(), getRowEndIndex(), getColumnStartIndex(), getColumnEndIndex()/2, getOutputPGMImage()).fork();	
				new ConcurrentApplyMask(getPgmImageUtils(), getPgmMask(), getRowStartIndex(), getRowEndIndex(), getColumnEndIndex()/2, getColumnEndIndex(), getOutputPGMImage()).compute();
			} else 
				if (getColumnStartIndex() == getColumnEndIndex()/2) {
					new ConcurrentApplyMask(getPgmImageUtils(), getPgmMask(), getRowStartIndex(), getRowEndIndex(), getColumnStartIndex(), ((getColumnEndIndex()/2)+1), getOutputPGMImage()).fork();	
					new ConcurrentApplyMask(getPgmImageUtils(), getPgmMask(), getRowStartIndex(), getRowEndIndex(), ((getColumnEndIndex()/2)+1), getColumnEndIndex(), getOutputPGMImage()).compute();					
				} else 
					if (getColumnStartIndex() > getColumnEndIndex()/2) {
						new ConcurrentApplyMask(getPgmImageUtils(), getPgmMask(), getRowStartIndex(), getRowEndIndex(), getColumnStartIndex(), (getColumnEndIndex()-1), getOutputPGMImage()).fork();			
						new ConcurrentApplyMask(getPgmImageUtils(), getPgmMask(), getRowEndIndex(), getRowEndIndex(), getColumnEndIndex()-1, getColumnEndIndex(), getOutputPGMImage()).compute();												
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
