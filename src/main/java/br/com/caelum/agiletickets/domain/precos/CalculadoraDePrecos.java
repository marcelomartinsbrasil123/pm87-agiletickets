package br.com.caelum.agiletickets.domain.precos;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import br.com.caelum.agiletickets.models.Sessao;
import br.com.caelum.agiletickets.models.TipoDeEspetaculo;

public class CalculadoraDePrecos {

	public static BigDecimal calcula(Sessao sessao, Integer quantidade) {
		BigDecimal preco;

		Map<String, String> listaDeMetodos = carregaMapa();
//		String nomeDoMetodo = listaDeMetodos.get(sessao.getEspetaculo()
//				.getTipo().toString());
//		preco = sessao.getPreco();
//		if (!nomeDoMetodo.equals("")) {
//			Class classe;
//			try {
//				classe = Class
//						.forName("br.com.caelum.agiletickets.domain.precos.CalculadoraDePrecos");
//				Method method = classe.getMethod(nomeDoMetodo,
//						sessao.getClass());
//				preco = (BigDecimal) method.invoke(sessao);
//			} catch (ClassNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IllegalAccessException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (NoSuchMethodException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (SecurityException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IllegalArgumentException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (InvocationTargetException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}

		if (sessao.getEspetaculo().getTipo().equals(TipoDeEspetaculo.CINEMA)
				|| sessao.getEspetaculo().getTipo()
						.equals(TipoDeEspetaculo.SHOW)) {
			preco = calculaPrecoSemVerificarDuracao(sessao);
		} else if (sessao.getEspetaculo().getTipo()
				.equals(TipoDeEspetaculo.BALLET)) {
			preco = calculaPrecoComVerificacaoDeDuracao(sessao);
		} else if (sessao.getEspetaculo().getTipo()
				.equals(TipoDeEspetaculo.ORQUESTRA)) {
			preco = calculaPrecoComVerificacaoDeDuracao(sessao);
		} else {
			preco = sessao.getPreco();
		}

		return preco.multiply(BigDecimal.valueOf(quantidade));
	}

	private static Map<String, String> carregaMapa() {
		Map<String, String> listaDeMetodos = new HashMap();
		listaDeMetodos.put(TipoDeEspetaculo.CINEMA.toString(),
				"calculaPrecoSemVerificarDuracao");
		listaDeMetodos.put(TipoDeEspetaculo.SHOW.toString(),
				"calculaPrecoSemVerificarDuracao");
		listaDeMetodos.put(TipoDeEspetaculo.BALLET.toString(),
				"calculaPrecoComVerificacaoDeDuracao");
		listaDeMetodos.put(TipoDeEspetaculo.ORQUESTRA.toString(),
				"calculaPrecoComVerificacaoDeDuracao");
		listaDeMetodos.put(TipoDeEspetaculo.TEATRO.toString(), "");
		return listaDeMetodos;
	}

	private static BigDecimal calculaPrecoSemVerificarDuracao(Sessao sessao) {
		BigDecimal preco;
		preco = testaSeEstahAcabando(sessao, BigDecimal.valueOf(0.10));
		return preco;
	}

	private static BigDecimal calculaPrecoComVerificacaoDeDuracao(Sessao sessao) {
		BigDecimal preco;
		preco = testaSeEstahAcabando(sessao, BigDecimal.valueOf(0.20));
		preco = verificaDuracao(sessao, preco);
		return preco;
	}

	private static BigDecimal verificaDuracao(Sessao sessao, BigDecimal preco) {
		if (sessao.getDuracaoEmMinutos() > 60) {
			preco = preco.add(sessao.getPreco().multiply(
					BigDecimal.valueOf(0.10)));
		}
		return preco;
	}

	private static double percentualRestante(Sessao sessao) {
		return (sessao.getTotalIngressos() - sessao.getIngressosReservados())
				/ sessao.getTotalIngressos().doubleValue();
	}

	private static BigDecimal testaSeEstahAcabando(Sessao sessao,
			BigDecimal percentual) {
		BigDecimal preco;
		// quando estiver acabando os ingressos...
		if (percentualRestante(sessao) <= 0.050) {
			preco = sessao.getPreco().multiply(
					percentual.add(BigDecimal.valueOf(1.0)));
		} else {
			preco = sessao.getPreco();
		}
		return preco;
	}
}