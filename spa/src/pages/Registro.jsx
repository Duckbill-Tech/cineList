import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { register } from "../../service/AuthService";

function Registro() {
  const [CPF, setCPF] = useState("");
  const [nome, setNome] = useState("");
  const [email, setEmail] = useState("");
  const [senha, setSenha] = useState("");
  const [feedback, setFeedback] = useState("");
  const navigate = useNavigate();

  // Função chamada quando o cadastro for realizado
  const handleCadastro = async (event) => {
    event.preventDefault(); 

    if (!CPF.trim() || !nome.trim() || !email.trim() || !senha.trim()) {
      setFeedback("Por favor, preencha todos os campos obrigatórios.");
      return;
    }

    try {
      // Chamada à API de registro
      const response = await register(nome, email, CPF, senha);
      console.log("Cadastro realizado com sucesso:", response);

      setFeedback("Cadastro realizado com sucesso!");
      setCPF("");
      setNome("");
      setEmail("");
      setSenha("");

      setTimeout(() => navigate("/login"), 2000);
    } catch (error) {
      console.error("Erro ao realizar cadastro:", error);
      setFeedback("Erro ao realizar cadastro. Verifique os dados e tente novamente.");
    }
  };

  return (
    <div className="flex justify-center items-center flex-grow py-10">
      <div className="w-full max-w-md space-y-6 p-8">
        <form onSubmit={handleCadastro} className="space-y-4">
          <input
            id="nome"
            type="text"
            placeholder="Nome"
            className="w-full px-4 py-3 rounded-md text-black"
            value={nome}
            onChange={(event) => setNome(event.target.value)}
            aria-required="true"
          />
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
            type="email"
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
            type="submit"
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