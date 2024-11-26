import { useState } from "react";

function AddMovie({ onAddMovieSubmit }) {
  const [titulo, setTitulo] = useState("");
  const [feedback, setFeedback] = useState("");

  const handleAddMovie = async (event) => {
    event.preventDefault();

    if (!titulo.trim()) {
      setFeedback("Por favor, preencha o título do filme.");
      return;
    }

    try {
      await onAddMovieSubmit(titulo);
      setTitulo("");
      setFeedback("Filme adicionado com sucesso!");
    } catch (error) {
      console.error("Erro ao adicionar filme:", error);
      setFeedback("Erro ao adicionar filme. Tente novamente.");
    }
  };

  const handleInputChange = (event) => {
    setTitulo(event.target.value);
    if (feedback) {
      setFeedback("");
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
          value={titulo}
          onChange={handleInputChange}
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

      {feedback && (
        <div
          aria-live="polite"
          className={`text-sm mt-2 ${
            feedback.includes("Erro") ? "text-red-500" : "text-green-500"
          }`}
          aria-label="Mensagem de feedback"
        >
          {feedback}
        </div>
      )}
    </div>
  );
}

export default AddMovie;