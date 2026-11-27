import java.util.Random;

public class ClienteDistribuido
{
    public static void main(String[] args)
    {
        boolean solicitacaoIniciada = false;
        if (args.length == 0)
        {
            System.err.println("Uso: java ClienteDistribuido host:porta [host:porta ...]");
            return;
        }

        try
        {
            EnderecoServidor[] servidores = lerServidores(args);

            System.out.println();
            System.out.println("========== Nova solicitacao ==========");
            solicitacaoIniciada = true;
            System.out.print("Digite o tamanho do vetor: ");
            int tamanho = Teclado.getUmInt();
            while (tamanho <= 0)
            {
                System.out.print("Tamanho invalido! Digite novamente: ");
                tamanho = Teclado.getUmInt();
            }

            byte[] vetor = new byte[tamanho];
            System.out.println();
            System.out.println("Opcoes de preenchimento:");
            System.out.println("1 - Preenchimento manual");
            System.out.println("2 - Preenchimento automatico");
            System.out.print("Como deseja preencher o vetor? ");
            int opcao = Teclado.getUmInt();

            while (opcao != 1 && opcao != 2)
            {
                System.out.println("Opcao invalida! Digite novamente: ");
                opcao = Teclado.getUmInt();
            }

            if (opcao == 1)
            {
                for (int i = 0; i < vetor.length; i++)
                {
                    System.out.print("vetor[" + i + "] = ");
                    while (true)
                    {
                        try
                        {
                            vetor[i] = Teclado.getUmByte();
                            break;
                        }
                        catch (Exception erro)
                        {
                            System.out.print("Valor invalido! Digite novamente: ");
                        }
                    }
                }
            }
            else
            {
                new Random().nextBytes(vetor);
            }

            System.out.println();
            System.out.println("Vetor preparado: " + vetor.length + " elemento(s).");
            int qtdServidores = Math.min(vetor.length, servidores.length);
            System.out.println("Distribuindo entre " + qtdServidores + " servidor(es).");

            long inicio = System.currentTimeMillis();
            byte[][] partes = distribuir(vetor, servidores);
            vetor = null;

            Fase2 fase2 = new Fase2(partes);
            fase2.iniciarFase2();
            vetor = fase2.getVetorResultado();
            long fim = System.currentTimeMillis();

            System.out.println("Ordenacao concluida.");
            System.out.println("Servidores utilizados: " + partes.length);
            System.out.println("Vetor ordenado: " + estaOrdenado(vetor));
            System.out.println("Tempo total: " + (fim - inicio) + " ms.");
            exibirVetor(vetor);
        }
        catch (OutOfMemoryError erro)
        {
            System.err.println("ERRO: Memoria insuficiente para ordenar esse vetor.");
        }
        catch (InterruptedException erro)
        {
            Thread.currentThread().interrupt();
            System.err.println("ERRO: Cliente interrompido enquanto aguardava os servidores.");
        }
        catch (Exception erro)
        {
            System.err.println("ERRO: Falha na solicitacao: " + erro.getMessage());
        }
        finally
        {
            if (solicitacaoIniciada)
                System.out.println("========== Fim da solicitacao ==========");
        }
    }

    private static EnderecoServidor[] lerServidores(String[] args)
    {
        EnderecoServidor[] servidores = new EnderecoServidor[args.length];
        for (int i = 0; i < args.length; i++)
        {
            int separador = args[i].lastIndexOf(':');
            if (separador <= 0 || separador == args[i].length() - 1)
                throw new IllegalArgumentException("Endereco invalido: " + args[i]);

            String host = args[i].substring(0, separador);
            try
            {
                int porta = Integer.parseInt(args[i].substring(separador + 1));
                if (porta < 1 || porta > 65535)
                    throw new NumberFormatException();
                servidores[i] = new EnderecoServidor(host, porta);
            }
            catch (NumberFormatException erro)
            {
                throw new IllegalArgumentException("Porta invalida: " + args[i]);
            }
        }
        return servidores;
    }

    private static byte[][] distribuir(byte[] vetor, EnderecoServidor[] servidores) throws InterruptedException, Exception
    {
        int qtdServidoresUsados = Math.min(vetor.length, servidores.length);
        byte[][] partes = new byte[qtdServidoresUsados][];
        ConversaComServidor[] conversas =
            new ConversaComServidor[qtdServidoresUsados];
        int tamanhoBase = vetor.length / qtdServidoresUsados;
        int resto = vetor.length % qtdServidoresUsados;
        int inicio = 0;

        for (int i = 0; i < qtdServidoresUsados; i++)
        {
            int tamanhoParte = tamanhoBase + (i < resto ? 1 : 0);
            byte[] fatia = new byte[tamanhoParte];
            System.arraycopy(vetor, inicio, fatia, 0, tamanhoParte);
            conversas[i] = new ConversaComServidor(
                servidores[i].host, servidores[i].porta, fatia);
            conversas[i].start();
            inicio += tamanhoParte;
        }

        for (ConversaComServidor conversa : conversas)
        {
            conversa.join();
            if (!conversa.deuCerto())
                throw new Exception("Falha no servidor " + conversa.getEndereco() + ": " + conversa.getErro());
        }

        for (int i = 0; i < conversas.length; i++)
            partes[i] = conversas[i].getResultado();
        return partes;
    }

    private static boolean estaOrdenado(byte[] vetor)
    {
        for (int i = 1; i < vetor.length; i++)
            if (vetor[i - 1] > vetor[i])
                return false;
        return true;
    }

    private static void exibirVetor(byte[] vetor) throws Exception
    {
        System.out.println();
        System.out.println("Deseja imprimir o vetor ordenado?");
        System.out.println("1 - Imprimir todo o vetor");
        System.out.println("2 - Imprimir uma parte do vetor");
        System.out.println("3 - Nao imprimir");
        System.out.print("Opcao: ");
        int opcao = Teclado.getUmInt();

        while (opcao < 1 || opcao > 3)
        {
            System.out.print("Opcao invalida! Digite novamente: ");
            opcao = Teclado.getUmInt();
        }
        System.out.println();

        if (opcao == 1)
        {
            for (int i = 0; i < vetor.length; i++)
                System.out.print(vetor[i] + " ");
            System.out.println();
        }
        else if (opcao == 2)
        {
            System.out.print("Digite a posicao inicial: ");
            int inicio = Teclado.getUmInt();
            System.out.print("Digite a posicao final: ");
            int fim = Teclado.getUmInt();

            while (inicio < 0 || fim >= vetor.length || inicio > fim)
            {
                System.out.println("Posicoes invalidas!");
                System.out.print("Digite a posicao inicial: ");
                inicio = Teclado.getUmInt();
                System.out.print("Digite a posicao final: ");
                fim = Teclado.getUmInt();
            }

            for (int i = inicio; i <= fim; i++)
                System.out.print(vetor[i] + " ");
            System.out.println();
        }
        else if (opcao == 3)
        {
            System.out.println("Vetor nao sera exibido.");
        }
        else
        {
            throw new IllegalArgumentException("Opcao invalida.");
        }
    }
}
