import java.io.File;
import java.util.Scanner;

class Node {
    int num;
    Node prox;

    public Node(int num) {
        this.num = num;
        this.prox = null;
    }
}

class Grafo {
    // Lista de adjacência
    Node[] lista;

    public Grafo(int qntVertices) {
        this.lista = new Node[qntVertices + 1];
        for (int i = 1; i <= qntVertices; i++) {
            lista[i] = new Node(i);
        }
    }

    public void adicionar(int origem, int destino) {
        Node node = new Node(destino);
        if (lista[origem].prox == null) {
            lista[origem].prox = node;
        } else {
            node.prox = lista[origem].prox;
            lista[origem].prox = node;
        }
    }

    public int getGrau(int vertice) {
        int grau = 0;

        if (vertice < lista.length) {
            Node ptr = lista[vertice].prox;
            while (ptr != null) {
                grau++;
                ptr = ptr.prox;
            }
        }

        return grau;
    }

    public void print() {
        System.out.println("Número de vértices: " + (lista.length - 1));
        for (int i = 1; i < lista.length; i++) {
            System.out.print("[" + lista[i].num + "] -> ");
            Node ptr = lista[i].prox;
            while (ptr != null) {
                String next = ptr.prox != null ? ", " : "";
                System.out.print(ptr.num + next);
                ptr = ptr.prox;
            }
            System.out.println();
        }
    }
}

public class listaAdjacente {
    public static int tempo;
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

            ponteInicial(grafo);
        } catch (Exception e) {
            System.out.println("Erro");
            e.printStackTrace();
        }
        sc.close();
    }

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