import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { login } from "../../service/AuthService";

function Login() {
  const [email, setEmail] = useState("");
  const [senha, setSenha] = useState("");
  const [feedback, setFeedback] = useState("");
  const navigate = useNavigate();

  // Função chamada quando o login for realizado
  const handleLogin = async (event) => {
    event.preventDefault(); // Impede o comportamento padrão do formulário

    if (!email.trim() || !senha.trim()) {
      setFeedback("Campos obrigatórios!");
      return;
    }

    try {
      // Chama a função de login do AuthService
      const response = await login(email, senha);

      // Caso o login seja bem-sucedido, redireciona para a página inicial
      navigate(`/home?token=${response.token}`);
    } catch (error) {
      // Define uma mensagem de erro no feedback
      setFeedback("Erro ao fazer login: Verifique suas credenciais.");
      console.error(error);
    } finally {
      // Limpa os campos após o login
      setEmail("");
      setSenha("");
    }
  };

  // Função para redirecionar para a página de registro
  const onRegisterButton = () => {
    navigate("/registro"); // Navega para a página de registro
  };

  // Função para redirecionar para a página de recuperação de senha
  const onForgotPassword = () => {
    navigate("/recuperar-senha"); // Navega para a página de recuperação de senha
  };

  return (
    <div className="w-full bg-black flex justify-center items-center flex-grow py-10">
      <div className="w-full max-w-md space-y-6 p-8">
        {/* Formulário de Login */}
        <form onSubmit={handleLogin} className="space-y-4">
          <label htmlFor="email" className="sr-only">
            Email
          </label>
          <input
            id="email"
            type="text"
            placeholder="Digite seu email"
            className="w-full px-4 py-3 rounded-md text-black"
            value={email}
            onChange={(event) => setEmail(event.target.value)}
            aria-required="true"
            aria-label="Digite seu email"
            autoComplete="email"
          />

          <label htmlFor="senha" className="sr-only">
            Senha
          </label>
          <input
            id="senha"
            type="password"
            placeholder="Digite sua senha"
            className="w-full px-4 py-3 rounded-md text-black"
            value={senha}
            onChange={(event) => setSenha(event.target.value)}
            aria-required="true"
            aria-label="Digite sua senha"
            autoComplete="current-password"
          />

          <button
            type="submit"
            className="border border-amber-500 text-amber-500 px-4 py-2 rounded-md font-medium w-full"
            aria-label="Entrar no sistema"
          >
            Entrar
          </button>

          {/* Mensagem de feedback */}
          {feedback && (
            <div className="text-sm text-red-500 mt-2" aria-live="polite">
              {feedback}
            </div>
          )}

          <div className="flex gap-4 mt-4 justify-between">
            <button
              onClick={onRegisterButton}
              className="text-sm text-amber-500 self-center"
              aria-label="Ir para a página de registro"
            >
              Ainda não sou cadastrada/o
            </button>

            <button
              type="button"
              onClick={onForgotPassword}
              className="text-sm text-amber-500 self-center"
            >
              Esqueci minha senha
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default Login;