// Elementos DOM
const formLogin = document.getElementById('form-login');
const formRegister = document.getElementById('form-register');
const messageDiv = document.getElementById('message');

const userInfoDiv = document.getElementById('user-info');
const userIdSpan = document.getElementById('user-id');
const userEmailSpan = document.getElementById('user-email');
const userNomeSpan = document.getElementById('user-nome');
const userTipoSpan = document.getElementById('user-tipo');
const userDataSpan = document.getElementById('user-data');
const logoutBtn = document.getElementById('logout-btn');

const nomeInput = document.getElementById('nome');

// API base URL
const API_BASE_URL = 'http://localhost:8080/acesso';

// Máscara para campo nome: só letras e espaços
if (nomeInput) {
  nomeInput.addEventListener('input', () => {
    nomeInput.value = nomeInput.value.replace(/[^a-zA-ZÀ-ÿ\s]/g, '');
  });
}

// Mensagens
function showMessage(msg, isError = false) {
  messageDiv.textContent = msg;
  messageDiv.style.color = isError ? 'red' : 'green';
  setTimeout(() => messageDiv.textContent = '', 5000);
}

// Token localStorage
function saveToken(token) {
  localStorage.setItem('authToken', token);
}
function getToken() {
  return localStorage.getItem('authToken');
}
function clearToken() {
  localStorage.removeItem('authToken');
}

// Exibir usuário logado
function showUserInfo(usuario, token) {
  userIdSpan.textContent = usuario.id || usuario.idUsuario || '';
  userEmailSpan.textContent = usuario.email || '';
  userNomeSpan.textContent = usuario.nome || '';
  userTipoSpan.textContent = usuario.tipo || '';
  userDataSpan.textContent = usuario.dataRegistro ? new Date(usuario.dataRegistro).toLocaleString() : '';
  userInfoDiv.style.display = 'block';
  formLogin.style.display = 'none';
  formRegister.style.display = 'none';
  showMessage(`Token JWT: ${token}`);
}

// Esconder usuário e mostrar formulários
function hideUserInfo() {
  userInfoDiv.style.display = 'none';
  formLogin.style.display = 'block';
  formRegister.style.display = 'block';
  messageDiv.textContent = '';
}

// Logout
logoutBtn.addEventListener('click', () => {
  clearToken();
  hideUserInfo();
  showMessage('Você saiu da sessão.');
});

// Login submit
if (formLogin) {
  formLogin.addEventListener('submit', async e => {
    e.preventDefault();
    const email = document.getElementById('email').value.trim();
    const password = document.getElementById('password').value.trim();

    try {
      const response = await fetch(`${API_BASE_URL}/login`, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({email, password})
      });
      const data = await response.json();

      if (!response.ok) {
        showMessage(data.error || 'Erro ao logar', true);
        return;
      }

      saveToken(data.token);
      showUserInfo(data.usuario, data.token);
    } catch {
      showMessage('Erro de conexão com o servidor', true);
    }
  });
}

// Cadastro submit
if (formRegister) {
  formRegister.addEventListener('submit', async e => {
    e.preventDefault();
    const nome = document.getElementById('nome').value.trim();
    const email = document.getElementById('email-register').value.trim();
    const password = document.getElementById('password-register').value.trim();
    const tipo = document.getElementById('tipo-register').value;  // Obtendo o valor do campo tipo

    const usuario = { nome, email, senha: password, tipo };  // Incluindo tipo na requisição

    try {
      const response = await fetch(`${API_BASE_URL}/cadastrar`, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(usuario)
      });

      if (response.ok) {
        showMessage('Usuário criado com sucesso.');
        formRegister.reset();
      } else {
        const data = await response.json();
        showMessage(data.error || 'Erro ao criar usuário', true);
      }
    } catch {
      showMessage('Erro de conexão com o servidor', true);
    }
  });
}

// Auto login
async function tryAutoLogin() {
  const token = getToken();
  if (!token) return;

  try {
    const response = await fetch(`${API_BASE_URL}/me`, {
      method: 'GET',
      headers: { Authorization: `Bearer ${token}` }
    });

    if (!response.ok) {
      clearToken();
      return;
    }

    const usuario = await response.json();
    showUserInfo(usuario, token);
  } catch {
    clearToken();
  }
}

tryAutoLogin();
