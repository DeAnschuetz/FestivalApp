import { defineConfig } from "orval";

export default defineConfig({
    myApi: {
        input: { target: "http://localhost:8080/q/openapi" },
        output: {
            target: "src/main/java/com/ffb/app/api/frontend/generated/ffbAPI.ts",
            client: "fetch",
            mode: "split"
        },
    },
});