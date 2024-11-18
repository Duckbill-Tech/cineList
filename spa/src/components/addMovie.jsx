import { useState } from "react";
import PropTypes from "prop-types";

function AddMovie({ onAddMovieSubmit }) {
  const [title, setTitle] = useState("");
  const [feedback, setFeedback] = useState("");

  const handleAddMovie = () => {
    if (!title.trim()) {
      setFeedback("Por favor, preencha o título do filme.");
      return;
    }

    onAddMovieSubmit(title);
    setTitle("");
    setFeedback("Filme adicionado com sucesso!");
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
  onAddMovieSubmit: PropTypes.func.isRequired,
};

export default AddMovie;
