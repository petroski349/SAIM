document.addEventListener("DOMContentLoaded", function() {
    // Elementos da página
    const novoMedicoBtn = document.getElementById("novoMedicoBtn");
    const medicoModal = document.getElementById("medicoModal");
    const closeBtn = document.querySelector(".close-btn");
    const cancelarBtn = document.getElementById("cancelarBtn");
    const medicoForm = document.getElementById("medicoForm");
    const medicosLista = document.getElementById("medicosLista");
    const buscaMedico = document.getElementById("buscaMedico");
    const buscarBtn = document.getElementById("buscarBtn");
    const modalTitulo = document.getElementById("modalTitulo");
    const erroDiv = document.getElementById("erroDiv"); // Adicionar um div para erros

    let usuarioLogadoId = null; // Variável para armazenar o ID do usuário logado

    // Busca o ID do usuário logado ao carregar a página
    getUsuarioLogadoId();
    // Carrega os médicos ao iniciar
    carregarMedicos();

    // Event listeners
    novoMedicoBtn.addEventListener("click", abrirModalNovoMedico);
    closeBtn.addEventListener("click", fecharModal);
    cancelarBtn.addEventListener("click", fecharModal);
    medicoForm.addEventListener("submit", salvarMedico);
    buscarBtn.addEventListener("click", carregarMedicos);
    buscaMedico.addEventListener("keypress", function(e) {
        if (e.key === "Enter") carregarMedicos();
    });

    // Máscara para telefone
    const medicoTelefone = document.getElementById("medicoTelefone");
    if (medicoTelefone) {
        medicoTelefone.addEventListener("input", function() {
            let telefone = this.value.replace(/\D/g, '');
            if (telefone.length > 11) telefone = telefone.slice(0, 11);

            telefone = telefone.replace(/^(\d{2})(\d)/g, "($1) $2");
            if (telefone.length > 10) {
                telefone = telefone.replace(/(\d{5})(\d)/, "$1-$2");
            } else {
                telefone = telefone.replace(/(\d{4})(\d)/, "$1-$2");
            }

            this.value = telefone;
        });
    }

    // Funções
    function exibirErro(mensagem) {
        // Implementar uma forma mais visual de exibir erros, se houver um elemento para isso
        // if (erroDiv) {
        //     erroDiv.textContent = mensagem;
        //     erroDiv.style.display = 'block';
        // }
        console.error("Erro:", mensagem);
        alert(mensagem); // Mantém o alert por enquanto
    }

    function limparErro() {
        // if (erroDiv) {
        //     erroDiv.textContent = '';
        //     erroDiv.style.display = 'none';
        // }
    }

    function getUsuarioLogadoId() {
        fetch('http://localhost:4567/api/colaborador')
            .then(response => {
                if (!response.ok) {
                    throw new Error(`Erro HTTP ${response.status} ao buscar colaborador`);
                }
                return response.json();
            })
            .then(colaborador => {
                if (colaborador && colaborador.id) {
                    usuarioLogadoId = colaborador.id;
                    console.log("ID do usuário logado:", usuarioLogadoId);
                } else {
                    throw new Error("Não foi possível obter o ID do usuário logado.");
                }
            })
            .catch(error => {
                exibirErro(`Falha ao obter informações do usuário logado: ${error.message}. Não será possível salvar médicos.`);
                usuarioLogadoId = null; // Garante que não tentaremos salvar sem ID
            });
    }

    function abrirModalNovoMedico() {
        limparErro();
        medicoForm.reset();
        document.getElementById("medicoId").value = "";
        modalTitulo.textContent = "Cadastrar Novo Médico";
        medicoModal.style.display = "block";
    }

    function abrirModalEditarMedico(medico) {
        limparErro();
        document.getElementById("medicoId").value = medico.id;
        document.getElementById("medicoNome").value = medico.nome;
        document.getElementById("medicoCrm").value = medico.crm;
        document.getElementById("medicoEspecialidade").value = medico.especialidade;
        document.getElementById("medicoTelefone").value = medico.telefone || "";
        document.getElementById("medicoEmail").value = medico.email || "";
        document.getElementById("medicoHorario").value = medico.horarioAtendimento || "";

        modalTitulo.textContent = "Editar Médico";
        medicoModal.style.display = "block";
    }

    function fecharModal() {
        limparErro();
        medicoModal.style.display = "none";
    }

    function carregarMedicos() {
        limparErro();
        const termoBusca = buscaMedico.value.trim();
        let url = "http://localhost:4567/api/medicos";

        // Busca por especialidade se houver termo
        // Nota: O backend foi ajustado para buscar por especialidade em /api/medicos/especialidade/:especialidade
        // Se a busca for por nome ou CRM, precisaria ajustar a URL ou o backend
        if (termoBusca) {
             // Ajuste: Verificar se o termo é um CRM (números) ou especialidade (texto)
             if (/^\d+$/.test(termoBusca)) { // Se for número, busca por CRM
                 url = `http://localhost:4567/api/medicos/crm/${termoBusca}`;
             } else { // Senão, busca por especialidade (ou nome, se o backend suportar)
                 url = `http://localhost:4567/api/medicos/especialidade/${termoBusca}`;
             }
        }

        fetch(url)
            .then(response => {
                if (!response.ok) {
                    // Tenta ler a mensagem de erro do JSON, se houver
                    return response.json().then(err => {
                        throw new Error(err.mensagem || `Erro HTTP ${response.status}`);
                    }).catch(() => {
                        // Se não conseguir ler JSON, lança erro genérico
                        throw new Error(`Erro HTTP ${response.status} ao carregar médicos`);
                    });
                }
                return response.json();
            })
            .then(medicosData => {
                medicosLista.innerHTML = "";
                let medicos = [];

                // Adapta a resposta caso a busca seja por CRM (retorna um objeto, não lista)
                if (!Array.isArray(medicosData) && medicosData && medicosData.id) {
                    medicos = [medicosData]; // Transforma em array de um elemento
                } else if (Array.isArray(medicosData)) {
                    medicos = medicosData;
                }

                if (medicos.length === 0) {
                    medicosLista.innerHTML = "<tr><td colspan='6'>Nenhum médico encontrado</td></tr>";
                    return;
                }

                medicos.forEach(medico => {
                    const tr = document.createElement("tr");
                    tr.innerHTML = `
                        <td>${medico.nome}</td>
                        <td>${medico.crm}</td>
                        <td>${medico.especialidade}</td>
                        <td>${medico.telefone || '-'}</td>
                        <td>${medico.email || '-'}</td>
                        <td class="action-buttons">
                            <button class="edit-btn" data-id="${medico.id}">Editar</button>
                            <button class="delete-btn" data-id="${medico.id}">Excluir</button>
                        </td>
                    `;
                    medicosLista.appendChild(tr);
                });

                // Adiciona eventos aos botões de edição e exclusão
                attachActionButtons();
            })
            .catch(error => {
                console.error("Erro ao carregar médicos:", error);
                medicosLista.innerHTML = `<tr><td colspan='6'>Erro ao carregar médicos: ${error.message}</td></tr>`;
                exibirErro(`Erro ao carregar médicos: ${error.message}`);
            });
    }

    function attachActionButtons() {
         document.querySelectorAll(".edit-btn").forEach(btn => {
            btn.addEventListener("click", function() {
                const medicoId = this.getAttribute("data-id");
                buscarMedicoPorId(medicoId);
            });
        });

        document.querySelectorAll(".delete-btn").forEach(btn => {
            btn.addEventListener("click", function() {
                const medicoId = this.getAttribute("data-id");
                if (confirm("Tem certeza que deseja excluir este médico?")) {
                    excluirMedico(medicoId);
                }
            });
        });
    }

    function buscarMedicoPorId(id) {
        limparErro();
        fetch(`http://localhost:4567/api/medicos/${id}`)
            .then(response => {
                 if (!response.ok) {
                    return response.json().then(err => { throw new Error(err.mensagem || `Erro HTTP ${response.status}`); });
                 }
                 return response.json();
             })
            .then(medico => {
                // O backend agora retorna o médico diretamente ou um erro JSON
                abrirModalEditarMedico(medico);
            })
            .catch(error => {
                console.error("Erro ao buscar médico:", error);
                exibirErro(`Erro ao buscar médico: ${error.message}`);
            });
    }

    function salvarMedico(e) {
        e.preventDefault();
        limparErro();

        // Verifica se temos o ID do usuário logado
        if (!usuarioLogadoId) {
            exibirErro("Não foi possível identificar o usuário logado. Não é possível salvar.");
            return;
        }

        const medico = {
            // Não envia 'id' no POST, apenas no PUT
            nome: document.getElementById("medicoNome").value.trim(),
            crm: document.getElementById("medicoCrm").value.trim(),
            especialidade: document.getElementById("medicoEspecialidade").value.trim(),
            telefone: document.getElementById("medicoTelefone").value.trim(),
            email: document.getElementById("medicoEmail").value.trim(),
            horarioAtendimento: document.getElementById("medicoHorario").value.trim(),
            usuarioId: usuarioLogadoId // *** ADICIONADO usuarioId ***
        };

        const medicoId = document.getElementById("medicoId").value;
        const method = medicoId ? "PUT" : "POST";
        const url = medicoId
            ? `http://localhost:4567/api/medicos/${medicoId}`
            : "http://localhost:4567/api/medicos";

        // Adiciona o ID ao objeto apenas se for PUT (edição)
        if (medicoId) {
            medico.id = medicoId;
        }

        // Validação básica no frontend (pode ser mais robusta)
        if (!medico.nome || !medico.crm || !medico.especialidade) {
            exibirErro("Nome, CRM e Especialidade são obrigatórios.");
            return;
        }

        fetch(url, {
            method: method,
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(medico)
        })
            .then(response => {
                // Sempre tenta parsear como JSON, mesmo em erro
                return response.json().then(data => {
                    if (!response.ok) {
                        // Se a resposta não for OK (e.g., 400, 500), lança um erro com a mensagem do JSON
                        throw new Error(data.mensagem || `Erro ${response.status}`);
                    }
                    return data; // Retorna os dados de sucesso
                });
            })
            .then(data => {
                // Se chegou aqui, a operação foi bem-sucedida (status 2xx)
                alert(data.mensagem || "Operação realizada com sucesso!"); // Usa a mensagem do backend
                fecharModal();
                carregarMedicos(); // Recarrega a lista
            })
            .catch(error => {
                console.error("Erro ao salvar médico:", error);
                exibirErro(`Erro ao salvar médico: ${error.message}`);
            });
    }

    function excluirMedico(id) {
        limparErro();
        fetch(`http://localhost:4567/api/medicos/${id}`, {
            method: "DELETE"
        })
            .then(response => {
                return response.json().then(data => {
                    if (!response.ok) {
                        throw new Error(data.mensagem || `Erro ${response.status}`);
                    }
                    return data;
                });
            })
            .then(data => {
                alert(data.mensagem || "Médico excluído com sucesso!");
                carregarMedicos(); // Recarrega a lista
            })
            .catch(error => {
                console.error("Erro ao excluir médico:", error);
                exibirErro(`Erro ao excluir médico: ${error.message}`);
            });
    }
});

