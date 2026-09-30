public class Programa
{
    public static void main (String[] args)
    {
        try{
            System.out.print(" ORDENACAO DE VETORES COM PARALELISMO ");
            System.out.println();
            System.out.print("Digite o tamanho do vetor: ");
            int tamanhoVetor = Teclado.getUmInt();

            while (tamanhoVetor <= 0)
            {
                System.out.print("Tamanho invalido! Digite novamente: ");
                tamanhoVetor = Teclado.getUmInt();
            }

            byte[] vetor = new byte[tamanhoVetor];
            
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

            if (opcao==1)
            {
                for (int i=0; i<vetor.length; i++)
                {
                    System.out.print ("vetor["+i+"] = ");
                    while (true) {
                        try {
                            vetor[i] = Teclado.getUmByte();
                            break;
                        } catch (Exception e) {
                            System.out.print("Valor invalido! Digite novamente: ");
                        }
                    }
                }
            }
            else
            {
                for (int i=0; i<vetor.length; i++)
            vetor[i] = (byte)((int)(Math.random()*256)-128);            
            }

            long inicio = System.currentTimeMillis();

            int qtdProcessadores = Runtime.getRuntime().availableProcessors();
            int qtdThreads = Math.max(1, qtdProcessadores - 1);
            qtdThreads = Math.min(qtdThreads, tamanhoVetor);

            System.out.println();
            System.out.println("\n Qtd de Processadores: " + qtdProcessadores);
            System.out.println(" Qtd threads ordenadora(s) " + qtdThreads);

            Fase1 fase1 = new Fase1(vetor, qtdThreads);
            fase1.iniciarFase1();

            byte[][] subVetores = fase1.getSubVetores();

            Fase2 fase2 = new Fase2(subVetores);
            fase2.iniciarFase2();

            vetor = fase2.getVetorResultado();

            long fim = System.currentTimeMillis();
            System.out.println("Tempo de execucao: " + (fim - inicio) + " ms");


            System.out.println();
            System.out.println("Deseja imprimir o vetor ordenado?");
            System.out.println("1 - Imprimir todo o vetor");
            System.out.println("2 - Imprimir uma parte do vetor");
            System.out.println("3 - Nao imprimir");
            System.out.print("Opcao: ");

            int opcaoPrint = Teclado.getUmInt();

            while (opcaoPrint<1 || opcaoPrint>3)
            {
                System.out.print("Opcao invalida! Digite novamente: ");
                opcaoPrint = Teclado.getUmInt();
            }

            if (opcaoPrint==1)
            {
                for (int i=0; i<vetor.length; i++)
                    System.out.print(vetor[i] + " ");

                System.out.println();
            }
            else
            if (opcaoPrint==2)
            {
                System.out.print("Digite a posicao inicial: ");
                int inicioPrint = Teclado.getUmInt();

                System.out.print("Digite a posicao final: ");
                int fimPrint = Teclado.getUmInt();

                while (inicioPrint<0 || fimPrint>=vetor.length || inicioPrint>fimPrint)
                {
                    System.out.println("Posicoes invalidas!");

                    System.out.print("Digite a posicao inicial: ");
                    inicioPrint = Teclado.getUmInt();

                    System.out.print("Digite a posicao final: ");
                    fimPrint = Teclado.getUmInt();
                }

                for (int i=inicioPrint; i<=fimPrint; i++)
                    System.out.print(vetor[i] + " ");

                System.out.println();
            }
            else
            {
                System.out.println("Vetor nao sera exibido.");
            }
        }
        catch (OutOfMemoryError e) {
            System.out.println("Erro: Memoria insuficiente para alocar um vetor deste tamanho!");
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}
