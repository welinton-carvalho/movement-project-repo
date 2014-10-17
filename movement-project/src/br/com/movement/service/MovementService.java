package br.com.movement.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import br.com.movement.eao.MovementEAO;
import br.com.movement.entity.Movement;
import br.com.movement.util.MapUtils;

/**
 * Classe de serviço responsável por controlar as regras de negócio da entidade Movimento.
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
	 * Lista de Movimentações recuperadas pela camada de EAO.
	 * */
	List<Movement> listOfMovements;

	/**
	 * Objeto de EAO utilizado pela entidade Movimento.
	 * */
	private MovementEAO movementEAO;
	
	/**
	 * Constante para definir a ordenação do haspmap com ASC.
	 * */
	private static char ORDENATION_ASC = 'a';
	
	/**
	 * Constante para definir a ordenação do haspmap como DESC.
	 * */
	private static char ORDENATION_DESC = 'd';
	
	/**
	 * Constante para definir a formatação do campo decimal do haspmap como dinheiro.
	 * */
	private static final char CASH_FORMAT = 'c';
	
	/**
	 * Constante para definir a formatação do campo decimal do haspmap como percentual.
	 * */
	private static final char PERCENTUAL_FORMAT = 'p';


	/**
	 * Construtor padrão.
	 * */
	public MovementService() {
		this.initializeData();
	}

	/**
	 * Método para acessar a camada de EAO e recuperar a lista de movimentações proveniente de um arquivo txt.
	 * */
	private void initializeData() {
		movementEAO = new MovementEAO();		
		try{
			listOfMovements = movementEAO.loadDataFromFile(filePath);
		}catch (Exception e){
			System.out.println("[Serviço] - Erro ao iniciar lista de movimentações: " + e.getMessage());
			e.printStackTrace();			
		}
	}

	/**
	 * Método que calcula qual filial teve o maior valor de vendas no período, esse método permite um período flexivél 
	 * que depende dos registros retornados pelo EAO. Ex. 1 ocorrência de São Paulo (Mensal), 2 ocorrêcias de São Paulo (Bimestral),
	 * 3 ocorrêcias de São Paulo (trimestral) etc. É necessário que haja em quantidade igualitária ocorrências entre todas 
	 * as filiais informadas, ou seja se houver 2 períodos de São Paulo deve-se ter 2 períodos informados do Rio de Janeiro e assim
	 * para as demais filiais.
	 * 
	 * @return Movimentação constando o nome da filial e a somatória do valor de suas movimentações.
	 * */
	public Movement subsidiaryWhoSoldMoreOnPeriod() {
		
		HashMap<String, Double> listOfAmountMap = this.checkAmountPeriod();
		
		if (!listOfAmountMap.isEmpty()) {
			//Pega a primeira ocorrência da coleção ja em ordem decrescente.
			Entry<?, Double> result = MapUtils.sortHashMapByDoubleValue(listOfAmountMap, ORDENATION_DESC).get(0);
			Movement movement = new Movement();
			movement.setSubsidiaryName(result.getKey().toString());
			movement.setMovementValue(result.getValue());

			// Imprime um relatório do processamento em linha de comando.
			String[] columns = {"Filial", "Mês de referência", "Total"};
			System.out.println("---------------------------------------------------------------------------------------");
			System.out.println("Relatório de todas as filiais com ordenação de maior valor de vendas no período:");
			System.out.println("---------------------------------------------------------------------------------------\n");
	 		System.out.println(MapUtils.mapToString(MapUtils.sortHashMapByDoubleValue(listOfAmountMap, ORDENATION_DESC), columns, CASH_FORMAT));			
			
			System.out.println("---------------------------------------------------------------------------------------");
			System.out.println("Filial que teve o maior valor de vendas no período:");
			System.out.println("---------------------------------------------------------------------------------------\n");
			System.out.println(movement.toString());
			
			return movement;
		} else {
			System.out.println("[Serviço] Não foi possível calcular o período informado.");
		}
		
		return null;
	}

	
	/**
	 * Método que faz a soma dos valore de movimentação para cada filial informada.
	 *
	 * @return Map com a soma de todos os meses para cada filial.
	 */
	private HashMap<String, Double> checkAmountPeriod(){		
		
		HashMap<String, Double> listOfAmountMap =  new HashMap<String, Double>();
		
		for (int i = 0; i < (listOfMovements.size()); i++) {
			Movement movementSelected = listOfMovements.get(i);
			double totalMovementValue = movementSelected.getMovementValue();
			
			// Seleciona uma movimentação de uma filial e procura nas demais posições movimentações para essa mesma filial.
			for (int j = (i + 1); j < listOfMovements.size(); j++) {
				if (listOfMovements.get(j).getSubsidiaryName().equalsIgnoreCase(movementSelected.getSubsidiaryName())) {
					totalMovementValue += listOfMovements.get(j).getMovementValue();
				}
			}

			// Verifica se ha repetição no map, se sim os valores necessários para o calculo já foram percorridos.
			if (!listOfAmountMap.containsKey(movementSelected.getSubsidiaryName())) {
				// Insere a filial com a soma dos valores de sua movimentação.
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
	 * Método que calcula qual filial teve o maior ou menor valor percentual de aumento ou decremento em vendas em um período de mês
	 * informado.
	 * 
	 * @param higerLowerGrowth Quando informado 'h' define a processamento para retornar a filial com maior aumento em vendas 
	 * e quando 'l' define menor aumento/decremento em vendas.
	 * @param initialMonth Mês inicial do período.
	 * @param endMonth Mês final do Período.
	 * @return Retorna a filial com maior ou menor valor de aumento em vendas.
	 */
	public Movement subsidiaryWithHigherLowerGrowth(String initialMonth, String endMonth, char higerLowerGrowth){

		Movement movement = new Movement();
		HashMap<String, Double> listOfGrowthMap =  new HashMap<String, Double>();
		
		// Percorre  a lista de movimentações para calcular de acordo com o período informado a taxa de crescimento.
		for (int i = 0; i < (listOfMovements.size()); i++) {
			Movement movementSelected = listOfMovements.get(i);
			// Seleciona uma movimentação de uma filial e procura nas demais posições movimentações para essa mesma filial.
			for (int j = (i + 1); j < listOfMovements.size(); j++) {
				if (listOfMovements.get(j).getSubsidiaryName().equalsIgnoreCase(movementSelected.getSubsidiaryName()) 
						&& movementSelected.getRefDate().equals(initialMonth) 
						&& listOfMovements.get(j).getRefDate().equals(endMonth)) {
					
					// Fórmula utilizada: (Valor_Presente / Valor_Passado) / Valor_Passado.
					double growthRate = (listOfMovements.get(j).getMovementValue() - movementSelected.getMovementValue()) / movementSelected.getMovementValue();
					double percentGrowthRate = growthRate * 100;
						
					listOfGrowthMap.put(movementSelected.getSubsidiaryName(), percentGrowthRate);							
				}
			}
		}
		
		// Ordena de acordo com o parâmetro passado higerLowerGrowth, 'h' para maior lucro, e 'l' para menor lucro.
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
					
			// Imprime um relatório do processamento em linha de comando.
			String[] columns = {"Filial", "Total"};
			if (higerLowerGrowth == 'h') {
				System.out.println("---------------------------------------------------------------------------------------");
				System.out.println("Relatório de todas as filiais com ordenação de maior valor de crescimento em vendas no período:");
				System.out.println("---------------------------------------------------------------------------------------\n");
		 		System.out.println(MapUtils.mapToString(MapUtils.sortHashMapByDoubleValue(listOfGrowthMap, ORDENATION_DESC), columns, PERCENTUAL_FORMAT));
		 		
				System.out.println("---------------------------------------------------------------------------------------");
				System.out.println("Filial que teve maior valor de crescimento em vendas no período:");
				System.out.println("---------------------------------------------------------------------------------------\n");
		 		System.out.println(movement.toStringPercentualOfGrowthRate());
			} else if (higerLowerGrowth == 'l') {
				System.out.println("---------------------------------------------------------------------------------------");
				System.out.println("Relatório de todas as filiais com ordenação de menor valor de crescimento em vendas  no período:");
				System.out.println("---------------------------------------------------------------------------------------\n");
		 		System.out.println(MapUtils.mapToString(MapUtils.sortHashMapByDoubleValue(listOfGrowthMap, ORDENATION_ASC), columns, PERCENTUAL_FORMAT));
		 		
				System.out.println("---------------------------------------------------------------------------------------");
				System.out.println("Filial que teve menor valor de crescimento em vendas no período:");
				System.out.println("---------------------------------------------------------------------------------------\n");
				System.out.println(movement.toStringPercentualOfGrowthRate());
			}
		
		} else {
			System.out.println("[Serviço] Não foi possível calcular o período informado.");
		}
		
		return movement;
	}
	
	
	/**
	 * Método que calcula qual mês a empresa teve um maior valor em vendas.
	 * 
	 * @return Nome do mês com maior valor de venda.
	 */
	public String monthWhenEntrepriseHaveMoreProfit(){
			
		HashMap<String, Double> listOfMonthsPeriod =  new HashMap<String, Double>();
		
		for (int i = 0; i < listOfMovements.size(); i++) {
			Movement movementSelected = listOfMovements.get(i);
			double totalMovementValueOnPeriod = movementSelected.getMovementValue();
			
			// Soma o valor das movimentações para cada mês.
			for (int j = (i + 1); j < listOfMovements.size(); j++) {
				
				// Somente entra no if se a comparação dos meses forem iguais e se o hashmap não conter o mês selecionado.
				if (listOfMovements.get(j).getRefDate().equalsIgnoreCase(movementSelected.getRefDate()) 
						&& !listOfMonthsPeriod.containsKey(movementSelected.getRefDate())) {					
					totalMovementValueOnPeriod += listOfMovements.get(j).getMovementValue();
				} 				
			}
			
			// Verifica se ha repetição no map, se sim os valores necessários para o calculo já foram percorridos.
			if (!listOfMonthsPeriod.containsKey(movementSelected.getRefDate())) {
				// Insere a filial com a soma dos valores de sua movimentação.
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
		
		// Imprime um relatório do processamento em linha de comando.
		String[] columns = {"Mês de referência", "Valor total de vendas"};
		System.out.println("---------------------------------------------------------------------------------------");
		System.out.println("Relatório de todas os meses do período com seu valor de vendas:");
		System.out.println("---------------------------------------------------------------------------------------\n");
 		System.out.println(MapUtils.mapToString(MapUtils.sortHashMapByDoubleValue(listOfMonthsPeriod, ORDENATION_DESC), columns, CASH_FORMAT));			
		
		System.out.println("---------------------------------------------------------------------------------------");
		System.out.println("Mês em que a empresa mais vendeu:");
		System.out.println("---------------------------------------------------------------------------------------\n");
		System.out.println(result.getKey().toString());
		
		return result.getKey().toString();		
	}
	
	/**
	 * Método get para recuperar o caminho do arquivo de carga inicial de dados.
	 * 
	 * @return o filePath.
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * Método set para difinir o caminho do arquivo de carga inicial de dados.
	 * 
	 * @param filePath para sertar o filePath.
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

}
