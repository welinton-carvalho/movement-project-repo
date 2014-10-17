package br.com.movement.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Utilit�rio gen�rico para para trabalhar com objetos Map.
 * 
 * @author Welinton Carvalho.
 * @since 15/10/2014.
 * @version 1.0.
 * */
public class MapUtils {
	
	/**
	 * Objeto comparator para ordenar o hashmap pelo value, ordena��o ascendente ASC.
	 */
	private static final Comparator<Entry<?, Double>> DOUBLE_VALUE_COMPARATOR_ASC = new Comparator<Entry<?, Double>>() {
		@Override
		public int compare(Entry<?, Double> o1, Entry<?, Double> o2) {
			return o1.getValue().compareTo(o2.getValue());
		}
	};
	
	/**
	 * Objeto comparator para ordenar o hashmap pelo value, ordena��o decrescente DESC.
	 */
	private static final Comparator<Entry<?, Double>> DOUBLE_VALUE_COMPARATOR_DESC = new Comparator<Entry<?, Double>>() {
		@Override
		public int compare(Entry<?, Double> o1, Entry<?, Double> o2) {
			return o2.getValue().compareTo(o1.getValue());
		}
	};

	/**
	 * M�todo customizado para reordenar um hashmap pelo value.
	 */
	public static final List<Entry<?, Double>> sortHashMapByDoubleValue(@SuppressWarnings("rawtypes") HashMap temp, char ordenation) {
		@SuppressWarnings("unchecked")
		Set<Entry<?, Double>> entryOfMap = temp.entrySet();
		List<Entry<?, Double>> entries = new ArrayList<Entry<?, Double>>(entryOfMap);

		// Define o tipo de ordena��o do hasmap.
		if (ordenation == 'a') {
			Collections.sort(entries, DOUBLE_VALUE_COMPARATOR_ASC);
		} else if (ordenation == 'd') {
			Collections.sort(entries, DOUBLE_VALUE_COMPARATOR_DESC);

		} else {
			Collections.sort(entries, DOUBLE_VALUE_COMPARATOR_ASC);
		}
		return entries;
	}
	
	/**
	 * M�todo que formata uma lista de entradas para exibir como resultado ap�s o processamento de um hashmap.
	 * 
	 * @param listOfEntries Lista de valores de um Map.
	 * @param columns Colunas para cabe�alho do resultado.
	 * @param percentOrCash Define se a formata��o do campo decimal deve ser percentual ou dinheiro.
	 * @return Texto tabulado em duas colunas.
	 */
	public static final String  mapToString(List<Entry<?, Double>> listOfEntries, String[] columns, char percentOrCash){
		
		StringBuffer stringBuffer = new StringBuffer();
		DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
		decimalFormatSymbols.setDecimalSeparator(',');
		decimalFormatSymbols.setGroupingSeparator('.');
		DecimalFormat decimalFormat = new DecimalFormat("#,##0.00", decimalFormatSymbols);
		
		// Insere o cabe�alho do resultado.
		if (columns.length > 0) {
			for (String column : columns) {
				stringBuffer.append(column);
				stringBuffer.append("\t");				
			}
			stringBuffer.append("\n\n");
		}
				
		// Insere o resultado tabulado.
		for (Entry<?, ?> map : listOfEntries) {
			
			// Valida��o para caso n�o houver um nome informado.
			if (map.getKey() != null) {
				stringBuffer.append(map.getKey());
			} else {
				stringBuffer.append("Filial sem nome");
			}
			stringBuffer.append("\t");
			
			// Verifica se a formata��o do campo decimal deve ser percentual o dinheiro.
			if (percentOrCash == 'p') {
				// Valida��o para caso n�o houver um valor informado.
				if (map.getValue() != null) {
					stringBuffer.append(decimalFormat.format(map.getValue()).concat(" %"));
				} else {
					stringBuffer.append(decimalFormat.format(0).concat(" %"));
				}					
			} else if (percentOrCash == 'c') {				
				// Valida��o para caso n�o houver um valor informado.
				if (map.getValue() != null) {
					stringBuffer.append( NumberFormat.getCurrencyInstance().format(map.getValue()));
				} else {
					stringBuffer.append( NumberFormat.getCurrencyInstance().format(0));
				}
			}
			stringBuffer.append("\n");

		}
		
		return stringBuffer.toString();	
	}

}
