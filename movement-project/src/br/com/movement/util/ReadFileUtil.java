package br.com.movement.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Utilitário genérico para leitura de arquivos de texto (.txt).
 * 
 * @author Welinton Carvalho.
 * @since 15/10/2014.
 * @version 1.0.
 * */
public class ReadFileUtil {
	
	/**
	 * Método genérico utilizado para ler o arquivo de texto, se comportamento cria uma
	 * lista de Objetos onde cada posição da lista contém um vetor de strings,
	 * posteriormente será necessário fazer um parser dessa lista para uma lista
	 * de objetos de interresse da classe que invocar esse utilitário.
	 * 
	 * @param filePath Caminho do arquivo de texto.
	 * @return Lista de vetores de string, cada posição do vetor corresponde a um campo da entidade abordada.
	 * */
	public static List<String[]> readContent(String filePath) {
		Scanner scan = null;
		List<String[]> listOfResult;		
		try {
			// Carrega o arquivo de acordo com o caminho informado.
			scan = loadFile(filePath);
			listOfResult = new ArrayList<String[]>();
			if (!scan.hasNextLine()) {
				System.out.println("O arquivo informado está vazio.");
			} else {
				while (scan.hasNextLine()) {
					// Lê a linha com o nextLine e a divide em colunas com o split baseando-se no delimitador como tab.
					String[] fields = scan.nextLine().split("\t");
					listOfResult.add(fields);
				}
			}						
			return listOfResult;			
		} catch (Exception e) {
			System.out.println("Não foi possível ler arquivo informado: " + e.getMessage());
			e.printStackTrace();
		} finally {
			scan.close();
		}
		return null;
	}

	/**
	 * Método responsável por carregar o arquivo.
	 * 
	 * @param filePath Caminho do arquivo.
	 * @return Objeto scanner com arquivo carregado em memória.
	 * @throws FileNotFoundException Possibilidade de não encontrar o arquivo. 
	 * */
	private static Scanner loadFile(String filePath) throws FileNotFoundException {
		try {
			if (filePath.isEmpty()) {
				System.out.println("O caminho informado está vazio ou não é válido.");
			} else {
				return new Scanner(new File(filePath));
			}
		} catch (IOException e) {
			System.out.println("Erro ao ler alquivo de texto, vericar o local informado e o conteúdo do arquivo: " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
}
