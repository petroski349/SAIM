function validarAparenciaSistema() {
    const nome = document.getElementById("nome").value.trim();
    const logo = document.getElementById("logo").value.trim();
    const rodape = document.getElementById("rodape").value.trim();

    if (nome === "" || logo === "" || rodape === "") {
        alert("Preencha todos os campos de aparência.");
        return false;
    }

    return true;
}

function validarDadosHospital() {
    const email = document.getElementById("email").value.trim();
    const telefoneLimpo = telefone.replace(/\D/g, '');
    const endereco = document.getElementById("endereco").value.trim();
    const horarioInicio = document.getElementById("horario_inicio").value.trim();
    const horarioFim = document.getElementById("horario_fim").value.trim();
    const cnpj = document.getElementById("cnpj").value.trim();

    if (email === "" || telefone === "" || endereco === "" || horario === "" || cnpj === "") {
        alert("Todos os campos são obrigatórios.");
        return false;
    }

    // Validação básica de e-mail
    if (!email.includes("@") || !email.includes(".")) {
        alert("E-mail inválido.");
        return false;
    }

    if (telefoneLimpo.length < 10 || telefoneLimpo.length > 11) {
        alert("Telefone inválido. Informe um número com DDD (ex: (18) 99799-0000).");
        return false;
    }
    if (horarioInicio === "" || horarioFim === "") {
        alert("Preencha o horário de funcionamento (início e fim).");
        return false;
    }

    // Validação básica de CNPJ (apenas tamanho)
    if (cnpj.length !== 14 || isNaN(cnpj)) {
        alert("CNPJ inválido. Deve conter 14 dígitos numéricos.");
        return false;
    }

    return true;
}


document.addEventListener("DOMContentLoaded", function () {
    const cnpjInput = document.getElementById("cnpj");

    if (cnpjInput) {
        cnpjInput.addEventListener("input", function () {
            let cnpj = cnpjInput.value.replace(/\D/g, '');

            if (cnpj.length > 14) {
                cnpj = cnpj.slice(0, 14);
            }

            cnpj = cnpj.replace(/^(\d{2})(\d)/, "$1.$2");
            cnpj = cnpj.replace(/^(\d{2})\.(\d{3})(\d)/, "$1.$2.$3");
            cnpj = cnpj.replace(/\.(\d{3})(\d)/, ".$1/$2");
            cnpj = cnpj.replace(/(\d{4})(\d)/, "$1-$2");

            cnpjInput.value = cnpj;
        });
    }
});

document.addEventListener("DOMContentLoaded", function () {
    const logoInput = document.getElementById("logo");
    const preview = document.getElementById("previewLogo");

    if (logoInput && preview) {
        logoInput.addEventListener("change", function () {
            const file = logoInput.files[0];

            if (file) {
                const reader = new FileReader();
                reader.onload = function (e) {
                    preview.src = e.target.result;
                    preview.style.display = "block";
                };
                reader.readAsDataURL(file);
            } else {
                preview.src = "#";
                preview.style.display = "none";
            }
        });
    }
});


document.getElementById("formDadosHospital").addEventListener("submit", function (e)
{
    e.preventDefault();

    const dados = {
    email: document.getElementById("email").value,
    telefone: document.getElementById("telefone").value,
    endereco: document.getElementById("endereco").value,
    horarioInicio: document.getElementById("horario_inicio").value,
    horarioFim: document.getElementById("horario_fim").value,
    cnpj: document.getElementById("cnpj").value
};

    fetch("http://localhost:4567/api/dados-hospital", {
    method: "POST",
    headers: {"Content-Type": "application/json"},
    body: JSON.stringify(dados)
})
    .then(resp => resp.json())
    .then(data => alert("Dados salvos com sucesso!"))
    .catch(err => alert("Erro: " + err));
});
