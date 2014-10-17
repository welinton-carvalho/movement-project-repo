package br.com.movement.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import br.com.movement.eao.MovementEAO;
import br.com.movement.entity.Movement;
import br.com.movement.util.MapUtils;

/**
 * Classe de servi�o respons�vel por controlar as regras de neg�cio da entidade Movimento.
 * 
 * @author Welinton Carvalho.
 * @since 15/10/2014.
 * @version 1.0.
 * */
public class MovementService {

	/**
	 * Caminho do arquivo utilizado para carga de dados.
	 * */
	String filePath = "C:/movimentacoes.txt";

	/**
	 * Lista de Movimenta��es recuperadas pela camada de EAO.
	 * */
	List<Movement> listOfMovements;

	/**
	 * Objeto de EAO utilizado pela entidade Movimento.
	 * */
	private MovementEAO movementEAO;
	
	/**
	 * Constante para definir a ordena��o do haspmap com ASC.
	 * */
	private static char ORDENATION_ASC = 'a';
	
	/**
	 * Constante para definir a ordena��o do haspmap como DESC.
	 * */
	private static char ORDENATION_DESC = 'd';
	
	/**
	 * Constante para definir a formata��o do campo decimal do haspmap como dinheiro.
	 * */
	private static final char CASH_FORMAT = 'c';
	
	/**
	 * Constante para definir a formata��o do campo decimal do haspmap como percentual.
	 * */
	private static final char PERCENTUAL_FORMAT = 'p';


	/**
	 * Construtor padr�o.
	 * */
	public MovementService() {
		this.initializeData();
	}

	/**
	 * M�todo para acessar a camada de EAO e recuperar a lista de movimenta��es proveniente de um arquivo txt.
	 * */
	private void initializeData() {
		movementEAO = new MovementEAO();		
		try{
			listOfMovements = movementEAO.loadDataFromFile(filePath);
		}catch (Exception e){
			System.out.println("[Servi�o] - Erro ao iniciar lista de movimenta��es: " + e.getMessage());
			e.printStackTrace();			
		}
	}

