function validarAparenciaSistema() {
    const nome = document.getElementById("nome").value.trim();
    const logo = document.getElementById("logo").files[0];
    const rodape = document.getElementById("rodape").value.trim();

    if (!nome || !logo || !rodape) {
        alert("Preencha todos os campos de aparência.");
        return false;
    }

    return true;
}

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

    const form = document.getElementById("formAparenciaSistema");
    if (form) {
        form.addEventListener("submit", function (e) {
            e.preventDefault();

            if (!validarAparenciaSistema()) return;

            const nome = document.getElementById("nome").value.trim();
            const cor = document.getElementById("cor").value;
            const rodape = document.getElementById("rodape").value.trim();
            const logoFile = document.getElementById("logo").files[0];

            const reader = new FileReader();
            reader.onload = function (e) {
                const base64Logo = e.target.result;

                const dados = {
                    nome,
                    cor,
                    rodape,
                    logoBase64: base64Logo // chave clara para indicar que é base64
                };

                fetch("http://localhost:4567/api/aparencia-sistema", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify(dados)
                })
                    .then(res => res.json())
                    .then(data => {
                        alert("Aparência do sistema salva com sucesso!");
                    })
                    .catch(err => {
                        console.error(err);
                        alert("Erro ao enviar os dados da aparência.");
                    });
            };

            reader.readAsDataURL(logoFile);
        });
    }
});
