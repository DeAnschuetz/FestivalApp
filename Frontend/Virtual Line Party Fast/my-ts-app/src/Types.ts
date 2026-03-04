export type OrderStatus =
  | 'ORDERED'
  | 'IN_PROGRESS'
  | 'READY_FOR_PICKUP'
  | 'DONE'
  | 'CANCELED';

export interface OrderItem {
  productID: string;
  displayName: string;
  iconIdentifier: string;
  count: number;
  extra: string;
  subItems?: any[];
}

export interface Order {
  id: string;
  status: OrderStatus;
  foodCourtName: string;
  waitingTime: number;
  orderItems: OrderItem[];
}

export interface FoodCourt {
  id: string;
  name: string;
  waitingTime: number;
}