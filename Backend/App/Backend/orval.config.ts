import { defineConfig } from "orval";

export default defineConfig({
  myApi: {
    input: { target: "http://localhost:8080/q/openapi" },
    output: {
      target: "src/api/generated",
      schemas: "src/api/generated/model",
      client: "fetch",
      mode: "split",
    },
  },
});