
public class BookUnavailableException extends Exception
{
	public BookUnavailableException() 
	{
		super("Livro não disponível (já emprestado)!");
	}
}