	/**
	 * M�todo que calcula qual filial teve o maior valor de vendas no per�odo, esse m�todo permite um per�odo flexiv�l 
	 * que depende dos registros retornados pelo EAO. Ex. 1 ocorr�ncia de S�o Paulo (Mensal), 2 ocorr�cias de S�o Paulo (Bimestral),
	 * 3 ocorr�cias de S�o Paulo (trimestral) etc. � necess�rio que haja em quantidade igualit�ria ocorr�ncias entre todas 
	 * as filiais informadas, ou seja se houver 2 per�odos de S�o Paulo deve-se ter 2 per�odos informados do Rio de Janeiro e assim
	 * para as demais filiais.
	 * 
	 * @return Movimenta��o constando o nome da filial e a somat�ria do valor de suas movimenta��es.
	 * */
	public Movement subsidiaryWhoSoldMoreOnPeriod() {
		
		HashMap<String, Double> listOfAmountMap = this.checkAmountPeriod();
		
		if (!listOfAmountMap.isEmpty()) {
			//Pega a primeira ocorr�ncia da cole��o ja em ordem decrescente.
			Entry<?, Double> result = MapUtils.sortHashMapByDoubleValue(listOfAmountMap, ORDENATION_DESC).get(0);
			Movement movement = new Movement();
			movement.setSubsidiaryName(result.getKey().toString());
			movement.setMovementValue(result.getValue());

			// Imprime um relat�rio do processamento em linha de comando.
			String[] columns = {"Filial", "M�s de refer�ncia", "Total"};
			System.out.println("---------------------------------------------------------------------------------------");
			System.out.println("Relat�rio de todas as filiais com ordena��o de maior valor de vendas no per�odo:");
			System.out.println("---------------------------------------------------------------------------------------\n");
	 		System.out.println(MapUtils.mapToString(MapUtils.sortHashMapByDoubleValue(listOfAmountMap, ORDENATION_DESC), columns, CASH_FORMAT));			
			
			System.out.println("---------------------------------------------------------------------------------------");
			System.out.println("Filial que teve o maior valor de vendas no per�odo:");
			System.out.println("---------------------------------------------------------------------------------------\n");
			System.out.println(movement.toString());
			
			return movement;
		} else {
			System.out.println("[Servi�o] N�o foi poss�vel calcular o per�odo informado.");
		}
		
		return null;
	}

	
	/**
	 * M�todo que faz a soma dos valore de movimenta��o para cada filial informada.
	 *
	 * @return Map com a soma de todos os meses para cada filial.
	 */
	private HashMap<String, Double> checkAmountPeriod(){		
		
		HashMap<String, Double> listOfAmountMap =  new HashMap<String, Double>();
		
		for (int i = 0; i < (listOfMovements.size()); i++) {
			Movement movementSelected = listOfMovements.get(i);
			double totalMovementValue = movementSelected.getMovementValue();
			
			// Seleciona uma movimenta��o de uma filial e procura nas demais posi��es movimenta��es para essa mesma filial.
			for (int j = (i + 1); j < listOfMovements.size(); j++) {
				if (listOfMovements.get(j).getSubsidiaryName().equalsIgnoreCase(movementSelected.getSubsidiaryName())) {
					totalMovementValue += listOfMovements.get(j).getMovementValue();
				}
			}

			// Verifica se ha repeti��o no map, se sim os valores necess�rios para o calculo j� foram percorridos.
			if (!listOfAmountMap.containsKey(movementSelected.getSubsidiaryName())) {
				// Insere a filial com a soma dos valores de sua movimenta��o.
				listOfAmountMap.put(movementSelected.getSubsidiaryName(), totalMovementValue);
			} else {
				return listOfAmountMap;
			}

			// Limpa o valor do acumulador.
			totalMovementValue = 0;
		}		
		return listOfAmountMap;
	}

	
	/**
	 * M�todo que calcula qual filial teve o maior ou menor valor percentual de aumento ou decremento em vendas em um per�odo de m�s
	 * informado.
	 * 
	 * @param higerLowerGrowth Quando informado 'h' define a processamento para retornar a filial com maior aumento em vendas 
	 * e quando 'l' define menor aumento/decremento em vendas.
	 * @param initialMonth M�s inicial do per�odo.
	 * @param endMonth M�s final do Per�odo.
	 * @return Retorna a filial com maior ou menor valor de aumento em vendas.
	 */
	public Movement subsidiaryWithHigherLowerGrowth(String initialMonth, String endMonth, char higerLowerGrowth){

		Movement movement = new Movement();
		HashMap<String, Double> listOfGrowthMap =  new HashMap<String, Double>();
		
		// Percorre  a lista de movimenta��es para calcular de acordo com o per�odo informado a taxa de crescimento.
		for (int i = 0; i < (listOfMovements.size()); i++) {
			Movement movementSelected = listOfMovements.get(i);
			// Seleciona uma movimenta��o de uma filial e procura nas demais posi��es movimenta��es para essa mesma filial.
			for (int j = (i + 1); j < listOfMovements.size(); j++) {
				if (listOfMovements.get(j).getSubsidiaryName().equalsIgnoreCase(movementSelected.getSubsidiaryName()) 
						&& movementSelected.getRefDate().equals(initialMonth) 
						&& listOfMovements.get(j).getRefDate().equals(endMonth)) {
					
					// F�rmula utilizada: (Valor_Presente / Valor_Passado) / Valor_Passado.
					double growthRate = (listOfMovements.get(j).getMovementValue() - movementSelected.getMovementValue()) / movementSelected.getMovementValue();
					double percentGrowthRate = growthRate * 100;
						
					listOfGrowthMap.put(movementSelected.getSubsidiaryName(), percentGrowthRate);							
				}
			}
		}
		
		// Ordena de acordo com o par�metro passado higerLowerGrowth, 'h' para maior lucro, e 'l' para menor lucro.
		Entry<?, Double> result = null;
		if (higerLowerGrowth == 'h' && !listOfGrowthMap.isEmpty()) {
			result = MapUtils.sortHashMapByDoubleValue(listOfGrowthMap,	ORDENATION_DESC).get(0);
		} else if (higerLowerGrowth == 'l' && !listOfGrowthMap.isEmpty()) {
			result = MapUtils.sortHashMapByDoubleValue(listOfGrowthMap,	ORDENATION_ASC).get(0);
		} else if (!listOfGrowthMap.isEmpty()) {
			result = MapUtils.sortHashMapByDoubleValue(listOfGrowthMap,	ORDENATION_ASC).get(0);
		}
		
		if (result != null) {
			
			// Monta o objeto de retorno.
			movement = new Movement();
			movement.setSubsidiaryName(result.getKey().toString());
			movement.setRefDate("[".concat(initialMonth.concat(" - ").concat(endMonth)).concat("]"));
			movement.setPercentOfGrowthRate(result.getValue());
					
			// Imprime um relat�rio do processamento em linha de comando.
			String[] columns = {"Filial", "Total"};
			if (higerLowerGrowth == 'h') {
				System.out.println("---------------------------------------------------------------------------------------");
				System.out.println("Relat�rio de todas as filiais com ordena��o de maior valor de crescimento em vendas no per�odo:");
				System.out.println("---------------------------------------------------------------------------------------\n");
		 		System.out.println(MapUtils.mapToString(MapUtils.sortHashMapByDoubleValue(listOfGrowthMap, ORDENATION_DESC), columns, PERCENTUAL_FORMAT));
		 		
				System.out.println("---------------------------------------------------------------------------------------");
				System.out.println("Filial que teve maior valor de crescimento em vendas no per�odo:");
				System.out.println("---------------------------------------------------------------------------------------\n");
		 		System.out.println(movement.toStringPercentualOfGrowthRate());
			} else if (higerLowerGrowth == 'l') {
				System.out.println("---------------------------------------------------------------------------------------");
				System.out.println("Relat�rio de todas as filiais com ordena��o de menor valor de crescimento em vendas  no per�odo:");
				System.out.println("---------------------------------------------------------------------------------------\n");
		 		System.out.println(MapUtils.mapToString(MapUtils.sortHashMapByDoubleValue(listOfGrowthMap, ORDENATION_ASC), columns, PERCENTUAL_FORMAT));
		 		
				System.out.println("---------------------------------------------------------------------------------------");
				System.out.println("Filial que teve menor valor de crescimento em vendas no per�odo:");
				System.out.println("---------------------------------------------------------------------------------------\n");
				System.out.println(movement.toStringPercentualOfGrowthRate());
			}
		
		} else {
			System.out.println("[Servi�o] N�o foi poss�vel calcular o per�odo informado.");
		}
		
		return movement;
	}
	
	
	/**
	 * M�todo que calcula qual m�s a empresa teve um maior valor em vendas.
	 * 
	 * @return Nome do m�s com maior valor de venda.
	 */
	public String monthWhenEntrepriseHaveMoreProfit(){
			
		HashMap<String, Double> listOfMonthsPeriod =  new HashMap<String, Double>();
		
		for (int i = 0; i < listOfMovements.size(); i++) {
			Movement movementSelected = listOfMovements.get(i);
			double totalMovementValueOnPeriod = movementSelected.getMovementValue();
			
			// Soma o valor das movimenta��es para cada m�s.
			for (int j = (i + 1); j < listOfMovements.size(); j++) {
				
				// Somente entra no if se a compara��o dos meses forem iguais e se o hashmap n�o conter o m�s selecionado.
				if (listOfMovements.get(j).getRefDate().equalsIgnoreCase(movementSelected.getRefDate()) 
						&& !listOfMonthsPeriod.containsKey(movementSelected.getRefDate())) {					
					totalMovementValueOnPeriod += listOfMovements.get(j).getMovementValue();
				} 				
			}
			
			// Verifica se ha repeti��o no map, se sim os valores necess�rios para o calculo j� foram percorridos.
			if (!listOfMonthsPeriod.containsKey(movementSelected.getRefDate())) {
				// Insere a filial com a soma dos valores de sua movimenta��o.
				listOfMonthsPeriod.put(movementSelected.getRefDate(), totalMovementValueOnPeriod);
			} else {				
				continue;
			}

			// Limpa o valor do acumulador.
			totalMovementValueOnPeriod = 0;
		}
		
		// Ordena de acordo de forma descrecente.
		Entry<?, Double> result;
		result = MapUtils.sortHashMapByDoubleValue(listOfMonthsPeriod, ORDENATION_DESC).get(0);
		
		// Imprime um relat�rio do processamento em linha de comando.
		String[] columns = {"M�s de refer�ncia", "Valor total de vendas"};
		System.out.println("---------------------------------------------------------------------------------------");
		System.out.println("Relat�rio de todas os meses do per�odo com seu valor de vendas:");
		System.out.println("---------------------------------------------------------------------------------------\n");
 		System.out.println(MapUtils.mapToString(MapUtils.sortHashMapByDoubleValue(listOfMonthsPeriod, ORDENATION_DESC), columns, CASH_FORMAT));			
		
		System.out.println("---------------------------------------------------------------------------------------");
		System.out.println("M�s em que a empresa mais vendeu:");
		System.out.println("---------------------------------------------------------------------------------------\n");
		System.out.println(result.getKey().toString());
		
		return result.getKey().toString();		
	}
	
	/**
	 * M�todo get para recuperar o caminho do arquivo de carga inicial de dados.
	 * 
	 * @return o filePath.
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * M�todo set para difinir o caminho do arquivo de carga inicial de dados.
	 * 
	 * @param filePath para sertar o filePath.
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

}
