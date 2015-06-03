import java.util.*;
import java.util.stream.*;

public class Biblioteca
{
	public static void main(String[] args) throws Exception
	{
		GregorianCalendar now = new GregorianCalendar();

		Sistema sist = new Sistema(now.getTime());
		
		sist.carregaArquivoUsuarios();
		sist.carregaArquivoLivros(); 
		sist.carregaEmprestados(); 
		System.out.println("=====================");
		sist.listarUsuarios();
		System.out.println("=====================");
	}
}


