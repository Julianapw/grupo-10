# grupo-10

### Ana Beatriz Maranho 23000755
### Júlia Carolina Kimura 23031075
### Juliana Prado Ferreira 24003497

## Diário de desenvolvimento da atividade

### 29/09/2026 - 19h20 - 21h

 Ana Beatriz: Desenvolvimento das classes de tarefa ordenadora e juntadora

 Júlia: Desenvolvimento das classes de progama (main) e lógica de ordenação sem paralelismo usando merge sort, ajuste classe Teclado

 Juliana: Desenvolvimento da classe de Merge Sort e lógica de ordenação sem paralelismo usando merge sort

### 29/09/2026 - 22h - 00h

Ana Beatriz: Testes, criação da classe Fase1, conclusão, ajustes necessários de logs

Júlia: Integração das classes na main, conclusão, ajustes de funcionamento, criação da classe MaiorVetorAproximado

Juliana: Testes, criação da classe Fase2, conclusão, ajustes de exceção


## Conclusão dos devs
Durante o desenvolvimento, a equipe pôde observar na prática os desafios e as vantagens da programação concorrente em Java. A adaptação do algoritmo MergeSort para operar corretamente com a divisão das arrays exigiu bastante atenção aos índices de intercalação. O ponto alto e de maior aprendizado prático foi a orquestração das threads: utilizar o método join() foi fundamental para garantir que a TarefaJuntadora apenas iniciasse seu trabalho após a conclusão das subdivisões da TarefaOrdenadora.

## Relato breve (até 10 linhas) sobre os testes realizados.
Os testes avaliaram vetores de 50, 10.000 e até 1.000.000.000 de elementos. A execução paralela operou com 12 processadores e 11 threads ordenadoras. No vetor menor, a versão sequencial foi superior (0 ms), pois o custo de criar e sincronizar threads excedeu o tempo de ordenação. Contudo, no teste extremo de 1 milhão de posições, o paralelismo se provou mais eficiente, finalizando o trabalho em apenas 14988 ms e provando que a divisão de tarefas compensa o custo adicional em grandes volumes, enquanto o teste com 1 milhão de elementos na ordenação sem paralelismo demorou 78137ms. Cabe ressaltar também que os println() dentro das threads geram um alto custo de I/O, logo, embora exigidos pela atividade, esses logs tornam a versão paralela artificialmente mais lenta em comparações puras de desempenho.