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

  return (
    <div className="flex flex-col gap-2 max-w-4xl mx-auto p-6">
      <label htmlFor="movieTitle" className="sr-only">
        Título do Filme
      </label>
      <div className="flex gap-2 w-full">
        <input
          id="movieTitle"
          type="text"
          placeholder="Digite o nome do filme"
          className="flex-grow px-5 py-2.5 text-sm rounded-md text-blacktext-sm me-2 mb-2 text-black"
          value={titulo}
          onChange={(event) => setTitulo(event.target.value)}
          onKeyDown={(event) => {
            if (event.key === "Enter") {
              handleAddMovie(event);
            }
          }}
          aria-required="true"
          aria-label="Digite o nome do filme"
        />
        <button
          onClick={handleAddMovie}
          type="button"
          className="text-yellow-400 hover:text-white border border-yellow-400 hover:bg-yellow-500 focus:ring-4 focus:outline-none focus:ring-yellow-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center me-2 mb-2 dark:border-yellow-300 dark:text-yellow-300 dark:hover:text-white dark:hover:bg-yellow-400 dark:focus:ring-yellow-900"
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

export default AddMovie;
