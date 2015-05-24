all: limpa comp exec
limpa:
	clear
comp:
	javac BookUnavailableException.java
	javac Biblioteca.java
	javac Livro.java
	javac Sistema.java
	javac Emprestimo.java
	javac Usuario.java
exec:
	java Biblioteca