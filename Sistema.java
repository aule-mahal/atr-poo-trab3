import java.util.*;
import java.util.stream.*;
import java.io.*;

/**
*CHANGELOG:
*	METODOS listarUsuarios();listarLivros();listarEmprestimos() agora retornam array de strings
*	CRIADO LISTA emprestimosInativos
*   CRIADO METODOS addEmprestimoInativo, SalvaArquivoEmprestimoInativo, CarregaEmprestimoInativo
**/

public class Sistema
{
	private GregorianCalendar dataAtual;
	private List<Usuario> todosUsuarios;
	private List<Livro> todosLivros;
	private List<Emprestimo> emprestimosAtivos;
	private List<Emprestimo> emprestimosInativos;
	private int totalEmprestimosAtivos;

	//construtor, recebendo a data a ser usada:
	public Sistema(Date data)
	{
		this.dataAtual = new GregorianCalendar();
		this.dataAtual.setTime(data);
		//System.out.println("Data Atual "+this.dataAtual.getTime());
		todosLivros = new ArrayList<Livro>();
		todosUsuarios = new ArrayList<Usuario>();
		emprestimosAtivos = new ArrayList<Emprestimo>();
		emprestimosInativos = new ArrayList<Emprestimo>();
		this.totalEmprestimosAtivos = 0;
	}

	public void setDataDoSistema(Date data) //seta data no sistema
	{
		this.dataAtual.setTime(data);
	}

	public GregorianCalendar getData() //pega a data so sistema
	{
		System.out.println("Hora: "+ dataAtual.getTime().toString());
		return this.dataAtual;
	}

	public void addUsuario(Usuario user) throws UserCodeAlreadyExistsException
	{
		if(this.todosUsuarios
			.stream()
			.anyMatch(x -> x.getCodigo().equals(user.getCodigo()))
			)
		{
			throw new UserCodeAlreadyExistsException();
		}

		this.todosUsuarios.add(user);
	}

	public void removeUsuario(String codigo) throws NoSuchElementException
	{
		Optional<Usuario> optional = this.todosUsuarios
			.stream()
			.filter( x -> x.getCodigo().equals(codigo))
			.findAny();
		Usuario u = optional.get();
		System.out.println("Removido: " + u.getCodigo());
		todosUsuarios.remove(u);
	}

	//PARECE OK
	public void addEmprestimoInativo(String codigoUsuario, String codigoLivro, GregorianCalendar dataEmprestimo,
		GregorianCalendar dataDevolucao, Boolean atraso){

		Optional<Usuario> optusu = this.todosUsuarios
			.stream()
			.filter( x -> x.getCodigo().equals(codigoUsuario))
			.findAny();
		Usuario u = optusu.get();

		Optional<Livro> optliv = this.todosLivros
			.stream()
			.filter( x -> x.getCodigo().equals(codigoLivro))
			.findAny();
		Livro l = optliv.get();

		Emprestimo inat = new Emprestimo(u, l, dataEmprestimo);
		inat.setAtrasado(dataDevolucao);
		inat.setDataDevolucao(dataDevolucao);

		this.emprestimosInativos.add(inat);

	}

	public List<String> listarUsuarios()
	{
		List<String> lusu = new ArrayList<String>();
		this.todosUsuarios
			.stream()
			.forEach(x->
			{	

				lusu.add("Nome: " + x.getNome());
				lusu.add("Endereco: " + x.getEndereco());
				lusu.add("Codigo: " + x.getCodigo() + "\n\n");

			});
			return lusu;
	}

	public void addLivro(Livro livro) throws BookCodeAlreadyExistsException
	{
		if(this.todosLivros
			.stream()
			.anyMatch(x -> x.getCodigo().equals(livro.getCodigo()))
			)
		{
			throw new BookCodeAlreadyExistsException();
		}
		this.todosLivros.add(livro);
	}

