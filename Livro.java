package pootrab3;

import java.io.*;



public class Livro{
	
	public enum Tipo{
		TEXTO, GERAL
	}
	private String titulo;
	private String autor;
	private Tipo tipo;
	private int quant;

	public Livro(String titulo, String autor, String tipo, int quant){
		this.titulo = titulo;
		this.autor = autor;
		this.quant = quant;

		if("TEXTO" == tipo.toUpperCase()){
			this.tipo = Tipo.TEXTO;
		}
		else if("GERAL" == tipo.toUpperCase()){
			this.tipo = Tipo.GERAL;
		}
	}

	public String getTitle(){
		return this.titulo;
	}

	public String getAuthor(){
		return this.autor;
	}

	public int getQuant(){
		return this.quant;
	}

	public String getTipo(){
		switch(this.tipo){
			case TEXTO:
				String tx = "TEXTO";
				return tx;
				
			case GERAL:
				String ge = "GERAL";
				return ge;
				
			default:
				String ti = "TIPO INVALIDO";
				return ti;
				
		}
	}
	

	


}