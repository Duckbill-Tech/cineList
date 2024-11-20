import React, { useState, useEffect, useRef } from "react";
import { House, Popcorn, Ellipsis, CircleUser } from "lucide-react";
import { useNavigate, useSearchParams } from "react-router-dom";
import PropTypes from "prop-types";

function NavBar() {
  const navigate = useNavigate();
  const [showMenu, setShowMenu] = useState(false);
  const [searchParams] = useSearchParams();
  const email = searchParams.get("email");
  const menuRef = useRef(null); // Referência para o menu do usuário - TODO: precisamos adicionar aqui uma chamada/rota para o valor do email.

  const handleScrollToSection = (sectionId) => {
    const section = document.getElementById(sectionId);
    if (section) {
      section.scrollIntoView({ behavior: "smooth", block: "start" });
      console.log(`Rolando para a seção: ${sectionId}`);
    } else {
      console.error(`Seção com ID "${sectionId}" não encontrada.`);
    }
  };

  const toggleUserMenu = () => setShowMenu((prev) => !prev);

  const logout = () => navigate("/");

  useEffect(() => {
    const handleClickOutside = (event) => {
      if (menuRef.current && !menuRef.current.contains(event.target)) {
        setShowMenu(false);
      }
    };

    document.addEventListener("mousedown", handleClickOutside);

    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, []);

  return (
    <nav
      className="bg-black p-4 fixed top-0 left-0 w-full shadow-md z-50"
      role="navigation"
      aria-label="Barra de Navegação"
    >
      <div className="flex justify-between items-center max-w-7xl mx-auto">
        <ul className="flex space-x-6 text-white">
          <NavButton
            onClick={() => handleScrollToSection("cinelist")}
            icon={<House aria-hidden="true" />}
            label="Ir para seção principal do CINELIST"
          />
          <NavButton
            onClick={() => handleScrollToSection("listas")}
            icon={<Popcorn aria-hidden="true" />}
            label="Ir para a seção de listas"
          />
          <NavButton
            onClick={() => handleScrollToSection("sobreNos")}
            icon={<Ellipsis aria-hidden="true" />}
            label="Ir para a seção sobre nós"
          />
        </ul>

        <div className="flex items-center space-x-2 relative" ref={menuRef}>
          <p
            className="text-white truncate max-w-[150px] overflow-hidden text-ellipsis"
            title={email || "Usuário"}
          >
            {email || "Usuário"}
          </p>
          <button
            onClick={toggleUserMenu}
            className="text-white hover:text-amber-400"
            aria-label="Menu do usuário"
            aria-expanded={showMenu}
            aria-haspopup="true"
          >
            <CircleUser aria-hidden="true" />
          </button>

          {showMenu && (
            <div
              className="absolute right-0 mt-2 bg-black text-white border border-gray-700 rounded shadow-md z-50"
              role="menu"
              aria-label="Opções do menu do usuário"
            >
              <ul className="flex flex-col">
                <li>
                  <button
                    onClick={logout}
                    className="block px-4 py-2 hover:bg-gray-800 text-left w-full"
                    role="menuitem"
                    aria-label="Sair e voltar à página de login"
                  >
                    Sair
                  </button>
                </li>
              </ul>
            </div>
          )}
        </div>
      </div>
    </nav>
  );
}

function NavButton({ onClick, icon, label }) {
  return (
    <li>
      <button
        onClick={onClick}
        className="hover:text-amber-400"
        aria-label={label}
      >
        {icon}
      </button>
    </li>
  );
}

NavButton.propTypes = {
  onClick: PropTypes.func.isRequired,
  icon: PropTypes.node.isRequired,
  label: PropTypes.string.isRequired,
};

export default NavBar;