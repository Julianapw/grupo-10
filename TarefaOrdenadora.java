public class TarefaOrdenadora extends Thread
{
    private byte[] vetor;

    public TarefaOrdenadora (byte[] vetor) throws Exception
    {
        if (vetor==null)
            throw new Exception ("Vetor ausente");

        this.vetor = vetor;
    }

    @Override
    public void run ()
    {
        MergeSort.ordene (this.vetor, 0, this.vetor.length-1);
    }

    public byte[] getResultado ()
    {
        return this.vetor;
    }
}