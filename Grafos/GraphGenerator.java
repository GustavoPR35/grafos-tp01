import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * GraphGenerator.java
 *
 * Gera grafos simples, não direcionados, e força propriedades:
 * - Euleriano: conexo (com arestas) e todos os vértices incidentes têm grau par.
 * - Semi-Euleriano: conexo (com arestas) e exatamente 2 vértices têm grau ímpar.
 * - Não-Euleriano: qualquer outro caso (p.ex. >2 vértices ímpares ou desconexo).
 *
 * Estratégia:
 * 1) Cria um grafo simples aleatório e conectado.
 * 2) Ajusta paridades dos vértices adicionando arestas:
 *    - Se dois vértices ímpares não estão ligados, liga-os (tornando-os pares).
 *    - Se já estiverem ligados, tenta encontrar um vértice intermediário w tal que
 *      (u,w) e (v,w) não existam; então adiciona ambas — isso deixa u e v pares e
 *      aumenta deg(w) em 2 (par).
 *
 * Observação: a rotina tenta manter simples (sem multiaresteas) e conectividade.
 */
public class GraphGenerator {

    public enum Type { EULERIAN, SEMI_EULERIAN, NON_EULERIAN }

    static class Graph {
        final int n;
        final Set<Integer>[] adj;
        int edgeCount = 0;

        @SuppressWarnings("unchecked")
        Graph(int n) {
            this.n = n;
            this.adj = new Set[n];
            for (int i = 0; i < n; i++) adj[i] = new HashSet<>();
        }

        boolean hasEdge(int u, int v) {
            return adj[u].contains(v);
        }

        boolean addEdge(int u, int v) {
            if (u == v) return false; // sem laços
            if (hasEdge(u, v)) return false; // sem multiaresteas
            adj[u].add(v);
            adj[v].add(u);
            edgeCount++;
            return true;
        }

        boolean removeEdge(int u, int v) {
            if (!hasEdge(u, v)) return false;
            adj[u].remove(v);
            adj[v].remove(u);
            edgeCount--;
            return true;
        }

        int degree(int v) { return adj[v].size(); }

        List<Integer> oddVertices() {
            List<Integer> odds = new ArrayList<>();
            for (int i = 0; i < n; i++) if ((degree(i) & 1) == 1) odds.add(i);
            return odds;
        }

        /**
         * Retorna os vértices que pertencem ao mesmo componente que pelo menos
         * uma aresta (i.e. ignoramos vértices isolados para definição de Euler).
         */
        boolean isConnectedConsideringEdges() {
            boolean[] visited = new boolean[n];
            int start = -1;
            for (int i = 0; i < n; i++) if (degree(i) > 0) { start = i; break; }
            if (start == -1) return true; // sem arestas: consideramos conectado (sem arestas)
            Queue<Integer> q = new ArrayDeque<>();
            q.add(start); visited[start] = true;
            while (!q.isEmpty()) {
                int u = q.poll();
                for (int v : adj[u]) if (!visited[v]) { visited[v] = true; q.add(v); }
            }
            for (int i = 0; i < n; i++) if (degree(i) > 0 && !visited[i]) return false;
            return true;
        }

        boolean isEulerianCircuit() {
            if (!isConnectedConsideringEdges()) return false;
            for (int i = 0; i < n; i++) if ((degree(i) & 1) == 1) return false;
            return true;
        }

        boolean isEulerianTrailButNotCircuit() {
            if (!isConnectedConsideringEdges()) return false;
            int odd = oddVertices().size();
            return odd == 2;
        }

        void printSummary() {
            System.out.println("Vértices: " + n + ", Arestas: " + edgeCount);
            System.out.print("Grau(s): ");
            //for (int i = 0; i < n; i++) System.out.print(degree(i) + (i+1<n? ", " : "\n"));
            //System.out.println("Ímpares: " + oddVertices());
            System.out.println("Conexo (considerando arestas): " + isConnectedConsideringEdges());
            System.out.println("Euleriano (circuito): " + isEulerianCircuit());
            System.out.println("Semi-Euleriano (trilha): " + isEulerianTrailButNotCircuit());
        }

        void makeFile(String fileName) throws IOException {
            // // Criar o diretório, se necessário
            // File file = new File(fileName);
            // file.getParentFile().mkdirs(); // Cria os diretórios, se necessário

            FileWriter fw = new FileWriter(fileName);
            fw.write(n + " " + (edgeCount * 2) + "\n");
            // percorre todos os vértices e seus vizinhos
            for (int u = 0; u < n; u++) {
                for (int v : adj[u]) {
                    fw.write((u+1) + " " + (v+1) + "\n");
                }
            }
            fw.close();
        }

        // debugging: imprime arestas
        void printEdges() {
            System.out.println("Arestas:");
            for (int u = 0; u < n; u++) {
                for (int v : adj[u]) if (u < v) System.out.println("  " + u + " - " + v);
            }
        }
    }

