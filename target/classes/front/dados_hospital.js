
function validarDadosHospital() {
    const email = document.getElementById("email").value.trim();
    const telefone = document.getElementById("telefone").value.trim();
    const telefoneLimpo = telefone.replace(/\D/g, '');
    const endereco = document.getElementById("endereco").value.trim();
    const horarioInicio = document.getElementById("horario_inicio").value.trim();
    const horarioFim = document.getElementById("horario_fim").value.trim();
    const cnpj = document.getElementById("cnpj").value.replace(/\D/g, '');

    if (!email || !telefone || !endereco || !horarioInicio || !horarioFim || !cnpj) {
        alert("Todos os campos são obrigatórios.");
        return false;
    }

    if (!email.includes("@") || !email.includes(".")) {
        alert("E-mail inválido.");
        return false;
    }

    if (telefoneLimpo.length < 10 || telefoneLimpo.length > 11) {
        alert("Telefone inválido. Informe com DDD.");
        return false;
    }

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
            if (cnpj.length > 14) cnpj = cnpj.slice(0, 14);

            cnpj = cnpj.replace(/^(\d{2})(\d)/, "$1.$2")
                       .replace(/^(\d{2})\.(\d{3})(\d)/, "$1.$2.$3")
                       .replace(/\.(\d{3})(\d)/, ".$1/$2")
                       .replace(/(\d{4})(\d)/, "$1-$2");

            cnpjInput.value = cnpj;
        });
    }

    const form = document.getElementById("formDadosHospital");
    if (form) {
        form.addEventListener("submit", function (e) {
            e.preventDefault();
            if (!validarDadosHospital()) return;

            const dados = {
                email: document.getElementById("email").value,
                telefone: document.getElementById("telefone").value,
                endereco: document.getElementById("endereco").value,
                horarioInicio: document.getElementById("horario_inicio").value,
                horarioFim: document.getElementById("horario_fim").value,
                cnpj: document.getElementById("cnpj").value.replace(/\D/g, '')
            };

            fetch("http://localhost:4567/api/dados-hospital", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(dados)
            })
                .then(res => res.json())
                .then(data => alert("Dados enviados com sucesso!"))
                .catch(err => {
                    console.error(err);
                    alert("Erro ao enviar os dados.");
                });
        });
    }
});
