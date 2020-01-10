package tareaIo;

/* SRFLP class
 * Clase que lee una instancia de
 *
 * @autor  Leslie Perez Caceres
 * @version 1.0
 *
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class Data {

  /* numero de instalaciones */
  private int n; 
  /* Lista de los tamaños de las instalaciones a ubicar */
  private int[] f_size;

  /* matriz de pesos: f_weight[i][j] peso/flujo entre las instalaciones i a j */
  private int[][] f_weight;

  /* instance file name */
  private String name;


  public Data (File file_name)
  /*
   * FUNCTION: Constuctor clase SRFLP
   * INPUT: Ruta al archivo de la instancia
   */
  {
    try {
      /* leer instancia desde un archivo */
      read_srflp(file_name); 
      name= file_name.getName();
    } catch (IOException e) {
      System.err.println("No se pudo leer el archivo. " + e.getMessage());
      System.exit(1);
    }
  }
    
  private void read_srflp(File file_name) throws IOException
    /*
     * FUNCTION: parsing y lectura de la instancia
     * INPUT: nombre de la instancia
     * OUTPUT: ninguno
     * COMMENTS: Instancias en el formato definido en al tarea 
     */
    {
    String[] buf;
    int i = -1;
    n = -1;
        
    /*if (file_name == null) {
      System.err.println("Instacia no especificada, abortando...");
      System.exit(1);
    }
        
    if (file_name.canRead()) {
      System.err.println("No se puede leer el archivo " + file_name.getName());
      System.exit(1);
    }*/
        
    System.out.println("\nLeyendo archivo " + file_name.getName() + " ... ");

    FileReader fr = new FileReader (file_name);
    BufferedReader bufferedReader = new BufferedReader(fr);
    String line = bufferedReader.readLine();
    
    while (line != null) {
      if (line.trim().startsWith("EOF"))
        break;
      if (line.trim().startsWith("#")) {
        line = bufferedReader.readLine();
        continue;
      }

      if (n < 0){
        // Primera linea número de instalaciones
        n = Integer.parseInt(line.trim());
        f_size = new int[n];
        f_weight = new int[n][n];
      } else if (i == -1) {
        // Segunda linea tamaño de instalaciones
        buf = line.split(",");
        for (int k=0; k<n; k++) {
          f_size[k] = Integer.parseInt(buf[k].trim());
        }
        i = 0;
      } else {
        // Tercera linea flujo entre instalaciones
        buf = line.split(",");
        for (int k=0; k<n; k++) {
          f_weight[i][k] = Integer.parseInt(buf[k].trim());
        }
        i = i + 1;
      }
      line = bufferedReader.readLine();
    }
  } 

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public int[] getF_size() {
        return f_size;
    }

    public void setF_size(int[] f_size) {
        this.f_size = f_size;
    }

    public int[][] getF_weight() {
        return f_weight;
    }

    public void setF_weight(int[][] f_weight) {
        this.f_weight = f_weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

  public void printInstance () {
    /*
     * FUNCTION: imprime por pantalla la información de la instancia
     * INPUT: ninguno
     * OUTPUT: ninguno
     */
    System.out.println("Numero de instalaciones: " + n);
    System.out.print("Tamaños: ");
	  for (int k=0; k<n; k++) {
       System.out.print(f_size[k]+" " );
    }
    System.out.print("\n");
    System.out.print("Pesos: \n");
    for (int i=0; i<n; i++) {
	    for (int k=0; k<n; k++) {
        System.out.print(f_weight[i][k]+" " );
      }
      System.out.print("\n");
    }
  };
  
  public int getProblemSize() {
    /*
     * FUNCTION: entrega el tamaño del problema (numero de puestos)
     * INPUT: ninguno
     * OUTPUT: ninguno
     */
	  return (n);
  };

  public int getWeight(int i, int j) {
    /*
     * FUNCTION: entrega el flujo entre dos puestos i y j
     * INPUT: indices de los puestos i y j
     * OUTPUT: valor del flujo entre los puestos i y j
     */
    if (i>=n || i<0 || j>=n || j<0) {
      System.err.println("Error no existe el puesto " + i + " o "+ j + " fuera de rango\n");
      System.exit(1);
    }
    return(f_weight[i][j]);
  };

  public int getFacilitySize(int i) {
    /*
     * FUNCTION: entrega el tamaño de un puesto
     * INPUT: indice del puesto i
     * OUTPUT: tamaño del puesto i
     */
    if (i>=n || i<0) {
      System.err.println("Error no existe el puesto " + i + " fuera de rango\n");
      System.exit(1);
    }
    return(f_size[i]);
  };

  public double getTotalDistance (int[] sol){
    /*
     * FUNCTION: calcula la distancia total recorrida por 
     *           los clientes en base a la solucion sol
     * INPUT: arreglo con el orden de los puestos. Ej. el arreglo 
     *        [2,1,5,4,3,0,6,7] indica que el puesto #2 esta 
     *        en la posicion 0 y puesto #0 esta el la posicion 5. 
     *        IMPORTANTE: los puestos estan numerados a partir del 0.
     * OUTPUT: distancia total recorrida por los clientes
     */
     double total = 0.0;
     double middle_distance;
     int p1, p2;

     // Calcular la distancia de todas las combinaciones de 
     // puestos
     for (int i=0; i < (n-1); i++) {
       // Indice del puesto en la posición 1
       p1 = sol[i];
       middle_distance = 0.0;
       for (int j=(i+1); j<n; j++) {
         p2 =  sol[j];
         total = total + (f_size[p1]/2 + middle_distance + f_size[p2]/2); 
         middle_distance = middle_distance + f_size[p2]; 
       }
     }
     return(total);
  };
};

  
