import { ChevronDownIcon, ChevronUpIcon, TrashIcon } from "lucide-react";
import { useState, useEffect } from "react";
import { deleteFilme, getAllFilmes, updateFilme} from "../../service/FilmeService";
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

  const handleSaveComment = async (movie) => {
    try {
      const updatedMovie = await updateFilme(movie.id, movie);
      setMoviesSeen((prevMoviesSeen) =>
        prevMoviesSeen.map((m) => (m.id === updatedMovie.id ? updatedMovie : m))
      );
    } catch (error) {
      console.error("Erro ao salvar comentário:", error);
      alert("Erro ao salvar comentário. Tente novamente.");
    }
  };

  const handleDeleteMovie = async (movieId) => {
    const confirmation = window.confirm(
      "Você tem certeza que deseja excluir este filme?"
    );
    if (!confirmation) return;

    try {
      await deleteFilme(movieId);
      setMoviesSeen((prevMoviesSeen) =>
        prevMoviesSeen.filter((movie) => movie.id !== movieId)
      );
    } catch (error) {
      console.error("Erro ao deletar o filme:", error);
      alert("Erro ao deletar o filme. Tente novamente.");
    }
  };

  const handleClearAllMovies = async () => {
    const confirmation = window.confirm(
      "Você tem certeza que deseja excluir todos os filmes da lista?"
    );
    if (!confirmation) return;

    try {
      await Promise.all(moviesSeen.map((movie) => deleteFilme(movie.id)));
      setMoviesSeen([]);
    } catch (error) {
      console.error("Erro ao limpar a lista de filmes assistidos:", error);
      alert("Erro ao limpar a lista. Tente novamente.");
    }
  };

  return (
    <div
      className="relative flex flex-col rounded-lg bg-amber-400 shadow-lg border border-black p-4"
      role="region"
      aria-label="Filmes assistidos"
    >
      <h1
        className="text-white text-xs text-left mb-2 font-josefin-sans tracking-widest"
        aria-live="polite"
      >
        ASSISTIDOS
      </h1>
      <ul className="flex flex-col gap-2">
        {moviesSeen.map((movie) => (
          <li key={movie.id} className="space-y-2" role="listitem">
            <div className="flex items-center w-full rounded-md p-2 pl-3 bg-white bg-opacity-90 hover:bg-gray-300 hover:bg-opacity-30 transition-all">
              <span className="flex-grow text-black">{movie.titulo}</span>
              <button
                onClick={() =>
                  setOpenForm((prev) => ({
                    ...prev,
                    [movie.id]: !prev[movie.id],
                  }))
                }
                className="rounded-md border border-transparent p-2 text-gray-600 hover:bg-gray-100 focus:bg-gray-100 active:bg-gray-200"
                aria-label={`${
                  openForm[movie.id] ? "Fechar" : "Abrir"
                } formulário para comentários do filme ${movie.titulo}`}
              >
                {openForm[movie.id] ? (
                  <ChevronUpIcon className="w-5 h-5" aria-hidden="true" />
                ) : (
                  <ChevronDownIcon className="w-5 h-5" aria-hidden="true" />
                )}
              </button>
              <button
                onClick={() => handleDeleteMovie(movie.id)}
                className="rounded-md border border-transparent p-2 border-amber-500 text-amber-500 hover:bg-red-100 focus:bg-red-100 active:bg-red-200"
                aria-label={`Remover o filme ${movie.titulo} da lista de assistidos`}
              >
                <TrashIcon className="w-5 h-5" aria-hidden="true" />
              </button>
            </div>
            {openForm[movie.id] && (
              <div
                className="flex flex-col text-black bg-gray-50 p-2 rounded-md mt-2"
                aria-live="polite"
              >
                <label className="text-xs mb-1" htmlFor={`comment-${movie.id}`}>
                  Análise:
                </label>
                {lockedFields[movie.id] ? (
                  <p
                    onClick={() =>
                      setLockedFields((prev) => ({
                        ...prev,
                        [movie.id]: false,
                      }))
                    }
                    className="cursor-pointer p-1 border border-gray-300 rounded-md"
                    aria-label={`Comentário salvo para o filme ${movie.titulo}`}
                  >
                    {movie.descricao || "Clique para adicionar um comentário"}
                  </p>
                ) : (
                <textarea
                  id={`comment-${movie.id}`}
                  placeholder="O que você achou do filme?"
                  value={movie.descricao || ""}
                  onChange={(e) =>
                    setMoviesSeen((prevMoviesSeen) =>
                      prevMoviesSeen.map((m) =>
                        m.id === movie.id ? { ...m, descricao: e.target.value } : m
                      )
                    )
                  }
                  onBlur={() => handleSaveComment(movie)}
                  className="p-2 border border-gray-300 rounded-md resize-none bg-gray-100 text-sm"
                  rows="3"
                />
                )}
                <label className="text-xs mt-2" htmlFor={`rating-${movie.id}`}>
                  Nota:
                </label>
                <ReactStars
                  count={5}
                  value={movie.nota || 0}
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
        onClick={handleClearAllMovies}
        className="mt-4 text-white hover:text-yellow-700 text-sm underline"
        aria-label="Limpar todos os filmes da lista de assistidos"
      >
        Limpar tudo
      </button>
    </div>
  );
}

export default MoviesSeen;
