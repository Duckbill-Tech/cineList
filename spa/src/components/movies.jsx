import { TrashIcon } from "lucide-react";
import PropTypes from "prop-types";

function Movies({
  movies,
  onMovieClick,
  onDeleteMovieClick,
  onClearAllMovies,
}) {
  return (
    <div
      className="border border-amber-500 rounded-md p-4"
      role="region"
      aria-label="Filmes para assistir"
    >
      <h1
        className="text-white text-xs text-left mb-2 font-josefin-sans tracking-widest"
        aria-live="polite"
      >
        PARA ASSISTIR
      </h1>
      <ul className="space-y-4 bg-amber-500 p-2 rounded-md font-josefin-slab text-s">
        {movies.map((movie) => (
          <li key={movie.id} className="flex gap-2">
            <button
              className="w-full text-left flex text-black bg-white p-2 rounded-md"
              onClick={() => onMovieClick(movie.id)}
              aria-label={`Marcar "${movie.title}" como assistido`}
            >
              {movie.title}
            </button>
            <button
              onClick={() => onDeleteMovieClick(movie.id)}
              className="text-black bg-white p-2 rounded-md"
              aria-label={`Remover "${movie.title}" da lista`}
            >
              <TrashIcon aria-hidden="true" />
            </button>
          </li>
        ))}
      </ul>
      <button
        onClick={onClearAllMovies}
        className="hover:text-black text-xs text-amber-500 py-2 font-josefin-sans"
        aria-label="Limpar toda a lista de filmes para assistir"
      >
        Limpar Tudo
      </button>
    </div>
  );
}

// Validação de propriedades
Movies.propTypes = {
  movies: PropTypes.array.isRequired,
  onMovieClick: PropTypes.func.isRequired,
  onDeleteMovieClick: PropTypes.func.isRequired,
  onClearAllMovies: PropTypes.func.isRequired,
};

export default Movies;
