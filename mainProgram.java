import java.io.File;
import java.util.List;
import java.util.Scanner;

public class mainProgram {
    public static void main(String[] args) {

        System.out.println("\n -------------------------------------------------------------- \n");

        System.out.println("--- Experimentos Eulerianos ---");

        // runByFile("Grafos/Experimentos/Eulerianos/graph-e-100000-1.txt");
        // runByFile("Grafos/Experimentos/Eulerianos/graph-e-100000-2.txt");
        // runByFile("Grafos/Experimentos/Eulerianos/graph-e-100000-3.txt");
        // runByFile("Grafos/Experimentos/Eulerianos/graph-e-100000-4.txt");
        // runByFile("Grafos/Experimentos/Eulerianos/graph-e-100000-5.txt");

        // System.out.println("\n -------------------------------------------------------------- \n");

        // System.out.println("--- Experimentos Semi-Eulerianos ---");

        // runByFile("Grafos/Experimentos/SemiEulerianos/graph-se-100-1.txt");
        // runByFile("Grafos/Experimentos/SemiEulerianos/graph-se-100000-2.txt");
        // runByFile("Grafos/Experimentos/SemiEulerianos/graph-se-100000-3.txt");
        // runByFile("Grafos/Experimentos/SemiEulerianos/graph-se-100000-4.txt");
        // runByFile("Grafos/Experimentos/SemiEulerianos/graph-se-100000-5.txt");

        // System.out.println("\n -------------------------------------------------------------- \n");

        // System.out.println("--- Experimentos Não Eulerianos ---");

        // runByFile("Grafos/Experimentos/NaoEulerianos/graph-ne-100000-1.txt");
        // runByFile("Grafos/Experimentos/NaoEulerianos/graph-ne-1000-2.txt");
        // runByFile("Grafos/Experimentos/NaoEulerianos/graph-ne-1000-3.txt");
        // runByFile("Grafos/Experimentos/NaoEulerianos/graph-ne-1000-4.txt");
        // runByFile("Grafos/Experimentos/NaoEulerianos/graph-ne-1000-5.txt");

        
    }

    public static void runByFile(String filename) {
        try {
            System.out.print("Rodando com o arquivo: " + filename + "\n");

            String arquivo = filename;
            File file = new File(arquivo);
            Scanner sc_arq = new Scanner(file);

            String header = sc_arq.nextLine();
            // Regex sugerido pelo ChatGPT
            String[] header_parts = header.split("\\s+");
            int n_vertices = Integer.parseInt(header_parts[0]);
            int m_linhas = Integer.parseInt(header_parts[1]);

            Grafo grafo = new Grafo(n_vertices);

            for (int i = 0; i < m_linhas; i++) {
                String line = sc_arq.nextLine();
                String[] line_parts = line.trim().split("\\s+");
                int origem = Integer.parseInt(line_parts[0]);
                int destino = Integer.parseInt(line_parts[1]);

                grafo.adicionar(origem, destino);
            }
            sc_arq.close();

            // grafo.print();

            System.out.println("Graus:");
            int graus_par = 0;
            int graus_impar = 0;
            for (Node i : grafo.lista) {
                if (i != null) {
                    int grau = grafo.getGrau(i.num);
                    if (grau % 2 == 0) {
                        graus_par++;
                    } else {
                        graus_impar++;
                    }
                }
            }
            System.out.println();

            System.out.println("Número de graus pares: " + graus_par);
            System.out.println("Número de graus impares: " + graus_impar);

            // Algoritmo de Fleury - Análise do grafo:
            long startTime = System.currentTimeMillis();
            List<Integer> caminhoEuleriano = metodoFleury.encontrarCaminhoEuleriano(grafo);
            long endTime = System.currentTimeMillis(); // Armazena o tempo final
            long duration = endTime - startTime; // Calcula a diferença
            metodoFleury.imprimirCaminho(caminhoEuleriano);
            if (caminhoEuleriano == null || caminhoEuleriano.isEmpty()) {
                System.out.println("Não existe caminho euleriano");
            } else {
                System.out.println("Caminho euleriano encontrado");
            }
            System.out.println("Tempo de execução: " + duration + " milissegundos");

        } catch (Exception e) {
            System.out.println("Erro");
            e.printStackTrace();
        }
    }

}