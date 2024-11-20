import React from "react";
import { Github, Linkedin } from "lucide-react";

function Nathalie() {
  return (
    <nav
      className="bg-black text-white text-left p-4 rounded-md"
      role="navigation"
      aria-label="Perfil de Nathalie"
    >
      <ul>
        <li>
          <h1
            className="text-lg font-medium tracking-widest"
            tabIndex="0"
            aria-label="Nathalie T"
          >
            Nathalie T
          </h1>
          <p
            className="text-xs mt-1 mb-2"
            aria-label="Graduanda em Ciência da Computação pela UAM"
          >
            Graduanda em Ciência da Computação - UAM
          </p>
          <div
            className="flex gap-2"
            role="navigation"
            aria-label="Links sociais"
          >
            <a
              href="https://github.com/nathalietaylor"
              target="_blank"
              rel="noopener noreferrer"
              className="hover:text-amber-400"
              aria-label="Acesse o GitHub de Nathalie"
            >
              <Github aria-hidden="true" />
            </a>
            <a
              href="https://www.linkedin.com/in/nathalie-m-taylor-zampieri-680252171/"
              target="_blank"
              rel="noopener noreferrer"
              className="hover:text-amber-400"
              aria-label="Acesse o LinkedIn de Nathalie"
            >
              <Linkedin aria-hidden="true" />
            </a>
          </div>
        </li>
      </ul>
    </nav>
  );
}

export default Nathalie;