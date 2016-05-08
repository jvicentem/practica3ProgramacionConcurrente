package practica3PC.modules;

import java.io.IOException;
import java.util.List;

import practica3PC.modules.concurrency.UseConcurrentApplyMask;
import practica3PC.utils.FileAndFolderUtils;

public class Menu {
	private Menu() {}
	
	public static final void execute() throws IOException {		
		List<String> linesFromPGMFile = FileAndFolderUtils.readTextFile("./imagenes_pgm/f14.ascii.pgm");
		
		PGMImageUtils pgmImageUtils = PGMImageUtils.parsePGMFile(linesFromPGMFile);
		
		PGMMask pgmMask = PGMMask.HORIZONTAL_SOBEL;
		
		PGMImageUtils outputPGMImage = (PGMImageUtils) pgmImageUtils.clone();
		
		//UseConcurrentApplyMask.ApplyMaskWithThreadPools(pgmImageUtils, pgmMask, outputPGMImage);
		UseConcurrentApplyMask.ApplyMaskWithForkJoin(pgmImageUtils, pgmMask, outputPGMImage);
		
		//System.err.println(outputPGMImage.filaMal());
		
		outputPGMImage.writePGMObjectInFile("./prueba.pgm");
	}
}