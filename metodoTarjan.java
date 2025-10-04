import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class metodoTarjan {
    public static int tempo = 0;
    public static List<int[]> pontes = new ArrayList<>();
    
    // Classe para representar um estado na pilha
    private static class EstadoDFS {
        int vertice;
        int pai;
        Node atual;
        boolean visitado;
        boolean processandoFilho;
        int filho;
        
        EstadoDFS(int vertice, int pai, Node atual) {
            this.vertice = vertice;
            this.pai = pai;
            this.atual = atual;
            this.visitado = false;
            this.processandoFilho = false;
        }
    }

    /*
     * Método de Tarjan para identificação de pontes (versão iterativa)
     * Baseado no código encontrado em:
     * https://www.geeksforgeeks.org/dsa/bridge-in-a-graph/
     * Inicialmente implementado com recursão, foi alterado com ajuda do ChatGPT
     * para impedir o estouro da pilha de recursão em grafos com muitos vértices.
     */
    public static void ponteInicial(Grafo grafo) {
        int n_vertices = grafo.lista.length - 1;
        int TD[] = new int[n_vertices + 1];
        int pai[] = new int[n_vertices + 1];
        int min[] = new int[n_vertices + 1];

        for (int i = 1; i < grafo.lista.length; i++) {
            if (TD[i] == 0) {
                ponteIterativo(grafo, i, TD, min, pai);
            }
        }
    }
    private static void ponteIterativo(Grafo grafo, int inicio, int TD[], int min[], int pai[]) {
        Stack<EstadoDFS> pilha = new Stack<>();
        
        // Iniciar DFS do vértice inicial
        pilha.push(new EstadoDFS(inicio, -1, grafo.lista[inicio].prox));
        
        while (!pilha.isEmpty()) {
            EstadoDFS estado = pilha.peek();
            int v = estado.vertice;
            
            // Primeira visita ao vértice
            if (!estado.visitado) {
                tempo++;
                TD[v] = min[v] = tempo;
                pai[v] = estado.pai;
                estado.visitado = true;
            }
            
            // Se está processando retorno de um filho
            if (estado.processandoFilho) {
                int w = estado.filho;
                min[v] = Math.min(min[v], min[w]);
                
                // Verificar se é uma ponte
                if (min[w] > TD[v]) {
                    pontes.add(new int[]{v, w});
                }
                
                estado.processandoFilho = false;
            }
            
            // Processar próximo vizinho
            if (estado.atual != null) {
                int w = estado.atual.num;
                estado.atual = estado.atual.prox;
                
                if (TD[w] == 0) {
                    // Vértice não visitado - fazer DFS
                    estado.processandoFilho = true;
                    estado.filho = w;
                    pilha.push(new EstadoDFS(w, v, grafo.lista[w].prox));
                } else if (w != pai[v]) {
                    // Aresta de retorno
                    min[v] = Math.min(min[v], TD[w]);
                }
            } else {
                // Terminou de processar todos os vizinhos
                pilha.pop();
            }
        }
    }
}