	public void removeLivro(String codigo) throws NoSuchElementException
	{
		Optional<Livro> optional = this.todosLivros
			.stream()
			.filter( x -> x.getCodigo().equals(codigo))
			.findAny();
		Livro livro = optional.get();
		System.out.println("Removido: " + livro.getCodigo());
		todosLivros.remove(livro);
	}

	public List<String> listarLivros()
	{
		List<String> lliv = new ArrayList<String>();
		this.todosLivros
			.stream()
			.forEach(x->
			{
				lliv.add("Titulo : " + x.getTitulo());
				lliv.add("Autor : " + x.getAutor());
				lliv.add("Editora : " + x.getEditora());
				lliv.add("Ano: " + x.getAno());
				lliv.add("Assunto: " + x.getAssunto());				
				lliv.add("Codigo: " + x.getCodigo()+ "\n\n");				
			});
			

			return lliv;
	}

	public void realizarEmprestimo(String codigoUsuario, String codigoLivro, GregorianCalendar dataEmprestimo) throws UserBlockedException, NoSuchElementException, 
																	UserHasMaxNumberOfBookException, BookUnavailableException
	{
		//System.out.println("Data Emprestimo "+dataEmprestimo.getTime());
		this.verificacao(codigoUsuario, codigoLivro, dataEmprestimo);
	}

	public void verificacao(String codigoUsuario, String codigoLivro, GregorianCalendar dataEmprestimo) throws UserBlockedException, NoSuchElementException, 
																	UserHasMaxNumberOfBookException, BookUnavailableException
	{
		Optional<Usuario> optional = this.todosUsuarios
			.stream()
			.filter( x -> x.getCodigo().equals(codigoUsuario))
			.findAny();   //na verdade, "find the only one"
		Usuario user = optional.get();

		if(user.getSuspensoAteEstaData() != null)
		{
			throw new UserBlockedException();
		}
		if(user.getTotalLivrosEmprestados() == user.maxLivros)
		{
			throw new UserHasMaxNumberOfBookException();
		}

		Optional<Livro> opt = this.todosLivros
			.stream()
			.filter( x -> x.getCodigo().equals(codigoLivro))
			.findAny();   //na verdade, "find the only one"
		
		Livro book = opt.get();

		if(book.getEmprestado())
		{
			throw new BookUnavailableException();
		}
		//depois de verificado, empresta livro em caso de sucesso
		this.emprestarLivro(user, book, dataEmprestimo);
	}	

	public void emprestarLivro(Usuario user, Livro book, GregorianCalendar dataAtual) 
	{
		Emprestimo emprest = new Emprestimo(user, book, dataAtual);
		user.incTotalLivrosEmprestados();
		book.setEmprestado(true);
		this.addEmprestimo(emprest);
	}

	public void addEmprestimo(Emprestimo emprest)
	{
		this.emprestimosAtivos.add(emprest);
		this.totalEmprestimosAtivos++;
	}

	public List<String> listarEmprestimos()
	{
		List<String> lemp = new ArrayList<String>();
		this.emprestimosAtivos
			.stream()
			.forEach(x->
			{
				lemp.add("Usuário: " + x.getNomeUsuario() + " ");
				lemp.add("Codigo Usuario: "+ x.getCodigoDoUsuario());
				lemp.add("\n" + "Livro: " + x.getNomeLivro() + " ");
				lemp.add("Codigo Livro: "+ x.getCodigoDoLivro());
				lemp.add("\n"+"Devolver em: " + x.getDataDevolucao().getTime().toString() + "\n\n");
			});
			return lemp;
	}

	public List<Emprestimo> getEmrestimosInativos(){
		return this.emprestimosInativos;
	}

	public void registrarDevolucao(String codigoLivro) throws NoSuchElementException
	{
		Optional<Emprestimo> opt = this.emprestimosAtivos
			.stream()
			.filter(x -> x.getCodigoDoLivro().equals(codigoLivro))
			.findAny();

		Emprestimo emprest = opt.get(); //pode lancar excecao
		this.emprestimosInativos.add(emprest);
		this.removeEmprestimo(emprest);

		/*
		Devolucao devolv = new Devolucao(emprest.getCodigoDoLivro(), emprest.getCodigoDoUsuario(), this.dataAtual);
		devolv.setAtrasado(emprest.getAtrasado());
		this.historicoDevolucoes.add(devolv);
		*/

	}

