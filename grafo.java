class Node {
    int num;
    Node prox;

    public Node(int num) {
        this.num = num;
        this.prox = null;
    }
}

public class Grafo {
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

    public void remover(int origem, int destino) {
        if (lista[origem].prox != null) {
            Node ptr = lista[origem];
            while (ptr.prox != null) {
                if (ptr.prox.num == destino) {
                    ptr.prox = ptr.prox.prox;
                    break;
                }
                ptr = ptr.prox;
            }
            ptr = lista[destino];
            while (ptr.prox != null) {
                if (ptr.prox.num == origem) {
                    ptr.prox = ptr.prox.prox;
                    break;
                }
                ptr = ptr.prox;
            }
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