package imd.ufrn.br.Banco;


public class Banco {
	int tamanho;
	int tamanho_max;
	String[][] banco;
	
	public Banco() {
		this.tamanho = 0;
		this.tamanho_max = 10;
		this.banco = new String[tamanho_max][3];
	}
	
	public String criar(String n, String d, String t) {
		if(this.tamanho != this.tamanho_max) {
			this.banco[this.tamanho][0] = d;
			this.banco[this.tamanho][1] = t;
			this.banco[this.tamanho][2] = n;
			this.tamanho = this.tamanho + 1;
			return "1";
		}
		return "0";
	}
	
	public int existe(String n) {
		int i = 0;
		if(tamanho == 0) {
			System.out.println("LISTA VAZIA");
			return -2;
		}
		System.out.println("PROCURANDO: "+n);
		while (i < this.tamanho) {
			System.out.println("VENDO CONTATOS: "+this.banco[i][2]);
            if (String.valueOf(this.banco[i][2].trim()).equals(String.valueOf(n.trim()))) {
            	System.out.println("ACHADO CONTATO, ID: "+i);
                return i;
            }
            else {
                i = i + 1;
            }
        }
		System.out.println("NÃO ACHADO CONTATO");
		return -1;
	}
	
	public String buscar(String n) {
		int e = this.existe(n);
		if(e < 0) {
			if(this.tamanho == 0) {
				return String.valueOf(e);
			}else {
				return String.valueOf(e);
			}
		}
		String r = "("+this.banco[e][0]+")"+  this.banco[e][1]+" - " + this.banco[e][2];
		return r;
	}

	public String deletar(String n) {
		int e = this.existe(n);
		if(e == -1 || e == -2) {
			return "-1";
		}
		String r = this.banco[e][2];
		if(e == 0 || e == this.tamanho) {
			this.tamanho = this.tamanho -1;
		}
		if(e > 0 && e < this.tamanho) {
			while (e < this.tamanho) {
				this.banco[e][0] = this.banco[e+1][0];
				this.banco[e][0] = this.banco[e+1][1];
				this.banco[e][0] = this.banco[e+1][2];
	            e = e + 1;    
	        }
		}
		return r;
	}



}