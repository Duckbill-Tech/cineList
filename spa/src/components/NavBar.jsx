import React, { useState, useEffect, useRef } from "react";
import { House, Popcorn, Ellipsis } from "lucide-react";
import { useNavigate, useSearchParams } from "react-router-dom";

function NavBar() {
  const navigate = useNavigate();
  const [showDropdown, setShowDropdown] = useState(false);
  const [searchParams] = useSearchParams();
  const email = searchParams.get("email") || "Usuário";
  const dropdownRef = useRef(null);
  const nome = searchParams.get("nome");

  const handleScrollToSection = (sectionId) => {
    const section = document.getElementById(sectionId);
    if (section) {
      section.scrollIntoView({ behavior: "smooth", block: "start" });
    } else {
      console.error(`Seção com ID "${sectionId}" não encontrada.`);
    }
  };

  const logout = () => navigate("/");

  useEffect(() => {
    const handleClickOutside = (event) => {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
        setShowDropdown(false);
      }
    };

    document.addEventListener("mousedown", handleClickOutside);
    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, []);

  return (
    <nav className="bg-black p-4 fixed top-0 left-0 w-full shadow-md z-50">
      <div className="flex justify-between items-center max-w-7xl mx-auto">
        <ul className="flex space-x-6 text-white">
          <NavButton
            onClick={() => handleScrollToSection("cinelist")}
            label="Seção Principal"
            icon={<House />}
          />
          <NavButton
            onClick={() => handleScrollToSection("listas")}
            label="Listas"
            icon={<Popcorn />}
          />
          <NavButton
            onClick={() => handleScrollToSection("sobreNos")}
            label="Sobre Nós"
            icon={<Ellipsis />}
          />
        </ul>

        <div className="relative" ref={dropdownRef}>
          <button
            onClick={() => setShowDropdown((prev) => !prev)}
            className="flex items-center space-x-2 text-white hover:text-amber-400"
            aria-expanded={showDropdown}
            aria-haspopup="true"
          >
            <span className="truncate max-w-[150px]" title={email}>
              {nome}
            </span>
            <svg
              className="w-5 h-5"
              xmlns="http://www.w3.org/2000/svg"
              viewBox="0 0 20 20"
              fill="currentColor"
            >
              <path
                fillRule="evenodd"
                d="M5.23 7.21a.75.75 0 011.06.02L10 11.168l3.71-3.938a.75.75 0 111.08 1.04l-4.25 4.5a.75.75 0 01-1.08 0l-4.25-4.5a.75.75 0 01.02-1.06z"
                clipRule="evenodd"
              />
            </svg>
          </button>

          {showDropdown && (
            <div
              className="absolute right-0 mt-2 bg-black text-white border border-gray-700 rounded shadow-md w-48"
              role="menu"
            >
              <div className="px-4 py-2 border-b border-gray-600 text-sm">
                {email}
              </div>
              <button
                onClick={logout}
                className="block w-full text-left px-4 py-2 text-sm hover:bg-gray-800"
              >
                Sair
              </button>
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

export default NavBar;