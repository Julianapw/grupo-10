import java.util.*;

public class ServidorDistribuido
{
    public static String PORTA_PADRAO = "3001";

    public static void main (String[] args)
    {
        if (args.length>1)
        {
            System.err.println("Uso: java ServidorDistribuido [PORTA]");
            return;
        }

        String porta=ServidorDistribuido.PORTA_PADRAO;

        if (args.length==1)
            porta = args[0];

        ArrayList<Parceiro> usuarios = new ArrayList<Parceiro> ();

        try
        {
            AceitadoraDeConexao aceitadoraDeConexao = new AceitadoraDeConexao (porta, usuarios);
            aceitadoraDeConexao.start();
        }
        catch (Exception erro)
        {
            System.err.println("Nao foi possivel iniciar na porta " + porta + ": " + erro.getMessage());
            return;
        }

        System.out.println("Servidor ativo na porta " + porta + ". Aguardando solicitacoes.");
    }
}
