package practica3PC.modules.concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

import practica3PC.modules.PGMImageUtils;
import practica3PC.modules.PGMMask;

public class UseConcurrentApplyMask {
	private UseConcurrentApplyMask() {}
	
	public static final void ApplyMaskWithThreadPools(PGMImageUtils pgmImageUtils, PGMMask pgmMask, PGMImageUtils outputPGMImage) {
		ExecutorService executor = Executors.newSingleThreadExecutor();

		executor.execute(new ConcurrentApplyMask(pgmImageUtils, pgmMask, 0, pgmImageUtils.getMaxRows(), 0, pgmImageUtils.getMaxColumns(), outputPGMImage));
				
		executor.shutdown();		
	}
	
	public static final void ApplyMaskWithForkJoin(PGMImageUtils pgmImageUtils, PGMMask pgmMask, PGMImageUtils outputPGMImage) {
		ForkJoinPool.commonPool().invoke(new ConcurrentApplyMask(pgmImageUtils, pgmMask, 0, pgmImageUtils.getMaxRows(), 0, pgmImageUtils.getMaxColumns(), outputPGMImage));
	}
}
