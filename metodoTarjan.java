public class metodoTarjan {

    public static int tempo = 0;

    /*
     * Método de Tarjan para identificação de pontes
     * Baseado no código encontrado em:
     * https://www.geeksforgeeks.org/dsa/bridge-in-a-graph/
     */
    public static boolean encontrarPonte(Grafo grafo, int v, int w) {
        int n_vertices = grafo.lista.length - 1;
        int TD[] = new int[n_vertices + 1];
        int pai[] = new int[n_vertices + 1];
        int min[] = new int[n_vertices + 1];

        int origem_v = v;
        int destino_w = w;

        for (int i = 1; i < grafo.lista.length; i++) {
            if (TD[i] == 0) {
                if (tarjan(grafo, i, TD, min, pai, origem_v, destino_w)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean tarjan(Grafo grafo, int v, int TD[], int min[], int pai[], int origem_v, int destino_w) {
        tempo = tempo + 1;
        TD[v] = min[v] = tempo;
        Node ptr = grafo.lista[v].prox;

        while (ptr != null) {
            int w = ptr.num;

            if (TD[w] == 0) {
                pai[w] = v;
                if (tarjan(grafo, w, TD, min, pai, origem_v, destino_w)) {
                    return true;
                }
                min[v] = Math.min(min[v], min[w]);

                if (min[w] > TD[v]) {
                    if ((v == origem_v && w == destino_w) || (v == destino_w && w == origem_v)) {
                        return true;
                    }
                }
            }

            else if (w != pai[v]) {
                min[v] = Math.min(min[v], TD[w]);
            }
            
            ptr = ptr.prox;
        }
        return false;
    }

}