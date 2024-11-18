import { useState } from "react";
import { useNavigate } from "react-router-dom";

function RecuperarSenha() {
  const [email, setEmail] = useState("");
  const [feedback, setFeedback] = useState("");
  const navigate = useNavigate();

  // Função chamada quando o email de recuperação for enviado
  const handleRecuperarSenha = (event) => {
    event.preventDefault(); // Impede o comportamento padrão do formulário

    if (!email.trim()) {
      return alert("Campo de email é obrigatório");
    }

    // Simula o processo de recuperação de senha (TODO: substituir por uma chamada de API real)
    setFeedback(
      "Instruções de recuperação de senha enviadas para o seu email!"
    );

    // Limpa o campo após o envio
    setEmail("");

    // Redireciona para a página de login após 3 segundos
    setTimeout(() => navigate("/login"), 3000);
  };

  return (
    <div className="w-full bg-black flex justify-center items-center flex-grow py-10">
      <div className="w-full max-w-md space-y-6 p-8">
        <form onSubmit={handleRecuperarSenha} className="space-y-4">
          <label htmlFor="email" className="sr-only">
            Email
          </label>
          <input
            id="email"
            type="email"
            placeholder="Digite seu email"
            className="w-full px-4 py-3 rounded-md text-black"
            value={email}
            onChange={(event) => setEmail(event.target.value)}
            aria-required="true"
            aria-label="Digite seu email"
            autoComplete="email"
          />

          <button
            type="submit"
            className="border border-amber-500 text-amber-500 px-4 py-2 rounded-md font-medium w-full"
            aria-label="Enviar instruções de recuperação"
          >
            Enviar Instruções
          </button>

          {feedback && (
            <div className="text-center text-white mt-4">
              <p>{feedback}</p>
            </div>
          )}
        </form>
      </div>
    </div>
  );
}

export default RecuperarSenha;
