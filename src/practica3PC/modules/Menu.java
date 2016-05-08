package practica3PC.modules;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import practica3PC.modules.concurrency.UseConcurrentApplyMask;
import practica3PC.utils.FileAndFolderUtils;

public class Menu {
	private Menu() {}
	
	public static final void execute() throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		reader.readLine();
		
		List<String> linesFromPGMFile = FileAndFolderUtils.readTextFile("./imagenes_pgm/f14.ascii.pgm");
		
		PGMImageUtils pgmImageUtils = PGMImageUtils.parsePGMFile(linesFromPGMFile);
		
		PGMMask pgmMask = PGMMask.HORIZONTAL_SOBEL;
		
		PGMImageUtils outputPGMImage = (PGMImageUtils) pgmImageUtils.clone();
		
		//UseConcurrentApplyMask.ApplyMaskWithThreadPools(pgmImageUtils, pgmMask, outputPGMImage);
		UseConcurrentApplyMask.ApplyMaskWithForkJoin(pgmImageUtils, pgmMask, outputPGMImage);
		
		outputPGMImage.writePGMObjectInFile("./prueba.pgm");
	}
}