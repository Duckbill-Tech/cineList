const BASE_URL = "http://localhost:5173/recuperar-senha";

/**
 * Envia uma solicitação de recuperação de senha.
 * @param {string} email - Email do usuário.
 * @returns {Promise<object>} - Resposta do servidor ou erro.
 */

export async function recuperarSenha(email) {
  try {
    const response = await fetch(`${BASE_URL}/recuperar-senha`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ email }),
    });

    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message || "Erro ao recuperar senha.");
    }

    const data = await response.json();
    return data;
  } catch (error) {
    console.error("Erro ao enviar recuperação de senha:", error);
    throw error;
  }
}
