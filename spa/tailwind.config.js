/** @type {import('tailwindcss').Config} */
export default {
  content: ["./index.html", "./src/**/*.{html,js,jsx,tsx}"],
  theme: {
    extend: {
      backgroundImage: {
        // Usando o alias que configuramos no Vite
        background: "url('@assets/background.svg')",
      },
      fontFamily: {
        "josefin-sans": ["Josefin Sans", "sans-serif"],
        "josefin-slab": ["Josefin Slab", "serif"],
      },
      letterSpacing: {
        widest: "0.5em",
      },
    },
  },
  plugins: [],
};
