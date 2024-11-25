import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

export default defineConfig({
  base: "/static/", 
  plugins: [react()],
  build: {
    outDir:  "../src/main/resources/static",
    emptyOutDir: true,
  },
});