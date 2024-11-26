import React from "react";

function Layout({ children }) {
  return (
    <div className="w-screen h-screen flex flex-col font-josefin-sans bg-black">
      {/* Imagem Fixa */}
      <div className="bg-black flex justify-center items-center py-4 mt-0">
        <img
          src="./assets/CINELIST.jpg"
          alt="CineList Logo"
          className="w-[800px] h-auto"
        />
      </div>

      {/* Conteúdo Dinâmico */}
      <main className="flex-grow">{children}</main>

      {/* Footer */}
      <footer className="flex justify-center items-center text-center text-xs text-white p-4 bg-black">
        <p>
          Desenvolvido com amor por Katerine Witkoski e Nathalie Taylor - 2024
        </p>
      </footer>
    </div>
  );
}

export default Layout;