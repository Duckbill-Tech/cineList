import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { recuperarSenha } from "../apis/apiRecuperarSenha";

function RecuperarSenha() {
  const [email, setEmail] = useState("");
  const [feedback, setFeedback] = useState("");
  const [erro, setErro] = useState(null);
  const navigate = useNavigate();

  // Função chamada quando o email de recuperação for enviado
  const handleRecuperarSenha = async (event) => {
    event.preventDefault(); // Impede o comportamento padrão do formulário

    if (!email.trim()) {
      setErro("Campo de email é obrigatório");
      return;
    }

    try {
      const response = await recuperarSenha(email); //Chama a API

      setFeedback(
        response.message ||
          "Instruções de recuperação de senha enviadas para o seu email!"
      ); // Simula o processo de recuperação de senha (TODO: substituir por uma chamada de API real)
      setErro(null);
      setEmail(""); // Limpa o campo após o envio
      setTimeout(() => navigate("/login"), 3000); // Redireciona para a página de login após 3 segundos
    } catch (error) {
      setErro(error.message);
      setFeedback("");
    }
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

          {erro && (
            <div className="text-center text-red-500 mt-4">
              <p>{erro}</p>
            </div>
          )}
        </form>
      </div>
    </div>
  );
}

export default RecuperarSenha;
