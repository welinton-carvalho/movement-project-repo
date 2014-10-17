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
	 * Construtor padr�o.
	 * */
	public MovementEAO() {

	}
		
	/**
	 * M�todo que recupera o movimento de caixa mensal de cada filial presente no arquivo de texto.
	 * 
	 * @param filePath Caminho do arquivo de texto.
	 * @return Lista de movimenta��es mensal de todas as filiais informadas no arquivo de texto. 
	 * */
	public List<Movement> loadDataFromFile(String filePath){
		
		// Acessa o utilit�rio de leitura de arquivos txt e recupera os campos encontrados.
		List<String[]> listOfFields = ReadFileUtil.readContent(filePath);
		
		List<Movement> listOfMovements = new ArrayList<Movement>();
		Movement movement = null;
		
		// Faz a leitura dos campos de cada Movimento e insere na lista de Movimenta��es.
		try{
			for (String[] movementVet : listOfFields) {
				movement = new Movement();
				
				// Pequena valida��o para integridado do campo nome.
				if (movementVet[0].isEmpty()) {
					movement.setSubsidiaryName("Filial sem nome");
				} else {				
					movement.setSubsidiaryName(movementVet[0]);
				}
		
				// Pequena valida��o para integridado do campo m�s de refer�ncia.
				if (movementVet[1].isEmpty()) {
					movement.setRefDate("M�s n�o informado");
				} else {				
					movement.setRefDate(movementVet[1]);
				}
				
				// Pequena valida��o para integridado do campo valor da movimenta��o.
				if (movementVet[2].isEmpty()) {
					movement.setMovementValue(0);
				} else {				
					double movementValue = 0;
					// Valida��o para ajustar pontos de virgulas do valor de entrada e permitir o parser.
					movementValue =  Double.valueOf(movementVet[2].replaceAll("\\.", "").replaceAll(",", "."));
					movement.setMovementValue(movementValue);
				}
				
				// Depois das valida��es a movimenta��o � adicionada a lista.
				listOfMovements.add(movement);
	
			}
		} catch (Exception e) {
			System.out.println("[EAO] Erro ao fazer o parser para o objeto Moviment, verifique se h� informa��es fora do pad�o no arquivo de texto: " + e.getMessage());
			e.printStackTrace();
		}

		if (!listOfMovements.isEmpty()) {
			return listOfMovements;
		} else {
			return null;
		}

	}

}
