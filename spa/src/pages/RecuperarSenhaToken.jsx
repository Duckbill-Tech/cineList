import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { resetPassword } from "../../service/AuthService";
 // TODO:
function ResetPassword() {
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [token, setToken] = useState("");
  const [feedback, setFeedback] = useState("");
  const navigate = useNavigate();

  // Função para redefinir a senha
  const handleResetPassword = async (event) => {
    event.preventDefault();

    if (!newPassword.trim() || !confirmPassword.trim() || !token.trim()) {
      setFeedback("Por favor, preencha todos os campos.");
      return;
    }

    if (newPassword !== confirmPassword) {
      setFeedback("As senhas não coincidem.");
      return;
    }

    try {
      // Chama a função de redefinição de senha do AuthService
      await resetPassword(token, newPassword);
      setFeedback("Senha redefinida com sucesso! Redirecionando para o login...");

      // Redireciona para a página de login após 3 segundos
      setTimeout(() => navigate("/login"), 3000);
    } catch (error) {
      // Define uma mensagem de erro no feedback
      setFeedback("Erro ao redefinir a senha. Por favor, tente novamente.");
      console.error(error);
    }
  };

  return (
    <div className="w-full bg-black flex justify-center items-center flex-grow py-10">
      <div className="w-full max-w-md space-y-6 p-8">
        {/* Formulário de Redefinição de Senha */}
        <form onSubmit={handleResetPassword} className="space-y-4">
          {/* Campo para o Token */}
          <label htmlFor="token" className="sr-only">
            Token
          </label>
          <input
            id="token"
            type="text"
            placeholder="Cole aqui o token"
            className="w-full px-4 py-3 rounded-md text-black"
            value={token}
            onChange={(event) => setToken(event.target.value)}
            aria-required="true"
            aria-label="Cole seu token aqui"
          />

          {/* Campo de Nova Senha */}
          <label htmlFor="newPassword" className="sr-only">
            Nova Senha
          </label>
          <input
            id="newPassword"
            type="password"
            placeholder="Digite sua nova senha"
            className="w-full px-4 py-3 rounded-md text-black"
            value={newPassword}
            onChange={(event) => setNewPassword(event.target.value)}
            aria-required="true"
            aria-label="Digite sua nova senha"
          />

          {/* Campo de Confirmação de Senha */}
          <label htmlFor="confirmPassword" className="sr-only">
            Confirme a Senha
          </label>
          <input
            id="confirmPassword"
            type="password"
            placeholder="Confirme sua nova senha"
            className="w-full px-4 py-3 rounded-md text-black"
            value={confirmPassword}
            onChange={(event) => setConfirmPassword(event.target.value)}
            aria-required="true"
            aria-label="Confirme sua nova senha"
          />

          {/* Botão de Submissão */}
          <button
            type="submit"
            className="border border-amber-500 text-amber-500 px-4 py-2 rounded-md font-medium w-full"
            aria-label="Redefinir senha"
          >
            Redefinir Senha
          </button>

          {/* Mensagem de feedback */}
          {feedback && (
            <div className="text-sm text-red-500 mt-2" aria-live="polite">
              {feedback}
            </div>
          )}
        </form>
      </div>
    </div>
  );
}

export default ResetPassword;