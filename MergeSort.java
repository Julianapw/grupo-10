public class MergeSort
{
    public static void ordene (byte[] vetor, int inicio, int fim)
    {
        
        if (inicio>=fim)
            return;

        int meio = (inicio+fim)/2;

       
        MergeSort.ordene (vetor, inicio, meio);
        MergeSort.ordene (vetor, meio+1, fim);

        
        MergeSort.intercale (vetor, inicio, meio, fim);
    }

    public static void intercale (byte[] vetor, int inicio, int meio, int fim)
    {
        byte[] auxiliar = new byte[fim-inicio+1];

        int i=inicio;
        int j=meio+1;
        int k=0;

        
        while (i<=meio && j<=fim)
        {
            if (vetor[i]<=vetor[j])
            {
                auxiliar[k]=vetor[i];
                i++;
            }
            else
            {
                auxiliar[k]=vetor[j];
                j++;
            }

            k++;
        }

       
        while (i<=meio)
        {
            auxiliar[k]=vetor[i];
            i++;
            k++;
        }

        
        while (j<=fim)
        {
            auxiliar[k]=vetor[j];
            j++;
            k++;
        }

      
        for (i=0; i<auxiliar.length; i++)
            vetor[inicio+i]=auxiliar[i];
    }

    public static byte[] intercale (byte[] vetor1, byte[] vetor2)
    {
        byte[] resultado = new byte [vetor1.length + vetor2.length];

        int i =0;
        int j =0;
        int k =0;

        while (i<vetor1.length && j<vetor2.length)
        {
            if (vetor1[i]<=vetor2[j])
            {
                resultado[k]=vetor1[i];
                i++;
            }
            else
            {
                resultado[k]=vetor2[j];
                j++;
            }
            k++;
        }

        while (i<vetor1.length)
        {
            resultado[k]=vetor1[i];
            i++;
            k++;
        }

        while (j<vetor2.length)
        {
            resultado[k]=vetor2[j];
            j++;
            k++;
        }

        return resultado;
    }
}