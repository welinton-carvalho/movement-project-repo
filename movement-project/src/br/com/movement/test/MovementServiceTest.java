package br.com.movement.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.movement.entity.Movement;
import br.com.movement.service.MovementService;

/**
 * Classe de Teste da Entidade Movimento e seu Servi�o.
 * Os m�todos foram baseados nos dados da carga inicial.
 *
 * */
public class MovementServiceTest {
	
	/**
	 * Servi�o da entidade.
	 */
	MovementService movementService;

	/**
	 * M�todo de teste inicial que faz o carregamento do arquivo Txt.
	 */
	@Before
	public void setUp() {
		
		// Ao instanciar o servi�o a carga de dados � iniciada.
		movementService = new MovementService();
	}

	@Test
	public void genericTestsCoverageTest() {
		
		// Cobertura da entidade.
		Movement movement = new Movement();
		movement.setSubsidiaryName(movement.getSubsidiaryName());
		movement.setRefDate(movement.getRefDate());
		movement.setMovementValue(movement.getMovementValue());
		
		// Teste dos m�todos personalisados.
		movement.compareTo(new Movement());
		movement.toString();
		movement.toStringPercentualOfGrowthRate();

	}

	/**
	 * M�todo para testar o processamento da regra de neg�cio (Filial que mais vendeu no per�odo).
	 */
	@Test
	public void subsidiaryWhoSoldMoreOnPeriodTest() {

		Movement movement = new Movement();
		movement = movementService.subsidiaryWhoSoldMoreOnPeriod();
		
		Assert.assertTrue(!movement.getSubsidiaryName().isEmpty());
		Assert.assertTrue(movement.getRefDate().isEmpty());
		Assert.assertTrue(movement.getMovementValue() != 0);		
	}
	
	/**
	 * M�todo para testar o processamento as regras de neg�cio (Filial que teve o maior crescimento) e (Filial que teve a maior queda nas vendas).
	 */
	@Test
	public void subsidiaryWithHigherLowerGrowthTest() {

		Movement movement = new Movement();
		movement = movementService.subsidiaryWithHigherLowerGrowth("Janeiro", "Fevereiro", 'h');
			
		Assert.assertEquals(movement.getSubsidiaryName(), "Brasilia");
		Assert.assertTrue(!movement.getRefDate().isEmpty());
		Assert.assertTrue(movement.getMovementValue() == 0.0);		
		
		movement = movementService.subsidiaryWithHigherLowerGrowth("Janeiro", "Mar�o", 'l');
		
		Assert.assertEquals(movement.getSubsidiaryName(), "Rio de Janeiro");
		Assert.assertTrue(!movement.getRefDate().isEmpty());
		Assert.assertTrue(movement.getMovementValue() == 0.0);	
	}
	
	/**
	 * M�todo para testar o processamento a regra de neg�cio (M�s em que a empresa mais vendeu).
	 */
	@Test
	public void monthWhenEntrepriseHaveMoreProfitTest() {

		String month;
		month = movementService.monthWhenEntrepriseHaveMoreProfit();
			
		Assert.assertTrue(month.equalsIgnoreCase("Mar�o"));
	
	}
}