    // Gera um grafo aleatório conectado simples como ponto de partida.
    static Graph randomConnectedGraph(int n, double p, Random rnd) {
        Graph g = new Graph(n);

        // Gera Erdos-Renyi aproximado (p probabilidade por par), sem garantir conexidade
        for (int u = 0; u < n; u++) {
            for (int v = u + 1; v < n; v++) {
                if (rnd.nextDouble() < p) g.addEdge(u, v);
            }
        }

        // Garantir que o grafo seja conectado (considerando apenas vértices com grau > 0):
        // Conectamos componentes com arestas simples.
        // Primeiro, se houver vértices isolados, conecte-os a um vértice aleatório.
        for (int v = 0; v < n; v++) {
            if (g.degree(v) == 0) {
                int target = rnd.nextInt(n);
                if (target == v) target = (v+1) % n;
                g.addEdge(v, target);
            }
        }
        // Agora, se ainda houver mais de um componente (improvável), conecte componentes.
        // Usa busca por componentes e conecta um representante de cada componente.
        boolean[] visited = new boolean[n];
        List<Integer> reps = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                // bfs
                Queue<Integer> q = new ArrayDeque<>();
                q.add(i); visited[i] = true;
                reps.add(i);
                while (!q.isEmpty()) {
                    int u = q.poll();
                    for (int v : g.adj[u]) if (!visited[v]) { visited[v] = true; q.add(v); }
                }
            }
        }
        for (int i = 0; i + 1 < reps.size(); i++) {
            int a = reps.get(i), b = reps.get(i+1);
            if (!g.hasEdge(a,b)) g.addEdge(a,b);
        }
        return g;
    }

    // Tenta ajustar grafo para que todos os vértices tenham grau par (Euleriano)
    static void makeEulerian(Graph g, Random rnd) {
        List<Integer> odds = g.oddVertices();
        // while existem vértices ímpares, pareie-os
        int attempts = 0;
        while (odds.size() > 0 && attempts < 10_000) {
            attempts++;
            // pega primeiro ímpar u
            int u = odds.remove(0);
            if (odds.isEmpty()) break; // safety (não deveria ocorrer)
            // escolhe v para parear
            int v = -1;
            // tenta encontrar v tal que (u,v) não exista
            for (int i = 0; i < odds.size(); i++) {
                if (!g.hasEdge(u, odds.get(i))) { v = odds.remove(i); break; }
            }
            if (v != -1) {
                g.addEdge(u, v);
            } else {
                // todos os possíveis pares com u já estão ligados; tente encontrar w tal que
                // (u,w) e (v,w) ausentes para um v qualquer
                boolean fixed = false;
                for (int i = 0; i < odds.size() && !fixed; i++) {
                    int cand = odds.get(i);
                    for (int w = 0; w < g.n && !fixed; w++) {
                        if (w == u || w == cand) continue;
                        if (!g.hasEdge(u, w) && !g.hasEdge(cand, w)) {
                            g.addEdge(u, w);
                            g.addEdge(cand, w);
                            // remove cand da lista e não re-adiciona u (ambos ficaram pares)
                            odds.remove(i);
                            fixed = true;
                        }
                    }
                }
                if (!fixed) {
                    // fallback: tente achar qualquer par (x,y) de ímpares e adiciona aresta se possível
                    boolean paired = false;
                    for (int i = 0; i < odds.size() && !paired; i++) {
                        int cand = odds.get(i);
                        if (!g.hasEdge(u, cand)) {
                            g.addEdge(u, cand);
                            odds.remove(i);
                            paired = true;
                        }
                    }
                    if (!paired) {
                        // última opção: reiniciar lista de ímpares e re-tentar
                        odds = g.oddVertices();
                    }
                }
            }
            odds = g.oddVertices();
        }

        // se ainda restarem ímpares e tentativas acabaram, podemos tentar pequenas modificações:
        if (!g.isEulerianCircuit()) {
            // pequena heurística: percorre pares de vértices pares e tenta adicionar aresta dupla via intermediário
            List<Integer> oddNow = g.oddVertices();
            if (oddNow.size() > 0) {
                // simples tentativa extra: pareie aleatoriamente com probabilidade
                while (oddNow.size() >= 2) {
                    int a = oddNow.remove(0);
                    int b = oddNow.remove(0);
                    if (!g.hasEdge(a,b)) g.addEdge(a,b);
                    else {
                        // encontra w qualquer que permita duas arestas
                        boolean done = false;
                        for (int w = 0; w < g.n && !done; w++) {
                            if (w==a || w==b) continue;
                            if (!g.hasEdge(a,w) && !g.hasEdge(b,w)) {
                                g.addEdge(a,w);
                                g.addEdge(b,w);
                                done = true;
                            }
                        }
                    }
                }
            }
        }
    }

    // Ajusta para semi-euleriano: exatamente 2 vértices ímpares.
    static void makeSemiEulerian(Graph g, Random rnd) {
        // Primeiro garanta conexidade
        if (!g.isConnectedConsideringEdges()) {
            // tenta tornar conexo ligando componentes
            // (a randomConnectedGraph já retornou conexo, então isso é rara exceção)
            for (int i = 0; i < g.n - 1; i++) if (!g.hasEdge(i, i+1)) g.addEdge(i, i+1);
        }
        List<Integer> odds = g.oddVertices();
        // Se temos >2 ímpares, pareie aleatoriamente até sobrar 2
        while (odds.size() > 2) {
            int u = odds.remove(0);
            int v = -1;
            for (int i = 0; i < odds.size(); i++) {
                if (!g.hasEdge(u, odds.get(i))) { v = odds.remove(i); break; }
            }
            if (v == -1) {
                // tenta com intermediário
                int cand = odds.remove(0);
                boolean fixed = false;
                for (int w = 0; w < g.n && !fixed; w++) {
                    if (w==u || w==cand) continue;
                    if (!g.hasEdge(u,w) && !g.hasEdge(cand,w)) {
                        g.addEdge(u,w);
                        g.addEdge(cand,w);
                        fixed = true;
                    }
                }
                if (!fixed) {
                    // tentativa de adicionar aresta direta
                    if (!g.hasEdge(u, cand)) g.addEdge(u, cand);
                }
            } else {
                g.addEdge(u, v);
            }
            odds = g.oddVertices();
        }

        // Se menos de 2 ímpares (0), force dois vértices a ficarem ímpares removendo/ adicionando
        if (odds.size() == 0) {
            // escolha dois vértices e torne-os ímpares: remover ou adicionar aresta entre eles
            int a = 0, b = 1;
            if (!g.hasEdge(a, b)) g.addEdge(a, b);
            else g.removeEdge(a, b); // isso os torna ímpares
        }
    }

    // Faz um grafo não-euleriano: vamos garantir ou >2 vértices ímpares ou desconectar.
    static void makeNonEulerian(Graph g, Random rnd) {
        // simples: garanta que haja 3 vértices ímpares (ou mais)
        List<Integer> odds = g.oddVertices();
        // se já tem >2, pronto
        if (odds.size() > 2) return;
        // caso contrário, torne 3 vértices ímpares: escolha 3 vértices distintos e ajuste
        Set<Integer> targets = new HashSet<>();
        for (int i = 0; i < g.n && targets.size() < 3; i++) targets.add(i);
        List<Integer> tlist = new ArrayList<>(targets);
        for (int t : tlist) {
            // se t é par, faça uma alteração simples: adicionar/remover aresta com alguém
            if ((g.degree(t) & 1) == 0) {
                // tenta encontrar v tal que (t,v) não exista e adiciona
                boolean added = false;
                for (int v = 0; v < g.n; v++) {
                    if (v==t) continue;
                    if (!g.hasEdge(t, v)) { g.addEdge(t, v); added = true; break; }
                }
                if (!added) {
                    // se todos ligados (grafo cheio), remova alguma aresta
                    for (int v : g.adj[t]) { g.removeEdge(t, v); break; }
                }
            }
        }
        // agora provavelmente há >=3 ímpares
    }

    // Geração de alto nível
    public static Graph generate(Type type, int n, double p, long seed) {
        Random rnd = new Random(seed);
        Graph g = randomConnectedGraph(n, p, rnd);
        switch (type) {
            case EULERIAN:
                makeEulerian(g, rnd);
                // garantir conectividade e propriedade final
                if (!g.isConnectedConsideringEdges()) {
                    // tenta reconectar
                    for (int i = 0; i < n-1; i++) if (!g.hasEdge(i, i+1)) g.addEdge(i, i+1);
                    makeEulerian(g, rnd);
                }
                break;
            case SEMI_EULERIAN:
                makeSemiEulerian(g, rnd);
                break;
            case NON_EULERIAN:
                makeNonEulerian(g, rnd);
                break;
        }
        return g;
    }

    // Demonstração / testes rápidos
    public static void main(String[] args) throws IOException {
        int[] n = {10000}; // número de vértices (mude para testar)
        double p = 0.18; // densidade inicial
        long seed = 42;


        System.out.println("=== Exemplos: Euleriano ===");
        for (int i : n) {
            System.out.println("Gerando euleriano: " + i + " vértices...");
            Graph gE = generate(Type.EULERIAN, i, p, seed);
            gE.makeFile("./Grafos/Experimentos/Eulerianos/graph-e-" + i + ".txt");
        }
        System.out.println();

        System.out.println("=== Exemplos: Semi-Euleriano ===");
        for (int i : n) {
            System.out.println("Gerando semi-euleriano: " + i + " vértices...");
            Graph gS = generate(Type.SEMI_EULERIAN, i, p, seed+1);
            gS.makeFile("./Grafos/Experimentos/SemiEulerianos/graph-se-" + i + ".txt");
        }
        System.out.println();

        System.out.println("=== Exemplos: Não-Euleriano ===");
        for (int i : n) {
            System.out.println("Gerando não-euleriano: " + i + " vértices...");
            Graph gN = generate(Type.NON_EULERIAN, i, p, seed+2);
            gN.makeFile("./Grafos/Experimentos/NaoEulerianos/graph-ne-" + i + ".txt");
        }
        System.out.println();
    }
}