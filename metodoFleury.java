import java.util.ArrayList;
import java.util.List;

public class metodoFleury {

    /**
     * Algoritmo de Fleury para encontrar um caminho euleriano ou ciclo euleriano em um grafo.
     * 
     * @param grafo O grafo original
     * @return Lista de vértices representando o caminho euleriano, ou null se não existir
     */
    public static List<Integer> encontrarCaminhoEuleriano(Grafo grafo) {
        int n_vertices = grafo.lista.length - 1;

        // Determinar tipo do grafo
        String tipoGrafo;

        // 1. Verificar se V(G) possui 3 ou mais vértices de grau ímpar
        int verticesGrauImpar = 0;
        List<Integer> verticesImpares = new ArrayList<>();

        for (int i = 1; i <= n_vertices; i++) {
            int grau = grafo.getGrau(i);
            if (grau % 2 != 0) {
                verticesGrauImpar++;
                verticesImpares.add(i);
            }
            if (verticesGrauImpar >= 3) {
                tipoGrafo = "Não-Euleriano";

                System.out.println();
                System.out.println("Análise do Grafo (Algoritmo de Fleury)");
                System.out.println("Tipo: " + tipoGrafo);
                
                return null;
            }
        }

        // 2. Criar grafo auxiliar G' = (V', E') tal que V' ⊆ V(G) e E' ⊆ E(G)
        Grafo grafoAux = grafo.copy();

        // 3. Selecionar vértice inicial v ∈ V' (escolher v cujo grau seja ímpar, se houver)
        int v;
        if (verticesGrauImpar > 0) {
            v = verticesImpares.get(0); // Escolher um vértice de grau ímpar
        } else {
            // Se todos têm grau par, escolher qualquer vértice com grau > 0
            v = 1;
            for (int i = 1; i <= n_vertices; i++) {
                if (grafoAux.getGrau(i) > 0) {
                    v = i;
                    break;
                }
            }
        }

        // Lista para armazenar o caminho euleriano
        List<Integer> caminho = new ArrayList<>();
        caminho.add(v);

        // 4. Enquanto E' ≠ ∅ efetuar
        while (possuiArestas(grafoAux)) {
            // System.out.println(v);
            int grauV = grafoAux.getGrau(v);

            if (grauV == 0) {
                // Não há mais arestas disponíveis a partir de v
                // System.out.println("Não há mais arestas disponíveis em " + v);
                break;
            }

            int w = -1;

            // 5. Se d(v) > 1 então
            if (grauV > 1) {
                // Selecionar aresta {v, w} que não seja ponte em G'
                w = selecionarArestaNaoPonte(grafoAux, v);
                // System.out.println("grauV > 1");
            } else {
                // Selecionar a única aresta {v, w} disponível em G'
                w = obterPrimeiroVizinho(grafoAux, v);
                // System.out.println("Selecionando unica aresta disponível");
            }

            if (w == -1) {
                // Não foi possível encontrar uma aresta válida
                // System.out.println("Não houve aresta válida");
                break;
            }

            // 6. v ← w; E' ← E' – {v, w}; // Caminhar de v para w e eliminar aresta
            grafoAux.remover(v, w);
            caminho.add(w);
            v = w;
        }

        if (verticesGrauImpar == 0) {
            tipoGrafo = "Euleriano";
        } else if (verticesGrauImpar == 2) {
            tipoGrafo = "Semi-Euleriano";
        } else {
            tipoGrafo = "Não-Euleriano";
        }

        // Imprimir resultados
        System.out.println();
        System.out.println("Análise do Grafo (Algoritmo de Fleury)");
        System.out.println("Tipo: " + tipoGrafo);

        return caminho;
    }

    /**
     * Verifica se o grafo ainda possui arestas
     */
    private static boolean possuiArestas(Grafo grafo) {
        for (int i = 1; i < grafo.lista.length; i++) {
            if (grafo.lista[i].prox != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * Obtém o primeiro vizinho de um vértice
     */
    private static int obterPrimeiroVizinho(Grafo grafo, int vertice) {
        if (grafo.lista[vertice].prox != null) {
            return grafo.lista[vertice].prox.num;
        }
        return -1;
    }

    /**
     * Seleciona uma aresta {v, w} que não seja ponte em G'
     * Utiliza o método de Tarjan para verificar se a aresta é ponte
     */
    private static int selecionarArestaNaoPonte(Grafo grafo, int v) {
        metodoTarjan.tempo = 0;

        // Percorrer os vizinhos de v
        Node ptr = grafo.lista[v].prox;
        while (ptr != null) {
            int w = ptr.num;

            // Verificar se a aresta {v, w} é uma ponte
            boolean ehPonte = metodoTarjan.encontrarPonte(grafo, v, w);

            // Se não é ponte, retornar este vizinho
            if (!ehPonte) {
                return w;
            }

            ptr = ptr.prox;
        }

        // Se todas as arestas são pontes, retornar o primeiro vizinho
        // (isso acontece quando resta apenas uma aresta)
        return obterPrimeiroVizinho(grafo, v);
    }

    /**
     * Imprime o caminho euleriano
     */
    public static void imprimirCaminho(List<Integer> caminho) {
        if (caminho == null || caminho.isEmpty()) {
            System.out.println("Caminho euleriano não encontrado.");
            return;
        }

        System.out.println("\nCaminho Euleriano (Algoritmo de Fleury):");
        System.out.print("Caminho: ");
        for (int i = 0; i < caminho.size(); i++) {
            System.out.print(caminho.get(i));
            if (i < caminho.size() - 1) {
                System.out.print(" -> ");
            }
        }
        System.out.println("\nTotal de vértices no caminho: " + caminho.size());
    }
}
