import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class metodoNaive {

    public static List<int[]> encontrarPontes(Grafo grafo) {
        List<int[]> arestas = listarArestas(grafo);
        List<int[]> pontes = new ArrayList<>();

        // Para cada aresta, remove e verifica se os vértices ainda estão conectados, se não, é ponte
        for (int[] e : arestas) {
            int u = e[0], v = e[1];

            grafo.remover(u, v);

            boolean conectados = haCaminho(grafo, u, v);
            if (!conectados) {
                pontes.add(new int[]{u, v});
                // System.out.println("Ponte: {" + u + ", " + v + "}");
            }

            grafo.adicionar(u, v);
            grafo.adicionar(v, u);
        }

        return pontes;
    }

    private static List<int[]> listarArestas(Grafo grafo) {
        List<int[]> arestas = new ArrayList<>();
        for (int u = 1; u < grafo.lista.length; u++) {
            Node ptr = grafo.lista[u].prox;
            while (ptr != null) {
                int v = ptr.num;
                if (u < v) {
                    arestas.add(new int[]{u, v});
                }
                ptr = ptr.prox;
            }
        }
        return arestas;
    }

    private static boolean haCaminho(Grafo grafo, int origem, int destino) {
        int n = grafo.lista.length - 1;
        boolean[] vis = new boolean[n + 1];
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(origem);
        vis[origem] = true;

        while (!stack.isEmpty()) {
            int u = stack.pop();
            if (u == destino) return true;
            Node ptr = grafo.lista[u].prox;
            while (ptr != null) {
                int v = ptr.num;
                if (!vis[v]) {
                    vis[v] = true;
                    stack.push(v);
                }
                ptr = ptr.prox;
            }
        }
        return false;
    }
}
