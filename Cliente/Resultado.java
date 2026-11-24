public class Resultado extends Comunicado
{
    private byte[] vetor;

    public Resultado(byte[] vetor)
    {
        this.vetor = vetor;
    }

    public byte[] getVetor()
    {
        return this.vetor;
    }
}
