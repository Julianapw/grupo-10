public class Programa2
{
    public static void main (String[] args)
    {
        try{
            System.out.print(" ORDENACAO DE VETORES SEM PARALELISMO ");
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
                    vetor[i] = (byte)(Math.random()*1000);
            }

            long inicio = System.currentTimeMillis();

            MergeSort.ordene(vetor, 0, vetor.length - 1);

            long fim = System.currentTimeMillis();
            System.out.println("Tempo de execucao: " + (fim - inicio) + " ms");


            System.out.println();
            System.out.print( "Deseja printar quantos valores do vetor? De 0 (nao printar) a 100 (no max, para nao ficar muito longo):  ");
            int qtdAPrintar = Teclado.getUmInt();

            while (qtdAPrintar < 0 || qtdAPrintar > tamanhoVetor)
            {
                System.out.println("Quantidade invalida! Digite novamente: ");
                qtdAPrintar = Teclado.getUmInt();
            }
            if (vetor.length<=100)
            {
                System.out.println("Vetor final a printar(" + qtdAPrintar + " elementos):");
                for (int i = 0; i < qtdAPrintar; i++) {
                    System.out.print(vetor[i] + " ");
                }
                System.out.println();
            }
            else if(qtdAPrintar == 0 || qtdAPrintar > 100) {
                System.out.println("Ordenacao finalizada. O vetor nao sera exibido.");
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}
