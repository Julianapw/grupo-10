import java.util.Arrays;

public class Fase1 {
    private byte[] vetor;
    private int qtdThreads;
    private byte[][] subVetores;

    public Fase1(byte[] vetor, int qtdThreads) {
        this.vetor = vetor;
        this.qtdThreads = qtdThreads;
        this.subVetores = new byte[qtdThreads][];
    }

    public void iniciarFase1() throws Exception {
        int tamanhoVetor = this.vetor.length;
        int tamanhoFatia = tamanhoVetor / this.qtdThreads;
        int resto = tamanhoVetor % this.qtdThreads;

        TarefaOrdenadora[] threadsOrd = new TarefaOrdenadora[this.qtdThreads];

        int inicioFatia = 0;
        for (int i = 0; i < this.qtdThreads; i++) {
            int fimFatia = inicioFatia + tamanhoFatia + (i < resto ? 1 : 0);
            byte[] pedaco = Arrays.copyOfRange(this.vetor, inicioFatia, fimFatia);

            threadsOrd[i] = new TarefaOrdenadora(pedaco);
            threadsOrd[i].start();

            inicioFatia = fimFatia;
        }

        for (int i = 0; i < this.qtdThreads; i++) {
            threadsOrd[i].join();
            this.subVetores[i] = threadsOrd[i].getResultado();
        }
    }

    public byte[][] getSubVetores() {
        return this.subVetores;
    }
}