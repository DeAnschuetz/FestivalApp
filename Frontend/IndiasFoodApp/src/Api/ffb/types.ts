
import type {
  AccountType,
  FoodOrderHistoryResponse,
  FoodOrderItemResponse,
  FoodOrderNotificationResponse,
  FoodOrderResponse,
  FoodOrderResponseHistory,
  FoodOrderStatus,
  ProductResponse,
  Uuid,
  FoodCourtResponse,
  NotificationStatus,
  CreditHistoryResponse,
  CartResponse,
  AccountRegisterResponse,
  AccountLoginResponse,
  CreditResponse,
} from "../generated/ffbAPI.schemas";

export type LoginResult = {
  loginNr: string;
  token: string;
};

export type RegisterResult = {
  id: Uuid;
  loginNr: string;
  type: AccountType;
};

export type CartItem = {
  id: Uuid;
  displayName: string;
  symbolIdentifier: string;
  price: number;
  count: number;
  extra: string;
  subItems: CartItem[];
};

export type Cart = {
  hasPrio: boolean;
  total: number;
  cartItems: CartItem[];
};

export type Credit = {
  credit: number;
};

export type FoodCourt = {
  id: Uuid;
  name: string;
  waitingTime: number;
};

export type OrderHistoryEntry = {
  oldStatus?: FoodOrderStatus;
  newStatus: FoodOrderStatus;
  statusChangeTime: string;
};

export type OrderItem = {
  productID: Uuid;
  displayName: string;
  iconIdentifier: string;
  count: number;
  extra: string;
  subItems: OrderItem[];
};

export type Order = {
  id: Uuid;
  status: FoodOrderStatus;
  foodCourtName: string;
  waitingTime: number;
  orderItems: OrderItem[];
};

export type OrderHistory = Order & {
  history: OrderHistoryEntry[];
};

export type Notification = {
  id: Uuid;
  type: FoodOrderStatus;
  status: NotificationStatus;
  message: string;
  creationTime: string;
  pickupTime?: string;
};

export type Product = {
  id: Uuid;
  price: number;
  displayName: string;
  symbolIdentifier: string;
  minimalWarning: number;
  productCount: number;
  subProducts: Product[];
};

export type ImportedProductAssignment = {
  id: string;
  mainProductId: Uuid;
  subProductId: Uuid;
};

export type ImportedAccountData = {
  accountId: Uuid;
  loginNr: string;
  devPassword?: string;
  type: AccountType;
  ownFoodCourtId?: Uuid;
  cart?: Cart | null;
  credit?: Credit | null;
  creditHistory?: CreditHistoryResponse[];
  visibleOrders: Order[];
  visibleOrderHistory: OrderHistory[];
  notifications: Notification[];
};

export type InitialImportData = {
  version: 1;
  generatedAt: string;
  source: string;
  global: {
    foodCourts: FoodCourt[];
    products: Product[];
    productAssignments: ImportedProductAssignment[];
    productFoodCourtMap: Record<string, Uuid>;
    foodCourtImages: Record<string, string>;
  };
  accounts: ImportedAccountData[];
};

export type RawOrderLike = FoodOrderResponse | FoodOrderResponseHistory;
export type RawOrderItemLike = FoodOrderItemResponse;
export type RawHistoryEntryLike = FoodOrderHistoryResponse;
export type RawNotificationLike = FoodOrderNotificationResponse;
export type RawProductLike = ProductResponse;
export type RawCartLike = CartResponse;
export type RawCreditLike = CreditResponse;
export type RawFoodCourtLike = FoodCourtResponse;
export type RawLoginLike = AccountLoginResponse;
export type RawRegisterLike = AccountRegisterResponse;
