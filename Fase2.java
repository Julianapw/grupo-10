public class Fase2 {
    private byte[][] subVetores;
    private int tamanhoOriginal;

    public Fase2(byte[][] subVetores) {
        if (subVetores == null || subVetores.length == 0)
            throw new IllegalArgumentException("Subvetores ausentes");
        int tamanho = 0;
        for (byte[] subVetor : subVetores)
        {
            if (subVetor == null)
                throw new IllegalArgumentException("Subvetor ausente");
            tamanho += subVetor.length;
        }
        this.subVetores = subVetores;
        this.tamanhoOriginal = tamanho;
    }

    public void iniciarFase2() throws Exception {
        while (this.subVetores.length > 1) {
            int metade = this.subVetores.length / 2;
            int restoSub = this.subVetores.length % 2;

            byte[][] proximosSubVetores = new byte[metade + restoSub][];
            TarefaJuntadora[] threadsJunt = new TarefaJuntadora[metade];

            for (int i = 0; i < metade; i++) {
                threadsJunt[i] = new TarefaJuntadora(this.subVetores[i * 2], this.subVetores[i * 2 + 1]);
                threadsJunt[i].start();
            }

            for (int i = 0; i < metade; i++) {
                threadsJunt[i].join();
                proximosSubVetores[i] = threadsJunt[i].getResultado();
                if (proximosSubVetores[i] == null)
                    throw new Exception("Nao foi possivel juntar todos os subvetores.");
            }

            if (restoSub == 1) {
                proximosSubVetores[metade] = this.subVetores[this.subVetores.length - 1];
            }

            this.subVetores = proximosSubVetores;
        }

        byte[] resultado = this.subVetores[0];
        if (resultado.length != this.tamanhoOriginal)
            throw new Exception("O vetor final tem tamanho incorreto.");

        for (int i = 1; i < resultado.length; i++)
        {
            if (resultado[i - 1] > resultado[i])
                throw new Exception("O vetor final nao esta ordenado.");
        }
    }

    public byte[] getVetorResultado() {
        return this.subVetores[0];
    }
}