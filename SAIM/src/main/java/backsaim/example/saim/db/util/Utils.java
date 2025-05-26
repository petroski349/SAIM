package backsaim.example.saim.db.util;

public class Utils {

    public static String aplicarMascaraCpf(String cpf) {
        if (cpf == null) return null;

        cpf = cpf.replaceAll("[^\\d]", "");

        if (cpf.length() == 11) {
            return cpf.substring(0, 3) + "." +
                    cpf.substring(3, 6) + "." +
                    cpf.substring(6, 9) + "-" +
                    cpf.substring(9, 11);
        }

        return cpf;
    }

    public static String aplicarMascaraRg(String rg) {
        if (rg == null) return null;

        rg = rg.replaceAll("[^\\d]", "");

        if (rg.length() == 9) {
            return rg.substring(0, 2) + "." +
                    rg.substring(2, 5) + "." +
                    rg.substring(5, 8) + "-" +
                    rg.substring(8, 9);
        }

        return rg;
    }

    public static String aplicarMascaraCep(String cep) {
        if (cep == null) return null;

        cep = cep.replaceAll("[^\\d]", "");

        if (cep.length() == 8) {
            return cep.substring(0, 5) + "-" + cep.substring(5, 8);
        }

        return cep;
    }
}
