/* eslint-disable react/prop-types */
import {
  ChevronDownIcon,
  ChevronUpIcon,
  TrashIcon,
  StarIcon,
} from "lucide-react";
import { useState } from "react";
import { deleteFilme, updateFilme } from "../../service/FilmeService";

function MoviesSeen({ moviesSeen, setMoviesSeen }) {
  const [movieComments, setMovieComments] = useState({});
  const [openForm, setOpenForm] = useState({});
  const [lockedFields, setLockedFields] = useState({});

  const handleCommentChange = (id, comment) => {
    setMovieComments((prevComments) => ({
      ...prevComments,
      [id]: { ...prevComments[id], comment },
    }));
  };

  const handleRatingChange = (id, rating) => {
    setMovieComments((prevComments) => ({
      ...prevComments,
      [id]: { ...prevComments[id], rating },
    }));
  };

  const saveMovieUpdates = async (movieId) => {
    const comment = movieComments[movieId]?.comment || "";
    const rating = movieComments[movieId]?.rating || 0;

    try {
      const movieToUpdate = moviesSeen.find((movie) => movie.id === movieId);
      const updates = {
        ...movieToUpdate,
        descricao: comment,
        nota: rating,
      };

      const updatedMovie = await updateFilme(movieId, updates);

      // Atualiza o estado com o filme atualizado
      setMoviesSeen((prevMoviesSeen) =>
        prevMoviesSeen.map((movie) =>
          movie.id === movieId ? updatedMovie : movie
        )
      );

      console.log("Filme atualizado com sucesso:", updatedMovie);
    } catch (error) {
      console.error("Erro ao salvar as alterações do filme:", error);
      alert("Erro ao salvar as alterações. Tente novamente.");
    }
  };

  const handleCommentKeyPress = (id, e) => {
    if (e.key === "Enter") {
      setLockedFields((prev) => ({ ...prev, [id]: true }));
      saveMovieUpdates(id); // Salva as alterações ao pressionar Enter
    }
  };

  const toggleForm = (id) => {
    setOpenForm((prevOpenForm) => ({
      ...prevOpenForm,
      [id]: !prevOpenForm[id],
    }));
  };

  const onDeleteMovieSeenClick = async (movieId) => {
    const confirmation = window.confirm("Tem certeza que deseja remover este filme?");
    if (!confirmation) return;

    try {
      await deleteFilme(movieId);
      setMoviesSeen((prevMoviesSeen) =>
        prevMoviesSeen.filter((movie) => movie.id !== movieId)
      );
      console.log(`Filme com ID ${movieId} foi removido.`);
    } catch (error) {
      console.error("Erro ao deletar o filme assistido:", error);
      alert("Não foi possível deletar o filme assistido. Tente novamente.");
    }
  };

  const onClearAllMoviesSeen = async () => {
    const confirmation = window.confirm(
      "Tem certeza que deseja remover todos os filmes assistidos?"
    );
    if (!confirmation) return;

    try {
      // Envia requisições para deletar todos os filmes no backend
      await Promise.all(moviesSeen.map((movie) => deleteFilme(movie.id)));

      // Limpa o estado local
      setMoviesSeen([]);
    } catch (error) {
      console.error("Erro ao limpar a lista de filmes assistidos:", error);
      alert("Não foi possível limpar a lista de filmes assistidos. Tente novamente.");
    }
  };

  return (
    <div
      className="border border-amber-500 rounded-md p-4"
      role="region"
      aria-label="Filmes assistidos"
    >
      <h1 className="text-white text-xs text-left mb-2 font-josefin-sans tracking-widest">
        ASSISTIDOS
      </h1>
      <ul className="space-y-4 bg-amber-500 p-2 rounded-md">
        {moviesSeen.map((movie) => (
          <li key={movie.id} className="space-y-2" role="listitem">
            <div className="flex gap-2 items-center">
              <button
                className="w-full text-left text-black bg-white p-2 rounded-md font-josefin-slab text-s"
                aria-label={`Título do filme: ${movie.titulo}`}
              >
                {movie.titulo}
              </button>
              <button
                onClick={() => toggleForm(movie.id)}
                className="text-black bg-white p-2 rounded-md"
                aria-label={`${
                  openForm[movie.id] ? "Fechar" : "Abrir"
                } formulário para comentários do filme ${movie.titulo}`}
              >
                {openForm[movie.id] ? <ChevronUpIcon /> : <ChevronDownIcon />}
              </button>
              <button
                onClick={() => onDeleteMovieSeenClick(movie.id)}
                className="text-black bg-white p-2 rounded-md"
                aria-label={`Remover o filme ${movie.titulo} da lista de assistidos`}
              >
                <TrashIcon aria-hidden="true" />
              </button>
            </div>

            {openForm[movie.id] && (
              <div
                className="flex flex-col text-black bg-white p-2 rounded-md"
                aria-live="polite"
              >
                <label className="text-xs" htmlFor={`comment-${movie.id}`}>
                  Comentário:
                </label>
                <input
                  id={`comment-${movie.id}`}
                  type="text"
                  placeholder="Deixe um comentário"
                  value={movieComments[movie.id]?.comment || ""}
                  onChange={(e) =>
                    handleCommentChange(movie.id, e.target.value)
                  }
                  onKeyDown={(e) => handleCommentKeyPress(movie.id, e)}
                  disabled={lockedFields[movie.id]}
                  className="p-1 border border-gray-300 rounded-md"
                  aria-label={`Campo de comentário para o filme ${movie.titulo}`}
                />
                <label className="text-xs mt-2" htmlFor={`rating-${movie.id}`}>
                  Nota:
                </label>
                <div
                  id={`rating-${movie.id}`}
                  className="flex items-center space-x-1"
                  role="radiogroup"
                  aria-label={`Avaliação do filme ${movie.titulo}`}
                >
                  {[1, 2, 3, 4, 5].map((star) => (
                    <StarIcon
                      key={star}
                      onClick={() => handleRatingChange(movie.id, star)}
                      className={`cursor-pointer ${
                        star <= (movieComments[movie.id]?.rating || 0)
                          ? "text-yellow-500"
                          : "text-gray-300"
                      }`}
                      role="radio"
                      aria-checked={
                        star === (movieComments[movie.id]?.rating || 0)
                      }
                      aria-label={`${star} estrelas`}
                    />
                  ))}
                </div>
              </div>
            )}
          </li>
        ))}
      </ul>
      <button
        onClick={onClearAllMoviesSeen}
        className="hover:text-black text-xs text-amber-500 py-2"
        aria-label="Limpar todos os filmes da lista de assistidos"
      >
        Limpar Tudo
      </button>
    </div>
  );
}

export default MoviesSeen;