	public void removeEmprestimo(Emprestimo emprest)
	{
		Optional<Usuario> opt = this.todosUsuarios
			.stream()
			.filter( x -> x.getCodigo().equals(emprest.getCodigoDoUsuario()))
			.findAny();
		Usuario user = opt.get();
		user.decTotalLivrosEmprestados();

		Optional<Livro> optional = this.todosLivros
			.stream()
			.filter( x -> x.getCodigo().equals(emprest.getCodigoDoLivro()))
			.findAny();

		Livro book = optional.get();
		book.setEmprestado(false);

		this.totalEmprestimosAtivos--;
		emprestimosAtivos.remove(emprest);
	}

	public void suspenderUsuario(Usuario user, Emprestimo emprest)//calcula quantos dias de suspensao pro usuário
	{
		int diasDeSuspensao = this.dataAtual.get(Calendar.DAY_OF_YEAR)
		+ 365*(this.dataAtual.get(Calendar.YEAR) - emprest.getDataDevolucao().get(Calendar.YEAR)) 
		- emprest.getDataDevolucao().get(Calendar.DAY_OF_YEAR);

		if(user.getSuspensoAteEstaData() == null) //usuario nao possuia atraso antes
		{
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(this.dataAtual.getTime());
			cal.add(Calendar.DAY_OF_MONTH, diasDeSuspensao);
			user.setSuspensoAteEstaData(cal);
		}
		else
		{
			GregorianCalendar cal = user.getSuspensoAteEstaData();
			cal.add(Calendar.DAY_OF_MONTH, diasDeSuspensao);
			user.setSuspensoAteEstaData(cal);
		}
		
	}

	public void atualizaSistema() //Atuliza sistema, verificando se ha emprestimos vencidos
	{
		this.emprestimosAtivos
			.stream()
			.forEach( x -> x.setAtrasado(this.dataAtual));

		this.emprestimosAtivos
			.stream()
			.filter( x -> x.getAtrasado() == true) //acha emprestimo atrasado
			.forEach( x -> 
			{	
				Optional<Usuario> op =  this.todosUsuarios //procura pelo usuario correspondene ao atraso
					.stream()
					.filter( y -> y.getCodigo().equals(x.getCodigoDoUsuario()))
					.findAny();

				Usuario user = op.get();
				this.suspenderUsuario(user, x);
			}
			);

		this.todosUsuarios
			.stream()
			.filter( x -> x.getSuspensoAteEstaData() != null)
			.filter( x -> x.getSuspensoAteEstaData().before(this.dataAtual))//se a data de suspensao está antes da data atual, nao esta mais suspenso
			.forEach( x -> x.setSuspensoAteEstaData(null));
	}

