import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

class GraphGenerator {
    public static void main(String[] args) {
        try {
            gerar(7, 6);
            System.out.println("Grafo gerado.");
        } catch (IOException e) {
            System.out.println("Erro ao gerar grafo: " + e);
        }
    }

    static Random random = new Random();

    public static void gerar(int n_vertices, int m_arestas) throws IOException {
        Set<String> arestas = new HashSet<>();

        System.out.println("Gerando grafo...");
        // gera M arestas não direcionadas únicas
        while (arestas.size() < m_arestas) {
            int u = random.nextInt(n_vertices) + 1; // 1..N
            int v = random.nextInt(n_vertices) + 1;

            if (u == v) continue; // evita laço

            // evita duplicadas (u,v) e (v,u)
            String a1 = u + "-" + v;
            String a2 = v + "-" + u;
            if (!arestas.contains(a1) && !arestas.contains(a2)) {
                arestas.add(a1);
            }
        }

        String arquivoSaida = "graph-test-" + n_vertices + ".txt";

        try (FileWriter fw = new FileWriter(arquivoSaida)) {
            fw.write(n_vertices + " " + (2 * arestas.size()) + "\n");

            for (String a : arestas) {
                String[] parts = a.split("-");
                int u = Integer.parseInt(parts[0]);
                int v = Integer.parseInt(parts[1]);

                fw.write(u + " " + v + "\n"); // u -> v
                fw.write(v + " " + u + "\n"); // v -> u
            }
        }
    }
}