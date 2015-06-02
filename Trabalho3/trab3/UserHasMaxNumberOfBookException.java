package trab3;

public class UserHasMaxNumberOfBookException extends Exception
{
	public UserHasMaxNumberOfBookException()
	{
		super("Usuário possui máximo número de livros emprestados!");
	}
}
