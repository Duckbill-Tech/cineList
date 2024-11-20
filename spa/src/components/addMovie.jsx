import { useState } from "react";
import PropTypes from "prop-types";

function AddMovie({ createFilme }) {
  const [title, setTitle] = useState("");
  const [feedback, setFeedback] = useState("");

  const handleAddMovie = async () => {
    if (!title.trim()) {
      setFeedback("Por favor, preencha o título do filme.");
      return;
    }

    try {
      // Substitua '12345' pelo ID real do usuário ou torne dinâmico
      const usuarioId = "12345";
      const filmeDTO = { titulo: title }; // Ajuste conforme o esperado pelo backend

      await createFilme(filmeDTO, usuarioId);
      setTitle("");
      setFeedback("Filme adicionado com sucesso!");
    } catch (error) {
      console.error("Erro ao adicionar filme:", error);
      setFeedback("Erro ao adicionar filme. Tente novamente.");
    }
  };

  return (
    <div className="flex flex-col gap-4 max-w-4xl mx-auto p-6">
      <label htmlFor="movieTitle" className="sr-only">
        Título do Filme
      </label>
      <div className="flex gap-4 w-full">
        <input
          id="movieTitle"
          type="text"
          placeholder="Digite o título do filme"
          className="flex-grow px-4 py-2 rounded-md text-black"
          value={title}
          onChange={(event) => setTitle(event.target.value)}
          aria-required="true"
          aria-label="Digite o título do filme"
        />
        <button
          onClick={handleAddMovie}
          className="border border-amber-500 text-amber-500 px-4 py-2 rounded-md font-medium"
          aria-label="Adicionar filme à lista"
        >
          Adicionar
        </button>
      </div>

      <div
        aria-live="polite"
        className="text-sm text-black mt-2"
        aria-label="Mensagem de feedback"
      >
        {feedback}
      </div>
    </div>
  );
}

AddMovie.propTypes = {
  createFilme: PropTypes.func.isRequired,
};

export default AddMovie;