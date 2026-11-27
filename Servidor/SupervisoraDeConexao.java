import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class SupervisoraDeConexao extends Thread
{
    private Parceiro            usuario;
    private Socket              conexao;
    private ArrayList<Parceiro> usuarios;

    public SupervisoraDeConexao(Socket conexao, ArrayList<Parceiro> usuarios) throws Exception
    {
        if (conexao == null)
            throw new Exception("Conexao ausente");

        if (usuarios == null)
            throw new Exception("Usuarios ausentes");
            
        this.conexao = conexao;
        this.usuarios = usuarios;
    }

    public void run()
    {
        String endereco = String.valueOf(this.conexao.getRemoteSocketAddress());
        boolean solicitacaoIniciada = false;
        try
        {
            ObjectOutputStream transmissor = new ObjectOutputStream(this.conexao.getOutputStream());

            ObjectInputStream receptor = new ObjectInputStream(this.conexao.getInputStream());

            this.usuario = new Parceiro(this.conexao, receptor, transmissor);

            synchronized (this.usuarios)
            {
                this.usuarios.add(this.usuario);
            }

            while (true)
            {
                Comunicado comunicado = this.usuario.envie();

                if (comunicado instanceof PedidoParaSair)
                {
                    this.usuario.adeus();
                    break;
                }

                if (!(comunicado instanceof PedidoDeOrdenacao))
                    continue;

                solicitacaoIniciada = true;
                byte[] vetor = ((PedidoDeOrdenacao) comunicado).getVetor();
                long inicio = System.currentTimeMillis();

                if (vetor == null)
                    throw new Exception("Vetor ausente");

                System.out.println("Nova solicitacao: vetor de " + vetor.length + " elementos.");

                if (vetor.length == 0)
                    throw new Exception("Vetor vazio");

                int qtdProcessadores = Runtime.getRuntime().availableProcessors();
                int qtdThreads = Math.max(1, qtdProcessadores - 1);
                qtdThreads = Math.min(qtdThreads, vetor.length);
                System.out.println("Ordenando com " + qtdThreads + " thread(s); processadores disponiveis: " + qtdProcessadores + ".");

                Fase1 fase1 = new Fase1(vetor, qtdThreads);
                fase1.iniciarFase1();

                Fase2 fase2 = new Fase2(fase1.getSubVetores());
                fase2.iniciarFase2();

                byte[] resultado = fase2.getVetorResultado();
                this.usuario.receba(new Resultado(resultado));
                System.out.println("Fim da solicitacao: " + (System.currentTimeMillis() - inicio) + " ms.");
                solicitacaoIniciada = false;
            }
        }
        catch (Exception erro)
        {
            String mensagem = erro.getMessage() == null
                ? erro.getClass().getSimpleName()
                : erro.getMessage();
            if (solicitacaoIniciada)
                System.err.println("Fim da solicitacao (erro: " + mensagem + ").");
            else
                System.err.println("Erro ao atender " + endereco + ": " + mensagem);
            return;
        }
        finally
        {
            synchronized (this.usuarios)
            {
                if (this.usuario != null)
                    this.usuarios.remove(this.usuario);
            }

            try
            {
                if (!this.conexao.isClosed())
                    this.conexao.close();
            }
            catch (Exception erro)
            {}
        }
    }
}