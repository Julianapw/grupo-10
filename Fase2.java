public class Fase2 {
    private byte[][] subVetores;

    public Fase2(byte[][] subVetores) {
        this.subVetores = subVetores;
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
            }

            if (restoSub == 1) {
                proximosSubVetores[metade] = this.subVetores[this.subVetores.length - 1];
            }

            this.subVetores = proximosSubVetores;
        }
    }

    public byte[] getVetorResultado() {
        return this.subVetores[0];
    }
}