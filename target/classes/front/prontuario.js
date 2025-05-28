let cpfSelecionado = "";

// ✅ Carrega dados do colaborador automaticamente
document.addEventListener("DOMContentLoaded", () => {
    fetch("http://localhost:4567/api/colaborador")
        .then(res => res.json())
        .then(data => {
            document.getElementById("nomeColaborador").textContent = data.nome;
            document.getElementById("cargoColaborador").textContent = data.cargo;
            document.getElementById("crmColaborador").textContent = data.crm || "---";
        });

    // Carregar cabeçalho e rodapé
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

    aplicarMascaraCPF();
});

function formatarData(dataString) {
    const data = new Date(dataString);
    const dia = String(data.getDate()).padStart(2, '0');
    const mes = String(data.getMonth() + 1).padStart(2, '0');
    const ano = data.getFullYear();
    const hora = String(data.getHours()).padStart(2, '0');
    const minuto = String(data.getMinutes()).padStart(2, '0');

    return `${dia}/${mes}/${ano} ${hora}:${minuto}`;
}

// 🔎 Buscar paciente e histórico
function buscarPaciente() {
    const cpf = document.getElementById("cpfBusca").value.trim();
    const nome = document.getElementById("nomeBusca").value.trim();

    if (!cpf && !nome) {
        alert("Informe CPF ou Nome do paciente.");
        return;
    }

    const url = cpf
        ? `http://localhost:4567/api/paciente?cpf=${cpf}`
        : `http://localhost:4567/api/paciente?nome=${nome}`;

    fetch(url)
        .then(res => res.json())
        .then(data => {
            cpfSelecionado = data.cpf;

            if (!data || !data.historico || data.historico.length === 0) {
                document.getElementById("historicoDados").innerHTML = "Nenhum histórico encontrado.";
            } else {
                const historicoHTML = data.historico.map(item => `
                    <div>
                        <strong>Data:</strong> ${formatarData(item.dataHora)}<br>
                        <strong>Descrição:</strong> ${item.descricao}
                        <hr>
                    </div>
                `).join('');

                document.getElementById("historicoDados").innerHTML = historicoHTML;
            }
        })
        .catch(err => {
            console.error("Erro ao buscar paciente", err);
            document.getElementById("historicoDados").innerHTML = "Erro ao buscar histórico.";
        });
}


// 🩺 Salvar prontuário
function salvarProntuario() {

    const anotacao = document.getElementById("anotacaoConsulta").value.trim();

    if (!cpfSelecionado) {
        alert("Busque um paciente antes de salvar.");
        return;
    }

    if (!anotacao) {
        alert("Preencha a anotação do prontuário.");
        return;
    }

    const dados = {
        cpfPaciente: cpfSelecionado,
        descricao: anotacao
    };

    fetch("http://localhost:4567/api/prontuario", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(dados)
    })
        .then(res => {
            if (res.ok) {
                alert("Prontuário salvo com sucesso!");
                document.getElementById("anotacaoConsulta").value = "";
                buscarPaciente();
            } else {
                throw new Error("Erro ao salvar prontuário");
            }
        })
        .catch(err => {
            console.error(err);
            alert("Erro ao salvar prontuário.");
        });
}


// 🎨 Preencher cabeçalho e rodapé
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
        .catch(err => console.error("Erro ao carregar aparência:", err));

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
        .catch(err => console.error("Erro ao carregar dados hospital:", err));
}

function carregarPrescricao() {
    const cpf = document.getElementById("cpfBusca").value.trim();

    if (!cpf) {
        alert("Informe o CPF do paciente.");
        return;
    }

    fetch(`http://localhost:4567/api/consulta?cpf=${cpf}`)
        .then(res => res.json())
        .then(data => {
            if (data.length === 0) {
                document.getElementById("tooltipPrescricao").innerHTML = "Nenhuma prescrição encontrada.";
                return;
            }

            const itens = data.map(item => `
                <div>
                    <strong>Medicamento:</strong> ${item.medicamento}<br>
                    <strong>Quantidade:</strong> ${item.quantidade}<br>
                    <strong>Período:</strong> ${item.periodo}<br>
                    <hr>
                </div>
            `).join('');

            document.getElementById("tooltipPrescricao").innerHTML = itens;
        })
        .catch(err => {
            console.error("Erro ao carregar prescrição:", err);
            document.getElementById("tooltipPrescricao").innerHTML = "Erro ao buscar dados.";
        });
}


function formatarData(dataString) {
    const data = new Date(dataString);
    const dia = String(data.getDate()).padStart(2, '0');
    const mes = String(data.getMonth() + 1).padStart(2, '0');
    const ano = data.getFullYear();
    const hora = String(data.getHours()).padStart(2, '0');
    const minuto = String(data.getMinutes()).padStart(2, '0');

    return `${dia}/${mes}/${ano} ${hora}:${minuto}`;
}


// 🔢 Máscara automática de CPF
function aplicarMascaraCPF() {
    const cpfInput = document.getElementById("cpfBusca");

    cpfInput.addEventListener("input", function () {
        let value = this.value.replace(/\D/g, "");

        if (value.length > 11) value = value.slice(0, 11);

        value = value.replace(/(\d{3})(\d)/, "$1.$2");
        value = value.replace(/(\d{3})(\d)/, "$1.$2");
        value = value.replace(/(\d{3})(\d{1,2})$/, "$1-$2");

        this.value = value;
    });
}
