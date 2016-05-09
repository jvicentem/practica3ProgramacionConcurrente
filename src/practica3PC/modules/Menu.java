package practica3PC.modules;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import practica3PC.modules.pgm.PGMImageUtils;
import practica3PC.modules.pgm.PGMMask;
import practica3PC.modules.pgm.concurrency.UseConcurrentApplyMask;
import practica3PC.utils.FileAndFolderUtils;

public class Menu {
	private Menu() {}
	
	public static final void execute(boolean developer) throws IOException {		
		BufferedReader keyboardReader = new BufferedReader(new InputStreamReader(System.in));
		
		String pgmFilePath = requestInputPGMImagePath(keyboardReader);
		
		PGMImageUtils pgmImageUtils = imageFileToObject(pgmFilePath);
		
		PGMMask pgmMask = requestMaskToApply(keyboardReader);
		
		PGMImageUtils outputPGMImage = pgmImageUtils.copyPGMImageUtilsForModificationPurposes();
		
		if (developer)
			requestConcurrencyStrategy(keyboardReader, pgmImageUtils, pgmMask, outputPGMImage);
		else
			UseConcurrentApplyMask.ApplyMaskWithForkJoin(pgmImageUtils, pgmMask, outputPGMImage);
		
		saveModifiedImage(keyboardReader, outputPGMImage);
		
		keyboardReader.close();
		
		System.out.println("Fin del programa");
	}
	
	private static String requestInputPGMImagePath(BufferedReader keyboardReader) throws IOException {
		System.out.println("> Introduce la ruta de la imagen sobre la que aplicar la máscara: ");
		String imageFilePath = keyboardReader.readLine();
		
		while (! FileAndFolderUtils.validFilePath(imageFilePath)) {
			System.err.println(">> Ruta de archivo no válida. Por favor, vuelva a introducir una ruta correcta:");
			imageFilePath = keyboardReader.readLine();
		}
		
		System.out.println("");
		
		return imageFilePath;		
	}
	
	private static PGMImageUtils imageFileToObject(String pgmFilePath) throws IOException {
		List<String> linesFromPGMFile = FileAndFolderUtils.readTextFile(pgmFilePath);
		
		return PGMImageUtils.parsePGMFile(linesFromPGMFile);
	}
	
	private static PGMMask requestMaskToApply(BufferedReader keyboardReader) throws IOException {
		int i = 1;
		
		PGMMask[] masks = PGMMask.values();
		
		for (PGMMask value : masks)
			System.out.println(i++ + ". " + value.toString());
		
		int option = Integer.parseInt(keyboardReader.readLine());
		
		while (option < 0 || option > masks.length) {
			for (PGMMask value : masks)
				System.out.println(i++ + ". " + value.toString());
			
			option = Integer.parseInt(keyboardReader.readLine());
		}
		
		System.out.println("");
				
		return masks[option];
	}
	
	private static void requestConcurrencyStrategy(BufferedReader keyboardReader, PGMImageUtils pgmImageUtils, PGMMask pgmMask, PGMImageUtils outputPGMImage) throws IOException {
		System.out.println("Elige la estrategia de concurrencia: ");
		System.out.println("1. Thread pools");
		System.out.println("2. Fork Join");
		
		String option = keyboardReader.readLine();
		
		while (!"1".equals(option) && !"2".equals(option)) {
			System.out.println("Elige la estrategia de concurrencia: ");
			System.out.println("1. Thread pools");
			System.out.println("2. Fork Join");			
			
			option = keyboardReader.readLine();
		}
		
		switch (option) {
			case "1":
				UseConcurrentApplyMask.ApplyMaskWithThreadPools(pgmImageUtils, pgmMask, outputPGMImage);
				break;
			case "2":
				UseConcurrentApplyMask.ApplyMaskWithForkJoin(pgmImageUtils, pgmMask, outputPGMImage);
				break;
		}

	}
	
	private static void saveModifiedImage(BufferedReader keyboardReader, PGMImageUtils outputPGMImage) throws IOException {
		System.out.println("\n> Introduce el nombre, sin extensión, de la imagen modificada a guardar: ");
		String imageName = keyboardReader.readLine();
		
		while (imageName.trim().isEmpty()) {
			System.out.println("> Introduce el nombre, sin extensión, de la imagen modificada a guardar: ");
			imageName = keyboardReader.readLine();
		}
		
		String overwrite = "";
		
		File auxFile = new File("." + File.separator + imageName + ".pgm");
		
		boolean goOn = false;
		
		while (!goOn) 
			if (auxFile.exists()) {
				System.out.println(">> El archivo ya existe ¿Sobre escribir? (S/N)");
				overwrite = keyboardReader.readLine();
				
				while (!"s".equalsIgnoreCase(overwrite) && !"n".equalsIgnoreCase(overwrite)) {
					System.out.println(">> El archivo ya existe ¿Sobre escribir? (S/N)");
					overwrite = keyboardReader.readLine();
					
					if (overwrite.toLowerCase().equals("s"))
						goOn = true;
				}
			} else 
				goOn = true;
		
		if ("".equals(overwrite) || "s".equalsIgnoreCase(overwrite)) 
			outputPGMImage.writePGMObjectInFile(imageName + ".pgm");
		
		System.out.println("");
	}
}