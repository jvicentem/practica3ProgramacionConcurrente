package practica3PC.modules.pgm.concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

import practica3PC.modules.pgm.PGMImageUtils;
import practica3PC.modules.pgm.PGMMask;

public class UseConcurrentApplyMask {
	private UseConcurrentApplyMask() {}
	
	public static final void ApplyMaskWithThreadPools(PGMImageUtils pgmImageUtils, PGMMask pgmMask, PGMImageUtils outputPGMImage) {
		ExecutorService executor = Executors.newCachedThreadPool();

		for (int row = 0; row <= pgmImageUtils.getMaxRows(); row++) 
			for (int column = 0; column <= pgmImageUtils.getMaxColumns(); column++)
				executor.execute(new ConcurrentApplyMask(pgmImageUtils, pgmMask, row, pgmImageUtils.getMaxRows(), column, pgmImageUtils.getMaxColumns(), outputPGMImage));
						
		executor.shutdown();		
	}
	
	public static final void ApplyMaskWithForkJoin(PGMImageUtils pgmImageUtils, PGMMask pgmMask, PGMImageUtils outputPGMImage) {
		ForkJoinPool.commonPool().invoke(new ConcurrentApplyMask(pgmImageUtils, pgmMask, 0, pgmImageUtils.getMaxRows()+1, 0, pgmImageUtils.getMaxColumns()+1, outputPGMImage));
	}
}
