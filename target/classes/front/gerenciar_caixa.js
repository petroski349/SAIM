document.addEventListener("DOMContentLoaded", function() {
    // Carregar cabeçalho
    fetch('cabecalho.html')
        .then(response => response.text())
        .then(data => {
            document.getElementById('cabecalho-container').innerHTML = data;
            carregarAparenciaSistema();
        });

    // Elementos da página
    const statusCaixa = document.getElementById("status-caixa");
    const caixaFechado = document.getElementById("caixa-fechado");
    const caixaAberto = document.getElementById("caixa-aberto");
    const dataAbertura = document.getElementById("data-abertura");
    const saldoInicial = document.getElementById("saldo-inicial");
    const saldoAtual = document.getElementById("saldo-atual");
    
    // Botões
    const abrirCaixaBtn = document.getElementById("abrirCaixaBtn");
    const fecharCaixaBtn = document.getElementById("fecharCaixaBtn");
    const atualizarSaldoBtn = document.getElementById("atualizarSaldoBtn");
    const novaEntradaBtn = document.getElementById("novaEntradaBtn");
    const novaSaidaBtn = document.getElementById("novaSaidaBtn");
    
    // Modais
    const abrirCaixaModal = document.getElementById("abrirCaixaModal");
    const fecharCaixaModal = document.getElementById("fecharCaixaModal");
    const movimentacaoModal = document.getElementById("movimentacaoModal");
    
    // Formulários
    const abrirCaixaForm = document.getElementById("abrirCaixaForm");
    const fecharCaixaForm = document.getElementById("fecharCaixaForm");
    const movimentacaoForm = document.getElementById("movimentacaoForm");
    
    // Botões de fechar modal
    const closeButtons = document.querySelectorAll(".close-btn");
    
    // Botões de cancelar
    const cancelarAbertura = document.getElementById("cancelarAbertura");
    const cancelarFechamento = document.getElementById("cancelarFechamento");
    const cancelarMovimentacao = document.getElementById("cancelarMovimentacao");
    
    // Tabela de movimentações
    const movimentacoesLista = document.getElementById("movimentacoes-lista");
    
    // Variáveis globais
    let usuarioLogadoId = null;
    let caixaAtualId = null;
    let totalEntradas = 0;
    let totalSaidas = 0;

    // Inicialização
    getUsuarioLogado();
    verificarStatusCaixa();

    // Event Listeners
    abrirCaixaBtn.addEventListener("click", abrirModalCaixa);
    fecharCaixaBtn.addEventListener("click", abrirModalFecharCaixa);
    atualizarSaldoBtn.addEventListener("click", verificarStatusCaixa);
    novaEntradaBtn.addEventListener("click", () => abrirModalMovimentacao("entrada"));
    novaSaidaBtn.addEventListener("click", () => abrirModalMovimentacao("saida"));
    
    abrirCaixaForm.addEventListener("submit", submeterAberturaCaixa);
    fecharCaixaForm.addEventListener("submit", submeterFechamentoCaixa);
    movimentacaoForm.addEventListener("submit", submeterMovimentacao);
    
    cancelarAbertura.addEventListener("click", () => fecharModal(abrirCaixaModal));
    cancelarFechamento.addEventListener("click", () => fecharModal(fecharCaixaModal));
    cancelarMovimentacao.addEventListener("click", () => fecharModal(movimentacaoModal));
    
    closeButtons.forEach(button => {
        button.addEventListener("click", function() {
            const modal = this.closest(".modal");
            fecharModal(modal);
        });
    });

    // Funções
    function getUsuarioLogado() {
        fetch('http://localhost:4567/api/colaborador')
            .then(response => {
                if (!response.ok) {
                    throw new Error(`Erro HTTP ${response.status}`);
                }
                return response.json();
            })
            .then(usuario => {
                if (usuario && usuario.id) {
                    usuarioLogadoId = usuario.id;
                    console.log("ID do usuário logado:", usuarioLogadoId);
                } else {
                    throw new Error("Não foi possível obter o ID do usuário logado");
                }
            })
            .catch(error => {
                console.error("Erro ao buscar usuário logado:", error);
                alert("Erro ao buscar usuário logado. Algumas funcionalidades podem não estar disponíveis.");
            });
    }

    function carregarAparenciaSistema() {
        fetch('http://localhost:4567/api/aparencia-sistema')
            .then(response => response.json())
            .then(aparencia => {
                if (aparencia) {
                    document.getElementById('nomeInstituicao').textContent = aparencia.nome || 'Sistema SAIM';
                    if (aparencia.logoBase64) {
                        document.getElementById('logoInstituicao').src = aparencia.logoBase64;
                    }
                    if (aparencia.cor) {
                        document.documentElement.style.setProperty('--cor-primaria', aparencia.cor);
                    }
                }
            })
            .catch(error => console.error('Erro ao carregar aparência:', error));
    }

    function verificarStatusCaixa() {
        fetch('http://localhost:4567/api/caixa/status')
            .then(response => {
                if (!response.ok) {
                    return response.json().then(err => { throw new Error(err.mensagem || `Erro HTTP ${response.status}`); });
                }
                return response.json();
            })
            .then(data => {
                if (data.status === "aberto" && data.caixa) {
                    // Caixa está aberto
                    caixaAtualId = data.caixa.id;
                    mostrarCaixaAberto(data.caixa);
                    carregarMovimentacoes(caixaAtualId);
                } else {
                    // Caixa está fechado
                    mostrarCaixaFechado();
                }
            })
            .catch(error => {
                console.error("Erro ao verificar status do caixa:", error);
                alert(`Erro ao verificar status do caixa: ${error.message}`);
            });
    }

    function mostrarCaixaAberto(caixa) {
        caixaFechado.style.display = "none";
        caixaAberto.style.display = "block";
        
        // Formatar data
        const dataFormatada = new Date(caixa.dataAbertura).toLocaleString('pt-BR');
        dataAbertura.textContent = dataFormatada;
        
        // Formatar valores monetários
        saldoInicial.textContent = formatarMoeda(caixa.saldoInicial);
        saldoAtual.textContent = formatarMoeda(caixa.saldoAtual || caixa.saldoInicial);
        
        // Habilitar botões de movimentação
        novaEntradaBtn.disabled = false;
        novaSaidaBtn.disabled = false;
    }

    function mostrarCaixaFechado() {
        caixaAberto.style.display = "none";
        caixaFechado.style.display = "block";
        caixaAtualId = null;
        
        // Desabilitar botões de movimentação
        novaEntradaBtn.disabled = true;
        novaSaidaBtn.disabled = true;
        
        // Limpar tabela de movimentações
        movimentacoesLista.innerHTML = "<tr><td colspan='4' class='text-center'>Nenhuma movimentação registrada</td></tr>";
    }

    function carregarMovimentacoes(caixaId) {
        if (!caixaId) return;
        
        fetch('http://localhost:4567/api/caixa/movimentacoes')
            .then(response => {
                if (!response.ok) {
                    return response.json().then(err => { throw new Error(err.mensagem || `Erro HTTP ${response.status}`); });
                }
                return response.json();
            })
            .then(movimentacoes => {
                if (movimentacoes && movimentacoes.length > 0) {
                    atualizarTabelaMovimentacoes(movimentacoes);
                    calcularTotais(movimentacoes);
                } else {
                    movimentacoesLista.innerHTML = "<tr><td colspan='4' class='text-center'>Nenhuma movimentação registrada</td></tr>";
                    totalEntradas = 0;
                    totalSaidas = 0;
                }
            })
            .catch(error => {
                console.error("Erro ao carregar movimentações:", error);
                movimentacoesLista.innerHTML = `<tr><td colspan='4' class='text-center'>Erro ao carregar movimentações: ${error.message}</td></tr>`;
            });
    }

    function atualizarTabelaMovimentacoes(movimentacoes) {
        movimentacoesLista.innerHTML = "";
        
        movimentacoes.forEach(mov => {
            const tr = document.createElement("tr");
            
            // Formatar data
            const dataFormatada = new Date(mov.dataHora).toLocaleString('pt-BR');
            
            // Formatar tipo
            const tipoFormatado = mov.tipo === "entrada" ? 
                "<span class='badge badge-success'>Entrada</span>" : 
                "<span class='badge badge-warning'>Saída</span>";
            
            // Formatar valor
            const valorFormatado = formatarMoeda(mov.valor);
            
            tr.innerHTML = `
                <td>${dataFormatada}</td>
                <td>${tipoFormatado}</td>
                <td>${mov.descricao}</td>
                <td class="valor ${mov.tipo === 'entrada' ? 'entrada' : 'saida'}">${valorFormatado}</td>
            `;
            
            movimentacoesLista.appendChild(tr);
        });
    }

    function calcularTotais(movimentacoes) {
        totalEntradas = 0;
        totalSaidas = 0;
        
        movimentacoes.forEach(mov => {
            const valor = parseFloat(mov.valor);
            if (mov.tipo === "entrada") {
                totalEntradas += valor;
            } else if (mov.tipo === "saida") {
                totalSaidas += valor;
            }
        });
    }

    function abrirModalCaixa() {
        document.getElementById("saldoInicial").value = "";
        document.getElementById("observacoesAbertura").value = "";
        abrirCaixaModal.style.display = "block";
    }

    function abrirModalFecharCaixa() {
        // Buscar dados atualizados do caixa
        fetch('http://localhost:4567/api/caixa/status')
            .then(response => response.json())
            .then(data => {
                if (data.status === "aberto" && data.caixa) {
                    const caixa = data.caixa;
                    const saldoInicialValor = parseFloat(caixa.saldoInicial);
                    const saldoAtualValor = parseFloat(caixa.saldoAtual || caixa.saldoInicial);
                    
                    // Preencher resumo
                    document.getElementById("resumo-saldo-inicial").textContent = formatarMoeda(saldoInicialValor);
                    document.getElementById("resumo-entradas").textContent = formatarMoeda(totalEntradas);
                    document.getElementById("resumo-saidas").textContent = formatarMoeda(totalSaidas);
                    document.getElementById("resumo-saldo-final").textContent = formatarMoeda(saldoAtualValor);
                    
                    document.getElementById("observacoesFechamento").value = "";
                    fecharCaixaModal.style.display = "block";
                } else {
                    alert("Não há caixa aberto para fechar.");
                }
            })
            .catch(error => {
                console.error("Erro ao buscar dados do caixa:", error);
                alert("Erro ao buscar dados do caixa para fechamento.");
            });
    }

    function abrirModalMovimentacao(tipo) {
        if (!caixaAtualId) {
            alert("É necessário abrir o caixa primeiro.");
            return;
        }
        
        document.getElementById("tipoMovimentacao").value = tipo;
        document.getElementById("valorMovimentacao").value = "";
        document.getElementById("descricaoMovimentacao").value = "";
        
        document.getElementById("movimentacaoTitulo").textContent = tipo === "entrada" ? "Nova Entrada" : "Nova Saída";
        
        movimentacaoModal.style.display = "block";
    }

    function fecharModal(modal) {
        modal.style.display = "none";
    }

    function submeterAberturaCaixa(e) {
        e.preventDefault();
        
        if (!usuarioLogadoId) {
            alert("Não foi possível identificar o usuário logado.");
            return;
        }
        
        const saldoInicial = document.getElementById("saldoInicial").value;
        const observacoes = document.getElementById("observacoesAbertura").value;
        
        const dados = {
            usuarioAberturaId: usuarioLogadoId,
            saldoInicial: parseFloat(saldoInicial),
            observacoes: observacoes
        };
        
        fetch('http://localhost:4567/api/caixa/abrir', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(dados)
        })
        .then(response => {
            if (!response.ok) {
                return response.json().then(err => { throw new Error(err.mensagem || `Erro HTTP ${response.status}`); });
            }
            return response.json();
        })
        .then(data => {
            if (data.status === "sucesso") {
                alert("Caixa aberto com sucesso!");
                fecharModal(abrirCaixaModal);
                verificarStatusCaixa();
            } else {
                alert(data.mensagem || "Erro ao abrir caixa.");
            }
        })
        .catch(error => {
            console.error("Erro ao abrir caixa:", error);
            alert(`Erro ao abrir caixa: ${error.message}`);
        });
    }

    function submeterFechamentoCaixa(e) {
        e.preventDefault();
        
        if (!usuarioLogadoId || !caixaAtualId) {
            alert("Não foi possível identificar o usuário logado ou o caixa atual.");
            return;
        }
        
        const observacoes = document.getElementById("observacoesFechamento").value;
        
        const dados = {
            usuarioFechamentoId: usuarioLogadoId,
            observacoes: observacoes
        };
        
        fetch('http://localhost:4567/api/caixa/fechar', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(dados)
        })
        .then(response => {
            if (!response.ok) {
                return response.json().then(err => { throw new Error(err.mensagem || `Erro HTTP ${response.status}`); });
            }
            return response.json();
        })
        .then(data => {
            if (data.status === "sucesso") {
                alert("Caixa fechado com sucesso!");
                fecharModal(fecharCaixaModal);
                verificarStatusCaixa();
            } else {
                alert(data.mensagem || "Erro ao fechar caixa.");
            }
        })
        .catch(error => {
            console.error("Erro ao fechar caixa:", error);
            alert(`Erro ao fechar caixa: ${error.message}`);
        });
    }

    function submeterMovimentacao(e) {
        e.preventDefault();
        
        if (!usuarioLogadoId || !caixaAtualId) {
            alert("Não foi possível identificar o usuário logado ou o caixa atual.");
            return;
        }
        
        const tipo = document.getElementById("tipoMovimentacao").value;
        const valor = document.getElementById("valorMovimentacao").value;
        const descricao = document.getElementById("descricaoMovimentacao").value;
        
        const dados = {
            caixaId: caixaAtualId,
            usuarioRegistroId: usuarioLogadoId,
            tipo: tipo,
            valor: parseFloat(valor),
            descricao: descricao,
            dataHora: new Date().toISOString()
        };
        
        fetch('http://localhost:4567/api/caixa/movimentacao', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(dados)
        })
        .then(response => {
            if (!response.ok) {
                return response.json().then(err => { throw new Error(err.mensagem || `Erro HTTP ${response.status}`); });
            }
            return response.json();
        })
        .then(data => {
            if (data.status === "sucesso") {
                alert("Movimentação registrada com sucesso!");
                fecharModal(movimentacaoModal);
                verificarStatusCaixa();
                carregarMovimentacoes(caixaAtualId);
            } else {
                alert(data.mensagem || "Erro ao registrar movimentação.");
            }
        })
        .catch(error => {
            console.error("Erro ao registrar movimentação:", error);
            alert(`Erro ao registrar movimentação: ${error.message}`);
        });
    }

    // Função auxiliar para formatar valores monetários
    function formatarMoeda(valor) {
        return new Intl.NumberFormat('pt-BR', {
            style: 'currency',
            currency: 'BRL'
        }).format(valor);
    }
});