	public void carregaArquivoUsuarios() 
	{
 
		String arquivocsv = "Arquivos/Dados_usuarios.csv";
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
				if(dadosUsuarios[0].equals("aluno")){        	
        			this.addUsuario(new Aluno(dadosUsuarios[1],dadosUsuarios[2],dadosUsuarios[3]));
        		}
        		else if(dadosUsuarios[0].equals("professor")){
	        		this.addUsuario(new Professor(dadosUsuarios[1],dadosUsuarios[2],dadosUsuarios[3]));	
        		}
        		else{
	        		this.addUsuario(new Comunidade(dadosUsuarios[1],dadosUsuarios[2],dadosUsuarios[3]));
        		}
			}
 
		}
		catch(Exception ex)
		{
			System.out.println("Erro: "+ ex.getMessage());
		}
		finally
		{
			if (br != null)
			{
				try
				{
					br.close();
				}
				catch (IOException ex)
				{
					ex.printStackTrace();
				}
			}
		}
  	}

  	

	public void carregaArquivoLivros() 
 	{
		String arquivocsv = "Arquivos/Dados_livros.csv";
		BufferedReader br = null;
		String linha = "";
		String separatorcsv = "ᶲ";
 	
 		try
		{ 
			br = new BufferedReader(new FileReader(arquivocsv));
			while ((linha = br.readLine()) != null)
			{
    	    	String[] dadosLivros = linha.split(separatorcsv); // use comma as separator
        		/*Verifica de que tipo e o livro*/

        		if (dadosLivros[0].equals("livro geral")){
					this.addLivro(new LivroGeral(dadosLivros[1],dadosLivros[2],dadosLivros[3],dadosLivros[4],Integer.parseInt(dadosLivros[5]),dadosLivros[6]));
					
        		}
				else
					this.addLivro(new LivroTexto(dadosLivros[1],dadosLivros[2],dadosLivros[3],dadosLivros[4],Integer.parseInt(dadosLivros[5]),dadosLivros[6]));
			}
		}
		catch(Exception ex)
		{
			System.out.println("Erro"+ ex.getMessage());
		}
		finally
		{
			if (br != null)
			{
				try
				{
					br.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
   	}

	public void carregaEmprestados() 
  	{
 
		String arquivocsv = "Arquivos/Dados_livros_emprestados.csv";
		BufferedReader br = null;
		String linha = "";
		String separatorcsv = ",";
 
		try
		{ 
			br = new BufferedReader(new FileReader(arquivocsv));
			while ((linha = br.readLine()) != null)
			{	

        		String[] dadosLivrosEmprestados = linha.split(separatorcsv); // use comma as separator

        		this.realizarEmprestimo(dadosLivrosEmprestados[0], dadosLivrosEmprestados[1], new GregorianCalendar(
        								Integer.parseInt(dadosLivrosEmprestados[4]), 
        								Integer.parseInt(dadosLivrosEmprestados[3]),
        								Integer.parseInt(dadosLivrosEmprestados[2]))
        								);
			}
 
		}
		catch(Exception e)
		{
			System.out.println("Erro: "+ e.getMessage());
		}
		finally
		{
			if (br != null)
			{
				try
				{
					br.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}


	//FALTA TESTAR
	public void carregaEmprestimosInativos() 
  	{
 
		String arquivocsv = "Arquivos/Dados_livros_emprestados_inativos.csv";
		BufferedReader br = null;
		String linha = "";
		String separatorcsv = ",";
 
		try
		{ 
			br = new BufferedReader(new FileReader(arquivocsv));
			while ((linha = br.readLine()) != null)
			{	

        		String[] dadosLeitura = linha.split(separatorcsv); // use comma as separator
        		/*
					public void addEmprestimoInativo(String codigoUsuario, String codigoLivro, GregorianCalendar dataEmprestimo
		GregorianCalendar dataDevolucao, Boolean atraso){
        		*/
        		//RECARREGAR EMPRESTIMOS INATIVOS
        		//COD_USUARIO,COD_LIVRO,DIA_EMP,MES_EMP,ANO_EMP,DIA_DEV,MES_DEV,ANO_DEV,ATRASADO

				Boolean atr;
				if(dadosLeitura[8].equals("1")){
					atr = true;
				}
				else{
					atr = false;
				}

        		this.addEmprestimoInativo(dadosLeitura[0], dadosLeitura[1],new GregorianCalendar(
        			Integer.parseInt(dadosLeitura[2]),
        			Integer.parseInt(dadosLeitura[3]),
        			Integer.parseInt(dadosLeitura[4])),
        			new GregorianCalendar(
        			Integer.parseInt(dadosLeitura[5]),
        			Integer.parseInt(dadosLeitura[6]),
        			Integer.parseInt(dadosLeitura[7])),
        			atr);

        		/*
        		this.realizarEmprestimo(dadosLivrosEmprestados[0], dadosLivrosEmprestados[1], new GregorianCalendar(
        								Integer.parseInt(dadosLivrosEmprestados[4]), 
        								Integer.parseInt(dadosLivrosEmprestados[3]),
        								Integer.parseInt(dadosLivrosEmprestados[2]))
        								);
        		*/
			}
 
		}
		catch(Exception e)
		{
			System.out.println("Erro: "+ e.getMessage());
		}
		finally
		{
			if (br != null)
			{
				try
				{
					br.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

//OK
	public void salvaArquivoEmprestimos()
  	{
  		String arquivocsv = "Arquivos/Dados_livros_emprestados.csv";
  		String s = ",";
  		File f = new File(arquivocsv);
  	 	try{
  	 		OutputStream os = new FileOutputStream(f);
  	 		Iterator<Emprestimo> usuit = emprestimosAtivos.iterator();
  	 		while(usuit.hasNext()){
  	 			
  	 			Emprestimo emps = (Emprestimo)usuit.next();
  	 			GregorianCalendar dev;
  	 			dev = emps.getDataEmprestimo();
  	 			
  	 			String content = new String(emps.getCodigoDoUsuario()+s+emps.getCodigoDoLivro()+s+
  	 					dev.get(Calendar.DAY_OF_MONTH)+s+
  	 					dev.get(Calendar.MONTH)+s+
  	 					dev.get(Calendar.YEAR)+"\n");
  	 			byte[] bud = content.getBytes();
  	 			os.write(bud);
  	 			
  	 			
  	 		}
  	 		os.close();
  	 	}
  	 	catch(Exception ex){
  	 		System.out.println("Erro: "+ ex.getMessage());
  	 	}
  		
  	}

  	//Salva os Emprestimos Inativos
  	public void salvaArquivoEmprestimosInativos()
  	{
  		String arquivocsv = "Arquivos/Dados_livros_emprestados_inativos.csv";
  		String s = ",";
  		File f = new File(arquivocsv);
  	 	try{
  	 		OutputStream os = new FileOutputStream(f);
  	 		Iterator<Emprestimo> usuit = emprestimosInativos.iterator();
  	 		while(usuit.hasNext()){
  	 			
  	 			Emprestimo emps = (Emprestimo)usuit.next();
  	 			GregorianCalendar datadev;
  	 			GregorianCalendar dataemp;
  	 			dataemp = emps.getDataEmprestimo();
  	 			datadev = emps.getDataDevolucao();

  	 			int atr = 0;
  	 			if(emps.getAtrasado()){
  	 				atr = 1;
  	 			}
  	 			//FORMATO DE LEITURA EMPRESTIMO INATIVO
  	 			//COD_USUARIO,COD_LIVRO,DIA_EMP,MES_EMP,ANO_EMP,DIA_DEV,MES_DEV,ANO_DEV,ATRASADO
  	 			String content = new String(emps.getCodigoDoUsuario()+s+emps.getCodigoDoLivro()+s+
  	 					dataemp.get(Calendar.DAY_OF_MONTH)+s+
  	 					dataemp.get(Calendar.MONTH)+s+
  	 					dataemp.get(Calendar.YEAR)+s+
  	 					datadev.get(Calendar.DAY_OF_MONTH)+s+
  	 					datadev.get(Calendar.MONTH)+s+
  	 					datadev.get(Calendar.YEAR)+s+
  	 					atr+"\n");
  	 			byte[] bud = content.getBytes();
  	 			os.write(bud);
  	 			
  	 			
  	 		}
  	 		os.close();
  	 	}
  	 	catch(Exception ex){
  	 		System.out.println("Erro: "+ ex.getMessage());
  	 	}
  		
  	}

	//OK
	public void salvaArquivoUsuarios()
  	{
  		String arquivocsv = "Arquivos/Dados_usuarios.csv";
  		String sep = ",";
  		File f = new File(arquivocsv);
  	 	try{
  	 		OutputStream os = new FileOutputStream(f);
  	 		Iterator<Usuario> usuit = todosUsuarios.iterator();
  	 		while(usuit.hasNext()){
  	 			
  	 			Usuario us = (Usuario)usuit.next();
  	 			
  	 			if(us instanceof Aluno){
  	 				String content = new String("aluno"+","+us.getNome()+","+us.getEndereco()+","+us.getCodigo()+"\n");
  	 				byte[] bud = content.getBytes();
  	 				os.write(bud);
  	 			}
  	 			else if(us instanceof Professor){
  	 				String content = new String("professor"+","+us.getNome()+","+us.getEndereco()+","+us.getCodigo()+"\n");
  	 				byte[] bud = content.getBytes();
  	 				os.write(bud);
  	 			}
  	 			else if (us instanceof Comunidade){
  	 				String content = new String("comunidade"+","+us.getNome()+","+us.getEndereco()+","+us.getCodigo()+"\n");
  	 				byte[] bud = content.getBytes();
  	 				os.write(bud);
  	 			}
  	 		}
  	 		os.close();
  	 	}
  	 	catch(Exception ex){
  	 		System.out.println("Erro: "+ ex.getMessage());
  	 	}
  		
  	}

  	//OK
  	public void salvaArquivoLivros()
  	{
  		String arquivocsv = "Arquivos/Dados_livros.csv";
  		String s = "ᶲ";
  		File f = new File(arquivocsv);
  	 	try{
  	 		OutputStream os = new FileOutputStream(f);
  	 		Iterator<Livro> usuit = todosLivros.iterator();
  	 		while(usuit.hasNext()){
  	 			
  	 			Livro li = (Livro)usuit.next();
  	 			int emp;
  	 			if(li.getEmprestado()){
  	 				emp = 1;
  	 			}
  	 			else{
  	 				emp = 0;
  	 			}
  	 			
  	 			
  	 			if(li instanceof LivroTexto){
  	 				String content = new String("livro texto"+s+li.getCodigo()+s+li.getTitulo()+s+li.getAutor()+s+li.getEditora()+s+li.getAno()+s+li.getAssunto()+"\n");
  	 				byte[] bud = content.getBytes();
  	 				os.write(bud);
  	 			}
  	 			else {
  	 				String content = new String("livro geral"+s+li.getCodigo()+s+li.getTitulo()+s+li.getAutor()+s+li.getEditora()+s+li.getAno()+s+li.getAssunto()+"\n");
  	 				byte[] bud = content.getBytes();
  	 				os.write(bud);
  	 			}
  	 			
  	 		}
  	 		os.close();
  	 	}
  	 	catch(Exception ex){
  	 		System.out.println("Erro: "+ ex.getMessage());
  	 	}
  		
  	}


	public Usuario buscaUsuarioCodigo(String code){
		Optional<Usuario> optional = this.todosUsuarios
			.stream()
			.filter( x -> x.getCodigo().equals(code))
			.findAny();
		Usuario u = optional.get();
		return u;
	}
	public Usuario buscaUsuarioNome(String name){
		Optional<Usuario> optional = this.todosUsuarios
			.stream()
			.filter( x -> x.getNome().equals(name))
			.findAny();
		Usuario u = optional.get();
		return u;
	}

	public Livro buscaLivroCodigo(String code){
		Optional<Livro> optional = this.todosLivros
			.stream()
			.filter( x -> x.getCodigo().equals(code))
			.findAny();
		Livro l = optional.get();
		return l;
	}
	public Livro buscaLivroTitulo(String title){
		Optional<Livro> optional = this.todosLivros
			.stream()
			.filter( x -> x.getTitulo().equals(title))
			.findAny();
		Livro l = optional.get();
		return l;
	}

	public Livro buscaLivroAutor(String author){
		Optional<Livro> optional = this.todosLivros
			.stream()
			.filter( x -> x.getAutor().equals(author))
			.findAny();
		Livro l = optional.get();
		return l;
	}
}
