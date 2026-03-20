import type {
  AccountType,
  CreditHistoryResponse,
  FoodCourtResponse,
  FoodOrderNotificationResponse,
  FoodOrderResponse,
  FoodOrderResponseHistory,
  GetCart200,
  GetCredit200,
  ProductResponse,
  Uuid,
} from "../generated/ffbAPI.schemas";

export type ImportedProductAssignment = {
  id: string;
  mainProductId: Uuid;
  subProductId: Uuid;
};

export type ImportedAccountData = {
  accountId: Uuid;
  loginNr: string;
  type: AccountType;
  ownFoodCourtId?: Uuid;
  cart?: GetCart200 | null;
  credit?: GetCredit200 | null;
  creditHistory?: CreditHistoryResponse[];
  visibleOrders: FoodOrderResponse[];
  visibleOrderHistory: FoodOrderResponseHistory[];
  notifications: FoodOrderNotificationResponse[];
};

export type InitialImportData = {
  version: 1;
  generatedAt: string;
  source: string;
  global: {
    foodCourts: FoodCourtResponse[];
    products: ProductResponse[];
    productAssignments: ImportedProductAssignment[];
    productFoodCourtMap: Record<string, Uuid>;
    foodCourtImages: Record<string, string>;
  };
  accounts: ImportedAccountData[];
};
