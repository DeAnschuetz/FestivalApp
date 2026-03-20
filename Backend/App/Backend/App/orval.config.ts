import { defineConfig } from "orval";

export default defineConfig({
    myApi: {
        input: { target: "src/main/resources/openapi.yaml" },
        output: {
            target: "src/main/java/com/ffb/app/api/frontend/generated/ffbAPI.ts",
            client: "fetch",
            mode: "split"
        },
    },
});