public class metodoTarjan {
    public static int tempo = 0;
    
    /*
     * Método de Tarjan para identificação de pontes
     * Baseado no código encontrado em:
     * https://www.geeksforgeeks.org/dsa/bridge-in-a-graph/
     */
    public static void ponteInicial(Grafo grafo) {
        int n_vertices = grafo.lista.length - 1;
        int TD[] = new int[n_vertices + 1];
        int pai[] = new int[n_vertices + 1];
        int min[] = new int[n_vertices + 1];

        for (int i = 1; i < grafo.lista.length; i++) {
            if (TD[i] == 0) {
                ponte(grafo, i, TD, min, pai);
            }
        }
    }
    private static void ponte(Grafo grafo, int v, int TD[], int min[], int pai[]) {
        tempo = tempo + 1;
        TD[v] = min[v] = tempo;

        Node ptr = grafo.lista[v].prox;
        while (ptr != null) {
            int w = ptr.num;

            if (TD[w] == 0) {
                pai[w] = v;
                ponte(grafo, w, TD, min, pai);
                min[v] = Math.min(min[v], min[w]);

                if (min[w] > TD[v]) {
                    System.out.println("Ponte: {" + v + ", " + w + "}");
                }
            }
            else if (w != pai[v]) {
                min[v] = Math.min(min[v], TD[w]);
            }
            
            ptr = ptr.prox;
        }
    }
}
