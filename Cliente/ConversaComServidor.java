import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ConversaComServidor extends Thread
{
    private String host;
    private int porta;
    private byte[] vetor;

    private byte[] resultado;
    private boolean deuCerto;

    public ConversaComServidor(String host, int porta, byte[] vetor)
    {
        this.host = host;
        this.porta = porta;
        this.vetor = vetor;
    }

    public void run()
    {
        try
        {
            Socket conexao = new Socket(this.host, this.porta);
            
            ObjectOutputStream transmissor =
            new ObjectOutputStream(conexao.getOutputStream());
            
            ObjectInputStream receptor =
            new ObjectInputStream(conexao.getInputStream());
            
            Parceiro servidor =
            new Parceiro(conexao, receptor, transmissor);

            System.out.println ("Enviando para "+this.host+":"+this.porta+
                                " -> soma de "+this.inicio+" ate "+this.fim);

            servidor.receba(new PedidoDeOrdenacao(this.vetor));

            Comunicado comunicado = servidor.envie();

            if (comunicado instanceof Resultado)
            {
                this.resultado = ((Resultado)comunicado).getVetor();
                this.deuCerto = true;
            }

            servidor.receba(new PedidoParaSair());
            servidor.adeus();
        }
        catch (Exception erro)
        {
            System.err.println("Falha com o servidor " +this.host+":"+this.porta);
        }
    }

    public boolean deuCerto()
    {
        return this.deuCerto;
    }

    public byte[] getResultado()
    {
        return this.resultado;
    }
}
