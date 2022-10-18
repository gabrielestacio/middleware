package imd.ufrn.br.Agenda;

public class Agenda {
	
	public Agenda() {
	}
	public String Criar(String r) {
		if(Integer.parseInt(r) == 1) {
			return "Contato criado com sucesso";
		}else {
			System.out.println(r);
			return "Agenda Lotada!, contato nao pode ser criado";
		}
	}
	public String Buscar(String r) {
		if(String.valueOf(r.trim()).equals("-2")) {
			String m = "Agenda vazia!, Contato nao encontrado";
			return m;
		}if(String.valueOf(r.trim()).equals("-1")) {
			String m = "Contato nao encontrado"; 
			return m;
		}
		return "Contato Encontrado: " + r;
	}
	public String Deletar(String r) {
		if(String.valueOf(r.trim()).equals("-1")) {
			String m = "Contato nao encontrado";
			return m;
		}else {
			String m = "Contato deletado: "+r;
			return m;
		}
	}
}
