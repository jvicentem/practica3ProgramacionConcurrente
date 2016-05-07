package practica3PC;

import java.io.IOException;

//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.ArrayList;
//import java.util.List;

//import practica3PC.utils.FileAndFolderUtils;
import practica3PC.modules.Menu;

public class App {
	public static void main(String[] args) throws IOException {
		Menu.execute();
	}
}
	
//	static ArrayList<ArrayList<Integer>> salida = new ArrayList<>();
//	
//
//	private static void prueba(ArrayList<ArrayList<Integer>> lineas, int inicioF, int finF, int[][] mascara, int sumaPositivos, int sumaNegativos) {
//		if (Math.abs(inicioF - finF) == 1) { 
//			System.err.println(">>>>>>>>>> Entra");
//			int[][] pixelesVecinos = new int[3][3];
//			
//			int nuevoValor = 0;
//			
//			for (int k = 0; k < lineas.get(inicioF).size(); k++) { //Itero por los distintos elementos de la fila
//				nuevoValor = 0;
//				pixelesVecinos = new int[3][3];
//				for (int i = 0; i < 3; i++)
//					for (int j = 0; j < 3; j++) {
//						if ( ((inicioF-(1-i)) < lineas.size()) && (inicioF-(1-i)) >= 0 )  {
//							if ((k-(1-j)) < lineas.get( inicioF-(1-i) ).size() && ((k-(1-j)) >= 0))  {
//								//System.err.println("Fila: "+(inicioF-(1-i))+" Columna: "+(k-(1-j)));
//								pixelesVecinos[i][j] = lineas.get(inicioF-(1-i)).get(k-(1-j));						
//							} else {
//								pixelesVecinos[i][j] = 0;
//							}
//						}						
//						else {
//							pixelesVecinos[i][j] = 0;
//						}
//					}				
//				
//				for (int i = 0; i < 3; i++) {
//					for (int j = 0; j < 3; j++) {
//						nuevoValor = nuevoValor + (mascara[i][j] * pixelesVecinos[i][j]);
//					}
//				}
//				
//				nuevoValor = Math.abs(nuevoValor) / Math.max(sumaPositivos, Math.abs(sumaNegativos));
//			
//				salida.get(inicioF).add(nuevoValor);				
//			} 
//		} else {
//			if (inicioF < finF/2) {
//				System.err.println("M1 inicioF: "+inicioF+" finF: "+finF/2);
//				prueba(lineas, inicioF, finF/2, mascara, sumaPositivos, sumaNegativos);	
//				System.err.println("M2 inicioF: "+finF/2+" finF: "+finF);
//				prueba(lineas, finF/2, finF, mascara, sumaPositivos, sumaNegativos);
//			} else {
//				if (inicioF == finF/2) {
//					System.err.println("M-1 inicioF: "+inicioF+" finF: "+ ((finF/2)+1));
//					prueba(lineas, inicioF, ((finF/2)+1), mascara, sumaPositivos, sumaNegativos);	
//					System.err.println("M-2 inicioF: "+((finF/2)+1)+" finF: "+finF);
//					prueba(lineas, ((finF/2)+1), finF, mascara, sumaPositivos, sumaNegativos);					
//				} else {
//					if (inicioF > finF/2) {
//						System.err.println("M--1 inicioF: "+inicioF+" finF: "+(finF-1));
//						prueba(lineas, inicioF, (finF-1), mascara, sumaPositivos, sumaNegativos);			
//						System.err.println("M--2 inicioF: "+(finF-1)+" finF: "+finF);
//						prueba(lineas, (finF-1), finF, mascara, sumaPositivos, sumaNegativos);							
//					}
//						
//				}
//			}
//		}
//	}
//	
//	public static void writeFile(String filePath, String text) throws IOException {
//		File file = new File(filePath);
//		PrintWriter out = new PrintWriter(file);
//		out.println(text);
//		out.close();		
//	}
//	
//	public static void writeAtEndOfFile(String filePath, String text) throws IOException {
//		FileWriter logfile = new FileWriter(filePath, true);
//		BufferedWriter logbw = new BufferedWriter(logfile);
//		logbw.write(text);
//		logbw.newLine();
//		logbw.close();		
//	}	
//	
//	public static void main(String[] args) throws IOException {
//		List<String> lineas = FileAndFolderUtils.readTextFile("./imagenes_pgm/f14.ascii.pgm");
//		
//		int[][] mascara = {{-1, -2, -1},
//						   {0, 0, 0},
//						   {1, 2, 1}};
//		
//		int sumaPositivos = 0;
//		int sumaNegativos = 0;
//		
//		for (int i = 0; i < 3; i++)
//			for (int j = 0; j < 3; j++)
//				sumaPositivos = (mascara[i][j] >= 0)? sumaPositivos + mascara[i][j] : sumaPositivos;
//		
//		for (int i = 0; i < 3; i++)
//			for (int j = 0; j < 3; j++)
//				sumaNegativos = (mascara[i][j] < 0)? sumaNegativos + mascara[i][j] : sumaNegativos;	
//		
//		
//		int k = 0;
//		
//		ArrayList<String> comentarios = new ArrayList<>();
//		
//		boolean size = false;
//		boolean valor = false;
//		
//		int ancho = 0;
//		int alto = 0;
//		int valorMax = 0;
//		
//		int i = 0;
//		int j = 0;
//		
//		int p = 0;
//		
//		ArrayList<ArrayList<Integer>> lineasSplit = new ArrayList<>();
//		
//		ArrayList<Integer> aux = null;
//		
//		String[] trozos;
//		
//		for (String linea : lineas) {
//			p++;
//			if (k != 0) {
//				if (linea.charAt(0) == '#') {
//					comentarios.add(linea);
//					size = true;
//				} else {
//					if (size) {
//						ancho = Integer.parseInt(linea.split("\\s+")[0]);
//						alto = Integer.parseInt(linea.split("\\s+")[1]); 
//						
//						valor = true;
//						
//						size = false;
//						
//						continue;
//					} else {
//						if (valor) {
//							valorMax = Integer.parseInt(linea);
//							
//							valor = false;
//							
//							continue;
//						} else {
//							if (!valor && !size) {
//								trozos = linea.split("\\s+");								
//								
//								for (String trozo : trozos) {
//									if (j == 0)
//										aux = new ArrayList<>();
//									
//									if (! trozo.equals(""))
//										aux.add(Integer.parseInt(trozo));
//									
//									j++;
//									
//									if (j == ancho || (p == lineas.size() && j == (trozos.length-1))) {
//										j = 0;
//										lineasSplit.add(aux);
//									}
//										
//								}
//								
//								i++;
//							}							
//						}				
//					}								
//				}
//				
//			} else {
//				k++;
//			}
//		}
//		
//		for (int l = 0; l < i; l++) {
//			salida.add(new ArrayList<Integer>());
//		}
//		
//		System.err.println("ENTRA inicioF: "+0+" finF: "+alto);
//		prueba(lineasSplit, 0, alto, mascara, sumaPositivos, sumaNegativos);
//		
//		
//		writeAtEndOfFile("./prueba.pgm", "P2");
//		
//		for (String comentario : comentarios) {
//			writeAtEndOfFile("./prueba.pgm", comentario);
//		}
//		
//		writeAtEndOfFile("./prueba.pgm", Integer.toString(ancho) + " " + Integer.toString(alto));
//		
//		writeAtEndOfFile("./prueba.pgm", Integer.toString(valorMax));
//		
//		List<String> resultado = new ArrayList<>();
//		
//		String auxString;
//		
//		for (ArrayList<Integer> fila : salida) {
//			auxString = "";
//			for (int numero : fila) {
//				auxString = (auxString.equals(""))? Integer.toString(numero) : auxString + " " + Integer.toString(numero);
//			}
//			resultado.add(auxString);
//		}
//		
//		for (String linea : resultado) {
//			writeAtEndOfFile("./prueba.pgm", linea);
//		}
//	}