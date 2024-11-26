import React, { useEffect } from "react";
import { Routes, Route } from "react-router-dom";
import Login from "./pages/Login";
import Home from "./pages/Home";
import Registro from "./pages/Registro";
import Layout from "./components/Layout";
import "./index.css";

// Função chamada quando o login é realizado
const onLoginSubmit = (email) => {
  console.log("Login com:", email);
};

function App() {
  useEffect(() => {
    document.body.classList.add("bg-black", "text-white");
    return () => {
      document.body.classList.remove("bg-black", "text-white");
    };
  }, []);

  return (
    <div
      className="w-screen h-screen flex flex-col font-josefin-sans"
      role="main"
    >
      <Routes>
        {/* Rota para a página inicial */}
        <Route
          path="/"
          element={
            <Layout>
              <Login onLoginSubmit={onLoginSubmit} />
            </Layout>
          }
        />

        {/* Rota para a página de home */}
        <Route
          path="/home"
          element={
            <Layout>
              <Home />
            </Layout>
          }
        />

        {/* Rota para a página de login */}
        <Route
          path="/login"
          element={
            <Layout>
              <Login onLoginSubmit={onLoginSubmit} />
            </Layout>
          }
        />

        {/* Rota para a página de registro */}
        <Route
          path="/registro"
          element={
            <Layout>
              <Registro />
            </Layout>
          }
        />
        {/* Rota para página não encontrada */}
        <Route
          path="*"
          element={
            <Layout>
              <div className="text-center text-white p-10">
                <h2>404 - Página Não Encontrada</h2>
                <p>
                  A página que você procura não existe. Verifique o URL ou vá
                  para a página inicial.
                </p>
              </div>
            </Layout>
          }
        />
      </Routes>
    </div>
  );
}

export default App;