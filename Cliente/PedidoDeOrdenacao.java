public class PedidoDeOrdenacao extends Comunicado
{
    private byte[] vetor;

    public PedidoDeOrdenacao(byte[] vetor)
    {
        this.vetor = vetor;
    }

    public byte[] getVetor()
    {
        return this.vetor;
    }
}