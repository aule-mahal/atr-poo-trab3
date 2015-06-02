README

Executar

-- Execute o arquivo jar "Dedalus.jar" para rodar o programa

INSTRUÇÔES DE USO

	Na barra de título (titlebar) é apresentada a data atual em que o sistema está operando.
    O programa apresenta um janela com quatro abas:
   
    Cadastrar:

    	Para cadastrar um novo usuário, escolha qual o tipo do usuário. O tipo do usuário define o 
    	número máximo de livros e o data máxima de empréstimo que poderá ser feito para o usuário
    	de acordo com a seguinte tabela.
    	|-----------|----------------|-------------------------------|
    	|TIPO       |N.Max. de Livros|N. Máximo de Dias de Empréstimo|
    	|-----------|----------------|-------------------------------|
    	|Aluno      |       4        |             15 dias           |
    	|-----------|----------------|-------------------------------|
    	|Professor  |       6        |             30 dias           |
    	|-----------|----------------|-------------------------------|
    	|Comunidade |       2        |             15 dias           | 
    	|-----------|----------------|-------------------------------|

    	Deve-se atribuir um código único ao novo usuário e nenhum campo deve ser deixado em branco.
    	Pressione o botão "Cadastrar" na área de cadastro de usuário.

    	Para cadastrar um novo livro, escolha qual o tipo do livro (Texto ou Geral).
    	Deve-se atribuir um código único ao novo livro, o campo referente ao ano de publicação deve
    	ser preenchido em numerais arábicos e nenhum campo deve ser deixado em branco.
    	Pressione o botão "Cadastrar" na área de cadastro de livro.


    Empréstimo/Devolução:

    	Para realizar um empréstimo entre com o código do usuário e livro nos campos delimitados e 
    	pressione o botão "Realizar empréstimo".
    	Apenas alunos e professores podem retirar livros texto. Qualquer usuário registrado pode retirar
    	livros gerais.
    	Um usuário ficará bloqueado e não poderá retirar livros por dois dias para cada dia de atraso
    	de entrega de um livro.

    	Para realizar uma devolução entre com o código do livro no campo delimitado e pressione o
    	botão "Devolver livro".


    Listar:

    	Selecione se deseja listar os Usuários, Livros ou Empréstimos ativos e pressione o botão "OK".


    Opções:

    	Para mudar a data do sistema digite a nova data do modo especificado no campo de texto e pressione o botão 

    	"Alterar data". Também se deseja pode mudar a data do sistema(hora do SO) apertando o botão "Alterar para Data e hora atual"

    	Na alteração de data, se desejamos voltar no tempo o sistema, não é possivél inserir nenhum dado. Só será permitido fazer buscas(usuário, livro)  e listar(usuarios,livros e emprestimos). Já para uma data futura, todas as operações são permitidas 




