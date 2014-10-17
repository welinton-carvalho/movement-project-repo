package br.com.movement.eao;

import java.util.ArrayList;
import java.util.List;

import br.com.movement.entity.Movement;
import br.com.movement.util.ReadFileUtil;

/**
 * Camada EAO para prover acesso aos dados da entidade Movimento.
 * 
 * @author Welinton Carvalho.
 * @since 15/10/2014.
 * @version 1.0.
 */
public class MovementEAO {

	/**
	 * Construtor padrão.
	 * */
	public MovementEAO() {

	}
		
	/**
	 * Método que recupera o movimento de caixa mensal de cada filial presente no arquivo de texto.
	 * 
	 * @param filePath Caminho do arquivo de texto.
	 * @return Lista de movimentações mensal de todas as filiais informadas no arquivo de texto. 
	 * */
	public List<Movement> loadDataFromFile(String filePath){
		
		// Acessa o utilitário de leitura de arquivos txt e recupera os campos encontrados.
		List<String[]> listOfFields = ReadFileUtil.readContent(filePath);
		
		List<Movement> listOfMovements = new ArrayList<Movement>();
		Movement movement = null;
		
		// Faz a leitura dos campos de cada Movimento e insere na lista de Movimentações.
		try{
			for (String[] movementVet : listOfFields) {
				movement = new Movement();
				
				// Pequena validação para integridado do campo nome.
				if (movementVet[0].isEmpty()) {
					movement.setSubsidiaryName("Filial sem nome");
				} else {				
					movement.setSubsidiaryName(movementVet[0]);
				}
		
				// Pequena validação para integridado do campo mês de referência.
				if (movementVet[1].isEmpty()) {
					movement.setRefDate("Mês não informado");
				} else {				
					movement.setRefDate(movementVet[1]);
				}
				
				// Pequena validação para integridado do campo valor da movimentação.
				if (movementVet[2].isEmpty()) {
					movement.setMovementValue(0);
				} else {				
					double movementValue = 0;
					// Validação para ajustar pontos de virgulas do valor de entrada e permitir o parser.
					movementValue =  Double.valueOf(movementVet[2].replaceAll("\\.", "").replaceAll(",", "."));
					movement.setMovementValue(movementValue);
				}
				
				// Depois das validações a movimentação é adicionada a lista.
				listOfMovements.add(movement);
	
			}
		} catch (Exception e) {
			System.out.println("[EAO] Erro ao fazer o parser para o objeto Moviment, verifique se há informações fora do padão no arquivo de texto: " + e.getMessage());
			e.printStackTrace();
		}

		if (!listOfMovements.isEmpty()) {
			return listOfMovements;
		} else {
			return null;
		}

	}

}
