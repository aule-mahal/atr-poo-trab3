//Bibioteca


		sist.carregaArquivoUsuarios();
		sist.carregaArquivoLivros(); 
	
		System.out.println("=====================");
		sist.listarEmprestimos();
		sist.carregaEmprestados(); 
		System.out.println("=====================");
	
//Sistema

public void carregaArquivoUsuarios() 
	{
 
		String arquivocsv = "Dados_usuarios.csv";
		BufferedReader br = null;
		String linha = "";
		String separatorcsv = ",";
 
		try
		{ 
			br = new BufferedReader(new FileReader(arquivocsv));
			while ((linha = br.readLine()) != null)
			{
	        	String[] dadosUsuarios = linha.split(separatorcsv); // use comma as separator
    	    	/*Verifica de que tipo e o usuario*/
				if(dadosUsuarios[0] == "aluno")        	
        			this.addUsuario(new Aluno(dadosUsuarios[1],dadosUsuarios[2],dadosUsuarios[3]));
        		else if(dadosUsuarios[0] == "professor")
	        		this.addUsuario(new Professor(dadosUsuarios[1],dadosUsuarios[2],dadosUsuarios[3]));		
        		else
	        		this.addUsuario(new Comunidade(dadosUsuarios[1],dadosUsuarios[2],dadosUsuarios[3]));		
			}
 
		}catch(Exception e){
			System.out.println("Erro"+ e);
		}
		finally {
			if (br != null)
			{
				try
				{
					br.close();
				}catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
   }

	public void carregaArquivoLivros() 
 	{
 
		String arquivocsv = "Dados_livros.csv";
		BufferedReader br = null;
		String linha = "";
		String separatorcsv = "#";
 	
 		try
		{ 
			br = new BufferedReader(new FileReader(arquivocsv));
			while ((linha = br.readLine()) != null)
			{
    	    	String[] dadosLivros = linha.split(separatorcsv); // use comma as separator
        		/*Verifica de que tipo e o livro*/
        		if (dadosLivros[0] == "livro geral")
					this.addLivro(new LivroGeral(dadosLivros[1],dadosLivros[2],dadosLivros[3],dadosLivros[4],Integer.parseInt(dadosLivros[5]),dadosLivros[6]));
				else
					this.addLivro(new LivroTexto(dadosLivros[1],dadosLivros[2],dadosLivros[3],dadosLivros[4],Integer.parseInt(dadosLivros[5]),dadosLivros[6]));
 
			}
 
		}catch(Exception e){
			System.out.println("Erro"+ e);
		}
		finally{

			if (br != null)
			{
				try
				{
					br.close();
				}catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
   }

	public void carregaEmprestados() 
  	{
 
		String arquivocsv = "Dados_livros_emprestados.csv";
		BufferedReader br = null;
		String linha = "";
		String separatorcsv = ",";
 
		try
		{ 
			br = new BufferedReader(new FileReader(arquivocsv));
			while ((linha = br.readLine()) != null)
			{
        		String[] dadosLivrosEmprestados = linha.split(separatorcsv); // use comma as separator
        		this.verificacao(dadosLivrosEmprestados[0],dadosLivrosEmprestados[1]);        	
 
			}
 
		}catch(Exception e){
			System.out.println("Erro"+ e);

		}
		finally{

			if (br != null)
			{
				try
				{
					br.close();
				}catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}