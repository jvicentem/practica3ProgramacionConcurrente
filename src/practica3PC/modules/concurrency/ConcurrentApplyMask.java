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
		} else throw new InvalidParameterException("Índeces no válidos");
		
		this.pgmImageUtils = pgmImageUtils;
		this.pgmMask = pgmMask;
		this.outputPGMImage = outputPGMImage;
	}

	@Override
	public void run() {
		divideAndConquerForRunMethod(getRowStartIndex(), getRowEndIndex());
	}
	
	private void divideAndConquerForRunMethod(int rowStartIndex, int getRowEndIndex) {
		if (Math.abs(getRowStartIndex() - getRowEndIndex()) == 1) { 
			int[][] neighbourPixels;
			
			int newValue = 0;
			
			for (int k = 0; k < getPgmImageUtils().getMaxColumns(); k++) { //Itero por los distintos elementos de la fila
				neighbourPixels = getPgmImageUtils().getNeighbourPixels(getRowStartIndex(), k, getPgmMask().getMaskSize());			
				
				newValue = getPgmMask().applyMaskToPixel(neighbourPixels);
			
				getOutputPGMImage().setValueToPixel(getRowStartIndex(), k, newValue);			
			} 
		} else {
			if (getRowStartIndex() < getRowEndIndex()/2) {
				divideAndConquerForRunMethod(getRowStartIndex(), getRowEndIndex()/2);	
				divideAndConquerForRunMethod(getRowEndIndex()/2, getRowEndIndex());
			} else {
				if (getRowStartIndex() == getRowEndIndex()/2) {
					divideAndConquerForRunMethod(getRowStartIndex(), ((getRowEndIndex()/2)+1));	
					divideAndConquerForRunMethod(((getRowEndIndex()/2)+1), getRowEndIndex());					
				} else {
					if (getRowStartIndex() > getRowEndIndex()/2) {
						divideAndConquerForRunMethod(getRowStartIndex(), (getRowEndIndex()-1));			
						divideAndConquerForRunMethod((getRowEndIndex()-1), getRowEndIndex());							
					}
				}
			}
		}		
	}

	@Override
	public void compute() {
		if (Math.abs(getRowStartIndex() - getRowEndIndex()) == 1) { 
			int[][] neighbourPixels;
			
			int newValue = 0;
			
			for (int k = 0; k < getPgmImageUtils().getMaxColumns(); k++) { //Itero por los distintos elementos de la fila
				neighbourPixels = getPgmImageUtils().getNeighbourPixels(getRowStartIndex(), k, getPgmMask().getMaskSize());			
				
				newValue = getPgmMask().applyMaskToPixel(neighbourPixels);
			
				getOutputPGMImage().setValueToPixel(getRowStartIndex(), k, newValue);				
			} 
		} else {
			if (getRowStartIndex() < getRowEndIndex()/2) {
				new ConcurrentApplyMask(getPgmImageUtils(), getPgmMask(), getRowStartIndex(), getRowEndIndex()/2, getColumnStartIndex(), getColumnEndIndex(), getOutputPGMImage()).fork();	
				new ConcurrentApplyMask(getPgmImageUtils(), getPgmMask(), getRowEndIndex()/2, getRowEndIndex(), getColumnStartIndex(), getColumnEndIndex(), getOutputPGMImage()).compute();
			} else {
				if (getRowStartIndex() == getRowEndIndex()/2) {
					new ConcurrentApplyMask(getPgmImageUtils(), getPgmMask(), getRowStartIndex(), ((getRowEndIndex()/2)+1), getColumnStartIndex(), getColumnEndIndex(), getOutputPGMImage()).fork();	
					new ConcurrentApplyMask(getPgmImageUtils(), getPgmMask(), ((getRowEndIndex()/2)+1), getRowEndIndex(), getColumnStartIndex(), getColumnEndIndex(), getOutputPGMImage()).compute();					
				} else {
					if (getRowStartIndex() > getRowEndIndex()/2) {
						new ConcurrentApplyMask(getPgmImageUtils(), getPgmMask(), getRowStartIndex(), (getRowEndIndex()-1), getColumnStartIndex(), getColumnEndIndex(), getOutputPGMImage()).fork();			
						new ConcurrentApplyMask(getPgmImageUtils(), getPgmMask(), (getRowEndIndex()-1), getRowEndIndex(), getColumnStartIndex(), getColumnEndIndex(), getOutputPGMImage()).compute();							
					}
				}
			}
		}		
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
