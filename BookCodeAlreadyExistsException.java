import java.util.*;
import java.io.*;

public class BookCodeAlreadyExistsException extends Exception
{
	public BookCodeAlreadyExistsException()
	{
		super("Código de livro já exsitente!");
	}
}