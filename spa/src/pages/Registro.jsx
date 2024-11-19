import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { registerUser } from "../apis/apiRegistro";

function Registro() {
  const [CPF, setCPF] = useState("");
  const [email, setEmail] = useState("");
  const [senha, setSenha] = useState("");
  const [feedback, setFeedback] = useState("");
  const [erro, setErro] = useState(null);
  const navigate = useNavigate();

  // Função chamada quando o cadastro for realizado
  const handleCadastro = async (event) => {
    event.preventDefault(); // Previne o comportamento padrão do form de recarregar a página

    if (!CPF.trim() || !email.trim() || !senha.trim()) {
      setFeedback("Por favor, preencha todos os campos obrigatórios.");
      return;
    }
    try {
      await registerUser(CPF, email, senha); // Chama a função de API
      setFeedback("Cadastro realizado com sucesso!");
      setCPF("");
      setEmail("");
      setSenha("");
      navigate("/login"); // Redireciona para a página de login após o cadastro
    } catch (error) {
      setErro(error.message);
    }
  };

  return (
    <div className="flex justify-center items-center flex-grow py-10">
      <div className="w-full max-w-md space-y-6 p-8">
        <form onSubmit={(e) => e.preventDefault()} className="space-y-4">
          <input
            id="CPF"
            type="text"
            placeholder="CPF"
            className="w-full px-4 py-3 rounded-md text-black"
            value={CPF}
            onChange={(event) => setCPF(event.target.value)}
            aria-required="true"
          />
          <input
            id="email"
            type="text"
            placeholder="Email"
            className="w-full px-4 py-3 rounded-md text-black"
            value={email}
            onChange={(event) => setEmail(event.target.value)}
            aria-required="true"
            aria-label="Digite seu email"
            autoComplete="email"
          />
          <input
            id="senha"
            type="password"
            placeholder="Senha"
            className="w-full px-4 py-3 rounded-md text-black"
            value={senha}
            onChange={(event) => setSenha(event.target.value)}
            aria-required="true"
            aria-label="Digite sua senha"
            autoComplete="current-password"
          />
          <button
            onClick={handleCadastro}
            className="border border-amber-500 text-amber-500 px-4 py-2 rounded-md font-medium w-full"
            aria-label="Clique para cadastrar"
          >
            Cadastrar!
          </button>
          <div className="text-sm text-white mt-2">{feedback}</div>
        </form>
      </div>
    </div>
  );
}

export default Registro;
