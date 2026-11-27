// Essa classe representa uma tarefa que junta dois vetores de bytes ordenados em um único vetor ordenado.
public class TarefaJuntadora extends Thread
{
    private byte[] vetor1;
    private byte[] vetor2;
    private byte[] resultado;

    public TarefaJuntadora (byte[] vetor1, byte[] vetor2) throws Exception
    {
        if (vetor1==null || vetor2==null)
            throw new Exception ("Vetor ausente");

        this.vetor1 = vetor1;
        this.vetor2 = vetor2;
    }

    @Override
    public void run ()
    {
        try{
            this.resultado = MergeSort.intercale (this.vetor1, this.vetor2);
        }
        catch(OutOfMemoryError e) {
            this.resultado = null;
        }
    }

    public byte[] getResultado ()
    {
        return this.resultado;
    }
}