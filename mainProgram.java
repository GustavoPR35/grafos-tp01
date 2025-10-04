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

            grafo.print();

            // Pontes Tarjan:
            List<int[]> pontes_tarjan = metodoTarjan.ponteInicial(grafo);
            // Pontes naive:
            List<int[]> pontes_naive = metodoNaive.encontrarPontes(grafo);
            
        } catch (Exception e) {
            System.out.println("Erro");
            e.printStackTrace();
        }
        sc.close();
    }
}
