import React from "react";
import { Github, Linkedin } from "lucide-react";

function Katerine() {
  return (
    <nav
      className="bg-black text-white text-left p-4 rounded-md"
      role="navigation"
      aria-label="Perfil de Katerine Witkoski"
    >
      <ul>
        <li>
          <h1
            className="text-lg font-medium tracking-widest"
            tabIndex={0}
            aria-label="Nome: Katerine W"
          >
            Katerine W
          </h1>
          <p
            className="text-xs mt-1 mb-2"
            aria-label="Descrição: Graduanda em Ciência da Computação pela UAM"
          >
            Graduanda em Ciência da Computação - UAM
          </p>
          <div
            className="flex gap-2"
            role="navigation"
            aria-label="Links sociais"
          >
            <a
              href="https://github.com/katerine-dev"
              target="_blank"
              rel="noopener noreferrer"
              className="hover:text-amber-400"
              aria-label="Acessar perfil no GitHub de Katerine"
            >
              <Github aria-hidden="true" />
            </a>
            <a
              href="https://www.linkedin.com/in/katerinewitkoski/"
              target="_blank"
              rel="noopener noreferrer"
              className="hover:text-amber-400"
              aria-label="Acessar perfil no LinkedIn de Katerine"
            >
              <Linkedin aria-hidden="true" />
            </a>
          </div>
        </li>
      </ul>
    </nav>
  );
}

export default Katerine;