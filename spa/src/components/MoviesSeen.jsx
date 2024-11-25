import {
  ChevronDownIcon,
  ChevronUpIcon,
  TrashIcon,
} from "lucide-react";
import { useState, useEffect } from "react";
import { deleteFilme, updateFilme, getAllFilmes } from "../../service/FilmeService";
import ReactStars from "react-stars";
function MoviesSeen({ moviesSeen, setMoviesSeen }) {
  const [openForm, setOpenForm] = useState({});
  const [lockedFields, setLockedFields] = useState({});

  useEffect(() => {
    async function fetchMovies() {
      try {
        const filmes = await getAllFilmes();
        const assistidos = filmes.filter((filme) => filme.completedAt);
        setMoviesSeen(assistidos);
      } catch (error) {
        console.error("Erro ao carregar filmes:", error);
      }
    }

    fetchMovies();
  }, [setMoviesSeen]);

  const handleRatingChange = async (id, rating) => {
    try {
      const movieToUpdate = moviesSeen.find((movie) => movie.id === id);
      const updates = { ...movieToUpdate, nota: rating };

      const updatedMovie = await updateFilme(id, updates);

      setMoviesSeen((prevMoviesSeen) =>
        prevMoviesSeen.map((movie) =>
          movie.id === id ? updatedMovie : movie
        )
      );
    } catch (error) {
      console.error("Erro ao salvar a nota:", error);
      alert("Erro ao salvar a avaliação. Tente novamente.");
    }
  };

  const handleCommentChange = (id, comment) => {
    setMoviesSeen((prevMoviesSeen) =>
      prevMoviesSeen.map((movie) =>
        movie.id === id ? { ...movie, descricao: comment } : movie
      )
    );
  };

  const handleCommentKeyPress = (id, e) => {
    if (e.key === "Enter") {
      e.preventDefault();
      saveMovieUpdates(id);
    }
  };

  const saveMovieUpdates = async (movieId) => {
    const movieToUpdate = moviesSeen.find((movie) => movie.id === movieId);
  
    try {
      const updatedMovie = await updateFilme(movieId, movieToUpdate);

      setMoviesSeen((prevMoviesSeen) =>
        prevMoviesSeen.map((movie) =>
          movie.id === movieId ? updatedMovie : movie
        )
      );
  
      console.log("Comentário salvo com sucesso:", updatedMovie);
    } catch (error) {
      console.error("Erro ao salvar o comentário:", error);
      alert("Erro ao salvar o comentário. Tente novamente.");
    }
  };
  
  const toggleForm = (id) => {
    setOpenForm((prevOpenForm) => ({
      ...prevOpenForm,
      [id]: !prevOpenForm[id],
    }));
  };

  const enableCommentEditing = (id) => {
    setLockedFields((prev) => ({ ...prev, [id]: false }));
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
      await Promise.all(moviesSeen.map((movie) => deleteFilme(movie.id)));
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
                {lockedFields[movie.id] ? (
                  <p
                    onClick={() => enableCommentEditing(movie.id)}
                    className="cursor-pointer p-1 border border-gray-300 rounded-md"
                    aria-label={`Comentário salvo para o filme ${movie.titulo}`}
                  >
                    {movie.descricao || "Clique para adicionar um comentário"}
                  </p>
                ) : (
                  <textarea
                    id={`comment-${movie.id}`}
                    placeholder="Deixe um comentário"
                    value={movie.descricao || ""}
                    onChange={(e) =>
                      handleCommentChange(movie.id, e.target.value)
                    }
                    onKeyDown={(e) => handleCommentKeyPress(movie.id, e)}
                    className="p-2 border border-neutral-200 rounded-md resize-none"
                    aria-label={`Campo de comentário para o filme ${movie.titulo}`}
                    rows="3"
                  />
                )}
                <label className="text-xs mt-2" htmlFor={`rating-${movie.id}`}>
                  Nota:
                </label>
                <ReactStars
                  count={5}
                  value={movie.nota || 0}
                  onChange={(rating) => handleRatingChange(movie.id, rating)}
                  size={24}
                  color2={"#ffd700"}
                  half={true}
                />
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