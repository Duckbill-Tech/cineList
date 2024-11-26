import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { login } from "../../service/AuthService";

function Login() {
  const [email, setEmail] = useState("");
  const [nome, setNome] = useState("");
  const [senha, setSenha] = useState("");
  const [feedback, setFeedback] = useState("");
  const navigate = useNavigate();

  // Função chamada quando o login for realizado
  const handleLogin = async (event) => {
    event.preventDefault();
    if (!email.trim() || !senha.trim()) {
      setFeedback("Campos obrigatórios!");
      return;
    }
    try {
      const {emailUsuario, nomeUsuario} =  await login(email, senha);
      setEmail(emailUsuario);
      setNome(nomeUsuario);
      // Caso o login seja bem-sucedido, redireciona para a página inicial
      navigate(`/home?nome=${nomeUsuario}&email=${emailUsuario}`);
      localStorage.setItem('nome', nome);
      localStorage.setItem('email', email);
    } catch (error) {
      setFeedback("Erro ao fazer login: Verifique suas credenciais.");
      console.error(error);
    } 
  };
    const onRegisterButton = () => {
    navigate("/registro");
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
          </div>
        </form>
      </div>
    </div>
  );
}

export default Login;