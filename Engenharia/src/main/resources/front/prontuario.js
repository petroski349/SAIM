
function coletarDadosFormulario() {
    return {
        nome: document.getElementById("nome").value,
        rg: document.getElementById("rg").value,
        cpf: document.getElementById("cpf").value,
        dataNascimento: document.getElementById("dataNascimento").value,
        exameFisico: document.getElementById("exameFisico").value,
        exameComplementar: document.getElementById("exameComplementar").value,
        crm: document.getElementById("crm").value,
        endereco: document.getElementById("endereco").textContent,
        dataEntrada: document.getElementById("dataEntrada").textContent,
        situacao: document.getElementById("situacao").textContent
    };
}

function efetuarProntuario() {
    const dados = coletarDadosFormulario();

    fetch("http://localhost:4567/api/prontuario", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(dados)
    })
        .then(res => res.json())
        .then(data => {
            alert("Prontuário efetuado com sucesso!");
        })
        .catch(err => {
            console.error("Erro ao efetuar prontuário:", err);
            alert("Erro ao efetuar prontuário.");
        });
}

function atualizarProntuario() {
    const dados = coletarDadosFormulario();

    fetch("http://localhost:4567/api/prontuario", {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(dados)
    })
        .then(res => res.json())
        .then(data => {
            alert("Prontuário atualizado com sucesso!");
        })
        .catch(err => {
            console.error("Erro ao atualizar prontuário:", err);
            alert("Erro ao atualizar prontuário.");
        });
}
document.addEventListener("DOMContentLoaded", () => {
    // Carrega cabeçalho e rodapé
    fetch("cabecalho.html")
        .then(res => res.text())
        .then(data => {
            document.getElementById("incluirCabecalho").innerHTML = data;
            preencherCabecalhoRodape();
        });

    fetch("rodape.html")
        .then(res => res.text())
        .then(data => {
            document.getElementById("incluirRodape").innerHTML = data;
        });
});
function preencherCabecalhoRodape() {
    // Aparência
    fetch("http://localhost:4567/api/aparencia-sistema")
        .then(res => res.json())
        .then(data => {
            document.getElementById("nomeInstituicao").textContent = data.nome;
            document.getElementById("logoInstituicao").src = data.logoBase64;
            document.getElementById("textoRodape").textContent = data.rodape;

            document.getElementById("cabecalho-sistema").style.backgroundColor = data.cor;
            document.getElementById("rodape-sistema").style.backgroundColor = data.cor;
        })
        .catch(err => {
            console.error("Erro ao carregar aparência do sistema", err);
        });

    // Dados do hospital
    fetch("http://localhost:4567/api/dados-hospital")
        .then(res => res.json())
        .then(data => {
            document.getElementById("emailHospital").textContent = data.email;
            document.getElementById("telefoneHospital").textContent = data.telefone;
            document.getElementById("enderecoHospital").textContent = data.endereco;
            document.getElementById("horarioHospital").textContent = `${data.horarioInicio} - ${data.horarioFim}`;
            document.getElementById("cnpjHospital").textContent = data.cnpj;
        })
        .catch(err => {
            console.error("Erro ao carregar dados do hospital", err);
        });
}


