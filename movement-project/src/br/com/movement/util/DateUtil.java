package br.com.movement.util;

import java.util.Calendar;

/**
 * Utilitário genérico para para trabalhar com datas.
 * 
 * @author Welinton Carvalho.
 * @since 15/10/2014.
 * @version 1.0.
 * */
public class DateUtil {

	/**
	 * Método que verifica a quantidade de mêses entre um mês e outro.
	 */
	public static int verifyDifQuantityBeteweenTwoMonths(String initialMonth, String endMonth) {
		int quantity = (intValueOfMonth(endMonth) + 1) - (intValueOfMonth(initialMonth) + 1);
		return quantity;
	}

	/**
	 * Método para pegar o valor inteiro correspondente a cada mês.
	 * 
	 * @param monthName Nome do mês.
	 * @return Valor inteiro reference ao mês.
	 */
	private static int intValueOfMonth(String monthName) {

		switch (monthName) {

		case "Janeiro":
			return Calendar.JANUARY;

		case "Fevereiro":
			return Calendar.FEBRUARY;

		case "Março":
			return Calendar.MARCH;

		case "Abril":
			return Calendar.APRIL;

		case "Maio":
			return Calendar.MAY;

		case "Junho":
			return Calendar.JUNE;

		case "Julho":
			return Calendar.JULY;

		case "Agosto":
			return Calendar.AUGUST;

		case "Setembro":
			return Calendar.SEPTEMBER;

		case "Outubro":
			return Calendar.OCTOBER;

		case "Novembro":
			return Calendar.NOVEMBER;

		case "Dezembro":
			return Calendar.DECEMBER;

		default:
			break;
		}

		return -1;
	}
}
