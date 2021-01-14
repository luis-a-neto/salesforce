@isTest public class ValidacaoDocsTest{
	/**
	* Testa a classe de validação de documentos. Fornece 100% de cobertura.
	* @see		ValidacaoDocs
	* 
	* @author	Luis Andrade <luis.a.neto@outlook.com>
	* @version	1.0
	* @date		Maio/2018
	*/
	
    @isTest static void testarCPFIncompleto(){
        System.assert(ValidacaoDocs.isValidDoc('360344879'),
		'CPF incompleto invalidado');
    }
    
    @isTest static void testarCPFIgual(){
        System.assert(!ValidacaoDocs.isValidDoc('88888888888'),
		'CPF fake validado');
    }
    
    @isTest static void testarCPFDigitos(){
        System.assert(!ValidacaoDocs.isValidDoc('CPF', '888888888889'),
		'CPF com dígitos a mais validado');
    }

    @isTest static void testarCNPJIncompleto(){
        System.assert(ValidacaoDocs.isValidDoc('1137324000139'),
		'CNPJ incompleto invalidado');
    }
    
    @isTest static void testarCNPJIgual(){
        System.assert(!ValidacaoDocs.isValidDoc('88888888888888'),
		'CNPJ fake validado');
    }
    
    @isTest static void testarCNPJDigitos(){
        System.assert(!ValidacaoDocs.isValidDoc('8888888888998888'),
		'CNPJ com dígitos a mais validado');
    }

	@isTest static void testarDocsValidos(){
		List<String> correta = new List<String>();
		correta.add('CPF,65343116663');
		correta.add('CNPJ,75034605000100');
		correta.add('AC,100482300112');
		correta.add('AL,248263137');
		correta.add('AM,453411550');
		correta.add('AP,033452172');
		correta.add('AP,030180301');
		correta.add('BA,46414678');
		correta.add('BA,61234557');
		correta.add('BA,100000306');
		correta.add('BA,296730584');
		correta.add('CE,874779952');
		correta.add('DF,710777300100');
		correta.add('ES,805597417');
		correta.add('GO,156440890');
		correta.add('GO,110944020');
		correta.add('MA,122974980');
		correta.add('MG,6884140839146');
		correta.add('MS,282411755');
		correta.add('MT,51633876');
		correta.add('PA,150670656');
		correta.add('PB,292371373');
		correta.add('PE,915224984');
		correta.add('PI,533940133');
		correta.add('PR,3065948850');
		correta.add('RJ,55842582');
		correta.add('RN,205556523');
		correta.add('RO,101625213');
		correta.add('RR,244034827');
		correta.add('RS,9690919330');
		correta.add('SC,712039384');
		correta.add('SE,558036783');
		correta.add('SP,110042490114');
		correta.add('TO,49036906534');
		
		for(String atual : correta){
			String[] prm = atual.split(',');
			System.assert(ValidacaoDocs.isValidDoc(prm[0], prm[1]),
				'Doc correto invalidado - Tipo: ' + prm[0] + '/Doc: ' + prm[1]);
		}
	}

	@isTest static void testarDocsInvalidos(){
		List<String> incorreta = new List<String>();
		incorreta.add('CPF,22758261660');
		incorreta.add('CNPJ,01137324000240');
		incorreta.add('AC,100482300113');
		incorreta.add('AL,248263138');
		incorreta.add('AM,453411551');
		incorreta.add('AP,23452178');
		incorreta.add('AP,33452174');
		incorreta.add('AP,30180232');
		incorreta.add('BA,46414679');
		incorreta.add('BA,61234558');
		incorreta.add('BA,100000307');
		incorreta.add('BA,296730585');
		incorreta.add('CE,874779953');
		incorreta.add('DF,710777300101');
		incorreta.add('ES,805597418');
		incorreta.add('GO,156440891');
		incorreta.add('MA,122974982');
		incorreta.add('MG,6884142839146');
		incorreta.add('MS,282411752');
		incorreta.add('MT,51633873');
		incorreta.add('PA,157670656');
		incorreta.add('PB,212371373');
		incorreta.add('PE,915274984');
		incorreta.add('PI,533947133');
		incorreta.add('PR,3065948250');
		incorreta.add('RJ,75842582');
		incorreta.add('RN,205456523');
		incorreta.add('RO,101625216');
		incorreta.add('RR,244534827');
		incorreta.add('RS,9690919332');
		incorreta.add('SC,512039384');
		incorreta.add('SE,558936783');
		incorreta.add('SP,110042490115');
		
		for(String atual : incorreta){
			String[] prm = atual.split(',');
			System.assert(!ValidacaoDocs.isValidDoc(prm[0], prm[1]),
				'Doc incorreto validado - Tipo: ' + prm[0] + '/Doc: ' + prm[1]);
		}
	}	
}
