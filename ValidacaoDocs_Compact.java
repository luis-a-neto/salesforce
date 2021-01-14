public class ValidacaoDocs{ // @author Luis Andrade <luis.a.neto@outlook.com>
	private static List<Integer> stringToIntList(String str){
		List<Integer> retorno = new List<Integer>();
		for (String dig : str.split('')){
			if (dig == 'a') retorno.add(10);
			else if (dig == 'b') retorno.add(11);
			else if (dig != null) retorno.add(Integer.valueOf(dig));
		}
		return retorno;
	}
	private static Integer multMascara(String mascaraStr, List<Integer> input){
		List<Integer> mascara = stringToIntList(mascaraStr);
		Integer soma = 0;
		for (Integer count = 0; count < mascaraStr.length(); count++) soma += input.get(count) * mascara.get(count);
		return soma;
	}
	private static Integer calculaDv(String regra, String mascara, List<Integer> input){
		Integer p = 0, d = 0, ieN = 0, segDig = input.get(1), dv = Math.mod(multMascara(mascara, input), 11);
		for (Integer exp = input.size(); exp > 1; exp--) ieN +=	Math.pow(10, exp-2).intValue() * input.get(input.size()-exp);
		if (regra == 'RR') dv = Math.mod(multMascara(mascara, input), 9);
		else if (regra == 'SP') { if(dv == 10) dv = 0; }
		else if (regra == 'BA' && segDig != 6 && segDig != 7 && segDig != 9){
			dv = 10 - Math.mod(multMascara(mascara, input), 10);
			if (dv == 10) dv = 0;
		} else if (regra == 'RO') dv = (dv < 2) ? 10 - dv : 11 - dv;
		else if (regra == 'AL' || regra == 'RN'){
			dv = Math.mod(multMascara(mascara, input) * 10, 11);
			if (dv == 10) dv = 0;
		} else if (regra == 'GO'){
			dv = 11 - dv;
			if (dv == 10 && ieN <= 10103105 && ieN >= 10119997) dv = 1;
			else if (dv > 9) dv = 0;
		} else if (regra == 'AP'){
			if ( ieN <= 03017000 ) p = 5;
			else if ( ieN <= 03019022 ) { p = 9; d = 1; }
			dv = 11 - Math.mod(multMascara(mascara, input) + p, 11);
			if (dv == 10) dv = 0;
			if (dv == 11) dv = d;
		} else dv = (dv < 2) ? 0 : 11 - dv;
		return dv;
	}
	private static boolean validaDoc(String documento, Integer posicoes, String mascara1, String mascara2, Integer pos1, Integer pos2, String regra, String padrao){
		documento = documento.leftPad(posicoes).replace(' ', '0');
		List<Integer> valor = stringToIntList(documento);
		return (documento.length() > posicoes || Pattern.matches(padrao, documento) || valor.get(pos1) != calculaDv(regra, mascara1, valor) || valor.get(pos2) != calculaDv(regra, mascara2, valor)) ? false : true;
	}
	public static boolean isValidDoc(String documento){ return (documento.length() > 11) ? isValidDoc('CNPJ', documento) : isValidDoc('CPF', documento); }
	public static boolean isValidDoc(String tp, String doc){
		tp = tp.toUpperCase();
		doc = doc.replace(' ','').replace('.','').replace('/','').replace('-','');
		String mPad = '98765432';
		if(tp == 'AC') return validaDoc(doc, 13, '432'+mPad, '5432'+mPad, 11, 12, tp, '');
		else if(tp == 'BA') return validaDoc(doc, 9, '987654302', '8765432', 7, 8, tp, '');
		else if(tp == 'DF') return validaDoc(doc, 13, '432'+mPad, '5432'+mPad, 11, 12, tp, '');
		else if(tp == 'MG') return validaDoc(doc, 13, '1212121212', '32ba'+mPad, 11, 12, tp, '');
		else if(tp == 'MT') return validaDoc(doc, 11, '32'+mPad, '32'+mPad, 10, 10, tp, '');
		else if(tp == 'PE') return validaDoc(doc, 9, '8765432', mPad, 7, 8, tp, '');
		else if(tp == 'PR') return validaDoc(doc, 10, '32765432', '432765432', 8, 9, tp, '');
		else if(tp == 'RJ') return validaDoc(doc, 8, '2765432', '2765432', 7, 7, tp, '');
		else if(tp == 'RN') return validaDoc(doc, 10, 'a'+mPad, 'a'+mPad, 9, 9, tp, '');
		else if(tp == 'RO'){
			if (doc.length() < 10) doc = doc.right(6);
			return validaDoc(doc, 14, '65432'+mPad, '65432'+mPad, 13, 13, tp, '');
		} else if(tp == 'RR') return validaDoc(doc, 9, '12345678', '12345678', 8, 8, tp, '');
		else if(tp == 'SP') return validaDoc(doc, 12, '1345678a', '32a'+mPad, 8, 11, tp, '');
		else if(tp == 'TO') return validaDoc(doc, 11, '9800765432', '9800765432', 10, 10, tp, '');
		else if(tp == 'RS') return validaDoc(doc, 10, '2'+mPad, '2'+mPad, 9, 9, tp, '');
		else if(tp == 'CPF') return validaDoc(doc, 11, 'a'+mPad, 'ba'+mPad, 9, 10, tp, '00000000000|11111111111|22222222222|33333333333|44444444444|55555555555|66666666666|77777777777|88888888888|99999999999|12345678909');
		else if(tp == 'CNPJ') return validaDoc(doc, 14, '5432'+mPad, '65432'+mPad, 12, 13, tp, '00000000000000|11111111111111|22222222222222|33333333333333|44444444444444|55555555555555|66666666666666|77777777777777|88888888888888|99999999999999');
		else return validaDoc(doc, 9, mPad, mPad, 8, 8, tp, '');
	}
}