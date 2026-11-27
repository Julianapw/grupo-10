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
    private String erro;

    public ConversaComServidor(String host, int porta, byte[] vetor)
    {
        this.host = host;
        this.porta = porta;
        this.vetor = vetor;
    }

    public void run()
    {
        Socket conexao = null;
        Parceiro servidor = null;

        try
        {
            conexao = new Socket(this.host, this.porta);

            ObjectOutputStream transmissor = new ObjectOutputStream(conexao.getOutputStream());
            ObjectInputStream receptor = new ObjectInputStream(conexao.getInputStream());
            servidor = new Parceiro(conexao, receptor, transmissor);

            servidor.receba(new PedidoDeOrdenacao(this.vetor));
            Comunicado comunicado = servidor.envie();

            if (comunicado instanceof Resultado)
            {
                byte[] vetorRecebido =
                    ((Resultado) comunicado).getVetor();

                if (vetorRecebido == null)
                    throw new Exception("O servidor devolveu um vetor nulo.");

                if (vetorRecebido.length != this.vetor.length)
                    throw new Exception("O servidor devolveu uma quantidade incorreta de elementos.");

                for (int i = 1; i < vetorRecebido.length; i++)
                {
                    if (vetorRecebido[i - 1] > vetorRecebido[i])
                        throw new Exception("O servidor devolveu um vetor fora de ordem.");
                }

                this.resultado = vetorRecebido;
                this.deuCerto = true;
            }
            else
            {
                throw new Exception("Resposta inesperada do servidor.");
            }
        }
        catch (Exception erro)
        {
            this.deuCerto = false;
            this.resultado = null;
            this.erro = erro.getMessage();
        }
        finally
        {
            if (servidor != null)
            {
                try
                {
                    servidor.receba(new PedidoParaSair());
                }
                catch (Exception erro)
                {}

                try
                {
                    servidor.adeus();
                }
                catch (Exception erro)
                {}
            }
            else if (conexao != null)
            {
                try
                {
                    conexao.close();
                }
                catch (Exception erro)
                {}
            }
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

    public String getEndereco()
    {
        return this.host + ":" + this.porta;
    }

    public String getErro()
    {
        return this.erro == null ? "Falha desconhecida." : this.erro;
    }
}
