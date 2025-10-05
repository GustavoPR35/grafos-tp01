import java.io.File;
import java.util.List;
import java.util.Scanner;

public class mainProgram {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            System.out.print("Escreva o nome do arquivo a ser aberto: ");

            String arquivo = sc.nextLine();
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
            for (Node i: grafo.lista) {
                if (i != null) {
                    int grau = grafo.getGrau(i.num);
                    System.out.print(grau + ", ");
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

            // System.out.println();

            // System.out.println("Método de Tarjan:");
            // List<int[]> pontes_tarjan = metodoTarjan.ponteInicial(grafo);
            // for (int i[] : pontes_tarjan) {
            //     System.out.println("Ponte: {" + i[0] + ", " + i[1] + "}");
            // }

            // System.out.println();

            // System.out.println("Método Naive:");
            // // List<int[]> pontes_naive = metodoNaive.encontrarPontes(grafo);
            // metodoNaive.encontrarPontes(grafo);

            // Algoritmo de Fleury - Análise do grafo:
            // metodoFleury.analisarGrafo(grafo);
            long startTime = System.currentTimeMillis();
            List<Integer> caminhoEuleriano = metodoFleury.encontrarCaminhoEuleriano(grafo);
            long endTime = System.currentTimeMillis(); // Armazena o tempo final
            long duration = endTime - startTime; // Calcula a diferença
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
        sc.close();
    }
}