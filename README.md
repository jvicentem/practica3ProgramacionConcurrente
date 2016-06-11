# Práctica 3: Procesamiento de imágenes 

El  objetivo  de  esta  práctica  es  desarrollar  un  procesador  de  imágenes  básico  que  aplique filtros  utilizando  convoluciones.  Una  imagen  se  define  como  una  matriz  de  tamaño  w × h donde w representa el ancho y h el alto. Si consideramos imágenes en escala de grises, el valor de cada pixel de la matriz indica el nivel de gris de ese pixel concreto, siendo 255 el blanco  y  0  el  negro.  Es  decir,  si  el  valor  de  un  pixel  de  una  imagen  es  255,  al  visualizarla veremos un pixel blanco en esa posición, y si es 0 veremos un pixel negro. 

La aplicación del filtro se lleva a cabo utilizando una máscara, la cual será diferente para cada filtro, que  se  representa  con  una  matriz  cuadrada  de  tamaño  reducido  (los  filtros  que utilizaremos tendrán tamaño 3x3). El valor de los elementos almacenados en la máscara será diferente en cada una, para poder producir diferentes resultados. 

Para aplicar un  filtro a una imagen, lo único que  tenemos que hacer es aplicar la máscara sobre el vecindario de cada pixel de la imagen.

El  procedimiento  a  realizar  es  el  siguiente:  dado  el  pixel  central  de  la  imagen  (147  en  el ejemplo), vamos a calcular el valor que obtendríamos en ese mismo pixel si aplicamos el filtro de Sobel. Para ello, seleccionamos una vecindad del mismo  tamaño que el  filtro  (3x3) con centro en el pixel 147. A continuación, sumamos las dos matrices resultantes, de la siguiente manera:

<table>
 <tbody>
  <tr>
   <td>   
r = | − 1 ∗ 221 + 0 ∗ 198 + 1 ∗ 149
−2 ∗ 205 + 0 ∗ 147 + 2 ∗ 173
−1 ∗ 149 + 0 ∗ 170 + 1 ∗ 222 |    
   </td>
  </tr>
 </tbody>
</table>

El  resultado  de  esta  operación  será  r  =  63,  que  se  corresponde  con  el  nivel  de  gris  que tendremos que asignar al pixel situado en la posición correspondiente en la imagen filtrada.
El último paso será escalar el resultado para que siempre quede en el rango [0,255]. Para ello, sumaremos todos los valores positivos de la máscara (1+2+1 en Sobel), y el valor absoluto de los negativos (|-1-2-1|), y dividiremos el resultado por el mayor valor obtenido (4 en el caso de Sobel).

Los  resultados los almacenaremos en  una  nueva matriz  del mismo  tamaño  que la imagen original,  que  posteriormente  guardaremos  en  un  fichero  como  una  nueva  imagen.  La aplicación del filtro se debe llevar a cabo para todos y cada uno de los pixeles de la imagen original. Para simplificar la  tarea, se considera que los pixeles de la primera y última  fila y primera y última columna siempre tendrán un valor de 0 en la imagen filtrada.El formato de la imagen con la que trabajaremos será Portable Gray Map (PGM), que no es más que un fichero de texto que almacena imágenes en escala de grises. El fichero de una imagen PGM siempre guarda la misma estructura: 

- La  primera  línea  tiene  un  identificador  del  tipo  de  imagen  que,  en  los  ficheros proporcionados, será siempre “P2”.
- Las  siguientes  líneas  pueden  contener  comentarios  (que  debemos  ignorar)  que comienzan por el carácter “#”
- La  siguiente línea  tiene,  separados  por espacios en  blanco, el ancho y el alto  de la imagen en número de pixeles.
- A continuación aparecerá el valor de gris de cada pixel, comenzando por la esquina superior  izquierda  y  terminando en  la esquina  inferior  derecha.  Cada  valor  vendrá separado del anterior y del siguiente por algún tipo de espacio en blanco (puede ser un espacio, varios, un tabulador, una nueva línea, etc.).

El programa que implementemos deberá funcionar de la siguiente manera:
1. Se solicita por teclado la ruta de la imagen PGM que se va a utilizar.
2. Se  lee  el  fichero  PGM  como  un  fichero  de  texto,  almacenando  la  imagen correspondiente en una matriz.
3. Se solicita al usuario qué tipo de filtro queremos aplicar de entre los disponibles.
4. Se aplica, de manera concurrente, el filtro sobre la matriz que hemos leído.
5. Se solicita al usuario el nombre del fichero de salida, y se guarda la imagen filtrada en dicho fichero en el formato PGM antes descrito.

Dado que la aplicación del filtro a cada pixel es totalmente independiente, la concurrencia en esta  práctica  se  aplicará  en  este  punto,  de  manera  que  varios  threads  (tantos  como  la máquina  tenga  disponibles)  deberán  estar  aplicando  el  filtro  al  pixel  que  le  corresponda simultáneamente.

El  alumno deberá  desarrollar  dos  algoritmos  diferentes:  uno  utilizando  la metodología  de colas de trabajo (ExecutorService) y otro con la metodología Fork /Join. El objetivo final será llevar a cabo una comparativa del tiempo de ejecución necesario en cada caso.
Los  filtros  que  debemos  poder  aplicar  serán  los  siguientes (ordenados  de  izquierda  a derecha): Sobel  horizontal,  Sobel  vertical,  enfocar,  detección  de  bordes,  emboss,  y desenfocar.
