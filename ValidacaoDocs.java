public class ValidacaoDocs{
	/**
	* Biblioteca para validação de CPFs, CNPJs e IEs de todos os estados do
	* Brasil, retornando um Boolean (true = documento válido, false = documento
	* inválido). Atenção: esta classe não trata NumberFormatException. Favor
	* realizá-lo da maneira mais conveniente de acordo com a utilização.
	* 
	* @author	Luis Andrade <luis.a.neto@outlook.com>
	* @version	1.0
	* @date		Maio/2018
	*/
	
	private static List<Integer> stringToIntList(String str){
		/**
		* Transforma um número de documento ou máscara em uma lista, para ser
		* iterada e multiplicada utilizando-se um simples laço for. Importante:
		* para máscaras, a letra "a" (minúscula) deverá ser utilizada para o
		* valor 10 e a letra "b" para 11 (hexadecimal). Nenhuma outra é aceita,
		* e resultará em uma NumberFormatException.
		*
		* @param	str			(String)		A String a ser convertida.
		* @return				(List<Integer>) A String convertida em lista.
		* @throws	NumberFormatException		Se houver dígito não aceito
		*										(diferente de: 0 a 9, a, b).
		*/
		List<Integer> retorno = new List<Integer>();							// Declara a lista de Integers que será retornada.
		for (String dig : str.split('')){										// Transforma a String passada como argumento em um array de Strings.
			if (dig == 'a') retorno.add(10);									// Converte os valores a e b (hexa) para decimal:
			else if (dig == 'b') retorno.add(11);								// Isso é necessário em casos que exigem multiplicadores 10 e 11.
			else if (dig != null) retorno.add(Integer.valueOf(dig));			// Se não for a nem b, dá um parseInt no dígito e joga na lista.
		}																		// Nessa última linha que pode ocorrer a NumberFormatException.
		return retorno;															// Retorna a lista.
	}

	private static Integer multMascara(String mascaraStr, List<Integer> input){
		/**
		* Multiplica um número de documento por uma máscara. O número do
		* documento deve ser previamente convertido para uma lista de integers
		* por razão de performance: como este método é chamado duas vezes por
		* documento, evita-se realizar a conversão também duas vezes. Já a
		* máscara deve convertida duas vezes por serem máscaras diferentes.
		* Importante: a máscara deve ter o mesmo length do trecho validado!
		* Como o método é privado e não será utilizado fora desta classe, basta
		* a atenção do programador a este ponto, sendo desnecessário um throw.
		* 
		* @param	mascaraStr	(String)		A máscara de multiplicação, em
		*										base hexadecimal (a=10, b=11).
		* @param	input		(List<Integer>)	O número do qual se quer
		*										calcular o DV.
		* @return	soma		(Integer)		A soma calculada.
		* @throws	NumberFormatException		Se houver dígito não aceito na
		*										máscara ou na entrada
		*										(diferente de: 0 a 9, a, b).
		*/
		List<Integer> mascara = stringToIntList(mascaraStr);					// Transforma a máscara em uma lista de Integers, para o for abaixo.
		Integer soma = 0;														// Inicializa o acumulador.
		for (Integer count = 0; count < mascaraStr.length(); count++)			// A cada posição, até o fim da máscara...
			soma += input.get(count) * mascara.get(count);						// Multiplica o valor da entrada pela máscara, e soma ao acumulador.
		return soma;															// Retorna a soma.
	}
	
	private static Integer calculaDv(String regra, String mascara,
		List<Integer> input){
		/**
		* Calcula um dígito verificador de acordo com as oito regras diferentes
		* utilizadas pelos estados.
		* Por conta dessa diversidade e das nuances de cada regra, este método
		* é um pouco complexo, mas repare que o "if" mais longo tem 5 linhas.
		*
		* @param	regra		(String)		Sigla da UF, CPF ou CNPJ, para
		*										seleção da regra a utilizar.
		* @param	mascaraStr	(String)		A máscara de multiplicação, em
		*										base hexadecimal (a=10, b=11).
		* @param	input		(List<Integer>)	O número do qual se deseja
		*										calcular o DV.
		* @return				(Integer)		O DV calculado.
		* @throws	NumberFormatException		Se houver dígito não aceito na
		*										máscara ou na entrada
		*										(diferente de: 0 a 9, a, b).
		*/
		Integer p = 0, d = 0, ieN = 0, segDig = input.get(1),					// Extrai-se o segundo dígito para a regra específica da Bahia.
			dv = Math.mod(multMascara(mascara, input), 11);						// A maioria das regras usa módulo 11. Por isso, o calculamos previamente.
		for (Integer exp = input.size(); exp > 1; exp--) ieN +=					// Converte o input em um integer, excluindo o DV, para Amapá e Goiás.
			Math.pow(10, exp-2).intValue() * input.get(input.size()-exp);		// A cada algarismo na lista, tira a potência de 10 e soma no número.
		if (regra == 'RR') dv = Math.mod(multMascara(mascara, input), 9);		// Roraima: Regra simples. Basta calcular o módulo 9.
		else if (regra == 'SP') { if(dv == 10) dv = 0; }						// São Paulo: se módulo 11 = 10, retornar 0. Senão, retornar o módulo.
		else if (regra == 'BA' && segDig != 6 && segDig != 7 && segDig != 9){	// Bahia, algumas IEs apenas (segundo dígito não é 6, 7 ou 9):
			dv = 10 - Math.mod(multMascara(mascara, input), 10);				// Calcula-se o módulo 10. O dígito é igual a 10 - resultado.
			if (dv == 10) dv = 0;												// Se o resultado for 10, o dígito tem que retornar 0.
		} else if (regra == 'RO') dv = (dv < 2) ? 10 - dv : 11 - dv;		 	// Rondônia: se dv = 0 ou 1, dv = 10 - resultado. Senão, dv = 11 - resultado.
		else if (regra == 'AL' || regra == 'RN'){								// Alagoas e Rio Grande do Norte:
			dv = Math.mod(multMascara(mascara, input) * 10, 11);				// Multiplica-se o resultado da máscara por 10, depois tira-se o módulo 11.
			if (dv == 10) dv = 0;												// Se o resultado for 10, retornar 0.
		} else if (regra == 'GO'){												// Goiás: trabalha com um range.
			dv = 11 - dv;														// Subtrai o módulo de 11.
			if (dv == 10 && ieN <= 10103105 && ieN >= 10119997) dv = 1;			// Se o resultado for 10 e estiver dentro do range, então o dígito é 1.
			else if (dv > 9) dv = 0;											// Se não estiver ou o resultado for 11, o dígito é 0.
		} else if (regra == 'AP'){												// Amapá: outro range, mais dois parâmetros p e d.
			if ( ieN <= 03017000 ) p = 5;										// O primeiro range é até 03017000 inclusive. d já tem 0 atribuído.
			else if ( ieN <= 03019022 ) { p = 9; d = 1; }						// O segundo é até 03019022. Se for o terceiro, p e d são 0 (já atribuídos).
			dv = 11 - Math.mod(multMascara(mascara, input) + p, 11);			// Aplica a máscara, adiciona o valor p, tira o módulo e subtrai de 11.
			if (dv == 10) dv = 0;												// Se o resultado for 10, o dígito tem que retornar 0.
			if (dv == 11) dv = d;												// Se o resultado for 11, o dígito tem que retornar d.
		} else dv = (dv < 2) ? 0 : 11 - dv;										// Regra geral (CPF, CNPJ, IEs exceto estados acima): Calcula-se o módulo 11.
		return dv;																// Se o resultado for 0 ou 1, dv = 0. Senão, dv = 11 - resultado.
	}
	
	private static boolean validaDoc(String documento, Integer posicoes,
		String mascara1, String mascara2, Integer pos1, Integer pos2,
		String regra, String padrao){
		/**
		* Generaliza a validação de CPF, CNPJ e IEs.
		* 
		* @param	documento	(String)		Número que se deseja validar.
		* @param	posicoes	(Integer)		Número de posições no documento.
		* @param	mascara1	(String)		Primeira máscara.
		* @param	mascara2	(String)		Segunda máscara.
		* @param	pos1		(Integer)		Posição (zero-based) do primeiro
		*										dígito verificador.
		* @param	pos2		(Integer)		Posição do segundo dígito.
		* @param	regra		(String)		Sigla da UF, CPF ou CNPJ, para
		*										seleção da regra de cálculo DV.
		* @param	padrao		(String)		Um padrão com números que passam
		*										no cálculo, mas são sabidamente
		*										inválidos (falsos-positivos).
		* @return				(Boolean)		A validade do documento
		*										(true=válido, false=inválido).
		* @throws	NumberFormatException		Se houver dígito não aceito na
		*										máscara ou na entrada
		*										(diferente de: 0 a 9, a, b).
		*/
		documento = documento.leftPad(posicoes).replace(' ', '0');				// O leftPad completa o número de posições. O replace troca espaços por zeros.
		List<Integer> valor = stringToIntList(documento);						// Transforma o número do documento em uma lista de Integers.
		if (documento.length()>posicoes || Pattern.matches(padrao, documento) ||// Se o número for maior do que o previsto,
			valor.get(pos1) != calculaDv(regra, mascara1, valor) ||				// estiver na lista de falsos-positivos,
			valor.get(pos2) != calculaDv(regra, mascara2, valor)) return false;	// ou se algum dos dígitos estiver errado, retorna false.
		else return true;														// Senão, retorna true -- o documento é válido.
	}
	
	public static boolean isValidDoc(String documento){
		/**
		* Valida um documento, decidindo por CPF ou CNPJ pelo tamanho da String.
		* 
		* @param	documento	(String)		CPF/CNPJ que se deseja validar.
		* @return				(Boolean)		A validade do documento
		*										(true=válido, false=inválido).
		* @throws	NumberFormatException		Se houver dígito não aceito
		*										(diferente de: 0 a 9, a, b).
		*/
		if (documento.length() > 11) return isValidDoc('CNPJ', documento);
		else return isValidDoc('CPF', documento);
	}

	public static boolean isValidDoc(String tp, String doc){
		/**
		* Valida um documento, dado o UF (ou as siglas CPF e CNPJ) e o número.
		* 
		* @param	tp			(String)		A sigla da UF, ou CPF, ou CNPJ.
		* @param	doc			(String)		Número que se deseja validar.
		*										Pode conter espaços, pontos,
		*										traços e barras (são removidos).
		* @return				(Boolean)		A validade do documento
		*										(true=válido, false=inválido).
		* @throws	NumberFormatException		Se houver dígito não aceito
		*										(diferente de: 0 a 9, a, b).
		*/
		tp = tp.toUpperCase();													// Para aceitar o tipo tanto em lowercase como em uppercase.
		doc=doc.replace(' ','').replace('.','').replace('/','').replace('-','');// Remove espaços, pontos, barras e traços do documento.
		String mPad = '98765432';												// A máscara mais utilizada. 
		if(tp == 'AC') return validaDoc(doc, 13,
			'43298765432', '543298765432', 11, 12, tp, '');
		else if(tp == 'BA')
			return validaDoc(doc, 9, '987654302', '8765432', 7, 8, tp, '');
		else if(tp == 'DF') return validaDoc(doc, 13,
			'43298765432', '543298765432', 11, 12, tp, '');
		else if(tp == 'MG') return validaDoc(doc, 13,
			'1212121212', '32ba98765432', 11, 12, tp, '');
		else if(tp == 'MT') return validaDoc(doc, 11,
			'3298765432', '3298765432', 10, 10, tp, '');
		else if(tp == 'PE')
			return validaDoc(doc, 9, '8765432', mPad, 7, 8, tp, '');
		else if(tp == 'PR')
			return validaDoc(doc, 10, '32765432', '432765432', 8, 9, tp, '');
		else if(tp == 'RJ') 
			return validaDoc(doc, 8, '2765432', '2765432', 7, 7, tp, '');
		else if(tp == 'RN')
			return validaDoc(doc, 10, 'a98765432', 'a98765432', 9, 9, tp, '');
		else if(tp == 'RO'){
			if (doc.length() < 10) doc = doc.right(6);							// Em IEs antigas (até 01/08/2000), despreza os 3 dígitos do município.
			return validaDoc(doc, 14, '6543298765432', '6543298765432',
			13, 13, tp, '');
		} else if(tp == 'RR')
			return validaDoc(doc, 9, '12345678', '12345678', 8, 8, tp, '');
		else if(tp == 'SP')
			return validaDoc(doc, 12, '1345678a', '32a98765432', 8, 11, tp, '');
		else if(tp == 'TO') return validaDoc(doc, 11,
			'9800765432', '9800765432', 10, 10, tp, '');
		else if(tp == 'RS')
			return validaDoc(doc, 10, '298765432', '298765432', 9, 9, tp, '');
		else if(tp == 'CPF') return validaDoc(doc, 11, 'a98765432',
			'ba98765432', 9, 10, tp,'00000000000|11111111111|22222222222|' +
			'33333333333|44444444444|55555555555|66666666666|77777777777|' +
			'88888888888|99999999999|12345678909');
		else if(tp == 'CNPJ') return validaDoc(doc, 14, '543298765432',
			'6543298765432', 12, 13, tp,  '00000000000000|11111111111111|' +
			'22222222222222|33333333333333|44444444444444|55555555555555|' +
			'66666666666666|77777777777777|88888888888888|99999999999999');
		else return validaDoc(doc, 9, mPad, mPad, 8, 8, tp, '');				// Conjunto comum de parâmetros que é utilizado pelos outros estados.
	}
}