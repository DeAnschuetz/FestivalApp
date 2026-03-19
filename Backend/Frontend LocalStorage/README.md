# FFB offline-first TypeScript client

This package adds a small **API-first with local fallback** layer on top of your generated `ffbAPI.ts` client.

## Important note for React

A normal React frontend cannot write real JSON files to the user's disk without extra desktop tooling or a backend.
Because of that, this solution uses **`localStorage` as JSON-file-like storage**:

- JSON data is stored as serialized strings
- images are stored as **data URLs** in local storage
- everything is safe to use in a browser-only React app
- when the API is reachable, the local cache is refreshed automatically

That matches your requirement closely while still working in a normal React app.

## Covered domains

Only endpoints available to **Guest** and **FoodCourtWorker** are wrapped:

- account
- cart
- credit
- food court
- food order
- notification
- product

Ignored on purpose:

- admin-only endpoints
- master endpoints

## Folder structure

```text
src/
  api/
    generated/
      ffbAPI.ts
      ffbAPI.schemas.ts
    ffb/
      core/
        api.ts
        errors.ts
        imageStorage.ts
        keys.ts
        storage.ts
      seed/
        initialImport.json
      accountApi.ts
      cartApi.ts
      creditApi.ts
      foodCourtApi.ts
      foodOrderApi.ts
      initialImport.ts
      import.types.ts
      notificationApi.ts
      productApi.ts
      index.ts
```

## Usage

```ts
import {
  login,
  getCart,
  addCartItem,
  getAllFoodCourts,
  getFoodCourtImageUrl,
  getAllProducts,
} from "./api/ffb";

// online login
await login({ loginNr: "1234", password: "secret" });

// online first, cached fallback when API is unavailable
const cart = await getCart();
const products = await getAllProducts();
const courts = await getAllFoodCourts();

// image is returned as a data URL string for direct use in React
const imageUrl = await getFoodCourtImageUrl("food-court-id");
```

## Initial import from your database dump

The package now also contains a ready-made `initialImport.json` generated from your SQL dump and mapper.

```ts
import {
  importInitialData,
  applyImportedAccount,
  getAllFoodCourts,
  getVisibleOrders,
} from "./api/ffb";

// Option A: import the JSON through your bundler
import initialImport from "./api/ffb/seed/initialImport.json";

importInitialData(initialImport);

// load the cached view for one login number
applyImportedAccount("V-000-000-001");

const courts = await getAllFoodCourts();
const orders = await getVisibleOrders();
```

If your setup does not support direct JSON imports, you can also load the file as text and pass the raw JSON string into `importInitialData(rawJson)`.

## Notes / assumptions

- The generated API uses `fetch`, so the wrapper keeps using it.
- The wrapper stores the auth token from `/account/login` and automatically sends `Authorization: Bearer ...`.
- Offline mutations update the local cache immediately.
- The initial import stores global data plus account-specific views for Guest and Food Court Worker accounts.
- Product ownership by food court is now cached explicitly so offline filtering by food court works after the import.
- `notification` has a spec/code mismatch in the uploaded OpenAPI/generated client:
  the endpoint is typed as `FoodOrderResponse[]`, although the schema also defines `FoodOrderNotificationResponse`.
  The wrapper normalizes this as best as possible.
- Image caching uses `data:` URLs because that is the simplest browser-safe format.
- The database dump only contains **password hashes**, not plaintext passwords.
  Because of that, the initial import can preload data, but it cannot reconstruct fully working offline password login on its own.
- `localStorage` has size limits. For many large images, IndexedDB would be better. For your "not production" case this is usually fine.
