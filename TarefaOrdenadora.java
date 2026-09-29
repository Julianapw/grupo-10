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
        System.out.println ("Thread ordenadora iniciou: " + this.getName());

        MergeSort.ordene (this.vetor, 0, this.vetor.length-1);

        System.out.println ("Thread ordenadora terminou: " + this.getName());
    }

    public byte[] getResultado ()
    {
        return this.vetor;
    }
}

//“A main divide o vetor grande em partes. 
// Cada objeto TarefaOrdenadora recebe uma dessas partes e executa MergeSort nela. 
// Depois do join(), a main pega os pedaços ordenados e passa dois a dois para as TarefaJuntadora.”